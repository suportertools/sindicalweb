package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.arrecadacao.Empregados;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
import br.com.rtools.endereco.Bairro;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.DescricaoEndereco;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.Logradouro;
import br.com.rtools.endereco.db.EnderecoDao;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.JuridicaReceita;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.Porte;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.CnaeDB;
import br.com.rtools.pessoa.db.CnaeDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.pessoa.db.TipoEnderecoDB;
import br.com.rtools.pessoa.db.TipoEnderecoDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import br.com.rtools.sistema.ConfiguracaoCnpj;
import br.com.rtools.sistema.Email;
import br.com.rtools.sistema.EmailPessoa;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mail;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.SelectTranslate;
import br.com.rtools.utilitarios.ValidaDocumentos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

@ManagedBean
@SessionScoped
public class ControleAcessoWebBean implements Serializable {

    private Registro registro = new Registro();
    private Pessoa pessoa = new Pessoa();
    private Pessoa pessoaContribuinte = null;
    private Pessoa pessoaContabilidade = null;
    private Pessoa pessoaPatronal = null;
    private Juridica empresa = new Juridica();
    private String login = "";
    private String status = "";
    private String strTipoPesquisa = "cnpj";
    private String descPesquisa = "";
    private String msgEmail = "";
    private String msgNovaSenha = "";
    private String verificaSenha = "";
    private String comoPesquisa = "";
    private String novaSenha = "";
    private String confirmaSenha = "";
    private String email = "";
    private String msgLoginInvalido = "";
    private boolean renEsqueci = false;
    private List listaEmpresas = new ArrayList();
    private int idJuridica = 0;
    private String link = "";
    private boolean tipoLink = false;

    private String documento = "";
    private Empregados empregados = new Empregados();

    public void validaEmpregados() {
        if (pessoaContribuinte.getEmail1().isEmpty()) {
            GenericaMensagem.warn("Atenção", "Informe o EMAIL da Empresa para entrar no Sistema!");
            return;
        }

        if (pessoaContribuinte.getTelefone1().isEmpty()) {
            GenericaMensagem.warn("Atenção", "Informe o TELEFONE da Empresa para entrar no Sistema!");
            return;
        }

        PF.openDialog("dlg_empregados_confirma");
        PF.update("i_panel_quantidade");
    }

    public Pessoa getPessoaContribuinte() {
        return pessoaContribuinte;
    }

    public void setPessoaContribuinte(Pessoa pessoaContribuinte) {
        this.pessoaContribuinte = pessoaContribuinte;
    }

    public Pessoa getPessoaContabilidade() {
        return pessoaContabilidade;
    }

