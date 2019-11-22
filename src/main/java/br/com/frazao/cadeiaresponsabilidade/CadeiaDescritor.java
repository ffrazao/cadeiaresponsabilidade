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
class CadeiaDescritor extends ComandoDescritor implements Comandos {

	@XmlAttribute(name = "acao")
	private CadeiaAcao acao = CadeiaAcao.MESCLAR;

	@XmlElements({ @XmlElement(name = "comando", type = ComandoDescritor.class),
			@XmlElement(name = "cadeia", type = CadeiaDescritor.class) })
	private List<ComandoDescritor> comandos = new ArrayList<>();

	@XmlAttribute(name = "tipo")
	private CadeiaTipo tipo;

	@Override
	public void adicionarComando(final ComandoDescritor comando) {
		this.comandos.add(comando);
	}

	public Optional<CadeiaAcao> getAcao() {
		return Optional.ofNullable(this.acao);
	}

	@Override
	public List<ComandoDescritor> getComandos() {
		return Collections.unmodifiableList(this.comandos);
	}

	public Optional<CadeiaTipo> getTipo() {
		return Optional.ofNullable(this.tipo);
	}

	void setAcao(final CadeiaAcao acao) {
		this.acao = acao;
	}

	public void setComandos(final List<ComandoDescritor> comados) {
		this.comandos = comados;
	}

	public void setTipo(final CadeiaTipo tipo) {
		if (tipo == null) {
			throw new NullPointerException();
		}
		this.tipo = tipo;
	}

}
