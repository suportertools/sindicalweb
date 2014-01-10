package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Patronal;
import br.com.rtools.arrecadacao.PisoSalarial;
import br.com.rtools.arrecadacao.PisoSalarialLote;
import br.com.rtools.arrecadacao.db.WebREPISDB;
import br.com.rtools.arrecadacao.db.WebREPISDBToplink;
import br.com.rtools.pessoa.Porte;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class PisoSalarialJSFBean {

    private List<SelectItem> listaComboPorte = new Vector<SelectItem>();
    private List<PisoSalarial> listaPisoSalarial = new ArrayList();
    private List<PisoSalarialLote> listaPisoSalarialLote = new ArrayList();
    private int idPorte = 0;
    private String msgConfirma = "";
    private String msgErro = "";
    private String msg = "";
    private String valor = "0,00";
    private String ano = DataHoje.livre(DataHoje.dataHoje(), "yyyy");
    private int idIndex = -1;
    //private PisoSalarial pisoSalarial = new PisoSalarial();
    private PisoSalarialLote pisoSalarialLote = new PisoSalarialLote();
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "";
    private PisoSalarial ps = new PisoSalarial();
    private String descricao = "";

    public String novo() {
        pisoSalarialLote = new PisoSalarialLote();
        listaPisoSalarialLote.clear();
        ps = new PisoSalarial();
        listaPisoSalarial.clear();
        listaComboPorte.clear();
        valor = "0,00";
        descricao = "";
        return "pisoSalarial";
    }

    public String novoTela() {
        pisoSalarialLote = new PisoSalarialLote();
        listaPisoSalarialLote.clear();
        ps = new PisoSalarial();
        listaPisoSalarial.clear();
        listaComboPorte.clear();
        valor = "0,00";
        descricao = "";
        msg = "";
        return "pisoSalarial";
    }

    public void limpar() {
        listaPisoSalarial.clear();
        novo();
    }

    public String salvar() {
        if (pisoSalarialLote.getPatronal().getPessoa().getId() == -1) {
            msg = "Pesquisar uma pessoa patronal!";
            return null;
        }
        if (descricao.equals("")) {
            msg = "Adicione uma descrição!";
            return null;
        }

        if (pisoSalarialLote.getAno() < 0) {
            msg = "Informe o ano!";
            return null;
        }
        pisoSalarialLote.setAno(Integer.parseInt(ano));

        if (valor.equals("0,00") || valor.isEmpty()) {
            msg = "Informe o valor salarial!";
            return null;
        }



        if (pisoSalarialLote.getValidade().isEmpty() || pisoSalarialLote.getValidade().length() < 10) {
            msg = "Informe uma data de validade!";
            return null;
        }

        SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();

        pisoSalarialLote.setPorte((Porte) db.pesquisaObjeto(Integer.parseInt(listaComboPorte.get(idPorte).getDescription()), "Porte"));
        db.abrirTransacao();

        if (pisoSalarialLote.getId() == -1) {
            if (!db.inserirObjeto(pisoSalarialLote)) {
                msg = "Erro ao salvar lote!";
                return null;
            }
            ps.setPisoSalarialLote(pisoSalarialLote);
            ps.setValor(Moeda.substituiVirgulaFloat(valor));
            ps.setDescricao(descricao);

            if (db.inserirObjeto(ps)) {
                db.comitarTransacao();
                msg = "Novo piso salarial adicionado com sucesso.";
                ps = new PisoSalarial();
                listaPisoSalarial.clear();
                listaPisoSalarialLote.clear();
                return null;
            }

            db.desfazerTransacao();
            msg = "Falha ao salvar o piso salarial!";
        } else {
            if (!db.alterarObjeto(pisoSalarialLote)) {
                msg = "Erro ao atualizar lote!";
                return null;
            }

            ps.setDescricao(descricao);
            ps.setValor(Moeda.substituiVirgulaFloat(valor));

            if (db.alterarObjeto(ps)) {
                db.comitarTransacao();
                msg = "Piso salarial atualizado com sucesso.";
                ps = new PisoSalarial();
                listaPisoSalarial.clear();
                return null;
            }

            db.desfazerTransacao();
            msg = "Falha ao salvar o piso salarial!";
        }
        return null;
    }

    public String addMais() {
        if (pisoSalarialLote.getId() == -1) {
            return null;
        }

        ps = new PisoSalarial();

        ps.setPisoSalarialLote(pisoSalarialLote);
        ps.setDescricao(descricao);
        ps.setValor(Moeda.substituiVirgulaFloat(valor));

        if (descricao.equals("")) {
            msg = "Adicione uma descrição!";
            return null;
        }

        if (valor.equals("0,00") || valor.isEmpty() || Moeda.converteUS$(valor) == 0) {
            msg = "Informe o valor salarial!";
            return null;
        }


        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (!sv.inserirObjeto(ps)) {
            msg = "Erro ao adicionar Piso Salarial!";
            sv.desfazerTransacao();
            return null;

        }
        sv.comitarTransacao();
        listaPisoSalarial.clear();
        return null;
    }

    public String excluir() {
        if (pisoSalarialLote.getId() != -1) {
            SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
            db.abrirTransacao();
            pisoSalarialLote = (PisoSalarialLote) db.pesquisaObjeto(pisoSalarialLote.getId(), "PisoSalarialLote");
            for (int i = 0; i < listaPisoSalarial.size(); i++) {
                if (!db.deletarObjeto(db.pesquisaCodigo(listaPisoSalarial.get(i).getId(), "PisoSalarial"))) {
                    msg = "Erro ao excluir Piso salarial!";
                    db.desfazerTransacao();
                    return null;
                }
            }

            if (db.deletarObjeto(pisoSalarialLote)) {
                db.comitarTransacao();
                novo();
                msg = "Registro excluído com sucesso.";
                return null;
            }

            db.desfazerTransacao();
            msg = "Falha ao excluir este registro!";
        }
        return null;
    }

    public String excluirPiso(int id_piso) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        for (int i = 0; i < listaPisoSalarial.size(); i++) {
            if (listaPisoSalarial.get(i).getId() == id_piso) {
                if (!sv.deletarObjeto(sv.pesquisaCodigo(listaPisoSalarial.get(i).getId(), "PisoSalarial"))) {
                    msg = "Erro ao excluir PisoSalarial!";
                    sv.desfazerTransacao();
                    return null;
                }
                break;
            }
        }

        sv.comitarTransacao();
        msg = "Piso Salarial excluído com sucesso!";
        listaPisoSalarial.clear();
        return null;
    }

    public String editar() {
        pisoSalarialLote = (PisoSalarialLote) listaPisoSalarialLote.get(idIndex);
        listaPisoSalarial = new WebREPISDBToplink().listaPisoSalarialLote(pisoSalarialLote.getId());
        ano = String.valueOf(pisoSalarialLote.getAno());
        for (int i = 0; i < listaComboPorte.size(); i++) {
            if (pisoSalarialLote.getPorte().getId() == Integer.parseInt(listaComboPorte.get(i).getDescription())) {
                idPorte = i;
                break;
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "pisoSalarial";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public String editarLote(int id) {
        for (int i = 0; i < listaPisoSalarial.size(); i++) {
            if (listaPisoSalarial.get(i).getId() == id) {
                ps = listaPisoSalarial.get(i);
                valor = Moeda.converteR$Float(listaPisoSalarial.get(i).getValor());
                descricao = listaPisoSalarial.get(i).getDescricao();
                break;
            }
        }
        return null;
    }

    public List<SelectItem> getListaComboPorte() {
        if (listaComboPorte.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<Porte> select = null;
            select = dB.listaObjeto("Porte");
            if (select != null) {
                int i = 0;
                while (i < select.size()) {
                    listaComboPorte.add(new SelectItem(new Integer(i), select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
                    i++;
                }
            }
        }
        return listaComboPorte;
    }

    public void setListaComboPorte(List<SelectItem> listaComboPorte) {
        this.listaComboPorte = listaComboPorte;
    }

    public List<PisoSalarial> getListaPisoSalarial() {
        if (listaPisoSalarial.isEmpty() && pisoSalarialLote.getId() != -1) {
            WebREPISDB db = new WebREPISDBToplink();
            listaPisoSalarial = db.listaPisoSalarialLote(pisoSalarialLote.getId());
        }
        return listaPisoSalarial;
    }

    public void setListaPisoSalarial(List<PisoSalarial> listaPisoSalarial) {
        this.listaPisoSalarial = listaPisoSalarial;
    }

    public int getIdPorte() {
        return idPorte;
    }

    public void setIdPorte(int idPorte) {
        this.idPorte = idPorte;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getMsgErro() {
        return msgErro;
    }

    public void setMsgErro(String msgErro) {
        this.msgErro = msgErro;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
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

    public String getValor() {
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = Moeda.substituiVirgula(valor);
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public List<PisoSalarialLote> getListaPisoSalarialLote() {
        if (listaPisoSalarialLote.isEmpty()) {
            SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
            listaPisoSalarialLote = db.listaObjeto("PisoSalarialLote");
        }
        return listaPisoSalarialLote;
    }

    public void setListaPisoSalarialLote(List<PisoSalarialLote> listaPisoSalarialLote) {
        this.listaPisoSalarialLote = listaPisoSalarialLote;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public PisoSalarialLote getPisoSalarialLote() {
        if ((Patronal) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("patronalPesquisa") != null) {
            pisoSalarialLote.setPatronal((Patronal) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("patronalPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("patronalPesquisa");
        }
        return pisoSalarialLote;
    }

    public void setPisoSalarialLote(PisoSalarialLote pisoSalarialLote) {
        this.pisoSalarialLote = pisoSalarialLote;
    }
}