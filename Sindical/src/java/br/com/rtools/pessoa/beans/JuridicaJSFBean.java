package br.com.rtools.pessoa.beans;

import br.com.rtools.arrecadacao.*;
import br.com.rtools.arrecadacao.db.*;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.db.EnderecoDB;
import br.com.rtools.endereco.db.EnderecoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.*;
import br.com.rtools.pessoa.db.*;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import knu.ReceitaCNPJ;
import knu.knu;

public class JuridicaJSFBean {

    private Juridica juridica = new Juridica();
    private Juridica contabilidade = new Juridica();
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private Filial filial = new Filial();
    private Filial filialSwap = new Filial();
    private ContribuintesInativos contribuintesInativos = new ContribuintesInativos();
    private Endereco endereco = new Endereco();
    private GrupoCidade gruCids = new GrupoCidade();
    private Convencao convencao = new Convencao();
    private CnaeConvencao cnaeConvencao = new CnaeConvencao();
    private EnvioEmails envioEmails = new EnvioEmails();
    private String indicaTab;
    private String enderecoCompleto;
    private String enderecoDeletado = null;
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "";
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
    private String renNovoEndereco = "false";
    private String renEndereco = "false";
    private String renAbreEnd = "true";
    private String renChkEndereco = "false";
    private String colorContri = "red";
    private String numDocumento = "";
    private String strArquivo = "";
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
    private boolean chkEndContabilidade = true;
    private List listaEnd = new ArrayList();
    private List listEn = new ArrayList();
    private List<Cnae> listaCnae = new ArrayList();
    private List<DataObject> listaJuridica = new ArrayList();
    private List<Juridica> listaContabilidade = new ArrayList();
    private List<DataObject> listaEmpresasPertencentes = new ArrayList();
    private List<SelectItem> listaTipoDocumento = new ArrayList<SelectItem>();
    private List<SelectItem> listaPorte = new ArrayList<SelectItem>();
    private List<ContribuintesInativos> listaContribuintesInativos = new ArrayList();
    private String atualiza = "";
    private JuridicaReceita juridicaReceita = new JuridicaReceita();

