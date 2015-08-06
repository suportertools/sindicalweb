package br.com.rtools.relatorios.beans;

import br.com.rtools.impressao.ParametroRelatorioEstorno;
import br.com.rtools.relatorios.dao.RelatorioEstornoDao;
import br.com.rtools.utilitarios.DataHoje;
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
public class RelatorioEstornoBean implements Serializable {
    private String dataLancamentoInicial = "";
    private String dataLancamentoFinal = "";
    private String dataBaixaInicial = "";
    private String dataBaixaFinal = "";
    
    @PostConstruct
    public void init(){
        
    }

    @PreDestroy
    public void destroy(){
        GenericaSessao.remove("relatorioEstornoBean");
    }
    
    public void imprimir(){
        File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/RELATORIO_ESTORNO.jasper"));
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);

            RelatorioEstornoDao dao = new RelatorioEstornoDao();

            List<Vector> result = dao.listaEstorno(dataLancamentoInicial, dataLancamentoFinal, dataBaixaInicial, dataBaixaFinal);
            List<ParametroRelatorioEstorno> lista = new ArrayList();
            for (Vector v : result) {
                lista.add(
                        new ParametroRelatorioEstorno(
                                DataHoje.converteData((Date) v.get(0)), // DATA LANCAMENTO
                                DataHoje.converteData((Date) v.get(1)),  // DATA BAIXA
                                v.get(2).toString(),  // RESPONSAVEL
                                v.get(3).toString(),  // TITULAR
                                v.get(4).toString(),  // BENEFICIARIO
                                (v.get(5) != null ? v.get(5).toString() : ""),  // NR ID BAIXA
                                v.get(6).toString(),  // USUARIO ESTORNO
                                v.get(7).toString(),  // USUARIO CAIXA
                                (v.get(8) != null ? v.get(8).toString() : ""),  // CAIXA
                                v.get(9).toString(),  // MOTIVO ESTORNO
                                DataHoje.converteData((Date) v.get(10)),  // VENCIMENTO
                                ((Double) v.get(11)).floatValue() // VALOR
                        )
                );
            }
            
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            HashMap param = new HashMap();
            // MOEDA PARA BRASIL VALORES IREPORT PTBR CONVERTE VALOR JASPER VALOR IREPORT VALOR
            param.put("REPORT_LOCALE", new Locale("pt", "BR"));
            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, dtSource);
            
            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
                
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "inline; filename=\"" + "relatorioEstorno" + ".pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();

            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException | IOException e) {
            e.getMessage();
        }        
    }

    public String getDataLancamentoInicial() {
        return dataLancamentoInicial;
    }

    public void setDataLancamentoInicial(String dataLancamentoInicial) {
        this.dataLancamentoInicial = dataLancamentoInicial;
    }

    public String getDataLancamentoFinal() {
        return dataLancamentoFinal;
    }

    public void setDataLancamentoFinal(String dataLancamentoFinal) {
        this.dataLancamentoFinal = dataLancamentoFinal;
    }

    public String getDataBaixaInicial() {
        return dataBaixaInicial;
    }

    public void setDataBaixaInicial(String dataBaixaInicial) {
        this.dataBaixaInicial = dataBaixaInicial;
    }

    public String getDataBaixaFinal() {
        return dataBaixaFinal;
    }

    public void setDataBaixaFinal(String dataBaixaFinal) {
        this.dataBaixaFinal = dataBaixaFinal;
    }
}
