package br.com.frazao.cadeiaresponsabilidade;

import java.util.List;

public class CadeiaSequencial extends Cadeia {

	public CadeiaSequencial() {
		super();
	}

	public CadeiaSequencial(final List<Comando> comandos) {
		super(comandos);
	}

	public CadeiaSequencial(final Comando... comandos) {
		super(comandos);
	}

	@Override
	protected final void procedimento(final Contexto<?, ?> contexto) throws Exception {
		if (this.log().isDebugEnabled()) {
			this.log().debug(String.format("(%s) cadeia sequenciada procedimento", this.getNome()));
		}
		for (final Comando comando : this.getComandos()) {
			comando.executar(contexto);
		}
	}

}
