package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class CadeiaParalela extends Cadeia {

	CadeiaParalela() {
		super();
	}

	public CadeiaParalela(final Comando... comandos) {
		super(comandos);
	}

	public CadeiaParalela(final List<Comando> comandos) {
		super(comandos);
	}

	@Override
	protected final <k, v> void procedimento(final Contexto contexto) throws Exception {
		if (this.log().isLoggable(Level.CONFIG)) {
			this.log().config(String.format("(%s) cadeia paralela procedimento", this));
		}
		final List<Callable<Void>> callableList = new ArrayList<>();
		this.getComandos().stream().forEach((comando) -> callableList.add(() -> {
			comando.executar(contexto);
			return null;
		}));
		ExecutorService es = null;
		try {
			es = Executors.newWorkStealingPool();
			es.invokeAll(callableList);
		} finally {
			es.shutdown();
			es = null;
		}
		if (this.log().isLoggable(Level.FINE)) {
			this.log().fine(String.format("(%s) FIM cadeia paralela procedimento", this));
		}
	}

}
