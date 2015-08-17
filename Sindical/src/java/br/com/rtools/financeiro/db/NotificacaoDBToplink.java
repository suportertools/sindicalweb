package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Cobranca;
import br.com.rtools.financeiro.CobrancaEnvio;
import br.com.rtools.financeiro.CobrancaLote;
import br.com.rtools.financeiro.CobrancaTipo;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class NotificacaoDBToplink extends DB implements NotificacaoDB {
    
    @Override
    public Object[] listaParaNotificacao(int id_lote, String data, String id_empresa, String id_contabil, String id_cidade, boolean comContabil, boolean semContabil) {
        Object[] obj = new Object[2];
        List<Vector> result = null;
        try {
            String filtro_empresa = "", filtro_contabil = "", filtro_cidade = "", filtro_lote = "", filtro_com_sem = "";
            if (!id_empresa.isEmpty()) {
                filtro_empresa = " and c.id_juridica in (" + id_empresa + ") ";
            }

            if (!id_contabil.isEmpty()) {
                filtro_contabil = " and c.id_contabilidade in (" + id_contabil + ") ";
            }

            if (!id_cidade.isEmpty()) {
                filtro_cidade = " and en.id_cidade in (" + id_cidade + ") ";
            }

            if (id_lote != -1) {
                filtro_lote = " and fc.id_lote = " + id_lote;
            }

            if (comContabil) {
                filtro_com_sem = " and c.id_contabilidade is not null ";
            }

            if (semContabil) {
                filtro_com_sem = " and c.id_contabilidade is null ";
            }

            String text_select = "select c.id_pessoa, "
                    + "       c.ds_nome, "
                    + "       count(*) as movimentos, "
                    + "       fc.id_lote as lote_cobranca, "
                    + "       ce.id_lote as lote_envio, "
                    + "       ce.dt_emissao";

            String text_from = "  from arr_contribuintes_vw as c  "
                    + "  left join pes_juridica as je on je.id = c.id_contabilidade "
                    + "  left join pes_pessoa as pe on pe.id = je.id_pessoa "
                    + " inner join fin_movimento as m on m.id_pessoa = c.id_pessoa "
                    + "  left join pes_pessoa_endereco as pee on pee.id_pessoa = c.id_pessoa "
                    + "  left join end_endereco as en on en.id = pee.id_endereco "
                    + "  left join fin_cobranca       as fc on fc.id_movimento = m.id  "
                    + "  left join fin_cobranca_envio as ce on ce.id_lote = fc.id_lote "
                    + " where c.dt_inativacao is null    "
                    + "   and m.is_ativo = true          "
                    + "   and m.id_baixa is null         "
                    + "   and m.dt_vencimento < '" + data + "' "
                    + "   and pee.id_tipo_endereco = 5 "
                    + filtro_empresa + filtro_contabil + filtro_lote + filtro_cidade + filtro_com_sem;

            String text_group_by = " group by c.id_pessoa, c.ds_nome, fc.id_lote, ce.id_lote, ce.dt_emissao ";
            String text_order_by = " order by c.ds_nome, c.id_pessoa ";

            Query qry = getEntityManager().createNativeQuery(text_select + text_from + text_group_by + text_order_by);
            result = qry.getResultList();

            obj[0] = text_from;
            obj[1] = result;
        } catch (Exception e) {
        }
        return obj;
    }

    @Override
    public List listaNotificado(int id_movimento) {
        List<Vector> result = null;
        try {
            String textQry = "select fc.id, cl.dt_emissao from fin_cobranca as fc "
                    + " inner join fin_cobranca_lote as cl on cl.id = fc.id_lote "
                    + " where fc.id_movimento = " + id_movimento;
            Query qry = getEntityManager().createNativeQuery(textQry);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<CobrancaLote> listaCobrancaLote() {
        try {
            Query qry = getEntityManager().createQuery(
                    "select cl "
                    + "  from CobrancaLote cl order by cl.dtEmissao desc, cl.hora desc");
            qry.setMaxResults(15);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List<CobrancaTipo> listaCobrancaTipoEnvio() {
        try {
            Query qry = getEntityManager().createQuery(
                    "select ct "
                    + "  from CobrancaTipo ct");
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List<Cobranca> listaCobranca(int id_lote_cobranca) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select c "
                    + "  from Cobranca c where c.lote.id = " + id_lote_cobranca);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public CobrancaLote pesquisaCobrancaLote(int id_usuario, Date dataEmissao) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select cl "
                    + "  from CobrancaLote cl where cl.usuario.id = " + id_usuario + " and cl.dtEmissao = :data");
            qry.setParameter("data", dataEmissao);
            return (CobrancaLote) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List listaNotificacaoEnvio(int tipo_envio, int id_lote) {
        try {
            String textQry =
                    " select "
                    + "       '' as sinLogo, "
                    + "       sind.jurSite as sindSite, "
                    + "       sind.jurNome        as sinnome, "
                    + "       sind.jurEndereco    as sinendereco, "
                    + "       sind.jurLogradouro  as sinlogradouro, "
                    + "       sind.jurNumero      as sinnumero, "
                    + "       sind.jurComplemento as sincomplemento, "
                    + "       sind.jurBairro      as sinbairro, "
                    + "       substring(sind.jurCep,1,5)||'-'||substring(sind.jurCep,6,3)  as sincep, "
                    + "       sind.jurCidade      as sincidade, "
                    + "       sind.jurUf          as sinuF, "
                    + "       sind.jurDocumento   as sindocumento, "
                    + "       pj.escNome, "
                    + "       pj.escTelefone, "
                    + "       pj.escLogradouro, "
                    + "       pj.escEndereco, "
                    + "       pj.escNumero, "
                    + "       pj.escComplemento, "
                    + "       pj.escBairro, "
                    + "       substring(pj.escCep,1,5)||'-'||substring(pj.escCep,6,3) as escCep,"
                    + "       pj.escCidade, "
                    + "       pj.escUf, "
                    + "       pj.jurNome, "
                    + "       pj.jurDocumento, "
                    + "       pj.jurTelefone, "
                    + "       pj.jurcidade, "
                    + "       pj.juruf, "
                    + "       pj.jurcep, "
                    + "       pj.jurlogradouro, "
                    + "       pj.jurendereco, "
                    + "       pj.jurnumero, "
                    + "       pj.jurcomplemento, "
                    + "       pj.jurbairro, "
                    + "       se.ds_descricao    as movServico, "
                    + "       ts.ds_descricao    as movTipoServico, "
                    + "       m.ds_referencia    as movReferencia, "
                    + "       m.ds_documento     as movNumeroBoleto, "
                    + "       l.ds_mensagem, "
                    + "       pj.escid, "
                    + "       pj.id_pessoa "
                    + "  from  pes_juridica_vw      as pj "
                    + " inner join fin_movimento          as m    on m.id_pessoa    = pj.id_pessoa "
                    + " inner join fin_servicos           as se   on se.id          = m.id_servicos "
                    + " inner join fin_tipo_servico       as ts   on ts.id          = m.id_tipo_servico "
                    
                    + " inner join pes_filial as fi on fi.id = (select id_filial from conf_arrecadacao) "
                    + " inner join pes_juridica as ju on ju.id = fi.id_filial"
                    + " inner join pes_juridica_vw as sind on sind.id_pessoa = ju.id_pessoa "
                    
                    + " inner join pes_juridica           as j    on j.id_pessoa    = pj.id_pessoa "
                    + " inner join arr_contribuintes_vw   as co   on co.id_juridica = j.id "
                    + " inner join fin_cobranca           as c    on c.id_movimento = m.id "
                    + " inner join fin_cobranca_lote      as l    on l.id           = c.id_lote "
                    + " where m.id_baixa is null and is_ativo = true and l.id = " + id_lote;

            // 1 "ESCRITÓRIO"
             if (tipo_envio == 1) {
                 textQry += " and pj.escNome is not null "
                        + " order by pj.escNome,pj.escid, pj.jurNome, pj.jurid, substring(m.ds_referencia,4,4) || substring(m.ds_referencia,1,2) ";
                // 2 "EMPRESA COM ESCRITÓRIO"
            } else if (tipo_envio == 2) {
                textQry += " and pj.escNome is not null "
                        + " order by pj.jurNome, pj.jurid, substring(m.ds_referencia,4,4) || substring(m.ds_referencia,1,2) "; 
                // 3 "EMPRESA SEM ESCRITÓRIO"    
            } else if (tipo_envio == 3) {
                textQry += " and pj.escNome is null "
                        + " order by pj.jurNome, pj.jurid, substring(m.ds_referencia,4,4) || substring(m.ds_referencia,1,2) ";
                // 4 "EMAIL PARA OS ESCRITÓRIO"    AGRUPAR POR pj.escid -- id_escritorio
            } else if (tipo_envio == 4) {
                textQry += " and pj.escNome is not null "
                        + " order by pj.escNome, pj.escid, pj.jurNome, pj.jurid, substring(m.ds_referencia,4,4) || substring(m.ds_referencia,1,2)";
                // 5 "EMAIL PARA AS EMPRESAS" -- AGRUPAR POR pj.id_pessoa -- id_pessoa
            } else if (tipo_envio == 5) {
                textQry += " order by pj.jurNome, pj.jurid, substring(m.ds_referencia,4,4) || substring(m.ds_referencia,1,2)";
            }

            Query qry = getEntityManager().createNativeQuery(textQry);

            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pollingEmail(int limite, int id_usuario) {
//        String text = "select * from fin_polling_email " +
//                      " where (replace(ds_hora,':','') <= '"+ DataHoje.horaMinuto().replace(":", "")+"' and dt_emissao = '"+DataHoje.data()+"' and is_ativo = true) " +
//                      "    or (replace(ds_hora,':','') <> '"+ DataHoje.horaMinuto().replace(":", "")+"' and dt_emissao < '"+DataHoje.data()+"' and is_ativo = true) " +
//                      " order by dt_emissao, replace(ds_hora,':','') asc" +
//                      " limit "+limite;
//        
//        String text = "select * from fin_polling_email pe " +
//                      " inner join fin_cobranca_envio ce on ce.id = pe.id_cobranca_envio " +
//                      " where (replace(pe.ds_hora,':','') <= '"+ DataHoje.horaMinuto().replace(":", "")+"' and pe. dt_emissao = '"+DataHoje.data()+"' and pe.is_ativo = true) " +
//                      "    or (replace(pe.ds_hora,':','') <> '"+ DataHoje.horaMinuto().replace(":", "")+"' and pe.dt_emissao <> '"+DataHoje.data()+"' and pe.is_ativo = true) " +
//                      "   and ce.id_usuario = " +id_usuario+
//                      " order by pe.dt_emissao, replace(pe.ds_hora,':','') asc " +
//                      " limit "+limite;
        String text = "select * from fin_polling_email pe "
                + " inner join fin_cobranca_envio ce on ce.id = pe.id_cobranca_envio "
                + " where ( "
                + "            (replace(pe.ds_hora,':','') <= '" + DataHoje.horaMinuto().replace(":", "") + "' and pe. dt_emissao = '" + DataHoje.data() + "') "
                + "         or (pe.dt_emissao < '" + DataHoje.data() + "') "
                + "       ) "
                + "   and pe.is_ativo = true"
                + "   and ce.id_usuario = " + id_usuario
                + " order by pe.dt_emissao, replace(pe.ds_hora,':','') asc "
                + " limit " + limite;
        try {
            Query qry = getEntityManager().createNativeQuery(text);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pollingEmailTrue() {
        String text = "select * from fin_polling_email where is_ativo = true limit 1";
        try {
            Query qry = getEntityManager().createNativeQuery(text);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pollingEmailNovo(int limite) {
        String text = "select * from fin_polling_email "
                + " where is_ativo = false "
                + "   and ds_hora = '' or ds_hora is null "
                + " order by dt_emissao "
                + " limit " + limite;
        try {
            Query qry = getEntityManager().createNativeQuery(text);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaUltimoLote() {
        String text = "select case when max(nr_lote_envio) = null then 1 else max(nr_lote_envio) end from fin_polling_email";
        try {
            Query qry = getEntityManager().createNativeQuery(text);
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public CobrancaEnvio pesquisaCobrancaEnvio(int id_lote) {
        //String text = "select ce from CobrancaEnvio ce where ce.dtEmissao = :data and ce.lote.id = "+id_lote;
        String text = "select ce from CobrancaEnvio ce where ce.lote.id = " + id_lote;
        try {
            Query qry = getEntityManager().createQuery(text);
            //qry.setParameter("data", DataHoje.dataHoje());
            return (CobrancaEnvio) qry.getSingleResult();
        } catch (Exception e) {
            return new CobrancaEnvio();
        }
    }
    
    @Override
    public List<Vector> listaParaEtiqueta(String string_qry, CobrancaTipo ct){
        List<Vector> result = null;
        
        String text = "";
        String text_group_by = "";
        String text_order_by = "";
        
        if (ct.getId() == 6){
            text = "select c.id_pessoa, c.ds_nome, fc.id_lote, ce.id_lote, ce.dt_emissao "+string_qry;
            text_group_by = " group by c.id_pessoa, c.ds_nome, fc.id_lote, ce.id_lote, ce.dt_emissao ";
            text_order_by = " order by c.ds_nome, c.id_pessoa ";
        }else{
            text = "select c.id_contabilidade, fc.id_lote, ce.id_lote, ce.dt_emissao "+string_qry;
            text_group_by = " group by c.id_contabilidade, fc.id_lote, ce.id_lote, ce.dt_emissao ";
            text_order_by = " order by c.id_contabilidade";
        }
        
        text += text_group_by+text_order_by;
        Query qry = getEntityManager().createNativeQuery(text);
        try{
            result = qry.getResultList();
        }catch(Exception e){
            
        }
        return result;
    }
        
}

//                String text_from = "  from arr_contribuintes_vw as c  "
//                    + "  left join pes_juridica as je on je.id = c.id_contabilidade "
//                    + "  left join pes_pessoa as pe on pe.id = je.id_pessoa "
//                    + " inner join fin_movimento as m on m.id_pessoa = c.id_pessoa "
//                    + "  left join pes_pessoa_endereco as pee on pee.id_pessoa = c.id_pessoa "
//                    + "  left join end_endereco as en on en.id = pee.id_endereco "
//                    + "  left join fin_cobranca       as fc on fc.id_movimento = m.id  "
//                    + "  left join fin_cobranca_envio as ce on ce.id_lote = fc.id_lote "
//                    + " where c.dt_inativacao is null    "
//                    + "   and m.is_ativo = true          "
//                    + "   and m.id_baixa is null         "
//                    + "   and m.dt_vencimento < '" + data + "' "
//                    + "   and pee.id_tipo_endereco = 5 ";    