package br.com.frazao.cadeiaresponsabilidade;

import java.security.Principal;
import java.util.HashMap;

public final class ContextoBase extends HashMap<String, Object> implements Contexto {

	public static final String CAMPO_CATALOGO = "catalogo";

	public static final String CAMPO_COMANDO = "comando";

	public static final String CAMPO_REQUISICAO = "requisicao";

	public static final String CAMPO_RESPOSTA = "resposta";

	public static final String CAMPO_USUARIO = "usuario";

	private static final long serialVersionUID = 1L;

	public ContextoBase() {
		this(null);
	}

	public ContextoBase(final Object requisicao) {
		super();
		this.setRequisicao(requisicao);
	}

	@Override
	public String getCatalogo() {
		return (String) this.get(ContextoBase.CAMPO_CATALOGO);
	}

	@Override
	public String getComando() {
		return (String) this.get(ContextoBase.CAMPO_COMANDO);
	}

	@Override
	public Object getRequisicao() {
		return this.get(ContextoBase.CAMPO_REQUISICAO);
	}

	@Override
	public Object getResposta() {
		return this.get(ContextoBase.CAMPO_RESPOSTA);
	}

	@Override
	public Principal getUsuario() {
		return (Principal) this.get(ContextoBase.CAMPO_USUARIO);
	}

	@Override
	public void setCatalogo(final String catalogo) {
		this.put(ContextoBase.CAMPO_CATALOGO, catalogo);
	}

	@Override
	public void setComando(final String comando) {
		this.put(ContextoBase.CAMPO_COMANDO, comando);
	}

	@Override
	public void setRequisicao(final Object requisicao) {
		if (this.getRequisicao() != null) {
			throw new IllegalStateException("A requisição não pode ser redefinida!");
		}
		this.put(ContextoBase.CAMPO_REQUISICAO, requisicao);
	}

	@Override
	public void setResposta(final Object resposta) {
		this.put(ContextoBase.CAMPO_RESPOSTA, resposta);
	}

	@Override
	public void setUsuario(final Principal usuario) {
		this.put(ContextoBase.CAMPO_USUARIO, usuario);
	}

}
