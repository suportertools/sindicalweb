package br.com.rtools.relatorios;

import br.com.rtools.seguranca.Rotina;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "sis_relatorios")
@NamedQueries({
    @NamedQuery(name = "Relatorios.pesquisaID", query = "SELECT R FROM Relatorios R WHERE R.id = :pid"),
    @NamedQuery(name = "Relatorios.findAll", query = "SELECT R FROM Relatorios AS R ORDER BY R.nome ASC")
})
public class Relatorios implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Rotina rotina;
    @Column(name = "ds_nome", length = 100, nullable = false)
    private String nome;
    @Column(name = "ds_jasper", length = 50, nullable = false)
    private String jasper;
    @Column(name = "ds_qry", length = 1000)
    private String qry;
    @Column(name = "ds_qry_ordem", length = 1000)
    private String qryOrdem;
    @Column(name = "is_por_folha", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean porFolha;
    @Column(name = "ds_nome_grupo", length = 100)
    private String nomeGrupo;
    @Column(name = "is_excel", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean excel;
    @Column(name = "ds_campos_excel", length = 255)
    private String camposExcel;
    @Column(name = "is_monta_query_string", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean montaQuery;
    @Column(name = "ds_query_string")
    private String queryString;
    @Column(name = "is_default", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean principal;

    public Relatorios() {
        this.id = -1;
        this.rotina = new Rotina();
        this.nome = "";
        this.jasper = "";
        this.qry = "";
        this.qryOrdem = "";
        this.porFolha = false;
        this.nomeGrupo = "";
        this.excel = false;
        this.camposExcel = "";
        this.montaQuery = false;
        this.queryString = "";
        this.principal = false;
    }

    public Relatorios(Integer id, Rotina rotina, String nome, String jasper, String qry, Boolean porFolha, String nomeGrupo, Boolean excel, String camposExcel, Boolean montaQuery, String queryString, Boolean principal) {
        this.id = id;
        this.rotina = rotina;
        this.nome = nome;
        this.jasper = jasper;
        this.qry = qry;
        this.porFolha = porFolha;
        this.nomeGrupo = nomeGrupo;
        this.excel = excel;
        this.camposExcel = camposExcel;
        this.montaQuery = montaQuery;
        this.queryString = queryString;
        this.principal = principal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getJasper() {
        return jasper;
    }

    public void setJasper(String jasper) {
        this.jasper = jasper;
    }

    public String getQry() {
        return qry;
    }

    public void setQry(String qry) {
        this.qry = qry;
    }

    public String getQryOrdem() {
        return qryOrdem;
    }

    public void setQryOrdem(String qryOrdem) {
        this.qryOrdem = qryOrdem;
    }

    public Boolean getPorFolha() {
        return porFolha;
    }

    public void setPorFolha(Boolean porFolha) {
        this.porFolha = porFolha;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    public Boolean getExcel() {
        return excel;
    }

    public void setExcel(Boolean excel) {
        this.excel = excel;
    }

    public String getCamposExcel() {
        return camposExcel;
    }

    public void setCamposExcel(String camposExcel) {
        this.camposExcel = camposExcel;
    }

    public Boolean getMontaQuery() {
        return montaQuery;
    }

    public void setMontaQuery(Boolean montaQuery) {
        this.montaQuery = montaQuery;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Relatorios other = (Relatorios) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Relatorios{" + "id=" + id + ", rotina=" + rotina + ", nome=" + nome + ", jasper=" + jasper + ", qry=" + qry + ", qryOrdem=" + qryOrdem + ", porFolha=" + porFolha + ", nomeGrupo=" + nomeGrupo + ", excel=" + excel + ", camposExcel=" + camposExcel + ", montaQuery=" + montaQuery + ", queryString=" + queryString + ", principal=" + principal + '}';
    }

}
