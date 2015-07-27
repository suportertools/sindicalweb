package br.com.rtools.atendimento.beans;

import br.com.rtools.atendimento.AteMovimento;
import br.com.rtools.atendimento.AteOperacao;
import br.com.rtools.atendimento.AteStatus;
import br.com.rtools.atendimento.db.AtendimentoDB;
import br.com.rtools.atendimento.db.AtendimentoDBTopLink;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.homologacao.db.HomologacaoDB;
import br.com.rtools.homologacao.db.HomologacaoDBToplink;
import br.com.rtools.impressao.ParametroSenha;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.PermissaoUsuario;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.db.PermissaoUsuarioDao;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.ValidaDocumentos;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class AtendimentoBean implements Serializable {

    private AteOperacao ateOperacao = new AteOperacao();
    private AteMovimento ateMovimento = new AteMovimento();
    private SisPessoa atePessoa = new SisPessoa();
    private String porPesquisa = "hoje";
    private MacFilial macFilial = new MacFilial();
    private Filial filial = new Filial();
    private int idIndexPessoa = -1;
    private int idIndexMovimento = -1;
    private List<AteMovimento> listaAteMovimento = new ArrayList();
    private List<SisPessoa> listaAtePessoas = new ArrayList();
    private int idFilial = 0;
    private int idOperacao = 0;
    private List<SelectItem> listaAtendimentoOperacoes = new ArrayList<SelectItem>();
    private List<SelectItem> listaFiliais = new ArrayList<SelectItem>();

    private String horaEmissaoString = "";
    private Juridica empresa = new Juridica();
    private Usuario usuario = new Usuario();

    private SisPessoa sisPessoa = new SisPessoa();
    private SisPessoa sisPessoaAtualiza = new SisPessoa();
    private StreamedContent fileDownload = null;
    private boolean visibleModal = false;
    private String tipoTelefone = "telefone";

    private List<SelectItem> listaUsuarios = new ArrayList<SelectItem>();
    private int index_usuario = 0;
    private boolean chkReserva = false;

    public AtendimentoBean() {
        usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
    }

    public void alterarTipoMascara() {
        if (tipoTelefone.equals("telefone")) {
            tipoTelefone = "celular";
        } else {
            tipoTelefone = "telefone";
        }
    }

    public SisPessoa getSisPessoa() {
        return sisPessoa;
    }

    public void setSisPessoa(SisPessoa sisPessoa) {
        this.sisPessoa = sisPessoa;
    }

    public void pesquisaCPFeOPOSICAO() {
        if (sisPessoa.getDocumento().isEmpty()) {
            return;
        }

        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(sisPessoa.getDocumento()))) {
            return;
        }

        FisicaDB fisicaDB = new FisicaDBToplink();
        List<Fisica> listf = fisicaDB.pesquisaFisicaPorDoc(sisPessoa.getDocumento());

        // SE NÃO ACHAR PESSOA FÍSICA, PESQUISAR EM SIS_PESSOA
        if (listf.isEmpty()) {
            AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
            SisPessoa spes = (SisPessoa) atendimentoDB.pessoaDocumento(sisPessoa.getDocumento());

            if (spes == null) {
                return;
            }
            sisPessoa = spes;
            verificaPessoaOposicao();
            return;
        }

        Fisica fi = (Fisica) listf.get(0);

        sisPessoa.setNome(fi.getPessoa().getNome());
        sisPessoa.setDocumento(fi.getPessoa().getDocumento());
        sisPessoa.setTelefone(fi.getPessoa().getTelefone1());
        sisPessoa.setRg(fi.getRg());
        verificaPessoaOposicao();
    }

    public void pesquisaRG() {
        if (sisPessoa.getRg().isEmpty()) {
            return;
        }

        FisicaDB fisicaDB = new FisicaDBToplink();
        List<Fisica> listf = fisicaDB.pesquisaFisicaPorDocRG(sisPessoa.getRg());

        // SE NÃO ACHAR PESSOA FÍSICA, PESQUISAR EM SIS_PESSOA
        if (listf.isEmpty()) {
            AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
            SisPessoa spes = (SisPessoa) atendimentoDB.pessoaDocumento(sisPessoa.getRg());

            if (spes == null) {
                return;
            }
            sisPessoa = spes;
            return;
        }

        Fisica fi = (Fisica) listf.get(0);

        sisPessoa.setNome(fi.getPessoa().getNome());
        sisPessoa.setDocumento(fi.getPessoa().getDocumento());
        sisPessoa.setTelefone(fi.getPessoa().getTelefone1());
        sisPessoa.setRg(fi.getRg());
    }

    public void editarSisPessoa() {
        sisPessoaAtualiza = sisPessoa;
    }

    public void atualizaSisPessoa() {
        if (!sisPessoaAtualiza.getDocumento().isEmpty()) {
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(sisPessoaAtualiza.getDocumento()))) {
                GenericaMensagem.warn("Atenção", "Esse documento é Inválido!!");
                return;
            }

