package br.com.rtools.relatorios.beans;

import br.com.rtools.associativo.EventoBaile;
import br.com.rtools.associativo.dao.VendaBaileDao;
import br.com.rtools.impressao.ParametroFechamentoBaile;
import br.com.rtools.relatorios.dao.RelatorioFechamentoBaileDao;
import br.com.rtools.utilitarios.DataHoje;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class RelatorioFechamentoBaileBean implements Serializable {

    private final List<SelectItem> listaEventoBaile = new ArrayList();
    private Integer idEventoBaile = 0;

    @PostConstruct
    public void init() {
        loadListaEventoBaile();
    }

    @PreDestroy
    public void destroy() {

    }

    public void imprimir() {
        File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/FECHAMENTO_BAILE.jasper"));
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);

            RelatorioFechamentoBaileDao dao = new RelatorioFechamentoBaileDao();

            List<Vector> result = dao.listaEventoBaile(Integer.valueOf(listaEventoBaile.get(idEventoBaile).getDescription()));
            List<ParametroFechamentoBaile> lista = new ArrayList();
            for (Vector v : result) {
                lista.add(
                        new ParametroFechamentoBaile(
                                DataHoje.converteData((Date) v.get(0)), // EMISSAO
                                v.get(1).toString(), // OPERADOR
                                v.get(2).toString(), // CODIGO
                                v.get(3).toString(), // CONVIDADO
                                ((Integer) v.get(12) == 13 || (Integer) v.get(12) == 15) ? "Cortesia" : v.get(4).toString(), // STATUS
                                (v.get(5) != null ? v.get(5).toString() : ""), // MESA
                                (v.get(6) != null ? v.get(6).toString() : ""), // CONVITE
                                DataHoje.converteData((Date) v.get(7)), // VENCIMENTO
                                DataHoje.converteData((Date) v.get(8)), // PAGAMENTO
                                (v.get(9) != null ? ((Double) v.get(9)).floatValue() : 0), // VALOR
                                (v.get(10) != null ? v.get(10).toString() : ""), // CAIXA
                                v.get(11).toString() // OBS
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
            res.setHeader("Content-disposition", "inline; filename=\"" + "relatorioFechamentoBaile" + ".pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();

            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException | IOException e) {
            e.getMessage();
        }
    }

    public void loadListaEventoBaile() {
        listaEventoBaile.clear();
        VendaBaileDao dao = new VendaBaileDao();
        List<EventoBaile> result = dao.listaBaile(false);
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaEventoBaile.add(new SelectItem(
                        i,
                        result.get(i).getEvento().getDescricaoEvento().getDescricao() + " -  "
                        + result.get(i).getDataString() + " - ("
                        + result.get(i).getHoraInicio() + " às  "
                        + result.get(i).getHoraFim() + ")   "
                        + result.get(i).getQuantidadeMesas() + " mesas  / " + result.get(i).getQuantidadeConvites() + " convites",
                        Integer.toString((result.get(i)).getId())
                )
                );
            }
        }
    }

    public List<SelectItem> getListaEventoBaile() {
        return listaEventoBaile;
    }

    public Integer getIdEventoBaile() {
        return idEventoBaile;
    }

    public void setIdEventoBaile(Integer idEventoBaile) {
        this.idEventoBaile = idEventoBaile;
    }
}
