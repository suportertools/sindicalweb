package br.com.rtools.erro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Erro {

    protected Map hashObject;
    protected Map hashMensagem;

    public Erro() {
        hashObject = new HashMap();
        hashMensagem = new HashMap();
    }

    public boolean criarErro(int id, String mensagem) {
        boolean result = false;
        if (this.hashMensagem.get(id) == null) {
            this.hashMensagem.put(id, mensagem);
            this.hashObject.put(id, new ArrayList());
            result = true;
        }
        return result;
    }

    public boolean adicionarObjetoEmErro(int id, Object objeto) {
        boolean result = false;
        Object object = this.hashObject.get(id);
        List lista = null;
        if (object != null) {
            lista = (ArrayList) object;
            result = lista.add(objeto);
        }
        return result;
    }

    public String capiturarErro(int id) {
        Object object = this.hashMensagem.get(id);
        String erro = null;
        if (object != null) {
            erro = (String) object;
            erro += ", \n" + this.getConteudo(id);
        }
        return erro;
    }

    public boolean is_ExisteErro(int id) {
        Object object = this.hashObject.get(id);
        List lista = null;
        if (object != null) {
            lista = (ArrayList) object;
            if (lista.isEmpty()) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    protected abstract String getConteudo(int id);
}
