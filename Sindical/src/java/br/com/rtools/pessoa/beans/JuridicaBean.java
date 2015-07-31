package br.com.rtools.pessoa.beans;

import br.com.rtools.arrecadacao.dao.OposicaoDao;
import br.com.rtools.arrecadacao.*;
import br.com.rtools.arrecadacao.beans.OposicaoBean;
import br.com.rtools.arrecadacao.db.*;
import br.com.rtools.associativo.dao.SociosDao;
import br.com.rtools.associativo.lista.ListaSociosEmpresa;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.dao.MalaDiretaDao;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.sistema.ConfiguracaoCnpj;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
//import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;
// import knu.ReceitaCNPJ;
// import knu.knu;

@ManagedBean
@SessionScoped
public class JuridicaBean implements Serializable {

    private Juridica juridica = new Juridica();
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private Filial filial = new Filial();
    private Filial filialSwap = new Filial();
    private ContribuintesInativos contribuintesInativos = new ContribuintesInativos();
    private Endereco endereco = new Endereco();
    private GrupoCidade gruCids = new GrupoCidade();
    private Convencao convencao = new Convencao();
    private CnaeConvencao cnaeConvencao = new CnaeConvencao();
    private EnvioEmails envioEmails = new EnvioEmails();
    private int indicaTab = 0;
    private String enderecoCompleto;
    private String enderecoDeletado = null;
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "I";
    private String descPesquisaCnae = "";
    private String porPesquisaCnae = "cnae";
    private String comoPesquisaCnae = "";
    private String filialMatriz = "m";
    private String msgConfirma;
    private String msgDocumento = "";
    private String maskCnae = "cnae";
    private String mask;
    private String log;
    private String desc;
    private String cid;
    private String uf;
    private String strGrupoCidade = "";
    private String strCnaeConvencao = "";
    private String cnaeContribuinte = " sem cnae! ";
    private String enderecoCobranca = "";
    private String strSimpleEndereco = "";
    private boolean renNovoEndereco = false;
    private boolean renEndereco = false;
    private String renChkEndereco = "false";
    private String colorContri = "red";
    private String numDocumento = "";
    private String strArquivo = "";
    private String nomeContabilidade = "";
    private String nomePesquisaContabilidade = "";
    private int idTipoDocumento = 1;
    private int idPorte = 0;
    private int idMotivoInativacao = 0;
    private int idIndex = -1;
    private int idIndexCnae = -1;
    private int idIndexEndereco = -1;
    private int idIndexInativacao = -1;
    private int idIndexContabilidade = -1;
    private int idIndexPertencente = -1;
    private boolean marcar;
    private boolean alterarEnd = false;
    private boolean endComercial = false;
    private boolean habServContabil = true;
    private boolean carregaEnvios = false;
    private boolean renderAtivoInativo = false;
    private List<Oposicao> listaOposicao = new ArrayList();
    private List<ListaSociosEmpresa> listSocios = new ArrayList<>();
    private boolean somenteAtivas = false;
    private boolean somenteContabilidades = false;
//    private boolean chkEndContabilidade = true;
    private List listaEnd = new ArrayList();
    private List listEn = new ArrayList();
    private List<Cnae> listaCnae = new ArrayList();
    private List<Juridica> listaJuridica = new ArrayList();
    private List<Juridica> listaContabilidade = new ArrayList();
    private List<DataObject> listaEmpresasPertencentes = new ArrayList();
    private List<SelectItem> listaTipoDocumento = new ArrayList();
    private List<SelectItem> listaPorte = new ArrayList();
    private List<ContribuintesInativos> listaContribuintesInativos = new ArrayList();
    private List<RepisMovimento> listRepisMovimento = new ArrayList();
    private List<SelectItem> listaMotivoInativacao = new ArrayList();
    private String atualiza = "";
    private String tipoFiltro = "todas";
    private JuridicaReceita juridicaReceita = new JuridicaReceita();
    // [0] Contribuintes
    // [1] Contabilidades
    // [2] Todas
    private Boolean[] disabled;
    private ConfiguracaoCnpj configuracaoCnpj;

    // MALA DIRETA
    private Boolean habilitaMalaDireta = false;
    private String idMalaDiretaGrupo = null;
    private List<MalaDireta> listMalaDireta = new ArrayList();
    private List<SelectItem> listMalaDiretaGrupo = new ArrayList();

    @PostConstruct
    public void init() {
        disabled = new Boolean[3];
        disabled[0] = false;
        disabled[1] = false;
        disabled[2] = false;
        configuracaoCnpj = (ConfiguracaoCnpj) new Dao().find(new ConfiguracaoCnpj(), 1);
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("juridicaBean");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("todosecontribuintes");
        GenericaSessao.remove("contribuintes");
        GenericaSessao.remove("escritorios");
        GenericaSessao.remove("pessoaBean");
    }

