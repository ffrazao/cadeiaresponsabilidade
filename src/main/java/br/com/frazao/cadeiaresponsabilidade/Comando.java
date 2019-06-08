package br.com.frazao.cadeiaresponsabilidade;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Comando {

	protected final static boolean CONTINUAR = true;

	protected final static boolean PARAR = false;

	protected Duration duracao;

	protected Instant inicio;

	private Log log = null;
	
	public String getNome() {
		return this.getClass().getName();
	}

	public Comando() {
		super();
		if (log().isTraceEnabled()) {
			log().trace(String.format("(%s) novo comando", getNome()));
		}
	}

	protected boolean antesProcedimento(Contexto<?, ?> contexto) {
		if (log().isDebugEnabled()) {
			log().debug(String.format("(%s) antes de executar", getNome()));
		}
		return CONTINUAR;
	}

	protected void depoisProcedimento(Contexto<?, ?> contexto) {
		if (log().isTraceEnabled()) {
			log().trace(String.format("(%s) depois de executar", getNome()));
		}
	}

	private String descreverTempo(long milessegundos) {
		if (milessegundos <= 0) {
			return "";
		}

		long dias = TimeUnit.MILLISECONDS.toDays(milessegundos);
		milessegundos -= TimeUnit.DAYS.toMillis(dias);
		long horas = TimeUnit.MILLISECONDS.toHours(milessegundos);
		milessegundos -= TimeUnit.HOURS.toMillis(horas);
		long minutos = TimeUnit.MILLISECONDS.toMinutes(milessegundos);
		milessegundos -= TimeUnit.MINUTES.toMillis(minutos);
		long segundos = TimeUnit.MILLISECONDS.toSeconds(milessegundos);
		milessegundos -= TimeUnit.SECONDS.toMillis(segundos);

		return String.format("%dd %dh %dm %ds %dms", dias, horas, minutos, segundos, milessegundos);
	}

	protected boolean erroAoExecutar(Contexto<?, ?> contexto, Exception e) throws Exception {
		if (log().isErrorEnabled()) {
			log().error(String.format("(%s) erro ao executar", getNome()), e);
		}
		return PARAR;
	}

	public void executar(Contexto<?, ?> contexto) throws Exception {
		try {
			if (inicio == null) {
				this.inicio = Instant.now();
			}
			if (log().isInfoEnabled()) {
				log().info(String.format("(%s) iniciado", getNome()));
			}
			if (contexto == null) {
				throw new IllegalArgumentException("Contexto não informado");
			}
			do {
				try {
					if (!antesProcedimento(contexto)) {
						break;
					}
					procedimento(contexto);
					depoisProcedimento(contexto);
				} catch (Exception e) {
					if (!erroAoExecutar(contexto, e)) {
						throw e;
					}
				}
			} while (vaiRepetir(contexto));
		} finally {
			this.duracao = Duration.between(inicio, Instant.now());
			if (log().isInfoEnabled()) {
				log().info(String.format("(%s) executou em [%s]", getNome(), descreverTempo(getDuracao().toMillis())));
			}
		}
	}

	public Duration getDuracao() {
		return this.duracao;
	}

	public Instant getInicio() {
		return this.inicio;
	}

	protected Log log() {
		if (log == null) {
			log = LogFactory.getLog(getClass());
		}
		return log;
	}

	protected abstract void procedimento(Contexto<?, ?> contexto) throws Exception;

	protected boolean vaiRepetir(Contexto<?, ?> contexto) {
		if (log().isDebugEnabled()) {
			log().debug(String.format("(%s) vai repetir?", getNome()));
		}
		return PARAR;
	}

}
