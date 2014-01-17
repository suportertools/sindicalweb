package br.com.rtools.utilitarios;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Diretorio {

    public static boolean criar(String diretorio) {
        if (new File(diretorio).exists()) {
            return false;
        }
        File file = new File(diretorio);
        if (file.mkdir()) {
            return true;
        }
        return false;
    }

    public static boolean criar(String path, String diretorio) {
        String cliente = "";
        if (GenericaSessao.exists("sessaoCliente")) {
            cliente = GenericaSessao.getString("sessaoCliente");
            if (cliente.equals("")) {
                return false;
            }
        }
        diretorio = path + "/" + cliente + "/" + diretorio;
        try {
            String s[] = diretorio.split("/");
            boolean err = false;
            for (int i = 0; i < s.length; i++) {
                if (!new File(s[i]).exists()) {
                    File file = new File(s[i]);
                    if (!file.mkdir()) {
                        err = false;
                        break;
                    }
                }
            }
            if (!err) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean remover(String diretorio) {
        if (new File(diretorio).exists()) {
            File file = new File(diretorio);
            if (file.delete()) {
                return true;
            }
        }
        return false;
    }

    public static boolean remover(String path, String diretorio) {
        String cliente = "";
        if (GenericaSessao.exists("sessaoCliente")) {
            cliente = GenericaSessao.getString("sessaoCliente");
            if (cliente.equals("")) {
                return false;
            }
        }
        diretorio = path + "/" + cliente + "/" + diretorio;
        if (new File(diretorio).exists()) {
            File file = new File(diretorio);
            if (file.delete()) {
                return true;
            }
        }
        return false;
    }

}