    public void pesquisaCnpj() {
        if (juridica.getId() != -1) {
            return;
        }

        if (juridica.getPessoa().getDocumento().isEmpty()) {
            return;
        }

        String documento = AnaliseString.extrairNumeros(juridica.getPessoa().getDocumento());

        if (!validaTipoDocumento(2, documento)) {
            msgDocumento = "Documento inválido!";
            return;
        }
        JuridicaDB dbj = new JuridicaDBToplink();
        List listDocumento = dbj.pesquisaJuridicaPorDoc(juridica.getPessoa().getDocumento());
        for (int i = 0; i < listDocumento.size(); i++) {
            if (!listDocumento.isEmpty()) {
                msgDocumento = "Empresa já esta cadastrada no Sistema!";
                return;
            }
        }

        PessoaDB db = new PessoaDBToplink();

        juridicaReceita = db.pesquisaJuridicaReceita(documento);
        if (juridicaReceita.getPessoa() != null && juridicaReceita.getPessoa().getId() != -1) {
            msgDocumento = "Pessoa já cadastrada no Sistema!";
            return;
        }
        if (juridicaReceita.getId() == -1) {
            try {
                System.loadLibrary("knu"); // PARA WINDOWS 
            } catch (Exception e) {
                System.out.println(e.getMessage() + " Erro Windows ");
                try {
                    System.loadLibrary("libknu"); // PARA LINUX
                } catch (Exception ex) {
                    System.out.println(ex.getMessage() + " Erro Linux ");
                }
            }

            ReceitaCNPJ resultado = knu.receitaCNPJ(documento);

            msgDocumento = resultado.getDesc_erro();
            if (resultado.getCod_erro() != 0) {
                return;
            } else {
                msgDocumento = "";
            }

            if (resultado.getNome_empresarial().isEmpty()) {
                msgDocumento = "Erro ao pesquisar na Receita!";
                return;
            }

            if (resultado.getSituacao_cadastral().equals("BAIXADA")) {
                msgDocumento = "Esta empresa esta INATIVA na receita!";
            }

            juridicaReceita.setNome(resultado.getNome_empresarial());
            juridicaReceita.setFantasia(resultado.getNome_empresarial());
            juridicaReceita.setDocumento(documento);
            juridicaReceita.setCep(resultado.getCep());
            juridicaReceita.setDescricaoEndereco(resultado.getLogradouro());
            juridicaReceita.setBairro(resultado.getBairro());
            juridicaReceita.setComplemento(resultado.getComplemento());
            juridicaReceita.setNumero(resultado.getNumero());
            juridicaReceita.setCnae(resultado.getAtividade_principal());
            juridicaReceita.setPessoa(null);
            juridicaReceita.setStatus(resultado.getSituacao_cadastral());

            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            sv.abrirTransacao();

            if (!sv.inserirObjeto(juridicaReceita)) {
                msgConfirma = "Erro ao Salvar pesquisa!";
                sv.desfazerTransacao();
                return;
            }
            sv.comitarTransacao();

            juridica.getPessoa().setNome(juridicaReceita.getNome());
            juridica.setFantasia(juridicaReceita.getNome());

            String result[] = juridicaReceita.getCnae().split(" ");
            CnaeDB dbc = new CnaeDBToplink();

            List<Cnae> listac = dbc.pesquisaCnae(result[0], "cnae", "I");

            if (listac.isEmpty()) {
                msgDocumento = "Erro ao pesquisar CNAE";
                return;
            }
            retornaCnaeReceita(listac.get(0));

            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();

            String cep = juridicaReceita.getCep();
            cep = cep.replace(".", "").replace("-", "");

            String descricao[] = AnaliseString.removerAcentos(resultado.getLogradouro()).split(" ");
            String bairros[] = AnaliseString.removerAcentos(resultado.getBairro()).split(" ");

            endereco = dbe.enderecoReceita(cep, descricao, bairros);

            if (endereco != null) {
                TipoEnderecoDB dbt = new TipoEnderecoDBToplink();
                List tiposE = dbt.listaTipoEnderecoParaJuridica();
                for (int i = 0; i < tiposE.size(); i++) {
                    pessoaEndereco.setEndereco(endereco);
                    pessoaEndereco.setTipoEndereco((TipoEndereco) tiposE.get(i));
                    pessoaEndereco.setPessoa(juridica.getPessoa());
                    pessoaEndereco.setNumero(juridicaReceita.getNumero());
                    pessoaEndereco.setComplemento(juridicaReceita.getComplemento());
                    listaEnd.add(pessoaEndereco);

                    pessoaEndereco = new PessoaEndereco();
                }
            } else {
                msgDocumento = "Endereço não encontrado no Sistema - CEP: " + resultado.getCep() + " DESC: " + resultado.getLogradouro() + " BAIRRO: " + resultado.getBairro();
            }
        } else {
            juridica.getPessoa().setNome(juridicaReceita.getNome());
            juridica.setFantasia(juridicaReceita.getNome());

            String result[] = juridicaReceita.getCnae().split(" ");
            CnaeDB dbc = new CnaeDBToplink();

            List<Cnae> listac = dbc.pesquisaCnae(result[0], "cnae", "I");

            if (listac.isEmpty()) {
                msgDocumento = "Erro ao pesquisar CNAE";
                return;
            }
            retornaCnaeReceita(listac.get(0));

            if (juridicaReceita.getStatus().equals("BAIXADA")) {
                msgDocumento = "Esta empresa esta INATIVA na receita!";
            }


            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();

            String cep = juridicaReceita.getCep();
            cep = cep.replace(".", "").replace("-", "");

            String descricao[] = AnaliseString.removerAcentos(juridicaReceita.getDescricaoEndereco()).split(" ");
            String bairros[] = AnaliseString.removerAcentos(juridicaReceita.getBairro()).split(" ");

            endereco = dbe.enderecoReceita(cep, descricao, bairros);

            if (endereco != null) {
                TipoEnderecoDB dbt = new TipoEnderecoDBToplink();
                List tiposE = dbt.listaTipoEnderecoParaJuridica();
                for (int i = 0; i < tiposE.size(); i++) {
                    pessoaEndereco.setEndereco(endereco);
                    pessoaEndereco.setTipoEndereco((TipoEndereco) tiposE.get(i));
                    pessoaEndereco.setPessoa(juridica.getPessoa());
                    pessoaEndereco.setNumero(juridicaReceita.getNumero());
                    pessoaEndereco.setComplemento(juridicaReceita.getComplemento());
                    listaEnd.add(pessoaEndereco);

                    pessoaEndereco = new PessoaEndereco();
                }
            } else {
                msgDocumento = "Endereço não encontrado no Sistema - CEP: " + juridicaReceita.getCep() + " DESC: " + juridicaReceita.getDescricaoEndereco() + " BAIRRO: " + juridicaReceita.getBairro();
            }
        }
    }

//    public void pesquisaCnpjXML(){
//        if (juridica.getId() != -1){
//            return;
//        }
//        
//        if (juridica.getPessoa().getDocumento().isEmpty()){
//            return;
//        }
//        
//        if (receita){
//            return;
//        }
//        
//        try{
//            SSLUtil.acceptSSL();
//            URL url = new URL("https://c.knu.com.br/webservice");  
//            URLConnection conn = url.openConnection();  
//            conn.setDoOutput(true);
//            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());  
//            wr.write(
//                    "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
//                    "<dados>\n" +
//                    "    <usuario>lucasprogramatecno@gmail.com</usuario>\n" +
//                    "    <senha>lucasjava13</senha>\n" +
//                    "    <funcao>receitaCNPJ</funcao>\n" +
//                    "    <param>"+AnaliseString.extrairNumeros(juridica.getPessoa().getDocumento())+"</param>\n" +
//                    "    <retorno>1</retorno>\n" +
//                    "</dados>"
//            );
//            
//            wr.flush();  
//            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));  
//            String line;  
//            while ((line = rd.readLine()) != null) {
//                if (line.contains("<atividade_principal>")){
//                    //juridica.getPessoa().setNome(line.substring(22, line.indexOf("</atividade_principal>")));
//                    //juridica.setFantasia(juridica.getPessoa().getNome());
//                }
//                if (line.contains("<nome_empresarial>")){
//                    juridica.getPessoa().setNome(line.substring(20, line.indexOf("</nome_empresarial>")));
//                    juridica.setFantasia(juridica.getPessoa().getNome());
//                }
//            }  
//            receita = true;
//            wr.close();  
//            rd.close();
//            }catch(Exception ex){
//                ex.printStackTrace();
//            }
//        }
    public void pesquisaDocumento() {
        JuridicaDB db = new JuridicaDBToplink();
        if (!juridica.getPessoa().getDocumento().isEmpty()) {
            List<Juridica> lista = db.pesquisaJuridicaPorDoc(juridica.getPessoa().getDocumento());
            if (lista.isEmpty()) {
                msgDocumento = "";
            } else {
                msgDocumento = "Esse documento já existe para: " + lista.get(0).getPessoa().getNome();
            }
        } else {
            msgDocumento = "";
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
            List<Vector> listax = db.listaJuridicaContribuinte(juridica.getId());

            for (int i = 0; i < listax.size(); i++) {
                if (listax.get(0).get(11) != null) {
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

    public String inativarContribuintes() {
        JuridicaDB db = new JuridicaDBToplink();
        ContribuintesInativosDB dbConIni = new ContribuintesInativosDBToplink();
        if (!getListaMotivoInativacao().isEmpty()) {
            contribuintesInativos.setJuridica(juridica);
            contribuintesInativos.setDtAtivacao(null);
            contribuintesInativos.setMotivoInativacao(db.pesquisaCodigoMotivoInativacao(Integer.parseInt(
                    ((SelectItem) getListaMotivoInativacao().get(idMotivoInativacao)).getDescription())));
            if (dbConIni.insert(contribuintesInativos)) {
                msgConfirma = "Contribuinte Inativado!";
                contribuintesInativos = new ContribuintesInativos();
                listaContribuintesInativos.clear();
                getListaContribuintesInativos();
                getContribuinte();
            }
        } else {
            msgConfirma = "Não exsiste Motivo de Inativação";
        }
        return null;
    }

    public String getEnderecoCobranca() {
        PessoaEndereco ende = null;
        String strCompl = "";
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
        }
        return listaContribuintesInativos;
    }

    public String btnExcluirMotivoInativacao() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        contribuintesInativos = (ContribuintesInativos) listaContribuintesInativos.get(idIndexInativacao);

        sv.abrirTransacao();
        if (!sv.deletarObjeto((ContribuintesInativos) sv.pesquisaCodigo(contribuintesInativos.getId(), "ContribuintesInativos"))) {
            msgConfirma = "Erro ao excluir motivo de inativação!";
            sv.desfazerTransacao();
            return null;
        } else {
            sv.comitarTransacao();
        }
        listaContribuintesInativos.clear();
        getListaContribuintesInativos();
        msgConfirma = "Motivo excluído com sucesso!";
        if (listaContribuintesInativos.isEmpty()) {
        }
        getContribuinte();
        return null;
    }

    public String reativarContribuintes() {
        retornarCnaeConvencao();
        if (cnaeConvencao == null || cnaeConvencao.getCnae().getId() == -1) {
            msgConfirma = "Cnae atual não pertence a Categoria!";
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
                msgConfirma = "Erro ao reativar empresa!";
                sv.desfazerTransacao();
                return null;
            } else {
                msgConfirma = "Contribuinte Reativado!";
                sv.comitarTransacao();
            }
            listaContribuintesInativos.clear();
            getListaContribuintesInativos();
            getContribuinte();
        }
        msgConfirma = "Salve o cadastro para efetuar a ativação";
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
        Pessoa pessoa = juridica.getPessoa();
        List listDocumento = new ArrayList();
        if (listaEnd.isEmpty() || pessoa.getId() == -1) {
            adicionarEnderecos();
        }

        juridica.setPorte((Porte) new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.parseInt(getListaPorte().get(idPorte).getDescription()), "Porte"));

        if (!chkEndContabilidade) {
            juridica.setEmailEscritorio(false);
        }
        //else
        //    juridica.setEmailEscritorio(false);

        dbSalvar.abrirTransacao();
        if (juridica.getId() == -1) {
            juridica.getPessoa().setTipoDocumento(
                    dbDoc.pesquisaCodigo(
                    Integer.parseInt(
                    ((SelectItem) getListaTipoDocumento().get(idTipoDocumento)).getDescription())));

            if (juridica.getPessoa().getNome().isEmpty()) {
                msgConfirma = "O campo nome não pode ser nulo! ";
                return null;
            }

            if (Integer.parseInt(((SelectItem) getListaTipoDocumento().get(idTipoDocumento)).getDescription()) == 4) {
                pessoa.setDocumento("0");
            } else {
                listDocumento = db.pesquisaJuridicaPorDoc(juridica.getPessoa().getDocumento());
                for (int i = 0; i < listDocumento.size(); i++) {
                    if (!listDocumento.isEmpty()) {
                        msgConfirma = "Empresa já existente no Sistema!";
                        return null;
                    }
                }
            }

            if (!validaTipoDocumento(Integer.parseInt(getListaTipoDocumento().get(idTipoDocumento).getDescription()), juridica.getPessoa().getDocumento())) {
                msgConfirma = "Documento Invalido!";
                return null;
            }

            if (listaEnd.isEmpty()) {
                msgConfirma = "Cadastro não pode ser salvo sem Endereço!";
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
                    msgConfirma = "Cadastro salvo com Sucesso!";
                    dbSalvar.comitarTransacao();
                    NovoLog novoLog = new NovoLog();
                    novoLog.novo("Salvar Pessoa Jurídica", "ID: " + juridica.getId() + " - Pessoa: " + juridica.getPessoa().getId() + " - " + juridica.getPessoa().getNome() + " - Abertura" + juridica.getAbertura() + " - Fechamento" + juridica.getAbertura() + " - I.E.: " + juridica.getInscricaoEstadual() + " - Insc. Mun.: " + juridica.getInscricaoMunicipal() + " - Responsável: " + juridica.getResponsavel());
                } else {
                    msgConfirma = "Erro ao Salvar Dados!";
                    dbSalvar.desfazerTransacao();
                    return null;
                }
            }
        } else {
            if (juridica.getPessoa().getNome().isEmpty()) {
                msgConfirma = "O campo nome não pode ser nulo! ";
                return null;
            }

            if (Integer.parseInt(((SelectItem) getListaTipoDocumento().get(idTipoDocumento)).getDescription()) == 4) {
                juridica.getPessoa().setDocumento("0");
            } else {
                listDocumento = db.pesquisaJuridicaPorDoc(juridica.getPessoa().getDocumento());
                for (int i = 0; i < listDocumento.size(); i++) {
                    if (!listDocumento.isEmpty() && ((Juridica) listDocumento.get(i)).getId() != juridica.getId()) {
                        msgConfirma = "Empresa já existente no Sistema!";
                        return null;
                    }
                }
                juridica.getPessoa().setTipoDocumento(
                        dbDoc.pesquisaCodigo(
                        Integer.parseInt(
                        ((SelectItem) getListaTipoDocumento().get(idTipoDocumento)).getDescription())));
            }
            if (!validaTipoDocumento(Integer.parseInt(getListaTipoDocumento().get(idTipoDocumento).getDescription()), juridica.getPessoa().getDocumento())) {
                msgConfirma = "Documento Invalido!";
                return null;
            }
            adicionarEnderecos();
            if ((juridica.getPessoa().getLogin()) == null && (juridica.getPessoa().getSenha()) == null) {
                gerarLoginSenhaPessoa(juridica.getPessoa(), dbSalvar);
            }
            dbSalvar.alterarObjeto(juridica.getPessoa());
            if (dbSalvar.alterarObjeto(juridica)) {
                msgConfirma = "Cadastro atualizado com Sucesso!";
                Juridica jur = (Juridica) dbSalvar.pesquisaCodigo(juridica.getId(), "Juridica");
                dbSalvar.comitarTransacao();
                String novoLogString = " de ID: " + jur.getId() + " - Pessoa: " + jur.getPessoa().getId() + " - " + jur.getPessoa().getNome() + " - Abertura: " + jur.getAbertura() + " - Fechamento: " + jur.getAbertura() + " - I.E.: " + jur.getInscricaoEstadual() + " - Insc. Mun.: " + jur.getInscricaoMunicipal() + " - Responsável: " + jur.getResponsavel()
                        + " para ID: " + juridica.getId() + " - Pessoa: " + juridica.getPessoa().getId() + " - " + juridica.getPessoa().getNome() + " - Abertura: " + juridica.getAbertura() + " - Fechamento: " + juridica.getAbertura() + " - I.E.: " + juridica.getInscricaoEstadual() + " - Insc. Mun.: " + juridica.getInscricaoMunicipal() + " - Responsável: " + juridica.getResponsavel();
                NovoLog novoLog = new NovoLog();
                novoLog.novo("Atualizar Pessoa Jurídica", novoLogString);
            } else {
                dbSalvar.desfazerTransacao();
                msgConfirma = "Erro ao atualizar Cadastro!";
            }
        }
        getContribuinte();
        salvarEndereco();

