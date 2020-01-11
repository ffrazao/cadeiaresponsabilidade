package br.com.frazao.cadeiaresponsabilidade;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public interface Comandos {

	default void adicionar(final Collection<ComandoDescritor> comandos) {
		comandos.forEach((c) -> this.adicionar(c));
	}

	void adicionar(final ComandoDescritor comando);

	default void adicionar(final ComandoDescritor... comandos) {
		this.adicionar(Arrays.asList(comandos));
	}

	default Optional<ComandoDescritor> getComando(final String nomeComando) {
		return getComando(nomeComando, this.getComandos());
	}

	default Optional<ComandoDescritor> getComando(final String nomeComando,
			final Collection<ComandoDescritor> comandos) {
		final Optional<ComandoDescritor> result = comandos.stream()
				.filter(c -> c.getNome().equalsIgnoreCase(nomeComando)).findFirst();
		return result;
	}

	Collection<ComandoDescritor> getComandos();

}
