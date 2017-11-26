package br.com.frazao.cadeiaresponsabilidade;

import java.util.Map;

public interface Contexto<K, V> extends Map<K, V> {

	public Object getRequisicao();

	public Object getResposta();

	public void setRequisicao(Object requisicao);

	public void setResposta(Object resposta);

}
