//
//package br.com.rtools.financeiro.beans;
//
//import br.com.rtools.arrecadacao.DescontoEmpregado;
//import br.com.rtools.arrecadacao.FolhaEmpresa;
//import br.com.rtools.arrecadacao.db.DescontoEmpregadoDB;
//import br.com.rtools.arrecadacao.db.DescontoEmpregadoDBToplink;
//import br.com.rtools.arrecadacao.db.FolhaEmpresaDB;
//import br.com.rtools.arrecadacao.db.FolhaEmpresaDBToplink;
//import br.com.rtools.classeOperacao.OperacaoMovimento;
//import br.com.rtools.financeiro.ComplementoValor;
//import br.com.rtools.financeiro.Movimento;
//import br.com.rtools.financeiro.Servicos;
//import br.com.rtools.financeiro.db.FinanceiroDB;
//import br.com.rtools.financeiro.db.FinanceiroDBToplink;
//import br.com.rtools.financeiro.db.MovimentoDB;
//import br.com.rtools.financeiro.db.MovimentoDBToplink;
//import br.com.rtools.financeiro.db.ServicosDB;
//import br.com.rtools.financeiro.db.ServicosDBToplink;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.pessoa.db.JuridicaDB;
//import br.com.rtools.pessoa.db.JuridicaDBToplink;
//import br.com.rtools.seguranca.controleUsuario.chamadaPaginaJSFBean;
//import br.com.rtools.utilitarios.DataHoje;
//import br.com.rtools.utilitarios.Linha;
//import br.com.rtools.utilitarios.Moeda;
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Vector;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//import org.richfaces.component.html.HtmlDataTable;
//
//public class AntigoMovimentosReceberJSFBean1 {
//
//    private HtmlDataTable htmlTable;
//    private String total = "0";
//    private String totalReceber = "0";
//    private String juros = "0";
//    private String correcao = "0";
//    private String multa = "0";
//    private String desconto = "0";
//    private String taxa = "0";
//    private String acrescimo = "0";
//    private String acrescimoSindical = "0";
//    private Pessoa pessoa = new Pessoa();
//    private boolean novaPessoa = false;
//    private Movimento movimento = new Movimento();
//    private OperacaoMovimento opMovimentos = null;
//    private List<Linha> listaGrid =  new Vector<Linha>();
//    private DescontoEmpregado descontoEmpregado = new DescontoEmpregado();
//    private Linha linhaClick;
//    private FolhaEmpresa folhaEmpresa = new FolhaEmpresa();
//    private String valor = "0";
//    private int qtdFuncionario = 0;
//    private boolean mostrarPainel;
//    private boolean desabilitaValor = false;
//    private String labelFolha = "";
//    private String labelFolha2 = "";
//    private String labelLink = "";
//    private boolean marcarTodos = false;
//    private List<Integer> listaMarcados = new ArrayList<Integer>();
//    private List<Object[]> listaObjetos;
//
//
//    public void refreshForm(){}
//
//    public List<Linha> getListaGrid() {
//        if ((listaGrid.isEmpty()) && (pessoa.getId() != -1)){
//            carregarGrid();
//        }
//        return listaGrid;
//    }
//
//    public synchronized  String somaValores(){
//        int i = 0;
//        total = "0";
//        totalReceber = "0";
//        multa = "0";
//        juros = "0";
//        correcao = "0";
//        while(i < listaGrid.size()){
//            if ((Boolean) listaGrid.get(i).getValor()){
//                total = Moeda.converteR$Float(Moeda.somaValores(
//                            Float.parseFloat(Moeda.substituiVirgula(total)),
//                            ( (Movimento )listaGrid.get(i).getColuna().getValor()).getValor()
//                        )
//                );
//
//                totalReceber = Moeda.converteR$Float(Moeda.somaValores(
//                            Float.parseFloat(Moeda.substituiVirgula(totalReceber)),
//                            Float.parseFloat(Moeda.substituiVirgula( (String) listaGrid.get(i).getColuna().getColuna().getColuna().getValor()))
//                        )
//                );
//
//                multa = Moeda.converteR$Float(Moeda.somaValores(
//                            Float.parseFloat(Moeda.substituiVirgula(multa)),
//                            Float.parseFloat(Moeda.substituiVirgula( (String) listaGrid.get(i).getColuna().getColuna().getColuna().getColuna().getValor()))
//                        )
//                );
//
//                juros = Moeda.converteR$Float(Moeda.somaValores(
//                            Float.parseFloat(Moeda.substituiVirgula(juros)),
//                            Float.parseFloat(Moeda.substituiVirgula( (String) listaGrid.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getValor()))
//                        )
//                );
//
//                correcao = Moeda.converteR$Float(Moeda.somaValores(
//                            Float.parseFloat(Moeda.substituiVirgula(correcao)),
//                            Float.parseFloat(Moeda.substituiVirgula( (String) listaGrid.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor()))
//                        )
//                );
//
//            }
//            i++;
//        }
//        return null;
//    }
//
//    public synchronized String marcar(){
//        Linha linha = (Linha) this.htmlTable.getRowData();
//        int indice = this.htmlTable.getRowIndex();
//        if ((Boolean) linha.getValor()){
//            for (int i = 0; i < listaMarcados.size(); i++){
//                if (listaMarcados.get(i) == indice){
//                    desconto = "0";
//                    somaValores();
//                    return null;
//                }
//            }
//            listaMarcados.add(indice);
//        }else{
//            listaMarcados.remove(listaMarcados.indexOf(indice));
//        }
//        desconto = "0";
//        somaValores();//        carregarGrid();
//        return null;
//    }
//
//    public String selecionarTodos(){
//        listaMarcados.clear();
//        int i = 0;
//        if(marcarTodos){
//            while (i < listaGrid.size()){
//                listaGrid.get(i).setValor(new Boolean(true));
//                listaMarcados.add(i);
//                i++;
//            }
//        }else{
//            while (i < listaGrid.size()){
//                listaGrid.get(i).setValor(new Boolean(false));
//                i++;
//            }
//        }
//        desconto = "0";
//        carregarGrid();
//        return null;
//    }
//
//    public synchronized void carregarGrid(){
//        MovimentoDB movDB = new MovimentoDBToplink();
//        DescontoEmpregadoDB desDB = new DescontoEmpregadoDBToplink();
//
//        listaObjetos = movDB.movimentosAbertoComVencimentoOriginal( getPessoa().getId());
//        ArrayList<Movimento> listaMovimento = new ArrayList<Movimento>();
//
//        //Object o = listaObjetos.get(0);
//        for(Object[] objeto : listaObjetos){
//            listaMovimento.add( (Movimento) objeto[0]);
//        }
//
//        if ((opMovimentos == null) || (novaPessoa)){
//            opMovimentos = new OperacaoMovimento( listaMovimento ,Float.parseFloat(desconto));
//            novaPessoa = false;
//        }else{
//            opMovimentos.setDesconto(Moeda.substituiVirgulaFloat(desconto));
//        }
//
//        int i = 0;
//        if (opMovimentos.getLista() == null)
//             opMovimentos.setLista(new ArrayList<Movimento>());
//        List lista = new ArrayList();
//        Linha linha = new Linha();
//        int j = 0;
//        int data1 = 0;
//        int data2 = 0;
//        boolean achou = false;
//        List<Float> listaDesconto = null;
//        List<Boolean> listaMarcar = new ArrayList<Boolean>();
//        float descontoI = 0;
//        Date dataCalculo = null;
//        Object[] valorFloat = null;
//        listaGrid.clear();
//        while(i < opMovimentos.getLista().size()){
//            valorFloat = movDB.pesquisaValorFolha(opMovimentos.getLista().get(i));
//
//            if (valorFloat == null){
//                valorFloat = new Float[] {new Float(0),new Float(0)};
//            }else if (( (Float) valorFloat[0]) != 0){
//                if ((opMovimentos.getLista().get(i).getTipoServico().getId() != 4) &&
//                    (
//                         (DataHoje.converteDataParaInteger(opMovimentos.getLista().get(i).getVencimento()) <
//                          DataHoje.converteDataParaInteger(DataHoje.data())
//                         ) ||
//                         (opMovimentos.getLista().get(i).getValor() == 0)
//                     )
//                     ){
//
//                    descontoEmpregado = desDB.pesquisaEntreReferencias(
//                        opMovimentos.getLista().get(i).getReferencia(),
//                        opMovimentos.getLista().get(i).getServicos().getId(),
//                        opMovimentos.getLista().get(i).getPessoa().getId()
//                    );
//
//                    folhaEmpresa = this.pesquisaFolhaEmpresa(
//                        opMovimentos.getLista().get(i).getPessoa().getId(),
//                        opMovimentos.getLista().get(i).getTipoServico().getId(),
//                        opMovimentos.getLista().get(i).getReferencia()
//                    );
//
//                    opMovimentos.getLista().get(i).setValor(
//                            Moeda.somaValores(
//                            Moeda.multiplicarValores( (Float) valorFloat[0], ( (Float) valorFloat[1]) / 100),
//                            Moeda.multiplicarValores( folhaEmpresa.getNumFuncionarios(), descontoEmpregado.getValorEmpregado())
//                            ));
//                }
//              //  salvar(opMovimentos.getLista().get(i), opMovimentos.getLista().get(i).getId());
//            }
//
//            valorFloat = null;
//            if (listaGrid.isEmpty()){
//                listaMarcar.add(new Boolean(false));
//            }else{
//                j = 0;
//                achou = false;
//                while (j < listaMarcados.size()){
//                    if (listaMarcados.get(j) == i){
//                        achou = true;
//                        break;
//                    }
//                    j++;
//                }
//                if(achou){
//                    listaMarcar.add(new Boolean(true));
//                }else{
//                    listaMarcar.add(new Boolean(false));
//                }
//            }
//            i++;
//        }
//
//        listaDesconto = opMovimentos.getListaDesconto(listaMarcados);//CHAMA CALCULO Desconto.
//        i = 0;
//        boolean m;
//        ComplementoValor complemento = null;
//        Long calcMes = new Long(0);
////        Movimento movimentoClicado = null;
////        if(linhaClick != null){
////            movimentoClicado = (Movimento) this.linhaClick.getColuna().getValor();
////        }
//        while (i < opMovimentos.getLista().size()){
//            try{
////                if(movimentoClicado != null){
////                    if(!movimentoClicado.getReferencia().equals(opMovimentos.getLista().get(i).getReferencia())){
////                        break;
////                    }
////                }
//
//                if(i == 6){
//                    i = i++;
//                    i = i--;
//                }
//
//                lista = new ArrayList();
//                linha = new Linha();
//                m = (Boolean) listaMarcar.get(i);
//                lista.add(m);
//                lista.add(opMovimentos.getLista().get(i));
//                lista.add(Moeda.converteR$Float( opMovimentos.getLista().get(i).getValor()));
//                if ((Float.parseFloat(desconto) != 0) && (!listaDesconto.isEmpty())){
//                   descontoI = listaDesconto.get(i);
//                }else{
//                   descontoI = 0;
//                }
//
//                data1 = DataHoje.converteDataParaInteger(opMovimentos.getLista().get(i).getVencimento());
//                data2 = DataHoje.converteDataParaInteger(DataHoje.data());
//                if ((opMovimentos.getLista().get(i).getTipoServico().getId() == 4)){
//                    //(opMovimentos.getLista().get(i).getServicos().getId() != 1)){ /// Quando Acordo não pegara venc da mensagem cobrança, pegara do mov.
//                    dataCalculo = opMovimentos.getLista().get(i).getDtVencimento();
//                }else{
//                    if (data1 < data2){
//                        dataCalculo = (Date) listaObjetos.get(i)[1];//DataHoje.converte(opMovimentos.geraVencimentoParaReferencia(opMovimentos.getLista().get(i).getReferencia(), i));
//                    }else{
//                        dataCalculo = opMovimentos.getLista().get(i).getDtVencimento();
//                    }
//                }
//
//                complemento = opMovimentos.getComplemento(i, dataCalculo);
//
//                lista.add(Moeda.converteR$Float(Moeda.subtracaoValores(
//                                      Moeda.somaValores(Moeda.somaValores(Moeda.somaValores(complemento.getMulta(),complemento.getJuros()) , complemento.getCorrecao() ),
//                                        opMovimentos.getLista().get(i).getValor()) , descontoI)));
//
//                lista.add(Moeda.converteR$Float(complemento.getMulta()));
//                lista.add(Moeda.converteR$Float(complemento.getJuros()));
//                lista.add(Moeda.converteR$Float(complemento.getCorrecao()));
//                lista.add(Moeda.converteR$Float(descontoI));
//                calcMes = DataHoje.calculoDosDias(dataCalculo, DataHoje.dataHoje());
//                if ( calcMes > 0)
//                    lista.add(calcMes);
//                else
//                    lista.add(new Integer(0));
//
//                //SE FOR ACORDO NAO DIGITAR FOLHA
//                if (opMovimentos.getLista().get(i).getTipoServico().getId() == 4)
//                    lista.add(true);
//                else
//                    lista.add(false);
//
//                lista.add(complemento);
//
//                if(opMovimentos.getLista().get(i).getBeneficiario() != null){
//                    lista.add(opMovimentos.getLista().get(i).getBeneficiario().getNome());
//                }else{
//                    lista.add("");
//                }
//
//                linha = Linha.preencherLinha(
//                        linha,
//                        lista,
//                        0);
//
//                listaGrid.add(linha);
//                calcMes = new Long(0);
//                complemento = null;
//            }catch (Exception e){
//                System.out.println(e.getMessage());
//            }
//            i++;
//        }
//        linhaClick = null;
//        somaValores();
//    }
//
//    public void carregarFolha(){
//        folhaEmpresa = new FolhaEmpresa();
//        try{
//            linhaClick = (Linha) this.htmlTable.getRowData();
//        }catch (Exception e){
//            String erro = e.getMessage();
//            return;
//        }
//        DescontoEmpregadoDB desDB = new DescontoEmpregadoDBToplink();
//        descontoEmpregado = desDB.pesquisaEntreReferencias(
//            ( (Movimento )linhaClick.getColuna().getValor()).getReferencia(),
//            ( (Movimento )linhaClick.getColuna().getValor()).getServicos().getId(),
//            ( (Movimento )linhaClick.getColuna().getValor()).getPessoa().getId()
//        );
//
//        if (descontoEmpregado == null){
//            descontoEmpregado = new DescontoEmpregado();
//            mostrarPainel = true;
//            labelLink = "Informe o desconto empregado para referência - Click aqui " + ((Movimento) linhaClick.getColuna().getValor()).getReferencia();
//            labelFolha = "";
//            labelFolha2 = "";
//        }else{
//            labelLink = "";
//            labelFolha2 = "Número de Funcionários: ";
//            labelFolha = "Atualizar valor da folha do mês para referência " + ((Movimento) linhaClick.getColuna().getValor()).getReferencia();
//            mostrarPainel = false;
//        }
//
//        folhaEmpresa = this.pesquisaFolhaEmpresa(
//            ( (Movimento )linhaClick.getColuna().getValor()).getPessoa().getId(),
//            ( (Movimento )linhaClick.getColuna().getValor()).getTipoServico().getId(),
//            ( (Movimento )linhaClick.getColuna().getValor()).getReferencia()
//        );
//
//        if (folhaEmpresa.getId() != -1){
//            String valorFolha = Float.toString(folhaEmpresa.getValorMes());
//            setValor(valorFolha);
//            setQtdFuncionario(folhaEmpresa.getNumFuncionarios());
//        }else{
//            setValor("0.0");
//            setQtdFuncionario(0);
//        }
//
//    }
//
//    public String getLabelFolha(){
//        return labelFolha;
//    }
//
//    public void setLabelFolha(String labelFolha){
//        this.labelFolha = labelFolha;
//    }
//
//    public String getValor() {
//        return Moeda.converteR$(valor);
//    }
//
//    public void setValor(String valor) {
//        this.valor = Moeda.substituiVirgula(valor);
//    }
//
//    public void atualizaValor(){
//        try{
//            float valorMes = Float.parseFloat(valor);
//            int i = -1;
//            int data1 = 0;
//            int data2 = 0;
//            Date dataCalculo = null;
//            DescontoEmpregadoDB desDB = new DescontoEmpregadoDBToplink();
//            if (valorMes != 0){
//                Movimento movimentoClicado = (Movimento) this.linhaClick.getColuna().getValor();
//                for(Movimento movimentoValor : this.opMovimentos.getLista()){
//                    i++;
//                    descontoEmpregado = desDB.pesquisaEntreReferencias(
//                        movimentoValor.getReferencia(),
//                        movimentoValor.getServicos().getId(),
//                        movimentoValor.getPessoa().getId()
//                    );
//
//                    if(linhaClick != null){
//                        if((!movimentoClicado.getReferencia().equals(movimentoValor.getReferencia())) ||
//                          (movimentoClicado.getTipoServico().getId() != movimentoValor.getTipoServico().getId())){
//                            continue;
//                        }
//                    }else{
//                        continue;
//                    }
//
//                    if(descontoEmpregado == null){
//                        continue;
//                    }
//
//                    if(listaObjetos.get(i)[1] == null){
//                        continue;
//                    }
//
//                    JuridicaDB jurDB = new JuridicaDBToplink();
//                    if (valorMes == 0){
//                        return;
//                    }
//                    if ((movimentoValor.getTipoServico().getId() != 4) &&
//                            (
//                                 (DataHoje.converteDataParaInteger(movimentoValor.getVencimento()) <
//                                  DataHoje.converteDataParaInteger(DataHoje.data())
//                                 ) ||
//                                 (movimentoValor.getValor() == 0)
//                             )
//                    ){
//                        movimentoValor.setValor(
//                                Moeda.converteFloatR$Float(
//                                    Moeda.somaValores(
//                                        Moeda.multiplicarValores(
//                                            valorMes,
//                                            (descontoEmpregado.getPercentual() / 100)
//                                        ),
//                                        Moeda.multiplicarValores(
//                                            folhaEmpresa.getNumFuncionarios(),
//                                            descontoEmpregado.getValorEmpregado()
//                                        )
//                                    )
//                                )
//                        );
//                    }
//
//                    data1 = DataHoje.converteDataParaInteger(opMovimentos.getLista().get(i).getVencimento());
//                    data2 = DataHoje.converteDataParaInteger(DataHoje.data());
//                    if ((opMovimentos.getLista().get(i).getTipoServico().getId() == 4)){
//
//                        dataCalculo = opMovimentos.getLista().get(i).getDtVencimento();
//                    }else{
//                        if (data1 < data2){
//                            dataCalculo = (Date) listaObjetos.get(i)[1];
//                        }else{
//                            dataCalculo = opMovimentos.getLista().get(i).getDtVencimento();
//                        }
//                    }
//
//                    this.opMovimentos.getLista().get(i).setValor(movimentoValor.getValor());
//                    this.opMovimentos.recalcularComplemento(i, dataCalculo);
//                    this.linhaClick.getColuna().getColuna().setValor(Moeda.converteR$Float(movimentoValor.getValor()));
//                    this.linhaClick.getColuna().getColuna().getColuna().setValor(Moeda.converteR$Float(this.opMovimentos.valorTotalParaMovimentoComDesconto(i)));
//                    this.linhaClick.getColuna().getColuna().getColuna().getColuna().setValor(Moeda.converteR$Float(this.opMovimentos.getComplemento(i).getMulta()));
//                    this.linhaClick.getColuna().getColuna().getColuna().getColuna().getColuna().setValor(Moeda.converteR$Float(this.opMovimentos.getComplemento(i).getJuros()));
//                    this.linhaClick.getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().setValor(Moeda.converteR$Float(this.opMovimentos.getComplemento(i).getCorrecao()));
//                    ( (Linha) listaGrid.get(i)).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().setValor(opMovimentos.getComplemento(i));
//                    salvar(movimentoValor, movimentoValor.getId());
//                    if (folhaEmpresa.getId() == -1){
//                        folhaEmpresa.setValorMes(valorMes);
//                        folhaEmpresa.setNumFuncionarios(qtdFuncionario);
//                        folhaEmpresa.setJuridica(jurDB.pesquisaJuridicaPorPessoa( movimentoValor.getPessoa().getId() ));
//                        folhaEmpresa.setReferencia(movimentoValor.getReferencia());
//                        folhaEmpresa.setTipoServico(movimentoValor.getTipoServico());
//                        salvar(folhaEmpresa);
//                    }else{
//                        folhaEmpresa.setValorMes(valorMes);
//                        folhaEmpresa.setNumFuncionarios(qtdFuncionario);
//                        salvar(folhaEmpresa);
//                    }
//                    folhaEmpresa = new FolhaEmpresa();
//                    descontoEmpregado = new DescontoEmpregado();
//
//                }
//            }
//        }catch (Exception e){
//            String a = e.getMessage();
//        }
//        somaValores();
//        //opMovimentos = null;
//        //carregarGrid();
//    }
//
//    private FolhaEmpresa pesquisaFolhaEmpresa(int idPessoa, int idTipoServico, String referencia){
//        FolhaEmpresa result = null;
//        FolhaEmpresaDB dbFolha = new FolhaEmpresaDBToplink();
//        result = dbFolha.pesquisaPorPessoa(idPessoa,idTipoServico,referencia);
//        if (result != null){
//            return result;
//        }else{
//            return new FolhaEmpresa();
//        }
//    }
//
//    public String salvar(Object object, int id){
//        FinanceiroDB db = new FinanceiroDBToplink();
//        if (id == -1){
//            db.insert(object);
//        }else{
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(object)){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//        return null;
//    }
//
//    public String salvar(Movimento object){
//        FinanceiroDB db = new FinanceiroDBToplink();
//        if (object.getId() == -1){
//            db.insert(object);
//        }else{
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(object)){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//        return null;
//    }
//
//    public void salvar(FolhaEmpresa object){
//        FolhaEmpresa folha = this.pesquisaFolhaEmpresa(
//                    object.getJuridica().getPessoa().getId(),
//                    object.getTipoServico().getId(),
//                    object.getReferencia()
//                );
//        FolhaEmpresaDB db = new FolhaEmpresaDBToplink();
//        if ((object.getId() == -1) && (folha == null)){
//            db.insert(object);
//        }else{
//            if(object.getId() == -1){
//                object.setId(folha.getId());
//            }
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(object)){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//    }
//
//    public Pessoa getPessoaPesquisa(){
//        Pessoa p = new Pessoa();
//        try{
//            p = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
//            if (p == null){
//                return new Pessoa();
//            }else{
//                novaPessoa = true;
//                listaGrid.clear();
//                desconto = "0";
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
//                return p;
//            }
//        }catch(Exception e){
//            return p;
//        }
//    }
//
//   public List<SelectItem> getListaServico(){
//       List<SelectItem> servicos = new Vector<SelectItem>();
//       int i = 0;
//       ServicosDB db = new ServicosDBToplink();
//       List select = db.pesquisaTodos(4);
//       while (i < select.size()){
//           servicos.add(new SelectItem(
//                  new Integer(i),
//                  (String) ((Servicos) select.get(i)).getDescricao() ,
//                  Integer.toString(((Servicos) select.get(i)).getId())  ));
//          i++;
//       }
//       return servicos;
//   }
//
//    public void setListaGrid(List<Linha> listaGrid) {
//        this.listaGrid = listaGrid;
//    }
//
//    public AntigoMovimentosReceberJSFBean1(){
//        htmlTable = new HtmlDataTable();
//    }
//
//    public HtmlDataTable getHtmlTable() {
//        return htmlTable;
//    }
//
//    public void setHtmlTable(HtmlDataTable htmlTable) {
//        this.htmlTable = htmlTable;
//    }
//
//    public String getTotal() {
//        return Moeda.converteR$(total);
//    }
//
//    public void setTotal(String total) {
//        this.total = Moeda.substituiVirgula(total);
//    }
//
//    public String getTotalReceber() {
//        return Moeda.converteR$(totalReceber);
//    }
//
//    public void setTotalReceber(String totalReceber) {
//        this.totalReceber = Moeda.substituiVirgula(totalReceber);
//    }
//
//    public String getJuros() {
//        return Moeda.converteR$(juros);
//    }
//
//    public void setJuros(String juros) {
//        this.juros = Moeda.substituiVirgula(juros);
//    }
//
//    public String getCorrecao() {
//        return Moeda.converteR$(correcao);
//    }
//
//    public void setCorrecao(String correcao) {
//        this.correcao = Moeda.substituiVirgula(correcao);
//    }
//
//    public String getMulta() {
//        return Moeda.converteR$(multa);
//    }
//
//    public void setMulta(String multa) {
//        this.multa = Moeda.substituiVirgula(multa);
//    }
//
//    public synchronized String getDesconto() {
//        if (desconto.isEmpty())
//            desconto = "0";
//        if ( Float.valueOf(Moeda.substituiVirgula(desconto)) > Float.valueOf(Moeda.substituiVirgula(acrescimoSindical))){
//            return Moeda.converteR$("0");
//        }else{
//            return Moeda.converteR$(desconto);
//        }
//    }
//
//    public synchronized void setDesconto(String desconto) {
//        if (desconto.isEmpty())
//            desconto = "0";
//        if ( Float.valueOf(Moeda.substituiVirgula(desconto)) > Float.valueOf(Moeda.substituiVirgula(acrescimoSindical))){
//            this.desconto = Moeda.substituiVirgula("0");
//        }else{
//           this.desconto = Moeda.substituiVirgula(desconto);
//        }
//    }
//
//    public synchronized String getAcrescimo() {
//        this.acrescimo =  String.valueOf( Moeda.somaValores(Moeda.somaValores(Float.parseFloat(Moeda.substituiVirgula(multa)), Float.parseFloat(Moeda.substituiVirgula(juros))),Float.parseFloat(Moeda.substituiVirgula(correcao))) );
//        return Moeda.converteR$(acrescimo);
//    }
//
//    public synchronized void setAcrescimo(String acrescimo) {
//        this.acrescimo = Moeda.substituiVirgula(acrescimo);
//    }
//
//    public synchronized String getAcrescimoSindical() {
//        float m = 0;
//        float j = 0;
//        float c = 0;
//        if(opMovimentos != null){
//            for(int i = 0; i < opMovimentos.getLista().size(); i++){
//                if (opMovimentos.getLista().get(i).getServicos().getId() != 1 && ((Boolean) listaGrid.get(i).getValor())){
//                    m = Moeda.somaValores(Float.parseFloat(Moeda.substituiVirgula( (String) listaGrid.get(i).getColuna().getColuna().getColuna().getColuna().getValor())), m);
//                    j = Moeda.somaValores(Float.parseFloat(Moeda.substituiVirgula( (String) listaGrid.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getValor())), j);
//                    c = Moeda.somaValores(Float.parseFloat(Moeda.substituiVirgula( (String) listaGrid.get(i).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor())), c);
//                }
//            }
//        }
//        acrescimoSindical = String.valueOf(Moeda.somaValores(Moeda.somaValores(m, j), c));
//        return Moeda.converteR$(acrescimoSindical);
//    }
//
//    public void setAcrescimoSindical(String acrescimoSindical) {
//        this.acrescimoSindical = Moeda.substituiVirgula(acrescimoSindical);
//    }
//
//    public String getTaxa() {
//        return Moeda.converteR$(taxa);
//    }
//
//    public void setTaxa(String taxa) {
//        this.taxa = Moeda.substituiVirgula(taxa);
//    }
//
//    public Pessoa getPessoa() {
//        Pessoa pes = getPessoaPesquisa();
//        if (pes.getId() != -1)
//            pessoa = pes;
//        return pessoa;
//    }
//
//    public void setPessoa(Pessoa pessoa) {
//        this.pessoa = pessoa;
//    }
//
//    public FolhaEmpresa getFolhaEmpresa() {
//        return folhaEmpresa;
//    }
//
//    public void setFolhaEmpresa(FolhaEmpresa folhaEmpresa) {
//        this.folhaEmpresa = folhaEmpresa;
//    }
//
//    public boolean isMostrarPainel() {
//        return mostrarPainel;
//    }
//
//    public void setMostrarPainel(boolean mostrarPainel) {
//        this.mostrarPainel = mostrarPainel;
//    }
//
//    public String getLabelLink() {
//        return labelLink;
//    }
//
//    public void setLabelLink(String labelLink) {
//        this.labelLink = labelLink;
//    }
//
//    public String getLabelFolha2() {
//        return labelFolha2;
//    }
//
//    public void setLabelFolha2(String labelFolha2) {
//        this.labelFolha2 = labelFolha2;
//    }
//
//    public int getQtdFuncionario() {
//        return qtdFuncionario;
//    }
//
//    public void setQtdFuncionario(int qtdFuncionario) {
//        this.qtdFuncionario = qtdFuncionario;
//    }
//
//    public String acordarMovimentos(){
//        int i = 0;
//        if (!listaGrid.isEmpty()){
//            List<Movimento> movto = new Vector<Movimento>();
//            List<ComplementoValor> compl = new ArrayList<ComplementoValor>();
//            while (i <  listaGrid.size()){
//                if (( (Boolean) ( (Linha) listaGrid.get(i)).getValor()) &&
//                        (( (Movimento) ( (Linha) listaGrid.get(i)).getColuna().getValor()).getValor() != 0)){
//                    movto.add( (Movimento) ( (Linha) listaGrid.get(i)).getColuna().getValor());
//                    compl.add( ((ComplementoValor)( (Linha) listaGrid.get(i)).getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getColuna().getValor()) );
//                }
//                i++;
//            }
//            opMovimentos.setLista(movto);
//            opMovimentos.setListaComplemento(compl);
//
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("arrayAcordo", opMovimentos);
//            return ((chamadaPaginaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).acordo();
//        }else{
//            return ((chamadaPaginaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).movimentosReceber();
//        }
//    }
//
//    public String baixarMovimentos(){
//        int i = 0;
//        if (!listaGrid.isEmpty()){
//            List<Movimento> movto = new Vector<Movimento>();
//            while (i <  listaGrid.size()){
//                if (( (Boolean) ( (Linha) listaGrid.get(i)).getValor()) &&
//                (( (Movimento) ( (Linha) listaGrid.get(i)).getColuna().getValor()).getValor() != 0)){
//                    movto.add( (Movimento) ( (Linha) listaGrid.get(i)).getColuna().getValor());
//                }
//                i++;
//            }
//            opMovimentos.setLista(movto);
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("arrayBaixa", opMovimentos);
//
//            return ((chamadaPaginaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
//        }else{
//            return ((chamadaPaginaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).movimentosReceber();
//        }
//    }
//
//    public boolean isMarcarTodos() {
//        return marcarTodos;
//    }
//
//    public void setMarcarTodos(boolean marcarTodos) {
//        this.marcarTodos = marcarTodos;
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
//    public void refreshFormDesconto(){
//        carregarGrid();
//    }
//
//    public boolean isDesabilitaValor() {
//        return desabilitaValor;
//    }
//
//    public void setDesabilitaValor(boolean desabilitaValor) {
//        this.desabilitaValor = desabilitaValor;
//    }
//}
//
//
