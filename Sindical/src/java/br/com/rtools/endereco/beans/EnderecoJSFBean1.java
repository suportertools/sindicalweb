//package br.com.rtools.endereco.beans;
//
//import br.com.rtools.endereco.*;
//import br.com.rtools.endereco.db.*;
//import br.com.rtools.logSistema.NovoLog;
//import br.com.rtools.pessoa.Filial;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.pessoa.PessoaEndereco;
//import br.com.rtools.pessoa.db.FilialDB;
//import br.com.rtools.pessoa.db.FilialDBToplink;
//import br.com.rtools.pessoa.db.PessoaEnderecoDB;
//import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Vector;
//import javax.faces.context.FacesContext;
//import javax.faces.event.AjaxBehaviorEvent;
//import javax.faces.model.SelectItem;
//
//public class EnderecoJSFBean1 {
//    private List<Endereco> listaEndereco = new ArrayList();
//    private Endereco endereco = new Endereco();
//    private Endereco endTeste = new Endereco();
//    private String validacao = null;
//    private String msgConfirma;
//    private String msgDetalhada;
//    private Endereco pesquisa = new Endereco();
//    private String msgDescricao = "";
//    private String msgBairro = "";
//    private int cidade;
//    private int logradouro;
//    private int descricao;
//    private int idLogradouro = 2;
//    private int idIndex = -1;
//    public List<SelectItem> listaCidade = new ArrayList<SelectItem>();
//    private boolean renCadEndereco = false;
//    private boolean renCadDescEnd = false;
//    private boolean renCadBairro = false;
//    private boolean renNovoEndereco = false;
//    private boolean renTipoPesquisa = true;
//    private DescricaoEndereco novoDescEnd = new DescricaoEndereco();
//    private Bairro novoBairro = new Bairro();
//    private String porPesquisa = "";
//    
//    public EnderecoJSFBean1() {
//        
//    }
//
//    public Endereco getEndereco() {
//        if (endereco.getId() == -1){
//            endereco.setBairro(this.getBairroPesquisa());
//            endereco.setCidade(this.getCidadePesquisa());
//            endereco.setLogradouro(this.getLogradouroPesquisa());
//            endereco.setDescricaoEndereco(this.getDescricaoEnderecoPesquisa());
//        }else{
//            if (getBairroPesquisa().getId() != -1)
//                endereco.setBairro(this.getBairroPesquisa());
//
//            if (getCidadePesquisa().getId() != -1)
//                endereco.setCidade(this.getCidadePesquisa());
//
//            if (getLogradouroPesquisa().getId() != -1)
//                endereco.setLogradouro(this.getLogradouroPesquisa());
//
//            if (getDescricaoEnderecoPesquisa().getId() != -1)
//                endereco.setDescricaoEndereco(this.getDescricaoEnderecoPesquisa());
//        }
//        return endereco;
//    }
//
//    public void setEndereco(Endereco endereco) {
//        this.endereco = endereco;
//    }
//
//    public String getValidacao() {
//        return validacao;
//    }
//
//    public void setValidacao(String validacao) {
//        this.validacao = validacao;
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
//    public Endereco getPesquisa() {
//        return pesquisa;
//    }
//
//    public void setPesquisa(Endereco pesquisa) {
//        this.pesquisa = pesquisa;
//    }
//
//    public int getCidade() {
//        return cidade;
//    }
//
//    public void setCidade(int cidade) {
//        this.cidade = cidade;
//    }
//
//    public int getLogradouro() {
//        return logradouro;
//    }
//
//    public void setLogradouro(int logradouro) {
//        this.logradouro = logradouro;
//    }
//
//    public int getDescricao() {
//        return descricao;
//    }
//
//    public void setDescricao(int descricao) {
//        this.descricao = descricao;
//    }
//
//    public String salvar() {
//        msgConfirma = "";
//        EnderecoDB db = new EnderecoDBToplink();
//        LogradouroDB dbLog = new LogradouroDBToplink();
//        NovoLog log = new NovoLog();
//        endereco.setLogradouro(dbLog.pesquisaCodigo( Integer.parseInt(
//                              ( (SelectItem) getListaLogradouroEndereco().get(idLogradouro)).getDescription()
//                               )));
//        if (endereco.getDescricaoEndereco().getId() == -1){
//            msgConfirma = "O campo Descrição Endereço deve ser preenchido!";
//            return null;
//        }
//
//        if (endereco.getCidade().getId() == -1){
//            msgConfirma = "O campo Cidade deve ser preenchido!";
//            return null;
//        }
//
//        if (endereco.getBairro().getId() == -1){
//            msgConfirma = "O campo Bairro deve ser preenchido!";
//            return null;
//        }
//
//        if (endereco.getLogradouro() == null){
//            msgConfirma = "O campo Logradouro está inválido!";
//            return null;
//        }
//
//        if (endereco.getId() == -1){
//            String cep = "";
//            if (!endereco.getCep().equals("")){
//                cep = endereco.getCep().substring(0, 5);
//                cep = cep + endereco.getCep().substring(6, 9);
//                endereco.setCep(cep);
//            }
//            if( db.pesquisaEndereco(endereco.getDescricaoEndereco().getId(),
//                                   endereco.getCidade().getId(),
//                                   endereco.getBairro().getId(),
//                                   endereco.getLogradouro().getId()).isEmpty() ){
//                if(db.insert(endereco)){
//                    msgConfirma = "Endereço salvo com Sucesso!";
//                    log.novo("Novo registro", "Endereco inserido "+endereco.getId()+" - "+endereco.getLogradouro().getLogradouro()+" "+endereco.getDescricaoEndereco().getDescricaoEndereco()+", "+endereco.getFaixa()+" - "+endereco.getBairro().getBairro()+" ("+endereco.getBairro().getId()+") - "+endereco.getCidade().getCidade()+" ("+endereco.getCidade().getId()+") - "+endereco.getCidade().getUf());
//                }else{
//                    msgConfirma = "Erro ao Salvar!";
//                }
//            }else
//                msgConfirma = "Endereço já Existente no Sistema!";
//            novo();
//        }else{
//            Endereco e = new Endereco();
//            e = db.pesquisaCodigo(endereco.getId());
//            String antes = "De: "+e.getId()+" - "+e.getLogradouro().getLogradouro()+" "+e.getDescricaoEndereco().getDescricaoEndereco()+", "+e.getFaixa()+" - "+e.getBairro().getBairro()+" ("+e.getBairro().getId()+") "+e.getCidade().getCidade()+" ("+e.getCidade().getId()+") - "+e.getCidade().getUf();
//            endereco.setLogradouro(dbLog.pesquisaCodigo( Integer.parseInt(
//                                  ( (SelectItem) getListaLogradouroEndereco().get(idLogradouro)).getDescription()
//                                   )));
//            db.getEntityManager().getTransaction().begin();
//            if (db.update(endereco)) {
//                db.getEntityManager().getTransaction().commit();
//                log.novo("Atualizado", antes +" - para: "+endereco.getId()+" - "+endereco.getLogradouro().getLogradouro()+" "+endereco.getDescricaoEndereco().getDescricaoEndereco()+", "+endereco.getFaixa()+" - "+endereco.getBairro().getBairro()+" ("+endereco.getBairro().getId()+") "+endereco.getCidade().getCidade()+" ("+endereco.getCidade().getId()+") - "+endereco.getCidade().getUf());
//            } else {
//                db.getEntityManager().getTransaction().rollback();
//            }
//            msgConfirma = "Endereço atualizado com Sucesso!";
//        }
//
//        return null;
//    }
//
//    public String novo() {
//        endereco = new Endereco();
//        msgBairro = "";
//        msgDescricao = "";
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("bairroPesquisa");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("logradouroPesquisa");
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("descricaoEnderecoPesquisa");
//        setValidacao("");
//        return "endereco";
//    }
//
//    public String excluir() {
//        EnderecoDB db = new EnderecoDBToplink();
//        NovoLog log = new NovoLog();
//        if (endereco.getId() != -1) {
//            db.getEntityManager().getTransaction().begin();
//            endereco = db.pesquisaCodigo(endereco.getId());
//            if (db.delete(endereco)) {
//                db.getEntityManager().getTransaction().commit();
//                msgConfirma = "Endereço excluido com Sucesso!";
//                log.novo("Excluido", endereco.getId()+" - "+endereco.getLogradouro().getLogradouro()+" "+endereco.getDescricaoEndereco().getDescricaoEndereco()+", "+endereco.getFaixa()+" - "+endereco.getBairro().getBairro()+" ("+endereco.getBairro().getId()+") "+endereco.getCidade().getCidade()+" ("+endereco.getCidade().getId()+") - "+endereco.getCidade().getUf());
//            } else {
//                db.getEntityManager().getTransaction().rollback();
//                msgConfirma = "Endereço não pode ser excluido!";
//            }
//        }
//        return null;
//    }
//
//    public String editar() {
//       endereco = (Endereco) getListaEndereco().get(getIdIndex());
//       for(int i = 0; i < (getListaLogradouroEndereco().size()); i++){
//           if(Integer.valueOf( getListaLogradouroEndereco().get(i).getDescription() ) == endereco.getLogradouro().getId()){
//               idLogradouro = i;
//               break;
//           }
//       }
//       String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
//       if (url != null){
//           FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("enderecoPesquisa", endereco);
//           return url;
//       }
//       return "endereco";
//    }
//
//    public String pesquisarEndPessoaFisica(){
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "pessoaFisica");
//        return "pesquisaEndereco";
//    }
//
//    public void listaEnderecoPorCep() {
//        porPesquisa = "cep";
//        listaEndereco.clear();
//        
//    }
//
//    public void listaEnderecoPorDescParcial() {
//        porPesquisa = "parcial";
//        listaEndereco.clear();
//    }
//
//    public void listaEnderecoPorInicial() {
//        porPesquisa = "inicial";
//        listaEndereco.clear();
//    }
//
////    public String getRetorno(){
////        return pesquisa.getCidade().getUf() + " " + pesquisa.getCidade().getCidade()  + " " +  pesquisa.getDescricaoEndereco().getDescricaoEndereco()  + " " +  pesquisa.getLogradouro().getLogradouro();
////    }
//
//    public List<String> BuscaCidade(Object event) {
//        List<String> result = new Vector<String>();
//        String txtDigitado = event.toString().toLowerCase().toUpperCase();
//        CidadeDB db = new CidadeDBToplink();
//        String str = endereco.getCidade().getUf();
//        result = db.pesquisaCidade('%' + txtDigitado + '%', str);
//        return (result);
//    }
//
//    public List<String> BuscaBairro(Object event) {
//        List<String> result = new Vector<String>();
//        String txtDigitado = event.toString().toLowerCase().toUpperCase();
//        BairroDB db = new BairroDBToplink();
//        result = db.pesquisaBairro('%' + txtDigitado + '%');
//        return (result);
//    }
//
//    public List<String> BuscaLogradouro(Object event) {
//        List<String> result = new Vector<String>();
//        String txtDigitado = event.toString().toLowerCase().toUpperCase();
//        LogradouroDB db = new LogradouroDBToplink();
//        result = db.pesquisaLogradouro('%' + txtDigitado + '%');
//        return (result);
//    }
//
//    public List<String> BuscaDescricaoEndereco(Object event) {
//        List<String> result = new Vector<String>();
//        String txtDigitado = event.toString().toLowerCase().toUpperCase();
//        DescricaoEnderecoDB db = new DescricaoEnderecoDBToplink();
//        result = db.pesquisaDescricaoEndereco('%' + txtDigitado + '%');
//        return (result);
//    }
//
//    public Bairro getBairroPesquisa(){
//        try{
//            Bairro c = (Bairro) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("bairroPesquisa");
//            if (c == null){
//                c = new Bairro();
//            }
//            return c;
//        }catch(Exception e){
//            Bairro c = new Bairro();
//            return c;
//        }
//    }
//
//    public Cidade getCidadePesquisa(){
//        FilialDB dbFil = new FilialDBToplink();
//        PessoaEnderecoDB dbPes = new PessoaEnderecoDBToplink();
//        Filial fili = dbFil.pesquisaCodigo(1);
//        Pessoa pes = fili.getMatriz().getPessoa();
//        try{
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("uf", endereco.getCidade().getUf());
//            Cidade c = (Cidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa");
//            if (c == null){
//                c = ((PessoaEndereco) dbPes.pesquisaEndPorPessoa( pes.getId() ).get(0)).getEndereco().getCidade();
//            }
//            return c;
//        }catch(Exception e){
//            Cidade c = new Cidade();
//            return c;
//        }
//    }
//
//    public DescricaoEndereco getDescricaoEnderecoPesquisa(){
//        try{
//            DescricaoEndereco c = (DescricaoEndereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("descricaoEnderecoPesquisa");
//            if (c == null){
//                c = new DescricaoEndereco();
//            }
//            return c;
//        }catch(Exception e){
//            DescricaoEndereco c = new DescricaoEndereco();
//            return c;
//        }
//    }
//
//    public Logradouro getLogradouroPesquisa(){
//        try{
//            Logradouro c = (Logradouro) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("logradouroPesquisa");
//            if (c == null){
//                c = new Logradouro();
//            }
//            return c;
//        }catch(Exception e){
//            Logradouro c = new Logradouro();
//            return c;
//        }
//    }
//
//    public String pesquisarEnderecoAge(){
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "agenda");
//        return "pesquisaEndereco";
//    }
//
//    public void refreshForm(){
//    
//    }
//    
//    public void refreshFormx(AjaxBehaviorEvent event){
//        event.queue();
//    }
//
//    public List<SelectItem> getListaCidade(){
//        Cidade cid = null;
//        if (listaCidade.isEmpty()){
//            FilialDB dbFil = new FilialDBToplink();
//            PessoaEnderecoDB dbPes = new PessoaEnderecoDBToplink();
//            Filial fili = dbFil.pesquisaCodigo(1);
//            if (fili == null){
//                msgDetalhada = "Não existe filial, CRIE uma e "
//                             + " vincule o endereço para evitar futuros erros!";
//                return new ArrayList();
//            }
//            Pessoa pes = fili.getMatriz().getPessoa();
//            cid = ((PessoaEndereco) dbPes.pesquisaEndPorPessoa( pes.getId() ).get(0)).getEndereco().getCidade();
//            pesquisa.getCidade().setUf(cid.getUf());
//        }else{
//            listaCidade.clear();
//        }
//        CidadeDB db = new CidadeDBToplink();
//        int i = 0;
//        List select = db.pesquisaCidadeObj(pesquisa.getCidade().getUf());
//        while (i < select.size()){
//           listaCidade.add(
//                   new SelectItem(
//                       new Integer(i),
//                       (String) ((Cidade) select.get(i)).getCidade(),
//                       Integer.toString(((Cidade) select.get(i)).getId())
//                   ));
//           i++;
//        }
//
//        i = 0;
//
//        if (cid != null){
//            while (i < listaCidade.size()){
//                if(Integer.parseInt(listaCidade.get(i).getDescription()) == cid.getId())
//                    cidade = i;
//                i++;
//            }
//        }
//
//        return listaCidade;
//    }
//
//    public List<SelectItem> getListaLogradouro(){
//        List<SelectItem> log = new Vector<SelectItem>();
//        int i = 0;
//        LogradouroDB db = new LogradouroDBToplink();
//        List select = db.pesquisaTodosOrdenado();
//        while (i < select.size()){
//           log.add(new SelectItem(  new Integer(i), (String) ((Logradouro) select.get(i)).getLogradouro(), Integer.toString(((Logradouro) select.get(i)).getId()) ));
//           i++;
//        }
//        i = 0;
//
//        if (pesquisa.getLogradouro().getLogradouro().equals("")){
//            while (i < log.size()){
//                if(log.get(i).getLabel().equals("Rua"))
//                    logradouro = i;
//                i++;
//            }
//        }
//        return log;
//    }
//
//    public List<SelectItem> getListaLogradouroEndereco(){
//        List<SelectItem> log = new Vector<SelectItem>();
//        int i = 0;
//        int w = 0;
//        LogradouroDB db = new LogradouroDBToplink();
//        List select1 = db.pesquisaLogEndereco1();
//        List select2 = db.pesquisaLogEndereco2();
//        while (i < select1.size()){
//           log.add(new SelectItem(  new Integer(i), 
//                                 (String) ((Logradouro) select1.get(i)).getLogradouro(),
//                                 Integer.toString(((Logradouro) select1.get(i)).getId()) ));
//           i++;
//        }
//        log.add(new SelectItem(  new Integer(i), " ------------------------------- ", "-1" ));
//        i++;
//        while (w < select2.size()){
//           log.add(new SelectItem(  new Integer(i), 
//                                 (String) ((Logradouro) select2.get(w)).getLogradouro(),
//                                 Integer.toString(((Logradouro) select2.get(w)).getId()) ));
//           i++; w++;
//        }
//        return log;
//    }
//
//    public String salvaDescEndereco(){
//        DescricaoEnderecoDB db = new DescricaoEnderecoDBToplink();
//        if (novoDescEnd.getId() == -1){
//            if (!novoDescEnd.getDescricaoEndereco().equals("")){
//                if (db.pesquisaDescricaoEndereco(novoDescEnd.getDescricaoEndereco()).isEmpty()){
//                    if(db.insert(novoDescEnd)){
//                        renCadDescEnd = false;
//                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("descricaoEnderecoPesquisa", novoDescEnd);
//                        setMsgDescricao("");
//                    }else
//                        setMsgDescricao(" Erro ao Salvar!");
//                }else{
//                    setMsgDescricao(" Descrição já Existente!");
//                }
//            }
//            novoDescEnd = new DescricaoEndereco();
//        }
//        return "endereco";
//    }
//    
//    public String abrirDescEnd(){
//        renCadDescEnd = true;
//        return null;
//    }
//    
//    public String fecharDescEnd(){
//        renCadDescEnd = false;
//        novoDescEnd = new DescricaoEndereco();
//        msgDescricao = "";
//        return null;
//    }
//
//    public String abrirBairro(){
//        renCadBairro = true;
//        return null;
//    }
//
//    public String fecharBairro(){
//        renCadBairro = false;
//        novoBairro = new Bairro();
//        msgBairro = "";
//        return null;
//    }
//    
//    public String salvaBairro(){
//        BairroDB db = new BairroDBToplink();
//        if (novoBairro.getId() == -1){
//            if (!novoBairro.getBairro().equals("")){
//                if ((db.pesquisaBairro(novoBairro.getBairro())).isEmpty()){
//                    if(db.insert(novoBairro)){
//                        renCadBairro = false;
//                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bairroPesquisa", novoBairro);
//                        setMsgBairro("");
//                    }else{
//                        setMsgBairro(" Erro ao Salvar");
//                    }
//                }else{
//                    setMsgBairro(" Bairro já Existente");
//                }
//            }
//            novoBairro = new Bairro();
//        }
//        return "endereco";
//    }
//
//    public boolean getRenCadEndereco() {
//        if (listaEndereco.isEmpty())
//            renCadEndereco = true;
//        else
//            renCadEndereco = false;
//        return renCadEndereco;
//    }
//
//    public void setRenCadEndereco(Boolean renCadEndereco) {
//        this.renCadEndereco = renCadEndereco;
//    }
//
//    public boolean isRenCadDescEnd() {
//        return renCadDescEnd;
//    }
//
//    public void setRenCadDescEnd(boolean renCadDescEnd) {
//        this.renCadDescEnd = renCadDescEnd;
//    }
//
//    public DescricaoEndereco getNovoDescEnd() {
//        return novoDescEnd;
//    }
//
//    public void setNovoDescEnd(DescricaoEndereco novoDescEnd) {
//        this.novoDescEnd = novoDescEnd;
//    }
//
//    public boolean isRenCadBairro() {
//        return renCadBairro;
//    }
//
//    public void setRenCadBairro(boolean renCadBairro) {
//        this.renCadBairro = renCadBairro;
//    }
//
//    public Bairro getNovoBairro() {
//        return novoBairro;
//    }
//
//    public void setNovoBairro(Bairro novoBairro) {
//        this.novoBairro = novoBairro;
//    }
//
//    public int getIdLogradouro() {
//        return idLogradouro;
//    }
//
//    public void setIdLogradouro(int idLogradouro) {
//        this.idLogradouro = idLogradouro;
//    }
//
//    public boolean isRenNovoEndereco() {
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno").equals("endereco"))
//            renNovoEndereco = false;
//        else
//            renNovoEndereco = true;
//        return renNovoEndereco;
//    }
//
//    public void setRenNovoEndereco(boolean renNovoEndereco) {
//        this.renNovoEndereco = renNovoEndereco;
//    }
//
//    public String getMsgDescricao() {
//        return msgDescricao;
//    }
//
//    public void setMsgDescricao(String msgDescricao) {
//        this.msgDescricao = msgDescricao;
//    }
//
//    public String getMsgBairro() {
//        return msgBairro;
//    }
//
//    public void setMsgBairro(String msgBairro) {
//        this.msgBairro = msgBairro;
//    }
//
//    public String getMsgDetalhada() {
//        return msgDetalhada;
//    }
//
//    public void setMsgDetalhada(String msgDetalhada) {
//        this.msgDetalhada = msgDetalhada;
//    }
//
//    public List<Endereco> getListaEndereco() {
//        if (listaEndereco.isEmpty()){
//                EnderecoDB db = new EnderecoDBToplink();
//            if (porPesquisa.equals("cep")){
//                listaEndereco = db.pesquisaEnderecoCep(pesquisa.getCep());
//            }else if (porPesquisa.equals("inicial")){
//                CidadeDB cidDB = new CidadeDBToplink();
//                LogradouroDB logDB = new LogradouroDBToplink();
//                pesquisa.setCidade(cidDB.pesquisaCodigo( Integer.parseInt( ( (SelectItem) listaCidade.get(cidade) ).getDescription() ) ));
//                pesquisa.setLogradouro(logDB.pesquisaCodigo( Integer.parseInt( ( (SelectItem) this.getListaLogradouro().get(logradouro) ).getDescription() ) ));
//                listaEndereco = db.pesquisaEnderecoDes(pesquisa.getCidade().getUf(), pesquisa.getCidade().getCidade(), pesquisa.getLogradouro().getLogradouro(),  pesquisa.getDescricaoEndereco().getDescricaoEndereco(), "I");                
//            }else if (porPesquisa.equals("parcial")){
//                CidadeDB cidDB = new CidadeDBToplink();
//                LogradouroDB logDB = new LogradouroDBToplink();
//                pesquisa.setCidade(cidDB.pesquisaCodigo( Integer.parseInt( ( (SelectItem) listaCidade.get(cidade) ).getDescription() ) ));
//                pesquisa.setLogradouro(logDB.pesquisaCodigo( Integer.parseInt( ( (SelectItem) this.getListaLogradouro().get(logradouro) ).getDescription() ) ));
//                listaEndereco = db.pesquisaEnderecoDes(pesquisa.getCidade().getUf(), pesquisa.getCidade().getCidade(), pesquisa.getLogradouro().getLogradouro(),  pesquisa.getDescricaoEndereco().getDescricaoEndereco(), "P");
//            }
//        }
//        return listaEndereco;
//    }
//
//    public void setListaEndereco(List<Endereco> listaEndereco) {
//        this.listaEndereco = listaEndereco;
//    }
//
//    public boolean isRenTipoPesquisa() {
//        return renTipoPesquisa;
//    }
//
//    public void setRenTipoPesquisa(boolean renTipoPesquisa) {
//        this.renTipoPesquisa = renTipoPesquisa;
//    }
//
//    public int getIdIndex() {
//        return idIndex;
//    }
//
//    public void setIdIndex(int idIndex) {
//        this.idIndex = idIndex;
//    }
//
//    public Endereco getEndTeste() {
//        return endTeste;
//    }
//
//    public void setEndTeste(Endereco endTeste) {
//        this.endTeste = endTeste;
//    }
//}
