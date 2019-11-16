package br.com.frazao.cadeiaresponsabilidade;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DescritorCadeia extends DescritorComando implements Comandos {

	@XmlAttribute(name = "tipo", required = false)
	private CadeiaTipo cadeiaTipo = CadeiaTipo.SEQUENCIAL;

	@XmlElements({ @XmlElement(name = "comando", type = DescritorComando.class),
			@XmlElement(name = "cadeia", type = DescritorCadeia.class) })
	private final Set<DescritorComando> comandos = new HashSet<>();

	public CadeiaTipo getCadeiaTipo() {
		return this.cadeiaTipo;
	}

	@Override
	public Set<DescritorComando> getComandos() {
		return this.comandos;
	}

	public void setCadeiaTipo(final CadeiaTipo cadeiaTipo) {
		this.cadeiaTipo = cadeiaTipo;
	}

}
