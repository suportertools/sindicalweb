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
    private Object detalhes_relatorio;
    private Object cnpj;
    private Object empresa;
    private Object convencao;
    private Object grupo;
    private Object telefone;
    private Object logradouro;
    private Object endereco;
    private Object complemento;
    private Object bairro;
    private Object cidade;
    private Object uf;
    private Object cep;
    private Object email;
    private Object escritorio;
    private Object escritorio_telefone;
    private Object escritorio_email;
    private Object escritorio_logradouro;
    private Object escritorio_endereco;
    private Object escritorio_complemento;
    private Object escritorio_bairro;
    private Object escritorio_cidade;
    private Object escritorio_uf;
    private Object escritorio_cep;
    private Object id;
    private Object escritorio_id;
    private Object quantidade;
    private Object numero;
    private Object escritorio_numero;

    public ParametroRaisNaoEnviadasRelatorio() {
        this.detalhes_relatorio = null;
        this.cnpj = null;
        this.empresa = null;
        this.convencao = null;
        this.grupo = null;
        this.telefone = null;
        this.logradouro = null;
        this.endereco = null;
        this.complemento = null;
        this.bairro = null;
        this.cidade = null;
        this.uf = null;
        this.cep = null;
        this.email = null;
        this.escritorio = null;
        this.escritorio_telefone = null;
        this.escritorio_email = null;
        this.escritorio_logradouro = null;
        this.escritorio_endereco = null;
        this.escritorio_numero = null;
        this.escritorio_complemento = null;
        this.escritorio_bairro = null;
        this.escritorio_cidade = null;
        this.escritorio_uf = null;
        this.escritorio_cep = null;
        this.id = 0;
        this.escritorio_id = 0;
        this.quantidade = 0;
    }

    public ParametroRaisNaoEnviadasRelatorio(Object detalhes_relatorio, Object escritorio, Object escritorio_telefone, Object escritorio_email, Object escritorio_id, Object quantidade, Object escritorio_logradouro, Object escritorio_endereco, Object escritorio_complemento, Object escritorio_bairro, Object escritorio_cidade, Object escritorio_uf, Object escritorio_cep, Object escritorio_numero) {
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

    public ParametroRaisNaoEnviadasRelatorio(Object detalhes_relatorio, Object cnpj, Object empresa, Object convencao, Object grupo, Object telefone, Object logradouro, Object endereco, Object complemento, Object bairro, Object cidade, Object uf, Object cep, Object email, Object escritorio, Object escritorio_telefone, Object escritorio_email, Object escritorio_logradouro, Object escritorio_endereco, Object escritorio_complemento, Object escritorio_bairro, Object escritorio_cidade, Object escritorio_uf, Object escritorio_cep, Object id, Object escritorio_id, Object quantidade, Object numero, Object escritorio_numero) {
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

    public Object getDetalhes_relatorio() {
        return detalhes_relatorio;
    }

    public void setDetalhes_relatorio(Object detalhes_relatorio) {
        this.detalhes_relatorio = detalhes_relatorio;
    }

    public Object getCnpj() {
        return cnpj;
    }

    public void setCnpj(Object cnpj) {
        this.cnpj = cnpj;
    }

    public Object getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Object empresa) {
        this.empresa = empresa;
    }

    public Object getConvencao() {
        return convencao;
    }

    public void setConvencao(Object convencao) {
        this.convencao = convencao;
    }

    public Object getGrupo() {
        return grupo;
    }

    public void setGrupo(Object grupo) {
        this.grupo = grupo;
    }

    public Object getTelefone() {
        return telefone;
    }

    public void setTelefone(Object telefone) {
        this.telefone = telefone;
    }

    public Object getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(Object logradouro) {
        this.logradouro = logradouro;
    }

    public Object getEndereco() {
        return endereco;
    }

    public void setEndereco(Object endereco) {
        this.endereco = endereco;
    }

    public Object getComplemento() {
        return complemento;
    }

    public void setComplemento(Object complemento) {
        this.complemento = complemento;
    }

    public Object getBairro() {
        return bairro;
    }

    public void setBairro(Object bairro) {
        this.bairro = bairro;
    }

    public Object getCidade() {
        return cidade;
    }

    public void setCidade(Object cidade) {
        this.cidade = cidade;
    }

    public Object getUf() {
        return uf;
    }

    public void setUf(Object uf) {
        this.uf = uf;
    }

    public Object getCep() {
        return cep;
    }

    public void setCep(Object cep) {
        this.cep = cep;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getEscritorio() {
        return escritorio;
    }

    public void setEscritorio(Object escritorio) {
        this.escritorio = escritorio;
    }

    public Object getEscritorio_telefone() {
        return escritorio_telefone;
    }

    public void setEscritorio_telefone(Object escritorio_telefone) {
        this.escritorio_telefone = escritorio_telefone;
    }

    public Object getEscritorio_email() {
        return escritorio_email;
    }

    public void setEscritorio_email(Object escritorio_email) {
        this.escritorio_email = escritorio_email;
    }

    public Object getEscritorio_logradouro() {
        return escritorio_logradouro;
    }

    public void setEscritorio_logradouro(Object escritorio_logradouro) {
        this.escritorio_logradouro = escritorio_logradouro;
    }

    public Object getEscritorio_endereco() {
        return escritorio_endereco;
    }

    public void setEscritorio_endereco(Object escritorio_endereco) {
        this.escritorio_endereco = escritorio_endereco;
    }

    public Object getEscritorio_bairro() {
        return escritorio_bairro;
    }

    public void setEscritorio_bairro(Object escritorio_bairro) {
        this.escritorio_bairro = escritorio_bairro;
    }

    public Object getEscritorio_cidade() {
        return escritorio_cidade;
    }

    public void setEscritorio_cidade(Object escritorio_cidade) {
        this.escritorio_cidade = escritorio_cidade;
    }

    public Object getEscritorio_uf() {
        return escritorio_uf;
    }

    public void setEscritorio_uf(Object escritorio_uf) {
        this.escritorio_uf = escritorio_uf;
    }

    public Object getEscritorio_cep() {
        return escritorio_cep;
    }

    public void setEscritorio_cep(Object escritorio_cep) {
        this.escritorio_cep = escritorio_cep;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getEscritorio_id() {
        return escritorio_id;
    }

    public void setEscritorio_id(Object escritorio_id) {
        this.escritorio_id = escritorio_id;
    }

    public Object getEscritorio_complemento() {
        return escritorio_complemento;
    }

    public void setEscritorio_complemento(Object escritorio_complemento) {
        this.escritorio_complemento = escritorio_complemento;
    }

    public Object getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Object quantidade) {
        this.quantidade = quantidade;
    }

    public Object getEscritorio_numero() {
        return escritorio_numero;
    }

    public void setEscritorio_numero(Object escritorio_numero) {
        this.escritorio_numero = escritorio_numero;
    }

    public Object getNumero() {
        return numero;
    }

    public void setNumero(Object numero) {
        this.numero = numero;
    }

}
