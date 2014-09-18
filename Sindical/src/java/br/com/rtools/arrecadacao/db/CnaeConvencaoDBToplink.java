package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.PatronalCnae;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CnaeConvencaoDBToplink extends DB implements CnaeConvencaoDB {

    @Override
    public CnaeConvencao pesquisaCnaeComConvencao(int idCnae) {
        try {
            Query qry = getEntityManager().createQuery("select cc from CnaeConvencao cc "
                    + " where cc.cnae.id = :id_Cnae "
                    + " order by cc.cnae.numero");
            qry.setParameter("id_Cnae", idCnae);
            return (CnaeConvencao) (qry.getSingleResult());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Convencao pesquisarCnaeConvencao(int idJuridica) {
        Convencao result;
        result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select cc.convencao "
                    + "  from Juridica pj, "
                    + "       CnaeConvencao cc	 "
                    + " where pj.id = :idJur    "
                    + "       and cc.cnae.id = pj.cnae.id");
            qry.setParameter("idJur", idJuridica);
            result = (Convencao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Convencao pesquisarCnaeConvencaoPorPessoa(int idPessoa) {
        Convencao result;
        result = null;
        try {
            Query qry = getEntityManager().createQuery(
                    "select cc.convencao "
                    + "  from Juridica      pj, "
                    + "       CnaeConvencao cc  "
                    + " where pj.pessoa.id = :id_jur    "
                    + "   and pj.cnae.id = cc.cnae.id ");
            qry.setParameter("id_jur", idPessoa);
            result = (Convencao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisarCnaeConvencaoPorConvencao(int id) {
        List result;
        result = null;
        try {
            Query qry = getEntityManager().createQuery("select cc from CnaeConvencao cc "
                    + " where cc.convencao.id = :id_Conv "
                    + " order by cc.cnae.numero");
            qry.setParameter("id_Conv", id);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<Juridica> pesquisarJuridicaPorCnae(int idCnae) {
        List<Juridica> result = new ArrayList<Juridica>();
        try {
            Query qry = getEntityManager().createQuery("select jur from Juridica jur "
                    + " where jur.cnae.id = :idCnae");
            qry.setParameter("idCnae", idCnae);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<Cnae> listaCnaePorConvencao(int id_convencao) {
        List<Cnae> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select c.cnae from CnaeConvencao c "
                    + " where c.convencao.id = " + id_convencao);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<PatronalCnae> listaCnaePorPatronal(int id_patronal) {
        List<PatronalCnae> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select pc from PatronalCnae pc "
                    + " where pc.patronal.id = " + id_patronal);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }
}
