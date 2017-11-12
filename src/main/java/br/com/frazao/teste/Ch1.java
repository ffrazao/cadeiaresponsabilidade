package br.com.frazao.teste;

import br.com.frazao.cadeiaresponsabilidade.CadeiaParalela;
import br.com.frazao.cadeiaresponsabilidade.Comando;

public class Ch1 extends CadeiaParalela {

	public Ch1(Comando... comandoList) {
		super(comandoList);
	}

}
