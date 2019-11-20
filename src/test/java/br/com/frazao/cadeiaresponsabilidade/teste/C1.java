package br.com.frazao.cadeiaresponsabilidade.teste;

import java.util.logging.Level;

import br.com.frazao.cadeiaresponsabilidade.Comando;
import br.com.frazao.cadeiaresponsabilidade.Contexto;

public class C1 extends Comando {

	private Integer cont = 0;
	private final Integer total = 2;

	@SuppressWarnings("unchecked")
	@Override
	protected <k, v> void procedimento(final Contexto<k, v> contexto) throws Exception {
		final Integer cont = (Integer) contexto.get("cont");
		if (cont == null) {
			contexto.put((k) "cont", (v) this.cont);
		}
		System.out.printf("Executando C1 %d/%d\n", cont + 1, this.total);
	}

	@Override
	protected <k, v> boolean vaiRepetir(final Contexto<k, v> contexto) {
		if (this.log().isLoggable(Level.CONFIG)) {
			this.log().config("repetindo...");
		}
		return this.cont++ < this.total;
	}

}
