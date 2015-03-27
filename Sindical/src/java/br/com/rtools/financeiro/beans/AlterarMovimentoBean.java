package br.com.rtools.financeiro.beans;

import br.com.rtools.associativo.beans.MovimentosReceberSocialBean;
import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.TipoServicoDB;
import br.com.rtools.financeiro.db.TipoServicoDBToplink;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public final class AlterarMovimentoBean implements Serializable {

    private List<DataObject> listaMovimento = new ArrayList();
    private Movimento movimento = new Movimento();
    private Lote lote = new Lote();
    private Baixa baixa = null;
    private String msgConfirma = "";
    private int idServicos = 0;
    private int idTipoServicos = 0;
    private int idCondicaoPagamento = 0;
    private List<SelectItem> listaServico = new ArrayList();
    private List<Servicos> selectServico = new ArrayList();
    private List<SelectItem> listaTipoServico = new ArrayList();
    private List<TipoServico> selectTipoServico = new ArrayList();
    private List<CondicaoPagamento> selectCondicao = new ArrayList();
    private List<SelectItem> listaCondicao = new ArrayList();
    private String historico = "";

    public AlterarMovimentoBean() {
        this.getMovimento();
        this.getListaServico();
        this.getListaTipoServico();

        for (int i = 0; i < selectServico.size(); i++) {
            if (selectServico.get(i).getId() == movimento.getServicos().getId()) {
                idServicos = i;
                break;
            }
        }

        for (int i = 0; i < selectTipoServico.size(); i++) {
            if (selectTipoServico.get(i).getId() == movimento.getTipoServico().getId()) {
                idTipoServicos = i;
                break;
            }
        }

        for (int i = 0; i < selectCondicao.size(); i++) {
            if (selectCondicao.get(i).getId() == lote.getCondicaoPagamento().getId()) {
                idCondicaoPagamento = i;
                break;
            }
        }
    }

    public List<SelectItem> getListaCondicao() {
        if (listaCondicao.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            selectCondicao = sv.listaObjeto("CondicaoPagamento");
            for (int i = 0; i < selectCondicao.size(); i++) {
                listaCondicao.add(new SelectItem(
                        new Integer(i),
                        selectCondicao.get(i).getDescricao(),
                        Integer.toString(selectCondicao.get(i).getId())
                )
                );
            }
        }
        return listaCondicao;
    }

    public List<SelectItem> getListaServico() {
        if (listaServico.isEmpty()) {
            selectServico = new Dao().list(new Servicos(), true);
            for (int i = 0; i < selectServico.size(); i++) {
                listaServico.add(new SelectItem(
                        new Integer(i),
                        selectServico.get(i).getDescricao(),
                        Integer.toString(selectServico.get(i).getId())
                )
                );
            }
        }
        return listaServico;
    }

    public List<SelectItem> getListaTipoServico() {
        if (listaTipoServico.isEmpty()) {
            TipoServicoDB db = new TipoServicoDBToplink();
            selectTipoServico = db.pesquisaTodos();
            for (int i = 0; i < selectTipoServico.size(); i++) {
                listaTipoServico.add(new SelectItem(
                        new Integer(i),
                        selectTipoServico.get(i).getDescricao(),
                        Integer.toString(selectTipoServico.get(i).getId())
                )
                );
            }
        }
        return listaTipoServico;
    }

    // LISTA DE MOVIMENTOS PARA SER ALTERADO ---------------------------
    public List<DataObject> getListaMovimento() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaMovimento") != null) {
            List<Movimento> lista = (List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaMovimento");
            for (int i = 0; i < lista.size(); i++) {
                listaMovimento.add(new DataObject(true, lista.get(i)));
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("listaMovimento");
        }
        return listaMovimento;
    }

    public void setListaMovimento(List<DataObject> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }
    // ------------------------------------------------------
    // ------------------------------------------------------

    public String salvar() {
        if (movimento.getId() == -1) {
            GenericaMensagem.warn("Atenção", "Nenhum movimento encontrado para ser alterado!");
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        FinanceiroDB db = new FinanceiroDBToplink();

        movimento.setServicos(selectServico.get(idServicos));
        movimento.setTipoServico(selectTipoServico.get(idTipoServicos));

        List<Movimento> lista = db.pesquisaMovimentoPorLote(lote.getId());
        if (lista.size() > 1) {
            for (int i = 0; i < lista.size(); i++) {
                if (movimento.getId() != lista.get(i).getId()) {
                    Movimento lm = (Movimento) sv.pesquisaCodigo(lista.get(i).getId(), "Movimento");
                    lm.setServicos(selectServico.get(idServicos));
                    sv.alterarObjeto(lm);
                }
            }
            lote.setCondicaoPagamento((CondicaoPagamento) sv.pesquisaCodigo(2, "CondicaoPagamento"));
        } else {
            if (DataHoje.converteDataParaInteger(movimento.getVencimento()) > DataHoje.converteDataParaInteger(lote.getEmissao())) {
                lote.setCondicaoPagamento((CondicaoPagamento) sv.pesquisaCodigo(2, "CondicaoPagamento"));
            } else {
                lote.setCondicaoPagamento((CondicaoPagamento) sv.pesquisaCodigo(1, "CondicaoPagamento"));
            }
        }

        sv.alterarObjeto(lote);
        movimento.setLote(lote);

        if (baixa != null) {
            sv.alterarObjeto(baixa);
            movimento.setBaixa(baixa);
        }

        if (sv.alterarObjeto(movimento)) {
            GenericaMensagem.info("OK", "Movimento atualizado com Sucesso!");
            sv.comitarTransacao();
        } else {
            GenericaMensagem.error("Atenção", "Erro ao atualizar Movimento!");
            sv.desfazerTransacao();
        }

        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        if (url.equals("movimentosReceberSocial")) {
            ((MovimentosReceberSocialBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("movimentosReceberSocialBean")).getListaMovimento().clear();
        }
        return null;
    }

    public String inativarBoleto() {
        MovimentoDB db = new MovimentoDBToplink();

        if (movimento.getBaixa() != null) {
            GenericaMensagem.warn("Atenção", "Boletos quitados não podem ser Excluídos!");
            return null;
        }

        if (movimento.getAcordo() != null) {
            GenericaMensagem.warn("Atenção", "Boletos do tipo acordo não podem ser Excluídos");
            return null;
        }

        if (historico.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Digite um motivo para exclusão!");
            return null;
        } else if (historico.length() < 6) {
            GenericaMensagem.warn("Atenção", "Motivo de exclusão inválido!");
            return null;
        }

        if (!GerarMovimento.inativarUmMovimento(db.pesquisaCodigo(movimento.getId()), historico).isEmpty()) {
            GenericaMensagem.error("Atenção", "Ocorreu um erro em uma das exclusões, verifique o log!");
        } else {
            GenericaMensagem.info("Atenção", "Boleto excluído com sucesso!");
        }

        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        if (url.equals("movimentosReceberSocial")) {
            ((MovimentosReceberSocialBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("movimentosReceberSocialBean")).getListaMovimento().clear();
            //return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).movimentosReceberSocial();
        }

        movimento = new Movimento();
        lote = new Lote();
        baixa = new Baixa();
        return voltar();
    }

    public Movimento getMovimento() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaMovimento") != null) {
            movimento = new Movimento();
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            movimento = (Movimento) sv.pesquisaCodigo(((Movimento) ((List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaMovimento")).get(0)).getId(), "Movimento");

            lote = (Lote) sv.pesquisaCodigo(movimento.getLote().getId(), "Lote");

            if (movimento.getBaixa() != null) {
                baixa = (Baixa) sv.pesquisaCodigo(movimento.getBaixa().getId(), "Baixa");
            }

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("listaMovimento");
        }

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            if (baixa != null) {
                Pessoa p = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");

                UsuarioDB db = new UsuarioDBToplink();

                //movimento.getBaixa().setUsuario(db.pesquisaUsuarioPorPessoa(p.getId()));
                baixa.setUsuario(db.pesquisaUsuarioPorPessoa(p.getId()));
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return movimento;
    }

    public String voltar() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdTipoServicos() {
        return idTipoServicos;
    }

    public void setIdTipoServicos(int idTipoServicos) {
        this.idTipoServicos = idTipoServicos;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Baixa getBaixa() {
        return baixa;
    }

    public void setBaixa(Baixa baixa) {
        this.baixa = baixa;
    }

    public int getIdCondicaoPagamento() {
        return idCondicaoPagamento;
    }

    public void setIdCondicaoPagamento(int idCondicaoPagamento) {
        this.idCondicaoPagamento = idCondicaoPagamento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
}
