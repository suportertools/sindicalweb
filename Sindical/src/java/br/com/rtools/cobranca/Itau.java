package br.com.rtools.cobranca;

import br.com.rtools.financeiro.Boleto;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.utilitarios.Moeda;
import java.util.Date;

public class Itau extends Cobranca {
// CNAB 400
// O layout com 15 digitos difilmente sera utilizado. So grandes emissoes, como por exemplo magazine e luiza

    public Itau(Integer id_pessoa, Float valor, Date vencimento, Boleto boleto) {
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

        if ((10 - (soma % 10)) > 9) {
            return "0";
        } else {
            return Integer.toString(10 - (soma % 10));
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


        if (soma < 11) {
            if ((soma == 1) || (soma == 0)) {
                return "1"; // a subtração abaixo pode resultar em 10 caso a soma seja "1". Apesar de ser um caso raro, estamos tratando esse posível erro.
            }
            return Integer.toString(11 - soma);
        }

        if (((11 - (soma % 11)) > 9)
                || ((11 - (soma % 11)) == 0) // Digito verificador geral nunca dara zero
                || ((11 - (soma % 11)) == 1)) {
            return "1";
        } else {
            return Integer.toString(11 - (soma % 11));
        }
    }

    @Override
    public String codigoBarras() {
        String codigoBarras = "";
        codigoBarras = boleto.getContaCobranca().getContaBanco().getBanco().getNumero() + boleto.getContaCobranca().getMoeda(); // banco + moeda
        codigoBarras += fatorVencimento(vencimento);   // fator de vencimento
        int i = 0;

        int tam = Moeda.limparPonto(Moeda.converteR$Float(valor)).length();
        while (i != (10 - tam)) { // zeros
            codigoBarras += "0";
            i++;
        }

        codigoBarras += Moeda.limparPonto(Float.toString(valor)); // valor

        codigoBarras += boleto.getContaCobranca().getCarteira();
        codigoBarras += boleto.getBoletoComposto();       // nosso numero
        codigoBarras += this.moduloDez(boleto.getBoletoComposto()
                + boleto.getContaCobranca().getCodCedente()
                + boleto.getContaCobranca().getCarteira()
                + boleto.getContaCobranca().getContaBanco().getAgencia());
        codigoBarras += boleto.getContaCobranca().getContaBanco().getAgencia(); // agencia
        codigoBarras += boleto.getContaCobranca().getCodCedente();        // codigo cedente
        codigoBarras += this.moduloDez(boleto.getContaCobranca().getContaBanco().getAgencia()
                + boleto.getContaCobranca().getCodCedente());
        codigoBarras += "000";
        codigoBarras = codigoBarras.substring(0, 4) + moduloOnzeDV(codigoBarras) + codigoBarras.substring(4, codigoBarras.length());
        int o = codigoBarras.length();
        return codigoBarras;
    }

    @Override
    public String representacao() {
        String codigoBarras = this.codigoBarras();
        int i = 0;
        // campo 1
        String repNumerica = boleto.getContaCobranca().getContaBanco().getBanco().getNumero(); // banco
        repNumerica += boleto.getContaCobranca().getMoeda();
        repNumerica += boleto.getContaCobranca().getCarteira();
        repNumerica += boleto.getBoletoComposto().substring(0, 2);
        repNumerica += this.moduloDez(boleto.getContaCobranca().getContaBanco().getBanco().getNumero()
                + boleto.getContaCobranca().getMoeda()
                + boleto.getContaCobranca().getCarteira()
                + boleto.getBoletoComposto().substring(0, 2));// DAC

        // campo 2
        repNumerica += boleto.getBoletoComposto().substring(2);
        repNumerica += this.moduloDez(boleto.getBoletoComposto()
                + boleto.getContaCobranca().getCodCedente()
                + boleto.getContaCobranca().getCarteira()
                + boleto.getContaCobranca().getContaBanco().getAgencia());
        repNumerica += boleto.getContaCobranca().getContaBanco().getAgencia().substring(0, 3);
        repNumerica += this.moduloDez(boleto.getBoletoComposto().substring(3)
                + this.moduloDez(boleto.getBoletoComposto()
                + boleto.getContaCobranca().getCodCedente()
                + boleto.getContaCobranca().getCarteira()
                + boleto.getContaCobranca().getContaBanco().getAgencia())
                + boleto.getContaCobranca().getContaBanco().getAgencia().substring(0, 3)); // DAC

        // campo3
        repNumerica += boleto.getContaCobranca().getContaBanco().getAgencia().substring(3, 4);
        repNumerica += boleto.getContaCobranca().getCodCedente()
                + this.moduloDez(boleto.getContaCobranca().getContaBanco().getAgencia()
                + boleto.getContaCobranca().getCodCedente());
        repNumerica += "000";
        repNumerica += this.moduloDez(boleto.getContaCobranca().getContaBanco().getAgencia().substring(3, 4)
                + boleto.getContaCobranca().getCodCedente()
                + this.moduloDez(boleto.getContaCobranca().getContaBanco().getAgencia()
                + boleto.getContaCobranca().getCodCedente())
                + "000");       // DAC
        /*
         // campo 4
         repNumerica += codigoBarras.substring(4, 5);

         // campo 5
         repNumerica += codigoBarras.substring(5, 10);
         repNumerica += codigoBarras.substring(10, 19);
         */

        // campo 4 e campo 5
        repNumerica += codigoBarras.substring(4, 19);

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
        return boleto.getContaCobranca().getCarteira() + "/" + boleto.getBoletoComposto() + "-"
                + this.moduloDez(boleto.getContaCobranca().getCarteira() + boleto.getBoletoComposto());
    }

    @Override
    public String getCedenteFormatado() {
        return boleto.getContaCobranca().getCodCedente().substring(0, 3) + boleto.getContaCobranca().getCodCedente().substring(3) + "-"
                + this.moduloDez(boleto.getContaCobranca().getContaBanco().getAgencia() + boleto.getContaCobranca().getCodCedente());
    }

    @Override
    public String codigoBanco() {
        return "341-7";
    }
}
