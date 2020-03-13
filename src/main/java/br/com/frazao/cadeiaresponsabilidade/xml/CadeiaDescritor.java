package br.com.frazao.cadeiaresponsabilidade.xml;

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

import br.com.frazao.cadeiaresponsabilidade.dominio.CadeiaAcao;
import br.com.frazao.cadeiaresponsabilidade.dominio.CadeiaTipo;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class CadeiaDescritor extends ComandoDescritor {

	@XmlAttribute(name = "acao")
	private CadeiaAcao acao = CadeiaAcao.MESCLAR;

	@XmlElements({ @XmlElement(name = "comando", type = ComandoDescritor.class),
			@XmlElement(name = "cadeia", type = CadeiaDescritor.class) })
	private final List<ComandoDescritor> comandos = new ArrayList<>();

	@XmlAttribute(name = "tipo")
	private CadeiaTipo tipo;

	CadeiaDescritor() {
	}

	public final Optional<CadeiaAcao> getAcao() {
		return Optional.ofNullable(this.acao);
	}

	public final List<ComandoDescritor> getComandos() {
		return Collections.unmodifiableList(this.comandos);
	}

	public final Optional<CadeiaTipo> getTipo() {
		return Optional.ofNullable(this.tipo);
	}

}
