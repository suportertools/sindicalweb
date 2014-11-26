package br.com.rtools.impressao;

public class ParametroRaisNaoEnviadasRelatorio {

//     0 detalhes_relatorio,                  
//     01 C.ds_documento     AS cnpj,                  
//     02 C.ds_nome          AS empresa,             
//     03 C.ds_descricao     AS convencao,           
//     04 C.ds_grupo_cidade  AS grupo,               
//     05 J.jurtelefone      AS telefone,            
//     06 J.jurlogradouro    AS logradouro,          
//     07 J.jurendereco      AS endereco,            
//     08 J.jurcomplemento   AS complemento,         
//     09 J.jurbairro        AS bairro,              
//     10 J.jurcidade        AS cidade,              
//     11 J.juruf            AS uf,                  
//     12 J.jurcep           AS cep,                 
//     13 J.juremail1        AS email,               
//     14 J.escnome          AS escritorio,          
//     15 J.esctelefone      AS esc_telefone,        
//     16 J.escemail         AS esc_email,           
//     17 J.esclogradouro    AS esc_logradouro,      
//     18 J.escendereco      AS esc_endereco,        
//     19 J.esccomplemento   AS esc_complemento,     
//     20 J.escbairro        AS esc_bairro,          
//     21 J.esccidade        AS esc_cidade,          
//     22 J.escuf            AS esc_uf,              
//     23 J.esccep           AS esc_cep,             
//     24 J.jurid,                                   
//     25 J.escid     
    private String detalhes_relatorio;
    private String cnpj;
    private String empresa;
    private String convencao;
    private String grupo;
    private String telefone;
    private String logradouro;
    private String endereco;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String email;
    private String escritorio;
    private String escritorio_telefone;
    private String escritorio_email;
    private String escritorio_logradouro;
    private String escritorio_endereco;
    private String escritorio_complemento;
    private String escritorio_bairro;
    private String escritorio_cidade;
    private String escritorio_uf;
    private String escritorio_cep;
    private String id;
    private String escritorio_id;
    private String quantidade;
    private String numero;
    private String escritorio_numero;

    public ParametroRaisNaoEnviadasRelatorio() {
        this.detalhes_relatorio = "";
        this.cnpj = "";
        this.empresa = "";
        this.convencao = "";
        this.grupo = "";
        this.telefone = "";
        this.logradouro = "";
        this.endereco = "";
        this.complemento = "";
        this.bairro = "";
        this.cidade = "";
        this.uf = "";
        this.cep = "";
        this.email = "";
        this.escritorio = "";
        this.escritorio_telefone = "";
        this.escritorio_email = "";
        this.escritorio_logradouro = "";
        this.escritorio_endereco = "";
        this.escritorio_numero = "";
        this.escritorio_complemento = "";
        this.escritorio_bairro = "";
        this.escritorio_cidade = "";
        this.escritorio_uf = "";
        this.escritorio_cep = "";
        this.id = "";
        this.escritorio_id = "";
        this.quantidade = "0";
    }

    public ParametroRaisNaoEnviadasRelatorio(String detalhes_relatorio, String escritorio, String escritorio_telefone, String escritorio_email, String escritorio_id, String quantidade, String escritorio_logradouro, String escritorio_endereco, String escritorio_complemento, String escritorio_bairro, String escritorio_cidade, String escritorio_uf, String escritorio_cep, String escritorio_numero) {
        this.detalhes_relatorio = detalhes_relatorio;
        this.escritorio = escritorio;
        this.escritorio_telefone = escritorio_telefone;
        this.escritorio_email = escritorio_email;
        this.escritorio_id = escritorio_id;
        this.quantidade = quantidade;
        this.escritorio_logradouro = escritorio_logradouro;
        this.escritorio_endereco = escritorio_endereco;
        this.escritorio_complemento = escritorio_complemento;
        this.escritorio_bairro = escritorio_bairro;
        this.escritorio_cidade = escritorio_cidade;
        this.escritorio_uf = escritorio_uf;
        this.escritorio_cep = escritorio_cep;
        this.escritorio_numero = escritorio_numero;
    }

