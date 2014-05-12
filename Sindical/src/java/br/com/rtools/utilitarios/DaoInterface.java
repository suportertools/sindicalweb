package br.com.rtools.utilitarios;

import java.util.List;

public interface DaoInterface {

    /**
     * <p>
     * <strong>Open Transaction</strong></p>
     *
     * @author Bruno
     */
    public void openTransaction();

    /**
     * <p>
     * <strong>Commit</strong></p>
     *
     * @author Bruno
     */
    public void commit();

    /**
     * <p>
     * <strong>Rollback</strong></p>
     *
     * @author Bruno
     */
    public void rollback();

    public void flush();

    /**
     * <p>
     * <strong>Active Session</strong></p>
     *
     * @author Bruno
     *
     * @return true or false
     */
    public boolean activeSession();

    /**
     * <p>
     * <strong>Save</strong></p>
     *
     * @param object
     *
     * @author Bruno
     *
     * @return true or false
     */
    public boolean save(final Object object);

    /**
     * <p>
     * <strong>Save transaction automatic</strong></p>
     *
     * @param object
     * @param transactionComplete
     * @author Bruno
     *
     * @return true or false
     */
    public boolean save(final Object object, boolean transactionComplete);

    /**
     * <p>
     * <strong>Update</strong></p>
     *
     * @param objeto
     * @author Bruno
     *
     * @return true or false
     */
    public boolean update(final Object objeto);

    /**
     * <p>
     * <strong>Updatetransaction automatic</strong></p>
     *
     * @param objeto
     * @param transactionComplete
     *
     * @author Bruno
     *
     * @return true or false
     */
    public boolean update(final Object objeto, boolean transactionComplete);

    /**
     * <p>
     * <strong>Delete</strong></p>
     *
     * @param object
     * @author Bruno
     *
     * @return true or false
     */
    public boolean delete(final Object object);

    /**
     * <p>
     * <strong>Delete automatic</strong></p>
     *
     * @param object
     * @param transactionComplete
     *
     * @author Bruno
     *
     * @return true or false
     */
    public boolean delete(final Object object, boolean transactionComplete);

    public Object rebind(Object object);

    public void refresh(Object object);

    /**
     * <p>
     * <strong>Find Object</strong></p>
     * <p>
     * <strong>Exemplo:</strong>User user = new User(1, "Paul"); find(user);</p>
     *
     * @param object (Nome do objeto String)
     *
     * @author Bruno
     *
     * @return Object
     */
    public Object find(final Object object);

    /**
     * <p>
     * <strong>Find Object</strong></p>
     * <p>
     * <strong>Exemplo:</strong>find("User" or new User(), objectId); </p>
     *
     * @param object (Nome do objeto String)
     * @param objectId (Id a ser pesquisado)
     *
     * @author Bruno
     *
     * @return Object
     */
    public Object find(Object object, final Object objectId);

    /**
     * <p>
     * <strong>Find Object</strong></p>
     * <p>
     * <strong>Exemplo:</strong>find("User", new int[]{1,2,3,4,5}); </p>
     *
     * @param id (Lista de ids)
     * @param className (Nome da classe)
     *
     * @author Bruno
     *
     * @return List
     */
    public List find(String className, int id[]);

    /**
     * <p>
     * <strong>Find Object</strong></p>
     * <p>
     * <strong>Exemplo:</strong>find("User", new int[]{1,2,3,4,5}, "id_people");
     * </p>
     *
     * @param id (Lista de ids)
     * @param className (Nome da classe)
     * @param field (Faz a consulta utilizando outro campo que não id como
     * parâmetro)
     *
     * @author Bruno
     *
     * @return List
     */
    public List find(String className, int id[], String field);

    /**
     * <p>
     * <strong>List</strong></p>
     * <p>
     * <strong>Exemplo:</strong> list(new User()).</p>
     *
     * @param className (Nome do objeto String)
     *
     * @author Bruno
     *
     * @return List
     */
    public List list(Object className);

    /**
     * <p>
     * <strong>List</strong></p>
     * <p>
     * <strong>Exemplo:</strong> list("User").</p>
     *
     * @param className (Nome do objeto String)
     *
     * @author Bruno
     *
     * @return List
     */
    public List list(String className);

    public List list(Object className, boolean order);

    /**
     * <p>
     * <strong>List</strong></p>
     * <p>
     * <strong>Exemplo:</strong> list("User", boolean (true or false)).</p>
     *
     * @param className (Nome do objeto String)
     * @param order [Se o resultado deve ser ordenado (Verificar se a namedQuery
     * esta na Classe/Entidade)]
     *
     * @author Bruno
     *
     * @return List
     */
    public List list(String className, boolean order);

