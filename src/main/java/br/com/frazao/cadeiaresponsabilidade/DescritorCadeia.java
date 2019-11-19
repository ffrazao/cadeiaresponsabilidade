package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	private final List<DescritorComando> comandos = new ArrayList<>();

	@Override
	public void adicionarComando(final DescritorComando comando) {
		this.comandos.add(comando);
	}

	public CadeiaTipo getCadeiaTipo() {
		return this.cadeiaTipo;
	}

	@Override
	public List<DescritorComando> getComandos() {
		return Collections.unmodifiableList(this.comandos);
	}

	public void setCadeiaTipo(final CadeiaTipo cadeiaTipo) {
		if (cadeiaTipo == null) {
			throw new NullPointerException();
		}
		this.cadeiaTipo = cadeiaTipo;
	}

}
