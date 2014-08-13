package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Patronal;
import br.com.rtools.arrecadacao.PisoSalarial;
import br.com.rtools.arrecadacao.PisoSalarialLote;
import br.com.rtools.arrecadacao.db.WebREPISDB;
import br.com.rtools.arrecadacao.db.WebREPISDBToplink;
import br.com.rtools.financeiro.SalarioMinimo;
import br.com.rtools.financeiro.dao.SalarioMinimoDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Porte;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
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
public class PisoSalarialBean implements Serializable {

    private PisoSalarialLote pisoSalarialLote;
    private PisoSalarial pisoSalarial;
    private List<SelectItem> listComboPorte;
    private List<PisoSalarial> listPisoSalarial;
    private List<PisoSalarialLote> listPisoSalarialLote;
    private int idPorte;
    private int idIndex;
    private String message;
    private String messageErro;
    private String valor;
    private String ano;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String descricao;
    private SalarioMinimo salarioMinimo;

    @PostConstruct
    public void init() {
        listComboPorte = new ArrayList<SelectItem>();
        listPisoSalarial = new ArrayList<PisoSalarial>();
        listPisoSalarialLote = new ArrayList<PisoSalarialLote>();
        idPorte = 0;
        message = "";
        messageErro = "";
        valor = "0,00";
        ano = DataHoje.livre(DataHoje.dataHoje(), "yyyy");
        idIndex = -1;
        pisoSalarialLote = new PisoSalarialLote();
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        pisoSalarial = new PisoSalarial();
        descricao = "";
        SalarioMinimoDao smd = new SalarioMinimoDao();
        salarioMinimo = smd.salarioMinimoVigente();
        if (salarioMinimo == null) {
            salarioMinimo = new SalarioMinimo();
        }
        valor = salarioMinimo.getValorMensalString();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("pisoSalarialBean");
        GenericaSessao.remove("patronalPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("pisoSalarialBean");
    }

    public String novo() {
        pisoSalarialLote = new PisoSalarialLote();
        listPisoSalarialLote.clear();
        pisoSalarial = new PisoSalarial();
        listPisoSalarial.clear();
        listComboPorte.clear();
        valor = "0,00";
        descricao = "";
        return "pisoSalarial";
    }

    public String novoTela() {
        pisoSalarialLote = new PisoSalarialLote();
        listPisoSalarialLote.clear();
        pisoSalarial = new PisoSalarial();
        listPisoSalarial.clear();
        listComboPorte.clear();
        valor = "0,00";
        descricao = "";
        message = "";
        return "pisoSalarial";
    }

    public void limpar() {
        listPisoSalarial.clear();
        novo();
    }

    public void save() {
        if (pisoSalarialLote.getPatronal().getPessoa().getId() == -1) {
            message = "Pesquisar uma pessoa patronal!";
            return;
        }
        if (pisoSalarialLote.getAno() < 0) {
            message = "Informe o ano!";
            return;
        }
        pisoSalarialLote.setAno(Integer.parseInt(ano));

        if (pisoSalarialLote.getValidade().isEmpty() || pisoSalarialLote.getValidade().length() < 10) {
            message = "Informe uma data de validade!";
            return;
        }

        DaoInterface di = new Dao();
        pisoSalarialLote.setPorte((Porte) di.find(new Porte(), Integer.parseInt(listComboPorte.get(idPorte).getDescription())));
        di.openTransaction();
        NovoLog novoLog = new NovoLog();
        if (pisoSalarialLote.getId() == -1) {

            if (valor.equals("0,00") || valor.isEmpty()) {
                message = "Informe o valor salarial!";
                return;
            }

            if (descricao.isEmpty()) {
                message = "Adicione uma descrição (Pisos / Valores)!";
                return;
            }

            if (!di.save(pisoSalarialLote)) {
                message = "Erro ao salvar lote!";
                return;
            }
            pisoSalarial.setPisoSalarialLote(pisoSalarialLote);
            pisoSalarial.setValor(Moeda.substituiVirgulaFloat(valor));
            pisoSalarial.setDescricao(descricao);
            if (di.save(pisoSalarial)) {
                di.commit();
                novoLog.save(
                        "ID: " + pisoSalarialLote.getId()
                        + " - Patronal: (" + pisoSalarialLote.getPatronal().getId() + ") - " + pisoSalarialLote.getPatronal().getPessoa().getNome()
                        + " - Porte: " + pisoSalarialLote.getPorte().getDescricao()
                        + " - Ano: " + pisoSalarialLote.getAno()
                        + " - Validade: " + pisoSalarialLote.getValidade()
                        + " - Piso Salarial: (" + pisoSalarial.getId() + ") [ Descrição: " + pisoSalarial.getDescricao() + " - Valor: " + pisoSalarial.getValor() + " ]"
                );
                message = "Novo piso salarial adicionado com sucesso.";
                pisoSalarial = new PisoSalarial();
                listPisoSalarial.clear();
                listPisoSalarialLote.clear();
                return;
            }

            di.rollback();
            message = "Falha ao salvar o piso salarial!";
        } else {
            PisoSalarialLote psl = (PisoSalarialLote) di.find(pisoSalarialLote);
            String beforeUpdate = 
                    "ID: " + psl.getId()
                    + " - Patronal: (" + psl.getPatronal().getId() + ") - " + psl.getPatronal().getPessoa().getNome()
                    + " - Porte: " + psl.getPorte().getDescricao()
                    + " - Ano: " + psl.getAno()
                    + " - Validade: " + psl.getValidade()
            ;    
            if (!di.update(pisoSalarialLote)) {
                message = "Erro ao atualizar lote!";
                return;
            }

            pisoSalarial.setDescricao(descricao);
            pisoSalarial.setValor(Moeda.substituiVirgulaFloat(valor));
            boolean err = false;
            if(pisoSalarial.getId() != -1) {
                if (!di.update(pisoSalarial)) {
                    err = true;
                }
                
            }
            if(!err) {
                novoLog.update(beforeUpdate,
                        "ID: " + pisoSalarialLote.getId()
                        + " - Patronal: (" + pisoSalarialLote.getPatronal().getId() + ") - " + pisoSalarialLote.getPatronal().getPessoa().getNome()
                        + " - Porte: " + pisoSalarialLote.getPorte().getDescricao()
                        + " - Ano: " + pisoSalarialLote.getAno()
                        + " - Validade: " + pisoSalarialLote.getValidade()
                );                
                di.commit();
                message = "Piso salarial atualizado com sucesso.";
                pisoSalarial = new PisoSalarial();
                listPisoSalarial.clear();
                return;
            }
            di.rollback();
            message = "Falha ao salvar o piso salarial!";
        }
    }

    public void addPisoSalariaLote() {
        if (pisoSalarialLote.getId() == -1) {
            GenericaMensagem.warn("Validação", "Cadastrar Piso Salarial!");
            return;
        }
        pisoSalarial = new PisoSalarial();
        pisoSalarial.setPisoSalarialLote(pisoSalarialLote);
        pisoSalarial.setDescricao(descricao);
        pisoSalarial.setValor(Moeda.substituiVirgulaFloat(valor));
        for (int i = 0; i < listPisoSalarial.size(); i++) {
            if (listPisoSalarial.get(i).getDescricao().equals(pisoSalarial.getDescricao())
                    && listPisoSalarial.get(i).getValor() == pisoSalarial.getValor()
                    && listPisoSalarial.get(i).getPisoSalarialLote().getId() == pisoSalarial.getPisoSalarialLote().getId()) {
                GenericaMensagem.warn("Validação", "Piso salarial já cadastrado!");
                return;
            }
        }
        if (descricao.equals("")) {
            GenericaMensagem.warn("Validação", "Adicione uma descrição!");
            return;
        }
        if (valor.equals("0,00") || valor.isEmpty() || Moeda.converteUS$(valor) == 0) {
            GenericaMensagem.warn("Validação", "Informe o valor salarial!");
            return;
        }

        DaoInterface di = new Dao();
        if (!di.save(pisoSalarial, true)) {
            message = "Erro ao adicionar Piso Salarial!";
            GenericaMensagem.warn("Erro", " Ao adicionar Piso Salarial!!");
            return;
        }
        GenericaMensagem.info("Sucesso", "Piso Salarial adicionado");
        listPisoSalarial.clear();
    }

    public void deletePisoSalariaLote() {
        if (pisoSalarialLote.getId() != -1) {
            DaoInterface di = new Dao();
            NovoLog novoLog = new NovoLog();
            di.openTransaction();
            for (int i = 0; i < listPisoSalarial.size(); i++) {
                if (!di.delete(listPisoSalarial.get(i))) {
                    message = "Erro ao excluir Piso salarial!";
                    di.rollback();
                    return;
                }
            }
            
            if (di.delete(pisoSalarialLote)) {
                novoLog.delete(
                        "ID: " + pisoSalarialLote.getId()
                        + " - Patronal: (" + pisoSalarialLote.getPatronal().getId() + ") - " + pisoSalarialLote.getPatronal().getPessoa().getNome()
                        + " - Porte: " + pisoSalarialLote.getPorte().getDescricao()
                        + " - Ano: " + pisoSalarialLote.getAno()
                        + " - Validade: " + pisoSalarialLote.getValidade()
                        + " - Pisos salariais: [" + listPisoSalarial.toString() + "] "
                );
                di.commit();
                novo();
                message = "Registro excluído com sucesso.";
                return;
            }
            di.rollback();
            message = "Falha ao excluir este registro!";
        }
    }

    public void delete(PisoSalarial pisoSalarial) {
        DaoInterface di = new Dao();
        di.openTransaction();
        if (!di.delete(pisoSalarial)) {
            message = "Erro ao excluir PisoSalarial!";
            di.rollback();
            return;
        }
        listPisoSalarial.clear();
        di.commit();
        message = "Piso Salarial excluído com sucesso!";
        listPisoSalarial.clear();
    }

    public String edit(PisoSalarialLote psl) {
        pisoSalarialLote = psl;
        listPisoSalarial = new WebREPISDBToplink().listaPisoSalarialLote(pisoSalarialLote.getId());
        ano = String.valueOf(pisoSalarialLote.getAno());
        for (int i = 0; i < listComboPorte.size(); i++) {
            if (pisoSalarialLote.getPorte().getId() == Integer.parseInt(listComboPorte.get(i).getDescription())) {
                idPorte = i;
                break;
            }
        }
        GenericaSessao.put("linkClicado", true);
        if (!GenericaSessao.exists("urlRetorno")) {
            return "pisoSalarial";
        } else {
            return GenericaSessao.getString("urlRetorno");
        }
    }

    public String editPisoSalarial(PisoSalarial ps) {
        for (int i = 0; i < listPisoSalarial.size(); i++) {
            if (listPisoSalarial.get(i).getId() == ps.getId()) {
                pisoSalarial = listPisoSalarial.get(i);
                valor = Moeda.converteR$Float(listPisoSalarial.get(i).getValor());
                descricao = listPisoSalarial.get(i).getDescricao();
                break;
            }
        }
        return null;
    }

    public List<SelectItem> getListComboPorte() {
        if (listComboPorte.isEmpty()) {
            DaoInterface di = new Dao();
            List<Porte> list = (List<Porte>) di.list(new Porte());
            for (int i = 0; i < list.size(); i++) {
                listComboPorte.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listComboPorte;
    }

    public void setListComboPorte(List<SelectItem> listComboPorte) {
        this.listComboPorte = listComboPorte;
    }

    public List<PisoSalarial> getListPisoSalarial() {
        if (listPisoSalarial.isEmpty() && pisoSalarialLote.getId() != -1) {
            WebREPISDB di = new WebREPISDBToplink();
            listPisoSalarial = di.listaPisoSalarialLote(pisoSalarialLote.getId());
        }
        return listPisoSalarial;
    }

    public void setListPisoSalarial(List<PisoSalarial> listPisoSalarial) {
        this.listPisoSalarial = listPisoSalarial;
    }

    public int getIdPorte() {
        return idPorte;
    }

    public void setIdPorte(int idPorte) {
        this.idPorte = idPorte;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgErro() {
        return messageErro;
    }

    public void setMsgErro(String messageErro) {
        this.messageErro = messageErro;
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

    public List<PisoSalarialLote> getListPisoSalarialLote() {
        if (listPisoSalarialLote.isEmpty()) {
            DaoInterface di = new Dao();
            listPisoSalarialLote = di.list(new PisoSalarialLote());
        }
        return listPisoSalarialLote;
    }

    public void setListPisoSalarialLote(List<PisoSalarialLote> listPisoSalarialLote) {
        this.listPisoSalarialLote = listPisoSalarialLote;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public PisoSalarialLote getPisoSalarialLote() {
        if (GenericaSessao.exists("patronalPesquisa")) {
            pisoSalarialLote.setPatronal((Patronal) GenericaSessao.getObject("patronalPesquisa", true));
        }
        return pisoSalarialLote;
    }

    public void setPisoSalarialLote(PisoSalarialLote pisoSalarialLote) {
        this.pisoSalarialLote = pisoSalarialLote;
    }

    public SalarioMinimo getSalarioMinimo() {
        return salarioMinimo;
    }

    public void setSalarioMinimo(SalarioMinimo salarioMinimo) {
        this.salarioMinimo = salarioMinimo;
    }
}
