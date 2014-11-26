package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Acordo;
import br.com.rtools.arrecadacao.db.AcordoComissaoDB;
import br.com.rtools.arrecadacao.db.AcordoComissaoDBToplink;
import br.com.rtools.impressao.ParametroAcordoAnalitico;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvaArquivos;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@SessionScoped
public class FechamentoComissaoAcordoBean {

    private List<SelectItem> listaData;
    private int idDataFechamento;
    private Acordo acordo;
    
    @PostConstruct
    public void init() {
        listaData = new ArrayList();
        idDataFechamento = 0;
        acordo = new Acordo();
        // mensagem = "";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("fechamentoComissaoAcordoBean");
    }

    public List<SelectItem> getListaData() {
        if (listaData.isEmpty()) {
            int i = 0;
            AcordoComissaoDB db = new AcordoComissaoDBToplink();
            List<Date> select = db.pesquisaTodosFechamento();
            if (select != null) {
                while (i < select.size()) {
                    listaData.add(new SelectItem(i, DataHoje.converteData(select.get(i))));
                    i++;
                }
            }
        }
        return listaData;
    }

    public synchronized void processar() {
        AcordoComissaoDB acordoComissaoDB = new AcordoComissaoDBToplink();
        if (acordoComissaoDB.inserirAcordoComissao()) {
            listaData.clear();
            GenericaMensagem.info("Sucesso", "Concluído com sucesso");
        } else {
            GenericaMensagem.warn("Erro", "Ao gerar comissão!");

        }
    }

    public void visualizar() {
        if (!listaData.isEmpty()) {
            AcordoComissaoDB db = new AcordoComissaoDBToplink();
            List result = db.listaAcordoComissao(listaData.get(idDataFechamento).getLabel());

            JasperReport jasper = null;
            Collection lista = new ArrayList<ParametroAcordoAnalitico>();
            BigDecimal repasse;
            BigDecimal liquido;
            BigDecimal comissao;
            BigDecimal valor;
            BigDecimal taxa;

            for (int i = 0; i < result.size(); i++) {
                valor = new BigDecimal(Double.valueOf(((List) result.get(i)).get(9).toString()));
                taxa = new BigDecimal(Double.valueOf(((List) result.get(i)).get(10).toString()));
                repasse = new BigDecimal(Double.valueOf(((List) result.get(i)).get(11).toString()));

                repasse = (valor.subtract(taxa).multiply(repasse)).divide(new BigDecimal(100));
                liquido = valor.subtract(taxa).subtract(repasse);
                comissao = valor.subtract(taxa).subtract(repasse).multiply(new BigDecimal(0.015));

                lista.add(new ParametroAcordoAnalitico(((List) result.get(i)).get(0).toString(),
                        ((List) result.get(i)).get(1).toString(),
                        (Integer) ((List) result.get(i)).get(2),
                        ((List) result.get(i)).get(3).toString(),
                        ((List) result.get(i)).get(4).toString(),
                        (Date) ((List) result.get(i)).get(5),
                        (Date) ((List) result.get(i)).get(6),
                        (Date) ((List) result.get(i)).get(7),
                        valor,
                        taxa,
                        repasse,
                        liquido,
                        DataHoje.converte(listaData.get(idDataFechamento).getLabel()),
                        (Date) ((List) result.get(i)).get(8),
                        comissao));
            }
            String patch = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos");
            File fileA = new File(patch + "/downloads");
            if (!fileA.exists()) {
                fileA.mkdir();
            }
            File fileB = new File(patch + "/downloads/relatorios");
            if (!fileB.exists()) {
                fileB.mkdir();
            }
            try {
                String patchRelatorio = "/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/ACORDO_ANALITICO.jasper";
                byte[] arquivo = new byte[0];
                if (!new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/ACORDO_ANALITICO.jasper")).exists()) {
                    patchRelatorio = "/Relatorios/ACORDO_ANALITICO.jasper";
                }
                
                File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(patchRelatorio));
                jasper = (JasperReport) JRLoader.loadObject(fl);
                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                arquivo = JasperExportManager.exportReportToPdf(print);

                String nomeDownload = "acordo_analitico_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";

                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/relatorios");

                sa.salvaNaPasta(pathPasta);

                Download download = new Download(nomeDownload,
                        pathPasta,
                        "application/pdf",
                        FacesContext.getCurrentInstance());
                download.baixar();
                download.remover();
            } catch (Exception e) {
            }
        }
    }

    public void estornar() {
        if (!listaData.isEmpty()) {
            AcordoComissaoDB acordoComissaoDB = new AcordoComissaoDBToplink();
            if (acordoComissaoDB.estornarAcordoComissao(listaData.get(idDataFechamento).getLabel())) {
                GenericaMensagem.info("Sucesso", "Fechamento de acordo estornado");
            } else {
                GenericaMensagem.warn("Erro", "Ao estornar fechamento!");
            }
            listaData.clear();
        } else {
            GenericaMensagem.warn("Validação", "Data de Fechamento vazia!");
        }
    }

    public void setListaData(List<SelectItem> listaData) {
        this.listaData = listaData;
    }

    public int getIdDataFechamento() {
        return idDataFechamento;
    }

    public void setIdDataFechamento(int idDataFechamento) {
        this.idDataFechamento = idDataFechamento;
    }

//    public String getMensagem() {
//        return mensagem;
//    }
//
//    public void setMensagem(String mensagem) {
//        this.mensagem = mensagem;
//    }

    public Acordo getAcordo() {
        return acordo;
    }

    public void setAcordo(Acordo acordo) {
        this.acordo = acordo;
    }
}
