package br.com.rtools.utilitarios;

public class ValidaDocumentos {

    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] pesoCEI = {7, 4, 1, 8, 5, 2, 1, 6, 3, 7, 4};

    //  private static final char[] MULT_A = "74185216374".toCharArray();
    private static int calcularDigito(String str, int[] peso) {
        try {
            int soma = 0;
            for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
                digito = Integer.parseInt(str.substring(indice, indice + 1));
                soma += digito * peso[peso.length - str.length() + indice];
            }
            soma = 11 - soma % 11;
            return soma > 9 ? 0 : soma;
        } catch (Exception e) {
            return 0;
        }
    }

    private static int calcularCei(String str, int[] peso) {
        try {
            int soma = 0;
            for (int i = 0; i < 11; i++) {
                int dig1 = Integer.parseInt(str.substring(i, i + 1));
                int dig2 = peso[i];
                soma += (dig1 * dig2);
            }
            String numA = String.valueOf(soma);
            String numN = String.valueOf(soma);
            int tamanho = numA.length();
            if (tamanho > 2) {
                numA = numA.substring(1, 2);
                numN = numN.substring(2, 3);
                soma = (Integer.parseInt(numA)) + (Integer.parseInt(numN));
            } else {
                numA = numA.substring(0, 1);
                numN = numN.substring(1, 2);
                soma = (Integer.parseInt(numA)) + (Integer.parseInt(numN));
            }
            soma = soma - 10;
            return soma;
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isValidoCPF(String cpf) {
        try {
            if ((cpf == null) || (cpf.length() != 11)) {
                return false;
            }
            Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
            Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
            boolean d = cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
            return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidoCNPJ(String cnpj) {
        try {
            if ((cnpj == null) || (cnpj.length() != 14)) {
                return false;
            }
            Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
            Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
            boolean d = cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
            return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
        } catch (Exception e) {
            return false;
        }
    }

    public static String retonarDigitoCNPJ(String cnpj) {
        try {
            if ((cnpj == null)) {
                return "";
            }
            Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
            Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
            return digito1.toString() + digito2.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isValidoCEI(String cei) {
        try {
            if ((cei == null) || (cei.length() != 12)) {
                return false;
            }
            Integer digitoD = calcularCei(cei, pesoCEI);
            return cei.substring(11, 12).equals(String.valueOf(digitoD));
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidoPIS(String pis) {
        try {

            String lsMultiplicador = "3298765432";
            int totalizador = 0;
            int resto;
            int multiplicando;
            int multiplicador;
            boolean retorno = true;
            int digito;
            String listaAuxiliar = pis;
            int tamanho = listaAuxiliar.length();
            if (tamanho != 11) {
                retorno = false;
            }
            if (retorno) {
                for (int i = 0; i < 10; i++) {
                    multiplicando = Integer.parseInt(listaAuxiliar.substring(i, i + 1));
                    multiplicador = Integer.parseInt(lsMultiplicador.substring(i, i + 1));
                    totalizador += multiplicando * multiplicador;
                }
                resto = 11 - totalizador % 11;
                resto = resto == 10 || resto == 11 ? 0 : resto;
                digito = Integer.parseInt("" + listaAuxiliar.charAt(10));
                retorno = resto == digito;
            }
            return retorno;
        } catch (Exception e) {
            return false;
        }
    }
}