    public ParametroRaisNaoEnviadasRelatorio(String detalhes_relatorio, String cnpj, String empresa, String convencao, String grupo, String telefone, String logradouro, String endereco, String complemento, String bairro, String cidade, String uf, String cep, String email, String escritorio, String escritorio_telefone, String escritorio_email, String escritorio_logradouro, String escritorio_endereco, String escritorio_complemento, String escritorio_bairro, String escritorio_cidade, String escritorio_uf, String escritorio_cep, String id, String escritorio_id, String quantidade, String numero, String escritorio_numero) {
        this.detalhes_relatorio = detalhes_relatorio;
        this.cnpj = cnpj;
        this.empresa = empresa;
        this.convencao = convencao;
        this.grupo = grupo;
        this.telefone = telefone;
        this.logradouro = logradouro;
        this.endereco = endereco;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
        this.email = email;
        this.escritorio = escritorio;
        this.escritorio_telefone = escritorio_telefone;
        this.escritorio_email = escritorio_email;
        this.escritorio_logradouro = escritorio_logradouro;
        this.escritorio_endereco = escritorio_endereco;
        this.escritorio_complemento = escritorio_complemento;
        this.escritorio_bairro = escritorio_bairro;
        this.escritorio_cidade = escritorio_cidade;
        this.escritorio_uf = escritorio_uf;
        this.escritorio_cep = escritorio_cep;
        this.id = id;
        this.escritorio_id = escritorio_id;
        this.quantidade = quantidade;
        this.numero = numero;
        this.escritorio_numero = escritorio_numero;
    }

    public String getDetalhes_relatorio() {
        return detalhes_relatorio;
    }

    public void setDetalhes_relatorio(String detalhes_relatorio) {
        this.detalhes_relatorio = detalhes_relatorio;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getConvencao() {
        return convencao;
    }

    public void setConvencao(String convencao) {
        this.convencao = convencao;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEscritorio() {
        return escritorio;
    }

    public void setEscritorio(String escritorio) {
        this.escritorio = escritorio;
    }

    public String getEscritorio_telefone() {
        return escritorio_telefone;
    }

    public void setEscritorio_telefone(String escritorio_telefone) {
        this.escritorio_telefone = escritorio_telefone;
    }

    public String getEscritorio_email() {
        return escritorio_email;
    }

    public void setEscritorio_email(String escritorio_email) {
        this.escritorio_email = escritorio_email;
    }

    public String getEscritorio_logradouro() {
        return escritorio_logradouro;
    }

    public void setEscritorio_logradouro(String escritorio_logradouro) {
        this.escritorio_logradouro = escritorio_logradouro;
    }

    public String getEscritorio_endereco() {
        return escritorio_endereco;
    }

    public void setEscritorio_endereco(String escritorio_endereco) {
        this.escritorio_endereco = escritorio_endereco;
    }

    public String getEscritorio_bairro() {
        return escritorio_bairro;
    }

    public void setEscritorio_bairro(String escritorio_bairro) {
        this.escritorio_bairro = escritorio_bairro;
    }

    public String getEscritorio_cidade() {
        return escritorio_cidade;
    }

    public void setEscritorio_cidade(String escritorio_cidade) {
        this.escritorio_cidade = escritorio_cidade;
    }

    public String getEscritorio_uf() {
        return escritorio_uf;
    }

    public void setEscritorio_uf(String escritorio_uf) {
        this.escritorio_uf = escritorio_uf;
    }

    public String getEscritorio_cep() {
        return escritorio_cep;
    }

    public void setEscritorio_cep(String escritorio_cep) {
        this.escritorio_cep = escritorio_cep;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEscritorio_id() {
        return escritorio_id;
    }

    public void setEscritorio_id(String escritorio_id) {
        this.escritorio_id = escritorio_id;
    }

    public String getEscritorio_complemento() {
        return escritorio_complemento;
    }

    public void setEscritorio_complemento(String escritorio_complemento) {
        this.escritorio_complemento = escritorio_complemento;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getEscritorio_numero() {
        return escritorio_numero;
    }

    public void setEscritorio_numero(String escritorio_numero) {
        this.escritorio_numero = escritorio_numero;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

}
