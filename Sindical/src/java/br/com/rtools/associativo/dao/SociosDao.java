package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.lista.ListaSociosEmpresa;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.Query;

public class SociosDao extends DB {

    /**
     * Retorna o sócio somente se o mesmo estiver ativo e serviço pessoa ativo
     *
     * @param id Pessoa do serviço pessoa
     * @return
     */
    public Socios pesquisaTitularPorDependente(Integer id) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.pessoa.id = :id AND S.matriculaSocios.dtInativo IS NULL AND S.servicoPessoa.ativo = true");
            query.setParameter("id", id);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Socios) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Retorna o sócio somente se o mesmo estiver ativo e serviço pessoa ativo
     *
     * @param pessoa (id) Pessoa
     * @return
     */
    public List listaPorPessoa(Integer pessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.pessoa.id = :pessoa");
            query.setParameter("pessoa", pessoa);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    /**
     * Retorna o sócio somente se o mesmo estiver ativo e serviço pessoa ativo
     *
     * @param idServicoPessoa Serviço pessoa
     * @return
     */
    public Socios pesquisaSocioPorServicoPessoa(Integer idServicoPessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.id = :id");
            query.setParameter("id", idServicoPessoa);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (Socios) query.getSingleResult();
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Retorna o sócios por pessoa empresa
     *
     * @param idJuridica Serviço pessoa
     * @return
     */
    public List pesquisaSocioPorEmpresa(Integer idJuridica) {
        try {
            String queryString = ""
                    + "     SELECT S.nome           AS nome,                    " // 0 - NOME
                    + "            S.matricula      AS matricula,               " // 1 - MATRÍCULA
                    + "            S.categoria      As categoria,               " // 2 - CATEGORIA
                    + "            S.filiacao       As filiacao,                " // 3 - FILIAÇÃO
                    + "            P.admissao       AS admissao,                " // 4 - ADMISSÃO
                    + "            S.desconto_folha AS desconto_folha           " // 5 - DESCONTO FOLHA
                    + "       FROM soc_socios_vw AS S                           "
                    + " INNER JOIN pes_pessoa_vw AS P ON P.codigo = S.codsocio  "
                    + "      WHERE P.e_id_pessoa = ?                            ";
            Query query = getEntityManager().createNativeQuery(queryString);
            query.setParameter("1", idJuridica);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

}
