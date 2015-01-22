package br.com.rtools.utilitarios;

import java.text.Normalizer;

public class AnaliseString {

    public static boolean conteudoNumero(String conteudo) {
        int i = 0;

        while (i < conteudo.length()) {
            if (!Character.isDigit(conteudo.charAt(i))) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean conteudoData(String conteudo) {
        int i = 0;

        while (i < conteudo.length()) {
            if ((conteudo.charAt(i) != '/')
                    && (conteudo.charAt(i) != '0')
                    && (conteudo.charAt(i) != '1')
                    && (conteudo.charAt(i) != '2')
                    && (conteudo.charAt(i) != '3')
                    && (conteudo.charAt(i) != '4')
                    && (conteudo.charAt(i) != '5')
                    && (conteudo.charAt(i) != '6')
                    && (conteudo.charAt(i) != '7')
                    && (conteudo.charAt(i) != '8')
                    && (conteudo.charAt(i) != '9')) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean conteudoValor(String conteudo) {
        int i = 0;

        while (i < conteudo.length()) {
            if ((conteudo.charAt(i) != '0')
                    && (conteudo.charAt(i) != '1')
                    && (conteudo.charAt(i) != '2')
                    && (conteudo.charAt(i) != '3')
                    && (conteudo.charAt(i) != '4')
                    && (conteudo.charAt(i) != '5')
                    && (conteudo.charAt(i) != '6')
                    && (conteudo.charAt(i) != '7')
                    && (conteudo.charAt(i) != '8')
                    && (conteudo.charAt(i) != '9')
                    && (conteudo.charAt(i) != ',')
                    && (conteudo.charAt(i) != '.')) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean conteudoPtoFlutuante(String conteudo) {
        int i = 0;

        while (i < conteudo.length()) {
            if ((conteudo.charAt(i) != '0')
                    && (conteudo.charAt(i) != '1')
                    && (conteudo.charAt(i) != '2')
                    && (conteudo.charAt(i) != '3')
                    && (conteudo.charAt(i) != '4')
                    && (conteudo.charAt(i) != '5')
                    && (conteudo.charAt(i) != '6')
                    && (conteudo.charAt(i) != '7')
                    && (conteudo.charAt(i) != '8')
                    && (conteudo.charAt(i) != '9')
                    && (conteudo.charAt(i) != '.')) {
                return false;
            }
            i++;
        }
        return true;
    }

    public static boolean conteudoPtoFlutuanteVirgula(String conteudo) {
        int i = 0;

        while (i < conteudo.length()) {
            if ((conteudo.charAt(i) != '0')
                    && (conteudo.charAt(i) != '1')
                    && (conteudo.charAt(i) != '2')
                    && (conteudo.charAt(i) != '3')
                    && (conteudo.charAt(i) != '4')
                    && (conteudo.charAt(i) != '5')
                    && (conteudo.charAt(i) != '6')
                    && (conteudo.charAt(i) != '7')
                    && (conteudo.charAt(i) != '8')
                    && (conteudo.charAt(i) != '9')
                    && (conteudo.charAt(i) != '.')) {
                return false;
            }
            i++;
        }
        return true;
    }

    public String buscaStringVetor(String a, String achar) {
        String result = "";
        String s = "";
        for (int i = 0; i < a.length(); i++) {
            s = a.substring(i, i);
            for (int j = 0; j < (s.length() - achar.length()) + i; j++) {
                if ((s.substring(j, j + achar.length())).toUpperCase().equals(achar.toUpperCase())) {
                    result = s;
                }

            }
        }
        return result;

    }

    public static String somenteNumero(String conteudo) {
        int i = 0;
        String retorno = "";
        while (i < conteudo.length()) {
            if ((conteudo.charAt(i) == '0')
                    || (conteudo.charAt(i) == '1')
                    || (conteudo.charAt(i) == '2')
                    || (conteudo.charAt(i) == '3')
                    || (conteudo.charAt(i) == '4')
                    || (conteudo.charAt(i) == '5')
                    || (conteudo.charAt(i) == '6')
                    || (conteudo.charAt(i) == '7')
                    || (conteudo.charAt(i) == '8')
                    || (conteudo.charAt(i) == '9')) {
                retorno += conteudo.charAt(i);
            }
            i++;
        }
        return retorno;
    }

    public static String subString(String conteudo, int inicio, int fim) {
        return conteudo.substring(inicio - 1, fim);
    }

    public static String removerAcentos(final String s) {
        String acentuado = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";
        String semAcento = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";
        char[] tabela;
        tabela = new char[256];
        for (int i = 0; i < tabela.length; ++i) {
            tabela[i] = (char) i;
        }
        for (int i = 0; i < acentuado.length(); ++i) {
            tabela[acentuado.charAt(i)] = semAcento.charAt(i);
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            if (ch < 256) {
                sb.append(tabela[ch]);
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static String normalizeLower(String value) {

        value = Normalizer.normalize(value, Normalizer.Form.NFD);
        value = value.toLowerCase().replaceAll("[^\\p{ASCII}]", "");

        return value;
    }

    public static String mascaraCep(final String cep) {
        String cepMask = "";
        if (!cep.equals("")) {
            cepMask = cep.substring(0, 2) + "." + cep.substring(2, 5) + "-" + cep.substring(5, 8);
        }
        return cepMask;
    }

    public static String mascaraCnpj(final String cnpj) {
        String cnpjMask = "";
        try {
            if (!cnpj.equals("")) {
                cnpjMask = cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14);
            }
            return cnpjMask;
        } catch (Exception e) {
            return cnpj;
        }
    }

    public static String extrairNumeros(String docS) {
        String documento = "";
        int i = 0;
        while (i < docS.length()) {
            String as = docS.substring(i, i + 1);
            if (!as.equals(".") && !as.equals("-") && !as.equals("/")) {
                documento = documento + as;
            }
            i++;
        }
        return documento;
    }

    public static String converterCapitalize(String descricao) {
        String[] strings = descricao.toLowerCase().split(" ");
        String novaDescricao = "";
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].toUpperCase().equals("POR") || strings[i].toUpperCase().equals("DOS") || strings[i].toUpperCase().equals("DAS") || strings[i].toUpperCase().equals("DE")) {
                novaDescricao += strings[i] + " ";
            } else {
                if (strings[i].length() > 0) {
                    novaDescricao += strings[i].substring(0, 1).toUpperCase() + strings[i].substring(1) + " ";
                }
            }
        }
        return novaDescricao;
    }

    public static String converteNullString(Object object) {
        if (object == null) {
            return "";
        } else {
            return String.valueOf(object);
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static boolean isFloat(String s) {
        boolean retorno = false;
        try {
            Float.parseFloat(s);
            retorno = true;
        } catch (NumberFormatException e) {
            retorno = false;
        }
        if (!retorno) {
            try {
                float numero = Moeda.substituiVirgulaFloat(s);
                retorno = true;
            } catch (NumberFormatException e) {
                retorno = false;
            }
        }
        // only got here if we didn't return false
        return retorno;
    }

    public static boolean isString(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static String removeDiff(String string, int length, boolean isWhites) {
        if (string.isEmpty()) {
            return string;
        }
        if (string.length() > length) {
            string = string.substring(length);
        }
        if (isWhites) {
            if (string.length() < length) {
                int diff = length - string.length();
                for (int i = 0; i < diff; i++) {
                    string += " ";
                }
            }
        }
        return string;
    }

    public static String removeDiff(String string, Integer length) {
        return AnaliseString.removeDiff(string, length, false);
    }
}
