
package br.com.rtools.atendimento;

import javax.persistence.*;

@Entity
@Table(name="ATE_PESSOA")
@NamedQuery(name="AtePessoa.pesquisaID", query="select apes from AtePessoa apes where apes.id=:pid")
public class AtePessoa implements java.io.Serializable {    

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_NOME", length=200,nullable=false)
    private String nome;
    @Column(name="DS_CPF", length=15,nullable=false)
    private String cpf;    
    @Column(name="DS_RG", length=12,nullable=false)
    private String rg;
    @Column(name="DS_TELEFONE", length=20,nullable=true)
    private String telefone;
    
    public AtePessoa() {
        this.id = -1;
        this.nome = "";
        this.cpf = "";
        this.rg = "";
        this.telefone = "";
    }
    
    public AtePessoa(int id, String nome, String cpf, String rg, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.telefone = telefone;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCPF() {
        return cpf;
    }

    public void setCPF(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}

    