    public void setPessoaContabilidade(Pessoa pessoaContabilidade) {
        this.pessoaContabilidade = pessoaContabilidade;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

//    public JuridicaReceita pesquisaNaReceita(String documentox) {
//        PessoaDB db = new PessoaDBToplink();
//        JuridicaReceita jr = db.pesquisaJuridicaReceita(documentox);
//
//        if (jr.getId() == -1) {
//            try {
//                System.loadLibrary("knu");
//            } catch (Exception | UnsatisfiedLinkError e) {
//                System.out.println(e.getMessage() + " Erro Carregar Lib ");
//                GenericaMensagem.warn("Erro", "Consulta temporarimente indisponível!");
//                return null;
//            }
//
//            // knu.ReceitaCNPJ resultado = knu.knu.receitaCNPJ(documentox);
//            knu.ReceitaCNPJ resultado = knu.knu.receitaCNPJ(documentox);
//
//            if (resultado.getCod_erro() != 0) {
//                GenericaMensagem.warn("Falha na Busca", resultado.getDesc_erro());
//                return null;
//            }
//
//            if (resultado.getNome_empresarial().isEmpty()) {
//                GenericaMensagem.warn("Erro", "Erro ao pesquisar na Receita!");
//                return null;
//            }
//
//            if (resultado.getSituacao_cadastral().equals("BAIXADA")) {
//                GenericaMensagem.warn("Erro", "Erro ao pesquisar na Receita!");
//                return null;
//            }
//
//            jr.setNome(resultado.getNome_empresarial());
//            jr.setFantasia(resultado.getNome_empresarial());
//            jr.setDocumento(documentox);
//            jr.setCep(resultado.getCep());
//            jr.setDescricaoEndereco(resultado.getLogradouro());
//            jr.setBairro(resultado.getBairro());
//            jr.setComplemento(resultado.getComplemento());
//            jr.setNumero(resultado.getNumero());
//            jr.setCnae(resultado.getAtividade_principal());
//            jr.setPessoa(null);
//            jr.setStatus(resultado.getSituacao_cadastral());
//            jr.setDtAbertura(DataHoje.converte(resultado.getData_abertura()));
//
//            Dao di = new Dao();
//            di.openTransaction();
//            if (!di.save(jr)) {
//                GenericaMensagem.warn("Erro", "Erro ao Salvar pesquisa!");
//                di.rollback();
//                return null;
//            }
//            di.commit();
//        }
//
//        Dao di = new Dao();
//        di.openTransaction();
//        if (jr.getPessoa() == null) {
//            Pessoa pessoax = new Pessoa(
//                    -1, jr.getNome(), (TipoDocumento) di.find(new TipoDocumento(), 2), "", "", DataHoje.data(), "", "", "", "", "", "", AnaliseString.mascaraCnpj(documento), "", ""
//            );
//
//            if (!di.save(pessoax)) {
//                GenericaMensagem.warn("Erro", "Erro ao Salvar pesquisa!");
//                di.rollback();
//                return null;
//            }
//            jr.setPessoa(pessoax);
//            di.update(jr);
//        }
//
//        Juridica juridica = new Juridica();
//        jr.getPessoa().setNome(jr.getPessoa().getNome().toUpperCase());
//        juridica.setPessoa(jr.getPessoa());
//        juridica.setFantasia(jr.getNome().toUpperCase());
//
//        String emails[] = (jr.getEmail() == null) ? "".split("") : jr.getEmail().toLowerCase().split(" ");
//        String telefones[] = (jr.getTelefone() == null) ? "".split("") : jr.getTelefone().split(" / ");
//        
//        JuridicaDB dbj = new JuridicaDBToplink();
//        if (!emails[0].isEmpty()){
//            juridica.setContabilidade(dbj.pesquisaContabilidadePorEmail(emails[0]));
//        }
//        
//        switch (emails.length) {
//            case 1:
//                juridica.getPessoa().setEmail1(emails[0]);
//                break;
//            case 2:
//                juridica.getPessoa().setEmail1(emails[0]);
//                juridica.getPessoa().setEmail2(emails[1]);
//                break;
//            case 3:
//                juridica.getPessoa().setEmail1(emails[0]);
//                juridica.getPessoa().setEmail2(emails[1]);
//                juridica.getPessoa().setEmail3(emails[2]);
//                break;
//        }
//        
//        switch (telefones.length) {
//            case 1:
//                juridica.getPessoa().setTelefone1(telefones[0]);
//                break;
//            case 2:
//                juridica.getPessoa().setTelefone1(telefones[0]);
//                juridica.getPessoa().setTelefone2(telefones[1]);
//                break;
//            case 3:
//                juridica.getPessoa().setTelefone1(telefones[0]);
//                juridica.getPessoa().setTelefone2(telefones[1]);
//                juridica.getPessoa().setTelefone3(telefones[2]);
//                break;
//        }
//        
//        String result[] = jr.getCnae().split(" ");
//        CnaeDB dbc = new CnaeDBToplink();
//        String cnaex = result[result.length - 1].replace("(", "").replace(")", "");
//        //List<Cnae> listac = dbc.pesquisaCnae(result[0], "cnae", "I");
//        List<Cnae> listac = dbc.pesquisaCnae(cnaex, "cnae", "I");
//        
//        if (listac.isEmpty()) {
//            GenericaMensagem.warn("Erro", "Erro ao pesquisar CNAE");
//            di.rollback();
//            return null;
//        }
//
//        CnaeConvencaoDB dbCnaeCon = new CnaeConvencaoDBToplink();
//        if (dbCnaeCon.pesquisaCnaeComConvencao(((Cnae) listac.get(0)).getId()) != null) {
//            juridica.setCnae((Cnae) listac.get(0));
//            // CNAE CONTRIBUINTE
//        } else {
//            // CNAE NÃO ESTA NA CONVENCAO
//            GenericaMensagem.warn("Atenção", "Empresa não pertence a esta entidade!");
//            di.rollback();
//            return null;
//        }
//
//        PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
//
//        String cep = jr.getCep();
//        cep = cep.replace(".", "").replace("-", "");
//
//        String descricao[] = AnaliseString.removerAcentos(jr.getDescricaoEndereco()).split(" ");
//        String bairros[] = AnaliseString.removerAcentos(jr.getBairro()).split(" ");
//
//        Endereco endereco = dbe.enderecoReceita(cep, descricao, bairros);
//        List<PessoaEndereco> listape = new ArrayList();
//        
//        if (endereco == null){
//            SelectTranslate st = new SelectTranslate();
//            
//            List<Bairro> lbairro = st.select(new Bairro()).where("ds_descricao", jr.getBairro()).find();
//            Bairro bx;
//            
//            if (lbairro.isEmpty()){
//                bx = new Bairro(-1, jr.getBairro(), false);
//                
//                if (!di.save(bx)){
//                    di.rollback();
//                    GenericaMensagem.error("Erro", "Não foi possível salvar o Bairro, tente novamente!");
//                    return null;
//                }
//            }else{
//                bx = lbairro.get(0);
//            }
//            
//            List<DescricaoEndereco> ldescricao = st.select(new DescricaoEndereco()).where("ds_descricao", jr.getDescricaoEndereco()).find();
//            DescricaoEndereco dex;
//            
//            if (ldescricao.isEmpty()){
//                dex = new DescricaoEndereco(-1, jr.getDescricaoEndereco(), false);
//                
//                if (!di.save(dex)){
//                    di.rollback();
//                    GenericaMensagem.error("Erro", "Não foi possível salvar o Descrição, tente novamente!");
//                    return null;
//                }
//            }else{
//                dex = ldescricao.get(0);
//            }
//            EnderecoDB dbx = new EnderecoDao();
//            List<Endereco> le = dbx.pesquisaEnderecoCep(cep);
//
//            Endereco ex = new Endereco(-1, le.get(0).getCidade(), bx, le.get(0).getLogradouro(), dex, cep, "", false);   
//
//            if (!di.save(ex)){
//                di.rollback();
//                GenericaMensagem.error("Erro", "Não foi possível salvar o Endereço, tente novamente!");
//                return null;
//            }
//        }
//        
//        if (endereco != null) {
//            TipoEnderecoDB dbt = new TipoEnderecoDBToplink();
//            List tiposE = dbt.listaTipoEnderecoParaJuridica();
//            for (int i = 0; i < tiposE.size(); i++) {
//                PessoaEndereco pessoaEndereco = new PessoaEndereco();
//                pessoaEndereco.setEndereco(endereco);
//                pessoaEndereco.setTipoEndereco((TipoEndereco) tiposE.get(i));
//                pessoaEndereco.setPessoa(juridica.getPessoa());
//                pessoaEndereco.setNumero(jr.getNumero());
//                pessoaEndereco.setComplemento(jr.getComplemento());
//                listape.add(pessoaEndereco);
//
//            }
//        } else {
//            String msg = "Endereço não encontrado no Sistema - CEP: " + jr.getCep() + " DESC: " + jr.getDescricaoEndereco() + " BAIRRO: " + jr.getBairro();
//            GenericaMensagem.warn("Erro", msg);
//            
//        }
//
//        juridica.setPorte((Porte) di.find(new Porte(), 1));
//        if (!di.save(juridica)) {
//            GenericaMensagem.warn("Erro", "Não foi possível salvar EMPRESA, tente novamente!");
//            di.rollback();
//            return null;
//        }
//
//        for (PessoaEndereco listapex : listape) {
//            if (!di.save(listapex)) {
//                GenericaMensagem.warn("Erro", "Não foi possível salvar ENDEREÇO, tente novamente!");
//                di.rollback();
//                return null;
//            }
//        }
//
//        di.commit();
//        return jr;
//    }
    public JuridicaReceita pesquisaNaReceitaWeb(String documentox) {
        PessoaDB db = new PessoaDBToplink();
        JuridicaReceita jr = db.pesquisaJuridicaReceita(documentox);
        Dao dao = new Dao();
        if (jr.getId() == -1) {
            try {
                ConfiguracaoCnpj configuracaoCnpj = (ConfiguracaoCnpj) dao.find(new ConfiguracaoCnpj(), 1);
                URL url = null;
                if (configuracaoCnpj == null) {
                    url = new URL("https://wooki.com.br/api/v1/cnpj/receitafederal?numero=" + documentox + "&dias=" + configuracaoCnpj.getDias() + "&usuario=rogerio@rtools.com.br&senha=989899");
                } else {
                    if (configuracaoCnpj.getEmail().isEmpty() || configuracaoCnpj.getSenha().isEmpty()) {
                        url = new URL("https://wooki.com.br/api/v1/cnpj/receitafederal?numero=" + documentox + "&dias=" + configuracaoCnpj.getDias() + "&usuario=rogerio@rtools.com.br&senha=989899");
                    } else {
                        url = new URL("https://wooki.com.br/api/v1/cnpj/receitafederal?numero=" + documentox + "&dias=" + configuracaoCnpj.getDias() + "&usuario=" + configuracaoCnpj.getEmail() + "&senha=" + configuracaoCnpj.getSenha());
                    }
                }

                //URL url = new URL("https://wooki.com.br/api/v1/cnpj/receitafederal?numero="+documentox+"&usuario=rogerio@rtools.com.br&senha=989899");
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
                        return null;
                    }

                    if (status == 1) {
                        GenericaMensagem.info("Atenção", "Atualizando esse CNPJ na receita, pesquise novamente em 30 segundos!");
                        return null;
                    }

                    if (status != 0) {
                        GenericaMensagem.error("Erro", error);
                        return null;
                    }

                    jr.setNome(obj.getString("nome_empresarial"));
                    jr.setFantasia(obj.getString("titulo_estabelecimento"));
                    jr.setDocumento(documentox);
                    jr.setCep(AnaliseString.mascaraCep(obj.getString("cep")));
                    jr.setDescricaoEndereco(obj.getString("logradouro"));
                    jr.setBairro(obj.getString("bairro"));
                    jr.setComplemento(obj.getString("complemento"));
                    jr.setNumero(obj.getString("numero"));
                    jr.setCnae(obj.getString("atividade_principal"));
                    jr.setPessoa(null);
                    jr.setStatus(obj.getString("situacao_cadastral"));
                    jr.setDtAbertura(DataHoje.converte(obj.getString("data_abertura")));
                    jr.setCnaeSegundario(obj.getString("atividades_secundarias"));
                    jr.setCidade(obj.getString("municipio"));
                    jr.setUf(obj.getString("uf"));
                    jr.setEmail(obj.getString("email_rf"));
                    jr.setTelefone(obj.getString("telefone_rf"));

                    Dao di = new Dao();
                    di.openTransaction();
                    if (!di.save(jr)) {
                        GenericaMensagem.warn("Erro", "Erro ao Salvar pesquisa!");
                        di.rollback();
                        return null;
                    }
                    di.commit();
                }
            } catch (IOException | JSONException e) {
                GenericaMensagem.warn("Erro", e.getMessage());
                return null;
            }
        }

