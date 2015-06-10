package br.com.rtools.impressao;

import java.math.BigDecimal;

public class ParametroBoleto {

    private String ref;
    private boolean imprimeVerso;
    private String escritorio;
    private String contribuicao;
    private String tipo;
    private String grupo;
    private String cgc;
    private String sacado;
    private BigDecimal valor;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String boleto;
    private String sacado_endereco;
    private String sacado_numero;
    private String sacado_complemento;
    private String sacado_bairro;
    private String sacado_cidade;
    private String sacado_estado;
    private String sacado_cep;
    private String nossonum;
    private String datadoc;
    private String VENCIMENTO;
    private String codbanco;
    private String moeda;
    private String especie;
    private String especie_doc;
    private String cod_agencia;
    private String codcedente;
    private String aceite;
    private String carteira;
    private String exercicio;
    private String nomeentidade;
    private String LAYOUT;
    private String mensagem;
    private String local_pag;
    private String endent;
    private String nument;
    private String compent;
    private String baient;
    private String cident;
    private String estent;
    private String cepent;
    private String cgcent;
    private String REPNUM;
    private String CODBAR;
    private String mensagem_boleto;
    private String logoBanco;
    private String logoEmpresa;
    private String serrilha;
    private String cnae;
    private String categoria;
    private String codigosindical;
    private String usoBanco;
    private String textoTitulo;
    private String caminhoVerso;
    private String entidade;
    private String descricaoServico;