//            FisicaDB fisicaDB = new FisicaDBToplink();
//            List<Fisica> listf = fisicaDB.pesquisaFisicaPorDoc(sisPessoaAtualiza.getDocumento());
//            
//            // DOCUMENTO JÁ EXISTE PARA OUTRA PESSOA FISICA
//            if (!listf.isEmpty()){
//                if (!listf.get(0).getPessoa().getNome().equals(sisPessoaAtualiza.getNome()) ){
//                    GenericaMensagem.warn("Atenção", "Esse documento já existe para outra Pessoa!");
//                    return;
//                }
//            }
            AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
            SisPessoa spes = (SisPessoa) atendimentoDB.pessoaDocumento(sisPessoaAtualiza.getDocumento());

            if (spes != null && spes.getId() != sisPessoaAtualiza.getId()) {
                GenericaMensagem.warn("Atenção", "Esse documento já existe para outra Pessoa!");
                return;
            }
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();

        if (!sv.alterarObjeto(sisPessoaAtualiza)) {
            sv.desfazerTransacao();
            GenericaMensagem.error("Erro", "Não foi possivel atualizar cadastro!");
            return;
        }

        sisPessoa = sisPessoaAtualiza;
        sisPessoaAtualiza = new SisPessoa();
        sv.comitarTransacao();

    }

    public String verSenha(AteMovimento atendimento) {
        AtendimentoDB db = new AtendimentoDBTopLink();
        Senha senha = db.pesquisaSenha(atendimento.getId());
        if (senha != null) {
            if (senha.getSenha() < 10) {
                return "0" + String.valueOf(senha.getSenha());
            } else {
                return String.valueOf(senha.getSenha());
            }
        }

        return "Sem Senha";
    }

    public String imprimirSenha(AteMovimento atendimento) throws JRException {

        AtendimentoDB db = new AtendimentoDBTopLink();

        Senha senha = db.pesquisaSenha(atendimento.getId());

        Collection lista = new ArrayList<ParametroSenha>();

        if (senha.getId() != -1) {

            if (senha.getAteMovimento().getJuridica() != null) {
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
            } else {
                lista.add(new ParametroSenha(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                        senha.getFilial().getFilial().getPessoa().getNome(),
                        senha.getFilial().getFilial().getPessoa().getDocumento(),
                        "", // NOME EMPRESA
                        "", // DOCUMENTO EMPRESA
                        "", // PREPOSTO
                        senha.getAteMovimento().getPessoa().getNome(),
                        senha.getUsuario().getPessoa().getNome(),
                        senha.getData(),
                        senha.getHora(),
                        String.valueOf(senha.getSenha())));

            }
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

        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/senhas" + "/" + nomeDownload);
        fileDownload = new DefaultStreamedContent(stream, "application/pdf", nomeDownload);

//        Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
//        download.baixar();
//        download.remover();
        return "atendimento";
    }

    public String novo() {
        GenericaSessao.put("atendimentoBean", new AtendimentoBean());
        return "atendimento";
    }

    public void fecharModal() {
        GenericaSessao.put("atendimentoBean", new AtendimentoBean());
    }

    public String salvarImprimir() {
        salvar(true);
        visibleModal = true;
        return null;
    }

    public void salvar(boolean imprimir) {

        AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();

        if (ateMovimento.getFilial().getId() == -1) {
            GenericaMensagem.error("Erro", "Informe qual a sua Filial!");
            return;
        }

        if (!sisPessoa.getDocumento().isEmpty()) {
            if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(sisPessoa.getDocumento()))) {
                GenericaMensagem.warn("Atenção", "Informe um CPF válido!");
                return;
            }
        }

        if (sisPessoa.getNome().isEmpty()) {
            GenericaMensagem.warn("Atenção", "Digite o NOME da pessoa!");
            return;
        }

