package br.com.frazao.cadeiaresponsabilidade.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import br.com.frazao.cadeiaresponsabilidade.Biblioteca;
import br.com.frazao.cadeiaresponsabilidade.Comando;

@Configuration
@Component
public class BibliotecaSpring extends Biblioteca implements BeanFactoryAware {

	private BeanFactory beanFactory;

	public BibliotecaSpring() {
	}

	@Bean
	@Primary
	public BibliotecaSpring create() {
		return new BibliotecaSpring();
	}

	// FIXME @Override
	protected Comando instanciar(final Class<? extends Comando> classe) throws Exception {
		return this.instanciarBean(classe);
	}

	public <E> E instanciarBean(final Class<E> c) {
		return this.beanFactory.getBean(c);
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