        Dao di = new Dao();
        di.openTransaction();
        if (jr.getPessoa() == null) {
            Pessoa pessoax = new Pessoa(
                    -1, jr.getNome(), (TipoDocumento) di.find(new TipoDocumento(), 2), "", "", DataHoje.data(), "", "", "", "", "", "", AnaliseString.mascaraCnpj(documentox), "", ""
            );

            if (!di.save(pessoax)) {
                GenericaMensagem.warn("Erro", "Erro ao Salvar pesquisa!");
                di.rollback();
                return null;
            }
            jr.setPessoa(pessoax);
            di.update(jr);
        }

        Juridica juridica = new Juridica();
        jr.getPessoa().setNome(jr.getPessoa().getNome().toUpperCase());
        juridica.setPessoa(jr.getPessoa());
        juridica.setFantasia(jr.getNome().toUpperCase());

        String emails[] = (jr.getEmail() == null) ? "".split("") : jr.getEmail().toLowerCase().split(" ");
        String telefones[] = (jr.getTelefone() == null) ? "".split("") : jr.getTelefone().split(" / ");

        JuridicaDB dbj = new JuridicaDBToplink();
        if (!emails[0].isEmpty()) {
            juridica.setContabilidade(dbj.pesquisaContabilidadePorEmail(emails[0]));
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

        String result[] = jr.getCnae().split(" ");
        CnaeDB dbc = new CnaeDBToplink();
        String cnaex = result[result.length - 1].replace("(", "").replace(")", "");
        //List<Cnae> listac = dbc.pesquisaCnae(result[0], "cnae", "I");
        List<Cnae> listac = dbc.pesquisaCnae(cnaex, "cnae", "I");

        if (listac.isEmpty()) {
            listac = dbc.pesquisaCnae(result[0], "cnae", "I");
            if (listac.isEmpty()) {
                GenericaMensagem.warn("Erro", "Erro ao pesquisar CNAE");
                di.rollback();
                return null;
            }
        }

        CnaeConvencaoDB dbCnaeCon = new CnaeConvencaoDBToplink();
        if (dbCnaeCon.pesquisaCnaeComConvencao(((Cnae) listac.get(0)).getId()) != null) {
            juridica.setCnae((Cnae) listac.get(0));
            // CNAE CONTRIBUINTE
        } else {
            // CNAE NÃO ESTA NA CONVENCAO
            if (((Cnae) listac.get(0)).getId() == 1) {
                GenericaMensagem.warn("Atenção", "CONTABILIDADE, entre com o CNPJ da Empresa!");
            } else {
                GenericaMensagem.warn("Atenção", "Empresa não pertence a esta entidade!");

            }
            di.rollback();
            return null;
        }

        PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();

        String cep = jr.getCep();
        cep = cep.replace(".", "").replace("-", "");

        String descricao[] = AnaliseString.removerAcentos(jr.getDescricaoEndereco()).split(" ");
        String bairros[] = AnaliseString.removerAcentos(jr.getBairro()).split(" ");

        Endereco endereco = dbe.enderecoReceita(cep, descricao, bairros);
        List<PessoaEndereco> listape = new ArrayList();

        if (endereco == null) {
            SelectTranslate st = new SelectTranslate();

            List<Bairro> lbairro = st.select(new Bairro()).where("ds_descricao", jr.getBairro()).find();
            Bairro bx;

            if (lbairro.isEmpty()) {
                bx = new Bairro(-1, jr.getBairro(), false);

                if (!di.save(bx)) {
                    di.rollback();
                    GenericaMensagem.error("Erro", "Não foi possível salvar o Bairro, tente novamente!");
                    return null;
                }
            } else {
                bx = lbairro.get(0);
            }

            List<DescricaoEndereco> ldescricao = st.select(new DescricaoEndereco()).where("ds_descricao", jr.getDescricaoEndereco()).find();
            DescricaoEndereco dex;

            if (ldescricao.isEmpty()) {
                dex = new DescricaoEndereco(-1, jr.getDescricaoEndereco(), false);

                if (!di.save(dex)) {
                    di.rollback();
                    GenericaMensagem.error("Erro", "Não foi possível salvar o Descrição, tente novamente!");
                    return null;
                }
            } else {
                dex = ldescricao.get(0);
            }

            List<Cidade> lcidade = st.select(new Cidade()).where("ds_cidade", jr.getCidade()).find();
            Cidade cx;

            if (lcidade.isEmpty()) {
                cx = new Cidade(-1, jr.getCidade(), jr.getUf());

                if (!di.save(cx)) {
                    di.rollback();
                    GenericaMensagem.error("Erro", "Não foi possível salvar a Cidade, tente novamente!");
                    return null;
                }
            } else {
                cx = lcidade.get(0);
            }

            EnderecoDao dbx = new EnderecoDao();
            List<Endereco> le = dbx.pesquisaEnderecoCep(cep);

            if (le.isEmpty()) {
                endereco = new Endereco(-1, cx, bx, (Logradouro) di.find(new Logradouro(), 0), dex, cep, "", false);
//                di.rollback();
//                GenericaMensagem.error("Erro", "CEP não encontrado no sistema, contate seu Sindicato!");
//                return null;
            } else {
                endereco = new Endereco(-1, le.get(0).getCidade(), bx, le.get(0).getLogradouro(), dex, cep, "", false);
            }

            if (!di.save(endereco)) {
                di.rollback();
                GenericaMensagem.error("Erro", "Não foi possível salvar o Endereço, tente novamente!");
                return null;
            }
        }

        if (endereco != null) {
            TipoEnderecoDB dbt = new TipoEnderecoDBToplink();
            List tiposE = dbt.listaTipoEnderecoParaJuridica();
            for (int i = 0; i < tiposE.size(); i++) {
                PessoaEndereco pessoaEndereco = new PessoaEndereco();
                pessoaEndereco.setEndereco(endereco);
                pessoaEndereco.setTipoEndereco((TipoEndereco) tiposE.get(i));
                pessoaEndereco.setPessoa(juridica.getPessoa());
                pessoaEndereco.setNumero(jr.getNumero());
                pessoaEndereco.setComplemento(jr.getComplemento());
                listape.add(pessoaEndereco);

            }
        } else {
            String msg = "Endereço não encontrado no Sistema - CEP: " + jr.getCep() + " DESC: " + jr.getDescricaoEndereco() + " BAIRRO: " + jr.getBairro();
            GenericaMensagem.warn("Erro", msg);

        }

        juridica.setPorte((Porte) di.find(new Porte(), 1));
        if (!di.save(juridica)) {
            GenericaMensagem.warn("Erro", "Não foi possível salvar EMPRESA, tente novamente!");
            di.rollback();
            return null;
        }

        for (PessoaEndereco listapex : listape) {
            if (!di.save(listapex)) {
                GenericaMensagem.warn("Erro", "Não foi possível salvar ENDEREÇO, tente novamente!");
                di.rollback();
                return null;
            }
        }

        di.commit();
        return jr;
    }

