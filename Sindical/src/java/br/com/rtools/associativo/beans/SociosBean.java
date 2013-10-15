package br.com.rtools.associativo.beans;

import br.com.rtools.arrecadacao.GrupoCidades;
import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.associativo.*;
import br.com.rtools.associativo.db.*;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.beans.FisicaBean;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

@ManagedBean
@SessionScoped
public class SociosBean implements Serializable {

    private ServicoPessoa servicoPessoa;
    private ServicoCategoria servicoCategoria;
    private Socios socios;
    private MatriculaSocios matriculaSocios;
    private PessoaEmpresa pessoaEmpresa;
    private Fisica dependente;
    private boolean chkContaCobranca;
    private List<SelectItem> listaTipoDocumento;
    private List<SelectItem> listaServicos;
    private List<SelectItem> listaParentesco;
    private List<DataObject> listaDependentes;
    private List<SelectItem> listaMotivoInativacao;
    private int idTipoDocumento;
    private int idServico;
    private int idGrupoCategoria;
    private int idCategoria;
    private int idIndexDep;
    private int idIndexCombo;
    private int idInativacao;
    private boolean renderServicos;
    private boolean fotoTemp;
    private boolean temFoto;
    private boolean desabilitaImpressao;
    private boolean imprimirVerso;
    private String msgConfirma;
    private String pesquisaLista;
    private String lblSocio;
    private String lblSocioPergunta;
    private String tipoDestinario;
    private String dataInativacao;
    private String dataReativacao;
    private String statusSocio;
    private List<Fisica> listaFisica;

    public SociosBean() {
        servicoPessoa = new ServicoPessoa();
        servicoCategoria = new ServicoCategoria();
        socios = new Socios();
        matriculaSocios = new MatriculaSocios();
        pessoaEmpresa = new PessoaEmpresa();
        dependente = new Fisica();
        chkContaCobranca = false;
        listaTipoDocumento = new ArrayList();
        listaServicos = new ArrayList();
        listaParentesco = new ArrayList();
        listaDependentes = new ArrayList();
        idTipoDocumento = 0;
        idServico = 0;
        idGrupoCategoria = 0;
        idCategoria = 0;
        idIndexDep = -1;
        idIndexCombo = -1;
        idInativacao = 0;
        renderServicos = true;
        fotoTemp = false;
        temFoto = false;
        desabilitaImpressao = true;
        imprimirVerso = false;
        dataInativacao = DataHoje.data();
        dataReativacao = DataHoje.data();
        msgConfirma = "";
        pesquisaLista = "";
        lblSocio = "";
        lblSocioPergunta = "";
        tipoDestinario = "socio";
        statusSocio = "NOVO";
        listaFisica = new ArrayList();
        listaMotivoInativacao = new ArrayList<SelectItem>();
    }

    public String inativarSocio() {
        if (socios.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            SociosDB db = new SociosDBToplink();
            if (dataInativacao.length() < 10) {
                msgConfirma = "Data de inativação inválida!";
                return null;
            }

            if (DataHoje.converteDataParaInteger(dataInativacao) > DataHoje.converteDataParaInteger(DataHoje.data())) {
                msgConfirma = "Data de inativação maior que dia de hoje!";
                return null;
            }

            sv.abrirTransacao();

            ServicoPessoa sp = (ServicoPessoa) sv.pesquisaCodigo(servicoPessoa.getId(), "ServicoPessoa");
            sp.setAtivo(false);
            List<Socios> listaDps = db.pesquisaDependentes(matriculaSocios.getId());
            if (!sv.alterarObjeto(sp)) {
                msgConfirma = "Erro ao alterar serviço pessoa";
                sv.desfazerTransacao();
                return null;
            }
            servicoPessoa = sp;
            ServicoPessoa sp2 = new ServicoPessoa();
            for (int i = 0; i < listaDps.size(); i++) {
                sp2 = (ServicoPessoa) sv.pesquisaCodigo(listaDps.get(i).getServicoPessoa().getId(), "ServicoPessoa");
                sp2.setAtivo(false);
                if (!sv.alterarObjeto(sp2)) {
                    msgConfirma = "Erro ao alterar serviço pessoa do Dependente";
                    sv.desfazerTransacao();
                    return null;
                }
                sp2 = new ServicoPessoa();
            }

            matriculaSocios.setMotivoInativacao((SMotivoInativacao) sv.pesquisaCodigo(Integer.parseInt(listaMotivoInativacao.get(idInativacao).getDescription()), "SMotivoInativacao"));
            matriculaSocios.setInativo(dataInativacao);
            if (!sv.alterarObjeto(matriculaSocios)) {
                msgConfirma = "Erro ao alterar matrícula";
                sv.desfazerTransacao();
                return null;
            }

            msgConfirma = "Sócio inativado com sucesso!";
            sv.comitarTransacao();
        } else {
            msgConfirma = "Não existe sócio para ser inativado!";
        }
        return null;
    }

    public String reativarSocio() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        SociosDB db = new SociosDBToplink();

        if (db.pesquisaSocioPorPessoaAtivo(socios.getServicoPessoa().getPessoa().getId()).getId() != -1) {
            msgConfirma = "Este sócio já esta cadastrado!";
            return null;
        }

        sv.abrirTransacao();

