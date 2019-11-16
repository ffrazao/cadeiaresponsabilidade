package br.com.frazao.cadeiaresponsabilidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Biblioteca implements Comandos {

	public static final String BIBLIOTECA_NOME = "biblioteca.xml";

	@XmlElement(name = "catalogo")
	private final Set<Catalogo> catalogos = new HashSet<>();

	@XmlElements({ @XmlElement(name = "comando", type = DescritorComando.class),
			@XmlElement(name = "cadeia", type = DescritorCadeia.class) })
	private final Set<DescritorComando> comandos = new HashSet<>();

	public Biblioteca() {
	}

	public void carregar(final File nomeArquivo) throws Exception {
		try (InputStream is = new FileInputStream(nomeArquivo)) {
			this.carregar(is);
		}
	}

	public void carregar(final InputStream arquivo) throws Exception {
		try {
			final JAXBContext context = JAXBContext.newInstance(this.getClass());
			final Unmarshaller unmarshaller = context.createUnmarshaller();
			final Biblioteca biblioteca = (Biblioteca) unmarshaller.unmarshal(arquivo);
			System.out.println(biblioteca);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void carregar(final Package pacote) throws Exception {
		Resource[] rs;
		try {
			final String nomeRecurso = String.format("classpath:%s/**/%s", pacote.getName().replaceAll("\\.", "/"),
					Biblioteca.BIBLIOTECA_NOME);
			rs = new PathMatchingResourcePatternResolver().getResources(nomeRecurso);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		for (final Resource r : rs) {
			try (InputStream is = r.getInputStream()) {
				this.carregar(is);
			}
		}
	}

	public void carregar(final String nomeArquivo) throws Exception {
		this.carregar(new File(nomeArquivo));
	}

	public void executar(final String nomeCatalogo, final String nomeComando, final Contexto<?, ?> contexto) {

	}

	public Set<Catalogo> getCatalogos() {
		return this.catalogos;
	}

	@Override
	public Set<DescritorComando> getComandos() {
		return this.comandos;
	}

}
