package br.com.rtools.associativo.db;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MovimentosReceberSocialDBToplink extends DB implements MovimentosReceberSocialDB {

    @Override
    public List pesquisaListaMovimentos(String ids, String por_status) {
        try{
            if (ids.isEmpty()){
                return new ArrayList();
            }
        String textqry = " select  "
                        + "     se.ds_descricao as servico,  "
                        + "     tp.ds_descricao as tipo, "
                        + "     m.ds_referencia, "
                        + "     m.dt_vencimento as vencimento, "
                        + "     func_valor(m.id) as valor, "
                        //+ "     m.nr_valor as valor, "
                        + "     func_multa_ass(m.id)+func_juros_ass(m.id)+func_correcao_ass(m.id) as acrescimo, "
                        + "     m.nr_desconto as desconto, "
                        + "     m.nr_valor+func_multa_ass(m.id)+func_juros_ass(m.id)+func_correcao_ass(m.id) as vl_calculado, "
                        + "     bx.dt_baixa, "
                        + "     nr_valor_baixa as valor_pago,  "
                        + "     m.ds_es as es, "
                        + "     p.ds_nome as responsavel, "
                        + "     b.ds_nome as beneficiario, " //12
                        + "     t.ds_nome as titular, " //13
                        + "     m.id as id_movimento, "
                        + "     m.id_lote as lote, "
                        + "     l.dt_lancamento as criacao,  " //16
                        + "     m.ds_documento as boleto, " //17
                        + "     func_intervalo_dias(m.dt_vencimento,CURRENT_DATE) as dias_atraso, "
                        + "     func_multa_ass(m.id) as multa,  "
                        + "     func_juros_ass(m.id) as juros, "
                        + "     func_correcao_ass(m.id) as correcao, "
                        + "     us.ds_nome as caixa,  "
                        + "     m.id_baixa as lote_baixa, "
                        + "     l.ds_documento as documento "
                        + " from fin_movimento as m  "
                        + "inner join fin_lote as l on l.id=m.id_lote "
                        + "inner join pes_pessoa as p on p.id=m.id_pessoa "
                        + "inner join pes_pessoa as b on b.id=m.id_beneficiario "
                        + "inner join pes_pessoa as t on t.id=m.id_titular "
                        + "inner join fin_servicos as se on se.id=m.id_servicos "
                        + "inner join fin_tipo_servico as tp on tp.id=m.id_tipo_servico  "
                        + " left join fin_baixa as bx on bx.id=m.id_baixa "
                        + " left join seg_usuario as u on u.id=bx.id_usuario "
                        + " left join pes_pessoa as us on us.id=u.id_pessoa";
                        
            String order_by = " order by m.dt_vencimento, se.ds_descricao, p.ds_nome, t.ds_nome, b.ds_nome ";
            String where = "";
            String ands = "";

            if (por_status.equals("todos")){
                ands = where + " where m.id_pessoa in ("+ids+") and m.is_ativo = true and m.id_servicos not in (select sr.id_servicos from fin_servico_rotina sr where id_rotina = 4) ";
            }else if (por_status.equals("abertos")){
                ands = where + " where m.id_pessoa in ("+ids+") and m.id_baixa is null and m.is_ativo = true and m.id_servicos not in (select sr.id_servicos from fin_servico_rotina sr where id_rotina = 4) ";
            }else{
                ands = where + " where m.id_pessoa in ("+ids+") and m.id_baixa is not null and m.is_ativo = true and m.id_servicos not in (select sr.id_servicos from fin_servico_rotina sr where id_rotina = 4) ";
            }
//            if (por_status.equals("todos")){
//                ands = where + " where m.id_pessoa in ("+ids+") and m.is_ativo = true and m.id_servicos in (select sr.id_servicos from fin_servico_rotina sr where id_rotina = 4) ";
//            }else if (por_status.equals("abertos")){
//                ands = where + " where m.id_pessoa in ("+ids+") and m.id_baixa is null and m.is_ativo = true and m.id_servicos in (select sr.id_servicos from fin_servico_rotina sr where id_rotina = 4) ";
//            }else{
//                ands = where + " where m.id_pessoa in ("+ids+") and m.id_baixa is not null and m.is_ativo = true and m.id_servicos in (select sr.id_servicos from fin_servico_rotina sr where id_rotina = 4) ";
//            }
        
            textqry += ands + order_by;
            Query qry = getEntityManager().createNativeQuery(textqry);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
