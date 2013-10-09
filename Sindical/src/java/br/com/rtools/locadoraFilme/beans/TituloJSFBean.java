package br.com.rtools.locadoraFilme.beans;

import br.com.rtools.locadoraFilme.Genero;
import br.com.rtools.locadoraFilme.Titulo;
import br.com.rtools.locadoraFilme.db.TituloDB;
import br.com.rtools.locadoraFilme.db.TituloDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class TituloJSFBean {

    private Titulo titulo;
    private String descPesquisa = "";
    private String porPesquisa = "descricao";
    private String comoPesquisa = "";
    private String msgConfirma;
    private int idGenero;
    private int idIndex = -1;
    private List<SelectItem> listaGeneroCombo;
    private List<Titulo> listaTitulo = new ArrayList();
    private List<Genero> listaGenero;
    private boolean required = false;
    private boolean limpar = false;

    public TituloJSFBean() {
        titulo = new Titulo();
        comoPesquisa = "";
        descPesquisa = "";
        porPesquisa = "";
        msgConfirma = "";
        listaGeneroCombo = new ArrayList<SelectItem>();
        listaGenero = new ArrayList<Genero>();
        required = false;
    }

    public String novo() {
        required = false;
        titulo = new Titulo();
        setComoPesquisa("");
        setDescPesquisa("");
        listaGeneroCombo = new ArrayList<SelectItem>();
        listaGenero = new ArrayList<Genero>();
        listaTitulo = new ArrayList<Titulo>();
        listaGenero.clear();
        listaTitulo.clear();
        msgConfirma = "";
        return "titulo";
    }

    public String limpar() {
        if (isLimpar() == true) {
            novo();
        }
        return "titulo";
    }

    public List<SelectItem> getListaGeneroCombo() {
        int i = 0;
        TituloDB db = new TituloDBToplink();
        if (listaGeneroCombo.isEmpty()) {
            setListaGenero((List<Genero>) db.pesquisaTodosGenero());
            while (i < getListaGenero().size()) {
                listaGeneroCombo.add(new SelectItem(
                        new Integer(i),
                        getListaGenero().get(i).getDescricao()));
                i++;
            }
        }
        return listaGeneroCombo;
    }

    public synchronized String salvar() {
        TituloDB db = new TituloDBToplink();
        titulo.setGenero(getListaGenero().get(idGenero));
        int hora = 0;
        if (!titulo.getDuracao().isEmpty()) {
            hora = Integer.parseInt(titulo.getDuracao().substring(0, 2));
            int ano = 0;
            int anoParametro = 1895;
            if (hora < 8) {
                ano = Integer.parseInt(titulo.getAnoLancamentoString());
                if (ano >= anoParametro) {
                    if (titulo.getId() == -1) {
                        if (titulo.getDescricao().equals("")) {
                            msgConfirma = "Digite o nome do titulo!";
                        } else {
                            if (db.pesquisaTitulo(titulo.getDescricao()) == null) {
                                if (db.insert(titulo)) {
                                    if (titulo.getBarras().isEmpty()) {
                                        titulo.setBarras(Integer.toString(titulo.getId()));
                                        db.update(titulo);
                                    }
                                    setLimpar(false);
                                    msgConfirma = "Cadastro efetuado com sucesso!";
                                } else {
                                    msgConfirma = "Erro! Cadastro não foi efetuado.";
                                }
                            } else {
                                msgConfirma = "Já existe um titulo com esse nome.";
                            }
                        }
                    } else {
                        if (db.update(titulo)) {
                            msgConfirma = "Cadastro atualizado com sucesso!";
                        } else {
                            msgConfirma = "Erro ao Atualizar.";
                        }
                    }
                } else {
                    msgConfirma = "O Ano de lançamento deve ser igual ou superior a 1895!";
                }
            } else {
                msgConfirma = "O tempo de duração deve ser inferior ou igual a 8 horas!";
            }
        } else {
            msgConfirma = "O campo de duração está vazio!";
        }
        return null;
        //return novoOperacao();
    }

    public synchronized String excluir() {
        TituloDB tituloDB = new TituloDBToplink();
        if (titulo.getId() != -1) {
            titulo = tituloDB.pesquisaCodigo(titulo.getId());
            if (tituloDB.delete(titulo)) {
                setLimpar(true);
                msgConfirma = "Cadastro excluído com sucesso!";
            } else {
                msgConfirma = "Erro! Cadastro não foi excluído.";
            }
        } else {
            msgConfirma = "Não há registro para excluir.";
        }
        return null;
    }

    public String editar() {
        titulo = (Titulo) listaTitulo.get(idIndex);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tituloPesquisa", titulo);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        titulo = new Titulo();
        setDescPesquisa("");
        setComoPesquisa("");
        setPorPesquisa("");
        required = true;
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null) {
            return "titulo";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
        //return null;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
    }

    public List<Titulo> getListaTitulo() {
        TituloDB db = new TituloDBToplink();
        if (descPesquisa.equals("")) {
            listaTitulo = new ArrayList();
            return listaTitulo;
        } else {
            listaTitulo = db.pesquisaTitulos(descPesquisa, porPesquisa, comoPesquisa);
            return listaTitulo;
        }
    }

    public void refreshForm() {
    }

    public String validaHora(String hora) {
        int n1 = 0;
        int n2 = 0;
        if (hora.length() == 1) {
            hora = "0" + hora + ":00";
        }

        if (hora.length() == 2) {
            if ((Integer.parseInt(hora) >= 0) && (Integer.parseInt(hora) <= 23)) {
                hora = hora + ":00";
            } else {
                hora = "";
            }
        } else if (hora.length() == 3) {
            n1 = Integer.parseInt(hora.substring(0, 2));
            String pontos = hora.substring(2, 3);

            if (((n1 >= 0) && (n1 <= 23)) && pontos.equals(":")) {
                hora = hora + "00";
            } else {
                hora = "";
            }
        } else if (hora.length() == 4) {
            n1 = Integer.parseInt(hora.substring(0, 2));
            n2 = Integer.parseInt(hora.substring(3, 4));
            String pontos = hora.substring(2, 3);

            if ((pontos.equals(":")) && ((n1 >= 0) && (n1 <= 23)) && ((n2 >= 0) && (n2 <= 5))) {
                hora = hora + "0";
            } else {
                hora = "";
            }
        } else if (hora.length() == 5) {
            n1 = Integer.parseInt(hora.substring(0, 2));
            n2 = Integer.parseInt(hora.substring(3, 5));
            String pontos = hora.substring(2, 3);

            if (!(((n1 >= 0) && (n1 <= 23)) && ((n2 >= 0) && (n2 <= 59)) && (pontos.equals(":")))) {
                hora = "";
            }
        }
        return hora;
    }

    public void validaDuracao() {
        this.titulo.setDuracao(this.validaHora(this.titulo.getDuracao()));
    }

    public Titulo getTitulo() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tituloPesquisa") != null) {
            titulo = (Titulo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("tituloPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("tituloPesquisa");
        }
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public void setListaGeneroCombo(List<SelectItem> listaGeneroCombo) {
        this.listaGeneroCombo = listaGeneroCombo;
    }

    public List<Genero> getListaGenero() {
        return listaGenero;
    }

    public void setListaGenero(List<Genero> listaGenero) {
        this.listaGenero = listaGenero;
    }

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
    }

    public void setListaTitulo(List<Titulo> listaTitulo) {
        this.listaTitulo = listaTitulo;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }
}