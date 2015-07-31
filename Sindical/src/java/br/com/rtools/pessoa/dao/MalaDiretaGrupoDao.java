package br.com.rtools.pessoa.dao;

import br.com.rtools.principal.DB;
import javax.persistence.Query;

public class MalaDiretaGrupoDao extends DB {

    /**
     * Deleta o grupo e tudo vínculado a ele
     *
     * @param id Id do Grupo
     * @return
     */
    public Boolean delete(Integer id) {
        String queryString;
        Query query;
        try {
            getEntityManager().getTransaction().begin();
            queryString = "DELETE FROM pes_mala_direta WHERE id_grupo = " + id;
            query = getEntityManager().createNativeQuery(queryString);
            if (query.executeUpdate() != 0) {
                getEntityManager().getTransaction().rollback();
                return false;
            }
            queryString = "DELETE FROM pes_mala_direta_grupo WHERE id = " + id;
            query = getEntityManager().createNativeQuery(queryString);
            if (query.executeUpdate() != 0) {
                getEntityManager().getTransaction().rollback();
                return false;
            }
            getEntityManager().getTransaction().begin();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Inátiva o grupo e apaga todos vínculados a ele
     *
     * @param id Id do Grupo
     * @return
     */
    public Boolean inative(Integer id) {
        String queryString;
        Query query;
        try {
            getEntityManager().getTransaction().begin();
            queryString = "DELETE FROM pes_mala_direta WHERE id_grupo = " + id;
            query = getEntityManager().createNativeQuery(queryString);
            if (query.executeUpdate() != 0) {
                getEntityManager().getTransaction().rollback();
                return false;
            }
            queryString = "UPDATE pes_mala_direta_grupo SET is_ativo = false WHERE id = " + id;
            query = getEntityManager().createNativeQuery(queryString);
            if (query.executeUpdate() != 0) {
                getEntityManager().getTransaction().rollback();
                return false;
            }
            getEntityManager().getTransaction().begin();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
