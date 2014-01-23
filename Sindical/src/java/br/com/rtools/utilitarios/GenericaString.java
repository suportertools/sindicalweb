package br.com.rtools.utilitarios;

public class GenericaString {

    public static String converterNullToString(Object object) {
        if (object == null) {
            return "";
        } else {
            return String.valueOf(object);
        }
    }
}
