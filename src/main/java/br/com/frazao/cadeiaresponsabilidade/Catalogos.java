package br.com.frazao.cadeiaresponsabilidade;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public interface Catalogos {

	void adicionarCatalogo(final Catalogo catalogo);

	default void adicionarCatalogo(final Catalogo... catalogos) {
		this.adicionarCatalogo(Arrays.asList(catalogos));
	}

	default void adicionarCatalogo(final Collection<Catalogo> catalogos) {
		catalogos.forEach((c) -> this.adicionarCatalogo(c));
	}

	default Optional<Catalogo> getCatalogo(final String nomeCatalogo) {
		final Optional<Catalogo> result = this.getCatalogos().stream()
				.filter(c -> c.getNome().equalsIgnoreCase(nomeCatalogo)).findFirst();
		return result;
	}

	Collection<Catalogo> getCatalogos();

}
