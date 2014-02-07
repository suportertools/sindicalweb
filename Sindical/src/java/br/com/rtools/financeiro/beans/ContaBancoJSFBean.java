package br.com.rtools.financeiro.beans;

import br.com.rtools.endereco.Cidade;
import br.com.rtools.financeiro.Banco;
import br.com.rtools.financeiro.ContaBanco;
import br.com.rtools.financeiro.Indice;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.db.ContaBancoDB;
import br.com.rtools.financeiro.db.ContaBancoDBToplink;
import br.com.rtools.financeiro.db.Plano5DB;
import br.com.rtools.financeiro.db.Plano5DBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.ListaArgumentos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class ContaBancoJSFBean {

    private ContaBanco contaBanco = new ContaBanco();
    private Indice banco = new Indice();
    private String indice;
    private String msgConfirma;
    private int idBanco = 0;
    private int idPlanoContas = 0;
    private int idFilial = 0;
    private int idIndex = -1;
    private Cidade cidade = new Cidade();
    private Banco Sbanco = null;
    private Plano5 plano5 = new Plano5();
    private boolean salvar = false;
    private List<ContaBanco> listaContaBanco = new ArrayList();

    public void removerCidade(){
        cidade = new Cidade();
    }
    
    public String salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Filial filial = (Filial) sv.pesquisaCodigo(Integer.parseInt(getListaFilial().get(idFilial).getDescription()), "Filial");

        contaBanco.setFilial(filial);
        if (cidade == null) {
            msgConfirma = "Pesquise uma Cidade!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (Integer.parseInt(getListaPlano5Conta().get(idPlanoContas).getDescription()) == 0) {
            msgConfirma = "Registro não pode ser salvo sem Plano de Contas!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        Sbanco = (Banco) sv.pesquisaCodigo(Integer.valueOf(getListaBancoCompleta().get(idBanco).getDescription()), "Banco");
        contaBanco.setCidade(cidade);
        contaBanco.setBanco(Sbanco);

        sv.abrirTransacao();
        if (contaBanco.getId() == -1) {
            salvar = true;
            if (!sv.inserirObjeto(contaBanco)) {
                msgConfirma = "Erro ao Salvar!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
                return null;
            }
            msgConfirma = "Conta salva com Sucesso!";
            GenericaMensagem.warn("Sucesso", msgConfirma);
        } else {
            if (!sv.alterarObjeto(contaBanco)) {
                msgConfirma = "Erro ao atualizar Conta!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
                return null;
            }
            msgConfirma = "Conta atualizada com Sucesso!";
            GenericaMensagem.warn("Sucesso", msgConfirma);
        }
        if (!atualizaPlano5Conta(sv)) {
            msgConfirma = "Erro ao atualizar Plano 5!";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
            return null;
        }
        sv.comitarTransacao();
        novo();
        return null;
    }

    public void refreshForm() {
    }

    public boolean atualizaPlano5Conta(SalvarAcumuladoDB sv) {
        plano5 = (Plano5) sv.pesquisaCodigo(Integer.parseInt(getListaPlano5Conta().get(idPlanoContas).getDescription()), "Plano5");
        if (plano5.getId() != -1) {
            plano5.setConta(contaBanco.getBanco().getBanco() + " - " + contaBanco.getConta());
            plano5.setContaBanco(contaBanco);
            if (!sv.alterarObjeto(plano5)) {
                return false;
            }
        }
        return true;
    }

    public String excluir() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if (contaBanco.getId() != -1) {
            sv.abrirTransacao();
            if (plano5 != null) {
                plano5.setContaBanco(null);
                plano5.setConta("??????????????????");
                if (!sv.alterarObjeto(plano5)) {
                    msgConfirma = "Erro ao atualizar Plano 5!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    sv.desfazerTransacao();
                    return null;
                }
            }
            contaBanco = (ContaBanco) sv.pesquisaCodigo(contaBanco.getId(), "ContaBanco");
            if (!sv.deletarObjeto(contaBanco)) {
                msgConfirma = "Conta não pode ser Excluida!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
                return null;
            } else {
                msgConfirma = "Conta Excluida com Sucesso!";
                GenericaMensagem.warn("Sucesso", msgConfirma);
                sv.comitarTransacao();
            }
            novo();
        }
        return null;
    }

    public List getListaBanco() {
//       Pesquisa pesquisa = new Pesquisa();
        ListaArgumentos por = new ListaArgumentos();
        por.adicionarObjeto(new Vector<List>(), "numero", "S");
        por.adicionarObjeto(new Vector<List>(), "banco", "S");
        List result = null;
//       result = pesquisa.pesquisar("Banco", "banco" , descPesquisa, "banco", comoPesquisa);
//       result = pesquisa.pesquisarComArgumentos("Indice", "banco" , descPesquisa, por , comoPesquisa, Integer.parseInt(indice));
        return result;
    }

    public List<ContaBanco> getListaContaBanco() {
        if (listaContaBanco.isEmpty()) {
            ContaBancoDB db = new ContaBancoDBToplink();
            listaContaBanco = db.pesquisaTodos();
        }
        return listaContaBanco;
    }

    public void setListaContaBanco(List<ContaBanco> listaContaBanco) {
        this.listaContaBanco = listaContaBanco;
    }

    public String editar(ContaBanco cb) {
        contaBanco = cb;
        Plano5DB db = new Plano5DBToplink();
        plano5 = db.pesquisaPlano5IDContaBanco(contaBanco.getId());

        if (plano5 != null && plano5.getId() != -1) {
            int qnt = getListaPlano5Conta().size();
            for (int i = 0; i < qnt; i++) {
                if (Integer.parseInt(getListaPlano5Conta().get(i).getDescription()) == plano5.getId()) {
                    idPlanoContas = i;
                }
            }
        }

        if (contaBanco != null && contaBanco.getBanco().getId() != -1) {
            int qnt = getListaBancoCompleta().size();
            for (int i = 0; i < qnt; i++) {
                if (Integer.valueOf(getListaBancoCompleta().get(i).getDescription()) == contaBanco.getBanco().getId()) {
                    idBanco = i;
                }
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contaBancoPesquisa", contaBanco);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno")).equals("menuFinanceiro")) {
            return "contaBanco";
        }
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");

    }

    public List<SelectItem> getListaBancoCompleta() {
        ContaBancoDB contaBancoDB = new ContaBancoDBToplink();
        List<SelectItem> result = new Vector<SelectItem>();
        List bancos = contaBancoDB.pesquisaTodosBancos();
        int i = 0;
        result.add(new SelectItem(new Integer(i), "Nenhum", "0"));
        while (i < bancos.size()) {
            result.add(new SelectItem(new Integer(i + 1),
                    ((Banco) bancos.get(i)).getNumero() + " - " + ((Banco) bancos.get(i)).getBanco(),
                    Integer.toString(((Banco) bancos.get(i)).getId())));
            i++;
        }
        return result;
    }

    public List<SelectItem> getListaFilial() {
//        FilialDB filialDB = new FilialDBToplink();
//        List<Filial> listaFilial = filialDB.pesquisaTodos();
        List<SelectItem> result = new ArrayList<SelectItem>();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        List<Filial> listaFilial = (List<Filial>) salvarAcumuladoDB.listaObjeto("Filial", true);          
        int i = 0;
        while (i < listaFilial.size()) {
            result.add(new SelectItem(
                    new Integer(i),
                    listaFilial.get(i).getFilial().getPessoa().getNome(),
                    Integer.toString(listaFilial.get(i).getId())));
            i++;
        }

        if (contaBanco != null) {
            if (contaBanco.getFilial().getId() != -1) {
                for (i = 0; i < result.size(); i++) {
                    if (Integer.parseInt(result.get(i).getDescription()) == contaBanco.getFilial().getId()) {
                        setIdFilial(i);
                    }
                }
            }
        }
        return result;
    }

    public List<SelectItem> getListaPlano5Conta() {
        ContaBancoDB contaBancoDB = new ContaBancoDBToplink();
        List<SelectItem> result = new Vector<SelectItem>();
        List planoContas;
        if ((contaBanco != null) && (contaBanco.getId() != -1) && salvar == false) {
            planoContas = contaBancoDB.pesquisaPlano5ContaComID(contaBanco.getId());
        } else {
            planoContas = contaBancoDB.pesquisaPlano5Conta();
        }
        int i = 0;
        result.add(new SelectItem(new Integer(i), "Nenhum", "0"));
        while (i < planoContas.size()) {
            result.add(new SelectItem(new Integer(i + 1),
                    ((Plano5) planoContas.get(i)).getConta(),
                    Integer.toString(((Plano5) planoContas.get(i)).getId())));
            i++;
        }
        return result;
    }

    public void novo() {
        contaBanco = new ContaBanco();
        idBanco = 0;
        idPlanoContas = 0;
        salvar = false;
        cidade = new Cidade();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contaBancoPesquisa");
        listaContaBanco.clear();
    }

    public String novoGeral() {
        contaBanco = new ContaBanco();
        cidade = new Cidade();
        idBanco = 0;
        idPlanoContas = 0;
        salvar = false;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contaBancoPesquisa");
        listaContaBanco.clear();
        return "contaBanco";
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

    public ContaBanco getContaBanco() {
        return contaBanco;
    }

    public void setContaBanco(ContaBanco contaBanco) {
        this.contaBanco = contaBanco;
    }

    public Indice getBanco() {
        return banco;
    }

    public void setBanco(Indice banco) {
        this.banco = banco;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public int getIdPlanoContas() {
        return idPlanoContas;
    }

    public void setIdPlanoContas(int idPlanoContas) {
        this.idPlanoContas = idPlanoContas;
    }

    public Cidade getCidade() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa") != null) {
            cidade = (Cidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
        } else if (contaBanco.getId() != -1) {
            cidade = contaBanco.getCidade();
        }
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}
