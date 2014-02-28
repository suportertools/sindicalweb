package br.com.rtools.escola.beans;

import br.com.rtools.associativo.Midia;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.escola.*;
import br.com.rtools.escola.db.MatriculaContratoDB;
import br.com.rtools.escola.db.MatriculaContratoDBToplink;
import br.com.rtools.escola.db.MatriculaEscolaDB;
import br.com.rtools.escola.db.MatriculaEscolaDBToplink;
import br.com.rtools.escola.db.TurmaDB;
import br.com.rtools.escola.db.TurmaDBToplink;
import br.com.rtools.escola.lista.ListaMatriculaEscola;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.LoteDB;
import br.com.rtools.financeiro.db.LoteDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.impressao.CarneEscola;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.pessoa.db.PessoaEmpresaDB;
import br.com.rtools.pessoa.db.PessoaEmpresaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.pessoa.db.SpcDB;
import br.com.rtools.pessoa.db.SpcDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.*;
import br.com.rtools.utilitarios.db.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class MatriculaEscolaBean implements Serializable {

    private Filial filial = new Filial();
    private Fisica aluno = new Fisica();
    private Juridica juridica = new Juridica();
    private Juridica empresa = new Juridica();
    private Lote lote = new Lote();
    private MatriculaEscola matriculaEscola = new MatriculaEscola();
    private Turma turma = new Turma();
    private MatriculaContrato matriculaContrato = new MatriculaContrato();
    private MatriculaIndividual matriculaIndividual = new MatriculaIndividual();
    private MatriculaTurma matriculaTurma = new MatriculaTurma();
    private Pessoa pessoaAlunoMemoria = new Pessoa();
    private Pessoa pessoaResponsavelMemoria = new Pessoa();
    private EscolaAutorizados escolaAutorizados = new EscolaAutorizados();
    private EscolaAutorizados escolaAutorizadosDetalhes = new EscolaAutorizados();
    private MacFilial macFilial = new MacFilial();
    private Movimento movimento = new Movimento();
    private Pessoa responsavel = new Pessoa();
    private PessoaComplemento pessoaComplemento = new PessoaComplemento();
    private Registro registro = new Registro();
    private List listaGridMEscola = new ArrayList();
    private List<SelectItem> listaStatus = new ArrayList<SelectItem>();
    private List<FTipoDocumento> listaFTipoDocumento = new ArrayList<FTipoDocumento>();
    private List<SelectItem> listaCursosDisponiveis = new ArrayList<SelectItem>();
    private List<SelectItem> listaVendedor = new ArrayList<SelectItem>();
    private List<SelectItem> listaProfessor = new ArrayList<SelectItem>();
    private List<SelectItem> listaMidia = new ArrayList<SelectItem>();
    private List<SelectItem> listaNumeros = new ArrayList<SelectItem>();
    private List<SelectItem> listaDataVencimento = new ArrayList<SelectItem>();
    private List<SelectItem> listaIndividual = new ArrayList<SelectItem>();
    private List<SelectItem> listaDataTaxa = new ArrayList<SelectItem>();
    private List<SelectItem> listaMesVencimento = new ArrayList<SelectItem>();
    private List<Turma> listaTurma = new ArrayList<Turma>();
    private List<Movimento> listaMovimentos = new ArrayList<Movimento>();
    private List<EscolaAutorizados> listaEscolaAutorizadas = new ArrayList<EscolaAutorizados>();
    private List<ListaMatriculaEscola> listaMatriculaEscolas = new ArrayList<ListaMatriculaEscola>();
    private int diaVencimento = 0;
    private int idDiaVencimento = 0;
    private int idDiaVencimentoPessoa = 0;
    private int idDataTaxa = 0;
    private int idMesVencimento = 0;
    private int idFTipoDocumento = 0;
    private int idIndividual = 0;
    private int idMidia = 0;
    private int idTurma = 0;
    private int idStatus = 0;
    private int idVendedor = 0;
    private int idProfessor = 0;
    private int idServico = 0;
    private int idCursosDisponiveis = 0;
    private int idadeAluno = 0;
    private int vagasDisponiveis = 0;
    private float vTaxa = 0;
    private boolean alunoFoto = false;
    private boolean alterarPessoaComplemento = false;
    private boolean desabilitaTurma = false;
    private boolean desabilitaIndividual = false;
    private boolean desabilitaCamposMovimento = false;
    private boolean desabilitaGeracaoContrato = false;
    private boolean desabilitaDescontoFolha = true;
    private boolean desabilitaDiaVencimento = false;
    private boolean desabilitaCampo = false;
    private boolean descontoProporcional = false;
    private boolean habilitaGerarParcelas = false;
    private boolean limpar = false;
    private boolean showDescontoProporcional = false;
    private boolean ocultaBotaoSalvar = false;
    private boolean ocultaDescontoFolha = true;
    private boolean responsavelNaoSocio = false;
    private boolean socio = false;
    private boolean taxa = false;
    private boolean visibility = false;
    private String comoPesquisa = "";
    private String descricao = "";
    private String descricaoCurso = "";
    private String msgStatusFilial = "";
    private String mensagem = "";
    private String msgStatusDebito = "";
    private String msgStatusEmpresa = "";
    private String openModal = "";
    private String porPesquisa = "";
    private String target = "#";
    private String tipoMatricula = "Turma";
    private String valor = "";
    private String valorParcela = "";
    private String valorDescontoProporcional = "";
    private String valorParcelaVencimento = "";
    private String valorLiquido = "";
    private String valorTaxa = "";

    public String novo() {
//        empresa = new Juridica();
//        ocultaBotaoSalvar = false;
//        vTaxa = 0;
//        pessoaAlunoMemoria = new Pessoa();
//        pessoaResponsavelMemoria = new Pessoa();
//        escolaAutorizados = new EscolaAutorizados();
//        escolaAutorizadosDetalhes = new EscolaAutorizados();
//        responsavel = new Pessoa();
//        matriculaTurma = new MatriculaTurma();
//        turma = new Turma();
//        matriculaIndividual = new MatriculaIndividual();
//        macFilial = new MacFilial();
//        filial = new Filial();
//        pessoaComplemento = new PessoaComplemento();
//        movimento = new Movimento();
//        matriculaEscola = new MatriculaEscola();
//        lote = new Lote();
//        idFTipoDocumento = 0;
//        socio = false;
//        alterarPessoaComplemento = false;
//        idDiaVencimentoPessoa = 0;
//        aluno = new Fisica();
//        idTurma = 0;
//        idIndividual = 0;
//        idStatus = 0;
//        idVendedor = 0;
//        idProfessor = 0;
//        idMidia = 0;
//        idCursosDisponiveis = 0;
//        idadeAluno = 0;
//        idDiaVencimento = 0;
//        diaVencimento = 0;
//        listaStatus.clear();
//        listaVendedor.clear();
//        listaDataTaxa.clear();
//        listaMesVencimento.clear();
//        listaProfessor.clear();
//        listaMidia.clear();
//        listaCursosDisponiveis.clear();
//        listaNumeros.clear();
//        listaFTipoDocumento.clear();
//        listaDataVencimento.clear();
//        listaTurma.clear();
//        listaIndividual.clear();
//        listaMatriculaEscolas.clear();
//        listaMovimentos.clear();
//        desabilitaTurma = false;
//        desabilitaIndividual = false;
//        desabilitaCamposMovimento = false;
//        desabilitaDescontoFolha = true;
//        limpar = false;
//        taxa = false;
//        desabilitaCampo = false;
//        taxa = false;
//        descontoProporcional = false;
//        showDescontoProporcional = false;
//        alunoFoto = false;
//        porPesquisa = "";
//        comoPesquisa = "";
//        mensagem = "";
//        valorTaxa = "";
//        target = "#";
//        valor = "";
//        valorLiquido = "";
//        valorParcela = "";
//        valorParcelaVencimento = "";
//        openModal = "";
//        visibility = false;
//        listaEscolaAutorizadas.clear();
        GenericaSessao.remove("matriculaEscolaBean");
        GenericaSessao.remove("matriculaEscolaPesquisa");
        GenericaSessao.remove("pesquisaFisicaTipo");
        GenericaSessao.remove("pesquisaFisica");
        GenericaSessao.remove("pesquisaJuridica");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("fisicaPesquisa");
        return null;
    }

    public void gerarContrato() {
        if (matriculaEscola.getEvt() == null) {
            GenericaMensagem.warn("Sistema", "Necessário gerar movimento para imprimir esse contrato!");
            return;
        }
        if (matriculaEscola.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            Turma turmax = new Turma();
            String contratoCurso;
            String contratoDiaSemana = "";
            MatriculaContratoDB dB = new MatriculaContratoDBToplink();
            if (tipoMatricula.equals("Individual")) {
                matriculaContrato = dB.pesquisaCodigoServico(matriculaIndividual.getCurso().getId());
            } else {
                matriculaContrato = dB.pesquisaCodigoServico(matriculaTurma.getTurma().getCursos().getId());
            }
            if (matriculaContrato == null) {
                mensagem = "Não é possível gerar um contrato para este serviço. Para gerar um contrato acesse: Menu Escola > Suporte > Modelo Contrato.";
                GenericaMensagem.warn("Sistema", mensagem);
                return;
            }
            String horaInicial;
            String horaFinal;
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$aluno", matriculaEscola.getAluno().getNome()));
            FisicaDB fisicaDB = new FisicaDBToplink();
            Fisica contratoFisica = fisicaDB.pesquisaFisicaPorPessoa(matriculaEscola.getResponsavel().getId());
            if (contratoFisica == null) {
                contratoFisica = new Fisica();
            }
            Fisica rgAluno = fisicaDB.pesquisaFisicaPorPessoa(matriculaEscola.getAluno().getId());
            if (rgAluno == null) {
                rgAluno = new Fisica();
            }
            List listaDiaSemana = new ArrayList();
            int periodoMeses;
            String periodoMesesExtenso;
            if (tipoMatricula.equals("Individual")) {
                contratoCurso = matriculaIndividual.getCurso().getDescricao();
                if (matriculaIndividual.isSegunda()) {
                    listaDiaSemana.add("Seg");
                }
                if (matriculaIndividual.isTerca()) {
                    listaDiaSemana.add("Ter");
                }
                if (matriculaIndividual.isQuarta()) {
                    listaDiaSemana.add("Qua");
                }
                if (matriculaIndividual.isQuinta()) {
                    listaDiaSemana.add("Qui");
                }
                if (matriculaIndividual.isSexta()) {
                    listaDiaSemana.add("Sex");
                }
                if (matriculaIndividual.isSabado()) {
                    listaDiaSemana.add("Sab");
                }
                if (matriculaIndividual.isDomingo()) {
                    listaDiaSemana.add("Dom");
                }
                horaInicial = matriculaIndividual.getInicio();
                horaFinal = matriculaIndividual.getTermino();
                periodoMeses = DataHoje.quantidadeMeses(matriculaIndividual.getDataInicio(), matriculaIndividual.getDataTermino()) + 1;
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$mesAnoInicialExtenso", DataHoje.dataExtenso(matriculaIndividual.getDataInicioString(), 1)));
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$mesAnoFinalExtenso", DataHoje.dataExtenso(matriculaIndividual.getDataTerminoString(), 1)));
            } else {
                turmax = ((MatriculaTurma) salvarAcumuladoDB.find(matriculaTurma)).getTurma();
                contratoCurso = matriculaTurma.getTurma().getCursos().getDescricao();
                periodoMeses = DataHoje.quantidadeMeses(turma.getDtInicio(), turma.getDtTermino()) + 1;
                if (turmax.isSegunda()) {
                    listaDiaSemana.add("Seg");
                }
                if (turmax.isTerca()) {
                    listaDiaSemana.add("Ter");
                }
                if (turmax.isQuarta()) {
                    listaDiaSemana.add("Qua");
                }
                if (turmax.isQuinta()) {
                    listaDiaSemana.add("Qui");
                }
                if (turmax.isSexta()) {
                    listaDiaSemana.add("Sex");
                }
                if (turmax.isSabado()) {
                    listaDiaSemana.add("Sab");
                }
                if (turmax.isDomingo()) {
                    listaDiaSemana.add("Dom");
                }
                horaInicial = matriculaTurma.getTurma().getHoraInicio();
                horaFinal = matriculaTurma.getTurma().getHoraTermino();
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$descricao", turmax.getDescricao()));
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$mesAnoInicialExtenso", DataHoje.dataExtenso(matriculaTurma.getTurma().getDataInicio(), 1)));
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$mesAnoFinalExtenso", DataHoje.dataExtenso(matriculaTurma.getTurma().getDataTermino(), 1)));
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$sala", matriculaTurma.getTurma().getSala()));
            }
            if (periodoMeses == 0) {
                periodoMesesExtenso = "mês atual";
            } else {
                ValorExtenso valorExtenso = new ValorExtenso();
                valorExtenso.setNumber((double) periodoMeses);
                periodoMesesExtenso = (valorExtenso.toString()).replace("reais", "");
            }
            for (int i = 0; i < listaDiaSemana.size(); i++) {
                if (i == 0) {
                    contratoDiaSemana = listaDiaSemana.get(i).toString();
                } else {
                    contratoDiaSemana += " , " + listaDiaSemana.get(i).toString();
                }
            }
            String enderecoAlunoString = "";
            String bairroAlunoString = "";
            String cidadeAlunoString = "";
            String estadoAlunoString = "";
            String cepAlunoString = "";
            String enderecoResponsavelString = "";
            String bairroResponsavelString = "";
            String cidadeResponsavelString = "";
            String estadoResponsavelString = "";
            String cepResponsavelString = "";
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            PessoaEndereco pessoaEnderecoAluno = (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoaTipo(matriculaEscola.getAluno().getId(), 1);

            int idTipoEndereco = -1;
            if (pessoaEnderecoAluno != null) {
                enderecoAlunoString = pessoaEnderecoAluno.getEndereco().getEnderecoSimplesToString() + ", " + pessoaEnderecoAluno.getNumero();
                bairroAlunoString = pessoaEnderecoAluno.getEndereco().getBairro().getDescricao();
                cidadeAlunoString = pessoaEnderecoAluno.getEndereco().getCidade().getCidade();
                estadoAlunoString = pessoaEnderecoAluno.getEndereco().getCidade().getUf();
                cepAlunoString = pessoaEnderecoAluno.getEndereco().getCep();
            }
            if (matriculaEscola.getResponsavel().getId() != matriculaEscola.getAluno().getId()) {
                // Tipo Documento - CPF
                if (matriculaEscola.getResponsavel().getTipoDocumento().getId() == 1) {
                    idTipoEndereco = 1;
                    // Tipo Documento - CNPJ
                } else if (matriculaEscola.getResponsavel().getTipoDocumento().getId() == 2) {
                    idTipoEndereco = 3;
                }
            } else {
                enderecoResponsavelString = enderecoAlunoString;
                bairroResponsavelString = bairroAlunoString;
                cidadeResponsavelString = cidadeAlunoString;
                estadoResponsavelString = estadoAlunoString;
                cepResponsavelString = cepAlunoString;
            }
            PessoaEndereco pessoaEnderecoResponsavel = (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoaTipo(matriculaEscola.getResponsavel().getId(), idTipoEndereco);
            if (pessoaEnderecoResponsavel != null) {
                enderecoResponsavelString = pessoaEnderecoResponsavel.getEndereco().getEnderecoSimplesToString() + ", " + pessoaEnderecoResponsavel.getNumero();
                bairroResponsavelString = pessoaEnderecoResponsavel.getEndereco().getBairro().getDescricao();
                cidadeResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCidade().getCidade();
                estadoResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCidade().getUf();
                cepResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCep();
            }
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cpfAluno", (matriculaEscola.getAluno().getDocumento())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$rgAluno", rgAluno.getRg()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$telefonesAluno", matriculaEscola.getAluno().getTelefone1() + " . " + matriculaEscola.getAluno().getTelefone2() + " . " + matriculaEscola.getAluno().getTelefone3()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$estadoCivilAluno", rgAluno.getEstadoCivil()));
            if (getResponsavel().getId() != -1) {

            }
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$responsavel", (matriculaEscola.getResponsavel().getNome())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cpfResponsavel", matriculaEscola.getResponsavel().getDocumento()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$rgResponsavel", (contratoFisica.getRg())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$telefonesResponsavel", matriculaEscola.getResponsavel().getTelefone1() + " . " + matriculaEscola.getResponsavel().getTelefone2() + " . " + matriculaEscola.getResponsavel().getTelefone3()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$estadoCivilResponsavel", contratoFisica.getEstadoCivil()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$curso", (contratoCurso)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$diaSemana", (contratoDiaSemana)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataInicialExtenso", (DataHoje.dataExtenso(turmax.getDataInicio()))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataFinalExtenso", (DataHoje.dataExtenso(turmax.getDataTermino()))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataExtenso", (DataHoje.dataExtenso(DataHoje.data()))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataInicial", (turmax.getDataInicio())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataFinal", (turmax.getDataTermino())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$valorParcela", (Moeda.converteR$Float(matriculaEscola.getValorTotal() / matriculaEscola.getNumeroParcelas()))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$descontoExtenso", Moeda.converteR$Float(matriculaEscola.getDesconto() + matriculaEscola.getValorDescontoProporcional())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$desconto", Moeda.converteR$Float(matriculaEscola.getDesconto() + matriculaEscola.getValorDescontoProporcional())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$parcelas", (Integer.toString(matriculaEscola.getNumeroParcelas()))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$diaVencimento", (Integer.toString(matriculaEscola.getDiaVencimento()))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$valorAteVencimento", (Moeda.converteR$Float((matriculaEscola.getValorTotal() - matriculaEscola.getDescontoAteVencimento()) / matriculaEscola.getNumeroParcelas()))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$horaInicial", (horaInicial)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$horaFinal", (horaFinal)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataMatricula", DataHoje.dataExtenso(matriculaEscola.getDataMatriculaString(), 1)));
            if (matriculaEscola.isDescontoProporcional()) {
                FunctionsDB functionsDB = new FunctionsDBTopLink();
                String valorTotal = functionsDB.scriptSimples(" SUM(nr_valor) FROM fin_movimento WHERE id_tipo_documento = 13 AND id_lote = " + listaMovimentos.get(0).getLote().getId());
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$valorTotal", Moeda.converteR$Float(Float.parseFloat(valorTotal))));
            } else {
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$valorTotal", (Moeda.converteR$Float((matriculaEscola.getValorTotal())))));
            }
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$matricula", (Integer.toString(matriculaEscola.getId()))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$ano", (DataHoje.livre(DataHoje.dataHoje(), "yyyy"))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$enderecoAluno", (enderecoAlunoString)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$bairroAluno", (bairroAlunoString)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cidadeAluno", (cidadeAlunoString)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$estadoAluno", (estadoAlunoString)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cepAluno", (cepAlunoString)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$mesesExtenso", (periodoMesesExtenso)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$meses", (Integer.toString(periodoMeses))));
            String alunoNascimento = "";
            if (contratoFisica.getId() != -1) {
                alunoNascimento = (contratoFisica.getNascimento());
            }
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$nascimentoAluno", (alunoNascimento)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$emailAluno", (matriculaEscola.getAluno().getEmail1())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$enderecoResponsavel", (enderecoResponsavelString)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$bairroResponsavel", (bairroResponsavelString)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cidadeResponsavel", (cidadeResponsavelString)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$estadoResponsavel", (estadoResponsavelString)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cepResponsavel", (cepResponsavelString)));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$emailResponsavel", (matriculaEscola.getResponsavel().getEmail1())));
            String valorTaxaString = "";
            String listaValores = "";
            String listaValoresComData = "";
            int z = 1;
            for (Movimento listaMovimento : listaMovimentos) {
                if (listaMovimento.getTipoServico().getId() == 5) {
                    valorTaxaString = Moeda.converteR$Float(listaMovimento.getValor());
                } else {
                    if (z == 1) {
                        listaValores = "Parcela nº" + z + " - Valor: R$ " + Moeda.converteR$Float(listaMovimento.getValor());
                        listaValoresComData = z + "º - " + listaMovimento.getVencimento() + " - Valor: R$ " + Moeda.converteR$Float(listaMovimento.getValor());
                    } else {
                        listaValores += "; " + "Parcela nº" + z + " - Valor: R$ " + Moeda.converteR$Float(listaMovimento.getValor());
                        listaValoresComData += "; " + z + "º - " + listaMovimento.getVencimento() + " - Valor: R$ " + Moeda.converteR$Float(listaMovimento.getValor());
                    }
                    matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$vencimentoParcela" + z, listaMovimento.getVencimento()));
                    z++;
                }
            }
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$taxa", valorTaxaString));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$listaValoresComData", listaValoresComData));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$listaValores", listaValores));

            PessoaEmpresaDB pessoaEmpresaDB = new PessoaEmpresaDBToplink();
            PessoaEmpresa pessoaEmpresaAluno = pessoaEmpresaDB.pesquisaPessoaEmpresaPorPessoa(matriculaEscola.getAluno().getId());
            PessoaEmpresa pessoaEmpresaResponsavel = pessoaEmpresaDB.pesquisaPessoaEmpresaPorPessoa(matriculaEscola.getResponsavel().getId());
            if (pessoaEmpresaAluno.getId() != -1) {
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$localTrabalhoAluno", (pessoaEmpresaAluno.getJuridica().getPessoa().getNome() + " - CNPJ: " + pessoaEmpresaAluno.getJuridica().getPessoa().getDocumento())));
            } else {
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$localTrabalhoAluno", ""));
            }
            if (pessoaEmpresaResponsavel.getId() != -1) {
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$localTrabalhoResponsavel", (pessoaEmpresaResponsavel.getJuridica().getPessoa().getNome() + " - CNPJ: " + pessoaEmpresaResponsavel.getJuridica().getPessoa().getDocumento())));
            } else {
                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$localTrabalhoResponsavel", ""));
            }
            try {
                File dirFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/"));
                if (!dirFile.exists()) {
                    if (!Diretorio.criar("Arquivos/contrato")) {
                        return;
                    }
                }
                String fileName = "contrato" + DataHoje.hora().hashCode() + ".pdf";
                String filePDF = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/" + fileName);
                File file = new File(filePDF);
                boolean success = file.createNewFile();
                if (success) {
                    OutputStream os = new FileOutputStream(filePDF);
                    HtmlToPDF.convert(matriculaContrato.getDescricao(), os);

                    os.close();
                    String linha = getRegistro().getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/" + fileName;
                    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                    response.sendRedirect(linha);
                }
            } catch (IOException e) {
                e.getMessage();
            } catch (com.itextpdf.text.DocumentException ex) {
                Logger.getLogger(MatriculaEscolaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void gerarCarne() throws Exception, JRException {
        if (matriculaEscola.getEvt() != null) {
            if (listaMovimentos.size() > 0) {
                PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
                PessoaEndereco pessoaEndereco = ((List<PessoaEndereco>) pessoaEnderecoDB.pesquisaEndPorPessoa(matriculaEscola.getFilial().getFilial().getPessoa().getId())).get(0);
                List<CarneEscola> list = new ArrayList<CarneEscola>();
                int j = 1;
                for (Movimento listaMovimento : listaMovimentos) {
                    if (listaMovimento.getTipoServico().getId() != 5) {
                        list.add(new CarneEscola(matriculaEscola.getId(), ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), GenericaString.converterNullToString(matriculaEscola.getFilial().getFilial().getPessoa().getNome()), GenericaString.converterNullToString(pessoaEndereco.getEndereco().getLogradouro().getDescricao()), GenericaString.converterNullToString(pessoaEndereco.getNumero()), GenericaString.converterNullToString(pessoaEndereco.getComplemento()), GenericaString.converterNullToString(pessoaEndereco.getEndereco().getBairro().getDescricao()), GenericaString.converterNullToString(pessoaEndereco.getEndereco().getCep()), GenericaString.converterNullToString(pessoaEndereco.getEndereco().getCidade().getCidade()), GenericaString.converterNullToString(pessoaEndereco.getEndereco().getCidade().getUf()), GenericaString.converterNullToString(matriculaEscola.getFilial().getFilial().getPessoa().getTelefone1()), GenericaString.converterNullToString(matriculaEscola.getFilial().getFilial().getPessoa().getEmail1()), GenericaString.converterNullToString(matriculaEscola.getFilial().getFilial().getPessoa().getSite()), GenericaString.converterNullToString(matriculaEscola.getFilial().getFilial().getPessoa().getDocumento()), GenericaString.converterNullToString(matriculaEscola.getResponsavel().getNome()), GenericaString.converterNullToString(Integer.toString(matriculaEscola.getResponsavel().getId())), GenericaString.converterNullToString(matriculaEscola.getAluno().getNome()), GenericaString.converterNullToString(""), GenericaString.converterNullToString(listaMovimento.getServicos().getDescricao()), GenericaString.converterNullToString(DataHoje.converteData(listaMovimento.getDtVencimento())), GenericaString.converterNullToString(Float.toString(matriculaEscola.getValorTotal())), GenericaString.converterNullToString(Float.toString(listaMovimento.getValor())), ((Usuario) (GenericaSessao.getObject("sessaoUsuario"))).getPessoa().getNome(), j++, ""));
                    }
                }
                if (!list.isEmpty()) {
                    JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(list);
                    JasperReport jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/CARNE.jasper"));
                    JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                    byte[] arquivo = JasperExportManager.exportReportToPdf(print);
                    String nomeDownload = "carne_escola" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
                    SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                    if (!new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/carnes")).exists()) {
                        File file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/carnes"));
                        file.mkdir();
                    }
                    String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/carnes");
                    sa.salvaNaPasta(pathPasta);
                    Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
                    download.baixar();
                    download.remover();
                }
            }
        }

    }

    public void limpar() {
        if (limpar == true) {
            novo();
        }
    }

    public void salvar() {
        Filial fil = getMacFilial().getFilial();
        if (fil.getId() == -1) {
            mensagem = "Para salvar este cadastro é necessário realizar acesso com MAC Filial!";
            return;
        }
        if (existeMovimento()) {
            mensagem = "Não é possível atualizar essa matrícula, já possui movimentos baixados!";
            return;
        }
        int idPessoa = matriculaEscola.getResponsavel().getId();
        if (matriculaEscola.getAluno().getId() == -1) {
            mensagem = "Informar nome do aluno!";
            return;
        }
        if (matriculaEscola.getResponsavel().getId() == -1) {
            mensagem = "Informar nome do responsável!";
            return;
        }
        if (verificaSeContribuinteInativo(matriculaEscola.getResponsavel())) {
            mensagem = "Empresa inátiva!";
            return;
        }
        SpcDB spcDB = new SpcDBToplink();
        if (spcDB.existeRegistroPessoaSPC(matriculaEscola.getResponsavel())) {
            mensagem = "Responsável possui cadastro SERASA/SPC!";
            return;
        }
        if (matriculaEscola.getFilial().getId() == -1) {
            mensagem = "Informar a filial! Obs: Necessário acessar o sistema usando autênticação.";
            return;
        }
        if (listaStatus.isEmpty()) {
            mensagem = "Informar a situação/status do aluno!";
            return;
        }
        if (listaVendedor.isEmpty()) {
            mensagem = "Informar o nome do vendedor!";
            return;
        }
        if (listaMidia.isEmpty()) {
            mensagem = "Informar o tipo de mídia!";
            return;
        }
        if (listaMidia.isEmpty()) {
            mensagem = "Informar o tipo de mídia!";
            return;
        }
        if (tipoMatricula.equals("Individual")) {
            if (matriculaEscola.getId() == -1) {
                if (matriculaIndividual.getDataInicio() == null) {
                    mensagem = "Informar a data inicial!";
                    return;
                }
                if (matriculaIndividual.getDataTermino() == null) {
                    mensagem = "Informar a data de término!";
                    return;
                }
                int dataInicioInteger = DataHoje.converteDataParaInteger(matriculaIndividual.getDataInicioString());
                int dataFinalInteger = DataHoje.converteDataParaInteger(matriculaIndividual.getDataTerminoString());
                int dataHojeInteger = DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()));
                if (dataInicioInteger < dataHojeInteger) {
                    mensagem = "A data inicial do curso deve ser maior ou igual a data de hoje!";
                    return;
                }
                if (dataFinalInteger < dataHojeInteger) {
                    mensagem = "A data final do curso deve ser maior ou igual a data de hoje!";
                    return;
                }
                if (dataFinalInteger < dataInicioInteger) {
                    mensagem = "A data final deve ser maior ou igual a data inicial!";
                    return;
                }
                if (DataHoje.validaHora(matriculaIndividual.getInicio()).isEmpty()) {
                    mensagem = "Hora inicial invalida!";
                    return;
                }
                if (DataHoje.validaHora(matriculaIndividual.getTermino()).isEmpty()) {
                    mensagem = "Hora final invalida!";
                    return;
                }
            }
            if (listaIndividual.isEmpty()) {
                mensagem = "Informar curso!";
                return;
            }
            if (listaProfessor.isEmpty()) {
                mensagem = "Informar o nome do professor! Caso não exista cadastre um professor.";
                return;
            }
        } else {
            if (listaTurma.isEmpty()) {
                mensagem = "Informar a turma!";
                return;
            }
            if (turma.getId() == -1) {
                mensagem = "Informar a turma!";
                return;
            }
        }
        matriculaEscola.setDescontoProporcional(descontoProporcional);
        if (!descontoProporcional) {
            matriculaEscola.setValorDescontoProporcional(0);
        }
        NovoLog novoLog = new NovoLog();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        matriculaEscola.setEscStatus((EscStatus) sv.find(new EscStatus(), Integer.parseInt(listaStatus.get(idStatus).getDescription())));
        matriculaEscola.setVendedor((Vendedor) sv.find(new Vendedor(), Integer.parseInt(listaVendedor.get(idVendedor).getDescription())));
        matriculaEscola.setMidia((Midia) sv.find(new Midia(), Integer.parseInt(listaMidia.get(idMidia).getDescription())));
        matriculaEscola.setTipoDocumento((FTipoDocumento) sv.find(new FTipoDocumento(), 2));
