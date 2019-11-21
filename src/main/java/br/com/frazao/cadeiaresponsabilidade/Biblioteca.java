package br.com.frazao.cadeiaresponsabilidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

	public void carregar(final Class<?> classe) throws Exception {
		this.carregar(classe.getPackage());
	}

	public void carregar(final File nomeArquivo) throws Exception {
		try (InputStream is = new FileInputStream(nomeArquivo)) {
			this.carregar(is);
		}
	}

	public void carregar(final InputStream arquivo) throws Exception {
		final JAXBContext context = JAXBContext.newInstance(Biblioteca.class);
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final Biblioteca biblioteca = (Biblioteca) unmarshaller.unmarshal(arquivo);
		this.adicionarComando(biblioteca.getComandos());
		this.adicionarCatalogo(biblioteca.getCatalogos());
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

	// instanciador de comando
	protected Comando instanciar(final String nomeComando, final Collection<DescritorComando> comandos)
			throws Exception {
		final DescritorComando descritorComando = this.getComando(nomeComando, comandos).get();

		Comando result = null;
		if (descritorComando instanceof DescritorCadeia) {
			switch (((DescritorCadeia) descritorComando).getTipo().get()) {
			case SEQUENCIAL:
				result = CadeiaSequencial.class.newInstance();
				break;
			case PARALELO:
				result = CadeiaParalela.class.newInstance();
				break;
			}
			for (final DescritorComando c : ((DescritorCadeia) descritorComando).getComandos()) {
				((Cadeia) result).adicionarComando(
						this.instanciar(c.getNome(), ((DescritorCadeia) descritorComando).getComandos()));
			}
		} else {
			if (!descritorComando.getClasse().isPresent()) {
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
		if (catalogo.getAntes().isPresent() || catalogo.getDepois().isPresent()) {
			result = new CadeiaSequencial();
			if (catalogo.getAntes().isPresent()) {
				((Cadeia) result).adicionarComando(this.instanciar(catalogo.getAntes().get().getNome()));
			}

			((Cadeia) result).adicionarComando(comando);

			if (catalogo.getDepois().isPresent()) {
				((Cadeia) result).adicionarComando(this.instanciar(catalogo.getDepois().get().getNome()));
			}
		}
		return result;
	}

	protected void mergeComandos(final Comandos base, final Comandos modelo) {
		// remover ordem comandos da cadeia não ordenados
		if (base instanceof DescritorCadeia) {
			for (final DescritorComando baseDc : base.getComandos()) {
				final Integer ordem = baseDc.getOrdem().orElse(null);
				baseDc.setOrdem((ordem == null) || (ordem < 0) ? null : ordem);
			}
		}

		for (final DescritorComando modeloDc : modelo.getComandos()) {
			boolean encontrou = false;
			for (final DescritorComando baseDc : base.getComandos()) {
				// verificar se a base já tem o comando
				if (baseDc.getNome().equals(modeloDc.getNome())) {
					encontrou = true;
					// se for cadeia, fazer o merge dos comandos da cadeia
					if (baseDc instanceof DescritorCadeia) {
						if (!((DescritorCadeia) baseDc).getTipo().isPresent()
								&& ((DescritorCadeia) modeloDc).getTipo().isPresent()) {
							((DescritorCadeia) baseDc).setTipo(((DescritorCadeia) modeloDc).getTipo().get());
						}
						this.mergeComandos(((DescritorCadeia) baseDc), ((DescritorCadeia) modeloDc));
					}
					break;
				}
			}
			if (!encontrou) {
				base.adicionarComando(modeloDc);
			}
		}

		// ordenar comandos da cadeia
		if (base instanceof DescritorCadeia) {
			int cont = 0;
			final List<Integer> e = base.getComandos().stream().filter(c -> c.getOrdem().isPresent())
					.map(c -> c.getOrdem().get()).collect(Collectors.toList());
			for (final DescritorComando baseDc : base.getComandos()) {
				if (!baseDc.getOrdem().isPresent()) {
					do {
					} while (e.contains(Math.abs(--cont)));
					baseDc.setOrdem(cont);
				}
			}

			((DescritorCadeia) base)
					.setComandos(((DescritorCadeia) base).getComandos().stream()
							.sorted(((a, b) -> new Integer(Math.abs(a.getOrdem().get()))
									.compareTo(new Integer(Math.abs(b.getOrdem().get())))))
							.collect(Collectors.toList()));
		}
	}

	protected Catalogo montarCatalogo(final Catalogo base, final Optional<Catalogo> modeloOpt) {
		if (!modeloOpt.isPresent()) {
			return base;
		}
		final Catalogo modelo = modeloOpt.get();

		if (!base.getAntes().isPresent() && modelo.getAntes().isPresent()) {
			base.setAntes(modelo.getAntes().get());
		}
		if (!base.getDepois().isPresent() && modelo.getDepois().isPresent()) {
			base.setDepois(modelo.getDepois().get());
		}

		// merge dos comandos
		this.mergeComandos(base, modelo);

		return this.montarCatalogo(base, modelo.getModelo());
	}

}
