package br.com.rtools.homologacao.beans;

import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.db.HorariosDB;
import br.com.rtools.homologacao.db.HorariosDBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDBToplink;
import br.com.rtools.sistema.Semana;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
// import java.util.Vector;
import javax.faces.model.SelectItem;

public class HorariosJSFBean {

    private Horarios horarios = new Horarios();
    private String msgConfirma = "";
    private String horaInicial = "";
    private String horaFinal = "";
    private boolean chkReplicarTodos = false;
    private int intervalo = 0;
    private int intInicial = 0;
    private int intFinal = 0;
    private int quantidade = 0;
    private int idFilial = 0;
    private int idSemana = 1;
    private int idIndex = -1;
    private List<DataObject> listaH = new ArrayList();
    List<SelectItem> listaSemana = new ArrayList();
    List<SelectItem> listaFiliais = new ArrayList();
    private String cor1 = "";
    private String cor2 = "color:white; background: dodgerblue!important;";
    private String cor3 = "";
    private String cor4 = "";
    private String cor5 = "";
    private String cor6 = "";
    private String cor7 = "";

    public String salvar() {
        HorariosDB db = new HorariosDBToplink();
        SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
        if (chkReplicarTodos) {
            if ((intervalo < 10) || (intervalo > 30)) {
                msgConfirma = "Intervalo de Horário inválido!";
                return null;
            }
            if (horarios.getQuantidade() <= 0) {
                msgConfirma = "Digite a quantidade para este Horário!";
                return null;
            }
            intInicial = Integer.parseInt(horaInicial.substring(0, 2) + horaInicial.substring(3, 5));
            intFinal = Integer.parseInt(horaFinal.substring(0, 2) + horaFinal.substring(3, 5));
            String strHoras = horaInicial.substring(0, 2);
            String strMinutos = horaInicial.substring(3, 5);
            String horarioIns = "";
            int soma = 0;
            int controlIntervalo = intervalo;
            horarios.setHora(strHoras + ":" + strMinutos);
            horarios.setFilial((Filial) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), "Filial"));
            horarios.setSemana((Semana) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaSemana().get(idSemana).getDescription()), "Semana"));
            quantidade = horarios.getQuantidade();
            if (!db.pesquisaPorHorarioFilial(horarios.getFilial().getId(), horarios.getHora(), horarios.getSemana().getId()).isEmpty()) {
                msgConfirma = "Horário ja Cadastrado!";
                return null;
            }
            db.insert(horarios);
            horarios = new Horarios();
