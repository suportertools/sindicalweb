package br.com.rtools.associativo.beans;

import br.com.rtools.arrecadacao.beans.GerarBoletoBean;
import br.com.rtools.cobranca.BancoDoBrasil;
import br.com.rtools.cobranca.Bradesco;
import br.com.rtools.cobranca.CaixaFederalSicob;
import br.com.rtools.cobranca.CaixaFederalSigCB;
import br.com.rtools.cobranca.CaixaFederalSindical;
import br.com.rtools.cobranca.Cobranca;
import br.com.rtools.cobranca.Itau;
import br.com.rtools.cobranca.Real;
import br.com.rtools.cobranca.Santander;
import br.com.rtools.cobranca.Sicoob;
import br.com.rtools.financeiro.Boleto;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.impressao.ParametroBoletoSocial;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.beans.UploadFilesBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.swap;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;


@ManagedBean
@SessionScoped
public class ImpressaoBoletoSocialBean {
    private List<DataObject> listaGrid = new ArrayList();
    private int de = 0;
    private int ate = 0;
    private boolean imprimeVerso = true;
    
    private String strResponsavel = "";
    private String strLote = "";
    private String strData = "";
    
    @PostConstruct
    public void init(){
        UploadFilesBean uploadFilesBean = new UploadFilesBean("Imagens/");
        GenericaSessao.put("uploadFilesBean", uploadFilesBean);
    }
    
    public void alterarPathImagem(String path){
        UploadFilesBean uploadFilesBean = new UploadFilesBean("Imagens/");
        GenericaSessao.put("uploadFilesBean", uploadFilesBean);
    }
    
//    public String imagemBannerBoletoSocial(){
//        File file_promo = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/BannerPromoBoleto.png"));
//
//        if (!file_promo.exists())
//            return null;
//        else
//            return "Imagens/BannerPromoBoleto.png";
//    } 
//    
//    public String imagemVersoBannerBoletoSocial(){
//        File file_verso = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoBoletoVersoSocial.png"));
//
//        if (!file_verso.exists())
//            return null;
//        else
//            return file_verso.getPath();
//    } 
//    
    public void filtrar(){
        listaGrid.clear();
    }
    
    public void marcar(){
        for (int i = 0; i < listaGrid.size(); i++){
            if ((i+1) >= de && ate == 0)
                listaGrid.get(i).setArgumento1(true);
            else if ((i+1) >= de && (i+1) <= ate)
                listaGrid.get(i).setArgumento1(true);
            else if (de == 0 && (i+1) <= ate)
                listaGrid.get(i).setArgumento1(true);
            else
                listaGrid.get(i).setArgumento1(false);
        }
    }
    
