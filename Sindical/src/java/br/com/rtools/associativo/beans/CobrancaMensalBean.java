package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.db.CobrancaMensalDB;
import br.com.rtools.associativo.db.CobrancaMensalDBToplink;
import br.com.rtools.associativo.db.LancamentoIndividualDB;
import br.com.rtools.associativo.db.LancamentoIndividualDBToplink;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicoRotinaDB;
import br.com.rtools.financeiro.db.ServicoRotinaDBToplink;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.db.FunctionsDB;
import br.com.rtools.utilitarios.db.FunctionsDao;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class CobrancaMensalBean {
    private ServicoPessoa servicoPessoa = new ServicoPessoa();
    private final List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private int idServicos = 0;
    private List<ServicoPessoa> listaCobrancaMensal = new ArrayList<ServicoPessoa>();
    private float valorCorrige = 0;
    private String valorFixo = "0,00";
    private Servicos servicos = new Servicos();
    private String descFiltro = "";
    private String tipoFiltro = "beneficiario";
    
    public void novo(){
        GenericaSessao.put("cobrancaMensalBean", new CobrancaMensalBean());
    }
    
    public void salvar(){
        if (servicoPessoa.getPessoa().getId() == -1){
            GenericaMensagem.warn("Atenção", "Pesquise um Beneficiário para esta cobrança!");
            return;
        }
        
        if (servicoPessoa.getCobranca().getId() == -1){
            GenericaMensagem.warn("Atenção", "Pesquise um Responsável para esta cobrança!");
            return;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        CobrancaMensalDB db = new CobrancaMensalDBToplink();
        
        if (servicoPessoa.getId() == -1){
            if (!db.listaCobrancaMensalServico( servicoPessoa.getPessoa().getId(), servicos.getId() ).isEmpty()){
                GenericaMensagem.warn("Atenção", "Serviço para esse Beneficiário já existe!");
                return;
            }
        }else{
            if ( servicoPessoa.getServicos().getId() !=  servicos.getId() ){
                if (!db.listaCobrancaMensalServico( servicoPessoa.getPessoa().getId(), servicos.getId() ).isEmpty()){
                    GenericaMensagem.warn("Atenção", "Serviço para esse Beneficiário já existe!");
                    return;
                }
            }
        }
        
        servicoPessoa.setTipoDocumento((FTipoDocumento)sv.pesquisaCodigo(13, "FTipoDocumento"));
        servicoPessoa.setServicos(servicos);
        servicoPessoa.setNrValorFixo(Moeda.converteUS$(valorFixo));
        
        sv.abrirTransacao();
        if (servicoPessoa.getId() == -1){
            if (!sv.inserirObjeto(servicoPessoa)){
                GenericaMensagem.error("Erro", "Não foi possível salvar Cobrança Mensal!");
                sv.desfazerTransacao();
                return;
            }
            GenericaMensagem.info("Sucesso", "Cobrança Mensal salva!");
        }else{
            if (!sv.alterarObjeto(servicoPessoa)){
                GenericaMensagem.error("Erro", "Não foi possível atualizar Cobrança Mensal!");
                sv.desfazerTransacao();
                return;
            }
            GenericaMensagem.info("Sucesso", "Cobrança Mensal atualizada!");
        }
        servicoPessoa = new ServicoPessoa();
        listaCobrancaMensal.clear();
        sv.comitarTransacao();
    }
    
    public void excluir(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        if (!sv.deletarObjeto( sv.pesquisaCodigo(servicoPessoa.getId(), "ServicoPessoa") )){
            GenericaMensagem.error("Erro", "Não foi possível Excluir Cobrança Mensal!");
            sv.desfazerTransacao();
            return;
        }
        sv.comitarTransacao();
        
        GenericaMensagem.info("Sucesso", "Cobrança Mensal excluída!");
        servicoPessoa = new ServicoPessoa();
        listaCobrancaMensal.clear();
    }
    
    public void editar(ServicoPessoa linha){
        servicoPessoa = linha;
        
        for (int i = 0; i < listaServicos.size(); i++){
            if ( Integer.valueOf(listaServicos.get(i).getDescription()) == servicoPessoa.getServicos().getId() ){
                idServicos = i;
            }
        }
        valorFixo = Moeda.converteR$Float(servicoPessoa.getNrValorFixo());
    }
    
    public void atualizarGrid(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        for (int i = 0; i < listaCobrancaMensal.size(); i++){
            float calculo = 0;
            
            calculo = Moeda.divisaoValores(Moeda.multiplicarValores(listaCobrancaMensal.get(i).getNrValorFixo(), valorCorrige), 100);
            
            calculo = Moeda.subtracaoValores(listaCobrancaMensal.get(i).getNrValorFixo(), calculo);
            
            listaCobrancaMensal.get(i).setNrValorFixo( calculo );
            
            ServicoPessoa sp = (ServicoPessoa)sv.pesquisaCodigo(listaCobrancaMensal.get(i).getId(), "ServicoPessoa");
            sp.setNrValorFixo(calculo);
            
            if (!sv.alterarObjeto(sp)){
                sv.desfazerTransacao();
                GenericaMensagem.error("Erro", "Não foi possível alterar porcentagem!");
                return;
            }
        }
        
        sv.comitarTransacao();
        GenericaMensagem.info("Sucesso", "Todos Valores foram Atualizados!");
        //listaCobrancaMensal.clear();
        
        //CobrancaMensalDB db = new CobrancaMensalDBToplink();
        //listaCobrancaMensal = db.listaCobrancaMensalFiltro(tipoFiltro, descFiltro);
    }
    
    public void filtrar(){
        listaCobrancaMensal.clear();
        
        CobrancaMensalDB db = new CobrancaMensalDBToplink();
        listaCobrancaMensal = db.listaCobrancaMensalFiltro(tipoFiltro, descFiltro);
    }
    
    public ServicoPessoa getServicoPessoa() {
        if (GenericaSessao.getObject("fisicaPesquisa") != null || GenericaSessao.getObject("pessoaPesquisa") != null){
            if (GenericaSessao.getObject("fisicaPesquisa") != null){
                servicoPessoa.setPessoa( ((Fisica)GenericaSessao.getObject("fisicaPesquisa")).getPessoa() );
                GenericaSessao.remove("fisicaPesquisa");
            }
            
            FunctionsDB fc = new FunctionsDao();
            
            int id_resp = -1; 
            
            if (GenericaSessao.getObject("pessoaPesquisa") == null){
                id_resp = fc.responsavel(servicoPessoa.getPessoa().getId(), false);
                if (id_resp == -1){
                    return servicoPessoa;
                }
            }else{
                id_resp = ((Pessoa)GenericaSessao.getObject("pessoaPesquisa")).getId();
                GenericaSessao.remove("pessoaPesquisa");
            }
            
            JuridicaDB dbj = new JuridicaDBToplink();
            FisicaDB dbf = new FisicaDBToplink();
            LancamentoIndividualDB dbl = new LancamentoIndividualDBToplink();
            
            Juridica jur = dbj.pesquisaJuridicaPorPessoa(id_resp);
            
            // PESQUISA NA TABELA DO SERASA tanto pessoa fisica quanto juridica ----
            if (!dbl.listaSerasa(id_resp).isEmpty()){
                GenericaMensagem.error("Erro", "Esta pessoa contém o nome no Serasa, não poderá ser responsável!");
                return servicoPessoa;
            }
            
            // CASO SEJA PESSOA JURIDICA -------------------
            if (jur != null){
                // VERIFICA SE É CONTRIBUINTE --------------
                List contribuintes = dbl.pesquisaContribuinteLancamento(jur.getPessoa().getId());
                if (!contribuintes.isEmpty()){
                    GenericaMensagem.error("Erro", "Esta empresa foi fechada, não poderá ser responsável!");
                    return servicoPessoa;
                }
                
                // VERIFICA SE A EMPRESA CONTEM LISTA DE ENDERECO -------
                List lista_pe = dbj.pesquisarPessoaEnderecoJuridica(jur.getPessoa().getId());
                if (lista_pe.isEmpty()){
                    GenericaMensagem.error("Erro", "Esta empresa não possui endereço cadastrado, não poderá ser responsável!");
                    return servicoPessoa;
                }
                
                servicoPessoa.setCobranca(jur.getPessoa());
            }
            
            Fisica fi = dbf.pesquisaFisicaPorPessoa(id_resp);
            
            // CASO SEJA PESSOA FISICA -------------------
            if (fi != null){
                // VERIFICA SE TEM MOVIMENTO EM ABERTO (DEVEDORES)
                List listam = dbl.pesquisaMovimentoFisica(fi.getPessoa().getId());
                if (!listam.isEmpty()){
                    GenericaMensagem.error("Erro", "Esta pessoa possui débitos com o Sindicato, não poderá ser responsável!");
                    return servicoPessoa;
                }
                
                // VERIFICA SE PESSOA É MAIOR DE IDADE
                DataHoje dh = new DataHoje();
                int idade = dh.calcularIdade(fi.getNascimento());
                if (idade < 18){
                    GenericaMensagem.error("Erro", "Esta pessoa não é maior de idade, não poderá ser responsável!");
                    return servicoPessoa;
                }
                
                // VERIFICA SE A PESSOA CONTEM LISTA DE ENDERECO -------
                List lista_pe = dbj.pesquisarPessoaEnderecoJuridica(fi.getPessoa().getId());
                if (lista_pe.isEmpty()){
                    GenericaMensagem.error("Erro", "Esta pessoa não possui endereço cadastrado, não poderá ser responsável!");
                    return servicoPessoa;
                }
                servicoPessoa.setCobranca(fi.getPessoa());
            }
        }
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            int i = 0;
            ServicoRotinaDB db = new ServicoRotinaDBToplink();
            List<Servicos> select = db.listaServicosNotIn("120, 121, 122, 151");
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
        }else{
            servicos = (Servicos) new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaServicos.get(idServicos).getDescription()), "Servicos");
        }
        return listaServicos;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public List<ServicoPessoa> getListaCobrancaMensal() {
        if (listaCobrancaMensal.isEmpty()){
            CobrancaMensalDB db = new CobrancaMensalDBToplink();
            listaCobrancaMensal = db.listaCobrancaMensal(-1);
        }
        return listaCobrancaMensal;
    }

    public void setListaCobrancaMensal(List<ServicoPessoa> listaCobrancaMensal) {
        this.listaCobrancaMensal = listaCobrancaMensal;
    }

    public String valor(float valorx) {
        return Moeda.converteR$Float(valorx);
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public float getValorCorrige() {
        return valorCorrige;
    }

    public void setValorCorrige(float valorCorrige) {
        this.valorCorrige = valorCorrige;
    }

    public String getValorFixo() {
        return Moeda.converteR$(valorFixo);
    }

    public void setValorFixo(String valorFixo) {
        this.valorFixo = Moeda.substituiVirgula(valorFixo);
    }

    public String getDescFiltro() {
        return descFiltro;
    }

    public void setDescFiltro(String descFiltro) {
        this.descFiltro = descFiltro;
    }

    public String getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(String tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }
    
    
}
