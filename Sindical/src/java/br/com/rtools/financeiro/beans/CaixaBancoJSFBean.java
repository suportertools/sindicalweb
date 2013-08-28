//
//package br.com.rtools.financeiro.beans;
//
//import br.com.rtools.financeiro.Cheques;
//import br.com.rtools.financeiro.ContaBanco;
//import br.com.rtools.financeiro.ContaRotina;
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
//import br.com.rtools.financeiro.db.Plano5DB;
//import br.com.rtools.financeiro.db.Plano5DBToplink;
//import br.com.rtools.pesquisa.Pesquisa;
//import br.com.rtools.pessoa.Fisica;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.pessoa.db.FilialDB;
//import br.com.rtools.pessoa.db.FilialDBToplink;
//import br.com.rtools.pessoa.db.PessoaDB;
//import br.com.rtools.pessoa.db.PessoaDBToplink;
//import br.com.rtools.seguranca.db.UsuarioDB;
//import br.com.rtools.seguranca.db.UsuarioDBToplink;
//import br.com.rtools.utilitarios.DataHoje;
//import br.com.rtools.utilitarios.Moeda;
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Vector;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//import org.richfaces.component.html.HtmlDataTable;
//
//
//public class CaixaBancoJSFBean {
//    private Movimento movimento = new Movimento();
//    private Movimento movimento2 = new Movimento();
//    private Lote lote = new Lote();
//    private ContaRotina contaRotina = new ContaRotina();
//    private Historico historico = new Historico();
//    private List listaMov = new Vector<Movimento>();
//    private HtmlDataTable htmlTable;
//    private HtmlDataTable htmlTableLote;
//    private String caixaBanco = "1";
//    private String pagRec = "P";
//    private int combo = 0;
//    private int comboDoc = 0;
//    private int comboDoc2 = 0;
//    private String descPesquisa;
//    private String porPesquisa = "d";
//    private String comoPesquisa = "T";
//    private String descPesquisaPes;
//    private String porPesquisaPes;
//    private String comoPesquisaPes = "T";
//    private String menssagem = "";
//    private String valorP = "0";
//    private String valorC = "0";
//    private int coisa = 32473248;
//    private HtmlDataTable htmlTablePes;
//    private boolean retorno = false;
//
//    public boolean getRetorno(){
//        return retorno;
//    }
//
//    public void setRetorno(boolean retorno){
//        this.retorno = retorno;
//    }
//
//
//    public int getCoisa() {
//        return coisa;
//    }
//
//    public void setCoisa(int coisa) {
//        this.coisa = coisa;
//    }
//
//    public void setLote(Lote lote) {
//        this.lote = lote;
//    }
//
//    public Lote getLote() {
//        return lote;
//    }
//
//    public void setMenssagem(String menssagem) {
//        this.menssagem = menssagem;
//    }
//
//    public String getMenssagem() {
//        return menssagem;
//    }
//
//    public void setHistorico(Historico historico) {
//        this.historico = historico;
//    }
//
//    public Historico getHistorico() {
//        return historico;
//    }
//
//    public void setComoPesquisa(String comoPesquisa) {
//        this.comoPesquisa = comoPesquisa;
//    }
//
//    public String getComoPesquisa() {
//        return comoPesquisa;
//    }
//
//    public void setPorPesquisa(String porPesquisa) {
//        this.porPesquisa = porPesquisa;
//    }
//
//    public String getPorPesquisa() {
//        return porPesquisa;
//    }
//
//    public void setDescPesquisa(String descPesquisa) {
//        this.descPesquisa = descPesquisa;
//    }
//
//    public String getDescPesquisa() {
//        return descPesquisa;
//    }
//
//    public int getCombo(){
//        return combo;
//    }
//
//    public void setCombo(int combo) {
//        this.combo = combo;
//    }
//
//    public int getComboDoc(){
//        return comboDoc;
//    }
//
//    public void setComboDoc(int comboDoc) {
//        this.comboDoc = comboDoc;
//    }
//
//    public int getComboDoc2(){
//        return comboDoc2;
//    }
//
//    public void setComboDoc2(int comboDoc2) {
//        this.comboDoc2 = comboDoc2;
//    }
//
//    public CaixaBancoJSFBean(){
//        htmlTable = new HtmlDataTable();
//    }
//
//    public String getPagRec() {
//        return pagRec;
//    }
//
//    public void setPagRec(String pagRec) {
//        this.pagRec = pagRec;
//    }
//
//    public ContaRotina getContaRotina(){
//        return contaRotina;
//    }
//
//    public void setContaRotina(ContaRotina contaRotina){
//        this.contaRotina = contaRotina;
//    }
//
//    public String getCaixaBanco() {
//        return caixaBanco;
//    }
//
//    public void setCaixaBanco(String caixaBanco) {
//        this.caixaBanco = caixaBanco;
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
//    public Movimento getMovimento2() {
//        return movimento2;
//    }
//
//    public void setMovimento2(Movimento movimento2) {
//        this.movimento2 = movimento2;
//    }
//
//
//    public String getDescPesquisaPes() {
//        return descPesquisaPes;
//    }
//
//
//    public void setDescPesquisaPes(String descPesquisaPes) {
//        this.descPesquisaPes = descPesquisaPes;
//    }
//
//
//    public String getPorPesquisaPes() {
//        return porPesquisaPes;
//    }
//
//
//    public void setPorPesquisaPes(String porPesquisaPes) {
//        this.porPesquisaPes = porPesquisaPes;
//    }
//
//
//    public String getComoPesquisaPes() {
//        return comoPesquisaPes;
//    }
//
//
//    public void setComoPesquisaPes(String comoPesquisaPes) {
//        this.comoPesquisaPes = comoPesquisaPes;
//    }
//
//
//    public HtmlDataTable getHtmlTablePes() {
//        return htmlTablePes;
//    }
//
//
//    public void setHtmlTablePes(HtmlDataTable htmlTablePes) {
//        this.htmlTablePes = htmlTablePes;
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
//    public HtmlDataTable getHtmlTableLote() {
//        return htmlTableLote;
//    }
//
//    public void setHtmlTableLote(HtmlDataTable htmlTableLote) {
//        this.htmlTableLote = htmlTableLote;
//    }
//
//
//    public String salvar( Movimento movimento){
//        FinanceiroDB db = new FinanceiroDBToplink();
//        if (movimento.getId()==-1){
//            db.insert(movimento);
//        }
//        else{
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(movimento))
//            {db.getEntityManager().getTransaction().commit();}
//            else
//            {db.getEntityManager().getTransaction().rollback();}
//        }
//        return null;
//    }
//
//    public String salvarHistorico( Historico historico){
//        FinanceiroDB db = new FinanceiroDBToplink();
//        if (historico.getId()==-1){
//            db.insert(historico);
//        }
//        else{
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(historico))
//            {db.getEntityManager().getTransaction().commit();}
//            else
//            {db.getEntityManager().getTransaction().rollback();}
//        }
//        return null;
//    }
//
//    public String salvarCheque( Cheques cheque){
//        FinanceiroDB db = new FinanceiroDBToplink();
//        if (cheque.getId()==-1){
//            db.insert(cheque);
//        }
//        else{
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(cheque))
//            {db.getEntityManager().getTransaction().commit();}
//            else
//            {db.getEntityManager().getTransaction().rollback();}
//        }
//        return null;
//    }
//
//
//   public String novo(){
//       listaMov = new Vector<Movimento>();
//       historico = new Historico();
//       Movimento mov = new Movimento();
//       String swap = ((String) this.getListaComboPlano5P().get(0));
//       movimento = new Movimento();
//       movimento.getPlano5().setConta(swap);
//       swap = ((String) this.getListaComboPlano5().get(0));
//       movimento2 = new Movimento();
//       movimento2.getPlano5().setConta(swap);
//       lote = new Lote();
//       valorP = "0";
//       valorC = "0";
//       movimento.setNumero("");
//       return "cadMovimento";
//   }
//
//   public boolean excluir(Movimento movimento){
//        MovimentoDB db = new MovimentoDBToplink();
//        boolean a = false;
//        if (movimento.getId() !=-1){
//            db.getEntityManager().getTransaction().begin();
//            a = db.delete(db.pesquisaCodigo(movimento.getId()));
//            if (a){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//       return a;
//   }
//
//   public String excluirCheque(int id){
//        MovimentoDB db2 = new MovimentoDBToplink();
//        String a =        "";
//        if (id !=-1){
//            db2.getEntityManager().getTransaction().begin();
//            Cheques c = db2.pesquisaCheques(id);
//            a = db2.deleteChque(   c  );
//            if ( a.equals("ok")){
//                db2.getEntityManager().getTransaction().commit();
//            }else{
//                db2.getEntityManager().getTransaction().rollback();
//            }
//        }
//       return null;
//   }
//
//   public void excluirLote(int id){
//        LoteDB db = new LoteDBToplink();
//        boolean a = false;
//        if (id !=-1){
//            db.getEntityManager().getTransaction().begin();
//            Lote l = db.pesquisaCodigo(id);
//            a = db.delete(l);
//            if (a){
//                db.getEntityManager().getTransaction().commit();
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            }
//        }
//   }
//
//
//   public String excluirHistorico(int id){
//        MovimentoDB db2 = new MovimentoDBToplink();
//        FinanceiroDB db = new FinanceiroDBToplink();
//        String a = "";
//        if (id !=-1){
//            db2.getEntityManager().getTransaction().begin();
//            Historico c = db2.pesquisaHistorico(id);
//            a = db2.deleteHistorico(c);
//            if (a.equals("ok")){
//                db2.getEntityManager().getTransaction().commit();
//            }else{
//                db2.getEntityManager().getTransaction().rollback();
//            }
//        }
//       return null;
//   }
//
//   public String excluir(){
//        MovimentoDB db = new MovimentoDBToplink();
//        int i = 0;
//        if (db.pesquisaHistorico(movimento2.getId()) != null){
//            this.excluirHistorico(movimento2.getId());
//        }
//
//        if (movimento2.getId() != -1){
//            this.excluir(movimento2);
//        }
//
//        while (i < listaMov.size()){
//            movimento = ( (Movimento) listaMov.get(i) );
//            if (db.pesquisaCheques(movimento.getId()) != null)
//                this.excluirCheque(movimento.getId());
//            this.excluir(movimento);
//            i++;
//        }
//
//        this.excluirLote(lote.getId());
//
//        historico = new Historico();
//        movimento2 = new Movimento();
//        movimento = new Movimento();
//        listaMov = new Vector<Movimento>();
//        lote = new Lote();
//        valorP = "0";
//        valorC = "0";
//
//        return "cadCaixaBanco";
//   }
//
//   public String editar(){
//      movimento = (Movimento) getHtmlTable().getRowData();
//      listaMov.remove(getHtmlTable().getRowIndex());
//      valorP = Float.toString(movimento.getValor());
//      return "cadCaixaBanco";
//   }
//
//   public String eliminarDaList(){
//      MovimentoDB db = new MovimentoDBToplink();
//      if (db.pesquisaCheques(movimento.getId()) != null)
//          this.excluirCheque(movimento.getId());
//      this.excluir(movimento);
//      movimento = new Movimento();
//      valorP = "0";
//      return "cadCaixaBanco";
//   }
//
//
//   public String salvarTodos(){
//        FinanceiroDB dbF = new FinanceiroDBToplink();
//        FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
//        LoteDB dbLote = new LoteDBToplink();
//        MovimentoDB a = new MovimentoDBToplink();
//        Plano5DB dbPl5 = new Plano5DBToplink();
//        PessoaDB dbPes = new  PessoaDBToplink();
//        UsuarioDB dbU = new UsuarioDBToplink();
//        Movimento mov = new Movimento();
//        Movimento Cmov = new Movimento();
//        FilialDB dbFil = new FilialDBToplink();
//        Cheques cheques = new Cheques();
//        ContaRotinaDB rot = new ContaRotinaDBToplink();
//        Plano4 pl4 = new Plano4();
//        float soma = 0;
//        int j = 0;
//
//        if ((!(valorP.equals("0"))) || (!(valorC.equals("0")))){
//            movimento.setValor(Float.parseFloat(Moeda.substituiVirgula(valorP)));
//            movimento2.setValor(Float.parseFloat(Moeda.substituiVirgula(valorC)));
//        }
//
//        if (Float.parseFloat(Moeda.substituiVirgula(valorP)) != 0){
//            this.inserir();
//        }
//
//
//        while (j < listaMov.size()){
//            soma = Moeda.somaValores(soma , ((Movimento) listaMov.get(j)).getValor());
//            j++;
//        }
//
//
//        if (listaMov.size() != 0){
//
//            int i = 0;
//            mov = (Movimento) listaMov.get(i);
//            pl4 = rot.pesquisaPlano4PorDescricao("%" + ((String) this.getListaCombo().get(combo).getLabel()) + "%");
//                if ((Integer.parseInt(caixaBanco) != -1) && ((dbPl5.pesquisaPlano5PorDesc( ( (Movimento) listaMov.get(0)).getPlano5().getConta())) != null)){
//                Plano5 pl5 = (dbPl5.pesquisaPlano5PorDesc( ( (Movimento) listaMov.get(0)).getPlano5().getConta()));
//
//                if (rot.pesquisaContaRotina( pl5.getPlano4().getId() ,  Integer.parseInt(this.caixaBanco)).getRotina() != null){
//                    lote.setRotina( rot.pesquisaContaRotina( pl5.getPlano4().getId() ,  Integer.parseInt(this.caixaBanco)).getRotina() );
//                }else{
//                    return "cadCaixaBanco";
//                }
//            }else{
//                return "cadCaixaBanco";
//            }
//
//
//            lote.setPagRec(pagRec);
//            lote.setCompetencia(DataHoje.data());
//
//            this.inserirLote(lote);
//
//            movimento2.setDepartamento(null);
//            movimento2.setLoteBaixa(null);
//            movimento2.setFilial(dbFil.pesquisaCodigo(1));
//            movimento2.setVencimento(null);
//            movimento2.setAcordo(-1);
//
//            if (movimento2.getPessoa().getId() == -1){
//                movimento2.setPessoa(dbPes.pesquisaCodigo(0));
//            }else if (movimento2.getPessoa().getId() != 0 ){
//          //      movimento2.setNomePessoa(movimento2.getPessoa().getNome());
//            }
//
//
//            Cmov = movimento2;
//
//            if ((this.getListaTipoDocCPartida().get(comboDoc).getLabel()) != null)
//                Cmov.setFTipoDocumento(db.pesquisaTipoDocPorDesc("%" + ((String) this.getListaTipoDocCPartida().get(comboDoc).getLabel()) + "%"));
//            else
//                return "cadCaixaBanco";
//
//
//            Plano5 p5 = dbPl5.pesquisaPlano5PorDesc(movimento2.getPlano5().getConta(), pl4.getConta());
//            if (p5 != null){
//                Cmov.setPlano5(p5);
//            }else{
//                return "cadCaixaBanco";
//            }
//
//
//            Cmov.setLote(lote);
//            Cmov.setServicos(null);
//            Cmov.setTipoServico(null);
//
//            this.salvar(Cmov);
//
//
//            if ((Cmov.getId() != -1) && (!(historico.getHistorico().equals("")))){
//                historico.setMovimento(Cmov);
//                this.salvarHistorico(historico);
//            }
//
//
//            while (i != listaMov.size()){
//                mov = (Movimento) listaMov.get(i);
//                mov.setLote(lote);
//                mov.setServicos(null);
//                mov.setTipoServico(null);
//                if (mov.getPessoa().getId() == -1){
//                  //  mov.setNomePessoa(movimento2.getNomePessoa());
//                    mov.setPessoa(dbPes.pesquisaCodigo(0));
//                }else{
//                //    mov.setNomePessoa(movimento2.getPessoa().getNome());
//                }
//                this.salvar( mov );
//
//                if (mov.getId() != -1){
//                    if (mov.getFTipoDocumento().getTipoDocumento().equals("Cheque")){
//                        cheques.setDataCancelamento(null);
//                        cheques.setDataImpressao(null);
//                        cheques.setNumero(mov.getNumero());
//                        cheques.setMovimento(mov);
//                        this.salvarCheque(cheques);
//                    }
//                }
//                cheques = new Cheques();
//                i++;
//
//            }
//
//        }
//
//        this.novo();
//        return "cadCaixaBanco";
//    }
//
//
//   public String inserir(){
//      FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
//      Plano5DB dbPl5 = new Plano5DBToplink();
//      FilialDB dbFil = new FilialDBToplink();
//      PessoaDB dbPes = new  PessoaDBToplink();
//      UsuarioDB dbU = new UsuarioDBToplink();
//      int j = 0;
//      float soma = 0;
//
//      movimento.setValor(Float.parseFloat(Moeda.substituiVirgula(valorP)));
//      movimento2.setValor(Float.parseFloat(Moeda.substituiVirgula(valorC)));
//
//      if ((movimento.getValor() == 0) || (movimento2.getValor() == 0))
//          return "cadCaixaBanco";
//
//      if (listaMov.size() > 0){
//          while (j < listaMov.size()){
//              soma = Moeda.somaValores(soma , ((Movimento) listaMov.get(j)).getValor());
//              j++;
//          }
//          soma = soma + movimento.getValor();
//          if (soma > movimento2.getValor())
//              return "cadCaixaBanco";
//      }
//
//
//      if (this.getListaTipoDocPartida().size() != 0)
//          movimento.setFTipoDocumento(db.pesquisaTipoDocPorDesc("%" + ((String) this.getListaTipoDocPartida().get(comboDoc2).getLabel()) + "%"));
//
//
//      movimento.setPlano5(dbPl5.pesquisaPlano5PorDesc(movimento.getPlano5().getConta()));
//
//
////      if ((movimento2.getNomePessoa().equals("")) && (movimento.getPessoa().getId() == -1)){
// //         return "cadCaixaBanco";
//   //   }
//
//
//      if ((movimento.getFTipoDocumento() != null)&&
//           (!((movimento.getPlano5().getConta().equals(""))) || (!(movimento2.getPlano5().getConta().equals("")))) &&
//           (movimento.getPlano5() != null)){
//
//
//              movimento.setDepartamento(null);
//              movimento.setLoteBaixa(null);
//              movimento.setFilial(dbFil.pesquisaCodigo(1));
//              movimento.setVencimento(null);
//              movimento.setAcordo(-1);
//
//
//              if (pagRec.equals("P")){
//                  movimento2.setDebitoCredito("D");
//                  movimento.setDebitoCredito("C");
//              }else if (pagRec.equals("R")){
//                  movimento2.setDebitoCredito("C");
//                  movimento.setDebitoCredito("D");
//              }
//
//
//              listaMov.add(listaMov.size(), movimento);
//              movimento = new Movimento();
//              valorP = "0";
//
//      }
//
//        return "cadCaixaBanco";
//   }
//
//
//    public boolean getHabilitaPlano5(){
//        if ((movimento.getPlano5().getPlano4().getConta() == null) ||
//            (movimento.getPlano5().getPlano4().getConta().equals("")))
//            return true;
//        else
//            return false;
//    }
//
//
//    public void refreshForm(){
//    }
//
//
//    public List getListaComboPlano5P(){
//        List lista = null;
//        ContaRotinaDB db = new ContaRotinaDBToplink();
//        lista = db.pesquisaPlano5Partida(Integer.parseInt(caixaBanco));
//        if (lista != null)
//            return lista;
//        else{
//             lista = new Vector<String>();
//             lista.add(" ");
//            return lista;
//        }
//    }
//
//
//   public List getListaComboPlano5(){
//       List lista = null;
//       Plano4 pl4 = null;
//       ContaRotinaDB db = new ContaRotinaDBToplink();
//       if (this.getListaCombo().size() != 0){
//           pl4 = db.pesquisaPlano4PorDescricao("%" + ((String) this.getListaCombo().get(combo).getLabel()) + "%");
//           lista = db.pesquisaPlano5(pl4.getId()  ,  Integer.parseInt(caixaBanco) );
//       }
//       if (lista != null)
//           return lista;
//       else{
//           lista = new Vector<String>();
//           lista.add(" ");
//           return lista;
//       }
//   }
//
//
//   public List<SelectItem> getListaCombo(){
//       List<SelectItem> grupoConta = new Vector<SelectItem>();
//       int i = 0;
//       ContaRotinaDB db = new ContaRotinaDBToplink();
//       List select = null;
//       grupoConta  = new Vector<SelectItem>();
//       select = db.pesquisaPlano4Grupo(Integer.parseInt(caixaBanco) , pagRec);
//       while (i < select.size()){
//          grupoConta.add(new SelectItem(  new Integer(i),(String) select.get(i))   );
//          i++;
//       }
//
//       if (this.retorno){
//           i = 0;
//           while (i < grupoConta.size()){
//               if (grupoConta.get(i).getLabel().equals(movimento2.getPlano5().getPlano4().getConta())){
//                   this.combo = i;
//                   break;
//               }
//               i++;
//           }
//
//       }
//
//       return grupoConta;
//   }
//
//
//
//   public List<SelectItem> getListaTipoDocPartida(){
//       List<SelectItem> tipoDoc = new Vector<SelectItem>();
//       int i = 0;
//       FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
//       List select = null;
//       tipoDoc  = new Vector<SelectItem>();
//
//       if (pagRec.equals("P"))
//           if (caixaBanco.equals("1"))
//               select = db.pesquisaTodosStringsPartida("(3)");
//           else
//               select = db.pesquisaTodosStringsPartida("(4)");
//       else
//           select = db.pesquisaTodosStringsPartida("(3 , 4)");
//
//       while (i < select.size()){
//          tipoDoc.add(new SelectItem(  new Integer(i),(String) select.get(i))   );
//          i++;
//       }
//
//       if (this.retorno){
//           i = 0;
//           while (i < tipoDoc.size()){
//               if (tipoDoc.get(i).getLabel().equals(movimento.getFTipoDocumento().getTipoDocumento())){
//                   this.comboDoc2 = i;
//                   setRetorno(false);
//                   break;
//               }
//               i++;
//           }
//
//       }
//
//       return tipoDoc;
//   }
//
//
//   public List<SelectItem> getListaTipoDocCPartida(){
//       List<SelectItem> tipoDoc = new Vector<SelectItem>();
//       int i = 0;
//       FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
//       List select = null;
//       tipoDoc  = new Vector<SelectItem>();
//       select = db.pesquisaTodosStringsCPartida();
//       while (i < select.size()){
//          tipoDoc.add(new SelectItem(  new Integer(i),(String) select.get(i))   );
//          i++;
//       }
//
//       if (this.retorno){
//           i = 0;
//           while (i < tipoDoc.size()){
//               if (tipoDoc.get(i).getLabel().equals(movimento2.getFTipoDocumento().getTipoDocumento())){
//                   this.comboDoc = i;
//                   break;
//               }
//               i++;
//           }
//       }
//
//       return tipoDoc;
//   }
//
//
//    public List getPesquisaLote(){
//       List result = new Vector<Movimento>();
//       LoteDB db = new LoteDBToplink();
//       result = db.pesquisarLoteEsp(descPesquisa , porPesquisa, comoPesquisa);
//       return result;
//    }
//
//    public String retornarLote(){
//       Plano5DB dbPl5 = new Plano5DBToplink();
//       Plano5 pl5 = new Plano5();
//       ContaRotina c = new ContaRotina();
//       MovimentoDB mov = new MovimentoDBToplink();
//       ContaRotinaDB dbContaRotina = new ContaRotinaDBToplink();
//       lote = ((Lote) getHtmlTableLote().getRowData());
//       MovimentoDB db = new MovimentoDBToplink();
//       if (db.pesquisaPartidas(lote.getId()) != null){
//           this.listaMov =  db.pesquisaPartidas(lote.getId());
//           movimento = ( (Movimento) this.listaMov.get(0));
//       }
//       if(db.pesquisaContraPartida(lote.getId()) != null)
//            movimento2 = db.pesquisaContraPartida(lote.getId());
//       valorP = "0";
//       valorC = Float.toString(movimento2.getValor());
//       this.setRetorno(true);
//       if (dbPl5.pesquisaPlano5PorDesc(movimento.getPlano5().getConta()) != null){
//           pl5 = dbPl5.pesquisaPlano5PorDesc(movimento.getPlano5().getConta());
//           if (dbContaRotina.pesquisaContaRotina( pl5.getPlano4().getId() ,  lote.getRotina().getId() ) != null){
//               c = dbContaRotina.pesquisaContaRotina( pl5.getPlano4().getId() ,  lote.getRotina().getId() );
//               pagRec = lote.getPagRec();
//               caixaBanco = Integer.toString(c.getRotina().getId());
//               if (mov.pesquisaHistorico(movimento2.getId()) != null){
//                   historico = mov.pesquisaHistorico(movimento2.getId());
//               }
//           }
//       }
//       Movimento swap = movimento;
//       movimento = new Movimento();
//       movimento.getPlano5().setConta( swap.getPlano5().getConta() );
//       movimento.setPessoa( swap.getPessoa() );
//       return "cadCaixaBanco";
//   }
//
//
//   public String getLibPagRec(){
//       String result = "";
//       if (pagRec.equals("P")){
//           result = "Pagamento";
//       }else{
//           result = "Recebimento";
//       }
//       return result;
//   }
//
//    public String getDefHistorico(){
//        String h = "";
//        if ((this.getLibPagRec()).toString().equals("Pagamento"))
//            h += (this.getLibPagRec()).toString() + " a(o) " ;
//        else
//            h += (this.getLibPagRec()).toString() + " do(a) " ;
//        h += (this.getLabelPessoa()).toString() + " ";
//        if ((movimento2.getPessoa().getId() == 0) || (movimento2.getPessoa().getId() == -1)){
////            h += movimento2.getNomePessoa();
//        }else{
//            h += movimento2.getPessoa().getNome();
//        }
//        h += " , referente a " +
//        movimento2.getPlano5().getConta() + ", documento ";
//        if (((String) this.getListaTipoDocCPartida().get(comboDoc).getLabel()) != null)
//            h += (((String) this.getListaTipoDocCPartida().get(comboDoc).getLabel())) +
//                 ", número " + movimento2.getNumero() + ".";
//        historico.setHistorico(h.toUpperCase());
//        return historico.getHistorico();
//    }
//
//
//   public String getCabecalho(){
//       if (caixaBanco.equals("1")){
//           return "Caixa";
//       }else{
//           return "Banco";
//       }
//   }
//
//    public String getLabelPessoa(){
//        String result = "";
//        if (pagRec.equals("P"))
//            result = "Fornecedor";
//        else
//            result =  "Cliente";
//        return result;
//    }
//
//   public String limpar(){
//       movimento.setPessoa(new Pessoa());
//       movimento2.setPessoa(new Pessoa());
////       movimento.setNomePessoa("");
// //      movimento2.setNomePessoa("");
//       return "cadMovimento";
//   }
//
//
//    public boolean getHabilitaPessoa(){
//        if (movimento2.getPessoa().getId() == -1)
//            return false;
//        else
//            return true;
//    }
//
//
//    public String CarregarPessoa(){
//        PessoaDB db_pessoa = new PessoaDBToplink();
//        movimento.setPessoa( ( (Fisica) getHtmlTablePes().getRowData()).getPessoa());
//        movimento2.setPessoa( ( (Fisica) getHtmlTablePes().getRowData()).getPessoa());
////        movimento.setNomePessoa( ( (Fisica) getHtmlTablePes().getRowData()).getPessoa().getNome());
////        movimento2.setNomePessoa( ( (Fisica) getHtmlTablePes().getRowData()).getPessoa().getNome());
//        getDefHistorico();
//        return "cadCaixaBanco";
//    }
//
//    public List getListaMovimento(){
//        List lista = null;
//        lista = listaMov;
//        return lista;
//    }
//
//
//    public String inserirLote(Lote lote){
//        FinanceiroDB db = new FinanceiroDBToplink();
//        if (lote.getId()==-1){
//            db.insert(lote);
//        }
//        else{
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(lote))
//            {db.getEntityManager().getTransaction().commit();}
//            else
//            {db.getEntityManager().getTransaction().rollback();}
//        }
//        return null;
//    }
//
//    public String getUltimoCheque(){
//        ContaRotinaDB db = new ContaRotinaDBToplink();
//        Plano5DB dbp5 = new Plano5DBToplink();
//        Plano5 b = new Plano5();
//        ContaBanco c = new ContaBanco();
//        String numero = movimento.getNumero();
//        if (!(movimento.getPlano5().getConta().equals(""))){
//            if (dbp5.pesquisaPlano5PorDesc(movimento.getPlano5().getConta()) != null){
//                b = dbp5.pesquisaPlano5PorDesc(movimento.getPlano5().getConta());
//                if (dbp5.pesquisaUltimoCheque(b.getId()) != null){
//                    c = dbp5.pesquisaUltimoCheque(b.getId());
//                    numero =  Integer.toString(c.getUCheque() + 1);
//                }else
//                    numero = "0";
//            }
//        }
//        movimento.setNumero(numero);
//        return numero;
//    }
//
//
//    public String getValorDefCombPartida(){
//        String result = movimento.getPlano5().getConta();
//        if (this.retorno){
//            return result;
//        }else{
//            if (((String) this.getListaComboPlano5P().get(0)) != null){
//                movimento.getPlano5().setConta((String) this.getListaComboPlano5P().get(0));
//                result = movimento.getPlano5().getConta();
//            }
//            return result;
//        }
//    }
//
//    public String getValorDefCombCPartida(){
//       ContaRotinaDB db = new ContaRotinaDBToplink();
//       Plano4 pl4 = new Plano4();
//       pl4.setConta("");
//       if (((String) this.getListaCombo().get(combo).getLabel()) != null)
//          pl4 = db.pesquisaPlano4PorDescricao("%" + ((String) this.getListaCombo().get(combo).getLabel()) + "%");
//       String conta0 = "";
//       Plano5DB dbp5 = new Plano5DBToplink();
//       Plano5 pl5 = movimento2.getPlano5();
//       String result = pl5.getConta();
//       if (this.retorno){
//           return result;
//       }else{
//           if (movimento2.getPlano5().getConta().equals("")){
//               movimento2.getPlano5().setConta((String) this.getListaComboPlano5().get(0));
//               return movimento2.getPlano5().getConta();
//           }
//
//           if (this.getListaComboPlano5() != null){
//               conta0 = ( (String) this.getListaComboPlano5().get(0));
//               if ((dbp5.pesquisaPl4PorString(pl5.getConta(), pl4.getConta())) != null){
//                   if ((!(conta0.equals(  pl5.getConta()  ))) && ((dbp5.pesquisaPl4PorString(pl5.getConta(), pl4.getConta())).getConta().equals( ( (String)  pl4.getConta()) ))){
//                          result = pl5.getConta();
//                   }else{
//                         movimento2.getPlano5().setConta((String) this.getListaComboPlano5().get(0));
//                         result = movimento2.getPlano5().getConta();
//                   }
//               }else{
//                   movimento2.getPlano5().setConta((String) this.getListaComboPlano5().get(0));
//                   result = movimento2.getPlano5().getConta();
//               }
//           }
//           return result;
//       }
//    }
//
//    public String getValorDefCombFilial(){
//        String result = movimento.getFilial().getMatriz().getPessoa().getNome();
//        if (!this.retorno){
//            if (( (String) this.getListaFilial().get(0)) != null){
//                movimento.getFilial().getMatriz().getPessoa().setNome( (String) this.getListaFilial().get(0));
//                result = movimento.getFilial().getMatriz().getPessoa().getNome();
//            }
//        }
//        return result;
//    }
//
//    public List getListaFilial(){
//       List lista = null;
//       FilialDB db = new FilialDBToplink();
//       lista = db.pesquisaRazao(2000);
//       return lista;
//    }
//
//    public String getValorC() {
//        return Moeda.converteR$(valorC);
//    }
//
//    public String getValorP() {
//        return Moeda.converteR$(valorP);
//    }
//
//    public void refreshValorC(){
//      //  if (AnaliseString.conteudoValor(valorC))
//         //   getValorC();
//    }
//
//    public void refreshValorP(){
//      //  if (AnaliseString.conteudoValor(valorC))
//         //  getValorP();
//    }
//
//    public void setValorC(String valorC) {
//        this.valorC = valorC;
//    }
//
//    public void setValorP(String valorP) {
//        this.valorP = valorP;
//    }
//
//
//    public List getPesquisaPessoa(){
//       List result = null;
//       Pesquisa pesq = new Pesquisa();
//       result = pesq.pesquisar("Fisica","id",descPesquisaPes , porPesquisaPes, comoPesquisaPes);
//       return result;
//    }
//
//
//
//    public String getValores(){
//        if (pagRec.equals("P")){
//            return "valida(this.form.numCheque)";
//        }else{
//            return "cadCaixaBanco";
//        }
//    }
//
//    public boolean getHabilitaCheque(){
//        if (((String) this.getListaTipoDocPartida().get(comboDoc2).getLabel()).equals( "Dinheiro" ))
//            return true;
//        else
//            return false;
//    }
//    public String linkVoltar(){
//        if ( FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null){
//            return "menuFinanceiro";
//        }else
//            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//    }
//
//
//}
//
//
