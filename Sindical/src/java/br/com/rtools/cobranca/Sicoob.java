package br.com.rtools.cobranca;

import br.com.rtools.financeiro.Boleto;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import java.util.Date;

public class Sicoob extends Cobranca {
    public Sicoob(Integer id_pessoa, Float valor, Date vencimento, Boleto boleto) {
        super(id_pessoa, valor, vencimento, boleto);
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

        if ((soma % 10) == 0) {
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
        if ((11 - (soma % 11)) == 0 || (11 - (soma % 11)) == 1 || (11 - (soma % 11)) > 9) {
            return "1";
        } else {
            return Integer.toString(11 - (soma % 11));
        }
    }

    @Override
    public String codigoBarras() {
        String iniCodigoBarras = "", fimCodigoBarras = "";
        iniCodigoBarras = boleto.getContaCobranca().getContaBanco().getBanco().getNumero() + boleto.getContaCobranca().getMoeda(); // banco + moeda
        
        //fimCodigoBarras += fatorVencimento(movimento.getDtVencimento());   // fator de vencimento
        fimCodigoBarras += fatorVencimentoSicoob(vencimento);   // fator de vencimento
        
        int tam = Moeda.limparPonto(Moeda.converteR$Float(valor)).length();
        
        fimCodigoBarras += "0000000000".substring(0, 10-tam) + Moeda.limparPonto(Moeda.converteR$Float(valor)); // valor
        
        fimCodigoBarras += boleto.getContaCobranca().getCarteira();       // carteira
        
        fimCodigoBarras += boleto.getContaCobranca().getContaBanco().getAgencia();
        
        fimCodigoBarras += "02";        // modalidade -- 01 com registro no banco // -- 02 sem registro no banco
        
        String cedente = "0000000".substring(0, 7 - boleto.getContaCobranca().getCodCedente().length()) + boleto.getContaCobranca().getCodCedente();        // codigo cedente
        fimCodigoBarras += cedente;
        
        String nossoNumero = boleto.getBoletoComposto() + calculoConstante();
        
        fimCodigoBarras += "00000000".substring(0, 8 - nossoNumero.length()) + nossoNumero;       // nosso numero
        
        fimCodigoBarras += "001";       // numero da parcela
        
        return iniCodigoBarras + this.moduloOnze(iniCodigoBarras+fimCodigoBarras) + fimCodigoBarras;
    }

    @Override
    public String representacao() {
        String codigoBarras = this.codigoBarras();
        String swap = "";
        int i = 0;
        String repNumerica = codigoBarras.substring(0, 4);
        repNumerica += codigoBarras.substring(19, 24);
        repNumerica += moduloDez(repNumerica);
        repNumerica += codigoBarras.substring(24, 34);
        repNumerica += moduloDez(codigoBarras.substring(24, 34));
        repNumerica += codigoBarras.substring(34, 44);
        repNumerica += moduloDez(codigoBarras.substring(34, 44));
        repNumerica += codigoBarras.substring(4, 5);
        
        swap += codigoBarras.substring(5, 19);
        i = 0;
        while (i < (15 - swap.length())) {
            swap = ("0" + swap);
            i++;
        }
        repNumerica += swap;
        repNumerica = repNumerica.substring(0, 5) + "."
                + repNumerica.substring(5, 10) + " "
                + repNumerica.substring(10, 15) + "."
                + repNumerica.substring(15, 21) + " "
                + repNumerica.substring(21, 26) + "."
                + repNumerica.substring(26, 32) + " "
                + repNumerica.substring(32, 33) + " "
                + repNumerica.substring(34, repNumerica.length());
        return repNumerica;
    }

    public String calculoConstante(){
        String agencia = boleto.getContaCobranca().getContaBanco().getAgencia();
        String cedente = "0000000".substring(0, 7 - boleto.getContaCobranca().getCodCedente().length()) + boleto.getContaCobranca().getCodCedente();
        String composicao = agencia+("0000000000".substring(0, 10- cedente.length()) + cedente) + "0000000".substring(0, 7 - boleto.getBoletoComposto().length()) + boleto.getBoletoComposto();
        //String composicao = "000100000000190000021";
        
        if (!composicao.isEmpty()){
            int soma = 0;
            
            int peso = 0;
            String constante[] = new String[4];
            constante[0] = "3";
            constante[1] = "1";
            constante[2] = "9";
            constante[3] = "7";
            
            String quebra[] = composicao.split("");
            
            for (String quebra1 : quebra) {
                if (!quebra1.isEmpty()) {
                    if (Integer.valueOf(quebra1) != 0) {
                        int um = Integer.valueOf(quebra1);
                        int vezes = Integer.valueOf(constante[peso]);
                        soma = (um * vezes) + soma; 
                    }
                    if (peso < 3)
                        peso = peso + 1;
                    else
                        peso = 0; 
                }
            }
            if ((soma % 11) == 0 || (soma % 11) == 1){
                composicao = "0";
            }else{
                composicao = Integer.toString(11 - (soma % 11));
            }
            
//            if ((11 - (soma % 11)) == 0 || (11 - (soma % 11)) == 1 || (11 - (soma % 11)) > 9) {
//                // NO MANUAL NÃO FALA DO CASO DE SER MAIOR QUE 9, PORÉM EM UM TESTE CAIU ESSE CASO
//            //if ((11 - (soma % 11)) == 0 || (11 - (soma % 11)) == 1) {
//                composicao = "0";
//            } else {
//                composicao = Integer.toString(11 - (soma % 11));
//            }
        }
        return composicao;
    }
        
    @Override
    public String getNossoNumeroFormatado() {
        return boleto.getBoletoComposto() + "-" + calculoConstante();
    }

    @Override
    public String getCedenteFormatado() {
        return boleto.getContaCobranca().getCodCedente();
    }

    @Override
    public String codigoBanco() {
        return "756";
    }
    
    
    public String fatorVencimentoSicoob(Date vencimento) {
        if (vencimento != null) {
            Date dataModel = DataHoje.converte("03/07/2000");
            long dias = vencimento.getTime() - dataModel.getTime();
            long total = dias / 86400000;
            total = total + 1000;
            return Long.toString(total);
        } else {
            return "";
        }
    }
}
