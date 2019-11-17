package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class Cadeia extends Comando {

	private final List<Comando> comandos = new ArrayList<>();

	private boolean congelado = false;

	public Cadeia() {
	}

	public Cadeia(final Comando... comandos) {
		this(Arrays.asList(comandos));
	}

	public Cadeia(final List<Comando> comandos) {
		this();
		this.adicionarComando(comandos);
	}

	public final void adicionarComando(final Comando... comandos) {
		this.adicionarComando(Arrays.asList(comandos));
	}

	public final void adicionarComando(final Comando comando) {
		if (this.log().isDebugEnabled()) {
			this.log().debug(String.format("(%) adicionando comando", this.getNome()));
		}
		if (comando == null) {
			throw new IllegalArgumentException("O comando não pode ser nulo!");
		}
		if (this.congelado) {
			throw new IllegalStateException("Neste momento não é possível adicionar nenhum comando à cadeia");
		}
		this.getComandos().add(comando);
	}

	public final void adicionarComando(final List<Comando> comandos) {
		comandos.forEach(comando -> this.adicionarComando(comando));
	}

	@Override
	public final <k, v> void executar(final Contexto<k, v> contexto) throws Exception {
		if ((this.getComandos() == null) || this.getComandos().isEmpty()) {
			throw new IllegalStateException("Cadeia sem comando(s)");
		}
		try {
			// Congelar a configuração da lista de comandos
			this.congelado = true;
			super.executar(contexto);
		} finally {
			this.congelado = false;
		}
	}

	protected final List<Comando> getComandos() {
		return this.comandos;
	}

}