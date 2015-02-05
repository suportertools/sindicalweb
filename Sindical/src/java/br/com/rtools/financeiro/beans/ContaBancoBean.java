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
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.ListaArgumentos;
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
public class ContaBancoBean implements Serializable {

    private ContaBanco contaBanco;
    private Indice banco;
    private String indice;
    private String msgConfirma;
    private int idBanco;
    private int idPlanoContas;
    private int idFilial;
    private int idIndex;
    private Cidade cidade;
    private Banco Sbanco;
    private Plano5 plano5;
    private boolean salvar;
    private List<ContaBanco> listaContaBanco;
    private List<SelectItem> listaBancoCompleta;
    private List<SelectItem> listaFilial;

    @PostConstruct
    public void init() {
        contaBanco = new ContaBanco();
        banco = new Indice();
        indice = "";
        msgConfirma = "";
        idBanco = 0;
        idPlanoContas = 0;
        idFilial = 0;
        idIndex = -1;
        cidade = new Cidade();
        Sbanco = null;
        plano5 = new Plano5();
        salvar = false;
        listaContaBanco = new ArrayList();
        listaBancoCompleta = new ArrayList();
        listaFilial = new ArrayList();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("contaBancoBean");
        GenericaSessao.remove("contaBancoPesquisa");
        GenericaSessao.remove("cidadePesquisa");
    }

    public void removerCidade() {
        cidade = new Cidade();
    }

    public String salvar() {
        DaoInterface di = new Dao();
        Filial filial = (Filial) di.find(new Filial(), Integer.parseInt(listaFilial.get(idFilial).getDescription()));
        contaBanco.setFilial(filial);
        if (cidade == null) {
            msgConfirma = "Pesquise uma Cidade!";
            GenericaMensagem.warn("Validação", msgConfirma);
            return null;
        }

        if (Integer.parseInt(getListaPlano5Conta().get(idPlanoContas).getDescription()) == 0) {
            msgConfirma = "Registro não pode ser salvo sem Plano de Contas!";
            GenericaMensagem.warn("Validação", msgConfirma);
            return null;
        }

        Sbanco = (Banco) di.find(new Banco(), Integer.parseInt(listaBancoCompleta.get(idBanco).getDescription()));
        contaBanco.setCidade(cidade);
        contaBanco.setBanco(Sbanco);

        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (contaBanco.getId() == -1) {
            salvar = true;
            if (!di.save(contaBanco)) {
                msgConfirma = "Erro ao Salvar!";
                GenericaMensagem.warn("Erro", msgConfirma);
                di.rollback();
                return null;
            }
            novoLog.save(
                    "ID: " + contaBanco.getId()
                    + " - Filial: (" + contaBanco.getFilial().getId() + ") " + contaBanco.getFilial().getFilial().getPessoa().getNome()
                    + " - Cidade: (" + contaBanco.getCidade().getId() + ") " + contaBanco.getCidade().getCidade() + " - UF: " + contaBanco.getCidade().getUf()
                    + " - Banco: (" + contaBanco.getBanco().getId() + ") " + contaBanco.getBanco().getBanco().trim()
                    + " - Agência: " + contaBanco.getAgencia().trim()
                    + " - C. Conta: " + contaBanco.getConta().trim()
            );
            msgConfirma = "Conta salva com Sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
        } else {
            ContaBanco c = (ContaBanco) di.find(contaBanco);
            String beforeUpdate
                    = "ID: " + c.getId()
                    + " - Filial: (" + c.getFilial().getId() + ") " + c.getFilial().getFilial().getPessoa().getNome()
                    + " - Cidade: (" + c.getCidade().getId() + ") " + c.getCidade().getCidade() + " - UF: " + c.getCidade().getUf()
                    + " - Banco: (" + c.getBanco().getId() + ") " + c.getBanco().getBanco().trim()
                    + " - Agência: " + c.getAgencia().trim()
                    + " - C. Conta: " + c.getConta().trim();
            if (!di.update(contaBanco)) {
                msgConfirma = "Erro ao atualizar Conta!";
                GenericaMensagem.warn("Erro", msgConfirma);
                di.rollback();
                return null;
            }
            novoLog.update(beforeUpdate,
                    "ID: " + contaBanco.getId()
                    + " - Filial: (" + contaBanco.getFilial().getId() + ") " + contaBanco.getFilial().getFilial().getPessoa().getNome()
                    + " - Cidade: (" + contaBanco.getCidade().getId() + ") " + contaBanco.getCidade().getCidade() + " - UF: " + contaBanco.getCidade().getUf()
                    + " - Banco: (" + contaBanco.getBanco().getId() + ") " + contaBanco.getBanco().getBanco().trim()
                    + " - Agência: " + contaBanco.getAgencia().trim()
                    + " - C. Conta: " + contaBanco.getConta().trim()
            );
            msgConfirma = "Conta atualizada com Sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
        }
        if (!atualizaPlano5Conta(di)) {
            msgConfirma = "Erro ao atualizar Plano 5!";
            GenericaMensagem.warn("Erro", msgConfirma);
            di.rollback();
            return null;
        }
        di.commit();
        novo();
        return null;
    }

    public void refreshForm() {
    }

