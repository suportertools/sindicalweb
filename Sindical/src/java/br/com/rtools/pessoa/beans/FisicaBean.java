package br.com.rtools.pessoa.beans;

import br.com.rtools.arrecadacao.db.OposicaoDBToplink;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.beans.SociosBean;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.beans.PesquisaEnderecoBean;
import br.com.rtools.endereco.db.EnderecoDB;
import br.com.rtools.endereco.db.EnderecoDBToplink;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.db.HomologacaoDB;
import br.com.rtools.homologacao.db.HomologacaoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.utilitarios.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.component.tabview.TabView;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class FisicaBean extends PesquisarProfissaoBean implements Serializable {

    private Fisica fisica = new Fisica();

    private PessoaProfissao pessoaProfissao = new PessoaProfissao();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();

    private Usuario usuario = new Usuario();
    private PessoaComplemento pessoaComplemento = new PessoaComplemento();
    private Socios socios = new Socios();
    //private String renEndereco = "false";
    private String indicaTab = "pessoal";
    private String enderecoCompleto = "";
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "I";
    private String mensagem = "";
    private String masc = "";
    private String maxl = "";
    private String enderecoCobranca = "";
    private String renAbreEnd = "true";
    private String msgSocio = "";
    private String lblSocio = "";
    private String pesquisaPor = "";
    private String tipo = "";
    private String tipoSocio = "";
    private int indexPessoaFisica = 0;
    private String indexNovoEndereco = "";
    private boolean alterarEnd = false;
    private boolean endResidencial = false;
    private boolean fotoTemp = false;
    private boolean renderJuridicaPesquisa = false;
    public List itens = new ArrayList();
    private List<DataObject> listaPessoa = new ArrayList();
    private List<Fisica> listaPessoaFisica = new ArrayList<Fisica>();
    private List<PessoaEmpresa> listaPessoaEmpresa = new ArrayList();
    private final List<SelectItem> listaProfissoes = new ArrayList();
    private List<SelectItem> listaPaises = new ArrayList();
    private int idPais = 11;
    private int idProfissao = 0;
    private int idIndexFisica = 0;
    private int idIndexPessoaEmp = 0;
    private Part file;
    private String fileContent = "";
    // ALTERAÇÕES
    private String fotoPerfil = "";
    private String fotoArquivo = "";
    private String fotoTempPerfil = "";
    private String fotoTempArquivo = "";
    private String cliente = "";
    private boolean readyOnlineNaturalidade = true;
    private boolean disabledNaturalidade = false;
    private String[] imagensTipo = new String[]{"jpg", "jpeg", "png", "gif"};
    private List<Socios> listaSocioInativo = new ArrayList<Socios>();
    private String mask = "";

    private Endereco enderecox = new Endereco();
    private List<PessoaEndereco> listaPessoaEndereco = new ArrayList<PessoaEndereco>();
    private String numero = "";
    private String complemento = "";
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private boolean visibleEditarEndereco = false;
    private List<ServicoPessoa> listaServicoPessoa = new ArrayList<ServicoPessoa>();
    private boolean chkDependente = false;
    private boolean pessoaOposicao = false;

    private int indexEndereco = 0;
    private String strEndereco = "";
    private Integer tipoCadastro = -1;

    public void novo() {
        GenericaSessao.put("fisicaBean", new FisicaBean());
        GenericaSessao.put("pessoaComplementoBean", new PessoaComplementoBean());
        GenericaSessao.put("pesquisaEnderecoBean", new PesquisaEnderecoBean());
    }

    public String getEnderecoCobranca() {
        for (PessoaEndereco pe : listaPessoaEndereco) {
            String strCompl = "";
            if (pe.getTipoEndereco().getId() == 3) {
                if (pe.getComplemento().isEmpty()) {
                    strCompl = " ";
                } else {
                    strCompl = " ( " + pe.getComplemento() + " ) ";
                }

                return enderecoCobranca = pe.getEndereco().getLogradouro().getDescricao() + " "
                        + pe.getEndereco().getDescricaoEndereco().getDescricao() + ", " + pe.getNumero() + " " + pe.getEndereco().getBairro().getDescricao() + ","
                        + strCompl + pe.getEndereco().getCidade().getCidade() + " - " + pe.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(pe.getEndereco().getCep());
            }
        }
        return enderecoCobranca;
    }

    public void salvar() {
        NovoLog logs = new NovoLog();
        FisicaDB db = new FisicaDBToplink();
        Pessoa pessoa = fisica.getPessoa();
        List listDocumento;
        if ((listaPessoaEndereco.isEmpty() || pessoa.getId() == -1) && enderecox.getId() != -1) {
            adicionarEnderecos();
        }

        boolean sucesso = false;
        Dao dao = new Dao();
        dao.openTransaction();
        pessoaUpper();
        if ((fisica.getPessoa().getId() == -1) && (fisica.getId() == -1)) {
            fisica.getPessoa().setTipoDocumento((TipoDocumento) dao.find(new TipoDocumento(), 1));
            if (!db.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
                    fisica.getDtNascimento(),
                    fisica.getRg()).isEmpty()) {
                mensagem = "Esta pessoa já esta cadastrada!";
                return;
            }
            if (fisica.getNascimento().isEmpty() || fisica.getNascimento().length() < 10) {
                mensagem = "Data de nascimento esta inválida!";
                return;
            }
            if (!fisica.getNascimento().isEmpty()) {
                if (!DataHoje.isDataValida(fisica.getNascimento())) {
                    mensagem = "Data de nascimento esta inválida!";
                    return;
                }
            }
            if (pessoa.getDocumento().equals("") || pessoa.getDocumento().equals("0")) {
                pessoa.setDocumento("0");
            } else {
                if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                    mensagem = "Documento Invalido!";
                    return;
                }
                listDocumento = db.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
                if (!listDocumento.isEmpty()) {
                    mensagem = "Documento já existente!";
                    return;
                }
            }
            if (fisica.getPessoa().getNome().equals("")) {
                mensagem = "O campo nome não pode ser nulo! ";
                return;
            }
            if (dao.save(pessoa)) {
                fisica.setNacionalidade(getListaPaises().get(idPais).getLabel());
                fisica.setPessoa(pessoa);
                if (dao.save(fisica)) {

                    GenericaSessao.put("fisicaPesquisa", fisica);
                    mensagem = "Cadastro salvo com Sucesso!";
                    logs.save("ID " + fisica.getId()
                            + " - Pessoa: " + fisica.getPessoa().getId()
                            + " - Nome: " + fisica.getPessoa().getNome()
                            + " - Nascimento: " + fisica.getNascimento()
                            + " - CPF: " + fisica.getPessoa().getDocumento()
                            + " - RG: " + fisica.getRg());
                    dao.commit();
                    sucesso = true;
                } else {
                    mensagem = "Erro ao Salvar Pessoa Fisica!";
                    dao.rollback();
                }
            } else {
                mensagem = "Erro ao Salvar Pessoa!";
                dao.rollback();
            }
        } else {
            fisica.getPessoa().setTipoDocumento((TipoDocumento) dao.find(new TipoDocumento(), 1));
            Fisica f = (Fisica) dao.find(new Fisica(), fisica.getId());
            String antes = " De: ID - " + fisica.getId()
                    + " Nome: " + f.getPessoa().getNome() + " - "
                    + " Nascimento: " + f.getNascimento() + " - "
                    + " CPF: " + f.getPessoa().getDocumento() + " - "
                    + " RG: " + f.getRg();
            if (fisica.getPessoa().getDocumento().equals("") || fisica.getPessoa().getDocumento().equals("0")) {
                fisica.getPessoa().setDocumento("0");
            } else {
                if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                    mensagem = "Documento Inválido!";
                    return;
                }
                listDocumento = db.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
                for (Object listDocumento1 : listDocumento) {
                    if (!listDocumento.isEmpty() && ((Fisica) listDocumento1).getId() != fisica.getId()) {
                        mensagem = "Documento já existente!";
                        return;
                    }
                }
            }
            if (fisica.getNascimento().isEmpty() || fisica.getNascimento().length() < 10) {
                mensagem = "Data de Nascimento esta inválida!";
                return;
            }
            List<Fisica> fisi = db.pesquisaFisicaPorNomeNascRG(fisica.getPessoa().getNome(),
                    fisica.getDtNascimento(),
                    fisica.getRg());
            if (!fisi.isEmpty()) {
                for (Fisica fisi1 : fisi) {
                    if (fisi1.getId() != fisica.getId()) {
                        mensagem = "Esta pessoa já esta cadastrada!";
                        return;
                    }
                }
            }

            fisica.setNacionalidade(getListaPaises().get(idPais).getLabel());
            if (dao.update(fisica.getPessoa())) {
                logs.update(antes,
                        " para: Nome: " + fisica.getPessoa().getNome() + " - "
                        + " Nascimento: " + f.getNascimento() + " - "
                        + " CPF: " + fisica.getPessoa().getDocumento() + " - "
                        + " RG: " + fisica.getRg());
            } else {
                dao.rollback();
                return;
            }

            if (dao.update(fisica)) {
                GenericaSessao.put("fisicaPesquisa", fisica);
                mensagem = "Cadastro atualizado com Sucesso!";
                sucesso = true;
                dao.commit();
            } else {
                mensagem = "Erro ao Atualizar!";
                dao.rollback();
            }
        }
        salvarEndereco();
        salvarPessoaEmpresa();
        salvarPessoaProfissao();
        //limparCamposData();
        if (sucesso) {
            salvarImagem();
        }
    }

    public void pessoaUpper() {
        fisica.getPessoa().setNome(fisica.getPessoa().getNome().toUpperCase());
        fisica.setRg(fisica.getRg().toUpperCase());
        fisica.setPai(fisica.getPai().toUpperCase());
        fisica.setMae(fisica.getMae().toUpperCase());
    }

    public void salvarPessoaProfissao() {
        if (!listaProfissoes.isEmpty()) {
            Dao dao = new Dao();
            dao.openTransaction();
            pessoaProfissao.setProfissao((Profissao) dao.find(new Profissao(), Integer.parseInt(listaProfissoes.get(idProfissao).getDescription())));
            if (fisica.getId() == -1) {
                pessoaProfissao = new PessoaProfissao();
                pessoaProfissao.setFisica(fisica);
                if (!dao.save(pessoaProfissao)) {
                    dao.rollback();
                } else {
                    dao.commit();
                }
            } else {
                if (pessoaProfissao.getId() == -1) {
                    pessoaProfissao.setFisica(fisica);
                    if (!dao.save(pessoaProfissao)) {
                        dao.rollback();
                        return;
                    }
                } else {
                    if (!dao.update(pessoaProfissao)) {
                        dao.rollback();
                        return;
                    }
                }
                dao.commit();
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

    public String novoOK() {
        if (fisica.getId() == -1) {
            GenericaSessao.put("fisicaBean", new FisicaBean());
        }
        return "pessoaFisica";
    }

    public void salvarEndereco() {
        //List endPorPessoa = getPesquisaEndPorPessoa();
        if (fisica.getId() != -1) {
            Dao dao = new Dao();
            if (!listaPessoaEndereco.isEmpty()) {
                dao.openTransaction();
                for (PessoaEndereco pe : listaPessoaEndereco) {
                    if (pe.getId() == -1) {
                        if (!dao.save(pe)) {
                            GenericaMensagem.warn("Erro", "Não foi possivel SALVAR endereço!");
                            dao.rollback();
                            return;
                        }
                    } else {
                        if (!dao.update(pe)) {
                            GenericaMensagem.warn("Erro", "Não foi possivel ALTERAR endereço!");
                            dao.rollback();
                            return;
                        }
                    }
                }

                dao.commit();
            }

        }
    }

    public void excluir() {
        if (socios.getId() != -1) {
            mensagem = "Esse cadastro esta associado, desvincule para excluir!";
            return;
        }
        //PessoaDB dbPessoa = new PessoaDBToplink();
        if (fisica.getId() != -1) {
            //fisica.setPessoa(dbPessoa.pesquisaCodigo(fisica.getPessoa().getId()));
            PessoaEnderecoDB dbPE = new PessoaEnderecoDBToplink();
            List<PessoaEndereco> listaEndereco = dbPE.pesquisaEndPorPessoa(fisica.getPessoa().getId());
            Dao dao = new Dao();
            dao.openTransaction();
            // EXCLUI ENDEREÇO -----------------
            if (!listaEndereco.isEmpty()) {
                for (PessoaEndereco listaEndereco1 : listaEndereco) {
                    if (!dao.delete(listaEndereco1)) {
                        dao.rollback();
                        mensagem = "Erro ao excluir endereços!";
                        return;
                    }
                }
            }
            PessoaProfissaoDB dbPP = new PessoaProfissaoDBToplink();
            PessoaProfissao pp = dbPP.pesquisaProfPorFisica(fisica.getId());
            // EXCLUI PROFISSÃO -----------------
            if (pp.getId() != -1) {
                if (!dao.delete(pp)) {
                    dao.rollback();
                    mensagem = "Erro ao excluir profissão!";
                    return;
                }
            }
            // EXCLUI PESSOA EMPRESA ------------
            PessoaEmpresaDB dbEM = new PessoaEmpresaDBToplink();
            List<PessoaEmpresa> listaPessoaEmp = dbEM.listaPessoaEmpresaTodos(fisica.getId());
            if (!listaPessoaEmp.isEmpty()) {
                for (PessoaEmpresa listaPessoaEmp1 : listaPessoaEmp) {
                    if (!dao.delete(listaPessoaEmp1)) {
                        dao.rollback();
                        mensagem = "Erro ao excluir pessoas empresa!";
                        return;
                    }
                }
            }
            if (!dao.delete(fisica)) {
                dao.rollback();
                mensagem = "Física não pode ser excluída!";
                return;
            }
            if (!dao.delete(fisica.getPessoa())) {
                dao.rollback();
                mensagem = "Cadastro Pessoa não pode ser excluída!";
                return;
            }
            dao.commit();
            excluirImagem();
            NovoLog logs = new NovoLog();
            logs.delete("ID: " + fisica.getId() + " - Pessoa: " + fisica.getPessoa().getId() + " - Nascimento: " + fisica.getNascimento() + " - Nome: " + fisica.getPessoa().getNome() + " - CPF: " + fisica.getPessoa().getDocumento() + " - RG: " + fisica.getRg());
            //GenericaSessao.put("fisicaBean", new FisicaBean());
            //((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).setMensagem("Cadastro Excluido com sucesso!");            
            novo();
            mensagem = "Cadastro Excluido com sucesso!";
        } else {
            mensagem = "Pesquise uma pessoa física para ser excluída!";
        }
    }

    public String editarFisica(Fisica f) {
        if (fisica.getId() != f.getId()) {
            FacesContext context = FacesContext.getCurrentInstance();
            File fExiste = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg"));
            if (fExiste.exists()) {
                fExiste.delete();
            }
        }
        PessoaEmpresaDB db = new PessoaEmpresaDBToplink();
        PessoaProfissaoDB dbp = new PessoaProfissaoDBToplink();
        fisica = f;
        GenericaSessao.remove("pessoaComplementoBean");
        GenericaSessao.put("fisicaPesquisa", fisica);
        String url = (String) GenericaSessao.getString("urlRetorno");
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        alterarEnd = true;
        listaPessoa = new ArrayList();
        msgSocio = "";
        pessoaEmpresa = (PessoaEmpresa) db.pesquisaPessoaEmpresaPorFisica(fisica.getId());
        if (pessoaEmpresa.getId() != -1) {
            if (pessoaEmpresa.getFuncao() != null) {
                profissao = pessoaEmpresa.getFuncao();
            } else {
                profissao = new Profissao();
            }
            //GenericaSessao.put("juridicaPesquisa", pessoaEmpresa.getJuridica());
            renderJuridicaPesquisa = true;
        } else {
            GenericaSessao.remove("juridicaPesquisa");
            profissao = new Profissao();
            renderJuridicaPesquisa = false;
        }

        pessoaProfissao = dbp.pesquisaProfPorFisica(fisica.getId());
        if (pessoaProfissao.getId() != -1) {
            for (int i = 0; i < listaProfissoes.size(); i++) {
                if (Objects.equals(Integer.valueOf(listaProfissoes.get(i).getDescription()), pessoaProfissao.getProfissao().getId())) {
                    idProfissao = i;
                    break;
                }
            }
        }
        for (int i = 0; i < listaPaises.size(); i++) {
            if ((listaPaises.get(i).getLabel().toUpperCase()).equals(fisica.getNacionalidade().toUpperCase())) {
                idPais = i;
                break;
            }
        }
        indexNovoEndereco = "";
        strEndereco = "";
        listaPessoaEndereco.clear();
        getListaPessoaEndereco();
        listaServicoPessoa.clear();
        editarFisicaSocio(fisica);
        showImagemFisica();
        GenericaSessao.put("linkClicado", true);
        existePessoaOposicaoPorPessoa();
        return url;
    }

    public void showImagemFisica() {
        String caminhoTemp = "/Cliente/" + getCliente() + "/Imagens/Fotos/";
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(caminhoTemp);
        for (String imagensTipo1 : imagensTipo) {
            File f = new File(arquivo + "/" + fisica.getPessoa().getId() + "." + imagensTipo1);
            if (f.exists()) {
                fotoPerfil = caminhoTemp + "/" + fisica.getPessoa().getId() + "." + imagensTipo1;
                fotoTempPerfil = "";
                break;
            } else {
                fotoPerfil = "";
                fotoTempPerfil = "";
            }
        }
    }

    public void existePessoaDocumento() {
        if (!fisica.getPessoa().getDocumento().isEmpty() && !fisica.getPessoa().getDocumento().equals("___.___.___-__") && fisica.getId() == -1) {
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(fisica.getPessoa().getDocumento()))) {
                mensagem = "Documento Invalido!";
                GenericaMensagem.warn("Validação", "Documento (CPF) inválido! " + fisica.getPessoa().getDocumento());
                PF.update("form:pessoa_fisica:i_tabview_fisica:id_valida_documento:" + fisica.getPessoa().getDocumento());
                fisica.getPessoa().setDocumento("");
                return;
            }
            FisicaDB db = new FisicaDBToplink();
            List lista = db.pesquisaFisicaPorDoc(fisica.getPessoa().getDocumento());
            if (!lista.isEmpty()) {
                String x = editarFisicaParametro((Fisica) lista.get(0));
                pessoaUpper();
                RequestContext.getCurrentInstance().update("form_pessoa_fisica:i_panel_pessoa_fisica");
                RequestContext.getCurrentInstance().update("form_pessoa_fisica:i_end_rendered");
                getListaPessoaEndereco();
                showImagemFisica();
            }
            existePessoaOposicaoPorPessoa();
            PessoaProfissaoDB dbp = new PessoaProfissaoDBToplink();
            pessoaProfissao = dbp.pesquisaProfPorFisica(fisica.getId());
            if (pessoaProfissao.getId() != -1) {
                for (int i = 0; i < listaProfissoes.size(); i++) {
                    if (Objects.equals(Integer.valueOf(listaProfissoes.get(i).getDescription()), pessoaProfissao.getProfissao().getId())) {
                        idProfissao = i;
                        break;
                    }
                }
            }
            RequestContext.getCurrentInstance().update("form_pessoa_fisica:i_p_o");
        }
    }

    public void existePessoaNomeNascimento() {
        if (fisica.getId() == -1) {
            if (!fisica.getNascimento().isEmpty() && !fisica.getPessoa().getNome().isEmpty()) {
                FisicaDB db = new FisicaDBToplink();
                Fisica f = db.pesquisaFisicaPorNomeNascimento(fisica.getPessoa().getNome(), fisica.getDtNascimento());
                if (f != null) {
                    String x = editarFisicaParametro(f);
                    pessoaUpper();
                    RequestContext.getCurrentInstance().update("form_pessoa_fisica:i_panel_pessoa_fisica");
                    showImagemFisica();
                }
            }
        }
    }

    public String editarFisicaParametro(Fisica fis) {
        PessoaEmpresaDB db = new PessoaEmpresaDBToplink();
        fisica = fis;
        GenericaSessao.put("fisicaPesquisa", fisica);
        String url = (String) GenericaSessao.getString("urlRetorno");
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        alterarEnd = true;
        pessoaEmpresa = (PessoaEmpresa) db.pesquisaPessoaEmpresaPorFisica(fisica.getId());
        if (pessoaEmpresa.getId() != -1) {
            if (pessoaEmpresa.getFuncao() != null) {
                profissao = pessoaEmpresa.getFuncao();
            } else {
                profissao = new Profissao();
            }
            //GenericaSessao.put("juridicaPesquisa", pessoaEmpresa.getJuridica());
            renderJuridicaPesquisa = true;
        } else {
            GenericaSessao.remove("juridicaPesquisa");
            profissao = new Profissao();
            renderJuridicaPesquisa = false;
        }

        listaServicoPessoa.clear();
        editarFisicaSocio(fisica);
        GenericaSessao.put("linkClicado", true);
        
        showImagemFisica();
        getListaSocioInativo().clear();
        getListaSocioInativo();
        getListaPessoaEndereco().clear();
        getListaPessoaEndereco();
        getStrEndereco();
        return url;
    }

    public void editarFisicaSocio(Fisica fis) {
        SociosDB db = new SociosDBToplink();
        socios = db.pesquisaSocioPorPessoaAtivo(fisica.getPessoa().getId());
        if (socios.getId() == -1) {
            socios = new SociosDBToplink().pesquisaSocioTitularInativoPorPessoa(fisica.getPessoa().getId());
            if (socios == null) {
                socios = new Socios();
            }
        }
        listaSocioInativo.clear();
    }

    public List getListaFisica() {
        FisicaDB db = new FisicaDBToplink();
        List result = db.pesquisaTodos();
        return result;
    }

    /**
     * TIPO ENDERECO PESSOA FÍSICA {1,3,4}
     */
    public void alterarEndereco() {
        visibleEditarEndereco = false;
        enderecox = new Endereco();
        for (PessoaEndereco pe : listaPessoaEndereco) {

        }
    }

    public void alterarTodosEndereco() {
        visibleEditarEndereco = false;

        for (int i = 0; i < listaPessoaEndereco.size(); i++) {
            listaPessoaEndereco.get(i).setEndereco(pessoaEndereco.getEndereco());
            listaPessoaEndereco.get(i).setNumero(pessoaEndereco.getNumero());
            listaPessoaEndereco.get(i).setComplemento(pessoaEndereco.getComplemento());
        }

        enderecox = new Endereco();
    }

    public void adicionarEnderecos() {
        Dao dao = new Dao();
        List<TipoEndereco> tipoEnderecos = (List<TipoEndereco>) dao.find("TipoEndereco", new int[]{1, 3, 4});
        if (enderecox.getId() != -1) {

            listaPessoaEndereco.clear();
            for (TipoEndereco tipoEndereco : tipoEnderecos) {
                listaPessoaEndereco.add(new PessoaEndereco(
                        -1,
                        enderecox,
                        tipoEndereco,
                        fisica.getPessoa(),
                        numero,
                        complemento
                ));
            }
        }

        enderecox = new Endereco();
        if (1 == 1) {
            return;
        }
        // COMPARA ENDERECOS
        //comparaEndereco(pessoaEndeAnt, (PessoaEndereco) listaEnd.get(u))
        Endereco endereco = new Endereco();

        //GenericaSessao.put("enderecoNum", pessoaEndereco.getNumero());
        //GenericaSessao.put("enderecoComp", pessoaEndereco.getComplemento());
    }

    public void editarPessoaEndereco(PessoaEndereco pessoaEnderecox, int index) {
        pessoaEndereco = pessoaEnderecox;
        visibleEditarEndereco = true;
        indexEndereco = index;
        //GenericaSessao.put("enderecoPesquisa", pessoaEndereco.getEndereco());
        //log = pessoaEndereco.getEndereco().getLogradouro().getDescricao();
        //desc = pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao();
        //cid = pessoaEndereco.getEndereco().getCidade().getCidade();
        //uf = pessoaEndereco.getEndereco().getCidade().getUf();
//        setEnderecoCompleto(log + " " + desc + ", " + cid + " - " + uf);
//        renEndereco = "false";
//        setRenNovoEndereco("true");
//        alterarEnd = true;
    }
//    public void editarPessoaEndereco(int index) {
//        pessoaEndereco = (PessoaEndereco) listaEnd.get(index);
//        GenericaSessao.put("enderecoPesquisa", pessoaEndereco.getEndereco());
//        log = pessoaEndereco.getEndereco().getLogradouro().getDescricao();
//        desc = pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao();
//        cid = pessoaEndereco.getEndereco().getCidade().getCidade();
//        uf = pessoaEndereco.getEndereco().getCidade().getUf();
//        setEnderecoCompleto(log + " " + desc + ", " + cid + " - " + uf);
//        renEndereco = "false";
//        setRenNovoEndereco("true");
//        alterarEnd = true;
//    }
//
//    public List getListaPessoaEndereco() {
//        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//        List list = salvarAcumuladoDB.listaObjeto("PessoaEndereco");
//        return list;
//    }

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

//    public String voltarEndereco() {
//        return "pessoaFisica";
//    }
    public boolean getHabilitar() {
        return fisica.getPessoa().getId() == -1;
    }

    public String excluirPessoaEndereco() {
        if (pessoaEndereco.getId() != -1) {
            Dao dao = new Dao();
            dao.delete(pessoaEndereco, true);
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
                mensagem = "Informar data de admissão!";
                return;
            }

            if (!pessoaEmpresa.getDemissao().isEmpty() && pessoaEmpresa.getDemissao() != null) {
                if (DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao())
                        > DataHoje.converteDataParaInteger(DataHoje.data())) {
                    mensagem = "Data de Demissão maior que atual!";
                    return;
                }
            }

            if (pessoaEmpresa.getDtAdmissao() != null && pessoaEmpresa.getDtDemissao() != null) {
                int dataAdmissao = DataHoje.converteDataParaInteger(pessoaEmpresa.getAdmissao());
                int dataDemissao = DataHoje.converteDataParaInteger(pessoaEmpresa.getDemissao());
                if (dataDemissao <= dataAdmissao) {
                    mensagem = "Data de demissão deve ser maior que data de admissão!";
                    pessoaEmpresa.setDemissao(null);
                    return;
                }
                pessoaEmpresa.setPrincipal(false);
            }

            if (profissao.getProfissao() == null || profissao.getProfissao().isEmpty()) {
                pessoaEmpresa.setFuncao(null);
            } else {
                pessoaEmpresa.setFuncao(profissao);
            }

            if (pessoaEmpresa.getId() == -1) {
                db.insert(pessoaEmpresa);
            } else {
                db.update(pessoaEmpresa);
            }

            if (pessoaEmpresa.getDemissao() != null && !pessoaEmpresa.getDemissao().isEmpty()) {
                pessoaEmpresa = new PessoaEmpresa();
                profissao = new Profissao();
                GenericaSessao.remove("juridicaPesquisa");
                renderJuridicaPesquisa = false;

                List<PessoaEmpresa> lpe = db.listaPessoaEmpresaPorFisicaDemissao(fisica.getId());

                if (!lpe.isEmpty()) {
                    lpe.get(0).setPrincipal(true);

                    db.update(lpe.get(0));

                    pessoaEmpresa = lpe.get(0);
                    renderJuridicaPesquisa = true;
                }
            }
        }
    }

    public void adicionarEmpresa() {
        if (fisica.getId() != -1 && pessoaEmpresa.getJuridica().getId() != -1) {
            if (pessoaEmpresa.getAdmissao().isEmpty()) {
                GenericaMensagem.warn("Atenção", "Data de Admissão não pode estar vazia!");
                return;
            }

            pessoaEmpresa.setFisica(fisica);
            pessoaEmpresa.setAvisoTrabalhado(false);
            pessoaEmpresa.setPrincipal(false);

            if (profissao.getProfissao() == null || profissao.getProfissao().isEmpty()) {
                pessoaEmpresa.setFuncao(null);
            } else {
                pessoaEmpresa.setFuncao(profissao);
            }

            Dao di = new Dao();

            di.openTransaction();

            if (pessoaEmpresa.getId() == -1) {
                if (!di.save(pessoaEmpresa)) {
                    di.rollback();
                    GenericaMensagem.error("ERRO", "Não foi possível adicionar Empresa!");
                    return;
                }
            } else {
                if (!di.update(pessoaEmpresa)) {
                    di.rollback();
                    GenericaMensagem.error("ERRO", "Não foi possível adicionar Empresa!");
                    return;
                }
            }
            di.commit();

            //RequestContext.getCurrentInstance().update("form_pessoa_fisica:i_panel_pessoa_fisica");
            GenericaMensagem.info("Sucesso", "Empresa Adicionada!");
            pessoaEmpresa = new PessoaEmpresa();
            profissao = new Profissao();
            renderJuridicaPesquisa = false;
        }
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listaPessoa.clear();
        listaPessoaFisica.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listaPessoa.clear();
        listaPessoaFisica.clear();
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
            for (Fisica result21 : result2) {
                listaPessoa.add(new DataObject(result21, (PessoaEmpresa) dbEmp.pesquisaPessoaEmpresaPorFisica(result21.getId())));
            }
        }
        return listaPessoa;
    }

    public void setListaPessoa(List<DataObject> listaPessoa) {
        this.listaPessoa = listaPessoa;
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
            compara = (pessoaEnde1.getEndereco().getId() == pessoaEnde2.getEndereco().getId()
                    && pessoaEnde1.getNumero().equals(pessoaEnde2.getNumero())
                    && pessoaEnde1.getComplemento().equals(pessoaEnde2.getComplemento()));
        } else {
            compara = false;
        }
        return compara;
    }

    public List<SelectItem> getListaPaises() {
        if (listaPaises.isEmpty()) {
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
            for (int i = 0; i < lista.length; i++) {
                listaPaises.add(new SelectItem(i, lista[i], String.valueOf(i)));
            }
//            if (fisica.getId() != -1) {
//                for (int i = 0; i < listaPaises.size(); i++) {
//                    if ((listaPaises.get(i).getLabel().toUpperCase()).equals(fisica.getNacionalidade().toUpperCase())) {
//                        idPais = i;
//                    }
//                }
//            }
        }
        return listaPaises;
    }

    public void setListaPaises(List<SelectItem> listaPaises) {
        this.listaPaises = listaPaises;
    }

    public List<SelectItem> getListaProfissoes() {
        if (listaProfissoes.isEmpty()) {
            ProfissaoDB db = new ProfissaoDBToplink();
            List<Profissao> lista = (List<Profissao>) db.pesquisaTodos();
            for (int i = 0; i < lista.size(); i++) {
                listaProfissoes.add(new SelectItem(i, lista.get(i).getProfissao(), "" + lista.get(i).getId()));
            }
        }
        return listaProfissoes;
    }

    public String getCidadeNaturalidade() {
        String nat;
        if (idPais != 11) {
            readyOnlineNaturalidade = false;
            disabledNaturalidade = true;
            nat = "";
            fisica.setNaturalidade(nat);
            return nat;
        } else {
            readyOnlineNaturalidade = true;
            disabledNaturalidade = false;
        }
        Cidade cidade;
        if (GenericaSessao.exists("cidadePesquisa")) {
            cidade = (Cidade) GenericaSessao.getObject("cidadePesquisa", true);
            nat = cidade.getCidade();
            nat = nat + " - " + cidade.getUf();
            fisica.setNaturalidade(nat);
        }

        if (!fisica.getNaturalidade().isEmpty()) {
            nat = fisica.getNaturalidade();
            return nat;
        }

        if (fisica.getId() == -1 || fisica.getNaturalidade().isEmpty()) {
            PessoaEnderecoDB dbPes = new PessoaEnderecoDBToplink();
            Dao dao = new Dao();
            Filial fili = (Filial) dao.find(new Filial(), 1);
            if (fili != null) {
                Pessoa pes = fili.getMatriz().getPessoa();
                if (pes.getId() != -1) {
                    cidade = ((PessoaEndereco) dbPes.pesquisaEndPorPessoa(pes.getId()).get(0)).getEndereco().getCidade();
                    nat = cidade.getCidade();
                    nat = nat + " - " + cidade.getUf();
                    // nat = nat + " <<<";
                    if (idPais != 11) {
                        fisica.setNaturalidade("");
                    } else {
                        fisica.setNaturalidade(nat);
                    }
                    return nat;
                }
            }
        }
        return null;
    }

    public String getPessoaImagem() {
        FacesContext context = FacesContext.getCurrentInstance();
        File files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/"));
        File fExiste = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg"));
        File listFile[] = files.listFiles();
        String nome;
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
                        caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg");
                        File fl = new File(caminho);
                        fl.delete();
                        break;
                    }
                } else {
                    fotoTemp = false;
                    break;
                }
            } catch (NumberFormatException e) {
            }
        }
        return nome + ".jpg";
    }

    public void salvarImagem() {
        if (!Diretorio.criar("Imagens/Fotos/")) {
            return;
        }
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/");
        boolean error = false;
        if (!fotoTempPerfil.equals("")) {
            File des = new File(arquivo + "/" + fisica.getPessoa().getId() + ".png");
            if (des.exists()) {
                des.delete();
            }
            File src = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(fotoTempPerfil));
            boolean rename = src.renameTo(des);
            fotoPerfil = "/Cliente/" + getCliente() + "/Imagens/Fotos/" + fisica.getPessoa().getId() + ".png";
            fotoTempPerfil = "";

            if (!rename) {
                error = true;
            }
        }
        if (!error) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/foto/" + getUsuario().getId()));
            boolean delete = f.delete();
        }
    }

