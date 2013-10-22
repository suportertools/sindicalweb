package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Acordo;
import br.com.rtools.arrecadacao.db.AcordoDB;
import br.com.rtools.arrecadacao.db.AcordoDBToplink;
import br.com.rtools.financeiro.*;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.ControleAcessoBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class ExtratoTelaJSFBean {

    private int idContribuicao = 0;
    private int idTipoServico = 0;
    private int idMovimento = 0;
    private int idIndex = -1;
    private Pessoa pessoa = new Pessoa();
    private Movimento mov = new Movimento();
    private List<DataObject> lista = new ArrayList();
    private List listMov = new Vector();
    private boolean chkData = false;
    private boolean chkContribuicao = false;
    private boolean chkNrBoletos = false;
    private boolean chkEmpresa = false;
    private boolean chkTipo = false;
    private boolean recarregaPag = false;
    private boolean chkExcluirBol = false;
    private String tipoPesquisa = "data";
    private String porPesquisa = "";
    private String geraPesquisa = "naoVerificar";
    private String tipoDataPesquisa = "vencimento";
    private String ordenacao = "referencia";
    private String dataInicial = "";
    private String dataFinal = "";
    private String dataRefInicial = "";
    private String dataRefFinal = "";
    private String boletoInicial = "";
    private String boletoFinal = "";
    private String vlTotal = "0";
    private String vlRecebido = "0";
    private String vlTaxa = "0";
    private String vlLiquido = "0";
    private String vlRepasse = "0";
    private String msgConfirma = "";
    private String linkVoltar;
    private String valorSomado;
    private String dataAntiga = "";
    private String dataNova = "";
    public boolean imprimirVerso = false;
    private String historico = "";
    private String tipoEnvio = "empresa";
    private String valorExtenso = "";

    public ExtratoTelaJSFBean() {
        ControleAcessoBean controx = new ControleAcessoBean();
        if (!controx.getListaExtratoTela()) {
            porPesquisa = "naoRecebidas";
        } else {
            porPesquisa = "todos";
        }
    }


    public List<SelectItem> getListaServico() {
        List<SelectItem> servicos = new Vector<SelectItem>();
        if (chkContribuicao) {
            int i = 0;
            ServicosDB db = new ServicosDBToplink();
            List select = db.pesquisaTodos();
            while (i < select.size()) {
                servicos.add(new SelectItem(new Integer(i),
                        (String) ((Servicos) select.get(i)).getDescricao(),
                        Integer.toString(((Servicos) select.get(i)).getId())));
                i++;
            }
        }
        return servicos;
    }

    public List<SelectItem> getListaTipoServico() {
        List<SelectItem> tipoServico = new Vector<SelectItem>();
        if (chkTipo) {
            int i = 0;
            TipoServicoDB db = new TipoServicoDBToplink();
            List select = db.pesquisaTodos();
            while (i < select.size()) {
                tipoServico.add(new SelectItem(new Integer(i),
                        (String) ((TipoServico) select.get(i)).getDescricao(),
                        Integer.toString(((TipoServico) select.get(i)).getId())));
                i++;
            }
        }
        return tipoServico;
    }

    public String novo() {
        pessoa = new Pessoa();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        return "extratoTela";
    }

    public String novoDelete() {
        return "extratoTela";
    }

    public List getListaMovimentos() {
        boolean habData = false;
        float somaValores = 0;
        float somaRepasse = 0;
        String classTbl = "";

        if (recarregaPag) {
            MovimentoDB db = new MovimentoDBToplink();
            DataObject dtObject;
            Date dtInicial;
            Date dtFinal;
            int ic;
            int its;
            int ii = 0;
            lista.clear();
            listMov.clear();

            if (chkData && !tipoDataPesquisa.equals("referencia")) {
                if (dataInicial.isEmpty() || dataFinal.isEmpty()) {
                    chkData = false;
                }
            } else {
                if (dataRefInicial.isEmpty() || dataRefFinal.isEmpty()) {
                    chkData = false;
                }
            }
            if (pessoa == null) {
                pessoa = new Pessoa();
            }

            if (!getListaServico().isEmpty()) {
                ic = Integer.parseInt(getListaServico().get(idContribuicao).getDescription());
            } else {
                ic = 0;
            }

            if (!getListaTipoServico().isEmpty()) {
                its = Integer.parseInt(getListaTipoServico().get(idTipoServico).getDescription());
            } else {
                its = 0;
            }

            if (!boletoInicial.equals("") && boletoFinal.equals("")) {
                boletoFinal = boletoInicial;
            }

            if (boletoInicial.equals("") && !boletoFinal.equals("")) {
                boletoInicial = boletoFinal;
            }

            if (porPesquisa.equals("todos")) {
                if (!dataInicial.equals("") && (!dataFinal.equals(""))) {
                    dtInicial = DataHoje.converte(dataInicial);
                    dtFinal = DataHoje.converte(dataFinal);
                } else if (!dataInicial.equals("")) {
                    dtInicial = DataHoje.converte(dataInicial);
                    dtFinal = dtInicial;
                } else {
                    dtInicial = DataHoje.dataHoje();
                    dtFinal = DataHoje.dataHoje();
                }
                listMov = db.listaTodosMovimentos(chkData, chkContribuicao, chkNrBoletos, chkEmpresa, chkTipo, tipoDataPesquisa,
                        dtInicial, dtFinal, dataRefInicial, dataRefFinal, ic, its, boletoInicial,
                        boletoFinal, getPessoa().getId(), ordenacao);

                while (ii < listMov.size()) {
                    if ((((Vector) listMov.get(ii)).get(21)) == null) {
                        ((Vector) listMov.get(ii)).set(21, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(9)) == null) {
                        ((Vector) listMov.get(ii)).set(9, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(13)) == null) {
                        ((Vector) listMov.get(ii)).set(13, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(14)) == null) {
                        ((Vector) listMov.get(ii)).set(14, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(15)) == null) {
                        ((Vector) listMov.get(ii)).set(15, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(16)) == null) {
                        ((Vector) listMov.get(ii)).set(16, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(17)) == null) {
                        ((Vector) listMov.get(ii)).set(17, 0.0);
                    }

                    somaValores = Moeda.subtracaoValores(Moeda.somaValores(
                            Moeda.somaValores(
                            Moeda.somaValores(
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(21))),//valor
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(14)))//juros
                            ),
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(15)))//correcao
                            ), Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(13))) //multa
                            ), Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(16))));// desconto
                    somaRepasse = Moeda.multiplicarValores(somaValores,
                            Moeda.divisaoValores(
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(17))), 100));

                    if (((Vector) listMov.get(ii)).get(12) == null
                            && ((String) ((Vector) listMov.get(ii)).get(11)).equals("Acordo")) {
                        habData = true;
                    } else {
                        habData = false;
                    }

                    if (((Vector) listMov.get(ii)).get(12) == null) {
                        classTbl = "tblExtratoTela";
                    } else {
                        classTbl = "";
                    }

                    dtObject = new DataObject(false,
                            ((Integer) ((Vector) listMov.get(ii)).get(0)), //ARG 1 id
                            ((Vector) listMov.get(ii)).get(13), // ARG 2 multa
                            somaValores, // ARG 3 somaValores
                            ((Vector) listMov.get(ii)).get(1), // ARG 4 documento
                            ((Vector) listMov.get(ii)).get(2), // ARG 5 nome
                            ((Vector) listMov.get(ii)).get(3), // ARG 6 boleto
                            ((Vector) listMov.get(ii)).get(4), // ARG 7 contribuicao
                            ((Vector) listMov.get(ii)).get(5), // ARG 8 referencia
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(6)), // ARG 9 vencimento
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(7)), // ARG 10 importacao
                            ((Vector) listMov.get(ii)).get(8), // ARG 11 valor
                            ((Vector) listMov.get(ii)).get(9), // ARG 12 taxa
                            ((Vector) listMov.get(ii)).get(10), // ARG 13 nomeUsuario
                            ((Vector) listMov.get(ii)).get(11), // ARG 14 tipo
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(12)),// ARG 15 quitacao
                            ((Vector) listMov.get(ii)).get(14), // ARG 16 juros
                            ((Vector) listMov.get(ii)).get(15), // ARG 17 correcao
                            ((Vector) listMov.get(ii)).get(16),// ARG 18 desconto
                            ((Vector) listMov.get(ii)).get(17), // ARG 19 repasse
                            somaRepasse,// ARG 20 somaRepasse
                            new Boolean(habData), // ARG 21 boolean habilita data
                            ((Vector) listMov.get(ii)).get(18), // ARG 22 lote baixa
                            ((Vector) listMov.get(ii)).get(19), // ARG 23 beneficiario
                            ((Vector) listMov.get(ii)).get(20), // ARG 24 filial
                            ((Vector) listMov.get(ii)).get(21), // ARG 25 valor_baixa
                            classTbl, // ARG 26 null
                            null, // ARG 27 null
                            null // ARG 28 null
                            );
                    lista.add(dtObject);
                    ii++;
                }
            }

            if (porPesquisa.equals("recebidas")) {
                if (!dataInicial.equals("") && (!dataFinal.equals(""))) {
                    dtInicial = DataHoje.converte(dataInicial);
                    dtFinal = DataHoje.converte(dataFinal);
                } else if (!dataInicial.equals("")) {
                    dtInicial = DataHoje.converte(dataInicial);
                    dtFinal = dtInicial;
                } else {
                    dtInicial = DataHoje.dataHoje();
                    dtFinal = DataHoje.dataHoje();
                }
                listMov = db.listaRecebidasMovimentos(chkData, chkContribuicao, chkNrBoletos, chkEmpresa, chkTipo, tipoDataPesquisa,
                        dtInicial, dtFinal, dataRefInicial, dataRefFinal, ic, its, boletoInicial,
                        boletoFinal, getPessoa().getId(), ordenacao);

                while (ii < listMov.size()) {
                    if ((((Vector) listMov.get(ii)).get(21)) == null) {
                        ((Vector) listMov.get(ii)).set(21, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(9)) == null) {
                        ((Vector) listMov.get(ii)).set(9, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(13)) == null) {
                        ((Vector) listMov.get(ii)).set(13, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(14)) == null) {
                        ((Vector) listMov.get(ii)).set(14, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(15)) == null) {
                        ((Vector) listMov.get(ii)).set(15, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(16)) == null) {
                        ((Vector) listMov.get(ii)).set(16, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(17)) == null) {
                        ((Vector) listMov.get(ii)).set(17, 0.0);
                    }

                    somaValores = Moeda.subtracaoValores(Moeda.somaValores(
                            Moeda.somaValores(
                            Moeda.somaValores(
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(21))),//valor
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(14)))//juros
                            ),
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(15)))//correcao
                            ), Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(13))) //multa
                            ), Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(16))));// desconto
                    somaRepasse = Moeda.multiplicarValores(somaValores,
                            Moeda.divisaoValores(
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(17))), 100));

                    if (((Vector) listMov.get(ii)).get(12) == null
                            && ((String) ((Vector) listMov.get(ii)).get(11)).equals("Acordo")) {
                        habData = true;
                    } else {
                        habData = false;
                    }

                    if (((Vector) listMov.get(ii)).get(12) == null) {
                        classTbl = "tblExtratoTela";
                    } else {
                        classTbl = "";
                    }

                    dtObject = new DataObject(new Boolean(false),
                            ((Integer) ((Vector) listMov.get(ii)).get(0)), //ARG 1 id
                            ((Vector) listMov.get(ii)).get(13), // ARG 2 multa
                            somaValores, // ARG 3 somaValores
                            ((Vector) listMov.get(ii)).get(1), // ARG 4 documento
                            ((Vector) listMov.get(ii)).get(2), // ARG 5 nome
                            ((Vector) listMov.get(ii)).get(3), // ARG 6 boleto
                            ((Vector) listMov.get(ii)).get(4), // ARG 7 contribuicao
                            ((Vector) listMov.get(ii)).get(5), // ARG 8 referencia
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(6)), // ARG 9 vencimento
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(7)), // ARG 10 importacao
                            ((Vector) listMov.get(ii)).get(8), // ARG 11 valor
                            ((Vector) listMov.get(ii)).get(9), // ARG 12 taxa
                            ((Vector) listMov.get(ii)).get(10), // ARG 13 nomeUsuario
                            ((Vector) listMov.get(ii)).get(11), // ARG 14 tipo
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(12)),// ARG 15 quitacao
                            ((Vector) listMov.get(ii)).get(14), // ARG 16 juros
                            ((Vector) listMov.get(ii)).get(15), // ARG 17 correcao
                            ((Vector) listMov.get(ii)).get(16),// ARG 18 desconto
                            ((Vector) listMov.get(ii)).get(17), // ARG 19 repasse
                            somaRepasse,// ARG 20 somaRepasse
                            new Boolean(habData), // ARG 21 boolean habilita data
                            ((Vector) listMov.get(ii)).get(18), // ARG 22 lote baixa
                            ((Vector) listMov.get(ii)).get(19), // ARG 23 beneficiario
                            ((Vector) listMov.get(ii)).get(20), // ARG 24 filial
                            ((Vector) listMov.get(ii)).get(21), // ARG 25 valor_baixa
                            classTbl, // ARG 26 null
                            null, // ARG 27 null
                            null // ARG 28 null
                            );
                    lista.add(dtObject);
                    ii++;
                }
            }

            if (porPesquisa.equals("naoRecebidas")) {
                if (!dataInicial.equals("") && (!dataFinal.equals(""))) {
                    dtInicial = DataHoje.converte(dataInicial);
                    dtFinal = DataHoje.converte(dataFinal);
                } else if (!dataInicial.equals("")) {
                    dtInicial = DataHoje.converte(dataInicial);
                    dtFinal = dtInicial;
                } else {
                    dtInicial = DataHoje.dataHoje();
                    dtFinal = DataHoje.dataHoje();
                }
                listMov = db.listaNaoRecebidasMovimentos(chkData, chkContribuicao, chkNrBoletos, chkEmpresa, chkTipo, tipoDataPesquisa,
                        dtInicial, dtFinal, dataRefInicial, dataRefFinal, ic, its, boletoInicial,
                        boletoFinal, getPessoa().getId(), ordenacao);

                while (ii < listMov.size()) {
                    if ((((Vector) listMov.get(ii)).get(21)) == null) {
                        ((Vector) listMov.get(ii)).set(21, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(9)) == null) {
                        ((Vector) listMov.get(ii)).set(9, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(13)) == null) {
                        ((Vector) listMov.get(ii)).set(13, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(14)) == null) {
                        ((Vector) listMov.get(ii)).set(14, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(15)) == null) {
                        ((Vector) listMov.get(ii)).set(15, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(16)) == null) {
                        ((Vector) listMov.get(ii)).set(16, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(17)) == null) {
                        ((Vector) listMov.get(ii)).set(17, 0.0);
                    }

                    somaValores = Moeda.subtracaoValores(Moeda.somaValores(
                            Moeda.somaValores(
                            Moeda.somaValores(
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(21))),//valor
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(14)))//juros
                            ),
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(15)))//correcao
                            ), Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(13))) //multa
                            ), Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(16))));// desconto
                    somaRepasse = Moeda.multiplicarValores(somaValores,
                            Moeda.divisaoValores(
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(17))), 100));
                    if (((Vector) listMov.get(ii)).get(12) == null
                            && ((String) ((Vector) listMov.get(ii)).get(11)).equals("Acordo")) {
                        habData = true;
                    } else {
                        habData = false;
                    }

                    if (((Vector) listMov.get(ii)).get(12) == null) {
                        classTbl = "tblExtratoTela";
                    } else {
                        classTbl = "";
                    }

                    dtObject = new DataObject(new Boolean(false),
                            ((Integer) ((Vector) listMov.get(ii)).get(0)), //ARG 1 id
                            ((Vector) listMov.get(ii)).get(13), // ARG 2 multa
                            somaValores, // ARG 3 somaValores
                            ((Vector) listMov.get(ii)).get(1), // ARG 4 documento
                            ((Vector) listMov.get(ii)).get(2), // ARG 5 nome
                            ((Vector) listMov.get(ii)).get(3), // ARG 6 boleto
                            ((Vector) listMov.get(ii)).get(4), // ARG 7 contribuicao
                            ((Vector) listMov.get(ii)).get(5), // ARG 8 referencia
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(6)), // ARG 9 vencimento
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(7)), // ARG 10 importacao
                            ((Vector) listMov.get(ii)).get(8), // ARG 11 valor
                            ((Vector) listMov.get(ii)).get(9), // ARG 12 taxa
                            ((Vector) listMov.get(ii)).get(10), // ARG 13 nomeUsuario
                            ((Vector) listMov.get(ii)).get(11), // ARG 14 tipo
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(12)),// ARG 15 quitacao
                            ((Vector) listMov.get(ii)).get(14), // ARG 16 juros
                            ((Vector) listMov.get(ii)).get(15), // ARG 17 correcao
                            ((Vector) listMov.get(ii)).get(16),// ARG 18 desconto
                            ((Vector) listMov.get(ii)).get(17), // ARG 19 repasse
                            somaRepasse,// ARG 20 somaRepasse
                            new Boolean(habData), // ARG 21 boolean habilita data
                            ((Vector) listMov.get(ii)).get(18), // ARG 22 lote baixa
                            ((Vector) listMov.get(ii)).get(19), // ARG 23 beneficiario
                            ((Vector) listMov.get(ii)).get(20), // ARG 24 filial
                            ((Vector) listMov.get(ii)).get(21), // ARG 25 valor_baixa
                            classTbl, // ARG 26 null
                            null, // ARG 27 null
                            null // ARG 28 null
                            );
                    lista.add(dtObject);
                    ii++;
                }

            }

            if (porPesquisa.equals("atrazadas")) {
                if (!dataInicial.equals("") && (!dataFinal.equals(""))) {
                    dtInicial = DataHoje.converte(dataInicial);
                    dtFinal = DataHoje.converte(dataFinal);
                } else if (!dataInicial.equals("")) {
                    dtInicial = DataHoje.converte(dataInicial);
                    dtFinal = dtInicial;
                } else {
                    dtInicial = DataHoje.dataHoje();
                    dtFinal = DataHoje.dataHoje();
                }
                listMov = db.listaAtrazadasMovimentos(chkData, chkContribuicao, chkNrBoletos, chkEmpresa, chkTipo, tipoDataPesquisa,
                        dtInicial, dtFinal, dataRefInicial, dataRefFinal, ic, its, boletoInicial,
                        boletoFinal, getPessoa().getId(), ordenacao);

                while (ii < listMov.size()) {
                    if ((((Vector) listMov.get(ii)).get(21)) == null) {
                        ((Vector) listMov.get(ii)).set(21, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(9)) == null) {
                        ((Vector) listMov.get(ii)).set(9, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(13)) == null) {
                        ((Vector) listMov.get(ii)).set(13, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(14)) == null) {
                        ((Vector) listMov.get(ii)).set(14, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(15)) == null) {
                        ((Vector) listMov.get(ii)).set(15, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(16)) == null) {
                        ((Vector) listMov.get(ii)).set(16, 0.0);
                    }
                    if ((((Vector) listMov.get(ii)).get(17)) == null) {
                        ((Vector) listMov.get(ii)).set(17, 0.0);
                    }

                    somaValores = Moeda.subtracaoValores(Moeda.somaValores(
                            Moeda.somaValores(
                            Moeda.somaValores(
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(21))),//valor
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(14)))//juros
                            ),
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(15)))//correcao
                            ), Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(13))) //multa
                            ), Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(16))));// desconto
                    somaRepasse = Moeda.multiplicarValores(somaValores,
                            Moeda.divisaoValores(
                            Float.parseFloat(Double.toString((Double) ((Vector) listMov.get(ii)).get(17))), 100));
                    if (((Vector) listMov.get(ii)).get(12) == null
                            && ((String) ((Vector) listMov.get(ii)).get(11)).equals("Acordo")) {
                        habData = true;
                    } else {
                        habData = false;
                    }

                    if (((Vector) listMov.get(ii)).get(12) == null) {
                        classTbl = "tblExtratoTela";
                    } else {
                        classTbl = "";
                    }

                    dtObject = new DataObject(new Boolean(false),
                            ((Integer) ((Vector) listMov.get(ii)).get(0)), //ARG 1 id
                            ((Vector) listMov.get(ii)).get(13), // ARG 2 multa
                            somaValores, // ARG 3 somaValores
                            ((Vector) listMov.get(ii)).get(1), // ARG 4 documento
                            ((Vector) listMov.get(ii)).get(2), // ARG 5 nome
                            ((Vector) listMov.get(ii)).get(3), // ARG 6 boleto
                            ((Vector) listMov.get(ii)).get(4), // ARG 7 contribuicao
                            ((Vector) listMov.get(ii)).get(5), // ARG 8 referencia
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(6)), // ARG 9 vencimento
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(7)), // ARG 10 importacao
                            ((Vector) listMov.get(ii)).get(8), // ARG 11 valor
                            ((Vector) listMov.get(ii)).get(9), // ARG 12 taxa
                            ((Vector) listMov.get(ii)).get(10), // ARG 13 nomeUsuario
                            ((Vector) listMov.get(ii)).get(11), // ARG 14 tipo
                            DataHoje.converteData((Date) ((Vector) listMov.get(ii)).get(12)),// ARG 15 quitacao
                            ((Vector) listMov.get(ii)).get(14), // ARG 16 juros
                            ((Vector) listMov.get(ii)).get(15), // ARG 17 correcao
                            ((Vector) listMov.get(ii)).get(16),// ARG 18 desconto
                            ((Vector) listMov.get(ii)).get(17), // ARG 19 repasse
                            somaRepasse,// ARG 20 somaRepasse
                            new Boolean(habData), // ARG 21 boolean habilita data
                            ((Vector) listMov.get(ii)).get(18), // ARG 22 lote baixa
                            ((Vector) listMov.get(ii)).get(19), // ARG 23 beneficiario
                            ((Vector) listMov.get(ii)).get(20), // ARG 24 filial
                            ((Vector) listMov.get(ii)).get(21), // ARG 25 valor_baixa
                            classTbl, // ARG 26 null
                            null, // ARG 27 null
                            null // ARG 28 null
                            );
                    lista.add(dtObject);
                    ii++;
                }

            }
            dataRefInicial = "";
            dataRefFinal = "";
        }
        recarregaPag = false;
        return lista;
    }

    public String getQntBoletos() {
        String n;
        if (!listMov.isEmpty()) {
            n = Integer.toString(listMov.size());
        } else {
            n = "0";
        }
        return n;
    }

    public String baixaBoletos() {

        return null;
    }

    public void filtrar() {
        recarregaPag = true;
    }

    public String excluirBoleto() {
        MovimentoDB db = new MovimentoDBToplink();
        if (listMov.isEmpty()) {
            return null;
        }
        if (bltQuitados() == true) {
            msgConfirma = "Boletos quitados não podem ser Excluídos!";
            return null;
        }
        if (bltSelecionados() != true) {
            msgConfirma = "Nenhum Boleto Selecionado!";
            return null;
        }
        if (bltAcordo() == true) {
            msgConfirma = "Boletos do tipo acordo não podem ser Excluídos";
            return null;
        }
        boolean exc = true;
        for (int i = 0; i < listMov.size(); i++) {
            if (((Boolean) lista.get(i).getArgumento0())) {
                if (!GerarMovimento.excluirUmMovimento(db.pesquisaCodigo((Integer) lista.get(i).getArgumento1()))) {
                    exc = false;
                }
            }
        }
        if (!exc) {
            msgConfirma = "Ocorreu um erro em uma das exclusões, verifique o log!";
        } else {
            msgConfirma = "Boleto excluído com sucesso!";
        }
        recarregaPag = true;
        getListaMovimentos();
        return null;
    }

    public String inativarBoleto() {
        MovimentoDB db = new MovimentoDBToplink();
        if (listMov.isEmpty()) {
            msgConfirma = "Lista Vazia!";
            return null;
        }
        if (bltQuitados() == true) {
            msgConfirma = "Boletos quitados não podem ser Excluídos!";
            return null;
        }
        if (bltSelecionados() != true) {
            msgConfirma = "Nenhum Boleto Selecionado!";
            return null;
        }
        if (bltAcordo() == true) {
            msgConfirma = "Boletos do tipo acordo não podem ser Excluídos";
            return null;
        }

        if (historico.isEmpty()) {
            msgConfirma = "Digite um motivo para exclusão!";
            return null;
        } else if (historico.length() < 6) {
            msgConfirma = "Motivo de exclusão inválido!";
            return null;
        }
        boolean exc = true;
        for (int i = 0; i < listMov.size(); i++) {
            if (((Boolean) lista.get(i).getArgumento0())) {
                if (!GerarMovimento.inativarUmMovimento(db.pesquisaCodigo((Integer) lista.get(i).getArgumento1()), historico).isEmpty()) {
                    exc = false;
                }
            }
        }
        if (!exc) {
            msgConfirma = "Ocorreu um erro em uma das exclusões, verifique o log!";
        } else {
            msgConfirma = "Boleto excluído com sucesso!";
        }
        recarregaPag = true;
        getListaMovimentos();
        return null;
    }

    public String getSomarVlRecebido() {
        String soma = "";
        float somaFloat = 0;
        int i = 0;
        String result = "R$ ";
        String r = "";
        if (!listMov.isEmpty()) {
            while (i < listMov.size()) {
                if (lista.get(i).getArgumento15() != null) {
                    soma = String.valueOf((Double) lista.get(i).getArgumento25());
                    somaFloat = Moeda.somaValores(somaFloat, Float.valueOf(soma));
                }
                i++;
            }
            vlRecebido = String.valueOf(somaFloat);
            r = Moeda.converteR$(vlRecebido);
            return result + r;
        } else {
            return result;
        }
    }

    public String getSomarVlNaoRecebido() {
        String soma = "";
        float somaFloat = 0;
        int i = 0;
        String result = "R$ ";
        String r = "";
        if (!listMov.isEmpty()) {
            while (i < listMov.size()) {
                soma = String.valueOf((Double) lista.get(i).getArgumento11());
                somaFloat = Moeda.somaValores(somaFloat, Float.valueOf(soma));
                i++;
            }
            //vlRecebido = String.valueOf(somaFloat);
            r = Moeda.converteR$(String.valueOf(somaFloat));
            return result + r;
        } else {
            return result;
        }
    }

    public String getSomarVlTotal() {
        String soma = "";
        float somaFloat = 0;
        int i = 0;
        String result = "R$ ";
        String r = "";
        if (!listMov.isEmpty()) {
            while (i < listMov.size()) {
                soma = String.valueOf((Double) lista.get(i).getArgumento25());
                somaFloat = Moeda.somaValores(somaFloat, Float.valueOf(soma));
                i++;
            }
            vlTotal = String.valueOf(somaFloat);
            r = Moeda.converteR$(vlTotal);
            return result + r;
        } else {
            return result;
        }
    }

    public String getSomarTaxa() {
        String soma = "";
        float somaFloat = 0;
        int i = 0;
        String result = "R$ ";
        String r = "";
        if (!listMov.isEmpty()) {
            while (i < listMov.size()) {
                if (lista.get(i).getArgumento15() != null) {
                    soma = Double.toString(((Double) lista.get(i).getArgumento12()));
                    somaFloat = Moeda.somaValores(somaFloat, Float.valueOf(soma));
                }
                i++;
            }
            vlTaxa = String.valueOf(somaFloat);
            r = Moeda.converteR$(vlTaxa);
            return result + r;
        } else {
            return result;
        }
    }

    public String getSomarVlLiquido() {
        float somaFloat = 0;
        int i = 0;
        String result = "R$ ";
        String r = "";
        if (!listMov.isEmpty()) {
            somaFloat = Moeda.subtracaoValores(Float.valueOf(vlRecebido), Float.valueOf(vlTaxa));
            vlLiquido = String.valueOf(somaFloat);
            r = Moeda.converteR$(vlLiquido);
            return result + r;
        } else {
            return result;
        }
    }

    public String getSomarVlLiquidoRepasse() {
        String soma = "";
        float somaFloat = 0;
        int i = 0;
        String result = "R$ ";
        String r = "";
        if (!listMov.isEmpty()) {
            while (i < listMov.size()) {
                soma = Float.toString(((Float) lista.get(i).getArgumento20()));
                somaFloat = Moeda.somaValores(somaFloat, Float.valueOf(soma));
                i++;
            }
            vlRepasse = Float.toString(Moeda.subtracaoValores(Float.valueOf(vlLiquido), somaFloat));
            r = Moeda.converteR$(vlRepasse);
            return result + r;
        } else {
            return result;
        }
    }

    public String getSomarBoletoSelecionados() {
        String soma = "";
        float somaFloat = 0;
        int i = 0;
        String result = "R$ ";
        String r = "";
        if (!listMov.isEmpty()) {
            while (i < listMov.size()) {
                if (bltSelecionados() == true) {
                    if ((Boolean) lista.get(i).getArgumento0()) {
                        soma = Float.toString(((Float) lista.get(i).getArgumento3()));
                        somaFloat = Moeda.somaValores(somaFloat, Float.valueOf(soma));
                    }
                } else {
                    break;
                }
                i++;
            }
            r = Moeda.converteR$(String.valueOf(somaFloat));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("valorTotalExtrato", result + r);
            return result + r;
        } else {
            return result;
        }
    }

    public boolean bltQuitados() {
        boolean result = false;
        if (!listMov.isEmpty()) {
            int i = 0;
            while (i < listMov.size()) {
                if ((Boolean) lista.get(i).getArgumento0()) {
                    if (lista.get(i).getArgumento15() != null) {
                        if (!lista.get(i).getArgumento15().equals("")) {
                            result = true;
                            break;
                        }
                    } else {
                        result = false;
                    }
                }
                i++;
            }
        }
        return result;
    }

    public boolean bltSelecionados() {
        boolean result = false;
        if (!listMov.isEmpty()) {
            int i = 0;
            while (i < listMov.size()) {
                if ((Boolean) lista.get(i).getArgumento0()) {
                    result = true;
                    break;
                } else {
                    result = false;
                }
                i++;
            }
        }
        return result;
    }

    public boolean bltAcordo() {
        boolean result = false;
        if (!listMov.isEmpty()) {
            int i = 0;
            while (i < listMov.size()) {
                if ((Boolean) lista.get(i).getArgumento0()) {
                    if (lista.get(i).getArgumento14().equals("Acordo")) {
                        result = true;
                        break;
                    } else {
                        result = false;
                    }
                }
                i++;
            }
        }
        return result;
    }

    public String linkVoltarBaixaMovimento() {
        linkVoltar = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        if (linkVoltar == null) {
            return "menuPrincipal";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("urlRetorno");
        }
        return linkVoltar;
    }

    public String getValorTotal() {
        getSomarBoletoSelecionados();
        String v = "";
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valorTotalExtrato") != null) {
            v = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("valorTotalExtrato");
        } else {
            v = "R$ ";
        }
        return v;
    }

    public String getOrdenacao() {
        return ordenacao;
    }

    public void setOrdenacao(String ordenacao) {
        this.ordenacao = ordenacao;
    }

    public String estornarBaixa() {
        MovimentoDB db = new MovimentoDBToplink();
        if (listMov.isEmpty()) {
            return null;
        }
        boolean est = true;
        for (int i = 0; i < listMov.size(); i++) {
            if (((Boolean) lista.get(i).getArgumento0())) {
                if (!GerarMovimento.estornarMovimento(db.pesquisaCodigo((Integer) lista.get(i).getArgumento1()))) {
                    est = false;
                }
            }
        }

        if (!est) {
            msgConfirma = "Ocorreu erros ao estornar boletos, verifique o log!";
        } else {
            msgConfirma = "Boletos estornados com sucesso!";
        }

        recarregaPag = true;
        getListaMovimentos();
        return null;
    }

