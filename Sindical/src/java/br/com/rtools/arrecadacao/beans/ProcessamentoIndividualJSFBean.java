package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.MensagemConvencao;
import br.com.rtools.arrecadacao.db.*;
import br.com.rtools.financeiro.*;
import br.com.rtools.financeiro.beans.MovimentoValorBean;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.beans.JuridicaBean;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.sistema.Links;
import br.com.rtools.sistema.db.LinksDB;
import br.com.rtools.sistema.db.LinksDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mail;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ManagedBean(name = "processamentoIndividualBean")
@SessionScoped
public class ProcessamentoIndividualJSFBean extends MovimentoValorBean implements Serializable {

    private String frenteVerso = "false";
    private String nImpressos = "false";
    private int nrBoletos;
    private int processados;
    private int processando;
    private List<DataObject> listMovimentos = new ArrayList();
    private List listaMovAdd = new ArrayList();
    private int idTipoServico;
    private int idServicos;
    private int idIndex = -1;
    private Object processamento = new Object();
    private String vencimento = DataHoje.data();
    private String msgConfirma = "";
    private String msgConfirmaTela = "";
    private String strReferencia = DataHoje.dataReferencia(vencimento);
    private String tipoEnvio = "empresa";
    private String linkAcesso = "";
    private Lote lote = null;
    private Juridica juridica = new Juridica();
    private boolean carregaList = true;
    private boolean renVisualizar = false;
    private boolean renLimparTodos = false;
    private boolean imprimeVerso = false;
    private boolean processou = false;
    private boolean marcarTodos = true;
    private boolean marcarReferencia = true;
    private boolean marcarVencimento = true;
    private boolean outrasEmpresas = false;
    DataObject dataObject;
    List<Boolean> marcados = new ArrayList<Boolean>();
    private Pessoa pessoaEnvio = new Pessoa();

    public ProcessamentoIndividualJSFBean() {

    }

    public void removerEmpresa() {
        juridica = new Juridica();
    }

    public void salvarEmail() {
        if (this.pessoaEnvio.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();

            if (sv.alterarObjeto(this.pessoaEnvio)) {
                msgConfirma = "Email atualizado";
                GenericaMensagem.info("Sucesso", msgConfirma);
                sv.comitarTransacao();
            } else {
                msgConfirma = "Erro ao atualizar Email";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
            }
        }
    }

    public void verificaEmail(Pessoa pessoaEnvio) {
        this.pessoaEnvio = pessoaEnvio;
        if (!this.pessoaEnvio.getEmail1().isEmpty()) {
        }
    }

    public String getStatusContribuinte() {
        JuridicaDB db = new JuridicaDBToplink();
        if (juridica.getId() != -1) {
            List<Vector> listax = db.listaJuridicaContribuinte(juridica.getId());

            if (listax.isEmpty()) {
                return "NÃO CONTRIBUINTE";
            }
            for (int i = 0; i < listax.size(); i++) {
                if (listax.get(0).get(11) != null) {
                    return "CONTRIBUINTE INATIVO";
                } else {
                    //return  "CONTRIBUINTE ATIVO";
                }
            }
        }
        return "";
    }

    public synchronized List getListaMovimentos() {
        if (carregaList) {
            gerarLista();
            listaMovAdd = new ArrayList();
            carregaList = false;
        }
        return listMovimentos;
    }

    public synchronized void gerarLista() {
        int i = 0;
        DataObject dtObject;
        boolean m = false;
        if ((juridica != null) && (juridica.getId() != -1)) {
            while (i < listaMovAdd.size()) {
                if ((i >= 0) && (i < marcados.size())) {
                    m = marcados.get(i);
                } else {
                    m = false;
                }

                JuridicaDB dbj = new JuridicaDBToplink();
                Juridica jur_lista = dbj.pesquisaJuridicaPorPessoa(((Movimento) (listaMovAdd.get(i))).getPessoa().getId());
                //int id_pessoa = juridica.getPessoa().getId();
                int id_pessoa = ((Movimento) (listaMovAdd.get(i))).getPessoa().getId(); //juridica.getPessoa().getId();
                int id_servico = ((Movimento) (listaMovAdd.get(i))).getServicos().getId();
                int id_tipo_servico = ((Movimento) (listaMovAdd.get(i))).getTipoServico().getId();
                String referencia = ((Movimento) (listaMovAdd.get(i))).getReferencia();

                MovimentoDB db = new MovimentoDBToplink();

                String juros = "0,00";
                String multa = "0,00";
                String correcao = "0,00";
                if (m) {
                    juros = Moeda.converteR$Float(Float.valueOf(Double.toString(db.funcaoJuros(id_pessoa, id_servico, id_tipo_servico, referencia))));
                    multa = Moeda.converteR$Float(Float.valueOf(Double.toString(db.funcaoMulta(id_pessoa, id_servico, id_tipo_servico, referencia))));
                    correcao = Moeda.converteR$Float(Float.valueOf(Double.toString(db.funcaoCorrecao(id_pessoa, id_servico, id_tipo_servico, referencia))));
                }

                String valor_calculado = Moeda.converteR$Float(Moeda.somaValores(
                        Moeda.somaValores(Moeda.somaValores(Moeda.converteUS$(juros), Moeda.converteUS$(multa)), Moeda.converteUS$(correcao)),
                        ((Movimento) (listaMovAdd.get(i))).getValor()));

                dtObject = new DataObject(new Boolean(m),
                        ((Movimento) (listaMovAdd.get(i))),
                        jur_lista.getContabilidade(),
                        Moeda.converteR$Float(((Movimento) (listaMovAdd.get(i))).getValor()),
                        juros, // JUROS
                        multa, // MULTA
                        correcao, // CORRECAO
                        valor_calculado, // VALOR CALCULADO
                        null,
                        null);

                listMovimentos.add(dtObject);
                i++;
            }
            listaMovAdd = new ArrayList();
        }
    }

