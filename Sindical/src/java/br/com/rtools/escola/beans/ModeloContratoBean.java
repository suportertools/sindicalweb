package br.com.rtools.escola.beans;

import br.com.rtools.escola.MatriculaContrato;
import br.com.rtools.escola.MatriculaContratoCampos;
import br.com.rtools.escola.MatriculaContratoServico;
import br.com.rtools.escola.db.MatriculaContratoDao;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.Modulo;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.beans.UploadFilesBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
//import br.com.rtools.utilitarios.DataObject;
//import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
//import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class ModeloContratoBean implements Serializable {

    private MatriculaContrato matriculaContrato;
    private MatriculaContratoCampos matriculaContratoCampos;
    private MatriculaContratoServico matriculaContratoServico;
    private List<MatriculaContrato> matriculaContratos;
    private List<MatriculaContratoServico> listaMatriculaContratoServico;
    private List<MatriculaContratoCampos> listaMatriculaContratoCampos;
    private List<SelectItem> listServicos;
    private List<SelectItem> listModulos;
    private List<SelectItem> listModulos2;
//    private List listaArquivos;
    private int idIndexServicos;
    private int idIndex;
    private Modulo modulo;
    private int idModulo;
    private int idModulo2;
    private int idServicos;
    private int quantidadeAnexo;
    private Servicos servicos;
    private String mensagem;
    private String descricaoPesquisa;
    private String msgServico;
    private boolean desabilitaObservacao;

    @PostConstruct
    public void init() {
        matriculaContrato = new MatriculaContrato();
        matriculaContratoCampos = new MatriculaContratoCampos();
        matriculaContratoServico = new MatriculaContratoServico();
        matriculaContratos = new ArrayList<MatriculaContrato>();
        listaMatriculaContratoServico = new ArrayList<MatriculaContratoServico>();
        listaMatriculaContratoCampos = new ArrayList<MatriculaContratoCampos>();
        listServicos = new ArrayList<SelectItem>();
        listModulos = new ArrayList<SelectItem>();
        listModulos2 = new ArrayList<SelectItem>();
//        listaArquivos = new ArrayList();
        idIndexServicos = -1;
        idIndex = -1;
        modulo = new Modulo();
        idModulo = 0;
        idModulo2 = 0;
        idServicos = 0;
        quantidadeAnexo = 0;
        servicos = new Servicos();
        mensagem = "";
        descricaoPesquisa = "";
        msgServico = "";
        desabilitaObservacao = false;
        UploadFilesBean uploadFilesBean = new UploadFilesBean();
        GenericaSessao.put("uploadFilesBean", uploadFilesBean);
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("modeloContratoBean");
        GenericaSessao.remove("matriculaContratoPesquisa");
        GenericaSessao.remove("uploadFilesBean");

    }

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
    public void clear() {
        GenericaSessao.remove("modeloContratoBean");
    }

    public void save() {
        if (matriculaContrato.getTitulo().equals("")) {
            mensagem = "Informar o titulo!";
            return;
        }
        if (matriculaContrato.getDescricao().equals("")) {
            mensagem = "Informar a descrição!";
            return;
        }
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (matriculaContrato.getId() == -1) {
            if (GenericaSessao.exists("idModulo")) {
                int idMod = GenericaSessao.getInteger("idModulo");
                if (idMod != 0) {
                    modulo = (Modulo) di.find(new Modulo(), idMod);
                }
            }
            matriculaContrato.setModulo(modulo);
            MatriculaContratoDao matriculaContratoDao = new MatriculaContratoDao();
            if (matriculaContratoDao.existeMatriculaContrato(matriculaContrato)) {
                mensagem = "Contrato já existe!";
                return;
            }
            di.openTransaction();
            if (di.save(matriculaContrato)) {
                di.commit();
                novoLog.save("ID: " + matriculaContrato.getId() + " - Título: " + matriculaContrato.getTitulo() + " - Módulo: (" + matriculaContrato.getModulo().getId() + ") " + matriculaContrato.getModulo().getDescricao());
                matriculaContratos.clear();
                mensagem = "Registro inserido com sucesso.";
            } else {
                di.rollback();
                mensagem = "Falha ao inserir o registro!";
            }
        } else {
            MatriculaContrato mc = (MatriculaContrato) di.find(matriculaContrato);
            String beforeUpdate = "ID: " + mc.getId() + " - Título: " + mc.getTitulo() + " - Módulo: (" + mc.getModulo().getId() + ") " + mc.getModulo().getDescricao();
            matriculaContrato.setDataAtualizado(DataHoje.data());
            di.openTransaction();
            if (di.update(matriculaContrato)) {
                novoLog.update(beforeUpdate, "ID: " + matriculaContrato.getId() + " - Título: " + matriculaContrato.getTitulo() + " - Módulo: (" + matriculaContrato.getModulo().getId() + ") " + matriculaContrato.getModulo().getDescricao());
                di.commit();
                matriculaContratos.clear();
                mensagem = "Registro atualizado com sucesso.";
            } else {
                di.rollback();
                mensagem = "Falha ao atualizar o registro!";
            }
        }
    }

    public void delete() {
        if (matriculaContrato.getId() != -1) {
            NovoLog novoLog = new NovoLog();
            DaoInterface di = new Dao();
            di.openTransaction();
            for (int i = 0; i < listaMatriculaContratoServico.size(); i++) {
                if (!di.delete(listaMatriculaContratoServico.get(i))) {
                    di.rollback();
                    mensagem = "Falha ao excluir esse registro!";
                    return;
                }
            }
            if (di.delete(matriculaContrato)) {
                di.commit();
                novoLog.delete("ID: " + matriculaContrato.getId() + " - Título: " + matriculaContrato.getTitulo() + " - Módulo: (" + matriculaContrato.getModulo().getId() + ") " + matriculaContrato.getModulo().getDescricao());
                matriculaContratos.clear();
                clear();
                mensagem = "Registro excluído com sucesso";
            } else {
                di.rollback();
                mensagem = "Falha ao excluir esse registro!";
            }
        }
    }

    public String edit(MatriculaContrato mc) {
        DaoInterface di = new Dao();
        UploadFilesBean uploadFilesBean = new UploadFilesBean();
        uploadFilesBean.setPath("Arquivos/contrato/" + mc.getId());
        uploadFilesBean.getListFiles();
        GenericaSessao.put("uploadFilesBean", uploadFilesBean);
        setMatriculaContrato((MatriculaContrato) di.find(new MatriculaContrato(), mc.getId()));
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

    public synchronized void addCamposModuloContrato() {
        if (matriculaContratoCampos.getCampo().equals("")) {
            GenericaMensagem.info("Sistema", "Informar o campo!");
            return;
        }
        if (matriculaContratoCampos.getVariavel().equals("")) {
            GenericaMensagem.warn("Sistema", "Informar a variável!");
            return;
        }
        DaoInterface di = new Dao();
        MatriculaContratoDao matriculaContratoDao = new MatriculaContratoDao();
        if (matriculaContratoCampos.getId() == -1) {
            if (matriculaContratoDao.existeMatriculaContratoCampo(matriculaContratoCampos, "campo")) {
                GenericaMensagem.warn("Sistema", "Variável já existe!");
                return;
            }
            if (matriculaContratoDao.existeMatriculaContratoCampo(matriculaContratoCampos, "variavel")) {
                GenericaMensagem.warn("Sistema", "Campo já cadastrado!");
                return;
            }
            if (matriculaContratoDao.existeMatriculaContratoCampo(matriculaContratoCampos, "tudo")) {
                GenericaMensagem.warn("Sistema", "Campo já cadastrado!");
                return;
            }
            matriculaContratoCampos.setModulo((Modulo) di.find(new Modulo(), Integer.parseInt(listModulos2.get(idModulo2).getDescription())));
            di.openTransaction();
            if (di.save(matriculaContratoCampos)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro inserido com sucesso.");
                listaMatriculaContratoCampos.clear();
                listModulos.clear();
                idModulo = 0;
            } else {
                di.rollback();
                GenericaMensagem.info("Erro", "Falha ao inserir o registro!");
            }
        } else {
            di.openTransaction();
            if (di.update(matriculaContratoCampos)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro atualizado com sucesso.");
                listaMatriculaContratoCampos.clear();
            } else {
                di.rollback();
                GenericaMensagem.info("Erro", "Falha ao atualizar o registro!");
            }
        }
        matriculaContratoCampos.setModulo(modulo);
    }

    public String removeCamposModuloContrato(MatriculaContratoCampos mcc) {
        DaoInterface di = new Dao();
        if (mcc.getId() != -1) {
            di.openTransaction();
            if (di.delete(mcc)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro excluído com sucesso");
                listaMatriculaContratoCampos.clear();
                listModulos.clear();
                idModulo = 0;
                matriculaContratoCampos = new MatriculaContratoCampos();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Falha ao excluir o registro!");
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

    public List<SelectItem> getListServicos() {
        if (listServicos.isEmpty()) {
            MatriculaContratoDao matriculaContratoDao = new MatriculaContratoDao();
            List<Servicos> list = (List<Servicos>) matriculaContratoDao.listaServicosDispiniveis();
            for (int i = 0; i < list.size(); i++) {
                listServicos.add(new SelectItem(i, (String) (list.get(i)).getDescricao(), Integer.toString((list.get(i)).getId())));
            }
        }
        return listServicos;
    }

    public void addServicos() {
        msgServico = "";
        if (matriculaContrato.getId() != -1) {
            int idServico = Integer.parseInt(getListServicos().get(idServicos).getDescription());
            MatriculaContratoDao matriculaContratoDao = new MatriculaContratoDao();
            if (matriculaContratoDao.validaMatriculaContratoServico(matriculaContrato.getId(), idServico)) {
                GenericaMensagem.warn("Validação", "Contrato já possui esse serviço!");
                return;
            }
            if (matriculaContratoDao.existeServicoMatriculaContrato(idServico)) {
                GenericaMensagem.warn("Validação", "Serviço já cadastrado para contrato (s)!");
                return;
            }
            DaoInterface di = new Dao();
            matriculaContratoServico.setServico((Servicos) (di.find(new Servicos(), idServico)));
            matriculaContratoServico.setContrato(matriculaContrato);
            di.openTransaction();
            if (di.save(matriculaContratoServico)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Serviço adicionado");
                matriculaContratoServico = new MatriculaContratoServico();
                listaMatriculaContratoServico.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao adicionar este serviço!");
                di.rollback();
            }
        }
    }

    public void removeServicos(MatriculaContratoServico mcs) {
        msgServico = "";
        if (mcs.getId() != -1) {
            matriculaContratoServico = mcs;
        }
        if (matriculaContratoServico.getId() != -1) {
            DaoInterface di = new Dao();
            di.openTransaction();
            if (di.delete(matriculaContratoServico)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Serviço removido");
                listaMatriculaContratoServico.clear();
                matriculaContratoServico = new MatriculaContratoServico();
            } else {
                di.rollback();
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
            MatriculaContratoDao matriculaContratoDao = new MatriculaContratoDao();
            if (getModulo().getId() != -1) {
                matriculaContratos = matriculaContratoDao.pesquisaTodosPorModulo(modulo.getId());
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
                MatriculaContratoDao mcd = new MatriculaContratoDao();
                listaMatriculaContratoServico = mcd.pesquisaMatriculaContratoServico(matriculaContrato.getId());
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
            int idMod = GenericaSessao.getInteger("idModulo");
            if (idMod != 0) {
                DaoInterface di = new Dao();
                modulo = (Modulo) di.find(new Modulo(), idMod);
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
            MatriculaContratoDao matriculaContratoDao = new MatriculaContratoDao();
            if (tipoLista.equals("this")) {
                if (GenericaSessao.exists("idModulo")) {
                    int idMod = GenericaSessao.getInteger("idModulo");
                    if (idMod != 0) {
                        if (descricaoPesquisa.equals("")) {
                            listaMatriculaContratoCampos = (List<MatriculaContratoCampos>) matriculaContratoDao.listaMatriculaContratoCampo(idMod);
                        } else {
                            listaMatriculaContratoCampos = (List<MatriculaContratoCampos>) matriculaContratoDao.listaMatriculaContratoCampo(idMod, descricaoPesquisa);
                        }
                    }
                }
            } else {
                listaMatriculaContratoCampos = (List<MatriculaContratoCampos>) matriculaContratoDao.listaMatriculaContratoCampo(Integer.parseInt(listModulos.get(idModulo).getDescription()));
            }
        }
        return listaMatriculaContratoCampos;
    }

    public void setListaMatriculaContratoCampos(List<MatriculaContratoCampos> listaMatriculaContratoCampos) {
        this.listaMatriculaContratoCampos = listaMatriculaContratoCampos;
    }

    public List<SelectItem> getListModulos() {
        if (listModulos.isEmpty()) {
            MatriculaContratoDao matriculaContratoDao = new MatriculaContratoDao();
            List<Modulo> lista = (List<Modulo>) matriculaContratoDao.listaModulosMatriculaContratoCampos();
            for (int i = 0; i < lista.size(); i++) {
                listModulos.add(new SelectItem(i, lista.get(i).getDescricao(), Integer.toString(lista.get(i).getId())));
            }
        }
        return listModulos;
    }

    public void setListModulos(List<SelectItem> listModulos) {
        this.listModulos = listModulos;
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

    public List<SelectItem> getListModulos2() {
        if (listModulos2.isEmpty()) {
            DaoInterface di = new Dao();
            List<Modulo> list = (List<Modulo>) di.list(new Modulo());
            for (int i = 0; i < list.size(); i++) {
                listModulos2.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listModulos2;
    }

    public void setListModulos2(List<SelectItem> listModulos2) {
        this.listModulos2 = listModulos2;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

//    public void upload(FileUploadEvent event) {
//        UploadFilesBean uploadFilesBean = new UploadFilesBean();
//        uploadFilesBean.setPath("Arquivos/contrato/" + matriculaContrato.getId());
//        uploadFilesBean.upload(event);
////        if (matriculaContrato.getId() != -1) {
////            ConfiguracaoUpload cu = new ConfiguracaoUpload();
////            cu.setArquivo(event.getFile().getFileName());
////            cu.setDiretorio("Arquivos/contrato/" + matriculaContrato.getId());
////            cu.setEvent(event);
////            if (Upload.enviar(cu, true)) {
////                listaArquivos.clear();
////            }
////        }
//    }
//
//    public void excluirArquivo(int index) {
//        if (Diretorio.remover("Arquivos/contrato/" + matriculaContrato.getId() + "/" + (String) ((DataObject) listaArquivos.get(index)).getArgumento1())) {
//            listaArquivos.remove(index);
//            listaArquivos.clear();
//            getListaArquivos();
//        }
//    }
//
//    public List getListaArquivos() {
//        if (matriculaContrato.getId() != -1) {
//            if (listaArquivos.isEmpty()) {
//                listaArquivos = Diretorio.listaArquivos("Arquivos/contrato/" + matriculaContrato.getId());
//                if (listaArquivos.size() > 0) {
//                    setQuantidadeAnexo(listaArquivos.size());
//                } else {
//                    setQuantidadeAnexo(0);
//                }
//            }
//        }
//        return listaArquivos;
//    }
//    public void setListaArquivos(List listaArquivos) {
//        this.listaArquivos = listaArquivos;
//    }
//
//    public int getQuantidadeAnexo() {
//        return quantidadeAnexo;
//    }
//
//    public void setQuantidadeAnexo(int quantidadeAnexo) {
//        this.quantidadeAnexo = quantidadeAnexo;
//    }
    public String getPath() {
        return "Arquivos/contrato/" + matriculaContrato.getId();
    }
}
