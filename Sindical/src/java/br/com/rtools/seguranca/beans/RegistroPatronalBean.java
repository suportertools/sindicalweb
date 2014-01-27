package br.com.rtools.seguranca.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.Patronal;
import br.com.rtools.arrecadacao.PatronalCnae;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
import br.com.rtools.arrecadacao.db.WebREPISDB;
import br.com.rtools.arrecadacao.db.WebREPISDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDBToplink;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.Upload;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class RegistroPatronalBean implements Serializable {

    private Patronal patronal = new Patronal();
    private String mensagem = "";
    private List<Patronal> listaPatronal = new ArrayList();
    private String descricaoPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "";
    private String logoPatronal = "";
    private List<PatronalCnae> listaPatronalCnae = new ArrayList();
    private Cnae cnae = new Cnae();

    public void novo() {
        patronal = new Patronal();
        mensagem = "";
        listaPatronalCnae.clear();
        logoPatronal = "";
    }

    public void salvar() {
        if (patronal.getPessoa().getId() == -1) {
            setMensagem("Pesquisar pessoa!");
            return;
        }
        if (patronal.getConvencao().getId() == -1) {
            setMensagem("Pesquisar convenção!");
            return;
        }
        if (patronal.getGrupoCidade().getId() == -1) {
            setMensagem("Pesquisar grupo cidade!");
            return;
        }
        if (patronal.getId() == -1) {
            if (validaPatronal(patronal.getPessoa().getId(),
                    patronal.getConvencao().getId(),
                    patronal.getGrupoCidade().getId()).size() > 0) {
                setMensagem("Pessoa já cadastrada para essa convenção / grupo cidade!");
                return;
            }
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        NovoLog log = new NovoLog();
        sv.abrirTransacao();
        if (patronal.getId() == -1) {
            if (!sv.inserirObjeto(patronal)) {
                sv.desfazerTransacao();
                setMensagem("Erro ao salvar patronal!");
                return;
            }
            log.novo("Novo registro", "Mensagem patronal inserido " + patronal.getId() + " - Pessoa: " + patronal.getPessoa().getId() + " - " + patronal.getPessoa().getNome() + " - Convenção: " + patronal.getConvencao().getId() + " - " + patronal.getConvencao().getDescricao() + " - Grupo Cidade: " + patronal.getGrupoCidade().getId() + " - " + patronal.getGrupoCidade().getDescricao());
            setMensagem("Patronal salvo com Sucesso!");
        } else {
            if (!sv.alterarObjeto(patronal)) {
                sv.desfazerTransacao();
                setMensagem("Erro ao atualizar patronal!");
                return;
            }
            Patronal patro = (Patronal) sv.pesquisaCodigo(getPatronal().getId(), "Patronal");
            String antes = "De - Referencia: " + patro.getId() + " Pessoa: " + patro.getPessoa().getId() + " - " + patro.getPessoa().getNome() + " - Convenção: " + patro.getConvencao().getId() + " - " + patro.getConvencao().getDescricao() + " - Grupo Cidade: " + patro.getGrupoCidade().getId() + " - " + patro.getGrupoCidade().getDescricao();
            log.novo("Atualizado", antes + " - para: " + patronal.getPessoa().getId() + " - " + patronal.getPessoa().getNome() + " - Convenção: " + patronal.getConvencao().getId() + " - " + patronal.getConvencao().getDescricao() + " - Grupo Cidade: " + patronal.getGrupoCidade().getId() + " - " + patronal.getGrupoCidade().getDescricao());
            setMensagem("Patronal atualizado com Sucesso!");
        }

        for (PatronalCnae listaPatronalCnae1 : listaPatronalCnae) {
            if (listaPatronalCnae1.getId() == -1) {
                if (!sv.inserirObjeto(listaPatronalCnae1)) {
                    mensagem = "Erro ao salvar Cnae!";
                    sv.desfazerTransacao();
                    return;
                }
            }
        }

        sv.comitarTransacao();
    }

    public List validaPatronal(int idPessoa, int idConvencao, int idGCidade) {
        FilialDB db = new FilialDBToplink();
        List result = db.pesquisaPessoaConvencaoGCidade(idPessoa, idConvencao, idGCidade);
        return result;
    }

    public String editar(Patronal p) {
        patronal = p;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("patronalPesquisa", getPatronal());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
    }

    public void excluirCnae(PatronalCnae pc) {
        if (pc.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            if (!sv.deletarObjeto(sv.pesquisaCodigo(pc.getId(), "PatronalCnae"))) {
                sv.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
                return;
            }
            sv.comitarTransacao();
            for (int i = 0; i < listaPatronalCnae.size(); i++) {
                if (pc.getId() == listaPatronalCnae.get(i).getId()) {
                    listaPatronalCnae.remove(i);
                    break;
                }
            }
            mensagem = "Excluído com sucesso!";
            GenericaMensagem.info("Sucesso", "Registro excluído");
        }
    }

    public void excluir() {
        if (patronal.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            NovoLog log = new NovoLog();
            sv.abrirTransacao();
            patronal = (Patronal) sv.pesquisaCodigo(patronal.getId(), "Patronal");
            if (sv.deletarObjeto(patronal)) {
                sv.comitarTransacao();
                setMensagem("Patronal Excluído com sucesso!");
                log.novo("Excluido", patronal.getId() + " - Pessoa: " + patronal.getPessoa().getId() + " - " + patronal.getPessoa().getNome());
                patronal = new Patronal();
                logoPatronal = "";
                listaPatronal.clear();
                listaPatronalCnae.clear();
            } else {
                sv.desfazerTransacao();
                setMensagem("Patronal não pode ser excluido!");
            }
        }
        patronal = new Patronal();
    }

    public Patronal getPatronal() {
        if (GenericaSessao.exists("patronalPesquisa")) {
            patronal = (Patronal) GenericaSessao.getObject("patronalPesquisa", true);
        }
        if (GenericaSessao.exists("juridicaPesquisa")) {
            patronal.setPessoa(((Juridica) GenericaSessao.getObject("juridicaPesquisa", true)).getPessoa());
        }
        if (GenericaSessao.exists("convencaoPesquisa")) {
            patronal.setConvencao((Convencao) GenericaSessao.getObject("convencaoPesquisa", true));
        }
        if (GenericaSessao.exists("simplesPesquisa")) {
            patronal.setGrupoCidade((GrupoCidade) GenericaSessao.getObject("simplesPesquisa", true));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesPesquisa");
        }
        return patronal;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listaPatronal.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listaPatronal.clear();
    }

    public void setPatronal(Patronal patronal) {
        this.patronal = patronal;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<Patronal> getListaPatronal() {
        if (listaPatronal.isEmpty()) {
            FilialDB db = new FilialDBToplink();
            String por = porPesquisa;
            if (porPesquisa.equals("cnpj")) {
                por = "documento";
            }
            listaPatronal = db.pesquisaPessoaPatronal(descricaoPesquisa, por, comoPesquisa);
        }
        return listaPatronal;
    }

    public void setListaPatronal(List<Patronal> listaPatronal) {
        this.listaPatronal = listaPatronal;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public void addCnae() {
        if (patronal.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            if (patronal.getGrupoCidade().getId() == -1) {
                mensagem = "Pesquise um Grupo Cidade!";
                return;
            }
            if (cnae.getId() == -1) {
                mensagem = "Pesquise um Cnae!";
                return;
            }
            WebREPISDB db = new WebREPISDBToplink();

            if (!db.pesquisaCnaePermitido(cnae.getId(), patronal.getGrupoCidade().getId())) {
                mensagem = "Cnae já existente em outro Grupo Cidade!";
                return;
            }
            sv.abrirTransacao();
            PatronalCnae pc = new PatronalCnae(-1, patronal, cnae);

            if (!sv.inserirObjeto(pc)) {
                mensagem = "Erro ao inserir patronal cnae!";
                sv.desfazerTransacao();
                return;
            } else {
                mensagem = "Inserido com sucesso!";
                listaPatronalCnae.add(pc);
            }
            sv.comitarTransacao();
            cnae = new Cnae();
        }
    }

    public List<PatronalCnae> getListaPatronalCnae() {
        if (listaPatronalCnae.isEmpty() && patronal.getConvencao().getId() != -1) {
            CnaeConvencaoDB db = new CnaeConvencaoDBToplink();
            List<PatronalCnae> listap = db.listaCnaePorPatronal(patronal.getId());
            if (!listap.isEmpty()) {
                listaPatronalCnae.addAll(listap);
            } else {
                List<Cnae> listac = db.listaCnaePorConvencao(patronal.getConvencao().getId());
                if (!listac.isEmpty()) {
                    for (Cnae listac1 : listac) {
                        listaPatronalCnae.add(new PatronalCnae(-1, patronal, listac1));
                    }
                }
            }
        }
        return listaPatronalCnae;
    }

    public void setListaPatronalCnae(List<PatronalCnae> listaPatronalCnae) {
        this.listaPatronalCnae = listaPatronalCnae;
    }

    public Cnae getCnae() {
        if (GenericaSessao.exists("cnaePesquisado")) {
            cnae = (Cnae) GenericaSessao.getObject("cnaePesquisado", true);
        }
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public void upload(FileUploadEvent event) {
        if (patronal.getId() != -1) {
            ConfiguracaoUpload cu = new ConfiguracaoUpload();
            cu.setArquivo(event.getFile().getFileName());
            cu.setDiretorio("Imagens/LogoPatronal"); 
            cu.setArquivo(porPesquisa); 
            cu.setSubstituir(true);
            cu.setRenomear(patronal.getId()+".png");
            cu.setEvent(event);
            Upload.enviar(cu, true);
        }
    }

    public String getLogoPatronal() {
        if (patronal.getId() != -1) {
            logoPatronal = Diretorio.arquivo("Imagens/LogoPatronal/", "" + patronal.getId()+".png");
            if (logoPatronal == null) {
                logoPatronal = "";
            } else {
                logoPatronal = patronal.getId()+".png";
            }
        }
        return logoPatronal;
    }

    public void setLogoPatronal(String logoPatronal) {
        this.logoPatronal = logoPatronal;
    }
    
    public String getMascaraPesquisa(){
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }      
}
