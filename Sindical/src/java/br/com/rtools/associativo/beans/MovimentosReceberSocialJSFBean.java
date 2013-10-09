package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.db.MovimentosReceberSocialDB;
import br.com.rtools.associativo.db.MovimentosReceberSocialDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Moeda;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class MovimentosReceberSocialJSFBean {

    private String porPesquisa = "";
    private List<DataObject> listaMovimento = new ArrayList();
    private String titular = "";
    private String beneficiario = "";
    private String data = "";
    private String boleto = "";
    private String diasAtraso = "";
    private String multa = "", juros = "", correcao = "";
    private String caixa = "";
    private String documento = "";
    private String desconto = "";

    public String getTotal() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));
                }
            }

            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getAcrescimo() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento7().toString()));
                }
            }

            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getTotalCalculado() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                }
            }
            return Moeda.converteR$Float(Moeda.subtracaoValores(soma, Float.valueOf(desconto)));
            //return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public void complementoPessoa(DataObject linha) {
        // COMENTARIO PARA ORDEM QUE VEM DA QUERY
        titular = (String) linha.getArgumento15(); // 13 - TITULAR
        beneficiario = (String) linha.getArgumento14(); // 12 - BENEFICIARIO
        data = DataHoje.converteData((Date) linha.getArgumento16()); // 16 - CRIACAO
        boleto = (String) linha.getArgumento17(); // 17 - BOLETO
        diasAtraso = linha.getArgumento18().toString(); // 18 - DIAS EM ATRASO
        multa = "R$ " + Moeda.converteR$(linha.getArgumento19().toString()); // 19 - MULTA
        juros = "R$ " + Moeda.converteR$(linha.getArgumento20().toString()); // 20 - JUROS
        correcao = "R$ " + Moeda.converteR$(linha.getArgumento21().toString()); // 21 - CORRECAO
        caixa = (linha.getArgumento22() == null) ? "Nenhum" : linha.getArgumento22().toString(); // 22 - CAIXA
        documento = (linha.getArgumento23() == null) ? "Sem Documento" : linha.getArgumento23().toString(); // 24 - DOCUMENTO

    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public List<DataObject> getListaMovimento() {
        if (listaMovimento.isEmpty()) {
            MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
            List<Vector> lista = db.pesquisaListaMovimentos();
            for (int i = 0; i < lista.size(); i++) {

                listaMovimento.add(new DataObject(
                        true,
                        lista.get(i).get(14),
                        lista.get(i).get(0),
                        lista.get(i).get(1),
                        lista.get(i).get(2),
                        lista.get(i).get(3),
                        lista.get(i).get(4),
                        lista.get(i).get(5),
                        lista.get(i).get(6),
                        lista.get(i).get(7),
                        lista.get(i).get(8),
                        lista.get(i).get(9),
                        lista.get(i).get(10),
                        lista.get(i).get(11),
                        lista.get(i).get(12),
                        lista.get(i).get(13),
                        lista.get(i).get(16),
                        lista.get(i).get(17),
                        lista.get(i).get(18),
                        lista.get(i).get(19),
                        lista.get(i).get(20),
                        lista.get(i).get(21),
                        lista.get(i).get(22),
                        lista.get(i).get(24),
                        null));
            }
        }
        return listaMovimento;
    }

    public void setListaMovimento(List<DataObject> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(String diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public String getMulta() {
        return multa;
    }

    public void setMulta(String multa) {
        this.multa = multa;
    }

    public String getJuros() {
        return juros;
    }

    public void setJuros(String juros) {
        this.juros = juros;
    }

    public String getCorrecao() {
        return correcao;
    }

    public void setCorrecao(String correcao) {
        this.correcao = correcao;
    }

    public String getCaixa() {
        return caixa;
    }

    public void setCaixa(String caixa) {
        this.caixa = caixa;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDesconto() {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        return Moeda.converteR$(desconto);
    }

    public void setDesconto(String desconto) {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        this.desconto = Moeda.substituiVirgula(desconto);
    }
}
