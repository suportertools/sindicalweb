package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.*;
import br.com.rtools.financeiro.db.ContaBancoDB;
import br.com.rtools.financeiro.db.ContaBancoDBToplink;
import br.com.rtools.financeiro.db.PlanoDB;
import br.com.rtools.financeiro.db.PlanoDBToplink;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class PlanoJSFBean {

    private Plano plano = new Plano();
    private Plano2 plano2 = new Plano2();
    private Plano3 plano3 = new Plano3();
    private Plano4 plano4 = new Plano4();
    private Plano5 plano5 = new Plano5();
    private int idIndex = -1;
    private List<DataObject> listaPlano = new ArrayList<DataObject>();
    private boolean limpar = false;
    private String msgConfirma = "";
    private String radioPlano = "pl5";
    private String divEditarPlano = "divPlano5";
    private String divNovoPlano = "divPlano5";
    private boolean radioPlanoVisivel = false;
    private int idPlanoConta;
    private List<SelectItem> listaContaBanco = new ArrayList();
    private List<Plano5> listaPlanoPorPesquisa = new ArrayList();
    private String descPesquisa = "";
    private String porPesquisa = "conta";
    private String comoPesquisa = "";
    private String cor1 = "";
    private String cor2 = "";
    private String cor3 = "";
    private String cor4 = "";
    private String cor5 = "";

    public String novo() {
        plano = new Plano();
        plano2 = new Plano2();
        plano3 = new Plano3();
        plano4 = new Plano4();
        plano5 = new Plano5();
        return "plano";
    }

    public String salvar() {
        SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
        db.abrirTransacao();

        if (radioPlano.equals("pl1") && divNovoPlano.equals("divPlano")) {
            if (!salvarPlano(db)) {
                db.desfazerTransacao();
                return null;
            }
        }

        if ((radioPlano.equals("pl2") || radioPlano.equals("pl1")) && divNovoPlano.equals("divPlano2")) {
            if (!salvarPlano2(db)) {
                db.desfazerTransacao();
                return null;
            }

            if (radioPlano.equals("pl1") && divNovoPlano.equals("divPlano2")) {
                radioPlano = "pl2";
            }

        }

        if ((radioPlano.equals("pl3") || radioPlano.equals("pl2")) && divNovoPlano.equals("divPlano3")) {
            if (!salvarPlano3(db)) {
                db.desfazerTransacao();
                return null;
            }

            if (radioPlano.equals("pl2") && divNovoPlano.equals("divPlano3")) {
                radioPlano = "pl3";
            }

        }

        if ((radioPlano.equals("pl4") || radioPlano.equals("pl3")) && divNovoPlano.equals("divPlano4")) {
            if (!salvarPlano4(db)) {
                db.desfazerTransacao();
                return null;
            }

            if (radioPlano.equals("pl3") && divNovoPlano.equals("divPlano4")) {
                radioPlano = "pl4";
            }
        }

        if ((radioPlano.equals("pl5") || radioPlano.equals("pl4")) && divNovoPlano.equals("divPlano5")) {
            if (!salvarPlano5(db)) {
                db.desfazerTransacao();
                return null;
            }

            if (radioPlano.equals("pl4") && divNovoPlano.equals("divPlano5")) {
                radioPlano = "pl5";
            }
        }

        listaPlano.clear();

        db.comitarTransacao();

        return null;
    }

    public String excluir() {
        SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
        db.abrirTransacao();

        if (radioPlano.equals("pl1")) {
            if (!excluirPlano(db)) {
                db.desfazerTransacao();
                return null;
            }
        }

        if (radioPlano.equals("pl2")) {
            if (!excluirPlano2(db)) {
                db.desfazerTransacao();
                return null;
            }
        }

        if (radioPlano.equals("pl3")) {
            if (!excluirPlano3(db)) {
                db.desfazerTransacao();
                return null;
            }
        }

        if (radioPlano.equals("pl4")) {
            if (!excluirPlano4(db)) {
                db.desfazerTransacao();
                return null;
            }
        }

        if (radioPlano.equals("pl5")) {
            if (!excluirPlano5(db)) {
                db.desfazerTransacao();
                return null;
            }
        }

        listaPlano.clear();
        db.comitarTransacao();

        return null;
    }

    public String acaoPlano(String valor) {
        radioPlano = valor;
        listaPlano.clear();
        if (valor.equals("pl1")) {
            radioPlanoVisivel = true;
        } else {
            radioPlanoVisivel = false;
        }
        return "plano";
    }

    public String editar() {
        if (listaPlano.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano")) {
            plano = (Plano) listaPlano.get(idIndex).getArgumento0();
        } else if (listaPlano.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano2")) {
            plano2 = (Plano2) listaPlano.get(idIndex).getArgumento0();
        } else if (listaPlano.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano3")) {
            plano3 = (Plano3) listaPlano.get(idIndex).getArgumento0();
        } else if (listaPlano.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano4")) {
            plano4 = (Plano4) listaPlano.get(idIndex).getArgumento0();
        } else if (listaPlano.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano5")) {
            plano5 = (Plano5) listaPlano.get(idIndex).getArgumento0();
        }
        return null;
    }

    public String editarPesquisa() {
        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        if(listaPlanoPorPesquisa.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano")){
//            plano = (Plano) listaPlanoPorPesquisa.get(idIndex).getArgumento0();
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pesquisaPlano", plano);
//        }else if(listaPlanoPorPesquisa.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano2")){
//            plano2 = (Plano2) listaPlanoPorPesquisa.get(idIndex).getArgumento0();
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pesquisaPlano", plano2);
//        }else if(listaPlanoPorPesquisa.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano3")){
//            plano3 = (Plano3) listaPlanoPorPesquisa.get(idIndex).getArgumento0();
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pesquisaPlano", plano3);
//        }else if(listaPlanoPorPesquisa.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano4")){
//            plano4 = (Plano4) listaPlanoPorPesquisa.get(idIndex).getArgumento0();
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pesquisaPlano", plano4);
//        }else if(listaPlanoPorPesquisa.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano5")){
//        }
        plano5 = (Plano5) listaPlanoPorPesquisa.get(idIndex);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pesquisaPlano", plano5);
        return url;
    }

    public boolean salvarPlano(SalvarAcumuladoDB db) {
        if (plano.getId() == -1) {
            if (db.inserirObjeto(plano)) {
                msgConfirma = "Plano 1 salvo com sucesso!";
            } else {
                return false;
            }
        } else {
            if (db.alterarObjeto(plano)) {
                msgConfirma = "Plano 1 atualizado com sucesso!";
            } else {
                return false;
            }
        }
        //plano = new Plano();
        return true;
    }

    public boolean excluirPlano(SalvarAcumuladoDB db) {
        if (plano.getId() != -1) {
            if (db.deletarObjeto((Plano) db.pesquisaCodigo(plano.getId(), "Plano"))) {
                msgConfirma = "Plano 1 deletado com sucesso!";
            } else {
                msgConfirma = "Erro ao deletar plano 1!";
                return false;
            }
        }
        plano = new Plano();
        return true;
    }

    public boolean salvarPlano2(SalvarAcumuladoDB db) {
        if (plano2.getId() == -1) {
            plano2.setNumero(plano.getNumero() + "." + plano2.getNumero());
            if (db.inserirObjeto(plano2)) {
                msgConfirma = "Plano 2 salvo com sucesso";
            } else {
                msgConfirma = "Falha ao inserir o Plano 2";
                return false;
            }
        } else {
            if (db.alterarObjeto(plano2)) {
                msgConfirma = "Plano 2 atualizado com sucesso";
            } else {
                msgConfirma = "Falha ao atualizar o Plano 2";
                return false;
            }
        }
        //plano2 = new Plano2();
        return true;
    }

    public boolean excluirPlano2(SalvarAcumuladoDB db) {
        if (plano2.getId() != -1) {
            if (db.deletarObjeto((Plano2) db.pesquisaCodigo(plano2.getId(), "Plano2"))) {
                msgConfirma = "Plano 2 deletado com sucesso!";
            } else {
                msgConfirma = "Erro ao deletar plano 2!";
                return false;
            }
        }
        plano2 = new Plano2();
        return true;
    }

    public boolean salvarPlano3(SalvarAcumuladoDB db) {
        if (plano3.getId() == -1) {
            plano3.setNumero(plano2.getNumero() + "." + plano3.getNumero());
            if (db.inserirObjeto(plano3)) {
                msgConfirma = "Plano 3 Salvo com sucesso";
            } else {
                msgConfirma = "Falha ao inserir o Plano 3";
                return false;
            }
        } else {
            if (db.alterarObjeto(plano3)) {
                msgConfirma = "Plano 3 atualizado com sucesso";
            } else {
                msgConfirma = "Falha ao atualizar o Plano 3";
                return false;
            }
        }
        //plano3 = new Plano3();
        return true;
    }

    public boolean excluirPlano3(SalvarAcumuladoDB db) {
        if (plano3.getId() != -1) {
            if (db.deletarObjeto((Plano3) db.pesquisaCodigo(plano3.getId(), "Plano3"))) {
                msgConfirma = "Plano 3 deletado com sucesso!";
            } else {
                msgConfirma = "Erro ao deletar plano 3!";
                return false;
            }
        }
        plano3 = new Plano3();
        return true;
    }

    public boolean salvarPlano4(SalvarAcumuladoDB db) {
        if (plano4.getId() == -1) {
            plano4.setNumero(plano3.getNumero() + "." + plano4.getNumero());
            if (db.inserirObjeto(plano4)) {
                msgConfirma = "Plano 4 Salvo com sucesso";
            } else {
                msgConfirma = "Falha ao inserir o Plano 4";
                return false;
            }
        } else {
            if (db.alterarObjeto(plano4)) {
                msgConfirma = "Plano 4 atualizar com sucesso";
            } else {
                msgConfirma = "Falha ao atualizar o Plano 4";
                return false;
            }

        }
        //plano4 = new Plano4();
        return true;
    }

    public boolean excluirPlano4(SalvarAcumuladoDB db) {
        if (plano4.getId() != -1) {
            if (db.deletarObjeto((Plano4) db.pesquisaCodigo(plano4.getId(), "Plano4"))) {
                msgConfirma = "Plano 4 deletado com sucesso!";
            } else {
                msgConfirma = "Erro ao deletar plano 4!";
                return false;
            }
        }
        plano4 = new Plano4();
        return true;
    }

    public boolean salvarPlano5(SalvarAcumuladoDB db) {
        if (plano5.getId() == -1) {
            plano5.setContaBanco(null);
            if (db.inserirObjeto(plano5)) {
                msgConfirma = "Plano 5 Salvo com sucesso";
            } else {
                msgConfirma = "Falha ao inserir o Plano 5";
                return false;
            }
        } else {
            if (db.alterarObjeto(plano5)) {
                msgConfirma = "Plano 5 atualizar com sucesso";
            } else {
                msgConfirma = "Falha ao atualizar o Plano 5";
                return false;
            }
        }
        //plano5 = new Plano5();
        return true;
    }

    public boolean excluirPlano5(SalvarAcumuladoDB db) {
        if (plano5.getId() != -1) {
            if (db.deletarObjeto((Plano5) db.pesquisaCodigo(plano5.getId(), "Plano5"))) {
                msgConfirma = "Plano 5 deletado com sucesso!";
            } else {
                msgConfirma = "Erro ao deletar plano 5!";
                return false;
            }
        }
        plano5 = new Plano5();
        return true;
    }

    public String adicionarPlano1() {
        plano = new Plano();
        divNovoPlano = "divPlano";
        return null;
    }

    public String adicionarNovo() {
        if (listaPlano.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano")) {
            plano2 = new Plano2();

            plano = (Plano) listaPlano.get(idIndex).getArgumento0();
            plano2.setPlano(plano);
            divNovoPlano = "divPlano2";
        } else if (listaPlano.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano2")) {
            plano3 = new Plano3();

            plano2 = (Plano2) listaPlano.get(idIndex).getArgumento0();
            plano3.setPlano2(plano2);
            divNovoPlano = "divPlano3";
        } else if (listaPlano.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano3")) {
            plano4 = new Plano4();

            plano3 = (Plano3) listaPlano.get(idIndex).getArgumento0();
            plano4.setPlano3(plano3);
            divNovoPlano = "divPlano4";
        } else if (listaPlano.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano4")) {
            plano5 = new Plano5();

            plano4 = (Plano4) listaPlano.get(idIndex).getArgumento0();
            plano5.setPlano4(plano4);
            divNovoPlano = "divPlano5";
        } else if (listaPlano.get(idIndex).getArgumento0().getClass().getSimpleName().equals("Plano5")) {
            plano5 = (Plano5) listaPlano.get(idIndex).getArgumento0();
            divNovoPlano = "divPlanox";
        }
        return null;
    }

    public String ok() {

        return "plano";
    }
//
//    public void limpar(String valor){
//        if(limpar == true){
//            novo();
//        }
//        if(valor.equals("")){
//            if(radioPlano.equals("pl1")){
//                radioPlano = "pl2";
//                divNovoPlano = "divPlano2";
//            }else if(radioPlano.equals("pl2")){
//                radioPlano = "pl3";
//                divNovoPlano = "divPlano3";
//            }else if(radioPlano.equals("pl3")){
//                radioPlano = "pl4";
//                divNovoPlano = "divPlano4";
//            }else if(radioPlano.equals("pl4")){
//                radioPlano = "pl5";
//                divNovoPlano = "divPlano5";
//            }else if(radioPlano.equals("pl5")){
//                radioPlano = "pl5";
//                divNovoPlano = "fail";
//            }
//        }else{
//            plano = new Plano();
//            radioPlano = "pl1";
//            divNovoPlano = "divPlano1";
//        }
//    }

    public List<DataObject> getListaPlano() {
        PlanoDB db = new PlanoDBToplink();

        List listaAuxiliar = new ArrayList();

        if (listaPlano.isEmpty()) {

            if (radioPlano.equals("pl1")) {
                listaAuxiliar = db.pesquisaPlanos("Plano");
                for (int i = 0; i < listaAuxiliar.size(); i++) {
                    listaPlano.add(new DataObject(((Plano) listaAuxiliar.get(i)), ((Plano) listaAuxiliar.get(i)).getNumero() + " " + (((Plano) listaAuxiliar.get(i)).getConta()), "divPlano", "Novo Plano 2", null, null));
                }

            } else if (radioPlano.equals("pl2")) {
                listaAuxiliar = db.pesquisaPlanos("Plano2");
                for (int i = 0; i < listaAuxiliar.size(); i++) {
                    listaPlano.add(new DataObject(((Plano2) listaAuxiliar.get(i)), ((Plano2) listaAuxiliar.get(i)).getNumero() + " " + (((Plano2) listaAuxiliar.get(i)).getConta()), "divPlano2", "Novo Plano 3", null, null));
                }

            } else if (radioPlano.equals("pl3")) {
                listaAuxiliar = db.pesquisaPlanos("Plano3");
                for (int i = 0; i < listaAuxiliar.size(); i++) {
                    listaPlano.add(new DataObject(((Plano3) listaAuxiliar.get(i)), ((Plano3) listaAuxiliar.get(i)).getNumero() + " " + (((Plano3) listaAuxiliar.get(i)).getConta()), "divPlano3", "Novo Plano 4", null, null));
                }

            } else if (radioPlano.equals("pl4")) {
                listaAuxiliar = db.pesquisaPlanos("Plano4");
                for (int i = 0; i < listaAuxiliar.size(); i++) {
                    listaPlano.add(new DataObject(((Plano4) listaAuxiliar.get(i)), ((Plano4) listaAuxiliar.get(i)).getNumero() + " " + (((Plano4) listaAuxiliar.get(i)).getConta()), "divPlano4", "Novo Plano 5", null, null));
                }

            } else if (radioPlano.equals("pl5")) {
                listaAuxiliar = db.pesquisaPlanos("Plano5");
                for (int i = 0; i < listaAuxiliar.size(); i++) {
                    listaPlano.add(new DataObject(((Plano5) listaAuxiliar.get(i)), ((Plano5) listaAuxiliar.get(i)).getNumero() + " " + (((Plano5) listaAuxiliar.get(i)).getConta()), "divPlano5", null, null, null));
                }
            }
        }
        return listaPlano;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
    }

    public String divNovoPlano() {
        return divNovoPlano;
    }

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    public Plano2 getPlano2() {
        return plano2;
    }

    public void setPlano2(Plano2 plano2) {
        this.plano2 = plano2;
    }

    public Plano3 getPlano3() {
        return plano3;
    }

    public void setPlano3(Plano3 plano3) {
        this.plano3 = plano3;
    }

    public Plano4 getPlano4() {
        return plano4;
    }

    public void setPlano4(Plano4 plano4) {
        this.plano4 = plano4;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getRadioPlano() {
        return radioPlano;
    }

    public void setRadioPlano(String radioPlano) {
        if (this.radioPlano != radioPlano) {
            listaPlano.clear();
        }
        this.radioPlano = radioPlano;
    }

    public void setListaPlano(List<DataObject> listaPlano) {
        this.listaPlano = listaPlano;
    }

    public String getDivNovoPlano() {
        return divNovoPlano;
    }

    public void setDivNovoPlano(String divNovoPlano) {
        this.divNovoPlano = divNovoPlano;
    }

    public List<SelectItem> getListaContaBanco() {
        ContaBancoDB db = new ContaBancoDBToplink();
        if (listaContaBanco.isEmpty()) {
            int i = 0;
            List select = db.pesquisaTodos();
            while (i < select.size()) {
                listaContaBanco.add(new SelectItem(new Integer(i), (String) ((ContaBanco) select.get(i)).getBanco().getBanco() + " - " + ((ContaBanco) select.get(i)).getAgencia() + " - " + ((ContaBanco) select.get(i)).getConta(), Integer.toString(((ContaBanco) select.get(i)).getId())));
                i++;
            }
        }
        return listaContaBanco;
    }

    public void setListaContaBanco(List<SelectItem> listaContaBanco) {
        this.listaContaBanco = listaContaBanco;
    }

    public int getIdPlanoConta() {
        return idPlanoConta;
    }

    public void setIdPlanoConta(int idPlanoConta) {
        this.idPlanoConta = idPlanoConta;
    }

    public boolean isRadioPlanoVisivel() {
        return radioPlanoVisivel;
    }

    public void setRadioPlanoVisivel(boolean radioPlanoVisivel) {
        this.radioPlanoVisivel = radioPlanoVisivel;
    }

    public String getDivEditarPlano() {
        return divEditarPlano;
    }

    public void setDivEditarPlano(String divEditarPlano) {
        this.divEditarPlano = divEditarPlano;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public List<Plano5> getListaPlanoPorPesquisa() {

        PlanoDB db = new PlanoDBToplink();
        String tipoPlano = "Plano5";

        if (radioPlano.equals("pl1")) {
            tipoPlano = "Plano";
        } else if (radioPlano.equals("pl2")) {
            tipoPlano = "Plano2";
        } else if (radioPlano.equals("pl3")) {
            tipoPlano = "Plano3";
        } else if (radioPlano.equals("pl4")) {
            tipoPlano = "Plano4";
        } else if (radioPlano.equals("pl5")) {
            tipoPlano = "Plano5";
        }

        if (descPesquisa.equals("")) {
            listaPlanoPorPesquisa = new ArrayList();
        } else {
            listaPlanoPorPesquisa = db.pesquisaPorPlano(descPesquisa, porPesquisa, comoPesquisa, tipoPlano);
//
//            for(int i = 0; i < listaAuxiliar.size(); i++){
//                if(radioPlano.equals("pl1")){
//                    listaPlanoPorPesquisa.add(new DataObject(((Plano) listaAuxiliar.get(i)), ((Plano)listaAuxiliar.get(i)).getId(), ((Plano)listaAuxiliar.get(i)).getConta(), ((Plano) listaAuxiliar.get(i)).getNumero(), null, null));
//                }else if(radioPlano.equals("pl2")){
//                    listaPlanoPorPesquisa.add(new DataObject(((Plano2) listaAuxiliar.get(i)), ((Plano2)listaAuxiliar.get(i)).getId(), ((Plano2)listaAuxiliar.get(i)).getConta(), ((Plano2) listaAuxiliar.get(i)).getNumero(), null, null));
//                }else if(radioPlano.equals("pl3")){
//                    listaPlanoPorPesquisa.add(new DataObject(((Plano3) listaAuxiliar.get(i)), ((Plano3)listaAuxiliar.get(i)).getId(), ((Plano3)listaAuxiliar.get(i)).getConta(), ((Plano3) listaAuxiliar.get(i)).getNumero(), null, null));
//                }else if(radioPlano.equals("pl4")){
//                    listaPlanoPorPesquisa.add(new DataObject(((Plano4) listaAuxiliar.get(i)), ((Plano4)listaAuxiliar.get(i)).getId(), ((Plano4)listaAuxiliar.get(i)).getConta(), ((Plano4) listaAuxiliar.get(i)).getNumero(), null, null));
//                }else if(radioPlano.equals("pl5")){
//                    listaPlanoPorPesquisa.add(new DataObject(((Plano5) listaAuxiliar.get(i)), ((Plano5)listaAuxiliar.get(i)).getId(), ((Plano5)listaAuxiliar.get(i)).getConta(), ((Plano5) listaAuxiliar.get(i)).getNumero(), null, null));
//                }
//            }
        }

        return listaPlanoPorPesquisa;
    }

    public void setListaPlanoPorPesquisa(List<Plano5> listaPlanoPorPesquisa) {
        this.listaPlanoPorPesquisa = listaPlanoPorPesquisa;
    }

    public String getCor1() {
        if (radioPlano.equals("pl1")) {
            cor1 = "color:white; background: dodgerblue;";
        } else {
            cor1 = "";
        }
        return cor1;
    }

    public void setCor1(String cor1) {
        this.cor1 = cor1;
    }

    public String getCor2() {
        if (radioPlano.equals("pl2")) {
            cor2 = "color:white; background: dodgerblue;";
        } else {
            cor2 = "";
        }
        return cor2;
    }

    public void setCor2(String cor2) {
        this.cor2 = cor2;
    }

    public String getCor3() {
        if (radioPlano.equals("pl3")) {
            cor3 = "color:white; background: dodgerblue;";
        } else {
            cor3 = "";
        }
        return cor3;
    }

    public void setCor3(String cor3) {
        this.cor3 = cor3;
    }

    public String getCor4() {
        if (radioPlano.equals("pl4")) {
            cor4 = "color:white; background: dodgerblue;";
        } else {
            cor4 = "";
        }
        return cor4;
    }

    public void setCor4(String cor4) {
        this.cor4 = cor4;
    }

    public String getCor5() {
        if (radioPlano.equals("pl5")) {
            cor5 = "color:white; background: dodgerblue;";
        } else {
            cor5 = "";
        }
        return cor5;
    }

    public void setCor5(String cor5) {
        this.cor5 = cor5;
    }
}