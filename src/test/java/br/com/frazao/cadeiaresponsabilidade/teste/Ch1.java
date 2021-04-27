package br.com.frazao.cadeiaresponsabilidade.teste;

import org.springframework.stereotype.Component;

import br.com.frazao.cadeiaresponsabilidade.Comando;
import br.com.frazao.cadeiaresponsabilidade.impl.CadeiaParalela;

@Component
public class Ch1 extends CadeiaParalela {
	
	public Ch1(String nome) {
		super(nome);
	}

	public Ch1(String nome, final Comando... comandoList) {
		super(nome, comandoList);
	}

}
