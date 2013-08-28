package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.db.AcordoComissaoDB;
import br.com.rtools.arrecadacao.db.AcordoComissaoDBToplink;
import br.com.rtools.impressao.ParametroAcordoAnalitico;
import br.com.rtools.seguranca.controleUsuario.controleUsuarioJSFBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.SalvaArquivos;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class FechamentoComissaoAcordoJSFBean {
    private List<SelectItem> listaData;
    private int idDataFechamento;
    private String mensagem;

    public FechamentoComissaoAcordoJSFBean(){
        listaData = new ArrayList<SelectItem>();
        idDataFechamento = 0;
        mensagem = "";
    }

    public List<SelectItem> getListaData() {
        if(listaData.isEmpty()){
            int i = 0;
            AcordoComissaoDB db = new AcordoComissaoDBToplink();
            List<Date> select = db.pesquisaTodosFechamento();
            if(select != null){
                while (i < select.size()){
                    listaData.add(new SelectItem(
                           new Integer(i),
                           DataHoje.converteData(select.get(i)) ));
                   i++;
                }
            }
        }
        return listaData;
    }

    public synchronized void processar(){
        AcordoComissaoDB acordoComissaoDB = new AcordoComissaoDBToplink();
        if (acordoComissaoDB.inserirAcordoComissao()){
            listaData.clear();
            setMensagem("Concluído com sucesso!");
        }else{
            setMensagem("Erro ao gerar comissão!");

        }
    }

    public void visualizar(){
        if (!listaData.isEmpty()){
            AcordoComissaoDB db = new AcordoComissaoDBToplink();
            List result = db.listaAcordoComissao(listaData.get(idDataFechamento).getLabel());

            JasperReport jasper = null;
                Collection lista = new ArrayList<ParametroAcordoAnalitico>();
            BigDecimal repasse = new BigDecimal(0), 
                       liquido = new BigDecimal(0),
                       comissao = new BigDecimal(0),
                       valor = new BigDecimal(0),
                       taxa = new BigDecimal(0);

            for(int i = 0; i < result.size(); i++){
                valor = new BigDecimal( Double.valueOf(((Vector)result.get(i)).get(9).toString() ));
                taxa = new BigDecimal( Double.valueOf(((Vector)result.get(i)).get(10).toString() ));
                repasse = new BigDecimal( Double.valueOf(((Vector)result.get(i)).get(11).toString() ));

                repasse = ( valor.subtract(taxa).multiply(repasse)).divide(new BigDecimal(100));
                liquido = valor.subtract(taxa).subtract(repasse);
                comissao = valor.subtract(taxa).subtract(repasse).multiply(new BigDecimal(0.015));

                lista.add( new ParametroAcordoAnalitico( ((Vector)result.get(i)).get(0).toString(), 
                                           ((Vector)result.get(i)).get(1).toString(), 
                                           (Integer)((Vector)result.get(i)).get(2), 
                                           ((Vector)result.get(i)).get(3).toString(), 
                                           ((Vector)result.get(i)).get(4).toString(), 
                                           (Date)((Vector)result.get(i)).get(5), 
                                           (Date)((Vector)result.get(i)).get(6), 
                                           (Date)((Vector)result.get(i)).get(7), 
                                           valor, 
                                           taxa, 
                                           repasse, 
                                           liquido, 
                                           DataHoje.converte(listaData.get(idDataFechamento).getLabel()), 
                                           (Date)((Vector)result.get(i)).get(8), 
                                           comissao
                                          ) 
                );
            }
            String patch = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/"+ controleUsuarioJSFBean.getCliente()+"/Arquivos");
            File fileA = new File(patch+"/downloads");
            if(!fileA.exists()){
                fileA.mkdir();
            }
            File fileB = new File(patch+"/downloads/relatorios");
            if(!fileB.exists()){
                fileB.mkdir();
            } 
            try{
                byte[] arquivo = new byte[0];
                jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/ACORDO_ANALITICO.jasper"));
                           JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
                JasperPrint print = JasperFillManager.fillReport(
                         jasper,
                         null,
                         dtSource);
                 arquivo = JasperExportManager.exportReportToPdf(print);
                     
                 String nomeDownload = "acordo_analitico_"+DataHoje.horaMinuto().replace(":", "")+".pdf";
                
                 SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                 String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/"+ controleUsuarioJSFBean.getCliente()+"/Arquivos/downloads/relatorios");
                
                 sa.salvaNaPasta(pathPasta);

                 Download download =  new Download(nomeDownload,
                         pathPasta,
                         "application/pdf",
                         FacesContext.getCurrentInstance()
                         );
                 download.baixar(); 
            }catch(Exception e){
                
            }
        }
    }

    public void estornar(){
        if (!listaData.isEmpty()){
            AcordoComissaoDB acordoComissaoDB = new AcordoComissaoDBToplink();
            if (acordoComissaoDB.estornarAcordoComissao(listaData.get(idDataFechamento).getLabel()))
                mensagem = "Fechamento estornado com sucesso";
            else
                mensagem = "Erro ao estornar Fechamento";
            listaData.clear();
        }else{
            mensagem = "Data de Fechamento vazia";
        }
    }

    public void refreshForm(){

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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
