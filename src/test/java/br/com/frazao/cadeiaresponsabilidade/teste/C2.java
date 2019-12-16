package br.com.frazao.cadeiaresponsabilidade.teste;

import org.springframework.stereotype.Component;

import br.com.frazao.cadeiaresponsabilidade.Comando;
import br.com.frazao.cadeiaresponsabilidade.Contexto;

@Component
public class C2 extends Comando {

	public C2() {
		this("C2");
	}

	public C2(String nome) {
		super(nome);
	}

	@Override
	protected void procedimento(final Contexto contexto) throws Exception {
	}

}
