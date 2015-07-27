package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.*;
import br.com.rtools.associativo.db.*;

import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.db.TipoServicoDB;
import br.com.rtools.financeiro.db.TipoServicoDBToplink;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDao;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;

import br.com.rtools.seguranca.db.RotinaDao;
import br.com.rtools.utilitarios.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class VendasCaravanaJSFBean1 {

    private Pessoa pessoa = new Pessoa();
    private Fisica fisica = new Fisica();
    private CVenda vendas = new CVenda();
    private Caravana caravana = new Caravana();
    private Reservas reservas = new Reservas();
    private EventoServicoValor eventoServicoValor = new EventoServicoValor();
    private EventoServico eventoServico = new EventoServico();
    private int idCaravana = 0;
    private int idTipo = 0;
    private int indice = 0;
    private int idPoltrona = 0;
    private int parcelas = 1;
    private List<SelectItem> resultLista = new Vector<SelectItem>();
    private List<DataObject> listaReservas = new ArrayList();
    private List<SelectItem> listaPolt = new Vector<SelectItem>();
    private List<DataObject> listaParcelas = new ArrayList();
    private List<Movimento> listaPagos = new ArrayList();
    private List<Movimento> listaMovimento = new ArrayList();
    private String msgConfirma = "";
    private String msgConfirma2 = "";
    private String valorEntrada = "0";
    private String valorLiquido = "0";
    private String valorTotal = "0";
    private String valorAPagar = "0";
    private String valorPago = "0";
    private String dataEntrada = DataHoje.data();
    private boolean renderMovimento = true;

    public String salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        List<Movimento> movs = new ArrayList();
        Movimento movimento = new Movimento();
        VendasCaravanaDB dbC = new VendasCaravanaDBToplink();
        Lote lot = new Lote();
        Evt evt = new Evt();
        FilialDB dbF = new FilialDao();
        TipoServicoDB dbT = new TipoServicoDBToplink();

        if (pessoa.getId() == -1) {
            msgConfirma = "Pesquise um responsável!";
            return null;
        }

        if (((Reservas) listaReservas.get(0).getArgumento1()).getId() == -1) {
            msgConfirma = "Não existe nenhum passageiro para este responsável";
            return null;
        }

        atualizaParcelaGrid();
        if (msgConfirma2.equals("Valor somado é MENOR que total!")) {
            msgConfirma = "Valor somado é MENOR que total!";
            return null;
        } else if (msgConfirma2.equals("Valor somado é MAIOR que total!")) {
            msgConfirma = "Valor somado é MAIOR que total!";
            return null;
        }

        sv.abrirTransacao();
        if (!sv.alterarObjeto(vendas)) {
            msgConfirma = "Erro ao atualizar venda!";
            sv.desfazerTransacao();
            return null;
        }

//        if (vendas.getEvt() != null)
//            movs = dbC.listaMovCaravana(vendas.getResponsavel().getId(), vendas.getEvt().getId());
//
//        if (!movs.isEmpty()){
//            msgConfirma = "Venda atualiza!";
//            sv.comitarTransacao();
//            return null;
//        }
        if (listaParcelas.size() == 1) {
            if (listaParcelas.get(0).getArgumento3() == null) {
                //lot = new Lote(-1, dbRot.pesquisaCodigo(142), "", DataHoje.data());
                if (!sv.inserirObjeto(lot)) {
                    msgConfirma = "Erro ao Inserir Lote!";
                    sv.desfazerTransacao();
                    return null;
                }
                evt = new Evt();
                if (!sv.inserirObjeto(evt)) {
                    msgConfirma = "Erro ao Salvar EVT!";
                    sv.desfazerTransacao();
                    return null;
                }
                vendas.setEvt(evt);
                if (!sv.alterarObjeto(vendas)) {
                    msgConfirma = "Erro ao atualizar venda!";
                    sv.desfazerTransacao();
                    return null;
                }
//                    movimento = new Movimento(-1,
//                                              lot,
//                                              eventoServico.getaEvento().getDescricaoEvento().getServicoMovimento(),
//                                              Moeda.substituiVirgulaFloat(((String)listaParcelas.get(0).getArgumento1())),
//                                              1,
//                                              eventoServico.getaEvento().getDescricaoEvento().getServicoMovimento().getPlano5,
//                                              dbF.pesquisaCodigo(1),
//                                              pessoa,
//                                              ((String)listaParcelas.get(0).getArgumento0()),
//                                              DataHoje.dataReferencia(((String)listaParcelas.get(0).getArgumento0())),
//                                              dbT.pesquisaCodigo(1),
//                                              1,
//                                              "D",
//                                              null,
//                                              evt,
//                                              null,
//                                              0);
                if (!sv.inserirObjeto(movimento)) {
                    msgConfirma = "Erro ao Salvar Parcela!";
                    sv.desfazerTransacao();
                    return null;
                }

                if (!listaPagos.isEmpty()) {
                    List<Movimento> listDel = new ArrayList();
                    for (int w = 0; w < listaPagos.size(); w++) {
                        //listDel = dbC.listaMovCaravanaBaixado(listaPagos.get(w).getLoteBaixa().getId());
                        for (int x = 0; x < listDel.size(); x++) {
                            if (!sv.deletarObjeto(sv.pesquisaCodigo(listDel.get(x).getId(), "Movimento"))) {
                                msgConfirma = "Erro Excluir Movimentos!";
                                sv.desfazerTransacao();
                                return null;
                            }
                        }
                    }
                }

                movs.add(movimento);
                sv.comitarTransacao();
                msgConfirma = "Reserva efetuada com sucesso!";
            } else {
                // AQUI É SE EXISTIR O MOVIMENTO
                msgConfirma = "Venda atualizada!";
                sv.comitarTransacao();
                return null;
            }
        } else {
            //lot = new Lote(-1, dbRot.pesquisaCodigo(142), "", DataHoje.data());
            if (!sv.inserirObjeto(lot)) {
                msgConfirma = "Erro ao Inserir Lote!";
                sv.desfazerTransacao();
                return null;
            }

            evt = new Evt();
            if (!sv.inserirObjeto(evt)) {
                msgConfirma = "Erro ao Salvar EVT!";
                sv.desfazerTransacao();
                return null;
            }
            vendas.setEvt(evt);
            if (!sv.alterarObjeto(vendas)) {
                msgConfirma = "Erro ao atualizar venda!";
                sv.desfazerTransacao();
                return null;
            }

            if (listaParcelas.get(0).getArgumento3() == null) {
                for (int i = 0; i < listaParcelas.size(); i++) {
//                    movimento = new Movimento(-1,
//                                              lot,
//                                              eventoServico.getaEvento().getDescricaoEvento().getServicoMovimento(),
//                                              Moeda.substituiVirgulaFloat(((String)listaParcelas.get(i).getArgumento1())),
//                                              1,
//                                              eventoServico.getaEvento().getDescricaoEvento().getServicoMovimento().getPlano5(),
//                                              dbF.pesquisaCodigo(1),
//                                              pessoa,
//                                              ((String)listaParcelas.get(i).getArgumento0()),
//                                              DataHoje.dataReferencia(((String)listaParcelas.get(i).getArgumento0())),
//                                              dbT.pesquisaCodigo(1),
//                                              1,
//                                              "D",
//                                              null,
//                                              evt,
//                                              null,
//                                              0);
                    if (!sv.inserirObjeto(movimento)) {
                        msgConfirma = "Erro ao Salvar parcelamento!";
                        sv.desfazerTransacao();
                        return null;
                    }
                    if (i == 0) {
                        movs.add(movimento);
                    }
                }
                if (!listaPagos.isEmpty()) {
                    List<Movimento> listDel = new ArrayList();
                    for (int w = 0; w < listaPagos.size(); w++) {
                        //listDel = dbC.listaMovCaravanaBaixado(listaPagos.get(w).getLoteBaixa().getId());
                        for (int x = 0; x < listDel.size(); x++) {
                            if (!sv.deletarObjeto(sv.pesquisaCodigo(listDel.get(x).getId(), "Movimento"))) {
                                msgConfirma = "Erro Excluir Movimentos!";
                                sv.desfazerTransacao();
                                return null;
                            }
                        }
                    }
                }
                sv.comitarTransacao();
                msgConfirma = "Reserva efetuada com sucesso!";
            } else {
                // AQUI É SE EXISTIR O MOVIMENTO
                msgConfirma = "Venda atualizada!";
                sv.comitarTransacao();
                return null;
            }
        }

        if (dataEntrada.equals(DataHoje.data())) {
            // ENVIAR MOVIMENTOS PARA BAIXA
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("arrayBaixa", null);
            novoVoid();
            //return ((chamadaPaginaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
        } else {
            novoVoid();
        }
        return null;
    }

    public String editar() {
        List<Reservas> listR = new ArrayList();
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        SociosDB dbS = new SociosDBToplink();
        FisicaDB dbF = new FisicaDBToplink();
        EventoServicoDB dbEs = new EventoServicoDBToplink();
        EventoServicoValorDB dbEv = new EventoServicoValorDBToplink();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        vendas = (CVenda)htmlTablePesquisa.getRowData();
        pessoa = vendas.getResponsavel();

        listR = db.listaReservasVenda(vendas.getId());
        listaReservas.clear();
        listaPagos.clear();
        String valor = "";
        float somaGrid = 0;
        for (int i = 0; i < listR.size(); i++) {
            //eventoServico = dbEs.pesquisaCodigo(listR.get(i).getEventoServico().getId());
            valor = Moeda.converteR$Float(dbS.descontoSocioEve(listR.get(i).getPessoa().getId(), listR.get(i).getEventoServico().getServicos().getId()));
            if (Float.parseFloat(Moeda.substituiVirgula(valor)) == 0) {
                valor = Moeda.converteR$Float(dbEv.pesquisaEventoServicoValor(listR.get(i).getEventoServico().getId()).getValor());
            }
            listaReservas.add(new DataObject(dbF.pesquisaFisicaPorPessoa(listR.get(i).getPessoa().getId()),
                    listR.get(i),
                    valor,
                    valor,
                    true,
                    false,
                    listR.get(i).getPoltrona(),
                    false,
                    listR.get(i).getEventoServico(),
                    null)
            );
            somaGrid = Moeda.somaValores(somaGrid, Moeda.substituiVirgulaFloat(valor));
        }

        listaReservas.add(new DataObject(new Fisica(),
                new Reservas(),
                Moeda.converteR$Float(eventoServicoValor.getValor()),
                Moeda.converteR$Float(eventoServicoValor.getValor()),
                false,
                true,
                0,
                true,
                eventoServico,
                null)
        );
        listaParcelas.clear();
        listaMovimento.clear();
        if (vendas.getEvt() != null) {
            listaMovimento = db.listaMovCaravana(vendas.getResponsavel().getId(), vendas.getEvt().getId());
        }

        if (!listaMovimento.isEmpty()) {
            //float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if (listaMovimento.get(i).getLote() != null) {
                    listaPagos.add(listaMovimento.get(i));
                    listaParcelas.add(new DataObject(listaMovimento.get(i).getVencimento(), Moeda.converteR$Float(listaMovimento.get(i).getValor()), true, listaMovimento.get(i), null, null));
                } else {
                    listaParcelas.add(new DataObject(listaMovimento.get(i).getVencimento(), Moeda.converteR$Float(listaMovimento.get(i).getValor()), false, listaMovimento.get(i), null, null));
                }
                //soma = Moeda.somaValores(soma, listaMovimento.get(i).getValor());
            }
            //if ( soma == somaGrid )
            //    renderMovimento = false;
            //else
            //    renderMovimento = true;
        }//else
        //renderMovimento = true;
        return "vendasCaravana";
    }

    public String excluir() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        List<Reservas> listR = new ArrayList();

        sv.abrirTransacao();
        if (vendas.getId() == -1) {
            sv.desfazerTransacao();
            msgConfirma = "Não existe venda para ser cancelada!";
            return null;
        }

        listR = db.listaReservasVenda(vendas.getId());
        for (int i = 0; i < listR.size(); i++) {
            reservas = (Reservas) sv.pesquisaCodigo(listR.get(i).getId(), "Reservas");
            if (!sv.deletarObjeto(reservas)) {
                msgConfirma = "Erro ao cancelar reservas!";
                sv.desfazerTransacao();
                novoVoid();
                return null;
            }
        }

        if (!listaMovimento.isEmpty()) {
            for (int i = 0; i < listaMovimento.size(); i++) {
                if (listaMovimento.get(i).getLote() != null) {
                    msgConfirma = "Reserva com parcela paga não pode ser excluída!";
                    sv.desfazerTransacao();
                    return null;
                }
            }
            Movimento mov = new Movimento();
            Lote lot = new Lote();
            for (int i = 0; i < listaMovimento.size(); i++) {
                mov = (Movimento) sv.pesquisaCodigo(listaMovimento.get(i).getId(), "Movimento");

                if (lot != null) {
                    lot = (Lote) sv.pesquisaCodigo(mov.getLote().getId(), "Lote");
                    if (!sv.deletarObjeto(lot)) {
                        msgConfirma = "Erro ao excluir Lote!";
                        sv.desfazerTransacao();
                        return null;
                    }
                    lot = null;
                }

                if (!sv.deletarObjeto(mov)) {
                    msgConfirma = "Erro ao excluir movimentos!";
                    sv.desfazerTransacao();
                    return null;
                }

            }
        }

        if (!sv.deletarObjeto(sv.pesquisaCodigo(vendas.getId(), "CVenda"))) {
            msgConfirma = "Erro ao cancelar Venda!";
            sv.desfazerTransacao();
            return null;
        } else {
            msgConfirma = "Reserva cancelada com sucesso!";
            sv.comitarTransacao();
            novoVoid();
            return null;
        }
    }

    public boolean salvarAdicionado(SalvarAcumuladoDB sv) {
        vendas.setResponsavel(pessoa);
        vendas.setaEvento(caravana.getaEvento());
        vendas.setEvt(null);
        if (sv.inserirObjeto(vendas)) {
            return true;
        } else {
            return false;
        }
    }

    public void novoVoid() {
        pessoa = new Pessoa();
        eventoServico = new EventoServico();
        eventoServicoValor = new EventoServicoValor();
        vendas = new CVenda();
        caravana = new Caravana();
        fisica = new Fisica();
        dataEntrada = DataHoje.data();
        idCaravana = 0;
        idPoltrona = 0;
        idTipo = 0;
        listaParcelas.clear();
        listaReservas.clear();
        listaPagos.clear();
        msgConfirma2 = "";
        parcelas = 1;
        reservas = new Reservas();
        valorEntrada = "0";
        valorLiquido = "0";
        valorTotal = "0";
        valorAPagar = "0";
        valorPago = "0";
    }

    public String novo() {
        return "vendasCaravana";
    }

    public String novaVenda() {
        novoVoid();
        return "vendasCaravana";
    }

    public String excluirReserva() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
