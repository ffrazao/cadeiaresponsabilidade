package br.com.frazao.cadeiaresponsabilidade.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "biblioteca")
public final class BibliotecaDescritor {

	public static final String BIBLIOTECA_ARQUIVO_NOME_PADRAO = "biblioteca.xml";

	public static final BibliotecaDescritor carregar(InputStream io) throws JAXBException {
		final BibliotecaDescritor result = (BibliotecaDescritor) JAXBContext.newInstance(BibliotecaDescritor.class).createUnmarshaller()
				.unmarshal(io);
		return result;
	}

	@XmlElement(name = "catalogo")
	private final List<CatalogoDescritor> catalogos = new ArrayList<>();

	@XmlElements({ @XmlElement(name = "comando", type = ComandoDescritor.class),
			@XmlElement(name = "cadeia", type = CadeiaDescritor.class) })
	private final List<ComandoDescritor> comandos = new ArrayList<>();

	BibliotecaDescritor() {
	}

	public final List<CatalogoDescritor> getCatalogos() {
		return Collections.unmodifiableList(this.catalogos);
	}

	public final List<ComandoDescritor> getComandos() {
		return Collections.unmodifiableList(this.comandos);
	}

	@Override
	public final String toString() {
		return String.format("comandos[%s]\ncatalogos[%s]", this.getComandos(), this.getCatalogos());
	}

}
