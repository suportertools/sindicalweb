package br.com.rtools.utilitarios;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

public class Compact {

    //Constantes    
    public static int BUFFER_SIZE = 4096; // 4kb        
    private static List listFiles = new ArrayList();
    public static String OUT_FILE = "";
    public static String PATH_OUT_FILE = "";

    /**
     *
     * @param OUT_FILE Arquivo de saída
     * @param IN_FILE Arquivo de entrada
     * @throws IOException
     */
    public static void toZip(String OUT_FILE, String IN_FILE) throws IOException {
        Compact.OUT_FILE = OUT_FILE;
        listFiles.add(IN_FILE);
        toZip();
    }

    public static void toZip() throws IOException {
        int cont;
        byte[] dados = new byte[BUFFER_SIZE];
        BufferedInputStream bis;
        FileInputStream fis;
        FileOutputStream fos;
        ZipOutputStream out;
        ZipEntry entry;
        File file;
        Diretorio.criar(PATH_OUT_FILE);
        try {
            fos = new FileOutputStream(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(PATH_OUT_FILE + "/" + OUT_FILE)));
            out = new ZipOutputStream(new BufferedOutputStream(fos));
            for (int i = 0; i < listFiles.size(); i++) {
                file = new File(listFiles.get(i).toString());
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis, BUFFER_SIZE);
                entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);
                while ((cont = bis.read(dados, 0, BUFFER_SIZE)) != -1) {
                    out.write(dados, 0, cont);
                }
                bis.close();
            }
            out.close();
        } catch (IOException e) {
            destroy();
            throw new IOException(e.getMessage());
        }
        destroy();
    }

    public static void destroy() {
        BUFFER_SIZE = 4096; // 4kb        
        listFiles = new ArrayList();
        OUT_FILE = "";
        PATH_OUT_FILE = "";
    }

    public static List getListFiles() {
        return listFiles;
    }

    public static void setListFiles(List aListFiles) {
        listFiles = aListFiles;
    }
}
