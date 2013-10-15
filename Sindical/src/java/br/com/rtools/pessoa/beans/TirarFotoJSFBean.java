package br.com.rtools.pessoa.beans;

import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.GenericaQuery;
import java.io.*;
import java.net.InetAddress;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

public class TirarFotoJSFBean {

    private List listaFotos = new Vector<GenericaQuery>();

    public String capturarImagem() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String caminho = ((ServletContext) facesContext.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/");
        criarCaminhoPath(caminho);
        ziparArquivos(caminho);
        downloadFile("Webcam.zip", caminho, "zip", FacesContext.getCurrentInstance());
        return null;
    }

    public static synchronized void downloadFile(String filename, String fileLocation, String mimeType,
            FacesContext facesContext) {

        ExternalContext context = facesContext.getExternalContext();

        String path = fileLocation; // LOCALIZACAO DO ARQUIVO
        String fullFileName = path + "/" + filename;
        File file = new File(fullFileName); // LINHA ALTERADA

        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\""); // SETA O HEADER COM O QUE VAI APARECER NA HORA DO DOWNLOAD
        response.setContentLength((int) file.length()); // TAMANHO DO ARQUIVO
        response.setContentType(mimeType); // O TIPO DO ARQUIVO

        try {
            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            byte[] buf = new byte[(int) file.length()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
            facesContext.responseComplete();
        } catch (IOException ex) {
            System.out.println("Error in downloadFile: " + ex.getMessage());
        }
    }

    public List getListaArquivoFotos() {
        GenericaQuery generica = new GenericaQuery();
        FacesContext context = FacesContext.getCurrentInstance();
        File files;
        files = new File(((ServletContext) context.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/Fotos/"));
        File listFile[] = files.listFiles();
        String[] parametros = new String[6];
        listaFotos.clear();
        int numArq = listFile.length;
        int i = 0;
        while (i < numArq) {
            parametros[0] = "false";
            parametros[1] = listFile[i].getName();
            parametros[2] = "";
            parametros[3] = "";
            parametros[4] = "";
            parametros[5] = "";
            generica = new GenericaQuery(
                    parametros[0],
                    parametros[1],
                    parametros[2],
                    parametros[3],
                    parametros[4],
                    parametros[5]);
            listaFotos.add(generica);
            i++;
        }
        return listaFotos;
    }

    public String atualizaLista() {
        listaFotos = new Vector<GenericaQuery>();
        return "capturarFoto";
    }

    public void criarCaminhoPath(String pathUrl) {
        String caminho = "";
        caminho = pathUrl + "\\pathUrl.txt";
        try {
            FileOutputStream file = new FileOutputStream(caminho);
            file.close();
            FileWriter writer = new FileWriter(caminho);
            BufferedWriter buffWriter = new BufferedWriter(writer);
            buffWriter.write(pathUrl + "\\Fotos");
            buffWriter.flush();
            buffWriter.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void ziparArquivos(String caminhoPath) {
        // nome do arquivo que será compactado
        String arquivo = caminhoPath + "\\pathUrl.txt";
        String arquivo2 = caminhoPath + "\\WebCam3.jar";
        // Cria um buffer para ler os dados do arquivo
        byte[] buf = new byte[1024];
        try {
            // Cria o arquivo zip
            String compac = caminhoPath + "\\Webcam.zip";
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(compac));
            // Comprime o arquivo
            FileInputStream in = new FileInputStream(arquivo);
            FileInputStream in2 = new FileInputStream(arquivo2);

            // Adiciona o arquivo ao fluxo de saída
            out.putNextEntry(new ZipEntry("pathUrl.txt"));
            // transfere dados do arquivo para o arquivo zip
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.putNextEntry(new ZipEntry("WebCam3.jar"));
            while ((len = in2.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            // Finaliza a entrada
            out.closeEntry();
            in.close();
            // Completa o arquivo zip
            out.flush();
            out.close();
        } catch (IOException e) {
            // possíveis erros aqui
        }
    }

    public String getIp() {
        String ip = "";
        try {
            InetAddress i = InetAddress.getLocalHost();
            ip = i.getHostAddress();
        } catch (Exception e) {
        }
        return ip;
    }
}