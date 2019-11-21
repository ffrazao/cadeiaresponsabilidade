package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DescritorCadeia extends DescritorComando implements Comandos {

	@XmlElements({ @XmlElement(name = "comando", type = DescritorComando.class),
			@XmlElement(name = "cadeia", type = DescritorCadeia.class) })
	private List<DescritorComando> comandos = new ArrayList<>();

	@XmlAttribute(name = "tipo")
	private CadeiaTipo tipo;

	@Override
	public void adicionarComando(final DescritorComando comando) {
		this.comandos.add(comando);
	}

	@Override
	public List<DescritorComando> getComandos() {
		return Collections.unmodifiableList(this.comandos);
	}

	public Optional<CadeiaTipo> getTipo() {
		return Optional.ofNullable(this.tipo);
	}

	public void setComandos(final List<DescritorComando> comados) {
		this.comandos = comados;
	}

	public void setTipo(final CadeiaTipo tipo) {
		if (tipo == null) {
			throw new NullPointerException();
		}
		this.tipo = tipo;
	}

}
