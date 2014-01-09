package br.com.rtools.escola;

public class ListaAgrupaTurma {

    private AgrupaTurma agrupaTurma;
    private boolean isIntegral;

    public ListaAgrupaTurma() {
        this.agrupaTurma = new AgrupaTurma();
        this.isIntegral = false;
    }

    public ListaAgrupaTurma(AgrupaTurma agrupaTurma, boolean isIntegral) {
        this.agrupaTurma = agrupaTurma;
        this.isIntegral = isIntegral;
    }

    public AgrupaTurma getAgrupaTurma() {
        return agrupaTurma;
    }

    public void setAgrupaTurma(AgrupaTurma agrupaTurma) {
        this.agrupaTurma = agrupaTurma;
    }

    public boolean isIsIntegral() {
        return isIntegral;
    }

    public void setIsIntegral(boolean isIntegral) {
        this.isIntegral = isIntegral;
    }

}