    public String salvarEmpregados() {
        Dao di = new Dao();

        di.openTransaction();
        JuridicaDB db = new JuridicaDBToplink();

        empregados.setJuridica(db.pesquisaJuridicaPorPessoa(pessoaContribuinte.getId()));
        empregados.setReferencia(DataHoje.data().substring(3));

        if (!di.save(empregados)) {
            GenericaMensagem.error("Erro", "Não foi possível salvar quantidade, tente novamente!");
            di.rollback();
            return null;
        }

        if (!di.update(pessoaContribuinte)) {
            GenericaMensagem.error("Erro", "Não foi possível alterar cadastro, tente novamente!");
            di.rollback();
            return null;
        }

        di.commit();

        status = "Contribuinte";
        login = pessoaContribuinte.getNome() + " - " + pessoaContribuinte.getTipoDocumento().getDescricao() + " : " + pessoaContribuinte.getDocumento() + " ( " + status + " )";

        GenericaSessao.put("sessaoUsuarioAcessoWeb", pessoaContribuinte);
        GenericaSessao.put("linkClicado", true);
        GenericaSessao.put("userName", pessoaContribuinte.getDocumento());
        GenericaSessao.put("indicaAcesso", "web");
        return "menuPrincipalAcessoWeb";
    }

    public String validacaoDocumento() throws IOException {
        if (documento.isEmpty()) {
            GenericaMensagem.error("Login Inválido", "Digite um CNPJ válido!");
            return null;
        }

        String documentox = AnaliseString.extrairNumeros(documento);

        if (!ValidaDocumentos.isValidoCNPJ(documentox)) {
            GenericaMensagem.warn("Erro", "Documento Inválido!");
            return null;
        }

        JuridicaDB db = new JuridicaDBToplink();
        List<Juridica> listDocumento = db.pesquisaJuridicaPorDoc(documento);

        if (!listDocumento.isEmpty() && listDocumento.size() > 1) {
            GenericaMensagem.warn("Atenção", "Documento Inválido, contate seu Sindicato!");
            return null;
        }

        Juridica juridica = null;
        UsuarioDB dbu = new UsuarioDBToplink();

        // SE TER CADASTRO NO SISTEMA
        if (!listDocumento.isEmpty()) {
            juridica = listDocumento.get(0);
        } else {
            // SE NÃO TER CADASTRO NO SISTEMA
            JuridicaReceita jr = pesquisaNaReceitaWeb(documentox);
            if (jr == null) {
                return null;
            }

            juridica = db.pesquisaJuridicaPorPessoa(jr.getPessoa().getId());
        }

        pessoaPatronal = dbu.ValidaUsuarioPatronalWeb(juridica.getPessoa().getId());

        if (pessoaPatronal != null) {
            GenericaMensagem.info("Confirmação de Acesso PATRONAL", "Confirme seu Login e Senha!");
            PF.openDialog("dlg_patronal");
            return null;
        }

        pessoaContribuinte = dbu.ValidaUsuarioContribuinteWeb(juridica.getPessoa().getId());
        pessoaContabilidade = dbu.ValidaUsuarioContabilidadeWeb(juridica.getPessoa().getId());

        if (pessoaContribuinte == null && pessoaContabilidade != null) {
            GenericaMensagem.warn("Atenção", "CONTABILIDADE, entre com o CNPJ da Empresa!");
            return null;
        }

//        if (pessoaContribuinte == null){
        //            GenericaMensagem.warn("Atenção", "Empresa não contribuinte, contate seu Sindicato!");
        //            return null;
        //        }
        List<Vector> listax = db.listaJuridicaContribuinte(juridica.getId());

        if (!listax.isEmpty()) {
            // 11 - DATA DE INATIVACAO
            if (listax.get(0).get(11) != null) {
                GenericaMensagem.warn("Atenção", "Empresa Inativa, contate seu Sindicato!");
                return null;
            }
        } else {
            GenericaMensagem.warn("Atenção", "Empresa não contribuinte, contate seu Sindicato!");
            return null;
        }

        empregados = db.pesquisaEmpregados(juridica.getId());

        if (empregados == null) {
            empregados = new Empregados();
            PF.openDialog("dlg_empregados");
            return null;
        }

        status = "Contribuinte";
        login = pessoaContribuinte.getNome() + " - " + pessoaContribuinte.getTipoDocumento().getDescricao() + " : " + pessoaContribuinte.getDocumento() + " ( " + status + " )";

        GenericaSessao.put("sessaoUsuarioAcessoWeb", pessoaContribuinte);
        GenericaSessao.put("linkClicado", true);
        GenericaSessao.put("userName", pessoaContribuinte.getDocumento());
        GenericaSessao.put("indicaAcesso", "web");
        return "menuPrincipalAcessoWeb";
    }

