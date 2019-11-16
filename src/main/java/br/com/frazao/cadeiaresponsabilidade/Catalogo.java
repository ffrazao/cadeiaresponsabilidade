package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Catalogo implements Comandos {

	@XmlAttribute(name = "antes")
	@XmlIDREF
	private DescritorComando antes;

	@XmlElements({ @XmlElement(name = "comando", type = DescritorComando.class),
			@XmlElement(name = "cadeia", type = DescritorCadeia.class) })
	private final List<DescritorComando> comandos = new ArrayList<>();

	@XmlAttribute(name = "depois")
	@XmlIDREF
	private DescritorComando depois;

	@XmlAttribute(name = "modelo")
	@XmlIDREF
	private Catalogo modelo;

	@XmlAttribute(name = "nome", required = true)
    @XmlID
	private String nome;

	public Catalogo() {
	}

	public Catalogo(final String nome) {
		if ((nome == null) || nome.trim().isEmpty()) {
			throw new IllegalArgumentException("Nome do catálogo não informado");
		}
		this.nome = nome;
	}

	public Catalogo(final String nome, final Collection<DescritorComando> comandos) {
		this(nome);
		this.adicionarComando(comandos);
	}

	public Catalogo(final String nome, final DescritorComando... comandos) {
		this(nome, Arrays.asList(comandos));
	}

	public Catalogo(final String nome, final DescritorComando comando) {
		this(nome);
		this.adicionarComando(comando);
	}

	public void adicionarComando(final Collection<DescritorComando> comandos) {
		comandos.forEach((c) -> this.adicionarComando(c));
	}

	public void adicionarComando(final DescritorComando comando) {
		this.comandos.add(comando);
	}

	public void adicionarComando(final DescritorComando... comandos) {
		this.adicionarComando(Arrays.asList(comandos));
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Catalogo)) {
			return false;
		}
		final Catalogo other = (Catalogo) obj;
		return Objects.equals(this.nome, other.nome);
	}

	public Optional<DescritorComando> getAntes() {
		return Optional.ofNullable(this.antes);
	}

	public Optional<DescritorComando> getComando(final String nomeComando) {
		return this.comandos.stream().filter(c -> c.getNome().equals(nomeComando)).findFirst();
	}

	@Override
	public List<DescritorComando> getComandos() {
		return Collections.unmodifiableList(this.comandos);
	}

	public Optional<DescritorComando> getDepois() {
		return Optional.ofNullable(this.depois);
	}

	public Optional<Catalogo> getModelo() {
		return Optional.ofNullable(this.modelo);
	}

	public String getNome() {
		return this.nome;
	}

	public void setAntes(final DescritorComando antes) {
		this.antes = antes;
	}

	public void setDepois(final DescritorComando depois) {
		this.depois = depois;
	}

	public void setModelo(final Catalogo modelo) {
		this.modelo = modelo;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Catalogo [" + this.nome + "]";
	}

}
