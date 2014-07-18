package br.com.rtools.atendimento.beans;

import br.com.rtools.atendimento.AteMovimento;
import br.com.rtools.atendimento.AteOperacao;
import br.com.rtools.atendimento.AteStatus;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.atendimento.db.AtendimentoDB;
import br.com.rtools.atendimento.db.AtendimentoDBTopLink;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.homologacao.db.HomologacaoDB;
import br.com.rtools.homologacao.db.HomologacaoDBToplink;
import br.com.rtools.impressao.ParametroSenha;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.pessoa.db.PessoaEmpresaDB;
import br.com.rtools.pessoa.db.PessoaEmpresaDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.ValidaDocumentos;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@SessionScoped
public class AtendimentoBean {

    private AteOperacao ateOperacao = new AteOperacao();
    private AteMovimento ateMovimento = new AteMovimento();
    private SisPessoa atePessoa = new SisPessoa();
    private String porPesquisa = "hoje";
    Pessoa pessoa = new Pessoa();
    Fisica fisica = new Fisica();
    private MacFilial macFilial = new MacFilial();
    private Filial filial = new Filial();
    private int idIndexPessoa = -1;
    private int idIndexMovimento = -1;
    private String msg = "";
    private String msgOposicao = "";
    private String msgConfirma = "";
    private String msgCPF = "";
    private String msgErro = "";
    private List<AteMovimento> listaAteMovimento = new ArrayList();
    private List<SisPessoa> listaAtePessoas = new ArrayList();
    private int idFilial = 0;
    private int idOperacao = 0;
    private List<SelectItem> listaAtendimentoOperacoes = new ArrayList<SelectItem>();
    private List<SelectItem> listaFiliais = new ArrayList<SelectItem>();
    private boolean desabilitaCamposPessoa = false;
    private boolean editaPessoa = false;
    private boolean btnEditaPessoa = true;
    private String horaEmissaoString = "";
    private String cpf = "";
    private String mensagem = "";
    private String strCPF = "";
    private String strRG = "";
    private String strTelefone = "";
    private Juridica empresa = new Juridica();
    private Usuario usuario = new Usuario();
    
    public AtendimentoBean(){
        usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
    }
    
    public String verSenha(AteMovimento atendimento){
        AtendimentoDB db = new AtendimentoDBTopLink();
        Senha senha = db.pesquisaSenha(atendimento.getId());
        if (senha.getSenha() < 10)
            return "0"+ String.valueOf(senha.getSenha());
        else
            return String.valueOf(senha.getSenha());
    }
    
