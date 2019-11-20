package br.com.frazao.cadeiaresponsabilidade.teste;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

import br.com.frazao.cadeiaresponsabilidade.BibliotecaSpring;
import br.com.frazao.cadeiaresponsabilidade.CadeiaParalela;
import br.com.frazao.cadeiaresponsabilidade.CadeiaSequencial;
import br.com.frazao.cadeiaresponsabilidade.Comando;
import br.com.frazao.cadeiaresponsabilidade.Contexto;
import br.com.frazao.cadeiaresponsabilidade.ContextoBase;

@ComponentScan(basePackages = {"br.com.frazao.cadeiaresponsabilidade", "br.com.frazao.cadeiaresponsabilidade.teste"})
public class TesteCadeia {
	
	@Autowired
	private BibliotecaSpring minhaBiblioteca;
	
	public static void main(String[] args) throws Exception {
		new TesteCadeia().test();
	}
	
	@Test
	public void test() throws Exception {
		SpringApplication.run(TesteCadeia.class, new String[0]);
		
		final Contexto<String, Object> contexto = new ContextoBase<>();
		
		minhaBiblioteca.carregar(TesteCadeia.class.getPackage());
		
		final Comando t1 = minhaBiblioteca.instanciar("DIA");
		contexto.put("a", "a");
		t1.executar(contexto);

		System.out.println(contexto);

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