    public void pesquisaCnpjXML() {
        if (configuracaoCnpj == null || configuracaoCnpj.getLocal()) {
            if (juridica.getId() != -1) {
                return;
            }
            if (juridica.getPessoa().getDocumento().isEmpty()) {
                return;
            }

            String documento = AnaliseString.extrairNumeros(juridica.getPessoa().getDocumento());

            if (!validaTipoDocumento(2, documento)) {
                msgDocumento = "Documento inválido!";
                GenericaMensagem.warn("Atenção", "Documento Inválido!");
                return;
            }
            JuridicaDB dbj = new JuridicaDBToplink();
            List listDocumento = dbj.pesquisaJuridicaPorDoc(juridica.getPessoa().getDocumento());
            for (int i = 0; i < listDocumento.size(); i++) {
                if (!listDocumento.isEmpty()) {
                    GenericaMensagem.warn("Atenção", "Empresa já esta cadastrada no Sistema!");
                    return;
                }
            }

            PessoaDB db = new PessoaDBToplink();

            juridicaReceita = db.pesquisaJuridicaReceita(documento);
            if (juridicaReceita.getPessoa() != null && juridicaReceita.getPessoa().getId() != -1) {
                GenericaMensagem.warn("Atenção", "Pessoa já cadastrada no Sistema!");
                return;
            }

            if (juridicaReceita.getId() == -1) {
                Dao dao = new Dao();
                URL url;
                try {
                    if (configuracaoCnpj == null) {
                        url = new URL("https://wooki.com.br/api/v1/cnpj/receitafederal?numero=" + documento + "&dias=" + configuracaoCnpj.getDias() + "&usuario=rogerio@rtools.com.br&senha=989899");
                    } else {
                        if (configuracaoCnpj.getEmail().isEmpty() || configuracaoCnpj.getSenha().isEmpty()) {
                            url = new URL("https://wooki.com.br/api/v1/cnpj/receitafederal?numero=" + documento + "&dias=" + configuracaoCnpj.getDias() + "&usuario=rogerio@rtools.com.br&senha=989899");
                        } else {
                            url = new URL("https://wooki.com.br/api/v1/cnpj/receitafederal?numero=" + documento + "&dias=" + configuracaoCnpj.getDias() + "&usuario=" + configuracaoCnpj.getEmail() + "&senha=" + configuracaoCnpj.getSenha());
                        }
                    }
                    //URL url = new URL("https://wooki.com.br/api/v1/cnpj/receitafederal?numero=00000000000191&usuario=teste@wooki.com.br&senha=teste");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                        String str = in.readLine();
                        JSONObject obj = new JSONObject(str);
                        int status = obj.getInt("status");
                        String error = obj.getString("msg");

                        if (status == 6) {
                            GenericaMensagem.warn("Atenção", "Limite de acessos excedido!");
                            return;
                        }

                        if (status == 1) {
                            GenericaMensagem.info("Atenção", "Atualizando esse CNPJ na receita, pesquise novamente em 30 segundos!");
                            return;
                        }

                        if (status != 0) {
                            GenericaMensagem.error("Erro", error);
                            return;
                        }

                        juridicaReceita.setNome(obj.getString("nome_empresarial"));
                        juridicaReceita.setFantasia(obj.getString("titulo_estabelecimento"));
                        juridicaReceita.setDocumento(documento);
                        juridicaReceita.setCep(AnaliseString.mascaraCep(obj.getString("cep")));
                        juridicaReceita.setDescricaoEndereco(obj.getString("logradouro"));
                        juridicaReceita.setBairro(obj.getString("bairro"));
                        juridicaReceita.setComplemento(obj.getString("complemento"));
                        juridicaReceita.setNumero(obj.getString("numero"));
                        juridicaReceita.setCnae(obj.getString("atividade_principal"));
                        juridicaReceita.setPessoa(null);
                        juridicaReceita.setStatus(obj.getString("situacao_cadastral"));
                        juridicaReceita.setDtAbertura(DataHoje.converte(obj.getString("data_abertura")));
                        juridicaReceita.setCnaeSegundario(obj.getString("atividades_secundarias"));
                        juridicaReceita.setCidade(obj.getString("municipio"));
                        juridicaReceita.setUf(obj.getString("uf"));
                        juridicaReceita.setEmail(obj.getString("email_rf"));
                        juridicaReceita.setTelefone(obj.getString("telefone_rf"));

                        dao.openTransaction();

                        if (!dao.save(juridicaReceita)) {
                            GenericaMensagem.warn("Erro", "Erro ao Salvar pesquisa!");
                            dao.rollback();
                            return;
                        }

                        dao.commit();
                        in.close();
                    }
                } catch (IOException | JSONException e) {

                }
            }

            juridica.getPessoa().setNome(juridicaReceita.getNome().toUpperCase());
            juridica.setFantasia(juridicaReceita.getFantasia().toUpperCase());

            String emails[] = (juridicaReceita.getEmail() == null) ? "".split("") : juridicaReceita.getEmail().toLowerCase().split(" ");
            String telefones[] = (juridicaReceita.getTelefone() == null) ? "".split("") : juridicaReceita.getTelefone().split(" / ");

            if (!emails[0].isEmpty()) {
                juridica.setContabilidade(dbj.pesquisaContabilidadePorEmail(emails[0]));
                if (juridica.getContabilidade() != null) {
                    nomeContabilidade = juridica.getContabilidade().getPessoa().getNome();
                }
            }

            switch (emails.length) {
                case 1:
                    juridica.getPessoa().setEmail1(emails[0]);
                    break;
                case 2:
                    juridica.getPessoa().setEmail1(emails[0]);
                    juridica.getPessoa().setEmail2(emails[1]);
                    break;
                case 3:
                    juridica.getPessoa().setEmail1(emails[0]);
                    juridica.getPessoa().setEmail2(emails[1]);
                    juridica.getPessoa().setEmail3(emails[2]);
                    break;
            }

            switch (telefones.length) {
                case 1:
                    juridica.getPessoa().setTelefone1(telefones[0]);
                    break;
                case 2:
                    juridica.getPessoa().setTelefone1(telefones[0]);
                    juridica.getPessoa().setTelefone2(telefones[1]);
                    break;
                case 3:
                    juridica.getPessoa().setTelefone1(telefones[0]);
                    juridica.getPessoa().setTelefone2(telefones[1]);
                    juridica.getPessoa().setTelefone3(telefones[2]);
                    break;
            }

            String result[] = juridicaReceita.getCnae().split(" ");
            CnaeDB dbc = new CnaeDBToplink();
            String cnaex = result[result.length - 1].replace("(", "").replace(")", "");
            List<Cnae> listac = dbc.pesquisaCnae(cnaex, "cnae", "I");

            if (listac.isEmpty()) {
                GenericaMensagem.warn("Erro", "Erro ao pesquisar CNAE");
                return;
            }
            retornaCnaeReceita(listac.get(0));

            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();

            String cep = juridicaReceita.getCep();
            cep = cep.replace(".", "").replace("-", "");

            String descricao[] = AnaliseString.removerAcentos(juridicaReceita.getDescricaoEndereco()).split(" ");
            String bairros[] = AnaliseString.removerAcentos(juridicaReceita.getBairro()).split(" ");

            endereco = dbe.enderecoReceita(cep, descricao, bairros);

            if (endereco != null) {
                TipoEnderecoDB dbt = new TipoEnderecoDBToplink();
                List tiposE = dbt.listaTipoEnderecoParaJuridica();
                for (Object tiposE1 : tiposE) {
                    pessoaEndereco.setEndereco(endereco);
                    pessoaEndereco.setTipoEndereco((TipoEndereco) tiposE1);
                    pessoaEndereco.setPessoa(juridica.getPessoa());
                    pessoaEndereco.setNumero(juridicaReceita.getNumero());
                    pessoaEndereco.setComplemento(juridicaReceita.getComplemento());
                    listaEnd.add(pessoaEndereco);
                    pessoaEndereco = new PessoaEndereco();
                }
            } else {
                String msg = "Endereço não encontrado no Sistema - CEP: " + juridicaReceita.getCep() + " DESC: " + juridicaReceita.getDescricaoEndereco() + " BAIRRO: " + juridicaReceita.getBairro();
                GenericaMensagem.warn("Atenção", msg);
            }
        }
    }

    public void accordion(TabChangeEvent event) {
        indicaTab = ((TabView) event.getComponent()).getActiveIndex();
    }

    public void pesquisaDocumento() {
        JuridicaDB db = new JuridicaDBToplink();
        if (!juridica.getPessoa().getDocumento().isEmpty()) {
            List<Juridica> lista = db.pesquisaJuridicaPorDoc(juridica.getPessoa().getDocumento());
            if (!lista.isEmpty()) {
                GenericaMensagem.warn("Erro", "Esse documento já existe para: " + lista.get(0).getPessoa().getNome());
            }
        }
    }

    public String getInadimplente() {
        if (juridica.getId() != -1) {
            JuridicaDB db = new JuridicaDBToplink();
            int[] in = db.listaInadimplencia(juridica.getPessoa().getId());

            if (in[0] > 0 && in[1] > 0) {
                return "Esta empresa está inadimplente em " + in[0] + " mes(es) e com " + in[1] + " movimento(s) em atraso.";
            }
        }
        return "";
    }

    public String getContribuinte() {
        JuridicaDB db = new JuridicaDBToplink();
        if (juridica.getId() != -1) {
            List listax = db.listaJuridicaContribuinte(juridica.getId());

            for (int i = 0; i < listax.size(); i++) {
                if (((List) listax.get(0)).get(11) != null) {
                    // CONTRIBUINTE INATIVO
                    //cnaeContribuinte = " cnae contribuinte porém empresa inativa!";
                    cnaeContribuinte = " ";
                    colorContri = "red";
                    renderAtivoInativo = false;
                    return "CONTRIBUINTE INATIVO";
                } else {
                    //cnaeContribuinte = "cnae contribuinte!";
                    cnaeContribuinte = " ";
                    colorContri = "blue";
                    renderAtivoInativo = true;
                    return "CONTRIBUINTE ATIVO";
                }
            }
        }
        if (juridica.getCnae() != null && juridica.getCnae().getId() != -1) {
            CnaeConvencaoDB dbCnaeCon = new CnaeConvencaoDBToplink();
            if (dbCnaeCon.pesquisaCnaeComConvencao(juridica.getCnae().getId()) != null) {
                //cnaeContribuinte = " cnae contribuinte!";
                cnaeContribuinte = " ";
                colorContri = "blue";
            } else {
                cnaeContribuinte = " este cnae não está na convenção! ";
                colorContri = "red";
            }
        }
        renderAtivoInativo = false;
        return "NÃO CONTRIBUINTE";
    }

    public void inativarContribuintes() {
        JuridicaDB db = new JuridicaDBToplink();

        Dao dao = new Dao();

        if (!listaMotivoInativacao.isEmpty()) {
            contribuintesInativos.setJuridica(juridica);
            contribuintesInativos.setDtAtivacao(null);
            contribuintesInativos.setMotivoInativacao(db.pesquisaCodigoMotivoInativacao(Integer.parseInt(((SelectItem) listaMotivoInativacao.get(idMotivoInativacao)).getDescription())));

            dao.openTransaction();

            if (!dao.save(contribuintesInativos)) {
                dao.rollback();
                GenericaMensagem.error("Erro", "Não foi possível salvar Inativação!");
                return;
            }

            PessoaEmpresaDB dbp = new PessoaEmpresaDBToplink();
            List<PessoaEmpresa> result = dbp.listaPessoaEmpresaPorJuridica(juridica.getId());

            if (!result.isEmpty()) {
                for (PessoaEmpresa pe : result) {
                    pe.setPrincipal(false);
                    pe.setDemissao(DataHoje.data());

                    if (!dao.update(pe)) {
                        dao.rollback();
                        GenericaMensagem.error("Erro", "Não foi possível demissionar sócios!");
                        return;
                    }
                }
            }
            dao.commit();

            GenericaMensagem.info("Sucesso", "Contribuinte Inativado!");
            contribuintesInativos = new ContribuintesInativos();
            listaContribuintesInativos.clear();
            getListaContribuintesInativos();
            getContribuinte();
        } else {
            GenericaMensagem.error("Erro", "Não existe Motivo de Inativação!");
        }
    }

    public List<PessoaEmpresa> getListaPessoaEmpresa() {
        List<PessoaEmpresa> result = new ArrayList();
        if (juridica.getId() != -1) {
            PessoaEmpresaDB dbp = new PessoaEmpresaDBToplink();
            result = dbp.listaPessoaEmpresaPorJuridica(juridica.getId());
        }
        return result;
    }

    public String getEnderecoCobranca() {
        PessoaEndereco ende = null;
        String strCompl;
        if (!listaEnd.isEmpty()) {
            ende = (PessoaEndereco) listaEnd.get(0);
        }

        if (ende != null) {
            if (ende.getComplemento() == null || ende.getComplemento().isEmpty()) {
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

    public List<ContribuintesInativos> getListaContribuintesInativos() {
        if (listaContribuintesInativos.isEmpty()) {
            ContribuintesInativosDB db = new ContribuintesInativosDBToplink();
            listaContribuintesInativos = db.listaContribuintesInativos(juridica.getId());
            if (listaContribuintesInativos == null) {
                listaContribuintesInativos = new ArrayList();
            }
        }
        return listaContribuintesInativos;
    }

    public String btnExcluirMotivoInativacao(ContribuintesInativos linha) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        contribuintesInativos = linha;//(ContribuintesInativos) listaContribuintesInativos.get(idIndexInativacao);

        sv.abrirTransacao();
        if (!sv.deletarObjeto((ContribuintesInativos) sv.pesquisaCodigo(contribuintesInativos.getId(), "ContribuintesInativos"))) {
            GenericaMensagem.error("Erro", "Não foi possível excluir Motivo de Inativação, tente novamente!");
            sv.desfazerTransacao();
            return null;
        } else {
            sv.comitarTransacao();
        }
        listaContribuintesInativos.clear();
        getListaContribuintesInativos();
        msgConfirma = "Motivo excluído com sucesso!";
        contribuintesInativos = new ContribuintesInativos();
        GenericaMensagem.info("Sucesso", "Motivo Excluído!");
        if (listaContribuintesInativos.isEmpty()) {
        }
        getContribuinte();
        return null;
    }

    public String reativarContribuintes() {
        retornarCnaeConvencao();
        if (cnaeConvencao == null || cnaeConvencao.getCnae().getId() == -1) {
            GenericaMensagem.warn("Erro", "Cnae atual não pertence a Categoria!");
            return null;
        }

        ContribuintesInativosDB db = new ContribuintesInativosDBToplink();
        ContribuintesInativos cont = db.pesquisaContribuintesInativos(juridica.getId());

        if (cont.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            contribuintesInativos = (ContribuintesInativos) sv.pesquisaCodigo(cont.getId(), "ContribuintesInativos");
            contribuintesInativos.setAtivacao(DataHoje.data());
            sv.abrirTransacao();
            if (!sv.alterarObjeto(contribuintesInativos)) {
                GenericaMensagem.warn("Erro", "Erro ao reativar Empresa!");
                sv.desfazerTransacao();
                return null;
            } else {
                GenericaMensagem.info("Sucesso", "Contribuinte Reativado!");
                sv.comitarTransacao();
            }
            contribuintesInativos = new ContribuintesInativos();
            listaContribuintesInativos.clear();
            getListaContribuintesInativos();
            getContribuinte();
        }
        //GenericaMensagem.warn("Erro", "Salve o cadastro para efetuar a ativação");
        return null;
    }

    public void setEnderecoCobranca(String enderecoCobranca) {
        this.enderecoCobranca = enderecoCobranca;
    }

    public String getDtAtivacao() {
        String dt = "";
        return dt;
    }

    public String getDtAtivacaoInativo() {
        String dt = "";
        if (contribuintesInativos != null) {
            if (contribuintesInativos.getId() != -1) {
                dt = contribuintesInativos.getAtivacao();
            }
        } else {
            dt = "";
        }
        return dt;
    }

    public String salvar() {
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        JuridicaDB db = new JuridicaDBToplink();
        TipoDocumentoDB dbDoc = new TipoDocumentoDBToplink();
        Pessoa pessoa = getJuridica().getPessoa();
        List listDocumento;
        if (listaEnd.isEmpty() || pessoa.getId() == -1) {
            adicionarEnderecos();
        }

        juridica.setPorte((Porte) new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.parseInt(getListaPorte().get(idPorte).getDescription()), "Porte"));

//        if (!chkEndContabilidade) {
//            juridica.setEmailEscritorio(false);
//        }
        //else
        //    juridica.setEmailEscritorio(false);
        juridica.getPessoa().setNome(juridica.getPessoa().getNome().trim());
        dbSalvar.abrirTransacao();
        if (juridica.getId() == -1) {
            juridica.getPessoa().setTipoDocumento((TipoDocumento) dbSalvar.pesquisaCodigo(Integer.parseInt(((SelectItem) getListaTipoDocumento().get(idTipoDocumento)).getDescription()), "TipoDocumento"));
            if (juridica.getPessoa().getNome().isEmpty()) {
                GenericaMensagem.error("Erro", "O campo nome não pode ser nulo!");
                return null;
            }

            if (Integer.parseInt(((SelectItem) getListaTipoDocumento().get(idTipoDocumento)).getDescription()) == 4) {
                pessoa.setDocumento("0");
            } else {
                listDocumento = db.pesquisaJuridicaPorDoc(juridica.getPessoa().getDocumento());
                for (int i = 0; i < listDocumento.size(); i++) {
                    if (!listDocumento.isEmpty()) {
                        GenericaMensagem.error("Erro", "Empresa já existente no Sistema!");
                        return null;
                    }
                }
            }

            if (!validaTipoDocumento(Integer.parseInt(getListaTipoDocumento().get(idTipoDocumento).getDescription()), juridica.getPessoa().getDocumento())) {
                GenericaMensagem.error("Erro", "Documento Invalido!");
                return null;
            }

            if (listaEnd.isEmpty()) {
                GenericaMensagem.error("Erro", "Cadastro não pode ser salvo sem Endereço!");
                return null;
            }

            if (juridica.getPessoa().getId() == -1) {
                dbSalvar.inserirObjeto(pessoa);
                juridica.setPessoa(pessoa);
                if (juridica.getCnae().getId() == -1) {
                    juridica.setCnae(null);
                }

                if (juridicaReceita.getId() != -1) {
                    juridicaReceita.setPessoa(pessoa);
                    dbSalvar.alterarObjeto(juridicaReceita);
                }

                gerarLoginSenhaPessoa(juridica.getPessoa(), dbSalvar);
                if (dbSalvar.inserirObjeto(juridica)) {
                    GenericaMensagem.info("Sucesso", "Cadastro salvo!");
                    dbSalvar.comitarTransacao();
                    NovoLog novoLog = new NovoLog();
                    novoLog.setTabela("pes_juridica");
                    novoLog.setCodigo(juridica.getId());
                    novoLog.save("ID: " + juridica.getId() + " - Pessoa: (" + juridica.getPessoa().getId() + ") " + juridica.getPessoa().getNome() + " - Abertura" + juridica.getAbertura() + " - Fechamento" + juridica.getAbertura() + " - I.E.: " + juridica.getInscricaoEstadual() + " - Insc. Mun.: " + juridica.getInscricaoMunicipal() + " - Responsável: " + juridica.getResponsavel());
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", juridica);
                } else {
                    GenericaMensagem.error("Erro", "Erro ao Salvar Dados!");
                    dbSalvar.desfazerTransacao();
                    return null;
                }
            }
        } else {
            if (juridica.getPessoa().getDtAtualizacao() == null) {
                juridica.getPessoa().setDtAtualizacao(new Date());
            }
            if (juridica.getPessoa().getNome().isEmpty()) {
                GenericaMensagem.error("Erro", "O campo nome não pode ser nulo!");
                return null;
            }

            if (Integer.parseInt(((SelectItem) getListaTipoDocumento().get(idTipoDocumento)).getDescription()) == 4) {
                juridica.getPessoa().setDocumento("0");
            } else {
                listDocumento = db.pesquisaJuridicaPorDoc(juridica.getPessoa().getDocumento());
                for (int i = 0; i < listDocumento.size(); i++) {
                    if (!listDocumento.isEmpty() && ((Juridica) listDocumento.get(i)).getId() != juridica.getId()) {
                        GenericaMensagem.error("Erro", "Empresa já existente no Sistema!");
                        return null;
                    }
                }
                juridica.getPessoa().setTipoDocumento((TipoDocumento) dbSalvar.pesquisaCodigo(Integer.parseInt(((SelectItem) getListaTipoDocumento().get(idTipoDocumento)).getDescription()), "TipoDocumento"));
            }
            if (!validaTipoDocumento(Integer.parseInt(getListaTipoDocumento().get(idTipoDocumento).getDescription()), juridica.getPessoa().getDocumento())) {
                GenericaMensagem.error("Erro", "Documento Invalido!");
                return null;
            }
            adicionarEnderecos();
            if ((juridica.getPessoa().getLogin()) == null && (juridica.getPessoa().getSenha()) == null) {
                gerarLoginSenhaPessoa(juridica.getPessoa(), dbSalvar);
            }
            Juridica jur = (Juridica) dbSalvar.pesquisaCodigo(juridica.getId(), "Juridica");
            String beforeUpdate = "ID: " + jur.getId() + " - Pessoa: (" + jur.getPessoa().getId() + ") " + jur.getPessoa().getNome() + " - Abertura: " + jur.getAbertura() + " - Fechamento: " + jur.getAbertura() + " - I.E.: " + jur.getInscricaoEstadual() + " - Insc. Mun.: " + jur.getInscricaoMunicipal() + " - Responsável: " + jur.getResponsavel();
            dbSalvar.alterarObjeto(juridica.getPessoa());
            if (dbSalvar.alterarObjeto(juridica)) {

                GenericaMensagem.info("Sucesso", "Cadastro atualizado com Sucesso!");
                dbSalvar.comitarTransacao();

                // ATUALIZA CONTABILIDADE SE FOR ELA MESMA A PESSOA JURIDICA ---
                if ((juridica.getContabilidade() != null && juridica.getContabilidade().getId() != -1) && (juridica.getContabilidade().getId() == juridica.getId())) {
                    nomeContabilidade = juridica.getPessoa().getNome();
                    //juridica = (Juridica) new Dao().find(new Juridica(), juridica.getId());
                    //juridica.setContabilidade(juridica);
                    //dbSalvar.alterarObjeto(juridica.getContabilidade());
                }

                NovoLog novoLog = new NovoLog();
                novoLog.setTabela("pes_juridica");
                novoLog.setCodigo(juridica.getId());
                novoLog.update(beforeUpdate, "ID: " + juridica.getId() + " - Pessoa: (" + juridica.getPessoa().getId() + ") " + juridica.getPessoa().getNome() + " - Abertura: " + juridica.getAbertura() + " - Fechamento: " + juridica.getAbertura() + " - I.E.: " + juridica.getInscricaoEstadual() + " - Insc. Mun.: " + juridica.getInscricaoMunicipal() + " - Responsável: " + juridica.getResponsavel());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", juridica);
            } else {
                dbSalvar.desfazerTransacao();
                GenericaMensagem.error("Erro", "Erro ao atualizar Cadastro!");
            }
        }
        getContribuinte();
        salvarEndereco();

        return null;
    }

    public String novo() {
//        juridica = new Juridica();
//        contabilidade = new Juridica();
//        pessoaEndereco = new PessoaEndereco();
//        convencao = new Convencao();
//        marcar = false;
//        alterarEnd = false;
//        renChkEndereco = "false";
//        cnaeContribuinte = " sem cnae! ";
//        colorContri = "red";
//        renEndereco = "false";
//        renNovoEndereco = "false";
//        msgDocumento = "";
//        listaEnd = new ArrayList();
//        idTipoDocumento = 1;
//        idPorte = 0;
//        listaContribuintesInativos.clear();
//        setEnderecoCompleto("");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaComplementoBean");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoNum");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoComp");
//        listaEmpresasPertencentes.clear();
        GenericaSessao.put("juridicaBean", new JuridicaBean());
        return "pessoaJuridica";
    }

    public void novoGenerico() {
        juridica = new Juridica();
        nomeContabilidade = "";
        pessoaEndereco = new PessoaEndereco();
        convencao = new Convencao();
        marcar = false;
        alterarEnd = false;
        renChkEndereco = "false";
        cnaeContribuinte = " sem cnae! ";
        colorContri = "red";
        renEndereco = false;
        renNovoEndereco = false;
        msgDocumento = "";
        listaEnd = new ArrayList();
        idTipoDocumento = 1;
        idPorte = 0;
        listaContribuintesInativos.clear();
        setEnderecoCompleto("");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoNum");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoComp");
    }

    public String excluir() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        PessoaEnderecoDB dbPE = new PessoaEnderecoDBToplink();
        if (juridica.getId() == -1) {
            GenericaMensagem.error("Erro", "Pesquise uma empresa para ser excluída!");
            return null;
        }
        List<PessoaEndereco> listaEndereco = dbPE.pesquisaEndPorPessoa(juridica.getPessoa().getId());

        sv.abrirTransacao();
        if (!listaEndereco.isEmpty()) {
            PessoaEndereco pe;
            for (int i = 0; i < listaEndereco.size(); i++) {
                pe = (PessoaEndereco) sv.pesquisaCodigo(listaEndereco.get(i).getId(), "PessoaEndereco");
                if (!sv.deletarObjeto(pe)) {
                    GenericaMensagem.error("Erro", "Erro ao excluir uma Pessoa Endereço!");
                    sv.desfazerTransacao();
                    return null;
                }
            }
        }

        ContribuintesInativosDB dbCI = new ContribuintesInativosDBToplink();
        List<ContribuintesInativos> listaCI = dbCI.listaContribuintesInativos(juridica.getId());

        if (!listaCI.isEmpty()) {
            ContribuintesInativos ci;
            for (int i = 0; i < listaCI.size(); i++) {
                ci = (ContribuintesInativos) sv.pesquisaCodigo(listaCI.get(i).getId(), "ContribuintesInativos");
                if (!sv.deletarObjeto(ci)) {
                    GenericaMensagem.error("Erro", "Erro ao excluir Contribuintes Inativos!");
                    sv.desfazerTransacao();
                    return null;
                }
            }
        }

        // ------------------------------------------------------------------------------------------------------
        if (!sv.deletarObjeto((Juridica) sv.pesquisaCodigo(juridica.getId(), "Juridica"))) {
            GenericaMensagem.error("Erro", "Erro ao excluir Jurídica!");
            sv.desfazerTransacao();
            return null;
        }

        //  EXCLUIR OS EMAILS ENVIADOS PESSOA --------------------------------------------------------------
        EnvioEmailsDB db = new EnvioEmailsDBToplink();
        List<EnvioEmails> listE = db.pesquisaTodosPorPessoa(juridica.getPessoa().getId());

        for (int i = 0; i < listE.size(); i++) {
            if (!sv.deletarObjeto((EnvioEmails) sv.pesquisaCodigo(listE.get(i).getId(), "EnvioEmails"))) {
                GenericaMensagem.error("Erro", "Erro ao excluir Emails enviados!");
                sv.desfazerTransacao();
                return null;
            }
        }

        PessoaDB dbp = new PessoaDBToplink();
        String documento = AnaliseString.extrairNumeros(juridica.getPessoa().getDocumento());
        JuridicaReceita jr = dbp.pesquisaJuridicaReceita(documento);

        if (jr.getId() != -1) {
            if (!sv.deletarObjeto(sv.pesquisaCodigo(jr.getId(), "JuridicaReceita"))) {
                GenericaMensagem.error("Erro", "Erro ao excluir Pesquisa da Receita!");
                sv.desfazerTransacao();
                return null;
            }
        }
        // -------------------------------------------------------------------------------------------------

        if (!sv.deletarObjeto((Pessoa) sv.pesquisaCodigo(juridica.getPessoa().getId(), "Pessoa"))) {
            GenericaMensagem.error("Erro", "Erro ao excluir Pessoa!");
            sv.desfazerTransacao();
            return null;
        }
        GenericaMensagem.info("Sucesso", "Cadastro excluido com sucesso!");
        NovoLog novoLog = new NovoLog();
        novoLog.delete("ID: " + juridica.getId() + " - Pessoa: (" + juridica.getPessoa().getId() + ") " + juridica.getPessoa().getNome() + " - Abertura" + juridica.getAbertura() + " - Fechamento" + juridica.getAbertura() + " - I.E.: " + juridica.getInscricaoEstadual() + " - Insc. Mun.: " + juridica.getInscricaoMunicipal() + " - Responsável: " + juridica.getResponsavel());
        sv.comitarTransacao();
        novoGenerico();
        return null;
    }

    public String editar(Juridica j, Boolean completo) {
        listRepisMovimento.clear();
        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        juridica = j;
        if (!completo) {
            if (url != null && !url.isEmpty() && !url.equals("pessoaJuridica")) {
                switch (url) {
                    case "agendamento":
                        break;
                    default:
                        GenericaSessao.remove("juridicaBean");
                }
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", juridica);
                return url;
            }
        }
        listSocios.clear();
        listaContribuintesInativos.clear();
        listaEmpresasPertencentes.clear();
        contribuintesInativos = new ContribuintesInativos();

        if (juridica.getContabilidade() != null) {
            nomeContabilidade = juridica.getContabilidade().getPessoa().getNome();
        } else {
            nomeContabilidade = "";
        }

        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        if (!getListaTipoDocumento().isEmpty()) {
            for (int o = 0; o < listaTipoDocumento.size(); o++) {
                if (Integer.parseInt(listaTipoDocumento.get(o).getDescription()) == juridica.getPessoa().getTipoDocumento().getId()) {
                    idTipoDocumento = o;
                }
            }
        }

        if (!getListaPorte().isEmpty()) {
            for (int o = 0; o < listaPorte.size(); o++) {
                if (Integer.parseInt(listaPorte.get(o).getDescription()) == juridica.getPorte().getId()) {
                    idPorte = o;
                }
            }
        }

        getContribuinte();
        if (juridica.getContabilidade() == null) {
            renChkEndereco = "false";
        } else {
            renChkEndereco = "true";
        }
        renNovoEndereco = false;
        renEndereco = false;
        alterarEnd = true;
        listaEnd = new ArrayList();
        enderecoCobranca = "NENHUM";
        getListaEnderecos();

//        if (contabilidade != null) {
//            if (contabilidade.getId() != -1) {
//                PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
//                // PessoaEndereco pesEnd1 = new PessoaEndereco(), pesEnd2 = new PessoaEndereco();
////                    PessoaEndereco pesEnd1 = db.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 3);
////                    PessoaEndereco pesEnd2 = db.pesquisaEndPorPessoaTipo(contabilidade.getPessoa().getId(), 3);
////                    if (comparaEndereco(pesEnd1, pesEnd2) || listaEnd.isEmpty()) {
////                        chkEndContabilidade = true;
////                    } else {
////                        chkEndContabilidade = false;
////                    }
//            }
//        }
        existeOposicaoEmpresa();
        getListSocios();
        loadMalaDireta();
        return "pessoaJuridica";

    }

    public String editar(Juridica j) {
        return editar(j, false);
    }

