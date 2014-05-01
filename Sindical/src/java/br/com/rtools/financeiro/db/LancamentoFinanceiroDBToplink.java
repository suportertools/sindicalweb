package br.com.rtools.financeiro.db;

import br.com.rtools.estoque.Pedido;
import br.com.rtools.financeiro.CentroCusto;
import br.com.rtools.financeiro.CentroCustoContabilSub;
import br.com.rtools.financeiro.ChequePag;
import br.com.rtools.financeiro.ContaOperacao;
import br.com.rtools.financeiro.ContaTipoPlano5;
import br.com.rtools.financeiro.FiltroLancamento;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Operacao;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class LancamentoFinanceiroDBToplink extends DB implements LancamentoFinanceiroDB{
    @Override
    public List<TipoDocumento> listaTipoDocumento() {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT tp " +
                    "  FROM TipoDocumento tp " +
                    " WHERE tp.id in (1,2,4)"
            );
            return qry.getResultList();
        } catch (Exception e) {
            
        }
        return new ArrayList<TipoDocumento>();
    }
    
    @Override
    public List<Operacao> listaOperacao(String ids) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT o " +
                    "  FROM Operacao o " +
                    " WHERE o.id in ("+ids+")"
            );
            return qry.getResultList();
        } catch (Exception e) {
            
        }
        return new ArrayList<Operacao>();
    }
    
    @Override
    public Juridica pesquisaJuridica(String documento) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT j " +
                    "  FROM Juridica j " +
                    " WHERE j.pessoa.documento like '"+documento+"%'"
            );
            return (Juridica)qry.getSingleResult();
        } catch (Exception e) {
            
        }
        return null;
    }
    
    @Override
    public Fisica pesquisaFisica(String documento) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT f " +
                    "  FROM Fisica f " +
                    " WHERE f.pessoa.documento like '"+documento+"%'"
            );
            return (Fisica)qry.getSingleResult();
        } catch (Exception e) {
            
        }
        return null;
    }
    
    @Override
    public List<ContaOperacao> listaContaOperacao(int id_operacao) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT co "+
                    "  FROM ContaOperacao co " +
                    " WHERE co.operacao.id = " + id_operacao
            );
            return qry.getResultList();
        } catch (Exception e) {
            
        }
        return new ArrayList<ContaOperacao>();
    }
    
    @Override
    public List<ContaOperacao> listaContaOperacaoContabil(int id_centro_custo_contabil_sub) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT co "+
                    "  FROM ContaOperacao co " +
                    " WHERE co.centroCustoContabilSub.id = " + id_centro_custo_contabil_sub
            );
            return qry.getResultList();
        } catch (Exception e) {
            
        }
        return new ArrayList<ContaOperacao>();
    }
    
    @Override
    public List<CentroCusto> listaCentroCusto(int id_filial) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT cc "+
                    "  FROM CentroCusto cc " +
                    " WHERE cc.filial.id = " + id_filial
            );
            return qry.getResultList();
        } catch (Exception e) {
            
        }
        return new ArrayList<CentroCusto>();
    }
    
    @Override
    public List<CentroCustoContabilSub> listaTipoCentroCusto(int id_centro_custo_contabil_sub, String es) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT cc "+
                    "  FROM CentroCustoContabilSub cc " +
                    " WHERE cc.centroCustoContabil.id = " + id_centro_custo_contabil_sub + 
                    "   AND cc.id in ( " +
                    "   SELECT co.centroCustoContabilSub.id " +
                    "     FROM ContaOperacao co " +
                    "    WHERE co.es = '" + es +"' )"
            );
            return qry.getResultList();
        } catch (Exception e) {
            
        }
        return new ArrayList<CentroCustoContabilSub>();
    }
    
    @Override
    public List<FiltroLancamento> listaLancamento(int userId, String forSearch, String description) {
        String select = 
                    "SELECT fl "+
                    "  FROM FiltroLancamento fl " +
                    " WHERE fl.lote.rotina.id = 231 ";
                    
        
        String and = "";
        
        if (userId != -1)
            and = "   AND fl.usuario.id = " + userId;
        
        String order_by = " ORDER BY fl.lote.dtLancamento desc";
        
        String data = "";
        if (forSearch.equals("dias")){
            DataHoje dh = new DataHoje();
            data = dh.decrementarMeses(2, DataHoje.data());
            
            and += "  AND fl.lote.dtLancamento >= :data";
        }else if (forSearch.equals("emissao")){
            data = description;
            and += "  AND fl.lote.dtEmissao = :data";
        }else if (forSearch.equals("fornecedor")){
            and += "  AND fl.lote.pessoa.nome LIKE '%" + description.toUpperCase() + "%'";
        }else if (forSearch.equals("documento")){
            and += "  AND fl.lote.pessoa.documento LIKE '%" + description + "%'";
        }
        try {
            Query qry = getEntityManager().createQuery(select+and+order_by);
            
            if (!data.isEmpty())
                qry.setParameter("data", DataHoje.converte(data));
            
            return qry.getResultList();
        } catch (Exception e) {
            
        }
        return new ArrayList<FiltroLancamento>();
    }
    
    @Override
    public List<Movimento> listaParcelaLote(int id_lote) {
        try {
            Query qry = getEntityManager().createQuery(
                    "SELECT m "+
                    "  FROM Movimento m " +
                    " WHERE m.lote.id = " + id_lote
            );
            return qry.getResultList();
        } catch (Exception e) {
        }
        return new ArrayList<Movimento>();
    }
    
    @Override
    public List<ContaTipoPlano5> listaContaTipoPlano5(int id_plano5, int id_tipo) {
        try {
            String text = "SELECT ct "+
                          "  FROM ContaTipoPlano5 ct " +
                          " WHERE ct.contaTipo.id = " + id_tipo;
            String and = "";
            if (id_plano5 != -1){
                and = " AND ct.plano5.id = " + id_plano5;
            }
            Query qry = getEntityManager().createQuery(text+and);
            return qry.getResultList();
        } catch (Exception e) {
            
        }
        return new ArrayList<ContaTipoPlano5>();
    }
    
    @Override
    public FiltroLancamento pesquisaFiltroLancamento(int id_lote) {
        try {
            Query qry = getEntityManager().createQuery(
                            "SELECT fl "+
                            "  FROM FiltroLancamento fl " +
                            " WHERE fl.lote.id = " + id_lote
            );
            return (FiltroLancamento)qry.getSingleResult();
        } catch (Exception e) {
        }
        return new FiltroLancamento();
    }
    
    @Override
    public List<Pedido> listaPedido(int id_lote) {
        try {
            Query qry = getEntityManager().createQuery(
                            "SELECT p "+
                            "  FROM Pedido p " +
                            " WHERE p.lote.id = " + id_lote
            );
            return qry.getResultList();
        } catch (Exception e) {
        }
        return new ArrayList();
    }
    
    @Override
    public List<Plano5> listaComboPagamentoBaixa() {
        try {
            Query qry = getEntityManager().createQuery(
                            "SELECT pl5 FROM Plano5 pl5 WHERE pl5.contaBanco IS NOT NULL"
            );
            return qry.getResultList();
        } catch (Exception e) {
        }
        return new ArrayList();
    }
    
    @Override
    public ChequePag pesquisaChequeConta(String numero, int id_plano) {
        try {
            Query qry = getEntityManager().createQuery(
                            "SELECT ch FROM ChequePag ch WHERE ch.plano5.id = "+id_plano+" AND ch.cheque = '"+numero+"'"
            );
            return (ChequePag) qry.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
    
}
