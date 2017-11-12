package br.com.frazao.teste;

import br.com.frazao.cadeiaresponsabilidade.Cadeia;
import br.com.frazao.cadeiaresponsabilidade.CadeiaParalela;
import br.com.frazao.cadeiaresponsabilidade.CadeiaSequenciada;
import br.com.frazao.cadeiaresponsabilidade.Comando;
import br.com.frazao.cadeiaresponsabilidade.Contexto;
import br.com.frazao.cadeiaresponsabilidade.ContextoBase;

public class Main {

	public static void main(String[] args) throws Exception {
		Comando c1 = new C1();
		Comando c2 = new C2();
		Comando c3 = new C3();
		Cadeia ch1 = new Ch1(c1, c2, c3);
		Cadeia ch2 = new Ch2(c1, c2, c3);
		Cadeia ch3 = new CadeiaParalela(ch1, ch2);
		Cadeia ch4 = new CadeiaSequenciada(ch3, ch1, ch2);

		Contexto<String, Object> contexto = new ContextoBase<>();
		try {
			c1.executar(contexto);
			System.out.println(c1.getDuracao());
		} catch (Exception e) {
		}
		
		try {
			c2.executar(contexto);
			System.out.println(c2.getDuracao());
		} catch (Exception e) {
		}
		
		try {
			c3.executar(contexto);
			System.out.println(c3.getDuracao());
		} catch (Exception e) {
		}
		
		try {
			ch1.executar(contexto);
			System.out.println(ch1.getDuracao());

		} catch (Exception e) {
		}
		
		try {
			ch2.executar(contexto);
			System.out.println(ch2.getDuracao());
		} catch (Exception e) {
		}
		
		try {
			ch3.executar(contexto);
			System.out.println(ch3.getDuracao());

		} catch (Exception e) {
		}
		
		try {
			ch4.executar(contexto);
			System.out.println(ch4.getDuracao());
		} catch (Exception e) {
		}
		
	}

}