//    public void salvarImagem() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        if (fisica.getId() != -1) {
//            String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + String.valueOf(fisica.getPessoa().getId()) + ".jpg");
//            String caminho2 = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/fotoTemp.jpg");
//            try {
//                File fl = new File(caminho);
//                File item = new File(caminho2);
//                FileInputStream in = new FileInputStream(item);
//                FileOutputStream out = new FileOutputStream(fl.getPath());
//
//                byte[] buf = new byte[(int) item.length()];
//                int count;
//                while ((count = in.read(buf)) >= 0) {
//                    out.write(buf, 0, count);
//                }
//                in.close();
//                out.flush();
//                out.close();
//            } catch (IOException e) {
//                System.out.println(e);
//            }
//        }
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
                Dao dao = new Dao();
                fisica.setDataFoto("");
                dao.update(fisica, true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void excluirImagem() {
        try {
            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + String.valueOf(fisica.getPessoa().getId()) + ".png"));
            if (fl.exists()) {
                fl.delete();
            }
        } catch (Exception e) {
            // System.out.println(e);
        }
    }

    public String excluirEmpresaAnterior(PessoaEmpresa pe) {
        HomologacaoDB dbAge = new HomologacaoDBToplink();
        List<Agendamento> agendas = dbAge.pesquisaAgendamentoPorPessoaEmpresa(pe.getId());
        Dao dao = new Dao();
        for (Agendamento agenda : agendas) {
            if (!dao.delete(agenda, true)) {
                ErrorCodeDao errorCodeDao = dao.deleteErrorCode(agenda);
                GenericaMensagem.warn("Erro", "Não foi possível remover este agendamento!");
                //GenericaMensagem.error("Sistema", errorCodeDao.getSimpleMessage());
                return null;
            }
        }
        if (dao.delete(pe, true)) {
            GenericaMensagem.info("Sucesso", "Empresa removida com sucesso");
        } else {
            ErrorCodeDao errorCodeDao = dao.deleteErrorCode(pe);
            GenericaMensagem.warn("Erro", "Não foi possível remover esta empresa!");
            //GenericaMensagem.error("Sistema", errorCodeDao.getSimpleMessage());
        }
        listaPessoaEmpresa.clear();
        return null;
    }

    public void removerJuridicaPesquisada() {
        if (pessoaEmpresa.getId() != -1) {
            Dao dao = new Dao();
            if (!dao.delete(pessoaEmpresa, true)) {
                mensagem = "Empresa com Agendamento não pode ser excluída!";
                PF.update("form_pessoa_fisica:i_panel_mensagem");
                PF.openDialog("dlg_painel_mensagem");
                return;
            }
        }
        GenericaSessao.remove("juridicaPesquisa");
        pessoaEmpresa = new PessoaEmpresa();
        profissao = new Profissao();
        renderJuridicaPesquisa = false;

        PessoaEmpresaDB db = new PessoaEmpresaDBToplink();
        List<PessoaEmpresa> lpe = db.listaPessoaEmpresaPorFisicaDemissao(fisica.getId());

        if (!lpe.isEmpty()) {
            lpe.get(0).setPrincipal(true);

            db.update(lpe.get(0));

            pessoaEmpresa = lpe.get(0);
            renderJuridicaPesquisa = true;
        }
        RequestContext.getCurrentInstance().update("form_pessoa_fisica:i_panel_pessoa_fisica");
    }

    public void alterarEmpresaAtual(PessoaEmpresa pe) {
        Dao di = new Dao();
        di.openTransaction();

        if (!pe.getDemissao().isEmpty()) {
            GenericaMensagem.error("Atenção", "Pessoa demissionada não pode ser Reativa!");
            return;
        }

        if (pessoaEmpresa.getId() == -1) {
            pe.setPrincipal(true);
            if (!di.update(pe)) {
                di.rollback();
                return;
            }
            pessoaEmpresa = pe;
        } else {
            pessoaEmpresa.setPrincipal(false);
            pe.setPrincipal(true);

            if (!di.update(pessoaEmpresa) || !di.update(pe)) {
                di.rollback();
                return;
            }
            pessoaEmpresa = pe;
        }

        di.commit();
    }

    public String associarFisica() {
        boolean reativar = false;
        Pessoa p = fisica.getPessoa();
        if (tipoCadastro == -1) {
            GenericaMensagem.warn("Validação", "Cadastre uma pessoa fisica para associar!");
            return "pessoaFisica";
        } else if (tipoCadastro == 1) {
            if (socios.getId() == -1) {
                if (fisica.getPessoa().getDocumento().isEmpty() || fisica.getPessoa().getDocumento().equals("0")) {
                    GenericaMensagem.warn("Erro", "Para se associar é necessário ter número de documento (CPF) no cadastro!");
                    return null;
                }
            }
            reativar = socios.getServicoPessoa().isAtivo();
        } else if (tipoCadastro == 2) {
            reativar = socios.getServicoPessoa().isAtivo();
        } else if (tipoCadastro == 3) {
            p = socios.getMatriculaSocios().getTitular();
            reativar = socios.getServicoPessoa().isAtivo();
        } else if (tipoCadastro == 4) {
            reativar = socios.getServicoPessoa().isAtivo();
        } else if (tipoCadastro == 5) {
            reativar = socios.getServicoPessoa().isAtivo();
        }
        if (pessoaEmpresa.getId() != -1) {
            GenericaSessao.put("pessoaEmpresaPesquisa", pessoaEmpresa);
        }
        String retorno = ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).socios();
        if (socios.getId() == -1) {
            reativar = false;
        }
        GenericaSessao.put("sociosBean", new SociosBean());
        SociosBean sb = (SociosBean) GenericaSessao.getObject("sociosBean");
        sb.loadSocio(p, reativar);
        return retorno;
    }

    public String associarFisica(Pessoa _pessoa) {
//        GenericaSessao.put("fisicaPesquisa", (new FisicaDBToplink()).pesquisaFisicaPorPessoa(_pessoa.getId()));
//        GenericaSessao.put("pessoaEmpresaPesquisa", (new PessoaEmpresaDBToplink()).pesquisaPessoaEmpresaPorPessoa(_pessoa.getId()));
//        GenericaSessao.put("reativarSocio", true);
//        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).socios();

        String retorno = ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).socios();
        GenericaSessao.put("pessoaEmpresaPesquisa", (new PessoaEmpresaDBToplink()).pesquisaPessoaEmpresaPorPessoa(_pessoa.getId()));
        GenericaSessao.put("sociosBean", new SociosBean());

        SociosBean sb = (SociosBean) GenericaSessao.getObject("sociosBean");
        sb.loadSocio(fisica.getPessoa(), true);

        return retorno;
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

    public PessoaEndereco getPessoaEndereco() {
        return pessoaEndereco;
    }
    
    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }
    
    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }


    public String getIndicaTab() {
        return indicaTab;
    }
    
    public void setIndicaTab(String indicaTab) {
        this.indicaTab = indicaTab;
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
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
        if (GenericaSessao.exists("juridicaPesquisa")) {
            pessoaEmpresa.setJuridica((Juridica) GenericaSessao.getObject("juridicaPesquisa"));
            GenericaSessao.remove("juridicaPesquisa");
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
        if (GenericaSessao.exists("socioPesquisa")) {
            socios = (Socios) GenericaSessao.getObject("socioPesquisa", true);
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
        } else if (socios.getId() != -1 && (socios.getMatriculaSocios().getDtInativo() != null || !socios.getServicoPessoa().isAtivo())) {
            lblSocio = "ASSOCIAR";
        } else {
            lblSocio = "VER CADASTRO";
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
            //SociosDB db = new SociosDBToplink();
            //Socios soc = db.pesquisaSocioDoDependente(socios.getServicoPessoa().getPessoa());
            if (socios.getMatriculaSocios().getTitular().getId() == fisica.getPessoa().getId()) {
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
            fileContent = new Scanner(file.getInputStream()).useDelimiter("\\A").next();
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
        Part files = (Part) value;
        if (files.getSize() > 1024) {
            msgs.add(new FacesMessage("file too big"));
        }
        if (!"text/plain".equals(files.getContentType())) {
            msgs.add(new FacesMessage("not a text file"));
        }
        if (!msgs.isEmpty()) {
            throw new ValidatorException(msgs);
        }
    }

    public void mascaraPesquisaFisica() {
        descPesquisa = "";
        mask = Mask.getMascaraPesquisa(porPesquisa, true);
    }

    public List<Fisica> getListaPessoaFisica() {
        if (listaPessoaFisica.isEmpty()) {
            FisicaDB db = new FisicaDBToplink();
            switch (pesquisaPor) {
                case "socioativo":
                    listaPessoaFisica = db.pesquisaPessoaSocio(descPesquisa, porPesquisa, comoPesquisa);
                    break;
                case "pessoa":
                    listaPessoaFisica = db.pesquisaPessoa(descPesquisa, porPesquisa, comoPesquisa);
                    break;
                case "socioinativo":
                    listaPessoaFisica = db.pesquisaPessoaSocioInativo(descPesquisa, porPesquisa, comoPesquisa);
                    break;
            }
        }
        return listaPessoaFisica;
    }

    public void setListaPessoaFisica(List<Fisica> listaPessoaFisica) {
        this.listaPessoaFisica = listaPessoaFisica;
    }

    public String pessoaEmpresaString(Fisica f) {
        String pessoaEmpresaString = "";
        PessoaEmpresaDB pessoaEmpresaDB = new PessoaEmpresaDBToplink();
        PessoaEmpresa pe = (PessoaEmpresa) pessoaEmpresaDB.pesquisaPessoaEmpresaPorFisica(f.getId());
        if (pe != null) {
            if (pe.getId() != -1) {
                pessoaEmpresaString = pe.getJuridica().getPessoa().getNome();
            }
        }
        return (pessoaEmpresaString.isEmpty()) ? "SEM EMPRESA" : pessoaEmpresaString;
    }

    public void novoEndereco(TabChangeEvent event) {
        indexNovoEndereco = ((AccordionPanel) event.getComponent()).getActiveIndex();
    }

    public void accordion(TabChangeEvent event) {
        indexPessoaFisica = ((TabView) event.getComponent()).getActiveIndex();
    }

    public String getIndexNovoEndereco() {
        return indexNovoEndereco;
    }

    public void setIndexNovoEndereco(String indexNovoEndereco) {
        this.indexNovoEndereco = indexNovoEndereco;
    }

    public int getIndexPessoaFisica() {
        return indexPessoaFisica;
    }

    public void setIndexPessoaFisica(int indexPessoaFisica) {
        this.indexPessoaFisica = indexPessoaFisica;
    }

    public String getFotoPerfil() {
        if (fisica.getId() != -1) {
            // TEM FOTO MAS NO BANCO ESTA FALSE == ALTERA PARA TRUE NO BANCO
            if (!fotoPerfil.isEmpty() && fisica.getDataFoto().isEmpty()) {
                Dao dao = new Dao();
                fisica.setDataFoto(DataHoje.data());
                dao.update(fisica, true);
                return fotoPerfil;
            }

            // TEM FOTO E NO BANCO ESTA TRUE == PERMANECE DO JEITO QUE ESTA
            // NÃO TEM FOTO E NO BANCO ESTA FALSE = PERMANECE DO JEITO QUE ESTA
            // NÃO TEM FOTO E NO BANCO ESTA TRUE = ALTERA PARA FALSE NO BANCO
            if (fotoPerfil.isEmpty() && !fisica.getDataFoto().isEmpty()) {
                Dao dao = new Dao();
                fisica.setDataFoto("");
                dao.update(fisica, true);
            }
        }
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
//
//    public String getFotoArquivo() {
//        return fotoArquivo;
//    }
//
//    public void setFotoArquivo(String fotoArquivo) {
//        this.fotoArquivo = fotoArquivo;
//    }

    public String getFotoTempPerfil() {
        return fotoTempPerfil;
    }

    public void setFotoTempPerfil(String fotoTempPerfil) {
        this.fotoTempPerfil = fotoTempPerfil;
    }

//    public String getFotoTempArquivo() {
//        return fotoTempArquivo;
//    }
//
//    public void setFotoTempArquivo(String fotoTempArquivo) {
//        this.fotoTempArquivo = fotoTempArquivo;
//    }
    public String getCliente() {
        if (cliente.equals("")) {
            if (GenericaSessao.exists("sessaoCliente")) {
                return GenericaSessao.getString("sessaoCliente");
            }
        }
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        if (GenericaSessao.exists("sessaoUsuario")) {
            usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void loadingImage() throws InterruptedException {
        Thread.sleep(5000);

        PF.closeDialog("dlg_loading_image");
    }

    public void capturar(CaptureEvent captureEvent) {
        String fotoTempCaminho = "foto/" + getUsuario().getId();
        if (PhotoCam.oncapture(captureEvent, "perfil", fotoTempCaminho, true)) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png"));
            if (f.exists()) {
                fotoTempPerfil = "/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png";
                fotoPerfil = "";
            } else {
                fotoTempPerfil = "";
            }
        }
//        RequestContext.getCurrentInstance().update(":form_pessoa_fisica");
//        RequestContext.getCurrentInstance().execute("dgl_captura.hide();");
    }

    public void upload(FileUploadEvent event) {
        try {
            String fotoTempCaminho = "foto/" + getUsuario().getId();
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png"));
            if (f.exists()) {
                boolean delete = f.delete();
            } else {
                fotoTempPerfil = "";
            }
            // Diretorio.criar("temp/foto/" + getUsuario().getId(), true);
            ConfiguracaoUpload cu = new ConfiguracaoUpload();
            cu.setArquivo(event.getFile().getFileName());
            cu.setDiretorio("temp/foto/" + getUsuario().getId());
            cu.setArquivo("perfil.png");
            cu.setSubstituir(true);
            cu.setRenomear("perfil.png");
            cu.setEvent(event);
            if (Upload.enviar(cu, true)) {
                fotoTempPerfil = "/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png";
                fotoPerfil = "";
            } else {
                fotoTempPerfil = "";
                fotoPerfil = "";
            }
            loadingImage();
            //RequestContext.getCurrentInstance().update(":form_pessoa_fisica");
        } catch (InterruptedException ex) {
            Logger.getLogger(FisicaBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void apagarImagem() {
        boolean sucesso = false;
        if (!fotoTempPerfil.equals("")) {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/foto/" + getUsuario().getId() + "/perfil.png"));
            sucesso = f.delete();
        } else {
            if (fisica.getId() != -1) {
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/" + fisica.getPessoa().getId() + ".png"));
                sucesso = f.delete();
                if(!sucesso) {
                    f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/" + fisica.getPessoa().getId() + ".jpg"));
                    sucesso = f.delete();                    
                }
            }
        }
        if (sucesso) {
            fotoTempPerfil = "";
            fotoPerfil = "";
            RequestContext.getCurrentInstance().update(":form_pessoa_fisica");
        }
    }

    public void validaPIS() {
        GenericaMensagem.warn("Validação", "Número do PIS inválido!");
        ValidaDocumentos.isValidoPIS(fisica.getPis());
    }

    public boolean isReadyOnlineNaturalidade() {
        return readyOnlineNaturalidade;
    }

    public void setReadyOnlineNaturalidade(boolean readyOnlineNaturalidade) {
        this.readyOnlineNaturalidade = readyOnlineNaturalidade;
    }

    public boolean isDisabledNaturalidade() {
        return disabledNaturalidade;
    }

    public void setDisabledNaturalidade(boolean disabledNaturalidad) {
        this.disabledNaturalidade = disabledNaturalidad;
    }

    public String[] getImagensTipo() {
        return imagensTipo;
    }

    public void setImagensTipo(String[] imagensTipo) {
        this.imagensTipo = imagensTipo;
    }

    public List<Socios> getListaSocioInativo() {
        if (listaSocioInativo.isEmpty() && fisica.getId() != -1) {
            //listaSocioInativo = new SociosDBToplink().listaSocioTitularInativoPorPessoa(fisica.getPessoa().getId()); 
            listaSocioInativo = new SociosDBToplink().pesquisaSocioPorPessoaInativo(fisica.getPessoa().getId());
        }
        return listaSocioInativo;
    }

    public void setListaSocioInativo(List<Socios> listaSocioInativo) {
        this.listaSocioInativo = listaSocioInativo;
    }

    public Endereco getEnderecox() {
        if (GenericaSessao.getObject("enderecoPesquisa") != null) {
            enderecox = (Endereco) GenericaSessao.getObject("enderecoPesquisa", true);

            enderecoCompleto = enderecox.getLogradouro().getDescricao() + " "
                    + enderecox.getDescricaoEndereco().getDescricao() + ", "
                    + enderecox.getCidade().getCidade() + " - "
                    + enderecox.getCidade().getUf();

            if (visibleEditarEndereco) {
                pessoaEndereco.setEndereco(enderecox);
            }
        }
        return enderecox;
    }

    public void setEnderecox(Endereco enderecox) {
        this.enderecox = enderecox;
    }

    public List<PessoaEndereco> getListaPessoaEndereco() {
        if (fisica.getId() != -1 && listaPessoaEndereco.isEmpty()) {
            PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
            listaPessoaEndereco = db.pesquisaEndPorPessoa(fisica.getPessoa().getId());
        }
        return listaPessoaEndereco;
    }

    public void setListaPessoaEndereco(List<PessoaEndereco> listaPessoaEndereco) {
        this.listaPessoaEndereco = listaPessoaEndereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public boolean isVisibleEditarEndereco() {
        return visibleEditarEndereco;
    }

    public void setVisibleEditarEndereco(boolean visibleEditarEndereco) {
        this.visibleEditarEndereco = visibleEditarEndereco;
    }

    public List<ServicoPessoa> getListaServicoPessoa() {
        if (fisica.getId() != -1 && listaServicoPessoa.isEmpty()) {
            FisicaDB db = new FisicaDBToplink();
            listaServicoPessoa = db.listaServicoPessoa(fisica.getPessoa().getId(), chkDependente);
        }
        return listaServicoPessoa;
    }

    public void setListaServicoPessoa(List<ServicoPessoa> listaServicoPessoa) {
        this.listaServicoPessoa = listaServicoPessoa;
    }

    public boolean isChkDependente() {
        return chkDependente;
    }

    public void setChkDependente(boolean chkDependente) {
        this.chkDependente = chkDependente;
    }

    public String getStrEndereco() {
        if (!listaPessoaEndereco.isEmpty()) {
            strEndereco
                    = listaPessoaEndereco.get(1).getEndereco().getLogradouro().getDescricao() + " "
                    + listaPessoaEndereco.get(1).getEndereco().getDescricaoEndereco().getDescricao() + " " + listaPessoaEndereco.get(1).getNumero() + ", "
                    + listaPessoaEndereco.get(1).getEndereco().getBairro().getDescricao() + ", " + listaPessoaEndereco.get(1).getComplemento() + " "
                    + listaPessoaEndereco.get(1).getEndereco().getCidade().getCidade() + "  -  "
                    + listaPessoaEndereco.get(1).getEndereco().getCidade().getUf();
        }
        return strEndereco;
    }

    public void setStrEndereco(String strEndereco) {
        this.strEndereco = strEndereco;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void existePessoaOposicaoPorPessoa() {
        if (!fisica.getPessoa().getDocumento().isEmpty()) {
            OposicaoDBToplink odbt = new OposicaoDBToplink();
            pessoaOposicao = odbt.existPessoaDocumentoPeriodo(fisica.getPessoa().getDocumento());
        }
    }

    public boolean existePessoaOposicaoPorDocumento(String documento) {
        if (!documento.isEmpty()) {
            OposicaoDBToplink odbt = new OposicaoDBToplink();
            return odbt.existPessoaDocumentoPeriodo(documento);
        }
        return false;
    }

    public boolean isPessoaOposicao() {
        return pessoaOposicao;
    }

    public void setPessoaOposicao(boolean pessoaOposicao) {
        this.pessoaOposicao = pessoaOposicao;
    }

    public Integer getTipoCadastro() {
        if (fisica.getId() == -1) {
            // CADASTRO NOVO
            tipoCadastro = -1;
        } else if (socios.getId() == -1) {
            // CADASTRO PARA ALTERAR
            tipoCadastro = 1;
        } else if (socios.getServicoPessoa().isAtivo()) {
            if (fisica.getPessoa().getId() == socios.getMatriculaSocios().getTitular().getId()) {
                // SÓCIO TITULAR
                tipoCadastro = 2;
            } else {
                // SÓCIO DEPENDENTE
                tipoCadastro = 3;
            }
        } else if (!socios.getServicoPessoa().isAtivo() && listaSocioInativo.isEmpty()) {
            // SÓCIO INÁTIVO
            tipoCadastro = 4;
        } else if (!socios.getServicoPessoa().isAtivo() && !listaSocioInativo.isEmpty()) {
            // SÓCIO INÁTIVO
            tipoCadastro = 5;
        }
        return tipoCadastro;
    }

    public void setTipoCadastro(Integer tipoCadastro) {
        this.tipoCadastro = tipoCadastro;
    }
}
