package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.DescontoEmpregado;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class DescontoEmpregadoDBToplink extends DB implements DescontoEmpregadoDB {

    @Override
    public boolean insert(DescontoEmpregado descontoEmpregado) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(descontoEmpregado);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(DescontoEmpregado descontoEmpregado) {
        try {
            getEntityManager().merge(descontoEmpregado);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(DescontoEmpregado descontoEmpregado) {
        try {
            getEntityManager().remove(descontoEmpregado);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public DescontoEmpregado pesquisaCodigo(int id) {
        DescontoEmpregado result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("DescontoEmpregado.pesquisaID");
            qry.setParameter("pid", id);
            result = (DescontoEmpregado) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        List vetor;
        List<DescontoEmpregado> listDesc = new ArrayList();
        try {
            Query qry = getEntityManager().createNativeQuery(" select de.id "
                    + "   from arr_Desconto_Empregado de "
                    + "  inner join fin_servicos s on (s.id = de.id_servicos) "
                    + "  inner join arr_grupo_cidade g on (g.id = de.id_grupo_cidade) "
                    + "  inner join arr_convencao c on (c.id = de.id_convencao) "
                    + "  order by s.ds_descricao, "
                    + "         c.ds_descricao, "
                    + "         g.ds_descricao, "
                    + "         substring(de.ds_ref_final,4,8) || substring(de.ds_ref_final,0,3)");
            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                for (int i = 0; i < vetor.size(); i++) {
                    listDesc.add(pesquisaCodigo((Integer) ((Vector) vetor.get(i)).get(0)));
                }
            }

            return listDesc;
        } catch (EJBQLException e) {
            return null;
        }
    }

    @Override
    public DescontoEmpregado pesquisaEntreReferencias(String referencia, int idServico, int idPessoa) {
        String ref = referencia.substring(3, 7) + referencia.substring(0, 2);
        try {
            Query qry = getEntityManager().createQuery(
                    "select de "
                    + "  from DescontoEmpregado de, "
                    + "       Juridica j,"
                    + "       CnaeConvencao cc,"
                    + "       ConvencaoCidade ci,"
                    + "       GrupoCidades gs,"
                    + "       PessoaEndereco pe"
                    + " where de.servicos.id = :s"
                    + "   and j.pessoa.id = :p"
                    + "   and de.convencao.id = ci.convencao.id"
                    + "   and de.grupoCidade.id = ci.grupoCidade.id"
                    + "   and gs.grupoCidade.id = ci.grupoCidade.id"
                    + "   and cc.convencao.id = ci.convencao.id"
                    + "   and cc.cnae.id = j.cnae.id"
                    + "   and pe.pessoa.id = j.pessoa.id"
                    + "   and pe.tipoEndereco.id = 5"
                    + "   and pe.endereco.cidade.id = gs.cidade.id"
                    + "   and :ref BETWEEN CONCAT( substring(de.referenciaInicial,4,8) , substring(de.referenciaInicial,0,3) )"
                    + "               AND CONCAT( substring(de.referenciaFinal,4,8) , substring(de.referenciaFinal,0,3) )");
            qry.setParameter("s", idServico);
            qry.setParameter("p", idPessoa);
            qry.setParameter("ref", ref);
//            Query qry = getEntityManager().createQuery(
//                    "select de " +
//                    "  from DescontoEmpregado de, " +
//                    "       Juridica j," +
//                    "       CnaeConvencao cc," +
//                    "       ConvencaoCidade ci," +
//                    "       GrupoCidades gs," +
//                    "       PessoaEndereco pe" +
//                    " where de.servicos.id = :s" +
//                    "   and j.pessoa.id = :p" +
//                    "   and de.convencao.id = ci.convencao.id" +
//                    "   and de.grupoCidade.id = ci.grupoCidade.id" +
//                    "   and gs.grupoCidade.id = ci.grupoCidade.id" +
//                    "   and cc.convencao.id = ci.convencao.id" +
//                    "   and cc.cnae.id = j.cnae.id" +
//                    "   and pe.pessoa.id = j.pessoa.id" +
//                    "   and pe.tipoEndereco.id = 5" +
//                    "   and pe.endereco.cidade.id = gs.cidade.id" +
//                    "   and :ref BETWEEN CONCAT( substring(de.referenciaInicial,4,8) , substring(de.referenciaInicial,0,3) )" +
//                    "               AND CONCAT( substring(de.referenciaFinal,4,8) , substring(de.referenciaFinal,0,3) )");
//            qry.setParameter("s", idServico);
//            qry.setParameter("p", idPessoa);
//            qry.setParameter("ref", ref);
            return (DescontoEmpregado) qry.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
}
