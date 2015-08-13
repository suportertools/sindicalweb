package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.ValidadeCartao;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ValidadeCartaoDao extends DB {

    /**
     * @param categoria_id Categoria
     * @param parentesco_id Parentesco
     * @return
     */
    public ValidadeCartao findByCategoriaParentesco(Integer categoria_id, Integer parentesco_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT VC FROM ValidadeCartao AS VC WHERE VC.categoria.id = :categoria_id AND VC.parentesco.id = :parentesco_id");
            query.setParameter("categoria_id", categoria_id);
            query.setParameter("parentesco_id", parentesco_id);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (ValidadeCartao) list.get(0);
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * @param categoria_id Categoria
     * @return
     */
    public List findAllByCategoria(Integer categoria_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT VC FROM ValidadeCartao AS VC WHERE VC.categoria.id = :categoria_id ORDER BY VC.parentesco.parentesco ASC");
            query.setParameter("categoria_id", categoria_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    /**
     * @param parentesco_id Parentesco
     * @return
     */
    public List findAllByParentesco(Integer parentesco_id) {
        try {
            Query query = getEntityManager().createQuery("SELECT VC FROM ValidadeCartao AS VC WHERE VC.parentesco.id = :parentesco_id ORDER BY VC.categoria.categoria ASC");
            query.setParameter("parentesco_id", parentesco_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }
}