    public String validacao() throws IOException {
        if (pessoa.getLogin().isEmpty()) {
            GenericaMensagem.error("Login Inválido", "Digite um LOGIN válido!");
            return null;
        }

        if (pessoa.getSenha().isEmpty()) {
            GenericaMensagem.error("Login Inválido", "Digite uma SENHA válida!");
            return null;
        }
        String pagina = null;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("indicaAcesso", "web");
        UsuarioDB db = new UsuarioDBToplink();
        pessoa = db.ValidaUsuarioWeb(pessoa.getLogin(), pessoa.getSenha());
        if (pessoa != null) {
            pessoaContribuinte = db.ValidaUsuarioContribuinteWeb(pessoa.getId());
            pessoaContabilidade = db.ValidaUsuarioContabilidadeWeb(pessoa.getId());
            if (pessoaContribuinte != null && pessoaContabilidade == null) {
                JuridicaDB dbj = new JuridicaDBToplink();
                List listax = dbj.listaJuridicaContribuinte(dbj.pesquisaJuridicaPorPessoa(pessoaContribuinte.getId()).getId());
                if (listax.isEmpty()) {
                    //msgLoginInvalido = "Usuário não contribuinte!";
                    GenericaMensagem.error("Login Inválido", "Usuário não Contribuinte");
                    return null;
                } else {
                    if (((List) listax.get(0)).get(11) != null) {
                        //msgLoginInvalido = "Contribuinte inativo, contate seu sindicato!";
                        GenericaMensagem.error("Login Inválido", "Contribuinte inativo, contate seu Sindicato!");
                        return null;
                    } else {
                    }
                }
            }
            pessoaPatronal = db.ValidaUsuarioPatronalWeb(pessoa.getId());
        } else {
            pessoaContribuinte = null;
            pessoaContabilidade = null;
            pessoaPatronal = null;
        }

        if ((pessoaContribuinte != null) && (pessoaContabilidade != null) && pessoaPatronal == null) {
            status = "Contribuinte - Contabilidade";
        } else if (pessoaContribuinte != null && pessoaPatronal == null) {
            status = "Contribuinte";
        } else if (pessoaContabilidade != null && pessoaPatronal == null) {
            status = "Contabilidade";
        } else if (pessoaPatronal != null) {
            status = "Patronal";
        }

        if ((pessoaContribuinte != null) || (pessoaContabilidade != null) || (pessoaPatronal != null)) {
            pagina = "menuPrincipalAcessoWeb";
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessaoUsuarioAcessoWeb", pessoa);
            if (pessoa.getTipoDocumento().getId() == 4) {
                login = pessoa.getNome() + " ( " + status + " )";
            } else {
                login = pessoa.getNome() + " - "
                        + pessoa.getTipoDocumento().getDescricao() + ": "
                        + pessoa.getDocumento() + " ( "
                        + status + " )";
            }
            pessoa = new Pessoa();
//           pessoaContribuinte = new Pessoa();
//           pessoaContabilidade = new Pessoa();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        } else {
            if (pessoa == null) {
                //msgLoginInvalido = "Usuário ou/e senha inválidos!";
                GenericaMensagem.error("Login Inválido", "Usuário ou/e senha Inválidos!");
            } else {
                //msgLoginInvalido = "Usuário não Contribuinte, ou Contabilidade sem Empresa!";
                GenericaMensagem.error("Login Inválido", "Usuário não Contribuinte, ou Contabilidade sem Empresa!");
            }
            pessoa = new Pessoa();
//           pessoaContribuinte = new Pessoa();
//           pessoaContabilidade = new Pessoa();
        }
        if (pessoa != null) {
            GenericaSessao.put("userName", pessoa.getLogin());
        }
        return pagina;
    }

