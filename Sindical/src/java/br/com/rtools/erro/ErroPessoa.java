package br.com.rtools.erro;

import br.com.rtools.pessoa.Pessoa;
import java.util.ArrayList;
import java.util.List;

public class ErroPessoa extends Erro {

    public ErroPessoa() {
        super();
    }

    @Override
    public boolean adicionarObjetoEmErro(int id, Object objeto) {
        Pessoa pessoa = null;
        try {
            pessoa = (Pessoa) objeto;
            return super.adicionarObjetoEmErro(id, objeto);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected String getConteudo(int id) {
        String result = "";
        try {
            List<Pessoa> lista = (ArrayList) super.hashObject.get(id);
            for (Pessoa pessoa : lista) {
                result += (pessoa.getDocumento() + " - " + pessoa.getNome()) + "\n";
            }
        } catch (Exception e) {
            result = "";
        }
        return result;
    }
}
