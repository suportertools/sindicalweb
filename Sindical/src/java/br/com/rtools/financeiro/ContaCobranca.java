package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_CONTA_COBRANCA")
@NamedQuery(name="ContaCobranca.pesquisaID", query="select c from ContaCobranca c where c.id=:pid")
public class ContaCobranca implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_CONTA_BANCO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private ContaBanco contaBanco;
    @Column(name="DS_COD_CEDENTE", length=100,nullable=false)
    private String codCedente;
    @Column(name="DS_LOCAL_PAGAMENTO", length=100,nullable=false)
    private String localPagamento;
    @Column(name="NR_REPASSE", length=100,nullable=false)
    private float repasse;
    @Column(name="DS_BOLETO_INICIAL", length=100,nullable=false)
    private String boletoInicial;
    @Column(name="DS_CATEGORIA_SINDICAL", length=1)
    private String categoriaSindical;
    @Column(name="DS_ARRECADACAO_SINDICAL", length=1)
    private String arrecadacaoSindical;
    @Column(name="DS_FEBRA_SINDICAL", length=4)
    private String febranSindical;
    @Column(name="DS_SEGMENTO_SINDICAL", length=1)
    private String segmentoSindical;
    @Column(name="DS_SICAS_SINDICAL", length=5)
    private String sicasSindical;
    @Column(name="DS_CODIGO_SINDICAL", length=50)
    private String codigoSindical;
    @Column(name="NR_MOEDA", length=50,nullable=false)
    private String moeda;
    @Column(name="DS_ESPECIE_MOEDA", length=50,nullable=false)
    private String especieMoeda;
    @Column(name="DS_ESPECIE_DOC", length=50,nullable=false)
    private String especieDoc;
    @Column(name="DS_CARTEIRA", length=50,nullable=false)
    private String carteira;
    @Column(name="DS_ACEITE", length=5,nullable=false)
    private String aceite;
    @Column(name="DS_CEDENTE", length=200,nullable=false)
    private String cedente;
    @OneToOne
    @JoinColumn(name="ID_LAYOUT", referencedColumnName="ID", nullable=false)
    private Layout layout;
    @Column(name="DS_CAMINHO_RETORNO", length=300)
    private String caminhoRetorno;
    @Column(name="IS_ATIVO")
    private boolean ativo;
    @Column(name="DS_APELIDO", length=50)
    private String apelido;
	
    public ContaCobranca() {
        this.id = -1;
        this.contaBanco = new ContaBanco();
        this.codCedente = "0";
        this.localPagamento = "";
        this.repasse = 0;
        this.boletoInicial = "0";
        this.categoriaSindical = "2";
        this.arrecadacaoSindical = "8";
        this.febranSindical = "0067";
        this.segmentoSindical = "";
        this.sicasSindical = "";
        this.codigoSindical = "";
        this.moeda = "9";
        this.especieMoeda = "R$";
        this.especieDoc = "";
        this.carteira = "";
        this.aceite = "";
        this.cedente = "";
        this.layout = new Layout();
        this.caminhoRetorno = "";
        this.ativo = true;
        this.apelido = "";
    }
    
    public ContaCobranca(int id, ContaBanco contaBanco, String codCedente, String localPagamento, float repasse, String boletoInicial, String categoriaSindical, String arrecadacaoSindical, String febranSindical, String segmentoSindical, String sicasSindical, String codigoSindical, String moeda, String especieMoeda, String especieDoc, String carteira, String aceite, String cedente, Layout layout, String caminhoRetorno, boolean ativo, String apelido) {
        this.id = id;
        this.contaBanco = contaBanco;
        this.codCedente = codCedente;
        this.localPagamento = localPagamento;
        this.repasse = repasse;
        this.boletoInicial = boletoInicial;
        this.categoriaSindical = categoriaSindical;
        this.arrecadacaoSindical = arrecadacaoSindical;
        this.febranSindical = febranSindical;
        this.segmentoSindical = segmentoSindical;
        this.sicasSindical = sicasSindical;
        this.codigoSindical = codigoSindical;
        this.moeda = moeda;
        this.especieMoeda = especieMoeda;
        this.especieDoc = especieDoc;
        this.carteira = carteira;
        this.aceite = aceite;
        this.cedente = cedente;
        this.layout = layout;
        this.caminhoRetorno = caminhoRetorno;
        this.ativo = ativo;
        this.apelido = apelido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContaBanco getContaBanco() {
        return contaBanco;
    }

    public void setContaBanco(ContaBanco contaBanco) {
        this.contaBanco = contaBanco;
    }

    public String getCodCedente() {
        return codCedente;
    }

    public void setCodCedente(String codCedente) {
        this.codCedente = codCedente;
    }

    public String getLocalPagamento() {
        return localPagamento;
    }

    public void setLocalPagamento(String localPagamento) {
        this.localPagamento = localPagamento;
    }

    public float getRepasse() {
        return repasse;
    }

    public void setRepasse(float repasse) {
        this.repasse = repasse;
    }

    public String getBoletoInicial() {
        return boletoInicial;
    }

    public void setBoletoInicial(String boletoInicial) {
        this.boletoInicial = boletoInicial;
    }

    public String getCategoriaSindical() {
        return categoriaSindical;
    }

    public void setCategoriaSindical(String categoriaSindical) {
        this.categoriaSindical = categoriaSindical;
    }

    public String getArrecadacaoSindical() {
        return arrecadacaoSindical;
    }

    public void setArrecadacaoSindical(String arrecadacaoSindical) {
        this.arrecadacaoSindical = arrecadacaoSindical;
    }

    public String getFebranSindical() {
        return febranSindical;
    }

    public void setFebranSindical(String febranSindical) {
        this.febranSindical = febranSindical;
    }

    public String getSegmentoSindical() {
        return segmentoSindical;
    }

    public void setSegmentoSindical(String segmentoSindical) {
        this.segmentoSindical = segmentoSindical;
    }

    public String getSicasSindical() {
        return sicasSindical;
    }

    public void setSicasSindical(String sicasSindical) {
        this.sicasSindical = sicasSindical;
    }

    public String getCodigoSindical() {
        return codigoSindical;
    }

    public void setCodigoSindical(String codigoSindical) {
        this.codigoSindical = codigoSindical;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public String getEspecieMoeda() {
        return especieMoeda;
    }

    public void setEspecieMoeda(String especieMoeda) {
        this.especieMoeda = especieMoeda;
    }

    public String getEspecieDoc() {
        return especieDoc;
    }

    public void setEspecieDoc(String especieDoc) {
        this.especieDoc = especieDoc;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public String getAceite() {
        return aceite;
    }

    public void setAceite(String aceite) {
        this.aceite = aceite;
    }

    public String getCedente() {
        return cedente;
    }

    public void setCedente(String cedente) {
        this.cedente = cedente;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public String getCaminhoRetorno() {
        return caminhoRetorno;
    }

    public void setCaminhoRetorno(String caminhoRetorno) {
        this.caminhoRetorno = caminhoRetorno;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }
}