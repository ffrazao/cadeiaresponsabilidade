package br.com.frazao.cadeiaresponsabilidade;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public interface Comandos {

	default void adicionarComando(final Collection<DescritorComando> comandos) {
		comandos.forEach((c) -> this.adicionarComando(c));
	}

	void adicionarComando(final DescritorComando comando);

	default void adicionarComando(final DescritorComando... comandos) {
		this.adicionarComando(Arrays.asList(comandos));
	}

	default Optional<DescritorComando> getComando(final String nomeComando) {
		return this.getComandos().stream().filter(c -> c.getNome().equals(nomeComando)).findFirst();
	}

	default Optional<DescritorComando> getComando(final String nomeComando,
			final Collection<DescritorComando> comandos) {
		final Optional<DescritorComando> result = comandos.stream().filter(c -> c.getNome().contentEquals(nomeComando))
				.findFirst();
		return result;
	}

	Collection<DescritorComando> getComandos();

}