    public synchronized void marcarTodosLista() {
        marcados = new ArrayList<Boolean>();
        for (int i = 0; i < listMovimentos.size(); i++) {
            listaMovAdd.add((Movimento) listMovimentos.get(i).getArgumento1());
            marcados.add(marcarTodos);
        }
        listMovimentos = new ArrayList();
        carregaList = true;
        gerarLista();
    }

    public synchronized String adicionarMovimento() {
        if (juridica.getId() == -1) {
            msgConfirmaTela = "Pesquise uma empresa!";
            GenericaMensagem.warn("Erro", msgConfirmaTela);
            return null;
        }
        FTipoDocumentoDB dbft = new FTipoDocumentoDBToplink();
        ContaCobrancaDB ctaCobraDB = new ContaCobrancaDBToplink();
        MensagemConvencaoDB menDB = new MensagemConvencaoDBToplink();
        Servicos servicos = new Servicos();
        TipoServico tipoServico = new TipoServico();
        ContaCobranca contaCob = new ContaCobranca();
        TipoServicoDB dbTipo = new TipoServicoDBToplink();
        servicos = (Servicos) new Dao().find(new Servicos(), Integer.valueOf(getListaServico().get(idServicos).getDescription()));
        tipoServico = dbTipo.pesquisaCodigo(Integer.valueOf(getListaTipoServico().get(idTipoServico).getDescription()));
        contaCob = ctaCobraDB.pesquisaServicoCobranca(servicos.getId(), tipoServico.getId());
        if (contaCob == null) {
            msgConfirmaTela = "Não existe conta Cobrança para gerar!";
            GenericaMensagem.warn("Erro", msgConfirmaTela);
            return null;
        }
        Movimento movim = new Movimento();
        MovimentoDB finDB = new MovimentoDBToplink();
        movim = finDB.pesquisaMovimentos(juridica.getPessoa().getId(), strReferencia, tipoServico.getId(), servicos.getId());

        if (movim != null) {
            if (movim.getBaixa() != null && movim.getBaixa().getId() != -1) {
                msgConfirmaTela = "Movimento já foi baixado!";
                GenericaMensagem.warn("Erro", msgConfirmaTela);
                return null;
            }
        }
        MensagemConvencao mensagemConvencao = menDB.retornaDiaString(
                juridica.getId(),
                strReferencia,
                tipoServico.getId(),
                servicos.getId());
        if (mensagemConvencao == null) {
            msgConfirmaTela = "Não existe mensagem para esta referência !";
            GenericaMensagem.warn("Erro", msgConfirmaTela);
            return null;
        }

        if ((mensagemConvencao != null)
                && (juridica != null)
                && (juridica.getId() != -1)) {

            if (strReferencia.length() == 7) {
                if (listMovimentos.isEmpty()) {
                    outrasEmpresas = false;

                    Movimento movi = new Movimento(-1,
                            null,
                            servicos.getPlano5(),
                            juridica.getPessoa(),
                            servicos,
                            null,
                            tipoServico,
                            null,
                            movim != null ? movim.getValor() : Moeda.converteFloatR$Float(super.carregarValor(servicos.getId(), tipoServico.getId(), strReferencia, juridica.getPessoa().getId())),
                            strReferencia,
                            vencimento,
                            1,
                            true,
                            "E",
                            false,
                            juridica.getPessoa(),
                            juridica.getPessoa(),
                            "",
                            "",
                            vencimento,
                            0,
                            0, 0, 0, 0, 0, 0, dbft.pesquisaCodigo(2), 0, null);
                    listaMovAdd.add(movi);
                } else {
                    int tamList = listMovimentos.size();
                    boolean ex = false;
                    for (int i = 0; i < tamList; i++) {
                        if (((Movimento) listMovimentos.get(i).getArgumento1()).getServicos().getId() == servicos.getId()
                                && ((Movimento) listMovimentos.get(i).getArgumento1()).getTipoServico().getId() == tipoServico.getId()
                                && ((Movimento) listMovimentos.get(i).getArgumento1()).getReferencia().equals(strReferencia)
                                && ((Movimento) listMovimentos.get(i).getArgumento1()).getPessoa().getId() == juridica.getPessoa().getId()) {
                            ex = true;
                        }
                    }
                    if (!ex) {
                        Movimento movi = new Movimento(-1,
                                null,
                                servicos.getPlano5(),
                                juridica.getPessoa(),
                                servicos,
                                null,
                                tipoServico,
                                null,
                                movim != null ? movim.getValor() : Moeda.converteFloatR$Float(super.carregarValor(servicos.getId(), tipoServico.getId(), strReferencia, juridica.getPessoa().getId())),
                                strReferencia,
                                vencimento,
                                1,
                                true,
                                "E",
                                false,
                                juridica.getPessoa(),
                                juridica.getPessoa(),
                                "",
                                "",
                                vencimento,
                                0,
                                0, 0, 0, 0, 0, 0, dbft.pesquisaCodigo(2), 0, null);
                        listaMovAdd.add(movi);
                        if (((Movimento) listMovimentos.get(0).getArgumento1()).getPessoa().getId() != juridica.getPessoa().getId()) {
                            outrasEmpresas = true;
                        }
                    } else {
                        msgConfirmaTela = "Esse movimento já está adicionado abaixo!";
                        GenericaMensagem.warn("Erro", msgConfirmaTela);
                    }
                }
                marcados.add(true);
                carregaList = true;
                renVisualizar = false;
                renLimparTodos = false;
                msgConfirmaTela = "";
            }
            msgConfirmaTela = "";
        }

        return null;
    }

