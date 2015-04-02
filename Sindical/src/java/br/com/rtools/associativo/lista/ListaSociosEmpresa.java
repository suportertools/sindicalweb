package br.com.rtools.associativo.lista;

// (nome, matricula, categoria, filiacao,admissão, Desc.Folha
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "listaSociosEmpresaMapping",
        entities = {
            @EntityResult(entityClass = ListaSociosEmpresa.class, fields = {
                @FieldResult(name = "nome", column = "nome"),
                @FieldResult(name = "matricula", column = "matricula"),
                @FieldResult(name = "categoria", column = "categoria")})
        }
)
public class ListaSociosEmpresa {

    private String nome;
    private Integer matricula;
    private String categoria;
    private Date filiacao;
    private Date admissao;
    private Boolean desconto_folha;

    public ListaSociosEmpresa() {
        this.nome = "";
        this.matricula = 0;
        this.categoria = "";
        this.filiacao = null;
        this.admissao = null;
        this.desconto_folha = false;
    }

    public ListaSociosEmpresa(String nome, Integer matricula, String categoria, Date filiacao, Date admissao, Boolean desconto_folha) {
        this.nome = nome;
        this.matricula = matricula;
        this.categoria = categoria;
        this.filiacao = filiacao;
        this.admissao = admissao;
        this.desconto_folha = desconto_folha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getFiliacao() {
        return filiacao;
    }

    public String getFiliacaoString() {
        return DataHoje.converteData(filiacao);
    }

    public void setFiliacao(Date filiacao) {
        this.filiacao = filiacao;
    }

    public Date getAdmissao() {
        return admissao;
    }

    public String getAdmissaoString() {
        return DataHoje.converteData(admissao);
    }

    public void setAdmissao(Date admissao) {
        this.admissao = admissao;
    }

    public Boolean getDesconto_folha() {
        return desconto_folha;
    }

    public void setDesconto_folha(Boolean desconto_folha) {
        this.desconto_folha = desconto_folha;
    }

}
