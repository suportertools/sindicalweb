package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.GrupoFinanceiro;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.SubGrupoFinanceiro;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public final class GrupoFinanceiroBean implements Serializable {

    private int idGrupo = 0;
    private List<SelectItem> listaGrupo = new ArrayList<SelectItem>();
    private List<SubGrupoFinanceiro> listaSubGrupoFinanceiro = new ArrayList<SubGrupoFinanceiro>();
    private boolean adicionar = false;

    private SubGrupoFinanceiro linhaSubGrupoFinanceiro = new SubGrupoFinanceiro();

    private GrupoFinanceiro grupoFinanceiro = new GrupoFinanceiro();
    private SubGrupoFinanceiro subGrupoFinanceiro = new SubGrupoFinanceiro();

    public GrupoFinanceiroBean() {
    }

    public void alterarGrupo() {
        listaSubGrupoFinanceiro.clear();
        adicionar = false;
        grupoFinanceiro = new GrupoFinanceiro();
        subGrupoFinanceiro = new SubGrupoFinanceiro();
    }

    public void excluirSubGrupo() {
        if (linhaSubGrupoFinanceiro.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            NovoLog novoLog = new NovoLog();

            linhaSubGrupoFinanceiro = (SubGrupoFinanceiro) sv.pesquisaCodigo(linhaSubGrupoFinanceiro.getId(), "SubGrupoFinanceiro");

            sv.abrirTransacao();
            if (!sv.deletarObjeto(linhaSubGrupoFinanceiro)) {
                GenericaMensagem.warn("Erro", "Não foi possível deletar este Sub Grupo!");
                return;
            }
            novoLog.delete(
                    "Subgrupo Financeiro - ID: " + linhaSubGrupoFinanceiro.getId()
                    + " - Grupo Financeiro: (" + linhaSubGrupoFinanceiro.getGrupoFinanceiro().getId() + ") " + linhaSubGrupoFinanceiro.getGrupoFinanceiro().getDescricao()
                    + " - Descrição: " + linhaSubGrupoFinanceiro.getDescricao()
            );
            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Sub Grupo excluído!");
            linhaSubGrupoFinanceiro = new SubGrupoFinanceiro();
            listaSubGrupoFinanceiro.clear();
        }
    }

    public void modalExcluir(SubGrupoFinanceiro linha) {
        linhaSubGrupoFinanceiro = linha;
    }

    public void editarSubGrupo(SubGrupoFinanceiro linha) {
        subGrupoFinanceiro = linha;
    }

    public void salvarSubGrupo() {
        if (Integer.valueOf(listaGrupo.get(idGrupo).getDescription()) == 0) {
            GenericaMensagem.warn("Erro", "NÃO EXISTE Grupo para ser SALVO!");
            return;
        }

        if (subGrupoFinanceiro.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Erro", "Digite uma DESCRIÇÃO para este Sub Grupo!");
            return;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        NovoLog novoLog = new NovoLog();
        sv.abrirTransacao();

        grupoFinanceiro = (GrupoFinanceiro) sv.pesquisaCodigo(Integer.valueOf(listaGrupo.get(idGrupo).getDescription()), "GrupoFinanceiro");
        subGrupoFinanceiro.setGrupoFinanceiro(grupoFinanceiro);

        if (subGrupoFinanceiro.getId() == -1) {
            if (!sv.inserirObjeto(subGrupoFinanceiro)) {
                GenericaMensagem.warn("Erro", "Não foi possivel SALVAR Sub Grupo!");
                sv.desfazerTransacao();
                return;
            }
            novoLog.save(
                    "Subgrupo Financeiro - ID: " + subGrupoFinanceiro.getId()
                    + " - Grupo Financeiro: (" + subGrupoFinanceiro.getGrupoFinanceiro().getId() + ") " + subGrupoFinanceiro.getGrupoFinanceiro().getDescricao()
                    + " - Descrição: " + subGrupoFinanceiro.getDescricao()
            );
        } else {
            SubGrupoFinanceiro sgf = (SubGrupoFinanceiro) sv.pesquisaObjeto(subGrupoFinanceiro.getId(), "SubGrupoFinanceiro");
            String beforeUpdate
                    = "Subgrupo Financeiro - ID: " + sgf.getId()
                    + " - Grupo Financeiro: (" + sgf.getGrupoFinanceiro().getId() + ") " + sgf.getGrupoFinanceiro().getDescricao()
                    + " - Descrição: " + sgf.getDescricao();
            if (!sv.alterarObjeto(subGrupoFinanceiro)) {
                GenericaMensagem.warn("Erro", "Não foi possivel ATUALIZAR Sub Grupo!");
                sv.desfazerTransacao();
                return;
            }
            novoLog.update(beforeUpdate,
                    "Subgrupo Financeiro - ID: " + subGrupoFinanceiro.getId()
                    + " - Grupo Financeiro: (" + subGrupoFinanceiro.getGrupoFinanceiro().getId() + ") " + subGrupoFinanceiro.getGrupoFinanceiro().getDescricao()
                    + " - Descrição: " + subGrupoFinanceiro.getDescricao()
            );
        }

        GenericaMensagem.info("Sucesso", "Sub Grupo SALVO!");
        sv.comitarTransacao();

        adicionar = false;
        grupoFinanceiro = new GrupoFinanceiro();
        subGrupoFinanceiro = new SubGrupoFinanceiro();
        listaGrupo.clear();
        listaSubGrupoFinanceiro.clear();
    }

    public void excluirGrupo() {
        if (Integer.valueOf(listaGrupo.get(idGrupo).getDescription()) == 0) {
            GenericaMensagem.warn("Erro", "NÃO EXISTE Grupo para ser excluido!");
            return;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        NovoLog novoLog = new NovoLog();
        grupoFinanceiro = (GrupoFinanceiro) sv.pesquisaCodigo(Integer.valueOf(listaGrupo.get(idGrupo).getDescription()), "GrupoFinanceiro");
        if (!sv.deletarObjeto(grupoFinanceiro)) {
            GenericaMensagem.warn("Erro", "Não foi possivel EXCLUIR Grupo!");
            grupoFinanceiro = new GrupoFinanceiro();
            sv.desfazerTransacao();
            return;
        }
        novoLog.delete(
                "ID: " + grupoFinanceiro.getId()
                + " Descriçao: " + grupoFinanceiro.getDescricao()
        );
        GenericaMensagem.info("Sucesso", "Grupo EXCLUÍDO!");
        sv.comitarTransacao();
        grupoFinanceiro = new GrupoFinanceiro();
        listaGrupo.clear();
    }

    public void editarGrupo() {
        if (Integer.valueOf(listaGrupo.get(idGrupo).getDescription()) == 0) {
            GenericaMensagem.warn("Erro", "NÃO EXISTE Grupo para ser editado!");
            return;
        }

        adicionar = true;
        grupoFinanceiro = (GrupoFinanceiro) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaGrupo.get(idGrupo).getDescription()), "GrupoFinanceiro");

    }

    public void salvarGrupo() {
        if (grupoFinanceiro.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Erro", "Digite uma DESCRIÇÃO para este Grupo!");
            return;
        }
        NovoLog novoLog = new NovoLog();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (grupoFinanceiro.getId() == -1) {
            if (!sv.inserirObjeto(grupoFinanceiro)) {
                GenericaMensagem.warn("Erro", "Não foi possivel ADICIONAR Grupo!");
                sv.desfazerTransacao();
                return;
            }
            novoLog.save(
                    "ID: " + grupoFinanceiro.getId()
                    + " - Descriçao: " + grupoFinanceiro.getDescricao()
            );
            GenericaMensagem.info("Sucesso", "Grupo SALVO!");
        } else {
            GrupoFinanceiro gf = (GrupoFinanceiro) sv.pesquisaObjeto(grupoFinanceiro.getId(), "GrupoFinanceiro");
            String beforeUpdate
                    = "ID: " + gf.getId()
                    + " - Descriçao: " + gf.getDescricao();
            if (!sv.alterarObjeto(grupoFinanceiro)) {
                GenericaMensagem.warn("Erro", "Não foi possivel ALTERAR Grupo!");
                sv.desfazerTransacao();
                return;
            }
            novoLog.update(beforeUpdate,
                    "ID: " + grupoFinanceiro.getId()
                    + " - Descriçao: " + grupoFinanceiro.getDescricao()
            );
            GenericaMensagem.info("Sucesso", "Grupo ATUALIZADO!");
        }

        sv.comitarTransacao();

        adicionar = false;
        grupoFinanceiro = new GrupoFinanceiro();
        listaGrupo.clear();
        listaSubGrupoFinanceiro.clear();
    }

    public void adicionarGrupo() {
        adicionar = true;
        grupoFinanceiro = new GrupoFinanceiro();
    }

    public void ocultarGrupo() {
        adicionar = false;
        grupoFinanceiro = new GrupoFinanceiro();
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public List<SelectItem> getListaGrupo() {
        if (listaGrupo.isEmpty()) {
            List<GrupoFinanceiro> result = new SalvarAcumuladoDBToplink().listaObjeto("GrupoFinanceiro");

            if (!result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    listaGrupo.add(new SelectItem(i,
                            result.get(i).getDescricao(),
                            Integer.toString(result.get(i).getId()))
                    );
                }
            } else {
                listaGrupo.add(new SelectItem(0, "Nenhum Grupo Financeiro Adicionado", "0"));
            }
        }
        return listaGrupo;
    }

    public void setListaGrupo(List<SelectItem> listaGrupo) {
        this.listaGrupo = listaGrupo;
    }

    public List<SubGrupoFinanceiro> getListaSubGrupoFinanceiro() {
        if (listaSubGrupoFinanceiro.isEmpty()) {
            FinanceiroDB db = new FinanceiroDBToplink();

            //listaSubGrupoFinanceiro = new SalvarAcumuladoDBToplink().listaObjeto("SubGrupoFinanceiro");
            listaSubGrupoFinanceiro = db.listaSubGrupo(Integer.valueOf(listaGrupo.get(idGrupo).getDescription()));

        }
        return listaSubGrupoFinanceiro;
    }

    public void setListaSubGrupoFinanceiro(List<SubGrupoFinanceiro> listaSubGrupoFinanceiro) {
        this.listaSubGrupoFinanceiro = listaSubGrupoFinanceiro;
    }

    public boolean isAdicionar() {
        return adicionar;
    }

    public void setAdicionar(boolean adicionar) {
        this.adicionar = adicionar;
    }

    public GrupoFinanceiro getGrupoFinanceiro() {
        return grupoFinanceiro;
    }

    public void setGrupoFinanceiro(GrupoFinanceiro grupoFinanceiro) {
        this.grupoFinanceiro = grupoFinanceiro;
    }

    public SubGrupoFinanceiro getSubGrupoFinanceiro() {
        return subGrupoFinanceiro;
    }

    public void setSubGrupoFinanceiro(SubGrupoFinanceiro subGrupoFinanceiro) {
        this.subGrupoFinanceiro = subGrupoFinanceiro;
    }

}
