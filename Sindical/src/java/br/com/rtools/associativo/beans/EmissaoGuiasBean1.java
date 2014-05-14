//package br.com.rtools.associativo.beans;
//
//import br.com.rtools.associativo.GrupoConvenio;
//import br.com.rtools.associativo.HistoricoEmissaoGuias;
//import br.com.rtools.associativo.SubGrupoConvenio;
//import br.com.rtools.associativo.db.ConvenioServicoDB;
//import br.com.rtools.associativo.db.ConvenioServicoDBToplink;
//import br.com.rtools.associativo.db.GrupoConvenioDB;
//import br.com.rtools.associativo.db.GrupoConvenioDBToplink;
//import br.com.rtools.associativo.db.LancamentoIndividualDB;
//import br.com.rtools.associativo.db.LancamentoIndividualDBToplink;
//import br.com.rtools.associativo.db.SubGrupoConvenioDB;
//import br.com.rtools.associativo.db.SubGrupoConvenioDBToplink;
//import br.com.rtools.financeiro.CondicaoPagamento;
//import br.com.rtools.financeiro.FStatus;
//import br.com.rtools.financeiro.FTipoDocumento;
//import br.com.rtools.financeiro.Guia;
//import br.com.rtools.financeiro.Lote;
//import br.com.rtools.financeiro.Movimento;
//import br.com.rtools.financeiro.Servicos;
//import br.com.rtools.financeiro.TipoServico;
//import br.com.rtools.financeiro.db.MovimentoDB;
//import br.com.rtools.financeiro.db.MovimentoDBToplink;
//import br.com.rtools.pessoa.Fisica;
//import br.com.rtools.pessoa.Juridica;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.pessoa.TipoDocumento;
//import br.com.rtools.pessoa.db.FisicaDB;
//import br.com.rtools.pessoa.db.FisicaDBToplink;
//import br.com.rtools.pessoa.db.PessoaDB;
//import br.com.rtools.pessoa.db.PessoaDBToplink;
//import br.com.rtools.seguranca.Rotina;
//import br.com.rtools.seguranca.Usuario;
//import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
//import br.com.rtools.utilitarios.DataHoje;
//import br.com.rtools.utilitarios.DataObject;
//import br.com.rtools.utilitarios.GenericaMensagem;
//import br.com.rtools.utilitarios.Moeda;
//import br.com.rtools.utilitarios.SalvarAcumuladoDB;
//import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Vector;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//
//@ManagedBean
//@SessionScoped
//public class EmissaoGuiasBean1 implements Serializable{
//    private Pessoa pessoa = new Pessoa();
//    private Fisica fisica = new Fisica();
//    private Lote lote = new Lote();
//    private int idGrupo = 0;
//    private int idSubGrupo = 0;
//    private int idServicos = 0;
//    private int quantidade = 1;
//    private int idJuridica = 0;
//    private final List<SelectItem> listaGrupo = new ArrayList<SelectItem>();
//    private final List<SelectItem> listaSubGrupo = new ArrayList<SelectItem>();
//    private final List<SelectItem> listaServicos = new ArrayList<SelectItem>();
//    private List<SelectItem> listaJuridica = new ArrayList<SelectItem>();
//    private List<Movimento> listaaux = new ArrayList();
//    private List<HistoricoEmissaoGuias> listah = new ArrayList();
//    private String valor = "";
//    private String desconto = "";
//    private String total = "";
//    private List<DataObject> listaMovimento = new ArrayList();
//    private String msgConfirma = "";
//    
//    private boolean validaok = false;
//    
//    public String baixarErrado(HistoricoEmissaoGuias heg){
//        if (!listah.isEmpty() ){
//            listaaux.clear();
//            
//            for(int i = 0; i < listah.size(); i++){
//                if (heg.getMovimento().getLote().getId() == listah.get(i).getMovimento().getLote().getId())
//                    listaaux.add(listah.get(i).getMovimento());
//            }
//            listah.clear();
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", listaaux);
//            return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
//        }
//        return null;
//    }
//    
//    public String excluirErrado(HistoricoEmissaoGuias heg){
//        if (!listah.isEmpty() ){
//            listaaux.clear();
//            
//            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//            MovimentoDB db = new MovimentoDBToplink();
//            sv.abrirTransacao();
//            
//            for(int i = 0; i < listah.size(); i++){
//                if (heg.getMovimento().getLote().getId() == listah.get(i).getMovimento().getLote().getId()){
//                    listaaux.add(listah.get(i).getMovimento());
//                    if (!sv.deletarObjeto(sv.pesquisaCodigo(listah.get(i).getId(), "HistoricoEmissaoGuias"))){
//                        sv.desfazerTransacao();
//                        return null;
//                    }
//                }
//            }
//            
//            // GUIA
//            Guia guias = db.pesquisaGuias(heg.getMovimento().getLote().getId());
//            
//            if (!sv.deletarObjeto(sv.pesquisaCodigo(guias.getId(), "Guia"))){
//                sv.desfazerTransacao();
//                return null;
//            }
//            
//            // MOVIMENTO
//            for (int i = 0; i < listaaux.size(); i++){
//                if (!sv.deletarObjeto(sv.pesquisaCodigo(listaaux.get(i).getId(), "Movimento"))){
//                    sv.desfazerTransacao();
//                    return null;    
//                }
//            }
//            
//            // LOTE
//            if (!sv.deletarObjeto(sv.pesquisaCodigo(heg.getMovimento().getLote().getId(), "Lote"))){
//                sv.desfazerTransacao();
//                return null;
//            }
//            sv.comitarTransacao();
//            
//            listah.clear();
//        }
//        return "menuPrincipal";
//    }
//    
//    public void atualizarHistorico(){
//        
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        
//        HistoricoEmissaoGuias heg = new HistoricoEmissaoGuias();
//        Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
//        MovimentoDB db = new MovimentoDBToplink();
//        
//        if (!listaaux.isEmpty()){
//            for (int i = 0; i < listaaux.size(); i++){
//                heg = db.pesquisaHistoricoEmissaoGuiasPorMovimento(usuario.getId(), listaaux.get(i).getId());
//                sv.abrirTransacao();
//                
//                heg.setBaixado(true);
//                sv.alterarObjeto(heg);
//                sv.comitarTransacao();
//            }   
//        }
//    }
//    
//    public List<HistoricoEmissaoGuias> getListah() {
//        if(listah.isEmpty())       {
//            MovimentoDB db = new MovimentoDBToplink();
//            Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");            
//            listah = db.pesquisaHistoricoEmissaoGuias(usuario.getId());
//        }
//        return listah;
//    }
//
//    public void setListah(List<HistoricoEmissaoGuias> listah) {
//        this.listah = listah;
//    }
//    
//    public void limpaGrupo(){     
//        listaSubGrupo.clear();
//        listaServicos.clear();
//        listaJuridica.clear();
//        idSubGrupo = 0;
//        idJuridica = 0;
//        idServicos = 0;
//    }
//    
//    public void limpaSubGrupo(){
//        listaSubGrupo.clear();
//        listaServicos.clear();
//        listaJuridica.clear();
//        idJuridica = 0;
//        idServicos = 0;
//    }
//    
//    public void pesquisaSemCadastro(String por){
//        PessoaDB db = new PessoaDBToplink();
//        FisicaDB dbf = new FisicaDBToplink();
//        
//        if (por.equals("cpf")){
//            if (!pessoa.getDocumento().isEmpty()){
//                List lista = dbf.pesquisaFisicaPorDoc(pessoa.getDocumento());
//                if (!lista.isEmpty()){
//                    msgConfirma = "Este CPF já esta cadastrado!";
//                    GenericaMensagem.warn("", msgConfirma);
//                    validaok = false;
//                    return;
//                }
//            }
//        }
//        
//        if (por.equals("rg")){
//            if (!fisica.getRg().isEmpty()){
//                List lista = dbf.pesquisaFisicaPorNomeNascRG("", null, fisica.getRg());
//                if (!lista.isEmpty()){
//                    msgConfirma = "Este RG já esta cadastrado!";
//                    GenericaMensagem.warn("", msgConfirma);
//                    validaok = false;
//                    return;
//                }
//            }
//        }
//        
//        if (por.equals("nome") || por.equals("nascimento")){
//            if (!pessoa.getNome().isEmpty() && !fisica.getNascimento().isEmpty()){
//                List lista = dbf.pesquisaFisicaPorNomeNascRG(pessoa.getNome(), fisica.getDtNascimento(), fisica.getRg());
//                if (!lista.isEmpty()){
//                    msgConfirma = "Esta pessoa já esta cadastrada em nosso sistema!";
//                    GenericaMensagem.warn("", msgConfirma);
//                    validaok = false;
//                    
//                    
//                    pessoa.setNome("");
//                    fisica.setDtNascimento(null);
//                    return;
//                }
//            }else{
//                validaok = false;
//                return;
//            }
//        }
//        
//        validaok = true;
//    }
//    
//    public void novaPessoa(){
//        if (pessoa.getId() == -1){
//            pessoa = new Pessoa();
//            fisica = new Fisica();
//        }
//    }
//    
//    public String novo(){
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("emissaoGuiasBean");
//        return "emissaoGuias";
//    }
//    
//    public void salvarSemCadastro(){
//        if (!validaok){
//            GenericaMensagem.fatal("Erro", "Verifique os dados antes de salvar!");
//            return;
//        }
//        
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        if (pessoa.getNome().isEmpty()){
//            msgConfirma = "Nome não pode estar vazio!";
//            return;
//        }
//        
//        pessoa.setTipoDocumento((TipoDocumento)sv.pesquisaCodigo(1, "TipoDocumento"));
//        fisica.setPessoa(pessoa);
//        
//        if (pessoa.getId() == -1 && fisica.getId() == -1){
//            sv.abrirTransacao();
//            if (!sv.inserirObjeto(pessoa)){
//                msgConfirma = "Falha ao salvar Pessoa!";
//                sv.desfazerTransacao();
//                return;
//            }
//            
//            if (!sv.inserirObjeto(fisica)){
//                msgConfirma = "Falha ao salvar Física!";
//                sv.desfazerTransacao();
//                return;
//            }
//            msgConfirma = "Pessoa salva com Sucesso!";
//            sv.comitarTransacao();
//        }
//    }
//    
//    public void adicionarServico(){
//        if (pessoa.getId() == -1){
//            GenericaMensagem.error("Erro", "Pesquise uma pessoa para gerar Parcelas");
//            return;
//        }
//        
//        if (listaServicos.isEmpty()){
//            GenericaMensagem.error("Erro", "A lista de serviços não pode estar vazia!");
//            return;
//        }
//        
//        DataHoje dh = new DataHoje();
//        String vencto_ini = dh.data();
//        //listaMovimento.clear();
//        
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//
//        FTipoDocumento td = new FTipoDocumento();
//        td = (FTipoDocumento)sv.pesquisaCodigo(2, "FTipoDocumento"); // FTipo_documento 13 - CARTEIRA, 2 - BOLETO
//        
//        
//        Servicos serv = (Servicos)sv.pesquisaCodigo(Integer.parseInt(listaServicos.get(idServicos).getDescription()),"Servicos");
//        
//        float valorx = Moeda.converteUS$(valor);
//        float descontox = Moeda.converteUS$(desconto);
//        
//        listaMovimento.add(new DataObject(
//                new Movimento(
//                -1, 
//                new Lote(), 
//                serv.getPlano5(), 
//                pessoa,
//                serv, 
//                null, // BAIXA
//                (TipoServico)sv.pesquisaCodigo(1, "TipoServico"), // TIPO SERVICO
//                null, // ACORDO
//                valorx, // VALOR
//                DataHoje.data().substring(3), // REFERENCIA
//                vencto_ini, // VENCIMENTO
//                quantidade, // QUANTIDADE
//                true, // ATIVO
//                "E", // ES
//                false, // OBRIGACAO
//                pessoa, // PESSOA TITULAR
//                pessoa, // PESSOA BENEFICIARIO
//                "", // DOCUMENTO
//                "", // NR_CTR_BOLETO
//                vencto_ini, // VENCIMENTO ORIGINAL
//                0, // DESCONTO ATE VENCIMENTO
//                0, // CORRECAO
//                0, // JUROS
//                0, // MULTA
//                0,//descontox, // DESCONTO
//                0, // TAXA
//                0,//Moeda.multiplicarValores(quantidade, Moeda.subtracaoValores(valorx, descontox)), // VALOR BAIXA
//                td, // FTipo_documento 13 - CARTEIRA, 2 - BOLETO
//                0 // REPASSE AUTOMATICO
//        ), 
//                Moeda.converteR$Float(Moeda.converteFloatR$Float(valorx)), // VALOR COM MASCARA
//                Moeda.converteR$Float(Moeda.converteFloatR$Float(descontox)), // DESCONTO COM MASCARA
//                Moeda.converteR$Float(Moeda.multiplicarValores(quantidade, Moeda.subtracaoValores(valorx, descontox))) // VALOR TOTAL QUANTIDADE * (VALOR-DESCONTO)
//        ));
//        total = "0";
//        for (int i = 0; i < listaMovimento.size(); i++){
//            String total_desconto = listaMovimento.get(i).getArgumento3().toString();
//            total = Moeda.converteR$Float(Moeda.somaValores(Moeda.converteUS$(total), Moeda.converteUS$(total_desconto)));
//        }
//    }    
//    
//    public String salvar(){
//        if (pessoa.getId() == -1){
//            msgConfirma = "Pesquise uma pessoa para gerar!";
//            return null;
//        }
//        
//        if (listaServicos.isEmpty()){
//            msgConfirma = "A lista de serviços não pode estar vazia!";
//            return null;
//        }
//        
//        if (listaMovimento.isEmpty()){
//            msgConfirma = "A lista de lançamento não pode estar vazia!";
//            return null;
//        }
//        
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        
//        // CODICAO DE PAGAMENTO
//        CondicaoPagamento cp = new CondicaoPagamento();
//        
//        if (DataHoje.converteDataParaInteger(((Movimento)listaMovimento.get(0).getArgumento0()).getVencimento()) > DataHoje.converteDataParaInteger(DataHoje.data())){
//            cp = (CondicaoPagamento)sv.pesquisaCodigo(2, "CondicaoPagamento");
//        }else{
//            cp = (CondicaoPagamento)sv.pesquisaCodigo(1, "CondicaoPagamento");
//        }
//        
//        // TIPO DE DOCUMENTO  FTipo_documento 13 - CARTEIRA, 2 - BOLETO
//        FTipoDocumento td = new FTipoDocumento();
//        td = (FTipoDocumento)sv.pesquisaCodigo(2, "FTipoDocumento");
//
//        
//        Servicos serv = (Servicos)sv.pesquisaCodigo(Integer.parseInt(listaServicos.get(idServicos).getDescription()),"Servicos");
//        
//        lote.setEmissao(DataHoje.data());
//        lote.setAvencerContabil(false);
//        lote.setPagRec("R");
//        lote.setValor(Moeda.converteUS$(total));
//        lote.setFilial( serv.getFilial() );
//        lote.setEvt(null);
//        lote.setPessoa(pessoa);
//        lote.setFTipoDocumento(td);
//        lote.setRotina((Rotina) sv.pesquisaCodigo(132, "Rotina"));
//        lote.setStatus((FStatus) sv.pesquisaCodigo(1, "FStatus"));
//        lote.setPessoaSemCadastro(null);
//        lote.setDepartamento(serv.getDepartamento());
//        lote.setCondicaoPagamento(cp);
//        lote.setPlano5(serv.getPlano5());
//        lote.setDescontoFolha( false );
//        
//        
//        sv.abrirTransacao();
//        if (!sv.inserirObjeto(lote) ){
//            msgConfirma = " Erro ao salvar Lote!";
//            sv.desfazerTransacao();
//            return null;
//        }
//
//        for (int i = 0; i < listaMovimento.size(); i++){
//            ((Movimento)listaMovimento.get(i).getArgumento0()).setLote(lote);
//            if (!sv.inserirObjeto((Movimento)listaMovimento.get(i).getArgumento0())){
//                msgConfirma = " Erro ao salvar Movimento!";
//                sv.desfazerTransacao();
//                return null;
//            }
//            //listaaux.add((Movimento)listaMovimento.get(i).getArgumento0());
//        }
//        
//        Guia guias = new Guia(
//                -1,
//                lote, 
//                pessoa, 
//                (SubGrupoConvenio)sv.pesquisaCodigo(Integer.parseInt(listaSubGrupo.get(idSubGrupo).getDescription()), "SubGrupoConvenio"),
//                false
//        );
//        
//        if (!sv.inserirObjeto(guias)){
//            msgConfirma = " Erro ao salvar Guias!";
//            sv.desfazerTransacao();
//            return null;
//        }
//        sv.comitarTransacao();
//        
//        Movimento movimento = new Movimento();
//        //List<Movimento> listaaux = new ArrayList();
//        float valorx = 0; Moeda.converteUS$(valor);
//        float descontox = 0; Moeda.converteUS$(desconto);
//        
//
//        for (int i = 0; i < listaMovimento.size(); i++){
//            HistoricoEmissaoGuias heg = new HistoricoEmissaoGuias();
//            movimento = (Movimento)sv.pesquisaCodigo( ((Movimento)listaMovimento.get(i).getArgumento0()).getId(), "Movimento");
//
//            descontox = Moeda.converteUS$(listaMovimento.get(i).getArgumento2().toString());
//            valorx = Moeda.converteUS$(listaMovimento.get(i).getArgumento3().toString());
//            
//            movimento.setMulta(((Movimento)listaMovimento.get(i).getArgumento0()).getMulta());
//            movimento.setJuros(((Movimento)listaMovimento.get(i).getArgumento0()).getJuros());
//            movimento.setCorrecao(((Movimento)listaMovimento.get(i).getArgumento0()).getCorrecao());
//            movimento.setDesconto(descontox);
//
//            //movimento.setValor( Float.valueOf( listaMovimento.get(i).getArgumento9().toString() ) );
//            movimento.setValor(((Movimento)listaMovimento.get(i).getArgumento0()).getValor());
//
//            // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
//            movimento.setValorBaixa(valorx);
//            
//            listaaux.add(movimento);
//            heg.setMovimento(movimento);
//            heg.setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
//            
//            sv.abrirTransacao();
//            if (!sv.inserirObjeto(heg))
//                sv.desfazerTransacao();
//            else
//                sv.comitarTransacao();
//        }
//        
//        if (!listaaux.isEmpty() ){
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", listaaux);
//            return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
//        }
//        
//        msgConfirma = " Lançamento efetuado com Sucesso!";
//        return null;
//    }    
//    
//    public String getValor() {
//        if (pessoa.getId() != -1){
//            LancamentoIndividualDB db = new LancamentoIndividualDBToplink();
//            List<Vector> valorx = db.pesquisaServicoValor(pessoa.getId(), Integer.parseInt(listaServicos.get(idServicos).getDescription()));
//            
//            float vl = Float.valueOf( ((Double)valorx.get(0).get(0)).toString() );
//            
//            valor = Moeda.converteR$Float(vl);
//        }
//        return Moeda.converteR$(valor);
//    }
//
//    public void setValor(String valor) {
//        this.valor = valor;
//    }
//
//    public List<SelectItem> getListaServico(){
//        if (listaServicos.isEmpty()){
//            ConvenioServicoDB db = new ConvenioServicoDBToplink();
//            List<Servicos> select = new ArrayList();
//            if(!listaGrupo.isEmpty() && !listaSubGrupo.isEmpty()){
//                select = db.pesquisaServicosSubGrupoConvenio(Integer.parseInt(listaSubGrupo.get(idSubGrupo).getDescription()) );
//            }
//            
//            for (int i = 0; i < select.size(); i++){
//                listaServicos.add(new SelectItem(new Integer(i),
//                                 select.get(i).getDescricao(),
//                                 Integer.toString(select.get(i).getId())  
//                ));
//                
//            }
//        }
//        return listaServicos;
//    }
//    
//    public Pessoa getPessoa() {
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null){
//            pessoa = (Pessoa)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
//            fisica = new Fisica();
//            FisicaDB db = new FisicaDBToplink();
//            
//            fisica = db.pesquisaFisicaPorPessoa(pessoa.getId());
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
//        }
//        return pessoa;
//    }
//
//    public void setPessoa(Pessoa pessoa) {
//        this.pessoa = pessoa;
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
//
//    public List<SelectItem> getListaGrupo(){
//        if (listaGrupo.isEmpty()){
//            GrupoConvenioDB db = new GrupoConvenioDBToplink();
//            List<GrupoConvenio> select = db.pesquisaTodos();
//            for (int i = 0; i < select.size(); i++){
//                listaGrupo.add(new SelectItem(new Integer(i),
//                              (String) select.get(i).getDescricao(),
//                              Integer.toString(select.get(i).getId())  
//                ));
//            }
//        }
//        return listaGrupo;
//    }
//
//    public List<SelectItem> getListaSubGrupo(){
//        if (listaSubGrupo.isEmpty()){
//            SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
//            List<SubGrupoConvenio> select = new ArrayList();
//            
//            if(!listaGrupo.isEmpty()){
//                select = db.listaSubGrupoConvenioPorGrupo(Integer.parseInt(listaGrupo.get(idGrupo).getDescription()));
//            }
//            
//            for (int i = 0; i < select.size(); i++){
//                listaSubGrupo.add(new SelectItem(new Integer(i),
//                                  select.get(i).getDescricao(),
//                                  Integer.toString(select.get(i).getId())  
//                ));
//            }
//        }
//        return listaSubGrupo;
//    }    
//
//    public int getIdServicos() {
//        return idServicos;
//    }
//
//    public void setIdServicos(int idServicos) {
//        this.idServicos = idServicos;
//    }
//
//    public String getDesconto() {
//        if (desconto.isEmpty()) {
//            desconto = "0";
//        }
//        return Moeda.converteR$(desconto);
//    }
//
//    public void setDesconto(String desconto) {
//        if (desconto.isEmpty()) {
//            desconto = "0";
//        }
//        this.desconto = Moeda.substituiVirgula(desconto);
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
//    public List<DataObject> getListaMovimento() {
//        for (int i = 0; i < listaMovimento.size(); i++){
//            listaMovimento.get(i).setArgumento1( Moeda.converteR$(listaMovimento.get(i).getArgumento1().toString()) );
//            ((Movimento)listaMovimento.get(i).getArgumento0()).setValor(
//                    Moeda.converteUS$(Moeda.converteR$(listaMovimento.get(i).getArgumento1().toString()))
//            );
//        }
//        return listaMovimento;
//    }
//
//    public void setListaMovimento(List<DataObject> listaMovimento) {
//        this.listaMovimento = listaMovimento;
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
//    public List<SelectItem> getListaJuridica() {
//        if (listaJuridica.isEmpty()){
//            LancamentoIndividualDB db = new LancamentoIndividualDBToplink();
//            if (listaSubGrupo.isEmpty())
//                return listaJuridica;
//            
//            List<Juridica> result = db.listaEmpresaConveniadaPorSubGrupo(Integer.parseInt(listaSubGrupo.get(idSubGrupo).getDescription()));
//            
//            for (int i = 0; i < result.size(); i++){
//                listaJuridica.add(new SelectItem(new Integer(i),
//                                  result.get(i).getPessoa().getNome(),
//                                  Integer.toString(result.get(i).getId())
//                        ));
//            }
//        }
//        return listaJuridica;
//    }
//
//    public void setListaJuridica(List<SelectItem> listaJuridica) {
//        this.listaJuridica = listaJuridica;
//    }
//
//    public int getIdJuridica() {
//        return idJuridica;
//    }
//
//    public void setIdJuridica(int idJuridica) {
//        this.idJuridica = idJuridica;
//    }
//
//    public Fisica getFisica() {
//        return fisica;
//    }
//
//    public void setFisica(Fisica fisica) {
//        this.fisica = fisica;
//    }
//    
//    public String getTotal() {
//        if (total.isEmpty()) {
//            total = "0";
//        }
//        return Moeda.converteR$(total);
//    }
//
//    public void setTotal(String total) {
//        if (total.isEmpty()) {
//            total = "0";
//        }
//        this.total = Moeda.substituiVirgula(total);
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
//    
//}
