package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.BloqueiaServicoPessoa;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class BloqueioServicosJSFBean {

    private BloqueiaServicoPessoa bloqueia = new BloqueiaServicoPessoa();
    private Pessoa pessoa = new Pessoa();
    private int idServicos = 0;
    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private List<BloqueiaServicoPessoa> listaBloqueios = new ArrayList();
    private String msgConfirma = "";
    private String refInicial = "";
    private String refFinal = "";

    public BloqueioServicosJSFBean() {
        DataHoje dh = new DataHoje();

        refInicial = DataHoje.dataReferencia(DataHoje.data());
        refFinal = refInicial.substring(0, 2) + "/2050";// DataHoje.dataReferencia(dh.incrementarAnos(1, DataHoje.data()));
    }

    public String salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        if (pessoa.getId() == -1) {
            msgConfirma = "Pesquise uma pessoa para bloquear!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (refInicial.isEmpty()) {
            msgConfirma = "Referência inicial esta vazia!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (refFinal.isEmpty()) {
            msgConfirma = "Referência final esta vazia!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        Servicos servicos = (Servicos) sv.pesquisaCodigo(Integer.parseInt(this.getListaServico().get(idServicos).getDescription()), "Servicos");
        NovoLog novoLog = new NovoLog();
        FinanceiroDB db = new FinanceiroDBToplink();

        int d_fim = DataHoje.qtdeDiasDoMes(Integer.valueOf(refFinal.substring(0, 2)), Integer.valueOf(refFinal.substring(3, 7)));

        bloqueia.setInicio("01/" + refInicial);
        bloqueia.setFim(d_fim + "/" + refFinal);

        if (db.pesquisaBloqueiaServicoPessoa(pessoa.getId(), servicos.getId(), bloqueia.getDtInicio(), bloqueia.getDtFim()) != null) {
            msgConfirma = "Este bloqueio já existe!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        bloqueia.setPessoa(pessoa);
        bloqueia.setServicos(servicos);

        sv.abrirTransacao();
        if (bloqueia.getId() == -1) {
            if (sv.inserirObjeto(bloqueia)) {
                novoLog.save(
                        "ID: " + bloqueia.getId()
                        + " - Pessoa: (" + bloqueia.getPessoa().getId() + ") " + bloqueia.getPessoa().getNome()
                        + " - Serviços: (" + bloqueia.getServicos().getId() + ") " + bloqueia.getServicos().getDescricao()
                        + " - Período: " + bloqueia.getInicio() + " - " + bloqueia.getFim()
                        + " - Gerar Guias: " + bloqueia.isGeracao()
                        + " - Impressão: " + bloqueia.isImpressao()
                );
                msgConfirma = "Bloqueio salvo com Sucesso!";
                GenericaMensagem.info("Sucesso", msgConfirma);
                listaBloqueios.clear();
                bloqueia = new BloqueiaServicoPessoa();
                sv.comitarTransacao();
            } else {
                msgConfirma = "Erro ao salvar Bloqueio!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
            }
        } else {
            BloqueiaServicoPessoa bsp = (BloqueiaServicoPessoa) sv.pesquisaObjeto(bloqueia.getId(), "BloqueiaServicoPessoa");
            String beforeUpdate
                    = "ID: " + bsp.getId()
                    + " - Pessoa: (" + bsp.getPessoa().getId() + ") " + bsp.getPessoa().getNome()
                    + " - Serviços: (" + bsp.getServicos().getId() + ") " + bsp.getServicos().getDescricao()
                    + " - Período: " + bsp.getInicio() + " - " + bsp.getFim()
                    + " - Gerar Guias: " + bsp.isGeracao()
                    + " - Impressão: " + bsp.isImpressao();
            if (sv.alterarObjeto(bloqueia)) {
                novoLog.update(beforeUpdate,
                        "ID: " + bloqueia.getId()
                        + " - Pessoa: (" + bloqueia.getPessoa().getId() + ") " + bloqueia.getPessoa().getNome()
                        + " - Serviços: (" + bloqueia.getServicos().getId() + ") " + bloqueia.getServicos().getDescricao()
                        + " - Período: " + bloqueia.getInicio() + " - " + bloqueia.getFim()
                        + " - Gerar Guias: " + bloqueia.isGeracao()
                        + " - Impressão: " + bloqueia.isImpressao()
                );
                msgConfirma = "Bloqueio alterado com Sucesso!";
                GenericaMensagem.info("Sucesso", msgConfirma);
                listaBloqueios.clear();
                bloqueia = new BloqueiaServicoPessoa();
                sv.comitarTransacao();
            } else {
                msgConfirma = "Erro ao excluir Bloqueio!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
            }
        }
        return null;
    }

    public String excluir(BloqueiaServicoPessoa bl) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        NovoLog novoLog = new NovoLog();
        sv.abrirTransacao();

        if (sv.deletarObjeto(sv.pesquisaCodigo(bl.getId(), "BloqueiaServicoPessoa"))) {
            novoLog.delete(
                    "ID: " + bl.getId()
                    + " - Pessoa: (" + bl.getPessoa().getId() + ") " + bl.getPessoa().getNome()
                    + " - Serviços: (" + bl.getServicos().getId() + ") " + bl.getServicos().getDescricao()
                    + " - Período: " + bl.getInicio() + " - " + bl.getFim()
                    + " - Gerar Guias: " + bl.isGeracao()
                    + " - Impressão: " + bl.isImpressao()
            );
            msgConfirma = "Bloqueio excluído com Sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
            listaBloqueios.clear();
            sv.comitarTransacao();
        } else {
            msgConfirma = "Erro ao excluir bloqueio!";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
        }
        return null;
    }

    public String editar(BloqueiaServicoPessoa bl) {
        bloqueia = bl;
        return null;
    }

    public String alteraImprime(BloqueiaServicoPessoa bl) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();

        if (bl.isImpressao()) {
            bl.setImpressao(false);
        } else {
            bl.setImpressao(true);
        }

        if (sv.alterarObjeto(bl)) {
            msgConfirma = "Bloqueio atualizado!";
            GenericaMensagem.info("Sucesso", msgConfirma);
            sv.comitarTransacao();
        } else {
            msgConfirma = "Erro ao atualizar status de Bloqueio!";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
        }

        listaBloqueios.clear();
        return null;
    }

    public String alteraGera(BloqueiaServicoPessoa bl) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();

        if (bl.isGeracao()) {
            bl.setGeracao(false);
        } else {
            bl.setGeracao(true);
        }

        if (sv.alterarObjeto(bl)) {
            msgConfirma = "Bloqueio atualizado!";
            GenericaMensagem.info("Sucesso", msgConfirma);
            sv.comitarTransacao();
        } else {
            msgConfirma = "Erro ao atualizar status de Bloqueio!";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
        }

        listaBloqueios.clear();
        return null;
    }

    public List<SelectItem> getListaServico() {
        if (listaServicos.isEmpty()) {
            List select = new Dao().list(new Servicos(), true);
            for (int i = 0; i < select.size(); i++) {
                listaServicos.add(new SelectItem(
                        new Integer(i),
                        (String) ((Servicos) select.get(i)).getDescricao(),
                        Integer.toString(((Servicos) select.get(i)).getId())));
            }
        }
        return listaServicos;
    }

    public String removerPesquisa() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        pessoa = new Pessoa();
        listaBloqueios.clear();
        return "bloqueioServicos";
    }

    public BloqueiaServicoPessoa getBloqueia() {
        return bloqueia;
    }

    public void setBloqueia(BloqueiaServicoPessoa bloqueia) {
        this.bloqueia = bloqueia;
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
            listaBloqueios.clear();
            msgConfirma = "";
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public List<BloqueiaServicoPessoa> getListaBloqueios() {
        if (listaBloqueios.isEmpty() && pessoa.getId() != -1) {
            FinanceiroDB db = new FinanceiroDBToplink();
            listaBloqueios = db.listaBloqueiaServicoPessoas(pessoa.getId());
        }
        return listaBloqueios;
    }

    public void setListaBloqueios(List<BloqueiaServicoPessoa> listaBloqueios) {
        this.listaBloqueios = listaBloqueios;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getRefInicial() {
        return refInicial;
    }

    public void setRefInicial(String refInicial) {
        this.refInicial = refInicial;
    }

    public String getRefFinal() {
        return refFinal;
    }

    public void setRefFinal(String refFinal) {
        this.refFinal = refFinal;
    }
}
