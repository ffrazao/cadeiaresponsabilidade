package br.com.frazao.cadeiaresponsabilidade;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
	private ComandoDescritor antes;

	@XmlElements({ @XmlElement(name = "comando", type = ComandoDescritor.class),
			@XmlElement(name = "cadeia", type = CadeiaDescritor.class) })
	private final Set<ComandoDescritor> comandos = new HashSet<>();

	@XmlAttribute(name = "depois")
	@XmlIDREF
	private ComandoDescritor depois;

	@XmlAttribute(name = "modelo")
	@XmlIDREF
	private Catalogo modelo;

	@XmlAttribute(name = "nome", required = true)
	@XmlID
	private String nome;

	Catalogo() {
	}

	public Catalogo(final String nome) {
		if ((nome == null) || nome.trim().isEmpty()) {
			throw new IllegalArgumentException("Nome do catálogo não informado");
		}
		this.nome = nome;
	}

	public Catalogo(final String nome, final Collection<ComandoDescritor> comandos) {
		this(nome);
		this.adicionarComando(comandos);
	}

	public Catalogo(final String nome, final ComandoDescritor... comandos) {
		this(nome, Arrays.asList(comandos));
	}

	public Catalogo(final String nome, final ComandoDescritor comando) {
		this(nome);
		this.adicionarComando(comando);
	}

	@Override
	public void adicionarComando(final ComandoDescritor comando) {
		this.comandos.add(comando);
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

	public Optional<ComandoDescritor> getAntes() {
		return Optional.ofNullable(this.antes);
	}

	@Override
	public Set<ComandoDescritor> getComandos() {
		return Collections.unmodifiableSet(this.comandos);
	}

	public Optional<ComandoDescritor> getDepois() {
		return Optional.ofNullable(this.depois);
	}

	public Optional<Catalogo> getModelo() {
		return Optional.ofNullable(this.modelo);
	}

	public String getNome() {
		return this.nome;
	}

	public void setAntes(final ComandoDescritor antes) {
		this.antes = antes;
	}

	public void setDepois(final ComandoDescritor depois) {
		this.depois = depois;
	}

	public void setModelo(final Catalogo modelo) {
		this.modelo = modelo;
	}

	void setNome(final String nome) {
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Catalogo [" + this.nome + "]";
	}

}
