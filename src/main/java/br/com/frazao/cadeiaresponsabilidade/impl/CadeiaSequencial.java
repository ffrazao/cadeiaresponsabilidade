package br.com.frazao.cadeiaresponsabilidade.impl;

import java.util.List;
import java.util.logging.Level;

import br.com.frazao.cadeiaresponsabilidade.Cadeia;
import br.com.frazao.cadeiaresponsabilidade.Comando;
import br.com.frazao.cadeiaresponsabilidade.Contexto;

public class CadeiaSequencial extends Cadeia {

	public CadeiaSequencial(final String nome, final Comando... comandos) {
		super(nome, comandos);
	}

	public CadeiaSequencial(final String nome, final List<Comando> comandos) {
		super(nome, comandos);
	}

	@Override
	protected final <k, v> void procedimento(final Contexto contexto) throws Exception {
		if (this.log().isLoggable(Level.CONFIG)) {
			this.log().config(String.format("(%s) cadeia sequenciada procedimento", this));
		}
		for (final Comando comando : this.getComandos()) {
			comando.executar(contexto);
		}
		if (this.log().isLoggable(Level.FINE)) {
			this.log().fine(String.format("(%s) FIM cadeia sequenciada procedimento", this));
		}
	}

}
