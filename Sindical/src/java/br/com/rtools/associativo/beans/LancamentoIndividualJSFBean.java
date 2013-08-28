//package br.com.rtools.associativo.beans;
//
//
//import br.com.rtools.associativo.Parentesco;
//import br.com.rtools.associativo.Socios;
//import br.com.rtools.associativo.db.SociosDB;
//import br.com.rtools.associativo.db.SociosDBToplink;
//import br.com.rtools.financeiro.*;
//import br.com.rtools.financeiro.db.ContaCobrancaDB;
//import br.com.rtools.financeiro.db.ContaCobrancaDBToplink;
//import br.com.rtools.financeiro.db.FTipoDocumentoDB;
//import br.com.rtools.financeiro.db.FTipoDocumentoDBToplink;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.seguranca.Usuario;
//import br.com.rtools.utilitarios.Moeda;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Vector;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//import org.richfaces.component.html.HtmlDataTable;
//
//public class LancamentoIndividualJSFBean {
//
//    private Movimento movimento = new Movimento();
//    private Lote lote = new Lote();
//    private Usuario usuario = new Usuario();
//    private Pessoa pessoa = new Pessoa();
//    private Pessoa titular = new Pessoa();
//    private int quantidade = 1;
//    private boolean decontoFolha = false;
//    private boolean chkContaCobranca = false;
//    //private Responsavel responsavel = new Responsavel();
//    private int idIndex = -1;
//    private List<Movimento> listaLancamentoIndividual = new ArrayList();    
//    private List<Movimento> listaMovimento = new ArrayList();
//    private int idContaCobranca = 0;
//    private int idTipoDocumento = 0;
//    private String valor = "0";
//    private Historico historico = new Historico();
//    private String codigo = "";
//    private String mensagem = "";
//
//    public void adicionar(){
//
//    }
//
//    public void refreshForm(){
//
//    }
//
//    public Pessoa getPessoaPesquisa(){
//        Pessoa p = new Pessoa();
//        try{
//            p = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
//            if (p == null){
//                return new Pessoa();
//            }else{
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
//                return p;
//            }
//        }catch(Exception e){
//            p = new Pessoa();
//            return p;
//        }
//    }
//
//    public List<SelectItem> getListaTipoDocumento(){
//        List<SelectItem> listaT = new Vector<SelectItem>();
//        int i = 0;
//        FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
//        //List select = db.pesquisaTodos();
//        List select = new ArrayList();
//        if (isChkContaCobranca())
//            select.add(db.pesquisaCodigo(2));
//        else
//            select = db.pesquisaListaTipoExtrato();
//        while (i < select.size()){
//            listaT.add(new SelectItem( new Integer(i),
//                        (String) ((FTipoDocumento) select.get(i)).getTipoDocumento(),
//                        Integer.toString(((FTipoDocumento) select.get(i)).getId()) ));
//            i++;
//        }
//        return listaT;
//    }
//
//    public List<SelectItem> getListaContaCobranca(){
//        List<SelectItem> listaC = new Vector<SelectItem>();
//        int i = 0;
//        ContaCobrancaDB db = new ContaCobrancaDBToplink();
//        List select = db.pesquisaTodos();
//        while (i < select.size()){
//            listaC.add(new SelectItem( new Integer(i),
//                        (String) ((ContaCobranca) select.get(i)).getCodCedente()+" - "+(String) ((ContaCobranca) select.get(i)).getContaBanco().getBanco().getBanco(),
//                        Integer.toString(((ContaCobranca) select.get(i)).getId()) ));
//            i++;
//        }
//        return listaC;
//    }
//
//    public Pessoa getPessoaTitular() {
//        SociosDB db = new SociosDBToplink();
//        if (titular.getId() == -1 && getPessoa().getId() != -1){
//            Socios socios = new Socios();
//            Socios sociosTitular = new Socios();
//            socios = db.pesquisaSocioPorPessoa(getPessoa().getId());
//            if (socios != null){
//                sociosTitular = db.pesquisaSocioDoDependente(socios.getId());
//                if ( sociosTitular != null){
//                    titular = sociosTitular.getServicoPessoa().getPessoa();
//                    if (titular == null)
//                        titular = new Pessoa();
//                }else{
//                    titular = getPessoa();
//                }
//            }else{
//                titular = getPessoa();
//            }
//        }
//        return titular;
//    }
//
//
//
//    public Movimento getMovimento() {
//        return movimento;
//    }
//
//    public void setMovimento(Movimento movimento) {
//        this.movimento = movimento;
//    }
//
//    public Lote getLote() {
//        return lote;
//    }
//
//    public void setLote(Lote lote) {
//        this.lote = lote;
//    }
//
//    public String getNumeroLote(){
//        if(lote.getId() == -1){
//            return "";
//        }else{
//            return Integer.toString(lote.getId());
//        }
//    }
//
//    public Usuario getUsuario() {
//        if(usuario.getId() == -1){
//            Usuario usuarioSessao = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
//            if(usuarioSessao == null){
//                usuario = usuarioSessao;
//            }
//        }
//        return usuario;
//    }
//
//    public void setUsuario(Usuario usuario) {
//        this.usuario = usuario;
//    }
//
//    public Pessoa getPessoa() {
//        if(pessoa.getId() == -1){
//            pessoa = getPessoaPesquisa();
//        }
//        return pessoa;
//    }
//
//    public void setPessoa(Pessoa pessoa) {
//        this.pessoa = pessoa;
//    }
//
//    public Pessoa getTitular() {
//        if (titular.getId() == -1){
//            titular = getPessoaPesquisa();
//        }
//        return titular;
//    }
//
//    public void setTitular(Pessoa titular) {
//        this.titular = titular;
//    }
//
//    public int getQuantidade() {
//        return quantidade;
//    }
//
//    public void setQuantidade(int quantidade) {
//        this.quantidade = quantidade;
//    }
//
//    public boolean isDecontoFolha() {
//        return decontoFolha;
//    }
//
//    public void setDecontoFolha(boolean decontoFolha) {
//        this.decontoFolha = decontoFolha;
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
//    public List<Movimento> getListaMovimento() {
//        return listaMovimento;
//    }
//
//    public void setListaMovimento(List<Movimento> listaMovimento) {
//        this.listaMovimento = listaMovimento;
//    }
//
//    public boolean isChkContaCobranca() {
//        return chkContaCobranca;
//    }
//
//    public void setChkContaCobranca(boolean chkContaCobranca) {
//        this.chkContaCobranca = chkContaCobranca;
//    }
//
////    public Responsavel getResponsavel() {
////        return responsavel;
////    }
//
////    public void setResponsavel(Responsavel responsavel) {
////        this.responsavel = responsavel;
////    }
//
//    public int getIdContaCobranca() {
//        return idContaCobranca;
//    }
//
//    public void setIdContaCobranca(int idContaCobranca) {
//        this.idContaCobranca = idContaCobranca;
//    }
//
//    public int getIdTipoDocumento() {
//        return idTipoDocumento;
//    }
//
//    public void setIdTipoDocumento(int idTipoDocumento) {
//        this.idTipoDocumento = idTipoDocumento;
//    }
//
//    public void setValor(String valor) {
//        this.valor = valor;
//    }
//
//    public String getValor(){
//        return Moeda.converteR$(valor);
//    }
//
//    public Historico getHistorico() {
//        return historico;
//    }
//
//    public void setHistorico(Historico historico) {
//        this.historico = historico;
//    }
//
//    public String getCodigo() {
//        if(getPessoa().getId() != -1){
//            codigo = Integer.toString(pessoa.getId()) + " - ";
//        }else{
//            codigo = "";
//        }
//        return codigo;
//    }
//
//    public void setCodigo(String codigo) {
//        this.codigo = codigo;
//    }
//
//    public String getMensagem() {
//        return mensagem;
//    }
//
//    public void setMensagem(String mensagem) {
//        this.mensagem = mensagem;
//    }
//}
