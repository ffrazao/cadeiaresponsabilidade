package br.com.frazao.cadeiaresponsabilidade.teste;

import org.springframework.stereotype.Component;

import br.com.frazao.cadeiaresponsabilidade.Comando;
import br.com.frazao.cadeiaresponsabilidade.Contexto;

@Component
public class C3 extends Comando {

	public C3(String nome) {
		super(nome);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected <k, v> void procedimento(final Contexto<k, v> contexto) throws Exception {
		// throw new Exception();
	}

}
