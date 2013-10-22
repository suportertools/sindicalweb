package br.com.rtools.logSistema;

import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class salvaLogs {

    private String dataHoje = null;
    private File arquivo = null;

    protected String criarArquivoData() {
        String logDir = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/logs/");
        if (!new File(logDir).exists()) {
            File file = new File(logDir);
            file.mkdir();
        }
        String dia = (DataHoje.data()).substring(0, 2);
        String mes = (DataHoje.data()).substring(3, 5);
        String ano = (DataHoje.data()).substring(6, 10);
        dataHoje = ano + "-" + mes + "-" + dia;
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/logs/" + dataHoje);
        HttpServletRequest paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String urlAtual = converteURL(paginaRequerida.getRequestURI());
        File fileData = new File(caminho);
        if (!fileData.exists()) {
            fileData.mkdir();
        }
        String caminho2 = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/logs/" + dataHoje + "/log_" + urlAtual + "_" + dataHoje + ".txt");
        try {
            arquivo = new File(caminho2);
            if (!arquivo.exists()) {
                FileOutputStream fileData2 = new FileOutputStream(caminho2);
                fileData2.close();
            }
        } catch (Exception e) {
        }
        return null;
    }

    protected String salvarLinha(String linha) {
        criarArquivoData();
        if (arquivo != null && arquivo.exists()) {
            try {
                FileWriter writer = new FileWriter(arquivo, true);
                BufferedWriter buffWriter = new BufferedWriter(writer);
                buffWriter.write(linha);
                buffWriter.newLine();
                buffWriter.flush();
                buffWriter.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

    public String converteURL(String url) {
        int iniURL = url.lastIndexOf("/");
        int fimURL = url.lastIndexOf(".");
        String paginaDestino = url.substring(iniURL + 1, fimURL);
        return paginaDestino;
    }

    protected File getArquivo() {
        return arquivo;
    }

    protected void setArquivo(File arquivo) {
        this.arquivo = arquivo;
    }
}
