package br.com.frazao.cadeiaresponsabilidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

	public void adicionarCatalogo(final Catalogo... catalogos) {
		this.adicionarCatalogo(Arrays.asList(catalogos));
	}

	public void adicionarCatalogo(final Catalogo catalogo) {
		this.catalogos.add(catalogo);
	}

	public void adicionarCatalogo(final Collection<Catalogo> catalogos) {
		catalogos.forEach(catalogo -> this.adicionarCatalogo(catalogo));
	}

	public void adicionarComando(final Collection<DescritorComando> comandos) {
		comandos.forEach(comando -> this.adicionarComando(comando));
	}

	public void adicionarComando(final DescritorComando... comandos) {
		this.adicionarComando(Arrays.asList(comandos));
	}

	public void adicionarComando(final DescritorComando comando) {
		this.comandos.add(comando);
	}

	public void carregar(final File nomeArquivo) throws Exception {
		try (InputStream is = new FileInputStream(nomeArquivo)) {
			this.carregar(is);
		}
	}

	public void carregar(final InputStream arquivo) throws Exception {
		final JAXBContext context = JAXBContext.newInstance(Biblioteca.class);
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final Biblioteca biblioteca = (Biblioteca) unmarshaller.unmarshal(arquivo);
		this.adicionarComando(biblioteca.getComandos());
		this.adicionarCatalogo(biblioteca.getCatalogos());
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

	public Set<Catalogo> getCatalogos() {
		return Collections.unmodifiableSet(this.catalogos);
	}

	@Override
	public Set<DescritorComando> getComandos() {
		return Collections.unmodifiableSet(this.comandos);
	}

	public Comando instanciar(final String nomeComando) throws Exception {
		final DescritorComando descritorComando = this.getComandos().stream()
				.filter(c -> c.getNome().contentEquals(nomeComando)).findFirst().get();
		final Comando result = descritorComando.getClasse().get().newInstance();
		return result;
	}

	public Comando instanciar(final String nomeCatalogo, final String nomeComando) {
		return null;
	}

}