    public void imprimirSenha(AteMovimento atendimento) throws JRException{
        AtendimentoDB db = new AtendimentoDBTopLink();
        
        Senha senha = db.pesquisaSenha(atendimento.getId());
        
        Collection lista = new ArrayList<ParametroSenha>();
        
        if (senha.getId() != -1) {
            lista.add(new ParametroSenha(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                    senha.getFilial().getFilial().getPessoa().getNome(),
                    senha.getFilial().getFilial().getPessoa().getDocumento(),
                    senha.getAteMovimento().getJuridica().getPessoa().getNome(),
                    senha.getAteMovimento().getJuridica().getPessoa().getDocumento(),
                    "", // PREPOSTO
                    senha.getAteMovimento().getPessoa().getNome(),
                    senha.getUsuario().getPessoa().getNome(),
                    senha.getData(),
                    senha.getHora(),
                    String.valueOf(senha.getSenha())));
        }
        
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File((((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/HOM_SENHA.jasper"))));
        JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
        JasperPrint print = JasperFillManager.fillReport(jasperReport, null, dtSource);
        byte[] arquivo = JasperExportManager.exportReportToPdf(print);
        String nomeDownload = "senha_" + DataHoje.hora().replace(":", "") + ".pdf";
        String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/senhas");
        Diretorio.criar("Arquivos/senhas");
        if (!new File(pathPasta).exists()) {
            File file = new File(pathPasta);
            file.mkdir();
        }
        SalvaArquivos salvaArquivos = new SalvaArquivos(arquivo, nomeDownload, false);
        salvaArquivos.salvaNaPasta(pathPasta);
        Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
        download.baixar();
        download.remover();
    }
    
    public String novo() {
        setMsgCPF("");
        setEditaPessoa(false);
        setBtnEditaPessoa(true);
        setDesabilitaCamposPessoa(false);
        setHoraEmissaoString("");
        pessoa = new Pessoa();
        fisica = new Fisica();
        atePessoa = new SisPessoa();
        ateMovimento = new AteMovimento();
        ateMovimento.setFilial(filial);
        getListaAtendimentoOperacoes().clear();
        getListaFiliais().clear();
        idFilial = 0;
        idOperacao = 0;
        msgOposicao = "";
        empresa = new Juridica();
        return "atendimento";
    }

    public String voltar() {
        setEditaPessoa(false);
        setBtnEditaPessoa(true);
        if (ateMovimento.getPessoa().getId() != -1) {
            setDesabilitaCamposPessoa(true);
        } else {
            setDesabilitaCamposPessoa(false);
        }
        return "atendimento";
    }

    public String salvar() throws JRException {
        msg = "";
        AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
        boolean isMostraDocumento = false;
        if (ateMovimento.getFilial().getId() == -1) {
            msg = "Informar a Filial!";
            return null;
        }
        if (ateMovimento.getPessoa().getDocumento().equals("___.___.___-__") || ateMovimento.getPessoa().getDocumento().length() < 14 || ateMovimento.getPessoa().getDocumento().equals("")) {
            msg = "Informar o CPF!";
        } else {
            isMostraDocumento = true;
        }
        if (!ateMovimento.getPessoa().getDocumento().equals("___.___.___-__") && !ateMovimento.getPessoa().getDocumento().equals("")) {
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(ateMovimento.getPessoa().getDocumento()))) {
                setMsgCPF("CPF inválido!");
                return null;
            }
        }
        if (isMostraDocumento == false) {
            if (ateMovimento.getPessoa().getRg().equals("")) {
                msg = "Informar o RG!";
                setMsgCPF("");
            } else {
                msg = "";
            }
        }
        if (!msg.equals("")) {
            return null;
        }
        if (ateMovimento.getPessoa().getNome().equals("")) {
            msg = "Informar o nome da pessoa!";
            return null;
        }
        
        
        if (empresa.getId() == -1){
            msg = "Pesquise uma Empresa para Agendar.";
            return null;
        }
        SisPessoa ap = atendimentoDB.pessoaDocumento(ateMovimento.getPessoa().getDocumento());
        if (ap == null) {
            ap = atendimentoDB.pessoaDocumento(ateMovimento.getPessoa().getRg());
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        ateMovimento.getPessoa().setTipoDocumento((TipoDocumento) salvarAcumuladoDB.pesquisaObjeto(1, "TipoDocumento"));
        ateMovimento.getPessoa().setEndereco(null);
        if (ap != null) {
            if (ateMovimento.getId() > 0 || ap.getId() > 0) {
                if (!ap.getNome().equals(ateMovimento.getPessoa().getNome()) || !ap.getTelefone().equals(ateMovimento.getPessoa().getTelefone()) || !ap.getRg().equals(ateMovimento.getPessoa().getRg()) || !ap.getDocumento().equals(ateMovimento.getPessoa().getDocumento())) {
                    salvarAcumuladoDB.abrirTransacao();
                    if (ateMovimento.getPessoa().getId() == -1) {
                        ateMovimento.getPessoa().setId(ap.getId());
                    }
                    if (salvarAcumuladoDB.alterarObjeto(ateMovimento.getPessoa())) {
                        salvarAcumuladoDB.comitarTransacao();
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                        return null;
                    }
                }
                ateMovimento.setPessoa(ap);
            }
        }
        ateMovimento.setHoraEmissao(getHoraEmissaoString());
        ateMovimento.setFilial(filial);
        ateMovimento.setOperacao((AteOperacao) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(listaAtendimentoOperacoes.get(idOperacao).getDescription()), "AteOperacao"));
        ateMovimento.setStatus((AteStatus) salvarAcumuladoDB.pesquisaCodigo(1, "AteStatus"));
        ateMovimento.setJuridica(empresa);
        
        if (ateMovimento.getId() == -1) {
            ateMovimento.setHoraEmissao(getHoraEmissaoString());
            if (atendimentoDB.existeAtendimento(ateMovimento)) {
                msg = "Atendimento já cadastrado!";
                return null;
            }
            if (ap == null) {
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.inserirObjeto(ateMovimento.getPessoa())) {
                    salvarAcumuladoDB.comitarTransacao();
                    ateMovimento.setPessoa(ateMovimento.getPessoa());
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                    msg = "Não foi possível incluir a pessoa";
                    return null;
                }
            }
            
            salvarAcumuladoDB.abrirTransacao();
            if (!salvarAcumuladoDB.inserirObjeto(ateMovimento)) {
                msg = "Falha ao inserir o movimento!";
                salvarAcumuladoDB.desfazerTransacao();
            } else {
                msg = "Movimento inserido com sucesso.";
            }
            
            HomologacaoDB dbh = new HomologacaoDBToplink();
            int ultima_senha = dbh.pesquisaUltimaSenha(filial.getId()) + 1;
            Senha senha = new Senha(-1, null, DataHoje.horaMinuto(), "", 0, usuario, DataHoje.data(), ultima_senha, filial, ateMovimento);
            
            if (salvarAcumuladoDB.inserirObjeto(senha)){
                salvarAcumuladoDB.comitarTransacao();
                getListaAteMovimento().clear();
                imprimirSenha(ateMovimento);
                novo();
            }else{
                msg = "Erro ao salvar Senha.";
                salvarAcumuladoDB.desfazerTransacao();
            }
            
            return "atendimento";
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(ateMovimento)) {
                getListaAteMovimento().clear();
                novo();
                salvarAcumuladoDB.comitarTransacao();
                msg = "Atendimento atualizado com sucesso!";
                return null;
            }
            msg = "Falha na atualização do atendimento!";
            return "atendimento";
        }
    }

