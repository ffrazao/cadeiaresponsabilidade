package br.com.frazao.cadeiaresponsabilidade;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("br.com.frazao.cadeiaresponsabilidade")
public class BibliotecaSpring extends Biblioteca implements BeanFactoryAware {

	private BeanFactory beanFactory;

	public BibliotecaSpring() {
	}

	@Bean
	@Primary
	public BibliotecaSpring create() {
		return new BibliotecaSpring();
	}

	@Override
	protected Comando instanciar(final Class<? extends Comando> classe) throws Exception {
		return this.beanFactory.getBean(classe);
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public <E> E instanciarBean(Class<E> c) {
		return this.beanFactory.getBean(c);
	}

}
