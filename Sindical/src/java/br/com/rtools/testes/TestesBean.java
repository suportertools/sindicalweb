package br.com.rtools.testes;

import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import static br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean.getCliente;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class TestesBean implements Serializable {

    private String imagem;

    @PostConstruct
    public void init() {

    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("fisicaBean");
        GenericaSessao.remove("fisicaPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("enderecoPesquisa");
        GenericaSessao.remove("remove_ids");
        GenericaSessao.remove("validacao_tipo");
        GenericaSessao.remove("uploadBean");
        GenericaSessao.remove("photocamBean");

    }

    public void clear() {
        GenericaSessao.remove("fisicaBean");
        clear(0);

    }

    public void clear(Integer tCase) {
        if (tCase == 0) {
            try {
                GenericaSessao.remove("cropperBean");
                GenericaSessao.remove("uploadBean");
                GenericaSessao.remove("photoCamBean");
                FileUtils.deleteDirectory(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + "/Cliente/" + getCliente() + "/temp/" + "foto/testes/"));
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/imagens/testes/" + -1 + ".png"));
                if (f.exists()) {
                    f.delete();
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(TestesBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void apagarImagem() {
        boolean sucesso = false;
        String extension = "";
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                extension = "png";
            }
            if (i == 1) {
                extension = "jpg";
            }
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/imagens/testes/" + "teste" + "." + extension));
            sucesso = f.delete();
            if (sucesso) {
                imagem = "";
                RequestContext.getCurrentInstance().update(":form_testes");
                break;
            }
        }
    }

    /**
     * Retorna foto
     *
     * @param filename
     * @return
     */
    public String getImagem(String filename) {
        return getImagem(filename, 0);
    }

    /**
     * Retorna foto
     *
     * @param filename
     * @param waiting
     * @return
     */
    public String getImagem(String filename, Integer waiting) {
        if (waiting > 0) {
            try {
                Thread.sleep(waiting);
            } catch (InterruptedException ex) {
            }
        }
        String foto = "/Cliente/" + ControleUsuarioBean.getCliente() + "/imagens/testes/" + filename + ".png";
        File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(foto));
        if (!f.exists()) {
            foto = "/Cliente/" + ControleUsuarioBean.getCliente() + "/imagens/testes/" + filename + ".jpg";
            f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(foto));
            if (!f.exists()) {
                foto = "/imagens/user_male.png";
            }
        }
        return foto;
    }

    public String getPath() {
        return "imagens/testes";
    }

}
