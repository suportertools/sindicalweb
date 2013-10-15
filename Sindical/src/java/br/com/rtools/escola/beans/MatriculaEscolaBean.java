package br.com.rtools.escola.beans;

import br.com.rtools.escola.*;
import br.com.rtools.associativo.Midia;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.escola.db.MatriculaContratoDB;
import br.com.rtools.escola.db.MatriculaContratoDBToplink;
import br.com.rtools.escola.db.MatriculaEscolaDB;
import br.com.rtools.escola.db.MatriculaEscolaDBToplink;
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
import br.com.rtools.financeiro.db.ServicoValorDB;
import br.com.rtools.financeiro.db.ServicoValorDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEmpresaDB;
import br.com.rtools.pessoa.db.PessoaEmpresaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.HtmlToPDF;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

@ManagedBean
@SessionScoped
public class MatriculaEscolaBean implements Serializable {

    private Filial filial = new Filial();
    private Fisica aluno = new Fisica();
    private Juridica juridica = new Juridica();
    private Lote lote = new Lote();
    private MatriculaEscola matriculaEscola = new MatriculaEscola();
    private MatriculaContrato matriculaContrato = new MatriculaContrato();
    private MatriculaIndividual matriculaIndividual = new MatriculaIndividual();
    private MatriculaTurma matriculaTurma = new MatriculaTurma();
    private Pessoa pessoaAlunoMemoria = new Pessoa();
    private Pessoa pessoaResponsavelMemoria = new Pessoa();
    private MacFilial macFilial = new MacFilial();
    private Movimento movimento = new Movimento();
    private Pessoa responsavel = new Pessoa();
    private PessoaComplemento pessoaComplemento = new PessoaComplemento();
    private List listaGridMEscola = new ArrayList();
    private List<SelectItem> listaStatus = new ArrayList<SelectItem>();
    private List<Movimento> listaMovimentos = new ArrayList<Movimento>();
    private List<FTipoDocumento> listaFTipoDocumento = new ArrayList<FTipoDocumento>();
    private List<SelectItem> listaVendedor = new ArrayList<SelectItem>();
    private List<SelectItem> listaProfessor = new ArrayList<SelectItem>();
    private List<SelectItem> listaMidia = new ArrayList<SelectItem>();
    private List<SelectItem> listaNumeros = new ArrayList<SelectItem>();
    private List<SelectItem> listaDataVencimento = new ArrayList<SelectItem>();
    private List<SelectItem> listaTurma = new ArrayList<SelectItem>();
    private List<SelectItem> listaIndividual = new ArrayList<SelectItem>();
    private List<MatriculaEscola> listaMatriculaEscolas = new ArrayList<MatriculaEscola>();
    private int idDiaVencimento = 0;
    private int idDiaVencimentoPessoa = 0;
    private int idFTipoDocumento = 0;
    private int idIndividual = 0;
    private int idMidia = 0;
    private int idTurma = -1;
    private int idStatus = 0;
    private int idVendedor = 0;
    private int idProfessor = 0;
    private int idServico = 0;
    private float vTaxa = 0;
    private boolean desabilitaTurma = false;
    private boolean desabilitaIndividual = false;
    private boolean desabilitaCamposMovimento = false;
    private boolean desabilitaGeracaoContrato = false;
    private boolean desabilitaDescontoFolha = true;
    private boolean desabilitaDiaVencimento = false;
    private boolean desabilitaCampo = false;
    private boolean responsavelNaoSocio = false;
    private boolean habilitaGerarParcelas = false;
    private boolean limpar = false;
    private boolean ocultaDescontoFolha = true;
    private boolean taxa = false;
    private boolean socio = false;
    private String msgConfirma = "";
    private String comoPesquisa = "";
    private String descricaoAluno = "";
    private String descricaoCurso = "";
    private String msgStatusFilial = "";
    private String openModal = "";
    private String porPesquisa = "";
    private String target = "#";
    private String tipoMatricula = "Turma";
    private String valor = "";
    private String valorParcela = "";
    private String valorParcelaVencimento = "";
    private String valorLiquido = "";
    private String valorTaxa = "";

    public String novo() {
        vTaxa = 0;
        pessoaAlunoMemoria = new Pessoa();
        pessoaResponsavelMemoria = new Pessoa();
        responsavel = new Pessoa();
        matriculaTurma = new MatriculaTurma();
        matriculaIndividual = new MatriculaIndividual();
        macFilial = new MacFilial();
        filial = new Filial();
        pessoaComplemento = new PessoaComplemento();
        movimento = new Movimento();
        matriculaEscola = new MatriculaEscola();
        lote = new Lote();
        setIdFTipoDocumento(0);
        socio = false;
        idDiaVencimentoPessoa = 0;
        aluno = new Fisica();
        idTurma = 0;
        idIndividual = 0;
        idStatus = 0;
        idVendedor = 0;
        idProfessor = 0;
        idMidia = 0;
        idDiaVencimento = 0;
        listaStatus.clear();
        listaVendedor.clear();
        listaProfessor.clear();
        listaMidia.clear();
        listaNumeros.clear();
        listaFTipoDocumento.clear();
        listaDataVencimento.clear();
        listaTurma.clear();
        listaIndividual.clear();
        listaMatriculaEscolas.clear();
        listaMovimentos.clear();
        desabilitaTurma = false;
        desabilitaIndividual = false;
        desabilitaCamposMovimento = false;
        desabilitaDescontoFolha = true;
        limpar = false;
        taxa = false;
        desabilitaCampo = false;
        taxa = false;
        porPesquisa = "";
        comoPesquisa = "";
        msgConfirma = "";
        valorTaxa = "";
        target = "#";
        valor = "";
        valorLiquido = "";
        valorParcela = "";
        valorParcelaVencimento = "";
        openModal = "";
        GenericaSessao.remove("matriculaEscolaPesquisa");
        GenericaSessao.remove("pesquisaFisicaTipo");
        GenericaSessao.remove("pesquisaFisica");
        GenericaSessao.remove("pesquisaJuridica");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("fisicaPesquisa");
        return "matriculaEscola";
    }

