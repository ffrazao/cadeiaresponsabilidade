package br.com.frazao.cadeiaresponsabilidade.teste;

import org.springframework.stereotype.Component;

import br.com.frazao.cadeiaresponsabilidade.CadeiaSequencial;

@Component
public class Ch2 extends CadeiaSequencial {

	public Ch2() {
		super(new C1("1"), new C2("2"), new C3("3"));
		this.adicionarComando(1, new C1("22"));
	}

}