    public ParametroBoleto(
            String ref,
            boolean imprimeVerso,
            String escritorio,
            String contribuicao,
            String tipo,
            String grupo,
            String cgc,
            String sacado,
            BigDecimal valor,
            String endereco,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String estado,
            String cep,
            String boleto,
            String sacado_endereco,
            String sacado_numero,
            String sacado_complemento,
            String sacado_bairro,
            String sacado_cidade,
            String sacado_estado,
            String sacado_cep,
            String nossonum,
            String datadoc,
            String VENCIMENTO,
            String codbanco,
            String moeda,
            String especie,
            String especie_doc,
            String cod_agencia,
            String codcedente,
            String aceite,
            String carteira,
            String exercicio,
            String nomeentidade,
            String LAYOUT,
            String mensagem,
            String local_pag,
            String endent,
            String nument,
            String compent,
            String baient,
            String cident,
            String estent,
            String cepent,
            String cgcent,
            String REPNUM,
            String CODBAR,
            String mensagem_boleto,
            String logoBanco,
            String logoEmpresa,
            String serrilha,
            String cnae,
            String categoria,
            String codigosindical,
            String usoBanco,
            String textoTitulo,
            String caminhoVerso,
            String entidade,
            String descricaoServico) {
        this.ref = ref;
        this.imprimeVerso = imprimeVerso;
        this.escritorio = escritorio;
        this.contribuicao = contribuicao;
        this.tipo = tipo;
        this.grupo = grupo;
        this.cgc = cgc;
        this.sacado = sacado;
        this.valor = valor;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.boleto = boleto;
        this.sacado_endereco = sacado_endereco;
        this.sacado_numero = sacado_numero;
        this.sacado_complemento = sacado_complemento;
        this.sacado_bairro = sacado_bairro;
        this.sacado_cidade = sacado_cidade;
        this.sacado_estado = sacado_estado;
        this.sacado_cep = sacado_cep;
        this.nossonum = nossonum;
        this.datadoc = datadoc;
        this.VENCIMENTO = VENCIMENTO;
        this.codbanco = codbanco;
        this.moeda = moeda;
        this.especie = especie;
        this.especie_doc = especie_doc;
        this.cod_agencia = cod_agencia;
        this.codcedente = codcedente;
        this.aceite = aceite;
        this.carteira = carteira;
        this.exercicio = exercicio;
        this.nomeentidade = nomeentidade;
        this.LAYOUT = LAYOUT;
        this.mensagem = mensagem;
        this.local_pag = local_pag;
        this.endent = endent;
        this.nument = nument;
        this.compent = compent;
        this.baient = baient;
        this.cident = cident;
        this.estent = estent;
        this.cepent = cepent;
        this.cgcent = cgcent;
        this.REPNUM = REPNUM;
        this.CODBAR = CODBAR;
        this.mensagem_boleto = mensagem_boleto;
        this.logoBanco = logoBanco;
        this.logoEmpresa = logoEmpresa;
        this.serrilha = serrilha;
        this.cnae = cnae;
        this.categoria = categoria;
        this.codigosindical = codigosindical;
        this.usoBanco = usoBanco;
        this.textoTitulo = textoTitulo;
        this.caminhoVerso = caminhoVerso;
        this.entidade = entidade;
        this.descricaoServico = descricaoServico;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public boolean isImprimeVerso() {
        return imprimeVerso;
    }

    public void setImprimeVerso(boolean imprimeVerso) {
        this.imprimeVerso = imprimeVerso;
    }

    public String getEscritorio() {
        return escritorio;
    }

    public void setEscritorio(String escritorio) {
        this.escritorio = escritorio;
    }

    public String getContribuicao() {
        return contribuicao;
    }

    public void setContribuicao(String contribuicao) {
        this.contribuicao = contribuicao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCgc() {
        return cgc;
    }

    public void setCgc(String cgc) {
        this.cgc = cgc;
    }

    public String getSacado() {
        return sacado;
    }

    public void setSacado(String sacado) {
        this.sacado = sacado;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getSacado_endereco() {
        return sacado_endereco;
    }

    public void setSacado_endereco(String sacado_endereco) {
        this.sacado_endereco = sacado_endereco;
    }

    public String getSacado_numero() {
        return sacado_numero;
    }

    public void setSacado_numero(String sacado_numero) {
        this.sacado_numero = sacado_numero;
    }

    public String getSacado_complemento() {
        return sacado_complemento;
    }

    public void setSacado_complemento(String sacado_complemento) {
        this.sacado_complemento = sacado_complemento;
    }

    public String getSacado_bairro() {
        return sacado_bairro;
    }

    public void setSacado_bairro(String sacado_bairro) {
        this.sacado_bairro = sacado_bairro;
    }

    public String getSacado_cidade() {
        return sacado_cidade;
    }

    public void setSacado_cidade(String sacado_cidade) {
        this.sacado_cidade = sacado_cidade;
    }

    public String getSacado_estado() {
        return sacado_estado;
    }

    public void setSacado_estado(String sacado_estado) {
        this.sacado_estado = sacado_estado;
    }

    public String getSacado_cep() {
        return sacado_cep;
    }

    public void setSacado_cep(String sacado_cep) {
        this.sacado_cep = sacado_cep;
    }

    public String getNossonum() {
        return nossonum;
    }

    public void setNossonum(String nossonum) {
        this.nossonum = nossonum;
    }

    public String getDatadoc() {
        return datadoc;
    }

    public void setDatadoc(String datadoc) {
        this.datadoc = datadoc;
    }

    public String getVENCIMENTO() {
        return VENCIMENTO;
    }

    public void setVENCIMENTO(String VENCIMENTO) {
        this.VENCIMENTO = VENCIMENTO;
    }

    public String getCodbanco() {
        return codbanco;
    }

    public void setCodbanco(String codbanco) {
        this.codbanco = codbanco;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getEspecie_doc() {
        return especie_doc;
    }

    public void setEspecie_doc(String especie_doc) {
        this.especie_doc = especie_doc;
    }

    public String getCod_agencia() {
        return cod_agencia;
    }

    public void setCod_agencia(String cod_agencia) {
        this.cod_agencia = cod_agencia;
    }

    public String getCodcedente() {
        return codcedente;
    }

    public void setCodcedente(String codcedente) {
        this.codcedente = codcedente;
    }

    public String getAceite() {
        return aceite;
    }

    public void setAceite(String aceite) {
        this.aceite = aceite;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public String getNomeentidade() {
        return nomeentidade;
    }

    public void setNomeentidade(String nomeentidade) {
        this.nomeentidade = nomeentidade;
    }

    public String getLAYOUT() {
        return LAYOUT;
    }

    public void setLAYOUT(String LAYOUT) {
        this.LAYOUT = LAYOUT;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getLocal_pag() {
        return local_pag;
    }

    public void setLocal_pag(String local_pag) {
        this.local_pag = local_pag;
    }

    public String getEndent() {
        return endent;
    }

    public void setEndent(String endent) {
        this.endent = endent;
    }

    public String getNument() {
        return nument;
    }

    public void setNument(String nument) {
        this.nument = nument;
    }

    public String getCompent() {
        return compent;
    }

    public void setCompent(String compent) {
        this.compent = compent;
    }

    public String getBaient() {
        return baient;
    }

    public void setBaient(String baient) {
        this.baient = baient;
    }

    public String getCident() {
        return cident;
    }

    public void setCident(String cident) {
        this.cident = cident;
    }

    public String getEstent() {
        return estent;
    }

    public void setEstent(String estent) {
        this.estent = estent;
    }

    public String getCepent() {
        return cepent;
    }

    public void setCepent(String cepent) {
        this.cepent = cepent;
    }

    public String getCgcent() {
        return cgcent;
    }

    public void setCgcent(String cgcent) {
        this.cgcent = cgcent;
    }

    public String getREPNUM() {
        return REPNUM;
    }

    public void setREPNUM(String REPNUM) {
        this.REPNUM = REPNUM;
    }

    public String getCODBAR() {
        return CODBAR;
    }

    public void setCODBAR(String CODBAR) {
        this.CODBAR = CODBAR;
    }

    public String getMensagem_boleto() {
        return mensagem_boleto;
    }

    public void setMensagem_boleto(String mensagem_boleto) {
        this.mensagem_boleto = mensagem_boleto;
    }

    public String getLogoBanco() {
        return logoBanco;
    }

    public void setLogoBanco(String logoBanco) {
        this.logoBanco = logoBanco;
    }

    public String getLogoEmpresa() {
        return logoEmpresa;
    }

    public void setLogoEmpresa(String logoEmpresa) {
        this.logoEmpresa = logoEmpresa;
    }

    public String getSerrilha() {
        return serrilha;
    }

    public void setSerrilha(String serrilha) {
        this.serrilha = serrilha;
    }

    public String getCnae() {
        return cnae;
    }

    public void setCnae(String cnae) {
        this.cnae = cnae;
    }

    public String getExercicio() {
        return exercicio;
    }

    public void setExercicio(String exercicio) {
        this.exercicio = exercicio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCodigosindical() {
        return codigosindical;
    }

    public void setCodigosindical(String codigosindical) {
        this.codigosindical = codigosindical;
    }

    public String getUsoBanco() {
        return usoBanco;
    }

    public void setUsoBanco(String usoBanco) {
        this.usoBanco = usoBanco;
    }

    public String getTextoTitulo() {
        return textoTitulo;
    }

    public void setTextoTitulo(String textoTitulo) {
        this.textoTitulo = textoTitulo;
    }

    public String getCaminhoVerso() {
        return caminhoVerso;
    }

    public void setCaminhoVerso(String caminhoVerso) {
        this.caminhoVerso = caminhoVerso;
    }

    public String getEntidade() {
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }
}
