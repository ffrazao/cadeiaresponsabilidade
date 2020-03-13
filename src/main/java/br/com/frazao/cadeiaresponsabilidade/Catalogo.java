package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public final class Catalogo {

	public static final String CATALOGO_BASE_NOME_PADRAO = "";

	private Comando antes;

	private final Set<Comando> comandos = new TreeSet<>();

	private Comando depois;

	private String nome;

	public Catalogo(final String nome) {
		this.setNome(nome);
	}

	public Catalogo(final String nome, final Collection<Comando> comandos) {
		this(nome);
		this.adicionar(comandos);
	}

	public Catalogo(final String nome, final Comando... comandos) {
		this(nome, Arrays.asList(comandos));
	}

	public final void adicionar(final Collection<Comando> comandos) {
		comandos.forEach(c -> this.adicionar(c));
	}

	public final void adicionar(final Comando... comandos) {
		this.adicionar(Arrays.asList(comandos));
	}

	public final void adicionar(final Comando comando) {
		if (comando == null) {
			throw new NullPointerException();
		}
		if (this.comandos.contains(comando)) {
			throw new IllegalArgumentException(String.format("%s já adicionado!", comando));
		}
		this.comandos.add(comando);
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Catalogo)) {
			return false;
		}
		final Catalogo other = (Catalogo) obj;
		return this.getNome().equalsIgnoreCase(other.getNome());
	}

	public final Optional<Comando> getAntes() {
		return Optional.ofNullable(this.antes);
	}

	public Comando getComando(String nome) {
		Set<Comando> pesquisa = null;
		pesquisa = this.getComandos().stream().filter(c -> c.getNome().equalsIgnoreCase(nome)).collect(Collectors.toSet());
		if (pesquisa.size() == 1) {
			return new ArrayList<>(pesquisa).get(0);
		} else {
			throw new IllegalArgumentException(String.format("Comando inválido [%s]", nome));
		}
	}

	public final Set<Comando> getComandos() {
		return Collections.unmodifiableSet(this.comandos);
	}

	public final Optional<Comando> getDepois() {
		return Optional.ofNullable(this.depois);
	}

	public final String getNome() {
		return this.nome == null ? this.getClass().getName() : this.nome;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(this.getNome().toUpperCase());
	}

	public final void setAntes(final Comando antes) {
		this.antes = antes;
	}

	public final void setDepois(final Comando depois) {
		this.depois = depois;
	}

	final void setNome(final String nome) {
		if (nome == null) {
			throw new NullPointerException("Nome de catálogo nulo");
		}
		this.nome = nome;
	}

	@Override
	public final String toString() {
		return "Catalogo [" + this.getNome() + "]";
	}

}
