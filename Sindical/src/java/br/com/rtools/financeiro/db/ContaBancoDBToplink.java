package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Banco;
import br.com.rtools.financeiro.ContaBanco;
import br.com.rtools.principal.DB;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class ContaBancoDBToplink extends DB implements ContaBancoDB {

    @Override
    public boolean insert(ContaBanco contaBanco) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(contaBanco);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(ContaBanco contaBanco) {
        try {
            getEntityManager().merge(contaBanco);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(ContaBanco contaBanco) {
        try {
            getEntityManager().remove(contaBanco);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ContaBanco pesquisaCodigo(int id) {
        ContaBanco result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("ContaBanco.pesquisaID");
            qry.setParameter("pid", id);
            result = (ContaBanco) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select cb from ContaBanco cb order by cb.banco.banco");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaTodosBancos() {
        try {
            Query qry = getEntityManager().createQuery("select b from Banco b ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Banco pesquisaBancoNumero(String num) {
        String query = "select b from Banco b where b.numero like '%" + num + "%' ";
        try {
            Query qry = getEntityManager().createQuery(query);
            return (Banco) (qry.getSingleResult());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Banco pesquisaBancoID(int id) {
        String query = "select b from Banco b where b.id = " + id;
        try {
            Query qry = getEntityManager().createQuery(query);
            return (Banco) (qry.getSingleResult());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaContaBanco(String desc, String por, String como) {
        List lista = new Vector<Object>();
        String textQuery = null;
        if (como.equals("T")) {
            textQuery = "select cb from ContaBanco cb";
        } else if (como.equals("P")) {
            desc = "%" + desc.toLowerCase().toUpperCase() + "%";
            if (por.equals("banco")) {
                textQuery = "select cb from ContaBanco cb where UPPER(cb.banco." + por + ") like :desc";
            } else {
                textQuery = "select cb from ContaBanco cb where UPPER(cb." + por + ") like :desc";
            }

        } else if (como.equals("I")) {
            desc = desc.toLowerCase().toUpperCase() + "%";
            if (por.equals("banco")) {
                textQuery = "select cb from ContaBanco cb where UPPER(cb.banco." + por + ") like :desc";
            } else {
                textQuery = "select cb from ContaBanco cb where UPPER(cb." + por + ") like :desc";
            }
        }
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if ((desc != null) && (!(como.equals("T")))) {
                qry.setParameter("desc", desc);
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = new Vector<Object>();
        }
        return lista;
    }

    @Override
    public List pesquisaPlano5Conta() {
        String query = "";
        query = "select pl5"
                + "  from Plano5 pl5 "
                + " where pl5.plano4.id in (select cr.plano4.id"
                + "                           from ContaRotina cr "
                + "                          where cr.rotina.id = 2 "
                + "                            and (cr.pagRec is null or cr.pagRec = '')"
                + "                        ) "
                + "   and pl5.contaBanco is null ";
        try {
            Query qry = getEntityManager().createQuery(query);
            return (List) (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaPlano5ContaComID(int id) {
        String query = "";
        query = "select pl5"
                + "  from Plano5 pl5, "
                + "       ContaBanco cb, "
                + "       Banco b"
                + " where pl5.contaBanco.id = cb.id "
                + "   and cb.banco.id = b.id "
                + "   and pl5.contaBanco.id = " + id;
        try {
            Query qry = getEntityManager().createQuery(query);
            return (List) (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ContaBanco idContaBanco(ContaBanco des_contaBanco) {
        ContaBanco result = null;
        try {
            Query qry = getEntityManager().createQuery("select con from ContaBanco con where con.conta = :d_contaBanco");
            qry.setParameter("d_contaBanco", des_contaBanco.getConta());
            result = (ContaBanco) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }
}