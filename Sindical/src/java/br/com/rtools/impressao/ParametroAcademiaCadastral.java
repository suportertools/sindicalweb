package br.com.rtools.impressao;

public class ParametroAcademiaCadastral {

    private String detalhes_relatorio;
    private String aluno_nome;
    private String aluno_idade;
    private String aluno_nascimento;
    private String aluno_sexo;
    private String aluno_cidade;
    private String servico;
    private String periodo;
    private String responsavel;

    public ParametroAcademiaCadastral(String detalhes_relatorio, String aluno_nome, String aluno_idade, String aluno_nascimento, String aluno_sexo, String aluno_cidade, String servico, String periodo, String responsavel) {
        this.detalhes_relatorio = detalhes_relatorio;
        this.aluno_nome = aluno_nome;
        this.aluno_idade = aluno_idade;
        this.aluno_nascimento = aluno_nascimento;
        this.aluno_sexo = aluno_sexo;
        this.aluno_cidade = aluno_cidade;
        this.servico = servico;
        this.periodo = periodo;
        this.responsavel = responsavel;
    }

    public String getAluno_nome() {
        return aluno_nome;
    }

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

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getDetalhes_relatorio() {
        return detalhes_relatorio;
    }

    public void setDetalhes_relatorio(String detalhes_relatorio) {
        this.detalhes_relatorio = detalhes_relatorio;
    }
}
