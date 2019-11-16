package br.com.frazao.cadeiaresponsabilidade;

import org.junit.Test;

public class TesteCadeia {

	@Test
	public void test() throws Exception {

		System.out.println(MinhaBiblioteca.getInstance().getComandos());

		if (1 == 1) {
			return;
		}

		final Comando c1 = new C1();
		final Comando c2 = new C2();
		final Comando c3 = new C3();
		final Comando ch1 = new Ch1(c1, c2, c3);
		final Comando ch2 = new Ch2();
		final Comando ch3 = new CadeiaParalela(ch1, ch2, c3);
		final Comando ch4 = new CadeiaSequencial(ch3, ch1, ch2);

		final Contexto<String, Object> contexto = new ContextoBase<>();
		try {
			c1.executar(contexto);
			System.out.println(c1.getDuracao());
		} catch (final Exception e) {
		}

		try {
			c2.executar(contexto);
			System.out.println(c2.getDuracao());
		} catch (final Exception e) {
		}

		try {
			c3.executar(contexto);
			System.out.println(c3.getDuracao());
		} catch (final Exception e) {
		}

		try {
			ch1.executar(contexto);
			System.out.println(ch1.getDuracao());

		} catch (final Exception e) {
		}

		try {
			ch2.executar(contexto);
			System.out.println(ch2.getDuracao());
		} catch (final Exception e) {
		}

		try {
			ch3.executar(contexto);
			System.out.println(ch3.getDuracao());

		} catch (final Exception e) {
		}

		try {
			ch4.executar(contexto);
			System.out.println(ch4.getDuracao());
		} catch (final Exception e) {
		}

	}

}
