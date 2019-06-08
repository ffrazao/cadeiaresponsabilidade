package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

abstract class Cadeia extends Comando {

	protected List<Comando> comandoList = new ArrayList<>();

	protected boolean congelado = false;

	public Cadeia() {
	}

	public Cadeia(Collection<Comando> comandoList) {
		this();
		if (comandoList == null || comandoList.isEmpty()) {
			throw new IllegalArgumentException("Comandos não informados");
		}
		this.comandoList.addAll(comandoList);
	}

	public Cadeia(Comando... comandoList) {
		this(Arrays.asList(comandoList));
	}

	public final void adicionarComando(Comando comando) {
		if (log().isDebugEnabled()) {
			log().debug(String.format("(%) adicionando comando", getNome()));
		}
		if (comando == null) {
			throw new IllegalArgumentException("O comando não pode ser nulo!");
		}
		if (congelado) {
			throw new IllegalStateException("Neste momento não é possível adicionar nenhum comando à cadeia");
		}
		comandoList.add(comando);
	}

	@Override
	public final void executar(Contexto<?, ?> contexto) throws Exception {
		if (getComandoList() == null || getComandoList().isEmpty()) {
			throw new IllegalStateException("Cadeia sem comando(s)");
		}
		try {
			// Congelar a configura��o da lista de comandos
			this.congelado = true;
			super.executar(contexto);
		} finally {
			this.congelado = false;
		}
	}

	protected final List<Comando> getComandoList() {
		return comandoList;
	}

}