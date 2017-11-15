package br.com.frazao.cadeiaresponsabilidade;

import java.util.Arrays;
import java.util.Collection;

public class CadeiaSequenciada extends Cadeia {

	public CadeiaSequenciada() {
		super();
		if (log().isTraceEnabled()) {
			log().trace("Nova Cadeia Sequenciada");
		}
	}

	public CadeiaSequenciada(Collection<Comando> comandoList) {
		super(comandoList);
		if (log().isTraceEnabled()) {
			log().trace("Nova Cadeia Sequenciada Collection<Comando> comandoList");
		}
	}

	public CadeiaSequenciada(Comando... comandoList) {
		this(Arrays.asList(comandoList));
		if (log().isTraceEnabled()) {
			log().trace("Nova Cadeia Sequenciada Comando... comandoList");
		}
	}

	@Override
	protected void procedimento(Contexto<?, ?> contexto) throws Exception {
		if (log().isDebugEnabled()) {
			log().debug("Cadeia Sequenciada procedimento");
		}
		for (Comando comando : getComandoList()) {
			comando.executar(contexto);
		}
	}

}
