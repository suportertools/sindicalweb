package br.com.rtools.academia.beans;

import br.com.rtools.academia.AcademiaSemana;
import br.com.rtools.academia.AcademiaServicoValor;
import br.com.rtools.academia.dao.AcademiaDao;
import br.com.rtools.associativo.DescontoSocial;
import br.com.rtools.associativo.HistoricoEmissaoGuias;
import br.com.rtools.associativo.MatriculaAcademia;
import br.com.rtools.associativo.MatriculaSocios;
import br.com.rtools.associativo.ModeloCarteirinha;
import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.SocioCarteirinhaDB;
import br.com.rtools.associativo.db.SocioCarteirinhaDBToplink;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.escola.dao.MatriculaEscolaDao;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.ServicoValor;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.LoteDB;
import br.com.rtools.financeiro.db.LoteDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.ServicoValorDB;
import br.com.rtools.financeiro.db.ServicoValorDBToplink;
import br.com.rtools.impressao.CarneEscola;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.pessoa.beans.FisicaBean;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.utilitarios.SegurancaUtilitariosBean;
import br.com.rtools.sistema.Periodo;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.db.FunctionsDB;
import br.com.rtools.utilitarios.db.FunctionsDao;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;

@ManagedBean
@SessionScoped
public class MatriculaAcademiaBean implements Serializable {

    private MatriculaAcademia matriculaAcademia;
    private Fisica aluno;
    private Registro registro;
    private Pessoa responsavel;
    private Pessoa cobranca;
    private Juridica juridica;
    private Pessoa pessoaAlunoMemoria;
    private Pessoa pessoaResponsavelMemoria;
    private PessoaComplemento pessoaComplemento;
    private Movimento movimento;
    private Socios socios;
    private Socios sociosCobranca;
    private Lote lote;
    private String descricaoPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String message;
    private String messageStatusDebito;
    private String messageStatusEmpresa;
    private String valor;
    private String valorParcela;
    private String valorParcelaVencimento;
    private String valorLiquido;
    private String valorTaxa;
    private String target;
    private List<MatriculaAcademia> listaAcademia;
    private List<Movimento> listaMovimentos;
    private List<SelectItem> listaDiaVencimento;
    private List<SelectItem> listaModalidades;
    private List<SelectItem> listaPeriodosGrade;
    private List<SelectItem> listaDiaParcela;
    private boolean taxa;
    private boolean ocultaBotaoSalvar;
    private boolean socio;
    private boolean desabilitaCamposMovimento;
    private boolean desabilitaGeracaoContrato;
    private boolean desabilitaDiaVencimento;
    private boolean ocultaParcelas;
    private boolean ocultaBotaoTarifaCartao;
    private boolean taxaCartao;
    private boolean alunoFoto;
    private boolean matriculaAtiva;
    private Boolean disabled;
    private int idDiaVencimento;
    private int idModalidade;
    private Object idModalidadePesquisa;
    private int idPeriodoGrade;
    private int idServico;
    private int idDiaVencimentoPessoa;
    private int idFTipoDocumento;
    private int idDiaParcela;
    private float vTaxa;
    private float desconto;
    private float valorCartao;
    private String dataValidade;
    private String mensagemInadinplente;

    @PostConstruct
    public void init() {
        matriculaAcademia = new MatriculaAcademia();
        aluno = new Fisica();
        registro = new Registro();
        responsavel = new Pessoa();
        cobranca = null;
        juridica = new Juridica();
        pessoaAlunoMemoria = new Pessoa();
        pessoaResponsavelMemoria = new Pessoa();
        pessoaComplemento = new PessoaComplemento();
        movimento = new Movimento();
        socios = new Socios();
        lote = new Lote();
        descricaoPesquisa = "";
        porPesquisa = "";
        comoPesquisa = "";
        message = "";
        messageStatusDebito = "";
        messageStatusEmpresa = "";
        valor = "";
        valorParcela = "";
        valorParcelaVencimento = "";
        valorLiquido = "";
        valorTaxa = "";
        target = "#";
        listaAcademia = new ArrayList();
        listaMovimentos = new ArrayList();
        listaDiaVencimento = new ArrayList();
        listaModalidades = new ArrayList();
        listaPeriodosGrade = new ArrayList();
        listaDiaParcela = new ArrayList();
        taxa = false;
        ocultaBotaoSalvar = false;
        socio = false;
        desabilitaCamposMovimento = false;
        desabilitaGeracaoContrato = false;
        desabilitaDiaVencimento = false;
        ocultaParcelas = true;
        ocultaBotaoTarifaCartao = true;
        matriculaAtiva = true;
        taxaCartao = false;
        alunoFoto = false;
        idDiaVencimento = Integer.parseInt(DataHoje.data().substring(0, 2));
        idModalidade = 0;
        idModalidadePesquisa = null;
        idPeriodoGrade = 0;
        idServico = 0;
        idDiaVencimentoPessoa = 0;
        idFTipoDocumento = 0;
        vTaxa = 0;
        desconto = 0;
        valorCartao = 0;
        idDiaParcela = 0;
        dataValidade = "";
        matriculaAcademia.getServicoPessoa().setReferenciaVigoracao(DataHoje.livre(matriculaAcademia.getServicoPessoa().getDtEmissao(), "MM/yyyy"));
        getRegistro();
        disabled = false;

        getListaModalidades();
        pegarIdServico();
        calculoValor();
        calculoDesconto();
    }

    @PreDestroy
    public void destroy() {
        clear();
        clear(2);
    }

    public void calculoValor() {
        String valorx;
        Servicos se = (Servicos) new Dao().find(new Servicos(), idServico);
        if (se != null) {
//            if (servicoPessoa.getNrDesconto() == 0) {
            if (aluno.getPessoa().getSocios().getId() != -1) {
                valorx = Moeda.converteR$Float(new FunctionsDao().valorServico(aluno.getPessoa().getId(), se.getId(), DataHoje.dataHoje(), 0, aluno.getPessoa().getSocios().getMatriculaSocios().getCategoria().getId()));
            } else {
                valorx = Moeda.converteR$Float(new FunctionsDao().valorServico(aluno.getPessoa().getId(), se.getId(), DataHoje.dataHoje(), 0, null));
            }
//            } else {
//                float valorx_c = new FunctionsDao().valorServicoCheio(servicoPessoa.getPessoa().getId(), se.getId(), DataHoje.dataHoje());
//
//                float calculo = Moeda.divisaoValores(Moeda.multiplicarValores(servicoPessoa.getNrDesconto(), valorx_c), 100);
//                valorx = Moeda.converteR$Float(Moeda.subtracaoValores(valorx_c, Moeda.converteFloatR$Float(calculo)));
//            }

            valor = Moeda.converteR$(valorx);
//            vTaxa = new FunctionsDao().valorServico(aluno.getPessoa().getId(), idServico, DataHoje.dataHoje(), 2, null);
        }
    }

