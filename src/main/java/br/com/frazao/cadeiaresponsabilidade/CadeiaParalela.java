package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CadeiaParalela extends Cadeia {

	public CadeiaParalela() {
		super();
	}

	public CadeiaParalela(final List<Comando> comandos) {
		super(comandos);
	}

	public CadeiaParalela(final Comando... comandos) {
		super(comandos);
	}

	@Override
	protected final void procedimento(final Contexto<?, ?> contexto) throws Exception {
		if (this.log().isDebugEnabled()) {
			this.log().debug(String.format("(%s) cadeia paralela procedimento", this.getNome()));
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
	}

}
