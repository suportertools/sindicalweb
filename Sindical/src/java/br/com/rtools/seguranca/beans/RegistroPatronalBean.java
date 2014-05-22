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
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDBToplink;
import br.com.rtools.sistema.ConfiguracaoUpload;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.Upload;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class RegistroPatronalBean implements Serializable {

    private Patronal patronal;
    private String mensagem;
    private List<Patronal> listaPatronal;
    private String descricaoPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String logoPatronal;
    private List<PatronalCnae> listaPatronalCnae;
    private Cnae cnae;

    @PostConstruct
    public void init() {
        patronal = new Patronal();
        mensagem = "";
        listaPatronal = new ArrayList<Patronal>();
        descricaoPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        logoPatronal = "";
        listaPatronalCnae = new ArrayList<PatronalCnae>();
        cnae = new Cnae();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("registroPatronalBean");
        GenericaSessao.remove("patronalPesquisa");
        GenericaSessao.remove("cnaePesquisado");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("simplesPesquisa");
        GenericaSessao.remove("convencaoPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("registroPatronalBean");
    }

    public void novo() {
        patronal = new Patronal();
        mensagem = "";
        listaPatronalCnae.clear();
        logoPatronal = "";
    }

    public void save() {
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
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (patronal.getId() == -1) {
            if (!di.save(patronal)) {
                di.rollback();
                setMensagem("Erro ao salvar patronal!");
                return;
            }
            novoLog.save("ID: " + patronal.getId() + " - Pessoa: (" + patronal.getPessoa().getId() + ") - " + patronal.getPessoa().getNome() + " - Convenção: (" + patronal.getConvencao().getId() + " ) " + patronal.getConvencao().getDescricao() + " - Grupo Cidade: (" + patronal.getGrupoCidade().getId() + ") " + patronal.getGrupoCidade().getDescricao());
            setMensagem("Patronal salvo com Sucesso!");
            GenericaMensagem.info("Sucesso", mensagem);
            listaPatronal.clear();
        } else {
            if (!di.update(patronal)) {
                di.rollback();
                setMensagem("Erro ao atualizar patronal!");
                return;
            }
            Patronal patro = (Patronal) di.find(getPatronal());
            String beforeUpdate = "ID: " + patro.getId() + " - Pessoa: (" + patro.getPessoa().getId() + ") " + patro.getPessoa().getNome() + " - Convenção: (" + patro.getConvencao().getId() + ") " + patro.getConvencao().getDescricao() + " - Grupo Cidade: (" + patro.getGrupoCidade().getId() + ") " + patro.getGrupoCidade().getDescricao();
            novoLog.update(beforeUpdate, "ID: " + patronal.getId() + " - Pessoa: (" + patronal.getPessoa().getId() + ") " + patronal.getPessoa().getNome() + " - Convenção: (" + patronal.getConvencao().getId() + ") " + patronal.getConvencao().getDescricao() + " - Grupo Cidade: (" + patronal.getGrupoCidade().getId() + ") " + patronal.getGrupoCidade().getDescricao());
            setMensagem("Patronal atualizado com Sucesso!");
            GenericaMensagem.info("Sucesso", mensagem);
        }

        for (PatronalCnae listaPatronalCnae1 : listaPatronalCnae) {
            if (listaPatronalCnae1.getId() == -1) {
                if (!di.save(listaPatronalCnae1)) {
                    mensagem = "Erro ao salvar Cnae!";
                    GenericaMensagem.warn("Erro", mensagem);
                    di.rollback();
                    return;
                }
            }
        }
        di.commit();
    }

    public List validaPatronal(int idPessoa, int idConvencao, int idGCidade) {
        FilialDB db = new FilialDBToplink();
        List result = db.pesquisaPessoaConvencaoGCidade(idPessoa, idConvencao, idGCidade);
        return result;
    }

    public String edit(Patronal p) {
        patronal = p;
        GenericaSessao.put("patronalPesquisa", getPatronal());
        GenericaSessao.put("linkClicado", true);
        return GenericaSessao.getString("urlRetorno");
    }

    public void deleteCnae(PatronalCnae pc) {
        if (pc.getId() != -1) {
            DaoInterface di = new Dao();
            di.openTransaction();
            if (!di.delete(pc)) {
                GenericaMensagem.warn("Erro", mensagem);
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
                return;
            }
            di.commit();
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

    public void delete() {
        if (patronal.getId() != -1) {
            DaoInterface di = new Dao();
            NovoLog novoLog = new NovoLog();
            di.openTransaction();
            if (di.delete(patronal)) {
                di.commit();
                setMensagem("Patronal Excluído com sucesso!");
                novoLog.delete("ID: " + patronal.getId() + " - Pessoa: (" + patronal.getPessoa().getId() + ") " + patronal.getPessoa().getNome());
                patronal = new Patronal();
                logoPatronal = "";
                listaPatronal.clear();
                listaPatronalCnae.clear();
                setPatronal(new Patronal());
                listaPatronalCnae.clear();
                listaPatronal.clear();
                cnae = new Cnae();
            } else {
                di.rollback();
                setMensagem("Patronal não pode ser excluido!");
                GenericaMensagem.warn("Erro", mensagem);
            }
        } else {
            GenericaMensagem.warn("Erro", "Pesquise um Patronal para ser excluído!");
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
        }
        return patronal;
    }

    public void removerJuridica() {
        if (patronal.getId() == -1) {
            patronal.setPessoa(new Pessoa());
        }
    }

    public void removerConvencao() {
        if (patronal.getId() == -1) {
            patronal.setConvencao(new Convencao());
        }
    }

    public void removerGrupo() {
        if (patronal.getId() == -1) {
            patronal.setGrupoCidade(new GrupoCidade());
        }
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
            DaoInterface di = new Dao();
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
            di.openTransaction();
            PatronalCnae pc = new PatronalCnae(-1, patronal, cnae);

            if (!di.save(pc)) {
                mensagem = "Erro ao inserir patronal cnae!";
                GenericaMensagem.warn("Erro", mensagem);
                di.rollback();
                return;
            } else {
                mensagem = "Inserido com sucesso!";
                GenericaMensagem.info("Sucesso", mensagem);
                listaPatronalCnae.add(pc);
            }
            di.commit();
            cnae = new Cnae();
        }
    }

    public void deleteCnae() {
        cnae = new Cnae();
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
            cu.setRenomear(patronal.getId() + ".png");
            cu.setEvent(event);
            Upload.enviar(cu, true);
        }
    }

    public String getLogoPatronal() {
        if (patronal.getId() != -1) {
            logoPatronal = Diretorio.arquivo("Imagens/LogoPatronal/", "" + patronal.getId() + ".png");
            if (logoPatronal == null) {
                logoPatronal = "";
            } else {
                logoPatronal = patronal.getId() + ".png";
            }
        }
        return logoPatronal;
    }

    public void setLogoPatronal(String logoPatronal) {
        this.logoPatronal = logoPatronal;
    }

    public String getMascaraPesquisa() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }
}
