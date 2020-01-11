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
public class CatalogoDescritor implements Comandos {

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
	private CatalogoDescritor modelo;

	@XmlAttribute(name = "nome", required = true)
	@XmlID
	private String nome;

	CatalogoDescritor() {
	}

	public CatalogoDescritor(final String nome) {
		this.setNome(nome);
	}

	public CatalogoDescritor(final String nome, final Collection<ComandoDescritor> comandos) {
		this(nome);
		this.adicionar(comandos);
	}

	public CatalogoDescritor(final String nome, final ComandoDescritor... comandos) {
		this(nome, Arrays.asList(comandos));
	}
	
	@Override
	public void adicionar(final ComandoDescritor comando) {
		if (comando == null) {
			throw new NullPointerException();
		}
		if (this.comandos.contains(comando)) {
			throw new IllegalArgumentException(String.format("%s repetido", comando));
		}
		this.comandos.add(comando);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CatalogoDescritor)) {
			return false;
		}
		CatalogoDescritor other = (CatalogoDescritor) obj;
		return this.getNome().equalsIgnoreCase(other.getNome());
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

	public Optional<CatalogoDescritor> getModelo() {
		return Optional.ofNullable(this.modelo);
	}

	public String getNome() {
		return this.nome == null ? this.getClass().getName() : this.nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	public void setAntes(final ComandoDescritor antes) {
		this.antes = antes;
	}

	public void setDepois(final ComandoDescritor depois) {
		this.depois = depois;
	}

	public void setModelo(final CatalogoDescritor modelo) {
		this.modelo = modelo;
	}

	void setNome(final String nome) {
		if (nome == null) {
			throw new NullPointerException("Nome de catálogo nulo");
		}
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Catalogo [" + this.getNome() + "]";
	}

}