//        if (empresa.getId() == -1){
//            //msg = "Pesquise uma Empresa para Agendar.";
//            GenericaMensagem.warn("Atenção", "Pesquise uma Empresa para concluir o Atendimento!");
//            return;
//        }
//        SisPessoa ap = atendimentoDB.pessoaDocumento(ateMovimento.getPessoa().getDocumento());
//        if (ap == null) {
//            ap = atendimentoDB.pessoaDocumento(ateMovimento.getPessoa().getRg());
//        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sisPessoa.setTipoDocumento((TipoDocumento) sv.pesquisaObjeto(1, "TipoDocumento"));
        sisPessoa.setEndereco(null);

        sv.abrirTransacao();
        if (sisPessoa.getId() == -1) {
            if (!sv.inserirObjeto(sisPessoa)) {
                sv.desfazerTransacao();
                return;
            }
        } else {
            if (!sv.alterarObjeto(sisPessoa)) {
                sv.desfazerTransacao();
                return;
            }
        }
        sv.comitarTransacao();

        ateMovimento.setHoraEmissao(getHoraEmissaoString());
        ateMovimento.setFilial(filial);
        ateMovimento.setOperacao((AteOperacao) sv.pesquisaObjeto(Integer.parseInt(listaAtendimentoOperacoes.get(idOperacao).getDescription()), "AteOperacao"));
        ateMovimento.setStatus((AteStatus) sv.pesquisaCodigo(1, "AteStatus"));
        ateMovimento.setJuridica((empresa == null || empresa.getId() == -1) ? null : empresa);
        ateMovimento.setPessoa(sisPessoa);
        ateMovimento.setAtendente(null);

        if (chkReserva && !listaUsuarios.isEmpty()) {
            PermissaoUsuario pu = (PermissaoUsuario) sv.pesquisaCodigo(Integer.valueOf(listaUsuarios.get(index_usuario).getDescription()), "PermissaoUsuario");
            ateMovimento.setReserva(pu.getUsuario());
        } else {
            ateMovimento.setReserva(null);
        }

        sv.abrirTransacao();
        if (ateMovimento.getId() == -1) {
            ateMovimento.setHoraEmissao(getHoraEmissaoString());

            // PERMITIR QUE CRIE UM ATENDIMENTO REPETIDO PARA MESMA PESSOA -- chamado 287 --
//            if (atendimentoDB.existeAtendimento(ateMovimento)) {
//                GenericaMensagem.error("Atenção", "Atendimento já cadastrado!");
//                sv.desfazerTransacao();
//                return;
//            }
            if (!sv.inserirObjeto(ateMovimento)) {
                GenericaMensagem.error("Erro", "Não foi possivel salvar Atendimento!");
                sv.desfazerTransacao();
                return;
            }

            HomologacaoDB dbh = new HomologacaoDBToplink();
            int ultima_senha = dbh.pesquisaUltimaSenha(filial.getId()) + 1;
            Senha senha = new Senha(-1, null, DataHoje.horaMinuto(), "", 0, usuario, DataHoje.data(), ultima_senha, filial, ateMovimento);

            if (!sv.inserirObjeto(senha)) {
                GenericaMensagem.error("Erro", "Erro ao Salvar Senha!");
                sv.desfazerTransacao();
                return;
            }

            sv.comitarTransacao();

            if (imprimir) {
                try {
                    imprimirSenha(ateMovimento);
                } catch (JRException ex) {
                }
            } else {
                GenericaSessao.put("atendimentoBean", new AtendimentoBean());
            }

            GenericaMensagem.info("Sucesso", "Atendimento Salvo!");
        } else {
            if (!sv.alterarObjeto(ateMovimento)) {
                sv.desfazerTransacao();
                GenericaMensagem.error("Erro", "Não foi possivel atualizar o Atendimento");
                return;
            }

            sv.comitarTransacao();

            GenericaSessao.put("atendimentoBean", new AtendimentoBean());
            GenericaMensagem.info("Sucesso", "Atendimento Atualizado!");
        }

    }

    public String editar(AteMovimento am) {
        ateMovimento = am;
        sisPessoa = ateMovimento.getPessoa();
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
        empresa = ateMovimento.getJuridica();

        setHoraEmissaoString(ateMovimento.getHoraEmissao());
        verificaPessoaOposicao();

        chkReserva = ateMovimento.getReserva() != null;
        if (chkReserva) {
            for (int i = 0; i < getListaUsuarios().size(); i++) {
                PermissaoUsuario pu = (PermissaoUsuario) new Dao().find(new PermissaoUsuario(), Integer.valueOf(listaUsuarios.get(i).getDescription()));
                if (pu.getUsuario().getId() == ateMovimento.getReserva().getId()) {
                    index_usuario = i;
                }
            }
        }

        return null;
    }

    public void excluir() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (ateMovimento.getId() > 0) {
            AteMovimento ateMov = (AteMovimento) salvarAcumuladoDB.pesquisaObjeto(ateMovimento.getId(), "AteMovimento");
            AtendimentoDB db = new AtendimentoDBTopLink();

            Senha senha = db.pesquisaSenha(ateMovimento.getId());

            salvarAcumuladoDB.abrirTransacao();

            if (senha != null) {
                if (!salvarAcumuladoDB.deletarObjeto(salvarAcumuladoDB.pesquisaObjeto(senha.getId(), "Senha"))) {
                    salvarAcumuladoDB.desfazerTransacao();
                    GenericaMensagem.error("Erro", "Falha excluir Senha!");
                    return;
                }
            }

            if (salvarAcumuladoDB.deletarObjeto(ateMov)) {
                salvarAcumuladoDB.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Atendimento Excluido!");
                novo();
                return;
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                GenericaMensagem.error("Erro", "Falha ao excluir Atendimento!");
            }

        }
        getListaAteMovimento().clear();
    }

    public void cancelar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        //AteMovimento ateMov = (AteMovimento) sv.pesquisaObjeto(ateMovimento.getId(), "AteMovimento");
        // status 3 Atendimento Cancelado
        ateMovimento.setStatus((AteStatus) sv.pesquisaCodigo(3, "AteStatus"));

        if (!sv.alterarObjeto(ateMovimento)) {
            sv.desfazerTransacao();
        } else {
            sv.comitarTransacao();
        }
    }

    public String retornaOposicaoPessoa(String documento) {
        AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
        if (atendimentoDB.pessoaOposicao(documento)) {
            return "tblOposicaox";
        } else {
            return "";
        }
    }

    public void verificaPessoaOposicao() {
        AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
        if (atendimentoDB.pessoaOposicao(sisPessoa.getDocumento())) {
            RequestContext.getCurrentInstance().execute("PF('dlg_mensagem_oposicao').show();");
        }
    }