    public String atualizarPessoa() {
        if (!ateMovimento.getPessoa().getDocumento().equals("___.___.___-__") && !ateMovimento.getPessoa().getDocumento().equals("")) {
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(ateMovimento.getPessoa().getDocumento()))) {
                msg = "CPF inválido!";
                return null;
            }
        } else {
            if (ateMovimento.getPessoa().getRg().equals("")) {
                msg = "Informar RG!";
                return null;
            }
        }
        setEditaPessoa(true);
        setBtnEditaPessoa(true);
        setDesabilitaCamposPessoa(false);
        AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
        SisPessoa ap = atendimentoDB.pessoaDocumento(ateMovimento.getPessoa().getDocumento());
        if (ap == null) {
            ap = atendimentoDB.pessoaDocumento(ateMovimento.getPessoa().getRg());
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (ap == null) {
            if (ateMovimento.getPessoa().getId() == -1) {
                salvarAcumuladoDB.abrirTransacao();
                if (salvarAcumuladoDB.inserirObjeto(ateMovimento.getPessoa())) {
                    salvarAcumuladoDB.comitarTransacao();
                    msg = "Registro atualizado com sucesso.";
                    return null;
                } else {
                    salvarAcumuladoDB.desfazerTransacao();
                    msg = "Falha na atualização do registro!";
                    return null;
                }
            }
        } else {
            if (ateMovimento.getPessoa().getId() != -1 || ap.getId() != -1) {
                if (!ap.getNome().equals(ateMovimento.getPessoa().getNome()) || !ap.getTelefone().equals(ateMovimento.getPessoa().getTelefone()) || !ap.getRg().equals(ateMovimento.getPessoa().getRg()) || !ap.getDocumento().equals(ateMovimento.getPessoa().getDocumento())) {
                    salvarAcumuladoDB.abrirTransacao();
                    if (ateMovimento.getPessoa().getId() == -1) {
                        ateMovimento.getPessoa().setId(ap.getId());
                    }
                    if (salvarAcumuladoDB.alterarObjeto(ateMovimento.getPessoa())) {
                        salvarAcumuladoDB.comitarTransacao();
                        msg = "Registro atualizado com sucesso.";
                        return null;
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                        msg = "Falha na atualização do registro!";
                        return null;
                    }
                }
            }
        }
        return "atendimento";

    }

    public String editar(AteMovimento am) {
        ateMovimento = am;
        for (int i = 0; i < listaAtendimentoOperacoes.size(); i++) {
            if (Integer.parseInt(listaAtendimentoOperacoes.get(i).getDescription()) == ateMovimento.getOperacao().getId()) {
                idOperacao = i;
            }
        }
        for (int i = 0; i < getListaFiliais().size(); i++) {
            if (Integer.parseInt(getListaFiliais().get(i).getDescription()) == ateMovimento.getFilial().getId()) {
                idFilial = i;
            }
        }
        setHoraEmissaoString(ateMovimento.getHoraEmissao());
        setBtnEditaPessoa(true);
        setEditaPessoa(false);
        setDesabilitaCamposPessoa(true);
        verificaPessoaOposicao();
        return null;
    }

    public String editarPessoa() {
        setEditaPessoa(true);
        setBtnEditaPessoa(false);
        setDesabilitaCamposPessoa(false);
        return "atendimento";
    }

    public String excluir() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (ateMovimento.getId() > 0) {
            AteMovimento ateMov = (AteMovimento) salvarAcumuladoDB.pesquisaObjeto(ateMovimento.getId(), "AteMovimento");
            AtendimentoDB db = new AtendimentoDBTopLink();
            
            Senha senha = db.pesquisaSenha(ateMovimento.getId());
            
            salvarAcumuladoDB.abrirTransacao();
            
            if (senha != null){
                if (!salvarAcumuladoDB.deletarObjeto( salvarAcumuladoDB.pesquisaObjeto(senha.getId(), "Senha"))){
                    salvarAcumuladoDB.desfazerTransacao();
                    msg = "Erro ao excluir Senha";
                    return null;
                }
            }
            
            if (salvarAcumuladoDB.deletarObjeto(ateMov)) {
                salvarAcumuladoDB.comitarTransacao();
                msg = "Movimento excluído com sucesso.";
                novo();
                return null;
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msg = "Falha ao excluir movimento!";
            }

        }
        getListaAteMovimento().clear();
        return "atendimento";
    }

    public String verificaPessoaOposicao() {
        setMsgOposicao("");
        AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
        if (atendimentoDB.pessoaOposicao(ateMovimento.getPessoa().getDocumento())) {
            setMsgOposicao("Pessoa cadastrada em oposição");
        }
        return "atendimento";
    }

    public String verificaCPF(String tipoVerificacao) {
        if (ateMovimento.getId() != -1 || editaPessoa == true || ateMovimento.getPessoa().getId() != -1) {
            return null;
        }
        String valorPesquisa = "";
        int count = ateMovimento.getPessoa().getDocumento().length();
        if (tipoVerificacao.equals("cpf")) {
            if (!ateMovimento.getPessoa().getDocumento().equals("___.___.___-__") && count == 14 && !ateMovimento.getPessoa().getDocumento().equals("")) {
                if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(ateMovimento.getPessoa().getDocumento()))) {
                    setMsgCPF("CPF inválido!");
                    return null;
                } else {
                    setMsgCPF("");
                }
            } else {
                setMsgCPF("");
                return null;
            }
            valorPesquisa = ateMovimento.getPessoa().getDocumento();
        } else if (tipoVerificacao.equals("rg")) {
            if (ateMovimento.getPessoa().getRg().equals("")) {
                return null;
            }
            if (ateMovimento.getPessoa().getId() != -1) {
                return null;
            }
            valorPesquisa = ateMovimento.getPessoa().getRg();
        }
        if (!ateMovimento.getPessoa().getDocumento().equals("___.___.___-__") && count == 14 || !ateMovimento.getPessoa().getRg().equals("")) {
            PessoaDB db = new PessoaDBToplink();
            pessoa = (Pessoa) db.pessoaDocumento(valorPesquisa);
            if (pessoa != null) {
                ateMovimento.getPessoa().setNome(pessoa.getNome());
                ateMovimento.getPessoa().setDocumento(pessoa.getDocumento());
                ateMovimento.getPessoa().setTelefone(pessoa.getTelefone1());
                if (!ateMovimento.getPessoa().getTelefone().equals("(__) ____-____")) {
                    ateMovimento.getPessoa().setDocumento(pessoa.getDocumento());
                }
                FisicaDB fisicaDB = new FisicaDBToplink();
                fisica = (Fisica) fisicaDB.pesquisaFisicaPorPessoa(pessoa.getId());
                ateMovimento.getPessoa().setRg(fisica.getRg());
                ateMovimento.getPessoa().setTelefone(pessoa.getTelefone1());
                if (fisica.getRg().equals("") || pessoa.getDocumento().equals("") || pessoa.getTelefone1().equals("")) {
                    setEditaPessoa(false);
                }
                setMsgCPF("");
                setDesabilitaCamposPessoa(true);
                PessoaEmpresaDB pedb = new PessoaEmpresaDBToplink();
                
                PessoaEmpresa pe = pedb.pesquisaPessoaEmpresaPorFisica(fisica.getId());
                
                if (pe.getId() != -1){
                    empresa = pe.getJuridica();
                }
            } else {
                AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
                SisPessoa atePessoaB = (SisPessoa) atendimentoDB.pessoaDocumento(valorPesquisa);
                setMsgCPF("");
                if (ateMovimento == null || (atePessoaB == null || atePessoaB.getId() == -1)) {
//                    AtePessoa atePes = new AtePessoa();
//                    ateMovimento.setPessoa(atePes);
                    setEditaPessoa(false);
                } else {
                    ateMovimento.setPessoa(atePessoaB);
                    setEditaPessoa(false);
                    setDesabilitaCamposPessoa(true);
                }
            }
        }
        return "atendimento";
    }

    public List<SelectItem> getListaFiliais() {
        if (listaFiliais.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Filial> listaFilial = (List<Filial>) salvarAcumuladoDB.listaObjeto("Filial", true);           
            for (int i = 0; i < listaFilial.size(); i++) {
                listaFiliais.add(new SelectItem(new Integer(i),
                        listaFilial.get(i).getFilial().getPessoa().getDocumento() + " / " + listaFilial.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(listaFilial.get(i).getId())));
            }
        }
        return listaFiliais;
    }

    public void setListaFiliais(List<SelectItem> listaFiliais) {
        this.listaFiliais = listaFiliais;
    }

    public List<SelectItem> getListaAtendimentoOperacoes() {
        if (listaAtendimentoOperacoes.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<AteOperacao> list = dB.listaObjeto("AteOperacao");
            if (list != null) {
                int i = 0;
                while (i < list.size()) {
                    listaAtendimentoOperacoes.add(new SelectItem(new Integer(i), list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
                    i++;
                }
            }

        }
        return listaAtendimentoOperacoes;
    }

    public AteOperacao getAteOperacao() {
        return ateOperacao;
    }

    public void setAteOperacao(AteOperacao ateOperacao) {
        this.ateOperacao = ateOperacao;
    }

    public AteMovimento getAteMovimento() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") != null) {
            if (filial.getId() == -1) {
                setMacFilial((MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial"));
                SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                filial = (Filial) salvarAcumuladoDB.pesquisaCodigo(getMacFilial().getFilial().getId(), "Filial");
                ateMovimento.setFilial(filial);
                setMensagem("");
            } else {
                setMensagem("");
            }
        } else {
            setMensagem("Não existe filial definida!");
        }
        return ateMovimento;
    }

    public void setAteMovimento(AteMovimento ateMovimento) {
        this.ateMovimento = ateMovimento;
    }

    public SisPessoa getAtePessoa() {
        return atePessoa;
    }

    public void setAtePessoa(SisPessoa atePessoa) {
        this.atePessoa = atePessoa;
    }

    public int getIdIndexPessoa() {
        return idIndexPessoa;
    }

    public void setIdIndexPessoa(int idIndexPessoa) {
        this.idIndexPessoa = idIndexPessoa;
    }

    public int getIdIndexMovimento() {
        return idIndexMovimento;
    }

    public void setIdIndexMovimento(int idIndexMovimento) {
        this.idIndexMovimento = idIndexMovimento;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getMsgErro() {
        return msgErro;
    }

    public void setMsgErro(String msgErro) {
        this.msgErro = msgErro;
    }

    public List<AteMovimento> getListaAteMovimento() {
        listaAteMovimento.clear();
        if (listaAteMovimento.isEmpty()) {
            AtendimentoDB db = new AtendimentoDBTopLink();
            int count = cpf.length();
            if (!cpf.equals("___.___.___-__") && count == 14) {
                listaAteMovimento = db.listaAteMovimentos(cpf, porPesquisa);
            } else {
                listaAteMovimento = db.listaAteMovimentos("", porPesquisa);
            }

        }

        return listaAteMovimento;
    }

    public void setListaAteMovimento(List<AteMovimento> listaAteMovimento) {
        this.listaAteMovimento = listaAteMovimento;
    }

    public List<SisPessoa> getListaAtePessoas() {
        return listaAtePessoas;
    }

    public void setListaAtePessoas(List<SisPessoa> listaAtePessoas) {
        this.listaAtePessoas = listaAtePessoas;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public String getMsgOposicao() {
        return msgOposicao;
    }

    public void setMsgOposicao(String msgOposicao) {
        this.msgOposicao = msgOposicao;
    }

    public int getIdOperacao() {
        return idOperacao;
    }

    public void setIdOperacao(int idOperacao) {
        this.idOperacao = idOperacao;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public boolean isDesabilitaCamposPessoa() {
        return desabilitaCamposPessoa;
    }

    public void setDesabilitaCamposPessoa(boolean desabilitaCamposPessoa) {
        this.desabilitaCamposPessoa = desabilitaCamposPessoa;
    }

    public boolean isEditaPessoa() {
        return editaPessoa;
    }

    public void setEditaPessoa(boolean editaPessoa) {
        this.editaPessoa = editaPessoa;
    }

    public String getHoraEmissaoString() {
        if (!this.horaEmissaoString.equals("")) {
            return this.horaEmissaoString;
        } else {
            Date date = new Date();
            return DataHoje.livre(date, "HH:mm");
        }
    }

    public void setHoraEmissaoString(String horaEmissaoString) {
        this.horaEmissaoString = horaEmissaoString;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public MacFilial getMacFilial() {
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public Filial getFilial(Filial filial) {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getStrCPF() {
        return strCPF;
    }

    public void setStrCPF(String strCPF) {
        this.strCPF = strCPF;
    }

    public String getStrRG() {
        return strRG;
    }

    public void setStrRG(String strRG) {
        this.strRG = strRG;
    }

    public String getStrTelefone() {
        return strTelefone;
    }

    public void setStrTelefone(String strTelefone) {
        this.strTelefone = strTelefone;
    }

    public boolean isBtnEditaPessoa() {
        return btnEditaPessoa;
    }

    public void setBtnEditaPessoa(boolean btnEditaPessoa) {
        this.btnEditaPessoa = btnEditaPessoa;
    }

    public String getMsgCPF() {
        return msgCPF;
    }

    public void setMsgCPF(String msgCPF) {
        this.msgCPF = msgCPF;
    }

    public Juridica getEmpresa() {
        if (GenericaSessao.getObject("juridicaPesquisa") != null){
            empresa = (Juridica)GenericaSessao.getObject("juridicaPesquisa");
            GenericaSessao.remove("juridicaPesquisa");
        }
        return empresa;
    }

    public void setEmpresa(Juridica empresa) {
        this.empresa = empresa;
    }
}