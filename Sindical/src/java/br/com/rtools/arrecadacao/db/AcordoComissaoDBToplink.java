package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.AcordoComissao;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class AcordoComissaoDBToplink extends DB implements AcordoComissaoDB {

    @Override
    public boolean insert(AcordoComissao acordoComissao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(acordoComissao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(AcordoComissao acordoComissao) {
        try {
            getEntityManager().merge(acordoComissao);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(AcordoComissao acordoComissao) {
        try {
            getEntityManager().remove(acordoComissao);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public AcordoComissao pesquisaCodigo(int id) {
        AcordoComissao result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("AcordoComissao.pesquisaID");
            qry.setParameter("pid", id);
            result = (AcordoComissao) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List<AcordoComissao> pesquisaData(String data) {
        List<AcordoComissao> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "select a"
                    + "  from AcordoComissao a"
                    + " where a.dtFechamento = :data");
            qry.setParameter("data", DataHoje.converte(data));
            result = qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select a from AcordoComissao a ");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public List<Date> pesquisaTodosFechamento() {
        try {
            Query qry = getEntityManager().createQuery(
                    "select a.dtFechamento "
                    + "  from AcordoComissao a"
                    + " group by a.dtFechamento"
                    + " order by a.dtFechamento desc");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean inserirAcordoComissao() {
        try {
            getEntityManager().getTransaction().begin();
            String textQuery =
                    " insert into arr_acordo_comissao (dt_inicio,dt_fechamento,nr_num_documento,id_conta_cobranca,id_acordo) "
                    + "            ( "
                    + "                select (select case when max(dt_fechamento) is null then (select min(dt_data) from arr_acordo) else (max(dt_fechamento) + 1) end from arr_acordo_comissao ) dt_inicio, "
                    + "                       '" + DataHoje.data() + "' as dt_fechamento, "
                    + "                       m.ds_documento BOLETO, "
                    + "                       bo.id_conta_cobranca, "
                    + "                       m.id_acordo "
                    + "                  from fin_movimento              as m "
                    + "                 inner join pes_pessoa            as p on p.id = m.id_pessoa "
                    + "                 inner join fin_baixa             as lb on lb.id = m.id_baixa "
                    + "                 inner join fin_servicos          as se on se.id = m.id_servicos "
                    + "                 inner join fin_boleto            as bo on bo.nr_ctr_boleto = m.nr_ctr_boleto "
                    + "                 inner join fin_conta_cobranca    as cc on cc.id = bo.id_conta_cobranca "
                    + "                 where m.id_tipo_servico = 4 "
                    + "                   and m.id_servicos <> 8 "
                    + "                   and lb.dt_baixa > '01/08/2010'"
                    + "                   and m.is_ativo = true "
                    + "                   and m.id_acordo > 0 "
                    + "                   and 'C'|| bo.id_conta_cobranca ||'D'||m.ds_documento not in (select 'C'|| "
                    + "                                                                                      id_conta_cobranca|| "
                    + "                                                                                      'D'|| "
                    + "                                                                                      nr_num_documento "
                    + "                                                                                 from arr_acordo_comissao)"
                    + ") ";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            qry.executeUpdate();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.getMessage();
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean estornarAcordoComissao(String data) {
        try {
            getEntityManager().getTransaction().begin();
            String textQuery =
                    " DELETE FROM arr_acordo_comissao WHERE dt_fechamento= \'" + data + "\' ";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            qry.executeUpdate();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.getMessage();
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public List<Vector> listaAcordoComissao(String data) {
        try {
            String textQuery =
                    "select DISTINCT ON (bo.id_conta_cobranca,m.ds_documento,p.ds_nome) "
                    + "                   p.ds_documento as CNPJ, "             // 0
                    + "                   p.ds_nome EMPRESA, "                  // 1
                    + "                   m.id_acordo ACORDO, "                 // 2
                    + "                   m.ds_documento BOLETO, "              // 3
                    + "                   se.ds_descricao  as CONTRIBUICAO, "   // 4    
                    + "                   lb.dt_importacao as IMPORTACAO, "     // 5
                    + "                   lb.dt_baixa   as RECEBTO, "           // 6
                    + "                   m.dt_vencimento  as VENCIMENTO, "     // 7
                    + "                   acc.dt_inicio    as DT_INICIO, "      // 8
                    + "                   m.nr_valor_baixa as VLRECEB, "        // 9
                    + "                   m.nr_taxa AS TAXA, "                  // 10
                    + "                   cc.nr_repasse AS REPASSE "            // 11
                    + "              from fin_movimento            as m "
                    + "             inner join pes_pessoa          as p   on p.id            = m.id_pessoa "
                    + "             inner join fin_baixa           as lb  on lb.id           = m.id_baixa "
                    + "             inner join fin_servicos        as se  on se.id           = m.id_servicos "
                    + "             inner join fin_boleto          as bo on bo.nr_ctr_boleto = m.nr_ctr_boleto "
                    + "             inner join fin_conta_cobranca  as cc  on cc.id           = bo.id_conta_cobranca "
                    + "             inner join arr_acordo_comissao as acc on bo.id_conta_cobranca=acc.id_conta_cobranca and m.ds_documento = acc.nr_num_documento and dt_fechamento = '" + data + "'"
                    + "            where m.id_tipo_servico = 4 "
                    + "              and m.id_servicos <> 8 and lb.dt_baixa > '01/08/2010' "
                    + "              and m.is_ativo = true "
                    + "              and m.id_acordo > 0 "
                    + "            order by p.ds_nome";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            return (Vector) qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }
}