    /**
     * <p>
     * <strong>List Query</strong></p>
     * <p>
     * <strong>Exemplo:</strong> E@NamedQuery(name = "Object.find", query =
     * "SELECT O FROM Object AS O WHERE O.id = :p1") Uso: listQuery(Object,
     * find) Exemplo 2 @NamedQuery(name = "Object.find") listQuery(Object,
     * find).</p>
     *
     * @param className (Nome do objeto)
     * @param find (Nome da NamedQuery dentro do objeto)
     *
     * @author Bruno
     *
     * @return List
     */
    public List listQuery(Object className, String find);

    /**
     * <p>
     * <strong>List Query</strong></p>
     * <p>
     * <strong>Exemplo:</strong> E@NamedQuery(name = "Object.find", query =
     * "SELECT O FROM Object AS O WHERE O.id = :p1") Uso: listQuery(Object,
     * find, {1}) Exemplo 2 @NamedQuery(name = "Object.find", query = "SELECT O
     * FROM Object AS O WHERE O.id = :p1 AND O.description = :p2") Uso:
     * listQuery(Object, find, {1, 'Feliz'}).</p>
     *
     * @param className (Nome do objeto)
     * @param find (Nome da NamedQuery dentro do objeto)
     * @param params (Cria se parâmetros organizados para realizar a consulta)
     *
     * @author Bruno
     *
     * @return List
     */
    public List listQuery(Object className, String find, Object[] params);

    /**
     * <p>
     * <strong>List Query</strong></p>
     * <p>
     * <strong>Exemplo:</strong> E@NamedQuery(name = "Object.find", query =
     * "SELECT O FROM Object AS O WHERE O.id = :p1") Uso: listQuery("Object",
     * find, {1}) Exemplo 2 @NamedQuery(name = "Object.find", query = "SELECT O
     * FROM Object AS O WHERE O.id = :p1 AND O.description = :p2") Uso:
     * listQuery(Object, find, {1, 'Feliz'}).</p>
     *
     * @param className (Nome do objeto)
     * @param find (Nome da NamedQuery dentro do objeto)
     * @param params (Cria se parâmetros organizados para realizar a consulta)
     *
     * @author Bruno
     *
     * @return List
     */
    public List listQuery(String className, String find, Object[] params);

    /**
     * <p>
     * <strong>Live List</strong></p>
     * <p>
     * <strong>Exemplos:</strong>Exemplo 1: liveList(SELECT U FROM User AS U);
     * Set nativeQuery = true. Caso não encontre nenhum resultado retorna uma
     * lista vazia; Se houver algum erro retorna lista vazia;</p>
     *
     * @param queryString
     *
     * @author Bruno
     *
     * @return List
     */
    public List liveList(String queryString);

    /**
     * <p>
     * <strong>Live List</strong></p>
     * <p>
     * <strong>Exemplos:</strong>Exemplo 1: liveList(SELECT U FROM User AS U);
     * Set nativeQuery = true Exemplo 2: liveList(select * from user as u,
     * true); Set maxResults = 5. Caso não encontre nenhum resultado retorna uma
     * lista vazia; Se houver algum erro retorna lista vazia;</p>
     *
     * @param queryString
     * @param nativeQuery
     *
     * @author Bruno
     *
     * @return List
     */
    public List liveList(String queryString, boolean nativeQuery);

    /**
     * <p>
     * <strong>Live List</strong></p>
     * <p>
     * <strong>Exemplos:</strong>Exemplo 1: liveList(SELECT U FROM User AS U);
     * Set nativeQuery = true Exemplo 2: liveList(select * from user as u,
     * true); Set maxResults = 5 Exemplo 3: liveList(SELECT U FROM User AS U,
     * true, 5) Caso não encontre nenhum resultado retorna uma lista vazia; Se
     * houver algum erro retorna lista vazia;</p>
     *
     * @param queryString
     * @param nativeQuery
     * @param maxResults
     *
     * @author Bruno
     *
     * @return List
     */
    public List liveList(String queryString, boolean nativeQuery, int maxResults);

    /**
     * <p>
     * <strong>Live Object</strong></p>
     * <p>
     * <strong>Exemplos:</strong>Exemplo 1: liveSingle(SELECT U FROM User AS U);
     * Set nativeQuery = true; Retorna somente um resultado, se houver mais de
     * um retornará null; Caso não encontre nenhum resultado retorna null;</p>
     *
     * @param queryString
     *
     * @author Bruno
     *
     * @return Object
     */
    public Object liveSingle(String queryString);

    /**
     * <p>
     * <strong>Live Object</strong></p>
     * <p>
     * <strong>Exemplos:</strong>Exemplo 1: liveSingle(SELECT U FROM User AS U);
     * Set nativeQuery = true Exemplo 2: liveSingle(select * from user as u,
     * true); Retorna somente um resultado, se houver mais de um retornará null;
     * Caso não encontre nenhum resultado retorna null;</p>
     *
     * @param queryString
     * @param nativeQuery
     *
     *
     * @author Bruno
     *
     * @return Object
     */
    public Object liveSingle(String queryString, boolean nativeQuery);
}