    public void enviarEmail() {
        if (email.isEmpty()) {
            GenericaMensagem.warn("Erro", "Digite um Email para verificação!");
            return;
        }
        Juridica empresax = new Juridica();

        if (strTipoPesquisa.equals("nome")) {
            if (descPesquisa.isEmpty()) {
                GenericaMensagem.warn("Atenção", "Digite o nome da Empresa para Pesquisar!");
                return;
            }

            if (getListaEmpresa().isEmpty()) {
                GenericaMensagem.warn("Atenção", "Pesquise sua empresa para concluir a Solicitação!");
                return;
            }

            empresax = (Juridica) new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.parseInt(getListaEmpresa().get(idJuridica).getDescription()), "Juridica");
            if (empresax == null) {
                GenericaMensagem.warn("Não foi possível completar seu pedido", "Empresa não encontrada no Sistema, Contate o seu Sindicato.");
                return;
            }
        } else {
            if (descPesquisa.isEmpty()) {
                GenericaMensagem.warn("Atenção", "Digite o documento da Empresa!");
                return;
            }

            JuridicaDB db = new JuridicaDBToplink();
            List<Juridica> lista = db.pesquisaJuridicaPorDoc(descPesquisa);

            if (lista.isEmpty()) {
                GenericaMensagem.warn("Não foi possível completar seu pedido", "Empresa não encontrada no Sistema, Contate o seu Sindicato.");
                return;
            }
            empresax = (Juridica) lista.get(0);
        }

