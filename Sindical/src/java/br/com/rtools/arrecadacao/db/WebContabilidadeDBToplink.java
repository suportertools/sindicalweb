package br.com.rtools.arrecadacao.db;

import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class WebContabilidadeDBToplink extends DB implements WebContabilidadeDB {

    @Override
    public List listaEmpresasPertContabilidade(int idContabilidade) {
        List result;
        List listaC = new ArrayList();
        Query qry = null;
        JuridicaDB db = new JuridicaDBToplink();
        String textQuery = "select j.id "
                + "  from pes_juridica j, pes_pessoa p "
                + " where j.id_pessoa = p.id "
                + "   and j.id_contabilidade = " + idContabilidade
                + "   and j.id in ( select id_juridica from arr_contribuintes_vw where dt_inativacao is null) "
                + " order by p.ds_nome";
        try {
            qry = getEntityManager().createNativeQuery(textQuery);
            result = qry.getResultList();
            if (!result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    listaC.add(db.pesquisaCodigo((Integer) ((Vector) result.get(i)).get(0)));
                }
            }
        } catch (Exception e) {
            listaC = new ArrayList();
        }
        return listaC;
    }

    @Override
    public List pesquisaMovParaWebContabilidade(int id_pessoa) {
        List result;
        Query qry = null;
        String textQuery;
        textQuery = "select m.ds_documento Boleto, "
                + "       se.id as id_servico, "
                + "       tp.id as id_tipo_servico, "
                + "       m.ds_referencia as Referencia,"
                + "       m.dt_vencimento as Vencimento, "
                + "       func_valor_folha(m.id) as Valor_Mov,"
                + "       f.nr_valor as Valor_Folha,"
                + "       func_multa(m.id) as Multa,"
                + "       func_juros(m.id) as Juros,"
                + "       func_correcao(m.id) as Correcao,"
                + "       null as Desconto,"
                + "       func_valor_folha(m.id) + func_multa(m.id) + func_juros(m.id) + func_correcao(m.id) as Valor_calculado,"
                + "       func_intervalo_meses(CURRENT_DATE,dt_vencimento) as Meses_em_Atraso,"
                + "       CURRENT_DATE-dt_vencimento as Dias_em_atraso,"
                + "       i.ds_descricao indice,"
                + "       m.id as id"
                + "  from fin_movimento as m"
                + " inner join fin_servicos as se on se.id=m.id_servicos"
                + " inner join fin_tipo_servico as tp on tp.id=m.id_tipo_servico"
                + " inner join pes_juridica as j on j.id_pessoa=m.id_pessoa"
                + "  left join arr_faturamento_folha_empresa as f on f.id_juridica=j.id and f.ds_referencia=m.ds_referencia and f.id_tipo_servico=m.id_tipo_servico"
                + "  left join fin_correcao as cr on cr.id_servicos=m.id_servicos and "
                + " (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and "
                + " (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))"
                + "  left join fin_indice as i on i.id=cr.id_indice"
                + " where m.id_pessoa = " + id_pessoa
                + "   and m.is_ativo is true "
                + "   and m.id_baixa is null "
                + " order by m.dt_vencimento";
        try {
            qry = getEntityManager().createNativeQuery(textQuery);
            result = qry.getResultList();
        } catch (Exception e) {
            result = new Vector();
        }
        return result;
    }
//    public List pesquisaMovParaWebContabilidade(int id_pessoa){
//        List result;
//        Query qry = null;
//        String textQuery;
//        textQuery = "select m from Movimento m " +
//                    " where m.servicos.id in (select sr.servicos.id from ServicoRotina sr where sr.rotina.id = 4) " +
//                    "   and m.pessoa.id = :idPes" +
//                    "   and m.ativo = true " +
//                    "   and m.baixa is null";
//        try{
//            qry = getEntityManager().createQuery(textQuery);
//            qry.setParameter("idPes", id_pessoa);
//            result = qry.getResultList();
//        }catch(Exception e){
//            result = new Vector();
//        }
//        return result;
//    }

    @Override
    public List pesquisaMovParaWebContabilidadeComRef(int id_pessoa, String referencia) {
        List result;
        Query qry = null;
        String textQuery;
        textQuery = "select m.ds_documento Boleto, "
                + "       se.id as id_servico, "
                + "       tp.id as id_tipo_servico, "
                + "       m.ds_referencia as Referencia,"
                + "       m.dt_vencimento as Vencimento, "
                + "       func_valor_folha(m.id) as Valor_Mov,"
                + "       f.nr_valor as Valor_Folha,"
                + "       func_multa(m.id) as Multa,"
                + "       func_juros(m.id) as Juros,"
                + "       func_correcao(m.id) as Correcao,"
                + "       null as Desconto,"
                + "       func_valor_folha(m.id) + func_multa(m.id) + func_juros(m.id) + func_correcao(m.id) as Valor_calculado,"
                + "       func_intervalo_meses(CURRENT_DATE,dt_vencimento) as Meses_em_Atraso,"
                + "       CURRENT_DATE-dt_vencimento as Dias_em_atraso,"
                + "       i.ds_descricao indice,"
                + "       m.id as id"
                + "  from fin_movimento as m"
                + " inner join fin_servicos as se on se.id=m.id_servicos"
                + " inner join fin_tipo_servico as tp on tp.id=m.id_tipo_servico"
                + " inner join pes_juridica as j on j.id_pessoa=m.id_pessoa"
                + "  left join arr_faturamento_folha_empresa as f on f.id_juridica=j.id and f.ds_referencia=m.ds_referencia and f.id_tipo_servico=m.id_tipo_servico"
                + "  left join fin_correcao as cr on cr.id_servicos=m.id_servicos and "
                + " (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and "
                + " (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))"
                + "  left join fin_indice as i on i.id=cr.id_indice"
                + " where m.id_pessoa = " + id_pessoa
                + "   and m.is_ativo is true "
                + "   and m.id_baixa is null "
                + "   and m.ds_referencia = '" + referencia + "'"
                + " order by m.dt_vencimento";
        try {
            qry = getEntityManager().createNativeQuery(textQuery);
            result = qry.getResultList();
        } catch (Exception e) {
            result = new Vector();
        }
        return result;
    }
//    public List pesquisaMovParaWebContabilidadeComRef(int id_pessoa, String referencia){
//        List result;
//        Query qry = null;
//        String textQuery;
//        textQuery = "select m from Movimento m " +
//                    " where m.servicos.id in (select sr.servicos.id from ServicoRotina sr where sr.rotina.id = 4) " +
//                    "   and m.pessoa.id = :idPes" +
//                    "   and m.referencia = :ref" +
//                    "   and m.ativo = true " +
//                    "   and m.baixa is null";
//        try{
//            qry = getEntityManager().createQuery(textQuery);
//            qry.setParameter("idPes", id_pessoa);
//            qry.setParameter("ref", referencia);
//            result = qry.getResultList();
//        }catch(Exception e){
//            result = new Vector();
//        }
//        return result;
//    }
}