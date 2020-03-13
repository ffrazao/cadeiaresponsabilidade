package br.com.frazao.cadeiaresponsabilidade.xml;

import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.frazao.cadeiaresponsabilidade.Comando;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ComandoDescritor {

	@XmlAttribute
	private Class<? extends Comando> classe;

	@XmlAttribute(required = true)
	@XmlID
	private String nome;

	@XmlAttribute
	private Integer ordem;

	ComandoDescritor() {
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ComandoDescritor)) {
			return false;
		}
		final ComandoDescritor other = (ComandoDescritor) obj;
		return this.nome.equalsIgnoreCase(other.nome);
	}

	public final Optional<Class<? extends Comando>> getClasse() {
		return Optional.ofNullable(this.classe);
	}

	public final String getNome() {
		return this.nome;
	}

	public final Optional<Integer> getOrdem() {
		return Optional.ofNullable(this.ordem);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(this.nome);
	}
	
	@Override
	public final String toString() {
		return String.format("%s[%s]", this.getClass().getSimpleName(), this.getNome());
	}

}
