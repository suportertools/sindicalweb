package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.HistoricoCarteirinha;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.dao.SociosDao;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.beans.BaixaGeralBean;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.utilitarios.SegurancaUtilitariosBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.db.FunctionsDao;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;

@ManagedBean
@SessionScoped
public class GeracaoDebitosCartaoBean implements Serializable {

    private Fisica fisica;
    private List<Socios> listaSocios;
    private List<Socios> selected;
    private Lote lote;
    private Registro registro;
    private List<Movimento> listMovimentos;
    private Boolean habilitaImpressao;
    private List<HistoricoCarteirinha> listHistoricoCarteirinhas;

    @PostConstruct
    public void init() {
        fisica = new Fisica();
        listaSocios = new ArrayList<>();
        selected = null;
        lote = new Lote();
        registro = (Registro) new Dao().find(new Registro(), 1);
        listMovimentos = new ArrayList();
        listHistoricoCarteirinhas = new ArrayList();
        habilitaImpressao = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("geracaoDebitosCartaoBean");
        GenericaSessao.remove("uploadBean");
        GenericaSessao.remove("photoCamBean");
        GenericaSessao.remove("cartaoSocialBean");
        GenericaSessao.remove("fisicaPesquisa");
        GenericaSessao.remove("fisicaBean");
        GenericaSessao.remove("baixa_sucesso");
        GenericaSessao.remove("cartao_social_sucesso");
        GenericaSessao.remove("listaMovimento");
        GenericaSessao.remove("lista_movimentos_baixados");
        GenericaSessao.remove("pessoaUtilitariosBean");
        clear(2);
    }

    public void load() {
        if (GenericaSessao.exists("baixa_sucesso")) {
            if (GenericaSessao.exists("lista_movimentos_baixados")) {
                List<Movimento> list = GenericaSessao.getList("lista_movimentos_baixados", true);
                listHistoricoCarteirinhas.clear();
                if (list.isEmpty()) {
                    selected = null;
                    listMovimentos.clear();
                    GenericaMensagem.warn("Erro", "Erro ao emitir cartões");
                    return;
                }
                listHistoricoCarteirinhas = CartaoSocialBean.gerarHistoricoCarteirinhas(list, getListaSocios().get(0).getMatriculaSocios().getCategoria().getId(), 170);
                listMovimentos.clear();
                GenericaSessao.remove("baixa_sucesso");
                if (!listHistoricoCarteirinhas.isEmpty()) {
                    for (int i = 0; i < listHistoricoCarteirinhas.size(); i++) {
                        listHistoricoCarteirinhas.get(i).getCarteirinha().setEmissao(DataHoje.data());
                        new Dao().update(listHistoricoCarteirinhas.get(i).getCarteirinha(), true);
                    }
                    GenericaMensagem.info("Sucesso", "Operação realizada com sucesso. Você já pode emitir seus cartões.");
                    return;
                }
            }
        }
        if (GenericaSessao.exists("cartao_social_sucesso")) {
            GenericaSessao.remove("cartao_social_sucesso");
            listHistoricoCarteirinhas.clear();
            selected = null;
            GenericaMensagem.info("Sucesso", "Cartão impresso com sucesso!");
        }
    }