        if (!validaEmail(empresax)) {
            GenericaMensagem.warn("Não foi possível completar seu pedido", "E-mail digitado é Inválido!");
            return;
        }

        DaoInterface di = new Dao();
        Mail mail = new Mail();
        mail.setFiles(new ArrayList());
        mail.setEmail(
                new Email(
                        -1,
                        DataHoje.dataHoje(),
                        DataHoje.livre(new Date(), "HH:mm"),
                        (Usuario) GenericaSessao.getObject("sessaoUsuario"),
                        (Rotina) di.find(new Rotina(), 261),
                        null,
                        "Envio de Login e Senha",
                        "<h5><b>Login: </b> " + empresax.getPessoa().getLogin() + "</h5><br /> <h5><b>Senha: </b> " + empresax.getPessoa().getSenha() + "</h5>",
                        false,
                        false
                )
        );
        List<Pessoa> pessoas = new ArrayList();
        pessoas.add(empresax.getPessoa());

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
            GenericaMensagem.error("Erro", retorno[1]);
        } else {
            GenericaMensagem.info("Confirmação", "Seu LOGIN e SENHA foram enviados para o email cadastrado!");
        }
    }

    public boolean validaEmail(Juridica emp) {
        if (emp.getPessoa().getEmail1() != null) {
            if (emp.getPessoa().getEmail1().equals(email)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public List<SelectItem> getListaEmpresa() {
        if (listaEmpresas.isEmpty()) {
            int i = 0;
            JuridicaDB db = new JuridicaDBToplink();
            List select = db.pesquisaPessoa(descPesquisa, "nome", comoPesquisa);
            while (i < select.size()) {
                listaEmpresas.add(new SelectItem(new Integer(i),
                        (String) ((Juridica) select.get(i)).getPessoa().getNome() + " - "
                        + (String) ((Juridica) select.get(i)).getPessoa().getTipoDocumento().getDescricao() + ": "
                        + (String) ((Juridica) select.get(i)).getPessoa().getDocumento(),
                        Integer.toString(((Juridica) select.get(i)).getId())));
                i++;
            }
        }
        return listaEmpresas;
    }

    public String salvarConf() {
        PessoaDB db = new PessoaDBToplink();
        if (!novaSenha.equals("")) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb");
            if (pessoa.getId() != -1) {
                if (verificaSenha.equals(pessoa.getSenha()) && confirmaSenha.equals(novaSenha)) {
                    pessoa.setSenha(novaSenha);
                    db.getEntityManager().getTransaction().begin();
                    if (db.update(pessoa)) {
                        msgNovaSenha = "Atualizado com sucesso!";
                        db.getEntityManager().getTransaction().commit();
                    } else {
                        msgNovaSenha = "Erro ao Atualizar Senha!";
                        db.getEntityManager().getTransaction().rollback();
                    }
                } else {
                    msgNovaSenha = "Senha de verificação inválida!";
                }
            }
        } else {
            msgNovaSenha = "Digite uma Senha para ser atualizado!";
        }
        pessoa = new Pessoa();
        return null;
    }

    public void inicial() {
        comoPesquisa = "I";
        listaEmpresas = new ArrayList();
    }

    public void parcial() {
        comoPesquisa = "P";
        listaEmpresas = new ArrayList();
    }

    public String esqueciASenha() {
        if (renEsqueci) {
            pessoa = new Pessoa();
            pessoaContribuinte = null;
            pessoaContabilidade = null;
            empresa = new Juridica();
            login = "";
            status = "";
            strTipoPesquisa = "cnpj";
            descPesquisa = "";
            msgEmail = "";
            msgNovaSenha = "";
            verificaSenha = "";
            comoPesquisa = "";
            msgNovaSenha = "";
            email = "";
            listaEmpresas = new ArrayList();
            idJuridica = 0;
            renEsqueci = false;
        } else {
            renEsqueci = true;
        }
        return "indexAcessoWeb";
    }

    public String getLinkSite() {
        PessoaDB db = new PessoaDBToplink();
        Pessoa p = db.pesquisaCodigo(1);
        if (p != null) {
            if (p.getSite() != null) {
                return p.getSite();
            }
        }
        return "";
    }

    public void sairSistemaWeb() throws IOException {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb") != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoUsuarioAcessoWeb");
        }
        String retorno = "";
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente") != null) {
            //retorno = "indexAcessoWeb.jsf?cliente=" + (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente");
            retorno = "web/" + (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente");
        }
        limparSessaoAcessoWeb();
        FacesContext.getCurrentInstance().getExternalContext().redirect(retorno);
    }

    public void limparSessaoAcessoWeb() {
        FacesContext conext = FacesContext.getCurrentInstance();
        //Verifica a sessao e a grava na variavel
        HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
        //Fecha/Destroi sessao
        session.invalidate();
    }

    public String getStrTipoPesquisa() {
        return strTipoPesquisa;
    }

    public void setStrTipoPesquisa(String strTipoPesquisa) {
        this.strTipoPesquisa = strTipoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getMsgEmail() {
        return msgEmail;
    }

    public void setMsgEmail(String msgEmail) {
        this.msgEmail = msgEmail;
    }

    public boolean isRenEsqueci() {
        return renEsqueci;
    }

    public void setRenEsqueci(boolean renEsqueci) {
        this.renEsqueci = renEsqueci;
    }

    public int getIdJuridica() {
        return idJuridica;
    }

    public void setIdJuridica(int idJuridica) {
        this.idJuridica = idJuridica;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getMsgNovaSenha() {
        return msgNovaSenha;
    }

    public void setMsgNovaSenha(String msgNovaSenha) {
        this.msgNovaSenha = msgNovaSenha;
    }

    public String getVerificaSenha() {
        return verificaSenha;
    }

    public void setVerificaSenha(String verificaSenha) {
        this.verificaSenha = verificaSenha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public String getMsgLoginInvalido() {
        return msgLoginInvalido;
    }

    public void setMsgLoginInvalido(String msgLoginInvalido) {
        this.msgLoginInvalido = msgLoginInvalido;
    }

    public Pessoa getPessoaPatronal() {
        return pessoaPatronal;
    }

    public void setPessoaPatronal(Pessoa pessoaPatronal) {
        this.pessoaPatronal = pessoaPatronal;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean getTipoLink() {
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        //PessoaDB db = new PessoaDBToplink();
        //Pessoa p = new Pessoa();
        Pessoa p = (Pessoa) sadb.find(new Pessoa(), 1);
        //p = db.pesquisaCodigo(1);
        if (p != null) {
            tipoLink = !p.getSite().equals("");
            link = p.getSite();
        }
        return tipoLink;
    }

    public void setTipoLink(boolean tipoLink) {
        this.tipoLink = tipoLink;
    }

    public Registro getRegistro() {
        //if (registro.getId() == -1) {
        registro = (Registro) new Dao().liveSingle("SELECT r FROM Registro r WHERE r.id = 1");
        //}
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Empregados getEmpregados() {
        return empregados;
    }

    public void setEmpregados(Empregados empregados) {
        this.empregados = empregados;
    }
}
