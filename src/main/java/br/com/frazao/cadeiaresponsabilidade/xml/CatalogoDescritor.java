package br.com.frazao.cadeiaresponsabilidade.xml;

import java.util.ArrayList;
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

import br.com.frazao.cadeiaresponsabilidade.Catalogo;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class CatalogoDescritor {
	
	public static CatalogoDescritor catalogoBase(List<ComandoDescritor> comandos) {
		final CatalogoDescritor result = new CatalogoDescritor();
		result.nome = Catalogo.CATALOGO_BASE_NOME_PADRAO;
		result.comandos.addAll(comandos);
		return result;
	}

	@XmlAttribute(name = "antes")
	@XmlIDREF
	private ComandoDescritor antes;

	@XmlElements({ @XmlElement(name = "comando", type = ComandoDescritor.class),
			@XmlElement(name = "cadeia", type = CadeiaDescritor.class) })
	private final List<ComandoDescritor> comandos = new ArrayList<>();

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

	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CatalogoDescritor)) {
			return false;
		}
		CatalogoDescritor other = (CatalogoDescritor) obj;
		return this.getNome().equalsIgnoreCase(other.getNome());
	}

	public final Optional<ComandoDescritor> getAntes() {
		return Optional.ofNullable(this.antes);
	}

	public final List<ComandoDescritor> getComandos() {
		return Collections.unmodifiableList(this.comandos);
	}

	public final Optional<ComandoDescritor> getDepois() {
		return Optional.ofNullable(this.depois);
	}

	public final Optional<CatalogoDescritor> getModelo() {
		return Optional.ofNullable(this.modelo);
	}

	public final String getNome() {
		return this.nome;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public final String toString() {
		return "Catalogo [" + this.getNome() + "]";
	}

}
