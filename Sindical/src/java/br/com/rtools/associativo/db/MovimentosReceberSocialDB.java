package br.com.rtools.associativo.db;

import java.util.List;

public interface MovimentosReceberSocialDB {
    public List pesquisaListaMovimentos(String ids, String por_status);
}
