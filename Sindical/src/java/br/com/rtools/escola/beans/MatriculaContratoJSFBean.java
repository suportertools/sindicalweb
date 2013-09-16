package br.com.rtools.escola.beans;

import br.com.rtools.escola.MatriculaContrato;
import br.com.rtools.escola.MatriculaContratoServico;
import br.com.rtools.escola.db.MatriculaContratoDB;
import br.com.rtools.escola.db.MatriculaContratoDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.seguranca.Modulo;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class MatriculaContratoJSFBean implements java.io.Serializable {

    private MatriculaContrato matriculaContrato = new MatriculaContrato();
    private MatriculaContratoServico matriculaContratoServico = new MatriculaContratoServico();
    private List<MatriculaContrato> matriculaContratos = new ArrayList<MatriculaContrato>();
    private List<MatriculaContratoServico> listaMatriculaContratoServico = new ArrayList<MatriculaContratoServico>();
    private int idIndexServicos = -1;
    private int idIndex = -1;
    private Modulo modulo = new Modulo();
    private int idServicos = 0;
    private Servicos servicos = new Servicos();
    private String msg = "";
    private String msgServico = "";
    List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private boolean desabilitaObservacao = false;

    public boolean isDesabilitaObservacao() {
        if (((Usuario) (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"))).getId() == 1) {
            desabilitaObservacao = true;
        } else {
            desabilitaObservacao = false;
        }
        return desabilitaObservacao;
    }

    public void setDesabilitaObservacao(boolean desabilitaObservacao) {
        this.desabilitaObservacao = desabilitaObservacao;
    }

    public String novo() {
        idServicos = 0;
        matriculaContrato = new MatriculaContrato();
        idIndex = -1;
        msg = "";
        setMsgServico("");
        matriculaContratos.clear();
        servicos = new Servicos();
        idIndexServicos = -1;
        listaMatriculaContratoServico.clear();
        return null;
    }

    public void salvar() {
        if (matriculaContrato.getTitulo().equals("")) {
            msg = "Informar o titulo!";
            return;
        }
        if (matriculaContrato.getDescricao().equals("")) {
            msg = "Informar a descrição!";
            return;
        }
        if (matriculaContrato.getObservacao().equals("")) {
            msg = "Informar as observações!";
            return;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (matriculaContrato.getId() == -1) {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo") != null) {
                int idModulo = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo");
                if (idModulo != 0) {
                    modulo = (Modulo) salvarAcumuladoDB.pesquisaCodigo(idModulo, "Modulo");
                    matriculaContrato.setModulo(modulo);
                }
            }
            MatriculaContratoDB matriculaContratoDB = new MatriculaContratoDBToplink();
            if (matriculaContratoDB.existeMatriculaContrato(matriculaContrato)) {
                msg = "Contrato já existe!";
                return;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(matriculaContrato)) {
                salvarAcumuladoDB.comitarTransacao();
                msg = "Registro inserido com sucesso.";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msg = "Falha ao inserir o registro!";
            }
        } else {
            matriculaContrato.setDataAtualizado(DataHoje.data());
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(matriculaContrato)) {
                salvarAcumuladoDB.comitarTransacao();
                msg = "Registro atualizado com sucesso.";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msg = "Falha ao atualizar o registro!";
            }
        }
    }

    public void excluir() {
        if (matriculaContrato.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            matriculaContrato = (MatriculaContrato) salvarAcumuladoDB.pesquisaCodigo(matriculaContrato.getId(), "MatriculaContrato");
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(matriculaContrato)) {
                salvarAcumuladoDB.comitarTransacao();
                msg = "Registro excluído com sucesso";
                novo();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msg = "Falha ao excluir esse registro!";
            }
        }
    }

    public String editar(MatriculaContrato mc) {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        setMatriculaContrato((MatriculaContrato) dB.pesquisaCodigo(mc.getId(), "MatriculaContrato"));
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("matriculaContratoPesquisa", matriculaContrato);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "matriculaContrato";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public MatriculaContrato getMatriculaContrato() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("matriculaContratoPesquisa") != null) {
            matriculaContrato = (MatriculaContrato) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("matriculaContratoPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("matriculaContratoPesquisa");
        }
        return matriculaContrato;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            ServicosDB servicosDB = new ServicosDBToplink();
            List list = servicosDB.pesquisaTodos();
            for (int i = 0; i < list.size(); i++) {
                listaServicos.add(new SelectItem(new Integer(i),
                        (String) ((Servicos) list.get(i)).getDescricao(),
                        Integer.toString(((Servicos) list.get(i)).getId())));
            }
        }
        return listaServicos;
    }

    public void adicionarServicos() {
        msgServico = "";
        if (matriculaContrato.getId() != -1) {
            int idServico = Integer.parseInt(getListaServicos().get(idServicos).getDescription());
            MatriculaContratoDB contratoDB = new MatriculaContratoDBToplink();
            if (contratoDB.validaMatriculaContratoServico(matriculaContrato.getId(), idServico)) {
                msgServico = "Contrato já possui esse serviço.";
                return;
            }
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            salvarAcumuladoDB.abrirTransacao();
            matriculaContratoServico.setServico((Servicos) (salvarAcumuladoDB.pesquisaCodigo(idServico, "Servicos")));
            matriculaContratoServico.setContrato(matriculaContrato);
            if (salvarAcumuladoDB.inserirObjeto(matriculaContratoServico)) {
                salvarAcumuladoDB.comitarTransacao();
                msgServico = "Serviço adicionado com sucesso.";
                matriculaContratoServico = new MatriculaContratoServico();
                listaMatriculaContratoServico.clear();
            } else {
                msgServico = "Erro ao adicionar este serviço!";
                salvarAcumuladoDB.desfazerTransacao();
            }
        }
    }

    public void removerServicos(MatriculaContratoServico mcs) {
        msgServico = "";
        if (mcs.getId() != -1) {
            matriculaContratoServico = mcs;
        }
        if (matriculaContratoServico.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            matriculaContratoServico = (MatriculaContratoServico) salvarAcumuladoDB.pesquisaCodigo(matriculaContratoServico.getId(), "MatriculaContratoServico");
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(matriculaContratoServico)) {
                salvarAcumuladoDB.comitarTransacao();
                msgServico = "Serviço removido com sucesso.";
                listaMatriculaContratoServico.clear();
                matriculaContratoServico = new MatriculaContratoServico();
            } else {
                msgServico = "Erro ao remover este serviço!";
                salvarAcumuladoDB.desfazerTransacao();
            }
        }
    }

    public void setMatriculaContrato(MatriculaContrato matriculaContrato) {
        this.matriculaContrato = matriculaContrato;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List<MatriculaContrato> getMatriculaContratos() {
        if (matriculaContratos.isEmpty()) {
            MatriculaContratoDB matriculaContratoDB = new MatriculaContratoDBToplink();
            if (getModulo().getId() != -1) {
                matriculaContratos = matriculaContratoDB.pesquisaTodosPorModulo(modulo.getId());
            }
        }
        return matriculaContratos;
    }

    public void setMatriculaContratos(List<MatriculaContrato> matriculaContratos) {
        this.matriculaContratos = matriculaContratos;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public List<MatriculaContratoServico> getListaMatriculaContratoServico() {
        if (matriculaContrato.getId() != -1) {
            if (listaMatriculaContratoServico.isEmpty()) {
                MatriculaContratoDB db = new MatriculaContratoDBToplink();
                listaMatriculaContratoServico = db.pesquisaMatriculaContratoServico(matriculaContrato.getId());
            }
        }
        return listaMatriculaContratoServico;
    }

    public void setListaMatriculaContratoServico(List<MatriculaContratoServico> listaMatriculaContratoServico) {
        this.listaMatriculaContratoServico = listaMatriculaContratoServico;
    }

    public int getIdIndexServicos() {
        return idIndexServicos;
    }

    public void setIdIndexServicos(int idIndexServicos) {
        this.idIndexServicos = idIndexServicos;
    }

    public MatriculaContratoServico getMatriculaContratoServico() {
        return matriculaContratoServico;
    }

    public void setMatriculaContratoServico(MatriculaContratoServico matriculaContratoServico) {
        this.matriculaContratoServico = matriculaContratoServico;
    }

    public Modulo getModulo() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo") != null) {
            int idModulo = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo");
            if (idModulo != 0) {
                SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
                modulo = (Modulo) acumuladoDB.pesquisaCodigo(idModulo, "Modulo");
            }
        }
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public String getMsgServico() {
        return msgServico;
    }

    public void setMsgServico(String msgServico) {
        this.msgServico = msgServico;
    }
}