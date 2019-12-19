package br.com.frazao.cadeiaresponsabilidade;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public final class ContextoBase extends HashMap<String, Object> implements Contexto {

	public static final String CAMPO_CATALOGO = "Catalogo";

	public static final String CAMPO_COMANDO = "Comando";

	public static final String CAMPO_REQUISICAO = "Requisicao";

	public static final String CAMPO_RESPOSTA = "Resposta";

	public static final String CAMPO_RESPOSTA_HISTORICO = "Resposta_historico";

	public static final String CAMPO_USUARIO = "Usuario";

	private static final long serialVersionUID = 1L;

	public ContextoBase() {
		this(null);
	}

	public ContextoBase(final Object valor) {
		super();
		this.setRequisicao(valor);
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

	@SuppressWarnings("unchecked")
	@Override
	public Object getResposta(Integer valor) {
		List<Object> respostaHistorico = (List<Object>) this.get(CAMPO_RESPOSTA_HISTORICO);
		return respostaHistorico.get(valor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Integer getRespostaTamanhoHistorico() {
		Optional<List<Object>> respostaHistoricoOpt = Optional.ofNullable((List<Object>) this.get(CAMPO_RESPOSTA_HISTORICO));
		Integer result = respostaHistoricoOpt.map(r -> r.size()).orElse(null);
		return result;
	}

	@Override
	public Principal getUsuario() {
		return (Principal) this.get(ContextoBase.CAMPO_USUARIO);
	}

	@Override
	public void setCatalogo(final String valor) {
		this.put(ContextoBase.CAMPO_CATALOGO, valor);
	}

	@Override
	public void setComando(final String valor) {
		this.put(ContextoBase.CAMPO_COMANDO, valor);
	}

	@Override
	public void setRequisicao(final Object valor) {
		if (this.getRequisicao() != null) {
			throw new IllegalArgumentException("A requisição não pode ser redefinida!");
		}
		this.put(ContextoBase.CAMPO_REQUISICAO, valor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setResposta(final Object valor) {
		List<Object> respostaHistorico = (List<Object>) this.get(ContextoBase.CAMPO_RESPOSTA_HISTORICO);
		if (respostaHistorico == null) {
			respostaHistorico = new ArrayList<Object>();
			this.put(ContextoBase.CAMPO_RESPOSTA_HISTORICO, respostaHistorico);
		}
		respostaHistorico.add(valor);
		this.put(ContextoBase.CAMPO_RESPOSTA, valor);
	}

	@Override
	public void setUsuario(final Principal valor) {
		if (this.getUsuario() != null) {
			throw new IllegalArgumentException("A informação do usuário requerente não pode ser redefinido!");
		}
		this.put(ContextoBase.CAMPO_USUARIO, valor);
	}

}
