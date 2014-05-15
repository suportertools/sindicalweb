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
import br.com.rtools.financeiro.beans.MovimentoValorJSFBean;
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
import br.com.rtools.pessoa.db.FilialDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class WebContabilidadeJSFBean extends MovimentoValorJSFBean {

    private Pessoa pessoa = null;
    private Juridica juridica = new Juridica();
    private Servicos servico = new Servicos();
    private TipoServico tipoServico = new TipoServico();
    private List<DataObject> listaEmpresas = new ArrayList();
    private List<DataObject> listMovimentos = new ArrayList();
    private boolean marca = false;
    private boolean filtrar = true;
    private int idServicos = 0;
    private int idTipoServico = 0;
    private int idIndex = 0;
    private int idIndexEmpresa = 0;
    private String strReferencia = "";
    private String strVencimento = "";
    private String strFiltroRef = "";
    private String vlFolha = "0";
    private boolean impVerso = false;
    private boolean renderNovo = false;
    private String msgConfirma = "";
    private List<SelectItem> listaVencimento = new ArrayList<SelectItem>();
    HtmlInputText priceRef;
    private Set<Integer> keys = null;
    private String lblLink = "";
    private Registro registro = new Registro();

    public WebContabilidadeJSFBean() {
        FilialDB filDB = new FilialDBToplink();
        registro = filDB.pesquisaRegistroPorFilial(1);
    }

    public String actionRender() {
        if (renderNovo) {
            renderNovo = false;
        } else {
            renderNovo = true;
        }
        return "webContabilidade";
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

    public List getListaEmpresasPertencentes() {
        List listEmp = null;
        WebContabilidadeDB db = new WebContabilidadeDBToplink();
        JuridicaDB dbJur = new JuridicaDBToplink();
        DataObject dtObject;
        if (filtrar == true) {
            msgConfirma = "";
            listaEmpresas = new ArrayList();
            if ((pessoa != null) && (pessoa.getId() != -1)) {
                setJuridica(dbJur.pesquisaJuridicaPorPessoa(pessoa.getId()));
                listEmp = db.listaEmpresasPertContabilidade(juridica.getId());
                for (int i = 0; i < listEmp.size(); i++) {
                    dtObject = new DataObject(new Boolean(marca),
                            ((Juridica) (listEmp.get(i))),
                            null,
                            null,
                            null,
                            null);
                    listaEmpresas.add(dtObject);
                }
            }
        }
        filtrar = false;
        return listaEmpresas;
    }

    public synchronized List getListaMovimentosContabil() {
        //listMovimentos.clear();
        try {
            if (listMovimentos.isEmpty()) {
                List lista = new ArrayList();
                WebContabilidadeDB db = new WebContabilidadeDBToplink();
                SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
                int k = 0;
                Juridica pessoaJuridica = null;
                Object obj = null;
                if (listaEmpresas.isEmpty()) {
                    return listMovimentos;
                }
                while (k < listaEmpresas.size()) {
                    if (!(Boolean) listaEmpresas.get(k).getArgumento0()) {
                        k++;
                        continue;
                    }
                    obj = ((DataObject) (listaEmpresas.get(k))).getArgumento1();
                    pessoaJuridica = (Juridica) obj;

                    if (strFiltroRef.isEmpty()) {
                        lista = db.pesquisaMovParaWebContabilidade(pessoaJuridica.getPessoa().getId());
                    } else {
                        lista = db.pesquisaMovParaWebContabilidadeComRef(pessoaJuridica.getPessoa().getId(), strFiltroRef);
                    }

                    for (int i = 0; i < lista.size(); i++) {
                        if (((Vector) lista.get(i)).get(5) == null) {
                            ((Vector) lista.get(i)).set(5, 0.0);
                        }
                        if (((Vector) lista.get(i)).get(6) == null) {
                            ((Vector) lista.get(i)).set(6, 0.0);
                        }
                        if (((Vector) lista.get(i)).get(7) == null) {
                            ((Vector) lista.get(i)).set(7, 0.0);
                        }
                        if (((Vector) lista.get(i)).get(8) == null) {
                            ((Vector) lista.get(i)).set(8, 0.0);
                        }
                        if (((Vector) lista.get(i)).get(9) == null) {
                            ((Vector) lista.get(i)).set(9, 0.0);
                        }
                        if (((Vector) lista.get(i)).get(10) == null) {
                            ((Vector) lista.get(i)).set(10, 0.0);
                        }
                        if (((Vector) lista.get(i)).get(11) == null) {
                            ((Vector) lista.get(i)).set(11, 0.0);
                        }

                        if (((Integer) ((Vector) lista.get(i)).get(13)) < 0) {
                            ((Vector) lista.get(i)).set(13, 0);
                        }

                        // data ----
                        boolean hdata, hvalor;
                        int data1 = DataHoje.converteDataParaInteger(DataHoje.converteData((Date) ((Vector) lista.get(i)).get(4)));

                        if (data1 < DataHoje.converteDataParaInteger(DataHoje.data())) {
                            hdata = true;
                        } else {
                            hdata = false;
                        }

                        // valor ----
                        if ((Integer) ((Vector) lista.get(i)).get(2) == 4) {
                            hvalor = false;
                        } else {
                            hvalor = true;
                        }


                        listMovimentos.add(new DataObject(
                                false,
                                ((Vector) lista.get(i)).get(0), // boleto
                                sv.pesquisaCodigo((Integer) ((Vector) lista.get(i)).get(1), "Servicos"), // servico
                                sv.pesquisaCodigo((Integer) ((Vector) lista.get(i)).get(2), "TipoServico"), // tipo
                                ((Vector) lista.get(i)).get(3), // referencia
                                DataHoje.converteData((Date) ((Vector) lista.get(i)).get(4)), // vencimento
                                Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(5))), // valor_mov
                                ((Vector) lista.get(i)).get(6), // valor_folha
                                Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(7))), // multa
                                Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(8))), // juros
                                Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(9))), // correcao
                                Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(10))), // desconto
                                Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(11))), // valor_calculado
                                ((Vector) lista.get(i)).get(12), // meses em atraso
                                ((Vector) lista.get(i)).get(13), // dias em atraso
                                ((Vector) lista.get(i)).get(14), // indice
                                ((Vector) lista.get(i)).get(15), // id movimento
                                Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(11))), // valor_calculado original
                                hdata, // null
                                hvalor, // null
                                "0", // null
                                pessoaJuridica.getPessoa().getNome() // null
                                ));
                    }
                    k++;
                }
            }
        } catch (Exception e) {
        }

        return listMovimentos;
    }

    public synchronized String adicionarBoleto() {
        try {
            MensagemConvencao mc = new MensagemConvencao();
            MensagemConvencaoDB menDB = new MensagemConvencaoDBToplink();
            ServicosDB dbSer = new ServicosDBToplink();
            TipoServicoDB dbTipo = new TipoServicoDBToplink();
            FTipoDocumentoDB dbFTipoDocumento = new FTipoDocumentoDBToplink();

            ContaCobrancaDB ctaCobraDB = new ContaCobrancaDBToplink();
            ContaCobranca contaCob = new ContaCobranca();
            msgConfirma = "";

            if (getListaServicos().isEmpty()) {
                msgConfirma = "Lista de Serviços está vazia!";
                return null;
            }

            if (getListaTipoServico().isEmpty()) {
                msgConfirma = "Lista de Tipo Serviço está vazia!";
                return null;
            }

            servico = dbSer.pesquisaCodigo(Integer.valueOf(getListaServicos().get(idServicos).getDescription()));
            tipoServico = dbTipo.pesquisaCodigo(Integer.valueOf(getListaTipoServico().get(idTipoServico).getDescription()));
            Juridica juri = new Juridica();

            MovimentoDB dbm = new MovimentoDBToplink();

            contaCob = ctaCobraDB.pesquisaServicoCobranca(servico.getId(), tipoServico.getId());
            if (contaCob == null) {
                msgConfirma = "  Não existe conta Cobrança para gerar!";
                return null;
            }


            if (dbm.pesquisaMovimentosAcordado(juridica.getPessoa().getId(), strReferencia, tipoServico.getId(), servico.getId()) != null) {
                msgConfirma = " Já foi gerado um Acordo para esta referência, serviço e tipo de serviço!";
                return null;
            }

            if ((new DataHoje()).integridadeReferencia(strReferencia)) {
                if (!listaEmpresas.isEmpty()) {
                    String msg = "";
                    for (int i = 0; i < listaEmpresas.size(); i++) {
                        if ((Boolean) listaEmpresas.get(i).getArgumento0()) {
                            juri = (Juridica) listaEmpresas.get(i).getArgumento1();
                            if (dbm.pesquisaMovimentos(juri.getPessoa().getId(), strReferencia, tipoServico.getId(), servico.getId()) != null) {
                                msg += " Este boleto já existe! " + juri.getPessoa().getNome();
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
                                    msgConfirma = " Não é permitido gerar boleto vencido! " + registro.getMensagemBloqueioBoletoWeb();
                                    return null;
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
                                msgConfirma = "Entrar em contato com seu Sindicato para permitir a criação desta referencia !";
                            }
                        }
                    }

                    if (!msg.isEmpty()) {
                        msgConfirma = msg;
                    }

                    if (msgConfirma.isEmpty()) {
                        msgConfirma = "Gerado com sucesso!";
                        renderNovo = false;
                    }

                    filtrar = false;
                    listMovimentos.clear();
                    //getListaMovimentosContabil();
                }
            } else {
                msgConfirma = " Referência não esta válida!";
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
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
        for (int i = 0; i < listMovimentos.size(); i++) {
            if ((Boolean) listMovimentos.get(i).getArgumento0()) {
                movimento = (Movimento) listMovimentos.get(i).getArgumento1();
                lista.add(movimento);
                listaValores.add(movimento.getValor());
                listaVencimentos.add(movimento.getVencimento());
            }
        }

        ImprimirBoleto imp = new ImprimirBoleto();
        imp.imprimirBoleto(lista, listaValores, listaVencimentos, impVerso);
        imp.visualizar(null);
        return null;
    }

    public String validaReferencia() {
        DataHoje data = new DataHoje();
        if (data.integridadeReferencia(strReferencia)) {
            String dataS = "01/" + strReferencia;
            if (!(data.faixaCincoAnosApos(dataS))) {
                strReferencia = "";
            }
        }
        return "webContabilidade";
    }

    public String imprimirComValorCalculado() {
        List<Movimento> lista = new ArrayList<Movimento>();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();

        ImpressaoWeb impressaoWeb;
        Movimento movimento;
        String data = "";
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();

        boolean vazio = true;
        String dataValida = "";
        DataHoje dh = new DataHoje();

        for (int x = 0; x < listMovimentos.size(); x++) {
            if ((Boolean) ((DataObject) listMovimentos.get(x)).getArgumento0()) {

                if (registro.getDiasBloqueiaAtrasadosWeb() <= 0) {
                    dataValida = (String) ((DataObject) listMovimentos.get(x)).getArgumento5();
                } else {
                    dataValida = dh.incrementarDias(registro.getDiasBloqueiaAtrasadosWeb(), (String) ((DataObject) listMovimentos.get(x)).getArgumento5());
                }

                if (getValidaBloqueio(dataValida)) {
                    msgConfirma = "Não é possivel imprimir boletos vencidos! " + registro.getMensagemBloqueioBoletoWeb();
                    return null;
                }
                vazio = false;

            }
        }

        if (vazio) {
            msgConfirma = "Nenhum boleto foi selecionado!";
            return null;
        }

        for (int i = 0; i < listMovimentos.size(); i++) {
            if ((Boolean) listMovimentos.get(i).getArgumento0()) {
                movimento = ((Movimento) sv.pesquisaCodigo((Integer) listMovimentos.get(i).getArgumento16(), "Movimento"));
                // COM VALOR ALTERADO ---------
                if (Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento12()) != 0) {
                    listaValores.add(Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento12()));
                } else {
                    if (Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento6()) <= 0) {
                        msgConfirma = "Valor não pode ser zerado!";
                        return null;
                    }

                    listaValores.add(Moeda.substituiVirgulaFloat((String) listMovimentos.get(i).getArgumento6()));
                }

                //COM DATA ALTERADA ---------
                if (movimento.getDtVencimento().before(DataHoje.dataHoje()) && !movimento.getVencimento().equals(DataHoje.data())) {
                    data = getListaVencimento().get(Integer.parseInt((String) listMovimentos.get(i).getArgumento20())).getDescription();
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
                    msgConfirma = "Erro ao salvar impressão web";
                    sv.desfazerTransacao();
                    return null;
                }
            }
        }
        sv.comitarTransacao();

        ImprimirBoleto imp = new ImprimirBoleto();
        lista = imp.atualizaContaCobrancaMovimento(lista);

        imp.imprimirBoleto(lista, listaValores, listaVencimentos, impVerso);
        imp.visualizar(null);
        listMovimentos.clear();

        return "webContabilidade";
    }

    public String filtrarBoleto() {
        filtrar = false;
        listMovimentos.clear();
        return null;
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
        FilialDB filDB = new FilialDBToplink();
        DataHoje data = new DataHoje();
        ServicosDB dbSer = new ServicosDBToplink();
        Servicos servicos = dbSer.pesquisaCodigo(Integer.valueOf(getListaServicos().get(idServicos).getDescription()));
        int i = 0;
        TipoServicoDB db = new TipoServicoDBToplink();
        if ((!data.integridadeReferencia(strReferencia))
                || (registro == null)
                || (servicos == null)) {
            return listaTipoServico;
        }
        List select = null;
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
        select = db.pesquisaTodosComIds(listaIds);
        if (select.size() != 0) {
            while (i < select.size()) {
                listaTipoServico.add(new SelectItem(
                        new Integer(i),
                        (String) ((TipoServico) select.get(i)).getDescricao(),
                        Integer.toString(((TipoServico) select.get(i)).getId())));
                i++;
            }
        }
        return listaTipoServico;
    }

    public String desmarcaTodas() {
        marca = false;
        filtrar = true;
        getListaEmpresasPertencentes();
        return "webContabilidade";
    }

    public String marcaTodas() {
        marca = true;
        filtrar = true;
        getListaEmpresasPertencentes();
        return "webContabilidade";
    }

    public String carregaUsuarioPG() {
        /*usuario = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb");
         FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
         filtrar = true;*/
        return null;
    }

    public void refreshForm() {
    }

    public Pessoa getPessoa() {
        pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb");
        //filtrar = true;
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

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
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

    public synchronized HtmlInputText getPriceRef() {
        return priceRef;
    }

    public synchronized void setPriceRef(HtmlInputText priceRef) {
        this.priceRef = priceRef;
    }

    public void carregarFolha(DataObject data) {
        
    }
    
    public void carregarFolha() {
        if (!listMovimentos.isEmpty()) {
            MovimentoDB db = new MovimentoDBToplink();
            Movimento movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listMovimentos.get(idIndex).getArgumento16())));
            super.carregarFolha(movimento);
        }
    }

    public void atualizaValorGrid() {
        // antes ---
        //listMovimentos.get(idIndex).setArgumento6(super.atualizaValor(true));

        listMovimentos.get(idIndex).setArgumento6(super.atualizaValor(false));
        listMovimentos.clear();
    }

    public String getStrFiltroRef() {
        return strFiltroRef;
    }

    public void setStrFiltroRef(String strFiltroRef) {
        this.strFiltroRef = strFiltroRef;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getIdIndexEmpresa() {
        return idIndexEmpresa;
    }

    public void setIdIndexEmpresa(int idIndexEmpresa) {
        this.idIndexEmpresa = idIndexEmpresa;
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

    public boolean isRenderNovo() {
        return renderNovo;
    }

    public void setRenderNovo(boolean renderNovo) {
        this.renderNovo = renderNovo;
    }
}
