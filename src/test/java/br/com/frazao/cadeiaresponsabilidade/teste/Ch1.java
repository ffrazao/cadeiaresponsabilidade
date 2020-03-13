package br.com.frazao.cadeiaresponsabilidade.teste;

import org.springframework.stereotype.Component;

import br.com.frazao.cadeiaresponsabilidade.Comando;
import br.com.frazao.cadeiaresponsabilidade.impl.CadeiaParalela;

@Component
public class Ch1 extends CadeiaParalela {
	
	public Ch1() {
	}

	public Ch1(final Comando... comandoList) {
		super(comandoList);
	}

}
