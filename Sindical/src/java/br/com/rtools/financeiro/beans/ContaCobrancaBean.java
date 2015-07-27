package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.ContaBanco;
import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.Layout;
import br.com.rtools.financeiro.db.ContaCobrancaDB;
import br.com.rtools.financeiro.db.ContaCobrancaDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ContaCobrancaBean {

    private ContaCobranca contaCobranca = new ContaCobranca();
    private List<ContaCobranca> listaContaCobranca = new ArrayList();
    private int idLayout = 0;
    private String msgConfirma = "";
    private String repasse = "0.0";
    private String sicas = "";
    private String codigoCedente = "";
    private boolean limpar = false;

    public String salvar() {

        ContaCobrancaDB db = new ContaCobrancaDBToplink();
        Layout la = db.pesquisaLayoutId(Integer.valueOf(getListaLayout().get(idLayout).getDescription()));

        msgConfirma = "";

        contaCobranca.setSicasSindical(sicas);
        contaCobranca.setCodigoSindical(codigoCedente);
        contaCobranca.setLayout(la);
        //contaCobranca.setRepasse( Moeda.substituiVirgulaFloat(repasse) );

        if (contaCobranca.getContaBanco().getBanco().getBanco().isEmpty()) {
            msgConfirma = "Atenção, é preciso pesquisar um Banco!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if ((contaCobranca.getCodCedente().isEmpty()) || (contaCobranca.getCodCedente().equals("0"))) {
            msgConfirma = "Digite um Código Cedente!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (contaCobranca.getCedente().isEmpty()) {
            msgConfirma = "Digite um Cedente!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (contaCobranca.getLocalPagamento().isEmpty()) {
            msgConfirma = "Local de Pagamento não pode ser nulo!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if ((contaCobranca.getBoletoInicial().equals("0")) || (contaCobranca.getBoletoInicial().isEmpty())) {
            msgConfirma = "Boleto Inicial está em branco!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (contaCobranca.getMoeda().isEmpty()) {
            msgConfirma = "O campo Moeda está em branco!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (contaCobranca.getEspecieMoeda().isEmpty()) {
            msgConfirma = "O campo Espécie Moeda está em branco!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (contaCobranca.getEspecieDoc().isEmpty()) {
            msgConfirma = "Digite uma Espécie de Documento!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (contaCobranca.getAceite().isEmpty()) {
            msgConfirma = "Digite um Aceite!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        NovoLog log = new NovoLog();

        if (contaCobranca.getId() == -1) {
            if (db.idContaCobranca(contaCobranca) != null) {
                msgConfirma = "Este cadastro já existe no Sistema.";
                GenericaMensagem.warn("Erro", msgConfirma);
            } else {
                atualizarSicas();
                if (db.insert(contaCobranca)) {
                    log.save("ID: " + contaCobranca.getId() + " Banco: " + contaCobranca.getContaBanco().getBanco().getBanco() + " - Agência: " + contaCobranca.getContaBanco().getAgencia() + " - Conta: " + contaCobranca.getContaBanco().getConta() + " - Cedente: " + contaCobranca.getCedente() + " - Código Cedente: " + contaCobranca.getCodCedente());
                    msgConfirma = "Cadastro salvo com Sucesso!";
                    GenericaMensagem.info("Sucesso", msgConfirma);
                } else {
                    msgConfirma = "Erro ao Salvar!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                }
            }
        } else {
            ContaCobranca conta = new ContaCobranca();
            conta = (ContaCobranca) db.pesquisaCodigo(contaCobranca.getId());
            String antes = "ID: " + conta.getId() + " Banco: " + conta.getContaBanco().getBanco().getBanco() + " - Agência: " + conta.getContaBanco().getAgencia() + " - Conta: " + conta.getContaBanco().getConta() + " - Cedente: " + conta.getCedente() + " - Código Cedente: " + conta.getCodCedente();
            atualizarSicas();
            if (db.update(contaCobranca)) {
                log.update(antes, "ID: " + contaCobranca.getId() + " Banco: " + contaCobranca.getContaBanco().getBanco().getBanco() + " - Agência: " + contaCobranca.getContaBanco().getAgencia() + " - Conta: " + contaCobranca.getContaBanco().getConta() + " - Cedente: " + contaCobranca.getCedente() + " - Código Cedente: " + contaCobranca.getCodCedente());
                msgConfirma = "Cadastro atualizado com sucesso!";
                GenericaMensagem.info("Sucesso", msgConfirma);
            } else {
                msgConfirma = "Falha na atualização do cadastro!";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        }
        limpar = false;
        return null;
    }

    private void atualizarSicas() {
        String codigoSindical = "";
        try {
            FilialDB filialDB = new FilialDao();
            String entidade = filialDB.pesquisaRegistroPorFilial(1).getTipoEntidade();
            codigoSindical = contaCobranca.getCodigoSindical();
            if (entidade.equals("S")) {
                contaCobranca.setSicasSindical(codigoSindical.substring(6, 11));
            } else if (entidade.equals("C")) {
                contaCobranca.setSicasSindical("00" + codigoSindical.substring(0, 3));
            } else if (entidade.equals("F")) {
                contaCobranca.setSicasSindical("00" + codigoSindical.substring(3, 6));
            }
        } catch (Exception e) {
            contaCobranca.setSicasSindical("");
        }
    }

    public String novo() {
        contaCobranca = new ContaCobranca();
        listaContaCobranca.clear();
        sicas = "";
        idLayout = 0;
        repasse = "0.0";
        codigoCedente = "";
        msgConfirma = "";
        limpar = false;
        return "contaCobranca";
    }

    public void limpar() {
        if (limpar == true) {
            novo();
        }
    }

    public String excluir() {
        SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
        if (contaCobranca.getId() != -1) {
            db.abrirTransacao();
            contaCobranca = (ContaCobranca) db.pesquisaCodigo(contaCobranca.getId(), "ContaCobranca");
            NovoLog log = new NovoLog();
            if (db.deletarObjeto(contaCobranca)) {
                log.delete(" ID: " + contaCobranca.getId() + " - Banco: " + contaCobranca.getContaBanco().getBanco().getBanco() + " - Agência: " + contaCobranca.getContaBanco().getAgencia() + " - Conta: " + contaCobranca.getContaBanco().getConta() + " - Cedente: " + contaCobranca.getCedente() + " - Código Cedente: " + contaCobranca.getCodCedente());
                msgConfirma = "Cadastro Excluido com sucesso!";
                GenericaMensagem.info("Sucesso", msgConfirma);
                limpar = true;
                db.comitarTransacao();
            } else {
                db.desfazerTransacao();
                msgConfirma = "Não foi possível excluir esse cadastro. Verifique se há vínculos externos!";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        }
        return null;
    }

    public List<ContaCobranca> getListaContaCobranca() {
        ContaCobrancaDB db = new ContaCobrancaDBToplink();
        listaContaCobranca = db.pesquisaTodos();
        return listaContaCobranca;
    }

    public void setListaContaCobranca(List<ContaCobranca> listaContaCobranca) {
        this.listaContaCobranca = listaContaCobranca;
    }

    public List<SelectItem> getListaLayout() {
        ContaCobrancaDB db = new ContaCobrancaDBToplink();
        List<SelectItem> result = new ArrayList<SelectItem>();
        List layouts = db.pesquisaLayouts();
        for (int i = 0; i < layouts.size(); i++) {
            result.add(new SelectItem(i, ((Layout) layouts.get(i)).getDescricao(), Integer.toString(((Layout) layouts.get(i)).getId())));
        }
        return result;
    }

    public String editar(ContaCobranca c) {
        contaCobranca = c;
        ContaCobrancaDB db = new ContaCobrancaDBToplink();
        List<Layout> layouts = db.pesquisaLayouts();
        for (int i = 0; i < layouts.size(); i++) {
            if (layouts.get(i).getId() == contaCobranca.getLayout().getId()) {
                idLayout = i;
            }
        }

        setSicas(contaCobranca.getSicasSindical());
        setCodigoCedente(contaCobranca.getCodigoSindical());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("contaCobrancaPesquisa", contaCobranca);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "contaCobranca";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public String preencheSicasECodSindical() {
        if (contaCobranca.getCodCedente().length() >= 5) {
            sicas = contaCobranca.getCodCedente().substring(contaCobranca.getCodCedente().length() - 5, contaCobranca.getCodCedente().length());
        } else {
            sicas = "";
        }
        codigoCedente = contaCobranca.getCodCedente();
        return null;
    }

    public String getRepasse() {
        return repasse;
    }

    public void setRepasse(String repasse) {
        this.repasse = Moeda.substituiVirgula(repasse);
    }

    public ContaCobranca getContaCobranca() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contaCobrancaPesquisa") != null) {
            contaCobranca = (ContaCobranca) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contaCobrancaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contaCobrancaPesquisa");
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contaBancoPesquisa") != null) {
            contaCobranca.setContaBanco((ContaBanco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("contaBancoPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contaBancoPesquisa");
        }
        return contaCobranca;
    }

    public void setContaCobranca(ContaCobranca contaCobranca) {
        this.contaCobranca = contaCobranca;
    }

    public int getIdLayout() {
        return idLayout;
    }

    public void setIdLayout(int idLayout) {
        this.idLayout = idLayout;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getSicas() {
        return sicas;
    }

    public void setSicas(String sicas) {
        this.sicas = sicas;
    }

    public String getCodigoCedente() {
        return codigoCedente;
    }

    public void setCodigoCedente(String codigoCedente) {
        this.codigoCedente = codigoCedente;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }
}
