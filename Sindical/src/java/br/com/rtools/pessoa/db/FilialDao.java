package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Registro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FilialDao extends DB implements FilialDB {

    @Override
    public Registro pesquisaRegistroPorFilial(int id) {
        Registro result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select r "
                    + "  from Registro r"
                    + " where r.filial.id = :pid");
            qry.setParameter("pid", id);
            result = (Registro) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaFilialExiste(int idFilial) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select fil.filial.pessoa.nome"
                    + "  from Filial fil "
                    + " where fil.filial.id = :filial ");
            qry.setParameter("filial", idFilial);
            return qry.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaRazao(int idMatriz) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select fil.matriz.pessoa.nome"
                    + "  from Filial fil "
                    + " where fil.matriz.id = :matriz ");
            qry.setParameter("matriz", idMatriz);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaJuridicaFilial(int idMatriz) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select fili"
                    + "  from Filial fili"
                    + " where fili.matriz.id = :matriz"
                    + "   and fili.matriz.id <> fili.filial.id");
            qry.setParameter("matriz", idMatriz);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Filial pesquisaFilialPertencente(int idMatriz, int idFilial) {
        Filial result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select fili"
                    + "  from Filial fili"
                    + " where fili.matriz.id = :matriz"
                    + "   and fili.filial.id = :filial");
            qry.setParameter("matriz", idMatriz);
            qry.setParameter("filial", idFilial);
            result = (Filial) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaFilial(String desc, String por, String como, int idmatriz) {
        List result = null;
        if (por.equals("todos")) {
            try {
                Query qry = getEntityManager().createQuery("select jur from Juridica jur");
                result = qry.getResultList();
            } catch (Exception e) {
                result = null;
            }
        }

        if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CNPJ"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento like :desc and juri.pessoa.tipoDocumento.id = 2");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CEI"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento like :desc and juri.pessoa.tipoDocumento.id = 3");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CPF"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento like :desc and juri.pessoa.tipoDocumento.id = 1");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("nome"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.nome like :desc");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
        } else if (como.equals("D")) {
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CNPJ"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento = :desc and juri.pessoa.tipoDocumento.id = 2");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CEI"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento = :desc and juri.pessoa.tipoDocumento.id = 3");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("CPF"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.documento = :desc and juri.pessoa.tipoDocumento.id = 1");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
            if (!(por.equals("todos")) && (desc != null) && (por.equals("nome"))) {
                try {
                    Query qry = getEntityManager().createQuery(
                            "select juri "
                            + "  from Juridica juri"
                            + " where juri.pessoa.nome = :desc");
                    qry.setParameter("desc", desc);
                    result = qry.getResultList();
                } catch (Exception e) {
                    result = null;
                }
            }
        }
        return result;
    }

    @Override
    public List pesquisaPessoaConvencaoGCidade(int idPessoa, int idConvencao, int idGCidade) {
        List result = null;
        try {
            String queryString = ""
                    + "SELECT PC"
                    + "  FROM PC AS PatronalConvencao"
                    + " WHERE PC.patronal.pessoa.id = :pessoa"
                    + "   AND PC.convencao.id = :convencao"
                    + "   AND PC.grupoCidade.id = :grupoCidade";
            Query qry = getEntityManager().createQuery(queryString);
            qry.setParameter("pessoa", idPessoa);
            qry.setParameter("convencao", idConvencao);
            qry.setParameter("grupoCidade", idGCidade);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaPessoaPatronal(String desc, String por, String como) {
        if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
        } else {
            desc = "%" + desc.toLowerCase().toUpperCase() + "%";
        }
        String textQuery = "SELECT patro FROM Patronal AS PATRO WHERE UPPER(PATRO.pessoa." + por + ") LIKE :desc ORDER BY PATRO.pessoa.nome ASC";
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if (!desc.equals("%%") && !desc.equals("%")) {
                if (por.equals("documento")) {
                    qry.setParameter("desc", desc);
                } else {
                    qry.setParameter("desc", desc);
                }
            }
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    /**
     * Nome da tabela onde esta a lista de filiais Ex:
     * findByTabela('matr_escola');
     *
     * @param table
     * @return Todas as filias da tabela específicada
     */
    public List findByTabela(String table) {
        try {
            String queryString
                    = "     SELECT F.* FROM pes_filial AS F                     \n"
                    + " INNER JOIN pes_juridica AS J ON J.id = F.id_filial      \n"
                    + " INNER JOIN pes_pessoa AS P ON P.id = J.id_pessoa        \n"
                    + "      WHERE F.id IN (                                    \n"
                    + "	           SELECT T.id_filial                           \n"
                    + "              FROM " + table + " AS T                    \n"
                    + "          GROUP BY T.id_filial                           \n"
                    + ")                                                        \n"
                    + " ORDER BY P.ds_nome ";
            Query query = getEntityManager().createNativeQuery(queryString, Filial.class);
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
