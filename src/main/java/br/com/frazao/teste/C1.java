package br.com.frazao.teste;

import br.com.frazao.cadeiaresponsabilidade.Comando;
import br.com.frazao.cadeiaresponsabilidade.Contexto;

public class C1 extends Comando {

	int cont = 0;

	@Override
	protected void procedimento(Contexto<?, ?> contexto) throws Exception {
	}

	@Override
	protected boolean vaiRepetir(Contexto<?, ?> contexto) {
		if (log().isDebugEnabled()) {
			log().debug("repetindo...");
		}
		return cont++ < 2;
	}

}