    public void imprimir(){
        List lista = new ArrayList();
        //List<Vector> result = new ArrayList<Vector>();//db.listaChequesRecebidos(ids_filial, ids_caixa, tipo, d_i, d_f, id_status);
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Filial filial = (Filial) sv.pesquisaCodigo(1, "Filial");
        FinanceiroDB db = new FinanceiroDBToplink();
        
        //List result = new ArrayList();
        
        //Vector x = new ArrayList()
        
        Map<String, Object> map = new LinkedHashMap<String, Object>();  
        
        //List<Vector> lista_agrupado = db.listaBoletoSocioAgrupado();
        
        float valor = 0, valor_total = 0;
        
        try {
            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO_SOCIAL.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);
            
            File file_jasper_verso = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO_SOCIAL_VERSO.jasper"));
            JasperReport jasperReportVerso = (JasperReport) JRLoader.loadObject(file_jasper_verso);
            
            List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
            File file_promo = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/BannerPromoBoleto.png"));
            
            if (!file_promo.exists())
                file_promo = null;
            
            MovimentoDB movDB = new MovimentoDBToplink();
            Cobranca cobranca = null;
            
            
            for (int i = 0; i < listaGrid.size(); i++){
                if ((Boolean)listaGrid.get(i).getArgumento1()){
                    List<Vector> lista_socio = db.listaBoletoSocio((String) ((Vector)listaGrid.get(i).getArgumento2()).get(0)); // NR_CTR_BOLETO

                    for (int w = 0; w < lista_socio.size(); w++){
                        Boleto boletox = movDB.pesquisaBoletos("'"+(String) ((Vector)listaGrid.get(i).getArgumento2()).get(0)+"'"); // NR_CTR_BOLETO
                        Movimento mov = (Movimento)sv.pesquisaCodigo((Integer)lista_socio.get(w).get(1), "Movimento");
                        
                        if (boletox.getContaCobranca().getLayout().getId() == Cobranca.SINDICAL) {
                            cobranca = new CaixaFederalSindical(mov, boletox);
                            //swap[43] = "EXERC " + lista.get(i).getReferencia().substring(3);
                            //swap[42] = "BLOQUETO DE CONTRIBUIÇÃO SINDICAL URBANA.";
                        } else if ((boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.caixaFederal))
                                && (boletox.getContaCobranca().getLayout().getId() == Cobranca.SICOB)) {
                            cobranca = new CaixaFederalSicob(mov, boletox);
                        } else if ((boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.caixaFederal))
                                && (boletox.getContaCobranca().getLayout().getId() == Cobranca.SIGCB)) {
                            cobranca = new CaixaFederalSigCB(mov, boletox);
                        } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.itau)) {
                            cobranca = new Itau(mov, boletox);
                        } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.bancoDoBrasil)) {
                            cobranca = new BancoDoBrasil(mov, boletox);
                        } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.real)) {
                            cobranca = new Real(mov, boletox);
                        } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.bradesco)) {
                            cobranca = new Bradesco(mov, boletox);
                        } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.santander)) {
                            cobranca = new Santander(mov, boletox);
                        } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.sicoob)) {
                            cobranca = new Sicoob(mov, boletox);
                        }

                        valor = Moeda.converteUS$(lista_socio.get(w).get(18).toString());
                        valor_total = Moeda.somaValores(valor_total, Moeda.converteUS$(lista_socio.get(w).get(18).toString()));

                        lista.add(new ParametroBoletoSocial(
                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // LOGO SINDICATO
                                filial.getFilial().getPessoa().getNome(), 
                                lista_socio.get(w).get(9).toString(), // CODIGO
                                lista_socio.get(w).get(10).toString(), // RESPONSAVEL
                                DataHoje.converteData((Date)lista_socio.get(w).get(11)), // VENCIMENTO
                                (lista_socio.get(w).get(12) == null) ? "" : lista_socio.get(w).get(12).toString(), // MATRICULA
                                (lista_socio.get(w).get(14) == null) ? "" : lista_socio.get(w).get(14).toString(), // CATEGORIA
                                (lista_socio.get(w).get(13) == null) ? "" : lista_socio.get(w).get(13).toString(), // GRUPO
                                lista_socio.get(w).get(16).toString(), // CODIGO BENEFICIARIO
                                lista_socio.get(w).get(17).toString(), // BENEFICIARIO
                                lista_socio.get(w).get(15).toString(), // SERVICO
                                Moeda.converteR$Float(valor), // VALOR
                                Moeda.converteR$Float(valor_total), // VALOR TOTAL
                                Moeda.converteR$(lista_socio.get(w).get(19).toString()), // VALOR ATRASADAS
                                Moeda.converteR$Float(valor_total), // VALOR ATÉ  VALORVENCIMENTO
                                file_promo == null ? null : file_promo.getAbsolutePath(), // LOGO PROMOÇÃO
                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(boletox.getContaCobranca().getContaBanco().getBanco().getLogo().trim()), // LOGO BANCO
                                lista_socio.get(w).get(20).toString(), // MENSAGEM
                                lista_socio.get(w).get(22).toString(), // AGENCIA
                                cobranca.representacao(), // REPRESENTACAO
                                lista_socio.get(w).get(23).toString(), // CODIGO CEDENTE
                                lista_socio.get(w).get(24).toString(), // NOSSO NUMENTO
                                DataHoje.converteData((Date)lista_socio.get(w).get(4)), // PROCESSAMENTO
                                cobranca.codigoBarras(), // CODIGO DE BARRAS
                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/serrilha.GIF"), // SERRILHA
                                lista_socio.get(w).get(35).toString() +" "+ lista_socio.get(w).get(36).toString(), // ENDERECO RESPONSAVEL
                                lista_socio.get(w).get(30).toString() +" "+ lista_socio.get(w).get(31).toString(), // ENDERECO FILIAL
                                lista_socio.get(w).get(39).toString() +" "+ lista_socio.get(w).get(38).toString() +" " + lista_socio.get(w).get(37).toString(), // COMPLEMENTO RESPONSAVEL
                                lista_socio.get(w).get(32).toString() +" - "+ lista_socio.get(w).get(33).toString() +" "+ lista_socio.get(w).get(34).toString(), // COMPLEMENTO FILIAL
                                lista_socio.get(w).get(28).toString(), // CNPJ FILIAL
                                lista_socio.get(w).get(29).toString(), // TELEFONE FILIAL
                                lista_socio.get(w).get(25).toString(), // EMAIL FILIAL
                                lista_socio.get(w).get(27).toString(), // SITE FILIAL
                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoBoletoVersoSocial.png"), // LOGO BOLETO VERSO SOCIAL
                                lista_socio.get(w).get(41).toString(), // LOCAL DE PAGAMENTO
                                lista_socio.get(w).get(40).toString() // INFORMATIVO
                        ));
                    }
                
                    JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
                    jasperPrintList.add(JasperFillManager.fillReport(jasperReport, null, dtSource));
                    if (imprimeVerso){
                        dtSource = new JRBeanCollectionDataSource(lista);
                        jasperPrintList.add(JasperFillManager.fillReport(jasperReportVerso, null, dtSource));
                    }

                    lista.clear();
                    valor = 0;
                    valor_total = 0;
                }
            }
            
            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream retorno = new ByteArrayOutputStream();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, retorno);
            exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
            exporter.exportReport();

            byte[] arquivo = retorno.toByteArray();
            
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "inline; filename=\"Boleto Social.pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();
            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(GerarBoletoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public List<DataObject> getListaGrid() {
        if (listaGrid.isEmpty() && !strData.isEmpty()){
            FinanceiroDB db = new FinanceiroDBToplink();
            List<Vector> lista_agrupado = db.listaBoletoSocioAgrupado(strResponsavel, strLote, strData);
            for (int i = 0; i < lista_agrupado.size(); i++){
                listaGrid.add(new DataObject(i+1, true, lista_agrupado.get(i), Moeda.converteR$(lista_agrupado.get(i).get(6).toString())));
            }
        }
        return listaGrid;
    }

    public void setListaGrid(List<DataObject> listaGrid) {
        this.listaGrid = listaGrid;
    }

    public int getDe() {
        return de;
    }

    public void setDe(int de) {
        this.de = de;
    }

    public int getAte() {
        return ate;
    }

    public void setAte(int ate) {
        this.ate = ate;
    }

    public boolean isImprimeVerso() {
        return imprimeVerso;
    }

    public void setImprimeVerso(boolean imprimeVerso) {
        this.imprimeVerso = imprimeVerso;
    }

    public String getStrResponsavel() {
        return strResponsavel;
    }

    public void setStrResponsavel(String strResponsavel) {
        this.strResponsavel = strResponsavel;
    }

    public String getStrLote() {
        return strLote;
    }

    public void setStrLote(String strLote) {
        this.strLote = strLote;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    
}
