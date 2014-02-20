package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "PES_FISICA")
@NamedQuery(name = "Fisica.pesquisaID", query = "select fis from Fisica fis where fis.id=:pid")
public class Fisica implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)//(optional=false)   (cascade=CascadeType.ALL)
    private Pessoa pessoa;
    @Column(name = "DS_RG", length = 20, nullable = false)
    private String rg;
    @Column(name = "DS_CARTEIRA", length = 30, nullable = false)
    private String carteira;
    @Column(name = "DS_SERIE", length = 15, nullable = false)
    private String serie;
    @Column(name = "DS_SEXO", length = 1, nullable = false)
    private String sexo;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_NASCIMENTO")
    private Date dtNascimento;
    @Column(name = "DS_NACIONALIDADE", length = 50, nullable = false)
    private String nacionalidade;
    @Column(name = "DS_NATURALIDADE", length = 50, nullable = false)
    private String naturalidade;
    @Column(name = "DS_ORGAO_EMISSAO_RG", length = 30, nullable = false)
    private String orgaoEmissaoRG;
    @Column(name = "DS_UF_EMISSAO_RG", length = 2, nullable = false)
    private String ufEmissaoRG;
    @Column(name = "DS_ESTADO_CIVIL", length = 30, nullable = false)
    private String estadoCivil;
    @Column(name = "DS_PAI", length = 100, nullable = false)
    private String pai;
    @Column(name = "DS_MAE", length = 100, nullable = false)
    private String mae;
    @Column(name = "DS_NIT", length = 30, nullable = false)
    private String nit;
    @Column(name = "DS_PIS", length = 30, nullable = false)
    private String pis;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_APOSENTADORIA")
    private Date dtAposentadoria;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_RECADASTRO")
    private Date dtRecadastro;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_FOTO")
    private Date dtFoto;

    public Fisica() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.rg = "";
        this.carteira = "";
        this.serie = "";
        this.sexo = "M";
        this.dtNascimento = null;
        this.nacionalidade = "";
        this.naturalidade = "";
        this.orgaoEmissaoRG = "";
        this.ufEmissaoRG = "";
        this.estadoCivil = "";
        this.pai = "";
        this.mae = "";
        this.nit = "";
        this.pis = "";
        this.setAposentadoria("");
        this.setRecadastro(DataHoje.data());
        this.setDataFoto("");
    }

    public Fisica(int id, Pessoa pessoa, String rg, String carteira, String serie, String sexo, Date dtNascimento, String nacionalidade,
            String naturalidade, String orgaoEmissaoRG, String ufEmissaoRG, String estadoCivil, String pai, String mae, String nit,
            String pis, String aposentadoria, String recadastro, String dataFoto) {
        this.id = id;
        this.pessoa = pessoa;
        this.rg = rg;
        this.carteira = carteira;
        this.serie = serie;
        this.sexo = sexo;
        this.dtNascimento = dtNascimento;
        this.nacionalidade = nacionalidade;
        this.naturalidade = naturalidade;
        this.orgaoEmissaoRG = orgaoEmissaoRG;
        this.ufEmissaoRG = ufEmissaoRG;
        this.estadoCivil = estadoCivil;
        this.pai = pai;
        this.mae = mae;
        this.nit = nit;
        this.pis = pis;
        this.setAposentadoria(aposentadoria);
        this.setRecadastro(recadastro);
        this.setDataFoto(dataFoto);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getOrgaoEmissaoRG() {
        return orgaoEmissaoRG;
    }

    public void setOrgaoEmissaoRG(String orgaoEmissaoRG) {
        this.orgaoEmissaoRG = orgaoEmissaoRG;
    }

    public String getUfEmissaoRG() {
        return ufEmissaoRG;
    }

    public void setUfEmissaoRG(String ufEmissaoRG) {
        this.ufEmissaoRG = ufEmissaoRG;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getNascimento() {
        if (dtNascimento != null) {
            return DataHoje.converteData(dtNascimento);
        } else {
            return "";
        }
    }

    public void setNascimento(String nascimento) {
        if (!(nascimento.isEmpty())) {
            this.dtNascimento = DataHoje.converte(nascimento);
        }
    }

    public Date getDtAposentadoria() {
        return dtAposentadoria;
    }

    public void setDtAposentadoria(Date dtAposentadoria) {
        this.dtAposentadoria = dtAposentadoria;
    }

    public String getAposentadoria() {
        return DataHoje.converteData(dtAposentadoria);
    }

    public void setAposentadoria(String aposentadoria) {
        this.dtAposentadoria = DataHoje.converte(aposentadoria);
    }

    public Date getDtRecadastro() {
        return dtRecadastro;
    }

    public void setDtRecadastro(Date dtRecadastro) {
        this.dtRecadastro = dtRecadastro;
    }

    public String getRecadastro() {
        return DataHoje.converteData(dtRecadastro);
    }

    public void setRecadastro(String recadastro) {
        this.dtRecadastro = DataHoje.converte(recadastro);
    }

    public Date getDtFoto() {
        return dtFoto;
    }

    public void setDtFoto(Date dtFoto) {
        this.dtFoto = dtFoto;
    }

    public String getDataFoto() {
        return DataHoje.converteData(dtFoto);
    }

    public void setDataFoto(String dataFoto) {
        this.dtFoto = DataHoje.converte(dataFoto);
    }

    public void selecionaDataNascimento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dtNascimento = DataHoje.converte(format.format(event.getObject()));
    }

    @Override
    public String toString() {
        return "Fisica{" + "id=" + id + ", pessoa=" + pessoa + ", rg=" + rg + ", carteira=" + carteira + ", serie=" + serie + ", sexo=" + sexo + ", dtNascimento=" + dtNascimento + ", nacionalidade=" + nacionalidade + ", naturalidade=" + naturalidade + ", orgaoEmissaoRG=" + orgaoEmissaoRG + ", ufEmissaoRG=" + ufEmissaoRG + ", estadoCivil=" + estadoCivil + ", pai=" + pai + ", mae=" + mae + ", nit=" + nit + ", pis=" + pis + ", dtAposentadoria=" + dtAposentadoria + ", dtRecadastro=" + dtRecadastro + ", dtFoto=" + dtFoto + '}';
    }
}
