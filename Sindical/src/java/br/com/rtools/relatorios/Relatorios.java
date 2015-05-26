package br.com.rtools.relatorios;

import br.com.rtools.seguranca.Rotina;
import javax.persistence.*;

@Entity
@Table(name = "sis_relatorios")
@NamedQuery(name = "Relatorios.pesquisaID", query = "select rel from Relatorios rel where rel.id=:pid")
public class Relatorios implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
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
    @Column(name = "is_por_folha")
    private Boolean porFolha;
    @Column(name = "ds_nome_grupo", length = 100)
    private String nomeGrupo;
    @Column(name = "is_excel")
    private Boolean excel;
    @Column(name = "ds_campos_excel", length = 255)
    private String camposExcel;

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
    }

    public Relatorios(int id, Rotina rotina, String nome, String jasper, String qry, String qryOrdem, Boolean porFolha, String nomeGrupo, Boolean excel, String camposExcel) {
        this.id = -1;
        this.rotina = rotina;
        this.nome = nome;
        this.jasper = jasper;
        this.qry = qry;
        this.qryOrdem = qryOrdem;
        this.porFolha = porFolha;
        this.nomeGrupo = nomeGrupo;
        this.excel = excel;
        this.camposExcel = camposExcel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
