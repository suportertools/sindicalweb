package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.CVenda;
import br.com.rtools.associativo.Caravana;
import br.com.rtools.associativo.EventoServico;
import br.com.rtools.associativo.EventoServicoValor;
import br.com.rtools.associativo.MatriculaSocios;
import br.com.rtools.associativo.Reservas;
import br.com.rtools.associativo.db.CaravanaDB;
import br.com.rtools.associativo.db.CaravanaDBToplink;
import br.com.rtools.associativo.db.EventoServicoDB;
import br.com.rtools.associativo.db.EventoServicoDBToplink;
import br.com.rtools.associativo.db.EventoServicoValorDB;
import br.com.rtools.associativo.db.EventoServicoValorDBToplink;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.associativo.db.VendasCaravanaDB;
import br.com.rtools.associativo.db.VendasCaravanaDBToplink;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.beans.FisicaBean;
import br.com.rtools.pessoa.beans.JuridicaBean;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class VendasCaravanaBean {

    private Caravana caravana = new Caravana();
    private CVenda vendas = new CVenda();
    private EventoServicoValor eventoServicoValor = new EventoServicoValor();
    private EventoServico eventoServico = new EventoServico();
    private Fisica pessoaFisica = new Fisica();
    private Fisica fisica = new Fisica();
    private Juridica pessoaJuridica = new Juridica();
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private final List<Caravana> listaCaravana = new ArrayList<Caravana>();
    private final List<SelectItem> listaCaravanaSelect = new ArrayList<SelectItem>();
    private final List<SelectItem> listaPoltrona = new ArrayList<SelectItem>();
    private List<SelectItem> listaDataEntrada = new ArrayList<SelectItem>();
    private List<SelectItem> listaMesVencimento = new ArrayList<SelectItem>();
    private List<DataObject> listaParcelas = new ArrayList();
    private List<DataObject> listaReserva = new ArrayList<DataObject>();
    private List<SelectItem> listaTipo = new ArrayList<SelectItem>();
    private List<SelectItem> listaDataVencimento = new ArrayList<SelectItem>();
    private int idCaravana = 0;
    private int idTipo = 0;
    private int idAdicionar = -1;
    private int idDataEntrada = 0;
    private int idMesVencimento = 0;
    private int idDiaVencimento = 0;
    private int parcelas = 1;
    private String dataEntrada = DataHoje.data();
    private Pessoa pessoa = new Pessoa();
    private String valorTotal = "0,00";
    private String valorPago = "0,00";
    private String valorOutras = "0,00";
    private String valorEntrada = "0,00";
    private Registro registro = new Registro();

    public void excluir() {
        if (vendas.getId() == -1) {
            GenericaMensagem.warn("Erro", "Pesquise uma venda para ser cancelada!");
            return;
        }

        List<Reservas> lr;
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        Reservas res;
        sv.abrirTransacao();

        lr = db.listaReservasVenda(vendas.getId());
        for (Reservas lr1 : lr) {
            res = (Reservas) sv.find(lr1);
            if (!sv.deletarObjeto(res)) {
                GenericaMensagem.warn("Erro", "Erro ao cancelar reservas!");
                sv.desfazerTransacao();
                return;
            }
        }

        List<Movimento> listaMovimento = db.listaMovCaravana(vendas.getResponsavel().getId(), vendas.getEvt().getId());
        if (!listaMovimento.isEmpty()) {
            for (Movimento listaMovimento1 : listaMovimento) {
                if (listaMovimento1.getBaixa() != null) {
                    GenericaMensagem.warn("Erro", "Reserva com parcela paga não pode ser excluída!");
                    sv.desfazerTransacao();
                    return;
                }
            }
            Movimento mov;
            Lote lot;
            Evt evt;
            for (Movimento listaMovimento1 : listaMovimento) {
                mov = (Movimento) sv.find(listaMovimento1);
                lot = (Lote) sv.find(mov.getLote());
                evt = (Evt) sv.find(mov.getLote().getEvt());
                if (!sv.deletarObjeto(mov)) {
                    GenericaMensagem.warn("Erro", "Erro ao excluir movimentos!");
                    sv.desfazerTransacao();
                    return;
                }
                if (!sv.deletarObjeto(lot)) {
                    GenericaMensagem.warn("Erro", "Erro ao excluir Lote!");
                    sv.desfazerTransacao();
                    return;
                }
                if (!sv.deletarObjeto(sv.find(vendas))) {
                    GenericaMensagem.warn("Erro", "Erro ao cancelar Venda!");
                    sv.desfazerTransacao();
                    return;
                }
                if (!sv.deletarObjeto(evt)) {
                    GenericaMensagem.warn("Erro", "Erro ao excluir EVT!");
                    sv.desfazerTransacao();
                    return;
                }
            }

            GenericaMensagem.info("Sucesso", "Reserva cancelada com sucesso!");
            sv.comitarTransacao();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("vendasCaravanaBean");
            return;
        } else {
            
        }
    }

    public String editar(CVenda v) {
        vendas = v;
        List<Reservas> lr;
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        FisicaDB dbf = new FisicaDBToplink();
        EventoServicoValorDB dbe = new EventoServicoValorDBToplink();
        SociosDB dbs = new SociosDBToplink();
        float valor;
        lr = db.listaReservasVenda(vendas.getId());
        listaReserva.clear();
        for (Reservas lr1 : lr) {
            valor = dbs.descontoSocioEve(lr1.getPessoa().getId(), lr1.getEventoServico().getServicos().getId());
            if (valor == 0) {
                valor = dbe.pesquisaEventoServicoValor(lr1.getEventoServico().getId()).getValor();
                listaReserva.add(new DataObject(dbf.pesquisaFisicaPorPessoa(lr1.getPessoa().getId()), 0, Moeda.converteR$Float(valor), Moeda.converteR$Float(lr1.getDesconto()), lr1, null));
            } else {
                listaReserva.add(new DataObject(dbf.pesquisaFisicaPorPessoa(lr1.getPessoa().getId()), 0, Moeda.converteR$Float(valor), Moeda.converteR$Float(lr1.getDesconto()), lr1, null));
            }
        }

        List<Movimento> listaMovimento = db.listaMovCaravana(vendas.getResponsavel().getId(), vendas.getEvt().getId());

        listaParcelas.clear();
        for (Movimento listaMovimento1 : listaMovimento) {
            if (listaMovimento1.getBaixa() == null) {
                listaParcelas.add(new DataObject(listaMovimento1.getVencimento(), Moeda.converteR$Float(listaMovimento1.getValor()), false, null, null, null));
            } else {
                listaParcelas.add(new DataObject(listaMovimento1.getVencimento(), Moeda.converteR$Float(listaMovimento1.getValor()), true, null, null, null));
            }
        }

        pessoa = vendas.getResponsavel();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return "vendasCaravana";
    }

    public void atualizaValoresParcela(int index) {
        listaParcelas.get(index).setArgumento1(Moeda.converteR$(listaParcelas.get(index).getArgumento1().toString()));
    }

    public void atualizaValoresGrid(int index) {
        listaReserva.get(index).setArgumento2(Moeda.converteR$(listaReserva.get(index).getArgumento2().toString()));
        if (Moeda.converteUS$(listaReserva.get(index).getArgumento3().toString()) > Moeda.converteUS$(listaReserva.get(index).getArgumento2().toString())) {
            listaReserva.get(index).setArgumento3(Moeda.converteR$(listaReserva.get(index).getArgumento2().toString()));
        } else {
            listaReserva.get(index).setArgumento3(Moeda.converteR$(listaReserva.get(index).getArgumento3().toString()));
        }
    }

    public String cadastroFisica() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaBean());
        ((FisicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).editarFisicaParametro(pessoaFisica);

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pessoaFisicaComParametros();
    }

    public String cadastroJuridica() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaBean", new JuridicaBean());
        ((JuridicaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaBean")).editar(pessoaJuridica);

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pessoaJuridicaComParametros();
    }

    public void salvar() {
        if (vendas.getId() != -1) {
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            sadb.abrirTransacao();
            if (sadb.alterarObjeto(vendas)) {
                sadb.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Venda atualizada com sucesso!");
            } else {
                sadb.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Erro ao atualizar esta venda!");
            }
            return;
        }

        if (pessoa.getId() == -1) {
            GenericaMensagem.warn("Erro", "Pesquise um responsável!");
            return;
        }

        if (listaReserva.isEmpty()) {
            GenericaMensagem.warn("Erro", "Não é possivel concluir nenhuma Reserva!");
            return;
        }

        if (listaParcelas.isEmpty()) {
            GenericaMensagem.warn("Erro", "Não é possivel concluir sem parcelas!");
            return;
        }
        float soma = 0;

        for (DataObject listaParcela : listaParcelas) {
            soma = Moeda.somaValores(soma, Moeda.converteUS$(String.valueOf(listaParcela.getArgumento1())));
        }

        if (soma < Moeda.converteUS$(valorTotal)) {
            GenericaMensagem.warn("Erro", "Valor das parcelas é MENOR que o valor total");
            return;
        }

        if (soma > Moeda.converteUS$(valorTotal)) {
            GenericaMensagem.warn("Erro", "Valor das parcelas é MAIOR que o valor total");
            return;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        Evt evt = new Evt();
        evt.setDescricao("Vendas Caravana");
        if (!sv.inserirObjeto(evt)) {
            GenericaMensagem.warn("Erro", "Não foi possível salvar EVT!");
            sv.desfazerTransacao();
            return;
        }

        vendas.setEvt(evt);

        if (!sv.inserirObjeto(vendas)) {
            GenericaMensagem.warn("Erro", "Não é possivel salvar venda!");
            return;
        }

        for (DataObject listaReserva1 : listaReserva) {
            Reservas res = new Reservas(-1, 
                    vendas, 
                    ((Fisica) listaReserva1.getArgumento0()).getPessoa(), 
                    Integer.valueOf(listaReserva1.getArgumento1().toString()), 
                    Moeda.converteUS$(listaReserva1.getArgumento3().toString()), 
                    ((Reservas) listaReserva1.getArgumento4()).getEventoServico());
            if (!sv.inserirObjeto(res)) {
                GenericaMensagem.warn("Erro", "Não é possivel salvar venda!");
                return;
            }
            listaReserva1.setArgumento4(res);
        }

        Lote lote = new Lote(-1, (Rotina) sv.find(new Rotina(), 142), "", DataHoje.data(), pessoa, null, false, "", Moeda.converteUS$(valorTotal), (Filial) sv.pesquisaObjeto(1, "Filial"),
                (Departamento) sv.find(new Departamento(), 6), evt, "", null, null, null, null, false, 0);
        if (!sv.inserirObjeto(lote)) {
            GenericaMensagem.warn("Erro", "Não foi possível salvar Lote!"); 
            sv.desfazerTransacao();
            return;
        }
        Movimento movimento;
        EventoServicoValor esv;
        for (int i = 0; i < listaParcelas.size(); i++) {
//            if(listaReserva.get(i).getArgumento6() == null) {
//                GenericaMensagem.warn("Erro", "Não foi possível salvar parcela nº"+ i+1 + " deste movimento!");
//                sv.desfazerTransacao();
//                return;
//            }
//            esv = (EventoServicoValor) listaReserva.get(0).getArgumento6();
            movimento = new Movimento(-1,
                    lote,
                    eventoServico.getServicos().getPlano5(), //esv.getEventoServico().getServicos().getPlano5(),
                    pessoa,
                    eventoServico.getServicos(), //esv.getEventoServico().getServicos(),
                    null,
                    (TipoServico) sv.find(new TipoServico(), 1),
                    null,
                    Moeda.converteUS$(String.valueOf(listaParcelas.get(i).getArgumento1())),
                    DataHoje.dataReferencia(String.valueOf(listaParcelas.get(i).getArgumento0())),
                    String.valueOf(listaParcelas.get(i).getArgumento0()),
                    parcelas,
                    true,
                    "",
                    false,
                    pessoa,
                    pessoa,
                    "",
                    "",
                    "",
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    null,
                    0, new MatriculaSocios());
            if (!sv.inserirObjeto(movimento)) {
                GenericaMensagem.warn("Erro", "Não é possivel salvar movimento!");
                return;
            }
        }

        GenericaMensagem.info("Sucesso", "Reserva concluída com Sucesso!");
        sv.comitarTransacao();
    }

    public void gerarParcelas() {
        if (parcelas < 0) {
            return;
        }

        String vencs = dataEntrada;
        String vlEnt = valorEntrada;
        float vE = Moeda.substituiVirgulaFloat(valorEntrada);
        DataHoje dh = new DataHoje();
        listaParcelas.clear();
        String vencimento = dataVencimento();
        if (parcelas == 1) {
            listaParcelas.add(new DataObject(vencs, Moeda.converteR$(valorTotal), false, null, null, null));
        } else {
            if (vE > 0) {
                listaParcelas.add(new DataObject(dataEntrada, Moeda.converteR$(valorEntrada), false, null, null, null));
                vlEnt = Moeda.converteR$Float(
                        Moeda.divisaoValores(
                                Moeda.subtracaoValores(Moeda.substituiVirgulaFloat(valorTotal), Moeda.substituiVirgulaFloat(vlEnt)
                                //Moeda.substituiVirgulaFloat(valorAPagar), Moeda.substituiVirgulaFloat(vlEnt)
                                ),
                                parcelas - 1
                        ));
                for (int i = 1; i < parcelas; i++) {
                    if (i > 1) {
                        vencimento = dh.incrementarMeses(1, vencimento);
                    }
                    listaParcelas.add(new DataObject(vencimento, Moeda.converteR$(vlEnt), false, null, null, null));
                }
            } else {
                float vParcela;
                for (int i = 0; i < parcelas; i++) {
                    vParcela = Moeda.substituiVirgulaFloat(valorTotal) / parcelas;
                    if (i > 0) {
                        vencimento = dh.incrementarMeses(1, vencimento);
                    }
                    listaParcelas.add(new DataObject(vencimento, Moeda.converteR$("" + vParcela), false, null, null, null));
                }
            }
        }
    }

    public String dataVencimento() {
        String dataVencimento;
        String mesPrimeiraParcela;
        String mes;
        String ano;
        if(!listaMesVencimento.isEmpty()) {
            mesPrimeiraParcela = listaMesVencimento.get(idMesVencimento).getDescription();            
        } else {
            mesPrimeiraParcela = DataHoje.dataReferencia(DataHoje.data());
        }
        mes = mesPrimeiraParcela.substring(0, 2);
        ano = mesPrimeiraParcela.substring(3, 7);
        if (DataHoje.qtdeDiasDoMes(Integer.parseInt(mes), Integer.parseInt(ano)) >= idDiaVencimento) {
            if (idDiaVencimento < 10) {
                dataVencimento = "0" + idDiaVencimento + "/" + mes + "/" + ano;
            } else {
                dataVencimento = idDiaVencimento + "/" + mes + "/" + ano;
            }
        } else {
            String diaSwap = Integer.toString(DataHoje.qtdeDiasDoMes(Integer.parseInt(mes), Integer.parseInt(ano)));
            if (diaSwap.length() < 2) {
                diaSwap = "0" + diaSwap;
            }
            dataVencimento = diaSwap + "/" + mes + "/" + ano;
        }
        return dataVencimento;
    }

    public void adicionarReserva() {
        if (pessoa.getId() == -1) {
            GenericaMensagem.warn("Erro", "Pesquise um responsável!");
            return;
        }

        if (pessoaEndereco.getId() == -1) {
            GenericaMensagem.warn("Erro", "Cadastre um endereço para este responsável!");
            return;
        }

        if (pessoaFisica.getId() != -1) {
            // VERIFICA SE PESSOA É MAIOR DE IDADE
            DataHoje dh = new DataHoje();
            int idade = dh.calcularIdade(pessoaFisica.getNascimento());
            if (idade < 18) {
                GenericaMensagem.warn("Erro", "Esta pessoa não é maior de idade, não poderá ser responsável!");
                return;
            }
        }

        if (listaCaravana.get(idCaravana).getId() == -1) {
            GenericaMensagem.warn("Erro", "Erro confirmar caravana!");
            return;
        }

        if (getListaPoltrona().isEmpty()) {
            GenericaMensagem.warn("Erro", "Não existe mais poltronas disponíveis!");
            return;
        }

        //EventoServicoDB dbEs = new EventoServicoDBToplink();
        // PASSAGEIRO --- VALOR --- DESCONTO --- TIPO / VALOR
        //listaReserva.add(new DataObject(new Fisica(), 0, "0,00", "0,00", dbEs.pesquisaCodigo(Integer.valueOf(listaTipo.get(idTipo).getDescription())), eventoServico));
        Reservas re = new Reservas();
        re.setEventoServico(eventoServico);
        //listaReserva.add(new DataObject(new Fisica(), 0, "0,00", "0,00", eventoServico, eventoServicoValor));
        listaReserva.add(new DataObject(new Fisica(), 0, "0,00", "0,00", re, eventoServicoValor));
    }

    public String pesquisaPassageiro(int index) {
        idAdicionar = index;
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pesquisaPessoaFisica();
    }

    public void removerPessoa() {
        pessoa = new Pessoa();
    }

    public void removerReserva(int index, DataObject datao) {
        listaReserva.remove(index);
    }

    public void atualizaCaravana() {
        caravana = listaCaravana.get(idCaravana);
        listaTipo.clear();
    }

    public void atualizaTipo() {
        listaTipo.clear();
    }

    public List getListaPesquisaVendas() {
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        return db.pesquisaTodos();
    }

    public List<SelectItem> getListaCaravanaSelect() {
        if (listaCaravanaSelect.isEmpty()) {
            CaravanaDB db = new CaravanaDBToplink();
            List<Caravana> result = db.pesquisaTodos();
            for (int i = 0; i < result.size(); i++) {
                listaCaravanaSelect.add(new SelectItem(i, result.get(i).getDataSaida() + " - " + result.get(i).getHoraSaida() + " - " + result.get(i).getaEvento().getDescricaoEvento().getDescricao(), String.valueOf(result.get(i).getId())));
                listaCaravana.add(result.get(i));
            }
            caravana = listaCaravana.get(idCaravana);
        }
        return listaCaravanaSelect;
    }

    public Fisica getFisica() {
        if (GenericaSessao.exists("fisicaPesquisa") && idAdicionar == -1) {
            fisica = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
        }

        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public List<SelectItem> getListaPoltrona() {
        List<Integer> select;
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        if (!listaCaravana.isEmpty() && listaPoltrona.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            //Caravana caravanax = (Caravana)sv.pesquisaCodigo(caravana.getId(), "Caravana");
            select = db.listaPoltronasUsadas(listaCaravana.get(idCaravana).getaEvento().getId());

            boolean adc = true;
            String pol;
            for (int i = 1; i <= listaCaravana.get(idCaravana).getQuantidadePoltronas(); i++) {
                for (Integer select1 : select) {
                    if (i == select1) {
                        adc = false;
                        break;
                    }
                }
                if (adc) {
                    pol = "000" + i;
                    listaPoltrona.add(new SelectItem(i, pol.substring(pol.length() - 2, pol.length()), "" + i));
                    //listaPoltrona.add(pol.substring(pol.length() - 2, pol.length()));
                }
                adc = true;
            }
        }
        return listaPoltrona;
    }

    public List<SelectItem> getListaTipo() {
        if (!listaCaravana.isEmpty()) {

            if (listaTipo.isEmpty() && listaCaravana.get(idCaravana).getId() != -1) {
                List<EventoServico> select;
                EventoServicoDB db = new EventoServicoDBToplink();
                EventoServicoValorDB dbE = new EventoServicoValorDBToplink();
                EventoServicoDB dbEs = new EventoServicoDBToplink();
                if (listaCaravana.get(idCaravana).getId() != -1) {
                    select = db.listaEventoServico(listaCaravana.get(idCaravana).getaEvento().getId());
                    for (int i = 0; i < select.size(); i++) {
                        listaTipo.add(new SelectItem(i, select.get(i).getDescricao(), "" + select.get(i).getId()));
                    }
                    if (idTipo >= select.size()) {
                        idTipo = 0;
                    }
                    eventoServico = dbEs.pesquisaCodigo(select.get(idTipo).getId());
                    eventoServicoValor = dbE.pesquisaEventoServicoValor(eventoServico.getId());
                }
            }
        }
        return listaTipo;
    }

    public String novo() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("vendasCaravanaBean");
        return "vendasCaravana";
    }

    public void setListaTipo(List<SelectItem> listaTipo) {
        this.listaTipo = listaTipo;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }

    public CVenda getVendas() {
        return vendas;
    }

    public Pessoa getPessoa() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            idDiaVencimento = 0;
            pessoa = (Pessoa) GenericaSessao.getObject("pessoaPesquisa", true);
            /**
             * Tipo : 0 => Aluno / 1 => Responsável
             *
             * @param tipo
             */
            PessoaDB pdb = new PessoaDBToplink();
            PessoaComplemento pc;
            Pessoa p;
            p = pessoa;
            pc = pdb.pesquisaPessoaComplementoPorPessoa(p.getId());
            if (pc.getId() == -1) {
                Registro r;
                SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
                r = (Registro) sadb.find("Registro", 1);
                if (r.getId() != -1) {
                    pc.setNrDiaVencimento(r.getFinDiaVencimentoCobranca());
                    pc.setCobrancaBancaria(true);
                    pc.setPessoa(p);
                    sadb.abrirTransacao();
                    if (sadb.inserirObjeto(pc)) {
                        sadb.comitarTransacao();
                    } else {
                        sadb.desfazerTransacao();
                    }
                }
            }
            FisicaDB dbf = new FisicaDBToplink();
            JuridicaDB dbj = new JuridicaDBToplink();
            pessoaFisica = dbf.pesquisaFisicaPorPessoa(pessoa.getId());
            if (pessoaFisica == null) {
                pessoaJuridica = dbj.pesquisaJuridicaPorPessoa(pessoa.getId());
                pessoaFisica = new Fisica();
                if (pessoaJuridica == null) {
                    pessoaJuridica = new Juridica();
                }
            } else {
                pessoaJuridica = new Juridica();
            }

            vendas.setResponsavel(pessoa);
            vendas.setaEvento(caravana.getaEvento());
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<DataObject> getListaReserva() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null && idAdicionar != -1) {
            Fisica fis = (Fisica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
            listaReserva.get(idAdicionar).setArgumento0(fis);

            SociosDB db = new SociosDBToplink();
            float valor;
            //valor = db.descontoSocioEve(fis.getPessoa().getId() , eventoServico.getServicos().getId() );
            valor = db.descontoSocioEve(fis.getPessoa().getId(), ((Reservas) listaReserva.get(idAdicionar).getArgumento4()).getEventoServico().getId());
            if (valor == 0) {
                listaReserva.get(idAdicionar).setArgumento2(Moeda.converteR$Float(((EventoServicoValor) listaReserva.get(idAdicionar).getArgumento5()).getValor()));
                //listaReserva.get(idAdicionar).setArgumento3(Moeda.converteR$Float( Moeda.subtracaoValores(eventoServicoValor.getValor(), 0)));// NA VERDADE SUBTRAI PELO DESCONTO
            } else {
                listaReserva.get(idAdicionar).setArgumento2(Moeda.converteR$Float(valor));
                //listaReserva.get(idAdicionar).setArgumento3(Moeda.converteR$Float( Moeda.subtracaoValores(valor, 0)));// NA VERDADE SUBTRAI PELO DESCONTO
            }
            idAdicionar = -1;
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        }
        return listaReserva;
    }

    public void setListaReserva(List<DataObject> listaReserva) {
        this.listaReserva = listaReserva;
    }

    public List<DataObject> getListaParcelas() {
        return listaParcelas;
    }

    public void setListaParcelas(List<DataObject> listaParcelas) {
        this.listaParcelas = listaParcelas;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(String dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public int getIdCaravana() {
        return idCaravana;
    }

    public void setIdCaravana(int idCaravana) {
        this.idCaravana = idCaravana;
    }

    public Caravana getCaravana() {
        return caravana;
    }

    public void setCaravana(Caravana caravana) {
        this.caravana = caravana;
    }

    public String getValorTotal() {
        if (!listaReserva.isEmpty()) {
            float valor = 0;
            float desconto = 0;
            for (DataObject listaReserva1 : listaReserva) {
                if (((Fisica) listaReserva1.getArgumento0()).getId() != -1) {
                    valor = Moeda.somaValores(valor, Moeda.substituiVirgulaFloat(String.valueOf(listaReserva1.getArgumento2())));
                    desconto = Moeda.somaValores(desconto, Moeda.substituiVirgulaFloat(String.valueOf(listaReserva1.getArgumento3())));
                }
            }
            valorTotal = Moeda.converteR$Float(Moeda.subtracaoValores(valor, desconto));
        } else {
            valorTotal = "0,00";
        }
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getValorPago() {
        if (!listaParcelas.isEmpty()) {
            float valor = 0;
            for (DataObject listaParcela : listaParcelas) {
                if (((Boolean) listaParcela.getArgumento2()) == true) {
                    valor = Moeda.somaValores(valor, Moeda.substituiVirgulaFloat(String.valueOf(listaParcela.getArgumento1())));
                }
            }
            valorPago = Moeda.converteR$Float(valor);
        } else {
            valorPago = "0,00";
        }
        return valorPago;
    }

    public void setValorPago(String valorPago) {
        this.valorPago = valorPago;
    }

    public String getValorOutras() {
        if (!listaParcelas.isEmpty()) {
            float valor = 0;
            for (DataObject listaParcela : listaParcelas) {
                if (((Boolean) listaParcela.getArgumento2()) != true) {
                    valor = Moeda.somaValores(valor, Moeda.substituiVirgulaFloat(String.valueOf(listaParcela.getArgumento1())));
                }
            }
            valorOutras = Moeda.converteR$Float(valor);
        } else {
            valorOutras = "0,00";
        }
        return valorOutras;
    }

    public void setValorOutras(String valorOutras) {
        this.valorOutras = valorOutras;
    }

    public String getValorEntrada() {
        if (valorEntrada.isEmpty()) {
            valorEntrada = "0";
        }

        if (Moeda.converteUS$(valorEntrada) > Moeda.converteUS$(valorTotal)) {
            valorEntrada = valorTotal;
        }
        if (Moeda.converteUS$(valorEntrada) < 0) {
            valorEntrada = "0";
        }

        return Moeda.converteR$(valorEntrada);
    }

    public void setValorEntrada(String valorEntrada) {
        if (valorEntrada.isEmpty()) {
            valorEntrada = "0";
        }
        this.valorEntrada = Moeda.substituiVirgula(valorEntrada);
    }

    public PessoaEndereco getPessoaEndereco() {
        if (pessoaEndereco.getId() == -1) {
            PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
            pessoaEndereco = dbp.pesquisaEndPorPessoaTipo(pessoa.getId(), 4);
            if (pessoaEndereco == null) {
                pessoaEndereco = new PessoaEndereco();
            }
        }
        return pessoaEndereco;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public Fisica getPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(Fisica pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    public Juridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(Juridica pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    public int getIdDataEntrada() {
        return idDataEntrada;
    }

    public void setIdDataEntrada(int idDataEntrada) {
        this.idDataEntrada = idDataEntrada;
    }

    public int getIdMesVencimento() {
        return idMesVencimento;
    }

    public void setIdMesVencimento(int idMesVencimento) {
        this.idMesVencimento = idMesVencimento;
    }

    public List<SelectItem> getListaEntrada() {
        float vE = Moeda.substituiVirgulaFloat(valorEntrada);
        if (vE > 0) {
            if (listaDataEntrada.isEmpty()) {
                idDataEntrada = 0;
                DataHoje dh = new DataHoje();
                String dataEntradaX;
                for (int i = 0; i < 20; i++) {
                    dataEntradaX = dh.incrementarDias(i, DataHoje.data());
                    listaDataEntrada.add(new SelectItem(i, dataEntradaX, dataEntradaX));
                    if (dataEntradaX.equals(DataHoje.data())) {
                        idDataEntrada = i;
                    }
                }
            }
        }
        return listaDataEntrada;
    }

    public void setListaDataEntrada(List<SelectItem> listaDataEntrada) {
        this.listaDataEntrada = listaDataEntrada;
    }

    public List<SelectItem> getListaMesVencimento() {
        if (listaMesVencimento.isEmpty()) {
            int dE = DataHoje.converteDataParaInteger(dataEntrada);
            boolean isTaxa = false;
            String data;
            DataHoje dh = new DataHoje();
            data = DataHoje.data();
            int dH = DataHoje.converteDataParaInteger(data);
            int dtVecto = 0;
            float vE = Moeda.substituiVirgulaFloat(valorEntrada);
            if(vE > 0) {
                //dtVecto = Integer.parseInt(dataEntrada.substring(0, 2));
                data = dh.incrementarMeses(1, data);
            } else {
                data = DataHoje.data();
            }
//            if (dE >= dH && dtVecto > idDiaVencimento && vE > 0) {
//                data = dh.incrementarMeses(1, data);
//            } else if (dE >= dH && dtVecto > idDiaVencimento) {
//                data = dh.incrementarMeses(1, data);
//            } else {
//            }
            String mesAno;
            int iDtMr;
            int iDtVct;
            int qtdeParcelas = parcelas;
            if(parcelas < 6) {
                qtdeParcelas = 6;
            }
            for (int i = 0; i < qtdeParcelas; i++) {
                if (i > 0) {
                    data = dh.incrementarMeses(1, data);
                }
                if (!isTaxa) {
                    iDtMr = DataHoje.converteDataParaInteger(DataHoje.data());
                    iDtVct = DataHoje.converteDataParaInteger(data);
                    if (vE > 0) {
                        if (iDtVct > iDtMr) {
                            idMesVencimento = i;
                            isTaxa = true;
                        } else {
                            idMesVencimento = 0;
                        }
                    } else {
                        isTaxa = true;
                        idMesVencimento = 0;
                    }
                }
                mesAno = data.substring(3, 5) + "/" + data.substring(6, 10);
                listaMesVencimento.add(new SelectItem(i, mesAno, mesAno));
            }
        }
        return listaMesVencimento;
    }

    public void setListaMesVencimento(List<SelectItem> listaMesVencimento) {
        this.listaMesVencimento = listaMesVencimento;
    }

    public List<SelectItem> getListaDataVencimento() {
        if (listaDataVencimento.isEmpty()) {
            for (int i = 1; i <= 31; i++) {
                listaDataVencimento.add(new SelectItem(Integer.toString(i)));
            }
        }
        return listaDataVencimento;
    }

    public void setListaDataVencimento(List<SelectItem> listaDataVencimento) {
        this.listaDataVencimento = listaDataVencimento;
    }

    public int getIdDiaVencimento() {
        PessoaDB pessoaDB = new PessoaDBToplink();
        if (pessoa.getId() != -1) {
            PessoaComplemento pc = pessoaDB.pesquisaPessoaComplementoPorPessoa(pessoa.getId());
            if (pc.getId() == -1) {
                if (getRegistro() != null) {
                    this.idDiaVencimento = registro.getFinDiaVencimentoCobranca();
                } else {
                    this.idDiaVencimento = Integer.parseInt(DataHoje.data().substring(0, 2));
                }
            } else {
                this.idDiaVencimento = pc.getNrDiaVencimento();
            }
        } else {
            this.idDiaVencimento = Integer.parseInt(DataHoje.data().substring(0, 2));
        }
        return idDiaVencimento;
    }

    public void setIdDiaVencimento(int idDiaVencimento) {
        this.idDiaVencimento = idDiaVencimento;
    }

    public void updatePessoaComplemento() {
        if (pessoa.getId() != -1) {
            PessoaDB pessoaDB = new PessoaDBToplink();
            PessoaComplemento pc = pessoaDB.pesquisaPessoaComplementoPorPessoa(pessoa.getId());
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            pc.setNrDiaVencimento(idDiaVencimento);
            sadb.abrirTransacao();
            if (sadb.alterarObjeto(pc)) {
                sadb.comitarTransacao();
            } else {
                sadb.desfazerTransacao();
            }
        }
    }

    public Registro getRegistro() {
        if (registro.getId() == -1) {
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            registro = (Registro) sadb.find("Registro", 1);
        }
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

}
