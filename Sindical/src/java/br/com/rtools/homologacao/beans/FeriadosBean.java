package br.com.rtools.homologacao.beans;

import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.homologacao.Feriados;
import br.com.rtools.homologacao.dao.FeriadosDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
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
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@ManagedBean
@SessionScoped
public class FeriadosBean implements Serializable {

    private Feriados feriados;
    private String message;
    private List<SelectItem> listCidade;
    private Integer index;
    private boolean chkCidades;
    private List<Feriados> listFeriados;
    private ScheduleModel eventModel;
    private boolean replicar;
    private int anos;
    private int ano;

    @PostConstruct
    public void init() {
        feriados = new Feriados();
        message = "";
        listCidade = new ArrayList();
        index = 0;
        chkCidades = false;
        listFeriados = new ArrayList();
        eventModel = new DefaultScheduleModel();
        replicar = false;
        anos = 0;
        ano = Integer.parseInt(DataHoje.livre(new Date(), "YYYY"));
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("feriadosBean");
    }

    public void save() {
        FeriadosDao feriadosDao = new FeriadosDao();
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (feriados.getId() == -1) {
            if (feriados.getNome().equals("")) {
                message = "Digite o nome do Feriado.";
                GenericaMensagem.warn("Validação", message);
                return;
            }
            if (feriados.getData().equals("") && feriados.getData().length() < 7) {
                message = "Data Inválida.";
                GenericaMensagem.warn("Validação", message);
                return;
            }
            if (chkCidades) {
                feriados.setCidade((Cidade) di.find(new Cidade(), Integer.parseInt(listCidade.get(index).getDescription())));
            } else {
                feriados.setCidade(null);
            }
            String cidade = "";
            if (feriados.getCidade() != null) {
                cidade = " - Cidade: (" + feriados.getCidade().getId() + ") " + feriados.getCidade().getCidade() + " - UF: " + feriados.getCidade().getUf();
            }
            if (replicar) {
                DataHoje dh = new DataHoje();
                String dataPadrao = feriados.getData();
                Feriados f = new Feriados();
                for (int i = 0; i < anos; i++) {
                    f.setNome(feriados.getNome());
                    f.setCidade(feriados.getCidade());
                    if (i == 0) {
                        f.setData(dataPadrao);
                    } else {
                        f.setData(dh.incrementarAnos(i, dataPadrao));
                    }
                    if (!feriadosDao.exiteFeriadoCidade(f)) {
                        if (di.save(f, true)) {
                            novoLog.save(
                                    "ID: " + f.getId()
                                    + " - Data: " + f.getData()
                                    + " - Nome: " + f.getNome()
                                    + cidade
                            );
                        }
                    }
                    f = new Feriados();
                }
                eventModel = new DefaultScheduleModel();
                listFeriados.clear();
                message = "Feriado adicionado com sucesso!";
                GenericaMensagem.info("Sucesso", message);
                feriados = new Feriados();
                replicar = false;
                anos = 0;
            } else {
                if (feriadosDao.exiteFeriadoCidade(feriados)) {
                    message = "Feriado já cadastrado!";
                    GenericaMensagem.warn("Validação", message);
                    return;
                }
                if (di.save(feriados, true)) {
                    novoLog.save(
                            "ID: " + feriados.getId()
                            + " - Data: " + feriados.getData()
                            + " - Nome: " + feriados.getNome()
                            + cidade
                    );
                    message = "Feriado adicionado com sucesso!";
                    GenericaMensagem.info("Sucesso", message);
                    eventModel = new DefaultScheduleModel();
                    feriados = new Feriados();
                    listFeriados.clear();
                    replicar = false;
                    anos = 0;
                } else {
                    message = "Erro ao Salvar Feriado!";
                    GenericaMensagem.warn("Erro", message);
                }
            }
        } else {
            Feriados f = (Feriados) di.find(feriados);
            String beforeUpdate
                    = "ID: " + f.getId()
                    + " - Data: " + f.getData()
                    + " - Nome: " + f.getNome();
            String cidadeBefore = "";
            if (f.getCidade() != null) {
                cidadeBefore = " - Cidade: (" + f.getCidade().getId() + ") " + f.getCidade().getCidade() + " - UF: " + f.getCidade().getUf();
            }
            String cidade = "";
            if (feriados.getCidade() != null) {
                cidade = " - Cidade: (" + feriados.getCidade().getId() + ") " + feriados.getCidade().getCidade() + " - UF: " + feriados.getCidade().getUf();
            }
            if (di.update(feriados, true)) {
                novoLog.update(beforeUpdate + cidadeBefore,
                        "ID: " + feriados.getId()
                        + " - Data: " + feriados.getData()
                        + " - Nome: " + feriados.getNome()
                        + cidade
                );
                message = "Registro atualizado com sucesso!";
                GenericaMensagem.info("Sucesso", message);
                eventModel = new DefaultScheduleModel();
                feriados = new Feriados();
                listFeriados.clear();
            } else {
                message = "Erro ao aterar registro!";
                GenericaMensagem.warn("Erro", message);
            }
        }
    }

