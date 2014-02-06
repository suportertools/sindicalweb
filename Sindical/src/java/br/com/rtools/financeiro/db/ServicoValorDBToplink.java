package br.com.rtools.financeiro.db;

import br.com.rtools.principal.DB;
import br.com.rtools.financeiro.ServicoValor;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class ServicoValorDBToplink extends DB implements ServicoValorDB {
    
    @Override
    public List pesquisaServicoValor(int idServico) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select p "
                    + "  from ServicoValor p "
                    + " where p.servicos.id = :pid");
            qry.setParameter("pid", idServico);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }
//
//    public ServicoValor pesquisaServicoValorPorIdade(int idServico, int idade) {
//        try{
//            Query qry = getEntityManager().createQuery(
//                    "select p " +
//                    "  from ServicoValor p " +
//                    " where p.servicos.id = :pid" +
//                    "   and :pIdade BETWEEN p.idadeIni and p.idadeFim"
//                    );
//            qry.setParameter("pid", idServico);
//            qry.setParameter("pIdade", idade);
//            return (ServicoValor) qry.getSingleResult();
//        }
//        catch(Exception e){
//            return null;
//        }
//    }

    @Override
    public ServicoValor pesquisaServicoValorPorPessoaFaixaEtaria(int idServico, int idPessoa) {
        ServicoValor servicoValor = new ServicoValor();
        try {
            String queryString = ""
                    + "        SELECT sv.id                                                                                "
                    + "          FROM fin_servicos s                                                                       "
                    + "   INNER JOIN fin_servico_valor sv ON (sv.id_servico = s.id)                                        "
                    + "   INNER JOIN pes_fisica fis ON (fis.id_pessoa = " + idPessoa + ")                                      "
                    + "          AND extract(YEAR FROM AGE(fis.dt_nascimento)) BETWEEN sv.nr_idade_ini AND sv.nr_idade_fim "
                    + "        WHERE s.id = " + idServico;
            Query qry = getEntityManager().createNativeQuery(queryString);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                List sungle = (List) qry.getSingleResult();
                int id = Integer.parseInt(sungle.get(0).toString());
                SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
                servicoValor = (ServicoValor) acumuladoDB.pesquisaCodigo(id, "ServicoValor");
                return servicoValor;
            }
            //return new Float[] {(new BigDecimal((Double) vector.get(0))).floatValue(),(new BigDecimal((Double) vector.get(1))).floatValue()};
        } catch (Exception e) {
            e.getMessage();
        }
        return new ServicoValor();
    }
    
    
    @Override
    public ServicoValor pesquisaServicoValorPorIdade(int idServico, int idade) {
        try {
            String queryString = ""
                    + "        SELECT sv.id                                                 "
                    + "          FROM fin_servicos s                                        "
                    + "   INNER JOIN fin_servico_valor sv ON (sv.id_servico = s.id)         "
                    + "        WHERE s.id = " + idServico                                   
                    + "          AND "+idade+" BETWEEN sv.nr_idade_ini AND sv.nr_idade_fim ";
            Query qry = getEntityManager().createNativeQuery(queryString);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                List sungle = (List) qry.getSingleResult();
                int id = Integer.parseInt(sungle.get(0).toString());
                SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
                ServicoValor sv = (ServicoValor) acumuladoDB.pesquisaCodigo(id, "ServicoValor");
                return sv;
            }
            //return new Float[] {(new BigDecimal((Double) vector.get(0))).floatValue(),(new BigDecimal((Double) vector.get(1))).floatValue()};
        } catch (NumberFormatException e) {
            e.getMessage();
        }
        return new ServicoValor();
    }    
    

    @Override
    public float pesquisaMaiorResponsavel(int idPessoa) {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    "            SELECT extract(year from age(dt_nascimento))           "
                    + "              FROM pes_fisica pf,                                  "
                    + "                   pes_pessoa p,                                   "
                    + "                   pes_pessoa_endereco pe                          "
                    + "             WHERE extract(year from age(dt_nascimento)) > 18      "
                    + "               AND pf.id_pessoa = p.id                             "
                    + "               AND pe.id_pessoa = p.id                             "
                    + "               AND p.id_tipo_documento = 1                         "
                    + "               AND pe.id_tipo_endereco = 3                         "
                    + "               AND p.id = " + idPessoa + "                         ");
            Vector vector = (Vector) qry.getSingleResult();
            return (new BigDecimal((Double) vector.get(0))).floatValue();
        } catch (Exception e) {
            e.getMessage();
        }
        return 0;
    }

}
