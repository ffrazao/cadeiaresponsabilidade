package br.com.frazao.cadeiaresponsabilidade;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Catalogo implements Comandos {

	@XmlAttribute(name = "antes")
	private String comandoAntes;

	@XmlAttribute(name = "depois")
	private String comandoDepois;

	@XmlElements({ @XmlElement(name = "comando", type = DescritorComando.class),
			@XmlElement(name = "cadeia", type = DescritorCadeia.class) })
	private final Set<DescritorComando> comandos = new HashSet<>();

	@XmlAttribute(name = "nome")
	private String nomeCatalogo;

	@XmlAttribute(name = "modelo")
	private String nomeModeloCatalogo;

	public Catalogo() {
	}

	public Catalogo(final String nomeCatalogo) {
		if ((nomeCatalogo == null) || nomeCatalogo.trim().isEmpty()) {
			throw new IllegalArgumentException("Nome não informado");
		}
		this.nomeCatalogo = nomeCatalogo;
	}

	public Catalogo(final String nomeCatalogo, final DescritorComando... comandos) {
		this(nomeCatalogo);
		this.adicionarComando(comandos);
	}

	public Catalogo(final String nomeCatalogo, final DescritorComando comando) {
		this(nomeCatalogo);
		this.adicionarComando(comando);
	}

	public void adicionarComando(final DescritorComando comando) {
		this.getComandos().add(comando);
	}

	public void adicionarComando(final DescritorComando... comandos) {
		Arrays.asList(comandos).forEach((c) -> this.adicionarComando(c));
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Catalogo)) {
			return false;
		}
		final Catalogo other = (Catalogo) obj;
		return Objects.equals(this.nomeCatalogo, other.nomeCatalogo);
	}

	public Optional<DescritorComando> getComando(final String nomeComando) {
		return this.getComandos().stream().filter(c -> c.getNome().equals(nomeComando)).findFirst();
	}

	public String getComandoAntes() {
		return this.comandoAntes;
	}

	public String getComandoDepois() {
		return this.comandoDepois;
	}

	@Override
	public Set<DescritorComando> getComandos() {
		return this.comandos;
	}

	public String getNomeCatalogo() {
		return this.nomeCatalogo;
	}

	public String getNomeModeloCatalogo() {
		return this.nomeModeloCatalogo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.nomeCatalogo);
	}

	public void setComandoAntes(final String comandoAntes) {
		this.comandoAntes = comandoAntes;
	}

	public void setComandoDepois(final String comandoDepois) {
		this.comandoDepois = comandoDepois;
	}

	public void setNomeCatalogo(final String nomeCatalogo) {
		this.nomeCatalogo = nomeCatalogo;
	}

	public void setNomeModeloCatalogo(final String nomeModeloCatalogo) {
		this.nomeModeloCatalogo = nomeModeloCatalogo;
	}

	@Override
	public String toString() {
		return "Catalogo [" + this.nomeCatalogo + "]";
	}

}