//   public String excluir(){
//        MovimentoDB db = new MovimentoDBToplink();
//        Movimento movimento = db.pesquisaCodigo(2156);
//        if (movimento.getId()!=-1){
//            db.getEntityManager().getTransaction().begin();
//            movimento = db.pesquisaCodigo(movimento.getId());
//            db.delete(movimento);
//            db.getEntityManager().getTransaction().rollback();
//
//            //    db.getEntityManager().getTransaction().commit();
//
//
//        }
//       return null;
//   }
    public String imprimirPromissoria() {
        List<Movimento> listaC = new ArrayList<Movimento>();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
        Acordo acordo = new Acordo();
        AcordoDB dbAc = new AcordoDBToplink();
        Historico historico = new Historico();
        if (bltSelecionados() != true) {
            msgConfirma = "Nenhum Boleto Selecionado!";
            return null;
        }

        int qnt = 0;
        for (int i = 0; i < listMov.size(); i++) {
            if (((Boolean) lista.get(i).getArgumento0())) {
                qnt++;
                if (qnt > 1) {
                    msgConfirma = "Somente um acordo pode ser selecionado!";
                    return null;
                }
                movimento = db.pesquisaCodigo((Integer) lista.get(i).getArgumento1());
                if (movimento.getAcordo() != null) {
                    acordo = dbAc.pesquisaCodigo(movimento.getAcordo().getId());
                    if (acordo != null) {
                        listaC.addAll(db.pesquisaAcordoAberto(acordo.getId()));
                    }
                } else {
                    msgConfirma = "Não existe acordo para este boleto!";
                    return null;
                }
            }
        }

        if (!listaC.isEmpty()) {
            historico = dbAc.pesquisaHistorico(listaC.get(0).getId());
            Boleto boleto = db.pesquisaBoletos(listaC.get(0).getNrCtrBoleto());
            if (historico == null && boleto != null) {
                historico = dbAc.pesquisaHistoricoBaixado(boleto.getContaCobranca().getId(),
                        boleto.getBoletoComposto(),
                        listaC.get(0).getServicos().getId());
            }
            if (historico != null) {
                ImprimirBoleto imp = new ImprimirBoleto();
                imp.imprimirPromissoria(listaC, false);
                imp.visualizar(null);
            } else {
                msgConfirma = "Não existe histórico para este acordo!";
            }
        } else {
            msgConfirma = "Nenhum Acordo encontrado!";
        }
        return null;
    }

    public String imprimir() {
        MovimentoDB db = new MovimentoDBToplink();
        List<Movimento> listaC = new ArrayList<Movimento>();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();

        if (listMov.isEmpty()) {
            msgConfirma = "Lista vazia!";
            return null;
        }

        if (bltQuitados() == true) {
            msgConfirma = "Boletos quitados não podem ser Impressos!";
            return null;
        }

        if (bltSelecionados() != true) {
            msgConfirma = "Nenhum Boleto Selecionado!";
            return null;
        }

        Movimento mov = new Movimento();
        for (int i = 0; i < listMov.size(); i++) {
            if (((Boolean) lista.get(i).getArgumento0())) {
                mov = db.pesquisaCodigo((Integer) lista.get(i).getArgumento1());
                listaC.add(mov);
                listaValores.add(mov.getValor());
                listaVencimentos.add(mov.getVencimento());
            }
        }
        ImprimirBoleto imp = new ImprimirBoleto();

        listaC = imp.atualizaContaCobrancaMovimento(listaC);

        imp.imprimirBoleto(listaC, listaValores, listaVencimentos, imprimirVerso);
        imp.visualizar(null);
        recarregaPag = true;
        getListaMovimentos();
        return "extratoTela";
    }

    public boolean isImprimirVerso() {
        return imprimirVerso;
    }

    public void setImprimirVerso(boolean imprimirVerso) {
        this.imprimirVerso = imprimirVerso;
    }

    public String enviarEmail() {
        //Movimento movimento = new Movimento();
        Juridica juridica = new Juridica();

        JuridicaDB dbj = new JuridicaDBToplink();
        MovimentoDB dbM = new MovimentoDBToplink();

        List<Movimento> movadd = new ArrayList();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();

        //List<Linha> select  = new ArrayList();

        List<Movimento> listaux = new ArrayList();
        boolean enviar = false;
        int id_contabil = 0, id_empresa = 0, id_compara = 0;

        if (lista.isEmpty()) {
            msgConfirma = "Lista vazia!";
            return null;
        }

        for (int i = 0; i < lista.size(); i++) {
            if ((Boolean) lista.get(i).getArgumento0()) {
                Movimento mo = (Movimento) new SalvarAcumuladoDBToplink().pesquisaCodigo((Integer) lista.get(i).getArgumento1(), "Movimento");
                if (mo.getBaixa() != null) {
                    msgConfirma = "Não pode enviar email de boletos quitados!";
                    return null;
                } else {
                    listaux.add(mo);
                }
            }
        }

        if (listaux.isEmpty()) {
            msgConfirma = "Nenhum boleto selecionado";
            return null;
        }


        for (int i = 0; i < listaux.size(); i++) {
            juridica = dbj.pesquisaJuridicaPorPessoa(listaux.get(i).getPessoa().getId());

            /* ENVIO PARA CONTABILIDADE */
            if (tipoEnvio.equals("contabilidade")) {
                if (juridica.getContabilidade() != null) {
                    id_contabil = juridica.getContabilidade().getId();
                } else {
                    msgConfirma = "Empresa " + juridica.getPessoa().getNome() + " não pertence a nenhuma contabilidade";
                    return null;
                }
                movadd.add(listaux.get(i));
                listaValores.add(listaux.get(i).getValor());
                listaVencimentos.add(listaux.get(i).getVencimento());

                try {
                    id_compara = dbj.pesquisaJuridicaPorPessoa(listaux.get(i + 1).getPessoa().getId()).getContabilidade().getId();
                    if (id_contabil != id_compara) {
                        enviar = true;
                    }
                } catch (Exception e) {
                    enviar = true;
                }

                if (enviar) {
                    enviar(movadd, listaValores, listaVencimentos, juridica.getContabilidade());
                    enviar = false;
                    movadd.clear();
                    listaValores.clear();
                    listaVencimentos.clear();
                }
                /* ENVIO PARA EMPRESA */
            } else {
                id_empresa = juridica.getId();

                movadd.add(listaux.get(i));
                listaValores.add(listaux.get(i).getValor());
                listaVencimentos.add(listaux.get(i).getVencimento());

                try {
                    id_compara = dbj.pesquisaJuridicaPorPessoa(listaux.get(i + 1).getPessoa().getId()).getId();
                    if (id_empresa != id_compara) {
                        enviar = true;
                    }
                } catch (Exception e) {
                    enviar = true;
                }

                if (enviar) {
                    enviar(movadd, listaValores, listaVencimentos, juridica);
                    enviar = false;
                    movadd.clear();
                    listaValores.clear();
                    listaVencimentos.clear();
                }
            }


        }
        return null;
    }

    public void enviar(List<Movimento> mov, List<Float> listaValores, List<String> listaVencimentos, Juridica jur) {
        try {

            Registro reg = new Registro();
            reg = (Registro) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Registro");

            ImprimirBoleto imp = new ImprimirBoleto();
            imp.imprimirBoleto(mov, listaValores, listaVencimentos, false);
            String nome = imp.criarLink(jur.getPessoa(), reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");
            List<Pessoa> p = new ArrayList();

            p.add(jur.getPessoa());

            String[] ret = new String[2];
            if (!reg.isEnviarEmailAnexo()) {
                ret = EnviarEmail.EnviarEmailPersonalizado(reg,
                        p,
                        " <div style='background:#00ccff; padding: 15px; font-size:13pt'>Envio cadastrado para <b>" + jur.getPessoa().getNome() + " </b></div><br />"
                        + " <h5>Visualize seu boleto clicando no link abaixo</h5><br /><br />"
                        + " <a href='" + reg.getUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "'>Clique aqui para abrir boleto</a><br />",
                        new ArrayList(),
                        "Envio de Boleto");
            } else {
                List<File> fls = new ArrayList<File>();
                fls.add(new File(imp.getPathPasta() + "/" + nome));

                ret = EnviarEmail.EnviarEmailPersonalizado(reg,
                        p,
                        " <div style='background:#00ccff; padding: 15px; font-size:13pt'>Envio cadastrado para <b>" + jur.getPessoa().getNome() + " </b></div><br />"
                        + " <h5>Baixe seu boleto anexado neste email</5><br /><br />",
                        fls,
                        "Envio de Boleto");
            }
            if (!ret[1].isEmpty()) {
                msgConfirma = ret[1];
            } else {
                msgConfirma = ret[0];
            }
            //msgConfirma = "Envio Concluído!";
        } catch (Exception erro) {
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());

        }
    }

    public String imprimirPlanilha() {
        List<Movimento> listaC = new ArrayList<Movimento>();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
        Acordo acordo = new Acordo();
        AcordoDB dbAc = new AcordoDBToplink();
        if (bltSelecionados() != true) {
            msgConfirma = "Nenhum Boleto Selecionado!";
            return null;
        }
        int qnt = 0;
        for (int i = 0; i < listMov.size(); i++) {
            if (((Boolean) lista.get(i).getArgumento0())) {
                qnt++;
                if (qnt > 1) {
                    msgConfirma = "Somente um acordo pode ser selecionado!";
                    return null;
                }
                movimento = db.pesquisaCodigo((Integer) lista.get(i).getArgumento1());
                if (movimento.getAcordo() != null) {
                    acordo = dbAc.pesquisaCodigo(movimento.getAcordo().getId());
                    if (acordo != null) {
                        listaC.addAll(db.pesquisaAcordoTodos(acordo.getId()));
                    }
                } else {
                    msgConfirma = "Não existe acordo para este boleto!";
                    return null;
                }
            }
        }

        if (!listaC.isEmpty()) {
            int ind = 0;
            for (int i = 0; i < listaC.size(); i++) {
                if (listaC.get(i).isAtivo()) {
                    ind = i;
                }
            }

            List hist = dbAc.listaHistoricoAgrupado(listaC.get(ind).getAcordo().getId());
            Boleto boleto = db.pesquisaBoletos(listaC.get(ind).getNrCtrBoleto());
            if (hist.isEmpty() && boleto != null) {
                Historico h = dbAc.pesquisaHistoricoBaixado(boleto.getContaCobranca().getId(),
                        boleto.getBoletoComposto(),
                        listaC.get(ind).getServicos().getId());
                if (h != null) {
                    hist.add(h.getHistorico());
                }
            }
            if (!hist.isEmpty()) {
                String s_hist = "Acordo correspondente a: ";
                for (int i = 0; i < hist.size(); i++) {
                    s_hist += " " + hist.get(i);
                }
                ImprimirBoleto imp = new ImprimirBoleto();
                imp.imprimirAcordoAcordado(listaC, acordo, s_hist, imprimirVerso);
                imp.visualizar(null);
            } else {
                msgConfirma = "Não existe histórico para este acordo!";
            }
        } else {
            msgConfirma = "Nenhum Acordo encontrado!";
        }
        return null;
    }

    public String excluirAcordo() {

        List<Movimento> listaC = new ArrayList<Movimento>();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
        if (bltSelecionados() != true) {
            msgConfirma = "Nenhum Boleto Selecionado!";
            return null;
        }
        int qnt = 0;
        for (int i = 0; i < listMov.size(); i++) {
            if (((Boolean) lista.get(i).getArgumento0())) {
                qnt++;
                if (qnt > 1) {
                    msgConfirma = "Somente um acordo pode ser selecionado!";
                    return null;
                }
                movimento = db.pesquisaCodigo((Integer) lista.get(i).getArgumento1());
                if (movimento.getAcordo() != null) {
                    if (movimento.getAcordo().getId() != -1) {
                        listaC.addAll(db.pesquisaAcordoParaExclusao(movimento.getAcordo().getId()));
                    }
                } else {
                    msgConfirma = "Não existe acordo para este boleto!";
                    return null;
                }
                for (int w = 0; w < listaC.size(); w++) {
                    if (listaC.get(w).getBaixa() != null && listaC.get(w).isAtivo()) {
                        msgConfirma = "Acordo com parcela já paga não pode ser excluído!";
                        return null;
                    }
                }
            }
        }

        if (!listaC.isEmpty()) {
            String ids = "";
            for (int i = 0; i < listaC.size(); i++) {
                if (ids.length() > 0 && i != listaC.size()) {
                    ids = ids + ",";
                }
                ids = ids + String.valueOf(listaC.get(i).getId());
            }



            if (ids.isEmpty()) {
                return null;
            } else {
                db.excluirAcordoIn(ids, listaC.get(0).getAcordo().getId());
            }
            recarregaPag = true;
            getListaMovimentos();
            msgConfirma = "Acordo Excluído com sucesso!";
        } else {
            msgConfirma = "Nenhum Acordo encontrado!";
        }
        return null;
    }

    public String carregaDataAntiga() {
        DataObject dtObj = lista.get(idIndex);
        dataAntiga = ((String) dtObj.getArgumento9());
        dataNova = "";
        idMovimento = (Integer) dtObj.getArgumento1();
        return null;
    }

    public String atualizarData() {
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movi = null;
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        if (dataNova.length() == 10) {
            if (DataHoje.converteDataParaInteger(dataNova) >= DataHoje.converteDataParaInteger(DataHoje.data())) {
                movi = db.pesquisaCodigo(idMovimento);
                movi.setVencimento(dataNova);
                dbSalvar.abrirTransacao();
                if (dbSalvar.alterarObjeto(movi)) {
                    dbSalvar.comitarTransacao();
                } else {
                    dbSalvar.desfazerTransacao();
                }
                recarregaPag = true;
            }
        }
        return "extratoTela";
    }

    public String getDataAntiga() {
        return dataAntiga;
    }

    public void setDataAntiga(String dataAntiga) {
        this.dataAntiga = dataAntiga;
    }

    public String getDataNova() {
        return dataNova;
    }

    public void setDataNova(String dataNova) {
        this.dataNova = dataNova;
    }

    public Pessoa getPessoa() {
        Pessoa p = this.getPesquisaPessoa();
        if (p.getId() != -1) {
            pessoa = p;
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(String tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public String getValorExtenso() {
        if (!listMov.isEmpty() && !vlLiquido.isEmpty()) {
            ValorExtenso ve = new ValorExtenso();
            ve.setNumber(Double.valueOf(Float.toString(Moeda.substituiVirgulaFloat(vlLiquido))));
            return valorExtenso = ve.toString();
        } else {
            return valorExtenso = "";
        }
    }

    public void setValorExtenso(String valorExtenso) {
        this.valorExtenso = valorExtenso;
    }
    

    public String getTipoPesquisa() {
        return tipoPesquisa;
    }

    public void setTipoPesquisa(String tipoPesquisa) {
        this.tipoPesquisa = tipoPesquisa;
    }

    public void refreshForm() {
    }

    public String refreshTela() {
        return "extratoTela";
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getGeraPesquisa() {
        return geraPesquisa;
    }

    public void setGeraPesquisa(String geraPesquisa) {
        this.geraPesquisa = geraPesquisa;
    }

    public String getTipoDataPesquisa() {
        return tipoDataPesquisa;
    }

    public void setTipoDataPesquisa(String tipoDataPesquisa) {
        this.tipoDataPesquisa = tipoDataPesquisa;
    }

    public int getIdContribuicao() {
        return idContribuicao;
    }

    public void setIdContribuicao(int idContribuicao) {
        this.idContribuicao = idContribuicao;
    }

    private Pessoa getPesquisaPessoa() {
        Pessoa p = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
        if (p == null) {
            p = new Pessoa();
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
            recarregaPag = true;
        }
        return p;
    }

    public boolean isChkData() {
        return chkData;
    }

    public void setChkData(boolean chkData) {
        this.chkData = chkData;
    }

    public boolean isChkContribuicao() {
        return chkContribuicao;
    }

    public void setChkContribuicao(boolean chkContribuicao) {
        this.chkContribuicao = chkContribuicao;
    }

    public boolean isChkNrBoletos() {
        return chkNrBoletos;
    }

    public void setChkNrBoletos(boolean chkNrBoletos) {
        this.chkNrBoletos = chkNrBoletos;
    }

    public boolean isChkEmpresa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            chkEmpresa = true;
        }
        return chkEmpresa;
    }

    public void setChkEmpresa(boolean chkEmpresa) {
        this.chkEmpresa = chkEmpresa;
    }

    public String getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(String dataInicial) {
        this.dataInicial = dataInicial;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Movimento getMov() {
        return mov;
    }

    public void setMov(Movimento mov) {
        this.mov = mov;
    }

    public int getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(int idTipoServico) {
        this.idTipoServico = idTipoServico;
    }

    public String getBoletoInicial() {
        return boletoInicial;
    }

    public void setBoletoInicial(String boletoInicial) {
        this.boletoInicial = boletoInicial;
    }

    public String getBoletoFinal() {
        return boletoFinal;
    }

    public void setBoletoFinal(String boletoFinal) {
        this.boletoFinal = boletoFinal;
    }

    public String getDataRefInicial() {
        return dataRefInicial;
    }

    public void setDataRefInicial(String dataRefInicial) {
        this.dataRefInicial = dataRefInicial;
    }

    public String getDataRefFinal() {
        return dataRefFinal;
    }

    public void setDataRefFinal(String dataRefFinal) {
        this.dataRefFinal = dataRefFinal;
    }

    public boolean isChkTipo() {
        return chkTipo;
    }

    public void setChkTipo(boolean chkTipo) {
        this.chkTipo = chkTipo;
    }

    public boolean isChkExcluirBol() {
        return chkExcluirBol;
    }

    public void setChkExcluirBol(boolean chkExcluirBol) {
        this.chkExcluirBol = chkExcluirBol;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getValorSomado() {
        valorSomado = getValorTotal();
        return valorSomado;
    }

    public void setValorSomado(String valorSomado) {
        this.valorSomado = valorSomado;
    }    
    
}