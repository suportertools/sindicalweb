package br.com.rtools.homologacao.beans;

import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.dao.HorariosDao;
import br.com.rtools.homologacao.lista.ListaHorarios;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.sistema.Semana;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class HorariosBean implements Serializable {

    private Horarios horarios;
    private Horarios horariosReativar;
    private String horaInicial;
    private String horaFinal;
    private boolean comIntervalo;
    private int intervalo;
    private int intInicial;
    private int intFinal;
    private int quantidade;
    private int idFilial;
    private int idSemana;
    private List<ListaHorarios> listHorarios;
    private List<SelectItem> listSemana;
    private List<SelectItem> listFiliais;

    @PostConstruct
    public void init() {
        horarios = new Horarios();
        horariosReativar = new Horarios();
        horaInicial = "";
        horaFinal = "";
        comIntervalo = false;
        intervalo = 0;
        intInicial = 0;
        intFinal = 0;
        quantidade = 0;
        idFilial = 0;
        idSemana = 1;
        listSemana = new ArrayList<>();
        listFiliais = new ArrayList<>();
        listHorarios = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("horariosBean");
    }

    public void save() {
        HorariosDao horariosDao = new HorariosDao();
        Dao dao = new Dao();
        if (comIntervalo) {
            if ((intervalo < 10) || (intervalo > 30)) {
                GenericaMensagem.warn("Validação", "Intervalo de Horário inválido!");
                return;
            }
            if (horarios.getQuantidade() <= 0) {
                GenericaMensagem.warn("Validação", "Digite a quantidade para este Horário!");
                return;
            }
            intInicial = Integer.parseInt(horaInicial.substring(0, 2) + horaInicial.substring(3, 5));
            intFinal = Integer.parseInt(horaFinal.substring(0, 2) + horaFinal.substring(3, 5));
            String strHoras = horaInicial.substring(0, 2);
            String strMinutos = horaInicial.substring(3, 5);
            String horarioIns = "";
            int soma = 0;
            int controlIntervalo = intervalo;
            horarios.setHora(strHoras + ":" + strMinutos);
            horarios.setFilial((Filial) dao.find(new Filial(), Integer.parseInt(getListFiliais().get(idFilial).getDescription())));
            horarios.setSemana((Semana) dao.find(new Semana(), Integer.parseInt(getListSemana().get(idSemana).getDescription())));
            quantidade = horarios.getQuantidade();
            if (!horariosDao.pesquisaPorHorarioFilial(horarios.getFilial().getId(), horarios.getHora(), horarios.getSemana().getId()).isEmpty()) {
                GenericaMensagem.warn("Validação", "Horário já cadastrado!");
                return;
            }
            dao.save(horarios, true);
            horarios = new Horarios();
            while (intInicial < intFinal) {
                if ((intInicial + intervalo) < intFinal) {
                    soma = Integer.parseInt(strMinutos) + intervalo;
                    if (soma >= 60) {
                        strHoras = Integer.toString(Integer.parseInt(strHoras) + 1);
                        if (soma == 60) {
                            strMinutos = "00";
                            controlIntervalo = 0;
                        } else {
                            strMinutos = Integer.toString(soma - 60);
                        }
                        horarioIns = ("00" + strHoras).substring(
                                ("00" + strHoras).length() - 2, ("00" + strHoras).length()) + ":"
                                + ("00" + strMinutos).substring(
                                        ("00" + strMinutos).length() - 2, ("00" + strMinutos).length());
                        horarios.setHora(horarioIns);
                        controlIntervalo = Integer.valueOf(strMinutos);
                    } else {
                        strMinutos = Integer.toString(soma);
                        horarioIns = ("00" + strHoras).substring(
                                ("00" + strHoras).length() - 2, ("00" + strHoras).length()) + ":"
                                + ("00" + strMinutos).substring(
                                        ("00" + strMinutos).length() - 2, ("00" + strMinutos).length());
                        horarios.setHora(horarioIns);
                    }
                    intInicial = Integer.parseInt(horarios.getHora().substring(0, 2) + horarios.getHora().substring(3, 5));
                    controlIntervalo += intervalo;
                    if (horarios.getFilial().getId() == -1) {
                        horarios.setFilial((Filial) dao.find(new Filial(), Integer.parseInt(getListFiliais().get(idFilial).getDescription())));
                    }
                    horarios.setSemana((Semana) dao.find(new Semana(), Integer.parseInt(getListSemana().get(idSemana).getDescription())));
                    if (!horariosDao.pesquisaPorHorarioFilial(Integer.parseInt(getListFiliais().get(idFilial).getDescription()), horarios.getHora(), horarios.getSemana().getId()).isEmpty()) {
                        horarios = new Horarios();
                        continue;
                    }

                    horarios.setQuantidade(quantidade);
                    if (dao.save(horarios, true)) {
                        GenericaMensagem.info("Sucesso", "Registro adicionado");
                    } else {
                        GenericaMensagem.warn("Erro", "Ao adicionar registro");
                    }
                    horarios = new Horarios();
                } else {
                    break;
                }
            }
            listHorarios.clear();
        } else {
            if (horarios.getHora().equals("")) {
                GenericaMensagem.warn("Validação", "Digite o horário!");
                return;
            }
            if (horarios.getQuantidade() <= 0) {
                GenericaMensagem.warn("Validação", "Digite a quantidade para este Horário!");
                return;
            }

            List hors = horariosDao.pesquisaPorHorarioFilial(Integer.parseInt(getListFiliais().get(idFilial).getDescription()), horarios.getHora(), Integer.parseInt(getListSemana().get(idSemana).getDescription()));
            if (horarios.getId() == -1) {
                if (!hors.isEmpty()) {
                    GenericaMensagem.warn("Validação", "Horário já cadastrado!");
                    return;
                }
                horarios.setFilial((Filial) dao.find(new Filial(), Integer.parseInt(getListFiliais().get(idFilial).getDescription())));
                horarios.setSemana((Semana) dao.find(new Semana(), Integer.parseInt(getListSemana().get(idSemana).getDescription())));
                if (dao.save(horarios, true)) {
                    GenericaMensagem.info("Sucesso", "Registro adicionado");
                    horarios = new Horarios();
                } else {
                    GenericaMensagem.warn("Erro", "Ao adicionar registro");
                }
            } else {
                if (!hors.isEmpty()
                        && ((Horarios) hors.get(0)).getQuantidade() == horarios.getQuantidade()) {
                    GenericaMensagem.warn("Validação", "Horário já cadastrado!");
                    return;
                }
                if (dao.update(horarios, true)) {
                    GenericaMensagem.info("Sucesso", "Registro atualizado");
                    horarios = new Horarios();
                } else {
                    GenericaMensagem.warn("Erro", "Ao atualizar registro");
                }
            }
            listHorarios.clear();
            horariosReativar = new Horarios();
        }
    }

    public void reativar() {
        if (horariosReativar.getId() != -1) {
            Dao dao = new Dao();
            horariosReativar.setAtivo(true);
            if (dao.update(horariosReativar, true)) {
                GenericaMensagem.info("Sucesso", "Horário reativado");
            } else {
                GenericaMensagem.warn("Erro", "Ao reativar horário!");
            }
            listHorarios.clear();
        }
        horariosReativar = new Horarios();
    }

    public void edit(Horarios h1) {
        horariosReativar = h1;
    }

    public String editQuantidade(Horarios h) {
        Dao dao = new Dao();
        if (h.getQuantidade() <= 0) {
            h.setQuantidade(0);
            h.setAtivo(false);
        }
        GenericaMensagem.info("Sucesso", "Registro atualizado");
        dao.update(h, true);
        listHorarios.clear();
        return null;
    }

    public void update(Horarios h) {
        Dao dao = new Dao();
        GenericaMensagem.info("Sucesso", "Registro atualizado");
        dao.update(h, true);
    }

    public void delete(Horarios h) {
        Dao dao = new Dao();
        if (h.getId() != -1) {
            if (dao.delete(h, true)) {
                GenericaMensagem.info("Sucesso", "Registro excluído");
            } else {
                h.setAtivo(false);
                if (dao.update(h, true)) {
                    GenericaMensagem.info("Sucesso", "Registro inátivado");
                }
            }
            listHorarios.clear();
        }
    }

    public List<SelectItem> getListSemana() {
        if (listSemana.isEmpty()) {
            Dao dao = new Dao();
            List<Semana> list = (List<Semana>) dao.list(new Semana());
            for (int i = 0; i < list.size(); i++) {
                listSemana.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listSemana;
    }

    public List<SelectItem> getListFiliais() {
        if (listFiliais.isEmpty()) {
            Dao dao = new Dao();
            List<Filial> list = (List<Filial>) dao.list(new Filial(), true);
            for (int i = 0; i < list.size(); i++) {
                listFiliais.add(new SelectItem(i,
                        list.get(i).getFilial().getPessoa().getDocumento() + " / " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listFiliais;
    }

    public Horarios getFeriados() {
        return horarios;
    }

    public void setFeriados(Horarios horarios) {
        this.setHorarios(horarios);
    }

    public Horarios getHorarios() {
        return horarios;
    }

    public void setHorarios(Horarios horarios) {
        this.horarios = horarios;
    }

    public int getIntervalo() {
        return intervalo;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public int getIntInicial() {
        return intInicial;
    }

    public void setIntInicial(int intInicial) {
        this.intInicial = intInicial;
    }

    public int getIntFinal() {
        return intFinal;
    }

    public void setIntFinal(int intFinal) {
        this.intFinal = intFinal;
    }

    public String getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(String horaInicial) {
        this.horaInicial = horaInicial;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public int getIdSemana() {
        return idSemana;
    }

    public void setIdSemana(int idSemana) {
        this.idSemana = idSemana;
    }

    public void setListSemana(List<SelectItem> listSemana) {
        this.listSemana = listSemana;
    }

    public void setListFiliais(List<SelectItem> listFiliais) {
        this.listFiliais = listFiliais;
    }

    public List<ListaHorarios> getListHorarios() {
        if (listHorarios.isEmpty()) {
            HorariosDao db = new HorariosDao();
            List<Horarios> list = db.pesquisaTodosPorFilial(Integer.parseInt(getListFiliais().get(idFilial).getDescription()), Integer.parseInt(getListSemana().get(idSemana).getDescription()));
            for (Horarios h : list) {
                if (h.isAtivo()) {
                    listHorarios.add(new ListaHorarios(h, "** ATIVO **"));
                } else {
                    listHorarios.add(new ListaHorarios(h, "** INATIVO **"));
                }
            }
        }
        return listHorarios;
    }

    public void setListHorarios(List<ListaHorarios> listHorarios) {
        this.listHorarios = listHorarios;
    }

    public boolean isComIntervalo() {
        return comIntervalo;
    }

    public void setComIntervalo(boolean comIntervalo) {
        this.comIntervalo = comIntervalo;
    }

    public Horarios getHorariosReativar() {
        return horariosReativar;
    }

    public void setHorariosReativar(Horarios horariosReativar) {
        this.horariosReativar = horariosReativar;
    }

}
