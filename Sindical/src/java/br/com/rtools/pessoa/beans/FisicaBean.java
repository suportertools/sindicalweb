package br.com.rtools.pessoa.beans;

import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.db.EnderecoDB;
import br.com.rtools.endereco.db.EnderecoDBToplink;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.db.HomologacaoDB;
import br.com.rtools.homologacao.db.HomologacaoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;

@ManagedBean
@SessionScoped
public class FisicaBean extends PesquisarProfissaoBean implements Serializable {

    private Fisica fisica;
    private PessoaEndereco pessoaEndereco;
    private PessoaProfissao pessoaProfissao;
    private PessoaEmpresa pessoaEmpresa;
    private Endereco endereco;
    private PessoaComplemento pessoaComplemento = new PessoaComplemento();
    private Socios socios;
    private String renEndereco;
    private String indicaTab;
    private String enderecoCompleto;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String msgConfirma;
    private String log;
    private String desc;
    private String cid;
    private String uf;
    private String masc;
    private String maxl;
    private String renNovoEndereco;
    private String enderecoCobranca;
    private String renAbreEnd;
    private String msgSocio;
    private String lblSocio;
    private String pesquisaPor;
    private String tipo;
    private String tipoSocio;
    private boolean alterarEnd;
    private boolean endResidencial;
    private boolean fotoTemp;
    private boolean renderJuridicaPesquisa;
    private boolean temFoto;
    private List listaEnd;
    public List itens;
    private List<DataObject> listaPessoa;
    private List<PessoaEmpresa> listaPessoaEmpresa;
    private List<SelectItem> listaProfissoes;
    private int idPais;
    private int idProfissao;
    private int idIndexEndereco;
    private int idIndexFisica;
    private int idIndexPessoaEmp;
    private Part file;
    private String fileContent = "";

