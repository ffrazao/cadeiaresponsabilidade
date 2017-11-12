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

	public Comando() {
		super();
		logTrace("Novo Comando");
	}

	protected boolean antesExecutar(Contexto<?, ?> contexto) {
		logDebug("Antes de executar");
		return CONTINUAR;
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

	protected boolean erroAoExecutar(Contexto<?, ?> contexto, Exception excecao) throws Exception {
		logError("Erro ao executar");
		excecao.printStackTrace();
		return PARAR;
	}

	public void executar(Contexto<?, ?> contexto) throws Exception {
		try {
			if (inicio == null) {
				this.inicio = Instant.now();
			}
			if (contexto == null) {
				throw new IllegalArgumentException("Contexto não informado");
			}
			logInfo("Vai executar");
			do {
				try {
					if (!antesExecutar(contexto)) {
						break;
					}
					procedimento(contexto);
				} catch (Exception e) {
					if (!erroAoExecutar(contexto, e)) {
						throw e;
					}
				}
			} while (vaiRepetir(contexto));
		} finally {
			this.duracao = Duration.between(inicio, Instant.now());
			logInfo("Executou em " + descreverTempo(getDuracao().toMillis()));
		}
	}

	public Duration getDuracao() {
		return this.duracao;
	}

	public Instant getInicio() {
		return this.inicio;
	}

	final Log log() {
		if (this.log == null) {
			log = LogFactory.getLog(getClass());
		}
		return log;
	}

	protected final void logDebug(Object message) {
		if (log().isDebugEnabled()) {
			log().debug(message);
		}
	}

	protected final void logError(Object message) {
		if (log().isErrorEnabled()) {
			log().error(message);
		}
	}

	protected final void logFatal(Object message) {
		if (log().isFatalEnabled()) {
			log().fatal(message);
		}
	}

	protected final void logInfo(Object message) {
		if (log().isInfoEnabled()) {
			log().info(message);
		}
	}

	protected final void logTrace(Object message) {
		if (log().isTraceEnabled()) {
			log().trace(message);
		}
	}

	protected final void logWarn(Object message) {
		if (log().isWarnEnabled()) {
			log().warn(message);
		}
	}

	protected abstract void procedimento(Contexto<?, ?> contexto) throws Exception;

	protected boolean vaiRepetir(Contexto<?, ?> contexto) {
		logDebug("Vai repetir?");
		return PARAR;
	}

}
