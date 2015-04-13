//package br.com.rtools.financeiro.beans;
//
//import java.io.Serializable;
//import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EntityResult;
//import javax.persistence.FieldResult;
//import javax.persistence.Id;
//import javax.persistence.SqlResultSetMapping;
//import javax.persistence.SqlResultSetMappings;
//import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
//
//@Entity()
////@Table(name = "EMPLOYEE_NATIVE_SQL_RESULTSET_MAPPING_SOCIOS")
////@SqlResultSetMappings({
////    @SqlResultSetMapping(name = "myMapping", entities = {
////        @EntityResult(entityClass = SociosResult.class, fields = {
////            @FieldResult(
////                    name = "socio_codigo", column = "nome"),
////                    name = "socio_codigo", column = "nome")
////        }
////        )
////    }),
////    @SqlResultSetMapping(name = "myMapping2", entities = {
////        @EntityResult(entityClass = SociosResult.class, fields = {
////            @FieldResult(name = "codsocio", column = "socio_codigo")}
////        )})
////})
//public class SociosResult implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//    @Id()
//    private Integer seq;
//    @Column
//    private Integer ordem;
//    @Column
//    private Integer socio_codigo;
//    @Column
//    private String socio_nome;
//    @Column
//    private Integer socio_matricula;
//    @Column
//    @Temporal(TemporalType.DATE)
//    private Date socio_data_cadastro;
//    @Column
//    @Temporal(TemporalType.DATE)
//    private Date socio_recadastro;
//    @Column
//    private String socio_grupo;
//    @Column
//    private String socio_categoria;
//    @Column
//    @Temporal(TemporalType.DATE)
//    private Date socio_data_nascimento;
//    @Column
//    private String socio_naturalidade;
//    @Column
//    private String socio_nacionalidade;
//    @Column
//    private String socio_rg;
//    @Column
//    private String socio_cpf;
//    @Column
//    private String socio_ctps;
//    @Column
//    private String socio_serie_ctps;
//    @Column
//    private String socio_estado_civil;
//    @Column
//    private String socio_pai;
//    @Column
//    private String socio_mae;
//    @Column
//    private String socio_telefone;
//    @Column
//    private String socio_celular;
//    @Column
//    private String socio_email;
//    @Column
//    private String socio_logradouro;
//    @Column
//    private String socio_endereco;
//    @Column
//    private String socio_numero;
//    @Column
//    private String socio_complemento;
//    @Column
//    private String socio_bairro;
//    @Column
//    private String socio_cidade;
//    @Column
//    private String socio_uf;
//    @Column
//    private String socio_cep;
//    @Column
//    private String dest_cnpj;
//    @Column
//    private String dest_nome;
//    @Column
//    private String dest_logradouro;
//    @Column
//    private String dest_endereco;
//    @Column
//    private String dest_numero;
//    @Column
//    private String dest_complemento;
//    @Column
//    private String dest_bairro;
//    @Column
//    private String dest_cidade;
//    @Column
//    private String dest_uf;
//    @Column
//    private String dest_cep;
//    @Column
//    private String empresa_nome;
//    @Column
//    private String empresa_telefone;
//    @Column
//    private String empresa_fax;
//    @Column
//    private String empresa_admissao;
//    @Column
//    private String empresa_cargo;
//    @Column
//    private String empresa_endereco;
//    @Column
//    private String empresa_numero;
//    @Column
//    private String empresa_complemento;
//    @Column
//    private String empresa_bairro;
//    @Column
//    private String empresa_cidade;
//    @Column
//    private String empresa_uf;
//    @Column
//    private String empresa_cep;
//    @Column
//    private String imagem;
//    @Column
//    private String obs;
//    @Column
//    private String socio_grau;
//    @Column
//    private String entidade;
//    @Column
//    private String entidade_endereco;
//    @Column
//    private String entidade_numero;
//    @Column
//    private String entidade_complemento;
//    @Column
//    private String entidade_bairro;
//    @Column
//    private String entidade_cidade;
//    @Column
//    private String entidade_uf;
//    @Column
//    private String entidade_cep;
//    @Column
//    private String entidade_cnpj;
//    @Column
//    private String texto;
//    @Column
//    private String imagem2;
//    @Column
//    private String foto;
//    @Column
//    private String entidade_email;
//    @Column
//    private String entidade_site;
//    @Column
//    private String entidade_telefone;
//    @Column
//    private String caminho_verso;
//    @Column
//    private String cnpj;
//    @Column
//    private String data_recadastro;
//    @Column
//    private String fantasia;
//    @Column
//    private String entidade_logradouro;
//    @Column
//    private String empresa_logradouro;
//    @Column
//    private String assinatura;
//    @Column
//    private String empresa_codigo;
//
//    public SociosResult() {
//        this.seq = null;
//        this.ordem = 0;
//        this.socio_codigo = 0;
//        this.socio_nome = "";
//        this.socio_matricula = 0;
//        this.socio_data_cadastro = null;
//        this.socio_recadastro = null;
//        this.socio_grupo = "";
//        this.socio_categoria = "";
//        this.socio_data_nascimento = null;
//        this.socio_naturalidade = "";
//        this.socio_nacionalidade = "";
//        this.socio_rg = "";
//        this.socio_cpf = "";
//        this.socio_ctps = "";
//        this.socio_serie_ctps = "";
//        this.socio_estado_civil = "";
//        this.socio_pai = "";
//        this.socio_mae = "";
//        this.socio_telefone = "";
//        this.socio_celular = "";
//        this.socio_email = "";
//        this.socio_logradouro = "";
//        this.socio_endereco = "";
//        this.socio_numero = "";
//        this.socio_complemento = "";
//        this.socio_bairro = "";
//        this.socio_cidade = "";
//        this.socio_uf = "";
//        this.socio_cep = "";
//        this.dest_cnpj = "";
//        this.dest_nome = "";
//        this.dest_logradouro = "";
//        this.dest_endereco = "";
//        this.dest_numero = "";
//        this.dest_complemento = "";
//        this.dest_bairro = "";
//        this.dest_cidade = "";
//        this.dest_uf = "";
//        this.dest_cep = "";
//        this.empresa_nome = "";
//        this.empresa_telefone = "";
//        this.empresa_fax = "";
//        this.empresa_admissao = "";
//        this.empresa_cargo = "";
//        this.empresa_endereco = "";
//        this.empresa_numero = "";
//        this.empresa_complemento = "";
//        this.empresa_bairro = "";
//        this.empresa_cidade = "";
//        this.empresa_uf = "";
//        this.empresa_cep = "";
//        this.imagem = "";
//        this.obs = "";
//        this.socio_grau = "";
//        this.entidade = "";
//        this.entidade_endereco = "";
//        this.entidade_numero = "";
//        this.entidade_complemento = "";
//        this.entidade_bairro = "";
//        this.entidade_cidade = "";
//        this.entidade_uf = "";
//        this.entidade_cep = "";
//        this.entidade_cnpj = "";
//        this.texto = "";
//        this.imagem2 = "";
//        this.foto = "";
//        this.entidade_email = "";
//        this.entidade_site = "";
//        this.entidade_telefone = "";
//        this.caminho_verso = "";
//        this.cnpj = "";
//        this.data_recadastro = "";
//        this.fantasia = "";
//        this.entidade_logradouro = "";
//        this.empresa_logradouro = "";
//        this.assinatura = "";
//        this.empresa_codigo = "";
//    }
//
//    public SociosResult(Integer seq, Integer ordem, Integer socio_codigo, String socio_nome, Integer socio_matricula, Date socio_data_cadastro, Date socio_recadastro, String socio_grupo, String socio_categoria, Date socio_data_nascimento, String socio_naturalidade, String socio_nacionalidade, String socio_rg, String socio_cpf, String socio_ctps, String socio_serie_ctps, String socio_estado_civil, String socio_pai, String socio_mae, String socio_telefone, String socio_celular, String socio_email, String socio_logradouro, String socio_endereco, String socio_numero, String socio_complemento, String socio_bairro, String socio_cidade, String socio_uf, String socio_cep, String dest_cnpj, String dest_nome, String dest_logradouro, String dest_endereco, String dest_numero, String dest_complemento, String dest_bairro, String dest_cidade, String dest_uf, String dest_cep, String empresa_nome, String empresa_telefone, String empresa_fax, String empresa_admissao, String empresa_cargo, String empresa_endereco, String empresa_numero, String empresa_complemento, String empresa_bairro, String empresa_cidade, String empresa_uf, String empresa_cep, String imagem, String obs, String socio_grau, String entidade, String entidade_endereco, String entidade_numero, String entidade_complemento, String entidade_bairro, String entidade_cidade, String entidade_uf, String entidade_cep, String entidade_cnpj, String texto, String imagem2, String foto, String entidade_email, String entidade_site, String entidade_telefone, String caminho_verso, String cnpj, String data_recadastro, String fantasia, String entidade_logradouro, String empresa_logradouro, String assinatura, String empresa_codigo) {
//        this.seq = seq;
//        this.ordem = ordem;
//        this.socio_codigo = socio_codigo;
//        this.socio_nome = socio_nome;
//        this.socio_matricula = socio_matricula;
//        this.socio_data_cadastro = socio_data_cadastro;
//        this.socio_recadastro = socio_recadastro;
//        this.socio_grupo = socio_grupo;
//        this.socio_categoria = socio_categoria;
//        this.socio_data_nascimento = socio_data_nascimento;
//        this.socio_naturalidade = socio_naturalidade;
//        this.socio_nacionalidade = socio_nacionalidade;
//        this.socio_rg = socio_rg;
//        this.socio_cpf = socio_cpf;
//        this.socio_ctps = socio_ctps;
//        this.socio_serie_ctps = socio_serie_ctps;
//        this.socio_estado_civil = socio_estado_civil;
//        this.socio_pai = socio_pai;
//        this.socio_mae = socio_mae;
//        this.socio_telefone = socio_telefone;
//        this.socio_celular = socio_celular;
//        this.socio_email = socio_email;
//        this.socio_logradouro = socio_logradouro;
//        this.socio_endereco = socio_endereco;
//        this.socio_numero = socio_numero;
//        this.socio_complemento = socio_complemento;
//        this.socio_bairro = socio_bairro;
//        this.socio_cidade = socio_cidade;
//        this.socio_uf = socio_uf;
//        this.socio_cep = socio_cep;
//        this.dest_cnpj = dest_cnpj;
//        this.dest_nome = dest_nome;
//        this.dest_logradouro = dest_logradouro;
//        this.dest_endereco = dest_endereco;
//        this.dest_numero = dest_numero;
//        this.dest_complemento = dest_complemento;
//        this.dest_bairro = dest_bairro;
//        this.dest_cidade = dest_cidade;
//        this.dest_uf = dest_uf;
//        this.dest_cep = dest_cep;
//        this.empresa_nome = empresa_nome;
//        this.empresa_telefone = empresa_telefone;
//        this.empresa_fax = empresa_fax;
//        this.empresa_admissao = empresa_admissao;
//        this.empresa_cargo = empresa_cargo;
//        this.empresa_endereco = empresa_endereco;
//        this.empresa_numero = empresa_numero;
//        this.empresa_complemento = empresa_complemento;
//        this.empresa_bairro = empresa_bairro;
//        this.empresa_cidade = empresa_cidade;
//        this.empresa_uf = empresa_uf;
//        this.empresa_cep = empresa_cep;
//        this.imagem = imagem;
//        this.obs = obs;
//        this.socio_grau = socio_grau;
//        this.entidade = entidade;
//        this.entidade_endereco = entidade_endereco;
//        this.entidade_numero = entidade_numero;
//        this.entidade_complemento = entidade_complemento;
//        this.entidade_bairro = entidade_bairro;
//        this.entidade_cidade = entidade_cidade;
//        this.entidade_uf = entidade_uf;
//        this.entidade_cep = entidade_cep;
//        this.entidade_cnpj = entidade_cnpj;
//        this.texto = texto;
//        this.imagem2 = imagem2;
//        this.foto = foto;
//        this.entidade_email = entidade_email;
//        this.entidade_site = entidade_site;
//        this.entidade_telefone = entidade_telefone;
//        this.caminho_verso = caminho_verso;
//        this.cnpj = cnpj;
//        this.data_recadastro = data_recadastro;
//        this.fantasia = fantasia;
//        this.entidade_logradouro = entidade_logradouro;
//        this.empresa_logradouro = empresa_logradouro;
//        this.assinatura = assinatura;
//        this.empresa_codigo = empresa_codigo;
//    }
//
//    public Integer getSeq() {
//        return seq;
//    }
//
//    public void setSeq(Integer seq) {
//        this.seq = seq;
//    }
//
//    public Integer getOrdem() {
//        return ordem;
//    }
//
//    public void setOrdem(Integer ordem) {
//        this.ordem = ordem;
//    }
//
//    public Integer getSocio_codigo() {
//        return socio_codigo;
//    }
//
//    public void setSocio_codigo(Integer socio_codigo) {
//        this.socio_codigo = socio_codigo;
//    }
//
//    public Integer getSocio_matricula() {
//        return socio_matricula;
//    }
//
//    public void setSocio_matricula(Integer socio_matricula) {
//        this.socio_matricula = socio_matricula;
//    }
//
//    public Date getSocio_data_cadastro() {
//        return socio_data_cadastro;
//    }
//
//    public void setSocio_data_cadastro(Date socio_data_cadastro) {
//        this.socio_data_cadastro = socio_data_cadastro;
//    }
//
//    public Date getSocio_recadastro() {
//        return socio_recadastro;
//    }
//
//    public void setSocio_recadastro(Date socio_recadastro) {
//        this.socio_recadastro = socio_recadastro;
//    }
//
//    public String getSocio_grupo() {
//        return socio_grupo;
//    }
//
//    public void setSocio_grupo(String socio_grupo) {
//        this.socio_grupo = socio_grupo;
//    }
//
//    public String getSocio_categoria() {
//        return socio_categoria;
//    }
//
//    public void setSocio_categoria(String socio_categoria) {
//        this.socio_categoria = socio_categoria;
//    }
//
//    public Date getSocio_data_nascimento() {
//        return socio_data_nascimento;
//    }
//
//    public void setSocio_data_nascimento(Date socio_data_nascimento) {
//        this.socio_data_nascimento = socio_data_nascimento;
//    }
//
//    public String getSocio_naturalidade() {
//        return socio_naturalidade;
//    }
//
//    public void setSocio_naturalidade(String socio_naturalidade) {
//        this.socio_naturalidade = socio_naturalidade;
//    }
//
//    public String getSocio_nacionalidade() {
//        return socio_nacionalidade;
//    }
//
//    public void setSocio_nacionalidade(String socio_nacionalidade) {
//        this.socio_nacionalidade = socio_nacionalidade;
//    }
//
//    public String getSocio_rg() {
//        return socio_rg;
//    }
//
//    public void setSocio_rg(String socio_rg) {
//        this.socio_rg = socio_rg;
//    }
//
//    public String getSocio_cpf() {
//        return socio_cpf;
//    }
//
//    public void setSocio_cpf(String socio_cpf) {
//        this.socio_cpf = socio_cpf;
//    }
//
//    public String getSocio_ctps() {
//        return socio_ctps;
//    }
//
//    public void setSocio_ctps(String socio_ctps) {
//        this.socio_ctps = socio_ctps;
//    }
//
//    public String getSocio_serie_ctps() {
//        return socio_serie_ctps;
//    }
//
//    public void setSocio_serie_ctps(String socio_serie_ctps) {
//        this.socio_serie_ctps = socio_serie_ctps;
//    }
//
//    public String getSocio_estado_civil() {
//        return socio_estado_civil;
//    }
//
//    public void setSocio_estado_civil(String socio_estado_civil) {
//        this.socio_estado_civil = socio_estado_civil;
//    }
//
//    public String getSocio_pai() {
//        return socio_pai;
//    }
//
//    public void setSocio_pai(String socio_pai) {
//        this.socio_pai = socio_pai;
//    }
//
//    public String getSocio_mae() {
//        return socio_mae;
//    }
//
//    public void setSocio_mae(String socio_mae) {
//        this.socio_mae = socio_mae;
//    }
//
//    public String getSocio_telefone() {
//        return socio_telefone;
//    }
//
//    public void setSocio_telefone(String socio_telefone) {
//        this.socio_telefone = socio_telefone;
//    }
//
//    public String getSocio_celular() {
//        return socio_celular;
//    }
//
//    public void setSocio_celular(String socio_celular) {
//        this.socio_celular = socio_celular;
//    }
//
//    public String getSocio_email() {
//        return socio_email;
//    }
//
//    public void setSocio_email(String socio_email) {
//        this.socio_email = socio_email;
//    }
//
//    public String getSocio_logradouro() {
//        return socio_logradouro;
//    }
//
//    public void setSocio_logradouro(String socio_logradouro) {
//        this.socio_logradouro = socio_logradouro;
//    }
//
//    public String getSocio_endereco() {
//        return socio_endereco;
//    }
//
//    public void setSocio_endereco(String socio_endereco) {
//        this.socio_endereco = socio_endereco;
//    }
//
//    public String getSocio_numero() {
//        return socio_numero;
//    }
//
//    public void setSocio_numero(String socio_numero) {
//        this.socio_numero = socio_numero;
//    }
//
//    public String getSocio_complemento() {
//        return socio_complemento;
//    }
//
//    public void setSocio_complemento(String socio_complemento) {
//        this.socio_complemento = socio_complemento;
//    }
//
//    public String getSocio_bairro() {
//        return socio_bairro;
//    }
//
//    public void setSocio_bairro(String socio_bairro) {
//        this.socio_bairro = socio_bairro;
//    }
//
//    public String getSocio_cidade() {
//        return socio_cidade;
//    }
//
//    public void setSocio_cidade(String socio_cidade) {
//        this.socio_cidade = socio_cidade;
//    }
//
//    public String getSocio_uf() {
//        return socio_uf;
//    }
//
//    public void setSocio_uf(String socio_uf) {
//        this.socio_uf = socio_uf;
//    }
//
//    public String getSocio_cep() {
//        return socio_cep;
//    }
//
//    public void setSocio_cep(String socio_cep) {
//        this.socio_cep = socio_cep;
//    }
//
//    public String getDest_cnpj() {
//        return dest_cnpj;
//    }
//
//    public void setDest_cnpj(String dest_cnpj) {
//        this.dest_cnpj = dest_cnpj;
//    }
//
//    public String getDest_nome() {
//        return dest_nome;
//    }
//
//    public void setDest_nome(String dest_nome) {
//        this.dest_nome = dest_nome;
//    }
//
//    public String getDest_logradouro() {
//        return dest_logradouro;
//    }
//
//    public void setDest_logradouro(String dest_logradouro) {
//        this.dest_logradouro = dest_logradouro;
//    }
//
//    public String getDest_endereco() {
//        return dest_endereco;
//    }
//
//    public void setDest_endereco(String dest_endereco) {
//        this.dest_endereco = dest_endereco;
//    }
//
//    public String getDest_numero() {
//        return dest_numero;
//    }
//
//    public void setDest_numero(String dest_numero) {
//        this.dest_numero = dest_numero;
//    }
//
//    public String getDest_complemento() {
//        return dest_complemento;
//    }
//
//    public void setDest_complemento(String dest_complemento) {
//        this.dest_complemento = dest_complemento;
//    }
//
//    public String getDest_bairro() {
//        return dest_bairro;
//    }
//
//    public void setDest_bairro(String dest_bairro) {
//        this.dest_bairro = dest_bairro;
//    }
//
//    public String getDest_cidade() {
//        return dest_cidade;
//    }
//
//    public void setDest_cidade(String dest_cidade) {
//        this.dest_cidade = dest_cidade;
//    }
//
//    public String getDest_uf() {
//        return dest_uf;
//    }
//
//    public void setDest_uf(String dest_uf) {
//        this.dest_uf = dest_uf;
//    }
//
//    public String getDest_cep() {
//        return dest_cep;
//    }
//
//    public void setDest_cep(String dest_cep) {
//        this.dest_cep = dest_cep;
//    }
//
//    public String getEmpresa_nome() {
//        return empresa_nome;
//    }
//
//    public void setEmpresa_nome(String empresa_nome) {
//        this.empresa_nome = empresa_nome;
//    }
//
//    public String getEmpresa_telefone() {
//        return empresa_telefone;
//    }
//
//    public void setEmpresa_telefone(String empresa_telefone) {
//        this.empresa_telefone = empresa_telefone;
//    }
//
//    public String getEmpresa_fax() {
//        return empresa_fax;
//    }
//
//    public void setEmpresa_fax(String empresa_fax) {
//        this.empresa_fax = empresa_fax;
//    }
//
//    public String getEmpresa_admissao() {
//        return empresa_admissao;
//    }
//
//    public void setEmpresa_admissao(String empresa_admissao) {
//        this.empresa_admissao = empresa_admissao;
//    }
//
//    public String getEmpresa_cargo() {
//        return empresa_cargo;
//    }
//
//    public void setEmpresa_cargo(String empresa_cargo) {
//        this.empresa_cargo = empresa_cargo;
//    }
//
//    public String getEmpresa_endereco() {
//        return empresa_endereco;
//    }
//
//    public void setEmpresa_endereco(String empresa_endereco) {
//        this.empresa_endereco = empresa_endereco;
//    }
//
//    public String getEmpresa_numero() {
//        return empresa_numero;
//    }
//
//    public void setEmpresa_numero(String empresa_numero) {
//        this.empresa_numero = empresa_numero;
//    }
//
//    public String getEmpresa_complemento() {
//        return empresa_complemento;
//    }
//
//    public void setEmpresa_complemento(String empresa_complemento) {
//        this.empresa_complemento = empresa_complemento;
//    }
//
//    public String getEmpresa_bairro() {
//        return empresa_bairro;
//    }
//
//    public void setEmpresa_bairro(String empresa_bairro) {
//        this.empresa_bairro = empresa_bairro;
//    }
//
//    public String getEmpresa_cidade() {
//        return empresa_cidade;
//    }
//
//    public void setEmpresa_cidade(String empresa_cidade) {
//        this.empresa_cidade = empresa_cidade;
//    }
//
//    public String getEmpresa_uf() {
//        return empresa_uf;
//    }
//
//    public void setEmpresa_uf(String empresa_uf) {
//        this.empresa_uf = empresa_uf;
//    }
//
//    public String getEmpresa_cep() {
//        return empresa_cep;
//    }
//
//    public void setEmpresa_cep(String empresa_cep) {
//        this.empresa_cep = empresa_cep;
//    }
//
//    public String getImagem() {
//        return imagem;
//    }
//
//    public void setImagem(String imagem) {
//        this.imagem = imagem;
//    }
//
//    public String getObs() {
//        return obs;
//    }
//
//    public void setObs(String obs) {
//        this.obs = obs;
//    }
//
//    public String getSocio_grau() {
//        return socio_grau;
//    }
//
//    public void setSocio_grau(String socio_grau) {
//        this.socio_grau = socio_grau;
//    }
//
//    public String getEntidade() {
//        return entidade;
//    }
//
//    public void setEntidade(String entidade) {
//        this.entidade = entidade;
//    }
//
//    public String getEntidade_endereco() {
//        return entidade_endereco;
//    }
//
//    public void setEntidade_endereco(String entidade_endereco) {
//        this.entidade_endereco = entidade_endereco;
//    }
//
//    public String getEntidade_numero() {
//        return entidade_numero;
//    }
//
//    public void setEntidade_numero(String entidade_numero) {
//        this.entidade_numero = entidade_numero;
//    }
//
//    public String getEntidade_complemento() {
//        return entidade_complemento;
//    }
//
//    public void setEntidade_complemento(String entidade_complemento) {
//        this.entidade_complemento = entidade_complemento;
//    }
//
//    public String getEntidade_bairro() {
//        return entidade_bairro;
//    }
//
//    public void setEntidade_bairro(String entidade_bairro) {
//        this.entidade_bairro = entidade_bairro;
//    }
//
//    public String getEntidade_cidade() {
//        return entidade_cidade;
//    }
//
//    public void setEntidade_cidade(String entidade_cidade) {
//        this.entidade_cidade = entidade_cidade;
//    }
//
//    public String getEntidade_uf() {
//        return entidade_uf;
//    }
//
//    public void setEntidade_uf(String entidade_uf) {
//        this.entidade_uf = entidade_uf;
//    }
//
//    public String getEntidade_cep() {
//        return entidade_cep;
//    }
//
//    public void setEntidade_cep(String entidade_cep) {
//        this.entidade_cep = entidade_cep;
//    }
//
//    public String getEntidade_cnpj() {
//        return entidade_cnpj;
//    }
//
//    public void setEntidade_cnpj(String entidade_cnpj) {
//        this.entidade_cnpj = entidade_cnpj;
//    }
//
//    public String getTexto() {
//        return texto;
//    }
//
//    public void setTexto(String texto) {
//        this.texto = texto;
//    }
//
//    public String getImagem2() {
//        return imagem2;
//    }
//
//    public void setImagem2(String imagem2) {
//        this.imagem2 = imagem2;
//    }
//
//    public String getFoto() {
//        return foto;
//    }
//
//    public void setFoto(String foto) {
//        this.foto = foto;
//    }
//
//    public String getEntidade_email() {
//        return entidade_email;
//    }
//
//    public void setEntidade_email(String entidade_email) {
//        this.entidade_email = entidade_email;
//    }
//
//    public String getEntidade_site() {
//        return entidade_site;
//    }
//
//    public void setEntidade_site(String entidade_site) {
//        this.entidade_site = entidade_site;
//    }
//
//    public String getEntidade_telefone() {
//        return entidade_telefone;
//    }
//
//    public void setEntidade_telefone(String entidade_telefone) {
//        this.entidade_telefone = entidade_telefone;
//    }
//
//    public String getCaminho_verso() {
//        return caminho_verso;
//    }
//
//    public void setCaminho_verso(String caminho_verso) {
//        this.caminho_verso = caminho_verso;
//    }
//
//    public String getCnpj() {
//        return cnpj;
//    }
//
//    public void setCnpj(String cnpj) {
//        this.cnpj = cnpj;
//    }
//
//    public String getData_recadastro() {
//        return data_recadastro;
//    }
//
//    public void setData_recadastro(String data_recadastro) {
//        this.data_recadastro = data_recadastro;
//    }
//
//    public String getFantasia() {
//        return fantasia;
//    }
//
//    public void setFantasia(String fantasia) {
//        this.fantasia = fantasia;
//    }
//
//    public String getEntidade_logradouro() {
//        return entidade_logradouro;
//    }
//
//    public void setEntidade_logradouro(String entidade_logradouro) {
//        this.entidade_logradouro = entidade_logradouro;
//    }
//
//    public String getEmpresa_logradouro() {
//        return empresa_logradouro;
//    }
//
//    public void setEmpresa_logradouro(String empresa_logradouro) {
//        this.empresa_logradouro = empresa_logradouro;
//    }
//
//    public String getAssinatura() {
//        return assinatura;
//    }
//
//    public void setAssinatura(String assinatura) {
//        this.assinatura = assinatura;
//    }
//
//    public String getEmpresa_codigo() {
//        return empresa_codigo;
//    }
//
//    public void setEmpresa_codigo(String empresa_codigo) {
//        this.empresa_codigo = empresa_codigo;
//    }
//
//    public String getSocio_nome() {
//        return socio_nome;
//    }
//
//    public void setSocio_nome(String socio_nome) {
//        this.socio_nome = socio_nome;
//    }
//
//}
//
////    @Override
////    public String toString() {
////        StringBuilder sb = new StringBuilder();
////        sb.append("codsocio: ").append(codsocio);
////        sb.append(", nome: ").append(nome);
////        return sb.toString();
////    }