        return null;
    }

    public String novo() {
        juridica = new Juridica();
        contabilidade = new Juridica();
        pessoaEndereco = new PessoaEndereco();
        convencao = new Convencao();
        marcar = false;
        alterarEnd = false;
        renChkEndereco = "false";
        cnaeContribuinte = " sem cnae! ";
        colorContri = "red";
        renEndereco = "false";
        renNovoEndereco = "false";
        renAbreEnd = "true";
        msgDocumento = "";
        listaEnd = new ArrayList();
        idTipoDocumento = 1;
        idPorte = 0;
        listaContribuintesInativos.clear();
        setEnderecoCompleto("");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaComplementoBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoNum");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoComp");
        return "pessoaJuridica";
    }

    public void novoGenerico() {
        juridica = new Juridica();
        contabilidade = new Juridica();
        pessoaEndereco = new PessoaEndereco();
        convencao = new Convencao();
        marcar = false;
        alterarEnd = false;
        renChkEndereco = "false";
        cnaeContribuinte = " sem cnae! ";
        colorContri = "red";
        renEndereco = "false";
        renNovoEndereco = "false";
        renAbreEnd = "true";
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
            msgConfirma = "Pesquise uma empresa para ser excluída!";
            return null;
        }
        List<PessoaEndereco> listaEndereco = dbPE.pesquisaEndPorPessoa(juridica.getPessoa().getId());

        sv.abrirTransacao();
        if (!listaEndereco.isEmpty()) {
            PessoaEndereco pe = new PessoaEndereco();
            for (int i = 0; i < listaEndereco.size(); i++) {
                pe = (PessoaEndereco) sv.pesquisaCodigo(listaEndereco.get(i).getId(), "PessoaEndereco");
                if (!sv.deletarObjeto(pe)) {
                    msgConfirma = "Erro ao excluir uma pessoa Endereço!";
                    sv.desfazerTransacao();
                    return null;
                }
            }
        }

        ContribuintesInativosDB dbCI = new ContribuintesInativosDBToplink();
        List<ContribuintesInativos> listaCI = dbCI.listaContribuintesInativos(juridica.getId());

        if (!listaCI.isEmpty()) {
            ContribuintesInativos ci = new ContribuintesInativos();
            for (int i = 0; i < listaCI.size(); i++) {
                ci = (ContribuintesInativos) sv.pesquisaCodigo(listaCI.get(i).getId(), "ContribuintesInativos");
                if (!sv.deletarObjeto(ci)) {
                    msgConfirma = "Erro ao excluir Contribuintes Inativos!";
                    sv.desfazerTransacao();
                    return null;
                }
            }
        }

        // ------------------------------------------------------------------------------------------------------
        if (!sv.deletarObjeto((Juridica) sv.pesquisaCodigo(juridica.getId(), "Juridica"))) {
            msgConfirma = "Erro ao excluir Jurídica!";
            sv.desfazerTransacao();
            return null;
        }

        //  EXCLUIR OS EMAILS ENVIADOS PESSOA --------------------------------------------------------------

        EnvioEmailsDB db = new EnvioEmailsDBToplink();
        List<EnvioEmails> listE = db.pesquisaTodosPorPessoa(juridica.getPessoa().getId());

        for (int i = 0; i < listE.size(); i++) {
            if (!sv.deletarObjeto((EnvioEmails) sv.pesquisaCodigo(listE.get(i).getId(), "EnvioEmails"))) {
                msgConfirma = "Erro ao emails enviados!";
                sv.desfazerTransacao();
                return null;
            }
        }
        // -------------------------------------------------------------------------------------------------

        if (!sv.deletarObjeto((Pessoa) sv.pesquisaCodigo(juridica.getPessoa().getId(), "Pessoa"))) {
            msgConfirma = "Erro ao excluir Pessoa!";
            sv.desfazerTransacao();
            return null;
        }
        msgConfirma = "Cadastro excluido com sucesso!";
        sv.comitarTransacao();
        novoGenerico();
        return null;
    }

    public String editar() {
        juridica = (Juridica) listaJuridica.get(idIndex).getArgumento0();
        if (juridica.getContabilidade() == null) {
            contabilidade = new Juridica();
        } else {
            contabilidade = juridica.getContabilidade();
        }
        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
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

        if (url != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", juridica);
            getContribuinte();
            if (juridica.getContabilidade() == null) {
                renChkEndereco = "false";
            } else {
                renChkEndereco = "true";
            }
            renNovoEndereco = "false";
            renEndereco = "false";
            alterarEnd = true;
            listaEnd = new ArrayList();
            enderecoCobranca = "NENHUM";
            getListaEnderecos();

            if (contabilidade != null) {
                if (contabilidade.getId() != -1) {
                    PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
                    PessoaEndereco pesEnd1 = new PessoaEndereco(), pesEnd2 = new PessoaEndereco();
                    pesEnd1 = db.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 3);
                    pesEnd2 = db.pesquisaEndPorPessoaTipo(contabilidade.getPessoa().getId(), 3);
                    if (comparaEndereco(pesEnd1, pesEnd2) || listaEnd.isEmpty()) {
                        chkEndContabilidade = true;
                    } else {
                        chkEndContabilidade = false;
                    }
                }
            }
            return url;
        }
        return "pessoaJuridica";
    }

    public String editar(Juridica jur) {
        juridica = jur;
        if (juridica.getContabilidade() == null) {
            contabilidade = new Juridica();
        } else {
            contabilidade = juridica.getContabilidade();
        }
        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
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
        if (url != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaPesquisa", juridica);

            if (juridica.getContabilidade() == null) {
                renChkEndereco = "false";
            } else {
                renChkEndereco = "true";
            }
            renNovoEndereco = "false";
            renEndereco = "false";
            alterarEnd = true;
            listaEnd = new ArrayList();
            enderecoCobranca = "NENHUM";
            getListaEnderecos();
            return url;
        }
        return "pessoaJuridica";
    }

    public String editarEmpresaPertencente() {
        juridica = (Juridica) listaEmpresasPertencentes.get(idIndexPertencente).getArgumento0();
        if (juridica.getContabilidade() == null) {
            contabilidade = new Juridica();
        } else {
            contabilidade = juridica.getContabilidade();
        }
        contabilidade = juridica.getContabilidade();
        String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";

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
            renNovoEndereco = "false";
            renEndereco = "false";
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
        JuridicaDB db = new JuridicaDBToplink();
        juridica = db.pesquisaCodigo(juridica.getContabilidade().getId());
        if (juridica.getContabilidade() == null) {
            contabilidade = new Juridica();
        } else {
            contabilidade = juridica.getContabilidade();
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
            renNovoEndereco = "false";
            renEndereco = "false";
            alterarEnd = true;
            listaEnd = new ArrayList();
            enderecoCobranca = "NENHUM";
            listaContribuintesInativos.clear();
            getListaEnderecos();
            return "pessoaJuridica";
        }
        return "pessoaJuridica";
    }

    public String editarContabilidade() {
        contabilidade = (Juridica) listaContabilidade.get(idIndexContabilidade);
        juridica.setContabilidade(contabilidade);
        juridica.setEmailEscritorio(true);
        renChkEndereco = "true";
        chkEndContabilidade = true; // ROGÉRINHO PEDIU PRA VOLTAR TRUE NA DATA -- 30/07/2013 -- POR CAUSA DO CARLOS DE LIMEIRA
        return "pessoaJuridica";
    }

