package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.db.CobrancaMensalDB;
import br.com.rtools.associativo.db.CobrancaMensalDBToplink;
import br.com.rtools.associativo.db.LancamentoIndividualDB;
import br.com.rtools.associativo.db.LancamentoIndividualDBToplink;
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
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.db.FunctionsDB;
import br.com.rtools.utilitarios.db.FunctionsDBTopLink;
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
    private float valor = 0;
    private Servicos servicos = new Servicos();
    
    public void novo(){
        GenericaSessao.put("CobrancaMensalBean", new CobrancaMensalBean());
    }
    
    public ServicoPessoa getServicoPessoa() {
        if (GenericaSessao.getObject("pessoaPesquisa") != null){
            servicoPessoa.setPessoa((Pessoa)GenericaSessao.getObject("pessoaPesquisa"));
            GenericaSessao.remove("pessoaPesquisa");
            
            FunctionsDB fc = new FunctionsDBTopLink();
            
            int id_resp = fc.responsavel(servicoPessoa.getPessoa().getId(), false);
            
            if (id_resp == -1){
                return servicoPessoa;
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

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }
    
    
}
