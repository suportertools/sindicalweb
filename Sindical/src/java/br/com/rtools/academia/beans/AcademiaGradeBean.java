package br.com.rtools.academia.beans;

import br.com.rtools.academia.AcademiaGrade;
import br.com.rtools.academia.AcademiaSemana;
import br.com.rtools.academia.dao.AcademiaDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.sistema.Semana;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class AcademiaGradeBean implements Serializable {

    private AcademiaGrade academiaGrade;
    //private AcademiaSemana academiaSemana;
    //private List<AcademiaSemana> academiaSemanas;
    private List<AcademiaGrade> listaAcademiaGrades;
    private List<Semana> listaSemana;
    private String mensagem;
    private Date horaInicio;
    private Date horaFim;
    // SEMANA
    private boolean dom;
    private boolean seg;
    private boolean ter;
    private boolean qua;
    private boolean qui;
    private boolean sex;
    private boolean sab;

    @PostConstruct
    public void init() {
        academiaGrade = new AcademiaGrade();
        //academiaSemana = new AcademiaSemana();
        //academiaSemanas = new ArrayList<AcademiaSemana>();
        listaAcademiaGrades = new ArrayList<AcademiaGrade>();
        listaSemana = new ArrayList<Semana>();
        mensagem = "";
        horaInicio = new Date();
        horaFim = new Date();
        academiaGrade.setHoraInicio(DataHoje.livre(new Date(), "HH:mm"));
        academiaGrade.setHoraFim(DataHoje.livre(new Date(), "HH:mm"));
        // SEMANA
        dom = false;
        seg = false;
        ter = false;
        qua = false;
        qui = false;
        sex = false;
        sab = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("academiaGradeBean");
    }

    public void clear() {
        GenericaSessao.remove("academiaGradeBean");
    }

    public void save() {
        int horaInicioI = Integer.parseInt(academiaGrade.getHoraInicio().replace(":", ""));
        int horaFimI = Integer.parseInt(academiaGrade.getHoraFim().replace(":", ""));
        if (horaFimI <= horaInicioI) {
            GenericaMensagem.info("Validação", "Hora inicio deve ser maior que hora fim");
            return;
        }
        Dao di = new Dao();
        NovoLog novoLog = new NovoLog();
        String s = "";
        if (academiaGrade.getId() == -1) {
//            AcademiaDao academiaDao = new AcademiaDao();
//            if (((AcademiaGrade) academiaDao.existeAcademiaGrade(academiaGrade.getHoraInicio(), academiaGrade.getHoraFim())) != null) {
//                GenericaMensagem.warn("Sistema", "Horário já cadastrado!");
//                return null;
//            }
            di.openTransaction();
            if (di.save(academiaGrade)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro inserido");
                novoLog.save("ID: " + academiaGrade.getId() + ". Horário das: " + academiaGrade.getHoraInicio() + " às " + academiaGrade.getHoraFim() + " (hrs). Dias da semana: " + s);

                listaAcademiaGrades.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            AcademiaGrade ag = (AcademiaGrade) di.find(academiaGrade);
            di.openTransaction();
            if (di.update(academiaGrade)) {
                novoLog.update("Horário das: " + ag.getHoraInicio() + " às " + ag.getHoraFim(), "ID: " + academiaGrade.getId() + " (hrs). Horário das: " + academiaGrade.getHoraInicio() + " às " + academiaGrade.getHoraFim() + " (hrs)");
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro atualizado");
                listaAcademiaGrades.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }
        }
        clear();
    }

    public boolean showSemanaGrade(AcademiaGrade academiaGrade, Integer idSemana) {
        AcademiaDao academiaDao = new AcademiaDao();
        boolean isSemana = academiaDao.existeAcademiaSemana(academiaGrade.getId(), idSemana);
        if (isSemana) {
            return true;
        }
        return false;
    }

    public void updateSemanaGrade(AcademiaGrade academiaGrade, Integer idSemana) {
        AcademiaDao academiaDao = new AcademiaDao();
        AcademiaSemana as = academiaDao.pesquisaAcademiaSemana(academiaGrade.getId(), idSemana);
        Dao dao = new Dao();
        if (as != null) {
            dao.openTransaction();
            if (dao.delete((AcademiaSemana) dao.find(as))) {
                dao.commit();
            } else {
                dao.rollback();
            }
        } else {
            dao.openTransaction();
            AcademiaSemana academiaSemanax = new AcademiaSemana();
            academiaSemanax.setAcademiaGrade(academiaGrade);
            academiaSemanax.setSemana((Semana) dao.find(new Semana(), idSemana));
            if (dao.save(academiaSemanax)) {
                dao.commit();
            } else {
                dao.rollback();
            }
        }
    }

    public void edit(AcademiaGrade ag) {
        academiaGrade = ag;
    }

    public void delete(AcademiaGrade ag) {
        if (ag.getId() != -1) {
            Dao di = new Dao();
            di.openTransaction();
            NovoLog novoLog = new NovoLog();
            String s = "";
            if (di.delete(ag)) {
                di.commit();
                novoLog.delete("ID: " + ag.getId() + ". Horário das: " + ag.getHoraInicio() + " às " + ag.getHoraFim() + " (hrs). Dias da semana: " + s);
                GenericaMensagem.info("Sucesso", "Excluído com sucesso");
                listaAcademiaGrades.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
            }
        }
    }

    public AcademiaGrade getAcademiaGrade() {
        return academiaGrade;
    }

    public void setAcademiaGrade(AcademiaGrade academiaGrade) {
        this.academiaGrade = academiaGrade;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<AcademiaGrade> getListaAcademiaGrades() {
        if (listaAcademiaGrades.isEmpty()) {
            Dao di = new Dao();
            listaAcademiaGrades = (List<AcademiaGrade>) di.list(new AcademiaGrade(), true);
        }
        return listaAcademiaGrades;
    }

    public void setListaAcademiaGrades(List<AcademiaGrade> listaAcademiaGrades) {
        this.listaAcademiaGrades = listaAcademiaGrades;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(Date horaFim) {
        this.horaFim = horaFim;
    }

    public void selecionaHoraInicio(SelectEvent event) {
        this.academiaGrade.setHoraInicio(DataHoje.livre((Date) event.getObject(), "HH:mm"));
    }

    public void selecionaHoraFim(SelectEvent event) {
        this.academiaGrade.setHoraFim(DataHoje.livre((Date) event.getObject(), "HH:mm"));
    }

    public boolean isDom() {
        return dom;
    }

    public void setDom(boolean dom) {
        this.dom = dom;
    }

    public boolean isSeg() {
        return seg;
    }

    public void setSeg(boolean seg) {
        this.seg = seg;
    }

    public boolean isTer() {
        return ter;
    }

    public void setTer(boolean ter) {
        this.ter = ter;
    }

    public boolean isQua() {
        return qua;
    }

    public void setQua(boolean qua) {
        this.qua = qua;
    }

    public boolean isQui() {
        return qui;
    }

    public void setQui(boolean qui) {
        this.qui = qui;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public boolean isSab() {
        return sab;
    }

    public void setSab(boolean sab) {
        this.sab = sab;
    }

    public List<Semana> getListaSemana() {
        if (listaSemana.isEmpty()) {
            Dao di = new Dao();
            listaSemana = (List<Semana>) di.list(new Semana());
        }
        return listaSemana;
    }

    public void setListaSemana(List<Semana> listaSemana) {
        this.listaSemana = listaSemana;
    }
//
//    public AcademiaSemana getAcademiaSemana() {
//        return academiaSemana;
//    }
//
//    public void setAcademiaSemana(AcademiaSemana academiaSemana) {
//        this.academiaSemana = academiaSemana;
//    }
//
//    public List<AcademiaSemana> getAcademiaSemanas() {
//        return academiaSemanas;
//    }
//
//    public void setAcademiaSemanas(List<AcademiaSemana> academiaSemanas) {
//        this.academiaSemanas = academiaSemanas;
//    }

}