//        Reservas res = (Reservas)sv.pesquisaCodigo( ((Reservas)listaReservas.get(htmlTable.getRowIndex()).getArgumento1()).getId() , "Reservas");
        Reservas res = null; //(Reservas)sv.pesquisaCodigo( ((Reservas)listaReservas.get(htmlTable.getRowIndex()).getArgumento1()).getId() , "Reservas");
        if (res != null) {
            sv.abrirTransacao();
            if (!sv.deletarObjeto(res)) {
                msgConfirma2 = "Erro ao excluir Reserva!";
                sv.desfazerTransacao();
                return null;
            }

            if (!listaMovimento.isEmpty()) {
                for (int i = 0; i < listaMovimento.size(); i++) {
                    if (listaMovimento.get(i).getLote() == null) {
                        if (!sv.deletarObjeto((Movimento) sv.pesquisaCodigo(listaMovimento.get(i).getId(), "Movimento"))) {
                            msgConfirma2 = "Erro ao excluir Movimentos!";
                            sv.desfazerTransacao();
                            return null;
                        }
                    }
                }
            }

            msgConfirma2 = "Reserva excluída!";
            sv.comitarTransacao();
            listaPolt = new Vector<SelectItem>();
            //listaReservas.remove(htmlTable.getRowIndex());
            //renderMovimento = true;
            listaParcelas.clear();
            return null;
        } else {
            //listaReservas.get(htmlTable.getRowIndex()).setArgumento0(new Fisica());
            //listaReservas.get(htmlTable.getRowIndex()).setArgumento6(0);
            msgConfirma2 = "Reserva excluída!";
            return null;
        }

    }

    public String adicionarParcela() {
        parcelamento();
        return null;
    }

    public void parcelamento() {
        String vencs = dataEntrada;
        String vlEnt = valorEntrada;
        DataHoje dh = new DataHoje();
        listaParcelas.clear();
//        if (!listaPagos.isEmpty()){
//            float soma = 0;
//            for (int i = 0; i < listaPagos.size(); i++){
//                soma = Moeda.somaValores(soma, listaPagos.get(i).getValor());
//            }
//            vlEnt = Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.substituiVirgulaFloat(vlEnt), soma));
//        }
        if (parcelas == 1) {
            listaParcelas.add(new DataObject(vencs, Moeda.converteR$(vlEnt), false, null, null, null));
        } else {
            listaParcelas.add(new DataObject(dataEntrada, Moeda.converteR$(valorEntrada), false, null, null, null));
            vlEnt = Moeda.converteR$Float(
                    Moeda.divisaoValores(
                            Moeda.subtracaoValores(
                                    //Moeda.substituiVirgulaFloat(valorTotal), Moeda.substituiVirgulaFloat(vlEnt)
                                    Moeda.substituiVirgulaFloat(valorAPagar), Moeda.substituiVirgulaFloat(vlEnt)
                            ),
                            parcelas - 1
                    ));
            for (int i = 1; i < parcelas; i++) {
                vencs = dh.incrementarMeses(1, vencs);
                listaParcelas.add(new DataObject(vencs, Moeda.converteR$(vlEnt), false, null, null, null));
            }
        }
    }

    public String adicionarReserva() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        atualizarValorServico();
        if (pessoa.getId() == -1) {
            msgConfirma2 = "Pesquise um responsável!";
            return null;
        }

        if (caravana.getId() == -1) {
            msgConfirma2 = "Erro confirmar caravana!";
            return null;
        }
