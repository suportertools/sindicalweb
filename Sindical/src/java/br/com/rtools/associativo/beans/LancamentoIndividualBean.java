package br.com.rtools.associativo.beans;


import br.com.rtools.associativo.db.LancamentoIndividualDB;
import br.com.rtools.associativo.db.LancamentoIndividualDBToplink;
import br.com.rtools.financeiro.Movimento;
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
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class LancamentoIndividualBean {
    private Fisica fisica = new Fisica();
    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private List<SelectItem> listaJuridica = new ArrayList<SelectItem>();
    private List<SelectItem> listaDiaVencimento = new ArrayList<SelectItem>();
    private int idServico = 0;
    private int idJuridica = 0;
    private int idDia = 0;
    private List<Movimento> listaMovimento = new ArrayList();
    private String cobrancaBancaria = "sim";
    private String entrada = "sim";
    private String descontoFolha = "nao";
    private String totalPagar = "";
    private String msgConfirma = "";
    private int parcelas = 0;
    private Pessoa responsavel = new Pessoa();
    
    public void pesquisaDescontoFolha(){
        responsavel = new Pessoa();
    }
    
    public void limpaEmpresaConvenio(){
        listaJuridica.clear();
    }
    
    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()) {
            int i = 0;
            ServicoRotinaDB db = new ServicoRotinaDBToplink();
            List<Servicos> select = db.pesquisaTodosServicosComRotinas(131);
            while (i < select.size()) {
                listaServicos.add(new SelectItem(new Integer(i),
                                  select.get(i).getDescricao(),
                                  Integer.toString(select.get(i).getId())
                        ));
                i++;
            }
        }
        return listaServicos;
    }
    
    public Fisica getFisica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null){
            fisica = (Fisica)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
            responsavel = new Pessoa();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public List<Movimento> getListaMovimento() {
        return listaMovimento;
    }

    public void setListaMovimento(List<Movimento> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }

    public List<SelectItem> getListaJuridica() {
        if (listaJuridica.isEmpty()){
            LancamentoIndividualDB db = new LancamentoIndividualDBToplink();
            List<Juridica> result = db.listaEmpresaConveniada(Integer.parseInt(listaServicos.get(idServico).getDescription()));
            
            for (int i = 0; i < result.size(); i++){
                listaJuridica.add(new SelectItem(new Integer(i),
                                  result.get(i).getPessoa().getNome(),
                                  Integer.toString(result.get(i).getId())
                        ));
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
        if (fisica.getId() != -1){
            LancamentoIndividualDB db = new LancamentoIndividualDBToplink();
            List<Vector> valor = db.pesquisaServicoValor(fisica.getPessoa().getId(), Integer.parseInt(listaServicos.get(idServico).getDescription()));
            
        }
        return Moeda.converteR$(totalPagar);
    }

    public void setTotalPagar(String totalPagar) {
        this.totalPagar = totalPagar;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public int getIdJuridica() {
        return idJuridica;
    }

    public void setIdJuridica(int idJuridica) {
        this.idJuridica = idJuridica;
    }

    public Pessoa getResponsavel() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null){
            responsavel = (Pessoa)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
            
            JuridicaDB dbj = new JuridicaDBToplink();
            FisicaDB dbf = new FisicaDBToplink();
            
            LancamentoIndividualDB dbl = new LancamentoIndividualDBToplink();
            Juridica jur = dbj.pesquisaJuridicaPorPessoa(responsavel.getId());
            
            // PESQUISA NA TABELA DO SERASA tanto pessoa fisica quanto juridica ----
            if (!dbl.listaSerasa(responsavel.getId()).isEmpty()){
                msgConfirma = "Esta pessoa contém o nome no Serasa, não poderá ser responsável!";
                return responsavel = new Pessoa();
            }
            
            // CASO SEJA PESSOA JURIDICA -------------------
            if (jur != null){
                // VERIFICA SE É CONTRIBUINTE --------------
                List contribuintes = dbl.pesquisaContribuinteLancamento(responsavel.getId());
                if (!contribuintes.isEmpty()){
                    msgConfirma = "Esta empresa foi fechada, não poderá ser responsável!";
                    return responsavel = new Pessoa();
                }
                
                // VERIFICA SE A EMPRESA CONTEM LISTA DE ENDERECO -------
                List lista_pe = dbj.pesquisarPessoaEnderecoJuridica(responsavel.getId());
                if (lista_pe.isEmpty()){
                    msgConfirma = "Esta empresa não possui endereço cadastrado, não poderá ser responsável!";
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
                    msgConfirma = "Esta pessoa possui débitos com o Sindicato, não poderá ser responsável!";
                    return responsavel = new Pessoa();
                }
                
                // VERIFICA SE PESSOA É MAIOR DE IDADE
                DataHoje dh = new DataHoje();
                int idade = dh.calcularIdade(fi.getNascimento());
                if (idade < 18){
                    msgConfirma = "Esta pessoa não é maior de idade, não poderá ser responsável!";
                    return responsavel = new Pessoa();
                }
                
                // VERIFICA SE A PESSOA CONTEM LISTA DE ENDERECO -------
                List lista_pe = dbj.pesquisarPessoaEnderecoJuridica(responsavel.getId());
                if (lista_pe.isEmpty()){
                    msgConfirma = "Esta pessoa não possui endereço cadastrado, não poderá ser responsável!";
                    return responsavel = new Pessoa();
                }
                
                return responsavel = fi.getPessoa();
            }
        }
        
        if (fisica.getId() != -1 && responsavel.getId() == -1){
            LancamentoIndividualDB db = new LancamentoIndividualDBToplink();
            
            List<Vector> result = db.pesquisaResponsavel(fisica.getPessoa().getId(), descontoFolha.equals("sim") ? true : false);
            
            if ((Integer) result.get(0).get(0) != 0){
                SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
                responsavel = (Pessoa)sv.pesquisaCodigo((Integer) result.get(0).get(0), "Pessoa");
            }
        }
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public String getDescontoFolha() {
        return descontoFolha;
    }

    public void setDescontoFolha(String descontoFolha) {
        this.descontoFolha = descontoFolha;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
}