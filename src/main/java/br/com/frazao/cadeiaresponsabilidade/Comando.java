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
	
	protected Log log() {
		if (log == null) {
			log = LogFactory.getLog(getClass());
		}
		return log;
	}
	
	public Comando() {
		super();
		if (log().isTraceEnabled()) {
			log().trace("Novo Comando");
		}
	}

	protected boolean antesExecutar(Contexto<?, ?> contexto) {
		if (log().isDebugEnabled()) {
			log().debug("Antes de executar");
		}
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
		if (log().isErrorEnabled()) {
			log().error("Erro ao executar");
		}
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
			if (log().isInfoEnabled()) {
				log().info("iniciado");
			}
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
			if (log().isInfoEnabled()) {
				log().info("Executou em " + descreverTempo(getDuracao().toMillis()));
			}
		}
	}

	public Duration getDuracao() {
		return this.duracao;
	}

	public Instant getInicio() {
		return this.inicio;
	}

	protected abstract void procedimento(Contexto<?, ?> contexto) throws Exception;

	protected boolean vaiRepetir(Contexto<?, ?> contexto) {
		if (log().isDebugEnabled()) {
			log().debug("Vai repetir?");
		}
		return PARAR;
	}

}
