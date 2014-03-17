package br.com.rtools.escola.lista;

import br.com.rtools.escola.MatriculaEscola;
import br.com.rtools.escola.MatriculaIndividual;
import br.com.rtools.escola.MatriculaTurma;
import java.io.Serializable;

public class ListaMatriculaEscola implements Serializable {

    private MatriculaEscola matriculaEscola;
    private MatriculaIndividual matriculaIndividual;
    private MatriculaTurma matriculaTurma;
    private String curso;
    private String periodo;

    public ListaMatriculaEscola() {
        this.matriculaEscola = new MatriculaEscola();
        this.matriculaIndividual = new MatriculaIndividual();
        this.matriculaTurma = new MatriculaTurma();
        this.curso = "";
        this.periodo = "";
    }

    public ListaMatriculaEscola(MatriculaEscola matriculaEscola, MatriculaIndividual matriculaIndividual, MatriculaTurma matriculaTurma, String curso, String periodo) {
        this.matriculaEscola = matriculaEscola;
        this.matriculaIndividual = matriculaIndividual;
        this.matriculaTurma = matriculaTurma;
        this.curso = curso;
        this.periodo = periodo;
    }

    public MatriculaEscola getMatriculaEscola() {
        return matriculaEscola;
    }

    public void setMatriculaEscola(MatriculaEscola matriculaEscola) {
        this.matriculaEscola = matriculaEscola;
    }

    public MatriculaIndividual getMatriculaIndividual() {
        return matriculaIndividual;
    }

    public void setMatriculaIndividual(MatriculaIndividual matriculaIndividual) {
        this.matriculaIndividual = matriculaIndividual;
    }

    public MatriculaTurma getMatriculaTurma() {
        return matriculaTurma;
    }

    public void setMatriculaTurma(MatriculaTurma matriculaTurma) {
        this.matriculaTurma = matriculaTurma;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

}
