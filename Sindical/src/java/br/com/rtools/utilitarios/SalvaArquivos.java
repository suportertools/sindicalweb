package br.com.rtools.utilitarios;

import java.io.File;
import java.io.FileOutputStream;

public class SalvaArquivos {

    private byte[] bytes;
    private String nomeArquivo;
    private boolean verifica;

    public SalvaArquivos(byte[] bytes, String nomeArquivo, boolean verifica) {
        this.bytes = bytes;
        this.nomeArquivo = nomeArquivo;
        this.verifica = verifica;
    }

    public void salvaNaPasta(String caminho) {
        try {
            File fl = new File(caminho + "/" + nomeArquivo);

            //FileInputStream in = new FileInputStream(fl);
            FileOutputStream out = new FileOutputStream(fl);
            out.write(bytes);
            out.flush();
            out.close();

            //byte[] buf = new byte[(int)item.length()];


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
