//
//package br.com.rtools.financeiro.beans;
//
//import br.com.rtools.financeiro.Historico;
//import br.com.rtools.financeiro.Lote;
//import br.com.rtools.financeiro.Movimento;
//import br.com.rtools.financeiro.Plano4;
//import br.com.rtools.financeiro.Plano5;
//import br.com.rtools.financeiro.db.ContaRotinaDB;
//import br.com.rtools.financeiro.db.ContaRotinaDBToplink;
//import br.com.rtools.financeiro.db.FTipoDocumentoDB;
//import br.com.rtools.financeiro.db.FTipoDocumentoDBToplink;
//import br.com.rtools.financeiro.db.FinanceiroDB;
//import br.com.rtools.financeiro.db.FinanceiroDBToplink;
//import br.com.rtools.financeiro.db.LoteDB;
//import br.com.rtools.financeiro.db.LoteDBToplink;
//import br.com.rtools.financeiro.db.MovimentoDB;
//import br.com.rtools.financeiro.db.MovimentoDBToplink;
//import br.com.rtools.financeiro.db.Plano4DB;
//import br.com.rtools.financeiro.db.Plano4DBToplink;
//import br.com.rtools.financeiro.db.Plano5DB;
//import br.com.rtools.financeiro.db.Plano5DBToplink;
//import br.com.rtools.pessoa.db.FilialDB;
//import br.com.rtools.pessoa.db.FilialDBToplink;
//import br.com.rtools.pessoa.db.PessoaDB;
//import br.com.rtools.pessoa.db.PessoaDBToplink;
//import br.com.rtools.seguranca.db.RotinaDB;
//import br.com.rtools.seguranca.db.RotinaDBToplink;
//import br.com.rtools.seguranca.db.UsuarioDB;
//import br.com.rtools.seguranca.db.UsuarioDBToplink;
//import br.com.rtools.utilitarios.DataHoje;
//import br.com.rtools.utilitarios.Moeda;
//import java.util.List;
//import java.util.Vector;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//import org.richfaces.component.html.HtmlDataTable;
//
//
//
//public class TransferenciaEntreContasJSFBean {
//    private HtmlDataTable htmlTable;
//    private Movimento movimentoE = new Movimento();
//    private Movimento movimentoS = new Movimento();
//    private Lote  lote = new Lote();
//    private Historico historico = new Historico();
//    private String descPesquisa = "";
//    private String porPesquisa = "d";
//    private String comoPesquisa = "T";
//    private int comboGrupoS = 0;
//    private int comboGrupoE = 0;
//    private int comboContaS = 0;
//    private int comboContaE = 0;
//    private String valor = "0";
//    private boolean retorno = false;
//    private String menssagem = "";
//    private String linkVoltar;
//
//
//
//    public HtmlDataTable getHtmlTable() {
//        return htmlTable;
//    }
//
//    public void setHtmlTable(HtmlDataTable htmlTable) {
//        this.htmlTable = htmlTable;
//    }
//
//    public Movimento getMovimentoE() {
//        return movimentoE;
//    }
//
//    public void setMovimentoE(Movimento movimentoE) {
//        this.movimentoE = movimentoE;
//    }
//
//    public Movimento getMovimentoS() {
//        return movimentoS;
//    }
//
//    public void setMovimentoS(Movimento movimentoS) {
//        this.movimentoS = movimentoS;
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
//    public Historico getHistorico() {
//        return historico;
//    }
//
//    public void setHistorico(Historico historico) {
//        this.historico = historico;
//    }
//
//    public int getComboGrupoS() {
//        return comboGrupoS;
//    }
//
//    public void setComboGrupoS(int comboGrupoS) {
//        this.comboGrupoS = comboGrupoS;
//    }
//
//    public int getComboGrupoE() {
//        return comboGrupoE;
//    }
//
//    public void setComboGrupoE(int comboGrupoE) {
//        this.comboGrupoE = comboGrupoE;
//    }
//
//    public int getComboContaS() {
//        return comboContaS;
//    }
//
//    public void setComboContaS(int comboContaS) {
//        this.comboContaS = comboContaS;
//    }
//
//    public int getComboContaE() {
//        return comboContaE;
//    }
//
//    public void setComboContaE(int comboContaE) {
//        this.comboContaE = comboContaE;
//    }
//
//    public boolean isRetorno() {
//        return retorno;
//    }
//
//    public void setRetorno(boolean retorno) {
//        this.retorno = retorno;
//    }
//
//
//    public String getMenssagem() {
//        return menssagem;
//    }
//
//    public void setMenssagem(String menssagem) {
//        this.menssagem = menssagem;
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
//    public String getPorPesquisa() {
//        return porPesquisa;
//    }
//
//    public void setPorPesquisa(String porPesquisa) {
//        this.porPesquisa = porPesquisa;
//    }
//
//    public String getComoPesquisa() {
//        return comoPesquisa;
//    }
//
//    public void setComoPesquisa(String comoPesquisa) {
//        this.comoPesquisa = comoPesquisa;
//    }
//
//
//    public List<SelectItem> getListaComboGSaida(){
//        List<SelectItem> grupoSaida = new Vector<SelectItem>();
//        comboContaS = 0;
//        Plano4DB db = new Plano4DBToplink();
//        int i = 0;
//        List select = null;
//        select = db.pesquisaTodasStrings();
//
//        while (i < select.size()){
//           grupoSaida.add(new SelectItem(  new Integer(i), (String) select.get(i)));
//           i++;
//        }
//
//        if (!(grupoSaida.isEmpty())){
//            if (this.isRetorno()){
//                i = 0;
//                while (i < grupoSaida.size()){
//                    if (grupoSaida.get(i).getLabel().equals(movimentoS.getPlano5().getPlano4().getConta())){
//                        this.comboGrupoS = i;
//                        break;
//                    }
//                    i++;
//                }
//            }
//        }
//
//        return grupoSaida;
//    }
//
//    public List<SelectItem> getListaComboSSaida(){
//        List<SelectItem> lista = new Vector<SelectItem>();
//        Plano5DB dbPL5 = new Plano5DBToplink();
//        ContaRotinaDB db = new ContaRotinaDBToplink();
//        Plano4 pl4 = new Plano4();
//        int i = 0;
//        List select = null;
//        if (this.getListaComboGSaida().size() != 0){
//            if (db.pesquisaPlano4PorDescricao((String) this.getListaComboGSaida().get(this.comboGrupoS).getLabel()) == null){
//                menssagem += "plano4";
//                return new Vector<SelectItem>();
//            }
//            pl4 = db.pesquisaPlano4PorDescricao((String) this.getListaComboGSaida().get(this.comboGrupoS).getLabel());
//            select = dbPL5.pesquisaPlano5(pl4.getId());
//            while (i < select.size()){
//               lista.add(new SelectItem(  new Integer(i), ((Plano5) select.get(i)).getConta() ));
//               i++;
//            }
//        }
//        if (!(lista.isEmpty())){
//            if (this.isRetorno()){
//                i = 0;
//                while (i < lista.size()){
//                    if (lista.get(i).getLabel().equals(movimentoS.getPlano5().getConta())){
//                        this.comboContaS = i;
//                        break;
//                    }
//                    i++;
//                }
//
//            }
//            return lista;
//        }else{
//            lista = new Vector<SelectItem>();
//            return lista;
//        }
//
//    }
//
//    public List<SelectItem> getListaComboGEntrada(){
//        List<SelectItem> grupoEntrada = new Vector<SelectItem>();
//        comboContaE = 0;
//        Plano4DB db = new Plano4DBToplink();
//        int i = 0;
//        List select = null;
//        select = db.pesquisaTodasStrings();
//
//        while (i < select.size()){
//           grupoEntrada.add(new SelectItem(  new Integer(i), (String) select.get(i)));
//           i++;
//        }
//
//        if (!(grupoEntrada.isEmpty())){
//            if (this.isRetorno()){
//                i = 0;
//                while (i < grupoEntrada.size()){
//                    if (grupoEntrada.get(i).getLabel().equals(movimentoE.getPlano5().getPlano4().getConta())){
//                        this.comboGrupoE = i;
//                        break;
//                    }
//                    i++;
//                }
//
//            }
//        }
//
//        return grupoEntrada;
//    }
//
//    public List<SelectItem> getListaComboSEntrada(){
//        List<SelectItem> lista = new Vector<SelectItem>();
//        Plano5DB dbPL5 = new Plano5DBToplink();
//        ContaRotinaDB db = new ContaRotinaDBToplink();
//        Plano4 pl4 = new Plano4();
//        int i = 0;
//        List select = null;
//        if (this.getListaComboGEntrada().size() != 0){
//            if (db.pesquisaPlano4PorDescricao((String) this.getListaComboGEntrada().get(this.comboGrupoE).getLabel()) == null){
//                menssagem += "plano4";
//                return new Vector<SelectItem>();
//            }
//            pl4 = db.pesquisaPlano4PorDescricao((String) this.getListaComboGEntrada().get(this.comboGrupoE).getLabel());
//            select = dbPL5.pesquisaPlano5(pl4.getId());
//            while (i < select.size()){
//               lista.add(new SelectItem(  new Integer(i), ((Plano5) select.get(i)).getConta()));
//               i++;
//            }
//        }
//        if (!(lista.isEmpty())){
//
//            if (this.isRetorno()){
//                i = 0;
//                while (i < lista.size()){
//                    if (lista.get(i).getLabel().equals(movimentoE.getPlano5().getConta())){
//                        this.comboContaE = i;
//                        break;
//                    }
//                    i++;
//                }
//                retorno = false;
//            }
//            return lista;
//        }else{
//            lista = new Vector<SelectItem>();
//            return lista;
//        }
//
//    }
//
//    public void refreshForm(){
//
//    }
//
//    public String getValor() {
//        return Moeda.converteR$(valor);
//    }
//
//    public void setValor(String valor) {
//        this.valor = valor;
//    }
//
//    public String salvar(Movimento movimento){
//        FinanceiroDB db = new FinanceiroDBToplink();
//        if (movimento.getId() == -1){
//            db.insert(movimento);
//        }
//        else{
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(movimento)){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//       return "cadTransferenciaEntreContas";
//    }
//
//    public String salvarLote(Lote lote){
//        FinanceiroDB db = new FinanceiroDBToplink();
//        if (lote.getId() == -1){
//            db.insert(lote);
//        }
//        else{
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(lote)){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//       return "cadTransferenciaEntreContas";
//    }
//
//    public String salvarHistorico(Historico historico){
//        FinanceiroDB db = new FinanceiroDBToplink();
//        if (historico.getId() == -1){
//            db.insert(historico);
//        }
//        else{
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(historico)){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//       return "cadTransferenciaEntreContas";
//    }
//
//   public String excluir(Movimento movimento){
//        MovimentoDB db = new MovimentoDBToplink();
//        if (movimento.getId()!=-1){
//            db.getEntityManager().getTransaction().begin();
//            movimento = db.pesquisaCodigo(movimento.getId());
//            if (db.delete(movimento)){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//       movimento = new Movimento();
//       return "cadTransferenciaEntreContas";
//   }
//
//   public String excluirHistorico(Movimento movimento){
//        MovimentoDB db = new MovimentoDBToplink();
//        Historico hist = null;
//        if (movimento.getId()!=-1){
//            db.getEntityManager().getTransaction().begin();
//            hist = db.pesquisaHistorico(movimento.getId());
//            if (db.deleteHistorico(hist).equals("ok")){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//       movimento = new Movimento();
//       return "cadTransferenciaEntreContas";
//   }
//
//   public String excluirLote(Lote lote){
//        LoteDB db = new LoteDBToplink();
//        if (lote.getId()!=-1){
//            db.getEntityManager().getTransaction().begin();
//            lote = db.pesquisaCodigo(lote.getId());
//            if (db.delete(lote)){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//       lote = new Lote();
//       return "cadTransferenciaEntreContas";
//   }
//
//
//   public String salvarTransferencia(){
//       FilialDB dbFil = new FilialDBToplink();
//       UsuarioDB dbU = new UsuarioDBToplink();
//       Plano5DB db = new Plano5DBToplink();
//       FTipoDocumentoDB dbTipo = new FTipoDocumentoDBToplink();
//       RotinaDB dbRot = new RotinaDBToplink();
//       PessoaDB dbPessoa = new PessoaDBToplink();
//       ContaRotinaDB dbConta = new ContaRotinaDBToplink();
//       int rotS = 0, rotE = 0;
//
//       if (Float.parseFloat(Moeda.substituiVirgula(valor)) == 0)
//           return "cadTransferenciaEntreContas";
//
//
//
//       List<SelectItem> lContaS = getListaComboSSaida();
//       List<SelectItem> lContaE = getListaComboSEntrada();
//       List<SelectItem> lGrupoS = getListaComboGSaida();
//       List<SelectItem> lGrupoE = getListaComboGEntrada();
//
///*
//       menssagem = "";
//       menssagem += "Plano5: Saida               \n";
//       menssagem += "Conta " + (String) this.getListaComboSSaida().get(this.comboContaS).getLabel();
//       menssagem += "Grupo " + (String) this.getListaComboGSaida().get(this.comboGrupoS).getLabel();
//       if ((db.pesquisaPlano5PorDesc( (String) this.getListaComboSSaida().get(this.comboContaS).getLabel() , (String) this.getListaComboGSaida().get(this.comboGrupoS).getLabel() )) != null)
//           menssagem += "Pesquisa" + db.pesquisaPlano5PorDesc( (String) this.getListaComboSSaida().get(this.comboContaS).getLabel() , (String) this.getListaComboGSaida().get(this.comboGrupoS).getLabel() ).getConta();
//
//       menssagem += "\n             \n";
//       menssagem += "Plano5: Entrada             \n";
//       menssagem += "Conta " + (String) this.getListaComboSEntrada().get(this.comboContaE).getLabel();
//       menssagem += "Grupo " + (String) this.getListaComboGEntrada().get(this.comboGrupoE).getLabel();
//       if (db.pesquisaPlano5PorDesc( (String) this.getListaComboSEntrada().get(this.comboContaE).getLabel() , (String) this.getListaComboGEntrada().get(this.comboGrupoE).getLabel() ) != null)
//           menssagem += "Pesquisa" + db.pesquisaPlano5PorDesc( (String) this.getListaComboSEntrada().get(this.comboContaE).getLabel() , (String) this.getListaComboGEntrada().get(this.comboGrupoE).getLabel() ).getConta();
//
//
//       if ((db.pesquisaPlano5PorDesc( (String) this.getListaComboSSaida().get(this.comboContaS).getLabel() , (String) this.getListaComboGSaida().get(this.comboGrupoS).getLabel() ) == null) ||
//            (db.pesquisaPlano5PorDesc( (String) this.getListaComboSEntrada().get(this.comboContaE).getLabel() , (String) this.getListaComboGEntrada().get(this.comboGrupoE).getLabel()) == null)){
//           menssagem += "###" + (String) this.getListaComboGSaida().get(this.comboGrupoS).getLabel();
//           menssagem += "  :  " + (String) this.getListaComboSSaida().get(this.comboContaS).getLabel();
//           menssagem += "###" + (String) this.getListaComboSEntrada().get(this.comboContaE).getLabel();
//           menssagem += "  :  " + (String) this.getListaComboGEntrada().get(this.comboGrupoE).getLabel();
//           return "cadTransferenciaEntreContas";
//       }*/
//
//       movimentoS.setDebitoCredito("C");
//       movimentoS.setPessoa( dbPessoa.pesquisaCodigo(0));
////       movimentoS.setNomePessoa("");
//
//       movimentoS.setValor( Float.parseFloat(Moeda.substituiVirgula(valor)));
//       movimentoS.setPlano5(db.pesquisaPlano5PorDesc( (String) lContaS.get(this.comboContaS).getLabel() , (String) lGrupoS.get(this.comboGrupoS).getLabel() ));
//       movimentoS.setFTipoDocumento(dbTipo.pesquisaCodigo(5));
//       movimentoS.setNumero("S/N");
//       movimentoS.setDepartamento(null);
//       movimentoS.setLoteBaixa(null);
//       movimentoS.setFilial(dbFil.pesquisaCodigo(1));
//       movimentoS.setVencimento(null);
//       movimentoS.setAcordo(-1);
//
//
//       movimentoE.setDebitoCredito("D");
//       movimentoE.setPessoa( dbPessoa.pesquisaCodigo(0));
////       movimentoE.setNomePessoa("");
//       movimentoE.setValor( Float.parseFloat(Moeda.substituiVirgula(valor)) );
//       movimentoE.setPlano5(db.pesquisaPlano5PorDesc( (String) lContaE.get(this.comboContaE).getLabel() , (String) lGrupoE.get(this.comboGrupoE).getLabel() ));
//       movimentoE.setFTipoDocumento(dbTipo.pesquisaCodigo(5));
//       movimentoE.setNumero("S/N");
//       movimentoE.setDepartamento(null);
//       movimentoE.setLoteBaixa(null);
//       movimentoE.setFilial(dbFil.pesquisaCodigo(1));
//       movimentoE.setVencimento(null);
//       movimentoE.setAcordo(-1);
//
//       if ((movimentoS.getPlano5().getPlano4().getId() == -1) ||
//           (movimentoE.getPlano5().getPlano4().getId() == -1)){
//           return "cadTransferenciaEntreContas";
//       }
//
//       lote.setPagRec("");
//       lote.setRotina(dbRot.pesquisaCodigo(3));
//       lote.setCompetencia(DataHoje.data());
//
//       this.salvarLote(lote);
//
//
//       movimentoE.setLote(lote);
//       movimentoS.setLote(lote);
//       this.salvar(movimentoS);
//       this.salvar(movimentoE);
//
//       rotS = dbConta.pesquisaRotina(movimentoS.getPlano5().getPlano4().getId());
//       rotE = dbConta.pesquisaRotina(movimentoE.getPlano5().getPlano4().getId());
//
//
//       if ((rotS == 1) && (rotE == 2)){
//           historico.setHistorico("DEPÓSITO");
//       }else if ((rotS == 2) && (rotE == 1)){
//           historico.setHistorico("SUPRIMENTO DE CAIXA");
//       }else{
//           historico.setHistorico("TRANSFERÊNCIA");
//       }
//
//       this.historico.setMovimento(movimentoS);
//
//       this.salvarHistorico(historico);
//
//       historico = new Historico();
//       movimentoS = new Movimento();
//       movimentoE = new Movimento();
//       lote = new Lote();
//       valor = "0";
//       comboGrupoS = 0;
//       comboGrupoE = 0;
//       comboContaS = 0;
//       comboContaE = 0;
//       return "cadTransferenciaEntreContas";
//   }
//
//   public String novo(){
//       historico = new Historico();
//       movimentoS = new Movimento();
//       movimentoE = new Movimento();
//       lote = new Lote();
//       valor = "0";
//       comboGrupoS = 0;
//       comboGrupoE = 0;
//       comboContaS = 0;
//       comboContaE = 0;
//       return "cadTransferenciaEntreContas";
//   }
//
//   public String excluirTransferencia(){
//       this.excluirHistorico(movimentoS) ;
//       this.excluirHistorico(movimentoE) ;
//       this.excluir(movimentoE);
//       this.excluir(movimentoS);
//       this.excluirLote(lote);
//       novo();
//       return "cadTransferenciaEntreContas";
//   }
//
//    public List getPesquisaLote(){
//       List result = null;
//       LoteDB db = new LoteDBToplink();
//       result = db.pesquisarLoteTransferência(descPesquisa , porPesquisa, comoPesquisa);
//       return result;
//    }
//
//    public String retornaLote(){
//        LoteDB db = new LoteDBToplink();
//        MovimentoDB dbMov = new MovimentoDBToplink();
//        lote = ((Lote) getHtmlTable().getRowData());
//        List lista =  db.dependentesTransferencia(lote.getId());
//        if (((Movimento) lista.get(0) ).getDebitoCredito().equals("C")){
//            movimentoS = (Movimento) lista.get(0);
//            movimentoE = (Movimento) lista.get(1);
//        }else{
//            movimentoS = (Movimento) lista.get(1);
//            movimentoE = (Movimento) lista.get(0);
//        }
//
//        historico = dbMov.pesquisaHistorico(movimentoE.getId());
//        if (historico == null)
//            historico = dbMov.pesquisaHistorico(movimentoS.getId());
//
//        valor = Float.toString(movimentoS.getValor());
//
//        retorno = true;
//
//        return "cadTransferenciaEntreContas";
//    }
//
//    public String pesquisarTransferenciaEntreContas(){
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "cadTransferenciaEntreContas");
//        return "pesquisaTransferencia";
//    }
//
//    public String linkVoltar(){
//        if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null){
//            return "menuFinanceiro";
//        }else
//            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//    }
//
//    public String linkVoltarPesquisaTransferencia(){
//        linkVoltar = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//        if ( linkVoltar == null){
//            return "cadTransferenciaEntreContas";
//        }else
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("urlRetorno");
//            return linkVoltar;
//    }
//}
