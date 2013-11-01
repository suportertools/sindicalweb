package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConvenioServico;
import br.com.rtools.associativo.GrupoConvenio;
import br.com.rtools.associativo.SubGrupoConvenio;
import br.com.rtools.associativo.db.SubGrupoConvenioDB;
import br.com.rtools.associativo.db.SubGrupoConvenioDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class SubGrupoConvenioBean implements Serializable {

    // Tela cadastro
    private List<SelectItem> listaGrupoConvenio = new ArrayList<SelectItem>();
    private List<SelectItem> listaSubGrupoConvenio = new ArrayList<SelectItem>();
    private int idGrupoConvenio = 0;
    private int idGrupoConvenioFiltro = 0;
    private int idSubGrupoConvenio = 0;
    private String mensagem;
    private Servicos[] servicoSelecionado;
    private ConvenioServico[] convenioServicoSelecionado;
    private SubGrupoConvenio subGrupoConvenio = new SubGrupoConvenio();
    private List<Servicos> listaServicosDisponiveis = new ArrayList<Servicos>();
    private List<ConvenioServico> listaServicosAdicionados = new ArrayList<ConvenioServico>();

    // Tela pesquisa
    private List<SubGrupoConvenio> listaSubGrupoConvenios = new ArrayList<SubGrupoConvenio>();

    public String novo() {
        idGrupoConvenio = 0;
        subGrupoConvenio = new SubGrupoConvenio();
        mensagem = "";
        return null;
    }

    public void adicionar() {
        if (listaGrupoConvenio.isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar o grupo convênio!");
            return;
        }
        if (subGrupoConvenio.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Validação", "Informar a descrição do subgrupo convênio!");
            return;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        SubGrupoConvenioDB sgcdb = new SubGrupoConvenioDBToplink();
        subGrupoConvenio.setGrupoConvenio((GrupoConvenio) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(listaGrupoConvenio.get(idGrupoConvenio).getDescription()), "GrupoConvenio"));
        if (subGrupoConvenio.getId() == -1) {
            if (sgcdb.existeSubGrupoConvenio(subGrupoConvenio)) {
                GenericaMensagem.info("Validação", "SubGrupo já existe!");
                return;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(subGrupoConvenio)) {
                salvarAcumuladoDB.comitarTransacao();;
                listaSubGrupoConvenio.clear();
                GenericaMensagem.info("Sucesso", "Cadastro realizado");
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Erro ao inserir este registro!");
            }
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(subGrupoConvenio)) {
                salvarAcumuladoDB.comitarTransacao();
                listaSubGrupoConvenio.clear();
                GenericaMensagem.info("Sucesso", "Registro atualizado");
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Erro ao atualizar este registro!");
            }
        }
        for (int i = 0; i < listaGrupoConvenio.size(); i++) {
            if (subGrupoConvenio.getGrupoConvenio().getId() == Integer.parseInt(listaGrupoConvenio.get(i).getDescription())) {
                idGrupoConvenioFiltro = i;
                break;
            }
        }
    }

    public void remover() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        SubGrupoConvenio sgc = (SubGrupoConvenio) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(listaSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()), "SubGrupoConvenio");
        remover(sgc);
    }

    public void remover(SubGrupoConvenio sgc) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (sgc.getId() != -1) {
            sgc = (SubGrupoConvenio) salvarAcumuladoDB.pesquisaCodigo(sgc.getId(), "SubGrupoConvenio");
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(sgc)) {
                salvarAcumuladoDB.comitarTransacao();
                listaSubGrupoConvenio.clear();
                subGrupoConvenio = new SubGrupoConvenio();
                GenericaMensagem.info("Sucesso", "Registro removido");
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                GenericaMensagem.warn("Erro", "SubGrupo convênio não pode ser excluido!");
            }
        }
    }

    public void editar() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        subGrupoConvenio = (SubGrupoConvenio) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(listaSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()), "SubGrupoConvenio");
        for (int i = 0; i < listaGrupoConvenio.size(); i++) {
            if (subGrupoConvenio.getGrupoConvenio().getId() == Integer.parseInt(listaGrupoConvenio.get(i).getDescription())) {
                idGrupoConvenio = i;
                break;
            }
        }
    }

    public String editar(SubGrupoConvenio sgc) {
        String url = null;
        if (GenericaSessao.exists("urlRetorno")) {
            GenericaSessao.put("linkClicado", true);
            GenericaSessao.put("subGrupoConvenioPesquisa", sgc);
            url = GenericaSessao.getString("urlRetorno");
        }
        return url;
    }

    public List<Servicos> getListaServicosDisponiveis() {
        listaServicosDisponiveis.clear();
        if (!listaSubGrupoConvenio.isEmpty()) {
            SubGrupoConvenioDB subGrupoConvenioDB = new SubGrupoConvenioDBToplink();
            listaServicosDisponiveis = subGrupoConvenioDB.listaServicosDisponiveis(Integer.parseInt(listaSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()));
        }
        return listaServicosDisponiveis;
    }

    public List<ConvenioServico> getListaServicosAdicionados() {
        listaServicosAdicionados.clear();
        if (!listaSubGrupoConvenio.isEmpty()) {
            SubGrupoConvenioDB subGrupoConvenioDB = new SubGrupoConvenioDBToplink();
            listaServicosAdicionados = subGrupoConvenioDB.listaServicosAdicionados(Integer.parseInt(listaSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()));
        }
        return listaServicosAdicionados;
    }

    public void adicionarConvenioServico() {
        if (servicoSelecionado.length == 0) {
            GenericaMensagem.warn("Validação", "Selecionar pelo menos um registro");
            return;
        }
        boolean sucesso = false;
        for (int i = 0; i < listaServicosDisponiveis.size(); i++) {
            for (Servicos servicos : servicoSelecionado) {
                if (servicos.getId() == listaServicosDisponiveis.get(i).getId()) {
                    ConvenioServico convenioServico = new ConvenioServico();
                    convenioServico.setServicos(listaServicosDisponiveis.get(i));
                    SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                    convenioServico.setSubGrupoConvenio((SubGrupoConvenio) salvarAcumuladoDB.pesquisaObjeto(Integer.parseInt(listaSubGrupoConvenio.get(idSubGrupoConvenio).getDescription()), "SubGrupoConvenio"));
                    salvarAcumuladoDB.abrirTransacao();
                    if (salvarAcumuladoDB.inserirObjeto(convenioServico)) {
                        sucesso = true;
                        salvarAcumuladoDB.comitarTransacao();
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                    }
                }
            }
        }
        if (sucesso) {
            GenericaMensagem.info("Sucesso", "Registro(s) adicionado(s)");
        }
    }

    public void removerConvenioServico() {
        if (convenioServicoSelecionado.length == 0) {
            GenericaMensagem.warn("Validação", "selecionar pelo menos um registro");
            return;
        }
        boolean sucesso = false;
        for (int i = 0; i < listaServicosAdicionados.size(); i++) {
            for (ConvenioServico convenioServico : convenioServicoSelecionado) {
                if (convenioServico.getId() == listaServicosAdicionados.get(i).getId()) {
                    SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
                    salvarAcumuladoDB.abrirTransacao();
                    if (salvarAcumuladoDB.deletarObjeto((ConvenioServico) salvarAcumuladoDB.pesquisaObjeto(listaServicosAdicionados.get(i).getId(), "ConvenioServico"))) {
                        sucesso = true;
                        salvarAcumuladoDB.comitarTransacao();
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                    }
                }
            }
        }
        if (sucesso) {
            GenericaMensagem.info("Sucesso", "Registro(s) removido(s)");
        }
    }

    public void atualizarConvenioServico(ConvenioServico convenioServico) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (convenioServico.isEncaminhamento()) {
            convenioServico.setEncaminhamento(false);
        } else {
            convenioServico.setEncaminhamento(true);
        }
        if (salvarAcumuladoDB.alterarObjeto(convenioServico)) {
            salvarAcumuladoDB.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Registro(s) atualizado(s)");
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Ao atualizar este registro");
        }

    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public SubGrupoConvenio getSubGrupoConvenio() {
        return subGrupoConvenio;
    }

    public List<SelectItem> getListaSubGrupoConvenio() {
        if (listaSubGrupoConvenio.isEmpty()) {
            SubGrupoConvenioDB subGrupoConvenioDB = new SubGrupoConvenioDBToplink();
            List<SubGrupoConvenio> list = subGrupoConvenioDB.listaSubGrupoConvenioPorGrupo(Integer.parseInt(listaGrupoConvenio.get(idGrupoConvenioFiltro).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listaSubGrupoConvenio.add(new SelectItem(new Integer(i), list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaSubGrupoConvenio;
    }

    public void setSubGrupoConvenio(SubGrupoConvenio subGrupoConvenio) {
        this.subGrupoConvenio = subGrupoConvenio;
    }

    public List<SelectItem> getListaGrupoConvenio() {
        if (listaGrupoConvenio.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<GrupoConvenio> list = (List<GrupoConvenio>) salvarAcumuladoDB.listaObjeto("GrupoConvenio", true);
            for (int i = 0; i < list.size(); i++) {
                listaGrupoConvenio.add(new SelectItem(new Integer(i), list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaGrupoConvenio;
    }

    public void setListaGrupoConvenio(List<SelectItem> listaGrupoConvenio) {
        this.listaGrupoConvenio = listaGrupoConvenio;
    }

    public int getIdGrupoConvenio() {
        return idGrupoConvenio;
    }

    public void setIdGrupoConvenio(int idGrupoConvenio) {
        this.idGrupoConvenio = idGrupoConvenio;
    }

    public int getIdSubGrupoConvenio() {
        return idSubGrupoConvenio;
    }

    public void setIdSubGrupoConvenio(int idSubGrupoConvenio) {
        this.idSubGrupoConvenio = idSubGrupoConvenio;
    }

    public int getIdGrupoConvenioFiltro() {
        return idGrupoConvenioFiltro;
    }

    public void setIdGrupoConvenioFiltro(int idGrupoConvenioFiltro) {
        this.idGrupoConvenioFiltro = idGrupoConvenioFiltro;
    }

    public Servicos[] getServicoSelecionado() {
        return servicoSelecionado;
    }

    public void setServicoSelecionado(Servicos[] servicoSelecionado) {
        this.servicoSelecionado = servicoSelecionado;
    }

    public ConvenioServico[] getConvenioServicoSelecionado() {
        return convenioServicoSelecionado;
    }

    public void setConvenioServicoSelecionado(ConvenioServico[] convenioServicoSelecionado) {
        this.convenioServicoSelecionado = convenioServicoSelecionado;
    }

    public List<SubGrupoConvenio> getListaSubGrupoConvenios() {
        if (listaSubGrupoConvenios.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaSubGrupoConvenios = (List<SubGrupoConvenio>) salvarAcumuladoDB.listaObjeto("SubGrupoConvenio", true);
        }
        return listaSubGrupoConvenios;
    }

    public void setListaSubGrupoConvenios(List<SubGrupoConvenio> listaSubGrupoConvenios) {
        this.listaSubGrupoConvenios = listaSubGrupoConvenios;
    }
}
