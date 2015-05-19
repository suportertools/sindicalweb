package br.com.rtools.escola.beans;

import br.com.rtools.associativo.MatriculaSocios;
import br.com.rtools.escola.EscStatus;
import br.com.rtools.escola.MatriculaEscola;
import br.com.rtools.escola.MatriculaIndividual;
import br.com.rtools.escola.MatriculaTurma;
import br.com.rtools.escola.dao.MatriculaEscolaDao;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.LoteDB;
import br.com.rtools.financeiro.db.LoteDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
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
public class RescisaoContratoBean implements Serializable {

    private MatriculaEscola matriculaEscola;
    private Servicos servicos;
    private Pessoa titular;
    private Pessoa pessoa;
    private Pessoa beneficiario;
    private Registro registro;
    private List<Movimento> movimentos;
    private List<Movimento> movimentosRescisao;
    private List<SelectItem> listaMesVencimento;
    private String descricaoServico;
    private String mensagem;
    private String tipoRescisaoContrato;
    private String valorMulta;
    private String valorTotal;
    private String valor;
    private String dataGeracao;
    private int id;
    private int idEvt;
    private int idMesVencimento;
    private int diaVencimento;
    private int numeroParcelas;
    private boolean rescindido;

    @PostConstruct
    public void init() {
        matriculaEscola = new MatriculaEscola();
        servicos = new Servicos();
        titular = new Pessoa();
        pessoa = new Pessoa();
        beneficiario = new Pessoa();
        registro = new Registro();
        movimentos = new ArrayList<>();
        movimentosRescisao = new ArrayList<>();
        listaMesVencimento = new ArrayList<>();
        descricaoServico = "";
        mensagem = "";
        tipoRescisaoContrato = "";
        valorMulta = "";
        valorTotal = "";
        valor = "";
        dataGeracao = "";
        id = -1;
        idEvt = -1;
        idMesVencimento = 0;
        diaVencimento = 0;
        numeroParcelas = 0;
        rescindido = false;
    }

    @PreDestroy
    public void destroy() {
        /* chamado quando outra view for chamada através do UIViewRoot.setViewId(String viewId) */
    }

    public String getLoad() {
        if (getTipoRescisaoContrato().equals("matriculaEscola")) {
            getMatriculaEscola();
        }
        return "";
    }

    public void novo() {
        GenericaSessao.remove("rescisaoContratoBean");
    }

    public MatriculaEscola getMatriculaEscola() {
        if (GenericaSessao.exists("matriculaEscolaPesquisa")) {
            matriculaEscola = (MatriculaEscola) GenericaSessao.getObject("matriculaEscolaPesquisa", true);
            if (matriculaEscola.getEvt() != null) {
                this.idEvt = matriculaEscola.getEvt().getId();
                titular = matriculaEscola.getServicoPessoa().getCobranca();
                beneficiario = matriculaEscola.getServicoPessoa().getPessoa();
                pesquisaMovimentosPorEvt(idEvt);
                MatriculaEscolaDao matriculaEscolaDao = new MatriculaEscolaDao();
                MatriculaIndividual mi = matriculaEscolaDao.pesquisaCodigoMIndividual(matriculaEscola.getId());
                MatriculaTurma mt = matriculaEscolaDao.pesquisaCodigoMTurma(matriculaEscola.getId());
                if (mi.getId() != -1) {
                    servicos = mi.getCurso();
                    descricaoServico = "Mátricula nº" + matriculaEscola.getId() + " - Serviço: " + servicos.getDescricao();
                } else if (mt.getId() != -1) {
                    servicos = mt.getTurma().getCursos();
                    descricaoServico = "Mátricula nº" + matriculaEscola.getId() + " - Serviço: " + servicos.getDescricao() + " - Descrição: " + mt.getTurma().getDescricao();
                }
                PessoaDB pessoaDB = new PessoaDBToplink();
                PessoaComplemento pc = pessoaDB.pesquisaPessoaComplementoPorPessoa(matriculaEscola.getServicoPessoa().getCobranca().getId());
                if (pc.getId() != -1) {
                    diaVencimento = pc.getNrDiaVencimento();
                }
            }
        }
        return matriculaEscola;
    }

