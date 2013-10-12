package br.com.rtools.utilitarios;

import java.util.List;

public interface SalvarAcumuladoDB {

    public boolean inserirObjeto(Object objeto);

    public boolean alterarObjeto(Object objeto);

    public boolean deletarObjeto(Object objeto);

    public void abrirTransacao();

    public void comitarTransacao();

    public void desfazerTransacao();

    public Object pesquisaCodigo(int id, String tipo);

    public List listaObjeto(String tabela);
    
    /**
     * 
     * @param tabela/Object
     * @param order (Default true)
     * @return 
     */
    public List listaObjeto(String tabela, boolean order);
    
    /**
     * 
     * @param tabela
     * @param descricao
     * @return 
     */
    public List pesquisaObjetoPorDescricao(String tabela, String descricao);
            
    /**
     * 
     * @param tabela
     * @param descricao
     * @param tipoPesquisa (default = null, inicial = i, parcial = p)
     * @return 
     */
    public List pesquisaObjetoPorDescricao(String tabela, String descricao, String tipoPesquisa);

    public List listaObjetoGenericoOrdem(String tabela, String descricao);

    public Object pesquisaObjeto(int id, String tabela);

    public boolean inserirQuery(String textQuery);

    public boolean descricaoExiste(String descricao, String campo, String objeto);
}
