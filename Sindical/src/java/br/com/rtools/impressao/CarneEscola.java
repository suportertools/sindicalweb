package br.com.rtools.impressao;

public class CarneEscola {

    private int id;
    private String sinLogo;
    private String sinNome;
    private String sinEndereco;
    private String sinLogradouro;
    private String sinNumero;
    private String sinComplemento;
    private String sinBairro;
    private String sinCep;
    private String sinCidade;
    private String sinUF;
    private String sinTelefone;
    private String sinEmail;
    private String sinSite;
    private String sinDocumento;
    private String responsavel;
    private String idResponsavel;
    private String aluno;
    private String contrato;
    private String curso;
    private String vencimento;
    private String valorCurso;
    private String valorParcela;
    private String usuario;
    private int numeroParcela;
    private String hora;

    public CarneEscola() {
        this.id = 0;
        this.sinLogo = "";
        this.sinNome = "";
        this.sinLogradouro = "";
        this.sinNumero = "";
        this.sinComplemento = "";
        this.sinBairro = "";
        this.sinCep = "";
        this.sinCidade = "";
        this.sinUF = "";
        this.sinTelefone = "";
        this.sinEmail = "";
        this.sinSite = "";
        this.sinDocumento = "";
        this.responsavel = "";
        this.idResponsavel = "";
        this.aluno = "";
        this.contrato = "";
        this.curso = "";
        this.vencimento = "";
        this.valorCurso = "";
        this.valorParcela = "";
        this.usuario = "";
        this.numeroParcela = 0;
        this.hora = "";
    }

    public CarneEscola(int id, String sinLogo, String sinNome, String sinLogradouro, String sinNumero, String sinComplemento, String sinBairro, String sinCep, String sinCidade, String sinUF, String sinTelefone, String sinEmail, String sinSite, String sinDocumento, String responsavel, String idResponsavel, String aluno, String contrato, String curso, String vencimento, String valorCurso, String valorParcela, String usuario, int numeroParcela, String hora) {
        this.id = id;
        this.sinLogo = sinLogo;
        this.sinNome = sinNome;
        this.sinLogradouro = sinLogradouro;
        this.sinNumero = sinNumero;
        this.sinComplemento = sinComplemento;
        this.sinBairro = sinBairro;
        this.sinCep = sinCep;
        this.sinCidade = sinCidade;
        this.sinUF = sinUF;
        this.sinTelefone = sinTelefone;
        this.sinEmail = sinEmail;
        this.sinSite = sinSite;
        this.sinDocumento = sinDocumento;
        this.responsavel = responsavel;
        this.idResponsavel = idResponsavel;
        this.aluno = aluno;
        this.contrato = contrato;
        this.curso = curso;
        this.vencimento = vencimento;
        this.valorCurso = valorCurso;
        this.valorParcela = valorParcela;
        this.usuario = usuario;
        this.numeroParcela = numeroParcela;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSinLogo() {
        return sinLogo;
    }

    public void setSinLogo(String sinLogo) {
        this.sinLogo = sinLogo;
    }

    public String getSinNome() {
        return sinNome;
    }

    public void setSinNome(String sinNome) {
        this.sinNome = sinNome;
    }

    public String getSinEndereco() {
        return sinEndereco;
    }

    public void setSinEndereco(String sinEndereco) {
        this.sinEndereco = sinEndereco;
    }

    public String getSinLogradouro() {
        return sinLogradouro;
    }

    public void setSinLogradouro(String sinLogradouro) {
        this.sinLogradouro = sinLogradouro;
    }

    public String getSinNumero() {
        return sinNumero;
    }

    public void setSinNumero(String sinNumero) {
        this.sinNumero = sinNumero;
    }

    public String getSinComplemento() {
        return sinComplemento;
    }

    public void setSinComplemento(String sinComplemento) {
        this.sinComplemento = sinComplemento;
    }

    public String getSinBairro() {
        return sinBairro;
    }

    public void setSinBairro(String sinBairro) {
        this.sinBairro = sinBairro;
    }

    public String getSinCep() {
        return sinCep;
    }

    public void setSinCep(String sinCep) {
        this.sinCep = sinCep;
    }

    public String getSinCidade() {
        return sinCidade;
    }

    public void setSinCidade(String sinCidade) {
        this.sinCidade = sinCidade;
    }

    public String getSinUF() {
        return sinUF;
    }

    public void setSinUF(String sinUF) {
        this.sinUF = sinUF;
    }

    public String getSinTelefone() {
        return sinTelefone;
    }

    public void setSinTelefone(String sinTelefone) {
        this.sinTelefone = sinTelefone;
    }

    public String getSinEmail() {
        return sinEmail;
    }

    public void setSinEmail(String sinEmail) {
        this.sinEmail = sinEmail;
    }

    public String getSinSite() {
        return sinSite;
    }

    public void setSinSite(String sinSite) {
        this.sinSite = sinSite;
    }

    public String getSinDocumento() {
        return sinDocumento;
    }

    public void setSinDocumento(String sinDocumento) {
        this.sinDocumento = sinDocumento;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getIdResponsavel() {
        return idResponsavel;
    }

    public void setIdResponsavel(String idResponsavel) {
        this.idResponsavel = idResponsavel;
    }

    public String getAluno() {
        return aluno;
    }

    public void setAluno(String aluno) {
        this.aluno = aluno;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getValorCurso() {
        return valorCurso;
    }

    public void setValorCurso(String valorCurso) {
        this.valorCurso = valorCurso;
    }

    public String getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(String valorParcela) {
        this.valorParcela = valorParcela;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getNumeroParcela() {
        return numeroParcela;
    }

    public void setNumeroParcela(int numeroParcela) {
        this.numeroParcela = numeroParcela;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