    public String chamarMensagem() {
        CnaeConvencaoDB cnaeConvencaoDB = new CnaeConvencaoDBToplink();
        ConvencaoCidadeDB convencaoCidade = new ConvencaoCidadeDBToplink();
        Convencao convencao = cnaeConvencaoDB.pesquisarCnaeConvencao(juridica.getId());
        PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
        GrupoCidade grupoCidade = null;
        if (convencao != null) {
            PessoaEndereco pessoaEndereco = pessoaEnderecoDB.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 5);
            if (pessoaEndereco != null) {
                grupoCidade = convencaoCidade.pesquisaGrupoCidadeJuridica(convencao.getId(), pessoaEndereco.getEndereco().getCidade().getId());
                if (grupoCidade != null) {
                    MensagemConvencao mensagemConvencao = new MensagemConvencao();
                    TipoServicoDB dbTipo = new TipoServicoDBToplink();
                    Servicos servicos = (Servicos) new Dao().find(new Servicos(), Integer.valueOf(getListaServico().get(idServicos).getDescription()));
                    TipoServico tipoServico = dbTipo.pesquisaCodigo(Integer.valueOf(getListaTipoServico().get(idTipoServico).getDescription()));
                    if ((servicos != null) && (tipoServico != null)) {
                        mensagemConvencao.setConvencao(convencao);
                        mensagemConvencao.setGrupoCidade(grupoCidade);
                        mensagemConvencao.setTipoServico(tipoServico);
                        mensagemConvencao.setServicos(servicos);
                        mensagemConvencao.setReferencia(strReferencia);
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("mensagemPesquisa", mensagemConvencao);
                        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).mensagem();
                    }
                }
            }
        }
        return null;
    }

    public synchronized void filtrarReferencia() {
        MensagemConvencaoDB menDB = new MensagemConvencaoDBToplink();
        if (!(new DataHoje()).integridadeReferencia(strReferencia)) {
            strReferencia = DataHoje.dataReferencia(vencimento);
            return;
        }
        msgConfirma = "";

        MensagemConvencao mensagemConvencao = menDB.retornaDiaString(
                juridica.getId(),
                strReferencia,
                Integer.valueOf(getListaTipoServico().get(idTipoServico).getDescription()),
                Integer.valueOf(getListaServico().get(idServicos).getDescription()));

        if (mensagemConvencao != null) {
            vencimento = mensagemConvencao.getVencimento();
            if (vencimento.isEmpty()) {
                vencimento = DataHoje.data();
            }
            if (DataHoje.converte(vencimento).before(DataHoje.dataHoje())) {
                vencimento = DataHoje.data();
            }
        } else {
            msgConfirma = "Não existe mensagem!";
            vencimento = DataHoje.data();
        }
        validaReferenciaVencimento(mensagemConvencao);
    }

    public synchronized String atualizaRef() {
        try {
            if (DataHoje.converte(vencimento).before(DataHoje.dataHoje())) {
                //vencimento = DataHoje.data();
            }
        } catch (Exception e) {
            vencimento = DataHoje.data();
        }
        return null;
    }

    @Override
    public synchronized void carregarFolha() {

    }

    @Override
    public synchronized void carregarFolha(DataObject linha) {
        dataObject = linha;
        if (dataObject == null) {
            return;
        }
        Movimento movi = (Movimento) linha.getArgumento1();
        super.carregarFolha(movi);
        dataObject.setArgumento3(Moeda.converteR$Float(movi.getValor()));
    }

    @Override
    public synchronized void atualizaValorGrid(String tipo) {
        dataObject.setArgumento3(super.atualizaValor(true, tipo));
        refreshGrid();
    }

    public synchronized void refreshGrid() {
        marcados = new ArrayList<Boolean>();
        for (int i = 0; i < listMovimentos.size(); i++) {
            listaMovAdd.add((Movimento) listMovimentos.get(i).getArgumento1());
            marcados.add((Boolean) listMovimentos.get(i).getArgumento0());
        }
        listMovimentos = new ArrayList();
        carregaList = true;
    }

    public String btnExcluirMov(int index) {
        listMovimentos.remove(index);
        msgConfirma = "";
        processou = false;
        return null;
    }

    public void excluirMov(int idMov) {
        for (int i = 0; i < listMovimentos.size(); i++) {
            if (idMov == ((Movimento) listMovimentos.get(i).getArgumento1()).getId()) {
                listMovimentos.remove(i);
            }
        }
    }

    public String limparTodos() {
        listMovimentos = new ArrayList();
        msgConfirma = "";
        renLimparTodos = false;
        renVisualizar = false;
        juridica = new Juridica();
        idServicos = 0;
        idTipoServico = 0;
        processou = false;
        vencimento = DataHoje.data();
        strReferencia = DataHoje.dataReferencia(vencimento);
        return "processamentoIndividual";
    }

    public synchronized String gerarBoleto() {
        Movimento movim = new Movimento();
        Lote lote = new Lote();
        Dao dao = new Dao();
        NovoLog novoLog = new NovoLog();
        String beforeUpdate = "";
        Movimento movimentoBefore = new Movimento();
        MovimentoDB finDB = new MovimentoDBToplink();
        if (!listMovimentos.isEmpty()) {
            for (int i = 0; i < listMovimentos.size(); i++) {
                movim = finDB.pesquisaMovimentos(
                        ((Movimento) listMovimentos.get(i).getArgumento1()).getPessoa().getId(),
                        ((Movimento) listMovimentos.get(i).getArgumento1()).getReferencia(),
                        ((Movimento) listMovimentos.get(i).getArgumento1()).getTipoServico().getId(),
                        ((Movimento) listMovimentos.get(i).getArgumento1()).getServicos().getId());
                if (movim != null) {
                    //movimentoBefore = (Movimento) dao.find((Movimento) listMovimentos.get(i).getArgumento1());
                    movimentoBefore = (Movimento) dao.find(movim);
                    beforeUpdate
                            = " Movimento: (" + movimentoBefore.getId() + ") "
                            + " - Referência: (" + movimentoBefore.getReferencia()
                            + " - Tipo Serviço: (" + movimentoBefore.getTipoServico().getId() + ") " + movim.getTipoServico().getDescricao()
                            + " - Serviços: (" + movimentoBefore.getServicos().getId() + ") " + movim.getServicos().getDescricao()
                            + " - Pessoa: (" + movimentoBefore.getPessoa().getId() + ") " + movim.getPessoa().getNome()
                            + " - Valor: " + movimentoBefore.getValorString()
                            + " - Vencimento: " + movimentoBefore.getVencimento();

                    movim.setValor(Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento3()));
                    if (GerarMovimento.alterarUmMovimento(movim)) {
                        novoLog.update(beforeUpdate,
                                " Movimento: (" + movim.getId() + ") "
                                + " - Referência: (" + movim.getReferencia()
                                + " - Tipo Serviço: (" + movim.getTipoServico().getId() + ") " + movim.getTipoServico().getDescricao()
                                + " - Serviços: (" + movim.getServicos().getId() + ") " + movim.getServicos().getDescricao()
                                + " - Pessoa: (" + movim.getPessoa().getId() + ") " + movim.getPessoa().getNome()
                                + " - Valor: " + movim.getValorString()
                                + " - Vencimento: " + movim.getVencimento()
                        );
                        msgConfirma = "Alterado com sucesso!";
                    } else {
                        msgConfirma = "Erro ao alterar boletos!";
                    }

                    movim.setVencimento(((Movimento) listMovimentos.get(i).getArgumento1()).getVencimento());
                    ((DataObject) listMovimentos.get(i)).setArgumento1(movim);
                } else {
                    movim = (Movimento) listMovimentos.get(i).getArgumento1();
                    movim.setValor(Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento3()));
                    String vencto = ((Movimento) listMovimentos.get(i).getArgumento1()).getVencimento();

                    if (GerarMovimento.salvarUmMovimento(lote, movim)) {
                        movim.setVencimento(vencto);
                        novoLog.save(
                                " Movimento: (" + movim.getId() + ") "
                                + " - Referência: (" + movim.getReferencia()
                                + " - Tipo Serviço: (" + movim.getTipoServico().getId() + ") " + movim.getTipoServico().getDescricao()
                                + " - Serviços: (" + movim.getServicos().getId() + ") " + movim.getServicos().getDescricao()
                                + " - Pessoa: (" + movim.getPessoa().getId() + ") " + movim.getPessoa().getNome()
                                + " - Valor: " + movim.getValorString()
                                + " - Vencimento: " + movim.getVencimento()
                        );
                        msgConfirma = "Gerado com sucesso!";
                    } else {
                        msgConfirma = "Erro ao Gerar boletos!";
                    }
                }
                movimentoBefore = new Movimento();
                movim = new Movimento();
                lote = new Lote();
                beforeUpdate = "";
            }
            processou = true;
        }
        return null;
    }

    public String imprimirBoleto() {
        List<Movimento> movs = new ArrayList();
        List<Float> listaValores = new ArrayList();
        List<String> listaVencimentos = new ArrayList();

        Movimento movi = null;
        if (!listMovimentos.isEmpty()) {
            Usuario usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            sv.abrirTransacao();
            for (int i = 0; i < listMovimentos.size(); i++) {
                movi = (Movimento) listMovimentos.get(i).getArgumento1();
                movs.add(movi);
                listaValores.add(Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento7()));
                //listaValores.add(movi.getValor());
                listaVencimentos.add(movi.getVencimento());

                Impressao impressao = new Impressao();

                impressao.setUsuario(usuario);
                impressao.setDtVencimento(movi.getDtVencimento());
                impressao.setMovimento(movi);

                if (!sv.inserirObjeto(impressao)) {
                    sv.desfazerTransacao();
                    GenericaMensagem.error("Erro", "Não foi possível SALVAR impressão!");
                    return null;
                }
            }

            sv.comitarTransacao();

            ImprimirBoleto imp = new ImprimirBoleto();

            movs = imp.atualizaContaCobrancaMovimento(movs);

            imp.imprimirBoleto(movs, listaValores, listaVencimentos, imprimeVerso);

            imp.visualizar(null);
        }
        return null;
    }

    public String enviarEmail() {
        JuridicaDB db = new JuridicaDBToplink();
        Juridica jur = new Juridica();
        List<Movimento> movs = new ArrayList<Movimento>();
        String empresasSemEmail = "";

        Registro reg = new Registro();
        reg = (Registro) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Registro");
        msgConfirma = "";
        if (tipoEnvio.equals("empresa")) {
            if (!listMovimentos.isEmpty()) {
                for (int i = 0; i < listMovimentos.size(); i++) {
                    jur = db.pesquisaJuridicaPorPessoa(((Movimento) listMovimentos.get(i).getArgumento1()).getPessoa().getId());
                    if (jur.getPessoa().getEmail1().equals("") || jur.getPessoa().getEmail1() == null) {
                        if (empresasSemEmail.equals("")) {
                            empresasSemEmail = jur.getPessoa().getNome();
                        } else {
                            empresasSemEmail = empresasSemEmail + ", " + jur.getPessoa().getNome();
                        }
                    }
                    jur = new Juridica();
                }
                if (!empresasSemEmail.equals("")) {
                    msgConfirma = " Empresas " + empresasSemEmail + " não contém e-mail para envio!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    return null;
                }

                if (!outrasEmpresas) {
                    enviarEmailPraUma(juridica);
                    return null;
                }

                List<Float> listaValores = new ArrayList<Float>();
                List<String> listaVencimentos = new ArrayList<String>();
                String mensagem = "";
                List<File> fls = new ArrayList<File>();

                for (int i = 0; i < listMovimentos.size(); i++) {
                    ((Movimento) listMovimentos.get(i).getArgumento1()).setValor(Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento3()));
                    movs.add((Movimento) listMovimentos.get(i).getArgumento1());
                    listaValores.add(Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento7())); // IMPRIMIR COM O VALOR CALCULADO
                    //listaValores.add(movs.get(i).getValor());
                    listaVencimentos.add(movs.get(0).getVencimento());

                    ImprimirBoleto imp = new ImprimirBoleto();
                    imp.imprimirBoleto(movs, listaValores, listaVencimentos, imprimeVerso);
                    String nome = imp.criarLink(movs.get(0).getPessoa(), reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
                    List<Pessoa> pessoas = new ArrayList();

                    pessoas.add(movs.get(0).getPessoa());

                    String nome_envio = "";
                    if (listMovimentos.size() == 1) {
                        nome_envio = "Boleto " + ((Movimento) listMovimentos.get(0).getArgumento1()).getServicos().getDescricao() + " N° " + ((Movimento) listMovimentos.get(0).getArgumento1()).getDocumento();
                    } else {
                        nome_envio = "Boleto";
                    }

                    if (!reg.isEnviarEmailAnexo()) {
                        mensagem = " <h5> Visualize seu boleto clicando no link abaixo </h5> <br /><br />"
                                + " <a href='" + reg.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "' target='_blank'>Clique aqui para abrir boleto</a><br />";
                    } else {
                        fls.add(new File(imp.getPathPasta() + "/" + nome));
                        mensagem = "<h5>Segue boleto em anexo</h5><br /><br />";
                    }

                    DaoInterface di = new Dao();
                    Mail mail = new Mail();
                    mail.setFiles(fls);
                    mail.setEmail(
                            new Email(
                                    -1,
                                    DataHoje.dataHoje(),
                                    DataHoje.livre(new Date(), "HH:mm"),
                                    (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                                    (Rotina) di.find(new Rotina(), 106),
                                    null,
                                    nome_envio,
                                    mensagem,
                                    false,
                                    false
                            )
                    );

                    List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
                    EmailPessoa emailPessoa = new EmailPessoa();

                    for (Pessoa pe : pessoas) {
                        emailPessoa.setDestinatario(pe.getEmail1());
                        emailPessoa.setPessoa(pe);
                        emailPessoa.setRecebimento(null);
                        emailPessoas.add(emailPessoa);
                        mail.setEmailPessoas(emailPessoas);
                        emailPessoa = new EmailPessoa();
                    }

                    String[] retorno = mail.send("personalizado");

                    if (!retorno[1].isEmpty()) {
                        msgConfirma = retorno[1];
                        GenericaMensagem.warn("Erro", msgConfirma);
                    } else {
                        msgConfirma = retorno[0];
                        GenericaMensagem.info("Sucesso", msgConfirma);
                    }

                    movs.clear();
                    listaValores.clear();
                    listaVencimentos.clear();
                    pessoas.clear();
                    fls.clear();
                }
            }
        } else {
            if (!listMovimentos.isEmpty()) {
                for (int i = 0; i < listMovimentos.size(); i++) {
                    ((Movimento) listMovimentos.get(i).getArgumento1()).setValor(Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento7()));
                    movs.add((Movimento) listMovimentos.get(i).getArgumento1());
                }

                List<Movimento> m = new ArrayList();
                List<Float> listaValores = new ArrayList<Float>();
                List<String> listaVencimentos = new ArrayList<String>();
                List<File> fls = new ArrayList<File>();
                String mensagem = "";
                for (int i = 0; i < movs.size(); i++) {
                    jur = db.pesquisaJuridicaPorPessoa(movs.get(i).getPessoa().getId());
                    if (jur.getContabilidade() == null) {
                        if (jur.getPessoa().getEmail1().equals("") || jur.getPessoa().getEmail1() == null) {
                            if (empresasSemEmail.equals("")) {
                                empresasSemEmail = jur.getPessoa().getNome();
                            } else {
                                empresasSemEmail = empresasSemEmail + ", " + jur.getPessoa().getNome();
                            }
                            jur = new Juridica();
                            continue;
                        }
                        m.add(movs.get(i));
                        listaValores.add(movs.get(i).getValor());
                        listaVencimentos.add(movs.get(i).getVencimento());

                        ImprimirBoleto imp = new ImprimirBoleto();
                        imp.imprimirBoleto(m, listaValores, listaVencimentos, imprimeVerso);
                        String nome = imp.criarLink(jur.getPessoa(), reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
                        List<Pessoa> pessoas = new ArrayList();
                        pessoas.add(jur.getPessoa());

                        String nome_envio = "";
                        if (movs.size() == 1) {
                            nome_envio = "Boleto " + m.get(0).getServicos().getDescricao() + " N° " + m.get(0).getDocumento();
                        } else {
                            nome_envio = "Boleto";
                        }

                        if (!reg.isEnviarEmailAnexo()) {
                            mensagem = " <h5> Visualize seu boleto clicando no link abaixo </h5> <br /><br />"
                                    + " <a href='" + reg.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "' target='_blank'>Clique aqui para abrir boleto</a><br />";
                        } else {
                            fls.add(new File(imp.getPathPasta() + "/" + nome));
                            mensagem = "<h5>Segue boleto em anexo</h5><br /><br />";
                        }

                        DaoInterface di = new Dao();
                        Mail mail = new Mail();
                        mail.setFiles(fls);
                        mail.setEmail(
                                new Email(
                                        -1,
                                        DataHoje.dataHoje(),
                                        DataHoje.livre(new Date(), "HH:mm"),
                                        (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                                        (Rotina) di.find(new Rotina(), 106),
                                        null,
                                        nome_envio,
                                        mensagem,
                                        false,
                                        false
                                )
                        );

                        List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
                        EmailPessoa emailPessoa = new EmailPessoa();
                        for (Pessoa pe : pessoas) {
                            emailPessoa.setDestinatario(pe.getEmail1());
                            emailPessoa.setPessoa(pe);
                            emailPessoa.setRecebimento(null);
                            emailPessoas.add(emailPessoa);
                            mail.setEmailPessoas(emailPessoas);
                            emailPessoa = new EmailPessoa();
                        }

                        String[] retorno = mail.send("personalizado");

                        if (!retorno[1].isEmpty()) {
                            msgConfirma = retorno[1];
                            GenericaMensagem.warn("Erro", msgConfirma);
                        } else {
                            msgConfirma = retorno[0];
                            GenericaMensagem.info("Sucesso", msgConfirma);
                        }

                        m.clear();
                        listaValores.clear();
                        listaVencimentos.clear();
                        pessoas.clear();
                        fls.clear();
                    } else {
                        if (jur.getContabilidade().getPessoa().getEmail1() == null || jur.getContabilidade().getPessoa().getEmail1().isEmpty()) {
                            if (empresasSemEmail.equals("")) {
                                empresasSemEmail = jur.getContabilidade().getPessoa().getNome() + " da empresa " + jur.getPessoa().getNome();
                            } else {
                                empresasSemEmail = empresasSemEmail + ", " + jur.getContabilidade().getPessoa().getNome() + " da empresa " + jur.getPessoa().getNome();
                            }
                            jur = new Juridica();
                            continue;
                        }
                        m.add(movs.get(i));
                        listaValores.add(movs.get(i).getValor());
                        listaVencimentos.add(movs.get(i).getVencimento());

                        ImprimirBoleto imp = new ImprimirBoleto();
                        imp.imprimirBoleto(m, listaValores, listaVencimentos, imprimeVerso);
                        String nome = imp.criarLink(jur.getContabilidade().getPessoa(), reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
                        List<Pessoa> pessoas = new ArrayList();
                        pessoas.add(jur.getContabilidade().getPessoa());

                        String nome_envio = "";
                        if (m.size() == 1) {
                            nome_envio = "Boleto " + m.get(0).getServicos().getDescricao() + " N° " + m.get(0).getDocumento();
                        } else {
                            nome_envio = "Boleto";
                        }

                        if (!reg.isEnviarEmailAnexo()) {
                            mensagem = " <h5> Visualize seu boleto clicando no link abaixo </h5> <br /><br />"
                                    + " <a href='" + reg.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "' target='_blank'>Clique aqui para abrir boleto</a><br />";
                        } else {
                            fls.add(new File(imp.getPathPasta() + "/" + nome));
                            mensagem = "<h5>Segue boleto em anexo</h5><br /><br />";
                        }

                        DaoInterface di = new Dao();
                        Mail mail = new Mail();
                        mail.setFiles(fls);
                        mail.setEmail(
                                new Email(
                                        -1,
                                        DataHoje.dataHoje(),
                                        DataHoje.livre(new Date(), "HH:mm"),
                                        (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                                        (Rotina) di.find(new Rotina(), 106),
                                        null,
                                        nome_envio,
                                        mensagem,
                                        false,
                                        false
                                )
                        );

                        List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
                        EmailPessoa emailPessoa = new EmailPessoa();
                        for (Pessoa pe : pessoas) {
                            emailPessoa.setDestinatario(pe.getEmail1());
                            emailPessoa.setPessoa(pe);
                            emailPessoa.setRecebimento(null);
                            emailPessoas.add(emailPessoa);
                            mail.setEmailPessoas(emailPessoas);
                            emailPessoa = new EmailPessoa();
                        }

                        String[] retorno = mail.send("personalizado");

                        if (!retorno[1].isEmpty()) {
                            msgConfirma = retorno[1];
                            GenericaMensagem.warn("Erro", msgConfirma);
                        } else {
                            msgConfirma = retorno[0];
                            GenericaMensagem.info("Sucesso", msgConfirma);
                        }

                        m.clear();
                        listaValores.clear();
                        listaVencimentos.clear();
                        pessoas.clear();
                        fls.clear();
                    }
                }
            }
            if (!empresasSemEmail.equals("")) {
                msgConfirma = msgConfirma + empresasSemEmail + " não contém e-mail para envio!";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
            return null;
        }
        return null;
    }

    public void enviarEmailPraUma(Juridica juridica) {
        List<Movimento> movs = new ArrayList<Movimento>();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();

        if (!listMovimentos.isEmpty()) {
            Registro reg = new Registro();
            reg = (Registro) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Registro");
            for (int i = 0; i < listMovimentos.size(); i++) {
                ((Movimento) listMovimentos.get(i).getArgumento1()).setValor(Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento3()));
                movs.add((Movimento) listMovimentos.get(i).getArgumento1());
                listaValores.add(Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento7())); // IMPRIMIR COM VALOR CALCULADO
                //listaValores.add(( (Movimento) listMovimentos.get(i).getArgumento1()).getValor());
                listaVencimentos.add(((Movimento) listMovimentos.get(i).getArgumento1()).getVencimento());
            }
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String path = request.getQueryString();

            ImprimirBoleto imp = new ImprimirBoleto();
            imp.imprimirBoleto(movs, listaValores, listaVencimentos, imprimeVerso);
            String nome = imp.criarLink(juridica.getPessoa(), reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
            List<Pessoa> pessoas = new ArrayList();
            pessoas.add(juridica.getPessoa());

            List<File> fls = new ArrayList<File>();
            String mensagem = "";

            String nome_envio = "";
            if (movs.size() == 1) {
                nome_envio = "Boleto " + movs.get(0).getServicos().getDescricao() + " N° " + movs.get(0).getDocumento();
            } else {
                nome_envio = "Boleto";
            }

            if (!reg.isEnviarEmailAnexo()) {
                mensagem = " <h5> Visualize seu boleto clicando no link abaixo </h5> <br /><br />"
                        + " <a href='" + reg.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "' target='_blank'>Clique aqui para abrir o boleto</a><br />";
            } else {
                fls.add(new File(imp.getPathPasta() + "/" + nome));
                mensagem = "<h5>Segue boleto em anexo</h5><br /><br />";
            }

            DaoInterface di = new Dao();
            Mail mail = new Mail();
            mail.setFiles(fls);
            mail.setEmail(
                    new Email(
                            -1,
                            DataHoje.dataHoje(),
                            DataHoje.livre(new Date(), "HH:mm"),
                            (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                            (Rotina) di.find(new Rotina(), 106),
                            null,
                            nome_envio,
                            mensagem,
                            false,
                            false
                    )
            );

            List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
            EmailPessoa emailPessoa = new EmailPessoa();
            for (Pessoa pe : pessoas) {
                emailPessoa.setDestinatario(pe.getEmail1());
                emailPessoa.setPessoa(pe);
                emailPessoa.setRecebimento(null);
                emailPessoas.add(emailPessoa);
                mail.setEmailPessoas(emailPessoas);
                emailPessoa = new EmailPessoa();
            }

            String[] retorno = mail.send("personalizado");

            if (!retorno[1].isEmpty()) {
                msgConfirma = retorno[1];
                GenericaMensagem.warn("Erro", msgConfirma);
            } else {
                msgConfirma = retorno[0];
                GenericaMensagem.info("Sucesso", msgConfirma);
            }
        }
    }

    public String getLinks() throws IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        linkAcesso = request.getParameter("arquivo");
        if (!linkAcesso.isEmpty()) {
            LinksDB db = new LinksDBToplink();
            Links link = db.pesquisaNomeArquivo(linkAcesso);
            if (link != null) {
                DataHoje dt = new DataHoje();
                int data1 = dt.converteDataParaInteger(dt.incrementarDias(30, link.getEmissao()));
                int data2 = dt.converteDataParaInteger(dt.data());

                if (data1 < data2) {
                    return "Arquivo expirado!";
                } else {
                    //String pathFile = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(link.getCaminho() + "/" + link.getNomeArquivo());
                    String pathFile = link.getCaminho() + "/" + link.getNomeArquivo();
                    //if (new File(pathFile).exists()) {
                    response.sendRedirect(link.getCaminho() + "/" + link.getNomeArquivo());
                    //} else {
                    //    NovoLog log = new NovoLog();
                    //    log.novo("Arquivo não encontrado", "Arquivo não existe no caminho específicado: " + link.getCaminho() + "/" + link.getNomeArquivo());
                    // }
                }
            }
        }
        return "Nenhum arquivo encontrado!";
    }

    public String editarJuridica(DataObject linha) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        JuridicaDB db = new JuridicaDBToplink();
        Juridica jur = new Juridica();
        jur = db.pesquisaJuridicaPorPessoa(((Movimento) linha.getArgumento1()).getPessoa().getId());
        JuridicaBean juridicaBean = new JuridicaBean();
        juridicaBean.editar(jur, true);
        GenericaSessao.put("juridicaBean", juridicaBean);
        // ((JuridicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaBean")).editar(jur);
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pesquisa("pessoaJuridica");
    }

    public List<SelectItem> getListaServico() {
        List<SelectItem> servicos = new Vector<SelectItem>();
        int i = 0;
        ServicosDB db = new ServicosDBToplink();
        List select = db.pesquisaTodos(4);
        if (select.size() != 0) {
            while (i < select.size()) {
                servicos.add(new SelectItem(
                        new Integer(i),
                        (String) ((Servicos) select.get(i)).getDescricao(),
                        Integer.toString(((Servicos) select.get(i)).getId())));
                i++;
            }
        }
        return servicos;
    }

    public List<SelectItem> getListaTipoServico() {
        List<SelectItem> tipoServico = new Vector<SelectItem>();
        FilialDB filDB = new FilialDao();
        DataHoje data = new DataHoje();
        Registro registro = filDB.pesquisaRegistroPorFilial(1);
        Servicos servicos = (Servicos) new Dao().find(new Servicos(), Integer.valueOf(getListaServico().get(idServicos).getDescription()));
        int i = 0;
        TipoServicoDB db = new TipoServicoDBToplink();
        if ((!data.integridadeReferencia(strReferencia))
                || (registro == null)
                || (servicos == null)) {
            return tipoServico;
        }
        List select = null;
        List<Integer> listaIds = new ArrayList<Integer>();

        listaIds.add(1);
        listaIds.add(2);
        listaIds.add(3);
        select = db.pesquisaTodosComIds(listaIds);
        if (!select.isEmpty()) {
            while (i < select.size()) {
                tipoServico.add(new SelectItem(
                        new Integer(i),
                        (String) ((TipoServico) select.get(i)).getDescricao(),
                        Integer.toString(((TipoServico) select.get(i)).getId())));
                i++;
            }
        }
        return tipoServico;
    }

    public void refreshForm() {
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public int getNrBoletos() {
        if (!listMovimentos.isEmpty()) {
            nrBoletos = listMovimentos.size();
        } else {
            nrBoletos = 0;
        }
        return nrBoletos;
    }

    public void setNrBoletos(int nrBoletos) {
        this.nrBoletos = nrBoletos;
    }

    public int getProcessados() {
        return processados;
    }

    public void setProcessados(int processados) {
        this.processados = processados;
    }

    public int getProcessando() {
        return processando;
    }

    public void setProcessando(int processando) {
        this.processando = processando;
    }

    public int getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(int idTipoServico) {
        this.idTipoServico = idTipoServico;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public Juridica getJuridica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            juridica = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");
            carregaList = true;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            filtrarReferencia();
        }
        return juridica;
    }

    public void validaReferenciaVencimento(MensagemConvencao mensa) {
        if (juridica.getPessoa().getId() != -1) {
            if (mensa != null) {
                marcarVencimento = false;
            } else {
                marcarVencimento = true;
            }
            marcarReferencia = false;
        } else {
            marcarVencimento = true;
            marcarReferencia = true;
        }
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public String getFrenteVerso() {
        return frenteVerso;
    }

    public void setFrenteVerso(String frenteVerso) {
        this.frenteVerso = frenteVerso;
    }

    public String getnImpressos() {
        return nImpressos;
    }

    public void setnImpressos(String nImpressos) {
        this.nImpressos = nImpressos;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public boolean isRenVisualizar() {
        return renVisualizar;
    }

    public void setRenVisualizar(boolean renVisualizar) {
        this.renVisualizar = renVisualizar;
    }

    public String getStrReferencia() {
        return strReferencia;
    }

    public void setStrReferencia(String strReferencia) {
        this.strReferencia = strReferencia;
    }

    public boolean isRenLimparTodos() {
        return renLimparTodos;
    }

    public void setRenLimparTodos(boolean renLimparTodos) {
        this.renLimparTodos = renLimparTodos;
    }

    public boolean isImprimeVerso() {
        return imprimeVerso;
    }

    public void setImprimeVerso(boolean imprimeVerso) {
        this.imprimeVerso = imprimeVerso;
    }

    public boolean isMarcarTodos() {
        return marcarTodos;
    }

    public void setMarcarTodos(boolean marcarTodos) {
        this.marcarTodos = marcarTodos;
    }

    public boolean isMarcarVencimento() {
        return marcarVencimento;
    }

    public void setMarcarVencimento(boolean marcarVencimento) {
        this.marcarVencimento = marcarVencimento;
    }

    public boolean isMarcarReferencia() {
        return marcarReferencia;
    }

    public void setMarcarReferencia(boolean marcarReferencia) {
        this.marcarReferencia = marcarReferencia;
    }

    public Object getProcessamento() {
        processamento = new Integer(-1);
        return processamento;
    }

    public void setProcessamento(Object processamento) {
        this.processamento = processamento;
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public boolean isProcessou() {
        return processou;
    }

    public void setProcessou(boolean processou) {
        this.processou = processou;
    }

    public String getMsgConfirmaTela() {
        return msgConfirmaTela;
    }

    public void setMsgConfirmaTela(String msgConfirmaTela) {
        this.msgConfirmaTela = msgConfirmaTela;
    }

    public Pessoa getPessoaEnvio() {
        return pessoaEnvio;
    }

    public void setPessoaEnvio(Pessoa pessoaEnvio) {
        this.pessoaEnvio = pessoaEnvio;
    }
}