//                if ( Integer.parseInt(horaFinal.substring(3, 5)) < intervalo )
//                    intFinal = (intFinal-100) + (60 - intervalo);
//                else
//                    intFinal = (intFinal - intervalo);
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
                        horarios.setFilial((Filial) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), "Filial"));
                    }
                    horarios.setSemana((Semana) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaSemana().get(idSemana).getDescription()), "Semana"));
                    // if (!db.pesquisaPorHorarioFilial(horarios.getFilial().getId(), horarios.getHora(), horarios.getSemana().getId()).isEmpty()) {
                    if (!db.pesquisaPorHorarioFilial(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), horarios.getHora(), horarios.getSemana().getId()).isEmpty()) {
                        horarios = new Horarios();
                        continue;
                    }

                    horarios.setQuantidade(quantidade);
                    if (db.insert(horarios)) {
                        msgConfirma = "Horário adicionado com sucesso!";
                    } else {
                        msgConfirma = "Erro ao Salvar Horário!";
                    }
                    horarios = new Horarios();
                } else {
                    break;
                }
            }
            listaH.clear();
        } else {
            if (horarios.getHora().equals("")) {
                msgConfirma = "Digite um horário!";
                return null;
            }
            if (horarios.getQuantidade() <= 0) {
                msgConfirma = "Digite a quantidade para este Horário!";
                return null;
            }

            List hors = db.pesquisaPorHorarioFilial(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), horarios.getHora(), Integer.parseInt(getListaSemana().get(idSemana).getDescription()));
            if (horarios.getId() == -1) {
                if (!hors.isEmpty()) {
                    msgConfirma = "Horário ja Cadastrado!";
                    return null;
                }
                horarios.setFilial((Filial) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), "Filial"));
                horarios.setSemana((Semana) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaSemana().get(idSemana).getDescription()), "Semana"));
                acumuladoDB.abrirTransacao();
                if (acumuladoDB.inserirObjeto(horarios)) {
                    acumuladoDB.comitarTransacao();
                    msgConfirma = "Horário adicionado com sucesso!";
                    horarios = new Horarios();
                } else {
                    acumuladoDB.desfazerTransacao();
                    msgConfirma = "Erro ao Salvar Horário!";
                }
            } else {
                if (!hors.isEmpty()
                        && ((Horarios) hors.get(0)).getQuantidade() == horarios.getQuantidade()) {
                    msgConfirma = "Horário ja Cadastrado!";
                    return null;
                }
                acumuladoDB.abrirTransacao();
                if (acumuladoDB.alterarObjeto(horarios)) {
                    acumuladoDB.comitarTransacao();
                    msgConfirma = "Horário atualizado com sucesso!";
                    horarios = new Horarios();
                } else {
                    acumuladoDB.desfazerTransacao();
                    msgConfirma = "Erro ao Atualizar Horário!";
                }
            }
            listaH.clear();
        }
        return null;
    }

    public String comIntervalo() {
        if (chkReplicarTodos) {
            chkReplicarTodos = false;
        } else {
            chkReplicarTodos = true;
        }
        return null;
    }

    public String editar(Horarios h) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        h.setAtivo(true);
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.alterarObjeto(h)) {
            salvarAcumuladoDB.comitarTransacao();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
        }
        listaH.clear();
        msgConfirma = "Horário reativado!";
        return null;
    }

    public String editarQuantidade(Horarios h) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (h.getQuantidade() <= 0) {
            h.setQuantidade(0);
            h.setAtivo(false);
        }
        if (salvarAcumuladoDB.alterarObjeto(h)) {
            salvarAcumuladoDB.comitarTransacao();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
        }
        listaH.clear();
        return null;
    }

    public String excluir(Horarios h) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        h = (Horarios) salvarAcumuladoDB.pesquisaCodigo(h.getId(), "Horarios");
        if (h.getId() != -1) {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(h)) {
                salvarAcumuladoDB.comitarTransacao();
                msgConfirma = "Horário Excluído com sucesso!";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                h.setAtivo(false);
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.alterarObjeto(h)) {
                    salvarAcumuladoDB.comitarTransacao();
                    msgConfirma = "Horário Inativado!";
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                }
            }
            listaH.clear();
        }
        return null;
    }

    public List getListaHorarios() {
        if (listaH.isEmpty()) {
            HorariosDB db = new HorariosDBToplink();
            List<Horarios> list = db.pesquisaTodosPorFilial(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), Integer.parseInt(getListaSemana().get(idSemana).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isAtivo()) {
                    listaH.add(new DataObject(list.get(i), "** ATIVO **"));
                } else {
                    listaH.add(new DataObject(list.get(i), "** INATIVO **"));
                }
            }
        }
        return listaH;
    }

    public List<SelectItem> getListaSemana() {
        if (listaSemana.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<Semana> result = (List<Semana>) dB.listaObjeto("Semana");
            for (int i = 0; i < result.size(); i++) {
                listaSemana.add(
                        new SelectItem(new Integer(i),
                        result.get(i).getDescricao(),
                        Integer.toString(result.get(i).getId())));
            }
        }
        return listaSemana;
    }

    public List<SelectItem> getListaFiliais() {
        if (listaFiliais.isEmpty()) {
            FilialDB db = new FilialDBToplink();
            List<Filial> select = db.pesquisaTodos();
            for (int i = 0; i < select.size(); i++) {
                listaFiliais.add(new SelectItem(new Integer(i),
                        select.get(i).getFilial().getPessoa().getDocumento() + " / " + select.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(select.get(i).getId())));
            }
        }
        return listaFiliais;
    }

    public void limpaLista() {
        listaH.clear();
    }

    public void showBottom(int id) {
        idSemana = id;
        switch (idSemana) {
            case 0: {
                setCor1("color:white; background: dodgerblue!important;");
                setCor2("");
                setCor3("");
                setCor4("");
                setCor5("");
                setCor6("");
                setCor7("");
                break;
            }
            case 1: {
                setCor1("");
                setCor2("color:white; background: dodgerblue!important;");
                setCor3("");
                setCor4("");
                setCor5("");
                setCor6("");
                setCor7("");
                break;
            }
            case 2: {
                setCor1("");
                setCor2("");
                setCor3("color:white; background: dodgerblue!important;");
                setCor4("");
                setCor5("");
                setCor6("");
                setCor7("");
                break;
            }
            case 3: {
                setCor1("");
                setCor2("");
                setCor3("");
                setCor4("color:white; background: dodgerblue!important;");
                setCor5("");
                setCor6("");
                setCor7("");
                break;
            }
            case 4: {
                setCor1("");
                setCor2("");
                setCor3("");
                setCor4("");
                setCor5("color:white; background: dodgerblue!important;");
                setCor6("");
                setCor7("");
                break;
            }
            case 5: {
                setCor1("");
                setCor2("");
                setCor3("");
                setCor4("");
                setCor5("");
                setCor6("color:white; background: dodgerblue!important;");
                setCor7("");
                break;
            }
            case 6: {
                setCor1("");
                setCor2("");
                setCor3("");
                setCor4("");
                setCor5("");
                setCor6("");
                setCor7("color:white; background: dodgerblue!important;");
                break;
            }
        }
        limpaLista();
    }

    public void refreshForm() {
        listaH = new ArrayList();
    }

    public Horarios getFeriados() {
        return horarios;
    }

    public void setFeriados(Horarios horarios) {
        this.setHorarios(horarios);
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public boolean isChkReplicarTodos() {
        return chkReplicarTodos;
    }

    public void setChkReplicarTodos(boolean chkReplicarTodos) {
        this.chkReplicarTodos = chkReplicarTodos;
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

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getIdSemana() {
        return idSemana;
    }

    public void setIdSemana(int idSemana) {
        this.idSemana = idSemana;
    }

    public String getCor1() {
        return cor1;
    }

    public void setCor1(String cor1) {
        this.cor1 = cor1;
    }

    public String getCor2() {
        return cor2;
    }

    public void setCor2(String cor2) {
        this.cor2 = cor2;
    }

    public String getCor3() {
        return cor3;
    }

    public void setCor3(String cor3) {
        this.cor3 = cor3;
    }

    public String getCor4() {
        return cor4;
    }

    public void setCor4(String cor4) {
        this.cor4 = cor4;
    }

    public String getCor5() {
        return cor5;
    }

    public void setCor5(String cor5) {
        this.cor5 = cor5;
    }

    public String getCor6() {
        return cor6;
    }

    public void setCor6(String cor6) {
        this.cor6 = cor6;
    }

    public String getCor7() {
        return cor7;
    }

    public void setCor7(String cor7) {
        this.cor7 = cor7;
    }
}