//
//    public void verificaCPF(String tipoVerificacao) {
//        if (ateMovimento.getId() != -1 || ateMovimento.getPessoa().getId() != -1) {
//            return;
//        }
//        String valorPesquisa = "";
//        
//        if (tipoVerificacao.equals("cpf")) {
//            if (!ateMovimento.getPessoa().getDocumento().isEmpty()) {
//                if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(ateMovimento.getPessoa().getDocumento()))) {
////                    setMsgCPF("CPF inválido!");
//                    GenericaMensagem.warn("Atenção", "CPF inválido!");
//                    return ;
//                } else {
//  //                  setMsgCPF("");
//                }
//            } else {
//                //setMsgCPF("");
//                return;
//            }
//            valorPesquisa = ateMovimento.getPessoa().getDocumento();
//        } else if (tipoVerificacao.equals("rg")) {
//            if (ateMovimento.getPessoa().getRg().isEmpty()) {
//                return;
//            }
//            if (ateMovimento.getPessoa().getId() != -1) {
//                return;
//            }
//            valorPesquisa = ateMovimento.getPessoa().getRg();
//        }
//        
//        PessoaDB db = new PessoaDBToplink();
//        pessoa = (Pessoa) db.pessoaDocumento(valorPesquisa);
//        if (pessoa != null) {
//            ateMovimento.getPessoa().setNome(pessoa.getNome());
//            ateMovimento.getPessoa().setDocumento(pessoa.getDocumento());
//            ateMovimento.getPessoa().setTelefone(pessoa.getTelefone1());
//            if (!ateMovimento.getPessoa().getTelefone().equals("(__) ____-____")) {
//                ateMovimento.getPessoa().setDocumento(pessoa.getDocumento());
//            }
//            FisicaDB fisicaDB = new FisicaDBToplink();
//            fisica = (Fisica) fisicaDB.pesquisaFisicaPorPessoa(pessoa.getId());
//            ateMovimento.getPessoa().setRg(fisica.getRg());
//            ateMovimento.getPessoa().setTelefone(pessoa.getTelefone1());
////            if (fisica.getRg().equals("") || pessoa.getDocumento().equals("") || pessoa.getTelefone1().equals("")) {
////                setEditaPessoa(false);
////            }
//            //setMsgCPF("");
////            setDesabilitaCamposPessoa(true);
//            PessoaEmpresaDB pedb = new PessoaEmpresaDBToplink();
//
//            PessoaEmpresa pe = pedb.pesquisaPessoaEmpresaPorFisica(fisica.getId());
//
//            if (pe.getId() != -1){
//                empresa = pe.getJuridica();
//            }
//        } else {
//            AtendimentoDB atendimentoDB = new AtendimentoDBTopLink();
//            SisPessoa atePessoaB = (SisPessoa) atendimentoDB.pessoaDocumento(valorPesquisa);
//            //setMsgCPF("");
//            if (ateMovimento == null || (atePessoaB == null || atePessoaB.getId() == -1)) {
////                    AtePessoa atePes = new AtePessoa();
////                    ateMovimento.setPessoa(atePes);
//                //setEditaPessoa(false);
//            } else {
//                ateMovimento.setPessoa(atePessoaB);
////                setEditaPessoa(false);
////                setDesabilitaCamposPessoa(true);
//            }
//        }
//    }

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
            }
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

    public List<AteMovimento> getListaAteMovimento() {
        if (!sisPessoa.getDocumento().equals("___.___.___-__")) {
            listaAteMovimento.clear();
            AtendimentoDB db = new AtendimentoDBTopLink();
            if (!sisPessoa.getDocumento().isEmpty()) {
                listaAteMovimento = db.listaAteMovimentos(sisPessoa.getDocumento(), porPesquisa, filial.getId());
            } else {
                listaAteMovimento = db.listaAteMovimentos("", porPesquisa, filial.getId());
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

    public Juridica getEmpresa() {
        if (GenericaSessao.getObject("juridicaPesquisa") != null) {
            empresa = (Juridica) GenericaSessao.getObject("juridicaPesquisa");
            GenericaSessao.remove("juridicaPesquisa");
        }
        return empresa;
    }

    public void setEmpresa(Juridica empresa) {
        this.empresa = empresa;
    }

    public SisPessoa getSisPessoaAtualiza() {
        return sisPessoaAtualiza;
    }

    public void setSisPessoaAtualiza(SisPessoa sisPessoaAtualiza) {
        this.sisPessoaAtualiza = sisPessoaAtualiza;
    }

    public StreamedContent getFileDownload() {
        return fileDownload;
    }

    public void setFileDownload(StreamedContent fileDownload) {
        this.fileDownload = fileDownload;
    }

    public boolean isVisibleModal() {
        return visibleModal;
    }

    public void setVisibleModal(boolean visibleModal) {
        this.visibleModal = visibleModal;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public List<SelectItem> getListaUsuarios() {
        if (listaUsuarios.isEmpty()) {
            PermissaoUsuarioDao pud = new PermissaoUsuarioDao();
//            Permissao permissao = db.pesquisaPermissao(4, 114, 4);

//            AtendimentoDB db = new AtendimentoDBTopLink();
            // DEPARTAMENTO 8 - HOMOLOGAÇÃO ---
            List<PermissaoUsuario> result = pud.listaPermissaoUsuarioDepartamento(8);

            if (result.isEmpty()) {
                listaUsuarios.add(new SelectItem(0, "Nenhum Usuário Encontrado", "0"));
                return listaUsuarios;
            }

            for (int i = 0; i < result.size(); i++) {
                listaUsuarios.add(new SelectItem(
                        i,
                        result.get(i).getUsuario().getPessoa().getNome(),
                        Integer.toString(result.get(i).getId()))
                );
            }
        }
        return listaUsuarios;
    }

    public void setListaUsuarios(List<SelectItem> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public int getIndex_usuario() {
        return index_usuario;
    }

    public void setIndex_usuario(int index_usuario) {
        this.index_usuario = index_usuario;
    }

    public boolean isChkReserva() {
        return chkReserva;
    }

    public void setChkReserva(boolean chkReserva) {
        this.chkReserva = chkReserva;
    }

}
