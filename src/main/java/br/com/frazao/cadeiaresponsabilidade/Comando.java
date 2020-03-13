package br.com.frazao.cadeiaresponsabilidade;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Comando {

	protected final static boolean CONTINUAR = true;

	protected final static boolean PARAR = false;

	private Duration duracao;

	private Instant inicio;

	private Logger log = null;

	private String nome = null;

	Integer ordem;

	public Comando() {
		if (this.log().isLoggable(Level.FINER)) {
			this.log().finer(String.format("(%s) novo comando", this));
		}
	}

	public Comando(final String nome) {
		this.nome = nome;
		if (this.log().isLoggable(Level.FINER)) {
			this.log().finer(String.format("(%s) novo comando", this));
		}
	}

	protected <k, v> boolean antesProcedimento(final Contexto contexto) {
		if (this.log().isLoggable(Level.CONFIG)) {
			this.log().config(String.format("(%s) antes de executar", this));
		}
		return Comando.CONTINUAR;
	}

	protected <k, v> void depoisProcedimento(final Contexto contexto) {
		if (this.log().isLoggable(Level.FINER)) {
			this.log().finer(String.format("(%s) depois de executar", this));
		}
	}

	private final String descreverTempo(long milessegundos) {
		if (milessegundos < 0) {
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
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Comando)) {
			return false;
		}
		final Comando other = (Comando) obj;
		return this.getNome().equalsIgnoreCase(other.getNome());
	}

	protected <k, v> boolean erroAoExecutar(final Contexto contexto, final Exception e) throws Exception {
		if (this.log().isLoggable(Level.SEVERE)) {
			this.log().severe(String.format("(%s) erro ao executar, [%s]", this, e));
		}
		return Comando.PARAR;
	}

	public <k, v> void executar(final Contexto contexto) throws Exception {
		try {
			if (this.inicio == null) {
				this.inicio = Instant.now();
			}
			if (this.log().isLoggable(Level.INFO)) {
				this.log().info(String.format("(%s) iniciado", this));
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
			if (this.log().isLoggable(Level.INFO)) {
				this.log().info(String.format("(%s) executou em [%s]", this,
						this.descreverTempo(this.getDuracao().toMillis())));
			}
		}
	}

	public final Duration getDuracao() {
		return this.duracao;
	}

	public final Instant getInicio() {
		return this.inicio;
	}

	public final String getNome() {
		return this.nome == null ? this.getClass().getName() : this.nome;
	}

	@Override
	public final int hashCode() {
		return Objects.hash(this.getNome().toUpperCase());
	}

	protected final Logger log() {
		if (this.log == null) {
			this.log = Logger.getLogger(this.getClass().getName());
		}
		return this.log;
	}

	protected abstract <k, v> void procedimento(Contexto contexto) throws Exception;

	void setNome(final String nome) {
		this.nome = nome;
	}

	protected <k, v> boolean vaiRepetir(final Contexto contexto) {
		if (this.log().isLoggable(Level.CONFIG)) {
			this.log().config(String.format("(%s) vai repetir?", this));
		}
		return Comando.PARAR;
	}

	@Override
	public String toString() {
		return "Comando [" + this.getNome() + "]";
	}

}
