package br.com.rtools.escola.beans; 

import br.com.rtools.escola.MatriculaContrato;
import br.com.rtools.escola.MatriculaContratoCampos;
import br.com.rtools.escola.MatriculaContratoServico;
import br.com.rtools.escola.db.MatriculaContratoDB;
import br.com.rtools.escola.db.MatriculaContratoDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.seguranca.Modulo;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class ModeloContratoBean implements java.io.Serializable {

    private MatriculaContrato matriculaContrato = new MatriculaContrato();
    private MatriculaContratoCampos matriculaContratoCampos = new MatriculaContratoCampos();
    private MatriculaContratoServico matriculaContratoServico = new MatriculaContratoServico();
    private List<MatriculaContrato> matriculaContratos = new ArrayList<MatriculaContrato>();
    private List<MatriculaContratoServico> listaMatriculaContratoServico = new ArrayList<MatriculaContratoServico>();
    private List<MatriculaContratoCampos> listaMatriculaContratoCampos = new ArrayList<MatriculaContratoCampos>();
    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private List<SelectItem> listaModulos = new ArrayList<SelectItem>();
    private List<SelectItem> listaModulos2 = new ArrayList<SelectItem>();
    private List listaArquivos = new ArrayList();
    private int idIndexServicos = -1;
    private int idIndex = -1;
    private Modulo modulo = new Modulo();
    private int idModulo = 0;
    private int idModulo2 = 0;
    private int idServicos = 0;
    private int quantidadeAnexo = 0;    
    private Servicos servicos = new Servicos();
    private String mensagem = "";
    private String descricaoPesquisa = "";
    private String msgServico = "";
    private boolean desabilitaObservacao = false;

    public boolean isDesabilitaObservacao() {
        if (((Usuario) (GenericaSessao.getObject("sessaoUsuario"))).getId() == 1) {
            desabilitaObservacao = true;
        } else {
            desabilitaObservacao = false;
        }
        return desabilitaObservacao;
    }

    public void setDesabilitaObservacao(boolean desabilitaObservacao) {
        this.desabilitaObservacao = desabilitaObservacao;
    }

    // MATRICULA CONTRATO
    public String novo() {
        listaServicos.clear();
        idServicos = 0;
        matriculaContrato = new MatriculaContrato();
        idIndex = -1;
        mensagem = "";
        setMsgServico("");
        matriculaContratos.clear();
        servicos = new Servicos();
        idIndexServicos = -1;
        listaMatriculaContratoServico.clear();
        matriculaContratoCampos = new MatriculaContratoCampos();
        return null;
    }

    public void salvar() {
        if (matriculaContrato.getTitulo().equals("")) {
            mensagem = "Informar o titulo!";
            return;
        }
        if (matriculaContrato.getDescricao().equals("")) {
            mensagem = "Informar a descrição!";
            return;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (matriculaContrato.getId() == -1) {
            if (GenericaSessao.exists("idModulo")) {
                int idMod = (Integer) GenericaSessao.getInteger("idModulo");
                if (idMod != 0) {
                    modulo = (Modulo) salvarAcumuladoDB.pesquisaCodigo(idMod, "Modulo");
                }
            }
            matriculaContrato.setModulo(modulo);
            MatriculaContratoDB matriculaContratoDB = new MatriculaContratoDBToplink();
            if (matriculaContratoDB.existeMatriculaContrato(matriculaContrato)) {
                mensagem = "Contrato já existe!";
                return;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(matriculaContrato)) {
                salvarAcumuladoDB.comitarTransacao();
                matriculaContratos.clear();
                mensagem = "Registro inserido com sucesso.";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Falha ao inserir o registro!";
            }
        } else {
            matriculaContrato.setDataAtualizado(DataHoje.data());
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(matriculaContrato)) {
                salvarAcumuladoDB.comitarTransacao();
                matriculaContratos.clear();
                mensagem = "Registro atualizado com sucesso.";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Falha ao atualizar o registro!";
            }
        }
    }

    public void excluir() {
        if (matriculaContrato.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            matriculaContrato = (MatriculaContrato) salvarAcumuladoDB.pesquisaCodigo(matriculaContrato.getId(), "MatriculaContrato");
            salvarAcumuladoDB.abrirTransacao();
            for (int i = 0; i < listaMatriculaContratoServico.size(); i++) {
                if (!salvarAcumuladoDB.deletarObjeto((MatriculaContratoServico) salvarAcumuladoDB.pesquisaCodigo(listaMatriculaContratoServico.get(i).getId(), "MatriculaContratoServico"))) {
                    salvarAcumuladoDB.desfazerTransacao();
                    mensagem = "Falha ao excluir esse registro!";
                    return;
                }
            }
            if (salvarAcumuladoDB.deletarObjeto(matriculaContrato)) {
                salvarAcumuladoDB.comitarTransacao();
                matriculaContratos.clear();
                novo();
                mensagem = "Registro excluído com sucesso";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Falha ao excluir esse registro!";
            }
        }
    }

    public String editar(MatriculaContrato mc) {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        setMatriculaContrato((MatriculaContrato) dB.pesquisaCodigo(mc.getId(), "MatriculaContrato"));
        GenericaSessao.put("matriculaContratoPesquisa", matriculaContrato);
        GenericaSessao.put("linkClicado", true);
        listaMatriculaContratoServico.clear();
        if (GenericaSessao.exists("urlRetorno")) {
            return (String) GenericaSessao.getString("urlRetorno");
        } else {
            return "matriculaContrato";
        }
    }

    // MATRICULA CONTRATO CAMPOS
    public void limparListaMCCampos() {
        // idModulo = 0;
        // listaModulos.clear();
        listaMatriculaContratoCampos.clear();
        // getListaMatriculaContratoCampos("");
    }

    public void novoMatriculaContratoCampos() {
        mensagem = "";
        idModulo = 0;
        listaMatriculaContratoServico.clear();
        matriculaContratoCampos = new MatriculaContratoCampos();
    }

    public synchronized void adicionarCamposModuloContrato() {
        if (matriculaContratoCampos.getCampo().equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sistema", "Informar o campo!"));
            return;
        }
        if (matriculaContratoCampos.getVariavel().equals("")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Informar a variável!"));
            return;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        MatriculaContratoDB matriculaContratoDB = new MatriculaContratoDBToplink();
        if (matriculaContratoCampos.getId() == -1) {
            if (matriculaContratoDB.existeMatriculaContratoCampo(matriculaContratoCampos, "campo")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Variável já existe!"));
                return;
            }
            if (matriculaContratoDB.existeMatriculaContratoCampo(matriculaContratoCampos, "variavel")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Campo já cadastrado!"));
                return;
            }
            if (matriculaContratoDB.existeMatriculaContratoCampo(matriculaContratoCampos, "tudo")) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Campo já cadastrado!"));
                return;
            }
            matriculaContratoCampos.setModulo((Modulo) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaModulos2.get(idModulo2).getDescription()), "Modulo"));
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(matriculaContratoCampos)) {
                salvarAcumuladoDB.comitarTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro inserido com sucesso."));
                listaMatriculaContratoCampos.clear();
                listaModulos.clear();
                idModulo = 0;
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao inserir o registro!"));
            }
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(matriculaContratoCampos)) {
                salvarAcumuladoDB.comitarTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro atualizado com sucesso."));
                listaMatriculaContratoCampos.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erro", "Falha ao atualizar o registro!"));
            }
        }
        matriculaContratoCampos.setModulo(modulo);
    }

    public String removerCamposModuloContrato(MatriculaContratoCampos mcc) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        mcc = (MatriculaContratoCampos) salvarAcumuladoDB.pesquisaCodigo(mcc.getId(), "MatriculaContratoCampos");
        if (mcc.getId() != -1) {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(mcc)) {
                salvarAcumuladoDB.comitarTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro excluído com sucesso"));
                listaMatriculaContratoCampos.clear();
                listaModulos.clear();
                idModulo = 0;
                matriculaContratoCampos = new MatriculaContratoCampos();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Falha ao excluir o registro!"));
            }
        }
        return "matriculaContratoCampos";
    }

    public MatriculaContrato getMatriculaContrato() {
        if (GenericaSessao.exists("matriculaContratoPesquisa")) {
            matriculaContrato = (MatriculaContrato) GenericaSessao.getObject("matriculaContratoPesquisa", true);
        }
        return matriculaContrato;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            MatriculaContratoDB matriculaContratoDB = new MatriculaContratoDBToplink();
            List<Servicos> list = (List<Servicos>) matriculaContratoDB.listaServicosDispiniveis();
            for (int i = 0; i < list.size(); i++) {
                listaServicos.add(new SelectItem(new Integer(i), (String) (list.get(i)).getDescricao(), Integer.toString((list.get(i)).getId())));
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
                GenericaMensagem.warn("Validação", "Contrato já possui esse serviço!");
                return;
            }
            if (contratoDB.existeServicoMatriculaContrato(idServico)) {
                GenericaMensagem.warn("Validação", "Serviço já cadastrado para contrato (s)!");
                return;
            }
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            matriculaContratoServico.setServico((Servicos) (salvarAcumuladoDB.pesquisaCodigo(idServico, "Servicos")));
            matriculaContratoServico.setContrato(matriculaContrato);
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(matriculaContratoServico)) {
                salvarAcumuladoDB.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Serviço adicionado");
                matriculaContratoServico = new MatriculaContratoServico();
                listaMatriculaContratoServico.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao adicionar este serviço!");
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
                GenericaMensagem.info("Sucesso", "Serviço removido");
                listaMatriculaContratoServico.clear();
                matriculaContratoServico = new MatriculaContratoServico();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Ao remover!");
            }
        }
    }

    public void setMatriculaContrato(MatriculaContrato matriculaContrato) {
        this.matriculaContrato = matriculaContrato;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List<MatriculaContrato> getMatriculaContratos() {
        matriculaContratos.clear();
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
        if (GenericaSessao.exists("idModulo")) {
            int idMod = (Integer) GenericaSessao.getInteger("idModulo");
            if (idMod != 0) {
                SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
                modulo = (Modulo) acumuladoDB.pesquisaCodigo(idMod, "Modulo");
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

    public MatriculaContratoCampos getMatriculaContratoCampos() {
        return matriculaContratoCampos;
    }

    public void setMatriculaContratoCampos(MatriculaContratoCampos matriculaContratoCampos) {
        this.matriculaContratoCampos = matriculaContratoCampos;
    }

    public List<MatriculaContratoCampos> getListaMatriculaContratoCampos(String tipoLista) {
        if (listaMatriculaContratoCampos.isEmpty()) {
            MatriculaContratoDB matriculaContratoDB = new MatriculaContratoDBToplink();
            if (tipoLista.equals("this")) {
                if (GenericaSessao.exists("idModulo")) {
                    int idMod = (Integer) GenericaSessao.getInteger("idModulo");
                    if (idMod != 0) {
                        if (descricaoPesquisa.equals("")) {
                            listaMatriculaContratoCampos = (List<MatriculaContratoCampos>) matriculaContratoDB.listaMatriculaContratoCampo(idMod);
                        } else {
                            listaMatriculaContratoCampos = (List<MatriculaContratoCampos>) matriculaContratoDB.listaMatriculaContratoCampo(idMod, descricaoPesquisa);
                        }
                    }
                }
            } else {
                listaMatriculaContratoCampos = (List<MatriculaContratoCampos>) matriculaContratoDB.listaMatriculaContratoCampo(Integer.parseInt(listaModulos.get(idModulo).getDescription()));
            }
        }
        return listaMatriculaContratoCampos;
    }

    public void setListaMatriculaContratoCampos(List<MatriculaContratoCampos> listaMatriculaContratoCampos) {
        this.listaMatriculaContratoCampos = listaMatriculaContratoCampos;
    }

    public List<SelectItem> getListaModulos() {
        if (listaModulos.isEmpty()) {
            MatriculaContratoDB matriculaContratoDB = new MatriculaContratoDBToplink();
            List<Modulo> lista = (List<Modulo>) matriculaContratoDB.listaModulosMatriculaContratoCampos();
            for (int i = 0; i < lista.size(); i++) {
                listaModulos.add(new SelectItem(i, lista.get(i).getDescricao(), Integer.toString(lista.get(i).getId())));
            }
        }
        return listaModulos;
    }

    public void setListaModulos(List<SelectItem> listaModulos) {
        this.listaModulos = listaModulos;
    }

    public int getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(int idModulo) {
        this.idModulo = idModulo;
    }

    public int getIdModulo2() {
        return idModulo2;
    }

    public void setIdModulo2(int idModulo2) {
        this.idModulo2 = idModulo2;
    }

    public List<SelectItem> getListaModulos2() {
        if (listaModulos2.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Modulo> list = (List<Modulo>) salvarAcumuladoDB.listaObjeto("Modulo");
            for (int i = 0; i < list.size(); i++) {
                listaModulos2.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listaModulos2;
    }

    public void setListaModulos2(List<SelectItem> listaModulos2) {
        this.listaModulos2 = listaModulos2;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }
    
    public void upload(FileUploadEvent event) {
        if (matriculaContrato.getId() != -1) {
            UploadedFile file = event.getFile();
            HttpServletRequest request = null;
            if (file.getFileName() == null) {
                return;
            }
            String cliente = "";
            String caminho = "";
            if (GenericaSessao.exists("sessaoCliente")) {
                cliente = GenericaSessao.getString("sessaoCliente");
            }
            caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + cliente + "/Arquivos/");
            if (!new File(caminho).exists()) {
                File fl2 = new File(caminho);
                fl2.mkdir();
            }
            if (!new File(caminho + "//").exists()) {
                File fl2 = new File(caminho + "/contrato/");
                fl2.mkdir();
            }
            if (!new File(caminho + "/contrato/" + matriculaContrato.getId() + "/").exists()) {
                File fl2 = new File(caminho + "/contrato/" + matriculaContrato.getId() + "/");
                fl2.mkdir();
            }
            try {
                File fl = new File(caminho + "/contrato/" + matriculaContrato.getId() + "/" + file.getFileName());
                InputStream in = file.getInputstream();
                FileOutputStream out = new FileOutputStream(fl.getPath());
                byte[] buf = new byte[(int) file.getSize()];
                int count;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
                in.close();
                out.flush();
                out.close();
                listaArquivos.clear();
            } catch (IOException e) {
                System.out.println(e);
            }
            
        }
    }    
    
    public void excluirArquivo(int index) {
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/"+matriculaContrato.getId()+"/" + (String) ((DataObject) listaArquivos.get(index)).getArgumento1());
        File fl = new File(caminho);
        fl.delete();
        listaArquivos.remove(index);
        listaArquivos.clear();
        getListaArquivos();
    }    

    public List getListaArquivos() {
        if (matriculaContrato.getId() != -1) {
            if (listaArquivos.isEmpty()) {
                String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/"+matriculaContrato.getId()+"/");
                try {
                    File files = new File(caminho);
                    if (!files.exists()) {
                        return new ArrayList();
                    }
                    File listFile[] = files.listFiles();
                    int numArq = listFile.length;
                    int i = 0;
                    while (i < numArq) {
                        listaArquivos.add(new DataObject(listFile[i], listFile[i].getName(), i));
                        i++;
                    }
                    if (listaArquivos.size() > 0) {
                        setQuantidadeAnexo(listaArquivos.size());
                    } else {
                        setQuantidadeAnexo(0);
                    }
                } catch (Exception e) {
                    return new ArrayList();
                }
            }
        }
        return listaArquivos;
    }

    public void setListaArquivos(List listaArquivos) {
        this.listaArquivos = listaArquivos;
    }

    public int getQuantidadeAnexo() {
        return quantidadeAnexo;
    }

    public void setQuantidadeAnexo(int quantidadeAnexo) {
        this.quantidadeAnexo = quantidadeAnexo;
    }
}