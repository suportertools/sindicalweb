package br.com.rtools.relatorios.beans;

import br.com.rtools.associativo.Midia;
import br.com.rtools.escola.EscStatus;
import br.com.rtools.escola.MatriculaEscola;
import br.com.rtools.escola.MatriculaIndividual;
import br.com.rtools.escola.MatriculaTurma;
import br.com.rtools.escola.Professor;
import br.com.rtools.escola.Turma;
import br.com.rtools.escola.Vendedor;
import br.com.rtools.escola.dao.TurmaDao;
import br.com.rtools.escola.lista.ListaMatriculaEscola;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RelatorioEscolaBean implements Serializable {

    private MatriculaEscola matriculaEscola = new MatriculaEscola();
    private MatriculaIndividual matriculaIndividual = new MatriculaIndividual();
    private MatriculaTurma matriculaTurma = new MatriculaTurma();
    private List<ListaMatriculaEscola> listaMatriculaEscola = new ArrayList<ListaMatriculaEscola>();
    private Pessoa aluno = new Pessoa();
    private Pessoa responsavel = new Pessoa();
    private Usuario usuario = new Usuario();
    private List<SelectItem> listaTipoRelatorios = new ArrayList<SelectItem>();
    private List<SelectItem> listaProfessores = new ArrayList<SelectItem>();
    private List<SelectItem> listaVendedores = new ArrayList<SelectItem>();
    private List<SelectItem> listaMidia = new ArrayList<SelectItem>();
    private List<SelectItem> listaStatus = new ArrayList<SelectItem>();
    private List<SelectItem> listaCursos = new ArrayList<SelectItem>();
    private int idRelatorios = 0;
    private String idCortesia = "todos";
    private int idProfessor = 0;
    private int idVendedor = 0;
    private int idStatus = 0;
    private int idMidia = 0;
    private int idCursos = 0;
    private Date dataMatriculaInicial = DataHoje.dataHoje();
    private Date dataMatriculaFinal = DataHoje.dataHoje();
    private Date dataInicioCurso = DataHoje.dataHoje();
    private Date dataFinalCurso = DataHoje.dataHoje();
    private boolean porAluno = false;
    private boolean porResponsavel = false;
    private boolean porProfessor = false;
    private boolean porVendedor = false;
    private boolean porMatricula = false;
    private boolean tipoMatricula = false;
    private boolean porPeriodoCurso = false;
    private boolean porMidia = false;
    private String tipoRelatorio = "Simples";
    private String indexAccordion = "Simples";

    public void visualizar() {
        Relatorios relatorios = null;
        if (!getListaTipoRelatorios().isEmpty()) {
            RelatorioDao rgdb = new RelatorioDao();
            relatorios = rgdb.pesquisaRelatorios(Integer.parseInt(listaTipoRelatorios.get(idRelatorios).getDescription()));
        }
        if (relatorios == null) {
            return;
        }
//        if (parametroConviteClubes.isEmpty()) {
//            ConviteDB conviteDB = new ConviteDBToplink();
//            int pSisPessoaI = 0;
//            int pPessoaI = 0;
//            int pDiretorI = 0;
//            int pOperadorI = 0;
//            String pEmissaoIStringI = "";
//            String pEmissaoFStringI = "";
//            String pValidadeIStringI = "";
//            String pValidadeFStringI = "";
//            if (convidado) {
//                if (sisPessoa.getId() != -1) {
//                    pSisPessoaI = sisPessoa.getId();
//                }
//            }
//            if (socio) {
//                if (fisica.getId() != -1) {
//                    pPessoaI = fisica.getPessoa().getId();
//                }
//            }
//            if (diretor) {
//                if (!listaDiretores.isEmpty()) {
//                    SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
//                    ConviteAutorizaCortesia cac = (ConviteAutorizaCortesia) sadb.pesquisaObjeto(Integer.parseInt(listaDiretores.get(idDiretor).getDescription()), "ConviteAutorizaCortesia");
//                    if(cac != null) {
//                        pDiretorI = cac.getPessoa().getId();                        
//                    }
//                }
//            }
//            if (operador) {
//                if (!listaOperadores.isEmpty()) {
//                    pOperadorI = Integer.parseInt(listaOperadores.get(idOperador).getDescription());
//                }
//            }
//            if (emissao) {
//                pEmissaoIStringI = DataHoje.converteData(dataEmissaoInicial);
//                pEmissaoFStringI = DataHoje.converteData(dataEmissaoFinal);
//            }
//            if (validade) {
//                pEmissaoIStringI = DataHoje.converteData(dataValidadeInicial);
//                pEmissaoFStringI = DataHoje.converteData(dataValidadeFinal);
//            }
//            List list = conviteDB.filtroRelatorio(
//                    pSisPessoaI,
//                    pPessoaI,
//                    pDiretorI,
//                    pOperadorI,
//                    pEmissaoIStringI,
//                    pEmissaoFStringI,
//                    pValidadeIStringI,
//                    pValidadeFStringI,
//                    idCortesia,
//                    "",
//                    relatorios
//            );
//            for (Object list1 : list) {
//                String cortesiaString = GenericaString.converterNullToString(((List) list1).get(9));
//                if(cortesiaString.equals("false")) {
//                    cortesiaString = "";
//                } else if (cortesiaString.equals("true")) {
//                    cortesiaString = "Sim";                    
//                }
//                String valor = GenericaString.converterNullToString(((List) list1).get(7));
//                if(valor.isEmpty()) {
//                    valor = "0";
//                }
//                String valorPago = GenericaString.converterNullToString(((List) list1).get(8));
//                if(valorPago.isEmpty()) {
//                    valorPago = "0";
//                }
//                ParametroConviteClube parametroConviteClube
//                        = new ParametroConviteClube(
//                                GenericaString.converterNullToString(((List) list1).get(0)),
//                                GenericaString.converterNullToString(((List) list1).get(1)),
//                                GenericaString.converterNullToString(((List) list1).get(2)),
//                                GenericaString.converterNullToString(((List) list1).get(3)),
//                                GenericaString.converterNullToString(((List) list1).get(4)),
//                                GenericaString.converterNullToString(((List) list1).get(5)),
//                                GenericaString.converterNullToString(((List) list1).get(6)),
//                                new BigDecimal(valor),
//                                new BigDecimal(valorPago),
//                                cortesiaString,
//                                GenericaString.converterNullToString(((List) list1).get(10))
//                        );
//                parametroConviteClubes.add(parametroConviteClube);
//            }
//
//        }
//        if (parametroConviteClubes.isEmpty()) {
//            GenericaMensagem.info("Sistema", "Não existem registros para o relatório selecionado");
//            return;
//        }
    }

    public List<SelectItem> getListaTipoRelatorios() {
        if (listaTipoRelatorios.isEmpty()) {
            RelatorioDao db = new RelatorioDao();
            List<Relatorios> list = (List<Relatorios>) db.pesquisaTipoRelatorio(226);
            for (int i = 0; i < list.size(); i++) {
                listaTipoRelatorios.add(new SelectItem(i, list.get(i).getNome(), "" + list.get(i).getId()));
            }
        }
        return listaTipoRelatorios;
    }

    public int getIdRelatorios() {
        return idRelatorios;
    }

    public void setIdRelatorios(int idRelatorios) {
        this.idRelatorios = idRelatorios;
    }

    public void setListaTipoRelatorios(List<SelectItem> listaTipoRelatorios) {
        this.listaTipoRelatorios = listaTipoRelatorios;
    }

    public Pessoa getAluno() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            aluno = ((Fisica) GenericaSessao.getObject("fisicaPesquisa", true)).getPessoa();
        }
        return aluno;
    }

    public void setAluno(Pessoa aluno) {
        this.aluno = aluno;
    }

    public Pessoa getResponsavel() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            responsavel = ((Juridica) GenericaSessao.getObject("juridicaPesquisa", true)).getPessoa();
        }
        return responsavel;
    }

    public void setPessoa(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getIdCortesia() {
        return idCortesia;
    }

    public void setIdCortesia(String idCortesia) {
        this.idCortesia = idCortesia;
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }

    public void tipoRelatorioChange(TabChangeEvent event) {
        tipoRelatorio = event.getTab().getTitle();
        indexAccordion = ((AccordionPanel) event.getComponent()).getActiveIndex();
        if (tipoRelatorio.equals("Simples")) {
            limpar();
            porAluno = false;
            porResponsavel = false;
            porProfessor = false;
            porVendedor = false;
            porMatricula = false;
        }
    }

    public List<SelectItem> getListaVendedores() {
        if (listaVendedores.isEmpty()) {
            Dao dao = new Dao();
            List<Vendedor> list = (List<Vendedor>) dao.list(new Vendedor());
            int i = 0;
            for (Vendedor v : list) {
                listaVendedores.add(new SelectItem(i, v.getPessoa().getNome(), "" + v.getId()));
                i++;
            }
        }
        return listaVendedores;
    }

    public void setListaVendedores(List<SelectItem> listaVendedores) {
        this.listaVendedores = listaVendedores;
    }

    public List<SelectItem> getListaProfessores() {
        if (listaProfessores.isEmpty()) {
            Dao dao = new Dao();
            List<Professor> list = (List<Professor>) dao.list(new Professor());
            int i = 0;
            for (Professor p : list) {
                listaProfessores.add(new SelectItem(i, p.getProfessor().getNome(), "" + p.getId()));
                i++;
            }
        }
        return listaProfessores;
    }

    public void setListaProfessores(List<SelectItem> listaProfessores) {
        this.listaProfessores = listaProfessores;
    }

    public void selecionaDataMatriculaInicial(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataMatriculaInicial = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataMatriculaFinal(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataMatriculaFinal = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataInicioCurso(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataInicioCurso = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataFinalCurso(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataFinalCurso = DataHoje.converte(format.format(event.getObject()));
    }

    public void limpar() {
        if (!porAluno) {
            aluno = new Pessoa();
        }
        if (!porResponsavel) {
            responsavel = new Pessoa();
        }
        if (!porVendedor) {
            listaVendedores.clear();
            idVendedor = 0;
        }
        if (!porProfessor) {
            listaProfessores.clear();
            idProfessor = 0;
        }
        if (!porMatricula) {
            dataMatriculaInicial = DataHoje.dataHoje();
            dataMatriculaFinal = DataHoje.dataHoje();
        }
        if (!porPeriodoCurso) {
            dataInicioCurso = DataHoje.dataHoje();
            dataFinalCurso = DataHoje.dataHoje();
        }
    }

    public void close(String close) {
        switch (close) {
            case "aluno":
                aluno = new Pessoa();
                porAluno = false;
                break;
            case "resposanvel":
                responsavel = new Pessoa();
                porResponsavel = false;
                break;
            case "vendedor":
                listaVendedores.clear();
                idVendedor = 0;
                porVendedor = false;
                break;
            case "professor":
                listaProfessores.clear();
                idProfessor = 0;
                porProfessor = false;
                break;
            case "matricula":
                dataMatriculaInicial = DataHoje.dataHoje();
                dataMatriculaFinal = DataHoje.dataHoje();
                porMatricula = false;
                break;
            case "validade":
                dataInicioCurso = DataHoje.dataHoje();
                dataFinalCurso = DataHoje.dataHoje();
                porPeriodoCurso = false;
                break;
        }
        RequestContext.getCurrentInstance().update("form_relatorio:id_panel");
    }

    public String getIndexAccordion() {
        return indexAccordion;
    }

    public void setIndexAccordion(String indexAccordion) {
        this.indexAccordion = indexAccordion;
    }

    public MatriculaEscola getMatriculaEscola() {
        return matriculaEscola;
    }

    public void setMatriculaEscola(MatriculaEscola matriculaEscola) {
        this.matriculaEscola = matriculaEscola;
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

    public List<ListaMatriculaEscola> getListaMatriculaEscola() {
        return listaMatriculaEscola;
    }

    public void setListaMatriculaEscola(List<ListaMatriculaEscola> listaMatriculaEscola) {
        this.listaMatriculaEscola = listaMatriculaEscola;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public int getIdProfessor() {
        return idProfessor;
    }

    public void setIdProfessor(int idProfessor) {
        this.idProfessor = idProfessor;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Date getDataMatriculaInicial() {
        return dataMatriculaInicial;
    }

    public void setDataMatriculaInicial(Date dataMatriculaInicial) {
        this.dataMatriculaInicial = dataMatriculaInicial;
    }

    public Date getDataMatriculaFinal() {
        return dataMatriculaFinal;
    }

    public void setDataMatriculaFinal(Date dataMatriculaFinal) {
        this.dataMatriculaFinal = dataMatriculaFinal;
    }

    public Date getDataInicioCurso() {
        return dataInicioCurso;
    }

    public void setDataInicioCurso(Date dataInicioCurso) {
        this.dataInicioCurso = dataInicioCurso;
    }

    public Date getDataFinalCurso() {
        return dataFinalCurso;
    }

    public void setDataFinalCurso(Date dataFinalCurso) {
        this.dataFinalCurso = dataFinalCurso;
    }

    public boolean isPorAluno() {
        return porAluno;
    }

    public void setPorAluno(boolean porAluno) {
        this.porAluno = porAluno;
    }

    public boolean isPorResponsavel() {
        return porResponsavel;
    }

    public void setPorResponsavel(boolean porResponsavel) {
        this.porResponsavel = porResponsavel;
    }

    public boolean isPorProfessor() {
        return porProfessor;
    }

    public void setPorProfessor(boolean porProfessor) {
        this.porProfessor = porProfessor;
    }

    public boolean isPorVendedor() {
        return porVendedor;
    }

    public void setPorVendedor(boolean porVendedor) {
        this.porVendedor = porVendedor;
    }

    public boolean isPorMatricula() {
        return porMatricula;
    }

    public void setPorMatricula(boolean porMatricula) {
        this.porMatricula = porMatricula;
    }

    public boolean isPorPeriodoCurso() {
        return porPeriodoCurso;
    }

    public void setPorPeriodoCurso(boolean porPeriodoCurso) {
        this.porPeriodoCurso = porPeriodoCurso;
    }

    public List<SelectItem> getListaMidia() {
        if (listaMidia.isEmpty()) {
            Dao dao = new Dao();
            List<Midia> list = (List<Midia>) dao.list(new Midia());
            for (int i = 0; i < list.size(); i++) {
                listaMidia.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listaMidia;
    }

    public void setListaMidia(List<SelectItem> listaMidia) {
        this.listaMidia = listaMidia;
    }

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()) {
            Dao dao = new Dao();
            List<EscStatus> list = (List<EscStatus>) dao.list(new EscStatus());
            for (int i = 0; i < list.size(); i++) {
                listaStatus.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listaStatus;
    }

    public void setListaStatus(List<SelectItem> listaStatus) {
        this.listaStatus = listaStatus;
    }

    public List<SelectItem> getListaCursos() {
        if (listaCursos.isEmpty()) {
            TurmaDao td = new TurmaDao();
            List<Turma> turmas = (List<Turma>) td.listaTurmaAtivaPorFilial(MacFilial.getAcessoFilial().getFilial().getId());
            for (int i = 0; i < turmas.size(); i++) {
                listaStatus.add(new SelectItem(i, turmas.get(i).getDescricao(), "" + turmas.get(i).getId()));
            }
        }
        return listaCursos;
    }

    public void setListaCursos(List<SelectItem> listaCursos) {
        this.listaCursos = listaCursos;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getIdMidia() {
        return idMidia;
    }

    public void setIdMidia(int idMidia) {
        this.idMidia = idMidia;
    }

    public int getIdCursos() {
        return idCursos;
    }

    public void setIdCursos(int idCursos) {
        this.idCursos = idCursos;
    }

    public boolean isTipoMatricula() {
        return tipoMatricula;
    }

    public void setTipoMatricula(boolean tipoMatricula) {
        this.tipoMatricula = tipoMatricula;
    }

}
