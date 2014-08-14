package br.com.rtools.utilitarios;

import java.util.List;

public interface SalvarAcumuladoDB {

    public boolean inserirObjeto(Object objeto);

    public boolean alterarObjeto(Object objeto);

    public boolean deletarObjeto(Object objeto);

    public void abrirTransacao();

    public void comitarTransacao();

    public void desfazerTransacao();

    /**
     * @deprecated Substituir pelo método pesquisaObjeto(int id, className) e
     * remover anotações @NamedQuery(name = "TipoEndereco.pesquisaID")
     * @param id
     * @param tipo
     * @return
     */
    public Object pesquisaCodigo(int id, String tipo);

    /**
     * @deprecated Substituir pelo método pesquisaObjeto(int id[], className) e
     * remover anotações @NamedQuery(name = "TipoEndereco.pesquisaID")
     * @param id
     * @param tipo
     * @return
     */
    public List pesquisaCodigo(int id[], String tipo);

    public Object pesquisaObjeto(int id, String className);

    public Object pesquisaObjeto(int id, String className, String field);

    /**
     *
     * @param id
     * @param className
     * @return object
     */
    public List pesquisaObjeto(int id[], String className);

    public List pesquisaObjeto(int id[], String className, String field);

    /**
     *
     * @param object
     * @return object
     */
    public Object find(Object object);

    public Object find(Object object, Object objectId);

    public Object find(String object, int objectId);
    
    public Object find(String object, Integer objectId);
    
//    public Object find(Object object, long objectId);

    public List listaObjeto(String className);

    /**
     *
     * @param className/Object
     * @param order (Default true)
     * @return
     */
    public List listaObjeto(String className, boolean order);

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

    public boolean executeQuery(String textQuery);
    
    public boolean executeQueryObject(String textQuery);

    public boolean executeQueryVetor(String textQuery);

    public boolean descricaoExiste(String descricao, String campo, String objeto);

    public List nativeQuery(String textQuery);

    public List nativeQuery(String textQuery, boolean singleResult);

    public List objectQuery(String textQuery);

    public List objectQuery(String textQuery, boolean singleResult);

    public void fecharTransacao();
    
    //public List<Object> selectTranslateNative(String table, String classe, String field, String and, String value_search);
}