    public void gerarContrato() throws IOException {
        if (matriculaEscola.getEvt() == null) {
            GenericaMensagem.warn("Sistema", "Necessário gerar movimento para imprimir esse contrato!");
            return;
        }
        if (matriculaEscola.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            Turma turma = new Turma();
            String contratoCurso;
            String contratoDiaSemana = "";
            MatriculaContratoDB dB = new MatriculaContratoDBToplink();
            if (tipoMatricula.equals("Individual")) {
                matriculaContrato = dB.pesquisaCodigoServico(matriculaIndividual.getCurso().getId());
            } else {
                matriculaContrato = dB.pesquisaCodigoServico(matriculaTurma.getTurma().getCursos().getId());
            }
            if (matriculaContrato == null) {
                msgConfirma = "Não é possível gerar um contrato para este serviço. Para gerar um contrato acesse: Menu Escola > Suporte > Modelo Contrato.";
                GenericaMensagem.warn("Sistema", msgConfirma);
                return;
            }
            String horaInicial; 
            String horaFinal; 
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$aluno", matriculaEscola.getAluno().getNome()));
            FisicaDB fisicaDB = new FisicaDBToplink();
            Fisica contratoFisica = fisicaDB.pesquisaFisicaPorPessoa(getResponsavel().getId());
            List listaDiaSemana = new ArrayList();
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
            } else {
                turma = (Turma) salvarAcumuladoDB.pesquisaCodigo(matriculaTurma.getTurma().getId(), "Turma");
                contratoCurso = matriculaTurma.getTurma().getCursos().getDescricao();
                if (turma.isSegunda()) {
                    listaDiaSemana.add("Seg");
                }
                if (turma.isTerca()) {
                    listaDiaSemana.add("Ter");
                }
                if (turma.isQuarta()) {
                    listaDiaSemana.add("Qua");
                }
                if (turma.isQuinta()) {
                    listaDiaSemana.add("Qui");
                }
                if (turma.isSexta()) {
                    listaDiaSemana.add("Sex");
                }
                if (turma.isSabado()) {
                    listaDiaSemana.add("Sab");
                }
                if (turma.isDomingo()) {
                    listaDiaSemana.add("Dom");
                }
                horaInicial = matriculaTurma.getTurma().getHoraInicio();
                horaFinal = matriculaTurma.getTurma().getHoraTermino();
            }
            for (int i = 0; i < listaDiaSemana.size(); i++) {
                if (i == 0) {
                    contratoDiaSemana = listaDiaSemana.get(i).toString();
                } else {
                    contratoDiaSemana += " , " + listaDiaSemana.get(i).toString();
                }
            }
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            PessoaEndereco pessoaEnderecoAluno =        (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoa(matriculaEscola.getAluno().getId());
            if (pessoaEnderecoAluno != null) {
                
            }
            PessoaEndereco pessoaEnderecoResponsavel =  (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoa(matriculaEscola.getResponsavel().getId());
            
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cpfAluno", matriculaEscola.getAluno().getDocumento()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$rgAluno", aluno.getRg()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$responsavel", getResponsavel().getNome()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cpfResponsavel", getResponsavel().getDocumento()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$rgResponsavel", contratoFisica.getRg()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$curso", contratoCurso));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$diaSemana", contratoDiaSemana));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataInicial", turma.getDataInicio()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataFinal", turma.getDataTermino()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataExtenso", DataHoje.dataExtenso(DataHoje.data())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$valorParcela", Moeda.converteR$Float(matriculaEscola.getValorTotal() / matriculaEscola.getNumeroParcelas())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$parcelas", Integer.toString(matriculaEscola.getNumeroParcelas())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$diaVencimento", Integer.toString(matriculaEscola.getDiaVencimento())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$valorAteVencimento", Moeda.converteR$Float((matriculaEscola.getValorTotal() - matriculaEscola.getDescontoAteVencimento()) / matriculaEscola.getNumeroParcelas())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$horaInicial", horaInicial));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$horaFinal", horaFinal));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$valorTotal", Moeda.converteR$Float((matriculaEscola.getValorTotal()))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$taxa", Moeda.converteR$Float(Float.parseFloat(valorTaxa))));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$matricula", Integer.toString(matriculaEscola.getId())));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$ano", "definir ano"));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$enderecoAluno", pessoaEnderecoAluno.getEndereco().getEnderecoSimplesToString())+", "+pessoaEnderecoAluno.getNumero());
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$bairroAluno", pessoaEnderecoAluno.getEndereco().getBairro().getDescricao()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cidadeAluno", pessoaEnderecoAluno.getEndereco().getCidade().getCidade()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$estadoAluno", pessoaEnderecoAluno.getEndereco().getCidade().getUf()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cepAluno", pessoaEnderecoAluno.getEndereco().getCep()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$emailAluno", matriculaEscola.getAluno().getEmail1()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$enderecoResponsavel", pessoaEnderecoResponsavel.getEndereco().getEnderecoSimplesToString())+", "+pessoaEnderecoAluno.getNumero());
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$bairroResponsavel", pessoaEnderecoResponsavel.getEndereco().getBairro().getDescricao()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cidadeResponsavel", pessoaEnderecoResponsavel.getEndereco().getCidade().getCidade()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$estadoResponsavel", pessoaEnderecoResponsavel.getEndereco().getCidade().getUf()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cepResponsavel", pessoaEnderecoResponsavel.getEndereco().getCep()));
            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$emailResponsavel", matriculaEscola.getResponsavel().getEmail1()));
            try {
                File dirFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/"));
                if (!dirFile.exists()) {
                    boolean success = dirFile.mkdir();
                    if (!success) {
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
                    Registro reg = (Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro");
                    String linha = reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/" + fileName;
                    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                    response.sendRedirect(linha);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    public void limpar() {
        if (limpar == true) {
            novo();
        }
    }

    public String salvar() {
        if (existeMovimento()) {
            msgConfirma = "Não é possível atualizar essa matrícula, já possui movimentos baixados!";
            GenericaMensagem.warn("Sistema", msgConfirma);
            return null;
        }
        int idPessoa = matriculaEscola.getResponsavel().getId();
        if (matriculaEscola.getAluno().getId() == -1) {
            msgConfirma = "Informar nome do aluno e do responsável!";
            GenericaMensagem.warn("Sistema", msgConfirma);
            return null;
        }
        if (matriculaEscola.getFilial().getId() == -1) {
            msgConfirma = "Informar a filial! Obs: Necessário acessar o sistema usando autênticação.";
            GenericaMensagem.warn("Sistema", msgConfirma);
            return null;
        }
        if (listaStatus.isEmpty()) {
            msgConfirma = "Informar a situação/status do aluno!";
            GenericaMensagem.warn("Sistema", msgConfirma);
            return null;
        }
        if (listaVendedor.isEmpty()) {
            msgConfirma = "Informar o nome do vendedor!";
            GenericaMensagem.warn("Sistema", msgConfirma);
            return null;
        }
        if (listaMidia.isEmpty()) {
            msgConfirma = "Informar o tipo de mídia!";
            GenericaMensagem.warn("Sistema", msgConfirma);
            return null;
        }
        if (tipoMatricula.equals("Individual")) {
            if (matriculaEscola.getId() == -1) {
                if (matriculaIndividual.getDataInicio() == null) {
                    msgConfirma = "Informar a data inicial!";
                    GenericaMensagem.warn("Sistema", msgConfirma);
                    return null;
                }
                if (matriculaIndividual.getDataTermino() == null) {
                    msgConfirma = "Informar a data de término!";
                    GenericaMensagem.warn("Sistema", msgConfirma);
                    return null;
                }
                int dataInicioInteger = DataHoje.converteDataParaInteger(matriculaIndividual.getDataInicioString());
                int dataFinalInteger = DataHoje.converteDataParaInteger(matriculaIndividual.getDataTerminoString());
                int dataHojeInteger = DataHoje.converteDataParaInteger(DataHoje.converteData(DataHoje.dataHoje()));
                if (dataInicioInteger < dataHojeInteger) {
                    msgConfirma = "A data inicial do curso deve ser maior ou igual a data de hoje!";
                    GenericaMensagem.warn("Sistema", msgConfirma);
                    return null;
                }
                if (dataFinalInteger < dataHojeInteger) {
                    msgConfirma = "A data final do curso deve ser maior ou igual a data de hoje!";
                    GenericaMensagem.warn("Sistema", msgConfirma);
                    return null;
                }
                if (dataFinalInteger < dataInicioInteger) {
                    msgConfirma = "A data final deve ser maior ou igual a data inicial!";
                    GenericaMensagem.warn("Sistema", msgConfirma);
                    return null;
                }
                if (DataHoje.validaHora(matriculaIndividual.getInicio()).isEmpty()) {
                    msgConfirma = "Hora inicial invalida!";
                    GenericaMensagem.warn("Sistema", msgConfirma);
                    return null;
                }
                if (DataHoje.validaHora(matriculaIndividual.getTermino()).isEmpty()) {
                    msgConfirma = "Hora final invalida!";
                    GenericaMensagem.warn("Sistema", msgConfirma);
                    return null;
                }
            }
            if (listaIndividual.isEmpty()) {
                msgConfirma = "Informar curso!";
                GenericaMensagem.warn("Sistema", msgConfirma);
                return null;
            }
            if (listaProfessor.isEmpty()) {
                msgConfirma = "Informar o nome do professor! Caso não exista cadastre um professor.";
                GenericaMensagem.warn("Sistema", msgConfirma);
                return null;
            }
        } else {
            if (listaTurma.isEmpty()) {
                msgConfirma = "Informar a turma!";
                GenericaMensagem.warn("Sistema", msgConfirma);
                return null;
            }
        }
        NovoLog novoLog = new NovoLog();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        matriculaEscola.setEscStatus((EscStatus) sv.pesquisaCodigo(Integer.parseInt(listaStatus.get(idStatus).getDescription()), "EscStatus"));
        matriculaEscola.setVendedor((Vendedor) sv.pesquisaCodigo(Integer.parseInt(listaVendedor.get(idVendedor).getDescription()), "Vendedor"));
        matriculaEscola.setMidia((Midia) sv.pesquisaCodigo(Integer.parseInt(listaMidia.get(idMidia).getDescription()), "Midia"));
        for (int i = 0; i < listaFTipoDocumento.size(); i++) {
            if (listaFTipoDocumento.get(i).getId() == idFTipoDocumento) {
                matriculaEscola.setTipoDocumento(listaFTipoDocumento.get(i));
                break;
            }
        }
        if (tipoMatricula.equals("Individual")) {
            matriculaIndividual.setCurso((Servicos) sv.pesquisaCodigo(Integer.parseInt(listaIndividual.get(idIndividual).getDescription()), "Servicos"));
            matriculaIndividual.setProfessor((Professor) sv.pesquisaCodigo(Integer.parseInt(listaProfessor.get(idProfessor).getDescription()), "Professor"));
        } else if (tipoMatricula.equals("Turma")) {
            matriculaTurma.setTurma((Turma) sv.pesquisaCodigo(Integer.parseInt(listaTurma.get(idTurma).getDescription()), "Turma"));
        }
        matriculaEscola.setEsEFinanceiro(null);
        matriculaEscola.setValorTotalString(valor);
        matriculaEscola.setDiaVencimento(idDiaVencimento);
        sv.abrirTransacao();
        if (matriculaEscola.getId() == -1) {
            MatriculaEscolaDB matriculaEscolaDB = new MatriculaEscolaDBToplink();
            pessoaComplemento = matriculaEscolaDB.pesquisaDataRefPessoaComplemto(idPessoa);
            if (pessoaComplemento == null) {
                pessoaComplemento = new PessoaComplemento();
                pessoaComplemento.setNrDiaVencimento(idDiaVencimento);
                if (idFTipoDocumento == 2) {
                    pessoaComplemento.setCobrancaBancaria(true);
                } else {
                    pessoaComplemento.setCobrancaBancaria(false);
                }
                pessoaComplemento.setPessoa((Pessoa) sv.pesquisaCodigo(idPessoa, "Pessoa"));
                if (!sv.inserirObjeto(pessoaComplemento)) {
                    sv.desfazerTransacao();
                    msgConfirma = "Falha ao inserir pessoa complemento!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    return null;
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
                        msgConfirma = "Não existem mais vagas disponíveis para essa turma!";
                        GenericaMensagem.warn("Validação", msgConfirma);
                        return null;
                    }
                    matriculaTurma.setMatriculaEscola(matriculaEscola);
                    if (matriculaEscolaDB.existeMatriculaTurma(matriculaTurma)) {
                        sv.desfazerTransacao();
                        matriculaEscola.setId(-1);
                        matriculaTurma.setId(-1);
                        matriculaIndividual.setId(-1);
                        msgConfirma = "Aluno já cadastrado para essa turma!";
                        GenericaMensagem.warn("Validação", msgConfirma);
                        return null;
                    }
                    setDesabilitaIndividual(true);
                    if (!sv.inserirObjeto(matriculaTurma)) {
                        matriculaEscola.setId(-1);
                        matriculaTurma.setId(-1);
                        matriculaIndividual.setId(-1);
                        sv.desfazerTransacao();
                        msgConfirma = "Falha ao adicionar a matricula turma!";
                        GenericaMensagem.warn("Erro", msgConfirma);
                        return null;
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
                        msgConfirma = "Aluno já cadastrado para essa mátricula!";
                        GenericaMensagem.warn("Validação", msgConfirma);
                        return null;
                    }
                    if (!sv.inserirObjeto(matriculaIndividual)) {
                        matriculaEscola.setId(-1);
                        matriculaTurma.setId(-1);
                        matriculaIndividual.setId(-1);
                        sv.desfazerTransacao();
                        msgConfirma = "Falha ao adicionar esta matricula individual!";
                        GenericaMensagem.warn("Erro", msgConfirma);
                        return null;
                    }
                    tipoMatriculaLog = " - Curso: " + matriculaIndividual.getCurso().getId() + " - " + matriculaIndividual.getCurso().getDescricao()
                            + " - Professor: " + matriculaIndividual.getProfessor().getId() + " - " + matriculaIndividual.getProfessor().getProfessor()
                            + " - Período: " + matriculaIndividual.getDataInicioString() + " até " + matriculaIndividual.getDataTerminoString();
                }
                sv.comitarTransacao();
                desabilitaCampo = true;
                msgConfirma = "Matrícula efetuada com sucesso.";
                GenericaMensagem.info("Sucesso", msgConfirma);
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
                return null;
            } else {
                sv.desfazerTransacao();
                matriculaEscola.setId(-1);
                matriculaTurma.setId(-1);
                matriculaIndividual.setId(-1);
            }
        } else {
            Filial fil = getMacFilial().getFilial();
            if (fil.getId() != matriculaEscola.getFilial().getId()) {
                msgConfirma = "Registro não pode ser atualizado por esta filial!";
                GenericaMensagem.warn("Sistema", msgConfirma);
                return null;
            }
            if (sv.alterarObjeto(matriculaEscola)) {
                if (tipoMatricula.equals("Turma")) {
                    setDesabilitaIndividual(true);
                    matriculaTurma.setMatriculaEscola(matriculaEscola);
                    if (!sv.alterarObjeto(matriculaTurma)) {
                        sv.desfazerTransacao();
                        msgConfirma = "Falha ao atualizar a matricula turma!";
                        GenericaMensagem.warn("Erro", msgConfirma);
                        return null;
                    }
                } else {
                    setDesabilitaTurma(true);
                    matriculaIndividual.setMatriculaEscola(matriculaEscola);
                    if (!sv.alterarObjeto(matriculaIndividual)) {
                        sv.desfazerTransacao();
                        msgConfirma = "Falha ao atualizar a matricula individual!";
                        GenericaMensagem.warn("Erro", msgConfirma);
                        return null;
                    }
                }
                sv.comitarTransacao();
                pessoaResponsavelMemoria = matriculaEscola.getResponsavel();
                pessoaAlunoMemoria = matriculaEscola.getAluno();
                msgConfirma = "Matrícula atualizada com sucesso.";
                GenericaMensagem.info("Sucesso", msgConfirma);
                target = "_blank";
                return null;
            } else {
                sv.desfazerTransacao();
            }
        }
        return null;
    }

    public String editar(String indice) {
        getListaGridMEscola().clear();
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        setMatriculaEscola((MatriculaEscola) dB.pesquisaCodigo(Integer.parseInt(indice), "MatriculaEscola"));
        GenericaSessao.put("matriculaEscolaPesquisa", matriculaEscola);
        GenericaSessao.put("linkClicado", true);
        if (GenericaSessao.exists("urlRetorno")) {
            return (String) GenericaSessao.getString("urlRetorno", true);
        } else {
            return "matriculaEscola";
        }
    }

    public String excluir() {
        if (matriculaEscola.getId() != -1) {
            SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
            if (existeMovimento()) {
                msgConfirma = "Não é possível excluir essa matrícula, já possui movimentos baixados!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                matriculaEscola.setHabilitado(false);
                return null;
            }
            MatriculaEscolaDB matriculaEscolaDB = new MatriculaEscolaDBToplink();
            if (matriculaEscolaDB.desfazerMovimento(matriculaEscola)) {
                listaMovimentos.clear();
                desabilitaCamposMovimento = false;
                db.abrirTransacao();
                if (tipoMatricula.equals("Individual")) {
                    if (!db.deletarObjeto((MatriculaIndividual) db.pesquisaCodigo(matriculaIndividual.getId(), "MatriculaIndividual"))) {
                        db.desfazerTransacao();
                        msgConfirma = "Falha ao excluir essa matrícula!";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                        return null;
                    }
                } else {
                    if (!db.deletarObjeto((MatriculaTurma) db.pesquisaCodigo(matriculaTurma.getId(), "MatriculaTurma"))) {
                        db.desfazerTransacao();
                        msgConfirma = "Falha ao excluir essa matrícula!";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                        return null;
                    }
                }
                matriculaEscola = (MatriculaEscola) db.pesquisaCodigo(matriculaEscola.getId(), "MatriculaEscola");
                if (db.deletarObjeto(matriculaEscola)) {
                    db.comitarTransacao();
                    novo();
                    msgConfirma = "Matricula excluída com sucesso!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                    return null;
                } else {
                    db.desfazerTransacao();
                    msgConfirma = "Falha ao excluir essa matrícula!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                }
            } else {
                db.abrirTransacao();
                NovoLog novoLog = new NovoLog();
                String stringLogMatricula = "  ID Matricula: " + matriculaEscola.getId()
                        + " - Responsável: " + matriculaEscola.getResponsavel().getId() + " - " + matriculaEscola.getResponsavel().getNome()
                        + "       - Aluno: " + matriculaEscola.getAluno().getId() + " - " + matriculaEscola.getAluno().getNome() + " - ";
                if (tipoMatricula.equals("Individual")) {
                    if (!db.deletarObjeto((MatriculaIndividual) db.pesquisaCodigo(matriculaIndividual.getId(), "MatriculaIndividual"))) {
                        db.desfazerTransacao();
                        msgConfirma = "Falha ao excluir essa matrícula!";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                        return null;
                    }
                    stringLogMatricula += "ID M. Individual: " + matriculaIndividual.getId()
                            + "         - Curso: " + matriculaIndividual.getCurso().getId() + " - " + matriculaIndividual.getCurso().getDescricao()
                            + "     - Professor: " + matriculaIndividual.getProfessor().getId() + " - " + matriculaIndividual.getProfessor().getProfessor().getNome();
                } else {
                    if (!db.deletarObjeto((MatriculaTurma) db.pesquisaCodigo(matriculaTurma.getId(), "MatriculaTurma"))) {
                        db.desfazerTransacao();
                        msgConfirma = "Falha ao excluir essa matrícula!";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                        return null;
                    }
                    stringLogMatricula += "  ID  M. Turma: " + matriculaTurma.getId()
                            + " -       Turma: " + matriculaTurma.getTurma().getCursos().getId() + " - " + matriculaTurma.getTurma().getCursos().getDescricao();
                }
                matriculaEscola = (MatriculaEscola) db.pesquisaCodigo(matriculaEscola.getId(), "MatriculaEscola");
                if (db.deletarObjeto(matriculaEscola)) {
                    db.comitarTransacao();
                    if (tipoMatricula.equals("Individual")) {
                        novoLog.novo("Excluir Matrícula Individual", stringLogMatricula);
                    } else {
                        novoLog.novo("Excluir Matrícula Turma", stringLogMatricula);
                    }
                    novo();
                    msgConfirma = "Matricula excluída com sucesso!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                    return null;
                } else {
                    db.desfazerTransacao();
                    msgConfirma = "Falha ao excluir essa matrícula!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                }
            }
        } else {
            msgConfirma = "Pesquisar registro a ser excluído!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Validação", msgConfirma));
        }
        return null;
    }

    public void calculaValorLiquido() {
        valor = Moeda.substituiVirgula(valor);
        if (!valor.isEmpty()) {
            if ((Float.parseFloat(valor) - matriculaEscola.getDesconto() - matriculaEscola.getDescontoAteVencimento()) > 0) {
                valorLiquido = Moeda.converteR$Float(Float.parseFloat(valor) - matriculaEscola.getDesconto() - matriculaEscola.getDescontoAteVencimento());
                valorParcela = Moeda.converteR$Float((Float.parseFloat(valor) - matriculaEscola.getDesconto()) / matriculaEscola.getNumeroParcelas());
                valorParcelaVencimento = Moeda.converteR$Float((Float.parseFloat(valor) - matriculaEscola.getDesconto() - matriculaEscola.getDescontoAteVencimento()) / matriculaEscola.getNumeroParcelas());
            } else {
                valorLiquido = "0";
                valorParcela = "0";
                valorParcelaVencimento = "0";
            }
        } else {
            valorLiquido = "0";
            valorParcela = "0";
            valorParcelaVencimento = "0";
        }
        valor = Moeda.converteR$(valor);
    }

    public void atualizaValor() {
        ServicoValorDB servicosValorDB = new ServicoValorDBToplink();
        valor = Float.toString(servicosValorDB.funcaoValorServico(aluno.getPessoa().getId(), idServico, DataHoje.dataHoje(), 0));
        matriculaEscola.setDescontoAteVencimento(servicosValorDB.funcaoValorServico(aluno.getPessoa().getId(), idServico, DataHoje.dataHoje(), 1));
        vTaxa = servicosValorDB.funcaoValorServico(aluno.getPessoa().getId(), idServico, DataHoje.dataHoje(), 2);
    }

    public String gerarMovimento() {
        if (matriculaEscola.getId() != -1) {
            if (matriculaEscola.getEvt() == null) {
                if (matriculaEscola.getAluno().getId() != pessoaAlunoMemoria.getId()) {
                    msgConfirma = "Salvar o novo aluno / responsável para gerar movimentos!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                    return null;
                }
                if (matriculaEscola.getResponsavel().getId() != pessoaResponsavelMemoria.getId()) {
                    msgConfirma = "Salvar o novo aluno / responsável para gerar movimentos!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                    return null;
                }
                Filial fil = getMacFilial().getFilial();
                if (fil.getId() != matriculaEscola.getFilial().getId()) {
                    msgConfirma = "Registro não pode ser atualizado por esta filial!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                    return null;
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
                FTipoDocumento fTipoDocumento = (FTipoDocumento) salvarAcumuladoDB.pesquisaCodigo(matriculaEscola.getTipoDocumento().getId(), "FTipoDocumento");
                setLote(
                        new Lote(
                        -1,
                        (Rotina) salvarAcumuladoDB.pesquisaCodigo(151, "Rotina"),
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
                        (CondicaoPagamento) salvarAcumuladoDB.pesquisaCodigo(idCondicaoPagto, "CondicaoPagamento"),
                        (FStatus) salvarAcumuladoDB.pesquisaCodigo(1, "FStatus"),
                        null,
                        matriculaEscola.isDescontoFolha()));
                salvarAcumuladoDB.abrirTransacao();
                try {

                    String nrCtrBoletoResp = "";

                    for (int x = 0; x < (Integer.toString(matriculaEscola.getResponsavel().getId())).length(); x++) {
                        nrCtrBoletoResp += 0;
                    }

                    nrCtrBoletoResp += matriculaEscola.getResponsavel().getId();

                    String mes = matriculaEscola.getDataMatriculaString().substring(3, 5);
                    String ano = matriculaEscola.getDataMatriculaString().substring(6, 10);
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
                        msgConfirma = "Não foi possível gerar esse movimento!";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                        return null;
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
                        Pessoa pessoaTitularResponsavel = matriculaEscola.getResponsavel();
                        Pessoa pessoaResponsavel;
                        ServicoValorDB servico = new ServicoValorDBToplink();
                        SociosDB socioDB = new SociosDBToplink();
                        boolean isSocioMenor = false;
                        boolean isResponsavelOutraEmpresa = false;
                        if (aluno.getId() != -1) {
                            if (servico.pesquisaMaiorResponsavel(aluno.getPessoa().getId()) == 0) {
                                Socios socios = socioDB.pesquisaSocioDoDependente(aluno.getPessoa());
                                if (socios != null) {
                                    if (servico.pesquisaMaiorResponsavel(socios.getServicoPessoa().getPessoa().getId()) == 0) {
                                        pessoaTitularResponsavel = aluno.getPessoa();
                                    } else {
                                        isSocioMenor = true;
                                        pessoaTitularResponsavel = socios.getServicoPessoa().getPessoa();
                                    }
                                } else {
                                    if (pessoaTitularResponsavel.getId() == -1) {
                                        pessoaTitularResponsavel = aluno.getPessoa();
                                    }
                                }
                            } else {
                                pessoaTitularResponsavel = aluno.getPessoa();
                            }
                        }
                        PessoaEmpresaDB pessoaEmpresaDB = new PessoaEmpresaDBToplink();
                        Pessoa pe = (pessoaEmpresaDB.pesquisaPessoaEmpresaPorPessoa(matriculaEscola.getResponsavel().getId())).getJuridica().getPessoa();
                        if (pe.getId() == -1) {
                            isResponsavelOutraEmpresa = true;
                            pessoaTitularResponsavel = matriculaEscola.getResponsavel();
                        }
                        if (matriculaEscola.isDescontoFolha()) {
                            pessoaResponsavel = matriculaEscola.getResponsavel();
                        } else {
                            if (!isResponsavelOutraEmpresa) {
                                if (isSocioMenor) {
                                    pessoaResponsavel = (pessoaEmpresaDB.pesquisaPessoaEmpresaPorPessoa(pessoaTitularResponsavel.getId())).getJuridica().getPessoa();
                                    if (pessoaResponsavel.getId() == -1) {
                                        pessoaResponsavel = matriculaEscola.getResponsavel();
                                    }
                                } else {
                                    pessoaResponsavel = pessoaTitularResponsavel;
                                }
                            } else {
                                pessoaResponsavel = pessoaTitularResponsavel;
                                pessoaTitularResponsavel = matriculaEscola.getAluno();
                            }
                        }
                        Pessoa pessoaBeneficiario = matriculaEscola.getAluno();
                        for (int i = 0; i < loop; i++) {
                            float valorParcelaF;
                            float valorDescontoAteVencimento;
                            TipoServico tipoServico;
                            if (insereTaxa == true) {
                                tipoServico = (TipoServico) salvarAcumuladoDB.pesquisaCodigo(5, "TipoServico");
                                valorParcelaF = vTaxa;
                                valorDescontoAteVencimento = 0;
                                vecimentoString = vencimento;
                                vencimento = DataHoje.data();
                                insereTaxa = false;
                            } else {
                                tipoServico = (TipoServico) salvarAcumuladoDB.pesquisaCodigo(1, "TipoServico");
                                valorParcelaF = Moeda.substituiVirgulaFloat(getValorParcela());
                                valorDescontoAteVencimento = (matriculaEscola.getValorTotal() - matriculaEscola.getDescontoAteVencimento()) / matriculaEscola.getNumeroParcelas();
                                if (!vecimentoString.equals("")) {
                                    vencimento = vecimentoString;
                                    vecimentoString = "";
                                }
                                mes = vencimento.substring(3, 5);
                                ano = vencimento.substring(6, 10);
                                referencia = mes + "/" + ano;
                                vencimento = (new DataHoje()).incrementarMeses(1, vencimento);
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
                                        vencimento,
                                        1,
                                        true,
                                        "E",
                                        false,
                                        pessoaTitularResponsavel, // TITULAR / RESPONSÁVEL
                                        pessoaBeneficiario, // BENEFICIÁRIO
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
                                msgConfirma = "Não foi possível gerar esse movimento!";
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                                return null;
                            }
                            if (!salvarAcumuladoDB.inserirObjeto(movimento)) {
                                salvarAcumuladoDB.desfazerTransacao();
                                msgConfirma = "Não foi possível gerar esse movimento!";
                                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                                return null;
                            }
                        }
                        matriculaEscola.setEvt(evt);
                        if (!salvarAcumuladoDB.alterarObjeto(matriculaEscola)) {
                            salvarAcumuladoDB.desfazerTransacao();
                            msgConfirma = "Não foi possível gerar esse movimento!";
                            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                            return null;
                        }
                        salvarAcumuladoDB.comitarTransacao();
                        msgConfirma = "Movimentos gerados com sucesso";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                        desabilitaCamposMovimento = true;
                        desabilitaDiaVencimento = true;
                        return null;
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                        msgConfirma = "Não foi possível gerar esse movimento!";
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                        return null;
                    }
                } catch (Exception e) {
                    salvarAcumuladoDB.desfazerTransacao();
                    return null;
                }
            } else {
                msgConfirma = "Esse movimento já foi gerado!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                return null;
            }
        } else {
            msgConfirma = "Pesquisar aluno!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
            return null;
        }
    }

    public void desfazerMovimento() {
        if (matriculaEscola.getId() != -1) {
            if (matriculaEscola.getEvt() != null) {
                if (existeMovimento()) {
                    msgConfirma = "Movimento já possui baixa, não pode ser cancelado!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                    return;
                }
                MatriculaEscolaDB matriculaEscolaDB = new MatriculaEscolaDBToplink();
                if (matriculaEscolaDB.desfazerMovimento(matriculaEscola)) {
                    listaMovimentos.clear();
                    desabilitaCamposMovimento = false;
                    bloqueiaComboDiaVencimento();
                    msgConfirma = "Transação desfeita com sucesso";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                } else {
                    msgConfirma = "Falha ao desfazer essa transação!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                }
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                matriculaEscola = (MatriculaEscola) salvarAcumuladoDB.pesquisaCodigo(matriculaEscola.getId(), "MatriculaEscola");
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
                String tipoFisica = GenericaSessao.getString("pesquisaFisicaTipo", true);
                if (tipoFisica.equals("aluno")) {
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
                    pegarIdServico();
                    atualizaValor();
                    calculaValorLiquido();
                } else if (tipoFisica.equals("responsavel")) {
                    Pessoa resp = ((Fisica) GenericaSessao.getObject("fisicaPesquisa", true)).getPessoa();
                    if (matriculaEscolaDB.verificaPessoaEnderecoDocumento("fisica", resp.getId())) {
                        matriculaEscola.setResponsavel(resp);
                    }
                    GenericaSessao.remove("juridicaPesquisa");
                }
            }
            if (matriculaEscola.getResponsavel().getId() == -1) {
                pessoaResponsavelMemoria = responsavel;
            } else {
                if (responsavel.getId() != matriculaEscola.getResponsavel().getId()) {
                    pessoaResponsavelMemoria = responsavel;
                }
            }
        }
        return aluno;
    }

    public void setAluno(Fisica aluno) {
        this.aluno = aluno;
    }

    public Pessoa getResponsavel() {
        ServicoValorDB servico = new ServicoValorDBToplink();
        SociosDB socioDB = new SociosDBToplink();
        if (aluno.getId() != -1) {
            if (servico.pesquisaMaiorResponsavel(aluno.getPessoa().getId()) == 0) {
                Socios socios = socioDB.pesquisaSocioDoDependente(aluno.getPessoa());
                if (socios != null) {
                    if (servico.pesquisaMaiorResponsavel(socios.getServicoPessoa().getPessoa().getId()) == 0) {
                        responsavelNaoSocio = true;
                        responsavel = aluno.getPessoa();
                    } else {
                        responsavelNaoSocio = false;
                        responsavel = socios.getServicoPessoa().getPessoa();
                    }
                } else {
                    if (responsavel.getId() == -1) {
                        responsavelNaoSocio = false;
                        responsavel = aluno.getPessoa();
                    }
                }
            } else {
                responsavelNaoSocio = false;
                responsavel = aluno.getPessoa();
                desabilitaDescontoFolha = true;
            }
        } else {
            responsavel = new Pessoa();
        }
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            List list = sv.listaObjeto("EscStatus");
            for (int i = 0; i < list.size(); i++) {
                listaStatus.add(new SelectItem(new Integer(i),
                        (String) ((EscStatus) list.get(i)).getDescricao(),
                        Integer.toString(((EscStatus) list.get(i)).getId())));
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
            List list = sv.listaObjeto("Vendedor", true);
            for (int i = 0; i < list.size(); i++) {
                listaVendedor.add(new SelectItem(new Integer(i),
                        (String) ((Vendedor) list.get(i)).getPessoa().getNome(),
                        Integer.toString(((Vendedor) list.get(i)).getId())));
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
                listaProfessor.add(new SelectItem(new Integer(i),
                        (String) (list.get(i).getProfessor().getNome()),
                        Integer.toString(list.get(i).getId())));
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
                listaMidia.add(new SelectItem(new Integer(i),
                        (String) (list.get(i).getDescricao()),
                        Integer.toString(list.get(i).getId())));
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

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
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
                this.idDiaVencimento = Integer.parseInt(DataHoje.data().substring(0, 2));
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

    public List<SelectItem> getListaTurma() {
        if (listaTurma.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            List<Turma> list = (List<Turma>) sv.listaObjeto("Turma", true);
            for (int i = 0; i < list.size(); i++) {
                listaTurma.add(new SelectItem(new Integer(i),
                        (String) list.get(i).getCursos().getDescricao() + " - "
                        + list.get(i).getDataInicio() + " - "
                        + list.get(i).getHoraInicio() + " h - Sala: "
                        + list.get(i).getSala() + " - Descrição: "
                        + list.get(i).getDescricao(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaTurma;
    }

    public void setListaTurma(List<SelectItem> listaTurma) {
        this.listaTurma = listaTurma;
    }

    public List<SelectItem> getListaIndividual() {
        if (listaIndividual.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List list = db.pesquisaTodos(151);
            for (int i = 0; i < list.size(); i++) {
                listaIndividual.add(new SelectItem(new Integer(i),
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

    public List getListaMatriculas() {
        listaGridMEscola.clear();
        getListaMatriculaEscolas();
        MatriculaEscolaDB dB = new MatriculaEscolaDBToplink();
        DataObject dtObj;
        for (int i = 0; i < listaMatriculaEscolas.size(); i++) {
            if (porPesquisa.equals("matriculaIndividual")) {
                MatriculaIndividual mIndividual = (MatriculaIndividual) dB.pesquisaCodigoMIndividual(listaMatriculaEscolas.get(i).getId());
                dtObj = new DataObject(
                        mIndividual.getMatriculaEscola().getId(),
                        mIndividual.getMatriculaEscola().getAluno().getNome(),
                        mIndividual.getCurso().getDescricao(),
                        mIndividual.getDataInicioString() + " - " + mIndividual.getDataTerminoString(),
                        mIndividual.getMatriculaEscola().getValorTotalString(),
                        mIndividual.getMatriculaEscola().getFilial().getFilial().getPessoa().getNome() + " - CNPJ: " + matriculaIndividual.getMatriculaEscola().getFilial().getFilial().getPessoa().getDocumento());
            } else {
                MatriculaTurma mTurma = dB.pesquisaCodigoMTurma(listaMatriculaEscolas.get(i).getId());
                dtObj = new DataObject(
                        mTurma.getMatriculaEscola().getId(),
                        mTurma.getMatriculaEscola().getAluno().getNome(),
                        mTurma.getTurma().getCursos().getDescricao(),
                        mTurma.getTurma().getDataInicio() + " - " + mTurma.getTurma().getDataTermino(),
                        mTurma.getMatriculaEscola().getValorTotalString(),
                        mTurma.getMatriculaEscola().getFilial().getFilial().getPessoa().getNome() + " - CNPJ: " + mTurma.getMatriculaEscola().getFilial().getFilial().getPessoa().getDocumento());
            }
            listaGridMEscola.add(dtObj);
        }
        return listaGridMEscola;
    }

    public String getDescricaoAluno() {
        return descricaoAluno;
    }

    public void setDescricaoAluno(String descricaoAluno) {
        this.descricaoAluno = descricaoAluno;
    }

    public String getDescricaoCurso() {
        return descricaoCurso;
    }

    public void setDescricaoCurso(String descricaoCurso) {
        this.descricaoCurso = descricaoCurso;
    }

    public List<MatriculaEscola> getListaMatriculaEscolas() {
        MatriculaEscolaDB dB = new MatriculaEscolaDBToplink();
        listaMatriculaEscolas = dB.pesquisaMatriculaEscola(getPorPesquisa(), getDescricaoCurso(), getDescricaoAluno(), getComoPesquisa());
        return listaMatriculaEscolas;
    }

    public void setListaMatriculaEscolas(List<MatriculaEscola> listaMatriculaEscolas) {
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
        if (GenericaSessao.exists("matriculaEscolaPesquisa")) {
            MatriculaEscolaDB matriculaEscolaDB = new MatriculaEscolaDBToplink();
            desabilitaCampo = true;
            idDiaVencimentoPessoa = 0;
            matriculaEscola = (MatriculaEscola) GenericaSessao.getObject("matriculaEscolaPesquisa", true);
            if (matriculaEscola.getEvt() != null) {
                desabilitaCamposMovimento = true;
                desabilitaDiaVencimento = true;
            }
            setValorString(matriculaEscola.getValorTotalString());
            if (porPesquisa.equals("matriculaIndividual")) {
                tipoMatricula = "Individual";
                matriculaIndividual = matriculaEscolaDB.pesquisaCodigoMIndividual(matriculaEscola.getId());
                desabilitaTurma = true;
                desabilitaIndividual = false;
            } else {
                tipoMatricula = "Turma";
                matriculaTurma = matriculaEscolaDB.pesquisaCodigoMTurma(matriculaEscola.getId());
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
        }
        if (matriculaEscola.getFilial().getId() == -1) {
            getMacFilial();
            matriculaEscola.setFilial(macFilial.getFilial());
        }
        if (matriculaEscola.getEvt() != null) {
            desabilitaGeracaoContrato = true;
            target = "_blank";
        } else {
            desabilitaGeracaoContrato = false;
        }
        JuridicaDB juridicaDB = new JuridicaDBToplink();
        Juridica juridicas = juridicaDB.pesquisaJuridicaPorPessoa(matriculaEscola.getResponsavel().getId());
        if (juridicas != null) {
            if (juridicas.getId() != -1) {
                desabilitaDescontoFolha = false;
                ocultaDescontoFolha = false;
            } else {
                desabilitaDescontoFolha = true;
                ocultaDescontoFolha = true;
            }
        } else {
            desabilitaDescontoFolha = true;
            ocultaDescontoFolha = true;
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
            if (!listaTurma.isEmpty()) {
                if (!listaIndividual.isEmpty()) {
                    idServico = Integer.parseInt(listaIndividual.get(idIndividual).getDescription());
                }
            }
        } else {
            if (listaTurma.isEmpty()) {
                getListaTurma();
            }
            if (!listaTurma.isEmpty()) {
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                idServico = ((Turma) (salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaTurma.get(idTurma).getDescription()), "Turma"))).getCursos().getId();
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
                    for (int i = 0; i < listaMovimentos.size(); i++) {
                        if (listaMovimentos.get(i).getTipoServico().getId() == 5) {
                            setTaxa(true);
                            valorTaxa = Moeda.converteR$Float(listaMovimentos.get(i).getValor());
                            listaMovimentos.get(i).setQuantidade(0);
                        } else {
                            count++;
                            listaMovimentos.get(i).setQuantidade(count);
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
        if (listaMovimentos.isEmpty()) {
            habilitaGerarParcelas = true;
        } else {
            habilitaGerarParcelas = false;
        }
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

    public void tipoMatriculaPor(AjaxBehaviorEvent event) {
        tipoMatricula = (String) event.getComponent().getAttributes().get("name");
        pegarIdServico();
        atualizaValor();
        calculaValorLiquido();
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
            listaFTipoDocumento.add((FTipoDocumento) salvarAcumuladoDB.pesquisaCodigo(13, "FTipoDocumento"));
            listaFTipoDocumento.add((FTipoDocumento) salvarAcumuladoDB.pesquisaCodigo(2, "FTipoDocumento"));
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
            if (matriculaEscola.getResponsavel().getId() == -1) {
                pessoaResponsavelMemoria = responsavel;
            } else {
                if (responsavel.getId() != matriculaEscola.getResponsavel().getId()) {
                    pessoaResponsavelMemoria = responsavel;
                }
            }
            juridica = new Juridica();
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }
}