package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.LancamentoIndividualDB;
import br.com.rtools.associativo.db.LancamentoIndividualDBToplink;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.Guia;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.ServicoPessoaDB;
import br.com.rtools.financeiro.db.ServicoPessoaDBToplink;
import br.com.rtools.financeiro.db.ServicoRotinaDB;
import br.com.rtools.financeiro.db.ServicoRotinaDBToplink;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.db.FunctionsDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class LancamentoIndividualBean {
    private Fisica fisica = new Fisica();
    private PessoaComplemento pessoaComplemento = new PessoaComplemento();
    private final List<SelectItem> listaServicos = new ArrayList();
    private final List<SelectItem> listaTipoServico = new ArrayList();
    private List<SelectItem> listaJuridica = new ArrayList();
    private List<SelectItem> listaDiaVencimento = new ArrayList();
    private List<SelectItem> listaParcelas = new ArrayList();
    private int idServico = 0;
    private int idTipoServico = 0;
    private int idJuridica = 0;
    private int idDia = 1;
    private int idParcela = 0;
    private List<DataObject> listaMovimento = new ArrayList();
    private String cobrancaBancaria = "sim";
    private String entrada = "sim";
    private String descontoFolha = "nao";
    private String totalPagar = "";
    private Pessoa responsavel = new Pessoa();
    private Lote lote = new Lote();
    private ServicoPessoa servicoPessoa = new ServicoPessoa();
    private Servicos servicos = new Servicos();
    
    @PostConstruct
    public void init(){
        servicos = (Servicos)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.parseInt(getListaServicos().get(idServico).getDescription()), "Servicos"));
    }
    
    public void salvarData(){
        if (servicoPessoa.getId() != -1){
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            
            if (!sv.alterarObjeto(servicoPessoa)){
                // ERRO
                sv.desfazerTransacao();
            }else{
                sv.comitarTransacao();
            }
        }
    }       
    
    public void adicionarParcelas(){
        if (fisica.getId() == -1){
            GenericaMensagem.error("Erro", "Pesquise uma pessoa para gerar Parcelas");
            return;
        }
        
        if (responsavel.getId() == -1){
            GenericaMensagem.error("Erro", "Pesquise um Responsável");
            return;
        }
        
        if (listaServicos.isEmpty()){
            GenericaMensagem.error("Erro", "A lista de serviços não pode estar vazia!");
            return;
        }
        
        
        String vencto_ini = "";
        DataHoje dh = new DataHoje();
        listaMovimento.clear();
        
        if (entrada.equals("sim")){
            vencto_ini = DataHoje.data();
        }else{
            vencto_ini = dh.incrementarMeses(1, idDia +"/"+ DataHoje.data().substring(3));
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        int parcelas = idParcela;
        
        FTipoDocumento td = new FTipoDocumento();
        if (descontoFolha.equals("sim")){
            td = (FTipoDocumento)sv.pesquisaCodigo(13, "FTipoDocumento");
        }else{
            td = (FTipoDocumento)sv.pesquisaCodigo(2, "FTipoDocumento");
        }
        
        Servicos serv = (Servicos)sv.pesquisaCodigo(Integer.parseInt(listaServicos.get(idServico).getDescription()), "Servicos");
        TipoServico tipo_serv = (TipoServico) sv.pesquisaCodigo(Integer.parseInt(listaTipoServico.get(idTipoServico).getDescription()), "TipoServico");
        
        float totalpagar = Moeda.converteUS$(totalPagar);
        float valor = Moeda.converteFloatR$Float(Moeda.divisaoValores(totalpagar, parcelas));
        
        for (int i = 0; i < parcelas; i++){
            float valorswap = 0;
            //if ((Moeda.subtracaoValores(totalpagar, valor) != 0) && ( (i+1) == parcelas)) {
            if ( (i+1) == parcelas) {
                valor = totalpagar;
            } else {
                totalpagar = Moeda.subtracaoValores(totalpagar, valor);
            }
            
            
            listaMovimento.add(new DataObject(
                    new Movimento(
                    -1, 
                    new Lote(), 
                    serv.getPlano5(), 
                    responsavel, //fisica.getPessoa(),
                    serv, 
                    null, // BAIXA
                    tipo_serv, // TIPO SERVICO
                    null, // ACORDO
                    valor, // VALOR
                    DataHoje.data().substring(3), // REFERENCIA
                    vencto_ini, // VENCIMENTO
                    1, // QUANTIDADE
                    true, // ATIVO
                    "E", // ES
                    false, // OBRIGACAO
                    responsavel, // PESSOA TITULAR
                    fisica.getPessoa(), // PESSOA BENEFICIARIO
                    "", // DOCUMENTO
                    "", // NR_CTR_BOLETO
                    vencto_ini, // VENCIMENTO ORIGINAL
                    0, // DESCONTO ATE VENCIMENTO
                    0, // CORRECAO
                    0, // JUROS
                    0, // MULTA
                    0, // DESCONTO
                    0, // TAXA
                    0, // VALOR BAIXA
                    td, // FTipo_documento 13 - CARTEIRA, 2 - BOLETO
                    0, // REPASSE AUTOMATICO
                    null // MATRICULA SÓCIO
            ), 
                    Moeda.converteR$Float(Moeda.converteFloatR$Float(valor))
            ));
            if (cobrancaBancaria.equals("sim"))
                vencto_ini = (idDia < 10) ? "0" + idDia + dh.incrementarMeses(1, vencto_ini).substring(2) : idDia + dh.incrementarMeses(1, vencto_ini).substring(2);
            else
                vencto_ini = dh.incrementarMeses(1, vencto_ini);
        }
    }
    
    public String salvar(){
        // VERIFICA SE OS VALORES ESTÃO BATENDO
        float valor = 0;
        for (int i = 0; i < listaMovimento.size(); i++){
            valor = Moeda.somaValores(valor,  Moeda.converteUS$(listaMovimento.get(i).getArgumento1().toString()));
        }
        
        // VERIFICA VENCIMENTO  
        for (int i = 0; i < listaMovimento.size(); i++){
            if (listaMovimento.get(i).getArgumento1().toString().isEmpty()){
                GenericaMensagem.error("Atenção", "Data de Vencimento na lista inválida!");
                return null;
            }
        }
        
        if (Moeda.converteFloatR$Float(valor) !=  Moeda.converteUS$(totalPagar)){
            float valordif1 = Moeda.converteFloatR$Float(valor), valordif2 = Moeda.converteUS$(totalPagar);
            if (valordif1 > valordif2){
                GenericaMensagem.warn("Atenção", "O valor total da parcela foi MAIOR em R$ "+Moeda.converteR$Float(Moeda.subtracaoValores(valordif1, valordif2)));
            }else 
                GenericaMensagem.warn("Atenção", "O valor total da parcela foi MENOR em R$ "+Moeda.converteR$Float(Moeda.subtracaoValores(valordif2, valordif1)));
            
            //GenericaMensagem.warn("Atenção", "Os valores da parcela não corresponde ao Total do Serviço, verifique!");
            return null;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Pessoa empresaConveniada = null;
        
        if (listaJuridica.size() == 1 && listaJuridica.get(idJuridica).getDescription().equals("0")){
//            GenericaMensagem.warn("Atenção", "Empresa Conveniada não encontrada!");
//            return null;
        }else{
            empresaConveniada = ((Juridica) sv.pesquisaCodigo(Integer.valueOf(listaJuridica.get(idJuridica).getDescription()), "Juridica")).getPessoa();
        }
        
        
        // CODICAO DE PAGAMENTO
        CondicaoPagamento cp = new CondicaoPagamento();
        
        if (DataHoje.converteDataParaInteger(((Movimento)listaMovimento.get(0).getArgumento0()).getVencimento()) > DataHoje.converteDataParaInteger(DataHoje.data())){
            cp = (CondicaoPagamento)sv.pesquisaCodigo(2, "CondicaoPagamento");
        }else{
            cp = (CondicaoPagamento)sv.pesquisaCodigo(1, "CondicaoPagamento");
        }
        
        // TIPO DE DOCUMENTO  FTipo_documento 13 - CARTEIRA, 2 - BOLETO
        FTipoDocumento td = new FTipoDocumento();
        if (descontoFolha.equals("sim")){
            td = (FTipoDocumento)sv.pesquisaCodigo(13, "FTipoDocumento");
        }else{
            td = (FTipoDocumento)sv.pesquisaCodigo(2, "FTipoDocumento");
        }
        
        Servicos serv = (Servicos)sv.pesquisaCodigo(Integer.parseInt(listaServicos.get(idServico).getDescription()),"Servicos");
        
        lote.setEmissao(DataHoje.data());
        lote.setAvencerContabil(false);
        lote.setPagRec("R");
        lote.setValor(Moeda.converteUS$(totalPagar));
        lote.setFilial( serv.getFilial() );
        lote.setEvt(null);
        lote.setPessoa(responsavel);
        lote.setFTipoDocumento(td);
        lote.setRotina((Rotina) sv.pesquisaCodigo(131, "Rotina"));
        lote.setStatus((FStatus) sv.pesquisaCodigo(1, "FStatus"));
        lote.setPessoaSemCadastro(null);
        lote.setDepartamento(serv.getDepartamento());
        lote.setCondicaoPagamento(cp);
        lote.setPlano5(serv.getPlano5());
        lote.setDescontoFolha( descontoFolha.equals("sim") );
        
        sv.abrirTransacao();
        if (!sv.inserirObjeto(lote) ){
            GenericaMensagem.error("Atenção", "Erro ao salvar Lote!");
            sv.desfazerTransacao();
            return null;
        }
        
        if (pessoaComplemento.getId() == -1){
            pessoaComplemento.setCobrancaBancaria(true);
            pessoaComplemento.setNrDiaVencimento(idDia);
            pessoaComplemento.setPessoa(fisica.getPessoa());
            
            if (!sv.inserirObjeto(pessoaComplemento)){
                GenericaMensagem.error("Atenção", "Erro ao salvar Pessoa Complemento!");
                sv.desfazerTransacao();
                return null;
            }
        }
        for (int i = 0; i < listaMovimento.size(); i++){
            ((Movimento)listaMovimento.get(i).getArgumento0()).setLote(lote);
            if (!sv.inserirObjeto((Movimento)listaMovimento.get(i).getArgumento0())){
                GenericaMensagem.error("Atenção", "Erro ao salvar Movimento!");
                sv.desfazerTransacao();                
                return null;
            }
        }
        
        if (empresaConveniada != null){
            Guia guias = new Guia(
                    -1,
                    lote, 
                    empresaConveniada, 
                    null, 
                    false
            );

            if (!sv.inserirObjeto(guias)){
                GenericaMensagem.error("Atenção", "Erro ao salvar Guias!");
                sv.desfazerTransacao();
                return null;
            }
        }
        sv.comitarTransacao();
        GenericaMensagem.info("OK", "Lançamento efetuado com Sucesso!");
        return null;
    }
    
    public String novo(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("lancamentoIndividualBean");
        return "lancamentoIndividual";
    }
    
    public void pesquisaDescontoFolha(){
        responsavel = new Pessoa();
    }
    
    public void limpaEmpresaConvenio(){
        listaJuridica.clear();
        servicos = (Servicos)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.parseInt(listaServicos.get(idServico).getDescription()), "Servicos"));
        totalPagar = "0,00";
    }
    
    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            int i = 0;
            ServicoRotinaDB db = new ServicoRotinaDBToplink();
            List<Servicos> select = db.pesquisaTodosServicosComRotinas(131);
            if (!select.isEmpty()){
                while (i < select.size()) {
                    listaServicos.add(new SelectItem(i,
                                      select.get(i).getDescricao(),
                                      Integer.toString(select.get(i).getId())
                            ));
                    i++;
                }
            }else{
                listaServicos.add(new SelectItem(0, "Nenhum Serviço Encontrado", "0"));
            }
        }
        return listaServicos;
    }
    
    public List<SelectItem> getListaTipoServico() {
        if (listaTipoServico.isEmpty()) {
            List<TipoServico> result = new Dao().list(new TipoServico());
                    
            if (!result.isEmpty()){
                for(int i = 0; i < result.size(); i++){
                    listaTipoServico.add(new SelectItem(i,
                                      result.get(i).getDescricao(),
                                      Integer.toString(result.get(i).getId())
                            ));
                }
            }else{
                listaTipoServico.add(new SelectItem(0, "Nenhum Tipo Serviço", "0"));
            }
        }
        return listaTipoServico;
    }

    public Fisica getFisica() {
        if (GenericaSessao.exists("fisicaPesquisa")){
            fisica = (Fisica) GenericaSessao.getObject("fisicaPesquisa");
            
            Socios s = fisica.getPessoa().getSocios();
            LancamentoIndividualDB dbl = new LancamentoIndividualDBToplink();
            if (!dbl.listaSerasa(fisica.getPessoa().getId()).isEmpty()){
                GenericaMensagem.warn("PESSOA", fisica.getPessoa().getNome() + " contém o nome no Serasa!");
            }
            if (s != null && s.getId() != -1){
                // PESSOA ASSOCIADA
                retornaResponsavel(fisica.getPessoa().getId(), true);
            }else{
                // PESSOA NÁO ASSOCIADA
                retornaResponsavel(fisica.getPessoa().getId(), false);
            }
            
            PessoaDB db = new PessoaDBToplink();
            ServicoPessoaDB dbS = new ServicoPessoaDBToplink();
            
            servicoPessoa = dbS.pesquisaServicoPessoaPorPessoa(fisica.getPessoa().getId());
            if (servicoPessoa == null){
                servicoPessoa = new ServicoPessoa();
            }
            GenericaSessao.remove("fisicaPesquisa");
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }
    
    public Pessoa retornaResponsavel(Integer id_pessoa, boolean associada){
        if (associada){
            responsavel = new FunctionsDao().titularDaPessoa(id_pessoa);
        }else{
            if (GenericaSessao.exists("pessoaPesquisa"))
                responsavel = (Pessoa) GenericaSessao.getObject("pessoaPesquisa");
            else
                responsavel = (Pessoa) new Dao().find(new Pessoa(), id_pessoa);
            
            // RESPONSAVEL FISICA
            FisicaDB dbf = new FisicaDBToplink();
            Fisica fi = dbf.pesquisaFisicaPorPessoa(responsavel.getId());
            if (fi != null){
                DataHoje dh = new DataHoje();
                int idade = dh.calcularIdade(fi.getNascimento());
                if (idade < 18){
                    GenericaMensagem.warn("RESPONSÁVEL", responsavel.getNome() + " não é maior de idade!");
                    return responsavel = new Pessoa();
                }
            }
            else
            {
            // RESPONSAVEL JURIDICA
            // POR ENQUANTO NÃO FAZ NADA
                GenericaMensagem.warn("RESPONSÁVEL", "Pessoa Juridica não disponível no momento!");
                return responsavel = new Pessoa();
            }
        }
        
        Socios s = responsavel.getSocios();
        if (s != null && s.getId() != -1){
            if (responsavel.getId() != s.getMatriculaSocios().getTitular().getId()) {
                GenericaMensagem.warn("RESPONSÁVEL", responsavel.getNome() + " é um sócio dependente!");
                return responsavel = new Pessoa();
            }
        }
        
        // MENSAGEM SE POSSUI DÉBITOS
        if (new FunctionsDao().inadimplente(responsavel.getId())){
            GenericaMensagem.warn("RESPONSÁVEL", responsavel.getNome() + " possui débitos com o Sindicato!");
        }
        
        // ENDEREÇO OBRIGATÓRIO
        JuridicaDB dbj = new JuridicaDBToplink();
        List lista_pe = dbj.pesquisarPessoaEnderecoJuridica(responsavel.getId());
        if (lista_pe.isEmpty()){
            GenericaMensagem.warn("RESPONSÁVEL", responsavel.getNome() + " não possui endereço cadastrado!");
            return responsavel = new Pessoa();
        }
        
        // CADASTRO NO SERASA
        LancamentoIndividualDB dbl = new LancamentoIndividualDBToplink();
        if (!dbl.listaSerasa(responsavel.getId()).isEmpty()){
            GenericaMensagem.warn("PESSOA", responsavel.getNome()+ " contém o nome no Serasa!");
        }
        
        PessoaDB db = new PessoaDBToplink();
        pessoaComplemento = db.pesquisaPessoaComplementoPorPessoa(responsavel.getId());
        if (pessoaComplemento.getId() != -1){
            if (pessoaComplemento.isCobrancaBancaria()){
                cobrancaBancaria = "sim";
            }else
                cobrancaBancaria = "nao";
            idDia = pessoaComplemento.getNrDiaVencimento();
        }
        
        return responsavel;
    }

    public Pessoa getResponsavel() {
        if (GenericaSessao.exists("pessoaPesquisa")){
            Socios s = fisica.getPessoa().getSocios();
            retornaResponsavel(fisica.getPessoa().getId(), (s != null && s.getId() != -1));
            GenericaSessao.remove("pessoaPesquisa");
        }
        // NÃO APAGAR COMENTÁRIO
        /*
        JuridicaDB dbj = new JuridicaDBToplink();
        FisicaDB dbf = new FisicaDBToplink();
        LancamentoIndividualDB dbl = new LancamentoIndividualDBToplink();

        if (GenericaSessao.exists("pessoaPesquisa")){
            responsavel = (Pessoa) GenericaSessao.getObject("pessoaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
            
            Juridica jur = dbj.pesquisaJuridicaPorPessoa(responsavel.getId());
            
            // PESQUISA NA TABELA DO SERASA tanto pessoa fisica quanto juridica ----
            if (!dbl.listaSerasa(responsavel.getId()).isEmpty()){
                GenericaMensagem.warn("Atenção", "Esta pessoa contém o nome no Serasa, não poderá ser responsável!");
                return responsavel = new Pessoa();
            }
            
            // CASO SEJA PESSOA JURIDICA -------------------
            if (jur != null){
                // VERIFICA SE É CONTRIBUINTE --------------
                List contribuintes = dbl.pesquisaContribuinteLancamento(responsavel.getId());
                if (!contribuintes.isEmpty()){
                    GenericaMensagem.warn("Atenção", "Esta empresa foi fechada, não poderá ser responsável!");
                    return responsavel = new Pessoa();
                }
                
                // VERIFICA SE A EMPRESA CONTEM LISTA DE ENDERECO -------
                List lista_pe = dbj.pesquisarPessoaEnderecoJuridica(responsavel.getId());
                if (lista_pe.isEmpty()){
                    GenericaMensagem.warn("Atenção", "Esta empresa não possui endereço cadastrado, não poderá ser responsável!");
                    return responsavel = new Pessoa();
                }
                
                return responsavel = jur.getPessoa();
            }
            
            Fisica fi = dbf.pesquisaFisicaPorPessoa(responsavel.getId());
            
            // CASO SEJA PESSOA FISICA -------------------
            if (fi != null){
                // VERIFICA SE TEM MOVIMENTO EM ABERTO (DEVEDORES)
                List listam = dbl.pesquisaMovimentoFisica(responsavel.getId());
                if (!listam.isEmpty()){
                    GenericaMensagem.warn("Atenção", "Esta pessoa possui débitos com o Sindicato, não poderá ser responsável!");
                    // 817 NÃO BLOQUEAR PESSOAS DEVEDORAS
                    //return responsavel = new Pessoa();
                }
                
                
                SociosDB dbs = new SociosDBToplink();
                Socios soc = dbs.pesquisaSocioPorPessoaAtivo(responsavel.getId());
                
                // CASO NÃO SEJA SÓCIO ---
                if (soc.getId() == -1){
                    DataHoje dh = new DataHoje();
                    int idade = dh.calcularIdade(fi.getNascimento());
                    if (idade < 18){
                        GenericaMensagem.warn("Atenção", "Esta pessoa não é maior de idade, não poderá ser responsável!");
                        return responsavel = new Pessoa();
                    }
                }else{
                    FunctionsDao dbfunc = new FunctionsDao();
                    Pessoa p = dbfunc.titularDaPessoa(responsavel.getId());
                    // CASO SEJA SÓCIO NÃO TITULAR
                    if (p.getId() != responsavel.getId()){
                        // VERIFICA SE PESSOA É MAIOR DE IDADE
                        DataHoje dh = new DataHoje();
                        int idade = dh.calcularIdade(fi.getNascimento());
                        if (idade < 18){
                            GenericaMensagem.warn("Atenção", "Esta pessoa não é maior de idade, não poderá ser responsável!");
                            return responsavel = new Pessoa();
                        }
                    }                    
                }
                
                
                // VERIFICA SE A PESSOA CONTEM LISTA DE ENDERECO -------
                List lista_pe = dbj.pesquisarPessoaEnderecoJuridica(responsavel.getId());
                if (lista_pe.isEmpty()){
                    GenericaMensagem.warn("Atenção", "Esta pessoa não possui endereço cadastrado, não poderá ser responsável!");
                    return responsavel = new Pessoa();
                }
                
                return responsavel = fi.getPessoa();
            }
        }
        
        // CASO SEJA PESSOA FISICA -------------------
        if (fisica.getId() != -1 && responsavel.getId() == -1){
            
            List<Vector> result = dbl.pesquisaResponsavel(fisica.getPessoa().getId(), descontoFolha.equals("sim"));
            if (!result.isEmpty() && (Integer) result.get(0).get(0) != 0){
                // VERIFICA SE TEM MOVIMENTO EM ABERTO (DEVEDORES)
                List listam = dbl.pesquisaMovimentoFisica(fisica.getPessoa().getId());
                if (!listam.isEmpty()){
                    GenericaMensagem.warn("Atenção", "Esta pessoa possui débitos com o Sindicato, não poderá ser responsável!");
                    // 817 NÃO BLOQUEAR PESSOAS DEVEDORAS
                    //return responsavel = new Pessoa();
                }
                

                SociosDB dbs = new SociosDBToplink();
                Socios soc = dbs.pesquisaSocioPorPessoaAtivo(fisica.getPessoa().getId());
                
                // CASO NÃO SEJA SÓCIO ---
                if (soc.getId() == -1){
                    DataHoje dh = new DataHoje();
                    int idade = dh.calcularIdade(fisica.getNascimento());
                    if (idade < 18){
                        GenericaMensagem.warn("Atenção", "Esta pessoa não é maior de idade, não poderá ser responsável!");
                        return responsavel = new Pessoa();
                    }
                }else{
                    FunctionsDao dbfunc = new FunctionsDao();
                    Pessoa p = dbfunc.titularDaPessoa(fisica.getPessoa().getId());
                    // CASO SEJA SÓCIO NÃO TITULAR
                    if (p.getId() != fisica.getPessoa().getId()){
                        // VERIFICA SE PESSOA É MAIOR DE IDADE
                        DataHoje dh = new DataHoje();
                        int idade = dh.calcularIdade(fisica.getNascimento());
                        if (idade < 18){
                            GenericaMensagem.warn("Atenção", "Esta pessoa não é maior de idade, não poderá ser responsável!");
                            return responsavel = new Pessoa();
                        }
                    }                    
                }
                
                // VERIFICA SE A PESSOA CONTEM LISTA DE ENDERECO -------
                // NÃO NECESSÁRIAMENTE JURIDICA COMO ESTA NO NOME DO MÉTODO
                List lista_pe = dbj.pesquisarPessoaEnderecoJuridica(fisica.getPessoa().getId());
                if (lista_pe.isEmpty()){
                    GenericaMensagem.warn("Atenção", "Esta pessoa não possui endereço cadastrado, não poderá ser responsável!");
                    return responsavel = new Pessoa();
                }
                return responsavel = fisica.getPessoa();
            }else{
                GenericaMensagem.fatal("Atenção", "Responsável não encontrado, erro na função!");
            }
        }
        */
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }    
    
    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public List<DataObject> getListaMovimento() {
        for (int i = 0; i < listaMovimento.size(); i++){
            listaMovimento.get(i).setArgumento1( Moeda.converteR$(listaMovimento.get(i).getArgumento1().toString()) );
            ((Movimento)listaMovimento.get(i).getArgumento0()).setValor(
                    Moeda.converteUS$(Moeda.converteR$(listaMovimento.get(i).getArgumento1().toString()))
            );
        }
        return listaMovimento;
    }

    public void setListaMovimento(List<DataObject> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }

    public List<SelectItem> getListaJuridica() {
        if (listaJuridica.isEmpty() && !listaServicos.isEmpty()){
            LancamentoIndividualDB db = new LancamentoIndividualDBToplink();
            List<Juridica> result = db.listaEmpresaConveniada(Integer.parseInt(listaServicos.get(idServico).getDescription()));
            
            if (listaServicos.isEmpty() || result.isEmpty()){
                listaJuridica.add(new SelectItem(0, "Nenhuma Empresa Conveniada", "0"));
                return listaJuridica;
            }
            
            if(!result.isEmpty()){
                for (int i = 0; i < result.size(); i++){
                    listaJuridica.add(new SelectItem(i,
                                      result.get(i).getPessoa().getNome(),
                                      Integer.toString(result.get(i).getId())
                            ));
                }
            }
        }
        return listaJuridica;
    }

    public void setListaJuridica(List<SelectItem> listaJuridica) {
        this.listaJuridica = listaJuridica;
    }
    
    public List<SelectItem> getListaDiaVencimento() {
        if (listaDiaVencimento.isEmpty()) {
            for (int i = 1; i <= 31; i++) {
                listaDiaVencimento.add(new SelectItem(Integer.toString(i)));
            }
        }
        return listaDiaVencimento;
    }

    public void setListaDiaVencimento(List<SelectItem> listaDiaVencimento) {
        this.listaDiaVencimento = listaDiaVencimento;
    }    

    public int getIdDia() {
        return idDia;
    }

    public void setIdDia(int idDia) {
        this.idDia = idDia;
    }

    public String getCobrancaBancaria() {
        return cobrancaBancaria;
    }

    public void setCobrancaBancaria(String cobrancaBancaria) {
        this.cobrancaBancaria = cobrancaBancaria;
    }

    public String getEntrada() {
        return entrada;
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public String getTotalPagar() {
        if (fisica.getId() != -1 && !listaServicos.isEmpty()){
            LancamentoIndividualDB db = new LancamentoIndividualDBToplink();
            
            if (!listaServicos.get(idServico).getDescription().equals("0")){
                Servicos se = (Servicos)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.parseInt(listaServicos.get(idServico).getDescription()), "Servicos"));

                List<Vector> valor = db.pesquisaServicoValor(fisica.getPessoa().getId(), se.getId());
                float vl = Float.valueOf( ((Double)valor.get(0).get(0)).toString() );

                if (!se.isAlterarValor())
                    totalPagar = Moeda.converteR$Float(vl);
            }
        }
        return Moeda.converteR$(totalPagar);
    }

    public void setTotalPagar(String totalPagar) {
        this.totalPagar = totalPagar;
    }

    public int getIdJuridica() {
        return idJuridica;
    }

    public void setIdJuridica(int idJuridica) {
        this.idJuridica = idJuridica;
    }

    public String getDescontoFolha() {
        return descontoFolha;
    }

    public void setDescontoFolha(String descontoFolha) {
        this.descontoFolha = descontoFolha;
    }

    public List<SelectItem> getListaParcelas() {
         if (listaParcelas.isEmpty()) {
            for (int i = 1; i <= 24; i++) {
                listaParcelas.add(new SelectItem(Integer.toString(i)));
            }
        }
        return listaParcelas;
    }

    public void setListaParcelas(List<SelectItem> listaParcelas) {
        this.listaParcelas = listaParcelas;
    }

    public int getIdParcela() {
        return idParcela;
    }

    public void setIdParcela(int idParcela) {
        this.idParcela = idParcela;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public ServicoPessoa getServicoPessoa() {
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }

    public int getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(int idTipoServico) {
        this.idTipoServico = idTipoServico;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

}