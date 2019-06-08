package br.com.frazao.cadeiaresponsabilidade;

import br.com.frazao.cadeiaresponsabilidade.CadeiaSequenciada;

public class Ch2 extends CadeiaSequenciada {

	public Ch2() {
		super(new C1(), new C2(), new C3());
		getComandoList().add(2, new C1());
	}

}
