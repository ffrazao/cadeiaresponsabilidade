package br.com.frazao.cadeiaresponsabilidade;

import java.util.HashMap;

public final class ContextoBase<K, V> extends HashMap<K, V> implements Contexto<K, V> {

	private static final long serialVersionUID = 1L;

	private Object requisicao;

	private Object resposta;

	public ContextoBase() {
		this(null);
	}

	public ContextoBase(final Object requisicao) {
		super();
		this.setRequisicao(requisicao);
	}

	@Override
	public Object getRequisicao() {
		return this.requisicao;
	}

	@Override
	public Object getResposta() {
		return this.resposta;
	}

	@Override
	public void setRequisicao(final Object requisicao) {
		if (this.requisicao != null) {
			throw new IllegalStateException("A requisição não pode ser redefinida!");
		}
		this.requisicao = requisicao;
	}

	@Override
	public void setResposta(final Object resposta) {
		this.resposta = resposta;
	}

}
