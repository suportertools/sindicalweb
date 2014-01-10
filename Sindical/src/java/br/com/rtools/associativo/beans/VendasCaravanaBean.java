package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.CVenda;
import br.com.rtools.associativo.Caravana;
import br.com.rtools.associativo.EventoServico;
import br.com.rtools.associativo.EventoServicoValor;
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
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.beans.FisicaBean;
import br.com.rtools.pessoa.beans.JuridicaBean;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class VendasCaravanaBean {
    private Fisica fisica = new Fisica();
    private List<DataObject> listaReserva = new ArrayList<DataObject>();
    private Caravana caravana = new Caravana();
    private int idCaravana = 0;
    private final List<SelectItem> listaCaravanaSelect = new ArrayList<SelectItem>();
    private final List<Caravana> listaCaravana = new ArrayList<Caravana>();
    private List<SelectItem> listaTipo = new Vector<SelectItem>();
    private int idTipo = 0;
    private int idAdicionar = -1;
    private CVenda vendas = new CVenda();
    private int parcelas = 1;
    private List<DataObject> listaParcelas = new ArrayList();
    private String dataEntrada = DataHoje.data();
    private final List<SelectItem> listaPoltrona = new ArrayList<SelectItem>();
    private EventoServicoValor eventoServicoValor = new EventoServicoValor();
    private EventoServico eventoServico = new EventoServico();
    private Pessoa pessoa = new Pessoa();
    private String valorTotal = "0,00";
    private String valorPago = "0,00";
    private String valorOutras = "0,00";
    private String valorEntrada = "0,00";
    
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private Fisica pessoaFisica = new Fisica();
    private Juridica pessoaJuridica = new Juridica();
    
    public void excluir(){
        if (vendas.getId() == -1){
            GenericaMensagem.warn("Erro", "Pesquise uma venda para ser cancelada!");
            return ;
        }
        
        List<Reservas> lr = new ArrayList();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        Reservas res = new Reservas();
        sv.abrirTransacao();
        
        
        lr = db.listaReservasVenda(vendas.getId());
        for (int i = 0; i < lr.size();i++){
            res = (Reservas)sv.pesquisaCodigo(lr.get(i).getId(), "Reservas");
            if (!sv.deletarObjeto(res)){
                GenericaMensagem.warn("Erro", "Erro ao cancelar reservas!");
                sv.desfazerTransacao();
                return ;
            }
        }
        
        List<Movimento> listaMovimento = db.listaMovCaravana(vendas.getResponsavel().getId(), vendas.getEvt().getId());
        if (!listaMovimento.isEmpty()){
            for (int i = 0; i < listaMovimento.size(); i++){
                if (listaMovimento.get(i).getBaixa() != null){
                    GenericaMensagem.warn("Erro", "Reserva com parcela paga não pode ser excluída!");
                    sv.desfazerTransacao();
                    return ;
                }
            }
            Movimento mov = new Movimento();
            Lote lot = new Lote();
            Evt evt = new Evt();
            for (int i = 0; i < listaMovimento.size(); i++){
                mov = (Movimento)sv.pesquisaCodigo(listaMovimento.get(i).getId(), "Movimento");
                lot = (Lote)sv.pesquisaCodigo(mov.getLote().getId(), "Lote");
                evt = (Evt)sv.pesquisaCodigo(mov.getLote().getEvt().getId(), "Evt");
                                
                if (!sv.deletarObjeto(mov)){
                    GenericaMensagem.warn("Erro", "Erro ao excluir movimentos!");
                    sv.desfazerTransacao();
                    return ;
                }
                
                if (!sv.deletarObjeto(lot)){
                    GenericaMensagem.warn("Erro", "Erro ao excluir Lote!");
                    sv.desfazerTransacao();
                    return ;
                }
                
                if (!sv.deletarObjeto(sv.pesquisaCodigo(vendas.getId(), "CVenda"))){
                    GenericaMensagem.warn("Erro", "Erro ao cancelar Venda!");
                    sv.desfazerTransacao();
                    return ;
                }
                
                if (!sv.deletarObjeto(evt)){
                    GenericaMensagem.warn("Erro", "Erro ao excluir EVT!");
                    sv.desfazerTransacao();
                    return ;
                }
            }
            
            GenericaMensagem.info("Sucesso", "Reserva cancelada com sucesso!");
            sv.comitarTransacao();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("vendasCaravanaBean");
        }
     }
    
    public String editar(CVenda v){
        vendas = v;
        List<Reservas> lr = new ArrayList();
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        FisicaDB dbf = new FisicaDBToplink();
        EventoServicoValorDB dbe = new EventoServicoValorDBToplink();
        SociosDB dbs = new SociosDBToplink();
        float valor = 0;
        
        
        lr = db.listaReservasVenda(vendas.getId());
        listaReserva.clear();
        for (int i = 0; i < lr.size(); i++){
            valor = dbs.descontoSocioEve(lr.get(i).getPessoa().getId() , lr.get(i).getEventoServico().getServicos().getId() );
            if (valor == 0){
                valor = dbe.pesquisaEventoServicoValor(lr.get(i).getEventoServico().getId()).getValor();
                listaReserva.add(new DataObject(dbf.pesquisaFisicaPorPessoa(lr.get(i).getPessoa().getId()), 0, Moeda.converteR$Float(valor), Moeda.converteR$Float(lr.get(i).getDesconto()), lr.get(i), null));
            }else
                listaReserva.add(new DataObject(dbf.pesquisaFisicaPorPessoa(lr.get(i).getPessoa().getId()), 0, Moeda.converteR$Float(valor), Moeda.converteR$Float(lr.get(i).getDesconto()), lr.get(i), null));
        }
        
        List<Movimento> listaMovimento = db.listaMovCaravana(vendas.getResponsavel().getId(), vendas.getEvt().getId());
        
        listaParcelas.clear();
        for (int i = 0; i < listaMovimento.size(); i++){
            if (listaMovimento.get(i).getBaixa() == null)
                listaParcelas.add(new DataObject(listaMovimento.get(i).getVencimento(), Moeda.converteR$Float(listaMovimento.get(i).getValor()), false, null, null, null));
            else
                listaParcelas.add(new DataObject(listaMovimento.get(i).getVencimento(), Moeda.converteR$Float(listaMovimento.get(i).getValor()), true, null, null, null));
        }
        
        pessoa = vendas.getResponsavel();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return "vendasCaravana";
    }
    
    public void atualizaValoresParcela(int index){
        listaParcelas.get(index).setArgumento1(Moeda.converteR$(listaParcelas.get(index).getArgumento1().toString()));
    }
    
    public void atualizaValoresGrid(int index){
        listaReserva.get(index).setArgumento2(Moeda.converteR$(listaReserva.get(index).getArgumento2().toString()));
        if (Moeda.converteUS$(listaReserva.get(index).getArgumento3().toString()) > Moeda.converteUS$(listaReserva.get(index).getArgumento2().toString()))
            listaReserva.get(index).setArgumento3(Moeda.converteR$(listaReserva.get(index).getArgumento2().toString()));
        else
            listaReserva.get(index).setArgumento3(Moeda.converteR$(listaReserva.get(index).getArgumento3().toString()));
    }
    
    public String cadastroFisica(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("fisicaBean", new FisicaBean());
        ((FisicaBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaBean")).editarFisicaParametro(pessoaFisica);
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return ((ChamadaPaginaBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pessoaFisicaComParametros();
    }
    
    public String cadastroJuridica(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("juridicaBean", new JuridicaBean());
        ((JuridicaBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaBean")).editar(pessoaJuridica);
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return ((ChamadaPaginaBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pessoaJuridicaComParametros();
    }
    
    public void salvar(){
        if (vendas.getId() != -1){
            GenericaMensagem.info("Sucesso", "Venda concluída!");
            return ;
        }
        
        if (pessoa.getId() == -1){
            GenericaMensagem.warn("Erro", "Pesquise um responsável!");
            return ;
        }
        
        if (listaReserva.isEmpty()){
            GenericaMensagem.warn("Erro", "Não é possivel concluir nenhuma Reserva!");
            return ;
        }
        
        if (listaParcelas.isEmpty()){
            GenericaMensagem.warn("Erro", "Não é possivel concluir sem parcelas!");
            return ;
        }
        float soma = 0;
        
        for (int i = 0; i < listaParcelas.size(); i++){
            soma = Moeda.somaValores( soma, Moeda.converteUS$(String.valueOf(listaParcelas.get(i).getArgumento1())) );
        }
        
        if (soma < Moeda.converteUS$(valorTotal)){
            GenericaMensagem.warn("Erro", "Valor das parcelas é MENOR que o valor total");
            return ;
        }
        
        if (soma > Moeda.converteUS$(valorTotal)){
            GenericaMensagem.warn("Erro", "Valor das parcelas é MAIOR que o valor total");
            return ;
        }
            
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        Evt evt = new Evt();
        if (!sv.inserirObjeto(evt)){
            GenericaMensagem.warn("Erro", "Não foi possível salvar EVT!");
            sv.desfazerTransacao();
            return ;
        }
        
        vendas.setEvt(evt);
        
        if (!sv.inserirObjeto(vendas)){
            GenericaMensagem.warn("Erro", "Não é possivel salvar venda!");
            return ;
        }
        
        for (int i = 0; i < listaReserva.size(); i++){
            Reservas res = new Reservas(-1, vendas, ((Fisica)listaReserva.get(i).getArgumento0()).getPessoa(), 
                                        //Integer.valueOf(listaPoltrona.get(Integer.valueOf(listaReserva.get(i).getArgumento1().toString())).getDescription()), 
                                        Integer.valueOf(listaReserva.get(i).getArgumento1().toString()), 
                                        Moeda.converteUS$(listaReserva.get(i).getArgumento3().toString()), ((Reservas) listaReserva.get(i).getArgumento4()).getEventoServico());
            if (!sv.inserirObjeto(res)){
                GenericaMensagem.warn("Erro", "Não é possivel salvar venda!");
                return ;
            }
            
            listaReserva.get(i).setArgumento4(res);
        }
        
        Lote lote = new Lote(-1, (Rotina)sv.pesquisaCodigo(142, "Rotina"), "", DataHoje.data(), pessoa, null, false, "", Moeda.converteUS$(valorTotal), (Filial)sv.pesquisaCodigo(1, "Filial"), 
                            (Departamento)sv.pesquisaCodigo(6, "Departamento"), evt, "", null, null, null, null, false, null);
        if (!sv.inserirObjeto(lote)){
            GenericaMensagem.warn("Erro", "Não foi possível salvar Lote!");
            sv.desfazerTransacao();
            return ;
        }
        
        Movimento movimento = null;
        for (int i = 0; i < listaParcelas.size(); i++){
            movimento = new Movimento(-1, lote, eventoServico.getaEvento().getDescricaoEvento().getServicoMovimento().getPlano5(), pessoa, eventoServico.getaEvento().getDescricaoEvento().getServicoMovimento(), 
                                      null, (TipoServico)sv.pesquisaCodigo(1, "TipoServico"), null, Moeda.converteUS$(String.valueOf(listaParcelas.get(i).getArgumento1())), DataHoje.dataReferencia(String.valueOf(listaParcelas.get(i).getArgumento0())), String.valueOf(listaParcelas.get(i).getArgumento0()), 
                                      parcelas, true, "", false, pessoa, pessoa, "", "", "", 0, 0, 0, 0, 0, 0, 0, null, 0);
            
            if (!sv.inserirObjeto(movimento)){
                GenericaMensagem.warn("Erro", "Não é possivel salvar movimento!");
                return ;
            }
        }
        
        GenericaMensagem.info("Sucesso", "Reserva concluída com Sucesso!");
        sv.comitarTransacao();
    }
    
    public void gerarParcelas(){
        if (parcelas < 0)
            return;
        
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
        if (parcelas == 1){
            listaParcelas.add(new DataObject(vencs, Moeda.converteR$(valorTotal), false, null, null, null));
        }else{
            listaParcelas.add(new DataObject(dataEntrada, Moeda.converteR$(valorEntrada), false, null, null, null));
            vlEnt = Moeda.converteR$Float(
                         Moeda.divisaoValores(
                                Moeda.subtracaoValores(
                                         Moeda.substituiVirgulaFloat(valorTotal), Moeda.substituiVirgulaFloat(vlEnt)
                                         //Moeda.substituiVirgulaFloat(valorAPagar), Moeda.substituiVirgulaFloat(vlEnt)
                                ),
                         parcelas - 1
                   ));
            for (int i = 1; i < parcelas; i++){
                vencs = dh.incrementarMeses(1, vencs);
                listaParcelas.add(new DataObject(vencs, Moeda.converteR$(vlEnt), false, null, null, null));
            }
        }
    }
    
    public void adicionarReserva(){
        if (pessoa.getId() == -1){
            GenericaMensagem.warn("Erro", "Pesquise um responsável!");
            return;
        }
        
        if (pessoaEndereco.getId() == -1){
            GenericaMensagem.warn("Erro", "Cadastre um endereço para este responsável!");
            return;
        }

        if (pessoaFisica.getId() != -1 ){
            // VERIFICA SE PESSOA É MAIOR DE IDADE
            DataHoje dh = new DataHoje();
            int idade = dh.calcularIdade(pessoaFisica.getNascimento());
            if (idade < 18){
                GenericaMensagem.warn("Erro", "Esta pessoa não é maior de idade, não poderá ser responsável!");
                return;
            }
        }
        
        if (listaCaravana.get(idCaravana).getId() == -1){
            GenericaMensagem.warn("Erro", "Erro confirmar caravana!");
            return;
        }
        
        if (getListaPoltrona().isEmpty()){
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
    
    public String pesquisaPassageiro(int index){
        idAdicionar = index;
        return ((ChamadaPaginaBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pesquisaPessoaFisica();
    }
    
    public void removerPessoa() {
        pessoa = new Pessoa();
    }
    
    public void removerReserva(int index, DataObject datao) {
        listaReserva.remove(index);
    }

    public void atualizaCaravana(){
        caravana = listaCaravana.get(idCaravana);
        listaTipo.clear();
    }

    public void atualizaTipo(){
        listaTipo.clear();
    }
    
    public List getListaPesquisaVendas(){
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        return db.pesquisaTodos();
    }
    
    public List<SelectItem> getListaCaravanaSelect(){
        if (listaCaravanaSelect.isEmpty()){
            CaravanaDB db = new CaravanaDBToplink();
            List<Caravana> result = db.pesquisaTodos();
            for (int i = 0; i < result.size(); i++){
                listaCaravanaSelect.add(new SelectItem(i, result.get(i).getDataSaida()+" - "+result.get(i).getHoraSaida()+" - "+ result.get(i).getaEvento().getDescricaoEvento().getDescricao(), String.valueOf(result.get(i).getId()) ));
                listaCaravana.add(result.get(i));
            }
            caravana = listaCaravana.get(idCaravana);
        }
        return listaCaravanaSelect;
    }
    
    public Fisica getFisica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null && idAdicionar == -1) {
            fisica = (Fisica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }
    
    public List<SelectItem> getListaPoltrona() {
        List<Integer> select = new ArrayList();
        VendasCaravanaDB db = new VendasCaravanaDBToplink();
        if (!listaCaravana.isEmpty() && listaPoltrona.isEmpty()){
            SalvarAcumuladoDB  sv = new SalvarAcumuladoDBToplink();
            //Caravana caravanax = (Caravana)sv.pesquisaCodigo(caravana.getId(), "Caravana");
            select = db.listaPoltronasUsadas(listaCaravana.get(idCaravana).getaEvento().getId() );
            
            boolean adc = true;
            String pol = "";
            for(int i = 1; i <= listaCaravana.get(idCaravana).getQuantidadePoltronas(); i++){
                for (int w = 0; w < select.size(); w++){
                    if (i == select.get(w)){
                        adc = false;
                        break;
                    }
                }
                if (adc){
                    pol = "000"+i;
                    listaPoltrona.add(new SelectItem(new Integer(i), pol.substring(pol.length() - 2, pol.length()), Integer.toString(i)));
                    //listaPoltrona.add(pol.substring(pol.length() - 2, pol.length()));
                }
                adc = true;
            }
        }
        return listaPoltrona;
    }

    public List<SelectItem> getListaTipo() {
        if (!listaCaravana.isEmpty()){
            
            if (listaTipo.isEmpty() && listaCaravana.get(idCaravana).getId() != -1){
                List<EventoServico> select = new ArrayList();
                EventoServicoDB db = new EventoServicoDBToplink();
                EventoServicoValorDB dbE = new EventoServicoValorDBToplink();
                EventoServicoDB dbEs = new EventoServicoDBToplink();
                if(listaCaravana.get(idCaravana).getId() != -1){
                    select = db.listaEventoServico(listaCaravana.get(idCaravana).getaEvento().getId());
                    for(int i = 0; i < select.size(); i++){
                        listaTipo.add(new SelectItem(new Integer(i),
                                                  select.get(i).getDescricao(),
                                                  Integer.toString(select.get(i).getId())));
                    }
                    if (idTipo >= select.size())
                        idTipo = 0;

                    eventoServico = dbEs.pesquisaCodigo(select.get(idTipo).getId());
                    eventoServicoValor = dbE.pesquisaEventoServicoValor(eventoServico.getId());
                }
            }
        }
        return listaTipo;
    }
    
    public String novo(){
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
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null){
            pessoa = (Pessoa)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
            
            FisicaDB dbf = new FisicaDBToplink();
            JuridicaDB dbj = new JuridicaDBToplink();
            
            pessoaFisica = dbf.pesquisaFisicaPorPessoa(pessoa.getId());
            if (pessoaFisica == null){
                pessoaJuridica = dbj.pesquisaJuridicaPorPessoa(pessoa.getId());
                pessoaFisica = new Fisica();
                if (pessoaJuridica == null)
                    pessoaJuridica = new Juridica();
            }else{
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
            float valor = 0;
            
            //valor = db.descontoSocioEve(fis.getPessoa().getId() , eventoServico.getServicos().getId() );
            valor = db.descontoSocioEve(fis.getPessoa().getId() , ((Reservas)listaReserva.get(idAdicionar).getArgumento4()).getEventoServico().getId());
            if (valor == 0){
                listaReserva.get(idAdicionar).setArgumento2(Moeda.converteR$Float(((EventoServicoValor)listaReserva.get(idAdicionar).getArgumento5()).getValor()));
                //listaReserva.get(idAdicionar).setArgumento3(Moeda.converteR$Float( Moeda.subtracaoValores(eventoServicoValor.getValor(), 0)));// NA VERDADE SUBTRAI PELO DESCONTO
            }else{
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
        if (!listaReserva.isEmpty()){
            float valor = 0;
            float desconto = 0;
            for (int i = 0; i < listaReserva.size(); i++){
                if ( ((Fisica)listaReserva.get(i).getArgumento0()).getId() != -1){
                    valor = Moeda.somaValores(valor, Moeda.substituiVirgulaFloat(String.valueOf(listaReserva.get(i).getArgumento2())));
                    desconto = Moeda.somaValores(desconto, Moeda.substituiVirgulaFloat(String.valueOf(listaReserva.get(i).getArgumento3())));
                }
            }
            valorTotal = Moeda.converteR$Float(Moeda.subtracaoValores(valor, desconto));
        }else
            valorTotal = "0,00";
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getValorPago() {
        if (!listaParcelas.isEmpty()){
            float valor = 0;
            for (int i = 0; i < listaParcelas.size(); i++){
                if ( ((Boolean)listaParcelas.get(i).getArgumento2()) == true ){
                    valor = Moeda.somaValores(valor, Moeda.substituiVirgulaFloat(String.valueOf(listaParcelas.get(i).getArgumento1())));
                }
            }
            valorPago = Moeda.converteR$Float(valor);
        }else
            valorPago = "0,00";
        return valorPago;
    }

    public void setValorPago(String valorPago) {
        this.valorPago = valorPago;
    }

    public String getValorOutras() {
        if (!listaParcelas.isEmpty()){
            float valor = 0;
            for (int i = 0; i < listaParcelas.size(); i++){
                if ( ((Boolean)listaParcelas.get(i).getArgumento2()) != true ){
                    valor = Moeda.somaValores(valor, Moeda.substituiVirgulaFloat(String.valueOf(listaParcelas.get(i).getArgumento1())));
                }
            }
            valorOutras = Moeda.converteR$Float(valor);
        }else
            valorOutras = "0,00";
        return valorOutras;
    }

    public void setValorOutras(String valorOutras) {
        this.valorOutras = valorOutras;
    }

    public String getValorEntrada() {
        if (valorEntrada.isEmpty()) {
            valorEntrada = "0";
        }
        
        if (Moeda.converteUS$(valorEntrada) > Moeda.converteUS$(valorTotal))
            valorEntrada = valorTotal;
        if (Moeda.converteUS$(valorEntrada) < 0)
            valorEntrada = "0";
        
        return Moeda.converteR$(valorEntrada);
    }

    public void setValorEntrada(String valorEntrada) {
        if (valorEntrada.isEmpty()) {
            valorEntrada = "0";
        }
        this.valorEntrada = Moeda.substituiVirgula(valorEntrada);
    }

    public PessoaEndereco getPessoaEndereco() {
        if (pessoaEndereco.getId() == -1){
            PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
            pessoaEndereco = dbp.pesquisaEndPorPessoaTipo(pessoa.getId(), 4);
            if (pessoaEndereco == null)
                pessoaEndereco = new PessoaEndereco();
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

}