//   public List getListaJuridica(){
//       List result = null;
//       JuridicaDB db = new JuridicaDBToplink();
//       result = db.pesquisaTodos();
//       return result;
//   }
    public String adicionarEnderecos() {
        List tiposE = new ArrayList();
        TipoEnderecoDB db_tipoEndereco = new TipoEnderecoDBToplink();
        PessoaEnderecoDB db_pesEnd = new PessoaEnderecoDBToplink();
        endereco = new Endereco();
        String num;
        String comp;
        int i = 0;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoNum", pessoaEndereco.getNumero());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoComp", pessoaEndereco.getComplemento());
        tiposE = db_tipoEndereco.listaTipoEnderecoParaJuridica();
        endereco = (Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa");
        if (endereco != null) {
            if (!alterarEnd) {
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
                if (!listaEnd.isEmpty() && pessoaEndereco.getTipoEndereco().getId() == 2) {

                    if (pessoaEndereco.getId() != -1) {
                        PessoaEndereco pessoaEndeAnt = new PessoaEndereco();
                        pessoaEndeAnt = db_pesEnd.pesquisaEndPorPessoaTipo(pessoaEndereco.getPessoa().getId(), 2);
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
            renEndereco = "true";
            renNovoEndereco = "false";
        }
        setEnderecoCompleto("");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoNum");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoComp");
        return "pessoaJuridica";
    }

    public boolean comparaEndereco(PessoaEndereco pessoaEnde1, PessoaEndereco pessoaEnde2) {
        boolean compara = false;
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
        PessoaEndereco pesEn = new PessoaEndereco();
        String strCompl = "";
        if (!getPesquisaEndPorPessoa().isEmpty() && alterarEnd && listaEnd.isEmpty()) {
            listaEnd = getPesquisaEndPorPessoa();
            pesEn = (PessoaEndereco) (listaEnd.get(1));
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

    public String abreEndereco() {
        listaEnd = getListaEnderecos();
        if (listaEnd.isEmpty()) {
            renEndereco = "false";
            renNovoEndereco = "true";
            pessoaEndereco = new PessoaEndereco();
            listaEnd = new ArrayList();
        } else {
            renEndereco = "true";
            renNovoEndereco = "false";
        }
        return "pessoaJuridica";
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
        PessoaEndereco pesEndCon = new PessoaEndereco();
        if (juridica.getId() != -1) {
            if (chkEndContabilidade && contabilidade.getId() != -1) {
                pesEndCon = db.pesquisaEndPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3);
                if ((!listaEnd.isEmpty()) && pesEndCon != null) {
                    pessoaEndereco = (PessoaEndereco) listaEnd.get(1);
                    pessoaEndereco.setComplemento(pesEndCon.getComplemento());
                    pessoaEndereco.setNumero(pesEndCon.getNumero());
                    endereco = pesEndCon.getEndereco();
                    pessoaEndereco.setEndereco(endereco);
                    listaEnd.set(1, pessoaEndereco);
                }
            } else if (juridica != null) {
                if (juridica.getContabilidade() != null) {
                    if (comparaEndereco((PessoaEndereco) listaEnd.get(1), db.pesquisaEndPorPessoaTipo(juridica.getContabilidade().getPessoa().getId(), 3))) {
                        pesEndCon = db.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 2);
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
                chkEndContabilidade = false;
            }
        }
    }

    public void pesquisaContabilidadeI() {
        JuridicaDB db = new JuridicaDBToplink();
        if (!contabilidade.getPessoa().getNome().isEmpty()) {
            listaContabilidade = db.pesquisaPessoa(contabilidade.getPessoa().getNome(), "nome", "I");
        }
    }

    public void pesquisaContabilidadeP() {
        JuridicaDB db = new JuridicaDBToplink();
        if (!contabilidade.getPessoa().getNome().isEmpty()) {
            listaContabilidade = db.pesquisaPessoa(contabilidade.getPessoa().getNome(), "nome", "P");
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

    public String RetornarObjetoDaGrid() {
        pessoaEndereco = (PessoaEndereco) listaEnd.get(idIndexEndereco);
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoPesquisa", pessoaEndereco.getEndereco());
        log = pessoaEndereco.getEndereco().getLogradouro().getDescricao();
        desc = pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao();
        cid = pessoaEndereco.getEndereco().getCidade().getCidade();
        uf = pessoaEndereco.getEndereco().getCidade().getUf();
        setEnderecoCompleto(log + " " + desc + ", " + cid + " - " + uf);
        renEndereco = "false";
        renNovoEndereco = "true";
        alterarEnd = true;
        return "pessoaJuridica";
    }

    public List getListaPessoaEndereco() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        List result = salvarAcumuladoDB.listaObjeto("PessoaEndereco");        
        return result;
    }

    public String CarregarEndereco() {
        EnderecoDB db_endereco = new EnderecoDBToplink();
        int idEndereco = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("paramEndereco"));
        pessoaEndereco.setEndereco(db_endereco.pesquisaCodigo(idEndereco));
        setEnderecoCompleto((pessoaEndereco.getEndereco().getLogradouro().getDescricao()) + " " + pessoaEndereco.getEndereco().getDescricaoEndereco().getDescricao());
        return "pessoaJuridica";
    }

    public List<String> BuscaTipoEndereco(Object event) {
        List<String> result = new Vector<String>();
        String txtDigitado = event.toString().toLowerCase().toUpperCase();
        TipoEnderecoDB db = new TipoEnderecoDBToplink();
        result = db.pesquisaTipoEnderecoParaJuridica('%' + txtDigitado + '%');
        return (result);
    }

    public List<String> BuscaTipoDocumento(Object event) {
        List<String> result = new Vector<String>();
        String txtDigitado = event.toString().toLowerCase().toUpperCase();
        TipoDocumentoDB db = new TipoDocumentoDBToplink();
        result = db.pesquisaTipoDocumento('%' + txtDigitado + '%');
        return (result);
    }

    public List getPesquisaEndPorPessoa() {
        List result = null;
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        result = db.pesquisaEndPorPessoa(juridica.getPessoa().getId());
        return result;
    }

    public String voltarEndereco() {
        setIndicaTab("juridica");
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
        FilialDB db = new FilialDBToplink();
        if ((juridica.getPessoa().getId() != -1) && (filialMatriz.equals("m"))) {
            if (filial.getId() == -1) {
                filial.setFilial(juridica);
                filial.setMatriz(juridica);
                db.insert(filial);
            } else {
                db.getEntityManager().getTransaction().begin();
                if (db.update(filial)) {
                    db.getEntityManager().getTransaction().commit();
                } else {
                    db.getEntityManager().getTransaction().rollback();
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
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listaJuridica.clear();
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

    public String retornaCnae() {
        Cnae tcnae = null;
        tcnae = (Cnae) listaCnae.get(idIndexCnae);
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

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cnaePesquisado");
            return "pessoaJuridica";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public String JuridicaFilialGrid() {
        FilialDB db = new FilialDBToplink();
        filial = new Filial();
        int i = filial.getFilial().getId();
        filial.setMatriz(juridica);
        db.insert(filial);
        setIndicaTab("filial");
        return "pessoaJuridica";
    }

    public List getPesquisaJuridicaFilial() {
        List result = null;
        FilialDB db = new FilialDBToplink();
        result = db.pesquisaJuridicaFilial(juridica.getId());
        return result;
    }

    public void excluirFilial() {
        FilialDB db = new FilialDBToplink();
        db.getEntityManager().getTransaction().begin();
        filial = db.pesquisaFilialPertencente(juridica.getId(), filial.getFilial().getId());
        if (db.delete(filial)) {
            db.getEntityManager().getTransaction().commit();
        } else {
            db.getEntityManager().getTransaction().rollback();
        }
        filial = new Filial();
    }

    public List getPesquisaFilial() {
        List result = null;
        FilialDB db = new FilialDBToplink();
        result = db.pesquisaFilial(descPesquisa, porPesquisa, comoPesquisa, juridica.getId());
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
            mask = "cpf";
        }
        if (i == 2) {
            mask = "cnpj";
        }
        if (i == 3) {
            mask = "cei";
        }
        if (i == 4) {
            mask = "";
        }
        return mask;
    }

    public List<SelectItem> getListaTipoDocumento() {
        TipoDocumentoDB db = new TipoDocumentoDBToplink();
        if (listaTipoDocumento.isEmpty()) {
            int i = 0;
            List select = db.pesquisaTodos();
            while (i < select.size()) {
                listaTipoDocumento.add(new SelectItem(new Integer(i), (String) ((TipoDocumento) select.get(i)).getDescricao(), Integer.toString(((TipoDocumento) select.get(i)).getId())));
                i++;
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

    public String btnExcluirContabilidadePertencente() {
        if (juridica.getId() != -1) {
            chkEndContabilidade = false;
            salvarEndereco();
            juridica.setContabilidade(null);
            juridica.setEmailEscritorio(false);
        } else {
            juridica.setContabilidade(null);
            juridica.setEmailEscritorio(false);
        }
        return "pessoaJuridica";
    }

    public List<SelectItem> getListaMotivoInativacao() {
        List<SelectItem> motIna = new Vector<SelectItem>();
        int i = 0;
        JuridicaDB db = new JuridicaDBToplink();
        List select = db.listaMotivoInativacao();
        while (i < select.size()) {
            motIna.add(new SelectItem(new Integer(i),
                    (String) ((MotivoInativacao) select.get(i)).getDescricao(),
                    Integer.toString(((MotivoInativacao) select.get(i)).getId())));
            i++;
        }
        return motIna;
    }

    public String pesquisarPessoaJuridicaGeracaoCadastrar() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "processamentoIndividual");
        return "pessoaJuridica";
    }

    public boolean validaTipoDocumento(int idDoc, String docS) {
        // 1 cpf, 2 cnpj, 3 cei, 4 nenhum
        String documento = "";
        documento = docS.replace(".", "").replace("/", "").replace("-", "");

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

    public List getListaEmpresasPertencentes() {
        JuridicaDB db = new JuridicaDBToplink();
        PessoaEnderecoDB dbPe = new PessoaEnderecoDBToplink();
        PessoaEndereco pe = new PessoaEndereco();

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
        FilialDB db = new FilialDBToplink();
        EnvioEmailsDB dbE = new EnvioEmailsDBToplink();
        if (juridica.getId() != -1) {
            msgConfirma = EnviarEmail.EnviarEmail(db.pesquisaCodigoRegistro(1), juridica);
            if (msgConfirma.equals("Enviado com Sucesso. Confira email cadastrado!")) {
                dbE.insert(envioEmails);
            }
        } else {
            msgConfirma = "Pesquisar uma Empresa para envio!";
        }
    }

    public String enviarEmailParaTodos() {
        FilialDB db = new FilialDBToplink();
        JuridicaDB dbJur = new JuridicaDBToplink();
        Registro reg = new Registro();
        List<Juridica> jur = new ArrayList<Juridica>();
        jur = dbJur.pesquisaJuridicaComEmail();
        reg = db.pesquisaCodigoRegistro(1);
        msgConfirma = EnviarEmail.EnviarEmailAutomatico(reg, jur);
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
        PessoaDB db = new PessoaDBToplink();
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

    public String extratoTela() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", juridica.getPessoa());
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

    public void setIndicaTab(String indicaTab) {
        this.indicaTab = indicaTab;
    }

    public String getIndicaTab() {
        return indicaTab;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Juridica getJuridica() {
        if (juridica.getFantasia().isEmpty() || juridica.getFantasia() == null) {
            juridica.setFantasia(juridica.getPessoa().getNome());
        }
        return juridica;
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
        ConvencaoCidade conv = new ConvencaoCidade();
        ConvencaoCidadeDB db = new ConvencaoCidadeDBToplink();
        conv = db.pesquisarConvencao(convencao.getId(), gruCids.getId());
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
        CnaeConvencao cc = new CnaeConvencao();
        cc = retornarCnaeConvencao();
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

    public String getRenNovoEndereco() {
        return renNovoEndereco;
    }

    public void setRenNovoEndereco(String renNovoEndereco) {
        this.renNovoEndereco = renNovoEndereco;
    }

    public String getRenEndereco() {
        return renEndereco;
    }

    public void setRenEndereco(String renEndereco) {
        this.renEndereco = renEndereco;
    }

    public String getRenAbreEnd() {
        return renAbreEnd;
    }

    public void setRenAbreEnd(String renAbreEnd) {
        this.renAbreEnd = renAbreEnd;
    }

    public boolean getChkEndContabilidade() {
        return chkEndContabilidade;
    }

    public void setChkEndContabilidade(boolean chkEndContabilidade) {
        this.chkEndContabilidade = chkEndContabilidade;
    }

    public String getRenChkEndereco() {
        if (renChkEndereco.equals("false")) {
            chkEndContabilidade = false;
        }
        return renChkEndereco;
    }

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

    public List<DataObject> getListaJuridica() {
        if (listaJuridica.isEmpty()) {
            JuridicaDB db = new JuridicaDBToplink();
            List<Juridica> lista = new ArrayList();
            lista = db.pesquisaPessoa(descPesquisa, porPesquisa, comoPesquisa);

            for (int i = 0; i < lista.size(); i++) {
                List<Vector> listax = db.listaJuridicaContribuinte(lista.get(i).getId());
                String status = "";
                if (listax.isEmpty()) {
                    status = "NÃO CONTRIBUINTE";
                } else {
                    if (listax.get(0).get(11) != null) {
                        status = "CONTRIBUINTE INATIVO";
                    } else {
                        status = "ATIVO";
                    }
                }
                listaJuridica.add(new DataObject(lista.get(i), status));
            }

        }
        return listaJuridica;
    }

    public void setListaJuridica(List<DataObject> listaJuridica) {
        this.listaJuridica = listaJuridica;
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

    public Juridica getContabilidade() {
        return contabilidade;
    }

    public void setContabilidade(Juridica contabilidade) {
        this.contabilidade = contabilidade;
    }

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
        ConvencaoCidade conv = new ConvencaoCidade();
        ConvencaoCidadeDB db = new ConvencaoCidadeDBToplink();
        conv = db.pesquisarConvencao(convencao.getId(), gruCids.getId());
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
        if (juridica.getId() != -1) {
            juridica.setCnae(null);
        }
    }
}
