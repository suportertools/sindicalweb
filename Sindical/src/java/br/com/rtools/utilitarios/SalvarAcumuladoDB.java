package br.com.rtools.utilitarios;

import java.util.List;

public interface SalvarAcumuladoDB {
    public boolean inserirObjeto(Object objeto);
    public boolean alterarObjeto(Object objeto);
    public boolean deletarObjeto(Object objeto);
    public void abrirTransacao();
    public void comitarTransacao();
    public void desfazerTransacao();
    public Object pesquisaCodigo(int id,String tipo);
    public List listaObjeto(String tabela);
    public List listaObjetoGenericoOrdem(String tabela);
    public Object pesquisaObjeto(int id, String tabela);
    public List listaObjetoGenericoOrdemDesc(String tabela, String descricao);
    public boolean inserirQuery(String textQuery);
    public boolean descricaoExiste(String descricao, String campo, String objeto);
}
