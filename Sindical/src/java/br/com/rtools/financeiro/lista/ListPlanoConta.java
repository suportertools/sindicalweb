package br.com.rtools.financeiro.lista;

import br.com.rtools.financeiro.Plano5;

public class ListPlanoConta {

    private String sequenciaPlano;
    private String titulo;
    private String panel;
    private String dialog;
    private Plano5 plano5;

    public ListPlanoConta() {
        this.sequenciaPlano = "plano1";
        this.titulo = "Plano 1";
        this.panel = "i_panel_plano1";
        this.dialog = "dlg_plano1";
        this.plano5 = new Plano5();
    }

    public ListPlanoConta(String sequenciaPlano, String titulo, String panel, String dialog, Plano5 plano5) {
        this.sequenciaPlano = sequenciaPlano;
        this.titulo = titulo;
        this.panel = panel;
        this.dialog = dialog;
        this.plano5 = plano5;
    }

    public void configuraPlano(Plano5 plano5, String titulo, String panel, String sequenciaPlano, String dialog) {
        this.titulo = titulo;
        this.panel = panel;
        this.sequenciaPlano = sequenciaPlano;
        this.plano5 = plano5;
        this.dialog = dialog;
    }

    public String getSequenciaPlano() {
        return sequenciaPlano;
    }

    public void setSequenciaPlano(String sequenciaPlano) {
        this.sequenciaPlano = sequenciaPlano;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

}
