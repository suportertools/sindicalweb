package br.com.rtools.associativo.db;

import br.com.rtools.financeiro.FormaPagamento;
import java.util.List;

public interface MovimentosReceberSocialDB {
    public List pesquisaListaMovimentos(String ids, String por_status);
    public List dadosSocio(int id_lote);
}
