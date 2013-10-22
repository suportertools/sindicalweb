package br.com.rtools.associativo.db;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Spc;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class LancamentoIndividualDBToplink extends DB implements LancamentoIndividualDB{
    @Override
    public List pesquisaResponsavel(int id_pessoa, boolean desconto_folha) {
        try{
            String textqry = " select func_responsavel("+id_pessoa+", "+desconto_folha+");";
            
            Query qry = getEntityManager().createNativeQuery(textqry);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    @Override
    public List pesquisaContribuinteLancamento(int id_pessoa) {
        try{
            String textqry = " select * from arr_contribuintes_vw where id_pessoa = "+id_pessoa+" and motivo = 'FECHOU'";
            
            Query qry = getEntityManager().createNativeQuery(textqry);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    @Override
    public List pesquisaMovimentoFisica(int id_pessoa) {
        try{
            String textqry = " select * from fin_movimento m where dt_vencimento < now() and m.id_pessoa = " +id_pessoa+ " "+
                             "   and m.is_ativo = true and m.id_servicos not in (select sr.id_servicos from fin_servico_rotina sr where id_rotina = 4)";
            
            Query qry = getEntityManager().createNativeQuery(textqry);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }    
    
    @Override
    public List<Juridica> listaEmpresaConveniada(int id_servico) {
        try{
            List<Vector> vetor = new ArrayList<Vector>();
            List result = new ArrayList();
            String textqry = " select j.id from pes_pessoa as p " +
                             " inner join pes_juridica as j on j.id_pessoa = p.id " +
                             " inner join soc_convenio as c on c.id_juridica = j.id " +
                             " inner join soc_convenio_servico as cs on cs.id_convenio_sub_grupo = c.id_convenio_sub_grupo " +
                             "   and cs.id_servico = "+id_servico;
            
            Query qry = getEntityManager().createNativeQuery(textqry);
            
            vetor = qry.getResultList();
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            if (!vetor.isEmpty()) {
                for (int i = 0; i < vetor.size(); i++) {
                    result.add((Juridica)sv.pesquisaCodigo( ((Integer) ((Vector) vetor.get(i)).get(0)), "Juridica"));
                }
            }

            return result;
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    @Override
    public List<Spc> listaSerasa(int id_pessoa) {
        try{
            List<Spc> result = new ArrayList();
            
            String textqry = " select s from Spc s where s.pessoa.id = "+id_pessoa+" and s.dtSaida is null";
            
            Query qry = getEntityManager().createQuery(textqry);
            
            result = qry.getResultList();
            return result;
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    @Override
    public List<Vector> pesquisaServicoValor(int id_pessoa, int id_servico) {
        try{
            String textqry = " select func_valor_servico("+id_pessoa+", "+id_servico+", current_date, 0) ";
            
            Query qry = getEntityManager().createNativeQuery(textqry);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}

