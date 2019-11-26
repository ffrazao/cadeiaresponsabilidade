package br.com.frazao.cadeiaresponsabilidade;

import java.util.List;
import java.util.logging.Level;

public class CadeiaSequencial extends Cadeia {

	CadeiaSequencial() {
		super();
	}

	public CadeiaSequencial(final Comando... comandos) {
		super(comandos);
	}

	public CadeiaSequencial(final List<Comando> comandos) {
		super(comandos);
	}

	@Override
	protected final <k, v> void procedimento(final Contexto contexto) throws Exception {
		if (this.log().isLoggable(Level.CONFIG)) {
			this.log().config(String.format("(%s) cadeia sequenciada procedimento", this.getNome()));
		}
		for (final Comando comando : this.getComandos()) {
			comando.executar(contexto);
		}
	}

}