//    public String editar(Juridica j) {
//        juridica = j;
//        if (juridica.getContabilidade() == null) {
//            contabilidade = new Juridica();
//        } else {
//            contabilidade = juridica.getContabilidade();
//        }
//        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        descPesquisa = "";
//        porPesquisa = "nome";
//        comoPesquisa = "";
//        if (!getListaTipoDocumento().isEmpty()) {
//            for (int o = 0; o < listaTipoDocumento.size(); o++) {
//                if (Integer.parseInt(listaTipoDocumento.get(o).getDescription()) == juridica.getPessoa().getTipoDocumento().getId()) {
//                    idTipoDocumento = o;
//                }
//            }
//        }
//
//        if (!getListaPorte().isEmpty()) {
//            for (int o = 0; o < listaPorte.size(); o++) {
//                if (Integer.parseInt(listaPorte.get(o).getDescription()) == juridica.getPorte().getId()) {
//                    idPorte = o;
//                }
//            }
//        }
//        if (url != null) {
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", juridica);
//
//            if (juridica.getContabilidade() == null) {
//                renChkEndereco = "false";
//            } else {
//                renChkEndereco = "true";
//            }
//            renNovoEndereco = "false";
//            renEndereco = "false";
//            alterarEnd = true;
//            listaEnd = new ArrayList();
//            enderecoCobranca = "NENHUM";
//            getListaEnderecos();
//            return url;
//        }
//        return "pessoaJuridica";
//    }
    public String editarEmpresaPertencente(DataObject linha) {
        juridica = (Juridica) linha.getArgumento0();// (Juridica) listaEmpresasPertencentes.get(idIndexPertencente).getArgumento0();
        if (juridica.getContabilidade() != null) {
            nomeContabilidade = juridica.getContabilidade().getPessoa().getNome();
        }

        //contabilidade = juridica.getContabilidade();
        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        indicaTab = 0;
        if (!getListaPorte().isEmpty()) {
            for (int o = 0; o < listaPorte.size(); o++) {
                if (Integer.parseInt(listaPorte.get(o).getDescription()) == juridica.getPorte().getId()) {
                    idPorte = o;
                }
            }
        }

        if (url != null) {
            if (!getListaTipoDocumento().isEmpty()) {
                for (int o = 0; o < listaTipoDocumento.size(); o++) {
                    if (Integer.parseInt(listaTipoDocumento.get(o).getDescription()) == juridica.getPessoa().getTipoDocumento().getId()) {
                        setIdTipoDocumento(o);
                    }
                }
            }
            if (juridica.getContabilidade() == null) {
                renChkEndereco = "false";
            } else {
                renChkEndereco = "true";
            }
            renNovoEndereco = false;
            renEndereco = false;
            alterarEnd = true;
            listaEnd = new ArrayList();
            enderecoCobranca = "NENHUM";
            listaContribuintesInativos.clear();
            getListaEnderecos();
            return "pessoaJuridica";
        }
        return "pessoaJuridica";
    }

    public String editarEmpresaContabilidade() {
        // JuridicaDB db = new JuridicaDBToplink();
        //juridica = db.pesquisaCodigo(juridica.getContabilidade().getId());
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        juridica = (Juridica) salvarAcumuladoDB.pesquisaObjeto(juridica.getContabilidade().getId(), "Juridica");
        if (juridica.getContabilidade() != null) {
            nomeContabilidade = juridica.getContabilidade().getPessoa().getNome();
        }
        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        if (url != null) {
            if (!getListaTipoDocumento().isEmpty()) {
                for (int o = 0; o < listaTipoDocumento.size(); o++) {
                    if (Integer.parseInt(listaTipoDocumento.get(o).getDescription()) == juridica.getPessoa().getTipoDocumento().getId()) {
                        setIdTipoDocumento(o);
                    }
                }
            }
            if (juridica.getContabilidade() == null) {
                renChkEndereco = "false";
            } else {
                renChkEndereco = "true";
            }
            renNovoEndereco = false;
            renEndereco = false;
            alterarEnd = true;
            listaEnd = new ArrayList();
            enderecoCobranca = "NENHUM";
            listaContribuintesInativos.clear();
            getListaEnderecos();
            return "pessoaJuridica";
        }
        return "pessoaJuridica";
    }

    public String editarContabilidade(Juridica j) {
        Juridica contabilidade = j; //(Juridica) listaContabilidade.get(idIndexContabilidade);
        juridica.setContabilidade(contabilidade);
        juridica.setEmailEscritorio(true);
        juridica.setCobrancaEscritorio(true);
        renChkEndereco = "true";
        nomeContabilidade = contabilidade.getPessoa().getNome();
        //chkEndContabilidade = true; // ROGÉRINHO PEDIU PRA VOLTAR TRUE NA DATA -- 30/07/2013 -- POR CAUSA DO CARLOS DE LIMEIRA
        return "pessoaJuridica";
    }