    public String save() {
        if (fisica.getPessoa().getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquise uma pessoa para gerar!");
            return null;
        }
        if (selected.isEmpty()) {
            GenericaMensagem.warn("Validação", "Selecione pelo menos um item da lista!");
            return null;
        }
        Dao dao = new Dao();
        Servicos serv = (Servicos) registro.getServicos();

        // CODICAO DE PAGAMENTO
        CondicaoPagamento cp = (CondicaoPagamento) dao.find(new CondicaoPagamento(), 1);
        // TIPO DE DOCUMENTO  FTipo_documento 13 - CARTEIRA, 2 - BOLETO
        FunctionsDao functionsDao = new FunctionsDao();
        FTipoDocumento td = (FTipoDocumento) dao.find(new FTipoDocumento(), 2);
        lote = new Lote();
        lote.setEmissao(DataHoje.data());
        lote.setPagRec("R");
        lote.setValor(functionsDao.valorServico(fisica.getPessoa().getId(), serv.getId(), DataHoje.dataHoje(), 0, null));
        lote.setFilial(serv.getFilial());
        lote.setEvt(null);
        lote.setPessoa(fisica.getPessoa());
        lote.setFTipoDocumento(td);
        lote.setRotina((Rotina) dao.find(new Rotina(), 301));
        lote.setStatus((FStatus) dao.find(new FStatus(), 1));
        lote.setPessoaSemCadastro(null);
        lote.setDepartamento(serv.getDepartamento());
        lote.setCondicaoPagamento(cp);
        lote.setPlano5(serv.getPlano5());

        dao.openTransaction();
        if (!dao.save(lote)) {
            GenericaMensagem.warn("Erro", "Ao salvar Lote!");
            dao.rollback();
            return null;
        }

        TipoServico tipoServico = (TipoServico) dao.find(new TipoServico(), 1);

        Movimento movimento;
        for (int i = 0; i < selected.size(); i++) {
            float valor = functionsDao.valorServico(selected.get(i).getServicoPessoa().getPessoa().getId(), serv.getId(), DataHoje.dataHoje(), 0, null);
            movimento = new Movimento();
            movimento.setLote(lote);
            movimento.setValor(valor);
            movimento.setValorBaixa(valor);
            movimento.setBaixa(null);
            movimento.setAcordo(null);
            movimento.setTitular(selected.get(i).getMatriculaSocios().getTitular());
            movimento.setPessoa(selected.get(i).getMatriculaSocios().getTitular());
            movimento.setBeneficiario(selected.get(i).getServicoPessoa().getPessoa());
            movimento.setFTipoDocumento(td);
            movimento.setMatriculaSocios(selected.get(i).getMatriculaSocios());
            movimento.setVencimento(DataHoje.data());
            movimento.setReferencia(DataHoje.converteDataParaReferencia(DataHoje.data()));
            movimento.setAtivo(true);
            movimento.setEs("E");
            movimento.setTipoServico(tipoServico);
            movimento.setPlano5(lote.getPlano5());
            movimento.setServicos(serv);
            if (dao.save(movimento)) {
                listMovimentos.add(movimento);
            } else {
                dao.rollback();
                lote = new Lote();
                return null;
            }
        }

        dao.commit();
        listaSocios.clear();
        BaixaGeralBean.listenerTipoCaixaSession("caixa");
        GenericaSessao.put("listaMovimento", listMovimentos);
        GenericaMensagem.info("Sucesso", "Geração efetuada com sucesso!");
        return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).baixaGeral();
        // return null;
    }

    public void clear(Integer tCase) {
        // Limpa toda sessão
        if (tCase == 0) {
            GenericaSessao.remove("geracaoDebitosCartaoBean");
            clear(2);
        }
        // Limpar e manter Sócio (Física)
        if (tCase == 1) {
            listaSocios.clear();
            selected = null;
            lote = new Lote();
            listMovimentos = new ArrayList();
            listHistoricoCarteirinhas.clear();
            habilitaImpressao = false;
            clear(2);
        }
        if (tCase == 2) {
            try {
                FileUtils.deleteDirectory(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + "/Cliente/" + ControleUsuarioBean.getCliente() + "/temp/" + "foto/" + new SegurancaUtilitariosBean().getSessaoUsuario().getId()));
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + -1 + ".png"));
                if (f.exists()) {
                    f.delete();
                }
            } catch (IOException ex) {

            }
        }
        if (tCase == 3) {
            GenericaSessao.remove("cropperBean");
            GenericaSessao.remove("uploadBean");
            GenericaSessao.remove("photoCamBean");
        }
    }

    public Fisica getFisica() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            fisica = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
            listaSocios.clear();
            listMovimentos.clear();
            listHistoricoCarteirinhas.clear();
            selected = null;
            habilitaImpressao = false;
            GenericaSessao.remove("baixa_sucesso");
            GenericaSessao.remove("lista_movimentos_baixados");
            GenericaSessao.remove("listaMovimento");
            GenericaSessao.remove("cartaoSocialBean");
            GenericaSessao.remove("pessoaUtilitariosBean");
            clear(2);
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public List<Socios> getListaSocios() {
        if (listaSocios.isEmpty()) {
            SociosDB sociosDB = new SociosDBToplink();
            Socios s = sociosDB.pesquisaSocioPorPessoaAtivo(fisica.getPessoa().getId());
            if (s != null) {
                SociosDao sociosDao = new SociosDao();
                listaSocios = sociosDao.pesquisaDependentePorMatricula(s.getMatriculaSocios().getId(), false);
                for (int i = 0; i < listaSocios.size(); i++) {
                    // Campo opcional
                    listaSocios.get(i).setObject(getMovimento(listaSocios.get(i).getServicoPessoa().getPessoa()));
                }
            }
        }
        return listaSocios;
    }

    public void setListaSocios(List<Socios> listaSocios) {
        this.listaSocios = listaSocios;
    }

    public Fisica pessoaFisica(Pessoa p) {
        Fisica f = new FisicaDBToplink().pesquisaFisicaPorPessoa(p.getId());
        return f;
    }

    public List<Socios> getSelected() {
        return selected;
    }

    public void setSelected(List<Socios> selected) {
        this.selected = selected;
    }

    public Movimento getMovimento(Pessoa p) {
        MovimentoDBToplink mdb = new MovimentoDBToplink();
        mdb.setLimit(1);
        List list = mdb.listaMovimentosUltimosDias(p.getId(), registro.getServicos().getId(), 301, 10, 2);
        if (!list.isEmpty()) {
            return (Movimento) list.get(0);
        }
        return null;
    }

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public Boolean renderedUpload(Pessoa p) {
        if (registro.isFotoCartao()) {
            Dao dao = new Dao();
            if (p.getFisica().getDtFoto() != null) {
                return false;
            }
        } else {
            return true;
        }
        return false;
    }

    public Boolean disabled(Pessoa p, Movimento m) {
        if (registro.isFotoCartao()) {
            if (p.getFisica().getDtFoto() == null) {
                return true;
            } else if (m != null) {
                return true;
            }
        } else {
            return m != null;
        }
        return false;
    }

    public Boolean getHabilitaImpressao() {
        return habilitaImpressao;
    }

    public void setHabilitaImpressao(Boolean habilitaImpressao) {
        this.habilitaImpressao = habilitaImpressao;
    }

    public List<HistoricoCarteirinha> getListHistoricoCarteirinhas() {
        return listHistoricoCarteirinhas;
    }

    public void setListHistoricoCarteirinhas(List<HistoricoCarteirinha> listHistoricoCarteirinhas) {
        this.listHistoricoCarteirinhas = listHistoricoCarteirinhas;
    }
}
