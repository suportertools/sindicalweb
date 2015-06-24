package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.FechamentoRepasse;
import br.com.rtools.associativo.dao.FechamentoRepasseDao;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.impressao.ParametroFechamentoRepasse;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;


@ManagedBean
@SessionScoped
public class FechamentoRepasseBean implements Serializable{
    private FechamentoRepasse fechamentoRepasse = new FechamentoRepasse();
    private Integer idDataFechamento = 0;
    private List<SelectItem> listaDataFechamento = new ArrayList();
    private Integer idServicos = 0;
    private List<SelectItem> listaServicos = new ArrayList();
    
    @PostConstruct
    public void init(){
        loadListaDataFechamento();
        loadListaServicos();
    }
    
    @PreDestroy
    public void destroy(){
        GenericaSessao.remove("fechamentoRepasseBean");
    }
    
    public void loadListaDataFechamento(){
        listaDataFechamento.clear();
        
        List<Vector> result = new FechamentoRepasseDao().listaDataFechamentoRepasse();
        
        if (!result.isEmpty()){
            int i = 0;
            for (Vector linha : result) {
                listaDataFechamento.add(new SelectItem(i, DataHoje.converteData( (Date) linha.get(0) ), linha.get(0).toString()));
                i++;
            }
        }
    }
    
    public void loadListaServicos(){
        listaServicos.clear();
        
        ServicosDB db = new ServicosDBToplink();
        
        List<Servicos> result = db.pesquisaTodos(314);
        
        if (!result.isEmpty()){
            int i = 0;
            listaServicos.add(new SelectItem(0, "TODOS", null));
            for (Servicos s : result) {
                listaServicos.add(new SelectItem(i+1, s.getDescricao(), ""+s.getId()));
                i++;
            }
        }
    }
    
    public void salvar(){
        if(!new FechamentoRepasseDao().listaFechamentoRepasse(DataHoje.data()).isEmpty()){
            GenericaMensagem.warn("Atenção", "Fechamento já concluído HOJE!");
            return;
        }
        
        if (new FechamentoRepasseDao().inserirFechamentoRepasse()){
            GenericaMensagem.info("Sucesso", "Fechamento de Repasse de Cursos inserido!");
        }else{
            GenericaMensagem.error("Atenção", "Não foi possível concluir Fechamento!");
        }
        
        loadListaDataFechamento();
    }
    
    public void excluir(){
        if (new FechamentoRepasseDao().excluirFechamentoRepasse(listaDataFechamento.get(idDataFechamento).getDescription())){
            GenericaMensagem.info("Sucesso", "Fechamento de Repasse de Cursos inserido!");
        }else{
            GenericaMensagem.error("Atenção", "Não foi possível concluir Fechamento!");
        }
        
        loadListaDataFechamento();
    }
    
    public void visualizar(){
        try {
            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/FECHAMENTO_REPASSE.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);
            
            List lista = new ArrayList();
            List<Movimento> listam;
            if (idDataFechamento != null)
                listam = new FechamentoRepasseDao().listaMovimentoFechamentoRepasse(
                        listaDataFechamento.get(idDataFechamento).getDescription(), 
                        listaServicos.get(idServicos).getDescription() != null ? Integer.valueOf(listaServicos.get(idServicos).getDescription()) : null
                );
            else
                listam = new ArrayList();
            
            for (Movimento m : listam){
                lista.add(
                    new ParametroFechamentoRepasse(
                            m.getServicos().getDescricao(),
                            m.getBeneficiario().getId(),
                            m.getBeneficiario().getNome(),
                            DataHoje.converteData(m.getDtVencimento()),
                            (m.getBaixa() != null) ? m.getBaixa().getBaixa() : "",
                            m.getValorBaixa()
                    )    
                );
            }
            
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            
            // CASO UMA LISTA DE JASPERS DIFERENTES
            //List<JasperPrint> jasperPrintList = new ArrayList();
            //jasperPrintList.add(JasperFillManager.fillReport(jasperReport, null, dtSource));
            HashMap param = new HashMap();
            // MOEDA PARA BRASIL VALORES IREPORT PTBR
            param.put("REPORT_LOCALE", new Locale("pt", "BR"));
            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, dtSource);
            
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
                
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "inline; filename=\"" + "fechamentoRepasse" + ".pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();

            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException | IOException e) {
            e.getMessage();
        }
    }
    
    public FechamentoRepasse getFechamentoRepasse() {
        return fechamentoRepasse;
    }

    public void setFechamentoRepasse(FechamentoRepasse fechamentoRepasse) {
        this.fechamentoRepasse = fechamentoRepasse;
    }

    public Integer getIdDataFechamento() {
        return idDataFechamento;
    }

    public void setIdDataFechamento(Integer idDataFechamento) {
        this.idDataFechamento = idDataFechamento;
    }

    public List<SelectItem> getListaDataFechamento() {
        return listaDataFechamento;
    }

    public void setListaDataFechamento(List<SelectItem> listaDataFechamento) {
        this.listaDataFechamento = listaDataFechamento;
    }

    public Integer getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(Integer idServicos) {
        this.idServicos = idServicos;
    }

    public List<SelectItem> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<SelectItem> listaServicos) {
        this.listaServicos = listaServicos;
    }
}
