package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConviteAutorizaCortesia;
import br.com.rtools.associativo.ConviteMovimento;
import br.com.rtools.associativo.ConviteServico;
import br.com.rtools.associativo.ConviteSuspencao;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.ConviteDB;
import br.com.rtools.associativo.db.ConviteDBToplink;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.ServicoValor;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.LoteDB;
import br.com.rtools.financeiro.db.LoteDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.ServicoValorDB;
import br.com.rtools.financeiro.db.ServicoValorDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.pessoa.db.SpcDB;
import br.com.rtools.pessoa.db.SpcDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.sistema.db.SisPessoaDB;
import br.com.rtools.sistema.db.SisPessoaDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.PhotoCam;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CaptureEvent;

@ManagedBean
@SessionScoped
public class ConviteMovimentoBean implements Serializable {

    private ConviteMovimento conviteMovimento = new ConviteMovimento();
    private Movimento movimento = new Movimento();
    private Socios socios = new Socios();
    private Usuario usuario = new Usuario();
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private List<ConviteMovimento> conviteMovimentos = new ArrayList<ConviteMovimento>();
    private List<SelectItem> listaPessoaAutoriza = new ArrayList<SelectItem>();
    private List<SelectItem> conviteServicos = new ArrayList<SelectItem>();
    private String fotoPerfil = "";
    private String fotoArquivo = "";
    private String fotoTempPerfil = "";
    private String fotoTempArquivo = "";
    private String mensagem = "";
    private String tipoCaptura = "";
    private String descricaoPesquisa = "";
    private String comoPesquisa = "";
    private String porPesquisa = "";
    private String valorString = "";
    private String cliente = "";
    private int idServico = 0;
    private int idPessoaAutoriza = 0;
    private int idadeConvidado = 0;
    private boolean visibility = false;
    private boolean desabilitaCampos = false;
    private boolean desabilitaCamposMovimento = false;
    private boolean desabilitaBaixa = false;

    public void novo() {
        valorString = "";
        idServico = 0;
        conviteMovimento = new ConviteMovimento();
        conviteMovimento.getSisPessoa().setDtNascimento(null);
        pessoaEndereco = new PessoaEndereco();
        socios = new Socios();
        mensagem = "";
        tipoCaptura = "";
        descricaoPesquisa = "";
        comoPesquisa = "";
        porPesquisa = "";
        conviteMovimentos.clear();
        fotoPerfil = "";
        fotoArquivo = "";
        fotoTempPerfil = "";
        fotoTempArquivo = "";
        visibility = true;
        desabilitaCampos = false;
        idadeConvidado = 0;
        desabilitaBaixa = false;
        desabilitaCamposMovimento = false;
        movimento = new Movimento();
    }

    public void openDialog() {
        visibility = true;
    }

    public void close() {
        novo();
        visibility = false;
        RequestContext.getCurrentInstance().execute("dgl_adicionar.hide()");
        RequestContext.getCurrentInstance().update("form_convite:i_panel_adicionar");
    }

