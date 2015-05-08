package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.FechamentoCaixa;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class FechamentoCaixaGeralBean implements Serializable {
    private List<DataObject> listaFechamentoCaixa;
    private String valorFechamento;
    private List<Vector> listaDetalhesFechamento;
    private FechamentoCaixa fechamento;
    
    @PostConstruct
    public void init(){
        listaFechamentoCaixa = new ArrayList();
        listaDetalhesFechamento = new ArrayList();
        loadListaFechamentoCaixa();
        
        valorFechamento = "0,00";
        
        fechamento = new FechamentoCaixa();
    }
    
    @PreDestroy
    public void destroy(){
        
    }
    
    public void estornarFechamento(){
        if (fechamento.getId() != -1){
            Dao dao = new Dao();
            
            dao.openTransaction();
            
            FinanceiroDB db = new FinanceiroDBToplink();
            
            List<TransferenciaCaixa> listat = db.listaTransferencia(fechamento.getId());
            
            if (!listat.isEmpty()){
                
                for (TransferenciaCaixa tc : listat){
                    if  ( !dao.delete(dao.find(tc)) ){
                        GenericaMensagem.error("Erro", "Não foi possível concluir estorno!");
                        dao.rollback();
                        return;
                    }
                }
                dao.commit();
                
                GenericaMensagem.info("Sucesso", "Estorno Concluído!");
                loadListaFechamentoCaixa();
            }
            
            
        }
        
    }
    
    public void loadEstornarFechamento(Integer id_fechamento){
        fechamento = (FechamentoCaixa) new Dao().find(new FechamentoCaixa(), id_fechamento);
    }
    
    public void loadListaDetalhesFechamento(Integer id_caixa, Integer id_fechamento){
        listaDetalhesFechamento.clear();
        FinanceiroDB db = new FinanceiroDBToplink();
        
        listaDetalhesFechamento = db.listaDetalhesFechamentoCaixaGeral(id_caixa, id_fechamento);
    }
    
    public void loadListaFechamentoCaixa(){
        listaFechamentoCaixa.clear();
        
        FinanceiroDB db = new FinanceiroDBToplink();
        
        List<Vector> list = db.listaFechamentoCaixaGeral();
        
        for (Vector result : list){
            Caixa cx = (Caixa) new Dao().find( new Caixa(), (Integer) result.get(6));
            listaFechamentoCaixa.add(new DataObject(result, cx));
        }
        
    }
    
    public void salvar(){
        if (!listaFechamentoCaixa.isEmpty()){
            
            Dao di = new Dao();
            
            float valor = Moeda.substituiVirgulaFloat(valorFechamento), soma = 0;
            
            for (DataObject listaFechamentoCaixa1 : listaFechamentoCaixa) {
                soma = Moeda.somaValores(soma, Moeda.converteUS$(((Vector) listaFechamentoCaixa1.getArgumento0()).get(4).toString()));
                if (((Vector) listaFechamentoCaixa1.getArgumento0()).get(1) == null) {
                    GenericaMensagem.warn("Atenção", "Caixa " + ((Vector) listaFechamentoCaixa1.getArgumento0()).get(0) + " não tem Data de FECHAMENTO!");
                    return;
                }
                if (((Vector) listaFechamentoCaixa1.getArgumento0()).get(3) == null) {
                    GenericaMensagem.warn("Atenção", "Caixa " + ((Vector) listaFechamentoCaixa1.getArgumento0()).get(0) + " não tem Data de TRANSFERÊNCIA!");
                    return;
                }
            }
            
            if (valor != Moeda.converteFloatR$Float(soma)){
                GenericaMensagem.warn("Atenção", "Total incorreto!");
                return;
            }
            
            di.openTransaction();
            for(int i = 0; i < listaFechamentoCaixa.size(); i++){
                FechamentoCaixa fc = (FechamentoCaixa) di.find(new FechamentoCaixa(), (Integer)((Vector)listaFechamentoCaixa.get(i).getArgumento0()).get(5) );
                
                fc.setDtFechamentoGeral(DataHoje.dataHoje());
                
                if (!di.update(fc)){
                    di.rollback();
                    GenericaMensagem.error("Erro", "Não foi possível alterar Fechamento de Caixa!");
                    return;
                }
            }
            di.commit();
            
            GenericaMensagem.info("Sucesso", "Fechamento Geral concluído!");
            loadListaFechamentoCaixa();
        }
    }
    
    public String converteData(Date data){
        return DataHoje.converteData(data);
    }
    
    public String converteValor(String valor){
        return Moeda.converteR$(valor);
    }

    public List<DataObject> getListaFechamentoCaixa() {
        return listaFechamentoCaixa;
    }

    public void setListaFechamentoCaixa(List<DataObject> listaFechamentoCaixa) {
        this.listaFechamentoCaixa = listaFechamentoCaixa;
    }

    public String getValorFechamento() {
        return Moeda.converteR$(valorFechamento);
    }

    public void setValorFechamento(String valorFechamento) {
        this.valorFechamento = Moeda.converteR$(valorFechamento);
    }

    public List<Vector> getListaDetalhesFechamento() {
        return listaDetalhesFechamento;
    }

    public void setListaDetalhesFechamento(List<Vector> listaDetalhesFechamento) {
        this.listaDetalhesFechamento = listaDetalhesFechamento;
    }

    public FechamentoCaixa getFechamento() {
        return fechamento;
    }

    public void setFechamento(FechamentoCaixa fechamento) {
        this.fechamento = fechamento;
    }
}