//   public List getListaJuridica(){
//       List result = null;
//       JuridicaDB db = new JuridicaDBToplink();
//       result = db.pesquisaTodos();
//       return result;
//   }
    public String adicionarEnderecos() {
        // List tiposE = new ArrayList();
        TipoEnderecoDB db_tipoEndereco = new TipoEnderecoDBToplink();
        PessoaEnderecoDB db_pesEnd = new PessoaEnderecoDBToplink();
        endereco = new Endereco();
        String num;
        String comp;
        int i = 0;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoNum", pessoaEndereco.getNumero());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoComp", pessoaEndereco.getComplemento());
        List tiposE = db_tipoEndereco.listaTipoEnderecoParaJuridica();
        endereco = (Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa");
        if (endereco != null) {
            if (!alterarEnd || listaEnd.isEmpty()) {
                num = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoNum");
                comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoComp");
                while (i < tiposE.size()) {
                    pessoaEndereco.setEndereco(endereco);
                    pessoaEndereco.setTipoEndereco((TipoEndereco) tiposE.get(i));
                    pessoaEndereco.setPessoa(juridica.getPessoa());
                    pessoaEndereco.setNumero(num);
                    pessoaEndereco.setComplemento(comp);
                    listaEnd.add(pessoaEndereco);
                    i++;
                    pessoaEndereco = new PessoaEndereco();
                }
            } else {
                num = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoNum");
                comp = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoComp");
                if (!listaEnd.isEmpty() && (pessoaEndereco.getTipoEndereco().getId() == 2)) {

                    if (pessoaEndereco.getId() != -1) {
                        // PessoaEndereco pessoaEndeAnt = new PessoaEndereco();
                        PessoaEndereco pessoaEndeAnt = db_pesEnd.pesquisaEndPorPessoaTipo(pessoaEndereco.getPessoa().getId(), 2);
                        ((PessoaEndereco) listaEnd.get(0)).setTipoEndereco((TipoEndereco) tiposE.get(0));
                        ((PessoaEndereco) listaEnd.get(0)).setEndereco(endereco);
                        ((PessoaEndereco) listaEnd.get(0)).setComplemento(pessoaEndereco.getComplemento());
                        ((PessoaEndereco) listaEnd.get(0)).setNumero(pessoaEndereco.getNumero());
                        for (int u = 1; u < listaEnd.size(); u++) {
                            if (comparaEndereco(pessoaEndeAnt, (PessoaEndereco) listaEnd.get(u))) {
                                ((PessoaEndereco) listaEnd.get(u)).setTipoEndereco((TipoEndereco) tiposE.get(u));
                                ((PessoaEndereco) listaEnd.get(u)).setEndereco(endereco);
                                ((PessoaEndereco) listaEnd.get(u)).setComplemento(pessoaEndereco.getComplemento());
                                ((PessoaEndereco) listaEnd.get(u)).setNumero(pessoaEndereco.getNumero());
                            }
                        }
                        endComercial = true;
                    } else {
                        listaEnd = new ArrayList();
                        for (int u = 0; u < tiposE.size(); u++) {
                            pessoaEndereco.setEndereco(endereco);
                            pessoaEndereco.setTipoEndereco((TipoEndereco) tiposE.get(u));
                            pessoaEndereco.setPessoa(juridica.getPessoa());
                            pessoaEndereco.setNumero(num);
                            pessoaEndereco.setComplemento(comp);
                            listaEnd.add(pessoaEndereco);
                            pessoaEndereco = new PessoaEndereco();
                        }
                    }
                } else {
                    pessoaEndereco.setEndereco(endereco);
                    pessoaEndereco.setPessoa(juridica.getPessoa());
                    pessoaEndereco.setNumero(num);
                    pessoaEndereco.setComplemento(comp);

                    ((PessoaEndereco) listaEnd.get(idIndexEndereco)).setEndereco(endereco);
                    ((PessoaEndereco) listaEnd.get(idIndexEndereco)).setComplemento(pessoaEndereco.getComplemento());
                    ((PessoaEndereco) listaEnd.get(idIndexEndereco)).setNumero(pessoaEndereco.getNumero());
                }
                alterarEnd = false;
            }
            renEndereco = true;
            renNovoEndereco = false;
        }
        setEnderecoCompleto("");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoNum");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoComp");
        return "pessoaJuridica";
    }

    public boolean comparaEndereco(PessoaEndereco pessoaEnde1, PessoaEndereco pessoaEnde2) {
        boolean compara;
        if (pessoaEnde1 != null && pessoaEnde2 != null) {
            if (pessoaEnde1.getComplemento() == null || pessoaEnde2.getComplemento() == null) {
                pessoaEnde1.setComplemento("");
                pessoaEnde2.setComplemento("");
            }
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

    public List<PessoaEndereco> getListaEnderecos() {
        // PessoaEndereco pesEn = new PessoaEndereco();
        String strCompl;
        if (!getPesquisaEndPorPessoa().isEmpty() && alterarEnd && listaEnd.isEmpty()) {
            listaEnd = getPesquisaEndPorPessoa();
            if (listaEnd.size() < 4) {
                PessoaEndereco pe = (PessoaEndereco) listaEnd.get(0);
                PessoaEndereco pe2 = new PessoaEndereco();
                Dao dao = new Dao();
                List<TipoEndereco> list = dao.find("TipoEndereco", new int[]{2, 3, 4, 5});
                List<TipoEndereco> listB = list;
                for (int i = 0; i < listaEnd.size(); i++) {
                    for (int y = 0; y < listB.size(); y++) {
                        if (((PessoaEndereco) listaEnd.get(i)).getTipoEndereco().getId() == listB.get(y).getId()) {
                            try {
                                list.remove(listB.get(y));
                            } catch (Exception e) {
                                // e.getMessage();
                            }
                        }
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    pe2 = new PessoaEndereco();
                    pe2.setComplemento(pe.getComplemento());
                    pe2.setEndereco(pe.getEndereco());
                    pe2.setNumero(pe.getNumero());
                    pe2.setPessoa(pe.getPessoa());
                    pe2.setTipoEndereco(list.get(i));
                    dao.save(pe2, true);
                }
                listaEnd.clear();
                listaEnd = getPesquisaEndPorPessoa();
            }
            PessoaEndereco pesEn = (PessoaEndereco) (listaEnd.get(1));
            if (pesEn.getComplemento() == null || pesEn.getComplemento().isEmpty()) {
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

    public void abreEndereco() {
        listaEnd = getListaEnderecos();
        if (listaEnd.isEmpty()) {
            renEndereco = false;
            renNovoEndereco = true;
            pessoaEndereco = new PessoaEndereco();
            listaEnd = new ArrayList();
        } else {
            renEndereco = true;
            renNovoEndereco = false;
        }
    }

    public void salvarEndereco() {
        //VERIFICAR ENDERECO CONTABILIDADE
        verificarEndContabilidade();
        if (juridica.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            if (getPesquisaEndPorPessoa().isEmpty()) {
                for (int i = 0; i < listaEnd.size(); i++) {
                    pessoaEndereco = (PessoaEndereco) listaEnd.get(i);
                    salvarAcumuladoDB.abrirTransacao();
                    if (salvarAcumuladoDB.inserirObjeto(pessoaEndereco)) {
                        salvarAcumuladoDB.comitarTransacao();
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                        msgConfirma = "Erro ao Salvar Endereço!";
                    }
                    pessoaEndereco = new PessoaEndereco();
                }
            } else {
                if (endComercial) {
                    atualizarEndJuridicaComContabil();
                    for (int o = 0; o < listaEnd.size(); o++) {
                        salvarAcumuladoDB.abrirTransacao();
                        if (salvarAcumuladoDB.alterarObjeto((PessoaEndereco) listaEnd.get(o))) {
                            salvarAcumuladoDB.comitarTransacao();
                        } else {
                            salvarAcumuladoDB.desfazerTransacao();
                        }
                    }
                    endComercial = false;
                } else {
                    if (pessoaEndereco.getTipoEndereco().getId() == 3) {
                        atualizarEndJuridicaComContabil();
                    }
                    for (int o = 0; o < listaEnd.size(); o++) {
                        salvarAcumuladoDB.abrirTransacao();
                        if (salvarAcumuladoDB.alterarObjeto((PessoaEndereco) listaEnd.get(o))) {
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

    public void verificarEndContabilidade() {
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        if (juridica.getId() != -1) {
            if (juridica.isCobrancaEscritorio() && (juridica.getContabilidade() != null && juridica.getContabilidade().getId() != -1)) {
                if (juridica.getId() != juridica.getContabilidade().getId()) {
                    PessoaEndereco pesEndCon = db.pesquisaEndPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3);
                    if ((!listaEnd.isEmpty()) && pesEndCon != null) {
                        pessoaEndereco = (PessoaEndereco) listaEnd.get(1);
                        pessoaEndereco.setComplemento(pesEndCon.getComplemento());
                        pessoaEndereco.setNumero(pesEndCon.getNumero());
                        endereco = pesEndCon.getEndereco();
                        pessoaEndereco.setEndereco(endereco);
                        listaEnd.set(1, pessoaEndereco);
                    }
                }
            } else if (juridica != null) {
                if (juridica.getContabilidade() != null) {
                    if (comparaEndereco((PessoaEndereco) listaEnd.get(1), db.pesquisaEndPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3))) {
                        PessoaEndereco pesEndCon = db.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 2);
                        if ((!listaEnd.isEmpty()) && pesEndCon != null && !endComercial) {
                            pessoaEndereco = (PessoaEndereco) listaEnd.get(1);
                            pessoaEndereco.setComplemento(pesEndCon.getComplemento());
                            pessoaEndereco.setNumero(pesEndCon.getNumero());
                            endereco = pesEndCon.getEndereco();
                            pessoaEndereco.setEndereco(endereco);
                            listaEnd.set(1, pessoaEndereco);
                        }
                    }
                }
                //juridica.setCobrancaEscritorio(false);
            }
        }
    }

    public void pesquisaContabilidadeI() {
        if (!nomePesquisaContabilidade.isEmpty()) {
            JuridicaDB db = new JuridicaDBToplink();
            listaContabilidade = db.pesquisaPessoa(nomePesquisaContabilidade, "nome", "I");
        } else {
            listaContabilidade = new ArrayList();
        }
    }

    public void pesquisaContabilidadeP() {
        if (!nomePesquisaContabilidade.isEmpty()) {
            JuridicaDB db = new JuridicaDBToplink();
            listaContabilidade = db.pesquisaPessoa(nomePesquisaContabilidade, "nome", "P");
        } else {
            listaContabilidade = new ArrayList();
        }
    }

    public String atualizarEndJuridicaComContabil() {
        if (juridica.getId() != -1) {
            JuridicaDB db = new JuridicaDBToplink();
            PessoaEnderecoDB pesEndDB = new PessoaEnderecoDBToplink();
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List listaPesEndEmpPertencente = db.pesquisaPesEndEmpresaComContabil(juridica.getId());
            PessoaEndereco endeEmp2 = pesEndDB.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 2);
            if (!listaPesEndEmpPertencente.isEmpty()) {
                pessoaEndereco = (PessoaEndereco) listaEnd.get(1);
                for (int i = 0; i < listaPesEndEmpPertencente.size(); i++) {
                    if (comparaEndereco(endeEmp2, (PessoaEndereco) listaPesEndEmpPertencente.get(i))) {
                        PessoaEndereco endeEmp = (PessoaEndereco) listaPesEndEmpPertencente.get(i);
                        endeEmp.setComplemento(pessoaEndereco.getComplemento());
                        endeEmp.setNumero(pessoaEndereco.getNumero());
                        endeEmp.setEndereco(pessoaEndereco.getEndereco());
                        salvarAcumuladoDB.abrirTransacao();
                        if (salvarAcumuladoDB.alterarObjeto(endeEmp)) {
                            salvarAcumuladoDB.comitarTransacao();
                            endeEmp = new PessoaEndereco();
                        } else {
                            salvarAcumuladoDB.desfazerTransacao();
                        }
                    }
                }
            }
        }
        return null;
    }

    public String RetornarObjetoDaGrid(PessoaEndereco linha, int index) {
        pessoaEndereco = linha;//(PessoaEndereco) listaEnd.get(idIndexEndereco);
        idIndexEndereco = index;
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoPesquisa", pessoaEndereco.getEndereco());
        log = pessoaEndereco.getEndereco().getLogradouro().getDescricao();
        desc = pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao();
        cid = pessoaEndereco.getEndereco().getCidade().getCidade();
        uf = pessoaEndereco.getEndereco().getCidade().getUf();
        setEnderecoCompleto(log + " " + desc + ", " + cid + " - " + uf);
        renEndereco = false;
        renNovoEndereco = true;
        alterarEnd = true;
        return "pessoaJuridica";
    }

    public List getListaPessoaEndereco() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        List result = salvarAcumuladoDB.listaObjeto("PessoaEndereco");
        return result;
    }

    public String CarregarEndereco() {
        int idEndereco = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("paramEndereco"));
        pessoaEndereco.setEndereco((Endereco) new Dao().find(new Endereco(), idEndereco));
        setEnderecoCompleto((pessoaEndereco.getEndereco().getLogradouro().getDescricao()) + " " + pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao());
        return "pessoaJuridica";
    }

    public List<String> BuscaTipoEndereco(Object event) {
        //List<String> result = new Vector<String>();
        String txtDigitado = event.toString().toLowerCase().toUpperCase();
        TipoEnderecoDB db = new TipoEnderecoDBToplink();
        List<String> result = db.pesquisaTipoEnderecoParaJuridica('%' + txtDigitado + '%');
        return (result);
    }

    public List<String> BuscaTipoDocumento(Object event) {
        //List<String> result = new Vector<String>();
        String txtDigitado = event.toString().toLowerCase().toUpperCase();
        TipoDocumentoDB db = new TipoDocumentoDBToplink();
        List<String> result = db.pesquisaTipoDocumento('%' + txtDigitado + '%');
        return (result);
    }

    public List getPesquisaEndPorPessoa() {
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        List result = db.pesquisaEndPorPessoa(juridica.getPessoa().getId());
        return result;
    }

    public String voltarEndereco() {
        indicaTab = 0;
        return "pessoaJuridica";
    }

    public boolean getHabilitar() {
        if (juridica.getPessoa().getId() == -1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getHabilitarFilial() {
        if ((juridica.getPessoa().getId() != -1) && (filialMatriz.equals("m"))) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            if (filial.getId() == -1) {
                filial.setFilial(juridica);
                filial.setMatriz(juridica);
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.inserirObjeto(filial)) {
                    salvarAcumuladoDB.comitarTransacao();
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                }
            } else {
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.alterarObjeto(filial)) {
                    salvarAcumuladoDB.comitarTransacao();
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                }
            }
            return false;
        } else {
            return true;
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
        return "pessoaJuridica";
    }

    public void refreshForm() {
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listaJuridica.clear();
        loadList();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listaJuridica.clear();
        loadList();
    }

    public void acaoPesquisaCnaeInicial() {
        comoPesquisaCnae = "I";
        descPesquisaCnae = descPesquisaCnae.replace("-", "").replace("/", "").replace(".", "");
        listaCnae.clear();
    }

    public void acaoPesquisaCnaeParcial() {
        comoPesquisaCnae = "P";
        descPesquisaCnae = descPesquisaCnae.replace("-", "").replace("/", "").replace(".", "");
        listaCnae.clear();
    }

    public List<Cnae> getListaCnae() {
        if (listaCnae.isEmpty()) {
            CnaeDB db = new CnaeDBToplink();
            listaCnae = db.pesquisaCnae(descPesquisaCnae, porPesquisaCnae, comoPesquisaCnae);
        }
        return listaCnae;
    }

    public void setListaCnae(List<Cnae> listaCnae) {
        this.listaCnae = listaCnae;
    }

    public void retornaCnaeReceita(Cnae cn) {
        juridica.setCnae(cn);
        CnaeConvencaoDB dbCnaeCon = new CnaeConvencaoDBToplink();
        if (dbCnaeCon.pesquisaCnaeComConvencao(juridica.getCnae().getId()) != null) {
            cnaeContribuinte = " cnae contribuinte!";
            colorContri = "blue";
        } else {
            cnaeContribuinte = " este cnae não está na convenção!";
            colorContri = "red";
        }
    }

    public String retornaCnae(Cnae cn) {
        //Cnae tcnae = null;
        //Cnae tcnae = (Cnae) listaCnae.get(idIndexCnae);
        Cnae tcnae = cn;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cnaePesquisado", tcnae);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        descPesquisaCnae = "";
        if (((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno")).equals("pessoaJuridica")) {
            juridica.setCnae(tcnae);
            CnaeConvencaoDB dbCnaeCon = new CnaeConvencaoDBToplink();
            if (dbCnaeCon.pesquisaCnaeComConvencao(juridica.getCnae().getId()) != null) {
                cnaeContribuinte = " cnae contribuinte!";
                colorContri = "blue";
            } else {
                cnaeContribuinte = " este cnae não está na convenção!";
                colorContri = "red";
            }
            return "pessoaJuridica";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public String JuridicaFilialGrid() {
        filial = new Filial();
        int i = filial.getFilial().getId();
        filial.setMatriz(juridica);
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.inserirObjeto(filial)) {
            salvarAcumuladoDB.comitarTransacao();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
        }
        //setIndicaTab("filial");
        return "pessoaJuridica";
    }

    public List getPesquisaJuridicaFilial() {
        //List result = null;
        FilialDB db = new FilialDao();
        List result = db.pesquisaJuridicaFilial(juridica.getId());
        return result;
    }

    public void excluirFilial() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        FilialDB db = new FilialDao();
        filial = db.pesquisaFilialPertencente(juridica.getId(), filial.getFilial().getId());
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.deletarObjeto(filial)) {
            salvarAcumuladoDB.comitarTransacao();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
        }
        filial = new Filial();
    }

    public List getPesquisaFilial() {
        //List result = null;
        FilialDB db = new FilialDao();
        List result = db.pesquisaFilial(descPesquisa, porPesquisa, comoPesquisa, juridica.getId());
        return result;
    }

    public boolean getHabilitarComboBoxFilial() {
        if (juridica.getPessoa().getId() == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getColocarMascara() {
        int i = Integer.parseInt(getListaTipoDocumento().get(idTipoDocumento).getDescription());
        // 1 cpf, 2 cnpj, 3 cei, 4 nenhum
        if (i == 1) {
            mask = "999.999.999-99";
        }
        if (i == 2) {
            mask = "99.999.999/9999-99";
        }
        if (i == 3) {
            //mask = "99.999.99999/99";
            mask = "";
        }
        if (i == 4) {
            mask = "";
        }
        return mask;
    }

    public List<SelectItem> getListaTipoDocumento() {
        if (listaTipoDocumento.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<TipoDocumento> list = (List<TipoDocumento>) salvarAcumuladoDB.listaObjeto("TipoDocumento");
            for (int i = 0; i < list.size(); i++) {
                listaTipoDocumento.add(new SelectItem(new Integer(i), (String) (list.get(i)).getDescricao(), Integer.toString((list.get(i)).getId())));
            }
        }
        return listaTipoDocumento;
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

    public CnaeConvencao retornarCnaeConvencao() {
        CnaeConvencaoDB dbCnae = new CnaeConvencaoDBToplink();
        if (juridica.getCnae() != null && juridica.getCnae().getId() != -1) {
            cnaeConvencao = dbCnae.pesquisaCnaeComConvencao(juridica.getCnae().getId());
        } else {
            cnaeConvencao = new CnaeConvencao();
        }
        return cnaeConvencao;
    }

    public void btnExcluirContabilidadePertencente() {
//        if (juridica.getId() != -1) {
//            //chkEndContabilidade = false;
//            juridica.setContabilidade(null);
//            juridica.setEmailEscritorio(false);
//            juridica.setCobrancaEscritorio(false);
//            
//            //salvarEndereco();
//        } else {
//            juridica.setContabilidade(null);
//            juridica.setEmailEscritorio(false);
//            juridica.setCobrancaEscritorio(false);
//            listaEnd.set(1, listaEnd.get(0));
//        }

        juridica.setContabilidade(null);
        juridica.setEmailEscritorio(false);
        juridica.setCobrancaEscritorio(false);

        if (!listaEnd.isEmpty()) {
            PessoaEndereco pe = (PessoaEndereco) listaEnd.get(1);
            pe.setComplemento(((PessoaEndereco) listaEnd.get(0)).getComplemento());
            pe.setEndereco(((PessoaEndereco) listaEnd.get(0)).getEndereco());
            pe.setNumero(((PessoaEndereco) listaEnd.get(0)).getNumero());
            listaEnd.set(1, pe);
        }
    }

    public List<SelectItem> getListaMotivoInativacao() {
        if (listaMotivoInativacao.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<MotivoInativacao> list = (List<MotivoInativacao>) salvarAcumuladoDB.listaObjeto("MotivoInativacao");
            for (int i = 0; i < list.size(); i++) {
                listaMotivoInativacao.add(new SelectItem(new Integer(i),
                        list.get(i).getDescricao(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaMotivoInativacao;
    }

    public String pesquisarPessoaJuridicaGeracaoCadastrar() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "processamentoIndividual");
        return "pessoaJuridica";
    }

    public boolean validaTipoDocumento(int idDoc, String docS) {
        // 1 cpf, 2 cnpj, 3 cei, 4 nenhum
        //String documento = "";
        String documento = docS.replace(".", "").replace("/", "").replace("-", "");

        boolean ye = false;
        if (idDoc == 1) {
            ye = ValidaDocumentos.isValidoCPF(documento);
        }
        if (idDoc == 2) {
            ye = ValidaDocumentos.isValidoCNPJ(documento);
        }
        if (idDoc == 3) {
            //ye = ValidaDocumentos.isValidoCEI(documento);
            ye = true;
        }
        if (idDoc == 4) {
            ye = true;
        }

        return ye;
    }

    public String linkDaReceita() {
        if (juridica != null) {
            int i = 0;
            String documento = "";
            String docLaco = juridica.getPessoa().getDocumento();
            if (validaTipoDocumento(2, docLaco)) {
                while (i < docLaco.length()) {
                    String as = docLaco.substring(i, i + 1);
                    if (!as.equals(".") && !as.equals("-") && !as.equals("/")) {
                        documento = documento + as;
                    }
                    i++;
                }
                Clipboard copia = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection(documento);
                copia.setContents(selection, null);
            } else {
                Clipboard copia = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection selection = new StringSelection("Ação Inválida!");
                copia.setContents(selection, null);
            }
        }
        return null;
    }

    public List<DataObject> getListaEmpresasPertencentes() {
        JuridicaDB db = new JuridicaDBToplink();
        PessoaEnderecoDB dbPe = new PessoaEnderecoDBToplink();
        PessoaEndereco pe;
        if (juridica.getId() != -1) {
            listaEmpresasPertencentes.clear();
            List listaX = db.listaContabilidadePertencente(juridica.getId());
            for (int i = 0; i < listaX.size(); i++) {
                pe = dbPe.pesquisaEndPorPessoaTipo(((Juridica) (listaX.get(i))).getPessoa().getId(), 2);
                listaEmpresasPertencentes.add(new DataObject((Juridica) (listaX.get(i)), pe));
            }
        }
        return listaEmpresasPertencentes;
    }

    public void enviarEmail() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        EnvioEmailsDB dbE = new EnvioEmailsDBToplink();
        if (juridica.getId() != -1) {
//            String msg = EnviarEmail.EnviarEmail((Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro"), juridica);
//            if (msg.equals("Enviado com Sucesso. Confira email cadastrado!")) {
//                dbE.insert(envioEmails);
//                GenericaMensagem.warn("Erro", "Enviado com Sucesso. Confira email cadastrado!");
//            }

            DaoInterface di = new Dao();
            Mail mail = new Mail();
            mail.setFiles(new ArrayList());
            mail.setEmail(
                    new Email(
                            -1,
                            DataHoje.dataHoje(),
                            DataHoje.livre(new Date(), "HH:mm"),
                            (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                            (Rotina) di.find(new Rotina(), 82),
                            null,
                            "Envio de Login e Senha",
                            "<h5><b>Login: </b> " + juridica.getPessoa().getLogin() + "</h5><br /> <h5><b>Senha: </b> " + juridica.getPessoa().getSenha() + "</h5>",
                            false,
                            false
                    )
            );
            List<Pessoa> pessoas = new ArrayList();
            pessoas.add(juridica.getPessoa());

            List<EmailPessoa> emailPessoas = new ArrayList<EmailPessoa>();
            EmailPessoa emailPessoa = new EmailPessoa();
            for (Pessoa pe : pessoas) {
                emailPessoa.setDestinatario(pe.getEmail1());
                emailPessoa.setPessoa(pe);
                emailPessoa.setRecebimento(null);
                emailPessoas.add(emailPessoa);
                mail.setEmailPessoas(emailPessoas);
                emailPessoa = new EmailPessoa();
            }

            String[] retorno = mail.send("personalizado");

            if (!retorno[1].isEmpty()) {
                msgConfirma = retorno[1];
                GenericaMensagem.error("Erro", msgConfirma);
            } else {
                msgConfirma = retorno[0];
                GenericaMensagem.info("Sucesso", msgConfirma);
            }

        } else {
            GenericaMensagem.warn("Erro", "Pesquisar uma Empresa para envio!");
        }
    }

    public String enviarEmailParaTodos() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        JuridicaDB juridicaDB = new JuridicaDBToplink();
        List<Juridica> juridicas = juridicaDB.pesquisaJuridicaComEmail();
        Registro reg = (Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro");
        msgConfirma = EnviarEmail.EnviarEmailAutomatico(reg, juridicas);
        return null;
    }

    public boolean isRenEnviarEmail() {
        if (juridica.getId() == 1) {
            return false;
        } else {
            return true;
        }
    }

    public void gerarLoginSenhaPessoa(Pessoa pessoa, SalvarAcumuladoDB dbSalvar) {
        String login = "", senha = "", nome = "";
        senha = senha + DataHoje.hora().replace(":", "");
        senha = Integer.toString(Integer.parseInt(senha) + Integer.parseInt(senha + "43"));
        senha = senha.substring(senha.length() - 6, senha.length());
        nome = AnaliseString.removerAcentos(pessoa.getNome().replace(" ", "X").toUpperCase());
        nome = nome.replace("-", "Y");
        nome = nome.replace(".", "W");
        nome = nome.replace("/", "Z");
        nome = nome.replace("A", "Q");
        nome = nome.replace("E", "R");
        nome = nome.replace("I", "H");
        nome = nome.replace("O", "P");
        nome = nome.replace("U", "M");
        nome = ("JHSRGDQ" + nome) + pessoa.getId();
        login = nome.substring(nome.length() - 6, nome.length());
        pessoa.setLogin(login);
        pessoa.setSenha(senha);
        dbSalvar.alterarObjeto(pessoa);
    }

    public void atualizaEnvioEmails() {
        if (juridica.getId() != -1) {
            envioEmails = new EnvioEmails();
            envioEmails.setEmail(juridica.getPessoa().getEmail1());
            envioEmails.setHistorico("Envio de Login e senha para Contribuinte.");
            envioEmails.setOperacao("LOGIN");
            envioEmails.setPessoa(juridica.getPessoa());
            envioEmails.setDtEnvio(DataHoje.dataHoje());
            carregaEnvios = true;
            listEn = new ArrayList();
        }
    }

    public String bloqueioContribuicao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", juridica.getPessoa());
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).bloqueioServicos();
    }

    public String extratoTela() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", juridica.getPessoa());
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).extratoTela();
    }

    public String extratoTelaListaContabil(Juridica j) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", j.getPessoa());
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).extratoTela();
    }

    public String retornaDaInativacao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return "pessoaJuridica";
    }

    public void setFilialSwap(Filial filialSwap) {
        this.filialSwap = filialSwap;
    }

    public Filial getFilialSwap() {
        return filialSwap;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilialMatriz(String filialMatriz) {
        this.filialMatriz = filialMatriz;
    }

    public String getFilialMatriz() {
        return filialMatriz;
    }

    public void setComoPesquisaCnae(String comoPesquisaCnae) {
        this.comoPesquisaCnae = comoPesquisaCnae;
    }

    public String getComoPesquisaCnae() {
        return comoPesquisaCnae;
    }

    public void setPorPesquisaCnae(String porPesquisaCnae) {
        this.porPesquisaCnae = porPesquisaCnae;
    }

    public String getPorPesquisaCnae() {
        return porPesquisaCnae;
    }

    public void setDescPesquisaCnae(String descPesquisaCnae) {
        this.descPesquisaCnae = descPesquisaCnae;
    }

    public String getDescPesquisaCnae() {
        return descPesquisaCnae;
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

    public void setEnderecoDeletado(String enderecoDeletado) {
        this.enderecoDeletado = enderecoDeletado;
    }

    public String getEnderecoDeletado() {
        return enderecoDeletado;
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

    public void setIndicaTab(int indicaTab) {
        this.indicaTab = indicaTab;
    }

    public int getIndicaTab() {
        return indicaTab;
    }

    public Juridica getJuridica() {
        if (juridica.getFantasia().isEmpty() || juridica.getFantasia() == null) {
            juridica.setFantasia(juridica.getPessoa().getNome());
        }
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public String getStrGrupoCidade() {
        ConvencaoCidadeDB dbCon = new ConvencaoCidadeDBToplink();
        if (convencao.getId() != -1 && !listaEnd.isEmpty()) {
            gruCids = dbCon.pesquisaGrupoCidadeJuridica(convencao.getId(), ((PessoaEndereco) listaEnd.get(3)).getEndereco().getCidade().getId());
            if (gruCids != null) {
                strGrupoCidade = gruCids.getDescricao();
            } else {
                strGrupoCidade = "";
                gruCids = new GrupoCidade();
            }
        } else {
            strGrupoCidade = "";
            gruCids = new GrupoCidade();
        }
        return strGrupoCidade;
    }

    public void setStrGrupoCidade(String strGrupoCidade) {
        this.strGrupoCidade = strGrupoCidade;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public boolean getMarcar() {
        return marcar;
    }

    public void setMarcar(boolean marcar) {
        this.marcar = marcar;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public int getIdMotivoInativacao() {
        return idMotivoInativacao;
    }

    public void setIdMotivoInativacao(int idMotivoInativacao) {
        this.idMotivoInativacao = idMotivoInativacao;
    }

    public Convencao getConvencao() {
        return convencao;
    }

    public void setConvencao(Convencao convencao) {
        this.convencao = convencao;
    }

    public CnaeConvencao getCnaeConvencao() {
        return cnaeConvencao;
    }

    public void setCnaeConvencao(CnaeConvencao cnaeConvencao) {
        this.cnaeConvencao = cnaeConvencao;
    }

    public String getCnaeContribuinte() {
        return cnaeContribuinte;
    }

    public void setCnaeContribuinte(String cnaeContribuinte) {
        this.cnaeContribuinte = cnaeContribuinte;
    }

    public String abrirPDFConvencao() {
//        ImprimirBoleto imp = new ImprimirBoleto();
//        ConvencaoCidade conv = new ConvencaoCidade();
        ConvencaoCidadeDB db = new ConvencaoCidadeDBToplink();
        ConvencaoCidade conv = db.pesquisarConvencao(convencao.getId(), gruCids.getId());
        try {
            if (conv != null) {
                if (conv.getCaminho() != null || !conv.getCaminho().isEmpty()) {
                    FacesContext context = FacesContext.getCurrentInstance();
                    String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/convencao/" + conv.getCaminho() + ".pdf");
                    File fl = new File(caminho);
                    if (fl.exists()) {

                        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                        response.sendRedirect("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/convencao/" + conv.getCaminho() + ".pdf");
                        //imp.visualizar(fl);
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public String getStrCnaeConvencao() {
        //CnaeConvencao cc = new CnaeConvencao();
        CnaeConvencao cc = retornarCnaeConvencao();
        if (cc != null) {
            strCnaeConvencao = cc.getConvencao().getDescricao();
            convencao = cc.getConvencao();
        } else {
            strCnaeConvencao = "";
            convencao = new Convencao();
        }
        return strCnaeConvencao;
    }

    public void setStrCnaeConvencao(String strCnaeConvencao) {
        this.strCnaeConvencao = strCnaeConvencao;
    }

    public ContribuintesInativos getContribuintesInativos() {
        return contribuintesInativos;
    }

    public void setContribuintesInativos(ContribuintesInativos contribuintesInativos) {
        this.contribuintesInativos = contribuintesInativos;
    }

    public String getStrSimpleEndereco() {
        if (juridica.getId() == -1) {
            strSimpleEndereco = "Adicionar Endereço";
        } else {
            strSimpleEndereco = "Mais Endereço";
        }
        return strSimpleEndereco;
    }

    public void setStrSimpleEndereco(String strSimpleEndereco) {
        this.strSimpleEndereco = strSimpleEndereco;
    }

    public boolean getRenNovoEndereco() {
        return renNovoEndereco;
    }

    public void setRenNovoEndereco(boolean renNovoEndereco) {
        this.renNovoEndereco = renNovoEndereco;
    }

    public boolean getRenEndereco() {
        return renEndereco;
    }

    public void setRenEndereco(boolean renEndereco) {
        this.renEndereco = renEndereco;
    }

//    public boolean getChkEndContabilidade() {
//        return chkEndContabilidade;
//    }
//
//    public void setChkEndContabilidade(boolean chkEndContabilidade) {
//        this.chkEndContabilidade = chkEndContabilidade;
//    }
//
//    public String getRenChkEndereco() {
//        if (renChkEndereco.equals("false")) {
//            chkEndContabilidade = false;
//        }
//        return renChkEndereco;
//    }
    public void setRenChkEndereco(String renChkEndereco) {
        this.renChkEndereco = renChkEndereco;
    }

    public String getColorContri() {
        return colorContri;
    }

    public void setColorContri(String colorContri) {
        this.colorContri = colorContri;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public void setHabServContabil(boolean habServContabil) {
        this.habServContabil = habServContabil;
    }

    public void setListaTipoDocumento(List<SelectItem> listaTipoDocumento) {
        this.setListaTipoDocumento(listaTipoDocumento);
    }

    public EnvioEmails getEnvioEmails() {
        return envioEmails;
    }

    public void setEnvioEmails(EnvioEmails envioEmails) {
        this.envioEmails = envioEmails;
    }

    public String getAtualiza() {
        if (atualiza.isEmpty()) {
            atualiza = "Agora ";
        } else {
            atualiza += "Funcionou!!";
        }
        return atualiza;
    }

    public void setAtualiza(String atualiza) {
        this.atualiza = atualiza;
    }

//    public List<DataObject> getListaJuridicax() {
//        if (listaJuridica.isEmpty()) {
//            JuridicaDB db = new JuridicaDBToplink();
//            //List<Juridica> lista = new ArrayList();
//            List<Juridica> lista = db.pesquisaPessoa(descPesquisa, porPesquisa, comoPesquisa);
//
//            for (int i = 0; i < lista.size(); i++) {
//                List listax = db.listaJuridicaContribuinte(lista.get(i).getId());
//                String status;
//                if (listax.isEmpty()) {
//                    status = "NÃO CONTRIBUINTE";
//                } else {
//                    if (((List) listax.get(0)).get(11) != null) {
//                        status = "CONTRIBUINTE INATIVO";
//                    } else {
//                        status = "ATIVO";
//                    }
//                }
//                listaJuridica.add(new DataObject(lista.get(i), status));
//            }
//
//        }
//        return listaJuridica;
//    }
//    
//    public void setListaJuridica(List<DataObject> listaJuridica) {
//        this.listaJuridica = listaJuridica;
//    }
    public void loadList() {
        JuridicaDB db = new JuridicaDBToplink();
        boolean somenteContabilidadesx = false;
        boolean somenteContribuintesAtivos = false;
        switch (tipoFiltro) {
            case "escritorios":
                somenteContabilidadesx = true;
                break;
            case "contribuintes_ativos":
                somenteContribuintesAtivos = true;
                break;
        }
        listaJuridica = db.pesquisaPessoa(descPesquisa.trim(), porPesquisa, comoPesquisa, somenteContabilidadesx, somenteContribuintesAtivos);
    }

    public List<Juridica> getListaJuridica() {
        return listaJuridica;
    }

    public String status(Juridica j) {
        String status;
        JuridicaDB db = new JuridicaDBToplink();
        List listax = db.listaJuridicaContribuinte(j.getId());
        if (listax.isEmpty()) {
            status = "NÃO CONTRIBUINTE";
        } else {
            if (((List) listax.get(0)).get(11) != null) {
                status = "CONTRIBUINTE INATIVO";
            } else {
                status = "ATIVO";
            }
        }
        return status;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getIdIndexCnae() {
        return idIndexCnae;
    }

    public void setIdIndexCnae(int idIndexCnae) {
        this.idIndexCnae = idIndexCnae;
    }

    public int getIdIndexEndereco() {
        return idIndexEndereco;
    }

    public void setIdIndexEndereco(int idIndexEndereco) {
        this.idIndexEndereco = idIndexEndereco;
    }

    public int getIdIndexInativacao() {
        return idIndexInativacao;
    }

    public void setIdIndexInativacao(int idIndexInativacao) {
        this.idIndexInativacao = idIndexInativacao;
    }

    public boolean isRenderAtivoInativo() {
        return renderAtivoInativo;
    }

    public void setRenderAtivoInativo(boolean renderAtivoInativo) {
        this.renderAtivoInativo = renderAtivoInativo;
    }

//    public Juridica getContabilidade() {
//        return contabilidade;
//    }
//
//    public void setContabilidade(Juridica contabilidade) {
//        this.contabilidade = contabilidade;
//    }
    public int getIdIndexContabilidade() {
        return idIndexContabilidade;
    }

    public void setIdIndexContabilidade(int idIndexContabilidade) {
        this.idIndexContabilidade = idIndexContabilidade;
    }

    public List<Juridica> getListaContabilidade() {
        return listaContabilidade;
    }

    public void setListaContabilidade(List<Juridica> listaContabilidade) {
        this.listaContabilidade = listaContabilidade;
    }

    public int getIdIndexPertencente() {
        return idIndexPertencente;
    }

    public void setIdIndexPertencente(int idIndexPertencente) {
        this.idIndexPertencente = idIndexPertencente;
    }

    public String getStrArquivo() {
        //ConvencaoCidade conv = new ConvencaoCidade();
        ConvencaoCidadeDB db = new ConvencaoCidadeDBToplink();
        ConvencaoCidade conv = db.pesquisarConvencao(convencao.getId(), gruCids.getId());
        if (!strGrupoCidade.isEmpty()) {
            if (conv != null) {
                FacesContext context = FacesContext.getCurrentInstance();
                String caminho = ((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/convencao/" + conv.getCaminho() + ".pdf");
                File fl = new File(caminho);
                if (fl.exists()) {
                    strArquivo = "true";
                } else {
                    strArquivo = "false";
                }
            } else {
                strArquivo = "false";
            }
        } else {
            strArquivo = "false";
        }
        return strArquivo;
    }

    public void setStrArquivo(String strArquivo) {
        this.strArquivo = strArquivo;
    }

    public String getMsgDocumento() {
        return msgDocumento;
    }

    public void setMsgDocumento(String msgDocumento) {
        this.msgDocumento = msgDocumento;
    }

    public String getMaskCnae() {
        if (porPesquisaCnae.equals("cnae")) {
            maskCnae = "cnae";
        } else {
            maskCnae = "";
        }
        return maskCnae;
    }

    public void setMaskCnae(String maskCnae) {
        this.maskCnae = maskCnae;
    }

    public int getIdPorte() {
        return idPorte;
    }

    public void setIdPorte(int idPorte) {
        this.idPorte = idPorte;
    }

    public List<SelectItem> getListaPorte() {
        if (listaPorte.isEmpty()) {
            List<Porte> select = new SalvarAcumuladoDBToplink().listaObjeto("Porte");
            for (int i = 0; i < select.size(); i++) {
                listaPorte.add(new SelectItem(new Integer(i),
                        select.get(i).getDescricao(),
                        Integer.toString(select.get(i).getId())));
            }
        }
        return listaPorte;
    }

    public void setListaPorte(List<SelectItem> listaPorte) {
        this.listaPorte = listaPorte;
    }

    public String limparCampoPesquisa() {
        setDescPesquisa("");
        return null;
    }

    public void limparCnae() {
        //if (juridica.getId() != -1) {
        juridica.setCnae(null);
        //}
    }

    public String getMascaraPesquisaJuridica() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }

    public void existeOposicaoEmpresa() {
        if (juridica.getId() != -1) {
            OposicaoDao odbt = new OposicaoDao();
            listaOposicao = odbt.listaOposicaoEmpresaID(juridica.getId());
        } else {
            listaOposicao = new ArrayList();
        }
    }

    public void listenerPessoaJuridia() {
        GenericaSessao.remove("oposicaoPesquisa");
        OposicaoBean oposicaoBean = new OposicaoBean();
        oposicaoBean.setPorPesquisa("cnpj");
        oposicaoBean.setComoPesquisa("Inicial");
        oposicaoBean.setDescricaoPesquisa(juridica.getPessoa().getDocumento());
        oposicaoBean.setListaOposicaos(new ArrayList());
        oposicaoBean.getListaOposicaos();
        GenericaSessao.put("oposicaoBean", oposicaoBean);
    }

    public String getNomeContabilidade() {
        if (juridica.getContabilidade() == null) {
            nomeContabilidade = "";
        }
        return nomeContabilidade;
    }

    public void setNomeContabilidade(String nomeContabilidade) {
        this.nomeContabilidade = nomeContabilidade;
    }

    public String getNomePesquisaContabilidade() {
        return nomePesquisaContabilidade;
    }

    public void setNomePesquisaContabilidade(String nomePesquisaContabilidade) {
        this.nomePesquisaContabilidade = nomePesquisaContabilidade;
    }

    public List<Oposicao> getListaOposicao() {
        return listaOposicao;
    }

    public void setListaOposicao(List<Oposicao> listaOposicao) {
        this.listaOposicao = listaOposicao;
    }

    public boolean isSomenteAtivas() {
        return somenteAtivas;
    }

    public void setSomenteAtivas(boolean somenteAtivas) {
        this.somenteAtivas = somenteAtivas;
    }

    public boolean isSomenteContabilidades() {
        return somenteContabilidades;
    }

    public void setSomenteContabilidades(boolean somenteContabilidades) {
        this.somenteContabilidades = somenteContabilidades;
    }

    public void pesquisaTodosEAtivos() {
        GenericaSessao.remove("juridicaBean");
        JuridicaBean juridicaBean = new JuridicaBean();
        juridicaBean.setSomenteAtivas(true);
        juridicaBean.getDisabled();
        Boolean[] bs = new Boolean[3];
        bs[0] = false;
        bs[1] = true;
        bs[2] = false;
        juridicaBean.setDisabled(bs);
        juridicaBean.setTipoFiltro("contribuintes_ativos");
        juridicaBean.loadList();
        GenericaSessao.put("juridicaBean", juridicaBean);
        GenericaSessao.put("tipoPesquisaPessoaJuridica", "todosecontribuintes");
    }

    public void pesquisaSomenteAtivos() {
        GenericaSessao.remove("juridicaBean");
        JuridicaBean juridicaBean = new JuridicaBean();
        juridicaBean.setSomenteAtivas(true);
        Boolean[] bs = new Boolean[3];
        bs[0] = false;
        bs[1] = true;
        bs[2] = true;
        juridicaBean.setDisabled(bs);
        juridicaBean.setTipoFiltro("contribuintes_ativos");
        juridicaBean.loadList();
        GenericaSessao.put("juridicaBean", juridicaBean);
        GenericaSessao.put("tipoPesquisaPessoaJuridica", "contribuintes");
    }

    public void pesquisaSomenteContabilidades() {
        GenericaSessao.remove("juridicaBean");
        JuridicaBean juridicaBean = new JuridicaBean();
        juridicaBean.setSomenteAtivas(true);
        Boolean[] bs = new Boolean[3];
        bs[0] = true;
        bs[1] = false;
        bs[2] = true;
        juridicaBean.setDisabled(bs);
        juridicaBean.setTipoFiltro("escritorios");
        juridicaBean.loadList();
        GenericaSessao.put("juridicaBean", juridicaBean);
        GenericaSessao.put("tipoPesquisaPessoaJuridica", "escritorios");
    }

    public String getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(String tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public Boolean[] getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean[] disabled) {
        this.disabled = disabled;
    }

    public List<RepisMovimento> getListRepisMovimento() {
        if (listRepisMovimento.isEmpty()) {
            WebREPISDB webREPISDB = new WebREPISDBToplink();
            listRepisMovimento = webREPISDB.listRepisPorPessoa(juridica.getPessoa().getId());
        }
        return listRepisMovimento;
    }

    public void setListRepisMovimento(List<RepisMovimento> listRepisMovimento) {
        this.listRepisMovimento = listRepisMovimento;
    }

    public List<ListaSociosEmpresa> getListSocios() {
        if (listSocios.isEmpty()) {
            if (juridica.getId() != -1) {
                SociosDao sociosDao = new SociosDao();
                List list = sociosDao.pesquisaSocioPorEmpresa(juridica.getId());
                for (int i = 0; i < list.size(); i++) {
                    Integer matricula = Integer.parseInt(AnaliseString.converteNullString(((List) list.get(i)).get(1)));
                    Date filiacao = null;
                    if (!((List) list.get(i)).get(3).toString().isEmpty()) {
                        filiacao = (Date) ((List) list.get(i)).get(3);
                    }
                    Date admissao = null;
                    if (!AnaliseString.converteNullString(((List) list.get(i)).get(4)).isEmpty()) {
                        admissao = (Date) ((List) list.get(i)).get(4);
                    }
                    Boolean desconto_folha = false;
                    if (!AnaliseString.converteNullString(((List) list.get(i)).get(5)).isEmpty()) {
                        desconto_folha = (Boolean) ((List) list.get(i)).get(5);
                    }
                    listSocios.add(
                            new ListaSociosEmpresa(
                                    AnaliseString.converteNullString(((List) list.get(i)).get(0)),
                                    matricula,
                                    AnaliseString.converteNullString(((List) list.get(i)).get(2)),
                                    filiacao,
                                    admissao,
                                    desconto_folha
                            ));
                }
            }
        }
        return listSocios;
    }

    public void setListSocios(List<ListaSociosEmpresa> listSocios) {
        this.listSocios = listSocios;
    }

    public ConfiguracaoCnpj getConfiguracaoCnpj() {
        return configuracaoCnpj;
    }

    public void setConfiguracaoCnpj(ConfiguracaoCnpj configuracaoCnpj) {
        this.configuracaoCnpj = configuracaoCnpj;
    }

    public void setListMalaDireta(List<MalaDireta> listMalaDireta) {
        this.listMalaDireta = listMalaDireta;
    }

    public List<SelectItem> getListMalaDiretaGrupo() {
        if (habilitaMalaDireta) {
            if (listMalaDiretaGrupo.isEmpty()) {
                idMalaDiretaGrupo = null;
                List<MalaDiretaGrupo> listMDG = new Dao().list(new MalaDiretaGrupo(), true);
                int x = 0;
                for (int i = 0; i < listMDG.size(); i++) {
                    if (listMDG.get(i).getAtivo()) {
                        if (x == 0) {
                            idMalaDiretaGrupo = "" + listMDG.get(i).getId();
                        }
                        listMalaDiretaGrupo.add(new SelectItem(listMDG.get(i).getId(), listMDG.get(i).getDescricao()));
                        x++;
                    }
                }
            }
        }
        return listMalaDiretaGrupo;
    }

    public void setListMalaDiretaGrupo(List<SelectItem> listMalaDiretaGrupo) {
        this.listMalaDiretaGrupo = listMalaDiretaGrupo;
    }

    public Boolean getHabilitaMalaDireta() {
        if (this.habilitaMalaDireta) {
            getListMalaDiretaGrupo();
        } else {
            listMalaDiretaGrupo.clear();
            idMalaDiretaGrupo = null;
        }
        return habilitaMalaDireta;
    }

    public void setHabilitaMalaDireta(Boolean habilitaMalaDireta) {
        this.habilitaMalaDireta = habilitaMalaDireta;
        if (this.habilitaMalaDireta) {
            getListMalaDiretaGrupo();
        } else {
            listMalaDiretaGrupo.clear();
            idMalaDiretaGrupo = null;
        }
    }

    public String getIdMalaDiretaGrupo() {
        return idMalaDiretaGrupo;
    }

    public void setIdMalaDiretaGrupo(String idMalaDiretaGrupo) {
        this.idMalaDiretaGrupo = idMalaDiretaGrupo;
    }

    public void saveMalaDireta() {
        if (juridica.getId() != -1) {
            Dao dao = new Dao();
            MalaDireta md = new MalaDiretaDao().findByPessoa(juridica.getPessoa().getId());
            if (idMalaDiretaGrupo != null) {
                if (habilitaMalaDireta) {
                    MalaDiretaGrupo mdg = (MalaDiretaGrupo) dao.find(new MalaDiretaGrupo(), Integer.parseInt(idMalaDiretaGrupo));
                    if (md == null) {
                        md = new MalaDireta();
                        md.setMalaDiretaGrupo(mdg);
                        md.setPessoa(juridica.getPessoa());
                        if (dao.save(md, true)) {
                            GenericaMensagem.info("Sucesso", "Registro atualizado!");
                            return;
                        }
                    } else {
                        md.setMalaDiretaGrupo(mdg);
                        if (dao.update(md, true)) {
                            GenericaMensagem.info("Sucesso", "Registro atualizado!");
                            return;
                        }
                    }
                } else {
                    if (md != null) {
                        if (dao.delete(md, true)) {
                            idMalaDiretaGrupo = null;
                            listMalaDiretaGrupo.clear();
                            GenericaMensagem.info("Sucesso", "Registro atualizado!");
                            return;
                        }
                    }
                }
            } else {
                if (md != null) {
                    if (dao.delete(md, true)) {
                        idMalaDiretaGrupo = null;
                        listMalaDiretaGrupo.clear();
                        GenericaMensagem.info("Sucesso", "Registro atualizado!");
                        return;
                    }
                }
            }
        }
        GenericaMensagem.warn("Erro", "Ao atualizar registro!");
    }

    public void loadMalaDireta() {
        habilitaMalaDireta = false;
        MalaDireta md = new MalaDiretaDao().findByPessoa(juridica.getPessoa().getId());
        if (md != null) {
            habilitaMalaDireta = true;
            for (int i = 0; i < getListMalaDiretaGrupo().size(); i++) {
                MalaDiretaGrupo mdg = (MalaDiretaGrupo) new Dao().find(new MalaDiretaGrupo(), getListMalaDiretaGrupo().get(i).getValue());
                if (mdg.getAtivo()) {
                    if (md.getMalaDiretaGrupo().getId().equals(mdg.getId())) {
                        idMalaDiretaGrupo = "" + mdg.getId();
                        break;
                    }
                }
            }
        }
        if (idMalaDiretaGrupo == null) {
            habilitaMalaDireta = false;
        }
    }
}

// ANTIGA PESQUISA POR WEBSERVICE KNU -- depois da data 01/12/2014 excluir esse comentário
//            try {
//                SSLUtil.acceptSSL();
//                URL url = new URL("https://c.knu.com.br/webservice");
//                URLConnection conn = url.openConnection();
//                conn.setDoOutput(true);
//                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//                wr.write(
//                        "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
//                        + "<dados>\n"
//                        + "    <usuario>lucasprogramatecno@gmail.com</usuario>\n"
//                        + "    <senha>lucasjava13</senha>\n"
//                        + "    <funcao>receitaCNPJ</funcao>\n"
//                        + "    <param>" + AnaliseString.extrairNumeros(juridica.getPessoa().getDocumento()) + "</param>\n"
//                        + "    <retorno>1</retorno>\n"
//                        + "</dados>"
//                );
//
//                wr.flush();
//                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    if (line.contains("<atividade_principal>")) {
//                        //juridica.getPessoa().setNome(line.substring(22, line.indexOf("</atividade_principal>")));
//                        //juridica.setFantasia(juridica.getPessoa().getNome());
//                    }
//                    if (line.contains("<nome_empresarial>")) {
//                        juridica.getPessoa().setNome(line.substring(20, line.indexOf("</nome_empresarial>")));
//                        juridica.setFantasia(juridica.getPessoa().getNome());
//                    }
//                }
//
//                wr.close();
//                rd.close();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//
//    public void pesquisaCnpj() {
//        if (juridica.getId() != -1) {
//            return;
//        }
//
//        if (juridica.getPessoa().getDocumento().isEmpty()) {
//            return;
//        }
//
//        String documento = AnaliseString.extrairNumeros(juridica.getPessoa().getDocumento());
//
//        if (!validaTipoDocumento(2, documento)) {
//            msgDocumento = "Documento inválido!";
//            GenericaMensagem.warn("Atenção", "Documento Inválido!");
//            return;
//        }
//        JuridicaDB dbj = new JuridicaDBToplink();
//        List listDocumento = dbj.pesquisaJuridicaPorDoc(juridica.getPessoa().getDocumento());
//        for (int i = 0; i < listDocumento.size(); i++) {
//            if (!listDocumento.isEmpty()) {
//                GenericaMensagem.warn("Atenção", "Empresa já esta cadastrada no Sistema!");
//                return;
//            }
//        }
//
//        PessoaDB db = new PessoaDBToplink();
//
//        juridicaReceita = db.pesquisaJuridicaReceita(documento);
//        if (juridicaReceita.getPessoa() != null && juridicaReceita.getPessoa().getId() != -1) {
//            GenericaMensagem.warn("Erro", "Pessoa já cadastrada no Sistema!");
//            return;
//        }
//        if (juridicaReceita.getId() == -1) {
//
//            try {
//                System.loadLibrary("knu");
//            } catch (Exception e) {
//                System.out.println(e.getMessage() + " Erro Carregar Lib ");
//                GenericaMensagem.warn("Erro", "Consulta temporarimente indisponível!");
//                return;
//            } catch (UnsatisfiedLinkError e) {
//                System.out.println(e.getMessage() + " Erro Carregar Lib ");
//                GenericaMensagem.warn("Erro", "Consulta temporarimente indisponível!");
//                return;
//            }
//
//            knu.ReceitaCNPJ resultado = knu.knu.receitaCNPJ(documento);
//
//            if (resultado.getCod_erro() != 0) {
//                GenericaMensagem.warn("Falha na Busca", resultado.getDesc_erro());
//                return;
//            }
//
//            if (resultado.getNome_empresarial().isEmpty()) {
//                GenericaMensagem.warn("Erro", "Erro ao pesquisar na Receita!");
//                return;
//            }
//
//            if (resultado.getSituacao_cadastral().equals("BAIXADA")) {
//                GenericaMensagem.warn("Erro", "Erro ao pesquisar na Receita!");
//                return;
//            }
//
//            juridicaReceita.setNome(resultado.getNome_empresarial());
//            juridicaReceita.setFantasia(resultado.getNome_empresarial());
//            juridicaReceita.setDocumento(documento);
//            juridicaReceita.setCep(resultado.getCep());
//            juridicaReceita.setDescricaoEndereco(resultado.getLogradouro());
//            juridicaReceita.setBairro(resultado.getBairro());
//            juridicaReceita.setComplemento(resultado.getComplemento());
//            juridicaReceita.setNumero(resultado.getNumero());
//            juridicaReceita.setCnae(resultado.getAtividade_principal());
//            juridicaReceita.setPessoa(null);
//            juridicaReceita.setStatus(resultado.getSituacao_cadastral());
//            juridicaReceita.setDtAbertura(DataHoje.converte(resultado.getData_abertura()));
//
//            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//
//            sv.abrirTransacao();
//
//            if (!sv.inserirObjeto(juridicaReceita)) {
//                GenericaMensagem.warn("Erro", "Erro ao Salvar pesquisa!");
//                sv.desfazerTransacao();
//                return;
//            }
//
//            sv.comitarTransacao();
//
//            juridica.getPessoa().setNome(juridicaReceita.getNome());
//            juridica.setFantasia(juridicaReceita.getNome());
//
//            String result[] = juridicaReceita.getCnae().split(" ");
//            CnaeDB dbc = new CnaeDBToplink();
//
//            List<Cnae> listac = dbc.pesquisaCnae(result[0], "cnae", "I");
//
//            if (listac.isEmpty()) {
//                GenericaMensagem.warn("Erro", "Erro ao pesquisar CNAE");
//                return;
//            }
//            retornaCnaeReceita(listac.get(0));
//
//            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
//
//            String cep = juridicaReceita.getCep();
//            cep = cep.replace(".", "").replace("-", "");
//
//            String descricao[] = AnaliseString.removerAcentos(resultado.getLogradouro()).split(" ");
//            String bairros[] = AnaliseString.removerAcentos(resultado.getBairro()).split(" ");
//
//            endereco = dbe.enderecoReceita(cep, descricao, bairros);
//
//            if (endereco != null) {
//                TipoEnderecoDB dbt = new TipoEnderecoDBToplink();
//                List tiposE = dbt.listaTipoEnderecoParaJuridica();
//                for (int i = 0; i < tiposE.size(); i++) {
//                    pessoaEndereco.setEndereco(endereco);
//                    pessoaEndereco.setTipoEndereco((TipoEndereco) tiposE.get(i));
//                    pessoaEndereco.setPessoa(juridica.getPessoa());
//                    pessoaEndereco.setNumero(juridicaReceita.getNumero());
//                    pessoaEndereco.setComplemento(juridicaReceita.getComplemento());
//                    listaEnd.add(pessoaEndereco);
//
//                    pessoaEndereco = new PessoaEndereco();
//                }
//            } else {
//                String msg = "Endereço não encontrado no Sistema - CEP: " + resultado.getCep() + " DESC: " + resultado.getLogradouro() + " BAIRRO: " + resultado.getBairro();
//                GenericaMensagem.warn("Erro", msg);
//            }
//        } else {
//            juridica.getPessoa().setNome(juridicaReceita.getNome());
//            juridica.setFantasia(juridicaReceita.getNome());
//
//            String result[] = juridicaReceita.getCnae().split(" ");
//            CnaeDB dbc = new CnaeDBToplink();
//
//            List<Cnae> listac = dbc.pesquisaCnae(result[0], "cnae", "I");
//
//            if (listac.isEmpty()) {
//                GenericaMensagem.warn("Erro", "Erro ao pesquisar CNAE");
//                return;
//            }
//            retornaCnaeReceita(listac.get(0));
//
//            if (juridicaReceita.getStatus().equals("BAIXADA")) {
//                GenericaMensagem.warn("Erro", "Esta empresa esta INATIVA na receita!");
//            }
//
//            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
//
//            String cep = juridicaReceita.getCep();
//            cep = cep.replace(".", "").replace("-", "");
//
//            String descricao[] = AnaliseString.removerAcentos(juridicaReceita.getDescricaoEndereco()).split(" ");
//            String bairros[] = AnaliseString.removerAcentos(juridicaReceita.getBairro()).split(" ");
//
//            endereco = dbe.enderecoReceita(cep, descricao, bairros);
//
//            if (endereco != null) {
//                TipoEnderecoDB dbt = new TipoEnderecoDBToplink();
//                List tiposE = dbt.listaTipoEnderecoParaJuridica();
//                for (Object tiposE1 : tiposE) {
//                    pessoaEndereco.setEndereco(endereco);
//                    pessoaEndereco.setTipoEndereco((TipoEndereco) tiposE1);
//                    pessoaEndereco.setPessoa(juridica.getPessoa());
//                    pessoaEndereco.setNumero(juridicaReceita.getNumero());
//                    pessoaEndereco.setComplemento(juridicaReceita.getComplemento());
//                    listaEnd.add(pessoaEndereco);
//                    pessoaEndereco = new PessoaEndereco();
//                }
//            } else {
//                String msg = "Endereço não encontrado no Sistema - CEP: " + juridicaReceita.getCep() + " DESC: " + juridicaReceita.getDescricaoEndereco() + " BAIRRO: " + juridicaReceita.getBairro();
//                GenericaMensagem.warn("Erro", msg);
//            }
//        }
//    }
