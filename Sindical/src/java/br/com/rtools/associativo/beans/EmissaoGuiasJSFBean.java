//package br.com.rtools.associativo.beans;
//
//import br.com.rtools.associativo.GrupoConvenio;
//import br.com.rtools.associativo.MovimentoResponsavel;
//import br.com.rtools.associativo.Socios;
//import br.com.rtools.associativo.SubGrupoConvenio;
//import br.com.rtools.associativo.db.ConvenioDB;
//import br.com.rtools.associativo.db.ConvenioDBToplink;
//import br.com.rtools.associativo.db.ConvenioServicoDB;
//import br.com.rtools.associativo.db.ConvenioServicoDBToplink;
//import br.com.rtools.associativo.db.GrupoConvenioDB;
//import br.com.rtools.associativo.db.GrupoConvenioDBToplink;
//import br.com.rtools.associativo.db.SociosDB;
//import br.com.rtools.associativo.db.SociosDBToplink;
//import br.com.rtools.associativo.db.SubGrupoConvenioDB;
//import br.com.rtools.associativo.db.SubGrupoConvenioDBToplink;
//import br.com.rtools.classeOperacao.OperacaoGuia;
//import br.com.rtools.classeOperacao.OperacaoMovimento;
//import br.com.rtools.financeiro.Guia;
//import br.com.rtools.financeiro.Movimento;
//import br.com.rtools.financeiro.ServicoValor;
//import br.com.rtools.financeiro.Servicos;
//import br.com.rtools.financeiro.db.MovimentoDB;
//import br.com.rtools.financeiro.db.MovimentoDBToplink;
//import br.com.rtools.financeiro.db.ServicoValorDB;
//import br.com.rtools.financeiro.db.ServicoValorDBToplink;
//import br.com.rtools.financeiro.db.ServicosDB;
//import br.com.rtools.financeiro.db.ServicosDBToplink;
//import br.com.rtools.lista.GGuia;
//import br.com.rtools.lista.GPGuia;
//import br.com.rtools.lista.Lista;
//import br.com.rtools.pessoa.Juridica;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.seguranca.controleUsuario.chamadaPaginaJSFBean;
//import br.com.rtools.utilitarios.DataHoje;
//import br.com.rtools.utilitarios.Moeda;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Vector;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//import org.richfaces.component.html.HtmlDataTable;
//
//public class EmissaoGuiasJSFBean {
//    private Pessoa pessoa = new Pessoa();
//    private Pessoa pessoaTitular = new Pessoa();
//    private Juridica juridicaConveniada = new Juridica();
//    private Lista listaInclusos = new Lista(GGuia.class);
//    private HtmlDataTable htmlTable = new HtmlDataTable();
//    private String totalValor = "0";
//    private String dataEmissao = DataHoje.data();
//    private String vlValor = "";
//    private String vlDesconto = "0";
//    private String msgConfirma = "";
//    private float valor = 0;
//    private float desconto = 0;
//    private int idServico = 0;
//    private int idGrupo = 0;
//    private int idSubGrupo = 0;
//    private int qntServicos = 1;
//    private String idLoteBaixa = "";
//    private String idGuia = "";
//    private String porPesquisa = "";
//    private String descPesquisa = "";
//    private Lista lista = new Lista(GPGuia.class);
//    private HtmlDataTable htmlTablePesquisa = new HtmlDataTable();
//
//
//    public List<SelectItem> getListaServico(){
//            List<SelectItem> servicos = new Vector<SelectItem>();
//            int i = 0;
//            ConvenioServicoDB db = new ConvenioServicoDBToplink();
//            List select = new ArrayList();
//            List grupo = getListaGrupo();
//            if(!grupo.isEmpty()){
//                select = db.pesquisaServicosSubGrupoConvenio(Integer.parseInt(getListaSubGrupo().get(idSubGrupo).getDescription()) );
//            }
//            while (i < select.size()){
//                servicos.add(new SelectItem(new Integer(i),
//                       (String) ((Servicos) select.get(i)).getDescricao(),
//                       Integer.toString(((Servicos) select.get(i)).getId())  ));
//                i++;
//            }
//        return servicos;
//    }
//
//    public List<SelectItem> getListaGrupo(){
//        List<SelectItem> grupos = new Vector<SelectItem>();
//            int i = 0;
//            GrupoConvenioDB db = new GrupoConvenioDBToplink();
//            List select = db.pesquisaTodos();
//            while (i < select.size()){
//                grupos.add(new SelectItem(new Integer(i),
//                       (String) ((GrupoConvenio) select.get(i)).getDescricao(),
//                       Integer.toString(((GrupoConvenio) select.get(i)).getId())  ));
//               i++;
//            }
//        return grupos;
//    }
//
//    public List<SelectItem> getListaSubGrupo(){
//        List<SelectItem> subGrupo = new Vector<SelectItem>();
//            int i = 0;
//            SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
//            List select = new ArrayList();
//            List grupo = getListaGrupo();
//            if(!grupo.isEmpty()){
//                select = db.pesquisaSubGrupoConvênioPorGrupo(Integer.parseInt(getListaGrupo().get(idGrupo).getDescription()));
//            }
//            while (i < select.size()){
//                subGrupo.add(new SelectItem(new Integer(i),
//                       (String) ((SubGrupoConvenio) select.get(i)).getDescricao(),
//                       Integer.toString(((SubGrupoConvenio) select.get(i)).getId())  ));
//                i++;
//            }
//        return subGrupo;
//    }
//
//    public List getListaServicosInclusos(){
//        return listaInclusos;
//    }
//
//    public String inserirServico(){
//        if (!getListaServico().isEmpty()){
//            ServicosDB dbS = new ServicosDBToplink();
//            Servicos serv = dbS.pesquisaCodigo(Integer.parseInt(getListaServico().get(idServico).getDescription()));
//            boolean existe = false;
//            for(int w = 0; w < listaInclusos.size();w++){
//                if (serv.getId() == ((GGuia)listaInclusos.get(w)).getServico().getId()){
//                    existe = true;
//                }
//            }
//            if (!existe){
//                desconto = Moeda.converteUS$(vlDesconto);
//                float valorLiquido = 0;
//                if (valor != 0){
//                    valorLiquido = Moeda.multiplicarValores(valor, qntServicos);
//                    valorLiquido = Moeda.subtracaoValores(valorLiquido, desconto);
//                }else valorLiquido = 0;
//                listaInclusos.add( new GGuia(serv,
//                                             qntServicos,
//                                             valor,
//                                             valorLiquido,
//                                             desconto)
//                                 );
//            }
//        }
//        qntServicos = 1;
//        vlDesconto = "0";
//        return null;
//    }
//
//    public void atualizaGrid(){
//    }
//
//    public String debito(){
//        if (!listaInclusos.isEmpty()){
//            msgConfirma = (new OperacaoGuia(listaInclusos)).debitarGuia(pessoaTitular, pessoa, dataEmissao);
//        }else{
//            msgConfirma = "Lista de Servicos vazia!";
//        }
//        return null;
//    }
//
//    public String baixar(){
//        if (!listaInclusos.isEmpty()){
//            OperacaoGuia operacaoGuia = new OperacaoGuia(listaInclusos);
//            msgConfirma = operacaoGuia.debitarGuia(pessoaTitular, pessoa, dataEmissao);
//            if (!operacaoGuia.getListaMovimento().isEmpty()){
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("arrayBaixa", new OperacaoMovimento(operacaoGuia.getListaMovimento()));
//                return ((chamadaPaginaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
//            }
//        }else{
//            msgConfirma = "Lista de Servicos vazia!";
//        }
//        return null;
//    }
//
//    public String excluirGuia(){
//        listaInclusos.remove(htmlTable.getRowIndex());
//        return null;
//    }
//    public String novo(){
//        juridicaConveniada = new Juridica();
//        pessoa = new Pessoa();
//        pessoaTitular = new Pessoa();
//        listaInclusos = new Lista(GGuia.class);
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
//        return "emissaoGuias";
//    }
//
//    public String editarEmpresaConveniada(){
//       GPGuia gPGuia = (GPGuia) getHtmlTablePesquisa().getRowData();
//       pessoa = gPGuia.getMovimentoResponsavel().getBeneficiario();
//       pessoaTitular = gPGuia.getMovimentoResponsavel().getTitular();
//       dataEmissao = gPGuia.getGuia().getLote().getData();
//       idGuia = Integer.toString(gPGuia.getGuia().getId());
//       if (gPGuia.getMovimentoResponsavel().getMovimento().getLoteBaixa() != null){
//           idLoteBaixa = Integer.toString(gPGuia.getMovimentoResponsavel().getMovimento().getLoteBaixa().getId());
//       }
//
//       MovimentoDB movimentoDB = new MovimentoDBToplink();
//       List<Movimento> listaMovimento = movimentoDB.pesquisaGuia(gPGuia.getGuia());
//
////       for(Movimento movimento : listaMovimento){
////           listaInclusos.add( new GGuia(
////                             movimento.getServicos(),
////                             movimento.getQuantidade(),
////                             movimento.getValor(),
////                             Moeda.subtracaoValores( movimento.getValor(), movimento.getDesconto() ),
////                             movimento.getDesconto()));
////       }
//
//       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
//       if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null)
//           return "emissaoGuias";
//       else
//           return (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//    }
//
//    public List getListaEmpresasConveniadas(){
//        ConvenioDB db = new ConvenioDBToplink();
//        List<Juridica> lista = db.pesquisaJuridicaPorGrupoESubGrupo(Integer.parseInt(getListaSubGrupo().get(idSubGrupo).getDescription()),
//                                                            Integer.parseInt(getListaGrupo().get(idGrupo).getDescription()));
//        return lista;
//    }
//
//    public void refreshForm(){
//
//    }
//
//    public void refreshFormRazao(){
//        juridicaConveniada = new Juridica();
//    }
//
//    public Pessoa getPessoa() {
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null){
//            pessoa = (Pessoa)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
//        }
//        return pessoa;
//    }
//
//    public void setPessoa(Pessoa pessoa) {
//        this.pessoa = pessoa;
//    }
//
//    public int getIdServico() {
//        return idServico;
//    }
//
//    public void setIdServico(int idServico) {
//        this.idServico = idServico;
//    }
//
//    public int getIdGrupo() {
//        return idGrupo;
//    }
//
//    public void setIdGrupo(int idGrupo) {
//        this.idGrupo = idGrupo;
//    }
//
//    public int getIdSubGrupo() {
//        return idSubGrupo;
//    }
//
//    public void setIdSubGrupo(int idSubGrupo) {
//        this.idSubGrupo = idSubGrupo;
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
//    public Juridica getJuridicaConveniada() {
//        return juridicaConveniada;
//    }
//
//    public void setJuridicaConveniada(Juridica juridicaConveniada) {
//        this.juridicaConveniada = juridicaConveniada;
//    }
//
//    public String getTotalValor(){
//        float somaFloat = 0 ;
//        int i = 0;
//        String result = "R$ ";
//        String r = "";
//        if (!listaInclusos.isEmpty()){
//            while (i < listaInclusos.size()){
//                somaFloat = Moeda.somaValores(somaFloat , ( (GGuia) listaInclusos.get(i)).getValorLiquido() ) ;
//                i++;
//            }
//            totalValor = String.valueOf(somaFloat);
//            r = Moeda.converteR$(totalValor);
//            return result + r;
//        }else
//            return result;
//    }
//
//    public String getDataEmissao() {
//        return dataEmissao;
//    }
//
//    public void setDataEmissao(String dataEmissao) {
//        this.dataEmissao = dataEmissao;
//    }
//
//    public Pessoa getPessoaTitular() {
//        SociosDB db = new SociosDBToplink();
//        if (pessoaTitular.getId() == -1 && getPessoa().getId() != -1){
//            Socios socios = new Socios();
//            Socios sociosTitular = new Socios();
//            socios = db.pesquisaSocioPorPessoa(getPessoa().getId());
//            if (socios != null){
//                sociosTitular = db.pesquisaSocioDoDependente(socios.getId());
//                if ( sociosTitular != null){
//                    pessoaTitular = sociosTitular.getServicoPessoa().getPessoa();
//                    if (pessoaTitular == null)
//                        pessoaTitular = new Pessoa();
//                }else{
//                    pessoaTitular = getPessoa();
//                }
//            }else{
//                pessoaTitular = getPessoa();
//            }
//        }
//        return pessoaTitular;
//    }
//
//    public void setPessoaTitular(Pessoa pessoaTitular) {
//        this.pessoaTitular = pessoaTitular;
//    }
//
//    public String getVlDesconto() {
//        return Moeda.converteR$(vlDesconto);
//    }
//
//    public void setVlDesconto(String vlDesconto) {
//        this.vlDesconto = vlDesconto;
//    }
//
//    public int getQntServicos() {
//        if (qntServicos == 0)
//            qntServicos = 1;
//        return qntServicos;
//    }
//
//    public void setQntServicos(int qntServicos) {
//        this.qntServicos = qntServicos;
//    }
//
//    public String getVlValor(){
//        ServicoValorDB db = new ServicoValorDBToplink();
//        List<ServicoValor> valores = null;
//        if(!getListaServico().isEmpty())
//            valores = db.pesquisaServicoValor(Integer.parseInt(getListaServico().get(idServico).getDescription()));
//        else
//            valores = new ArrayList();
//        valor = 0;
//        if (!valores.isEmpty()){
//            for(int i = 0; i < valores.size();i++){
//                valor = Moeda.somaValores(valor, valores.get(i).getValor());
//            }
//            vlValor = Moeda.converteR$(String.valueOf(valor));
//        }else{
//            vlValor = Moeda.converteR$("0");
//        }
//        return vlValor;
//    }
//
//    public String pesquisaComIniciais(){
//        MovimentoDB movimentoDB = new MovimentoDBToplink();
//        List result = movimentoDB.pesquisaGuia(porPesquisa, descPesquisa + "%");
//        this.carregarLista(result);
//        return null;
//    }
//
//    private void carregarLista(List result){
//        int i = 0;
//        Object[] linha = null;
//        lista = new Lista(GPGuia.class);
//        while(i < result.size()){
//            linha = (Object[]) result.get(i);
//            lista.add(new GPGuia( (Guia) linha[1], (MovimentoResponsavel) linha[2] ) );
//            i++;
//        }
//    }
//
//    public void pesquisaParcial(){
//        MovimentoDB movimentoDB = new MovimentoDBToplink();
//        List result = movimentoDB.pesquisaGuia(porPesquisa, "%" + descPesquisa + "%");
//        this.carregarLista(result);
//    }
//
//    public String getMascara(){
//        if (porPesquisa.equals("data")){
//            return "data";
//        }else{
//            return "";
//        }
//    }
//
//    public String getMaxLength(){
//        if (porPesquisa.equals("data")){
//            return "10";
//        }else{
//            return "50";
//        }
//    }
//
//    public void setVlValor(String vlValor) {
//        this.vlValor = vlValor;
//    }
//
//    public String getMsgConfirma() {
//        return msgConfirma;
//    }
//
//    public void setMsgConfirma(String msgConfirma) {
//        this.msgConfirma = msgConfirma;
//    }
//
//    public String getIdLoteBaixa() {
//        return idLoteBaixa;
//    }
//
//    public void setIdLoteBaixa(String idLoteBaixa) {
//        this.idLoteBaixa = idLoteBaixa;
//    }
//
//    public String getIdGuia() {
//        return idGuia;
//    }
//
//    public void setIdGuia(String idGuia) {
//        this.idGuia = idGuia;
//    }
//
//    public String getPorPesquisa() {
//        return porPesquisa;
//    }
//
//    public void setPorPesquisa(String porPesquisa) {
//        this.porPesquisa = porPesquisa;
//    }
//
//    public String getDescPesquisa() {
//        return descPesquisa;
//    }
//
//    public void setDescPesquisa(String descPesquisa) {
//        this.descPesquisa = descPesquisa;
//    }
//
//    public Lista getLista() {
//        return lista;
//    }
//
//    public void setLista(Lista lista) {
//        this.lista = lista;
//    }
//
//    public HtmlDataTable getHtmlTablePesquisa() {
//        return htmlTablePesquisa;
//    }
//
//    public void setHtmlTablePesquisa(HtmlDataTable htmlTablePesquisa) {
//        this.htmlTablePesquisa = htmlTablePesquisa;
//    }
//
//}