//        if (((Fisica)listaReservas.get(htmlTable.getRowIndex()).getArgumento0()).getId() == -1){
//            msgConfirma2 = "Pesquise ou cadastre uma passageiro antes de salvar!";
//            return null;
//        }

        if (listaPolt.isEmpty()) {
            msgConfirma2 = "Não existe mais poltronas disponíveis!";
            return null;
        }

        sv.abrirTransacao();
        if (vendas.getId() == -1) {
            if (!salvarAdicionado(sv)) {
                msgConfirma2 = "A venda ainda não foi salva!";
                sv.desfazerTransacao();
                return null;
            }
        }

        //if (( ((Reservas)listaReservas.get(htmlTable.getRowIndex()).getArgumento1()).getId() == -1 )){
        if (true) {

//            reservas.setDesconto(0);
//            reservas.setPessoa(((Fisica)listaReservas.get(htmlTable.getRowIndex()).getArgumento0()).getPessoa());
//            reservas.setcVenda(vendas);
//            reservas.setPoltrona(Integer.valueOf ((String)listaReservas.get(htmlTable.getRowIndex()).getArgumento6()));
//            reservas.setEventoServico( (EventoServico)listaReservas.get(htmlTable.getRowIndex()).getArgumento8() );
            //if (sv.inserirObjeto(((Reservas)listaReservas.get(htmlTable.getRowIndex()).getArgumento1()))){
            if (sv.inserirObjeto(reservas)) {
                if (!deletarMovimentosNaoPagos(sv)) {
                    return null;
                }
                sv.comitarTransacao();

//                listaReservas.get(htmlTable.getRowIndex()).setArgumento4(true);
//                listaReservas.get(htmlTable.getRowIndex()).setArgumento5(false);
//                listaReservas.get(htmlTable.getRowIndex()).setArgumento7(false);
//                listaReservas.get(htmlTable.getRowIndex()).setArgumento1(reservas);
                reservas = new Reservas();
                fisica = new Fisica();
                listaPolt = new Vector<SelectItem>();
                msgConfirma2 = "Passageiro salvo com Sucesso!";
                listaReservas.add(new DataObject(fisica,
                        reservas,
                        Moeda.converteR$Float(eventoServicoValor.getValor()),
                        Moeda.converteR$Float(eventoServicoValor.getValor()),
                        false,
                        true,
                        0,
                        true,
                        eventoServico,
                        null)
                );
            } else {
                msgConfirma2 = "Erro ao salvar venda!";
                sv.desfazerTransacao();
            }
        } else {
//            ((Reservas)listaReservas.get(htmlTable.getRowIndex()).getArgumento1()).setDesconto(0);
//            ((Reservas)listaReservas.get(htmlTable.getRowIndex()).getArgumento1()).setPessoa(((Fisica)listaReservas.get(htmlTable.getRowIndex()).getArgumento0()).getPessoa());
            //if (sv.alterarObjeto(((Reservas)listaReservas.get(htmlTable.getRowIndex()).getArgumento1()))){
            if (true) {
                if (!deletarMovimentosNaoPagos(sv)) {
                    return null;
                }

                sv.comitarTransacao();
//                listaReservas.get(htmlTable.getRowIndex()).setArgumento4(true);
//                listaReservas.get(htmlTable.getRowIndex()).setArgumento5(false);
                //reservas = new Reservas();
                //fisica = new Fisica();
                listaPolt = new Vector<SelectItem>();
                msgConfirma2 = "Reserva atualizada com sucesso!";
            } else {
                msgConfirma2 = "Erro ao salvar venda!";
                sv.desfazerTransacao();
            }
        }
        //renderMovimento = true;
        return "vendasCaravana";
    }

    public boolean deletarMovimentosNaoPagos(SalvarAcumuladoDB sv) {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            listaPagos.clear();
            listaParcelas.clear();
            for (int i = 0; i < listaMovimento.size(); i++) {
                soma = soma + listaMovimento.get(i).getValor();
            }
            if (soma != Moeda.substituiVirgulaFloat(valorTotal)) {
                for (int i = 0; i < listaMovimento.size(); i++) {
                    if (listaMovimento.get(i).getLote() == null) {
                        if (!sv.deletarObjeto((Movimento) sv.pesquisaCodigo(listaMovimento.get(i).getId(), "Movimento"))) {
                            msgConfirma2 = "Erro ao excluir Movimentos!";
                            sv.desfazerTransacao();
                            return false;
                        }
                    } else {
                        listaPagos.add(listaMovimento.get(i));
                        //listaParcelas.add(new DataObject(listaMovimento.get(i).getVencimento(), Moeda.converteR$Float(listaMovimento.get(i).getValor()), true, null, null, null));
                    }
                }
                //renderMovimento = true;
                return true;
            } else {
                return true;
            }
        }
        return true;
    }

    public String atualizarReserva() {
//        listaReservas.get(htmlTable.getRowIndex()).setArgumento4(false);
//        listaReservas.get(htmlTable.getRowIndex()).setArgumento5(true);
        atualizarValorServico();
        return "vendasCaravana";
    }

    public void atualizarValorServico() {
        SociosDB db = new SociosDBToplink();
        float valor = 0;
        for (int i = 0; i < listaReservas.size(); i++) {
            if (!((Boolean) listaReservas.get(i).getArgumento4()) && ((Reservas) listaReservas.get(i).getArgumento1()).getId() == -1) {
                getFisica();
                valor = db.descontoSocioEve(((Fisica) listaReservas.get(i).getArgumento0()).getPessoa().getId(), eventoServico.getServicos().getId());
                listaReservas.get(i).setArgumento8(eventoServico);
                if (valor == 0) {
                    listaReservas.get(i).setArgumento2(Moeda.converteR$Float(eventoServicoValor.getValor()));
                    listaReservas.get(i).setArgumento3(Moeda.converteR$Float(Moeda.subtracaoValores(eventoServicoValor.getValor(), 0)));// NA VERDADE SUBTRAI PELO DESCONTO
                } else {
                    listaReservas.get(i).setArgumento2(Moeda.converteR$Float(valor));
                    listaReservas.get(i).setArgumento3(Moeda.converteR$Float(Moeda.subtracaoValores(valor, 0)));// NA VERDADE SUBTRAI PELO DESCONTO
                }
            }
        }
    }

    public List<DataObject> getListaReservas() {
        if (listaReservas.isEmpty()) {
            valorLiquido = Moeda.converteR$Float(Moeda.subtracaoValores(eventoServicoValor.getValor(), 0));// NA VERDADE SUBTRAI PELO DESCONTO
            listaReservas.add(new DataObject(fisica,
                    reservas,
                    Moeda.converteR$Float(eventoServicoValor.getValor()),
                    valorLiquido,
                    false,
                    true,
                    0,
                    true,
                    eventoServico,
                    null)
            );
            atualizarValorServico();
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null) {
            listaReservas.get(indice).setArgumento0(getFisica());
        }
        return listaReservas;
    }

    public List<SelectItem> getListaPoltronas() {
        List<Integer> select = new ArrayList();
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        if (caravana.getId() != -1) {
            if (listaPolt.isEmpty()) {
                select = db.listaPoltronasUsadas(caravana.getaEvento().getId());
                boolean adc = true;
                String pol = "";
                for (int i = 1; i <= caravana.getQuantidadePoltronas(); i++) {
                    for (int w = 0; w < select.size(); w++) {
                        if (i == select.get(w)) {
                            adc = false;
                            break;
                        }
                    }
                    if (adc) {
                        pol = "000" + i;
                        listaPolt.add(new SelectItem(new Integer(i), pol.substring(pol.length() - 2, pol.length()), Integer.toString(i)));
                    }
                    adc = true;
                }
            }
        }
        return listaPolt;
    }

    public void refreshForm() {

    }

    public String indice() {
//        indice = htmlTable.getRowIndex();
        return null;
    }

    public String refreshFormVenda() {
        return "vendasCaravana";
    }

    public String refreshFormPesquisa() {
        return "pesquisaVendasCaravana";
    }

    public List<SelectItem> getListaCaravanas() {
        List<Caravana> select = new ArrayList();
        CaravanaDB db = new CaravanaDBToplink();
        listaPolt.clear();
        idPoltrona = 0;
        if (resultLista.isEmpty()) {
            select = db.pesquisaTodos();
            for (int i = 0; i < select.size(); i++) {
                resultLista.add(new SelectItem(new Integer(i),
                        select.get(i).getaEvento().getDescricaoEvento().getDescricao() + " - " + select.get(i).getDataSaida(),
                        Integer.toString(select.get(i).getId())));
            }
        }
        return resultLista;
    }

    public List<SelectItem> getListaTipos() {
        List<SelectItem> result = new Vector<SelectItem>();
        List<EventoServico> select = new ArrayList();
        EventoServicoDB db = new EventoServicoDBToplink();
        EventoServicoValorDB dbE = new EventoServicoValorDBToplink();
        EventoServicoDB dbEs = new EventoServicoDBToplink();
        getCaravana();
        if (caravana.getId() != -1) {
            select = db.listaEventoServico(caravana.getaEvento().getId());
            for (int i = 0; i < select.size(); i++) {
                result.add(new SelectItem(new Integer(i),
                        select.get(i).getServicos().getDescricao(),
                        Integer.toString(select.get(i).getId())));
            }
            if (idTipo >= select.size()) {
                idTipo = 0;
            }
            eventoServico = dbEs.pesquisaCodigo(select.get(idTipo).getId());
            eventoServicoValor = dbE.pesquisaEventoServicoValor(eventoServico.getId());
            atualizarValorServico();
        }
        return result;
    }

    public void atualizaParcelaGrid() {
        float soma = 0;
        for (int i = 0; i < listaParcelas.size(); i++) {
            if (((String) listaParcelas.get(i).getArgumento1()).isEmpty()) {
                listaParcelas.get(i).setArgumento1(Moeda.converteR$("0"));
            }
            soma = Moeda.somaValores(soma, Moeda.substituiVirgulaFloat((String) listaParcelas.get(i).getArgumento1()));
            listaParcelas.get(i).setArgumento1(Moeda.converteR$((String) listaParcelas.get(i).getArgumento1()));
        }
        //if (soma < Moeda.substituiVirgulaFloat(valorTotal)){
        if (soma < Moeda.substituiVirgulaFloat(valorAPagar)) {
            msgConfirma2 = "Valor somado é MENOR que total!";
            //} else if (soma > Moeda.substituiVirgulaFloat(valorTotal)){
        } else if (soma > Moeda.substituiVirgulaFloat(valorAPagar)) {
            msgConfirma2 = "Valor somado é MAIOR que total!";
        } else {
            msgConfirma2 = "";
        }
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public CVenda getVendas() {
        return vendas;
    }

    public void setVendas(CVenda vendas) {
        this.vendas = vendas;
    }

    public int getIdCaravana() {
        return idCaravana;
    }

    public void setIdCaravana(int idCaravana) {
        this.idCaravana = idCaravana;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public Caravana getCaravana() {
        CaravanaDB db = new CaravanaDBToplink();
        VendasCaravanaDB dbV = new VendasCaravanaDBToplink();
        if (!getListaCaravanas().isEmpty()) {
            int qnt = 0;
            if (caravana.getId() != Integer.parseInt(getListaCaravanas().get(idCaravana).getDescription())) {
                caravana = db.pesquisaCodigo(Integer.parseInt(getListaCaravanas().get(idCaravana).getDescription()));
//                qnt = dbV.qntReservas(caravana.getaEvento().getId(), caravana.getaEvento().getDescricaoEvento().getGrupoEvento().getId());
//                if (qnt == -1)
//                    caravana.setQuantidadePoltronas(0);
//                else{
//                    qnt = caravana.getQuantidadePoltronas() - qnt;
//                    caravana.setQuantidadePoltronas(qnt);
//                }
            }
        }
        return caravana;
    }

    public void setCaravana(Caravana caravana) {
        this.caravana = caravana;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public Reservas getReservas() {
        return reservas;
    }

    public void setReservas(Reservas reservas) {
        this.reservas = reservas;
    }

    public List getListaPesquisaVendas() {
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        return db.pesquisaTodos();
    }

    public Fisica getFisica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null) {
            fisica = (Fisica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
            for (int i = 0; i < listaReservas.size(); i++) {
                if (((Fisica) listaReservas.get(i).getArgumento0()).getId() == fisica.getId()) {
                    listaReservas.get(indice).setArgumento0(new Fisica());
                    msgConfirma2 = "Este passageiro já tem reserva!";
                    return new Fisica();
                }
            }
            listaReservas.get(indice).setArgumento0(fisica);
            listaPagos.clear();
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public int getIdPoltrona() {
        return idPoltrona;
    }

    public void setIdPoltrona(int idPoltrona) {
        this.idPoltrona = idPoltrona;
    }

    public String getValorLiquido() {
        return Moeda.converteR$(valorLiquido);
    }

    public void setValorLiquido(String valorLiquido) {
        this.valorLiquido = Moeda.substituiVirgula(valorLiquido);
    }

    public String getValorTotal() {
        valorTotal = "0";
        for (int i = 0; i < listaReservas.size(); i++) {
            if (((Fisica) listaReservas.get(i).getArgumento0()).getId() != -1) {
                valorTotal = Moeda.converteR$Float(Moeda.somaValores(Moeda.converteUS$(valorTotal), Moeda.converteUS$((String) listaReservas.get(i).getArgumento3())));
            }
        }
        //valorTotal = Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.substituiVirgulaFloat(valorTotal), Moeda.substituiVirgulaFloat(valorPago)));
        return Moeda.converteR$(valorTotal);
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = Moeda.substituiVirgula(valorTotal);
    }

    public String getValorAPagar() {
        valorAPagar = "0";
        if (!listaParcelas.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaParcelas.size(); i++) {
                if (!(Boolean) listaParcelas.get(i).getArgumento2()) {
                    soma = Moeda.somaValores(soma, Moeda.substituiVirgulaFloat(((String) listaParcelas.get(i).getArgumento1())));
                }
            }
            valorAPagar = Moeda.converteR$Float(soma);
        } else {
            valorAPagar = Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.substituiVirgulaFloat(valorTotal), Moeda.substituiVirgulaFloat(valorPago)));
        }
        return Moeda.converteR$(valorAPagar);
    }
//    public String getValorAPagar() {
//        valorAPagar = "0";
//        for (int i = 0;i < listaReservas.size(); i++){
//            if ( ((Fisica)listaReservas.get(i).getArgumento0()).getId() != -1){
//                valorAPagar = Moeda.converteR$Float(Moeda.somaValores(Moeda.converteUS$(valorAPagar), Moeda.converteUS$( (String)listaReservas.get(i).getArgumento3())));
//            }
//        }
//        if ( Moeda.substituiVirgulaFloat(valorPago) == 0){
//            float soma = 0;
//            if (!listaParcelas.isEmpty()){
//                for (int i = 0; i < listaParcelas.size(); i++){
//                    soma = Moeda.somaValores(soma, Moeda.substituiVirgulaFloat(((String)listaParcelas.get(i).getArgumento1())));
//                }
//                valorAPagar = Moeda.converteR$Float(soma);
//            }else{
//                valorAPagar = valorTotal;
//            }
//        } else
//            valorAPagar = Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.substituiVirgulaFloat(valorAPagar), Moeda.substituiVirgulaFloat(valorPago)));
//        return Moeda.converteR$(valorAPagar);
//    }

    public void setValorAPagar(String valorAPagar) {
        this.valorAPagar = Moeda.substituiVirgula(valorAPagar);
    }

    public String getMsgConfirma2() {
        return msgConfirma2;
    }

    public void setMsgConfirma2(String msgConfirma2) {
        this.msgConfirma2 = msgConfirma2;
    }

    public int getParcelas() {
        if (parcelas < 1) {
            parcelas = 1;
        }
        //valorEntrada = Float.toString(Moeda.divisaoValores(Moeda.substituiVirgulaFloat(valorTotal), parcelas));
        valorEntrada = Float.toString(Moeda.divisaoValores(Moeda.substituiVirgulaFloat(valorAPagar), parcelas));
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public String getDataEntrada() {
        if (dataEntrada.length() == 10) {
            DataHoje dt = new DataHoje();
            String hoje = dt.incrementarDias(30, DataHoje.data());
            if ((DataHoje.converteDataParaInteger(dataEntrada) < DataHoje.converteDataParaInteger(DataHoje.data()))
                    || DataHoje.converteDataParaInteger(dataEntrada) > DataHoje.converteDataParaInteger(hoje)) {
                dataEntrada = DataHoje.data();
            }
        }
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public String getValorEntrada() {
        //if ( (Float.parseFloat(Moeda.substituiVirgula(valorEntrada))) > (Float.parseFloat(Moeda.substituiVirgula(valorTotal)) )){
        if ((Float.parseFloat(Moeda.substituiVirgula(valorEntrada))) > (Float.parseFloat(Moeda.substituiVirgula(valorAPagar)))) {
            //valorEntrada = valorTotal;
            valorEntrada = valorAPagar;
            parcelas = 1;
        } else {
            if (parcelas == 1) {
                //valorEntrada = valorTotal;
                valorEntrada = valorAPagar;
            }
//            String divi = Float.toString(Moeda.divisaoValores(Moeda.substituiVirgulaFloat(valorTotal), parcelas));
//            if ( Float.parseFloat(Moeda.substituiVirgula(valorEntrada)) <= Float.parseFloat(divi) )
//                valorEntrada = divi;
        }
        return Moeda.converteR$(valorEntrada);
    }

    public void setValorEntrada(String valorEntrada) {
        this.valorEntrada = Moeda.substituiVirgula(valorEntrada);
    }

    public List<DataObject> getListaParcelas() {
        return listaParcelas;
    }

    public void setListaParcelas(List<DataObject> listaParcelas) {
        this.listaParcelas = listaParcelas;
    }

//    public String getValorPago() {
//        getValorAPagar();
//        valorPago = Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.substituiVirgulaFloat(valorTotal), Moeda.substituiVirgulaFloat(valorAPagar)));
//        return Moeda.converteR$(valorPago);
//    }
    public String getValorPago() {
        if (!listaPagos.isEmpty()) {
//            float calc = 0;
//            for (int i = 0; i < listaPagos.size(); i++){
//                calc = Moeda.somaValores(calc, listaPagos.get(i).getValor());
//            }
            getValorAPagar();
//            valorPago = Moeda.converteR$Float(calc);
//            float pago = Moeda.substituiVirgulaFloat(valorPago);
            float sub = Moeda.subtracaoValores(Moeda.substituiVirgulaFloat(valorTotal), Moeda.substituiVirgulaFloat(valorAPagar));
            //valorPago = Moeda.converteR$Float(Moeda.somaValores(pago, sub));
            valorPago = Moeda.converteR$Float(sub);
        }
        return Moeda.converteR$(valorPago);
    }

    public void setValorPago(String valorPago) {
        this.valorPago = Moeda.substituiVirgula(valorPago);
    }

    public boolean isRenderMovimento() {
        float soma = 0;
        if (!listaMovimento.isEmpty()) {
            for (int i = 0; i < listaMovimento.size(); i++) {
                if (listaMovimento.get(i).getLote() == null) {
                    soma = Moeda.somaValores(soma, listaMovimento.get(i).getValor());
                }
            }
            if (soma == Moeda.substituiVirgulaFloat(valorAPagar)) {
                renderMovimento = false;
            } else {
                renderMovimento = true;
            }
        } else {
            renderMovimento = true;
        }
        return renderMovimento;
    }

    public void setRenderMovimento(boolean renderMovimento) {
        this.renderMovimento = renderMovimento;
    }

//    public String visualizarContrato(){
//        ArquivoContrato.lerContrato();
//        return null;
//    }
}