    public FisicaBean() {
        fisica = new Fisica();
        pessoaEndereco = new PessoaEndereco();
        pessoaProfissao = new PessoaProfissao();
        pessoaEmpresa = new PessoaEmpresa();
        endereco = new Endereco();
        pessoaComplemento = new PessoaComplemento();
        socios = new Socios();
        renEndereco = "false";
        indicaTab = "pessoal";
        enderecoCompleto = "";
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        msgConfirma = "";
        log = "";
        desc = "";
        cid = "";
        uf = "";
        masc = "";
        maxl = "";
        renNovoEndereco = "false";
        enderecoCobranca = "";
        renAbreEnd = "true";
        msgSocio = "";
        lblSocio = "";
        pesquisaPor = "";
        tipoSocio = "";
        alterarEnd = false;
        endResidencial = false;
        fotoTemp = false;
        renderJuridicaPesquisa = false;
        temFoto = false;
        listaEnd = new ArrayList();
        itens = new ArrayList();
        listaPessoa = new ArrayList();
        listaProfissoes = new ArrayList();
        listaPessoaEmpresa = new ArrayList();
        idPais = 11;
        // idProfissao = 386; ANTES ESTAVA FUNCIONANDO
        idProfissao = 0;
        idIndexEndereco = 0;
        idIndexFisica = 0;
        idIndexPessoaEmp = 0;
        limpaFoto();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoNum");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoComp");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("FisicaJSFBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaComplementoBean");
    }

    public String getEnderecoCobranca() {
        PessoaEndereco ende = null;
        String strCompl;
        if (!listaEnd.isEmpty()) {
            ende = (PessoaEndereco) listaEnd.get(0);
        }

        if (ende != null) {
            if (ende.getComplemento().equals("")) {
                strCompl = " ";
            } else {
                strCompl = " ( " + ende.getComplemento() + " ) ";
            }
            enderecoCobranca = ende.getEndereco().getLogradouro().getDescricao() + " "
                    + ende.getEndereco().getDescricaoEndereco().getDescricao() + ", " + ende.getNumero() + " " + ende.getEndereco().getBairro().getDescricao() + ","
                    + strCompl + ende.getEndereco().getCidade().getCidade() + " - " + ende.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(ende.getEndereco().getCep());
        } else {
            if (alterarEnd) {
                getListaEnderecos();
            } else {
                enderecoCobranca = "NENHUM";
            }
        }
        return enderecoCobranca;
    }

    public String salvar() {
        NovoLog logs = new NovoLog();
        FisicaDB db = new FisicaDBToplink();
        Pessoa pessoa = fisica.getPessoa();
        List listDocumento;
        if (listaEnd.isEmpty() || pessoa.getId() == -1) {
            adicionarEnderecos();
        }
        if (temFoto) {
            fisica.setDataFoto(DataHoje.data());
        } else {
            fisica.setDtFoto(null);
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if ((fisica.getPessoa().getId() == -1) && (fisica.getId() == -1)) {
            fisica.getPessoa().setTipoDocumento((TipoDocumento) salvarAcumuladoDB.pesquisaCodigo(1, "TipoDocumento"));

            if (!db.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
                    fisica.getDtNascimento(),
                    fisica.getRg()).isEmpty()) {
                msgConfirma = "Esta pessoa já esta cadastrada!";
                return null;
            }

            if (fisica.getNascimento().isEmpty() || fisica.getNascimento().length() < 10) {
                msgConfirma = "Data de Nascimento esta inválida!";
                return null;
            }

            if (pessoa.getDocumento().equals("") || pessoa.getDocumento().equals("0")) {
                pessoa.setDocumento("0");
            } else {
                if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                    msgConfirma = "Documento Invalido!";
                    return null;
                }
                listDocumento = db.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
                if (!listDocumento.isEmpty()) {
                    msgConfirma = "Documento já existente!";
                    return null;
                }
            }
            if (fisica.getPessoa().getNome().equals("")) {
                msgConfirma = "O campo nome não pode ser nulo! ";
                return null;
            }
            if (salvarAcumuladoDB.inserirObjeto(pessoa)) {
                fisica.setNacionalidade(getListaPaises().get(idPais).getLabel());
                fisica.setPessoa(pessoa);
                if (salvarAcumuladoDB.inserirObjeto(fisica)) {

                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaPesquisa", fisica);
                    msgConfirma = "Cadastro salvo com Sucesso!";
                    logs.novo("Novo registro", "Pessoa fisica inserida: ID " + fisica.getId()
                            + " - Pessoa: " + fisica.getPessoa().getId()
                            + " - Nome: " + fisica.getPessoa().getNome()
                            + " - Nascimento: " + fisica.getNascimento()
                            + " - CPF: " + fisica.getPessoa().getDocumento()
                            + " - RG: " + fisica.getRg());
                    salvarAcumuladoDB.comitarTransacao();
                } else {
                    msgConfirma = "Erro ao Salvar Pessoa Fisica!";
                    salvarAcumuladoDB.desfazerTransacao();
                }
            } else {
                msgConfirma = "Erro ao Salvar Pessoa!";
                salvarAcumuladoDB.desfazerTransacao();
            }
        } else {
            Fisica f = db.pesquisaCodigo(fisica.getId());
            String antes = " De: ID - " + fisica.getId()
                    + " Nome: " + f.getPessoa().getNome() + " - "
                    + " Nascimento: " + f.getNascimento() + " - "
                    + " CPF: " + f.getPessoa().getDocumento() + " - "
                    + " RG: " + f.getRg();
            if (fisica.getPessoa().getDocumento().equals("") || fisica.getPessoa().getDocumento().equals("0")) {
                fisica.getPessoa().setDocumento("0");
            } else {
                if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                    msgConfirma = "Documento Inválido!";
                    return null;
                }
                listDocumento = db.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
                for (int i = 0; i < listDocumento.size(); i++) {
                    if (!listDocumento.isEmpty() && ((Fisica) listDocumento.get(i)).getId() != fisica.getId()) {
                        msgConfirma = "Documento já existente!";
                        return null;
                    }
                }
            }
            if (fisica.getNascimento().isEmpty() || fisica.getNascimento().length() < 10) {
                msgConfirma = "Data de Nascimento esta inválida!";
                return null;
            }
            List<Fisica> fisi = db.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
                    fisica.getDtNascimento(),
                    fisica.getRg());
            if (!fisi.isEmpty()) {
                for (int i = 0; i < fisi.size(); i++) {
                    if (fisi.get(i).getId() != fisica.getId()) {
                        msgConfirma = "Esta pessoa já esta cadastrada!";
                        return null;
                    }
                }
            }

            fisica.setNacionalidade(getListaPaises().get(idPais).getLabel());
            if (salvarAcumuladoDB.alterarObjeto(fisica.getPessoa())) {
                logs.novo("Atualizado", antes
                        + " para: Nome: " + fisica.getPessoa().getNome() + " - "
                        + " Nascimento: " + f.getNascimento() + " - "
                        + " CPF: " + fisica.getPessoa().getDocumento() + " - "
                        + " RG: " + fisica.getRg());
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                return null;
            }

            if (salvarAcumuladoDB.alterarObjeto(fisica)) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaPesquisa", fisica);
                msgConfirma = "Cadastro atualizado com Sucesso!";
                salvarAcumuladoDB.comitarTransacao();
            } else {
                msgConfirma = "Erro ao Atualizar!";
                salvarAcumuladoDB.desfazerTransacao();
            }
        }
        salvarEndereco();
        salvarPessoaEmpresa();
        salvarImagem();
        salvarPessoaProfissao();
        limparCamposData();
        return null;
    }

    public void salvarPessoaProfissao() {
        if (!listaProfissoes.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            if (fisica.getId() == -1) {
                pessoaProfissao = new PessoaProfissao();
                pessoaProfissao.setFisica(fisica);
                pessoaProfissao.setProfissao((Profissao) sv.pesquisaCodigo(Integer.valueOf(listaProfissoes.get(idProfissao).getDescription()), "Profissao"));

                sv.abrirTransacao();
                if (!sv.inserirObjeto(pessoaProfissao)) {
                    sv.desfazerTransacao();
                } else {
                    sv.comitarTransacao();
                }
            } else {
                pessoaProfissao.setProfissao((Profissao) sv.pesquisaCodigo(Integer.valueOf(listaProfissoes.get(idProfissao).getDescription()), "Profissao"));
                if (pessoaProfissao.getId() == -1) {
                    pessoaProfissao.setFisica(fisica);
                    if (!sv.inserirObjeto(pessoaProfissao)) {
                        sv.desfazerTransacao();
                        return;
                    }
                } else {
                    if (!sv.alterarObjeto(pessoaProfissao)) {
                        sv.desfazerTransacao();
                        return;
                    }
                }
                sv.comitarTransacao();
            }
        }
    }

    public void limpaFoto() {
        FacesContext context = FacesContext.getCurrentInstance();
        File fExiste = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg"));
        if (fExiste.exists()) {
            fExiste.delete();
        }
    }

    public String novo() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaBean());
        return "pessoaFisica";
    }

    public String novoOK() {
        if (fisica.getId() == -1) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaBean());
        }
        return "pessoaFisica";
    }

    public void salvarEndereco() {
        List endPorPessoa = getPesquisaEndPorPessoa();
        int i = 0;
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (fisica.getId() != -1) {
            if (endPorPessoa.isEmpty()) {
                while (i < listaEnd.size()) {
                    pessoaEndereco = (PessoaEndereco) listaEnd.get(i);
                    salvarAcumuladoDB.abrirTransacao();
                    if (salvarAcumuladoDB.inserirObjeto(pessoaEndereco)) {
                        salvarAcumuladoDB.comitarTransacao();
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                        msgConfirma = "Erro ao Salvar Endereço!";
                    }
                    pessoaEndereco = new PessoaEndereco();
                    i++;
                }
            } else {
                if (endResidencial) {
                    for (int o = 0; o < listaEnd.size(); o++) {
                        salvarAcumuladoDB.abrirTransacao();
                        if (salvarAcumuladoDB.alterarObjeto((PessoaEndereco) listaEnd.get(o))) {
                            salvarAcumuladoDB.comitarTransacao();
                        } else {
                            salvarAcumuladoDB.desfazerTransacao();
                        }
                    }
                    endResidencial = false;
                } else {
                    if (pessoaEndereco.getId() != -1) {
                        salvarAcumuladoDB.abrirTransacao();
                        if (salvarAcumuladoDB.alterarObjeto(pessoaEndereco)) {
                            salvarAcumuladoDB.comitarTransacao();
                        } else {
                            salvarAcumuladoDB.desfazerTransacao();
                        }
                    }
                }
            }
            pessoaEndereco = new PessoaEndereco();
        }
    }

    public String abreEndereco() {
        listaEnd = getListaEnderecos();
        if (listaEnd.isEmpty()) {
            if (renEndereco.equals("false")) {
                renEndereco = "false";
                renNovoEndereco = "true";
                pessoaEndereco = new PessoaEndereco();
                listaEnd = new ArrayList();
            } else {
                renEndereco = "false";
                renNovoEndereco = "false";
            }
        } else {
            if (renEndereco.equals("true")) {
                renEndereco = "false";
                renNovoEndereco = "false";
            } else {
                renEndereco = "true";
                renNovoEndereco = "false";
            }

        }
        return "pessoaFisica";
    }

    public String excluir() {
        if (socios.getId() != -1) {
            msgConfirma = "Esse cadastro esta associado, desvincule para excluir!";
            return null;
        }

        PessoaEnderecoDB dbPE = new PessoaEnderecoDBToplink();
        PessoaProfissaoDB dbPP = new PessoaProfissaoDBToplink();
        PessoaEmpresaDB dbEM = new PessoaEmpresaDBToplink();
        List<PessoaEndereco> listaEndereco = dbPE.pesquisaEndPorPessoa(fisica.getPessoa().getId());
        PessoaProfissao pp = dbPP.pesquisaProfPorFisica(fisica.getId());
        List<PessoaEmpresa> listaPessoaEmp = dbEM.listaPessoaEmpresaTodos(fisica.getId());
        //PessoaDB dbPessoa = new PessoaDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        NovoLog logs = new NovoLog();
        if (fisica.getId() != -1) {
            //fisica.setPessoa(dbPessoa.pesquisaCodigo(fisica.getPessoa().getId()));

            sv.abrirTransacao();
            // EXCLUI ENDEREÇO -----------------
            if (!listaEndereco.isEmpty()) {
                for (int i = 0; i < listaEndereco.size(); i++) {
                    if (!sv.deletarObjeto(sv.pesquisaCodigo(listaEndereco.get(i).getId(), "PessoaEndereco"))) {
                        sv.desfazerTransacao();
                        msgConfirma = "Erro ao excluir endereços!";
                        return null;
                    }
                }
            }

            // EXCLUI PROFISSÃO -----------------
            if (pp.getId() != -1) {
                if (!sv.deletarObjeto(sv.pesquisaCodigo(pp.getId(), "PessoaProfissao"))) {
                    sv.desfazerTransacao();
                    msgConfirma = "Erro ao excluir profissão!";
                    return null;
                }
            }
            // EXCLUI PESSOA EMPRESA ------------
            if (!listaPessoaEmp.isEmpty()) {
                for (int i = 0; i < listaPessoaEmp.size(); i++) {
                    if (!sv.deletarObjeto(sv.pesquisaCodigo(listaPessoaEmp.get(i).getId(), "PessoaEmpresa"))) {
                        sv.desfazerTransacao();
                        msgConfirma = "Erro ao excluir pessoas empresa!";
                        return null;
                    }
                }
            }

            excluirImagem();

            if (!sv.deletarObjeto(sv.pesquisaCodigo(fisica.getId(), "Fisica"))) {
                sv.desfazerTransacao();
                msgConfirma = "Física não pode ser excluída!";
                return null;
            }

            if (!sv.deletarObjeto(sv.pesquisaCodigo(fisica.getPessoa().getId(), "Pessoa"))) {
                sv.desfazerTransacao();
                msgConfirma = "Cadastro Pessoa não pode ser excluída!";
                return null;
            }
            msgConfirma = "Cadastro Excluido com sucesso!";
            sv.comitarTransacao();
            logs.novo("Excluido", "ID: " + fisica.getId() + " - Pessoa: " + fisica.getPessoa().getId() + " - Nascimento: " + fisica.getNascimento() + " - Nome: " + fisica.getPessoa().getNome() + " - CPF: " + fisica.getPessoa().getDocumento() + " - RG: " + fisica.getRg());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaBean());
            //((FisicaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).fisica = new Fisica();
            ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).setMsgConfirma("Cadastro Excluido com sucesso!");
            //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaBean");
        } else {
            msgConfirma = "Pesquise uma pessoa física para ser excluída!";
        }
        //return "pessoaFisica";
        return null;
    }

    public String editarFisica(DataObject object) {
        if (fisica.getId() != ((Fisica) object.getArgumento0()).getId()) {
            FacesContext context = FacesContext.getCurrentInstance();
            File fExiste = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg"));
            if (fExiste.exists()) {
                fExiste.delete();
            }

        }
        PessoaEmpresaDB db = new PessoaEmpresaDBToplink();
        PessoaProfissaoDB dbp = new PessoaProfissaoDBToplink();
        fisica = (Fisica) object.getArgumento0();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaComplementoBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaPesquisa", fisica);
        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        alterarEnd = true;
        renNovoEndereco = "false";
        renEndereco = "false";
        listaEnd = new ArrayList();
        listaPessoa = new ArrayList();
        enderecoCobranca = "NENHUM";
        msgSocio = "";
        pessoaEmpresa = (PessoaEmpresa) db.pesquisaPessoaEmpresaPorFisica(fisica.getId());
        if (pessoaEmpresa.getId() != -1) {
            profissao = pessoaEmpresa.getFuncao();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", pessoaEmpresa.getJuridica());
            renderJuridicaPesquisa = true;
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            profissao = new Profissao();
            renderJuridicaPesquisa = false;
        }

        pessoaProfissao = dbp.pesquisaProfPorFisica(fisica.getId());
        if (pessoaProfissao.getId() != -1) {
            for (int i = 0; i < listaProfissoes.size(); i++) {
                if (Integer.valueOf(listaProfissoes.get(i).getDescription()) == pessoaProfissao.getProfissao().getId()) {
                    idProfissao = i;
                    break;
                }
            }
        }

        getListaEnderecos();
        editarFisicaSocio(fisica);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return url;
    }

    public String editarFisicaCPF() {
        if (!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__") && fisica.getId() == -1) {
            FisicaDB db = new FisicaDBToplink();
            List lista = db.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            if (!lista.isEmpty()) {
                editarFisicaParametro((Fisica) lista.get(0));
            }
        }
        return "pessoaFisica";
    }

    public String editarFisicaParametro(Fisica fis) {
        PessoaEmpresaDB db = new PessoaEmpresaDBToplink();
        fisica = fis;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaPesquisa", fisica);
        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        alterarEnd = true;
        renNovoEndereco = "false";
        renEndereco = "false";
        listaEnd = new ArrayList();
        enderecoCobranca = "NENHUM";
        pessoaEmpresa = (PessoaEmpresa) db.pesquisaPessoaEmpresaPorFisica(fisica.getId());
        if (pessoaEmpresa.getId() != -1) {
            profissao = pessoaEmpresa.getFuncao();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", pessoaEmpresa.getJuridica());
            renderJuridicaPesquisa = true;
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            profissao = new Profissao();
            renderJuridicaPesquisa = false;
        }
        getListaEnderecos();
        editarFisicaSocio(fisica);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return url;
    }

    public void editarFisicaSocio(Fisica fis) {
        SociosDB db = new SociosDBToplink();
        socios = db.pesquisaSocioPorPessoa(fisica.getPessoa().getId());
    }

    public List getListaFisica() {
        FisicaDB db = new FisicaDBToplink();
        List result = db.pesquisaTodos();
        return result;
    }

    public List<PessoaEndereco> getListaEnderecos() {
        PessoaEndereco pesEn;
        String strCompl;
        if (!getPesquisaEndPorPessoa().isEmpty() && alterarEnd && listaEnd.isEmpty()) {
            listaEnd = getPesquisaEndPorPessoa();
            pesEn = (PessoaEndereco) (listaEnd.get(0));
            if (pesEn.getComplemento().equals("")) {
                strCompl = " ";
            } else {
                strCompl = " ( " + pesEn.getComplemento() + " ) ";
            }
            enderecoCobranca = pesEn.getEndereco().getLogradouro().getDescricao() + " "
                    + pesEn.getEndereco().getDescricaoEndereco().getDescricao() + ", " + pesEn.getNumero() + " " + pesEn.getEndereco().getBairro().getDescricao() + ","
                    + strCompl + pesEn.getEndereco().getCidade().getCidade() + " - " + pesEn.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(pesEn.getEndereco().getCep());
        }
        return listaEnd;
    }

    public String adicionarEnderecos() {
        TipoEnderecoDB db_tipoEndereco = new TipoEnderecoDBToplink();
        PessoaEnderecoDB db_pesEnd = new PessoaEnderecoDBToplink();
        endereco = new Endereco();
        String num;
        String comp;
        int i = 0;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoNum", pessoaEndereco.getNumero());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoComp", pessoaEndereco.getComplemento());
        List tiposEndereco = db_tipoEndereco.listaTipoEnderecoParaFisica();
        endereco = (Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa");
        if (endereco != null) {
            if (!alterarEnd) {
                num = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoNum");
                comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoComp");
                while (i < tiposEndereco.size()) {
                    pessoaEndereco.setEndereco(endereco);
                    pessoaEndereco.setTipoEndereco((TipoEndereco) tiposEndereco.get(i));
                    pessoaEndereco.setPessoa(fisica.getPessoa());
                    pessoaEndereco.setNumero(num);
                    pessoaEndereco.setComplemento(comp);
                    listaEnd.add(pessoaEndereco);
                    i++;
                    pessoaEndereco = new PessoaEndereco();
                }
            } else {
                num = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoNum");
                comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoComp");
                if (listaEnd.isEmpty()) {
                    for (int u = 0; u < tiposEndereco.size(); u++) {
                        pessoaEndereco.setEndereco(endereco);
                        pessoaEndereco.setTipoEndereco((TipoEndereco) tiposEndereco.get(u));
                        pessoaEndereco.setPessoa(fisica.getPessoa());
                        pessoaEndereco.setNumero(num);
                        pessoaEndereco.setComplemento(comp);
                        listaEnd.add(pessoaEndereco);
                        pessoaEndereco = new PessoaEndereco();
                    }
                } else if (!listaEnd.isEmpty() && pessoaEndereco.getTipoEndereco().getId() == 1) {
                    if (pessoaEndereco.getId() != -1) {
                        PessoaEndereco pessoaEndeAnt = db_pesEnd.pesquisaEndPorPessoaTipo(pessoaEndereco.getPessoa().getId(), 1);
                        ((PessoaEndereco) listaEnd.get(0)).setTipoEndereco((TipoEndereco) tiposEndereco.get(0));
                        ((PessoaEndereco) listaEnd.get(0)).setEndereco(endereco);
                        ((PessoaEndereco) listaEnd.get(0)).setComplemento(pessoaEndereco.getComplemento());
                        ((PessoaEndereco) listaEnd.get(0)).setNumero(pessoaEndereco.getNumero());
                        for (int u = 1; u < listaEnd.size(); u++) {
                            if (comparaEndereco(pessoaEndeAnt, (PessoaEndereco) listaEnd.get(u))) {
                                ((PessoaEndereco) listaEnd.get(u)).setTipoEndereco((TipoEndereco) tiposEndereco.get(u));
                                ((PessoaEndereco) listaEnd.get(u)).setEndereco(endereco);
                                ((PessoaEndereco) listaEnd.get(u)).setComplemento(pessoaEndereco.getComplemento());
                                ((PessoaEndereco) listaEnd.get(u)).setNumero(pessoaEndereco.getNumero());
                            }
                        }
                        endResidencial = true;
                    } else {
                        listaEnd = new ArrayList();
                        for (int u = 0; u < tiposEndereco.size(); u++) {
                            pessoaEndereco.setEndereco(endereco);
                            pessoaEndereco.setTipoEndereco((TipoEndereco) tiposEndereco.get(u));
                            pessoaEndereco.setPessoa(fisica.getPessoa());
                            pessoaEndereco.setNumero(num);
                            pessoaEndereco.setComplemento(comp);
                            listaEnd.add(pessoaEndereco);
                            pessoaEndereco = new PessoaEndereco();
                        }
                    }
                } else {
                    pessoaEndereco.setEndereco(endereco);
                    pessoaEndereco.setPessoa(fisica.getPessoa());
                    pessoaEndereco.setNumero(num);
                    pessoaEndereco.setComplemento(comp);

                    ((PessoaEndereco) listaEnd.get(idIndexEndereco)).setEndereco(endereco);
                    ((PessoaEndereco) listaEnd.get(idIndexEndereco)).setComplemento(pessoaEndereco.getComplemento());
                    ((PessoaEndereco) listaEnd.get(idIndexEndereco)).setNumero(pessoaEndereco.getNumero());
                }
                alterarEnd = false;
            }
            renEndereco = "false";
            renNovoEndereco = "false";
        }
        setEnderecoCompleto("");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoNum");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoComp");
        return "pessoaFisica";
    }

    public String RetornarObjetoDaGrid() {
        pessoaEndereco = (PessoaEndereco) listaEnd.get(idIndexEndereco);
        //PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoPesquisa", pessoaEndereco.getEndereco());
        log = pessoaEndereco.getEndereco().getLogradouro().getDescricao();
        desc = pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao();
        cid = pessoaEndereco.getEndereco().getCidade().getCidade();
        uf = pessoaEndereco.getEndereco().getCidade().getUf();
        setEnderecoCompleto(log + " " + desc + ", " + cid + " - " + uf);
        renEndereco = "false";
        setRenNovoEndereco("true");
        alterarEnd = true;
        return "pessoaFisica";
    }

    public List getListaPessoaEndereco() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        List list = salvarAcumuladoDB.listaObjeto("PessoaEndereco");
        return list;
    }

    public String CarregarEndereco() {
        EnderecoDB db_endereco = new EnderecoDBToplink();
        int idEndereco = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("paramEndereco"));
        pessoaEndereco.setEndereco(db_endereco.pesquisaCodigo(idEndereco));
        setEnderecoCompleto((pessoaEndereco.getEndereco().getLogradouro().getDescricao()) + " " + pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao());
        return "pessoaFisica";
    }

    public List<String> BuscaTipoEndereco(Object object) {
        String txtDigitado = object.toString().toLowerCase().toUpperCase();
        TipoEnderecoDB db = new TipoEnderecoDBToplink();
        List<String> list = db.pesquisaTipoEnderecoParaFisica('%' + txtDigitado + '%');
        return list;
    }

    public List<String> BuscaTipoDocumento(Object object) {
        String txtDigitado = object.toString().toLowerCase().toUpperCase();
        TipoDocumentoDB db = new TipoDocumentoDBToplink();
        List<String> list = db.pesquisaTipoDocumento('%' + txtDigitado + '%');
        return list;
    }

    public String getRetornaEndereco() {
        return "pessoaFisica";
    }

    public List getPesquisaEndPorPessoa() {
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        List list = db.pesquisaEndPorPessoa(fisica.getPessoa().getId());
        return list;
    }

    public String voltarEndereco() {
        return "pessoaFisica";
    }

    public boolean getHabilitar() {
        if (fisica.getPessoa().getId() == -1) {
            return true;
        } else {
            return false;
        }
    }

    public String excluirPessoaEndereco() {
        if (pessoaEndereco.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            pessoaEndereco = (PessoaEndereco) salvarAcumuladoDB.pesquisaCodigo(pessoaEndereco.getId(), "PessoaEndereco");
            if (salvarAcumuladoDB.deletarObjeto(pessoaEndereco)) {
                salvarAcumuladoDB.comitarTransacao();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
            }
        }
        pessoaEndereco = new PessoaEndereco();
        setEnderecoCompleto("");
        return "pessoaFisica";
    }

    public void salvarPessoaEmpresa() {
        PessoaEmpresaDB db = new PessoaEmpresaDBToplink();
        if (fisica.getId() != -1 && pessoaEmpresa.getJuridica().getId() != -1) {
            pessoaEmpresa.setFisica(fisica);
            pessoaEmpresa.setAvisoTrabalhado(false);
            if (pessoaEmpresa.getDtAdmissao() == null) {
                msgConfirma = "Informar data de admissão!";
                return;
            }
            if (profissao.getProfissao().equals("") || profissao.getProfissao() == null) {
                msgConfirma = "Pesquise uma função!";
                return;
            }
            pessoaEmpresa.setFuncao(profissao);
            if (pessoaEmpresa.getDemissao() != null && !pessoaEmpresa.getDemissao().equals("")) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
                        > DataHoje.converteDataParaInteger(DataHoje.data())) {
                    msgConfirma = "Data de Demissão maior que atual!";
                    return;
                }
            }
            if (pessoaEmpresa.getId() == -1) {
                db.insert(pessoaEmpresa);
            } else {
                db.update(pessoaEmpresa);
            }
            if (pessoaEmpresa.getDemissao() != null && !pessoaEmpresa.getDemissao().equals("")) {
                pessoaEmpresa = new PessoaEmpresa();
                profissao = new Profissao();
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
                renderJuridicaPesquisa = false;
            }
        }
    }

    public void refreshForm() {
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listaPessoa.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listaPessoa.clear();
    }

    public int getRetornaIdPessoaList() {
        //fisica = (Fisica) getHtmlTable().getRowData();
        return fisica.getPessoa().getId();
    }

    public List<DataObject> getListaPessoa() {
        if (listaPessoa.isEmpty()) {
            List<Fisica> result2 = new ArrayList();
            FisicaDB db = new FisicaDBToplink();
            PessoaEmpresaDB dbEmp = new PessoaEmpresaDBToplink();
            if (pesquisaPor.equals("socioativo")) {
                result2 = db.pesquisaPessoaSocio(descPesquisa, porPesquisa, comoPesquisa);
            } else if (pesquisaPor.equals("pessoa")) {
                result2 = db.pesquisaPessoa(descPesquisa, porPesquisa, comoPesquisa);
            } else if (pesquisaPor.equals("socioinativo")) {
                result2 = db.pesquisaPessoaSocioInativo(descPesquisa, porPesquisa, comoPesquisa);
            }
            for (int i = 0; i < result2.size(); i++) {
                listaPessoa.add(new DataObject(
                        result2.get(i), (PessoaEmpresa) dbEmp.pesquisaPessoaEmpresaPorFisica(result2.get(i).getId())));
            }
        }
        return listaPessoa;
    }

    public void setListaPessoa(List<DataObject> listaPessoa) {
        this.listaPessoa = listaPessoa;
    }

    public String getRetornarEnderecoAmbos() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa") != null) {
            log = ((Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa")).getLogradouro().getDescricao();
            desc = ((Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa")).getDescricaoEndereco().getDescricao();
            cid = ((Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa")).getCidade().getCidade();
            uf = ((Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa")).getCidade().getUf();
            setEnderecoCompleto(log + " " + desc + ", " + cid + " - " + uf);
        }
        return enderecoCompleto;
    }

    public String getColocarMascaraPesquisa() {
        masc = "";
        if (porPesquisa.equals("cpf")) {
            masc = "cpf";
        }
        return masc;
    }

    public String getColocarMaxlenghtPesquisa() {
        maxl = "50";
        if (porPesquisa.equals("cpf")) {
            maxl = "14";
        }
        return maxl;
    }

    public boolean comparaEndereco(PessoaEndereco pessoaEnde1, PessoaEndereco pessoaEnde2) {
        boolean compara;
        if (pessoaEnde1 != null && pessoaEnde2 != null) {
            if ((pessoaEnde1.getEndereco().getId() == pessoaEnde2.getEndereco().getId()
                    && pessoaEnde1.getNumero().equals(pessoaEnde2.getNumero())
                    && pessoaEnde1.getComplemento().equals(pessoaEnde2.getComplemento()))) {
                compara = true;
            } else {
                compara = false;
            }
        } else {
            compara = false;
        }
        return compara;
    }

    public List<SelectItem> getListaPaises() {
        // String[] lista = new String[]{};
        String[] lista = new String[]{
            "Africana(o)",
            "Afegã(o)",
            "Alemã(o)",
            "Americana(o)",
            "Angolana(o)",
            "Argelina(o)",
            "Argentina(o)",
            "Asiática(o)",
            "Australiana(o)",
            "Belga(o)",
            "Boliviana(o)",
            "Brasileira(o)",
            "Canadense(o)",
            "Canadiana(o)",
            "Chilena(o)",
            "Chinesa(o)",
            "Colombiana(o)",
            "Cubana(o)",
            "Da Nova Zelândia(o)",
            "Dinamarquesa(o)",
            "Egípcia(o)",
            "Equatoriana(o)",
            "Espanha(o)",
            "Espanhola(o)",
            "Europeu(o)",
            "Finlandesa(o)",
            "Francesa(o)",
            "Grega(o)",
            "Haitiana(o)",
            "Holandesa(o)",
            "Hondurenha(o)",
            "Hungara(o)",
            "Indiana(o)",
            "Inglesa(o)",
            "Iraneana(o)",
            "Iraquiana(o)",
            "Italiana(o)",
            "Jamaicana(o)",
            "Japonesa(o)",
            "Marroquina(o)",
            "Mexicana(o)",
            "Norte Americana(o)",
            "Norueguesa(o)",
            "Paquistanesa(o)",
            "Paraguaia(o)",
            "Peruana(o)",
            "Polaca(o)",
            "Portuguesa(o)",
            "Queniana(o)",
            "Russa(o)",
            "Sueca(o)",
            "Suiça(o)",
            "Sul-Africana(o)",
            "Sul-Coreana(o)",
            "Turca(o)",
            "Uraguaia(o)",
            "Venezuelana(o)"};
        List<SelectItem> selectPais = new ArrayList<SelectItem>();
        int i = 0;
        while (i < lista.length) {
            selectPais.add(new SelectItem(new Integer(i), lista[i], String.valueOf(i)));
            i++;
        }
//        if (selectPais != null) {
        if (fisica.getId() != -1) {
            for (i = 0; i < selectPais.size(); i++) {
                if (selectPais.get(i).getLabel().equals(fisica.getNacionalidade())) {
                    idPais = i;
                }
            }
        }
//        }
        return selectPais;
    }

    public List<SelectItem> getListaProfissoes() {
        if (listaProfissoes.isEmpty()) {
            ProfissaoDB db = new ProfissaoDBToplink();
            List lista = db.pesquisaTodos();
            for (int i = 0; i < lista.size(); i++) {
                listaProfissoes.add(new SelectItem(new Integer(i),
                        (String) ((Profissao) lista.get(i)).getProfissao(),
                        Integer.toString(((Profissao) lista.get(i)).getId())));
            }
        }
        return listaProfissoes;
    }

    public String getCidadeNaturalidade() {
        String nat;
        Cidade cidade;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa") != null) {
            cidade = (Cidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa");
            nat = cidade.getCidade();
            nat = nat + " - " + cidade.getUf();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
            fisica.setNaturalidade(nat);
        }

        if (!fisica.getNaturalidade().isEmpty()) {
            nat = fisica.getNaturalidade() + " <<<";
            return nat;
        }

        if (fisica.getId() == -1 || fisica.getNaturalidade().isEmpty()) {
            PessoaEnderecoDB dbPes = new PessoaEnderecoDBToplink();
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            Filial fili = (Filial) salvarAcumuladoDB.pesquisaCodigo(1, "Filial");
            if (fili != null) {
                Pessoa pes = fili.getMatriz().getPessoa();
                cidade = ((PessoaEndereco) dbPes.pesquisaEndPorPessoa(pes.getId()).get(0)).getEndereco().getCidade();
                nat = cidade.getCidade();
                nat = nat + " - " + cidade.getUf();
                fisica.setNaturalidade(nat);
                nat = nat + " <<<";
                return nat;
            }
        }
        return null;
    }

    public String getPessoaImagem() {
        FacesContext context = FacesContext.getCurrentInstance();
        File files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/"));
        File fExiste = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg"));
        File listFile[] = files.listFiles();
        String nome = "";
        String caminho;
        //temFoto = false;
        if (fExiste.exists() && fisica.getDataFoto().isEmpty()) {
            fotoTemp = true;
        } else if (fExiste.exists()) {
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
                    if (Integer.parseInt(n) == fisica.getPessoa().getId()) {
                        nome = n;
                        fotoTemp = false;
                        temFoto = true;
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

    public void salvarImagem() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (fisica.getId() != -1) {
            String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + String.valueOf(fisica.getPessoa().getId()) + ".jpg");
            String caminho2 = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg");
            try {
                File fl = new File(caminho);
                File item = new File(caminho2);
                FileInputStream in = new FileInputStream(item);
                FileOutputStream out = new FileOutputStream(fl.getPath());

                byte[] buf = new byte[(int) item.length()];
                int count;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
                in.close();
                out.flush();
                out.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

//    public void upload(FileUploadEvent event){
//        this.itens.add(event.getUploadedFile());
//        UploadedFile item = event.getUploadedFile();
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        String nomeArq = "fotoTemp";
//        fotoTemp = true;
//        
//        String caminho = ((ServletContext) facesContext.getExternalContext().getContext()).getRealPath("/Imagens/Fotos/");
//        caminho = caminho + "/" + nomeArq+".jpg";
//        try{
//            File fl = new File(caminho);
//            InputStream in = item.getInputStream();
//            FileOutputStream out = new FileOutputStream(fl.getPath());
//
//            byte[] buf = new byte[(int)item.getSize()];
//            int count;
//            while ((count = in.read(buf)) >= 0) {
//                out.write(buf, 0, count);
//            }
//            in.close();
//            out.flush();
//            out.close();
//            temFoto = true;
//            }catch(Exception e){
//                temFoto = false;
//                System.out.println(e);
//            }
//    }
    public void excluirImagemSozinha() {
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg");
        try {
            File fl = new File(caminho);
            if (fl.exists()) {
                fl.delete();
            } else if (fisica.getId() != -1) {
                caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + String.valueOf(fisica.getPessoa().getId()) + ".jpg");
                fl = new File(caminho);
                fl.delete();
                SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
                fisica.setDataFoto("");
                sv.abrirTransacao();
                sv.alterarObjeto(fisica);
                sv.comitarTransacao();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void excluirImagem() {
        FacesContext context = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + String.valueOf(fisica.getPessoa().getId()) + ".jpg");
        try {
            File fl = new File(caminho);
            if (fl.exists()) {
                fl.delete();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String excluirEmpresaAnterior(PessoaEmpresa pe) {
        HomologacaoDB dbAge = new HomologacaoDBToplink();
        List<Agendamento> agendas = dbAge.pesquisaAgendamentoPorPessoaEmpresa(pe.getId());
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        for (int i = 0; i < agendas.size(); i++) {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto((Agendamento) salvarAcumuladoDB.pesquisaCodigo(agendas.get(i).getId(), "Agendamento"))) {
                salvarAcumuladoDB.comitarTransacao();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
            }
        }
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.deletarObjeto((PessoaEmpresa) salvarAcumuladoDB.pesquisaCodigo(pe.getId(), "PessoaEmpresa"))) {
            salvarAcumuladoDB.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Empresa removida com sucesso");
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Não foi possível remover esta empresa!");
        }
        listaPessoaEmpresa.clear();
        return null;
    }

    public String removerJuridicaPesquisada() {
        PessoaEmpresaDB db = new PessoaEmpresaDBToplink();
        if (pessoaEmpresa.getId() != -1) {
            db.getEntityManager().getTransaction().begin();
            if (db.delete(db.pesquisaCodigo(pessoaEmpresa.getId()))) {
                db.getEntityManager().getTransaction().commit();
            } else {
                db.getEntityManager().getTransaction().rollback();
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        renderJuridicaPesquisa = false;
        return "pessoaFisica";
    }

    public String associarFisica() {
        if (fisica.getId() == -1) {
            msgSocio = "Cadastre uma pessoa fisica para associar!";
            return "pessoaFisica";
        } else {
            msgSocio = "";
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaPesquisa", fisica);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaEmpresaPesquisa", pessoaEmpresa);

        }
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).socios();
    }

    public String hojeRecadastro() {
        fisica.setRecadastro(DataHoje.data());
        return null;
    }

    public List getItens() {
        return itens;
    }

    public void setItens(List itens) {
        this.itens = itens;
    }

    public String getRenEndereco() {
        return renEndereco;
    }

    public void setRenEndereco(String renEndereco) {
        this.renEndereco = renEndereco;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public PessoaProfissao getPessoaProfissao() {
        return pessoaProfissao;
    }

    public void setPessoaProfissao(PessoaProfissao pessoaProfissao) {
        this.pessoaProfissao = pessoaProfissao;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public PessoaEndereco getPessoaEndereco() {
        return pessoaEndereco;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setIndicaTab(String indicaTab) {
        this.indicaTab = indicaTab;
    }

    public String getIndicaTab() {
        return indicaTab;
    }

    public Fisica getFisica() {
        if (fisica.getId() == -1) {
            tipo = "novo";
        } else {
            tipo = "naonovo";
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getRenNovoEndereco() {
        return renNovoEndereco;
    }

    public void setRenNovoEndereco(String renNovoEndereco) {
        this.renNovoEndereco = renNovoEndereco;
    }

    public String getRenAbreEnd() {
        return renAbreEnd;
    }

    public void setRenAbreEnd(String renAbreEnd) {
        this.renAbreEnd = renAbreEnd;
    }

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public int getIdProfissao() {
        return idProfissao;
    }

    public void setIdProfissao(int idProfissao) {
        this.idProfissao = idProfissao;
    }

    public PessoaEmpresa getPessoaEmpresa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            pessoaEmpresa.setJuridica((Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa"));
            renderJuridicaPesquisa = true;
        }

        return pessoaEmpresa;
    }

    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
        this.pessoaEmpresa = pessoaEmpresa;
    }

    public boolean isRenderJuridicaPesquisa() {
        return renderJuridicaPesquisa;
    }

    public void setRenderJuridicaPesquisa(boolean renderJuridicaPesquisa) {
        this.renderJuridicaPesquisa = renderJuridicaPesquisa;
    }

    public boolean isTemFoto() {
        return temFoto;
    }

    public void setTemFoto(boolean temFoto) {
        this.temFoto = temFoto;
    }

    public int getIdIndexEndereco() {
        return idIndexEndereco;
    }

    public void setIdIndexEndereco(int idIndexEndereco) {
        this.idIndexEndereco = idIndexEndereco;
    }

    public int getIdIndexFisica() {
        return idIndexFisica;
    }

    public void setIdIndexFisica(int idIndexFisica) {
        this.idIndexFisica = idIndexFisica;
    }

    public List<PessoaEmpresa> getListaPessoaEmpresa() {
        PessoaEmpresaDB db = new PessoaEmpresaDBToplink();
        if (fisica.getId() != -1) {
            listaPessoaEmpresa = db.listaPessoaEmpresaPorFisica(fisica.getId());
        }
        return listaPessoaEmpresa;
    }

    public void setListaPessoaEmpresa(List<PessoaEmpresa> listaPessoaEmpresa) {
        this.listaPessoaEmpresa = listaPessoaEmpresa;
    }

    public int getIdIndexPessoaEmp() {
        return idIndexPessoaEmp;
    }

    public void setIdIndexPessoaEmp(int idIndexPessoaEmp) {
        this.idIndexPessoaEmp = idIndexPessoaEmp;
    }

    public Socios getSocios() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("socioPesquisa") != null) {
            socios = (Socios) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("socioPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("socioPesquisa");
        }
        return socios;
    }

    public void setSocios(Socios socios) {
        this.socios = socios;
    }

    public String getMsgSocio() {
        return msgSocio;
    }

    public void setMsgSocio(String msgSocio) {
        this.msgSocio = msgSocio;
    }

    public String getLblSocio() {
        if (socios.getId() == -1) {
            lblSocio = "ASSOCIAR";
        } else {
            lblSocio = "CADASTRO";
        }
        return lblSocio;
    }

    public void setLblSocio(String lblSocio) {
        this.lblSocio = lblSocio;
    }

    public String getPesquisaPor() {
        return pesquisaPor;
    }

    public void setPesquisaPor(String pesquisaPor) {
        this.pesquisaPor = pesquisaPor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoSocio() {
        if (socios.getId() != -1) {
            SociosDB db = new SociosDBToplink();
            Socios soc = db.pesquisaSocioDoDependente(socios.getServicoPessoa().getPessoa());
            if (soc == null) {
                tipoSocio = "Titular";
            } else {
                tipoSocio = "Dependente";
            }
        } else {
            tipoSocio = "";
        }
        return tipoSocio;
    }

    public void setTipoSocio(String tipoSocio) {
        this.tipoSocio = tipoSocio;
    }

    public void limparCamposData() {
        if (pessoaEmpresa.getId() != -1) {
            if (pessoaEmpresa.getJuridica().getId() != -1) {
                if (!pessoaEmpresa.getDemissao().equals("")) {
                    pessoaEmpresa.setAdmissao("");
                    pessoaEmpresa.setDemissao("");
                    pessoaEmpresa = new PessoaEmpresa();
                }
            }
        }
    }

    public PessoaComplemento getPessoaComplemento() {
        return pessoaComplemento;
    }

    public void setPessoaComplemento(PessoaComplemento pessoaComplemento) {
        this.pessoaComplemento = pessoaComplemento;
    }

    public void upload() {
        try {
            fileContent = new Scanner(file.getInputStream())
                    .useDelimiter("\\A").next();
        } catch (IOException e) {
            // Error handling
        }
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
        List<FacesMessage> msgs = new ArrayList<FacesMessage>();
        Part file = (Part) value;
        if (file.getSize() > 1024) {
            msgs.add(new FacesMessage("file too big"));
        }
        if (!"text/plain".equals(file.getContentType())) {
            msgs.add(new FacesMessage("not a text file"));
        }
        if (!msgs.isEmpty()) {
            throw new ValidatorException(msgs);
        }
    }
}