//        for (FTipoDocumento listaFTipoDocumento1 : listaFTipoDocumento) {
//            if (listaFTipoDocumento1.getId() == idFTipoDocumento) {
//                matriculaEscola.setTipoDocumento(listaFTipoDocumento1);
//                break;
//            }
//        }
        if (tipoMatricula.equals("Individual")) {
            matriculaIndividual.setCurso((Servicos) sv.find(new Servicos(), Integer.parseInt(listaIndividual.get(idIndividual).getDescription())));
            matriculaIndividual.setProfessor((Professor) sv.find(new Professor(), Integer.parseInt(listaProfessor.get(idProfessor).getDescription())));
        } else if (tipoMatricula.equals("Turma")) {
//            matriculaTurma.setTurma((Turma) sv.pesquisaCodigo(Integer.parseInt(listaTurma.get(idTurma).getDescription()), "Turma"));
            matriculaTurma.setTurma(turma);
        }
        matriculaEscola.setEsEFinanceiro(null);
        matriculaEscola.setValorTotalString(valor);
        matriculaEscola.setDiaVencimento(idDiaVencimento);
        sv.abrirTransacao();
        if (matriculaEscola.getId() == -1) {
            MatriculaEscolaDB matriculaEscolaDB = new MatriculaEscolaDBToplink();
            FunctionsDB functionsDB = new FunctionsDBTopLink();
            int idNumeroVagas = functionsDB.vagasEscolaTurma(matriculaTurma.getTurma().getId());
            if (idNumeroVagas == 0) {
                matriculaEscola.setId(-1);
                matriculaTurma.setId(-1);
                matriculaIndividual.setId(-1);
                sv.desfazerTransacao();
                mensagem = "Não existem mais vagas disponíveis para esta turma!";
                return;
            }
            pessoaComplemento = matriculaEscolaDB.pesquisaDataRefPessoaComplemto(idPessoa);
            if (pessoaComplemento == null) {
                pessoaComplemento = new PessoaComplemento();
                pessoaComplemento.setNrDiaVencimento(idDiaVencimento);
                if (idFTipoDocumento == 2) {
                    pessoaComplemento.setCobrancaBancaria(true);
                } else {
                    pessoaComplemento.setCobrancaBancaria(false);
                }
                pessoaComplemento.setPessoa((Pessoa) sv.find(new Pessoa(), idPessoa));
                if (!sv.inserirObjeto(pessoaComplemento)) {
                    sv.desfazerTransacao();
                    mensagem = "Falha ao inserir pessoa complemento!";
                    return;
                }
            }
            matriculaEscola.setHabilitado(true);
            getFilial();
            matriculaEscola.setFilial(macFilial.getFilial());
            Evt evt = new Evt();
            matriculaEscola.setEvt(null);
            getMacFilial();
            String tipoMatriculaLog;
            if (sv.inserirObjeto(matriculaEscola)) {
                if (tipoMatricula.equals("Turma")) {
                    if (matriculaEscolaDB.existeVagasDisponivel(matriculaTurma)) {
                        sv.desfazerTransacao();
                        mensagem = "Não existem mais vagas disponíveis para essa turma!";
                        return;
                    }
                    matriculaTurma.setMatriculaEscola(matriculaEscola);
                    if (matriculaEscolaDB.existeMatriculaTurma(matriculaTurma)) {
                        sv.desfazerTransacao();
                        matriculaEscola.setId(-1);
                        matriculaTurma.setId(-1);
                        matriculaIndividual.setId(-1);
                        mensagem = "Aluno já cadastrado para essa turma!";
                        return;
                    }
                    setDesabilitaIndividual(true);
                    if (!sv.inserirObjeto(matriculaTurma)) {
                        matriculaEscola.setId(-1);
                        matriculaTurma.setId(-1);
                        matriculaIndividual.setId(-1);
                        sv.desfazerTransacao();
                        mensagem = "Falha ao adicionar a matricula turma!";
                        return;
                    }
                    tipoMatriculaLog = ""
                            + " - Turma: " + matriculaTurma.getTurma().getId() + " - " + matriculaTurma.getTurma().getCursos().getDescricao();
                } else {
                    setDesabilitaTurma(true);
                    matriculaIndividual.setMatriculaEscola(matriculaEscola);
                    if (matriculaEscolaDB.existeMatriculaIndividual(matriculaIndividual)) {
                        sv.desfazerTransacao();
                        matriculaEscola.setId(-1);
                        matriculaTurma.setId(-1);
                        matriculaIndividual.setId(-1);
                        mensagem = "Aluno já cadastrado para essa mátricula!";
                        return;
                    }
                    if (!sv.inserirObjeto(matriculaIndividual)) {
                        matriculaEscola.setId(-1);
                        matriculaTurma.setId(-1);
                        matriculaIndividual.setId(-1);
                        sv.desfazerTransacao();
                        mensagem = "Falha ao adicionar esta matricula individual!";
                        return;
                    }
                    tipoMatriculaLog = " - Curso: " + matriculaIndividual.getCurso().getId() + " - " + matriculaIndividual.getCurso().getDescricao()
                            + " - Professor: " + matriculaIndividual.getProfessor().getId() + " - " + matriculaIndividual.getProfessor().getProfessor()
                            + " - Período: " + matriculaIndividual.getDataInicioString() + " até " + matriculaIndividual.getDataTerminoString();
                }
                pessoaResponsavelMemoria = matriculaEscola.getResponsavel();
                pessoaAlunoMemoria = matriculaEscola.getAluno();
                sv.comitarTransacao();
                desabilitaCampo = true;
                mensagem = "Matrícula efetuada com sucesso.";
                novoLog.novo("Novo registro", "Matricula Escola "
                        + tipoMatricula + ": ID " + matriculaEscola.getId()
                        + " - Aluno: " + matriculaEscola.getAluno().getId() + " - " + matriculaEscola.getAluno().getNome()
                        + " - Responsável: " + matriculaEscola.getResponsavel().getId() + " - " + matriculaEscola.getResponsavel().getNome()
                        + " - Desconto: " + matriculaEscola.getDescontoAteVencimentoString()
                        + " - Dia do vencimento: " + matriculaEscola.getDiaVencimento()
                        + " - Valor: " + matriculaEscola.getValorTotalString()
                        + " - Número de Parcelas: " + matriculaEscola.getEscStatus().getDescricao()
                        + " - Filial: " + matriculaEscola.getFilial().getFilial().getPessoa().getId()
                        + " - Midia: " + matriculaEscola.getMidia().getDescricao()
                        + tipoMatriculaLog);

                target = "_blank";
                sv.fecharTransacao();
            } else {
                sv.desfazerTransacao();
                matriculaEscola.setId(-1);
                matriculaTurma.setId(-1);
                matriculaIndividual.setId(-1);
            }
        } else {
            if (fil.getId() != matriculaEscola.getFilial().getId()) {
                mensagem = "Registro não pode ser atualizado por esta filial!";
                return;
            }
            if (sv.alterarObjeto(matriculaEscola)) {
                if (tipoMatricula.equals("Turma")) {
                    setDesabilitaIndividual(true);
                    matriculaTurma.setMatriculaEscola(matriculaEscola);
                    if (!sv.alterarObjeto(matriculaTurma)) {
                        sv.desfazerTransacao();
                        mensagem = "Falha ao atualizar a matricula turma!";
                        return;
                    }
                } else {
                    setDesabilitaTurma(true);
                    matriculaIndividual.setMatriculaEscola(matriculaEscola);
                    if (!sv.alterarObjeto(matriculaIndividual)) {
                        sv.desfazerTransacao();
                        mensagem = "Falha ao atualizar a matricula individual!";
                        return;
                    }
                }
                sv.comitarTransacao();
                pessoaResponsavelMemoria = matriculaEscola.getResponsavel();
                pessoaAlunoMemoria = matriculaEscola.getAluno();
                mensagem = "Matrícula atualizada com sucesso.";
                target = "_blank";
                sv.fecharTransacao();
            } else {
                sv.desfazerTransacao();
            }
        }
    }

    public String editar(MatriculaEscola me) {
        escolaAutorizadosDetalhes = new EscolaAutorizados();
        getListaGridMEscola().clear();
        matriculaEscola = me;
        MatriculaEscolaDB matriculaEscolaDB = new MatriculaEscolaDBToplink();
        desabilitaCampo = true;
        idDiaVencimentoPessoa = 0;
        if (matriculaEscola.getEvt() != null) {
            desabilitaCamposMovimento = true;
            desabilitaDiaVencimento = true;
        }
        for (int i = 0; i < listaVendedor.size(); i++) {
            if (matriculaEscola.getVendedor().getId() == Integer.parseInt(listaVendedor.get(i).getDescription())) {
                idVendedor = i;
                break;
            }
        }
        for (int i = 0; i < listaMidia.size(); i++) {
            if (matriculaEscola.getMidia().getId() == Integer.parseInt(listaMidia.get(i).getDescription())) {
                idMidia = i;
                break;
            }
        }
        setValorString(matriculaEscola.getValorTotalString());
        if (porPesquisa.equals("matriculaIndividual")) {
            tipoMatricula = "Individual";
            matriculaIndividual = matriculaEscolaDB.pesquisaCodigoMIndividual(matriculaEscola.getId());
            desabilitaTurma = true;
            desabilitaIndividual = false;
            for (int i = 0; i < listaProfessor.size(); i++) {
                if (matriculaIndividual.getProfessor().getId() == Integer.parseInt(listaProfessor.get(i).getDescription())) {
                    idProfessor = i;
                    break;
                }
            }
            for (int i = 0; i < listaIndividual.size(); i++) {
                if (matriculaIndividual.getCurso().getId() == Integer.parseInt(listaIndividual.get(i).getDescription())) {
                    idIndividual = i;
                    break;
                }
            }
        } else {
            tipoMatricula = "Turma";
            matriculaTurma = matriculaEscolaDB.pesquisaCodigoMTurma(matriculaEscola.getId());
            turma = matriculaTurma.getTurma();
            for (int i = 0; i < listaTurma.size(); i++) {
                if (matriculaTurma.getTurma().getId() == turma.getId()) {
                    idTurma = i;
                    break;
                }
            }
            desabilitaTurma = false;
            desabilitaIndividual = true;
        }
        idFTipoDocumento = matriculaEscola.getTipoDocumento().getId();
        FisicaDB fisicaDB = new FisicaDBToplink();
        aluno = fisicaDB.pesquisaFisicaPorPessoa(matriculaEscola.getAluno().getId());
        if (aluno.getId() != -1) {
            getResponsavel();
            verificaSocio();
        }
        pegarIdServico();
        atualizaValor();
        calculaValorLiquido();
        pessoaResponsavelMemoria = matriculaEscola.getResponsavel();
        pessoaAlunoMemoria = matriculaEscola.getAluno();
        analisaResponsavel();
        GenericaSessao.put("linkClicado", true);
        return "matriculaEscola";
    }

    public void excluir() {
        if (matriculaEscola.getId() != -1) {
            SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
            if (existeMovimento()) {
                mensagem = "Não é possível excluir essa matrícula, já possui movimentos baixados!";
                matriculaEscola.setHabilitado(false);
                return;
            }
            MatriculaEscolaDB matriculaEscolaDB = new MatriculaEscolaDBToplink();
            if (matriculaEscolaDB.desfazerMovimento(matriculaEscola)) {
                listaMovimentos.clear();
                desabilitaCamposMovimento = false;
                db.abrirTransacao();
                if (tipoMatricula.equals("Individual")) {
                    if (!db.deletarObjeto((MatriculaIndividual) db.find(matriculaIndividual))) {
                        db.desfazerTransacao();
                        mensagem = "Falha ao excluir essa matrícula!";
                        return;
                    }
                } else {
                    if (!db.deletarObjeto((MatriculaTurma) db.find(matriculaTurma))) {
                        db.desfazerTransacao();
                        mensagem = "Falha ao excluir essa matrícula!";
                        return;
                    }
                }
                matriculaEscola = (MatriculaEscola) db.find(matriculaEscola);
                if (db.deletarObjeto(matriculaEscola)) {
                    db.comitarTransacao();
                    novo();
                    mensagem = "Matricula excluída com sucesso!";
                } else {
                    db.desfazerTransacao();
                    mensagem = "Falha ao excluir essa matrícula!";
                }
            } else {
                db.abrirTransacao();
                NovoLog novoLog = new NovoLog();
                String stringLogMatricula = "  ID Matricula: " + matriculaEscola.getId()
                        + " - Responsável: " + matriculaEscola.getResponsavel().getId() + " - " + matriculaEscola.getResponsavel().getNome()
                        + "       - Aluno: " + matriculaEscola.getAluno().getId() + " - " + matriculaEscola.getAluno().getNome() + " - ";
                if (tipoMatricula.equals("Individual")) {
                    if (!db.deletarObjeto((MatriculaIndividual) db.find(matriculaIndividual))) {
                        db.desfazerTransacao();
                        mensagem = "Falha ao excluir essa matrícula!";
                        return;
                    }
                    stringLogMatricula += "ID M. Individual: " + matriculaIndividual.getId()
                            + "         - Curso: " + matriculaIndividual.getCurso().getId() + " - " + matriculaIndividual.getCurso().getDescricao()
                            + "     - Professor: " + matriculaIndividual.getProfessor().getId() + " - " + matriculaIndividual.getProfessor().getProfessor().getNome();
                } else {
                    if (!db.deletarObjeto((MatriculaTurma) db.find(matriculaTurma))) {
                        db.desfazerTransacao();
                        mensagem = "Falha ao excluir essa matrícula!";
                        return;
                    }
                    stringLogMatricula += "  ID  M. Turma: " + matriculaTurma.getId()
                            + " -       Turma: " + matriculaTurma.getTurma().getCursos().getId() + " - " + matriculaTurma.getTurma().getCursos().getDescricao();
                }
                matriculaEscola = (MatriculaEscola) db.find(matriculaEscola);
                if (db.deletarObjeto(matriculaEscola)) {
                    db.comitarTransacao();
                    if (tipoMatricula.equals("Individual")) {
                        novoLog.novo("Excluir Matrícula Individual", stringLogMatricula);
                    } else {
                        novoLog.novo("Excluir Matrícula Turma", stringLogMatricula);
                    }
                    novo();
                    mensagem = "";
                    mensagem = "Matricula excluída com sucesso!";
                } else {
                    db.desfazerTransacao();
                    mensagem = "Falha ao excluir essa matrícula!";
                }
            }
        } else {
            mensagem = "Pesquisar registro a ser excluído!";
        }
    }

    public void calculaValorLiquido() {
        if (turma.getId() != -1) {
            valor = Moeda.substituiVirgula(valor);
            valorLiquido = "0";
            valorParcela = "0";
            valorParcelaVencimento = "0";
            int periodoMeses = 0;
            int periodoMesesRestantes = 0;
            float desconto = 0;
            if (!valor.isEmpty()) {
                if (!desabilitaTurma) {
                    if (matriculaTurma.getTurma().getId() == -1) {
                        periodoMeses = DataHoje.quantidadeMeses(turma.getDtInicio(), turma.getDtTermino()) + 1;
                        periodoMesesRestantes = DataHoje.quantidadeMeses(DataHoje.dataHoje(), turma.getDtTermino()) + 1;
                    } else {
                        periodoMeses = DataHoje.quantidadeMeses(matriculaTurma.getTurma().getDtInicio(), matriculaTurma.getTurma().getDtTermino()) + 1;
                        periodoMesesRestantes = DataHoje.quantidadeMeses(DataHoje.dataHoje(), matriculaTurma.getTurma().getDtTermino()) + 1;
                    }
                } else {
                    periodoMeses = DataHoje.quantidadeMeses(DataHoje.dataHoje(), matriculaTurma.getTurma().getDtTermino()) + 1;
                }
                if (periodoMeses != periodoMesesRestantes) {
                    showDescontoProporcional = true;
                } else {
                    showDescontoProporcional = false;
                }
                // Desconto proporcional = (valor integral do curso/numero de meses da turma) * numero de meses não frequentado a partir do inicio do curso
                if (descontoProporcional) {
                    if (periodoMesesRestantes > 1) {
                        desconto = Float.parseFloat(valor) - ((Float.parseFloat(valor) / periodoMeses) * periodoMesesRestantes);
                        matriculaEscola.setValorDescontoProporcional(Moeda.converteFloatR$Float(desconto));
                    }
                }
                if ((Float.parseFloat(valor) - matriculaEscola.getDesconto() - matriculaEscola.getDescontoAteVencimento()) > 0) {
                    valorLiquido = Moeda.converteR$Float(Float.parseFloat(valor) - matriculaEscola.getDesconto() - desconto - matriculaEscola.getDescontoAteVencimento());
                    valorParcela = Moeda.converteR$Float((Float.parseFloat(valor) - desconto - matriculaEscola.getDesconto()) / matriculaEscola.getNumeroParcelas());
                    valorParcelaVencimento = Moeda.converteR$Float((Float.parseFloat(valor) - desconto - matriculaEscola.getDesconto() - matriculaEscola.getDescontoAteVencimento()) / matriculaEscola.getNumeroParcelas());
                }
            }
            valor = Moeda.converteR$(valor);
        }
    }

    public void atualizaValor() {
        if (turma.getId() != -1) {
            FunctionsDB functionsDB = new FunctionsDBTopLink();
            valor = Float.toString(functionsDB.valorServico(aluno.getPessoa().getId(), idServico, DataHoje.dataHoje(), 0));
            matriculaEscola.setDescontoAteVencimento(functionsDB.valorServico(aluno.getPessoa().getId(), idServico, DataHoje.dataHoje(), 1));
            vTaxa = functionsDB.valorServico(aluno.getPessoa().getId(), idServico, DataHoje.dataHoje(), 2);
        }
    }

    public void updateGrid() {
        pegarIdServico();
        atualizaValor();
        calculaValorLiquido();
    }

    public void gerarMovimento() {
        if (matriculaEscola.getId() != -1) {
            if (matriculaEscola.getEvt() == null) {
                if (matriculaEscola.getAluno().getId() != pessoaAlunoMemoria.getId()) {
                    mensagem = "Salvar o novo aluno / responsável para gerar movimentos!";
                    GenericaMensagem.warn("Sistema", mensagem);
                    return;
                }
                if (matriculaEscola.getResponsavel().getId() != pessoaResponsavelMemoria.getId()) {
                    mensagem = "Salvar o novo aluno / responsável para gerar movimentos!";
                    GenericaMensagem.warn("Sistema", mensagem);
                    return;
                }
                Filial fil = getMacFilial().getFilial();
                if (fil.getId() != matriculaEscola.getFilial().getId()) {
                    mensagem = "Registro não pode ser atualizado por esta filial!";
                    GenericaMensagem.warn("Sistema", mensagem);
                    return;
                }
                String vencimento;
                String referencia;
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                Plano5 plano5;
                Servicos servicos;
                int idCondicaoPagto;
                if (matriculaEscola.getNumeroParcelas() == 1) {
                    idCondicaoPagto = 1;
                } else {
                    idCondicaoPagto = 2;
                }
                if (tipoMatricula.equals("Turma")) {
                    plano5 = matriculaTurma.getTurma().getCursos().getPlano5();
                    servicos = matriculaTurma.getTurma().getCursos();
                } else {
                    plano5 = matriculaIndividual.getCurso().getPlano5();
                    servicos = matriculaIndividual.getCurso();
                }
                FTipoDocumento fTipoDocumento = (FTipoDocumento) salvarAcumuladoDB.find("FTipoDocumento", matriculaEscola.getTipoDocumento().getId());
                setLote(
                        new Lote(
                                -1,
                                (Rotina) salvarAcumuladoDB.find("Rotina", 151),
                                "R",
                                DataHoje.data(),
                                matriculaEscola.getResponsavel(),
                                plano5,
                                false,
                                "",
                                matriculaEscola.getValorTotal(),
                                matriculaEscola.getFilial(),
                                null,
                                null,
                                "",
                                fTipoDocumento,
                                (CondicaoPagamento) salvarAcumuladoDB.find("CondicaoPagamento", idCondicaoPagto),
                                (FStatus) salvarAcumuladoDB.find("FStatus", 1),
                                null,
                                matriculaEscola.isDescontoFolha(), null));
                salvarAcumuladoDB.abrirTransacao();
                try {

                    String nrCtrBoletoResp = "";

                    for (int x = 0; x < (Integer.toString(matriculaEscola.getResponsavel().getId())).length(); x++) {
                        nrCtrBoletoResp += 0;
                    }

                    nrCtrBoletoResp += matriculaEscola.getResponsavel().getId();
                    String mesPrimeiraParcela = listaMesVencimento.get(idMesVencimento).getDescription();
                    String mes = mesPrimeiraParcela.substring(0, 2);
                    String ano = mesPrimeiraParcela.substring(3, 7);
//                    String mes = matriculaEscola.getDataMatriculaString().substring(3, 5);
//                    String ano = matriculaEscola.getDataMatriculaString().substring(6, 10);
                    referencia = mes + "/" + ano;

                    if (DataHoje.qtdeDiasDoMes(Integer.parseInt(mes), Integer.parseInt(ano)) >= matriculaEscola.getDiaVencimento()) {
                        if (matriculaEscola.getDiaVencimento() < 10) {
                            vencimento = "0" + matriculaEscola.getDiaVencimento() + "/" + mes + "/" + ano;
                        } else {
                            vencimento = matriculaEscola.getDiaVencimento() + "/" + mes + "/" + ano;
                        }
                    } else {
                        String diaSwap = Integer.toString(DataHoje.qtdeDiasDoMes(Integer.parseInt(mes), Integer.parseInt(ano)));
                        if (diaSwap.length() < 2) {
                            diaSwap = "0" + diaSwap;
                        }
                        vencimento = diaSwap + "/" + mes + "/" + ano;
                    }
                    boolean insereTaxa = false;
                    if (getTaxa() == true) {
                        insereTaxa = true;
                    }
                    Evt evt = new Evt();
                    if (!salvarAcumuladoDB.inserirObjeto(evt)) {
                        salvarAcumuladoDB.desfazerTransacao();
                        mensagem = "Não foi possível gerar esse movimento!";
                        GenericaMensagem.warn("Sistema", mensagem);
                        return;
                    }
                    lote.setEvt(evt);
                    if (salvarAcumuladoDB.inserirObjeto(lote)) {
                        int loop;
                        if (insereTaxa == true) {
                            loop = matriculaEscola.getNumeroParcelas() + 1;
                        } else {
                            loop = matriculaEscola.getNumeroParcelas();
                        }
                        String vecimentoString = "";
                        Pessoa pessoaAluno = matriculaEscola.getAluno();
                        Pessoa pessoaResponsavelTitular = matriculaEscola.getResponsavel();
                        Pessoa pessoaResponsavel;
                        FunctionsDB functionsDB = new FunctionsDBTopLink();
                        if (matriculaEscola.isDescontoFolha()) {
                            int idResponsavel = functionsDB.responsavel(pessoaAluno.getId(), matriculaEscola.isDescontoFolha());
                            if (idResponsavel != -1) {
                                pessoaResponsavel = (Pessoa) salvarAcumuladoDB.find(new Pessoa(), idResponsavel);
                            } else {
                                pessoaResponsavel = matriculaEscola.getResponsavel();
                            }
                        } else {
                            int idResponsavelEmpresa = functionsDB.responsavel(aluno.getPessoa().getId(), true);
                            if (idResponsavelEmpresa != -1) {
                                JuridicaDB juridicaDB = new JuridicaDBToplink();
                                Juridica juridicaB = juridicaDB.pesquisaJuridicaPorPessoa(idResponsavelEmpresa);
                                if (juridicaB.getId() != -1) {
                                    pessoaResponsavel = (Pessoa) salvarAcumuladoDB.find(new Pessoa(), idResponsavelEmpresa);
                                } else {
                                    pessoaResponsavel = pessoaResponsavelTitular;
                                }
                            } else {
                                pessoaResponsavel = pessoaResponsavelTitular;
                            }
                        }

                        if (pessoaResponsavel.getId() == -1) {
                            salvarAcumuladoDB.desfazerTransacao();
                            return;
                        }
                        int j = 0;
                        boolean isTx = false;
                        float valorParcelaF;
                        float valorDescontoAteVencimento;
                        for (int i = 0; i < loop; i++) {
                            TipoServico tipoServico;
                            if (insereTaxa == true) {
                                tipoServico = (TipoServico) salvarAcumuladoDB.find(new TipoServico(), 5);
                                valorParcelaF = vTaxa;
                                valorDescontoAteVencimento = 0;
                                vecimentoString = listaDataTaxa.get(idDataTaxa).getDescription();
                                isTx = insereTaxa;
                                insereTaxa = false;
                            } else {
                                tipoServico = (TipoServico) salvarAcumuladoDB.find(new TipoServico(), 1);
                                // ALTERADO: MUDAR EM FEVEREIRO
                                if (j == 0) {
//                                    ADICIONAR SOMENTE SE FOR DESCONTAR A TAXA DO VALOR DA PRIMEIRA MENSALIDADE
//                                    if (isTx) {
//                                        valorParcelaF = Moeda.substituiVirgulaFloat(getValorParcela()) - vTaxa;
//                                        if (matriculaEscola.getDescontoAteVencimento() > 0) {
//                                            valorDescontoAteVencimento = Moeda.divisaoValores(matriculaEscola.getDescontoAteVencimento(), (float) matriculaEscola.getNumeroParcelas()) - vTaxa;
//                                        } else {
//                                            valorDescontoAteVencimento = Moeda.divisaoValores(matriculaEscola.getDescontoAteVencimento(), (float) matriculaEscola.getNumeroParcelas());
//                                        }
//                                        isTx = false;
//                                    } else {
//                                    }
                                    valorParcelaF = Moeda.substituiVirgulaFloat(getValorParcela());
                                    valorDescontoAteVencimento = Moeda.divisaoValores(matriculaEscola.getDescontoAteVencimento(), (float) matriculaEscola.getNumeroParcelas());
                                } else {
                                    valorParcelaF = Moeda.substituiVirgulaFloat(getValorParcela());
                                    valorDescontoAteVencimento = Moeda.divisaoValores(matriculaEscola.getDescontoAteVencimento(), (float) matriculaEscola.getNumeroParcelas());
                                }
                                mes = vencimento.substring(3, 5);
                                ano = vencimento.substring(6, 10);
                                referencia = mes + "/" + ano;
                                if (j > 0) {
                                    vecimentoString = (new DataHoje()).incrementarMeses(j, vencimento);
                                } else {
                                    vecimentoString = vencimento;
                                }
                                j++;
                            }
                            String nrCtrBoleto = nrCtrBoletoResp + Long.toString(DataHoje.calculoDosDias(DataHoje.converte("07/10/1997"), DataHoje.converte(vencimento)));
                            if (valorParcelaF > 0) {
                                setMovimento(new Movimento(
                                        -1,
                                        lote,
                                        plano5,
                                        pessoaResponsavel, // EMPRESA DO RESPONSÁVEL (SE DESCONTO FOLHA) OU RESPONSÁVEL (SE NÃO FOR DESCONTO FOLHA)
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
                                        pessoaResponsavelTitular, // TITULAR / RESPONSÁVEL
                                        pessoaAluno, // BENEFICIÁRIO
                                        "",
                                        nrCtrBoleto,
                                        vencimento,
                                        valorDescontoAteVencimento,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        fTipoDocumento,
                                        0));
                            } else {
                                salvarAcumuladoDB.desfazerTransacao();
                                mensagem = "Não foi possível gerar esse movimento!";
                                GenericaMensagem.warn("Sistema", mensagem);
                                return;
                            }
                            if (!salvarAcumuladoDB.inserirObjeto(movimento)) {
                                salvarAcumuladoDB.desfazerTransacao();
                                mensagem = "Não foi possível gerar esse movimento!";
                                GenericaMensagem.warn("Sistema", mensagem);
                                return;
                            }
                        }
                        matriculaEscola.setEvt(evt);
                        matriculaEscola.setDescontoProporcional(descontoProporcional);
                        if (!descontoProporcional) {
                            matriculaEscola.setValorDescontoProporcional(0);
                        }
                        if (!salvarAcumuladoDB.alterarObjeto(matriculaEscola)) {
                            salvarAcumuladoDB.desfazerTransacao();
                            mensagem = "Não foi possível gerar esse movimento!";
                            GenericaMensagem.warn("Sistema", mensagem);
                            return;
                        }
                        salvarAcumuladoDB.comitarTransacao();
                        mensagem = "Movimentos gerados com sucesso";
                        GenericaMensagem.info("Sucesso", mensagem);
                        target = "_blank";
                        desabilitaCamposMovimento = true;
                        desabilitaDiaVencimento = true;
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                        mensagem = "Não foi possível gerar esse movimento!";
                        GenericaMensagem.warn("Sistema", mensagem);
                    }
                } catch (NumberFormatException e) {
                    salvarAcumuladoDB.desfazerTransacao();
                }
            } else {
                mensagem = "Esse movimento já foi gerado!";
                GenericaMensagem.warn("Sistema", mensagem);
            }
        } else {
            mensagem = "Pesquisar aluno!";
            GenericaMensagem.warn("Sistema", mensagem);
        }
    }

    public void desfazerMovimento() {
        if (matriculaEscola.getId() != -1) {
            if (matriculaEscola.getEvt() != null) {
                if (existeMovimento()) {
                    mensagem = "Movimento já possui baixa, não pode ser cancelado!";
                    GenericaMensagem.warn("Sistema", mensagem);
                    return;
                }
                MatriculaEscolaDB matriculaEscolaDB = new MatriculaEscolaDBToplink();
                if (matriculaEscolaDB.desfazerMovimento(matriculaEscola)) {
                    listaMovimentos.clear();
                    desabilitaCamposMovimento = false;
                    bloqueiaComboDiaVencimento();
                    mensagem = "Transação desfeita com sucesso";
                    GenericaMensagem.info("Sucesso", mensagem);
                } else {
                    mensagem = "Falha ao desfazer essa transação!";
                    GenericaMensagem.warn("Erro", mensagem);
                }
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                matriculaEscola = (MatriculaEscola) salvarAcumuladoDB.find(matriculaEscola);
            }
        }
    }

    public MatriculaIndividual getMatriculaIndividual() {
        return matriculaIndividual;
    }

    public void setMatriculaIndividual(MatriculaIndividual matriculaIndividual) {
        this.matriculaIndividual = matriculaIndividual;
    }

    public MatriculaTurma getMatriculaTurma() {
        return matriculaTurma;
    }

    public void setMatriculaTurma(MatriculaTurma matriculaTurma) {
        this.matriculaTurma = matriculaTurma;
    }

    public Fisica getAluno() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            MatriculaEscolaDB matriculaEscolaDB = new MatriculaEscolaDBToplink();
            if (GenericaSessao.exists("pesquisaFisicaTipo")) {
                String tipoFisica = GenericaSessao.getString("pesquisaFisicaTipo");
                if (tipoFisica.equals("aluno")) {
                    GenericaSessao.remove("pesquisaFisicaTipo");
                    valorTaxa = "";
                    taxa = false;
                    aluno = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
                    if (matriculaEscola.getAluno().getId() == -1) {
                        pessoaAlunoMemoria = aluno.getPessoa();
                    } else {
                        if (aluno.getPessoa().getId() != matriculaEscola.getAluno().getId()) {
                            pessoaAlunoMemoria = aluno.getPessoa();
                        }
                    }
                    if (aluno.getId() != -1) {
                        getResponsavel();
                        verificaSocio();
                    }
                    if (responsavel.getId() != -1) {
                        pessoaComplemento = new PessoaComplemento();
                        pessoaComplemento = matriculaEscolaDB.pesquisaDataRefPessoaComplemto(responsavel.getId());
                        if (pessoaComplemento != null) {
                            this.idDiaVencimentoPessoa = pessoaComplemento.getNrDiaVencimento();
                        }
                        matriculaEscola.setResponsavel(responsavel);
                    }
                    matriculaEscola.setAluno(aluno.getPessoa());
                    matriculaEscola.setResponsavel(responsavel);
                    atualizaPessoaComplemento(0);
                    pegarIdServico();
                    atualizaValor();
                    calculaValorLiquido();
                } else if (tipoFisica.equals("responsavel")) {
                    GenericaSessao.remove("pesquisaFisicaTipo");
                    Pessoa resp = ((Fisica) GenericaSessao.getObject("fisicaPesquisa", true)).getPessoa();
                    FunctionsDB functionsDB = new FunctionsDBTopLink();
                    int idade = functionsDB.idade("dt_nascimento", "current_date", resp.getId());
                    if (idade >= 18) {
                        if (matriculaEscolaDB.verificaPessoaEnderecoDocumento("fisica", resp.getId())) {
                            matriculaEscola.setResponsavel(resp);
                            atualizaPessoaComplemento(0);
                        }
                    } else {
                        GenericaMensagem.warn("Validação", "Responsável deve ser maior de idade!");
                    }
                    GenericaSessao.remove("juridicaPesquisa");
                }
            }
            analisaResponsavel();
        }
        return aluno;
    }

    public void setAluno(Fisica aluno) {
        this.aluno = aluno;
    }

    public Pessoa getResponsavel() {
        if (aluno.getId() != -1) {
            FunctionsDB functionsDB = new FunctionsDBTopLink();
            int titularResponsavel = functionsDB.responsavel(aluno.getPessoa().getId(), matriculaEscola.isDescontoFolha());
            if (titularResponsavel > 0) {
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                responsavel = (Pessoa) salvarAcumuladoDB.find(new Pessoa(), titularResponsavel);
                atualizaPessoaComplemento(1);
            }
        } else {
            responsavel = new Pessoa();
        }
        return responsavel;
    }

    public boolean verificaSeContribuinteInativo(Pessoa responsavelPessoa) {
        JuridicaDB juridicaDB = new JuridicaDBToplink();
        if (juridicaDB.empresaInativa(responsavelPessoa, "FECHOU")) {
            msgStatusEmpresa = "Empresa inátiva!";
            return true;
        }
        return false;
    }

    public void verificaDebitosResponsavel(Pessoa responsavelPessoa) {
        msgStatusDebito = "";
        setOcultaBotaoSalvar(false);
        if (responsavelPessoa.getId() != -1) {
            MovimentoDB movimentoDB = new MovimentoDBToplink();
            if (movimentoDB.existeDebitoPessoa(responsavelPessoa, null)) {
                msgStatusDebito = "Responsável possui débitos!";
                setOcultaBotaoSalvar(true);
            }
        }
    }

    public void descontoFolhaResponsavel() {
        getResponsavel();
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            List<EscStatus> list = (List<EscStatus>) sv.listaObjeto("EscStatus");
            for (int i = 0; i < list.size(); i++) {
                listaStatus.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listaStatus;
    }

    public void setListaStatus(List<SelectItem> listaStatus) {
        this.listaStatus = listaStatus;
    }

    public List<SelectItem> getListaVendedor() {
        if (listaVendedor.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            List<Vendedor> list = (List<Vendedor>) sv.listaObjeto("Vendedor", true);
            for (int i = 0; i < list.size(); i++) {
                listaVendedor.add(new SelectItem(i, list.get(i).getPessoa().getNome(), "" + list.get(i).getId()));
            }
        }
        return listaVendedor;
    }

    public void setListaVendedor(List<SelectItem> listaVendedor) {
        this.listaVendedor = listaVendedor;
    }

    public List<SelectItem> getListaProfessor() {
        if (listaProfessor.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            List<Professor> list = (List<Professor>) sv.listaObjeto("Professor", true);
            for (int i = 0; i < list.size(); i++) {
                listaProfessor.add(new SelectItem(i, list.get(i).getProfessor().getNome(), "" + list.get(i).getId()));
            }
        }
        return listaProfessor;
    }

    public void setListaProfessor(List<SelectItem> listaProfessor) {
        this.listaProfessor = listaProfessor;
    }

    public List<SelectItem> getListaMidia() {
        if (listaMidia.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            List<Midia> list = (List<Midia>) sv.listaObjeto("Midia", true);
            for (int i = 0; i < list.size(); i++) {
                listaMidia.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listaMidia;
    }

    public void setListaMidia(List<SelectItem> listaMidia) {
        this.listaMidia = listaMidia;
    }

    public List<SelectItem> getListaNumeros() {
        if (listaNumeros.isEmpty()) {
            for (int i = 1; i <= 12; i++) {
                listaNumeros.add(new SelectItem(Integer.toString(i)));
            }
        }
        return listaNumeros;
    }

    public void setListaNumeros(List<SelectItem> listaNumeros) {
        this.listaNumeros = listaNumeros;
    }

    public List<SelectItem> getListaDataVencimento() {
        if (listaDataVencimento.isEmpty()) {
            for (int i = 1; i <= 31; i++) {
                listaDataVencimento.add(new SelectItem(Integer.toString(i)));
            }
        }
        return listaDataVencimento;
    }

    public void setListaDataVencimento(List<SelectItem> listaDataVencimento) {
        this.listaDataVencimento = listaDataVencimento;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

    public int getIdIndividual() {
        return idIndividual;
    }

    public void setIdIndividual(int idIndividual) {
        this.idIndividual = idIndividual;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

    public int getIdMidia() {
        return idMidia;
    }

    public void setIdMidia(int idMidia) {
        this.idMidia = idMidia;
    }

    public int getIdDiaVencimento() {
        if (idDiaVencimentoPessoa == 0) {
            if (matriculaEscola.getId() == -1) {
                if (getRegistro() != null) {
                    this.idDiaVencimento = registro.getFinDiaVencimentoCobranca();
                } else {
                    this.idDiaVencimento = Integer.parseInt(DataHoje.data().substring(0, 2));
                }
            } else {
                this.idDiaVencimento = matriculaEscola.getDiaVencimento();
            }
        } else {
            this.idDiaVencimento = idDiaVencimentoPessoa;
        }
        return idDiaVencimento;
    }

    public void setIdDiaVencimento(int idDiaVencimento) {
        this.idDiaVencimento = idDiaVencimento;
    }

    public int getDiaVencimento() {
        if (idDiaVencimentoPessoa == 0) {
            if (matriculaEscola.getId() == -1) {
                this.diaVencimento = Integer.parseInt(DataHoje.data().substring(0, 2));
            } else {
                this.diaVencimento = matriculaEscola.getDiaVencimento();
            }
        } else {
            this.diaVencimento = idDiaVencimentoPessoa;
        }
        return this.diaVencimento;
    }

    public void setDiaVencimento(int diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public List<SelectItem> getListaIndividual() {
        if (listaIndividual.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List list = db.pesquisaTodos(151);
            for (int i = 0; i < list.size(); i++) {
                listaIndividual.add(new SelectItem((int) i,
                        (String) ((Servicos) list.get(i)).getDescricao(),
                        Integer.toString(((Servicos) list.get(i)).getId())));
            }
        }
        return listaIndividual;
    }

    public void setListaIndividual(List<SelectItem> listaIndividual) {
        this.listaIndividual = listaIndividual;
    }

    public String getValorParcelaVencimento() {
        return Moeda.converteR$(valorParcelaVencimento);
    }

    public void setValorParcelaVencimento(String valorParcelaVencimento) {
        this.valorParcelaVencimento = Moeda.substituiVirgula(valorParcelaVencimento);
    }

    public String getValorParcelaVencimentoString() {
        return Moeda.converteR$(valorParcelaVencimento);
    }

    public void setValorParcelaVencimentoString(String valorParcelaVencimento) {
        this.valorParcelaVencimento = Moeda.substituiVirgula(valorParcelaVencimento);
    }

    public String getValorParcela() {
        return Moeda.substituiVirgula(valorParcela);
    }

    public void setValorParcela(String valorParcela) {
        this.valorParcela = Moeda.substituiVirgula(valorParcela);
    }

    public String getValorLiquido() {
        return Moeda.substituiVirgula(valorLiquido);
    }

    public void setValorLiquido(String valorLiquido) {
        this.valorLiquido = Moeda.substituiVirgula(valorLiquido);
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValorString() {
        return Moeda.converteR$(valor);
    }

    public void setValorString(String valor) {
        this.valor = Moeda.substituiVirgula(valor);
    }

    public void validaHoraInicio() {
        matriculaIndividual.setInicio(DataHoje.validaHora(matriculaIndividual.getInicio()));
    }

    public void validaHoraFinal() {
        matriculaIndividual.setTermino(DataHoje.validaHora(matriculaIndividual.getTermino()));
    }

    public boolean isDesabilitaTurma() {
        return desabilitaTurma;
    }

    public void setDesabilitaTurma(boolean desabilitaTurma) {
        this.desabilitaTurma = desabilitaTurma;
    }

    public boolean isDesabilitaIndividual() {
        return desabilitaIndividual;
    }

    public void setDesabilitaIndividual(boolean desabilitaIndividual) {
        this.desabilitaIndividual = desabilitaIndividual;
    }

    public String getValorParcelaString() {
        return Moeda.converteR$(valorParcela);
    }

    public void setValorParcelaString(String valorParcela) {
        this.valorParcela = Moeda.substituiVirgula(valorParcela);
    }

    public void acaoPesquisaInicial() {
        setComoPesquisa("Inicial");
    }

    public void acaoPesquisaParcial() {
        setComoPesquisa("Parcial");
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricaoCurso() {
        return descricaoCurso;
    }

    public void setDescricaoCurso(String descricaoCurso) {
        this.descricaoCurso = descricaoCurso;
    }

    public List<ListaMatriculaEscola> getListaMatriculaEscolas() {
        if (!descricaoCurso.isEmpty() || !descricao.isEmpty()) {
            if (listaMatriculaEscolas.isEmpty()) {
                MatriculaEscolaDB dB = new MatriculaEscolaDBToplink();
                List<MatriculaEscola> list = dB.pesquisaMatriculaEscola(tipoMatricula, descricaoCurso, descricao, comoPesquisa, porPesquisa);
                MatriculaIndividual mIndividual = null;
                MatriculaTurma mTurma = null;
                for (MatriculaEscola listaMatriculaEscola : list) {
                    if (tipoMatricula.equals("Individual")) {
                        mIndividual = (MatriculaIndividual) dB.pesquisaCodigoMIndividual(listaMatriculaEscola.getId());
                        listaMatriculaEscolas.add(new ListaMatriculaEscola(listaMatriculaEscola, mIndividual, null, mIndividual.getCurso().getDescricao(), mIndividual.getDataInicioString() + " - " + mIndividual.getDataTerminoString()));
                    } else {
                        mTurma = dB.pesquisaCodigoMTurma(listaMatriculaEscola.getId());
                        listaMatriculaEscolas.add(new ListaMatriculaEscola(listaMatriculaEscola, null, mTurma, mTurma.getTurma().getCursos().getDescricao(), mTurma.getTurma().getDataInicio() + " - " + mTurma.getTurma().getDataTermino()));
                    }
                }
            }
        }
        return listaMatriculaEscolas;
    }

    public void setListaMatriculaEscolas(List<ListaMatriculaEscola> listaMatriculaEscolas) {
        this.listaMatriculaEscolas = listaMatriculaEscolas;
    }

    public List getListaGridMEscola() {
        return listaGridMEscola;
    }

    public void setListaGridMEscola(List listaGridMEscola) {
        this.listaGridMEscola = listaGridMEscola;
    }

    public MatriculaEscola getMatriculaEscola() {
        getAluno();
        if (socio == false) {
            getJuridica();
        }
        if (matriculaEscola.getFilial().getId() == -1) {
            getMacFilial();
            matriculaEscola.setFilial(macFilial.getFilial());
        }
        if (target.equals("#")) {
            if (matriculaEscola.getEvt() != null) {
                desabilitaGeracaoContrato = true;
                target = "_blank";
            } else {
                desabilitaGeracaoContrato = false;
            }
        }
        return matriculaEscola;
    }

    public void setMatriculaEscola(MatriculaEscola matriculaEscola) {
        this.matriculaEscola = matriculaEscola;
    }

    public MatriculaContrato getContratoEscola() {
        return matriculaContrato;
    }

    public void setContratoEscola(MatriculaContrato matriculaContrato) {
        this.matriculaContrato = matriculaContrato;
    }

    public String getOpenModal() {
        return openModal;
    }

    public void setOpenModal(String openModal) {
        this.openModal = openModal;
    }

    public boolean getTaxa() {
        return taxa;
    }

    public void setTaxa(boolean taxa) {
        this.taxa = taxa;
    }

    public void pegarIdServico() {
        if (tipoMatricula.equals("Individual")) {
            if (listaIndividual.isEmpty()) {
                getListaIndividual();
            }
            if (turma.getId() != -1) {
                idServico = turma.getCursos().getId();
            }
        } else {
            if (turma.getId() != -1) {
                idServico = turma.getCursos().getId();
            }
        }
    }

    public MacFilial getMacFilial() {
        macFilial = MacFilial.getAcessoFilial();
        msgStatusFilial = "";
        if (macFilial.getId() == -1) {
            msgStatusFilial = "Informar filial";
        }
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public String getMsgStatusFilial() {
        if (msgStatusFilial.equals("")) {
            getFilial();
        }
        return msgStatusFilial;
    }

    public void setMsgStatusFilial(String msgStatusFilial) {
        this.msgStatusFilial = msgStatusFilial;
    }

    public PessoaComplemento getPessoaComplemento() {
        return pessoaComplemento;
    }

    public void setPessoaComplemento(PessoaComplemento pessoaComplemento) {
        this.pessoaComplemento = pessoaComplemento;
    }

    public int getIdDiaVencimentoPessoa() {
        return idDiaVencimentoPessoa;
    }

    public void setIdDiaVencimentoPessoa(int idDiaVencimentoPessoa) {
        this.idDiaVencimentoPessoa = idDiaVencimentoPessoa;
    }

    public boolean isDesabilitaCampo() {
        return desabilitaCampo;
    }

    public void setDesabilitaCampo(boolean desabilitaCampo) {
        this.desabilitaCampo = desabilitaCampo;
    }

    public boolean isSocio() {
        return socio;
    }

    public void setSocio(boolean socio) {
        this.socio = socio;
    }

    public void verificaSocio() {
        SociosDB dB = new SociosDBToplink();
        Socios socios = dB.pesquisaSocioPorPessoa(aluno.getId());
        if (socios.getId() != -1) {
            setSocio(true);
        } else {
            setSocio(false);
        }
    }

    public void pesquisaFisica(String tipo) {
        if (GenericaSessao.exists("pesquisaFisicaTipo")) {
            GenericaSessao.remove("pesquisaFisicaTipo");
        }
        GenericaSessao.put("pesquisaFisicaTipo", tipo);
    }

    public void cobrarTaxa() {
        if (taxa == true) {
            this.taxa = true;
            this.valorTaxa = Moeda.converteR$Float(vTaxa);
        } else {
            this.taxa = false;
            this.valorTaxa = "";
        }
        idDataTaxa = 0;
        idMesVencimento = 0;
        listaDataTaxa.clear();
        listaMesVencimento.clear();
        getListaDataTaxa();
        getListaMesVencimento();
    }

    public String getValorTaxa() {
        return valorTaxa;
    }

    public void setValorTaxa(String valorTaxa) {
        this.valorTaxa = valorTaxa;
    }

    public String getValorTaxaString() {
        return Moeda.substituiVirgula(valorTaxa);
    }

    public void setValorTaxaString(String valorTaxa) {
        this.valorTaxa = Moeda.substituiVirgula(valorTaxa);
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public List<Movimento> getListaMovimentos() {
        if (listaMovimentos.isEmpty()) {
            if (matriculaEscola.getId() != -1) {
                int count = 0;
                if (matriculaEscola.getEvt() != null) {
                    MovimentoDB movimentoDB = new MovimentoDBToplink();
                    LoteDB loteDB = new LoteDBToplink();
                    lote = (Lote) loteDB.pesquisaLotePorEvt(matriculaEscola.getEvt());
                    listaMovimentos = movimentoDB.listaMovimentosDoLote(lote.getId());
                    for (Movimento listaMovimento : listaMovimentos) {
                        if (listaMovimento.getTipoServico().getId() == 5) {
                            setTaxa(true);
                            valorTaxa = Moeda.converteR$Float(listaMovimento.getValor());
                            listaMovimento.setQuantidade(0);
                        } else {
                            count++;
                            listaMovimento.setQuantidade(count);
                        }
                    }
                    lote = new Lote();
                }
            }
        }
        return listaMovimentos;
    }

    public void setListaMovimentos(List<Movimento> listaMovimentos) {
        this.listaMovimentos = listaMovimentos;
    }

    public boolean isHabilitaGerarParcelas() {
        habilitaGerarParcelas = listaMovimentos.isEmpty();
        return habilitaGerarParcelas;
    }

    public void setHabilitaGerarParcelas(boolean habilitaGerarParcelas) {
        this.habilitaGerarParcelas = habilitaGerarParcelas;
    }

    public boolean isDesabilitaCamposMovimento() {
        return desabilitaCamposMovimento;
    }

    public void setDesabilitaCamposMovimento(boolean desabilitaCamposMovimento) {
        this.desabilitaCamposMovimento = desabilitaCamposMovimento;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isDesabilitaGeracaoContrato() {
        return desabilitaGeracaoContrato;
    }

    public void setDesabilitaGeracaoContrato(boolean desabilitaGeracaoContrato) {
        this.desabilitaGeracaoContrato = desabilitaGeracaoContrato;
    }

    public boolean existeMovimento() {
        if (matriculaEscola.getEvt() != null) {
            MovimentoDB movimentoDB = new MovimentoDBToplink();
            if (!((List) movimentoDB.movimentosBaixadosPorEvt(matriculaEscola.getEvt().getId())).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public String getTipoMatricula() {
        return tipoMatricula;
    }

    public void setTipoMatricula(String tipoMatricula) {
        this.tipoMatricula = tipoMatricula;
    }

    public void tipoMatriculaPor(TabChangeEvent event) {
        tipoMatricula = event.getTab().getTitle();
        updateGrid();
    }

    public boolean isDesabilitaDescontoFolha() {
        return desabilitaDescontoFolha;
    }

    public void setDesabilitaDescontoFolha(boolean desabilitaDescontoFolha) {
        this.desabilitaDescontoFolha = desabilitaDescontoFolha;
    }

    public List<FTipoDocumento> getListaFTipoDocumento() {
        if (listaFTipoDocumento.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaFTipoDocumento = (List<FTipoDocumento>) salvarAcumuladoDB.pesquisaObjeto(new int[]{13, 2}, "FTipoDocumento");
        }
        return listaFTipoDocumento;
    }

    public void setListaFTipoDocumento(List<FTipoDocumento> listaFTipoDocumento) {
        this.listaFTipoDocumento = listaFTipoDocumento;
    }

    public int getIdFTipoDocumento() {
        return idFTipoDocumento;
    }

    public void setIdFTipoDocumento(int idFTipoDocumento) {
        this.idFTipoDocumento = idFTipoDocumento;
    }

    public boolean isDesabilitaDiaVencimento() {
        return desabilitaDiaVencimento;
    }

    public void setDesabilitaDiaVencimento(boolean desabilitaDiaVencimento) {
        this.desabilitaDiaVencimento = desabilitaDiaVencimento;
    }

    public void bloqueiaComboDiaVencimento() {
        if (idFTipoDocumento == 2) {
            desabilitaDiaVencimento = true;
        } else if (idFTipoDocumento == 13) {
            desabilitaDiaVencimento = false;
        }
    }

    public boolean isOcultaDescontoFolha() {
        return ocultaDescontoFolha;
    }

    public void setOcultaDescontoFolha(boolean ocultaDescontoFolha) {
        this.ocultaDescontoFolha = ocultaDescontoFolha;
    }

    public Juridica getJuridica() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
            MatriculaEscolaDB matriculaEscolaDB = new MatriculaEscolaDBToplink();
            if (matriculaEscolaDB.verificaPessoaEnderecoDocumento("juridica", juridica.getPessoa().getId())) {
                responsavel = juridica.getPessoa();
                if (responsavel.getId() != -1) {
                    atualizaPessoaComplemento(1);
                    pessoaComplemento = new PessoaComplemento();
                    pessoaComplemento = matriculaEscolaDB.pesquisaDataRefPessoaComplemto(responsavel.getId());
                    if (pessoaComplemento != null) {
                        this.idDiaVencimentoPessoa = pessoaComplemento.getNrDiaVencimento();
                    }
                    matriculaEscola.setResponsavel(juridica.getPessoa());
                }
                setOcultaDescontoFolha(false);
                desabilitaDescontoFolha = false;
                pegarIdServico();
                atualizaValor();
                calculaValorLiquido();
            }
            juridica = new Juridica();
            analisaResponsavel();
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    /**
     * Tipo : 0 => Aluno / 1 => Responsável
     *
     * @param tipo
     */
    public void atualizaPessoaComplemento(int tipo) {
        PessoaDB pdb = new PessoaDBToplink();
        PessoaComplemento pc;
        Pessoa p;
        if (tipo == 0) {
            p = matriculaEscola.getResponsavel();
        } else {
            p = responsavel;
        }
        pc = pdb.pesquisaPessoaComplementoPorPessoa(p.getId());
        if (pc.getId() == -1) {
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            pc.setNrDiaVencimento(getRegistro().getFinDiaVencimentoCobranca());
            pc.setCobrancaBancaria(true);
            pc.setPessoa(p);
            sadb.abrirTransacao();
            if (sadb.inserirObjeto(pc)) {
                sadb.comitarTransacao();
            } else {
                sadb.desfazerTransacao();
            }
        }
    }

    public boolean isOcultaBotaoSalvar() {
        return ocultaBotaoSalvar;
    }

    public void setOcultaBotaoSalvar(boolean ocultaBotaoSalvar) {
        this.ocultaBotaoSalvar = ocultaBotaoSalvar;
    }

    public String getMsgStatusDebito() {
        return msgStatusDebito;
    }

    public void setMsgStatusDebito(String msgStatusDebito) {
        this.msgStatusDebito = msgStatusDebito;
    }

    public Juridica getEmpresa() {
        FunctionsDB functionsDB = new FunctionsDBTopLink();
        int titular = functionsDB.responsavel(matriculaEscola.getResponsavel().getId(), true);
        if (titular > 0) {
            JuridicaDB juridicaDB = new JuridicaDBToplink();
            Juridica juridicaB = juridicaDB.pesquisaJuridicaPorPessoa(titular);
            if (juridicaB != null) {
                empresa = juridicaB;
            }

        }
        return empresa;
    }

    public void setEmpresa(Juridica empresa) {
        this.empresa = empresa;
    }

    public String getMsgStatusEmpresa() {
        return msgStatusEmpresa;
    }

    public void setMsgStatusEmpresa(String msgStatusEmpresa) {
        this.msgStatusEmpresa = msgStatusEmpresa;
    }

    public int getVagasDisponiveis() {
        if (turma.getId() != -1) {
            FunctionsDB functionsDB = new FunctionsDBTopLink();
            setVagasDisponiveis(functionsDB.vagasEscolaTurma(turma.getId()));
        }
        return vagasDisponiveis;
    }

    public void setVagasDisponiveis(int vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public boolean isDescontoProporcional() {
        return descontoProporcional;
    }

    public void setDescontoProporcional(boolean descontoProporcional) {
        this.descontoProporcional = descontoProporcional;
    }

    public boolean isShowDescontoProporcional() {
        return showDescontoProporcional;
    }

    public void setShowDescontoProporcional(boolean showDescontoProporcional) {
        this.showDescontoProporcional = showDescontoProporcional;
    }

    public String getValorDescontoProporcional() {
        return valorDescontoProporcional;
    }

    public void setValorDescontoProporcional(String valorDescontoProporcional) {
        this.valorDescontoProporcional = valorDescontoProporcional;
    }

    public void analisaResponsavel() {
        if (matriculaEscola.getResponsavel().getId() != -1) {
            if (responsavel.getId() != matriculaEscola.getResponsavel().getId()) {
                pessoaResponsavelMemoria = responsavel;
            }
            if (matriculaEscola.getResponsavel().getTipoDocumento().getId() == 2) {
                desabilitaDescontoFolha = false;
                ocultaDescontoFolha = false;
                verificaSeContribuinteInativo(matriculaEscola.getResponsavel());
            } else {
                desabilitaDescontoFolha = true;
                ocultaDescontoFolha = true;
            }
            verificaDebitosResponsavel(matriculaEscola.getResponsavel());
        } else {
            pessoaResponsavelMemoria = responsavel;
        }
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public int getIdadeAluno() {
        if (matriculaEscola.getAluno().getId() != -1) {
            FunctionsDB fdb = new FunctionsDBTopLink();
            idadeAluno = fdb.idade("dt_nascimento", "current_date", matriculaEscola.getAluno().getId());
        }
        return idadeAluno;
    }

    public void setIdadeAluno(int idadeAluno) {
        this.idadeAluno = idadeAluno;
    }

    public List<SelectItem> getListaCursosDisponiveis() {
        if (listaCursosDisponiveis.isEmpty()) {
            TurmaDB dB = new TurmaDBToplink();
            List<Turma> list = (List<Turma>) dB.listaTurmaAtivaPorFilial(macFilial.getFilial().getId());
            int idCurso = 0;
            int j = 0;
            for (Turma list1 : list) {
                if (idCurso != list1.getCursos().getId()) {
                    idCurso = list1.getCursos().getId();
                    listaCursosDisponiveis.add(new SelectItem((int) j, (String) list1.getCursos().getDescricao(), Integer.toString(list1.getCursos().getId())));
                    j++;
                }
            }
        }
        return listaCursosDisponiveis;
    }

    public void setListaCursosDisponiveis(List<SelectItem> listaCursosDisponiveis) {
        this.listaCursosDisponiveis = listaCursosDisponiveis;
    }

    public int getIdCursosDisponiveis() {
        return idCursosDisponiveis;
    }

    public void setIdCursosDisponiveis(int idCursosDisponiveis) {
        this.idCursosDisponiveis = idCursosDisponiveis;
    }

    public List<Turma> getListaTurma() {
        if (listaTurma.isEmpty()) {
            if (!listaCursosDisponiveis.isEmpty()) {
                TurmaDB dB = new TurmaDBToplink();
                listaTurma = dB.listaTurmaAtivaPorFilialServico(macFilial.getFilial().getId(), Integer.parseInt(listaCursosDisponiveis.get(idCursosDisponiveis).getDescription()));
            }
        }
        return listaTurma;
    }

    public void setListaTurma(List<Turma> listaTurma) {
        this.listaTurma = listaTurma;
    }

    public void selecionaTurma(Turma t) {
        turma = t;
        pegarIdServico();
        atualizaValor();
        calculaValorLiquido();
    }

//    public static String UTF8toISO(String str) {
//        Charset utf8charset = Charset.forName("UTF-8");
//        Charset iso88591charset = Charset.forName("ISO-8859-1");
//        ByteBuffer inputBuffer = ByteBuffer.wrap(str.getBytes());
//        // decode UTF-8
//        CharBuffer data = utf8charset.decode(inputBuffer);
//        // encode ISO-8559-1
//        ByteBuffer outputBuffer = iso88591charset.encode(data);
//        byte[] outputData = outputBuffer.array();
//        return new String(outputData);
//    }
    public boolean isAlunoFoto() {
        if (matriculaEscola.getAluno().getId() != -1) {
            File file = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + matriculaEscola.getAluno().getId() + ".png"));
            alunoFoto = file.exists();
        }
        return alunoFoto;
    }

    public void setAlunoFoto(boolean alunoFoto) {
        this.alunoFoto = alunoFoto;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public void openDialog() {
        visibility = true;
    }

    public void close() {
        visibility = false;
        RequestContext.getCurrentInstance().execute("dgl_adicionar.hide()");
    }

    // PESSOAS AUTORIZADAS
    public void novaPessoaAutorizada() {
        escolaAutorizados = new EscolaAutorizados();
        listaEscolaAutorizadas.clear();
    }

    public void salvarPessoaAutorizada() {
        if (matriculaEscola.getId() != -1) {
            if (escolaAutorizados.getPessoa().getId() == -1) {
                GenericaMensagem.warn("Validação", "Pesquisar uma pessoa!");
                return;
            }
            for (EscolaAutorizados listaEscolaAutorizada : listaEscolaAutorizadas) {
                if (listaEscolaAutorizada.getPessoa().getId() == escolaAutorizados.getPessoa().getId()) {
                    GenericaMensagem.warn("Validação", "Pessoa já cadastrada!");
                    return;
                }
            }
            escolaAutorizados.setMatriculaEscola(matriculaEscola);
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            if (escolaAutorizados.getId() == -1) {
                dB.abrirTransacao();
                if (dB.inserirObjeto(escolaAutorizados)) {
                    dB.comitarTransacao();
                    escolaAutorizados = new EscolaAutorizados();
                    listaEscolaAutorizadas.clear();
                    GenericaMensagem.info("Sucesso", "Pessoa adicionada com sucesso");
                } else {
                    GenericaMensagem.warn("Erro", "Erro ao adicionar pessoa!");
                    dB.desfazerTransacao();
                }
            }
        }
    }

    public void detalhesAutorizados(EscolaAutorizados ea) {
        escolaAutorizadosDetalhes = ea;
    }

    public void excluirPessoaAutorizada(EscolaAutorizados ea) {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        dB.abrirTransacao();
        if (dB.deletarObjeto((EscolaAutorizados) dB.pesquisaObjeto(ea.getId(), "EscolaAutorizados"))) {
            dB.comitarTransacao();
            escolaAutorizados = new EscolaAutorizados();
            listaEscolaAutorizadas.clear();
            GenericaMensagem.info("Sucesso", "Pessoa excluída com sucesso");
        } else {
            GenericaMensagem.warn("Erro", "Erro ao excluir pessoa!");
            dB.desfazerTransacao();
        }
        PF.update("i_msg_autorizados");
    }

    public EscolaAutorizados getEscolaAutorizados() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            if (GenericaSessao.exists("pesquisaFisicaTipo")) {
                String tipoFisica = GenericaSessao.getString("pesquisaFisicaTipo");
                if (tipoFisica.equals("autorizada")) {
                    GenericaSessao.remove("pesquisaFisicaTipo");
                    escolaAutorizados.setPessoa(((Fisica) GenericaSessao.getObject("fisicaPesquisa", true)).getPessoa());
                }
            }
        }
        return escolaAutorizados;
    }

    public void setEscolaAutorizados(EscolaAutorizados escolaAutorizados) {
        this.escolaAutorizados = escolaAutorizados;
    }

    public List<EscolaAutorizados> getListaEscolaAutorizadas() {
        if (matriculaEscola.getId() != -1) {
            if (listaEscolaAutorizadas.isEmpty()) {
                MatriculaEscolaDB medb = new MatriculaEscolaDBToplink();
                listaEscolaAutorizadas = medb.listaPessoasAutorizas(matriculaEscola.getId());
            }
        }
        return listaEscolaAutorizadas;
    }

    public void setListaEscolaAutorizadas(List<EscolaAutorizados> listaEscolaAutorizadas) {
        this.listaEscolaAutorizadas = listaEscolaAutorizadas;
    }

    public EscolaAutorizados getEscolaAutorizadosDetalhes() {
        return escolaAutorizadosDetalhes;
    }

    public void setEscolaAutorizadosDetalhes(EscolaAutorizados escolaAutorizadosDetalhes) {
        this.escolaAutorizadosDetalhes = escolaAutorizadosDetalhes;
    }

    public boolean isAlterarPessoaComplemento() {
        if (matriculaEscola.getId() != -1) {
            if (matriculaEscola.getResponsavel().getId() != -1) {
                PessoaDB pessoaDB = new PessoaDBToplink();
                PessoaComplemento pc = pessoaDB.pesquisaPessoaComplementoPorPessoa(matriculaEscola.getResponsavel().getId());
                alterarPessoaComplemento = pc.getId() != -1;
            }
        }
        return alterarPessoaComplemento;
    }

    public void setAlterarPessoaComplemento(boolean alterarPessoaComplemento) {
        this.alterarPessoaComplemento = alterarPessoaComplemento;
    }

    public void updatePessoaComplemento() {
        if (matriculaEscola.getResponsavel().getId() != -1) {
            PessoaDB pessoaDB = new PessoaDBToplink();
            PessoaComplemento pc = pessoaDB.pesquisaPessoaComplementoPorPessoa(matriculaEscola.getResponsavel().getId());
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            pc.setNrDiaVencimento(diaVencimento);
            sadb.abrirTransacao();
            if (sadb.alterarObjeto(pc)) {
                matriculaEscola.setDiaVencimento(pc.getNrDiaVencimento());
                if (sadb.alterarObjeto(matriculaEscola)) {
                    sadb.comitarTransacao();
                    listaMatriculaEscolas.clear();
                } else {
                    sadb.desfazerTransacao();
                }
            } else {
                sadb.desfazerTransacao();
            }
        }
    }

    public Registro getRegistro() {
        if (registro.getId() == -1) {
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            registro = (Registro) sadb.pesquisaObjeto(1, "Registro");
        }
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public int getIdDataTaxa() {
        return idDataTaxa;
    }

    public void setIdDataTaxa(int idDataTaxa) {
        this.idDataTaxa = idDataTaxa;
    }

    public int getIdMesVencimento() {
        return idMesVencimento;
    }

    public void setIdMesVencimento(int idMesVencimento) {
        this.idMesVencimento = idMesVencimento;
    }

    public List<SelectItem> getListaDataTaxa() {
        if (taxa) {
            if (listaDataTaxa.isEmpty()) {
                idDataTaxa = 0;
                DataHoje dh = new DataHoje();
                String dataTaxa;
                for (int i = 0; i < 20; i++) {
                    dataTaxa = dh.incrementarDias(i, matriculaEscola.getDataMatriculaString());
                    listaDataTaxa.add(new SelectItem(i, dataTaxa, dataTaxa));
                    if (dataTaxa.equals(DataHoje.data())) {
                        idDataTaxa = i;
                    }
                }
            }
        }
        return listaDataTaxa;
    }

    public void setListaDataTaxa(List<SelectItem> listaDataTaxa) {
        this.listaDataTaxa = listaDataTaxa;
    }

    public List<SelectItem> getListaMesVencimento() {
        if (listaMesVencimento.isEmpty()) {
            boolean isTaxa = false;
            DataHoje dh = new DataHoje();
            String data = matriculaEscola.getDataMatriculaString();
            String mesAno;
            int iDtMr;
            int iDtVct;
            for (int i = 0; i < listaNumeros.size(); i++) {
                if (i > 0) {
                    data = dh.incrementarMeses(1, data);
                }
                if (!isTaxa) {
                    iDtMr = DataHoje.converteDataParaInteger(matriculaEscola.getDataMatriculaString());
                    iDtVct = DataHoje.converteDataParaInteger(data);
                    if (taxa) {
                        if (iDtVct > iDtMr) {
                            idMesVencimento = i;
                            isTaxa = true;
                        } else {
                            idMesVencimento = 0;
                        }
                    } else {
                        isTaxa = true;
                        idMesVencimento = 0;
                    }
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
}
