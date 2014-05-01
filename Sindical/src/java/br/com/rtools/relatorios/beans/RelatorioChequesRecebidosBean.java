package br.com.rtools.relatorios.beans;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.impressao.ParametroChequesRecebidos;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.relatorios.db.RelatorioFinanceiroDB;
import br.com.rtools.relatorios.db.RelatorioFinanceiroDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

@ManagedBean
@SessionScoped
public class RelatorioChequesRecebidosBean implements Serializable {
    // FILIAL
    private boolean chkFilial = false;
    private List<Filial> listaFilial = new ArrayList<Filial>();
    private List<Filial> listaFilialSelecionada = new ArrayList<Filial>();
    
    // CAIXA
    private boolean chkCaixa = false;
    private List<Caixa> listaCaixa = new ArrayList<Caixa>();
    private List<Caixa> listaCaixaSelecionado = new ArrayList<Caixa>();
        
    // TIPO DATA
    private boolean chkTipoData = false;
    private String tipoData = "emissao";
    private String dataInicial = DataHoje.data();
    private String dataFinal = DataHoje.data();
    
    // STATUS
    private boolean chkStatus = false;
    private int idStatus = 0;
    private List<SelectItem> listaStatus = new ArrayList<SelectItem>();
    
    public void porFilial() {
        chkFilial = chkFilial == true ? false : true;
    }
    
    public void porCaixa() {
        chkCaixa = chkCaixa == true ? false : true;
    }
    
    public void porTipoData() {
        chkTipoData = chkTipoData == true ? false : true;
    }
    
    public void porStatus() {
        chkStatus = chkStatus == true ? false : true;
    }
    
    
    public void visualizar() {
        String ids_filial = "";
        if (chkFilial && !listaFilialSelecionada.isEmpty()){
            for (int i = 0; i < listaFilialSelecionada.size(); i++) {
                if (ids_filial.length() > 0 && i != listaFilialSelecionada.size()) {
                    ids_filial += ",";
                }
                ids_filial += listaFilialSelecionada.get(i).getId();
            }
        }
        
        String ids_caixa = "";
        if (chkCaixa && !listaCaixaSelecionado.isEmpty()){
            for (int i = 0; i < listaCaixaSelecionado.size(); i++) {
                if (ids_caixa.length() > 0 && i != listaCaixaSelecionado.size()) {
                    ids_caixa += ",";
                }
                ids_caixa += listaCaixaSelecionado.get(i).getId();
            }
        }
        
        String tipo = "", d_i = "", d_f = "";
        if (chkTipoData && (!dataInicial.isEmpty() || !dataFinal.isEmpty())){
            tipo = tipoData;
            d_i = dataInicial;
            d_f = dataFinal;
        }
        
        int id_status = 0;
        if (chkStatus){
            id_status = Integer.valueOf(listaStatus.get(idStatus).getDescription());
        }
        
        Collection lista = new ArrayList();

        RelatorioFinanceiroDB db = new RelatorioFinanceiroDBToplink();
        
        List<Vector> result = db.listaChequesRecebidos(ids_filial, ids_caixa, tipo, d_i, d_f, id_status);
        
        for (Vector linha : result){
            lista.add(new ParametroChequesRecebidos(
                    linha.get(0).toString(), // FILIAL
                    linha.get(1).toString(), // EMISSAO
                    linha.get(2).toString(), // VENCIMENTO
                    linha.get(3).toString(), // BANCO
                    linha.get(4).toString(), // AGENCIA
                    linha.get(5).toString(), // CONTA
                    linha.get(6).toString(), // CHEQUE
                    new BigDecimal(linha.get(7).toString()), // VALOR
                    //Moeda.converteR$(linha.get(7).toString()), // VALOR
                    linha.get(8).toString(), // ID_BAIXA
                    linha.get(9).toString() // CAIXA
            ));
        }
        
        try {
            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/CHEQUES_RECEBIDOS.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dtSource);
//            byte[] arquivo = JasperExportManager.exportReportToPdf(jasperPrint);
//            
//            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            res.setContentType("application/pdf");
//            res.setHeader("Content-disposition", "inline; filename=\"Relatório Fechamento Caixa.pdf\"");
//            res.getOutputStream().write(arquivo);
//            res.getCharacterEncoding();
//            FacesContext.getCurrentInstance().responseComplete();

            JasperViewer jrviewer = new JasperViewer(jasperPrint, false);
            jrviewer.setTitle("Relatório Cheques Recebidos");
            jrviewer.setVisible(true);
        } catch (JRException e) {
        }

    }

    public boolean isChkFilial() {
        return chkFilial;
    }

    public void setChkFilial(boolean chkFilial) {
        this.chkFilial = chkFilial;
    }

    public List<Filial> getListaFilial() {
        if (listaFilial.isEmpty()){
            listaFilial = (List<Filial>) (new SalvarAcumuladoDBToplink()).listaObjeto("Filial", true);
        }
        return listaFilial;
    }

    public void setListaFilial(List<Filial> listaFilial) {
        this.listaFilial = listaFilial;
    }
    public List<Filial> getListaFilialSelecionada() {
        return listaFilialSelecionada;
    }

    public void setListaFilialSelecionada(List<Filial> listaFilialSelecionada) {
        this.listaFilialSelecionada = listaFilialSelecionada;
    }

    public boolean isChkCaixa() {
        return chkCaixa;
    }

    public void setChkCaixa(boolean chkCaixa) {
        this.chkCaixa = chkCaixa;
    }

    public List<Caixa> getListaCaixa() {
        if (listaCaixa.isEmpty()){
            listaCaixa = (new FinanceiroDBToplink()).listaCaixa();
        }
        return listaCaixa;
    }

    public void setListaCaixa(List<Caixa> listaCaixa) {
        this.listaCaixa = listaCaixa;
    }

    public List<Caixa> getListaCaixaSelecionado() {
        return listaCaixaSelecionado;
    }

    public void setListaCaixaSelecionado(List<Caixa> listaCaixaSelecionado) {
        this.listaCaixaSelecionado = listaCaixaSelecionado;
    }

    public String getTipoData() {
        return tipoData;
    }

    public void setTipoData(String tipoData) {
        this.tipoData = tipoData;
    }

    public boolean isChkTipoData() {
        return chkTipoData;
    }

    public void setChkTipoData(boolean chkTipoData) {
        this.chkTipoData = chkTipoData;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public boolean isChkStatus() {
        return chkStatus;
    }

    public void setChkStatus(boolean chkStatus) {
        this.chkStatus = chkStatus;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()){
            RelatorioFinanceiroDB db = new RelatorioFinanceiroDBToplink();
            
            List<FStatus> select = db.listaStatusCheque("7,8,9,10,11");
            
                for (int i = 0; i < select.size(); i++) {
                    listaStatus.add(new SelectItem(
                            i,
                            select.get(i).getDescricao(),
                            Integer.toString(select.get(i).getId()))
                    );
                }
            
        }
        return listaStatus;
    }

    public void setListaStatus(List<SelectItem> listaStatus) {
        this.listaStatus = listaStatus;
    }
}
