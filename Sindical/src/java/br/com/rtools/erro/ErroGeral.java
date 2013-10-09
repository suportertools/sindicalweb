package br.com.rtools.erro;

import java.util.ArrayList;
import java.util.List;

public class ErroGeral extends Erro {

    public ErroGeral() {
        super();
    }

    @Override
    public boolean adicionarObjetoEmErro(int id, Object objeto) {
        String texto = null;
        try {
            texto = (String) objeto;
            return super.adicionarObjetoEmErro(id, objeto);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected String getConteudo(int id) {
        String result = "";
        try {
            List<String> lista = (ArrayList) super.hashObject.get(id);
            for (String texto : lista) {
                result += texto + "\n";
            }
        } catch (Exception e) {
            result = "";
        }
        return result;
    }
}
