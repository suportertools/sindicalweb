package br.com.rtools.impressao;

public class ParametroEscolaCadastral {

    private String aluno_nome;
    private String aluno_nascimento;
    private String aluno_sexo;
    private String curso;
    private String status;
    private String data_inicio;
    private String data_termino;
    private String socio_codigo;

    public ParametroEscolaCadastral(String aluno_nome, String aluno_nascimento, String aluno_sexo, String curso, String status, String data_inicio, String data_termino, String socio_codigo) {
        this.aluno_nome = aluno_nome;
        this.aluno_nascimento = aluno_nascimento;
        this.aluno_sexo = aluno_sexo;
        this.curso = curso;
        this.status = status;
        this.data_inicio = data_inicio;
        this.data_termino = data_termino;
        this.socio_codigo = socio_codigo;
    }

    public String getAluno_nome() {
        return aluno_nome;
    }

    public void setAluno_nome(String aluno_nome) {
        this.aluno_nome = aluno_nome;
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getData_termino() {
        return data_termino;
    }

    public void setData_termino(String data_termino) {
        this.data_termino = data_termino;
    }

    public String getSocio_codigo() {
        return socio_codigo;
    }

    public void setSocio_codigo(String socio_codigo) {
        this.socio_codigo = socio_codigo;
    }

}
