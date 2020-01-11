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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import br.com.frazao.cadeiaresponsabilidade.apoio.Util;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Biblioteca implements Catalogos {

	public static final String BIBLIOTECA_ARQUIVO_NOME_PADRAO = "biblioteca.xml";

	public static final String CATALOGO_BASE = "";

	@XmlElement(name = "catalogo")
	private Collection<Catalogo> catalogos = new ArrayList<>();

	@XmlElements({ @XmlElement(name = "comando", type = ComandoDescritor.class),
			@XmlElement(name = "cadeia", type = CadeiaDescritor.class) })
	private Collection<ComandoDescritor> comandos = new ArrayList<>();

	public Biblioteca() {
	}

	@Override
	public void adicionarCatalogo(final Catalogo catalogo) {
		if (catalogo == null) {
			throw new NullPointerException();
		}
		if (this.catalogos instanceof List) {
			if (this.catalogos == null || this.catalogos.size() > 0) {
				throw new IllegalStateException("Estado inválido dos catálogos");
			}
			this.catalogos = new HashSet<>();
		}
		if (this.catalogos.contains(catalogo)) {
			throw new IllegalArgumentException(String.format("%s repetido", catalogo));
		}
		this.catalogos.add(catalogo);
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
		// carregar o arquivo .xml
		final Biblioteca xml = (Biblioteca) JAXBContext.newInstance(Biblioteca.class).createUnmarshaller()
				.unmarshal(arquivo);

		// preparar os comandos base
		if (xml.comandos != null && xml.comandos.size() > 0) {
			// encontrar comandos repetidos
			List<?> repetido = Util.itemsRepetidos((List<?>) xml.comandos);
			if (repetido.size() > 0) {
				throw new IllegalArgumentException(String.format("Comando(s) Base repetido(s) [%s]", repetido));
			}
			// verificar se há comandos sem a classe definida
			if (xml.comandos.stream()
					.filter(c -> (c instanceof CadeiaDescritor && !c.getClasse().isPresent()
							&& !((CadeiaDescritor) c).getTipo().isPresent())
							|| (!(c instanceof CadeiaDescritor) && !c.getClasse().isPresent()))
					.count() > 0) {
				throw new IllegalArgumentException("Comandos Base sem classe definida não são permitidos");
			}
			// adicionar comandos ao catálogo base
			this.getCatalogoBase().adicionar(xml.comandos);
		}
		// preparar os catálogos da biblioteca
		if (xml.catalogos != null && xml.catalogos.size() > 0) {
			// encontrar comandos repetidos
			List<?> repetido = Util.itemsRepetidos((List<?>) xml.catalogos);
			if (repetido.size() > 0) {
				throw new IllegalArgumentException(String.format("Catálogo(s) repetido(s) [%s]", repetido));
			}
			// verificar se o nome do catálogo base foi utilizado
			if (xml.catalogos.stream().filter(c -> c.getNome().trim().equalsIgnoreCase(CATALOGO_BASE)).count() > 0) {
				throw new IllegalArgumentException(
						"O nome do Catálogo Base não pode ser utilizado no(s) catálogo(s) da biblioteca");
			}
			// adicionar catálogo(s)
			this.adicionarCatalogo(xml.catalogos);
		}
	}

	public void carregar(final Package pacote) throws Exception {
		Resource[] rs;
		try {
			final String nomeRecurso = String.format("classpath:%s/**/%s", pacote.getName().replaceAll("\\.", "/"),
					Biblioteca.BIBLIOTECA_ARQUIVO_NOME_PADRAO);
			rs = new PathMatchingResourcePatternResolver().getResources(nomeRecurso);
			for (final Resource r : rs) {
				try (InputStream is = r.getInputStream()) {
					this.carregar(is);
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void carregar(final String nomeArquivo) throws Exception {
		this.carregar(new File(nomeArquivo));
	}

	public Catalogo getCatalogoBase() {
		return getCatalogo(CATALOGO_BASE).orElseGet(() -> {
			this.adicionarCatalogo(new Catalogo(CATALOGO_BASE));
			return this.getCatalogo(CATALOGO_BASE).get();
		});
	}

	@Override
	public Set<Catalogo> getCatalogos() {
		if (this.catalogos instanceof List) {
			if (this.catalogos.size() > 0) {
				throw new IllegalStateException();
			}
			this.catalogos = new HashSet<>();
		}
		return Collections.unmodifiableSet((Set<Catalogo>) this.catalogos);
	}

	private Optional<ComandoDescritor> getComando(final String nomeComando,
			final Collection<ComandoDescritor> comandos) {
		final Optional<ComandoDescritor> result = comandos.stream()
				.filter(c -> c.getNome().equalsIgnoreCase(nomeComando)).findFirst();
		return result;
	}

	// instanciar classe diretamente por meio de reflection
	Comando instanciar(final Class<? extends Comando> classe) throws Exception {
		return classe.newInstance();
	}

	// instanciador de comando
	public Comando instanciar(final String nomeComando) throws Exception {
		return instanciar(CATALOGO_BASE, nomeComando);
	}

	private Comando instanciar(ComandoDescritor cd) {
		String[] nome = cd.getNome().split("\\.");
		try {
			if (nome.length == 1) {
				return this.instanciar(nome[0]);
			} else if (nome.length == 2) {
				return this.instanciar(nome[0], nome[1]);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	// em revisao

	private Comando instanciar(final String nomeComando, final Collection<ComandoDescritor> comandos) throws Exception {
		final AtomicReference<Comando> result = new AtomicReference<>();

		final ComandoDescritor comandoDescritor = this.getComando(nomeComando, comandos).orElseGet(() -> {
			throw new IllegalArgumentException(String.format("Nome de comando não encontrado %s", nomeComando));
		});

		// Se informada, instanciar o comando pela classe
		comandoDescritor.getClasse().ifPresent((c) -> {
			try {
				result.set(this.instanciar(c));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

		// instanciar comando
		if (!(comandoDescritor instanceof CadeiaDescritor)) {
			if (result. get() == null) {
				// procurar o comando pelo nome de referência
				result.set(this.instanciar(comandoDescritor));
				if (result.get() == null) {
					throw new IllegalArgumentException(
							String.format("Comando %s mal definido", comandoDescritor.getNome()));
				}
			}
		} else {
			// instanciar cadeia
			if (result.get() == null) {
				// instanciar pelo tipo de cadeia informado
				((CadeiaDescritor) comandoDescritor).getTipo().ifPresent((t) -> {
					try {
						switch (t) {
						case SEQUENCIAL:
							result.set(CadeiaSequencial.class.newInstance());
							break;
						case PARALELO:
							result.set(CadeiaParalela.class.newInstance());
							break;
						}
					} catch (InstantiationException | IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				});
			}

			if (result.get() == null) {
				// procurar a cadeia pelo nome de referência
				result.set(this.instanciar(comandoDescritor));
				if (result.get() == null) {
					result.set(CadeiaSequencial.class.newInstance());
				}
			}

//				if (CadeiaAcao.SUBSTITUIR.equals(((CadeiaDescritor) comandoDescritor).getAcao().orElse(null))) {
//					((Cadeia) result.get()).setComandos(new ArrayList<>());
//				} else {
//					((CadeiaDescritor) comandoDescritor).setComandos(((CadeiaDescritor) comandoBase.get()).getComandos());
//				}

			// instanciar os comandos vinculados
			for (final ComandoDescritor c : ((CadeiaDescritor) comandoDescritor).getComandos()) {
				final Comando comando = this.instanciar(c.getNome(),
						((CadeiaDescritor) comandoDescritor).getComandos());
				comando.ordem = c.getOrdem().orElse(null);
				((Cadeia) result.get()).adicionarComando(comando);
			}

			// ordenar comandos da cadeia
			// adicionar os números para ordenação
			int cont = 0;
			final List<Integer> e = ((Cadeia) result.get()).getComandos().stream().filter(c -> c.ordem != null)
					.map(c -> c.ordem).collect(Collectors.toList());
			for (final Comando comando : ((Cadeia) result.get()).getComandos()) {
				if (comando.ordem == null) {
					do {
					} while (e.contains(Math.abs(--cont)));
					comando.ordem = cont;
				}
			}

			// ordenar comandos
//			final List<Comando> listaOrdenada = ((Cadeia) result.get()).getComandos().stream()
//					.sorted(((a, b) -> new Integer(Math.abs(a.ordem)).compareTo(new Integer(Math.abs(b.ordem)))))
//					.collect(Collectors.toList());

			// ((Cadeia) result.get()).setComandos(listaOrdenada);
		}

		// atribuir o nome do comando
		result.get().setNome(nomeComando);

		return result.get();
	}

	public Comando instanciar(final String nomeCatalogo, final String nomeComando) throws Exception {
		Catalogo catalogo = new Catalogo(nomeCatalogo);

		catalogo = this.montarCatalogo(catalogo, this.getCatalogo(nomeCatalogo));

		final Comando comando = this.instanciar(nomeComando, catalogo.getComandos());

		Comando result = comando;
		if (catalogo.getAntes().isPresent() || catalogo.getDepois().isPresent()) {
			final Cadeia cadeia = new CadeiaSequencial();
			catalogo.getAntes().ifPresent(c -> {
				try {
					cadeia.adicionarComando(this.instanciar(c.getNome()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			cadeia.adicionarComando(comando);
			catalogo.getDepois().ifPresent(c -> {
				try {
					cadeia.adicionarComando(this.instanciar(c.getNome()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
			result = cadeia;
		}

		return result;
	}

	private void mergeComandos(final Comandos base, final Comandos modelo) {
		boolean mesclar = true;
		if (base instanceof CadeiaDescritor) {
			mesclar = CadeiaAcao.MESCLAR.equals(((CadeiaDescritor) base).getAcao().orElse(CadeiaAcao.MESCLAR));
		}

		// analisar necessidade de mesclar comandos
		if (mesclar) {
			for (final ComandoDescritor modeloCd : modelo.getComandos()) {
				boolean encontrou = false;
				for (final ComandoDescritor baseCd : base.getComandos()) {
					// verificar se a base já tem o comando
					if (baseCd.getNome().equalsIgnoreCase(modeloCd.getNome())) {
						// se for cadeia, fazer o merge dos comandos da cadeia
						if (baseCd instanceof CadeiaDescritor) {
							this.mergeComandos(((CadeiaDescritor) baseCd), ((CadeiaDescritor) modeloCd));
						}
						encontrou = true;
						break;
					}
				}
				if (!encontrou) {
					base.adicionar(modeloCd);
				}
			}
		}
	}

	private Catalogo montarCatalogo(final Catalogo base, final Optional<Catalogo> modeloOpt) {
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
