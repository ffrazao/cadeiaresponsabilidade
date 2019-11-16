package br.com.frazao.cadeiaresponsabilidade;

import java.util.Collection;

public class CadeiaSequencial extends Cadeia {

	public CadeiaSequencial() {
		super();
	}

	public CadeiaSequencial(final Collection<Comando> sequencia) {
		super(sequencia);
	}

	public CadeiaSequencial(final Comando... sequencia) {
		super(sequencia);
	}

	@Override
	protected final void procedimento(final Contexto<?, ?> contexto) throws Exception {
		if (this.log().isDebugEnabled()) {
			this.log().debug(String.format("(%s) cadeia sequenciada procedimento", this.getNome()));
		}
		for (final Comando comando : this.getSequencia()) {
			comando.executar(contexto);
		}
	}

}
