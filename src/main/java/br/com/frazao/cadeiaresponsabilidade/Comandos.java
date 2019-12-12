package br.com.frazao.cadeiaresponsabilidade;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public interface Comandos {

	default void adicionarComando(final Collection<ComandoDescritor> comandos) {
		comandos.forEach((c) -> this.adicionarComando(c));
	}

	void adicionarComando(final ComandoDescritor comando);

	default void adicionarComando(final ComandoDescritor... comandos) {
		this.adicionarComando(Arrays.asList(comandos));
	}

	default Optional<ComandoDescritor> getComando(final String nomeComando) {
		return this.getComandos().stream().filter(c -> c.getNome().equalsIgnoreCase(nomeComando)).findFirst();
	}

	default Optional<ComandoDescritor> getComando(final String nomeComando,
			final Collection<ComandoDescritor> comandos) {
		final Optional<ComandoDescritor> result = comandos.stream()
				.filter(c -> c.getNome().equalsIgnoreCase(nomeComando)).findFirst();
		return result;
	}

	Collection<ComandoDescritor> getComandos();

}
