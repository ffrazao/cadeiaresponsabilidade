package br.com.frazao.cadeiaresponsabilidade;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DescritorComando {

	@XmlAttribute
	private Class<? extends Comando> classe;

	@XmlAttribute(required = true)
	private String nome;

	public Class<? extends Comando> getClasse() {
		return this.classe;
	}

	public String getNome() {
		return this.nome;
	}

	public void setClasse(final Class<? extends Comando> classe) {
		this.classe = classe;
	}

	public void setNome(final String nome) {
		this.nome = nome;
	}

}
