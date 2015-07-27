package br.com.rtools.seguranca.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.Patronal;
import br.com.rtools.arrecadacao.PatronalConvencao;
import br.com.rtools.financeiro.SalarioMinimo;
import br.com.rtools.financeiro.dao.SalarioMinimoDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDao;
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
    private PatronalConvencao patronalConvencao;
    private String message;
    private List<Patronal> listaPatronal;
    private String descricaoPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String logoPatronal;
    private List<PatronalConvencao> listaPatronalConvencao;
    private Convencao convencao;
    private GrupoCidade grupoCidade;

    @PostConstruct
    public void init() {
        patronal = new Patronal();
        patronalConvencao = new PatronalConvencao();
        message = "";
        listaPatronal = new ArrayList<Patronal>();
        descricaoPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        logoPatronal = "";
        listaPatronalConvencao = new ArrayList<PatronalConvencao>();
        convencao = new Convencao();
        grupoCidade = new GrupoCidade();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("registroPatronalBean");
        GenericaSessao.remove("patronalPesquisa");
        GenericaSessao.remove("cnaePesquisado");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("simplesPesquisa");
        GenericaSessao.remove("convencaoPesquisa");
        GenericaSessao.remove("grupoCidadePesquisa");
    }

    public void clear() {
        GenericaSessao.remove("registroPatronalBean");
    }

    public void novo() {
        patronal = new Patronal();
        message = "";
        listaPatronalConvencao.clear();
        logoPatronal = "";
        grupoCidade = new GrupoCidade();
        convencao = new Convencao();
    }

    public void save() {
        if (patronal.getPessoa().getId() == -1) {
            message = "Pesquisar pessoa!";
            return;
        }
        message = "";
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        novoLog.startList();
        di.openTransaction();
        if (patronal.getId() == -1) {
            if (!di.save(patronal)) {
                novoLog.cancelList();
                di.rollback();
                message = "Erro ao salvar patronal!";
                return;
            }
            novoLog.save("ID: " + patronal.getId() + " - Pessoa: (" + patronal.getPessoa().getId() + ") - " + patronal.getPessoa().getNome());
            message = "Patronal salvo com Sucesso!";
            listaPatronal.clear();
        } else {
            if (!di.update(patronal)) {
                novoLog.cancelList();
                di.rollback();
                message = "Erro ao atualizar patronal!";
                return;
            }
            Patronal patro = (Patronal) di.find(patronal);
            String beforeUpdate = "ID: " + patro.getId() + " - Pessoa: (" + patro.getPessoa().getId() + ") " + patro.getPessoa().getNome();
            novoLog.update(beforeUpdate, "ID: " + patronal.getId() + " - Pessoa: (" + patronal.getPessoa().getId() + ") " + patronal.getPessoa().getNome());
            message = "Patronal atualizado com Sucesso!";
        }

        for (PatronalConvencao pc : listaPatronalConvencao) {
            if (pc.getId() == -1) {
                if (pc.getPatronal().getId() == -1) {
                    pc.setPatronal(patronal);
                }
                if (!di.save(pc)) {
                    di.rollback();
                    novoLog.cancelList();
                    message = "Erro ao inserir / atualizar registro!";
                    return;
                }
                novoLog.save("Patronal Convenção - ID: " + pc.getId()
                        + " - Patronal: (" + pc.getPatronal().getId() + ") " + pc.getPatronal().getPessoa().getNome()
                        + " - Convenção: (" + pc.getConvencao().getId() + ") " + pc.getConvencao().getDescricao()
                        + " - Grupo Cidade: (" + pc.getGrupoCidade().getId() + ") " + pc.getGrupoCidade().getDescricao()
                );
            }
        }
        novoLog.saveList();
        listaPatronalConvencao.clear();
        di.commit();
    }

    public List validaPatronalConvencao(int idPessoa, int idConvencao, int idGCidade) {
        FilialDB db = new FilialDao();
        List result = db.pesquisaPessoaConvencaoGCidade(idPessoa, idConvencao, idGCidade);
        return result;
    }

    public String edit(Patronal p) {
        patronal = p;
        GenericaSessao.put("patronalPesquisa", getPatronal());
        GenericaSessao.put("linkClicado", true);
        return GenericaSessao.getString("urlRetorno");
    }

    public void deletePatronalConvencao(int index) {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        for (int i = 0; i < listaPatronalConvencao.size(); i++) {
            if (i == index) {
                if (listaPatronalConvencao.get(i).getId() != -1) {
                    if (!di.delete(listaPatronalConvencao.get(i))) {
                        di.rollback();
                        message = "Erro ao excluir registro!";
                        GenericaMensagem.warn("Erro", "Ao excluir registro!");
                        return;
                    }
                    novoLog.delete("Patronal Convenção - ID: " + listaPatronalConvencao.get(i).getId()
                            + " - Patronal: (" + listaPatronalConvencao.get(i).getPatronal().getId() + ") " + listaPatronalConvencao.get(i).getPatronal().getPessoa().getNome()
                            + " - Convenção: (" + listaPatronalConvencao.get(i).getConvencao().getId() + ") " + listaPatronalConvencao.get(i).getConvencao().getDescricao()
                            + " - Grupo Cidade: (" + listaPatronalConvencao.get(i).getGrupoCidade().getId() + ") " + listaPatronalConvencao.get(i).getGrupoCidade().getDescricao()
                    );
                }
                listaPatronalConvencao.remove(i);
            }

        }
        di.commit();
        message = "Registro excluído com sucesso";
        GenericaMensagem.info("Sucesso", "Registro excluído");
    }

    public void delete() {
        if (patronal.getId() != -1) {
            DaoInterface di = new Dao();
            NovoLog novoLog = new NovoLog();
            di.openTransaction();
            for (PatronalConvencao pcs : listaPatronalConvencao) {
                if (!di.delete(pcs)) {
                    message = "Erro ao excluir patronal convensão!";
                    GenericaMensagem.warn("Erro", message);
                    di.rollback();
                    return;
                }
            }
            if (di.delete(patronal)) {
                di.commit();
                setMessage("Patronal Excluído com sucesso!");
                novoLog.delete("ID: " + patronal.getId() + " - Pessoa: (" + patronal.getPessoa().getId() + ") " + patronal.getPessoa().getNome());
                patronal = new Patronal();
                logoPatronal = "";
                listaPatronal.clear();
                listaPatronalConvencao.clear();
                setPatronal(new Patronal());
                listaPatronalConvencao.clear();
                listaPatronal.clear();
            } else {
                di.rollback();
                setMessage("Patronal não pode ser excluido!");
                GenericaMensagem.warn("Erro", message);
            }
        } else {
            GenericaMensagem.warn("Erro", "Pesquise um Patronal para ser excluído!");
        }
        patronal = new Patronal();
    }

    public void add() {
        if (convencao.getId() == -1) {
            setMessage("Pesquisar convenção!");
            return;
        }
        if (grupoCidade.getId() == -1) {
            setMessage("Pesquisar grupo cidade!");
            return;
        }
        patronalConvencao = new PatronalConvencao();
        patronalConvencao.setPatronal(patronal);
        patronalConvencao.setConvencao(convencao);
        patronalConvencao.setGrupoCidade(grupoCidade);
        grupoCidade = new GrupoCidade();
        DaoInterface di = new Dao();
        List<PatronalConvencao> pcs = (List<PatronalConvencao>) di.list(new PatronalConvencao());
        for (PatronalConvencao pc : pcs) {
            if (pc.getGrupoCidade().getId() == patronalConvencao.getGrupoCidade().getId() && pc.getConvencao().getId() == patronalConvencao.getConvencao().getId()) {
                GenericaMensagem.warn("Validação", "Convenão e Grupo Cidade já adicionado!");
                message = "Validação: Convenão e Grupo Cidade já adicionado!";
                return;
            }
        }
        if (patronal.getId() == -1) {
            listaPatronalConvencao.add(patronalConvencao);
            message = "Item adicionado a lista";
        } else {
            NovoLog novoLog = new NovoLog();
            if (di.save(patronalConvencao, true)) {
                message = "Item salvo com sucesso a lista";
                listaPatronalConvencao.clear();
                novoLog.save("Patronal Convenção - ID: " + patronalConvencao.getId()
                        + " - Patronal: (" + patronalConvencao.getPatronal().getId() + ") " + patronalConvencao.getPatronal().getPessoa().getNome()
                        + " - Convenção: (" + patronalConvencao.getConvencao().getId() + ") " + patronalConvencao.getConvencao().getDescricao()
                        + " - Grupo Cidade: (" + patronalConvencao.getGrupoCidade().getId() + ") " + patronalConvencao.getGrupoCidade().getDescricao()
                );
                patronalConvencao = new PatronalConvencao();
            } else {
                message = "Erro ao inserir regostro!";
            }
        }
        GenericaMensagem.info("Validação", message);
    }

    public Patronal getPatronal() {
        if (GenericaSessao.exists("patronalPesquisa")) {
            patronal = (Patronal) GenericaSessao.getObject("patronalPesquisa", true);
            listaPatronalConvencao.clear();
        }
        if (GenericaSessao.exists("juridicaPesquisa")) {
            patronal.setPessoa(((Juridica) GenericaSessao.getObject("juridicaPesquisa", true)).getPessoa());
            DaoInterface di = new Dao();
            List<PatronalConvencao> list = (List<PatronalConvencao>) di.listQuery(new PatronalConvencao(), "findPorPessoaPatronal", new Object[]{patronal.getPessoa().getId()});
            if (!list.isEmpty()) {
                patronal = list.get(0).getPatronal();
            }
            listaPatronalConvencao.clear();
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
            patronalConvencao.setConvencao(new Convencao());
        }
    }

    public void removerGrupo() {
        if (patronal.getId() == -1) {
            patronalConvencao.setGrupoCidade(new GrupoCidade());
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Patronal> getListaPatronal() {
        if (listaPatronal.isEmpty()) {
            FilialDB db = new FilialDao();
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

    public void deleteCnae() {
        patronalConvencao = new PatronalConvencao();
    }

    public List<PatronalConvencao> getListaPatronalConvencao() {
        if (listaPatronalConvencao.isEmpty()) {
            if (patronal.getId() != -1) {
                DaoInterface di = new Dao();
                listaPatronalConvencao = (List<PatronalConvencao>) di.listQuery(new PatronalConvencao(), "findPorPessoaPatronal", new Object[]{patronal.getPessoa().getId()});
            }
        }
        return listaPatronalConvencao;
    }

    public void setListaPatronalConvencao(List<PatronalConvencao> listaPatronalConvencao) {
        this.listaPatronalConvencao = listaPatronalConvencao;
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

    public PatronalConvencao getPatronalConvencao() {
        return patronalConvencao;
    }

    public void setPatronalConvencao(PatronalConvencao patronalConvencao) {
        this.patronalConvencao = patronalConvencao;
    }

    public Convencao getConvencao() {
        if (GenericaSessao.exists("convencaoPesquisa")) {
            convencao = (Convencao) GenericaSessao.getObject("convencaoPesquisa", true);
        }
        return convencao;
    }

    public void setConvencao(Convencao convencao) {
        this.convencao = convencao;
    }

    public GrupoCidade getGrupoCidade() {
        if (GenericaSessao.exists("simplesPesquisa")) {
            grupoCidade = (GrupoCidade) GenericaSessao.getObject("simplesPesquisa", true);
        }
        return grupoCidade;
    }

    public void setGrupoCidade(GrupoCidade grupoCidade) {
        this.grupoCidade = grupoCidade;
    }
}
