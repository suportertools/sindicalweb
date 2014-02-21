package br.com.rtools.cobranca;

import br.com.rtools.financeiro.Boleto;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.utilitarios.Moeda;

public class Santander extends Cobranca {

    public Santander(Movimento movimento, Boleto boleto) {
        super(movimento, boleto);
    }

    @Override
    public String moduloDez(String composicao) {
        int i = composicao.length();
        int j = 2;
        int soma = 0;
        int swap = 0;
        String m;
        while (i > 0) {
            m = composicao.substring(i - 1, i);
            swap = Integer.parseInt(m) * j;
            if (swap > 9) {
                String num = Integer.toString(swap);
                swap = Integer.parseInt(num.substring(0, 1)) + Integer.parseInt(num.substring(1, 2));
            }
            soma += swap;
            if (j == 2) {
                j = 1;
            } else if (j == 1) {
                j = 2;
            }
            i--;
        }

        if (((10 - (soma % 10)) == 10) || ((soma % 10) == 0)) {
            return "0";
        } else {
            if (soma < 10) {
                return Integer.toString(10 - soma);
            } else {
                return Integer.toString(10 - (soma % 10));
            }
        }
    }

    @Override
    public String moduloOnze(String composicao) {
        int i = composicao.length();
        int j = 2;
        int soma = 0;
        String m;
        while (i > 0) {
            if (j > 9) {
                j = 2;
            }
            m = composicao.substring(i - 1, i);
            soma += Integer.parseInt(m) * j;
            j++;
            i--;
        }
        if (((11 - (soma % 11)) == 0) || ((11 - (soma % 11)) == 1)) {
            return "0";
        } else if ((11 - (soma % 11)) > 9) {
            return "1";
        } else {
            return Integer.toString(11 - (soma % 11));
        }
//        if (((11 - (soma % 11)) == 0) || ((11 - (soma % 11)) == 1) || ((11 - (soma % 11)) > 10)) {
//            return "0";
//        } else if ((11 - (soma % 11)) == 10) {
//            return "1";
//        } else {
//            return Integer.toString(11 - (soma % 11));
//        }
//       int base = 9;  
//       int r    = 0;  
//       String num = composicao;
//       
//       int soma = 0;  
//       int fator = 2;  
//       String[] numeros,parcial;  
//       numeros = new String[num.length()+1];  
//       parcial = new String[num.length()+1];  
//
//       /* Separacao dos numeros */  
//       for (int i = num.length(); i > 0; i--) {  
//           // pega cada numero isoladamente  
//           numeros[i] = num.substring(i-1,i);  
//           // Efetua multiplicacao do numero pelo falor  
//           parcial[i] = String.valueOf(Integer.parseInt(numeros[i]) * fator);  
//           // Soma dos digitos  
//           soma += Integer.parseInt(parcial[i]);  
//           if (fator == base) {  
//               // restaura fator de multiplicacao para 2  
//               fator = 1;  
//           }  
//           fator++;  
//       }  
//
//       /* Calculo do modulo 11 */  
//       if (r == 0) {  
//           soma *= 10;  
//           int digito = soma % 11;  
//           if (digito == 10) {  
//               digito = 0;  
//           }  
//           return Integer.toString(digito);
//       } else {  
//           int resto = soma % 11;  
//           return Integer.toString(resto);
//       }          
    }

    @Override
    public String codigoBarras() {
        String iniCodigoBarras = "", fimCodigoBarras = "";
        iniCodigoBarras = boleto.getContaCobranca().getContaBanco().getBanco().getNumero() + boleto.getContaCobranca().getMoeda(); // banco + moeda
        
        fimCodigoBarras += fatorVencimento(movimento.getDtVencimento());   // fator de vencimento
        
        String valor = Moeda.limparPonto(Moeda.converteR$Float(movimento.getValor()));
        int tam = valor.length();
        
        fimCodigoBarras += "0000000000".substring(0, 10-tam) + valor; // valor
        
        fimCodigoBarras += "9";
        
        String cedente = "0000000".substring(0, 7 - boleto.getContaCobranca().getCodCedente().length()) + boleto.getContaCobranca().getCodCedente();        // codigo cedente
        fimCodigoBarras += cedente;
        
        String nossoNumero = boleto.getBoletoComposto()+this.moduloOnze(boleto.getBoletoComposto());//boleto.getBoletoComposto() + calculoConstante();
        fimCodigoBarras += "0000000000000".substring(0, 13 - nossoNumero.length()) + nossoNumero;       // nosso numero

        fimCodigoBarras += "0";       // IOS -- [ 0 demais clientes ] -- [ 7 - 7% ] -- limitado a [ 9% - 9 ]
        fimCodigoBarras += "102";
        
        return iniCodigoBarras + this.moduloOnzeDV(iniCodigoBarras+fimCodigoBarras) + fimCodigoBarras;
    }

    @Override
    public String representacao() {
        String codigoBarras = this.codigoBarras();
        //String swap = "";
        int i = 0;
        // PRIMEIRO GRUPO --
        String primeiro_grupo = codigoBarras.substring(0, 4);
        primeiro_grupo += codigoBarras.substring(19, 24);
        primeiro_grupo += moduloDez(primeiro_grupo);
        
        // SEGUNDO GRUPO --
        String segundo_grupo = codigoBarras.substring(24, 27);
        String nossoNumero = boleto.getBoletoComposto()+this.moduloOnze(boleto.getBoletoComposto());
        nossoNumero = "0000000000000".substring(0, 13 - nossoNumero.length()) + nossoNumero;
        segundo_grupo += nossoNumero.substring(0, 7);
        segundo_grupo += moduloDez(segundo_grupo);
        
        // TERCEIRO GRUPO --
        String terceiro_grupo = nossoNumero.substring(7, 13);
        terceiro_grupo += "0"; // IOS -- [ 0 demais clientes ] -- [ 7 - 7% ] -- limitado a [ 9% - 9 ]
        terceiro_grupo += "102"; 
        terceiro_grupo +=  moduloDez(terceiro_grupo);
        
        // QUARTO GRUPO
        String quarto_grupo = codigoBarras.substring(4, 5);
        
        String quinto_grupo = codigoBarras.substring(5, 19);
        
        String repNumerica = primeiro_grupo + segundo_grupo + terceiro_grupo + quarto_grupo + quinto_grupo;
        repNumerica = repNumerica.substring(0, 5) + "."
                    + repNumerica.substring(5, 10) + " "
                    + repNumerica.substring(10, 15) + "."
                    + repNumerica.substring(15, 21) + " "
                    + repNumerica.substring(21, 26) + "."
                    + repNumerica.substring(26, 32) + " "
                    + repNumerica.substring(32, 33) + " "
                    + repNumerica.substring(33, repNumerica.length());
        return repNumerica;
    }

    @Override
    public String getNossoNumeroFormatado() {
        return boleto.getBoletoComposto() + "-" + this.moduloOnze(boleto.getBoletoComposto());
    }

    @Override
    public String getCedenteFormatado() {
        return boleto.getContaCobranca().getCodCedente();
    }

    @Override
    public String getAgenciaFormatada() {
        return boleto.getContaCobranca().getContaBanco().getAgencia() + "-" + this.moduloDez(boleto.getContaCobranca().getContaBanco().getAgencia());
    }

    @Override
    public String codigoBanco() {
        return "033-7";
    }
}