    public String salvar() throws IOException {
        if (MacFilial.getAcessoFilial().getId() == -1) {
            if (conviteMovimento.getId() == -1) {
                if (!conviteMovimento.isCortesia()) {
                    mensagem = "Para salvar convites não cortesia configurar Filial em sua estação trabalho!";
                    return null;
                }
            }
        }
        boolean sucesso = false;
        if (conviteServicos.isEmpty()) {
            mensagem = "Cadastrar serviços!";
            return null;
        }
        if (conviteMovimento.getPessoa().getId() == -1) {
            mensagem = "Pesquisar sócio!";
            return null;
        }
        if (conviteMovimento.getSisPessoa().getNome().equals("")) {
            mensagem = "Informar nome do convidado!";
            return null;
        }
        conviteMovimento.getSisPessoa().setNome(conviteMovimento.getSisPessoa().getNome().toUpperCase());
        if (conviteMovimento.getSisPessoa().getNascimento().equals("")) {
            mensagem = "Informar data de nascimento do convidado!";
            return null;
        }
        if (conviteMovimento.getSisPessoa().getTelefone().equals("") && conviteMovimento.getSisPessoa().getCelular().equals("")) {
            mensagem = "Informar um número de telefone/celular!";
            return null;
        }
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        if (conviteMovimento.isCortesia()) {
            conviteMovimento.setAutorizaCortesia(((ConviteAutorizaCortesia) dB.pesquisaObjeto(Integer.parseInt(listaPessoaAutoriza.get(idPessoaAutoriza).getDescription()), "ConviteAutorizaCortesia")).getPessoa());
        }
        conviteMovimento.setConviteServico((ConviteServico) dB.pesquisaObjeto(Integer.parseInt(conviteServicos.get(idServico).getDescription()), "ConviteServico"));
        dB.abrirTransacao();
        if (conviteMovimento.getSisPessoa().getId() == -1) {
            conviteMovimento.getSisPessoa().setTipoDocumento((TipoDocumento) dB.pesquisaObjeto(1, "TipoDocumento"));
            if (dB.inserirObjeto(conviteMovimento.getSisPessoa())) {
                dB.comitarTransacao();
            } else {
                dB.desfazerTransacao();
                mensagem = "Erro ao inserir sis pessoa!";
                return null;
            }
        } else {
            if (dB.alterarObjeto(conviteMovimento.getSisPessoa())) {
                dB.comitarTransacao();
            } else {
                dB.desfazerTransacao();
                mensagem = "Erro ao atualizar sis pessoa!";
                return null;
            }
        }

        if (conviteMovimento.getId() == -1) {
            Registro r = (Registro) dB.pesquisaObjeto(1, "Registro");
            SpcDB spcDB = new SpcDBToplink();
            if (spcDB.existeRegistroPessoaSPC(conviteMovimento.getPessoa())) {
                mensagem = "Existem débitos com o síndicato!";
                return null;
            }
            ConviteDB cdb = new ConviteDBToplink();
            if (!conviteMovimento.isCortesia()) {
                if (cdb.limiteConvitePorSocio(r.getConviteQuantidadeSocio(), r.getConviteDiasSocio(), conviteMovimento.getPessoa().getId())) {
                    mensagem = "Limite de convites excedido para este sócio! Este sócio tem direito a disponibilizar " + r.getConviteQuantidadeSocio() + " convite(s) a cada " + r.getConviteDiasSocio() + "dia(s)";
                    return null;
                }
                if (cdb.limiteConviteConvidado(r.getConviteQuantidadeConvidado(), r.getConviteDiasConvidado(), conviteMovimento.getSisPessoa().getId())) {
                    mensagem = "Limite de convites excedido para convidado! Este convidado tem direito a " + r.getConviteQuantidadeConvidado() + " a cada " + r.getConviteDiasConvidado() + "dia(s)";
                    return null;
                }
                if (valorString.equals("")) {
                    mensagem = "Informar o valor do serviço, faixa etária não possuí valor do serviço!";
                    return null;
                }
            }
                if (cdb.socio(conviteMovimento.getSisPessoa())) {
                    mensagem = "Convidado não pode ser sócio ativo!";
                    return null;
                }
            ConviteSuspencao cs = new ConviteSuspencao();
            cs.setSisPessoa(conviteMovimento.getSisPessoa());
            if (cdb.existeSisPessoaSuspensa(cs)) {
                mensagem = "Convidado possui cadastro suspenso!";
                return null;
            }
            if (spcDB.existeRegistroPessoaSPC(conviteMovimento.getPessoa())) {
                mensagem = "Existem débitos com o síndicato!";
                return null;
            }
            SociosDB sdb = new SociosDBToplink();
            if (sdb.socioDebito(conviteMovimento.getPessoa().getId())) {
                mensagem = "Sócio possui débitos!";
                return null;
            }
            List list = sdb.pesquisaSocioPorPessoaInativo(conviteMovimento.getPessoa().getId());
            if (!list.isEmpty()) {
                mensagem = "Sócio encontra-se inátivo!";
                return null;
            }
            conviteMovimento.setUsuario(usuario);
            conviteMovimento.setEvt(null);
            conviteMovimento.setAutorizaCortesia(null);
            conviteMovimento.setDepartamento(null);
            conviteMovimento.setUsuarioInativacao(null);
            conviteMovimento.setAtivo(true);
            dB.abrirTransacao();
            if (dB.inserirObjeto(conviteMovimento)) {
                if (conviteMovimento.isCortesia()) {
                    dB.comitarTransacao();
                    mensagem = "Registro inserido com sucesso";
                    desabilitaCamposMovimento = true;
                    sucesso = true;
                } else {
                    float valor = Moeda.substituiVirgulaFloat(valorString);
                    if (valor == 0) {
                        dB.desfazerTransacao();
                        conviteMovimento.setId(-1);
                        mensagem = "Informar valor do serviço! Deve ser direfente de 0.";
                        return null;
                    }
                    try {
                        if (gerarMovimento(dB)) {
                            dB.comitarTransacao();
                            mensagem = "Registro inserido com sucesso";
                            desabilitaCamposMovimento = true;
                            sucesso = true;
                        } else {
                            dB.desfazerTransacao();
                            conviteMovimento.setId(-1);
                            mensagem = "Erro ao inserir registro!";
                            return null;
                        }
                    } catch (Exception e) {
                        dB.desfazerTransacao();
                        conviteMovimento.setId(-1);
                        mensagem = "Erro ao inserir registro!";
                        return null;
                    }
                }
            } else {
                dB.desfazerTransacao();
                conviteMovimento.setId(-1);
                mensagem = "Erro ao inserir registro!";
                return null;
            }
        } else {
            dB.abrirTransacao();
            String cvAntes = ((ConviteMovimento) dB.pesquisaObjeto(conviteMovimento.getId(), "ConviteMovimento")).toString();
            if (dB.alterarObjeto(conviteMovimento)) {
                dB.comitarTransacao();
                mensagem = "Registro atualizado com sucesso";
                NovoLog log = new NovoLog();
                log.novo("Atualizado", "De:" + cvAntes + " para " + conviteMovimento.toString());
                sucesso = true;
            } else {
                dB.desfazerTransacao();
                mensagem = "Erro ao atualizar registro!";
                return null;
            }
        }
        if (sucesso) {
            NovoLog log = new NovoLog();
            log.novo("Novo registro", conviteMovimento.toString());
            if (!Diretorio.criar("Imagens/Fotos/SisPessoa/" + conviteMovimento.getId())) {
                return null;
            }
            String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/SisPessoa/" + conviteMovimento.getId());
            boolean error = false;
            if (!fotoTempPerfil.equals("")) {
                File des = new File(arquivo + "/perfil.png");
                if (des.exists()) {
                    des.delete();
                }
                File src = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(fotoTempPerfil));
                boolean rename = src.renameTo(des);
                fotoPerfil = "/Cliente/" + getCliente() + "/Imagens/Fotos/SisPessoa/" + conviteMovimento.getId() + "/perfil.png";
                fotoTempPerfil = "";
                if (!rename) {
                    error = true;
                }
            }
            if (!fotoTempArquivo.equals("")) {
                File des = new File(arquivo + "/documento.png");
                if (des.exists()) {
                    des.delete();
                }
                File src = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(fotoTempArquivo));
                boolean rename = src.renameTo(des);
                if (!rename) {
                    error = true;
                }
                fotoArquivo = "/Cliente/" + getCliente() + "/Imagens/Fotos/SisPessoa/" + conviteMovimento.getId() + "/documento.png";
                fotoTempArquivo = "";
            }
            if (!error) {
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/convite/0/"));
                boolean delete = f.delete();
            }
            if (!conviteMovimento.isCortesia()) {

            }
            RequestContext.getCurrentInstance().update("form_convite:i_panel_perfil");
            RequestContext.getCurrentInstance().update("form_convite:i_panel_documento");
            if (getMovimento().getId() != -1) {
                if (getMovimento().getBaixa() == null) {
                    getMovimento().setValorBaixa(getMovimento().getValor());
                    List listMovimento = new ArrayList();
                    listMovimento.add(getMovimento());
                    GenericaSessao.put("listaMovimento", listMovimento);
                    return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
                }
            }
        }
        return null;
    }

    public void convidadoExiste() {
        if (conviteMovimento.getSisPessoa().getId() == -1) {
            if (!conviteMovimento.getSisPessoa().getDocumento().equals("") || !conviteMovimento.getSisPessoa().getNome().equals("") || !conviteMovimento.getSisPessoa().getNascimento().equals("")) {
                SisPessoaDB sisPessoaDB = new SisPessoaDBToplink();
                SisPessoa sp = sisPessoaDB.sisPessoaExiste(conviteMovimento.getSisPessoa(), true);
                if (sp == null) {
                    conviteMovimento.getSisPessoa().setNome(conviteMovimento.getSisPessoa().getNome().toUpperCase());
                    sp = sisPessoaDB.sisPessoaExiste(conviteMovimento.getSisPessoa());
                    if (sp == null) {
                    } else {
                        conviteMovimento.setSisPessoa(sp);
                    }
                } else {
                    conviteMovimento.setSisPessoa(sp);
                }
                RequestContext.getCurrentInstance().update("form_convite:i_panel_convidado");
                RequestContext.getCurrentInstance().update("form_convite:i_panel_endereco_convidado");
                visibility = true;
            }
        }
    }

    public void excluir() {
        String msg = "";
        if (conviteMovimento.getId() != -1) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            conviteMovimento.setUsuarioInativacao((Usuario) dB.pesquisaObjeto(getUsuario().getId(), "Usuario"));
            conviteMovimento.setAtivo(false);
            dB.abrirTransacao();
            if (dB.alterarObjeto((ConviteMovimento) dB.pesquisaObjeto(conviteMovimento.getId(), "ConviteMovimento"))) {
                if (getMovimento().getId() != -1) {
                    getMovimento().setAtivo(false);
                    if (!dB.alterarObjeto(getMovimento())) {
                        dB.desfazerTransacao();
                        return;
                    }
                }
                NovoLog log = new NovoLog();
                dB.comitarTransacao();
                msg = "Registro inativado com sucesso";
                RequestContext.getCurrentInstance().execute("dgl_adicionar.show()");
                RequestContext.getCurrentInstance().update("form_convite:i_panel_adicionar");
            } else {
                dB.desfazerTransacao();
                msg = "Erro ao inativar registro!";
            }
        }
        novo();
        mensagem = msg;
    }

    public void editar(ConviteMovimento cm) {
        conviteMovimento = cm;
        carregaSocio(conviteMovimento.getPessoa());
        carregaEndereco(conviteMovimento.getPessoa());

        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/SisPessoa/" + conviteMovimento.getId());
        File perfil = new File(arquivo + "/perfil.png");
        if (perfil.exists()) {
            fotoPerfil = "/Cliente/" + getCliente() + "/Imagens/Fotos/SisPessoa/" + conviteMovimento.getId() + "/perfil.png";
            fotoTempPerfil = "";
        }
        File documento = new File(arquivo + "/documento.png");
        if (documento.exists()) {
            fotoArquivo = "/Cliente/" + getCliente() + "/Imagens/Fotos/SisPessoa/" + conviteMovimento.getId() + "/documento.png";
            fotoTempArquivo = "";
        }
        if(conviteMovimento.getAutorizaCortesia() != null) {
            for (int i = 0; i < getListaPessoaAutoriza().size(); i++) {
                if (Integer.parseInt(listaPessoaAutoriza.get(i).getDescription()) == conviteMovimento.getAutorizaCortesia().getId()) {
                    idPessoaAutoriza = i;
                    break;
                }
            }
        }
        for (int i = 0; i < getConviteServicos().size(); i++) {
            if (Integer.parseInt(getConviteServicos().get(i).getDescription()) == conviteMovimento.getConviteServico().getId()) {
                idServico = i;
                break;
            }
        }
        RequestContext.getCurrentInstance().execute("dgl_adicionar.show()");
        RequestContext.getCurrentInstance().update("form_convite:i_panel_adicionar");
    }

    public List<ConviteMovimento> getConviteMovimentos() {
        if (conviteMovimentos.isEmpty()) {
            if (porPesquisa.equals("todos")) {
                descricaoPesquisa = "";
            }
            ConviteDB conviteDB = new ConviteDBToplink();
            conviteMovimentos = (List<ConviteMovimento>) conviteDB.pesquisaConviteMovimento(descricaoPesquisa, porPesquisa, comoPesquisa);
        }
        return conviteMovimentos;
    }

    public void setConviteMovimentos(List<ConviteMovimento> conviteMovimentos) {
        this.conviteMovimentos = conviteMovimentos;
    }

    public ConviteMovimento getConviteMovimento() {
        if (conviteMovimento.getId() != -1) {
            visibility = true;
            desabilitaCampos = true;
            if (conviteMovimento.getEvt() != null) {
                desabilitaCamposMovimento = true;
            }
        } else {
            desabilitaCampos = false;
        }
        if (GenericaSessao.exists("fisicaPesquisa")) {
            Pessoa p = (Pessoa) ((Fisica) GenericaSessao.getObject("fisicaPesquisa", true)).getPessoa();
            conviteMovimento.setPessoa(p);
            carregaSocio(p);
            RequestContext.getCurrentInstance().execute("dgl_adicionar.show()");
            RequestContext.getCurrentInstance().update("i_panel_adicionar");
            visibility = true;
            carregaEndereco(p);
        }
        if (GenericaSessao.exists("sisPessoaPesquisa")) {
            conviteMovimento.setSisPessoa(((SisPessoa) GenericaSessao.getObject("sisPessoaPesquisa", true)));
            RequestContext.getCurrentInstance().execute("dgl_adicionar.show()");
            RequestContext.getCurrentInstance().update("i_panel_adicionar");
            visibility = true;
        }
        if (GenericaSessao.exists("enderecoPesquisa")) {
            conviteMovimento.getSisPessoa().setEndereco((Endereco) GenericaSessao.getObject("enderecoPesquisa", true));
            RequestContext.getCurrentInstance().execute("dgl_adicionar.show()");
            RequestContext.getCurrentInstance().update("i_panel_adicionar");
            visibility = true;
        }
        return conviteMovimento;
    }

    public void setConviteMovimento(ConviteMovimento conviteMovimento) {
        this.conviteMovimento = conviteMovimento;
    }

    public Socios getSocios() {
        return socios;
    }

    public void setSocios(Socios socios) {
        this.socios = socios;
    }

    public void carregaSocio(Pessoa p) {
        SociosDB dB = new SociosDBToplink();
        socios = dB.pesquisaSocioPorPessoa(p.getId());
    }

    public void carregaEndereco(Pessoa p) {
        PessoaEnderecoDB dB = new PessoaEnderecoDBToplink();
        int idEndereco[] = new int[]{1, 2, 3, 4};
        for (int i = 0; i < idEndereco.length; i++) {
            pessoaEndereco = (PessoaEndereco) dB.pesquisaEndPorPessoaTipo(p.getId(), idEndereco[i]);
            if (pessoaEndereco == null) {
                pessoaEndereco = new PessoaEndereco();
            } else {
                break;
            }
        }
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<SelectItem> getConviteServicos() {
        if (conviteServicos.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<ConviteServico> list = (List<ConviteServico>) dB.listaObjeto("ConviteServico", true);
            int i = 0;
            for (ConviteServico cs : list) {
                List listSemana = new ArrayList();
                if (cs.isDomingo()) {
                    listSemana.add("Dom");
                }
                if (cs.isSegunda()) {
                    listSemana.add("Seg");
                }
                if (cs.isTerca()) {
                    listSemana.add("Ter");
                }
                if (cs.isQuarta()) {
                    listSemana.add("Qua");
                }
                if (cs.isQuinta()) {
                    listSemana.add("Qui");
                }
                if (cs.isSexta()) {
                    listSemana.add("Sex");
                }
                if (cs.isSabado()) {
                    listSemana.add("Sáb");
                }
                if (cs.isFeriado()) {
                    listSemana.add("Feriado");
                }
                conviteServicos.add(new SelectItem(i, cs.getServicos().getDescricao() + " " + listSemana, "" + cs.getId()));
                i++;
            }
        }
        return conviteServicos;
    }

    public void setConviteServicos(List<SelectItem> conviteServicos) {
        this.conviteServicos = conviteServicos;
    }

    public PessoaEndereco getPessoaEndereco() {
        return pessoaEndereco;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public void capturar(CaptureEvent captureEvent) {
        String fotoTempCaminho = "convite/" + getUsuario().getId();
        if (tipoCaptura.equals("perfil")) {
            if (PhotoCam.oncapture(captureEvent, "perfil", fotoTempCaminho, true)) {
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png"));
                if (f.exists()) {
                    fotoTempPerfil = "/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/perfil.png";
                    fotoPerfil = "";
                } else {
                    fotoTempPerfil = "";
                }
            }
            RequestContext.getCurrentInstance().update("form_convite:i_panel_perfil");
        } else {
            if (PhotoCam.oncapture(captureEvent, "documento", fotoTempCaminho, true)) {
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/documento.png"));
                if (f.exists()) {
                    fotoTempArquivo = "/Cliente/" + getCliente() + "/temp/" + fotoTempCaminho + "/documento.png";
                    fotoArquivo = "";
                } else {
                    fotoTempArquivo = "";
                }
            }
            RequestContext.getCurrentInstance().update("form_convite:i_panel_documento");
        }
        RequestContext.getCurrentInstance().execute("dgl_captura.hide();");
        RequestContext.getCurrentInstance().execute("dgl_captura.hide();");
    }

    public String getTipoCaptura() {
        return tipoCaptura;
    }

    public void setTipoCaptura(String tipoCaptura) {
        this.tipoCaptura = tipoCaptura;
    }

    public void capturarTipo(String tipoCaptura) {
        this.tipoCaptura = tipoCaptura;
        RequestContext.getCurrentInstance().execute("pc.capture();");
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getFotoArquivo() {
        return fotoArquivo;
    }

    public void setFotoArquivo(String fotoArquivo) {
        this.fotoArquivo = fotoArquivo;
    }

    public String getFotoTempPerfil() {
        return fotoTempPerfil;
    }

    public void setFotoTempPerfil(String fotoTempPerfil) {
        this.fotoTempPerfil = fotoTempPerfil;
    }

    public String getFotoTempArquivo() {

        return fotoTempArquivo;
    }

    public void setFotoTempArquivo(String fotoTempArquivo) {
        this.fotoTempArquivo = fotoTempArquivo;
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

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public boolean isDesabilitaCampos() {
        return desabilitaCampos;
    }

    public void setDesabilitaCampos(boolean desabilitaCampos) {
        this.desabilitaCampos = desabilitaCampos;
    }

    public void apagarImagem(String tipoCaptura) {
        boolean sucesso = false;
        if (tipoCaptura.equals("perfil")) {
            if (!fotoTempPerfil.equals("")) {
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/convite/" + getUsuario().getId() + "/perfil.png"));
                sucesso = f.delete();
            } else {
                if (conviteMovimento.getId() != -1) {
                    File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/SisPessoa/" + conviteMovimento.getId() + "/perfil.png"));
                    sucesso = f.delete();
                }
            }
            if (sucesso) {
                fotoTempPerfil = "";
                fotoPerfil = "";
                RequestContext.getCurrentInstance().update("form_convite:i_panel_perfil");
            }
        } else {
            if (!fotoTempArquivo.equals("")) {
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/temp/convite/" + getUsuario().getId() + "/documento.png"));
                sucesso = f.delete();
            } else {
                if (conviteMovimento.getId() != -1) {
                    File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/Imagens/Fotos/SisPessoa/" + conviteMovimento.getId() + "/documento.png"));
                    sucesso = f.delete();
                }
            }
            if (sucesso) {
                fotoTempArquivo = "";
                fotoArquivo = "";
                RequestContext.getCurrentInstance().update("form_convite:i_panel_documento");
            }
        }
    }

    public int getIdPessoaAutoriza() {
        return idPessoaAutoriza;
    }

    public void setIdPessoaAutoriza(int idPessoaAutoriza) {
        this.idPessoaAutoriza = idPessoaAutoriza;
    }

    public List<SelectItem> getListaPessoaAutoriza() {
        if (listaPessoaAutoriza.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<ConviteAutorizaCortesia> list = (List<ConviteAutorizaCortesia>) dB.listaObjeto("ConviteAutorizaCortesia");
            int i = 0;
            for (ConviteAutorizaCortesia cac : list) {
                listaPessoaAutoriza.add(new SelectItem(i, cac.getPessoa().getNome(), "" + cac.getId()));
                i++;
            }
        }
        return listaPessoaAutoriza;
    }

    public void setListaPessoaAutoriza(List<SelectItem> listaPessoaAutoriza) {
        this.listaPessoaAutoriza = listaPessoaAutoriza;
    }

    public String validadeConvite(String dataEmissao) {
        DataHoje dh = new DataHoje();
        dataEmissao = (String) dh.incrementarMeses(1, dataEmissao);
        return dataEmissao;
    }

    public String getMascara() {
        String mask = porPesquisa;
        if (porPesquisa.equals("socioCPF")) {
            mask = "cpf";
        }
        return Mask.getMascaraPesquisa(mask, true);
    }

    public void acaoPesquisaInicial() {
        setComoPesquisa("Inicial");
    }

    public void acaoPesquisaParcial() {
        setComoPesquisa("Parcial");
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public int getIdadeConvidado() {
        if (!conviteMovimento.getSisPessoa().getNascimento().equals("")) {
            if (idadeConvidado == 0) {
                DataHoje dh = new DataHoje();
                idadeConvidado = (int) dh.calcularIdade(conviteMovimento.getSisPessoa().getNascimento());
            }
        }
        return idadeConvidado;
    }

    public void setIdadeConvidado(int idadeConvidado) {
        this.idadeConvidado = idadeConvidado;
    }

    public String getValorString() {
        if (!conviteMovimento.isCortesia()) {
            if (!conviteServicos.isEmpty()) {
                if (!conviteMovimento.getSisPessoa().getNome().equals("")) {
                    SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
                    ServicoValorDB svdb = new ServicoValorDBToplink();
                    try {
                        DataHoje dh = new DataHoje();
                        ServicoValor sv = (ServicoValor) svdb.pesquisaServicoValorPorIdade(((ConviteServico) sadb.pesquisaObjeto(Integer.parseInt(conviteServicos.get(idServico).getDescription()), "ConviteServico")).getServicos().getId(), dh.calcularIdade(conviteMovimento.getSisPessoa().getNascimento()));
                        valorString = Moeda.converteR$(Float.toString((sv.getValor())));
                    } catch (NumberFormatException e) {
                        valorString = "0,00";
                    }
                }
            }
        } else {
            valorString = "0,00";
        }
        return valorString;
    }

    public void setValorString(String valorString) {
        this.valorString = valorString;
    }

    public void gerarMovimento() {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        dB.abrirTransacao();
        gerarMovimento(dB);
    }

    public boolean gerarMovimento(SalvarAcumuladoDB dB) {
        if (conviteMovimento.getEvt() == null) {
            String vencimento = conviteMovimento.getEmissao();
            String referencia;
            Plano5 plano5 = conviteMovimento.getConviteServico().getServicos().getPlano5();
            FTipoDocumento fTipoDocumento = (FTipoDocumento) dB.pesquisaCodigo(13, "FTipoDocumento");
            float valor = Moeda.substituiVirgulaFloat(valorString);
            Lote lote = new Lote(
                    -1,
                    (Rotina) dB.pesquisaCodigo(215, "Rotina"),
                    "R",
                    DataHoje.data(),
                    conviteMovimento.getPessoa(),
                    plano5,
                    false,
                    "",
                    valor,
                    conviteMovimento.getConviteServico().getServicos().getFilial(),
                    null,
                    null,
                    "",
                    (FTipoDocumento) dB.pesquisaCodigo(13, "FTipoDocumento"),
                    (CondicaoPagamento) dB.pesquisaCodigo(1, "CondicaoPagamento"),
                    (FStatus) dB.pesquisaCodigo(1, "FStatus"),
                    null,
                    false,
                    null
            );
            try {
                String nrCtrBoletoResp = "";
                for (int x = 0; x < (Integer.toString(conviteMovimento.getPessoa().getId())).length(); x++) {
                    nrCtrBoletoResp += 0;
                }
                nrCtrBoletoResp += conviteMovimento.getPessoa().getId();
                String mes = conviteMovimento.getEmissao().substring(3, 5);
                String ano = conviteMovimento.getEmissao().substring(6, 10);
                referencia = mes + "/" + ano;
                Evt evt = new Evt();
                evt.setDescricao("CONVITE MOVIMENTO");
                if (!dB.inserirObjeto(evt)) {
                    return false;
                }
                lote.setEvt(evt);
                if (!dB.inserirObjeto(lote)) {
                    return false;
                }
                String nrCtrBoleto = nrCtrBoletoResp + Long.toString(DataHoje.calculoDosDias(DataHoje.converte("07/10/1997"), DataHoje.converte(vencimento)));
                movimento = new Movimento(
                        -1,
                        lote,
                        plano5,
                        conviteMovimento.getPessoa(),
                        conviteMovimento.getConviteServico().getServicos(),
                        null,
                        (TipoServico) dB.pesquisaCodigo(1, "TipoServico"),
                        null,
                        valor,
                        referencia,
                        conviteMovimento.getEmissao(),
                        1,
                        true,
                        "E",
                        false,
                        conviteMovimento.getPessoa(), // TITULAR / RESPONSÁVEL
                        conviteMovimento.getPessoa(), // BENEFICIÁRIO
                        "",
                        nrCtrBoleto,
                        vencimento,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        fTipoDocumento,
                        0
                );
                if (dB.inserirObjeto(movimento)) {
                    conviteMovimento.setEvt(evt);
                    return dB.alterarObjeto(conviteMovimento);
                } else {
                    return false;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    public boolean isDesabilitaCamposMovimento() {
        return desabilitaCamposMovimento;
    }

    public void setDesabilitaCamposMovimento(boolean desabilitaCamposMovimento) {
        this.desabilitaCamposMovimento = desabilitaCamposMovimento;
    }

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

    public String baixarMovimento() {
        if (getMovimento().getId() != -1) {
            List list = new ArrayList();
            movimento.setValorBaixa(movimento.getValor());
            list.add(movimento);
            GenericaSessao.put("listaMovimento", list);
            return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
        }
        return null;
    }

    public Movimento getMovimento() {
        if (conviteMovimento.getEvt() != null) {
            LoteDB loteDB = new LoteDBToplink();
            Lote lote = (Lote) loteDB.pesquisaLotePorEvt(conviteMovimento.getEvt());
            MovimentoDB mdb = new MovimentoDBToplink();
            List<Movimento> movimentos = (List<Movimento>) mdb.listaMovimentosDoLote(lote.getId());
            for (Movimento m : movimentos) {
                movimento = m;
                break;
            }
            if (movimento.getBaixa() != null) {
                desabilitaBaixa = true;
            } else {
                desabilitaBaixa = false;
            }
        }
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public boolean isDesabilitaBaixa() {
        return desabilitaBaixa;
    }

    public void setDesabilitaBaixa(boolean desabilitaBaixa) {
        this.desabilitaBaixa = desabilitaBaixa;
    }
}
