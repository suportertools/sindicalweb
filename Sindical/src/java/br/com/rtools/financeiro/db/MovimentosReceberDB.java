package br.com.rtools.financeiro.db;

import java.util.List;
import javax.persistence.EntityManager;

public interface MovimentosReceberDB {
    public List pesquisaListaMovimentosDesconto(int id_juridica, float desconto, float total);
    public List pesquisaListaMovimentos(int id_juridica);
}
