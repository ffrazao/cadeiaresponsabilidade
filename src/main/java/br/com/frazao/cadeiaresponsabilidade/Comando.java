package br.com.frazao.cadeiaresponsabilidade;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Comando {

	protected final static boolean CONTINUAR = true;

	protected final static boolean PARAR = false;

	private Duration duracao;

	private Instant inicio;

	private Log log = null;

	private String nome = null;

	public Comando() {
		super();
		if (this.log().isTraceEnabled()) {
			this.log().trace(String.format("(%s) novo comando", this.getNome()));
		}
	}

	public Comando(final String nome) {
		this();
		this.nome = nome;
	}

	protected <k, v> boolean antesProcedimento(final Contexto<k, v> contexto) {
		if (this.log().isDebugEnabled()) {
			this.log().debug(String.format("(%s) antes de executar", this.getNome()));
		}
		return Comando.CONTINUAR;
	}

	protected <k, v> void depoisProcedimento(final Contexto<k, v> contexto) {
		if (this.log().isTraceEnabled()) {
			this.log().trace(String.format("(%s) depois de executar", this.getNome()));
		}
	}

	private String descreverTempo(long milessegundos) {
		if (milessegundos <= 0) {
			return "";
		}

		final long dias = TimeUnit.MILLISECONDS.toDays(milessegundos);
		milessegundos -= TimeUnit.DAYS.toMillis(dias);
		final long horas = TimeUnit.MILLISECONDS.toHours(milessegundos);
		milessegundos -= TimeUnit.HOURS.toMillis(horas);
		final long minutos = TimeUnit.MILLISECONDS.toMinutes(milessegundos);
		milessegundos -= TimeUnit.MINUTES.toMillis(minutos);
		final long segundos = TimeUnit.MILLISECONDS.toSeconds(milessegundos);
		milessegundos -= TimeUnit.SECONDS.toMillis(segundos);

		return String.format("%dd %dh %dm %ds %dms", dias, horas, minutos, segundos, milessegundos);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Comando)) {
			return false;
		}
		final Comando other = (Comando) obj;
		return Objects.equals(this.getNome(), other.getNome());
	}

	protected <k, v> boolean erroAoExecutar(final Contexto<k, v> contexto, final Exception e) throws Exception {
		if (this.log().isErrorEnabled()) {
			this.log().error(String.format("(%s) erro ao executar", this.getNome()), e);
		}
		return Comando.PARAR;
	}

	public <k, v> void executar(final Contexto<k, v> contexto) throws Exception {
		try {
			if (this.inicio == null) {
				this.inicio = Instant.now();
			}
			if (this.log().isInfoEnabled()) {
				this.log().info(String.format("(%s) iniciado", this.getNome()));
			}
			if (contexto == null) {
				throw new IllegalArgumentException("Contexto não informado");
			}
			do {
				try {
					if (!this.antesProcedimento(contexto)) {
						break;
					}
					this.procedimento(contexto);
					this.depoisProcedimento(contexto);
				} catch (final Exception e) {
					if (!this.erroAoExecutar(contexto, e)) {
						throw e;
					}
				}
			} while (this.vaiRepetir(contexto));
		} finally {
			this.duracao = Duration.between(this.inicio, Instant.now());
			if (this.log().isInfoEnabled()) {
				this.log().info(String.format("(%s) executou em [%s]", this.getNome(),
						this.descreverTempo(this.getDuracao().toMillis())));
			}
		}
	}

	public Duration getDuracao() {
		return this.duracao;
	}

	public Instant getInicio() {
		return this.inicio;
	}

	public String getNome() {
		return this.nome == null ? this.getClass().getName() : this.nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getNome());
	}

	protected Log log() {
		if (this.log == null) {
			this.log = LogFactory.getLog(this.getClass());
		}
		return this.log;
	}

	protected abstract <k, v> void procedimento(Contexto<k, v> contexto) throws Exception;

	protected <k, v> boolean vaiRepetir(final Contexto<k, v> contexto) {
		if (this.log().isDebugEnabled()) {
			this.log().debug(String.format("(%s) vai repetir?", this.getNome()));
		}
		return Comando.PARAR;
	}

}
