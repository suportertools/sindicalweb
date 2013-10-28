package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.db.MovimentosReceberSocialDB;
import br.com.rtools.associativo.db.MovimentosReceberSocialDBToplink;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Moeda;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;

public class MovimentosReceberSocialJSFBean {

    private String porPesquisa = "todos";
    private List<DataObject> listaMovimento = new ArrayList();
    private String titular = "";
    private String beneficiario = "";
    private String data = "";
    private String boleto = "";
    private String diasAtraso = "";
    private String multa = "", juros = "", correcao = "";
    private String caixa = "";
    private String documento = "";
    private String msgConfirma = "";
    private String desconto = "0";
    private boolean chkSeleciona = false;
    private Pessoa pessoa = new Pessoa();
    private List<Pessoa> listaPessoa = new ArrayList();
    
    public String removerPesquisa() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        pessoa = new Pessoa();
        return "movimentosReceberSocial";
    }

    public String removerPessoaLista(int index) {
        listaPessoa.remove(index);
        listaMovimento.clear();
        return "movimentosReceberSocial";
    }

    public String adicionarPesquisa() {
        listaPessoa.add(pessoa);
        pessoa = new Pessoa();
        listaMovimento.clear();
        return "movimentosReceberSocial";
    }

    public boolean baixado() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) > 0.0) {
                return true;
            }
        }
        return false;
    }

    public boolean semValor() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()) <= 0.0) {
                return true;
            }
        }
        return false;
    }
    
    public boolean acordados() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && String.valueOf(listaMovimento.get(i).getArgumento3()).equals("Acordo")) {
                return true;
            }
        }
        return false;
    }

    public String estornarBaixa() {
        if (listaMovimento.isEmpty()) {
            msgConfirma = "Não existem boletos para serem estornados!";
            return null;
        }

        if (!baixado()) {
            msgConfirma = "Existem boletos que não foram baixados na lista para estornar!";
            return null;
        }

        MovimentoDB db = new MovimentoDBToplink();
        boolean est = true;
        Movimento movimento = null;
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));
                if (!movimento.isAtivo()) {
                    msgConfirma = "Boleto ID: " + movimento.getId() + " esta inativo, não é possivel concluir estorno!";
                    return null;
                }
                if (!GerarMovimento.estornarMovimento(movimento)) {
                    est = false;
                }
            }
        }

        if (!est) {
            msgConfirma = "Ocorreu erros ao estornar boletos, verifique o log!";
        } else {
            msgConfirma = "Boletos estornados com sucesso!";
        }
        listaMovimento.clear();
        chkSeleciona = true;
        return null;
    }

    public String telaBaixa() {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
        if (baixado()) {
            msgConfirma = "Existem boletos baixados na lista!";
            return null;
        }

        if (semValor()) {
            msgConfirma = "Boletos sem valor não podem ser Baixados!";
            return null;
        }

        if (!listaMovimento.isEmpty()) {
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));

                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
                    movimento.setJuros(Moeda.converteUS$( listaMovimento.get(i).getArgumento20().toString() ));
                    movimento.setCorrecao(Moeda.converteUS$( listaMovimento.get(i).getArgumento21().toString()));
                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));

                    //movimento.setValor( Float.valueOf( listaMovimento.get(i).getArgumento9().toString() ) );
                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));

                    // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));

                    lista.add(movimento);
                }
            }
            if (!lista.isEmpty()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
            } else {
                msgConfirma = "Nenhum boleto foi selecionado";
            }
        } else {
            msgConfirma = "Lista vazia!";
        }
        return null;
    }

    public String telaMovimento(int id_movimento) {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
       
        
//                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));
        movimento = db.pesquisaCodigo(id_movimento);

//                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
//                    movimento.setJuros(Moeda.converteUS$( listaMovimento.get(i).getArgumento20().toString()));
//                    movimento.setCorrecao(Moeda.converteUS$( listaMovimento.get(i).getArgumento21().toString()));
//
//                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));
//
//                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));

                    // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
//                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
        lista.add(movimento);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).alterarMovimento();
    }
    
    public String telaAcordo() {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
        if (baixado()) {
            msgConfirma = "Existem boletos baixados na lista!";
            return null;
        }

        if (semValor()) {
            msgConfirma = "Boletos sem valor não podem ser Acordados!";
            return null;
        }
        
        if (acordados()) {
            msgConfirma = "Boletos do tipo Acordo não podem ser Reacordados!";
            return null;
        }
        if (!listaMovimento.isEmpty()) {
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));

                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
                    movimento.setJuros(Moeda.converteUS$( listaMovimento.get(i).getArgumento20().toString()));
                    movimento.setCorrecao(Moeda.converteUS$( listaMovimento.get(i).getArgumento21().toString()));

                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));

                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));

                    // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                    lista.add(movimento);
                }
            }
            if (!lista.isEmpty()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).acordo();
            } else {
                msgConfirma = "Nenhum boleto foi selecionado";
            }
        } else {
            msgConfirma = "Lista vazia!";
        }
        return null;
    }

    public void calculoDesconto() {
        float descPorcento = 0;
        float desc = 0;
        float acre = 0;
        float calc = Moeda.substituiVirgulaFloat(getValorPraDesconto());
        if (Float.valueOf(desconto) > calc) {
            desconto = String.valueOf(calc);
        }

        descPorcento = Moeda.multiplicarValores(Moeda.divisaoValores(Float.valueOf(desconto), calc), 100);
        
        
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                acre = Moeda.converteUS$(listaMovimento.get(i).getArgumento7().toString());
                
                float valor_calc = Moeda.somaValores(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()), acre);
                desc = Moeda.divisaoValores(Moeda.multiplicarValores(valor_calc, descPorcento), 100);
                
                listaMovimento.get(i).setArgumento8( Moeda.converteR$(String.valueOf(desc)) );
                listaMovimento.get(i).setArgumento9( Moeda.converteR$(String.valueOf(Moeda.subtracaoValores(valor_calc, desc) )));
            }
        }
    }

    public void atualizarStatus() {
        listaMovimento.clear();
    }

    public String getTotal() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
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

    public String getValorPraDesconto(){
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento24().toString()));
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
                if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                }
            }
            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public void complementoPessoa(DataObject linha) {
        // COMENTARIO PARA ORDEM QUE VEM DA QUERY
        titular = (String) linha.getArgumento15(); // 13 - TITULAR
        beneficiario = (String) linha.getArgumento14(); // 12 - BENEFICIARIO
        data = linha.getArgumento16().toString(); // 16 - CRIACAO
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

    public void marcarTodos() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            listaMovimento.get(i).setArgumento0(chkSeleciona);
        }
    }

    public List<DataObject> getListaMovimento() {
        if (listaMovimento.isEmpty()) {
            MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
            String ids = "";
            for (int i = 0; i < listaPessoa.size(); i++) {
                if (ids.length() > 0 && i != listaPessoa.size()) {
                    ids = ids + ",";
                }
                ids = ids + String.valueOf(listaPessoa.get(i).getId());
            }
            List<Vector> lista = db.pesquisaListaMovimentos(ids, porPesquisa);
            //float soma = 0;
            boolean chk = false, disabled = false;
            String dataBaixa = "";
            
            
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).get(8) != null){
                    dataBaixa = DataHoje.converteData( (Date)lista.get(i).get(8) );
                }else{
                    dataBaixa = "";
                }
                //soma = Moeda.somaValores(Moeda.converteR$(lista.get(i).get(5).toString()), Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                // DATA DE HOJE MENOR OU IGUAL A DATA DE VENCIMENTO
                if ( DataHoje.converteDataParaInteger(DataHoje.converteData((Date)lista.get(i).get(3))) <= 
                     DataHoje.converteDataParaInteger(DataHoje.data()) &&
                     dataBaixa.isEmpty()){
                    chk = true;
                }else{
                    chk = false;
                }
                
                // DATA DE HOJE MENOR QUE DATA DE VENCIMENTO
                if (DataHoje.converteDataParaInteger(DataHoje.converteData((Date)lista.get(i).get(3))) < 
                    DataHoje.converteDataParaInteger(DataHoje.data())){
                   disabled = true; 
                }else{
                   disabled = false;
                }
                
                listaMovimento.add(new DataObject(
                            chk, // ARG 0
                            lista.get(i).get(14), // ARG 1 ID_MOVIMENTO
                            lista.get(i).get(0), // ARG 2 SERVICO
                            lista.get(i).get(1), // ARG 3 TIPO_SERVICO
                            lista.get(i).get(2), // ARG 4 REFERENCIA
                            DataHoje.converteData((Date)lista.get(i).get(3)), // ARG 5 VENCIMENTO
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(4))), // ARG 6 VALOR
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(5))), // ARG 7 ACRESCIMO
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(6))), // ARG 8 DESCONTO
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(7))), // ARG 9 VALOR CALCULADO
                            dataBaixa, // ARG 10 DATA BAIXA
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(9))), // ARG 11 VALOR_BAIXA
                            lista.get(i).get(10), // ARG 12 ES
                            lista.get(i).get(11), // ARG 13 RESPONSAVEL
                            lista.get(i).get(12), // ARG 14 BENEFICIARIO
                            lista.get(i).get(13), // ARG 15 TITULAR
                            DataHoje.converteData((Date)lista.get(i).get(16)), // ARG 16 CRIACAO
                            lista.get(i).get(17), // ARG 17 BOLETO
                            lista.get(i).get(18), // ARG 18 DIAS DE ATRASO
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(19))), // ARG 29 MULTA
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(20))), // ARG 20 JUROS
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(21))), // ARG 21 CORRECAO
                            getConverteNullString(lista.get(i).get(22)), // ARG 22 CAIXA
                            lista.get(i).get(24), // ARG 23 DOCUMENTO
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(7))),  // ARG 24 VALOR CALCULADO ORIGINAL
                            disabled
                            )
                        );
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

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public boolean isChkSeleciona() {
        return chkSeleciona;
    }

    public void setChkSeleciona(boolean chkSeleciona) {
        this.chkSeleciona = chkSeleciona;
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
            listaMovimento.clear();
            calculoDesconto();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Pessoa> getListaPessoa() {
        return listaPessoa;
    }

    public void setListaPessoa(List<Pessoa> listaPessoa) {
        this.listaPessoa = listaPessoa;
    }
    
    public String getConverteNullString(Object object) {
        if (object == null) {
            return "";
        } else {
            return String.valueOf(object);
        }
    }
}
