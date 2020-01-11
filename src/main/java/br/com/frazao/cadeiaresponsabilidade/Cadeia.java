package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

abstract class Cadeia extends Comando {

	private final List<Comando> comandos = new ArrayList<>();

	private boolean congelado = false;

	Cadeia() {
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
		this.adicionarComando(this.comandos.size(), comando);
	}

	public final void adicionarComando(final Integer posicao, final Comando comando) {
		if (this.log().isLoggable(Level.CONFIG)) {
			this.log().config(String.format("(%s) adicionando comando (%s)", this, comando));
		}
		if (comando == null) {
			throw new IllegalArgumentException("O comando não pode ser nulo!");
		}
		if (this.congelado) {
			throw new IllegalStateException(
					String.format("Neste momento não é possível adicionar nenhum comando à cadeia (%s)", this));
		}
		this.comandos.add(posicao, comando);
	}

	public final void adicionarComando(final List<Comando> comandos) {
		comandos.forEach(comando -> this.adicionarComando(comando));
	}

	@Override
	public final <k, v> void executar(final Contexto contexto) throws Exception {
		if ((this.comandos == null) || this.comandos.isEmpty()) {
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
		return Collections.unmodifiableList(this.comandos);
	}

	@Override
	public String toString() {
		return "Cadeia [" + this.getNome() + "]";
	}

}