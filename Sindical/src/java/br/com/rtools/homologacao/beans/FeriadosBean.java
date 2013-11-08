package br.com.rtools.homologacao.beans;

import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.homologacao.Feriados;
import br.com.rtools.homologacao.db.FeriadosDB;
import br.com.rtools.homologacao.db.FeriadosDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.primefaces.component.schedule.Schedule;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@ManagedBean
@SessionScoped
public class FeriadosBean implements Serializable {

    private Feriados feriados = new Feriados();
    private String msgConfirma = "";
    private List<SelectItem> listaCidade = new ArrayList<SelectItem>();
    private int idCidade = 0;
    private boolean chkCidades = false;
    private List<Feriados> listaFeriados = new ArrayList();
    private ScheduleModel eventModel;

    public FeriadosBean() {
    }

    public void salvar() {
        FeriadosDB db = new FeriadosDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (feriados.getId() == -1) {
            if (feriados.getNome().equals("")) {
                msgConfirma = "Digite o nome do Feriado.";
                GenericaMensagem.warn("Validação", msgConfirma);
                return;
            }
            if (feriados.getData().equals("") && feriados.getData().length() < 7) {
                msgConfirma = "Data Inválida.";
                GenericaMensagem.warn("Validação", msgConfirma);
                return;
            }
            if (chkCidades) {
                feriados.setCidade((Cidade) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaCidade.get(idCidade).getDescription()), "Cidade"));
            } else {
                feriados.setCidade(null);
            }
            if (db.exiteFeriadoCidade(feriados)) {
                msgConfirma = "Feriado já cadastrado!";
                GenericaMensagem.warn("Validação", msgConfirma);
                return;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(feriados)) {
                salvarAcumuladoDB.comitarTransacao();
                msgConfirma = "Feriado adicionado com sucesso!";
                GenericaMensagem.info("Sucesso", msgConfirma);
                eventModel = null;
                feriados = new Feriados();
                listaFeriados.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao Salvar Feriado!";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(feriados)) {
                salvarAcumuladoDB.comitarTransacao();
                msgConfirma = "Registro atualizado com sucesso!";
                GenericaMensagem.info("Sucesso", msgConfirma);
                eventModel = null;
                feriados = new Feriados();
                listaFeriados.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao aterar registro!";
                GenericaMensagem.warn("Erro", msgConfirma);
            }            
        }
    }

    public void excluir() {
        excluir(feriados);
    }
    
    public void excluir(Feriados fer) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        fer = (Feriados) salvarAcumuladoDB.pesquisaCodigo(fer.getId(), "Feriados");
        if (fer.getId() != -1) {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(fer)) {
                salvarAcumuladoDB.comitarTransacao();
                msgConfirma = "Feriado Excluído com sucesso!";
                GenericaMensagem.info("Sucesso", msgConfirma);
                eventModel = null;
                listaFeriados.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao Excluído Feriado!";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        } else {
            GenericaMensagem.warn("Validação", "Informar feriado!");
        }
    }
 
    public void editar(SelectEvent selectEvent) {  
         DefaultScheduleEvent event = (DefaultScheduleEvent) selectEvent.getObject();  
         feriados = (Feriados) event.getData();
    }

    public List<SelectItem> getListaCidade() {
        if (chkCidades) {
            if (listaCidade.isEmpty()) {
                GrupoCidadesDB db = new GrupoCidadesDBToplink();
                List<Cidade> listaCidades = db.pesquisaTodosCidadeAgrupada();
                for (int i = 0; i < listaCidades.size(); i++) {
                    listaCidade.add(new SelectItem(new Integer(i),
                            (String) listaCidades.get(i).getCidade() + " - " + listaCidades.get(i).getUf(),
                            Integer.toString((listaCidades.get(i)).getId())));
                }
            }
        } else {
            listaCidade.clear();
        }
        return listaCidade;
    }

    public void setListaCidade(List<SelectItem> listaCidade) {
        this.listaCidade = listaCidade;

    }

    public void refreshForm() {
    }

    public Feriados getFeriados() {
        return feriados;
    }

    public void setFeriados(Feriados feriados) {
        this.feriados = feriados;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(int idCidade) {
        this.idCidade = idCidade;
    }

    public boolean isChkCidades() {
        return chkCidades;
    }

    public void setChkCidades(boolean chkCidades) {
        this.chkCidades = chkCidades;
    }

    public List<Feriados> getListaFeriados() {
        if (listaFeriados.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaFeriados = (List<Feriados>) salvarAcumuladoDB.listaObjeto("Feriados", true);
            eventModel = new DefaultScheduleModel();
            for (int i = 0; i < listaFeriados.size(); i++) {
                eventModel.addEvent(new DefaultScheduleEvent(listaFeriados.get(i).getNome(), listaFeriados.get(i).getDtData(), listaFeriados.get(i).getDtData(), listaFeriados.get(i)));
            }              
        }
        return listaFeriados;
    }

    public void setListaFeriados(List<Feriados> listaFeriados) {
        this.listaFeriados = listaFeriados;
    }
    
    public void preProcessPDF(Object document) throws IOException, BadElementException, DocumentException {  
        //cria o documento  
        Document pdf = (Document) document;
        //seta a margin e página, precisa estar antes da abertura do documento, ou seja da linha: pdf.open()  
        // pdf.setMargins(200f, 200f, 200f, 200f);  
        pdf.setPageSize(PageSize.A4);  
        pdf.addTitle("Lista de feriados");  

        pdf.open();  
        //aqui pega o contexto para formar a url da imagem  
        String logo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png");


        //cria a imagem e passando a url  
        Image image = Image.getInstance(logo);  

        //alinha ao centro  
        image.setAlignment(Image.ALIGN_CENTER);  

        //adciona a img ao pdf  
        pdf.add(image);  


        //adiciona um paragrafo ao pdf, alinha também ao centro  
        Paragraph p = new Paragraph("teste frase");
        p.setAlignment("center");
        Paragraph p2 = new Paragraph("teste frase");
        p2.setAlignment(Element.ALIGN_LEFT);
        pdf.add(p);   
        pdf.add(p2); 
        pdf.getHtmlStyleClass();
        pdf.add(image);   
    }     

    public ScheduleModel getEventModel() {
        return eventModel;
    }

    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }
    
    public void dataSelecionada(SelectEvent selectEvent) {  
        ScheduleEvent event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());  
        feriados = new Feriados();
        feriados.setDtData(event.getStartDate());
    }  
    
    public void dataListener(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy"); 
        Date data = DataHoje.converte(format.format(event.getObject()));
        feriados.setDtData(data);
    }
}