        ServicoPessoa sp = (ServicoPessoa) sv.pesquisaCodigo(servicoPessoa.getId(), "ServicoPessoa");
        sp.setAtivo(true);
        if (!sv.alterarObjeto(sp)) {
            msgConfirma = "Erro ao alterar serviço pessoa";
            sv.desfazerTransacao();
            return null;
        }
        servicoPessoa = sp;
        ServicoPessoa sp2 = new ServicoPessoa();
        List<Socios> listaDps = db.pesquisaDependentes(matriculaSocios.getId());
        for (int i = 0; i < listaDps.size(); i++) {
            sp2 = (ServicoPessoa) sv.pesquisaCodigo(listaDps.get(i).getServicoPessoa().getId(), "ServicoPessoa");
            sp2.setAtivo(true);
            if (!sv.alterarObjeto(sp2)) {
                msgConfirma = "Erro ao alterar serviço pessoa do Dependente";
                sv.desfazerTransacao();
                return null;
            }
            sp2 = new ServicoPessoa();
        }
        matriculaSocios.setMotivoInativacao(null);
        matriculaSocios.setDtInativo(null);
        if (!sv.alterarObjeto(matriculaSocios)) {
            msgConfirma = "Erro ao ativar matrícula";
            sv.desfazerTransacao();
            return null;
        }
        msgConfirma = "Sócio ativado com sucesso!";
        dataInativacao = DataHoje.data();
        sv.comitarTransacao();
        return null;
    }

    public String salvar() {
        if (!validaSalvar()) {
            return null;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        GrupoCategoriaDB dbGCat = new GrupoCategoriaDBToplink();
        GrupoCategoria gpCategoria = new GrupoCategoria();
        FTipoDocumentoDB dbFTipo = new FTipoDocumentoDBToplink();
        MatriculaSociosDB dbMat = new MatriculaSociosDBToplink();
        ParentescoDB dbPar = new ParentescoDBToplink();

        gpCategoria = dbGCat.pesquisaCodigo(Integer.parseInt(getListaGrupoCategoria().get(idGrupoCategoria).getDescription()));

        sv.abrirTransacao();
        try {
            servicoPessoa.setTipoDocumento(dbFTipo.pesquisaCodigo(Integer.parseInt(getListaTipoDocumento().get(idTipoDocumento).getDescription())));
        } catch (Exception e) {
            servicoPessoa.setTipoDocumento(dbFTipo.pesquisaCodigo(Integer.parseInt(getListaTipoDocumento().get(0).getDescription())));
        }

        // NOVO REGISTRO -----------------------
        if (servicoPessoa.getId() == -1) {
            servicoPessoa.setAtivo(true);
            if (!sv.inserirObjeto(servicoPessoa)) {
                msgConfirma = "Erro ao salvar serviço Pessoa!";
                return null;
            }
            matriculaSocios.setMotivoInativacao(null);
        } else {
            // ATUALIZA REGISTRO -------------------
            if (!sv.alterarObjeto(servicoPessoa)) {
                msgConfirma = "Erro ao alterar serviço Pessoa!";
                return null;
            }
        }

        matriculaSocios.setCategoria(servicoCategoria.getCategoria());
        matriculaSocios.setTitular(servicoPessoa.getPessoa());
        if (matriculaSocios.getNrMatricula() <= 0) {
            // MATRICULA MENOR QUE ZERO 
            matriculaSocios.setNrMatricula(gpCategoria.getNrProximaMatricula());
            gpCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula() + 1);

        } else if (matriculaSocios.getNrMatricula() > gpCategoria.getNrProximaMatricula()) {
            // MATRICULA MAIOR QUE A PROXIMA DA CATEGORIA 
            matriculaSocios.setNrMatricula(gpCategoria.getNrProximaMatricula());
            gpCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula() + 1);

        } else if (matriculaSocios.getNrMatricula() < gpCategoria.getNrProximaMatricula()
                && // MATRICULA MENOR QUE A PROXIMA DA CATEGORIA E NÃO EXISTIR UMA IGUAL 
                dbMat.pesquisaPorNrMatricula(matriculaSocios.getNrMatricula(), servicoCategoria.getCategoria().getId()) != null) {
            matriculaSocios.setNrMatricula(gpCategoria.getNrProximaMatricula());
            gpCategoria.setNrProximaMatricula(matriculaSocios.getNrMatricula() + 1);

        } else if (matriculaSocios.getNrMatricula() < gpCategoria.getNrProximaMatricula()
                && dbMat.pesquisaPorNrMatricula(matriculaSocios.getNrMatricula(), servicoCategoria.getCategoria().getId()) == null) {
            // MATRICULA MENOR QUE A PROXIMA DA CATEGORIA E NÃO EXISTIR
            //////////////////////////////////// NAO FAZ NADA
        }

        if (matriculaSocios.getId() == -1) {
            if (!sv.inserirObjeto(matriculaSocios)) {
                msgConfirma = "Erro ao salvar matrícula!";
                sv.desfazerTransacao();
                return null;
            }
        } else {
            if (!sv.alterarObjeto(matriculaSocios)) {
                msgConfirma = "Erro ao atualizar matrícula!";
                sv.desfazerTransacao();
                return null;
            }
        }
        sv.alterarObjeto(gpCategoria);

        socios.setMatriculaSocios(matriculaSocios);
        socios.setParentesco((Parentesco) sv.pesquisaCodigo(1, "Parentesco"));
        socios.setServicoPessoa(servicoPessoa);
        socios.setNrViaCarteirinha(1);

        if (socios.getId() == -1) {
            if (!sv.inserirObjeto(socios)) {
                msgConfirma = "Erro ao salvar sócio!";
                sv.desfazerTransacao();
                return null;
            }
            msgConfirma = "Pessoa associada com sucesso!";
        } else {
            if (!sv.alterarObjeto(socios)) {
                msgConfirma = "Erro ao atualizar sócio!";
                sv.desfazerTransacao();
                return null;
            }
            msgConfirma = "Cadastro atualizado com sucesso!";
        }

        if (!listaDependentes.isEmpty()) {
            Socios socioDependente = new Socios();
            SociosDB db = new SociosDBToplink();

            ServicoCategoriaDB dbSCat = new ServicoCategoriaDBToplink();
            Parentesco parentesco = new Parentesco();
            for (int i = 0; i < listaDependentes.size(); i++) {
                if (((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getId() != -1) {
                    socioDependente = db.pesquisaSocioPorPessoaAtivo(((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getId());
                    parentesco = dbPar.pesquisaCodigo(Integer.parseInt(getListaParentesco().get(Integer.parseInt((String) ((DataObject) listaDependentes.get(i)).getArgumento1())).getDescription()));
                    ServicoCategoria servicoCategoriaDep = dbSCat.pesquisaPorParECat(parentesco.getId(), servicoCategoria.getCategoria().getId());

                    if (servicoCategoriaDep == null) {
                        msgConfirma = "Erro para Serviço Categoria: " + ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getNome();
                        sv.desfazerTransacao();
                        return null;
                    }
                    if (socioDependente.getId() == -1) {
                        ServicoPessoa servicoPessoaDependente = new ServicoPessoa(-1,
                                servicoPessoa.getEmissao(),
                                ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa(),
                                servicoPessoa.isDescontoFolha(),
                                servicoPessoa.getServicos(),
                                servicoPessoa.getNrDesconto(),
                                servicoPessoa.getReferenciaVigoracao(),
                                servicoPessoa.getReferenciaValidade(),
                                servicoPessoa.getNrDiaVencimento(),
                                servicoPessoa.getTipoDocumento(),
                                servicoPessoa.getCobranca(),
                                servicoPessoa.isAtivo(),
                                servicoPessoa.isBanco());

                        if (!sv.inserirObjeto(servicoPessoaDependente)) {
                            msgConfirma = "Erro ao salvar Serviço Pessoa: " + ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getNome();
                            sv.desfazerTransacao();
                            return null;
                        }

                        socioDependente = new Socios(-1,
                                matriculaSocios,
                                servicoPessoaDependente,
                                parentesco,
                                Integer.parseInt((String) ((DataObject) listaDependentes.get(i)).getArgumento2()),
                                DataHoje.converte((String) ((DataObject) listaDependentes.get(i)).getArgumento3()));

                        if (!sv.inserirObjeto(socioDependente)) {
                            msgConfirma = "Erro ao salvar sócio: " + ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getNome();
                            sv.desfazerTransacao();
                            return null;
                        }
                    } else {
                        ServicoPessoaDB dbSerP = new ServicoPessoaDBToplink();
                        ServicoPessoa servicoPessoaDependente = dbSerP.pesquisaServicoPessoaPorPessoa(((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getId());

                        servicoPessoaDependente.setEmissao(servicoPessoa.getEmissao());
                        servicoPessoaDependente.setPessoa(((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa());
                        servicoPessoaDependente.setDescontoFolha(servicoPessoa.isDescontoFolha());
                        servicoPessoaDependente.setServicos(servicoPessoa.getServicos());
                        servicoPessoaDependente.setNrDesconto(servicoPessoa.getNrDesconto());
                        servicoPessoaDependente.setReferenciaVigoracao(servicoPessoa.getReferenciaVigoracao());
                        servicoPessoaDependente.setReferenciaValidade(servicoPessoa.getReferenciaValidade());
                        servicoPessoaDependente.setNrDiaVencimento(servicoPessoa.getNrDiaVencimento());
                        servicoPessoaDependente.setTipoDocumento(servicoPessoa.getTipoDocumento());
                        servicoPessoaDependente.setCobranca(servicoPessoa.getCobranca());
                        servicoPessoaDependente.setAtivo(servicoPessoa.isAtivo());
                        servicoPessoaDependente.setBanco(servicoPessoa.isBanco());

                        if (!sv.alterarObjeto(servicoPessoaDependente)) {
                            msgConfirma = "Erro ao alterar Serviço Pessoa: " + ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getNome();
                            sv.desfazerTransacao();
                            return null;
                        }

                        socioDependente.setValidadeCarteirinha((String) ((DataObject) listaDependentes.get(i)).getArgumento3());
                        socioDependente.setServicoPessoa(servicoPessoaDependente);
                        socioDependente.setMatriculaSocios(matriculaSocios);
                        socioDependente.setNrViaCarteirinha(Integer.parseInt((String) ((DataObject) listaDependentes.get(i)).getArgumento2()));
                        socioDependente.setParentesco(parentesco);

                        if (!sv.alterarObjeto(socioDependente)) {
                            msgConfirma = "Erro ao salvar sócio: " + ((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getNome();
                            sv.desfazerTransacao();
                            return null;
                        }
                    }

                }
            }
        }
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("socioPesquisa",socios);
        //((FisicaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).setLblSocio("CADASTRO");
        ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).setSocios(socios);
        sv.comitarTransacao();
        return null;
    }

    public String editarTitular() {
        FisicaDB db = new FisicaDBToplink();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaBean());
        ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).editarFisicaParametro(db.pesquisaFisicaPorPessoa(socios.getServicoPessoa().getPessoa().getId()));
        ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).setSocios(socios);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return "pessoaFisica";
    }

    public boolean validaSalvar() {
        if (matriculaSocios.getEmissao().isEmpty()) {
            msgConfirma = "Data de emissáo inválida!";
            return false;
        }

        if (getListaGrupoCategoria().isEmpty()) {
            msgConfirma = "Lista de Grupos Categoria Vazia!";
            return false;
        }

        if (getListaCategoria().isEmpty()) {
            msgConfirma = "Lista de Categoria Vazia!";
            return false;
        }

        if (listaTipoDocumento.isEmpty()) {
            msgConfirma = "Lista de Tipo Documentos Vazia!";
            return false;
        }

        ServicoCategoriaDB dbSCat = new ServicoCategoriaDBToplink();
        int idCat = Integer.parseInt(getListaCategoria().get(idCategoria).getDescription());
        servicoCategoria = dbSCat.pesquisaPorParECat(1, idCat);
        if (servicoCategoria == null) {
            msgConfirma = "Não existe Serviço Categoria cadastrado!";
            return false;
        } else {
            servicoPessoa.setServicos(servicoCategoria.getServicos());
        }

        if (servicoPessoa.isDescontoFolha()) {
            if (pessoaEmpresa.getId() == -1) {
                msgConfirma = "Este sócio não possui empresa para desconto em folha!";
                servicoPessoa.setDescontoFolha(false);
                return false;
            }
            servicoPessoa.setCobranca(pessoaEmpresa.getJuridica().getPessoa());
        } else {
            servicoPessoa.setCobranca(servicoPessoa.getPessoa());
        }


        SociosDB db = new SociosDBToplink();
        if ((servicoPessoa.getId() == -1) && (db.pesquisaSocioPorPessoaAtivo(servicoPessoa.getPessoa().getId()).getId() != -1)) {
            msgConfirma = "Esta pessoa já um sócio Cadastrado!";
            return false;
        }

        ServicoContaCobrancaDB dbSCB = new ServicoContaCobrancaDBToplink();
        if (chkContaCobranca) {
            List l = dbSCB.pesquisaServPorIdServIdTipoServ(servicoCategoria.getServicos().getId(), 1);
            if (!l.isEmpty()) {
                servicoPessoa.setBanco(true);
            } else {
                servicoPessoa.setBanco(false);
                msgConfirma = "Não existe Serviço Conta Cobrança!";
                return false;
            }
        } else {
            servicoPessoa.setBanco(false);
        }

        return true;
    }

    public String salvarFisicaDependente() {
        if (!validaSalvarDependente()) {
            return null;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        TipoDocumentoDB dbDoc = new TipoDocumentoDBToplink();

        if (temFoto) {
            dependente.setDataFoto(DataHoje.data());
        } else {
            dependente.setDtFoto(null);
        }

        if (dependente.getId() == -1) {
            dependente.getPessoa().setTipoDocumento(dbDoc.pesquisaCodigo(1));
            sv.abrirTransacao();
            if (!sv.inserirObjeto(dependente.getPessoa())) {
                msgConfirma = "Erro ao salvar Pessoa!";
                sv.desfazerTransacao();
                return null;
            }

            if (!sv.inserirObjeto(dependente)) {
                msgConfirma = "Erro ao salvar Cadastro!";
                sv.desfazerTransacao();
                return null;
            }
            sv.comitarTransacao();
            //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaPesquisa",dependente);
            msgConfirma = "Dependente salvo com sucesso!";
        } else {
            sv.abrirTransacao();
            if (!sv.alterarObjeto(dependente.getPessoa())) {
                msgConfirma = "Erro ao atualizar Pessoa!";
                sv.desfazerTransacao();
                return null;
            }

            if (!sv.alterarObjeto(dependente)) {
                msgConfirma = "Erro ao atualizar Cadastro!";
                sv.desfazerTransacao();
                return null;
            }
            sv.comitarTransacao();
            ((DataObject) listaDependentes.get(idIndexDep)).setArgumento0(dependente);
            msgConfirma = "Dependente atualizado com sucesso!";
        }
        return null;
    }

    public boolean validaSalvarDependente() {
        if (dependente.getNascimento().isEmpty() || dependente.getNascimento().length() < 10) {
            msgConfirma = "Data de Nascimento esta inválida!";
            return false;
        }
        if (dependente.getPessoa().getNome().equals("")) {
            msgConfirma = "O campo nome não pode ser nulo! ";
            return false;
        }
        if (!dependente.getPessoa().getDocumento().isEmpty() && !dependente.getPessoa().getDocumento().equals("0")) {
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(dependente.getPessoa().getDocumento()))) {
                msgConfirma = "Documento Inválido!";
                return false;
            }
        }
        List listDocumento = new ArrayList();
        FisicaDB db = new FisicaDBToplink();
        if (dependente.getId() == -1) {
            if (!db.pesquisaFisicaPorNomeNascRG(dependente.getPessoa().getNome(),
                    dependente.getDtNascimento(),
                    dependente.getRg()).isEmpty()) {
                msgConfirma = "Esta pessoa já esta cadastrada!";
                return false;
            }

            if (dependente.getPessoa().getDocumento().equals("") || dependente.getPessoa().getDocumento().equals("0")) {
                dependente.getPessoa().setDocumento("0");
            } else {
                listDocumento = db.pesquisaFisicaPorDoc(dependente.getPessoa().getDocumento());
                if (!listDocumento.isEmpty()) {
                    msgConfirma = "Documento já existente!";
                    return false;
                }
            }
        } else {
            if (dependente.getPessoa().getDocumento().equals("") || dependente.getPessoa().getDocumento().equals("0")) {
                dependente.getPessoa().setDocumento("0");
            } else {

                listDocumento = db.pesquisaFisicaPorDoc(dependente.getPessoa().getDocumento());
                for (int i = 0; i < listDocumento.size(); i++) {
                    if (!listDocumento.isEmpty() && ((Fisica) listDocumento.get(i)).getId() != dependente.getId()) {
                        msgConfirma = "Documento já existente!";
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String adicionarDependente() {
        Fisica fisica = new Fisica();
        if (listaDependentes.isEmpty()) {
            fisica.getPessoa().setNome("");
            DataObject dtObj = new DataObject(fisica, // NOME
                    0, // IDPARENTESCO
                    1, // VIA CARTEIRINHA
                    (new DataHoje()).incrementarAnos(5, DataHoje.data()), // DATA VALIDADE CARTEIRINHA
                    null, // DATA VAL DEP
                    null);
            listaDependentes.add(dtObj);
            //servicoPessoa.adicionaTitular = false;
        } else {
            for (int i = 0; i < listaDependentes.size(); i++) {
                if (((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getId() != -1
                        && (i - (listaDependentes.size() - 1) == 0)) {
                    fisica.getPessoa().setNome("");
                    DataObject dtObj = new DataObject(fisica, // NOME
                            0, // IDPARENTESCO
                            1, // VIA CARTEIRINHA
                            (new DataHoje()).incrementarAnos(5, DataHoje.data()), // DATA VALIDADE CARTEIRINHA
                            null, // DATA VAL DEP
                            null);
                    listaDependentes.add(dtObj);
                    //servicoPessoa.adicionaTitular = false;
                    break;
                }
            }
        }
        dependente = new Fisica();
        return null;
    }

    public String novoCadastroDependente() {
        dependente = new Fisica();
        temFoto = false;
        listaFisica.clear();
        return null;
    }

    public void novoVoid() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sociosBean", new SociosBean());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaBean());
    }

    public String excluir() {
        if (!validaExcluir()) {
            return null;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        SociosDB db = new SociosDBToplink();

        // DELETAR DEPENDENTES -----
        sv.abrirTransacao();
        for (int i = 0; i < listaDependentes.size(); i++) {
            Socios soc = db.pesquisaSocioPorPessoa(((Fisica) ((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getId());
            if (soc.getId() == -1) {
                listaDependentes.remove(idIndexDep);
            } else if (!excluirDependentes(sv, (Socios) sv.pesquisaCodigo(soc.getId(), "Socios"))) {
                sv.desfazerTransacao();
                msgConfirma = "Erro ao excluir dependentes!";
                return null;
            }
        }

        List<SocioCarteirinha> list = db.pesquisaCarteirinhasPorSocio(socios.getId());

        if (!list.isEmpty()) {
            for (SocioCarteirinha socioCarteirinha : list) {
                if (!sv.deletarObjeto(sv.pesquisaCodigo(socioCarteirinha.getId(), "SocioCarteirinha"))) {
                    msgConfirma = "Erro ao excluir carteirinha do Dependente!";
                    return null;
                }
            }
        }

        // DELETAR SOCIOS ------
        if (!sv.deletarObjeto(sv.pesquisaCodigo(socios.getId(), "Socios"))) {
            msgConfirma = "Erro ao Deletar Sócio!";
            sv.desfazerTransacao();
            return null;
        }

        if (!sv.deletarObjeto(sv.pesquisaCodigo(matriculaSocios.getId(), "MatriculaSocios"))) {
            msgConfirma = "Erro ao Deletar Matricula!";
            sv.desfazerTransacao();
            return null;
        }

        if (!sv.deletarObjeto(sv.pesquisaCodigo(servicoPessoa.getId(), "ServicoPessoa"))) {
            msgConfirma = "Erro ao Deletar Servico Pessoa!";
            sv.desfazerTransacao();
            return null;
        }
        msgConfirma = "Cadastro deletado com sucesso!";
        sv.comitarTransacao();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);

        // FUNCIONANDO --
        //FisicaJSFBean fizx = (FisicaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaJSFBean());
        //fizx.setSocios(new Socios());

        ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).setSocios(new Socios());
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("socioPesquisa",socios);
        return "pessoaFisica";
    }

    public boolean validaExcluir() {
        if (servicoPessoa.getId() == -1) {
            msgConfirma = "Não existe sócio para ser excluido!";
            return false;
        }
        return true;
    }

    public String excluirDependente() {
        if (servicoPessoa.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            SociosDB db = new SociosDBToplink();
            sv.abrirTransacao();
            Socios soc = db.pesquisaSocioPorPessoa(((Fisica) listaDependentes.get(idIndexDep).getArgumento0()).getPessoa().getId());
            if (soc.getId() == -1) {
                listaDependentes.remove(idIndexDep);
                sv.desfazerTransacao();
                msgConfirma = "Registro excluído!";
            } else if (!excluirDependentes(sv, soc)) {
                sv.desfazerTransacao();
            } else {
                sv.comitarTransacao();
                listaDependentes.remove(idIndexDep);
                //servicoPessoa.adicionaTitular = true;
            }
        } else {
            listaDependentes.remove(idIndexDep);
            msgConfirma = "Registro excluído!";
            //servicoPessoa.adicionaTitular = true;
        }
        return null;
    }

    public boolean excluirDependentes(SalvarAcumuladoDB sv, Socios soc) {
        SociosDB db = new SociosDBToplink();
        ServicoPessoaDB dbS = new ServicoPessoaDBToplink();

        List<SocioCarteirinha> list = db.pesquisaCarteirinhasPorSocio(soc.getId());

        if (!list.isEmpty()) {
            for (SocioCarteirinha socioCarteirinha : list) {
                if (!sv.deletarObjeto(sv.pesquisaCodigo(socioCarteirinha.getId(), "SocioCarteirinha"))) {
                    msgConfirma = "Erro ao excluir carteirinha do Dependente!";
                    return false;
                }
            }
        }

        if (!sv.deletarObjeto(sv.pesquisaCodigo(soc.getId(), "Socios"))) {
            msgConfirma = "Erro ao excluir Dependente!";
            return false;
        }

        ServicoPessoa serPessoa = dbS.pesquisaServicoPessoaPorPessoa(soc.getServicoPessoa().getPessoa().getId());
        if (!sv.deletarObjeto(sv.pesquisaCodigo(serPessoa.getId(), "ServicoPessoa"))) {
            msgConfirma = "Erro ao excluir serviço pessoa dependente!";
            return false;
        }
        msgConfirma = "Dependente excluído!";
        return true;
    }

    public void editarGenerico(Pessoa sessao) {
        CategoriaDB dbCat = new CategoriaDBToplink();
        GrupoCategoria gpCat = new GrupoCategoria();
        SociosDB db = new SociosDBToplink();
        FisicaDB dbf = new FisicaDBToplink();
        Socios soc = new Socios();
        Socios socSessao = new Socios();

        //socSessao = db.pesquisaSocioPorPessoaAtivo(sessao.getId());
        socSessao = db.pesquisaSocioPorPessoa(sessao.getId());
        if (socSessao.getId() != -1) {
            socios = socSessao;
        } else {
            return;
        }

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno").equals("emissaoCarteirinha")) {
            return;
        }

        soc = db.pesquisaSocioDoDependente(socios.getId());
        if (soc != null) {
            socios = soc;
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        servicoPessoa = socios.getServicoPessoa();
        matriculaSocios = socios.getMatriculaSocios();

        gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());
        for (int i = 0; i < getListaGrupoCategoria().size(); i++) {
            if (Integer.parseInt((String) getListaGrupoCategoria().get(i).getDescription()) == gpCat.getId()) {
                idGrupoCategoria = i;
                break;
            }
        }
        int qntCategoria = getListaCategoria().size();
        for (int i = 0; i < qntCategoria; i++) {
            if (Integer.parseInt((String) getListaCategoria().get(i).getDescription()) == socios.getMatriculaSocios().getCategoria().getId()) {
                idCategoria = i;
                break;
            }
        }

        for (int i = 0; i < getListaTipoDocumento().size(); i++) {
            if (Integer.parseInt((String) listaTipoDocumento.get(i).getDescription()) == servicoPessoa.getTipoDocumento().getId()) {
                idTipoDocumento = i;
                break;
            }
        }
        int index = 0;
        List<Socios> listaDeps = db.pesquisaDependentes(matriculaSocios.getId());
        if (!listaDeps.isEmpty()) {
            listaDependentes.clear();
            for (int i = 0; i < listaDeps.size(); i++) {
                // FISICA, PARENTESCO, VIA_CARTEIRINHA, DATA VALIDADE CARTEIRINHA, DATA VAL DEP
                for (int w = 0; w < getListaParentesco().size(); w++) {
                    if (listaDeps.get(i).getParentesco().getId() == Integer.parseInt(listaParentesco.get(w).getDescription())) {
                        index = w;
                    }
                }
                Fisica fisica = dbf.pesquisaFisicaPorPessoa(listaDeps.get(i).getServicoPessoa().getPessoa().getId());
                ParentescoDB dbp = new ParentescoDBToplink();
                Parentesco par = dbp.pesquisaCodigo(Integer.valueOf(listaParentesco.get(index).getDescription()));

                listaDependentes.add(new DataObject(fisica,
                        index,// PARENTESCO
                        listaDeps.get(i).getNrViaCarteirinha(), // VIA CARTEIRINHA
                        listaDeps.get(i).getValidadeCarteirinha(), // DATA VALIDADE CARTEIRINHA
                        atualizaValidade(par, fisica), // DATA VAL DEP
                        null));
            }
        }
    }

    public void editarOUsalvar() {
        dependente = (Fisica) listaDependentes.get(idIndexDep).getArgumento0();
        listaFisica.clear();
        pesquisaLista = "";
    }

    public void editarDependente() {
        dependente = (Fisica) listaFisica.get(idIndexCombo);
        listaFisica.clear();
        pesquisaLista = "";

    }

    public void excluirImagemDependente() {
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = "";
        caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg");
        try {
            File fl = new File(caminho);
            if (fl.exists()) {
                fl.delete();
            } else if (dependente.getId() != -1) {
                caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + String.valueOf(dependente.getPessoa().getId()) + ".jpg");
                fl = new File(caminho);
                fl.delete();
                SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
                dependente.setDataFoto("");
                sv.abrirTransacao();
                sv.alterarObjeto(dependente);
                sv.comitarTransacao();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String atualizaValidade(Parentesco par, Fisica fisica) {
        if (par.getNrValidade() == 0) {
            return null;
        } else if (par.getNrValidade() > 0 && par.isValidade()) {
            if (!fisica.getNascimento().equals("") && fisica.getNascimento() != null) {
                return (new DataHoje()).incrementarAnos(par.getNrValidade(), fisica.getNascimento()).substring(3, 10);
            } else {
                return null;
            }
        } else if (par.getNrValidade() > 0 && !par.isValidade()) {
            return ((new DataHoje()).incrementarAnos(par.getNrValidade(), DataHoje.data())).substring(3, 10);
        }
        return null;
    }

    public void atualizaValidadeTela(int index) {
        ParentescoDB db = new ParentescoDBToplink();
        Parentesco par = db.pesquisaCodigo(Integer.parseInt(listaParentesco.get(Integer.parseInt((String) ((DataObject) listaDependentes.get(index)).getArgumento1())).getDescription()));
        Fisica fisica = (Fisica) ((DataObject) listaDependentes.get(index)).getArgumento0();
        ((DataObject) listaDependentes.get(index)).setArgumento4(atualizaValidade(par, fisica));
    }

    public String visualizarCarteirinha() {
        if (socios.getId() == -1) {
            return null;
        }

        boolean comita = false;
        SocioCarteirinhaDB dbc = new SocioCarteirinhaDBToplink();

        String data = DataHoje.data();
        SocioCarteirinha carteirinha = new SocioCarteirinha();

        boolean isSocioCarteirinhaSocio = dbc.pesquisaSocioCarteirinhaSocio(socios.getId()).isEmpty();

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (isSocioCarteirinhaSocio) {
            carteirinha.setEmissao(data);
            carteirinha.setSocios(socios);
            if (!sv.inserirObjeto(carteirinha)) {
                sv.desfazerTransacao();
                return null;
            }
            DataHoje dh = new DataHoje();

            socios.setNrViaCarteirinha(1);

            CategoriaDB dbCat = new CategoriaDBToplink();
            GrupoCategoria gpCat = dbCat.pesquisaGrupoPorCategoria(socios.getMatriculaSocios().getCategoria().getId());

            socios.setDtValidadeCarteirinha(DataHoje.converte(dh.incrementarMeses(gpCat.getNrValidadeMesCartao(), DataHoje.data())));
            if (!sv.alterarObjeto(socios)) {
                sv.desfazerTransacao();
                return null;
            }
            sv.comitarTransacao();
            comita = true;
        } else {
            carteirinha.setEmissao(data);
            carteirinha.setSocios(socios);
            if (!dbc.verificaSocioCarteirinhaExiste(socios.getId())) {
                if (!sv.inserirObjeto(carteirinha)) {
                    sv.desfazerTransacao();
                    return null;
                }
                sv.comitarTransacao();
                comita = true;
            }
        }

        List listaAux = dbc.filtroCartao(socios.getId());
        if (!listaAux.isEmpty()) {
            ((List) listaAux.get(0)).set(6, socios.getValidadeCarteirinha());
            ((List) listaAux.get(0)).set(11, socios.getNrViaCarteirinha());
            ImpressaoParaSocios.imprimirCarteirinha(listaAux);
        } else {
            msgConfirma = "Socio não tem carteirinha";
        }

        if (!comita) {
            sv.desfazerTransacao();
        }
//        if (ImpressaoParaSocios.imprimirCarteirinha(listaAux)) {
//            sv.comitarTransacao();
//        } else {
//            sv.desfazerTransacao();
//        }



//        try {
//            if (dbF.pesquisaCodigoRegistro(1).isCarteirinhaDependente()){
//                if (ImpressaoParaSocios.imprimirCarteirinha(listaAux)){
//                    sv.comitarTransacao();
//                }else{
//                    sv.desfazerTransacao();
//                }
//            }else{
//                if (ImpressaoParaSocios.imprimirCarteirinha(listaAux)){
//                    sv.comitarTransacao();
//                }else{
//                    sv.desfazerTransacao();
//                }
//            }
//        }catch(Exception e){
//            
//        }        
//        for (int i = 0; i < listaDependentes.size();i++)
//            listaAux.add(db.pesquisaSocioPorPessoa(((Fisica)((DataObject) listaDependentes.get(i)).getArgumento0()).getPessoa().getId()));
//        
//          INSERIR CARTEIRINHAS DEPENDENTE
//        carteirinha = new SocioCarteirinha();
//        for (int i = 0; i < listaAux.size();i++){
//            carteirinha.setEmissao(data);
//            carteirinha.setSocios( (Socios)listaAux.get(i) );
//            if (!sv.inserirObjeto(carteirinha)){
//                sv.desfazerTransacao();
//                return null;
//            }
//            carteirinha = new SocioCarteirinha();
//        }        
        return null;
    }

    public String imprimirFichaSocial() {
        String foto = getFotoSocio();
        String path = "/Relatorios/FICHACADASTRO.jasper";
        String pathVerso = "/Relatorios/FICHACADASTROVERSO.jasper";
        String caminhoDiretorio = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/fichas");
        ImpressaoParaSocios.comDependente(
                caminhoDiretorio,
                "ficha_" + socios.getId() + "_" + socios.getServicoPessoa().getPessoa().getId() + ".pdf",
                path,
                pathVerso,
                socios,
                pessoaEmpresa,
                matriculaSocios,
                imprimirVerso,
                foto);
        return null;
    }

    public String getFotoSocio() {
        FacesContext context = FacesContext.getCurrentInstance();
        File files;
        if (socios.getId() != -1) {
            files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + socios.getServicoPessoa().getPessoa().getId() + ".jpg"));
            if (files.exists()) {
                return files.getPath();
            } else {
                return ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg");
            }
        } else {
            return ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/semFoto.jpg");
        }
    }

    public List<SelectItem> getListaGrupoCategoria() {
        List<SelectItem> listaGrupoCategoria = new Vector<SelectItem>();;
        int i = 0;
        GrupoCategoriaDB db = new GrupoCategoriaDBToplink();
        List select = db.pesquisaTodos();
        while (i < select.size()) {
            listaGrupoCategoria.add(new SelectItem(new Integer(i),
                    (String) ((GrupoCategoria) select.get(i)).getGrupoCategoria(),
                    Integer.toString(((GrupoCategoria) select.get(i)).getId())));
            i++;
        }
        return listaGrupoCategoria;
    }

    public List<SelectItem> getListaCategoria() {
        List<SelectItem> listaCategoria = new Vector<SelectItem>();
        int i = 0;
        if (!getListaGrupoCategoria().isEmpty()) {
            CategoriaDB db = new CategoriaDBToplink();
            List select = db.pesquisaCategoriaPorGrupo(Integer.parseInt(getListaGrupoCategoria().get(idGrupoCategoria).getDescription()));
            while (i < select.size()) {
                listaCategoria.add(new SelectItem(new Integer(i),
                        (String) ((Categoria) select.get(i)).getCategoria(),
                        Integer.toString(((Categoria) select.get(i)).getId())));
                i++;
            }
        }
        return listaCategoria;
    }

    public ServicoPessoa getServicoPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null) {
            servicoPessoa.setPessoa(((Fisica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa")).getPessoa());
            editarGenerico(((Fisica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa")).getPessoa());
            pessoaEmpresa = (PessoaEmpresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaEmpresaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaEmpresaPesquisa");
        }
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }

    public boolean isChkContaCobranca() {
        return chkContaCobranca;
    }

    public void setChkContaCobranca(boolean chkContaCobranca) {
        if (this.chkContaCobranca != chkContaCobranca) {
            listaTipoDocumento.clear();
        }
        this.chkContaCobranca = chkContaCobranca;
    }

    public List<SelectItem> getListaTipoDocumento() {
        if (listaTipoDocumento.isEmpty()) {
            int i = 0;
            FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
            List<FTipoDocumento> select = new ArrayList();
            if (isChkContaCobranca()) {
                select.add(db.pesquisaCodigo(2));
            } else {
                select = db.pesquisaListaTipoExtrato();
            }
            if (!select.isEmpty()) {
                while (i < select.size()) {
                    listaTipoDocumento.add(new SelectItem(new Integer(i),
                            (String) (select.get(i).getDescricao()),
                            Integer.toString(select.get(i).getId())));
                    i++;
                }
            }
        }
        return listaTipoDocumento;
    }

    public void setListaTipoDocumento(List<SelectItem> listaTipoDocumento) {
        this.listaTipoDocumento = listaTipoDocumento;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public boolean isRenderServicos() {
        return renderServicos;
    }

    public void setRenderServicos(boolean renderServicos) {
        this.renderServicos = renderServicos;
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            int i = 0;
            ServicosDB db = new ServicosDBToplink();
            //RotinaDB dbr = new RotinaDBToplink();
            List<Servicos> select = db.pesquisaTodos(120);
            if (!select.isEmpty()) {
                while (i < select.size()) {
                    listaServicos.add(new SelectItem(new Integer(i),
                            (String) select.get(i).getDescricao(),
                            Integer.toString(select.get(i).getId())));
                    i++;
                }
            }
        }
        return listaServicos;
    }

    public void setListaServicos(List<SelectItem> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public Socios getSocios() {
        return socios;
    }

    public void setSocios(Socios socios) {
        this.socios = socios;
    }

    public int getIdGrupoCategoria() {
        return idGrupoCategoria;
    }

    public void setIdGrupoCategoria(int idGrupoCategoria) {
        this.idGrupoCategoria = idGrupoCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public MatriculaSocios getMatriculaSocios() {
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        GrupoCidadesDB dbGCids = new GrupoCidadesDBToplink();
        List<GrupoCidades> cids = dbGCids.pesquisaTodos();
        if (socios.getId() == -1 && matriculaSocios.getId() == -1) {
            matriculaSocios.setEmissao(DataHoje.data());
            PessoaEndereco ende = new PessoaEndereco();
            ende = db.pesquisaEndPorPessoaTipo(servicoPessoa.getPessoa().getId(), 3);
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa") != null) {
                matriculaSocios.setCidade((Cidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa"));
            } else if (ende != null && ende.getId() != -1) {
                for (int i = 0; i < cids.size(); i++) {
                    if (cids.get(i).getCidade().getId() == ende.getEndereco().getCidade().getId()) {
                        matriculaSocios.setCidade(ende.getEndereco().getCidade());
                        return matriculaSocios;
                    }
                }
                matriculaSocios.setCidade(((PessoaEndereco) db.pesquisaEndPorPessoaTipo(1, 3)).getEndereco().getCidade());
            } else {
                matriculaSocios.setCidade(((PessoaEndereco) db.pesquisaEndPorPessoaTipo(1, 3)).getEndereco().getCidade());
            }
        } else if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa") != null) {
            matriculaSocios.setCidade((Cidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
        }
        return matriculaSocios;
    }

    public void setMatriculaSocios(MatriculaSocios matriculaSocios) {
        this.matriculaSocios = matriculaSocios;
    }

    public List<DataObject> getListaDependentes() {
        return listaDependentes;
    }

    public void setListaDependentes(List<DataObject> listaDependentes) {
        this.listaDependentes = listaDependentes;
    }

    public List<SelectItem> getListaParentesco() {
        if (listaParentesco.isEmpty()) {
            ParentescoDB db = new ParentescoDBToplink();
            List select = db.pesquisaTodosSemTitular();
            for (int i = 0; i < select.size(); i++) {
                listaParentesco.add(new SelectItem(new Integer(i),
                        (String) ((Parentesco) select.get(i)).getParentesco(),
                        Integer.toString(((Parentesco) select.get(i)).getId())));
            }
        }
        return listaParentesco;
    }

    public void setListaParentesco(List<SelectItem> listaParentesco) {
        this.listaParentesco = listaParentesco;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdIndexDep() {
        return idIndexDep;
    }

    public void setIdIndexDep(int idIndexDep) {
        this.idIndexDep = idIndexDep;
    }

    public String getPessoaImagem() {
        FacesContext context = FacesContext.getCurrentInstance();
        File files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/"));
        File fExiste = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg"));
        File listFile[] = files.listFiles();
        String nome = "";
        String caminho = "";
        //temFoto = false;
        if (fExiste.exists() && dependente.getDataFoto().isEmpty()) {
            fotoTemp = true;
        }
        if (fotoTemp) {
            nome = "fotoTemp";
        } else {
            nome = "semFoto";
        }
        int numArq = listFile.length;
        for (int i = 0; i < numArq; i++) {
            String n = listFile[i].getName();
            for (int o = 0; o < n.length(); o++) {
                if (n.substring(o, o + 1).equals(".")) {
                    n = listFile[i].getName().substring(0, o);
                }
            }
            try {
                if (!fotoTemp) {
                    if (Integer.parseInt(n) == dependente.getPessoa().getId()) {
                        nome = n;
                        fotoTemp = false;
                        caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg");
                        File fl = new File(caminho);
                        fl.delete();
                        break;
                    }
                } else {
                    fotoTemp = false;
                    break;
                }
            } catch (Exception e) {
                continue;
            }
        }
        return nome + ".jpg";
    }

    public List<Fisica> getListaFisica() {
        if (!pesquisaLista.isEmpty()) {
            List listaAux = new ArrayList();
            FisicaDB db = new FisicaDBToplink();
            listaFisica = db.pesquisaFisicaPorNome(pesquisaLista.toUpperCase());
            for (Iterator it = listaFisica.iterator(); it.hasNext();) {
                Object item = it.next();
                //if ( ((Fisica)item).getPessoa().getNome().contains(pesquisaLista) )
                listaAux.add(item);
            }
            if (!listaAux.isEmpty()) {
                return listaAux;
            } else {
                return new ArrayList();
            }
            //lista.addAll(listaAux);
        }
        //if (listaFisica.size() > 200){
        //    return listaFisica.subList(0, 200);
        // }

        return new ArrayList();
    }

    public void setListaFisica(List<Fisica> listaFisica) {
        this.listaFisica = listaFisica;
    }

    public String getPesquisaLista() {
        return pesquisaLista;
    }

    public void setPesquisaLista(String pesquisaLista) {
        this.pesquisaLista = pesquisaLista;
    }

    public Fisica getDependente() {
        return dependente;
    }

    public void setDependente(Fisica dependente) {
        this.dependente = dependente;
    }

    public int getIdIndexCombo() {
        return idIndexCombo;
    }

    public void setIdIndexCombo(int idIndexCombo) {
        this.idIndexCombo = idIndexCombo;
    }

    public String getLblSocio() {
        if (socios.getId() == -1) {
            lblSocio = "SALVAR ";
        } else {
            lblSocio = "ALTERAR";
        }
        return lblSocio;
    }

    public void setLblSocio(String lblSocio) {
        this.lblSocio = lblSocio;
    }

    public String getLblSocioPergunta() {
        if (socios.getId() == -1) {
            lblSocioPergunta = "Deseja associar esse cadastro? ";
        } else {
            lblSocioPergunta = "Deseja alterar esse cadastro?";
        }
        return lblSocioPergunta;
    }

    public void setLblSocioPergunta(String lblSocioPergunta) {
        this.lblSocioPergunta = lblSocioPergunta;
    }

    public boolean isDesabilitaImpressao() {
        if (socios.getId() != -1 && matriculaSocios.getId() != -1) {
            desabilitaImpressao = false;
        }
        return desabilitaImpressao;
    }

    public void setDesabilitaImpressao(boolean desabilitaImpressao) {
        this.desabilitaImpressao = desabilitaImpressao;
    }

    public String getTipoDestinario() {
        return tipoDestinario;
    }

    public void setTipoDestinario(String tipoDestinario) {
        this.tipoDestinario = tipoDestinario;
    }

    public boolean isImprimirVerso() {
        return imprimirVerso;
    }

    public void setImprimirVerso(boolean imprimirVerso) {
        this.imprimirVerso = imprimirVerso;
    }

    public String getDataInativacao() {
        return dataInativacao;
    }

    public void setDataInativacao(String dataInativacao) {
        this.dataInativacao = dataInativacao;
    }

    public int getIdInativacao() {
        return idInativacao;
    }

    public void setIdInativacao(int idInativacao) {
        this.idInativacao = idInativacao;
    }

    public List<SelectItem> getListaMotivoInativacao() {
        if (listaMotivoInativacao.isEmpty()) {
            SociosDB db = new SociosDBToplink();
            List select = db.pesquisaMotivoInativacao();
            for (int i = 0; i < select.size(); i++) {
                listaMotivoInativacao.add(new SelectItem(new Integer(i),
                        (String) ((SMotivoInativacao) select.get(i)).getDescricao(),
                        Integer.toString(((SMotivoInativacao) select.get(i)).getId())));
            }
        }
        return listaMotivoInativacao;
    }

    public String getDataReativacao() {
        return dataReativacao;
    }

    public void setDataReativacao(String dataReativacao) {
        this.dataReativacao = dataReativacao;
    }

    public String getStatusSocio() {
        if (socios.getId() == -1) {
            statusSocio = "STATUS";
        } else {
            if (matriculaSocios.getMotivoInativacao() != null) {
                statusSocio = "INATIVO / " + matriculaSocios.getMotivoInativacao().getDescricao() + " - " + matriculaSocios.getInativo();;
            } else {
                statusSocio = "ATIVO";
            }
        }
        return statusSocio;
    }

    public void setStatusSocio(String statusSocio) {
        this.statusSocio = statusSocio;
    }
}