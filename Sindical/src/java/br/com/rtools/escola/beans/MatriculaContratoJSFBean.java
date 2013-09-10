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
        if (((Usuario) (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario")) ).getId() == 1) {
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

    public String salvar() {
        if (matriculaContrato.getTitulo().equals("")) {
            msg = "Informar o titulo!";
            return null;
        }
        if (matriculaContrato.getDescricao().equals("")) {
            msg = "Informar a descrição!";
            return null;
        }
        if (matriculaContrato.getObservacao().equals("")) {
            msg = "Informar as observações!";
            return null;
        }        
        SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
        if (matriculaContrato.getId() == -1) {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo") != null){
                int idModulo = (Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo");
                if (idModulo != 0){
                    modulo = (Modulo) db.pesquisaCodigo(idModulo, "Modulo");
                    matriculaContrato.setModulo(modulo);
                }
            }
            MatriculaContratoDB cedb = new MatriculaContratoDBToplink();
            if (cedb.pesquisaTitulo(matriculaContrato.getTitulo())) {
                msg = "Titulo já existe!";
                return null;
            }
            db.abrirTransacao();
            if (db.inserirObjeto(matriculaContrato)) {
                db.comitarTransacao();
                msg = "Registro inserido com sucesso.";
                return null;
            } else {
                db.desfazerTransacao();
                msg = "Falha ao inserir o registro!";
                return null;
            }
        } else {
            matriculaContrato.setDataAtualizado(DataHoje.data());
            db.abrirTransacao();
            if (db.alterarObjeto(matriculaContrato)) {
                db.comitarTransacao();
                msg = "Registro atualizado com sucesso.";
                return null;
            } else {
                db.desfazerTransacao();
                msg = "Falha ao atualizar o registro!";
                return null;
            }
        }
    }

    public String excluir() {
        if (matriculaContrato.getId() != -1) {
            SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
            matriculaContrato = (MatriculaContrato) db.pesquisaCodigo(matriculaContrato.getId(), "MatriculaContrato");
            db.abrirTransacao();
            if (db.deletarObjeto(matriculaContrato)) {
                db.comitarTransacao();
                msg = "Registro excluído com sucesso";
                novo();
                return null;
            } else {
                db.desfazerTransacao();
                msg = "Falha ao excluir esse registro!";
                return null;
            }
        }
        return null;
    }

    public String editar() {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        setMatriculaContrato((MatriculaContrato) dB.pesquisaCodigo(getMatriculaContratos().get(idIndex).getId(), "MatriculaContrato"));
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
            ServicosDB db = new ServicosDBToplink();
            List select = db.pesquisaTodos();
            for (int i = 0; i < select.size(); i++) {
                listaServicos.add(new SelectItem(new Integer(i),
                        (String) ((Servicos) select.get(i)).getDescricao(),
                        Integer.toString(((Servicos) select.get(i)).getId())));
            }
        }
        return listaServicos;
    }

    public String adicionarServicos() {
        msgServico = "";
        if (matriculaContrato.getId() != -1) {
            int idServico = Integer.parseInt(getListaServicos().get(idServicos).getDescription());
            MatriculaContratoDB contratoDB = new MatriculaContratoDBToplink();
            if (contratoDB.validaMatriculaContratoServico(matriculaContrato.getId(), idServico)) {
                msgServico = "Contrato já possui esse serviço.";
                return null;
            }
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            dB.abrirTransacao();
            matriculaContratoServico.setServico((Servicos) (dB.pesquisaCodigo(idServico, "Servicos")));
            matriculaContratoServico.setContrato(matriculaContrato); 
            if (dB.inserirObjeto(matriculaContratoServico)) {
                dB.comitarTransacao();
                msgServico = "Serviço adicionado com sucesso.";
                matriculaContratoServico = new MatriculaContratoServico();
                listaMatriculaContratoServico.clear();
            } else {
                msgServico = "Erro ao adicionar este serviço!";
                dB.desfazerTransacao();
            }
        }
        return null;
    }
    
    public String removerServicos(MatriculaContratoServico mcs) {
        msgServico = "";        
        if (mcs.getId() != -1) {
            matriculaContratoServico = mcs;
        }
        if (matriculaContratoServico.getId() != -1) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            matriculaContratoServico = (MatriculaContratoServico) dB.pesquisaCodigo(matriculaContratoServico.getId(), "MatriculaContratoServico");
            dB.abrirTransacao();
            if (dB.deletarObjeto(matriculaContratoServico)) {
                dB.comitarTransacao();
                msgServico = "Serviço removido com sucesso.";
                listaMatriculaContratoServico.clear();
                matriculaContratoServico = new MatriculaContratoServico();
            } else {
                msgServico = "Erro ao remover este serviço!";
                dB.desfazerTransacao();
            }
        }
        return null;
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
        if(matriculaContratos.isEmpty()){
            MatriculaContratoDB matriculaContratoDB = new MatriculaContratoDBToplink();
            if(getModulo().getId() != -1){
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
        SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo") != null){
            int idModulo = (Integer)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idModulo");
            if (idModulo != 0){
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