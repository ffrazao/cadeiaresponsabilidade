package br.com.frazao.cadeiaresponsabilidade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class CadeiaParalela extends Cadeia {

	public CadeiaParalela() {
		super();
	}

	public CadeiaParalela(Collection<Comando> comandoList) {
		super(comandoList);
	}

	public CadeiaParalela(Comando... comandoList) {
		super(comandoList);
	}

	@Override
	protected final void procedimento(Contexto<?, ?> contexto) throws Exception {
		logDebug("Cadeia Paralela");
		List<Callable<Void>> callableList = new ArrayList<>();
		getComandoList().stream().forEach((comando) -> callableList.add(() -> {
			comando.executar(contexto);
			return null;
		}));
		Executors.newWorkStealingPool().invokeAll(callableList);
	}

}
