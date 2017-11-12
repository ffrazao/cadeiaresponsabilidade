package br.com.frazao.cadeiaresponsabilidade;

import java.util.Collection;

public class CadeiaSequenciada extends Cadeia {

	public CadeiaSequenciada() {
		super();
	}

	public CadeiaSequenciada(Collection<Comando> comandoList) {
		super(comandoList);
	}

	public CadeiaSequenciada(Comando... comandoList) {
		super(comandoList);
	}

	@Override
	protected void procedimento(Contexto<?, ?> contexto) throws Exception {
		logDebug("Cadeia Sequenciada");
		for (Comando comando : getComandoList()) {
			comando.executar(contexto);
		} 
	}

}
