package br.com.frazao.cadeiaresponsabilidade;

public class Ch2 extends CadeiaSequencial {

	public Ch2() {
		super(new C1(), new C2(), new C3());
		this.getSequencia().add(2, new C1());
	}

}
