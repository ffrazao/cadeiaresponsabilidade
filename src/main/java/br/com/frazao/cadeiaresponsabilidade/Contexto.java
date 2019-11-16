package br.com.frazao.cadeiaresponsabilidade;

import java.util.Map;

public interface Contexto<K, V> extends Map<K, V> {

	Object getRequisicao();

	Object getResposta();

	void setRequisicao(Object requisicao);

	void setResposta(Object resposta);

}
