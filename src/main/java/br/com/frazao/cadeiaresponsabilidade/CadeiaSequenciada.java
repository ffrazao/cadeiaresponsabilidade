package br.com.frazao.cadeiaresponsabilidade;

import java.util.Arrays;
import java.util.Collection;

public class CadeiaSequenciada extends Cadeia {

	public CadeiaSequenciada() {
		super();
	}

	public CadeiaSequenciada(Collection<Comando> comandoList) {
		super(comandoList);
	}

	public CadeiaSequenciada(Comando... comandoList) {
		this(Arrays.asList(comandoList));
	}

	@Override
	protected void procedimento(Contexto<?, ?> contexto) throws Exception {
		if (log().isDebugEnabled()) {
			log().debug(String.format("(%s) cadeia sequenciada procedimento", getNome()));
		}
		for (Comando comando : getComandoList()) {
			comando.executar(contexto);
		}
	}

}
