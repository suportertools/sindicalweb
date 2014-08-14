package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.MovimentosReceberDB;
import br.com.rtools.financeiro.db.MovimentosReceberDBToplink;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;

public class MovimentosReceberJSFBean extends MovimentoValorJSFBean {

    private List<DataObject> listaMovimentos = new ArrayList();
    private Pessoa pessoa = new Pessoa();
    private String multa = "0";
    private String juros = "0";
    private String correcao = "0";
    private String desconto = "0";
    private String descontoTela = "0";
    private String total = "0";
    private String acrescimo = "0";
    private String acrescimoSemSindical = "0";
    private String totalSemSindical = "0";
    private boolean marcarTodos = false;
    private int index = 0;
    private String msgConfirma = "";

    public String imprimirPlanilha() {
        List<Movimento> listaC = new ArrayList<Movimento>();
        List<Float> listaValores = new ArrayList<Float>();
        MovimentoDB db = new MovimentoDBToplink();

        Movimento mov = new Movimento();
        for (int i = 0; i < listaMovimentos.size(); i++) {
            if ((Boolean) listaMovimentos.get(i).getArgumento0()) {
                mov = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimentos.get(i).getArgumento16())));

                mov.setMulta(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento8()));
                mov.setJuros(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento9()));
                mov.setCorrecao(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento10()));
                mov.setDesconto(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento11()));
                mov.setValorBaixa(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento12()));

                listaValores.add(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento6()));
                listaC.add(mov);
            }
        }

        if (!listaC.isEmpty()) {
            ImprimirBoleto imp = new ImprimirBoleto();
            imp.imprimirPlanilha(listaC, listaValores, false, false);
            imp.visualizar(null);
        } else {
            msgConfirma = "Nenhum boleto selecionado!";
        }
        return null;
    }

    public String imprimir() {
        MovimentoDB db = new MovimentoDBToplink();
        List<Movimento> lista = new ArrayList<Movimento>();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();

        if (!listaMovimentos.isEmpty()) {
            Movimento mov = new Movimento();
            for (int i = 0; i < listaMovimentos.size(); i++) {
                if ((Boolean) listaMovimentos.get(i).getArgumento0()) {
                    mov = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimentos.get(i).getArgumento16())));
                    if (mov.getTipoServico().getId() == 4 && Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento12()) <= 0) {
                        msgConfirma = "Acordo sem valor";
                        return null;
                    }

                    mov.setMulta(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento8()));
                    mov.setJuros(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento9()));
                    mov.setCorrecao(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento10()));
                    mov.setDesconto(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento11()));

                    //mov.setValor( Moeda.converteUS$((String)listaMovimentos.get(i).getArgumento12()) );
                    listaValores.add(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento12()));

                    if (DataHoje.converteDataParaInteger(mov.getVencimento()) < DataHoje.converteDataParaInteger(DataHoje.data())) {
                        DataHoje d = new DataHoje();
                        String novaData = d.incrementarMesesUltimoDia(1, d.decrementarMeses(1, DataHoje.data()));
                        //mov.setVencimento(novaData);
                        listaVencimentos.add(novaData);
                    } else {
                        listaVencimentos.add(mov.getVencimento());
                    }

                    lista.add(mov);
                }
            }
        }

        if (lista.isEmpty()) {
            msgConfirma = "Nenhum Boleto Selecionado!";
            return null;
        }
        ImprimirBoleto imp = new ImprimirBoleto();

        lista = imp.atualizaContaCobrancaMovimento(lista);

        imp.imprimirBoleto(lista, listaValores, listaVencimentos, false);
        imp.visualizar(null);
        listaMovimentos.clear();
        return null;
    }

    public String telaAcordo() {
        List lista = new ArrayList();
        ///MovimentoDB db = new MovimentoDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Movimento movimento = new Movimento();
        if (!listaMovimentos.isEmpty()) {
            sv.abrirTransacao();
            for (int i = 0; i < listaMovimentos.size(); i++) {
                if ((Boolean) listaMovimentos.get(i).getArgumento0()) {
                    movimento = (Movimento) sv.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimentos.get(i).getArgumento16())), "Movimento");

                    if (movimento.getTipoServico().getId() == 4) {
                        msgConfirma = "Não é possível criar este acordo novamente";
                        return null;
                    }

                    if (Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento12()) <= 0) {
                        msgConfirma = "Não é possível criar acordo com valores zerados";
                        return null;
                    }


                    movimento.setValor(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento6()));
                    if (!sv.alterarObjeto(movimento)) {
                        msgConfirma = "Erro ao alterar valor do Movimento id: " + movimento.getId();
                        sv.desfazerTransacao();
                    }

                    movimento.setMulta(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento8()));
                    movimento.setJuros(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento9()));
                    movimento.setCorrecao(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento10()));
                    movimento.setDesconto(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento11()));

                    movimento.setValorBaixa(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento12()));
                    lista.add(movimento);
                }
            }
            sv.comitarTransacao();
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

    public String telaBaixa() {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
        if (!listaMovimentos.isEmpty()) {
            for (int i = 0; i < listaMovimentos.size(); i++) {
                if ((Boolean) listaMovimentos.get(i).getArgumento0()) {
                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimentos.get(i).getArgumento16())));

                    movimento.setMulta(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento8()));
                    movimento.setJuros(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento9()));
                    movimento.setCorrecao(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento10()));
                    movimento.setDesconto(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento11()));
                    movimento.setValor(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento12()));

                    // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
                    movimento.setValorBaixa(movimento.getValor());
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

    public void carregarFolha(DataObject data) {
        
    }
    
    @Override
    public synchronized void carregarFolha() {
        if (!listaMovimentos.isEmpty()) {
            MovimentoDB db = new MovimentoDBToplink();
            Movimento movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimentos.get(index).getArgumento16())));
            super.carregarFolha(movimento);
        }
    }

    @Override
    public void atualizaValorGrid(String tipo) {
        listaMovimentos.get(index).setArgumento6(super.atualizaValor(true, tipo));
        listaMovimentos.clear(); // LIMPANDO AQUI PARA ATUALIZAR O VALOR CALCULADO
    }

    public void marcar() {
        for (int i = 0; i < listaMovimentos.size(); i++) {
            listaMovimentos.get(i).setArgumento0(marcarTodos);
        }
    }

    public String getTotal() {
        if (!listaMovimentos.isEmpty()) {
            float soma = 0;
            float somaS = 0;
            for (int i = 0; i < listaMovimentos.size(); i++) {
                if ((Boolean) listaMovimentos.get(i).getArgumento0()) {
                    if (listaMovimentos.get(i).getArgumento2().toString().toUpperCase().equals("SINDICAL")) {
                        somaS = Moeda.somaValores(soma, Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento17()));
                    }

                    soma = Moeda.somaValores(soma, Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento17()));
                }
            }
            totalSemSindical = Moeda.converteR$Float(Moeda.subtracaoValores(soma, somaS));
            return total = Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getMulta() {
        if (!listaMovimentos.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimentos.size(); i++) {
                if ((Boolean) listaMovimentos.get(i).getArgumento0()) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento8()));
                }
            }
            return multa = Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getJuros() {
        if (!listaMovimentos.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimentos.size(); i++) {
                if ((Boolean) listaMovimentos.get(i).getArgumento0()) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento9()));
                }
            }
            return juros = Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getCorrecao() {
        if (!listaMovimentos.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimentos.size(); i++) {
                if ((Boolean) listaMovimentos.get(i).getArgumento0()) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento10()));
                }
            }
            return correcao = Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getAcrescimo() {
        if (!listaMovimentos.isEmpty()) {
            return acrescimo = Moeda.converteR$Float(Moeda.somaValores(Moeda.somaValores(Moeda.converteUS$(juros), Moeda.converteUS$(correcao)), Moeda.converteUS$(multa)));
        } else {
            return "0";
        }
    }

    public String getAcrescimoSemSindical() {
        float m = 0;
        float j = 0;
        float c = 0;
        if (!listaMovimentos.isEmpty()) {
            for (int i = 0; i < listaMovimentos.size(); i++) {
                if (!listaMovimentos.get(i).getArgumento2().toString().toUpperCase().equals("SINDICAL") && ((Boolean) listaMovimentos.get(i).getArgumento0())) {
                    m = Moeda.somaValores(m, Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento8()));
                    j = Moeda.somaValores(j, Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento9()));
                    c = Moeda.somaValores(c, Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento10()));
                }
            }
            return acrescimoSemSindical = Moeda.converteR$(String.valueOf(Moeda.somaValores(Moeda.somaValores(m, j), c)));
        } else {
            return "0";
        }
    }

    public String getTotalPagar() {
        if (!listaMovimentos.isEmpty()) {
            return Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.substituiVirgulaFloat(total), Float.valueOf(desconto)));
        } else {
            return "0";
        }
    }

    public String removerPesquisa() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        pessoa = new Pessoa();
        listaMovimentos.clear();
        desconto = "0";
        return "movimentosReceber";
    }

    public void calculoDesconto() {
        float desc = 0;
        float acre = 0;

        if (Float.valueOf(desconto) > Moeda.substituiVirgulaFloat(acrescimoSemSindical)) {
            desconto = String.valueOf(Moeda.substituiVirgulaFloat(acrescimoSemSindical));
        }

        for (int i = 0; i < listaMovimentos.size(); i++) {
            if (!listaMovimentos.get(i).getArgumento2().toString().toUpperCase().equals("SINDICAL") && (Boolean) listaMovimentos.get(i).getArgumento0()) {
                acre = Moeda.somaValores(Moeda.somaValores(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento8()), Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento9())), Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento10()));

                desc = Moeda.multiplicarValores(Moeda.divisaoValores(Float.valueOf(desconto), Moeda.substituiVirgulaFloat(acrescimoSemSindical)), 100);
                desc = Moeda.divisaoValores(Moeda.multiplicarValores(acre, desc), 100);

                listaMovimentos.get(i).setArgumento11(Moeda.converteR$Float(desc));
                listaMovimentos.get(i).setArgumento12(Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento17()), desc)));
            } else {
                listaMovimentos.get(i).setArgumento11(Moeda.converteR$Float(0));
                listaMovimentos.get(i).setArgumento12(Moeda.converteR$Float(Moeda.converteUS$((String) listaMovimentos.get(i).getArgumento17())));
            }
        }
    }

    public void limpaLista() {
        listaMovimentos.clear();
    }

    public List getListaMovimentos() {
        if (listaMovimentos.isEmpty() && pessoa.getId() != -1) {
            MovimentosReceberDB db = new MovimentosReceberDBToplink();
            float desc = Moeda.substituiVirgulaFloat(desconto), tot = Moeda.substituiVirgulaFloat(total);
            //List lista = db.pesquisaListaMovimentosDesconto(pessoa.getId(), desc, tot);
            List lista = db.pesquisaListaMovimentos(pessoa.getId());
            for (int i = 0; i < lista.size(); i++) {

                if (((Vector) lista.get(i)).get(5) == null) {
                    ((Vector) lista.get(i)).set(5, 0.0);
                }
                if (((Vector) lista.get(i)).get(6) == null) {
                    ((Vector) lista.get(i)).set(6, 0.0);
                }
                if (((Vector) lista.get(i)).get(7) == null) {
                    ((Vector) lista.get(i)).set(7, 0.0);
                }
                if (((Vector) lista.get(i)).get(8) == null) {
                    ((Vector) lista.get(i)).set(8, 0.0);
                }
                if (((Vector) lista.get(i)).get(9) == null) {
                    ((Vector) lista.get(i)).set(9, 0.0);
                }
                if (((Vector) lista.get(i)).get(10) == null) {
                    ((Vector) lista.get(i)).set(10, 0.0);
                }
                if (((Vector) lista.get(i)).get(11) == null) {
                    ((Vector) lista.get(i)).set(11, 0.0);
                }

                if (((Integer) ((Vector) lista.get(i)).get(13)) < 0) {
                    ((Vector) lista.get(i)).set(13, 0);
                }
                //Movimento mov = (Movimento)(new SalvarAcumuladoDBToplink()).pesquisaCodigo( (Integer) ((Vector) lista.get(i)).get(15), "Movimento");
                listaMovimentos.add(new DataObject(
                        false,
                        ((Vector) lista.get(i)).get(0), // boleto
                        ((Vector) lista.get(i)).get(1), // servico
                        ((Vector) lista.get(i)).get(2), // tipo
                        ((Vector) lista.get(i)).get(3), // referencia
                        DataHoje.converteData((Date) ((Vector) lista.get(i)).get(4)), // vencimento
                        Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(5))), // valor_mov
                        ((Vector) lista.get(i)).get(6), // valor_folha
                        Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(7))), // multa
                        Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(8))), // juros
                        Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(9))), // correcao
                        Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(10))), // desconto
                        Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(11))), // valor_calculado
                        ((Vector) lista.get(i)).get(12), // meses em atraso
                        ((Vector) lista.get(i)).get(13), // dias em atraso
                        ((Vector) lista.get(i)).get(14), // indice
                        ((Vector) lista.get(i)).get(15), // id movimento
                        Moeda.converteR$(Double.toString((Double) ((Vector) lista.get(i)).get(11))), // valor_calculado original
                        null, // null
                        null, // null
                        null, // null
                        null // null
                        ));
            }
        }
        return listaMovimentos;
    }

    public void setListaMovimentos(List listaMovimentos) {
        this.listaMovimentos = listaMovimentos;
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
            listaMovimentos.clear();
            desconto = "0";
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
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

    public boolean isMarcarTodos() {
        return marcarTodos;
    }

    public void setMarcarTodos(boolean marcarTodos) {
        this.marcarTodos = marcarTodos;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
}