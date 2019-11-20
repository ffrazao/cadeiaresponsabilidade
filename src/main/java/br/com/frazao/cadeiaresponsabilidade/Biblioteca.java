package br.com.frazao.cadeiaresponsabilidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Biblioteca implements Comandos, Catalogos {

	public static final String BIBLIOTECA_NOME = "biblioteca.xml";

	@XmlElement(name = "catalogo")
	private final Set<Catalogo> catalogos = new HashSet<>();

	@XmlElements({ @XmlElement(name = "comando", type = DescritorComando.class),
			@XmlElement(name = "cadeia", type = DescritorCadeia.class) })
	private final Set<DescritorComando> comandos = new HashSet<>();

	public Biblioteca() {
	}

	@Override
	public void adicionarCatalogo(final Catalogo catalogo) {
		this.catalogos.add(catalogo);
	}

	@Override
	public void adicionarComando(final DescritorComando comando) {
		this.comandos.add(comando);
	}

	public void carregar(final File nomeArquivo) throws Exception {
		try (InputStream is = new FileInputStream(nomeArquivo)) {
			this.carregar(is);
		}
	}

	public void carregar(final InputStream arquivo) throws Exception {
		final JAXBContext context = JAXBContext.newInstance(this.getClass());
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final Biblioteca biblioteca = (Biblioteca) unmarshaller.unmarshal(arquivo);
		this.adicionarComando(biblioteca.getComandos());
		this.adicionarCatalogo(biblioteca.getCatalogos());
	}

	public void carregar(final Class<?> classe) throws Exception {
		this.carregar(classe.getPackage());
	}
	
	public void carregar(final Package pacote) throws Exception {
		Resource[] rs;
		try {
			final String nomeRecurso = String.format("classpath:%s/**/%s", pacote.getName().replaceAll("\\.", "/"),
					Biblioteca.BIBLIOTECA_NOME);
			rs = new PathMatchingResourcePatternResolver().getResources(nomeRecurso);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		for (final Resource r : rs) {
			try (InputStream is = r.getInputStream()) {
				this.carregar(is);
			}
		}
	}

	public void carregar(final String nomeArquivo) throws Exception {
		this.carregar(new File(nomeArquivo));
	}

	@Override
	public Set<Catalogo> getCatalogos() {
		return Collections.unmodifiableSet(this.catalogos);
	}

	@Override
	public Set<DescritorComando> getComandos() {
		return Collections.unmodifiableSet(this.comandos);
	}

	// instanciar classe diretamente por meio de reflection
	protected Comando instanciar(final Optional<Class<? extends Comando>> classe) throws Exception {
		return classe.get().newInstance();
	}

	public Comando instanciar(final String nomeComando) throws Exception {
		return this.instanciar(nomeComando, this.getComandos());
	}

	protected Comando instanciar(final String nomeComando, final Collection<DescritorComando> comandos)
			throws Exception {
		final DescritorComando descritorComando = this.getComando(nomeComando, comandos).get();

		Comando result = null;
		if (descritorComando instanceof DescritorCadeia) {
			switch (((DescritorCadeia) descritorComando).getCadeiaTipo()) {
			case SEQUENCIAL:
				result = CadeiaSequencial.class.newInstance();
				break;
			case PARALELA:
				result = CadeiaParalela.class.newInstance();
				break;
			}
			for (final DescritorComando c : ((DescritorCadeia) descritorComando).getComandos()) {
				((Cadeia) result).adicionarComando(
						this.instanciar(c.getNome(), ((DescritorCadeia) descritorComando).getComandos()));
			}
		} else {
			if (descritorComando.getClass() == null) {
				descritorComando.setClasse(this.getComando(descritorComando.getNome()).get().getClasse().get());
			}
			result = this.instanciar(descritorComando.getClasse());
		}

		return result;
	}

	public Comando instanciar(final String nomeCatalogo, final String nomeComando) throws Exception {
		Catalogo catalogo = new Catalogo(nomeCatalogo);

		catalogo = this.montarCatalogo(catalogo, this.getCatalogo(nomeCatalogo));

		final Comando comando = this.instanciar(nomeComando, catalogo.getComandos());

		Comando result = comando;
		if ((catalogo.getAntes() != null) || (catalogo.getDepois() != null)) {
			result = new CadeiaSequencial();
			if (catalogo.getAntes() != null) {
				((Cadeia) result).adicionarComando(this.instanciar(catalogo.getAntes().get().getNome()));
			}

			((Cadeia) result).adicionarComando(comando);

			if (catalogo.getDepois() != null) {
				((Cadeia) result).adicionarComando(this.instanciar(catalogo.getDepois().get().getNome()));
			}
		}
		return result;
	}

	protected void mergeComandos(final Comandos base, final Comandos modelo) {

		for (final DescritorComando modeloDc : modelo.getComandos()) {
			boolean encontrou = false;
			for (final DescritorComando baseDc : base.getComandos()) {
				// se o comando nao tiver a classe definida
				if ((baseDc.getClasse() == null) && !(baseDc instanceof DescritorCadeia)) {
					// buscar a classe nos comandos genéricos
					baseDc.setClasse(this.getComando(baseDc.getNome()).get().getClasse().get());
				}
				// verificar se a base já tem o comando
				if (baseDc.getNome().equals(modeloDc.getNome())) {
					encontrou = true;
					// se for cadeia, fazer o merge dos comandos da cadeia
					if (baseDc instanceof DescritorCadeia) {
						this.mergeComandos(((DescritorCadeia) baseDc), ((DescritorCadeia) modeloDc));
					}
					break;
				}
			}
			if (!encontrou) {
				base.adicionarComando(modeloDc);
			}
		}
	}

	protected Catalogo montarCatalogo(final Catalogo base, final Optional<Catalogo> modelo) {
		if (!modelo.isPresent()) {
			return base;
		}

		if (!base.getAntes().isPresent() && modelo.get().getAntes().isPresent()) {
			base.setAntes(modelo.get().getAntes().get());
		}
		if (!base.getDepois().isPresent() && modelo.get().getDepois().isPresent()) {
			base.setDepois(modelo.get().getDepois().get());
		}

		// merge dos comandos
		this.mergeComandos(base, modelo.get());

		return this.montarCatalogo(base, modelo.get().getModelo());
	}

}
