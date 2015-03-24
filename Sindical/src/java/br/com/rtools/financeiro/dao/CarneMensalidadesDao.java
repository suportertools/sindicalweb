package br.com.rtools.financeiro.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CarneMensalidadesDao extends DB {
    public List listaServicosCarneMensalidades(Integer id_pessoa, String datas) {
        String text = 
                "select \n " +
                " se.ds_descricao as servico, count(*) \n " +
                "  from fin_movimento as m \n " +
                " inner join pes_pessoa as p on p.id=m.id_pessoa \n " +
                " inner join fin_servicos as se on se.id=m.id_servicos \n " +
                "  left join soc_socios_vw as s on s.codsocio=m.id_pessoa \n " +
                " where is_ativo = true \n " +
                "   and id_baixa is null \n " +
                "   and to_char(m.dt_vencimento, 'MM/YYYY') in ("+datas+") \n " +
                "   and m.id_servicos not in (select id_servicos from fin_servico_rotina where id_rotina = 4) \n " +
                "   and func_titular_da_pessoa(m.id_beneficiario) = "+id_pessoa+" \n" +
                " group by se.ds_descricao \n" +
                " order by se.ds_descricao";
        try{
            Query qry = getEntityManager().createNativeQuery(text);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List listaCarneMensalidades(Integer id_pessoa, String datas) {
        String text = 
                " select \n " +
                "  p.ds_nome as Titular, \n " +
                "  s.matricula, \n " +
                "  s.categoria, \n " +
                "  m.id_pessoa id_responsavel, \n" +
                "  m.dt_vencimento vencimento, \n " +
                "  sum(nr_valor-nr_desconto_ate_vencimento) valor \n " +
                "  from fin_movimento as m \n " +
                " inner join pes_pessoa as p on p.id = m.id_pessoa \n" +
                "  left join soc_socios_vw as s on s.codsocio = m.id_pessoa \n " +
                " where is_ativo = true \n " +
                "   and id_baixa is null \n " +
                "   and to_char(m.dt_vencimento, 'MM/YYYY') in ("+datas+") \n " +
                "   and m.id_servicos not in (select id_servicos from fin_servico_rotina where id_rotina = 4) \n " +
                "   and func_titular_da_pessoa(m.id_beneficiario) = "+id_pessoa+" \n " +
                " group by \n " +
                "  p.ds_nome, \n " +
                "  s.matricula, \n " +
                "  s.categoria, \n " +
                "  m.id_pessoa, \n " +
                "  m.dt_vencimento \n " +
                " order by p.ds_nome, m.id_pessoa,m.dt_vencimento ";
        try{
            Query qry = getEntityManager().createNativeQuery(text);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List listaCarneMensalidadesAgrupado(String id_pessoa, String datas) {
        String and = "";
        
        if (id_pessoa != null){
            and = "   and func_titular_da_pessoa(m.id_beneficiario) in ("+id_pessoa+") \n ";
        }
        
        String text = 
                " select \n " +
                "  p.ds_nome as Titular, \n " +
                "  s.matricula, \n " +
                "  s.categoria, \n " +
                "  m.id_pessoa id_responsavel, \n " +
                "  sum(nr_valor-nr_desconto_ate_vencimento) valor \n " +
                "  from fin_movimento as m \n " +
                " inner join pes_pessoa as p on p.id = m.id_pessoa \n " +
                "  left join soc_socios_vw as s on s.codsocio = m.id_pessoa \n " +
                " where is_ativo = true \n " +
                "   and id_baixa is null \n " +
                "   and to_char(m.dt_vencimento, 'MM/YYYY') in ("+datas+") \n " +
                "   and m.id_servicos not in (select id_servicos from fin_servico_rotina where id_rotina = 4) \n " +
                    and +
                " group by \n " +
                "  p.ds_nome, \n " +
                "  s.matricula, \n " +
                "  s.categoria, \n " +
                "  m.id_pessoa \n " +
                " order by p.ds_nome, m.id_pessoa";
        try{
            Query qry = getEntityManager().createNativeQuery(text);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
