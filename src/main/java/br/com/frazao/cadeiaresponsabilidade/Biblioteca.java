package br.com.frazao.cadeiaresponsabilidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import br.com.frazao.cadeiaresponsabilidade.apoio.Util;
import br.com.frazao.cadeiaresponsabilidade.xml.BibliotecaDescritor;
import br.com.frazao.cadeiaresponsabilidade.xml.CadeiaDescritor;
import br.com.frazao.cadeiaresponsabilidade.xml.CatalogoDescritor;

public class Biblioteca {

	private static final String SEPARADOR_CATALOGO_COMANDO = "\\.";
	
	private Set<Catalogo> catalogos = new HashSet<>();
	
	public Biblioteca() {
	}

	private List<BibliotecaDescritor> lerDefinicao(final Class<?> classe) throws Exception {
		final List<BibliotecaDescritor> result = this.lerDefinicao(classe.getPackage());
		return result;
	}

	private List<BibliotecaDescritor> lerDefinicao(final Package pacote) throws Exception {
		final List<BibliotecaDescritor> result = new ArrayList<>();
		final String nomeRecurso = String.format("classpath:%s/**/%s",
				pacote.getName().replaceAll(Biblioteca.SEPARADOR_CATALOGO_COMANDO, "/"),
				BibliotecaDescritor.BIBLIOTECA_ARQUIVO_NOME_PADRAO);
		Arrays.asList(new PathMatchingResourcePatternResolver().getResources(nomeRecurso)).stream().forEach(r -> {
			try {
				result.add(this.lerDefinicao(r.getFile()));
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		});
		return Collections.unmodifiableList(result);
	}

	private BibliotecaDescritor lerDefinicao(final File arquivo) throws Exception {
		try (InputStream is = new FileInputStream(arquivo)) {
			final BibliotecaDescritor result = this.validarDefinicao(BibliotecaDescritor.carregar(is));
			return result;
		}
	}

	public void lerCarregarDefinicao(final Class<?> classe) throws Exception {
		final List<BibliotecaDescritor> result = this.lerDefinicao(classe);
		this.carregarDefinicao(result);
	}

	public void lerCarregarDefinicao(final Package pacote) throws Exception {
		List<BibliotecaDescritor> lista = this.lerDefinicao(pacote);
		this.carregarDefinicao(lista);
	}

	private BibliotecaDescritor validarDefinicao(BibliotecaDescritor xml) throws Exception {
		// preparar o catalogo base
		if (xml.getComandos() != null && xml.getComandos().size() > 0) {
			// encontrar comandos repetidos
			List<?> repetido = Util.itemsRepetidos((List<?>) xml.getComandos());
			if (repetido.size() > 0) {
				throw new IllegalArgumentException(String.format("Comando(s) Base repetido(s) [%s]", repetido));
			}
			// verificar se há comandos sem a classe definida
			if (xml.getComandos().stream()
					.filter(c -> (c instanceof CadeiaDescritor && !c.getClasse().isPresent()
							&& !((CadeiaDescritor) c).getTipo().isPresent())
							|| (!(c instanceof CadeiaDescritor) && !c.getClasse().isPresent()))
					.count() > 0) {
				throw new IllegalArgumentException("Comando(s) Base sem classe definida não são permitidos");
			}
		}
		// preparar os catálogos da biblioteca
		if (xml.getCatalogos() != null && xml.getCatalogos().size() > 0) {
			// encontrar comandos repetidos
			List<?> repetido = Util.itemsRepetidos((List<?>) xml.getCatalogos());
			if (repetido.size() > 0) {
				throw new IllegalArgumentException(String.format("Catálogo(s) repetido(s) [%s]", repetido));
			}
		}
		return xml;
	}

	private void carregarDefinicao(List<BibliotecaDescritor> xmls) {
		for (BibliotecaDescritor xml : xmls) {
			for (CatalogoDescritor cd: xml.getCatalogos()) {				
				Catalogo catalogo = new Catalogo(cd.getNome());
				System.out.println(catalogo);
			}
		}
	}

//	private List<BibliotecaDescritor> definicoes = new ArrayList<>();

	public Catalogo getCatalogo(String nome) {
		Set<Catalogo> pesquisa = null;
		pesquisa = this.getCatalogos().stream().filter(c -> c.getNome().equalsIgnoreCase(nome)).collect(Collectors.toSet());
		if (pesquisa.size() == 1) {
			return new ArrayList<>(pesquisa).get(0);
		} else {
			throw new IllegalArgumentException(String.format("Catálogo inválido [%s]", nome));
		}
	}

	public Comando getComando(final String nomeCatalogoComando) {
		String[] partes = nomeCatalogoComando.split(Biblioteca.SEPARADOR_CATALOGO_COMANDO);
		if (partes.length == 1) {
			return getComando(Catalogo.CATALOGO_BASE_NOME_PADRAO, partes[0]);
		} else if (partes.length == 2) {
			return getComando(partes[0], partes[1]);
		} else {
			throw new IllegalArgumentException(
					String.format("Nome inválido de catalogo/comando [%s]", nomeCatalogoComando));
		}
	}

	public Comando getComando(final String nomeCatalogo, final String nomeComando) {
		Comando result = null;
		Catalogo catalogo = getCatalogo(nomeCatalogo);
		result = catalogo.getComando(nomeComando);
		return result;
	}

	public void adicionar(final Catalogo catalogo) {
		if (catalogo == null) {
			throw new NullPointerException();
		}
		if (this.catalogos.contains(catalogo)) {
			throw new IllegalArgumentException(String.format("Catálogo [%s] repetido", catalogo));
		}
		this.catalogos.add(catalogo);
	}

	public Set<Catalogo> getCatalogos() {
		return Collections.unmodifiableSet(this.catalogos);
	}

	/*
	 * // FIXME
	 * 
	 * private void mergeComandos(final Comandos base, final Comandos modelo) {
	 * boolean mesclar = true; if (base instanceof CadeiaDescritor) { mesclar =
	 * CadeiaAcao.MESCLAR.equals(((CadeiaDescritor)
	 * base).getAcao().orElse(CadeiaAcao.MESCLAR)); }
	 * 
	 * // analisar necessidade de mesclar comandos if (mesclar) { for (final
	 * ComandoDescritor modeloCd : modelo.getComandos()) { boolean encontrou =
	 * false; for (final ComandoDescritor baseCd : base.getComandos()) { //
	 * verificar se a base já tem o comando if
	 * (baseCd.getNome().equalsIgnoreCase(modeloCd.getNome())) { // se for cadeia,
	 * fazer o merge dos comandos da cadeia if (baseCd instanceof CadeiaDescritor) {
	 * this.mergeComandos(((CadeiaDescritor) baseCd), ((CadeiaDescritor) modeloCd));
	 * } encontrou = true; break; } } if (!encontrou) { base.adicionar(modeloCd); }
	 * } } } public Catalogo getCatalogoBase() { return
	 * getCatalogo(CATALOGO_BASE).orElseGet(() -> { this.adicionarCatalogo(new
	 * Catalogo(CATALOGO_BASE)); return this.getCatalogo(CATALOGO_BASE).get(); }); }
	 * 
	 * private Catalogo montarCatalogo(final Catalogo base, final Optional<Catalogo>
	 * modeloOpt) { if (!modeloOpt.isPresent()) { return base; } final Catalogo
	 * modelo = modeloOpt.get();
	 * 
	 * if (!base.getAntes().isPresent() && modelo.getAntes().isPresent()) {
	 * base.setAntes(modelo.getAntes().get()); } if (!base.getDepois().isPresent()
	 * && modelo.getDepois().isPresent()) {
	 * base.setDepois(modelo.getDepois().get()); }
	 * 
	 * // merge dos comandos this.mergeComandos(base, modelo);
	 * 
	 * return this.montarCatalogo(base, modelo.getModelo()); }
	 */

	/*
	 * // FIXME public Catalogo getCatalogoBase() { return
	 * getCatalogo(CATALOGO_BASE).orElseGet(() -> { this.adicionarCatalogo(new
	 * Catalogo(CATALOGO_BASE)); return this.getCatalogo(CATALOGO_BASE).get(); }); }
	 * 
	 * @Override public Set<Catalogo> getCatalogos() { if (this.catalogos instanceof
	 * List) { if (this.catalogos.size() > 0) { throw new IllegalStateException(); }
	 * this.catalogos = new HashSet<>(); } return
	 * Collections.unmodifiableSet((Set<Catalogo>) this.catalogos); }
	 */

	// instanciar classe diretamente por meio de reflection
	Comando instanciar(final Class<? extends Comando> classe) throws Exception {
		return classe.newInstance();
	}

	/*
	 * // FIXME // instanciador de comando public Comando instanciar(final String
	 * nomeComando) throws Exception { return instanciar(CATALOGO_BASE,
	 * nomeComando); }
	 * 
	 * private Comando instanciar(ComandoDescritor cd) { String[] nome =
	 * cd.getNome().split("\\."); try { if (nome.length == 1) { return
	 * this.instanciar(nome[0]); } else if (nome.length == 2) { return
	 * this.instanciar(nome[0], nome[1]); } } catch (Exception e) { throw new
	 * RuntimeException(e); } return null; }
	 * 
	 * // em revisao
	 * 
	 * private Comando instanciar(final String nomeComando, final
	 * Collection<ComandoDescritor> comandos) throws Exception { final
	 * AtomicReference<Comando> result = new AtomicReference<>();
	 * 
	 * final ComandoDescritor comandoDescritor = this.getComando(nomeComando,
	 * comandos).orElseGet(() -> { throw new
	 * IllegalArgumentException(String.format("Nome de comando não encontrado %s",
	 * nomeComando)); });
	 * 
	 * // Se informada, instanciar o comando pela classe
	 * comandoDescritor.getClasse().ifPresent((c) -> { try {
	 * result.set(this.instanciar(c)); } catch (Exception e) { throw new
	 * RuntimeException(e); } });
	 * 
	 * // instanciar comando if (!(comandoDescritor instanceof CadeiaDescritor)) {
	 * if (result.get() == null) { // procurar o comando pelo nome de referência
	 * result.set(this.instanciar(comandoDescritor)); if (result.get() == null) {
	 * throw new IllegalArgumentException( String.format("Comando %s mal definido",
	 * comandoDescritor.getNome())); } } } else { // instanciar cadeia if
	 * (result.get() == null) { // instanciar pelo tipo de cadeia informado
	 * ((CadeiaDescritor) comandoDescritor).getTipo().ifPresent((t) -> { try {
	 * switch (t) { case SEQUENCIAL:
	 * result.set(CadeiaSequencial.class.newInstance()); break; case PARALELO:
	 * result.set(CadeiaParalela.class.newInstance()); break; } } catch
	 * (InstantiationException | IllegalAccessException e) { throw new
	 * RuntimeException(e); } }); }
	 * 
	 * if (result.get() == null) { // procurar a cadeia pelo nome de referência
	 * result.set(this.instanciar(comandoDescritor)); if (result.get() == null) {
	 * result.set(CadeiaSequencial.class.newInstance()); } }
	 * 
	 * // if (CadeiaAcao.SUBSTITUIR.equals(((CadeiaDescritor)
	 * comandoDescritor).getAcao().orElse(null))) { // ((Cadeia)
	 * result.get()).setComandos(new ArrayList<>()); // } else { //
	 * ((CadeiaDescritor) comandoDescritor).setComandos(((CadeiaDescritor)
	 * comandoBase.get()).getComandos()); // }
	 * 
	 * // instanciar os comandos vinculados for (final ComandoDescritor c :
	 * ((CadeiaDescritor) comandoDescritor).getComandos()) { final Comando comando =
	 * this.instanciar(c.getNome(), ((CadeiaDescritor)
	 * comandoDescritor).getComandos()); comando.ordem = c.getOrdem().orElse(null);
	 * ((Cadeia) result.get()).adicionarComando(comando); }
	 * 
	 * // ordenar comandos da cadeia // adicionar os números para ordenação int cont
	 * = 0; final List<Integer> e = ((Cadeia)
	 * result.get()).getComandos().stream().filter(c -> c.ordem != null) .map(c ->
	 * c.ordem).collect(Collectors.toList()); for (final Comando comando : ((Cadeia)
	 * result.get()).getComandos()) { if (comando.ordem == null) { do { } while
	 * (e.contains(Math.abs(--cont))); comando.ordem = cont; } }
	 * 
	 * // ordenar comandos // final List<Comando> listaOrdenada = ((Cadeia)
	 * result.get()).getComandos().stream() // .sorted(((a, b) -> new
	 * Integer(Math.abs(a.ordem)).compareTo(new Integer(Math.abs(b.ordem))))) //
	 * .collect(Collectors.toList());
	 * 
	 * // ((Cadeia) result.get()).setComandos(listaOrdenada); }
	 * 
	 * // atribuir o nome do comando result.get().setNome(nomeComando);
	 * 
	 * return result.get(); }
	 * 
	 * /* // FIXME
	 * 
	 * public Comando instanciar(final String nomeCatalogo, final String
	 * nomeComando) throws Exception { Catalogo catalogo = new
	 * Catalogo(nomeCatalogo);
	 * 
	 * catalogo = this.montarCatalogo(catalogo, this.getCatalogo(nomeCatalogo));
	 * 
	 * final Comando comando = this.instanciar(nomeComando, catalogo.getComandos());
	 * 
	 * Comando result = comando; if (catalogo.getAntes().isPresent() ||
	 * catalogo.getDepois().isPresent()) { final Cadeia cadeia = new
	 * CadeiaSequencial(); catalogo.getAntes().ifPresent(c -> { try {
	 * cadeia.adicionarComando(this.instanciar(c.getNome())); } catch (Exception e)
	 * { throw new RuntimeException(e); } }); cadeia.adicionarComando(comando);
	 * catalogo.getDepois().ifPresent(c -> { try {
	 * cadeia.adicionarComando(this.instanciar(c.getNome())); } catch (Exception e)
	 * { throw new RuntimeException(e); } }); result = cadeia; }
	 * 
	 * return result; }
	 * 
	 * private void mergeComandos(final Comandos base, final Comandos modelo) {
	 * boolean mesclar = true; if (base instanceof CadeiaDescritor) { mesclar =
	 * CadeiaAcao.MESCLAR.equals(((CadeiaDescritor)
	 * base).getAcao().orElse(CadeiaAcao.MESCLAR)); }
	 * 
	 * // analisar necessidade de mesclar comandos if (mesclar) { for (final
	 * ComandoDescritor modeloCd : modelo.getComandos()) { boolean encontrou =
	 * false; for (final ComandoDescritor baseCd : base.getComandos()) { //
	 * verificar se a base já tem o comando if
	 * (baseCd.getNome().equalsIgnoreCase(modeloCd.getNome())) { // se for cadeia,
	 * fazer o merge dos comandos da cadeia if (baseCd instanceof CadeiaDescritor) {
	 * this.mergeComandos(((CadeiaDescritor) baseCd), ((CadeiaDescritor) modeloCd));
	 * } encontrou = true; break; } } if (!encontrou) { base.adicionar(modeloCd); }
	 * } } }
	 */
	/*
	 * // FIXME
	 * 
	 * private Catalogo montarCatalogo(final Catalogo base, final Optional<Catalogo>
	 * modeloOpt) { if (!modeloOpt.isPresent()) { return base; } final Catalogo
	 * modelo = modeloOpt.get();
	 * 
	 * if (!base.getAntes().isPresent() && modelo.getAntes().isPresent()) {
	 * base.setAntes(modelo.getAntes().get()); } if (!base.getDepois().isPresent()
	 * && modelo.getDepois().isPresent()) {
	 * base.setDepois(modelo.getDepois().get()); }
	 * 
	 * // merge dos comandos this.mergeComandos(base, modelo);
	 * 
	 * return this.montarCatalogo(base, modelo.getModelo()); }
	 * 
	 * public void mesclar(Biblioteca biblioteca) {
	 * this.getCatalogos().stream().forEach(c -> this.adicionarCatalogo(c)); }
	 */
}

class InstaciaComando {
	private Comando comando;
	private InstanciaStatus status = InstanciaStatus.AGUARDANDO;
}

enum InstanciaStatus {
	AGUARDANDO, EXECUTANDO, DESTRUINDO;
}