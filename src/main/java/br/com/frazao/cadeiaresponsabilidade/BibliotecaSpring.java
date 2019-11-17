package br.com.frazao.cadeiaresponsabilidade;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BibliotecaSpring extends Biblioteca implements BeanFactoryAware {

	private BeanFactory beanFactory;

	@Bean
	@Primary
	public BibliotecaSpring create() {
		return new BibliotecaSpring();
	}

	public BibliotecaSpring() {
	}

	@Override
	public Comando instanciar(final String nomeComando) {
		final DescritorComando descritorComando = this.getComandos().stream()
				.filter(c -> c.getNome().contentEquals(nomeComando)).findFirst().get();
		Class<?> c = descritorComando.getClasse().get();

		this.beanFactory.getAliases("DIA");

		final Comando result = (Comando) this.beanFactory.getBean(c);

		return result;
	}

	@Override
	public Comando instanciar(final String nomeCatalogo, final String nomeComando) {
		return null;
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
