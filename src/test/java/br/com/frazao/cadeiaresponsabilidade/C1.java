package br.com.frazao.cadeiaresponsabilidade;

public class C1 extends Comando {

	int cont = 0;

	@Override
	protected void procedimento(final Contexto<?, ?> contexto) throws Exception {
	}

	@Override
	protected boolean vaiRepetir(final Contexto<?, ?> contexto) {
		if (this.log().isDebugEnabled()) {
			this.log().debug("repetindo...");
		}
		return this.cont++ < 2;
	}

}
