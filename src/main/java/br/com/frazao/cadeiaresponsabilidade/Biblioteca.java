package br.com.frazao.cadeiaresponsabilidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
		// verificar se há comandos sem a classe definida
		if (biblioteca.getComandos().stream().filter(c -> !c.getClasse().isPresent()).count() > 0) {
			throw new IllegalArgumentException("Comandos genéricos sem classe definida não são permitidos");
		}
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
	Comando instanciar(final Class<? extends Comando> classe) throws Exception {
		return classe.newInstance();
	}

	public Comando instanciar(final String nomeComando) throws Exception {
		return this.instanciar(nomeComando, this.getComandos());
	}

	// instanciador de comando
	Comando instanciar(final String nomeComando, final Collection<DescritorComando> comandos)
			throws Exception {
		final DescritorComando descritorComando = this.getComando(nomeComando, comandos).get();

		Comando result = null;
		if (descritorComando instanceof DescritorCadeia) {
			if (descritorComando.getClasse().isPresent()) {
				// instanciar diretamente pela classe informada
				result = this.instanciar(descritorComando.getClasse().get());
			} else if (((DescritorCadeia) descritorComando).getTipo().isPresent()) {
				// instanciar pela tipo de cadeia informado
				switch (((DescritorCadeia) descritorComando).getTipo().get()) {
				case SEQUENCIAL:
					result = CadeiaSequencial.class.newInstance();
					break;
				case PARALELO:
					result = CadeiaParalela.class.newInstance();
					break;
				}
			} else {
				// instanciar pelos comandos genéricos da biblioteca
				result = this.instanciar(this.getComando(descritorComando.getNome()).get().getClasse().get());
				if (CadeiaAcao.SUBSTITUIR.equals(((DescritorCadeia) descritorComando).getAcao().orElse(null))) {
					((Cadeia) result).setComandos(new ArrayList<>());
				}
			}

			// instanciar os comandos vinculados
			for (final DescritorComando c : ((DescritorCadeia) descritorComando).getComandos()) {
				final Comando comando = this.instanciar(c.getNome(),
						((DescritorCadeia) descritorComando).getComandos());
				comando.ordem = c.getOrdem().orElse(null);
				((Cadeia) result).adicionarComando(comando);
			}

			// ordenar comandos da cadeia
			// adicionar os números para ordenação
			int cont = 0;
			final List<Integer> e = ((Cadeia) result).getComandos().stream().filter(c -> c.ordem != null)
					.map(c -> c.ordem).collect(Collectors.toList());
			for (final Comando comando : ((Cadeia) result).getComandos()) {
				if (comando.ordem == null) {
					do {
					} while (e.contains(Math.abs(--cont)));
					comando.ordem = cont;
				}
			}

			// ordenar comandos
			final List<Comando> listaOrdenada = ((Cadeia) result).getComandos().stream()
					.sorted(((a, b) -> new Integer(Math.abs(a.ordem)).compareTo(new Integer(Math.abs(b.ordem)))))
					.collect(Collectors.toList());

			((Cadeia) result).setComandos(listaOrdenada);
		} else {
			// instanciar comandos
			if (!descritorComando.getClasse().isPresent()) {
				descritorComando.setClasse(this.getComando(descritorComando.getNome()).get().getClasse().get());
			}
			result = this.instanciar(descritorComando.getClasse().get());
		}

		// atribuir o nome da cadeia
		result.setNome(nomeComando);

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

	void mergeComandos(final Comandos base, final Comandos modelo) {
		boolean mesclar = true;
		if (base instanceof DescritorCadeia) {
			mesclar = CadeiaAcao.MESCLAR.equals(((DescritorCadeia) base).getAcao().orElse(CadeiaAcao.MESCLAR));
		}
		
		// analisar necessidade de mesclar comandos
		if (mesclar) {
			for (final DescritorComando modeloDc : modelo.getComandos()) {
				boolean encontrou = false;
				for (final DescritorComando baseDc : base.getComandos()) {
					// verificar se a base já tem o comando
					if (baseDc.getNome().equals(modeloDc.getNome())) {
						// se for cadeia, fazer o merge dos comandos da cadeia
						if (baseDc instanceof DescritorCadeia) {
							this.mergeComandos(((DescritorCadeia) baseDc), ((DescritorCadeia) modeloDc));
						}
						encontrou = true;
						break;
					}
				}
				if (!encontrou) {
					base.adicionarComando(modeloDc);
				}
			}		
		}
	}

	Catalogo montarCatalogo(final Catalogo base, final Optional<Catalogo> modeloOpt) {
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
