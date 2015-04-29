package br.com.rtools.cobranca;

import br.com.rtools.financeiro.Boleto;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.Date;

public class CaixaFederalSindical extends Cobranca {

    public CaixaFederalSindical(Integer id_pessoa, float valor, Date vencimento, Boleto boleto) {
        super(id_pessoa, valor, vencimento, boleto);
    }

    @Override
    public String codigoBarras() {
        JuridicaDB jurDB = new JuridicaDBToplink();
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        String ent = ((Registro) salvarAcumuladoDB.pesquisaCodigo(1, "Registro")).getTipoEntidade();
        // (1-Sindicato, 2-Federação, 3-Confederação)
        if (ent.equals("S")) {
            ent = "1";
        } else if (ent.equals("F")) {
            ent = "2";
        } else if (ent.equals("C")) {
            ent = "3";
        }
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
        codigoBarras += "97";
        codigoBarras += boleto.getContaCobranca().getSicasSindical();
        codigoBarras += jurDB.pesquisaJuridicaPorPessoa(id_pessoa).getCnae().getNumero().substring(0, 1);
        //codigoBarras += "1";
        codigoBarras += ent;
        codigoBarras += "77";
        codigoBarras += boleto.getBoletoComposto();       // nosso numero
        codigoBarras += jurDB.pesquisaJuridicaPorPessoa(id_pessoa).getCnae().getNumero().substring(1, 3);
        codigoBarras = codigoBarras.substring(0, 4) + moduloOnzeDV(codigoBarras) + codigoBarras.substring(4, codigoBarras.length());
        int dd = codigoBarras.length();
        return codigoBarras;
    }

    @Override
    public String representacao() {
        String codigoBarras = codigoBarras();
        int i = 0;
        String repNumerica = codigoBarras.substring(0, 4);
        repNumerica += "97";
        repNumerica += codigoBarras.substring(21, 44);
        repNumerica += codigoBarras.substring(4, 19);
        repNumerica = repNumerica.substring(0, 9) + this.moduloDez(repNumerica.substring(0, 9)) + repNumerica.substring(9, repNumerica.length());
        repNumerica = repNumerica.substring(0, 20) + this.moduloDez(repNumerica.substring(10, 20)) + repNumerica.substring(20, repNumerica.length());
        repNumerica = repNumerica.substring(0, 31) + this.moduloDez(repNumerica.substring(21, 31)) + repNumerica.substring(31, repNumerica.length());
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
        if ((11 - (soma % 11)) > 9) {
            return "0";
        } else {
            return Integer.toString(11 - (soma % 11));
        }
    }

    @Override
    public String getCedenteFormatado() {
        return boleto.getContaCobranca().getCodCedente().substring(0, 3) + "."
                + boleto.getContaCobranca().getCodCedente().substring(3, 6) + "."
                + boleto.getContaCobranca().getCodCedente().substring(6) + "-"
                + this.moduloOnze(boleto.getContaCobranca().getCodCedente());
    }

    @Override
    public String getNossoNumeroFormatado() {
        return boleto.getBoletoComposto();
    }

    @Override
    public String codigoBanco() {
        return "104-0";
    }
}