    public void delete() {
        delete(feriados);
    }

    public void delete(Feriados f) {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (f.getId() != -1) {
            String cidade = "";
            if (f.getCidade() != null) {
                cidade = " - Cidade: (" + f.getCidade().getId() + ") " + f.getCidade().getCidade() + " - UF: " + f.getCidade().getUf();
            }
            if (di.delete(f, true)) {
                novoLog.delete(
                        "ID: " + f.getId()
                        + " - Data: " + f.getData()
                        + " - Nome: " + f.getNome()
                        + cidade
                );
                message = "Registro excluído com sucesso";
                GenericaMensagem.info("Sucesso", message);
                eventModel = new DefaultScheduleModel();
                listFeriados.clear();
            } else {
                message = "Erro ao excluir registro!";
                GenericaMensagem.warn("Erro", message);
            }
        } else {
            GenericaMensagem.warn("Validação", "Informar feriado!");
        }
    }

    public void edit(SelectEvent selectEvent) {
        DefaultScheduleEvent event = (DefaultScheduleEvent) selectEvent.getObject();
        feriados = (Feriados) event.getData();
    }

    public List<SelectItem> getListCidade() {
        if (chkCidades) {
            if (listCidade.isEmpty()) {
                GrupoCidadesDB db = new GrupoCidadesDBToplink();
                List<Cidade> listaCidades = db.pesquisaTodosCidadeAgrupada();
                for (int i = 0; i < listaCidades.size(); i++) {
                    listCidade.add(new SelectItem(i,
                            listaCidades.get(i).getCidade() + " - " + listaCidades.get(i).getUf(),
                            Integer.toString((listaCidades.get(i)).getId())));
                }
            }
        } else {
            listCidade.clear();
        }
        return listCidade;
    }

    public void setListCidade(List<SelectItem> listCidade) {
        this.listCidade = listCidade;

    }

    public Feriados getFeriados() {
        return feriados;
    }

    public void setFeriados(Feriados feriados) {
        this.feriados = feriados;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public boolean isChkCidades() {
        return chkCidades;
    }

    public void setChkCidades(boolean chkCidades) {
        this.chkCidades = chkCidades;
    }

    public List<Feriados> getListFeriados() {
        if (listFeriados.isEmpty()) {
            FeriadosDao feriadosDao = new FeriadosDao();
            listFeriados = feriadosDao.pesquisaTodosFeriados();
            eventModel = new DefaultScheduleModel();
            List<Feriados> list = new ArrayList<>();
            for (int i = 0; i < listFeriados.size(); i++) {
                if (ano == 0) {
                    list.add(listFeriados.get(i));
                } else if (ano == Integer.parseInt(DataHoje.livre(listFeriados.get(i).getDtData(), "YYYY"))) {
                    list.add(listFeriados.get(i));
                }
            }
            listFeriados = new ArrayList<>();
            listFeriados.addAll(list);
            for (int i = 0; i < listFeriados.size(); i++) {
                eventModel.addEvent(new DefaultScheduleEvent(listFeriados.get(i).getNome(), listFeriados.get(i).getDtData(), listFeriados.get(i).getDtData(), listFeriados.get(i)));
            }
        }
        return listFeriados;
    }

    public void setListFeriados(List<Feriados> listFeriados) {
        this.listFeriados = listFeriados;
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

    public void selectedData(SelectEvent selectEvent) {
        Date d = (Date) selectEvent.getObject();
        DataHoje dh = new DataHoje();
        d = DataHoje.converte(dh.incrementarDias(1, DataHoje.converteData(d)));
        ScheduleEvent event = new DefaultScheduleEvent("", d, d);
        feriados = new Feriados();
        feriados.setDtData(event.getStartDate());
    }

    public void dataListener(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        Date data = DataHoje.converte(format.format(event.getObject()));
        feriados.setDtData(data);
    }

    public boolean isReplicar() {
        return replicar;
    }

    public void setReplicar(boolean replicar) {
        this.replicar = replicar;
    }

    public int getAnos() {
        return anos;
    }

    public void setAnos(int anos) {
        this.anos = anos;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String clearList() {
        listFeriados.clear();
        // eventModel = null;
        getListFeriados();
        return null;

    }
}
