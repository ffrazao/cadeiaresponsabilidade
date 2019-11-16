package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

abstract class Cadeia extends Comando {

	private boolean congelado = false;

	private final List<Comando> sequencia = new ArrayList<>();

	public Cadeia() {
	}

	public Cadeia(final Collection<Comando> sequencia) {
		this();
		if ((sequencia == null) || sequencia.isEmpty()) {
			throw new IllegalArgumentException("Sequência de comando(s) não informado(s)");
		}
		sequencia.forEach(comando -> this.adicionarComando(comando));
	}

	public Cadeia(final Comando... sequencia) {
		this(Arrays.asList(sequencia));
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
		this.getSequencia().add(comando);
	}

	@Override
	public final void executar(final Contexto<?, ?> contexto) throws Exception {
		if ((this.getSequencia() == null) || this.getSequencia().isEmpty()) {
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

	protected final List<Comando> getSequencia() {
		return this.sequencia;
	}

}