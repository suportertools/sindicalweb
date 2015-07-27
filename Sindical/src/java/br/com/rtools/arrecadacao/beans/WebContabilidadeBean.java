package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.MensagemConvencao;
import br.com.rtools.arrecadacao.db.MensagemConvencaoDB;
import br.com.rtools.arrecadacao.db.MensagemConvencaoDBToplink;
import br.com.rtools.arrecadacao.db.WebContabilidadeDB;
import br.com.rtools.arrecadacao.db.WebContabilidadeDBToplink;
import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.ImpressaoWeb;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.beans.MovimentoValorBean;
import br.com.rtools.financeiro.db.ContaCobrancaDB;
import br.com.rtools.financeiro.db.ContaCobrancaDBToplink;
import br.com.rtools.financeiro.db.FTipoDocumentoDB;
import br.com.rtools.financeiro.db.FTipoDocumentoDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.financeiro.db.TipoServicoDB;
import br.com.rtools.financeiro.db.TipoServicoDBToplink;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDao;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class WebContabilidadeBean extends MovimentoValorBean {

    private Pessoa pessoa = null;
    private Juridica juridica = new Juridica();
    private Servicos servico = new Servicos();
    private TipoServico tipoServico = new TipoServico();
    private List<Juridica> listaEmpresa = new ArrayList();
    private List<Juridica> listaEmpresaSelecionada = new ArrayList();
    private List<DataObject> listaMovimento = new ArrayList();
    private List<DataObject> listaMovimentoSelecionado = new ArrayList();
    private int idServicos = 0;
    private int idTipoServico = 0;
    private String strReferencia = "";
    private String strVencimento = "";
    private String strFiltroRef = "";
    private String vlFolha = "0";
    private boolean impVerso = false;
    private final List<SelectItem> listaVencimento = new ArrayList();
    private Set<Integer> keys = null;
    private String lblLink = "";
    private Registro registro = new Registro();

    public WebContabilidadeBean() {
        FilialDB filDB = new FilialDao();
        registro = filDB.pesquisaRegistroPorFilial(1);
        pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb");
        juridica = new JuridicaDBToplink().pesquisaJuridicaPorPessoa(pessoa.getId());

        WebContabilidadeDB db = new WebContabilidadeDBToplink();
        listaEmpresa = db.listaEmpresasPertContabilidade(juridica.getId());

    }

    public void loadList() {
        try {
            listaMovimento.clear();
            if (listaEmpresaSelecionada.isEmpty()) {
                return;
            }

            WebContabilidadeDB db = new WebContabilidadeDBToplink();
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            for (int ix = 0; ix < listaEmpresaSelecionada.size(); ix++) {
                List lista_result = new ArrayList();
                if (strFiltroRef.isEmpty()) {
                    lista_result = db.pesquisaMovParaWebContabilidade(listaEmpresaSelecionada.get(ix).getPessoa().getId());
                } else {
                    lista_result = db.pesquisaMovParaWebContabilidadeComRef(listaEmpresaSelecionada.get(ix).getPessoa().getId(), strFiltroRef);
                }

                for (Object linha : lista_result) {
                    if (((Vector) linha).get(5) == null) {
                        ((Vector) linha).set(5, 0.0);
                    }
                    if (((Vector) linha).get(6) == null) {
                        ((Vector) linha).set(6, 0.0);
                    }
                    if (((Vector) linha).get(7) == null) {
                        ((Vector) linha).set(7, 0.0);
                    }
                    if (((Vector) linha).get(8) == null) {
                        ((Vector) linha).set(8, 0.0);
                    }
                    if (((Vector) linha).get(9) == null) {
                        ((Vector) linha).set(9, 0.0);
                    }
                    if (((Vector) linha).get(10) == null) {
                        ((Vector) linha).set(10, 0.0);
                    }
                    if (((Vector) linha).get(11) == null) {
                        ((Vector) linha).set(11, 0.0);
                    }
                    if (((Integer) ((Vector) linha).get(13)) < 0) {
                        ((Vector) linha).set(13, 0);
                    }
                    boolean hdata, hvalor;
                    int data1 = DataHoje.converteDataParaInteger(DataHoje.converteData((Date) ((Vector) linha).get(4)));
                    if (data1 < DataHoje.converteDataParaInteger(DataHoje.data())) {
                        hdata = true;
                    } else {
                        hdata = false;
                    }
                    // valor ----
                    if ((Integer) ((Vector) linha).get(2) == 4) {
                        hvalor = false;
                    } else {
                        hvalor = true;
                    }
                    listaMovimento.add(new DataObject(
                            false,
                            ((Vector) linha).get(0), // boleto
                            sv.pesquisaCodigo((Integer) ((Vector) linha).get(1), "Servicos"), // servico
                            sv.pesquisaCodigo((Integer) ((Vector) linha).get(2), "TipoServico"), // tipo
                            ((Vector) linha).get(3), // referencia
                            DataHoje.converteData((Date) ((Vector) linha).get(4)), // vencimento
                            Moeda.converteR$(Double.toString((Double) ((Vector) linha).get(5))), // valor_mov
                            ((Vector) linha).get(6), // valor_folha
                            Moeda.converteR$(Double.toString((Double) ((Vector) linha).get(7))), // multa
                            Moeda.converteR$(Double.toString((Double) ((Vector) linha).get(8))), // juros
                            Moeda.converteR$(Double.toString((Double) ((Vector) linha).get(9))), // correcao
                            Moeda.converteR$(Double.toString((Double) ((Vector) linha).get(10))), // desconto
                            Moeda.converteR$(Double.toString((Double) ((Vector) linha).get(11))), // valor_calculado
                            ((Vector) linha).get(12), // meses em atraso
                            ((Vector) linha).get(13), // dias em atraso
                            ((Vector) linha).get(14), // indice
                            ((Vector) linha).get(15), // id movimento
                            Moeda.converteR$(Double.toString((Double) ((Vector) linha).get(11))), // valor_calculado original
                            hdata, // null
                            hvalor, // null
                            "0", // null
                            listaEmpresaSelecionada.get(ix).getPessoa().getNome() // null
                    ));
                }
            }

        } catch (Exception e) {
        }
    }

    public List<SelectItem> getListaVencimento() {
        if (listaVencimento.isEmpty()) {
            String data = DataHoje.data();
            String newData = "";
            int i = 0;
            while (i < 6) {
                newData = (new DataHoje()).incrementarDias(i, data);
                listaVencimento.add(new SelectItem(new Integer(i), newData, newData));
                i++;
            }
        }
        return listaVencimento;
    }

    public void adicionarBoleto() {
        try {
            if (listaEmpresaSelecionada.isEmpty()) {
                GenericaMensagem.warn("Atenção", "Selecione uma empresa para gerar Boletos!");
                return;
            }

            MensagemConvencao mc = new MensagemConvencao();
            MensagemConvencaoDB menDB = new MensagemConvencaoDBToplink();
            ServicosDB dbSer = new ServicosDBToplink();
            TipoServicoDB dbTipo = new TipoServicoDBToplink();
            FTipoDocumentoDB dbFTipoDocumento = new FTipoDocumentoDBToplink();

            ContaCobrancaDB ctaCobraDB = new ContaCobrancaDBToplink();
            ContaCobranca contaCob = new ContaCobranca();

            if (getListaServicos().isEmpty()) {
                GenericaMensagem.warn("Atenção", "Lista de Serviços está vazia!");
                return;
            }

            if (getListaTipoServico().isEmpty()) {
                GenericaMensagem.warn("Atenção", "Lista de Tipo Serviço está vazia!");
                return;
            }

            servico = (Servicos) new Dao().find(new Servicos(), Integer.valueOf(getListaServicos().get(idServicos).getDescription()));
            tipoServico = dbTipo.pesquisaCodigo(Integer.valueOf(getListaTipoServico().get(idTipoServico).getDescription()));
            Juridica juri = new Juridica();

            MovimentoDB dbm = new MovimentoDBToplink();

            contaCob = ctaCobraDB.pesquisaServicoCobranca(servico.getId(), tipoServico.getId());
            if (contaCob == null) {
                GenericaMensagem.warn("Atenção", "Não existe conta Cobrança para gerar!");
                return;
            }

            if (dbm.pesquisaMovimentosAcordado(juridica.getPessoa().getId(), strReferencia, tipoServico.getId(), servico.getId()) != null) {
                GenericaMensagem.warn("Atenção", "Já foi gerado um Acordo para esta referência, serviço e tipo de serviço!");
                return;
            }

            if ((new DataHoje()).integridadeReferencia(strReferencia)) {
                for (int i = 0; i < listaEmpresaSelecionada.size(); i++) {
                    juri = listaEmpresaSelecionada.get(i);
                    if (dbm.pesquisaMovimentos(juri.getPessoa().getId(), strReferencia, tipoServico.getId(), servico.getId()) != null) {
                        GenericaMensagem.warn("Atenção", "Este boleto já existe para " + juri.getPessoa().getNome());
                        continue;
                    }

                    String dataValida = "";
                    DataHoje dh = new DataHoje();
                    mc = menDB.retornaDiaString(juri.getId(), strReferencia, tipoServico.getId(), servico.getId());

                    if (mc != null) {
                        if (registro.getDiasBloqueiaAtrasadosWeb() <= 0) {
                            strVencimento = mc.getVencimento();
                            dataValida = strVencimento;
                        } else {
                            strVencimento = mc.getVencimento();
                            dataValida = dh.incrementarDias(registro.getDiasBloqueiaAtrasadosWeb(), strVencimento);
                        }

                        if (getValidaBloqueio(dataValida)) {
                            GenericaMensagem.warn("Atenção", "Não é permitido gerar boleto vencido! " + registro.getMensagemBloqueioBoletoWeb());
                            return;
                        }

                        Movimento movi = new Movimento(-1,
                                null,
                                servico.getPlano5(),
                                juri.getPessoa(),
                                servico,
                                null,
                                tipoServico,
                                null,
                                Moeda.converteFloatR$Float(super.carregarValor(servico.getId(), tipoServico.getId(), strReferencia, juridica.getPessoa().getId())),
                                strReferencia,
                                strVencimento,
                                1,
                                true,
                                "E",
                                false,
                                juri.getPessoa(),
                                juri.getPessoa(),
                                "",
                                "",
                                strVencimento,
                                0,
                                0, 0, 0, 0, 0, 0, dbFTipoDocumento.pesquisaCodigo(2), 0, null);

                        GerarMovimento.salvarUmMovimento(new Lote(), movi);
                    } else {
                        GenericaMensagem.warn("Atenção", "Mensagem Convenção não existe. Entrar em contato com seu Sindicato para permitir a criação desta referencia !");
                        return;
                    }
                }

                GenericaMensagem.info("Sucesso", "Boletos adicionados!");
                loadList();
            } else {
                GenericaMensagem.warn("Atenção", "Referência não é válida!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean getValidaBloqueio(String data) {
        if (registro.isBloqueiaAtrasadosWeb()) {
            int data1 = DataHoje.converteDataParaInteger(data);
            int data2 = DataHoje.converteDataParaInteger(DataHoje.data());

            if (data1 < data2) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String imprimirBoleto() {
        List<Movimento> lista = new ArrayList<Movimento>();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();
        Movimento movimento;
        for (int i = 0; i < listaMovimentoSelecionado.size(); i++) {
            movimento = (Movimento) listaMovimentoSelecionado.get(i).getArgumento1();
            lista.add(movimento);
            listaValores.add(movimento.getValor());
            listaVencimentos.add(movimento.getVencimento());
        }

        ImprimirBoleto imp = new ImprimirBoleto();
        imp.imprimirBoleto(lista, listaValores, listaVencimentos, impVerso);
        imp.visualizar(null);
        return null;
    }

    public void validaReferencia() {
        DataHoje dataHoje = new DataHoje();
        if (dataHoje.integridadeReferencia(strReferencia)) {
            String dataS = "01/" + strReferencia;
            // A diferença é de no máximo 5 anos para geração de boletos
            String dataLimite = dataHoje.decrementarMeses(60, DataHoje.data());
            Integer[] integer = dataHoje.diferencaEntreDatas(dataLimite, dataS);
            if (integer == null) {
                strReferencia = "";
                GenericaMensagem.warn("Atenção", "Essa referência não é válida!");
                return;
            }
            if (integer[2] < 0) {
                strReferencia = "";
                GenericaMensagem.warn("Atenção", "Não é permitido emitir boletos com período superior a 5 anos atras!");
                return;
            }
            if (integer[2] > 5) {
                strReferencia = "";
                GenericaMensagem.warn("Atenção", "Não é permitido emitir boletos com períodos futuros que excedem a faixa do ano atual! Ex. Não é possível emitir um boleto de referência superior ao ano corrente.");
            }
        } else {
            strReferencia = "";
            GenericaMensagem.warn("Atenção", "Essa referência não é válida!");
        }
    }

//    public void validaReferencia() {
//        DataHoje data = new DataHoje();
//        if (data.integridadeReferencia(strReferencia)) {
//            String dataS = "01/" + strReferencia;
//            if (!(data.faixaCincoAnosApos(dataS))) {
//                strReferencia = "";
//                GenericaMensagem.warn("Atenção", "Essa referência não é válida, faixa de 5 anos após!");
//            }
//        }else{
//            strReferencia = "";
//            GenericaMensagem.warn("Atenção", "Essa referência não é válida!");
//        }
//    }
    public String imprimirComValorCalculado() {
        List<Movimento> lista = new ArrayList<Movimento>();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();

        ImpressaoWeb impressaoWeb;
        Movimento movimento;
        String data = "";
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();

        String dataValida = "";
        DataHoje dh = new DataHoje();

        if (listaMovimentoSelecionado.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Nenhum boleto foi selecionado!");
            return null;
        }

        for (int x = 0; x < listaMovimentoSelecionado.size(); x++) {
            if (registro.getDiasBloqueiaAtrasadosWeb() <= 0) {
                dataValida = (String) ((DataObject) listaMovimentoSelecionado.get(x)).getArgumento5();
            } else {
                dataValida = dh.incrementarDias(registro.getDiasBloqueiaAtrasadosWeb(), (String) ((DataObject) listaMovimentoSelecionado.get(x)).getArgumento5());
            }

            if (getValidaBloqueio(dataValida)) {
                GenericaMensagem.warn("Atenção", "Não é possivel imprimir boletos vencidos! " + registro.getMensagemBloqueioBoletoWeb());
                return null;
            }
        }

        for (int i = 0; i < listaMovimentoSelecionado.size(); i++) {
            movimento = ((Movimento) sv.pesquisaCodigo((Integer) listaMovimentoSelecionado.get(i).getArgumento16(), "Movimento"));
            // COM VALOR ALTERADO ---------
            if (Moeda.substituiVirgulaFloat((String) listaMovimentoSelecionado.get(i).getArgumento12()) != 0) {
                listaValores.add(Moeda.substituiVirgulaFloat((String) listaMovimentoSelecionado.get(i).getArgumento12()));
            } else {
                if (Moeda.substituiVirgulaFloat((String) listaMovimentoSelecionado.get(i).getArgumento6()) <= 0) {
                    GenericaMensagem.warn("Atenção", "Valor não pode ser zerado!");
                    return null;
                }

                listaValores.add(Moeda.substituiVirgulaFloat((String) listaMovimentoSelecionado.get(i).getArgumento6()));
            }

            //COM DATA ALTERADA ---------
            if (movimento.getDtVencimento().before(DataHoje.dataHoje()) && !movimento.getVencimento().equals(DataHoje.data())) {
                data = getListaVencimento().get(Integer.parseInt((String) listaMovimentoSelecionado.get(i).getArgumento20())).getDescription();
                listaVencimentos.add(data);
            } else {
                listaVencimentos.add(movimento.getVencimento());
            }

            lista.add(movimento);
            impressaoWeb = new ImpressaoWeb(-1,
                    movimento,
                    pessoa,
                    DataHoje.dataHoje(), DataHoje.hora());
            if (!sv.inserirObjeto(impressaoWeb)) {
                GenericaMensagem.error("Erro", "Não foi possível salvar impressão web");
                sv.desfazerTransacao();
                return null;
            }
        }
        sv.comitarTransacao();

        ImprimirBoleto imp = new ImprimirBoleto();
        lista = imp.atualizaContaCobrancaMovimento(lista);

        imp.imprimirBoleto(lista, listaValores, listaVencimentos, impVerso);
        imp.visualizar(null);

        loadList();
        return "webContabilidade";
    }

    public List<SelectItem> getListaServicos() {
        ServicosDB db = new ServicosDBToplink();
        List<SelectItem> result = new Vector<SelectItem>();
        List servicos = db.pesquisaTodos(4);
        int i = 0;
        while (i < servicos.size()) {
            result.add(new SelectItem(new Integer(i),
                    ((Servicos) servicos.get(i)).getDescricao(),
                    Integer.toString(((Servicos) servicos.get(i)).getId())));
            i++;
        }
        return result;
    }

    public List<SelectItem> getListaTipoServico() {
        List<SelectItem> listaTipoServico = new Vector<SelectItem>();
        DataHoje data = new DataHoje();
        Servicos servicos = (Servicos) new Dao().find(new Servicos(), Integer.valueOf(getListaServicos().get(idServicos).getDescription()));
        int i = 0;
        TipoServicoDB db = new TipoServicoDBToplink();
        if ((!data.integridadeReferencia(strReferencia))
                || (registro == null)
                || (servicos == null)) {
            listaTipoServico.add(new SelectItem(0, "Digite uma referência", "0"));
            return listaTipoServico;
        }
        List<Integer> listaIds = new ArrayList<Integer>();

        if (registro.getTipoEmpresa().equals("E")) {
            if ((Integer.parseInt(strReferencia.substring(0, 2)) == 3)
                    && (servicos.getId() == 1)) {
                listaIds.add(1);
                listaIds.add(3);
            } else if ((Integer.parseInt(strReferencia.substring(0, 2)) != 3)
                    && (servicos.getId() == 1)) {
                listaIds.add(2);
                listaIds.add(3);
            } else {
                listaIds.add(1);
                listaIds.add(2);
                listaIds.add(3);
            }
        } else if (registro.getTipoEmpresa().equals("P")) {
            if ((Integer.parseInt(strReferencia.substring(0, 2)) == 1)
                    && (servicos.getId() == 1)) {
                listaIds.add(1);
                listaIds.add(3);
            } else if ((Integer.parseInt(strReferencia.substring(0, 2)) != 1)
                    && (servicos.getId() == 1)) {
                listaIds.add(2);
                listaIds.add(3);
            } else {
                listaIds.add(1);
                listaIds.add(2);
                listaIds.add(3);
            }
        } else {
            return listaTipoServico;
        }
        List<TipoServico> select = db.pesquisaTodosComIds(listaIds);
        if (!select.isEmpty()) {
            while (i < select.size()) {
                listaTipoServico.add(new SelectItem(
                        i,
                        select.get(i).getDescricao(),
                        Integer.toString(select.get(i).getId())));
                i++;
            }
        } else {
            listaTipoServico.add(new SelectItem(0, "Selecionar um Tipo Serviço", "0"));
        }
        return listaTipoServico;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(int idTipoServico) {
        this.idTipoServico = idTipoServico;
    }

    public String getStrReferencia() {
        return strReferencia;
    }

    public void setStrReferencia(String strReferencia) {
        this.strReferencia = strReferencia;
    }

    public String getVlFolha() {
        return Moeda.converteR$(vlFolha);
    }

    public void setVlFolha(String vlFolha) {
        this.vlFolha = Moeda.substituiVirgula(vlFolha);
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public Servicos getServico() {
        return servico;
    }

    public void setServico(Servicos servico) {
        this.servico = servico;
    }

    public String getStrVencimento() {
        return strVencimento;
    }

    public void setStrVencimento(String strVencimento) {
        this.strVencimento = strVencimento;
    }

    public boolean isImpVerso() {
        return impVerso;
    }

    public void setImpVerso(boolean impVerso) {
        this.impVerso = impVerso;
    }

    public void setListaVencimento(List<SelectItem> listaVencimento) {
        this.setListaVencimento(listaVencimento);
    }

    public synchronized Set getKeys() {
        return keys;
    }

    public synchronized void setKeys(Set keys) {
        this.keys = keys;
    }

    @Override
    public void carregarFolha(DataObject data) {
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(data.getArgumento16())));
        super.carregarFolha(movimento);
    }

    @Override
    public void carregarFolha() {
    }

    @Override
    public void atualizaValorGrid(String tipo) {
        // antes ---
        //listMovimentos.get(idIndex).setArgumento6(super.atualizaValor(true));

        //listaMovimentos.get(idIndex).setArgumento6(super.atualizaValor(true, tipo));
        super.atualizaValor(true, tipo);
        loadList();
    }

    public String getStrFiltroRef() {
        return strFiltroRef;
    }

    public void setStrFiltroRef(String strFiltroRef) {
        this.strFiltroRef = strFiltroRef;
    }

    public String getLblLink() {
        if (!super.getLabelLink().isEmpty()) {
            lblLink = "Não existe desconto empregado para essa referência, favor contate seu Sindicato";
        } else {
            lblLink = "";
        }
        return lblLink;
    }

    public void setLblLink(String lblLink) {
        this.lblLink = lblLink;
    }

    public List<Juridica> getListaEmpresa() {
        return listaEmpresa;
    }

    public void setListaEmpresa(List<Juridica> listaEmpresa) {
        this.listaEmpresa = listaEmpresa;
    }

    public List<Juridica> getListaEmpresaSelecionada() {
        return listaEmpresaSelecionada;
    }

    public void setListaEmpresaSelecionada(List<Juridica> listaEmpresaSelecionada) {
        this.listaEmpresaSelecionada = listaEmpresaSelecionada;
    }

    public List<DataObject> getListaMovimento() {
        return listaMovimento;
    }

    public void setListaMovimentos(List<DataObject> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }

    public List<DataObject> getListaMovimentoSelecionado() {
        return listaMovimentoSelecionado;
    }

    public void setListaMovimentoSelecionado(List<DataObject> listaMovimentoSelecionado) {
        this.listaMovimentoSelecionado = listaMovimentoSelecionado;
    }
}
