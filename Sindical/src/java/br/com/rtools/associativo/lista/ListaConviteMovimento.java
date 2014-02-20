//package br.com.rtools.associativo.lista;
//
//import br.com.rtools.associativo.ConviteMovimento;
//import br.com.rtools.financeiro.Baixa;
//import br.com.rtools.financeiro.Movimento;
//
//public class ListaConviteMovimento {
//
//    private String convidado;
//    private String socio;
//    private String diretor;
//    private String operador;
//    private String emissa;
//    private String validade;
//    private String dataPagamento;
//    private String valor;
//+ "            sp.ds_nome    AS convidado,          "
//                + "            p.ds_nome     AS socio,              "
//                + "            pc.ds_nome    AS diretor,            "
//                + "            pu.ds_nome    AS operador,           "
//                + "            c.dt_emissao  AS emissao,            "
//                + "            c.dt_validade AS validade,           "
//                + "            b.dt_baixa    AS dataPagto,          "
//                + "            m.nr_valor    AS valor,              "
//                + "            m.nr_valor_baixa AS valor_pago,      "
//                + "            c.is_cortesia    AS cortesia,        "
//                + "            c.ds_obs         AS obs              "
//
//    public ListaConviteMovimento(ConviteMovimento conviteMovimento, Movimento movimento, Baixa baixa) {
//        this.conviteMovimento = conviteMovimento;
//        this.movimento = movimento;
//        this.baixa = baixa;
//    }
//
//    public ListaConviteMovimento() {
//        this.conviteMovimento = new ConviteMovimento();
//        this.movimento = new Movimento();
//        this.baixa = new Baixa();
//    }
//
//    public ConviteMovimento getConviteMovimento() {
//        return conviteMovimento;
//    }
//
//    public void setConviteMovimento(ConviteMovimento conviteMovimento) {
//        this.conviteMovimento = conviteMovimento;
//    }
//
//    public Movimento getMovimento() {
//        return movimento;
//    }
//
//    public void setMovimento(Movimento movimento) {
//        this.movimento = movimento;
//    }
//
//    public Baixa getBaixa() {
//        return baixa;
//    }
//
//    public void setBaixa(Baixa baixa) {
//        this.baixa = baixa;
//    }
//}
