package br.com.rtools.arrecadacao.dao;

import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.ConvencaoServico;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ConvencaoServicoDao extends DB {

    public ConvencaoServico pesquisaConvencaoServico(Integer id_convencao, Integer id_grupo_cidade) {
        try {
            Query qry = getEntityManager().createQuery("SELECT cs FROM ConvencaoServico cs WHERE cs.convencaoCidade.convencao.id = " + id_convencao + " AND cs.convencaoCidade.grupoCidade.id = " + id_grupo_cidade);
            return (ConvencaoServico) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
    
    public List<ConvencaoServico> listaConvencaoServico() {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT cs FROM ConvencaoServico cs ORDER BY cs.convencaoCidade.convencao.descricao, cs.convencaoCidade.grupoCidade.descricao"
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<ConvencaoCidade> listaConvencaoCidade() {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT cc FROM ConvencaoCidade cc ORDER BY cc.convencao.descricao, cc.grupoCidade.descricao"
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
    
    public List<ConvencaoServico> listaConvencaoServico(Integer id_convencao_cidade) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT cs FROM ConvencaoServico cs WHERE cs.convencaoCidade.id = "+id_convencao_cidade
            );
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