    public void setMatriculaEscola(MatriculaEscola matriculaEscola) {
        this.matriculaEscola = matriculaEscola;
    }

    public List<Movimento> getMovimentos() {
        return movimentos;
    }

    public void setMovimentos(List<Movimento> movimentos) {
        this.movimentos = movimentos;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public List<Movimento> pesquisaMovimentosPorEvt(int idEvt) {
        if (movimentos.isEmpty()) {
            dataGeracao = "";
            LoteDB dB = new LoteDBToplink();
            Lote lote = dB.pesquisaLotePorEvt(idEvt);
            int dataHoje = DataHoje.converteDataParaInteger(DataHoje.data());
            int data = 0;
            int i = 0;
            if (lote != null) {
                float soma = 0;
                MovimentoDB mvDB = new MovimentoDBToplink();
                if (matriculaEscola.getEscStatus().getId() != 3) {
                    List<Movimento> list = (List<Movimento>) mvDB.listaMovimentosDoLote(lote.getId());
                    if (!list.isEmpty()) {
                        for (Movimento list1 : list) {
                            if (list1.isAtivo()) {
                                if (list1.getBaixa() == null) {
                                    data = DataHoje.converteDataParaInteger(list1.getVencimento());
                                    if (i == 0) {
                                        dataGeracao = list1.getLote().getEmissao();
                                    }
                                    if (data >= dataHoje) {
                                        soma += list1.getValor();
                                        movimentos.add(list1);
                                    }
                                }
                            }
                            i++;
                        }
                    }
                } else {
                    LoteDB loteDB = new LoteDBToplink();
                    List<Lote> lotes = loteDB.pesquisaLotesPorEvt(lote.getEvt().getId());
                    if (!lotes.isEmpty()) {
                        List<Movimento> ms = new ArrayList<>();
                        for (Lote l : lotes) {
                            ms.addAll(mvDB.listaMovimentosDoLote(l.getId()));
                        }
                        if (!ms.isEmpty()) {
                            rescindido = true;
                            for (Movimento list1 : ms) {
                                if (list1.isAtivo()) {
                                    if (list1.getBaixa() == null) {
                                        if (i == 0) {
                                            dataGeracao = list1.getLote().getEmissao();
                                        }
                                        data = DataHoje.converteDataParaInteger(list1.getVencimento());
                                        //if (data >= dataHoje) {
                                        if (list1.getTipoServico().getId() == 6) {
                                            soma += list1.getValor();
                                            movimentos.add(list1);
                                        }
                                        //}
                                    }
                                }
                            }
                        }
                    }
                    if (soma != 0) {
                        valor = Moeda.converteR$Float(soma);
                    }
                }
            }
        }
        return movimentos;
    }

    public int getIdEvt() {
        return idEvt;
    }

    public void setIdEvt(int idEvt) {
        this.idEvt = idEvt;
    }

    public void rescindirMatriculaEscola() {
        if (idEvt == -1) {
            // GenericaMensagem.warn("Validação", "Não existem movimentos gerados!");
            mensagem = "Não existem movimentos gerados!";
            return;
        }
        if (matriculaEscola.getEscStatus().getId() == 2) {
            mensagem = "Não é possível rescindir uma matrícula concluinte!";
            return;
        }
        Lote lote = null;
        Pessoa r = null;
        Dao dao = new Dao();
        dao.openTransaction();
        if (!movimentos.isEmpty()) {
            for (int i = 0; i < movimentos.size(); i++) {
                movimentos.get(i).setAtivo(false);
                lote = movimentos.get(i).getLote();
                r = movimentos.get(i).getPessoa();
                if (!dao.update(movimentos.get(i))) {
                    dao.rollback();
                    //GenericaMensagem.warn("Erro", "Ao inativar movimentos!");
                    mensagem = "Erro ao inativar movimentos!";
                    return;
                }
            }
        }
        matriculaEscola.setEscStatus((EscStatus) dao.find(new EscStatus(), 3));
        matriculaEscola.setStatus(DataHoje.dataHoje());
        //matriculaEscola.setHabilitado(false);
        if (!dao.update(matriculaEscola)) {
            dao.rollback();
            //GenericaMensagem.warn("Erro", "Ao inativar matrícula!");
            mensagem = "Erro ao inativar matrícula!";
            return;
        }
        if (gerarMovimentoMulta(dao, lote, servicos, r)) {
            movimentos.clear();
            // GenericaMensagem.info("Sucesso", "Matrícula rescindida com sucesso");
            mensagem = "Matrícula rescindida com sucesso";
            dao.commit();
        } else {
            //GenericaMensagem.warn("Erro", "Ao inativar matrícula!");
            mensagem = "Erro ao inativar matrícula!";
            dao.rollback();
        }
    }

    public boolean gerarMovimentoMulta(Dao dao, Lote lote, Servicos s, Pessoa p) {
        if (lote == null) {
            return false;
        }
        try {
            String mesPrimeiraParcela = listaMesVencimento.get(idMesVencimento).getDescription();
            String mes = mesPrimeiraParcela.substring(0, 2);
            String ano = mesPrimeiraParcela.substring(3, 7);
            String referencia = mes + "/" + ano;
            String vencimento;
            Movimento m = null;
            if (DataHoje.qtdeDiasDoMes(Integer.parseInt(mes), Integer.parseInt(ano)) >= diaVencimento) {
                if (diaVencimento < 10) {
                    vencimento = "0" + diaVencimento + "/" + mes + "/" + ano;
                } else {
                    vencimento = diaVencimento + "/" + mes + "/" + ano;
                }
            } else {
                String diaSwap = Integer.toString(DataHoje.qtdeDiasDoMes(Integer.parseInt(mes), Integer.parseInt(ano)));
                if (diaSwap.length() < 2) {
                    diaSwap = "0" + diaSwap;
                }
                vencimento = diaSwap + "/" + mes + "/" + ano;
            }
            float valorParcelaF;
            String nrCtrBoletoResp = "";
            String vecimentoString = "";
            TipoServico tipoServico = (TipoServico) dao.find(new TipoServico(), 6);
            FTipoDocumento fTipoDocumento = (FTipoDocumento) dao.find(new FTipoDocumento(), 2);
            for (int x = 0; x < (Integer.toString(matriculaEscola.getServicoPessoa().getCobranca().getId())).length(); x++) {
                nrCtrBoletoResp += 0;
            }
            nrCtrBoletoResp += matriculaEscola.getServicoPessoa().getCobranca().getId();
            if (numeroParcelas > 0) {
                valorParcelaF = Moeda.substituiVirgulaFloat(valorMulta) / numeroParcelas;
            } else {
                valorParcelaF = Moeda.substituiVirgulaFloat(valorMulta);
            }
            int nr = numeroParcelas;
            if (numeroParcelas < 1) {
                nr = 1;
            }
            int idCondicaoPagto;
            if (numeroParcelas == 1) {
                idCondicaoPagto = 1;
            } else {
                idCondicaoPagto = 2;
            }

            Lote l = new Lote(
                    -1,
                    lote.getRotina(),
                    "R",
                    DataHoje.data(),
                    titular,
                    lote.getPlano5(),
                    false,
                    "",
                    Moeda.substituiVirgulaFloat(valorMulta),
                    lote.getFilial(),
                    null,
                    lote.getEvt(),
                    "",
                    fTipoDocumento,
                    (CondicaoPagamento) dao.find(new CondicaoPagamento(), idCondicaoPagto),
                    lote.getStatus(),
                    null,
                    lote.isDescontoFolha(),
                    0);

            if (!dao.save(l)) {
                dao.rollback();
                return false;
            }

            for (int i = 0; i < nr; i++) {
                if (i > 0) {
                    referencia = mes + "/" + ano;
                    vecimentoString = (new DataHoje()).incrementarMeses(i, vencimento);
                    mes = vecimentoString.substring(3, 5);
                    ano = vecimentoString.substring(6, 10);
                } else {
                    vecimentoString = vencimento;
                }
                String nrCtrBoleto = nrCtrBoletoResp + Long.toString(DataHoje.calculoDosDias(DataHoje.converte("07/10/1997"), DataHoje.converte(vencimento)));
                m = new Movimento(
                        -1,
                        l,
                        servicos.getPlano5(),
                        p, // EMPRESA DO RESPONSÁVEL (SE DESCONTO FOLHA) OU RESPONSÁVEL (SE NÃO FOR DESCONTO FOLHA)
                        servicos,
                        null,
                        tipoServico,
                        null,
                        valorParcelaF,
                        referencia,
                        vecimentoString,
                        1,
                        true,
                        "E",
                        false,
                        titular, // TITULAR / RESPONSÁVEL
                        beneficiario, // BENEFICIÁRIO
                        "",
                        nrCtrBoleto,
                        vencimento,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        fTipoDocumento,
                        0,
                        new MatriculaSocios());
                if (!dao.save(m)) {
                    dao.rollback();
                    return false;
                }
            }
        } catch (Exception e) {
            dao.rollback();
            return false;
        }
        return true;
    }

    public String getTipoRescisaoContrato() {
        if (GenericaSessao.exists("tipoRescisaoContrato")) {
            tipoRescisaoContrato = GenericaSessao.getString("tipoRescisaoContrato");
        }
        return tipoRescisaoContrato;
    }

    public void setTipoRescisaoContrato(String tipoRescisaoContrato) {
        this.tipoRescisaoContrato = tipoRescisaoContrato;
    }

    public void defineTipoRescisaoContrato(String tipoRescisaoContrato) {
        GenericaSessao.put("tipoRescisaoContrato", tipoRescisaoContrato);
    }

    public Pessoa getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(Pessoa beneficiario) {
        this.beneficiario = beneficiario;
    }

    public Pessoa getTitular() {
        return titular;
    }

    public void setTitular(Pessoa titular) {
        this.titular = titular;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getValorMulta() {
        return valorMulta;
    }

    public void setValorMulta(String valorMulta) {
        this.valorMulta = valorMulta;
    }

    public List<Movimento> getMovimentosRescisao() {
        return movimentosRescisao;
    }

    public void setMovimentosRescisao(List<Movimento> movimentosRescisao) {
        this.movimentosRescisao = movimentosRescisao;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMesVencimento() {
        return idMesVencimento;
    }

    public void setIdMesVencimento(int idMesVencimento) {
        this.idMesVencimento = idMesVencimento;
    }

    public List<SelectItem> getListaMesVencimento() {
        if (listaMesVencimento.isEmpty()) {
            DataHoje dh = new DataHoje();
            String data = DataHoje.data();
            String mesAno;
            int nr = numeroParcelas;
            if (nr < 2) {
                nr = 6;
            }
            for (int i = 0; i < nr; i++) {
                if (i > 0) {
                    data = dh.incrementarMeses(1, data);
                }
                mesAno = data.substring(3, 5) + "/" + data.substring(6, 10);
                listaMesVencimento.add(new SelectItem(i, mesAno, mesAno));
            }
        }
        return listaMesVencimento;
    }

    public void setListaMesVencimento(List<SelectItem> listaMesVencimento) {
        this.listaMesVencimento = listaMesVencimento;
    }

    public int getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(int diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public Registro getRegistro() {
        if (registro.getId() == -1) {
            registro = (Registro) new Dao().find(new Registro(), 1);
        }
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public String getValorTotal() {
        float valorT = 0;
        if (numeroParcelas > 0) {
            if (Moeda.substituiVirgulaFloat(valorMulta) > 0) {
                valorT = Moeda.substituiVirgulaFloat(valorMulta) / numeroParcelas;
            }
        }
        if (numeroParcelas < 2) {
            valorTotal = "O valor da multa será pago em parcela única no valor de R$" + valorMulta;
        } else {
            valorTotal = "O valor da multa será pago em " + numeroParcelas + " parcelas de R$" + Moeda.converteR$Float(valorT);
        }
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isRescindido() {
        return rescindido;
    }

    public void setRescindido(boolean rescindido) {
        this.rescindido = rescindido;
    }

    public String getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(String dataGeracao) {
        this.dataGeracao = dataGeracao;
    }
}
