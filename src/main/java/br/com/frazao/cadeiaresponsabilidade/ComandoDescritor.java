package br.com.frazao.cadeiaresponsabilidade;

import java.util.Objects;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ComandoDescritor {

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
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ComandoDescritor)) {
			return false;
		}
		final ComandoDescritor other = (ComandoDescritor) obj;
		return this.nome.equalsIgnoreCase(other.nome);
	}

	public Optional<Class<? extends Comando>> getClasse() {
		return Optional.ofNullable(this.classe);
	}

	public String getNome() {
		return this.nome;
	}

	public Optional<Integer> getOrdem() {
		return Optional.ofNullable(this.ordem);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.nome);
	}

	public void setClasse(final Class<? extends Comando> classe) {
		this.classe = classe;
	}

	public void setOrdem(final Integer ordem) {
		this.ordem = ordem;
	}
	
	@Override
	public String toString() {
		return String.format("%s[%s]", this.getClass().getSimpleName(), this.getNome());
	}

}