    public boolean atualizaPlano5Conta(DaoInterface di) {
        plano5 = (Plano5) di.find(new Plano5(), Integer.parseInt(getListaPlano5Conta().get(idPlanoContas).getDescription()));
        if (plano5.getId() != -1) {
            plano5.setConta(contaBanco.getBanco().getBanco() + " - " + contaBanco.getConta());
            plano5.setContaBanco(contaBanco);
            if (!di.update(plano5)) {
                return false;
            }
        }
        return true;
    }

    public String excluir() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (contaBanco.getId() != -1) {
            di.openTransaction();
            if (plano5 != null) {
                plano5.setContaBanco(null);
                plano5.setConta("??????????????????");
                if (!di.update(plano5)) {
                    msgConfirma = "Erro ao atualizar Plano 5!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    di.rollback();
                    return null;
                }
            }
            if (!di.delete(contaBanco)) {
                msgConfirma = "Conta não pode ser Excluida!";
                GenericaMensagem.warn("Erro", msgConfirma);
                di.rollback();
                return null;
            } else {
                novoLog.delete(
                        "ID: " + contaBanco.getId()
                        + " - Filial: (" + contaBanco.getFilial().getId() + ") " + contaBanco.getFilial().getFilial().getPessoa().getNome()
                        + " - Cidade: (" + contaBanco.getCidade().getId() + ") " + contaBanco.getCidade().getCidade() + " - UF: " + contaBanco.getCidade().getUf()
                        + " - Banco: (" + contaBanco.getBanco().getId() + ") " + contaBanco.getBanco().getBanco().trim()
                        + " - Agência: " + contaBanco.getAgencia().trim()
                        + " - C. Conta: " + contaBanco.getConta().trim()
                );
                msgConfirma = "Conta Excluida com Sucesso!";
                GenericaMensagem.info("Sucesso", msgConfirma);
                di.commit();

            }
            novo();
        }
        return null;
    }

    public List getListaBanco() {
//       Pesquisa pesquisa = new Pesquisa();
        ListaArgumentos por = new ListaArgumentos();
        por.adicionarObjeto(new ArrayList<List>(), "numero", "S");
        por.adicionarObjeto(new ArrayList<List>(), "banco", "S");
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
            int qnt = listaBancoCompleta.size();
            for (int i = 0; i < qnt; i++) {
                if (Integer.valueOf(listaBancoCompleta.get(i).getDescription()) == contaBanco.getBanco().getId()) {
                    idBanco = i;
                }
            }
        }
        GenericaSessao.put("contaBancoPesquisa", contaBanco);
        GenericaSessao.put("linkClicado", true);
        if ((GenericaSessao.getString("urlRetorno")).equals("menuFinanceiro")) {
            return "contaBanco";
        }
        return GenericaSessao.getString("urlRetorno");

    }

    public List<SelectItem> getListaBancoCompleta() {
        if (listaBancoCompleta.isEmpty()){
            DaoInterface di = new Dao();
            List<Banco> list = (List<Banco>) di.list(new Banco());
            int i = 0;
            listaBancoCompleta.add(new SelectItem(i, "Nenhum", "0"));
            for (Banco b : list) {
                listaBancoCompleta.add(new SelectItem(i + 1, b.getNumero() + " - " + b.getBanco(), Integer.toString(b.getId())));
                i++;
            }
        }
        return listaBancoCompleta;
    }

    public List<SelectItem> getListaFilial() {
        if (listaFilial.isEmpty()){
            DaoInterface di = new Dao();
            List<Filial> result = (List<Filial>) di.list(new Filial(), true);
            for (int i = 0; i < result.size(); i++) {
                listaFilial.add(new SelectItem(
                        i,
                        result.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(result.get(i).getId())));
            }
        }
        
        if (contaBanco != null) {
            if (contaBanco.getFilial().getId() != -1) {
                for (int i = 0; i < listaFilial.size(); i++) {
                    if (Integer.parseInt(listaFilial.get(i).getDescription()) == contaBanco.getFilial().getId()) {
                        setIdFilial(i);
                    }
                }
            }
        }
        return listaFilial;
    }

    public List<SelectItem> getListaPlano5Conta() {
        ContaBancoDB contaBancoDB = new ContaBancoDBToplink();
        List<SelectItem> result = new ArrayList();
        List planoContas;
        if ((contaBanco != null) && (contaBanco.getId() != -1) && salvar == false) {
            planoContas = contaBancoDB.pesquisaPlano5ContaComID(contaBanco.getId());
        } else {
            planoContas = contaBancoDB.pesquisaPlano5Conta();
        }
        int i = 0;
        result.add(new SelectItem(i, "Nenhum", "0"));
        while (i < planoContas.size()) {
            result.add(new SelectItem(i + 1,
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
        GenericaSessao.remove("contaBancoPesquisa");
        listaContaBanco.clear();
    }

    public String novoGeral() {
        contaBanco = new ContaBanco();
        cidade = new Cidade();
        idBanco = 0;
        idPlanoContas = 0;
        salvar = false;
        GenericaSessao.remove("contaBancoPesquisa");
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
        if (GenericaSessao.exists("cidadePesquisa")) {
            cidade = (Cidade) GenericaSessao.getObject("cidadePesquisa", true);
        } else if (contaBanco.getId() != -1) {
            cidade = contaBanco.getCidade();
        }
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }
}