    public void calculoDesconto() {
        String valorx;
        Servicos se = (Servicos) new Dao().find(new Servicos(), idServico);
        if (se != null) {
            if (aluno.getPessoa().getSocios().getId() != -1) {
                valorx = Moeda.converteR$Float(new FunctionsDao().valorServico(aluno.getPessoa().getId(), se.getId(), DataHoje.dataHoje(), 0, aluno.getPessoa().getSocios().getMatriculaSocios().getCategoria().getId()));
            } else {
                valorx = Moeda.converteR$Float(new FunctionsDao().valorServico(aluno.getPessoa().getId(), se.getId(), DataHoje.dataHoje(), 0, null));
            }

            String valorx_cheio = Moeda.converteR$Float(new FunctionsDao().valorServicoCheio(aluno.getPessoa().getId(), se.getId(), DataHoje.dataHoje()));
            float calculo = Moeda.converteUS$(valorx_cheio) - (Moeda.converteUS$(valorx) - desconto);
            float valor_do_percentual = Moeda.converteUS$(Moeda.percentualDoValor(valorx_cheio, Moeda.converteR$Float(calculo)));

            if (desconto == 0) {
                matriculaAcademia.getServicoPessoa().setNrDescontoString("0.0");
            } else {
                //float valorx_c = Moeda.subtracaoValores(Moeda.converteUS$(valorx_cheio), Moeda.converteUS$(valor));
                //valorx_c = Moeda.multiplicarValores(Moeda.divisaoValores(valorx_c, Moeda.converteUS$(valorx_cheio)), 100);
                matriculaAcademia.getServicoPessoa().setNrDesconto(valor_do_percentual);
            }

            if (aluno.getPessoa().getId() != -1) {
                Integer idade = new DataHoje().calcularIdade(aluno.getDtNascimento());
                List<ServicoValor> lsv = new MatriculaEscolaDao().listServicoValorPorServicoIdade(se.getId(), idade);
                valorLiquido = (lsv.isEmpty()) ? valor : Moeda.converteR$Float(Moeda.converteUS$(Moeda.valorDoPercentual(valor, Moeda.converteR$Float(lsv.get(0).getDescontoAteVenc()))));
                valorLiquido = Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.converteUS$(valorLiquido), desconto));
            } else {
                valorLiquido = Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.converteUS$(valor), desconto));
            }
        }
    }

    public void load() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            getAluno();
        }
        if (GenericaSessao.exists("juridicaPesquisa")) {
            getJuridica();
        }
    }

    public void clear() {
        clear(0);
        clear(1);
    }

    public void clear(Integer tCase) {
        if (tCase == 0) {
            GenericaSessao.remove("matriculaAcademiaBean");
            GenericaSessao.remove("fisicaPesquisa");
            GenericaSessao.remove("juridicaPesquisa");
            GenericaSessao.remove("uploadBean");
            GenericaSessao.remove("photoCamBean");
        }
        if (tCase == 1) {
            try {
                FileUtils.deleteDirectory(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + "/Cliente/" + ControleUsuarioBean.getCliente() + "/temp/" + "foto/" + new SegurancaUtilitariosBean().getSessaoUsuario().getId()));
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + -1 + ".png"));
                if (f.exists()) {
                    f.delete();
                }
            } catch (IOException ex) {
                Logger.getLogger(FisicaBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (tCase == 2) {
            GenericaSessao.remove("cropperBean");
            GenericaSessao.remove("uploadBean");
            GenericaSessao.remove("photoCamBean");
        }
    }

    public void salvarData() {
        if (matriculaAcademia.getServicoPessoa().getCobranca().getId() != -1) {
            Dao dao = new Dao();
            Pessoa pResponsavel = matriculaAcademia.getServicoPessoa().getCobranca();
            MatriculaEscolaDao med = new MatriculaEscolaDao();
            PessoaComplemento pc = med.pesquisaDataRefPessoaComplemto(pResponsavel.getId());
            if (pc == null || pc.getId() == -1) {
                pc = new PessoaComplemento();
                pc.setPessoa(pResponsavel);
                pc.setNrDiaVencimento(idDiaVencimento);
                dao.save(pc, true);
            } else {
                pc.setNrDiaVencimento(idDiaVencimento);
                dao.update(pc, true);
            }
        }
    }

    public String save() {
        message = "";
        if (MacFilial.getAcessoFilial().getId() == -1) {
            message = "Para salvar convites não cortesia configurar Filial em sua estação trabalho!";
            return null;
        }
        if (matriculaAcademia.getServicoPessoa().getPessoa().getId() == -1) {
            message = "Pesquisar uma pessoa!";
            return null;
        }
        if (matriculaAcademia.getServicoPessoa().getCobranca().getId() == -1) {
            message = "Pesquisar um responsável!";
            return null;
        }
        if (listaModalidades.isEmpty()) {
            message = "Cadastrar modalidades!";
            return null;
        }
        if (listaPeriodosGrade.isEmpty()) {
            message = "Cadastrar período grade!";
            return null;
        }
        if (valor.isEmpty() || valor.equals("0") || valor.equals("0,00")) {
            message = "O valor do serviço deve ser maior que R$ 0,00 !";
            return null;
        }
        matriculaAcademia.getServicoPessoa().setNrDiaVencimento(idDiaParcela);
        Dao di = new Dao();
        matriculaAcademia.getServicoPessoa().setTipoDocumento((FTipoDocumento) di.find(new FTipoDocumento(), 1));
        matriculaAcademia.setAcademiaServicoValor((AcademiaServicoValor) di.find(new AcademiaServicoValor(), Integer.parseInt(listaPeriodosGrade.get(idPeriodoGrade).getDescription())));
        if (cobranca != null) {
            matriculaAcademia.getServicoPessoa().setCobranca(cobranca);
        } else {
            matriculaAcademia.getServicoPessoa().setCobranca(matriculaAcademia.getServicoPessoa().getCobranca());
        }
        if (matriculaAcademia.getServicoPessoa().isDescontoFolha()) {
            if (sociosCobranca.getId() != -1) {
                //matriculaAcademia.getServicoPessoa().setCobranca(matriculaAcademia.getServicoPessoa().getPessoa());
            }
        }
        if (responsavel != null && responsavel.getId() != -1) {
            matriculaAcademia.getServicoPessoa().setCobranca(responsavel);
        }
        NovoLog novoLog = new NovoLog();
        if (matriculaAcademia.getId() == -1) {
            AcademiaDao academiaDao = new AcademiaDao();
            if (academiaDao.existeAlunoModalidade(matriculaAcademia.getServicoPessoa().getPessoa().getId(), matriculaAcademia.getAcademiaServicoValor().getServicos().getId(), matriculaAcademia.getServicoPessoa().getDtEmissao())) {
                message = "Aluno já cadastrado para esta modalidade!";
                return null;
            }
            SocioCarteirinha socioCarteirinha = new SocioCarteirinha();
            SocioCarteirinhaDB scdb = new SocioCarteirinhaDBToplink();
            // PESQUISA CARTEIRINHA SEM MODELO
            String validadeCarteirinha = "";
            DataHoje dh = new DataHoje();
            if (socios.getId() != -1) {
                // validadeCarteirinha = dh.incrementarMeses(socios.getMatriculaSocios().getCategoria().getGrupoCategoria().getNrValidadeMesCartao(), DataHoje.data());
                // modeloCarteirinha = scdb.pesquisaModeloCarteirinha(socios.getMatriculaSocios().getCategoria().getId(), 122);
            } else {
                validadeCarteirinha = dh.incrementarAnos(5, DataHoje.data());
            }
            ModeloCarteirinha modeloCarteirinha = scdb.pesquisaModeloCarteirinha(-1, 122);
            if (modeloCarteirinha == null) {
                message = "Informar modelo da carteirinha!";
                return null;
            }
            // CRIA CARTEIRINHA CASO NÃO EXISTA
            SocioCarteirinha scx = scdb.pesquisaCarteirinhaPessoa(matriculaAcademia.getServicoPessoa().getPessoa().getId(), modeloCarteirinha.getId());
            Boolean insert = false;
            if (scx == null || scx.getId() == -1) {
                socioCarteirinha.setDtEmissao(DataHoje.dataHoje());
                socioCarteirinha.setCartao(0);
                socioCarteirinha.setValidadeCarteirinha(validadeCarteirinha);
                socioCarteirinha.setPessoa(matriculaAcademia.getServicoPessoa().getPessoa());
                socioCarteirinha.setModeloCarteirinha(modeloCarteirinha);
                insert = true;
            } else {
                socioCarteirinha = null;
            }
            Usuario usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
            matriculaAcademia.setUsuario(usuario);
            matriculaAcademia.getServicoPessoa().setServicos(matriculaAcademia.getAcademiaServicoValor().getServicos());
            di.openTransaction();
            if (socioCarteirinha != null) {
                if (!di.save(socioCarteirinha)) {
                    di.rollback();
                    message = "Erro ao adicionar sócio carteirinha!";
                    return null;
                }
                if (insert) {
                    socioCarteirinha.setCartao(socioCarteirinha.getId());
                    if (!di.update(socioCarteirinha)) {
                        di.rollback();
                        message = "Erro ao atualizar sócio carteirinha!";
                        return null;
                    }
                }
            }
            // DESCONTO SOCIAL DEFAULT
            matriculaAcademia.getServicoPessoa().setDescontoSocial((DescontoSocial) di.find(new DescontoSocial(), 1));
            if (!di.save(matriculaAcademia.getServicoPessoa())) {
                di.rollback();
                message = "Erro ao adicionar serviço pessoa!";
                return null;
            }
            matriculaAcademia.setEvt(null);
            matriculaAcademia.setValidade(dataValidade);
            if (!di.save(matriculaAcademia)) {
                di.rollback();
                message = "Erro ao adicionar registro!";
                return null;
            }
            MatriculaEscolaDao med = new MatriculaEscolaDao();
            pessoaComplemento = med.pesquisaDataRefPessoaComplemto(matriculaAcademia.getServicoPessoa().getCobranca().getId());
            if (pessoaComplemento == null) {
                pessoaComplemento = new PessoaComplemento();
                pessoaComplemento.setNrDiaVencimento(idDiaVencimento);
                pessoaComplemento.setPessoa((Pessoa) di.find(new Pessoa(), matriculaAcademia.getServicoPessoa().getCobranca().getId()));
                if (!di.save(pessoaComplemento)) {
                    di.rollback();
                    message = "Falha ao inserir pessoa complemento!";
                    return null;
                }
            }
            pessoaAlunoMemoria = matriculaAcademia.getServicoPessoa().getPessoa();
            pessoaResponsavelMemoria = matriculaAcademia.getServicoPessoa().getCobranca();
            message = "Registro inserido com sucesso";
            novoLog.save(""
                    + "ID: " + matriculaAcademia.getId()
                    + " - Pessoa: (" + matriculaAcademia.getServicoPessoa().getPessoa().getId() + ") " + matriculaAcademia.getServicoPessoa().getPessoa().getNome()
                    + " - Cobrança: (" + matriculaAcademia.getServicoPessoa().getCobranca().getId() + ") " + matriculaAcademia.getServicoPessoa().getCobranca().getNome()
                    + " - Serviço: (" + matriculaAcademia.getAcademiaServicoValor().getServicos().getId() + ") " + matriculaAcademia.getAcademiaServicoValor().getServicos().getDescricao()
                    + " - Academia Servico Valor: (" + matriculaAcademia.getAcademiaServicoValor().getId() + ")"
                    + " - Parcelas: " + matriculaAcademia.getNumeroParcelas() + " "
            );
            di.commit();
            return gerarMovimento();
        } else {
            SocioCarteirinha socioCarteirinha = new SocioCarteirinha();
            SocioCarteirinhaDB scdb = new SocioCarteirinhaDBToplink();
            // PESQUISA CARTEIRINHA SEM MODELO
            String validadeCarteirinha = "";
            DataHoje dh = new DataHoje();
            if (socios.getId() != -1) {
                // validadeCarteirinha = dh.incrementarMeses(socios.getMatriculaSocios().getCategoria().getGrupoCategoria().getNrValidadeMesCartao(), DataHoje.data());
                // modeloCarteirinha = scdb.pesquisaModeloCarteirinha(socios.getMatriculaSocios().getCategoria().getId(), 122);
            } else {
                validadeCarteirinha = dh.incrementarAnos(5, DataHoje.data());
            }
            ModeloCarteirinha modeloCarteirinha = scdb.pesquisaModeloCarteirinha(-1, 122);
            if (modeloCarteirinha == null) {
                message = "Informar modelo da carteirinha!";
                return null;
            }
            // CRIA CARTEIRINHA CASO NÃO EXISTA
            SocioCarteirinha scx = scdb.pesquisaCarteirinhaPessoa(matriculaAcademia.getServicoPessoa().getPessoa().getId(), modeloCarteirinha.getId());
            Boolean insert = false;
            if (scx == null || scx.getId() == -1) {
                socioCarteirinha.setDtEmissao(DataHoje.dataHoje());
                socioCarteirinha.setCartao(0);
                socioCarteirinha.setValidadeCarteirinha(validadeCarteirinha);
                socioCarteirinha.setPessoa(matriculaAcademia.getServicoPessoa().getPessoa());
                socioCarteirinha.setModeloCarteirinha(modeloCarteirinha);
                insert = true;
            } else {
                socioCarteirinha = null;
            }
            di.openTransaction();
            if (socioCarteirinha != null) {
                if (!di.save(socioCarteirinha)) {
                    di.rollback();
                    message = "Erro ao adicionar sócio carteirinha!";
                    return null;
                }
                if (insert) {
                    socioCarteirinha.setCartao(socioCarteirinha.getId());
                    if (!di.update(socioCarteirinha)) {
                        di.rollback();
                        message = "Erro ao atualizar sócio carteirinha!";
                        return null;
                    }
                }
            }
            if (!di.update(matriculaAcademia.getServicoPessoa())) {
                di.rollback();
                message = "Erro ao atualizar serviço pessoa!";
                return null;
            }
            MatriculaAcademia ma = (MatriculaAcademia) di.find(matriculaAcademia);
            String beforeUpdate = ""
                    + "ID: " + ma.getId()
                    + " - Pessoa: (" + ma.getServicoPessoa().getPessoa().getId() + ") " + ma.getServicoPessoa().getPessoa().getNome()
                    + " - Cobrança: (" + ma.getServicoPessoa().getCobranca().getId() + ") " + ma.getServicoPessoa().getCobranca().getNome()
                    + " - Serviço: (" + ma.getAcademiaServicoValor().getServicos().getId() + ") " + ma.getAcademiaServicoValor().getServicos().getDescricao()
                    + " - Academia Servico Valor: (" + ma.getAcademiaServicoValor().getId() + ")"
                    + " - Parcelas: " + ma.getNumeroParcelas() + " ";
            if (!di.update(matriculaAcademia)) {
                di.rollback();
                message = "Erro ao atualizar registro!";
                return null;
            }
            pessoaAlunoMemoria = matriculaAcademia.getServicoPessoa().getPessoa();
            pessoaResponsavelMemoria = matriculaAcademia.getServicoPessoa().getCobranca();
            message = "Registro atualizado com sucesso";
            novoLog.update(beforeUpdate,
                    "ID: " + matriculaAcademia.getId()
                    + " - Pessoa: (" + matriculaAcademia.getServicoPessoa().getPessoa().getId() + ") " + matriculaAcademia.getServicoPessoa().getPessoa().getNome()
                    + " - Cobrança: (" + matriculaAcademia.getServicoPessoa().getCobranca().getId() + ") " + matriculaAcademia.getServicoPessoa().getCobranca().getNome()
                    + " - Serviço: (" + matriculaAcademia.getAcademiaServicoValor().getServicos().getId() + ") " + matriculaAcademia.getAcademiaServicoValor().getServicos().getDescricao()
                    + " - Academia Servico Valor: (" + matriculaAcademia.getAcademiaServicoValor().getId() + ")"
                    + " - Parcelas: " + matriculaAcademia.getNumeroParcelas() + " "
            );
            di.commit();
            if (matriculaAcademia.getAcademiaServicoValor().getPeriodo().getId() == 3) {
                new FunctionsDao().gerarMensalidades(matriculaAcademia.getServicoPessoa().getPessoa().getId(), matriculaAcademia.getServicoPessoa().getReferenciaVigoracao());
            }
        }
        return null;
    }

    public void delete() {
        Dao di = new Dao();
        if (matriculaAcademia.getId() != -1) {
            di.openTransaction();
            listaMovimentos.clear();
            for (int i = 0; i < getListaMovimentos().size(); i++) {
                if (listaMovimentos.get(i).getBaixa() != null) {
                    message = "Não é possível excluir um registro com movimentos já baixados!";
                    di.rollback();
                    return;
                }
                if (!di.delete(listaMovimentos.get(i))) {
                    message = "Erro ao excluir movimento!";
                    di.rollback();
                    return;
                }
            }
            if (!listaMovimentos.isEmpty()) {
                if (!di.delete(listaMovimentos.get(0).getLote())) {
                    message = "Erro ao excluir lote!";
                    di.rollback();
                    return;
                }
            }
            if (!di.delete(matriculaAcademia)) {
                di.rollback();
                message = "Erro ao excluir registro!";
                return;
            }
            if (!di.delete(matriculaAcademia.getServicoPessoa())) {
                di.rollback();
                message = "Erro ao excluir serviço pessoa!";
                return;
            }
            message = "Registro excluído com sucesso";
            NovoLog novoLog = new NovoLog();
            novoLog.delete(""
                    + "ID: " + matriculaAcademia.getId()
                    + " - Pessoa: (" + matriculaAcademia.getServicoPessoa().getPessoa().getId() + ") " + matriculaAcademia.getServicoPessoa().getPessoa().getNome()
                    + " - Cobrança: (" + matriculaAcademia.getServicoPessoa().getCobranca().getId() + ") " + matriculaAcademia.getServicoPessoa().getCobranca().getNome()
                    + " - Serviço: (" + matriculaAcademia.getAcademiaServicoValor().getServicos().getId() + ") " + matriculaAcademia.getAcademiaServicoValor().getServicos().getDescricao()
                    + " - Academia Servico Valor: (" + matriculaAcademia.getAcademiaServicoValor().getId() + ")"
                    + " - Parcelas: " + matriculaAcademia.getNumeroParcelas() + " "
            );
            di.commit();
            clear();
            clear(2);
        }

    }

    public String editar(MatriculaAcademia ma) {
        disabled = false;
        socios = new Socios();
        mensagemInadinplente = "";
        matriculaAcademia = (MatriculaAcademia) new Dao().find(new MatriculaAcademia(), ma.getId());
        idDiaVencimentoPessoa = 0;
        if (matriculaAcademia.getEvt() != null || matriculaAcademia.getAcademiaServicoValor().getPeriodo().getId() == 3) {
            desabilitaCamposMovimento = true;
            desabilitaDiaVencimento = true;
        }

        Dao dao = new Dao();
        for (int i = 0; i < listaModalidades.size(); i++) {
            AcademiaServicoValor asv = (AcademiaServicoValor) dao.find(new AcademiaServicoValor(), Integer.parseInt(listaModalidades.get(i).getDescription()));
            if (asv.getServicos().getId() == ma.getServicoPessoa().getServicos().getId()) {
                idModalidade = i;
                break;
            }
        }
        listaPeriodosGrade.clear();
        getListaPeriodosGrade();
        for (int i = 0; i < listaPeriodosGrade.size(); i++) {
            if (Integer.parseInt(listaPeriodosGrade.get(i).getDescription()) == ma.getAcademiaServicoValor().getId()) {
                idPeriodoGrade = i;
                break;
            }
        }
        taxa = matriculaAcademia.isTaxa();
        taxaCartao = matriculaAcademia.isTaxaCartao();
        idDiaVencimento = ma.getServicoPessoa().getNrDiaVencimento();
        idFTipoDocumento = matriculaAcademia.getServicoPessoa().getTipoDocumento().getId();
        FisicaDB fisicaDB = new FisicaDBToplink();
        aluno = fisicaDB.pesquisaFisicaPorPessoa(matriculaAcademia.getServicoPessoa().getPessoa().getId());
        if (aluno.getId() != -1) {
            getResponsavel();
            if (responsavel.getId() != -1) {
                MatriculaEscolaDao med = new MatriculaEscolaDao();
                PessoaComplemento pc = med.pesquisaDataRefPessoaComplemto(responsavel.getId());
                if (pc != null && pc.getId() != -1) {
                    this.idDiaVencimento = pc.getNrDiaVencimento();
                } else {
                    this.idDiaVencimento = Integer.parseInt(DataHoje.data().substring(0, 2));
                }
            }
            verificaSocio();
        }

        String valorx;
        if (aluno.getPessoa().getSocios().getId() != -1) {
            valorx = Moeda.converteR$Float(new FunctionsDao().valorServico(aluno.getPessoa().getId(), matriculaAcademia.getServicoPessoa().getServicos().getId(), DataHoje.dataHoje(), 0, aluno.getPessoa().getSocios().getMatriculaSocios().getCategoria().getId()));
        } else {
            valorx = Moeda.converteR$Float(new FunctionsDao().valorServico(aluno.getPessoa().getId(), matriculaAcademia.getServicoPessoa().getServicos().getId(), DataHoje.dataHoje(), 0, null));
        }
        String valorx_cheio = Moeda.converteR$Float(new FunctionsDao().valorServicoCheio(aluno.getPessoa().getId(), matriculaAcademia.getAcademiaServicoValor().getServicos().getId(), DataHoje.dataHoje()));

        //desconto = Moeda.subtracaoValores(Moeda.converteUS$(valorx_cheio), Moeda.converteUS$(Moeda.valorDoPercentual(valorx_cheio, Float.toString(matriculaAcademia.getServicoPessoa().getNrDesconto())))) ;
        desconto = Moeda.subtracaoValores(Moeda.converteUS$(valorx), Moeda.converteUS$(Moeda.valorDoPercentual(valorx_cheio, Float.toString(matriculaAcademia.getServicoPessoa().getNrDesconto()))));
//(Float.parseFloat(Moeda.substituiVirgula(valor)) * matriculaAcademia.getServicoPessoa().getNrDesconto() / 100);

        pegarIdServico();
        atualizaValor();
        //valorLiquido = "0,0";
        //calculaValorLiquido();
        pessoaResponsavelMemoria = matriculaAcademia.getServicoPessoa().getCobranca();
        pessoaAlunoMemoria = matriculaAcademia.getServicoPessoa().getPessoa();
        alunoFoto = false;
        GenericaSessao.put("linkClicado", true);
        listaDiaParcela.clear();
        getListaDiaParcela();
        clear(1);
        clear(2);
        return "matriculaAcademia";
    }

    public void inative() {
        if (matriculaAcademia.getServicoPessoa().isAtivo()) {
            matriculaAcademia.getServicoPessoa().setAtivo(false);
            matriculaAcademia.setDtInativo(new Date());
            Dao dao = new Dao();
            dao.update(matriculaAcademia, true);
            dao.update(matriculaAcademia.getServicoPessoa(), true);
            GenericaMensagem.info("Sucesso", "Matrícula inativada");
            desabilitaCamposMovimento = true;
            desabilitaDiaVencimento = true;
            listaAcademia.clear();
        }
    }

    public MatriculaAcademia getMatriculaAcademia() {
        getListaModalidades();
        getListaPeriodosGrade();
        if (socio == false) {
            getJuridica();
        }
        if (cobranca == null) {
            Dao di = new Dao();
            FunctionsDB functionsDB = new FunctionsDao();
            if (matriculaAcademia.getServicoPessoa().isDescontoFolha()) {
                int idResponsavel = functionsDB.responsavel(matriculaAcademia.getServicoPessoa().getPessoa().getId(), matriculaAcademia.getServicoPessoa().isDescontoFolha());
                if (idResponsavel != -1) {
                    cobranca = (Pessoa) di.find(new Pessoa(), idResponsavel);
                } else {
                    cobranca = matriculaAcademia.getServicoPessoa().getCobranca();
                }
            } else {
                int idResponsavelEmpresa = functionsDB.responsavel(aluno.getPessoa().getId(), true);
                if (idResponsavelEmpresa != -1) {
                    JuridicaDB juridicaDB = new JuridicaDBToplink();
                    Juridica juridicaB = juridicaDB.pesquisaJuridicaPorPessoa(idResponsavelEmpresa);
                    if (juridicaB != null) {
                        if (juridicaB.getId() != -1) {
                            cobranca = (Pessoa) di.find(new Pessoa(), idResponsavelEmpresa);
                        } else {
                            cobranca = matriculaAcademia.getServicoPessoa().getCobranca();
                        }
                    } else {
                        cobranca = matriculaAcademia.getServicoPessoa().getCobranca();
                    }
                } else {
                    cobranca = matriculaAcademia.getServicoPessoa().getCobranca();
                }
            }
            if (cobranca.getId() == -1) {
                cobranca = null;
            }
        }
        if (matriculaAcademia.getServicoPessoa().getCobranca().getId() != -1) {
            if (matriculaAcademia.getId() == -1) {
                JuridicaDB juridicaDB = new JuridicaDBToplink();
                Juridica j = juridicaDB.pesquisaJuridicaPorPessoa(matriculaAcademia.getServicoPessoa().getCobranca().getId());
                if (j != null) {
                    verificaSeContribuinteInativo();
                }
            }
        }
        return matriculaAcademia;
    }

    public void setMatriculaAcademia(MatriculaAcademia matriculaAcademia) {
        this.matriculaAcademia = matriculaAcademia;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MatriculaAcademia> getListaAcademia() {
        return listaAcademia;
    }

    public void setListaAcademia(List<MatriculaAcademia> listaAcademia) {
        this.listaAcademia = listaAcademia;
    }

    public List<SelectItem> getListaDiaVencimento() {
        if (listaDiaVencimento.isEmpty()) {
            for (int i = 1; i <= 31; i++) {
                listaDiaVencimento.add(new SelectItem(Integer.toString(i)));
            }
        }
        return listaDiaVencimento;
    }

    public void setListaDiaVencimento(List<SelectItem> listaDiaVencimento) {
        this.listaDiaVencimento = listaDiaVencimento;
    }

    public List<SelectItem> getListaModalidades() {
        if (listaModalidades.isEmpty()) {
            AcademiaDao academiaDao = new AcademiaDao();
            List<AcademiaServicoValor> list = academiaDao.listaServicoValorPorRotina();
            int idServicoMemoria = 0;
            int b = 0;
            for (int i = 0; i < list.size(); i++) {
                if (idServicoMemoria != list.get(i).getServicos().getId()) {
                    listaModalidades.add(new SelectItem(b, list.get(i).getServicos().getDescricao(), Integer.toString(list.get(i).getId())));
                    idServicoMemoria = list.get(i).getServicos().getId();
                    b++;
                }
            }
        }
        return listaModalidades;
    }

    public void setListaModalidades(List<SelectItem> listaModalidades) {
        this.listaModalidades = listaModalidades;
    }

    public List<SelectItem> getListaPeriodosGrade() {

        if (listaPeriodosGrade.isEmpty()) {
            if (!listaModalidades.isEmpty()) {
                // idPeriodoGrade = 0;
                // AcademiaDao db = new AcademiaDao();

                Dao di = new Dao();
                AcademiaDao academiaDao = new AcademiaDao();
                // List<AcademiaServicoValor> listaAcademiaServicoValor = di.list(new AcademiaServicoValor(), true);
                AcademiaServicoValor asv = (AcademiaServicoValor) di.find(new AcademiaServicoValor(), Integer.parseInt(listaModalidades.get(idModalidade).getDescription()));
                List<AcademiaServicoValor> listaAcademiaServicoValor = academiaDao.listaAcademiaServicoValorPorServico(asv.getServicos().getId());

                for (int w = 0; w < listaAcademiaServicoValor.size(); w++) {
                    String text = "";
                    List<AcademiaSemana> listaAcademiaSemana = academiaDao.listaAcademiaSemana(listaAcademiaServicoValor.get(w).getId());
                    for (int i = 0; i < listaAcademiaSemana.size(); i++) {
                        text += listaAcademiaSemana.get(i).getSemana().getDescricao().substring(0, 3) + ": " + listaAcademiaSemana.get(i).getAcademiaGrade().getHoraInicio() + " às " + listaAcademiaSemana.get(i).getAcademiaGrade().getHoraFim() + " ";
                        //listaPeriodosGrade.add(new SelectItem(i, text, Integer.toString(listaAcademiaSemana.get(i).getId())));
                    }

                    text = listaAcademiaServicoValor.get(w).getPeriodo().getDescricao() + " - " + text;
                    listaPeriodosGrade.add(new SelectItem(w, text, Integer.toString(listaAcademiaServicoValor.get(w).getId())));
                }

                Collections.sort(listaPeriodosGrade, new Comparator<SelectItem>() {
                    @Override
                    public int compare(SelectItem sItem1, SelectItem sItem2) {
                        String sItem1Label = sItem1.getLabel();
                        String sItem2Label = sItem2.getLabel();

                        return (sItem1Label.compareToIgnoreCase(sItem2Label));
                    }
                });

                List<SelectItem> list = new ArrayList<>();
                for (int i = 0; i < listaPeriodosGrade.size(); i++) {
                    list.add(new SelectItem(i, listaPeriodosGrade.get(i).getLabel(), listaPeriodosGrade.get(i).getDescription()));
                }

                listaPeriodosGrade = list;

//                AcademiaDao academiaDao = new AcademiaDao();
//                Dao di = new Dao();
//                List<AcademiaServicoValor> list = academiaDao.listaAcademiaServicoValorPorServico(((AcademiaServicoValor) di.find(new AcademiaServicoValor(), Integer.parseInt(listaModalidades.get(idModalidade).getDescription()))).getServicos().getId());
//                List<AcademiaSemana> listSemana = new ArrayList<AcademiaSemana>();
//                for (int i = 0; i < list.size(); i++) {
//                    listSemana.clear();
//                    //listSemana = academiaDao.listaAcademiaSemana(list.get(i).getAcademiaGrade().getId());
//                    String periodoSemana = "";
//                    for (int j = 0; j < listSemana.size(); j++) {
//                        if (j == 0) {
//                            periodoSemana += semanaResumo(listSemana.get(j).getSemana().getDescricao());
//                        } else {
//                            periodoSemana += " - " + semanaResumo(listSemana.get(j).getSemana().getDescricao());
//                        }
//                    }
//                    //listaPeriodosGrade.add(new SelectItem(i, list.get(i).getPeriodo().getDescricao() + " - " + list.get(i).getAcademiaGrade().getHoraInicio() + "-" + list.get(i).getAcademiaGrade().getHoraFim() + " - " + periodoSemana, Integer.toString(list.get(i).getId())));
//                }
            }
            if (matriculaAcademia.getId() == -1) {
                Dao dao = new Dao();
                AcademiaServicoValor asv = (AcademiaServicoValor) dao.find(new AcademiaServicoValor(), Integer.parseInt(listaPeriodosGrade.get(idPeriodoGrade).getDescription()));
                if (asv.getPeriodo().getId() == 3) {
                    DataHoje dh = new DataHoje();
                    String novaData = dh.incrementarMeses(1, DataHoje.data());
                    matriculaAcademia.getServicoPessoa().setReferenciaVigoracao(DataHoje.converteDataParaReferencia(novaData));
                }
            }
        }
        return listaPeriodosGrade;
    }

    public void setListaPeriodosGrade(List<SelectItem> listaPeriodosGrade) {
        this.listaPeriodosGrade = listaPeriodosGrade;
    }

    public void carregaParcelas() {
        Dao di = new Dao();
        AcademiaServicoValor asv = (AcademiaServicoValor) di.find(new AcademiaServicoValor(), Integer.parseInt(getListaPeriodosGrade().get(idPeriodoGrade).getDescription()));
        int id = asv.getPeriodo().getId();
        switch (id) {
            case 5:
                ocultaParcelas = false;
                break;
            case 6:
                ocultaParcelas = false;
                break;
            case 7:
                ocultaParcelas = false;
                break;
            default:
                ocultaParcelas = true;
        }
        if (matriculaAcademia.getNumeroParcelas() == 0 || matriculaAcademia.getNumeroParcelas() == asv.getNumeroParcelas()) {
            matriculaAcademia.setNumeroParcelas(asv.getNumeroParcelas());
        }
    }

    public boolean isTaxa() {
        matriculaAcademia.setTaxa(taxa);
        return taxa;
    }

    public void setTaxa(boolean taxa) {
        this.taxa = taxa;
    }

    public Fisica getAluno() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            clear(1);
            disabled = false;
            MatriculaEscolaDao med = new MatriculaEscolaDao();
            if (GenericaSessao.exists("pesquisaFisicaTipo")) {
                socios = new Socios();
                mensagemInadinplente = "";
                String tipoFisica = GenericaSessao.getString("pesquisaFisicaTipo", true);
                switch (tipoFisica) {
                    case "aluno":
                        valorTaxa = "";
                        taxa = false;
                        aluno = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
                        if (matriculaAcademia.getServicoPessoa().getPessoa().getId() == -1) {
                            pessoaAlunoMemoria = aluno.getPessoa();
                        } else {
                            if (aluno.getPessoa().getId() != matriculaAcademia.getServicoPessoa().getPessoa().getId()) {
                                pessoaAlunoMemoria = aluno.getPessoa();
                            }
                        }
                        if (aluno.getId() != -1) {
                            getResponsavel();
                            verificaSocio();
                        }
                        if (responsavel.getId() != -1) {
                            pessoaComplemento = new PessoaComplemento();
                            pessoaComplemento = med.pesquisaDataRefPessoaComplemto(responsavel.getId());
                            if (pessoaComplemento != null && pessoaComplemento.getId() != -1) {
                                this.idDiaVencimentoPessoa = pessoaComplemento.getNrDiaVencimento();
                                this.idDiaVencimento = pessoaComplemento.getNrDiaVencimento();
                            }
                            matriculaAcademia.getServicoPessoa().setCobranca(responsavel);
                        }
                        matriculaAcademia.getServicoPessoa().setPessoa(aluno.getPessoa());
                        matriculaAcademia.getServicoPessoa().setCobranca(responsavel);
                        pegarIdServico();
                        atualizaValor();
                        //calculaValorLiquido();
                        GenericaSessao.remove("juridicaPesquisa");
                        if (matriculaAcademia.getServicoPessoa().getPessoa().getId() != -1 || matriculaAcademia.getServicoPessoa().getCobranca().getId() != -1) {
                            if (new FunctionsDao().inadimplente(matriculaAcademia.getServicoPessoa().getCobranca().getId()) || new FunctionsDao().inadimplente(matriculaAcademia.getServicoPessoa().getPessoa().getId())) {
                                responsavel = new Pessoa();
                                matriculaAcademia.getServicoPessoa().setCobranca(responsavel);
                                mensagemInadinplente = "Aluno em Débito!";
                                GenericaMensagem.fatal("Atenção", "Aluno em Débito!");
                                disabled = true;
                                return null;
                            }
                        }
                        // verificaDebitosResponsavel(matriculaAcademia.getServicoPessoa().getCobranca());
                        break;
                    case "responsavel":
                        socios = new Socios();
                        Pessoa resp = ((Fisica) GenericaSessao.getObject("fisicaPesquisa", true)).getPessoa();
                        FunctionsDB functionsDB = new FunctionsDao();
                        int idade = functionsDB.idade("dt_nascimento", "current_date", resp.getId());
                        if (idade >= 18) {
                            if (matriculaAcademia.getServicoPessoa().getPessoa().getId() != -1 || matriculaAcademia.getServicoPessoa().getCobranca().getId() != -1) {
                                if (new FunctionsDao().inadimplente(matriculaAcademia.getServicoPessoa().getCobranca().getId()) || new FunctionsDao().inadimplente(matriculaAcademia.getServicoPessoa().getPessoa().getId())) {
                                    mensagemInadinplente = "Aluno em Débito!";
                                    GenericaMensagem.fatal("Atenção", "Aluno em Débito!");
                                    disabled = true;
                                    return null;
                                }
                            }
                            if (med.verificaPessoaEnderecoDocumento("fisica", resp.getId())) {
                                matriculaAcademia.getServicoPessoa().setCobranca(resp);
                            }
                        } else {
                            GenericaMensagem.warn("Validação", "Responsável deve ser maior de idade!");
                        }
                        GenericaSessao.remove("juridicaPesquisa");
                        //                    verificaDebitosResponsavel(matriculaAcademia.getServicoPessoa().getCobranca());
                        pessoaComplemento = new PessoaComplemento();
                        pessoaComplemento = med.pesquisaDataRefPessoaComplemto(matriculaAcademia.getServicoPessoa().getCobranca().getId());
                        if (pessoaComplemento != null) {
                            this.idDiaVencimentoPessoa = pessoaComplemento.getNrDiaVencimento();
                            this.idDiaVencimento = pessoaComplemento.getNrDiaVencimento();
                        }
                        break;
                }
            }
            if (matriculaAcademia.getServicoPessoa().getCobranca().getId() == -1) {
                pessoaResponsavelMemoria = responsavel;
            } else {
                if (responsavel.getId() != matriculaAcademia.getServicoPessoa().getCobranca().getId()) {
                    pessoaResponsavelMemoria = responsavel;
                }
            }
        }
        getSocios();
        return aluno;
    }

    public void setAluno(Fisica aluno) {
        this.aluno = aluno;
    }

    public Pessoa getResponsavel() {
        if (aluno.getId() != -1) {
            FunctionsDB functionsDB = new FunctionsDao();
            int titularResponsavel = functionsDB.responsavel(aluno.getPessoa().getId(), matriculaAcademia.getServicoPessoa().isDescontoFolha());
            if (titularResponsavel > -1 && titularResponsavel > 0) {
                Dao di = new Dao();
                responsavel = (Pessoa) di.find(new Pessoa(), titularResponsavel);
            }
        } else {
            responsavel = new Pessoa();
        }
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public int getIdDiaVencimento() {
        return idDiaVencimento;
    }

    public void setIdDiaVencimento(int idDiaVencimento) {
        this.idDiaVencimento = idDiaVencimento;
    }

    public int getIdModalidade() {
        return idModalidade;
    }

    public void setIdModalidade(int idModalidade) {
        this.idModalidade = idModalidade;
    }

    public int getIdPeriodoGrade() {
        return idPeriodoGrade;
    }

    public void setIdPeriodoGrade(int idPeriodoGrade) {
        this.idPeriodoGrade = idPeriodoGrade;
    }

    public void verificaDebitosResponsavel(Pessoa responsavelPessoa) {
        messageStatusDebito = "";
        setOcultaBotaoSalvar(false);
        if (responsavelPessoa.getId() != -1) {
            MovimentoDB movimentoDB = new MovimentoDBToplink();
            if (movimentoDB.existeDebitoPessoa(responsavelPessoa, null)) {
                messageStatusDebito = "Responsável possui débitos!";
                setOcultaBotaoSalvar(true);
            }
        }
    }

    public String getMensagemStatusDebito() {
        return messageStatusDebito;
    }

    public void setMensagemStatusDebito(String messageStatusDebito) {
        this.messageStatusDebito = messageStatusDebito;
    }

    public boolean isOcultaBotaoSalvar() {
        return ocultaBotaoSalvar;
    }

    public void setOcultaBotaoSalvar(boolean ocultaBotaoSalvar) {
        this.ocultaBotaoSalvar = ocultaBotaoSalvar;
    }

    public void pegarIdServico() {
        if (!listaModalidades.isEmpty()) {
            Dao di = new Dao();
            idServico = ((AcademiaServicoValor) (di.find(new AcademiaServicoValor(), Integer.parseInt(listaModalidades.get(idModalidade).getDescription())))).getServicos().getId();
        }
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public void verificaSocio() {
        SociosDB dB = new SociosDBToplink();
        Socios sociosx = dB.pesquisaSocioPorPessoa(aluno.getId());
        if (sociosx != null) {
            socio = sociosx.getId() != -1;
        }
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(String valorParcela) {
        this.valorParcela = valorParcela;
    }

    public String getValorParcelaVencimento() {
        return valorParcelaVencimento;
    }

    public void setValorParcelaVencimento(String valorParcelaVencimento) {
        this.valorParcelaVencimento = valorParcelaVencimento;
    }

    public String getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(String valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public String getValorTaxa() {
        if (vTaxa > 0) {
            valorTaxa = "" + vTaxa;
        } else {
            valorTaxa = "";
        }
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

    public void recalcular1() {
        pegarIdServico();
        atualizaValor();
        //calculaValorLiquido();
        listaPeriodosGrade.clear();
        getListaPeriodosGrade();
        if (matriculaAcademia.getId() == -1) {
            Dao dao = new Dao();
            AcademiaServicoValor asv = (AcademiaServicoValor) dao.find(new AcademiaServicoValor(), Integer.parseInt(listaPeriodosGrade.get(idPeriodoGrade).getDescription()));
            if (asv.getPeriodo().getId() == 3) {
                DataHoje dh = new DataHoje();
                String novaData = dh.incrementarMeses(1, matriculaAcademia.getServicoPessoa().getEmissao());
                matriculaAcademia.getServicoPessoa().setReferenciaVigoracao(DataHoje.converteDataParaReferencia(novaData));
            }
        }
    }

    public void recalcular2() {
        pegarIdServico();
        atualizaValor();
        //calculaValorLiquido();
        if (matriculaAcademia.getId() == -1) {
            Dao dao = new Dao();
            AcademiaServicoValor asv = (AcademiaServicoValor) dao.find(new AcademiaServicoValor(), Integer.parseInt(listaPeriodosGrade.get(idPeriodoGrade).getDescription()));
            if (asv.getPeriodo().getId() == 3) {
                DataHoje dh = new DataHoje();
                String novaData = dh.incrementarMeses(1, matriculaAcademia.getServicoPessoa().getEmissao());
                matriculaAcademia.getServicoPessoa().setReferenciaVigoracao(DataHoje.converteDataParaReferencia(novaData));
            }
        }
    }

    public void calculaValorLiquido() {
        listaPeriodosGrade.clear();
        valor = Moeda.substituiVirgula(valor);
        valorLiquido = "0";
        valorParcela = "0";
        valorParcelaVencimento = "0";
        if (!valor.isEmpty()) {
            if (desconto > Float.parseFloat(valor)) {
                desconto = 0;
            }
            if (Float.parseFloat(valor) - desconto >= 0) {
                float valor_cheio = new FunctionsDao().valorServicoCheio(aluno.getPessoa().getId(), idServico, DataHoje.dataHoje());
                float valor_desconto = Moeda.subtracaoValores(100, Moeda.converteUS$(Moeda.percentualDoValor(Moeda.converteR$Float(valor_cheio), Moeda.converteR$Float(desconto))));
                valorLiquido = valor;
                valorLiquido = Moeda.converteR$Float(Float.parseFloat(Moeda.substituiVirgula(valorLiquido)) - desconto);

                if (aluno.getPessoa().getId() != -1) {
                    Integer idade = new DataHoje().calcularIdade(aluno.getDtNascimento());
                    List<ServicoValor> lsv = new MatriculaEscolaDao().listServicoValorPorServicoIdade(idServico, idade);
                    valorLiquido = (lsv.isEmpty()) ? valor : Moeda.converteR$Float(Moeda.converteUS$(Moeda.valorDoPercentual(valor, Moeda.converteR$Float(lsv.get(0).getDescontoAteVenc()))));
                }

                //float valorDesconto = Moeda.converteFloatR$Float(desconto * 100 / Float.parseFloat(Moeda.substituiVirgula(valor)));
                float valorDesconto = 0;//Moeda.subtracaoValores(valor_cheio, Moeda.converteUS$(valorLiquido));
                valorDesconto = Moeda.multiplicarValores(Moeda.divisaoValores(valor_desconto, valor_cheio), 100);
                matriculaAcademia.getServicoPessoa().setNrDesconto(valorDesconto);
            }
        }
        valor = Moeda.converteR$(valor);
        carregaParcelas();
    }

    public void atualizaValor() {
        calculoValor();
        calculoDesconto();
//        valor = "";
//        FunctionsDB functionsDB = new FunctionsDao();
//        valor = Float.toString(functionsDB.valorServico(aluno.getPessoa().getId(), idServico, DataHoje.dataHoje(), 0, null));
//        Dao di = new Dao();
//        AcademiaServicoValor asv = (AcademiaServicoValor) di.find(new AcademiaServicoValor(), Integer.parseInt(getListaPeriodosGrade().get(idPeriodoGrade).getDescription()));
//        if (!asv.getFormula().isEmpty()) {
//            String calculoFormula = asv.getFormula().replace("valor", valor);
//            if (!(functionsDB.scriptSimples(calculoFormula)).isEmpty()) {
//                valor = Moeda.converteR$(functionsDB.scriptSimples(calculoFormula));
//            }
//        }
//        vTaxa = functionsDB.valorServico(aluno.getPessoa().getId(), idServico, DataHoje.dataHoje(), 2, null);
    }

    public void pesquisaFisica(String tipoPesquisa) {
        GenericaSessao.put("pesquisaFisicaTipo", tipoPesquisa);
    }

    public boolean isSocio() {
        return socio;
    }

    public void setSocio(boolean socio) {
        this.socio = socio;
    }

    public boolean isDesabilitaCamposMovimento() {
        return desabilitaCamposMovimento;
    }

    public void setDesabilitaCamposMovimento(boolean desabilitaCamposMovimento) {
        this.desabilitaCamposMovimento = desabilitaCamposMovimento;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    public String getDescontoString() {
        return Moeda.converteR$Float(desconto);
    }

    public void setDescontoString(String descontoString) {
        this.desconto = Moeda.converteUS$(descontoString);
    }

    public void cobrarTaxa() {
        if (taxa == true) {
            this.valorTaxa = Moeda.converteR$Float(vTaxa);
        } else {
            this.valorTaxa = "";
        }
    }

    public Juridica getJuridica() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
            MatriculaEscolaDao med = new MatriculaEscolaDao();
            if (med.verificaPessoaEnderecoDocumento("juridica", juridica.getPessoa().getId())) {
                responsavel = juridica.getPessoa();
                if (responsavel.getId() != -1) {
                    pessoaComplemento = new PessoaComplemento();
                    pessoaComplemento = med.pesquisaDataRefPessoaComplemto(responsavel.getId());
                    if (pessoaComplemento != null) {
                        this.idDiaVencimentoPessoa = pessoaComplemento.getNrDiaVencimento();
                    }
                    matriculaAcademia.getServicoPessoa().setCobranca(juridica.getPessoa());
                }
                pegarIdServico();
                atualizaValor();
                //calculaValorLiquido();
            }
            if (matriculaAcademia.getServicoPessoa().getCobranca().getId() == -1) {
                pessoaResponsavelMemoria = responsavel;
            } else {
                if (responsavel.getId() != matriculaAcademia.getServicoPessoa().getCobranca().getId()) {
                    pessoaResponsavelMemoria = responsavel;
                }
            }
            juridica = new Juridica();
            // verificaDebitosResponsavel(matriculaAcademia.getServicoPessoa().getCobranca());
            if (matriculaAcademia.getServicoPessoa().getPessoa().getId() != -1 || matriculaAcademia.getServicoPessoa().getCobranca().getId() != -1) {
                if (new FunctionsDao().inadimplente(matriculaAcademia.getServicoPessoa().getCobranca().getId()) || new FunctionsDao().inadimplente(matriculaAcademia.getServicoPessoa().getPessoa().getId())) {
                    GenericaMensagem.fatal("Atenção", "Aluno em Débito!");
                    mensagemInadinplente = "Aluno em Débito!";
                    responsavel = new Pessoa();
                    matriculaAcademia.getServicoPessoa().setCobranca(responsavel);
                    disabled = true;
                    return null;
                }
            }
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public boolean verificaSeContribuinteInativo() {
        JuridicaDB juridicaDB = new JuridicaDBToplink();
        if (juridicaDB.empresaInativa(matriculaAcademia.getServicoPessoa().getCobranca(), "FECHOU")) {
            messageStatusEmpresa = "Empresa inátiva!";
            return true;
        }
        return false;
    }

    public String getMensagemStatusEmpresa() {
        return messageStatusEmpresa;
    }

    public void setMensagemStatusEmpresa(String messageStatusEmpresa) {
        this.messageStatusEmpresa = messageStatusEmpresa;
    }

    public String gerarMovimento() {
        if (matriculaAcademia.getId() != -1) {
            if (matriculaAcademia.getEvt() == null) {
                int periodo = matriculaAcademia.getAcademiaServicoValor().getPeriodo().getId();
                int numeroParcelas = matriculaAcademia.getNumeroParcelas();
                if (numeroParcelas == 0) {
                    numeroParcelas = 1;
                }
                if (periodo == 3) {
                    new FunctionsDao().gerarMensalidades(matriculaAcademia.getServicoPessoa().getPessoa().getId(), matriculaAcademia.getServicoPessoa().getReferenciaVigoracao());
                    if (!matriculaAcademia.isTaxa()) {
                        desabilitaCamposMovimento = true;
                        desabilitaDiaVencimento = true;
                        GenericaMensagem.warn("Validação", "Movimento gerado com sucesso");
                        return null;
                    }
                }
                if (matriculaAcademia.getServicoPessoa().getPessoa().getId() != pessoaAlunoMemoria.getId()) {
                    GenericaMensagem.warn("Validação", "Salvar o novo aluno / responsável para gerar movimentos!");
                    return null;
                }
                if (matriculaAcademia.getServicoPessoa().getCobranca().getId() != pessoaResponsavelMemoria.getId()) {
                    GenericaMensagem.warn("Validação", "Salvar o novo aluno / responsável para gerar movimentos!");
                    return null;
                }
                String vencimento;
                String referencia;
                Dao di = new Dao();
                Plano5 plano5;
                // 1 | DIÁRIO       | 1
                // 2 | SEMANAL      | 7
                // 3 | MENSAL       | 30
                // 4 | BIMESTRAL    | 60
                // 5 | TRIMESTRAL   | 90
                // 6 | SEMESTRAL    | 180
                // 7 | ANUAL        | 365
                Servicos servicos;
                int idCondicaoPagto;
                if (numeroParcelas == 1) {
                    idCondicaoPagto = 1;
                } else {
                    idCondicaoPagto = 2;
                }
                plano5 = matriculaAcademia.getServicoPessoa().getServicos().getPlano5();
                servicos = matriculaAcademia.getServicoPessoa().getServicos();
                FTipoDocumento fTipoDocumento = (FTipoDocumento) di.find(new FTipoDocumento(), matriculaAcademia.getServicoPessoa().getTipoDocumento().getId());
                setLote(
                        new Lote(
                                -1,
                                (Rotina) di.find(new Rotina(), 122),
                                "R",
                                DataHoje.data(),
                                matriculaAcademia.getServicoPessoa().getCobranca(),
                                matriculaAcademia.getServicoPessoa().getServicos().getPlano5(),
                                false,
                                "",
                                0,
                                null,
                                null,
                                null,
                                "",
                                fTipoDocumento,
                                (CondicaoPagamento) di.find(new CondicaoPagamento(), idCondicaoPagto),
                                (FStatus) di.find(new FStatus(), 1),
                                null,
                                matriculaAcademia.getServicoPessoa().isDescontoFolha(), 0));
                di.openTransaction();
                try {

                    String nrCtrBoletoResp = "";

                    for (int x = 0; x < (Integer.toString(matriculaAcademia.getServicoPessoa().getCobranca().getId())).length(); x++) {
                        nrCtrBoletoResp += 0;
                    }

                    nrCtrBoletoResp += matriculaAcademia.getServicoPessoa().getCobranca().getId();

                    String mes = matriculaAcademia.getServicoPessoa().getEmissao().substring(3, 5);
                    String ano = matriculaAcademia.getServicoPessoa().getEmissao().substring(6, 10);
                    referencia = mes + "/" + ano;

                    //if (DataHoje.qtdeDiasDoMes(Integer.parseInt(mes), Integer.parseInt(ano)) >= matriculaAcademia.getServicoPessoa().getNrDiaVencimento()) {
                    if (DataHoje.qtdeDiasDoMes(Integer.parseInt(mes), Integer.parseInt(ano)) >= idDiaParcela) {
                        if (idDiaParcela < 10) {
                            vencimento = "0" + idDiaParcela + "/" + mes + "/" + ano;
                        } else {
                            vencimento = idDiaParcela + "/" + mes + "/" + ano;
                        }
                    } else {
                        String diaSwap = Integer.toString(DataHoje.qtdeDiasDoMes(Integer.parseInt(mes), Integer.parseInt(ano)));
                        if (diaSwap.length() < 2) {
                            diaSwap = "0" + diaSwap;
                        }
                        vencimento = diaSwap + "/" + mes + "/" + ano;
                    }
                    String dataVencimento = "";
                    boolean insereTaxa = false;
                    if (isTaxa()) {
                        insereTaxa = true;
                    }
                    boolean cobrarTaxaCartao = false;
                    if (taxaCartao) {
                        cobrarTaxaCartao = true;
                    }
                    Evt evt = new Evt();
                    if (!di.save(evt)) {
                        di.rollback();
                        GenericaMensagem.warn("Sistema", "Não foi possível gerar esse movimento!");
                        return null;
                    }
                    lote.setFilial((Filial) di.find(new Filial(), 1));
                    lote.setEvt(evt);
                    matriculaAcademia.setEvt(evt);
                    if (di.save(lote)) {
                        int loop;
                        if (insereTaxa) {
                            loop = numeroParcelas + 1;
                        } else {
                            loop = numeroParcelas;
                        }
                        if (cobrarTaxaCartao) {
                            loop = loop + 1;
                        }
                        String vecimentoString = "";
                        Pessoa pessoaAluno = matriculaAcademia.getServicoPessoa().getPessoa();
                        Pessoa pessoaResponsavelTitular = matriculaAcademia.getServicoPessoa().getCobranca();
                        Pessoa pessoaResponsavel = matriculaAcademia.getServicoPessoa().getCobranca();
                        if (pessoaResponsavel.getId() == -1) {
                            di.rollback();
                            return null;
                        }
                        int b = 0;
                        for (int i = 0; i < loop; i++) {
                            float valorParcelaF;
                            float valorDescontoAteVencimento;
                            TipoServico tipoServico;
                            if (insereTaxa) {
                                tipoServico = (TipoServico) di.find(new TipoServico(), 5);
                                valorParcelaF = vTaxa;
                                valorDescontoAteVencimento = 0;
                                vecimentoString = vencimento;
                                vencimento = DataHoje.data();
                                insereTaxa = false;
                            } else if (cobrarTaxaCartao) {
                                tipoServico = (TipoServico) di.find(new TipoServico(), 5);
                                valorParcelaF = valorCartao;
                                valorDescontoAteVencimento = 0;
                                vecimentoString = vencimento;
                                vencimento = DataHoje.data();
                                cobrarTaxaCartao = false;
                            } else {
                                tipoServico = (TipoServico) di.find(new TipoServico(), 1);
                                valorDescontoAteVencimento = 0;
                                valorParcelaF = Moeda.substituiVirgulaFloat(valorLiquido);
                                if (!vecimentoString.equals("")) {
                                    vencimento = vecimentoString;
                                    vecimentoString = "";
                                }
                                mes = vencimento.substring(3, 5);
                                ano = vencimento.substring(6, 10);
                                referencia = mes + "/" + ano;
                                switch (periodo) {
                                    case 1:
                                        vencimento = DataHoje.data();
                                        break;
                                    case 4:
                                    case 5:
                                    case 6:
                                    case 7:
                                        valorParcelaF = valorParcelaF / matriculaAcademia.getNumeroParcelas();
                                        if (b > 0) {
                                            vencimento = (new DataHoje()).incrementarMeses(1, vencimento);
                                        }
                                        break;
                                }
                                b++;
                            }
                            String nrCtrBoleto = nrCtrBoletoResp + Long.toString(DataHoje.calculoDosDias(DataHoje.converte("07/10/1997"), DataHoje.converte(vencimento)));
                            setMovimento(new Movimento(
                                    -1,
                                    lote,
                                    plano5,
                                    pessoaResponsavel, // EMPRESA DO RESPONSÁVEL (SE DESCONTO FOLHA) OU RESPONSÁVEL (SE NÃO FOR DESCONTO FOLHA)
                                    matriculaAcademia.getServicoPessoa().getServicos(),
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
                                    0,
                                    new MatriculaSocios()));
                            if (!di.save(movimento)) {
                                di.rollback();
                                GenericaMensagem.warn("Sistema", "Não foi possível gerar esse movimento!");
                                return null;
                            }
                            if (matriculaAcademia.getAcademiaServicoValor().getPeriodo().getId() == 3) {
                                break;
                            }
                        }
                        if (!di.update(matriculaAcademia)) {
                            di.rollback();
                            GenericaMensagem.warn("Sistema", "Não foi possível gerar esse movimento!");
                            return null;
                        }
                        if (!di.update(matriculaAcademia.getServicoPessoa())) {
                            di.rollback();
                            GenericaMensagem.warn("Sistema", "Não foi possível gerar esse movimento!");
                            return null;
                        }
                        di.commit();
                        GenericaMensagem.info("Sucesso", "Movimentos gerados com sucesso");
                        desabilitaCamposMovimento = true;
                        desabilitaDiaVencimento = true;
                        return baixaGeral(false);
                    } else {
                        di.rollback();
                        GenericaMensagem.warn("Sistema", "Não foi possível gerar esse movimento!");
                    }
                } catch (NumberFormatException e) {
                    di.rollback();
                }
            } else {
                GenericaMensagem.warn("Sistema", "Esse movimento já foi gerado!");
            }
        } else {
            GenericaMensagem.warn("Sistema", "Pesquisar aluno!");
        }
        return null;
    }

    public String baixaGeral(boolean mensal) {
        Dao dao = new Dao();
        List<Movimento> listaMovimentoAuxiliar = new ArrayList<Movimento>();
        Movimento m = new Movimento();
        float valorx = 0;
        float descontox = 0;
        if (mensal) {

        } else {
            if (matriculaAcademia.isTaxa()) {
                for (int i = 0; i < getListaMovimentos().size(); i++) {
                    HistoricoEmissaoGuias heg = new HistoricoEmissaoGuias();
                    m = (Movimento) dao.find(new Movimento(), listaMovimentos.get(i).getId());
                    descontox = listaMovimentos.get(i).getDesconto();
                    valorx = Moeda.converteUS$(listaMovimentos.get(i).getValorString());
                    m.setMulta(listaMovimentos.get(i).getMulta());
                    m.setJuros(listaMovimentos.get(i).getJuros());
                    m.setDesconto(descontox);
                    m.setValor(listaMovimentos.get(i).getValor());
                    m.setValorBaixa(valorx);
                    listaMovimentoAuxiliar.add(m);
                    heg.setMovimento(m);
                    heg.setUsuario((Usuario) GenericaSessao.getObject("sessaoUsuario"));
                    if (i == 1) {
                        break;
                    }
                }
            } else {
                for (int i = 0; i < getListaMovimentos().size(); i++) {
                    HistoricoEmissaoGuias heg = new HistoricoEmissaoGuias();
                    m = (Movimento) dao.find(new Movimento(), listaMovimentos.get(i).getId());
                    descontox = listaMovimentos.get(i).getDesconto();
                    valorx = Moeda.converteUS$(listaMovimentos.get(i).getValorString());
                    m.setMulta(listaMovimentos.get(i).getMulta());
                    m.setJuros(listaMovimentos.get(i).getJuros());
                    m.setDesconto(descontox);
                    m.setValor(listaMovimentos.get(i).getValor());
                    m.setValorBaixa(valorx);
                    listaMovimentoAuxiliar.add(m);
                    heg.setMovimento(m);
                    heg.setUsuario((Usuario) GenericaSessao.getObject("sessaoUsuario"));
                    break;
                }
            }
            if (!listaMovimentoAuxiliar.isEmpty()) {
                GenericaSessao.put("listaMovimento", listaMovimentoAuxiliar);
                return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).baixaGeral();
            }
        }
        return null;
    }

    public void desfazerMovimento() {
        if (matriculaAcademia.getId() != -1) {
            if (matriculaAcademia.getEvt() != null) {
                if (existeMovimento()) {
                    GenericaMensagem.warn("Validação", "Movimento já possui baixa, não pode ser cancelado!");
                    return;
                }
                AcademiaDao academiaDao = new AcademiaDao();
                if (academiaDao.desfazerMovimento(matriculaAcademia)) {
                    listaMovimentos.clear();
                    desabilitaCamposMovimento = false;
                    bloqueiaComboDiaVencimento();
                    GenericaMensagem.info("Sucesso", "Transação desfeita com sucesso");
                } else {
                    GenericaMensagem.warn("Falha", "ao desfazer essa transação!");
                }
                Dao di = new Dao();
                matriculaAcademia = (MatriculaAcademia) di.find(matriculaAcademia);
            }
        }
    }

    public void gerarContrato() {
//        if (matriculaEscola.getEvt() == null) {
//            GenericaMensagem.warn("Sistema", "Necessário gerar movimento para imprimir esse contrato!");
//            return;
//        }
//        if (matriculaEscola.getId() != -1) {
//            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//            Turma turma = new Turma();
//            String contratoCurso;
//            String contratoDiaSemana = "";
//            MatriculaContratoDB dB = new MatriculaContratoDBToplink();
//            if (tipoMatricula.equals("Individual")) {
//                matriculaContrato = dB.pesquisaCodigoServico(matriculaIndividual.getCurso().getId());
//            } else {
//                matriculaContrato = dB.pesquisaCodigoServico(matriculaTurma.getTurma().getCursos().getId());
//            }
//            if (matriculaContrato == null) {
//                msgConfirma = "Não é possível gerar um contrato para este serviço. Para gerar um contrato acesse: Menu Escola > Suporte > Modelo Contrato.";
//                GenericaMensagem.warn("Sistema", msgConfirma);
//                return;
//            }
//            String horaInicial;
//            String horaFinal;
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$aluno", matriculaEscola.getAluno().getNome()));
//            FisicaDB fisicaDB = new FisicaDBToplink();
//            Fisica contratoFisica = fisicaDB.pesquisaFisicaPorPessoa(matriculaEscola.getResponsavel().getId());
//            List listaDiaSemana = new ArrayList();
//            int periodoMeses;
//            String periodoMesesExtenso;
//            if (tipoMatricula.equals("Individual")) {
//                contratoCurso = matriculaIndividual.getCurso().getDescricao();
//                if (matriculaIndividual.isSegunda()) {
//                    listaDiaSemana.add("Seg");
//                }
//                if (matriculaIndividual.isTerca()) {
//                    listaDiaSemana.add("Ter");
//                }
//                if (matriculaIndividual.isQuarta()) {
//                    listaDiaSemana.add("Qua");
//                }
//                if (matriculaIndividual.isQuinta()) {
//                    listaDiaSemana.add("Qui");
//                }
//                if (matriculaIndividual.isSexta()) {
//                    listaDiaSemana.add("Sex");
//                }
//                if (matriculaIndividual.isSabado()) {
//                    listaDiaSemana.add("Sab");
//                }
//                if (matriculaIndividual.isDomingo()) {
//                    listaDiaSemana.add("Dom");
//                }
//                horaInicial = matriculaIndividual.getInicio();
//                horaFinal = matriculaIndividual.getTermino();
//                periodoMeses = DataHoje.quantidadeMeses(matriculaIndividual.getDataInicio(), matriculaIndividual.getDataTermino());
//            } else {
//                turma = (Turma) salvarAcumuladoDB.pesquisaCodigo(matriculaTurma.getTurma().getId(), "Turma");
//                contratoCurso = matriculaTurma.getTurma().getCursos().getDescricao();
//                periodoMeses = DataHoje.quantidadeMeses(turma.getDtInicio(), turma.getDtTermino());
//                if (turma.isSegunda()) {
//                    listaDiaSemana.add("Seg");
//                }
//                if (turma.isTerca()) {
//                    listaDiaSemana.add("Ter");
//                }
//                if (turma.isQuarta()) {
//                    listaDiaSemana.add("Qua");
//                }
//                if (turma.isQuinta()) {
//                    listaDiaSemana.add("Qui");
//                }
//                if (turma.isSexta()) {
//                    listaDiaSemana.add("Sex");
//                }
//                if (turma.isSabado()) {
//                    listaDiaSemana.add("Sab");
//                }
//                if (turma.isDomingo()) {
//                    listaDiaSemana.add("Dom");
//                }
//                horaInicial = matriculaTurma.getTurma().getHoraInicio();
//                horaFinal = matriculaTurma.getTurma().getHoraTermino();
//                matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$descricao", turma.getDescricao()));
//            }
//            if (periodoMeses == 0) {
//                periodoMesesExtenso = "mês atual";
//            } else {
//                ValorExtenso valorExtenso = new ValorExtenso();
//                valorExtenso.setNumber((double) periodoMeses);
//                periodoMesesExtenso = (valorExtenso.toString()).replace("reais", "");
//            }
//            for (int i = 0; i < listaDiaSemana.size(); i++) {
//                if (i == 0) {
//                    contratoDiaSemana = listaDiaSemana.get(i).toString();
//                } else {
//                    contratoDiaSemana += " , " + listaDiaSemana.get(i).toString();
//                }
//            }
//            String enderecoAlunoString = "";
//            String bairroAlunoString = "";
//            String cidadeAlunoString = "";
//            String estadoAlunoString = "";
//            String cepAlunoString = "";
//            String enderecoResponsavelString = "";
//            String bairroResponsavelString = "";
//            String cidadeResponsavelString = "";
//            String estadoResponsavelString = "";
//            String cepResponsavelString = "";
//            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
//            PessoaEndereco pessoaEnderecoAluno = (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoaTipo(matriculaEscola.getAluno().getId(), 1);
//
//            int idTipoEndereco = -1;
//            if (pessoaEnderecoAluno != null) {
//                enderecoAlunoString = pessoaEnderecoAluno.getEndereco().getEnderecoSimplesToString() + ", " + pessoaEnderecoAluno.getNumero();
//                bairroAlunoString = pessoaEnderecoAluno.getEndereco().getBairro().getDescricao();
//                cidadeAlunoString = pessoaEnderecoAluno.getEndereco().getCidade().getCidade();
//                estadoAlunoString = pessoaEnderecoAluno.getEndereco().getCidade().getUf();
//                cepAlunoString = pessoaEnderecoAluno.getEndereco().getCep();
//            }
//            if (matriculaEscola.getResponsavel().getId() != matriculaEscola.getAluno().getId()) {
//                // Tipo Documento - CPF
//                if (matriculaEscola.getResponsavel().getTipoDocumento().getId() == 1) {
//                    idTipoEndereco = 1;
//                    // Tipo Documento - CNPJ
//                } else if (matriculaEscola.getResponsavel().getTipoDocumento().getId() == 2) {
//                    idTipoEndereco = 3;
//                }
//            } else {
//                enderecoResponsavelString = enderecoAlunoString;
//                bairroResponsavelString = bairroAlunoString;
//                cidadeResponsavelString = cidadeAlunoString;
//                estadoResponsavelString = estadoAlunoString;
//                cepResponsavelString = cepAlunoString;
//            }
//            PessoaEndereco pessoaEnderecoResponsavel = (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoaTipo(matriculaEscola.getResponsavel().getId(), idTipoEndereco);
//            if (pessoaEnderecoResponsavel != null) {
//                enderecoResponsavelString = pessoaEnderecoResponsavel.getEndereco().getEnderecoSimplesToString() + ", " + pessoaEnderecoResponsavel.getNumero();
//                bairroResponsavelString = pessoaEnderecoResponsavel.getEndereco().getBairro().getDescricao();
//                cidadeResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCidade().getCidade();
//                estadoResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCidade().getUf();
//                cepResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCep();
//            }
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cpfAluno", (matriculaEscola.getAluno().getDocumento())));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$rgAluno", (aluno.getRg())));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$responsavel", (getResponsavel().getNome())));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cpfResponsavel", (getResponsavel().getDocumento())));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$rgResponsavel", (contratoFisica.getRg())));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$curso", (contratoCurso)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$diaSemana", (contratoDiaSemana)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataInicialExtenso", (DataHoje.dataExtenso(turma.getDataInicio()))));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataFinalExtenso", (DataHoje.dataExtenso(turma.getDataTermino()))));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataExtenso", (DataHoje.dataExtenso(DataHoje.data()))));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataInicial", (turma.getDataInicio())));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$dataFinal", (turma.getDataTermino())));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$valorParcela", (Moeda.converteR$Float(matriculaEscola.getValorTotal() / matriculaEscola.getNumeroParcelas()))));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$parcelas", (Integer.toString(matriculaEscola.getNumeroParcelas()))));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$diaVencimento", (Integer.toString(matriculaEscola.getDiaVencimento()))));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$valorAteVencimento", (Moeda.converteR$Float((matriculaEscola.getValorTotal() - matriculaEscola.getDescontoAteVencimento()) / matriculaEscola.getNumeroParcelas()))));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$horaInicial", (horaInicial)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$horaFinal", (horaFinal)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$valorTotal", (Moeda.converteR$Float((matriculaEscola.getValorTotal())))));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$matricula", (Integer.toString(matriculaEscola.getId()))));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$ano", (DataHoje.livre(DataHoje.dataHoje(), "yyyy"))));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$enderecoAluno", (enderecoAlunoString)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$bairroAluno", (bairroAlunoString)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cidadeAluno", (cidadeAlunoString)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$estadoAluno", (estadoAlunoString)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cepAluno", (cepAlunoString)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$mesesExtenso", (periodoMesesExtenso)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$meses", (Integer.toString(periodoMeses))));
//            String alunoNascimento = "";
//            if (contratoFisica.getId() != -1) {
//                alunoNascimento = (contratoFisica.getNascimento());
//            }
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$nascimentoAluno", (alunoNascimento)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$emailAluno", (matriculaEscola.getAluno().getEmail1())));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$enderecoResponsavel", (enderecoResponsavelString)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$bairroResponsavel", (bairroResponsavelString)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cidadeResponsavel", (cidadeResponsavelString)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$estadoResponsavel", (estadoResponsavelString)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$cepResponsavel", (cepResponsavelString)));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$emailResponsavel", (matriculaEscola.getResponsavel().getEmail1())));
//            String valorTaxaString = "";
//            String listaValores = "";
//            String listaValoresComData = "";
//            int z = 1;
//            for (int y = 0; y < listaMovimentos.size(); y++) {
//                if (listaMovimentos.get(y).getTipoServico().getId() == 5) {
//                    valorTaxaString = Float.toString(listaMovimentos.get(y).getTaxa());
//                } else {
//                    if (z == 1) {
//                        listaValores = "Parcela nº" + z + " - Valor: R$ " + Float.toString(listaMovimentos.get(y).getValor());
//                        listaValoresComData = listaMovimentos.get(y).getVencimento() + " - Valor: R$ " + Float.toString(listaMovimentos.get(y).getValor());
//                    } else {
//                        listaValores += ", " + "Parcela nº" + z + " - Valor: R$ " + Float.toString(listaMovimentos.get(y).getValor());
//                        listaValoresComData += ", " + listaMovimentos.get(y).getVencimento() + " - Valor: R$ " + Float.toString(listaMovimentos.get(y).getValor());
//                    }
//                    z++;
//                }
//            }
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$taxa", valorTaxaString));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$listaValoresComData", listaValoresComData));
//            matriculaContrato.setDescricao(matriculaContrato.getDescricao().replace("$listaValores", listaValores));
//            try {
//                File dirFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/"));
//                if (!dirFile.exists()) {
//                    boolean success = dirFile.mkdir();
//                    if (!success) {
//                        return;
//                    }
//                }
//                String fileName = "contrato" + DataHoje.hora().hashCode() + ".pdf";
//                String filePDF = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/" + fileName);
//                File file = new File(filePDF);
//                boolean success = file.createNewFile();
//                if (success) {
//                    OutputStream os = new FileOutputStream(filePDF);
//                    HtmlToPDF.convert(matriculaContrato.getDescricao(), os);
//                    os.close();
//                    Registro reg = (Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro");
//                    String linha = reg.getUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/" + fileName;
//                    HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//                    response.sendRedirect(linha);
//                }
//            } catch (IOException e) {
//                e.getMessage();
//            } catch (DocumentException e) {
//                e.getMessage();
//            }
//        }
    }

    public void gerarCarne() throws Exception {
        if (matriculaAcademia.getEvt() != null) {
            if (listaMovimentos.size() > 0) {
                //PessoaEndereco pessoaEndereco = ((List<PessoaEndereco>) pessoaEnderecoDB.pesquisaEndPorPessoa(matriculaEscola.getFilial().getFilial().getPessoa().getId())).get(0);
                List<CarneEscola> list = new ArrayList<>();
                if (!list.isEmpty()) {
                    Jasper.PATH = "downloads";
                    Jasper.PART_NAME = "academia";
                    Jasper.printReports("/Relatorios/CARNE.jasper", "carne", list);
                }
            }
        }
    }

    public boolean isDesabilitaGeracaoContrato() {
        return desabilitaGeracaoContrato;
    }

    public void setDesabilitaGeracaoContrato(boolean desabilitaGeracaoContrato) {
        this.desabilitaGeracaoContrato = desabilitaGeracaoContrato;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<Movimento> getListaMovimentos() {
        if (listaMovimentos.isEmpty()) {
            if (matriculaAcademia.getId() != -1) {
                int count = 0;
                if (matriculaAcademia.getEvt() != null) {
                    MovimentoDB movimentoDB = new MovimentoDBToplink();
                    LoteDB loteDB = new LoteDBToplink();
                    lote = (Lote) loteDB.pesquisaLotePorEvt(matriculaAcademia.getEvt());
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

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public boolean existeMovimento() {
        if (matriculaAcademia.getEvt() != null) {
            MovimentoDB movimentoDB = new MovimentoDBToplink();
            if (!((List) movimentoDB.movimentosBaixadosPorEvt(matriculaAcademia.getEvt().getId())).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void bloqueiaComboDiaVencimento() {
        if (idFTipoDocumento == 2) {
            desabilitaDiaVencimento = true;
        } else if (idFTipoDocumento == 13) {
            desabilitaDiaVencimento = false;
        }
    }

    public boolean isDesabilitaDiaVencimento() {
        return desabilitaDiaVencimento;
    }

    public void setDesabilitaDiaVencimento(boolean desabilitaDiaVencimento) {
        this.desabilitaDiaVencimento = desabilitaDiaVencimento;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public String periodoSemanaString(MatriculaAcademia academia) {
        String periodoSemana = "";
        AcademiaDao academiaDao = new AcademiaDao();
        List<AcademiaServicoValor> list = academiaDao.listaAcademiaServicoValorPorServico(academia.getServicoPessoa().getServicos().getId());
        List<AcademiaSemana> listSemana = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listSemana.clear();
//            listSemana = academiaDao.listaAcademiaSemana(list.get(i).getAcademiaGrade().getId());
            for (int j = 0; j < listSemana.size(); j++) {
                if (j == 0) {
                    periodoSemana += listSemana.get(j).getSemana().getDescricao();
                } else {
                    periodoSemana += " - " + listSemana.get(j).getSemana().getDescricao();
                }
            }
        }
        return periodoSemana;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listaAcademia.clear();
        loadList();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listaAcademia.clear();
        loadList();
    }

    public void loadList() {
        int id = 0;
        if (idModalidadePesquisa != null) {
            try {
                id = Integer.parseInt(idModalidadePesquisa.toString());
            } catch (NumberFormatException e) {
                id = 0;
            }
        }
        if (idModalidadePesquisa != null || !descricaoPesquisa.isEmpty()) {
            AcademiaDao academiaDao = new AcademiaDao();
            listaAcademia = academiaDao.pesquisaMatriculaAcademia("", porPesquisa, comoPesquisa, descricaoPesquisa, matriculaAtiva, id);
        }
    }

    public String getMascaraPesquisa() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }

    public boolean isOcultaParcelas() {
        return ocultaParcelas;
    }

    public void setOcultaParcelas(boolean ocultaParcelas) {
        this.ocultaParcelas = ocultaParcelas;
    }

    public Pessoa getCobranca() {
        return cobranca;
    }

    public void setCobranca(Pessoa cobranca) {
        this.cobranca = cobranca;
    }

    public String semanaResumo(String descricao) {
        descricao = descricao.substring(0, 3);
        return descricao;
    }

    public Registro getRegistro() {
        if (registro != null) {
            Dao di = new Dao();
            registro = (Registro) di.find(new Registro(), 1);
            if (registro.getServicos() != null) {
                ServicoValorDB servicoValorDB = new ServicoValorDBToplink();
                List<ServicoValor> list = (List<ServicoValor>) servicoValorDB.pesquisaServicoValor(registro.getServicos().getId());
                if (!list.isEmpty()) {
                    valorCartao = list.get(0).getValor();
                }
                ocultaBotaoTarifaCartao = false;
            } else {
                ocultaBotaoTarifaCartao = true;
            }
        }
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public boolean isOcultaBotaoTarifaCartao() {
        return ocultaBotaoTarifaCartao;
    }

    public void setOcultaBotaoTarifaCartao(boolean ocultaBotaoTarifaCartao) {
        this.ocultaBotaoTarifaCartao = ocultaBotaoTarifaCartao;
    }

    public float getValorCartao() {
        return valorCartao;
    }

    public void setValorCartao(float valorCartao) {
        this.valorCartao = valorCartao;
    }

    public boolean isTaxaCartao() {
        return taxaCartao;
    }

    public void setTaxaCartao(boolean taxaCartao) {
        this.taxaCartao = taxaCartao;
    }

    public boolean isAlunoFoto() {
        if (matriculaAcademia.getServicoPessoa().getPessoa().getId() != -1) {
            File file = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + matriculaAcademia.getServicoPessoa().getPessoa().getId() + ".png"));
            alunoFoto = file.exists();
        }
        return alunoFoto;
    }

    public void setAlunoFoto(boolean alunoFoto) {
        this.alunoFoto = alunoFoto;
    }

    public int getIdDiaParcela() {
        return idDiaParcela;
    }

    public void setIdDiaParcela(int idDiaParcela) {
        this.idDiaParcela = idDiaParcela;
    }

    public List<SelectItem> getListaDiaParcela() {
        if (listaDiaParcela.isEmpty() || matriculaAcademia.getServicoPessoa().getPessoa().getId() != -1) {
            if (matriculaAcademia.getServicoPessoa().getId() == -1) {
                listaDiaParcela.clear();
                int dia;
                if (matriculaAcademia.getServicoPessoa().getPessoa().getId() != -1) {
                    PessoaDB dbp = new PessoaDBToplink();
                    pessoaComplemento = dbp.pesquisaPessoaComplementoPorPessoa(matriculaAcademia.getServicoPessoa().getPessoa().getId());

                    if (pessoaComplemento.getId() == -1) {
                        dia = getRegistro().getFinDiaVencimentoCobranca();
                    } else {
                        dia = pessoaComplemento.getNrDiaVencimento();
                    }

                } else {
                    dia = DataHoje.DataToArrayInt(matriculaAcademia.getServicoPessoa().getEmissao())[0];
                }

                for (int i = 1; i <= 31; i++) {
                    listaDiaParcela.add(new SelectItem(Integer.toString(i)));
                    if (dia == i) {
                        idDiaParcela = i;
                    }
                }
            } else {
                int dia = matriculaAcademia.getServicoPessoa().getNrDiaVencimento();
                for (int i = 1; i <= 31; i++) {
                    listaDiaParcela.add(new SelectItem(Integer.toString(i)));
                    if (dia == i) {
                        idDiaParcela = i;
                    }
                }

            }

        }
        return listaDiaParcela;
    }

    public String getLoad() {
        return "";
    }

    public boolean isMatriculaAtiva() {
        return matriculaAtiva;
    }

    public void setMatriculaAtiva(boolean matriculaAtiva) {
        this.matriculaAtiva = matriculaAtiva;
    }

    public Object getIdModalidadePesquisa() {
        return idModalidadePesquisa;
    }

    public void setIdModalidadePesquisa(Object idModalidadePesquisa) {
        this.idModalidadePesquisa = idModalidadePesquisa;
    }

    public String getDataValidade() {
        if (matriculaAcademia.getServicoPessoa().getDtEmissao() != null) {
            if (listaPeriodosGrade.isEmpty()) {
                return "";
            }
            Dao dao = new Dao();
            Periodo periodo = ((AcademiaServicoValor) dao.find(new AcademiaServicoValor(), Integer.parseInt(listaPeriodosGrade.get(idPeriodoGrade).getDescription()))).getPeriodo();
            DataHoje dh = new DataHoje();
            switch (periodo.getId()) {
                case 1:
                    dataValidade = matriculaAcademia.getServicoPessoa().getEmissao();
                    break;
                case 3:
                    dataValidade = "";
                    break;
                default:
                    dataValidade = dh.incrementarDias(periodo.getDias(), matriculaAcademia.getServicoPessoa().getEmissao());
                    break;
            }
        }
        return dataValidade;
    }

    public void setDataValidade(String dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Socios getSocios() {
        if (aluno.getId() != -1) {
            SociosDB sociosDB = new SociosDBToplink();
            if (socios.getId() == -1) {
                socios = sociosDB.pesquisaSocioPorPessoa(matriculaAcademia.getServicoPessoa().getPessoa().getId());
            }
        }
        return socios;
    }

    public void setSocios(Socios socios) {
        this.socios = socios;
    }

    public boolean isOcultaMensal() {
        if (matriculaAcademia.getAcademiaServicoValor().getPeriodo().getId() == 2) {

        }
        return true;
    }

    public Socios getSociosCobranca() {
        if (cobranca != null) {
            SociosDB sociosDB = new SociosDBToplink();
            sociosCobranca = sociosDB.pesquisaSocioPorPessoa(matriculaAcademia.getServicoPessoa().getCobranca().getId());
        }
        return sociosCobranca;
    }

    public void setSociosCobranca(Socios sociosCobranca) {
        this.sociosCobranca = sociosCobranca;
    }

    public String getMensagemInadinplente() {
        return mensagemInadinplente;
    }

    public void setMensagemInadinplente(String mensagemInadinplente) {
        this.mensagemInadinplente = mensagemInadinplente;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

}
