package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "pes_pessoa_sem_cadastro")
@NamedQuery(name = "PessoaSemCadastro.pesquisaID", query = "select p from PessoaSemCadastro p where p.id=:pid")
public class PessoaSemCadastro implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_nome", length = 100)
    private String nome;
    @Column(name = "ds_documento", length = 50)
    private String documento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date dtData;

    public PessoaSemCadastro() {
        this.id = -1;
        this.nome = "";
        this.documento = "";
        setData(DataHoje.data());
    }

    public PessoaSemCadastro(int id, String nome, String documento, String data) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        setData(data);
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getDtData() {
        return dtData;
    }

    public void setDtData(Date dtData) {
        this.dtData = dtData;
    }

    public String getData() {
        return DataHoje.converteData(dtData);
    }

    public void setData(String data) {
        this.dtData = DataHoje.converte(data);
    }
}
