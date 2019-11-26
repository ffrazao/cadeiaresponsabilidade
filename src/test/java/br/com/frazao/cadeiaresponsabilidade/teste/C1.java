package br.com.frazao.cadeiaresponsabilidade.teste;

import java.util.logging.Level;

import br.com.frazao.cadeiaresponsabilidade.Comando;
import br.com.frazao.cadeiaresponsabilidade.Contexto;

public class C1 extends Comando {

	private Integer cont = 0;

	private final Integer total = 2;

	public C1(final String nome) {
		super(nome);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void procedimento(final Contexto contexto) throws Exception {
		final Integer cont = (Integer) contexto.get("cont");
		if (cont == null) {
			contexto.put("cont", this.cont);
		}
		System.out.printf("Executando C1 %d/%d\n", cont + 1, this.total);
	}

	@Override
	protected boolean vaiRepetir(final Contexto contexto) {
		if (this.log().isLoggable(Level.CONFIG)) {
			this.log().config("repetindo...");
		}
		return this.cont++ < this.total;
	}

}
