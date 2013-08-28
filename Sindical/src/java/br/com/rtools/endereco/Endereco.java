package br.com.rtools.endereco;

import javax.persistence.*;


@Entity
@Table(name="END_ENDERECO")
@NamedQuery(name="Endereco.pesquisaID", query="select e from Endereco e where e.id=:pid")
public class Endereco implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_CIDADE", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Cidade cidade;
    @JoinColumn(name="ID_BAIRRO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Bairro bairro;       
    @JoinColumn(name="ID_LOGRADOURO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Logradouro logradouro;    
    @JoinColumn(name="ID_DESCRICAO_ENDERECO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private DescricaoEndereco descricaoEndereco;      
    @Column(name="DS_CEP", length=9 ,nullable=false)
    private String cep;
    @Column(name="DS_FAIXA", length=100 ,nullable=true)
    private String faixa;    

    public Endereco() {
        this.id = -1;
        this.cidade = new Cidade();
        this.bairro = new Bairro();
        this.logradouro = new Logradouro();
        this.descricaoEndereco = new DescricaoEndereco();
        this.cep = "";
        this.faixa = "";
    } 
    
    public Endereco(int id, Cidade cidade, Bairro bairro, Logradouro logradouro, DescricaoEndereco descricaoEndereco, String cep, String faixa) {
        this.id = id;
        this.cidade = cidade;
        this.bairro = bairro;
        this.logradouro = logradouro;
        this.descricaoEndereco = descricaoEndereco;
        this.cep = cep;
        this.faixa = faixa;
    }    
   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public Logradouro getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
    }

    public DescricaoEndereco getDescricaoEndereco() {
        return descricaoEndereco;
    }

    public void setDescricaoEndereco(DescricaoEndereco descricaoEndereco) {
        this.descricaoEndereco = descricaoEndereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getFaixa() {
        return faixa;
    }

    public void setFaixa(String faixa) {
        this.faixa = faixa;
    }

    public String getEnderecoToString(){
        if ((this.getLogradouro().getDescricao().equals("")) ||
            (this.getDescricaoEndereco().getDescricao().equals("")) ||
            (this.getBairro().getDescricao().equals("")) ||
            (this.getCidade().getCidade().equals("")) ||
            (this.getCidade().getUf().equals(""))){
            return "";
        }else{
            return this.getLogradouro().getDescricao()+ " " +
                   this.getDescricaoEndereco().getDescricao() + ", " +
                   this.getBairro().getDescricao() + ", " +
                   this.getCidade().getCidade() + "  -  "  +
                   this.getCidade().getUf() + "  ";
        }
    }
    
    public String getEnderecoSimplesToString(){
        if ((this.getLogradouro().getDescricao().equals("")) ||
            (this.getDescricaoEndereco().getDescricao().equals(""))){
            return "";
        }else{
            return this.getLogradouro().getDescricao() + " " +
                   this.getDescricaoEndereco().getDescricao();
        }
    }   
}