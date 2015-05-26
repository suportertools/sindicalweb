package br.com.rtools.impressao;

import javax.persistence.Column;

/**
 *
 * @author rtools2
 */
public class ParametroAcademiaCadastral {

    private String detalhes_relatorio;
    @Column(name = "Aluno")
    private String aluno_nome;
    @Column(name = "Idade")
    private String aluno_idade;
    @Column(name = "Nascimento")
    private String aluno_nascimento;
    @Column(name = "Sexo")
    private String aluno_sexo;
    @Column(name = "Cidade")
    private String aluno_cidade;
    @Column(name = "Serviço")
    private String servico;
    @Column(name = "Período")
    private String periodo;
    @Column(name = "Responsável")
    private String responsavel_nome;
    @Column(name = "Emissão")
    private String emissao;

    /**
     *
     * @param detalhes_relatorio
     * @param aluno_nome
     * @param aluno_idade
     * @param aluno_nascimento
     * @param aluno_sexo
     * @param aluno_cidade
     * @param servico
     * @param periodo
     * @param responsavel_nome
     * @param emissao
     */
    public ParametroAcademiaCadastral(String detalhes_relatorio, String aluno_nome, String aluno_idade, String aluno_nascimento, String aluno_sexo, String aluno_cidade, String servico, String periodo, String responsavel_nome, String emissao) {
        this.detalhes_relatorio = detalhes_relatorio;
        this.aluno_nome = aluno_nome;
        this.aluno_idade = aluno_idade;
        this.aluno_nascimento = aluno_nascimento;
        this.aluno_sexo = aluno_sexo;
        this.aluno_cidade = aluno_cidade;
        this.servico = servico;
        this.periodo = periodo;
        this.responsavel_nome = responsavel_nome;
        this.emissao = emissao;
    }

    /**
     * Nome
     *
     * @see aaaa
     * @return
     */
    // ALUNO
    public String getAluno_nome() {
        return aluno_nome;
    }

    // ALUNO
    public void setAluno_nome(String aluno_nome) {
        this.aluno_nome = aluno_nome;
    }

    public String getAluno_idade() {
        return aluno_idade;
    }

    public void setAluno_idade(String aluno_idade) {
        this.aluno_idade = aluno_idade;
    }

    public String getAluno_nascimento() {
        return aluno_nascimento;
    }

    public void setAluno_nascimento(String aluno_nascimento) {
        this.aluno_nascimento = aluno_nascimento;
    }

    public String getAluno_sexo() {
        return aluno_sexo;
    }

    public void setAluno_sexo(String aluno_sexo) {
        this.aluno_sexo = aluno_sexo;
    }

    public String getAluno_cidade() {
        return aluno_cidade;
    }

    public void setAluno_cidade(String aluno_cidade) {
        this.aluno_cidade = aluno_cidade;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getDetalhes_relatorio() {
        return detalhes_relatorio;
    }

    public void setDetalhes_relatorio(String detalhes_relatorio) {
        this.detalhes_relatorio = detalhes_relatorio;
    }

    public String getResponsavel_nome() {
        return responsavel_nome;
    }

    public void setResponsavel_nome(String responsavel_nome) {
        this.responsavel_nome = responsavel_nome;
    }

    public String getEmissao() {
        return emissao;
    }

    public void setEmissao(String emissao) {
        this.emissao = emissao;
    }
}
