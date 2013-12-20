//package br.com.rtools.view;
//
//import br.com.rtools.pessoa.*;
//import br.com.rtools.utilitarios.DataHoje;
//import java.util.Date;
//import javax.persistence.*;
//
//public class ContribuintesView implements Serializable {
//    private int id;
//    private Juridica juridica;
//    private Juridica contabilidade;
//    private String nome;
//    private String doc;
//
//    public ContribuintesView() {
//        this.id = -1;
//        this.pessoa = new Pessoa();
//        this.fantasia = "";
//        this.cnae = new Cnae();
//        this.contabilidade = null;
//        this.inscricaoEstadual = "";
//        this.inscricaoMunicipal = "";
//        this.contato = "";
//        this.responsavel = "";
//        this.porte = new Porte();
//        setAbertura("");
//        setFechamento("");
//        this.emailEscritorio = false;
//    }
//
//    public ContribuintesView(int id, Pessoa pessoa, String fantasia, Cnae cnae, Juridica contabilidade, String inscricaoEstadual, String inscricaoMunicipal, String contato, String responsavel, Porte porte, String abertura, String fechamento, boolean emailEscritorio) {
//        this.id = id;
//        this.pessoa = pessoa;
//        this.fantasia = fantasia;
//        this.cnae = cnae;
//        this.contabilidade = contabilidade;
//        this.inscricaoEstadual = inscricaoEstadual;
//        this.inscricaoMunicipal = inscricaoMunicipal;
//        this.contato = contato;
//        this.responsavel = responsavel;
//        this.porte = porte;
//        setAbertura(abertura);
//        setFechamento(fechamento);
//        this.emailEscritorio = emailEscritorio;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public Pessoa getPessoa() {
//        return pessoa;
//    }
//
//    public void setPessoa(Pessoa pessoa) {
//        this.pessoa = pessoa;
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
//    public Cnae getCnae() {
//        return cnae;
//    }
//
//    public void setCnae(Cnae cnae) {
//        this.cnae = cnae;
//    }
//
//    public String getInscricaoEstadual() {
//        return inscricaoEstadual;
//    }
//
//    public void setInscricaoEstadual(String inscricaoEstadual) {
//        this.inscricaoEstadual = inscricaoEstadual;
//    }
//
//    public String getInscricaoMunicipal() {
//        return inscricaoMunicipal;
//    }
//
//    public void setInscricaoMunicipal(String inscricaoMunicipal) {
//        this.inscricaoMunicipal = inscricaoMunicipal;
//    }
//
//    public String getContato() {
//        return contato;
//    }
//
//    public void setContato(String contato) {
//        this.contato = contato;
//    }
//
//    public String getResponsavel() {
//        return responsavel;
//    }
//
//    public void setResponsavel(String responsavel) {
//        this.responsavel = responsavel;
//    }
//
//    public Date getDtAbertura() {
//        return dtAbertura;
//    }
//
//    public void setDtAbertura(Date dtAbertura) {
//        this.dtAbertura = dtAbertura;
//    }
//
//    public Date getDtFechamento() {
//        return dtFechamento;
//    }
//
//    public void setDtFechamento(Date dtFechamento) {
//        this.dtFechamento = dtFechamento;
//    }
//
//    public String getAbertura() {
//        if (dtAbertura != null) {
//            return DataHoje.converteData(dtAbertura);
//        } else {
//            return "";
//        }
//    }
//
//    public void setAbertura(String abertura) {
//        if (!(abertura.isEmpty())) {
//            this.dtAbertura = DataHoje.converte(abertura);
//        }
//    }
//
//    public String getFechamento() {
//        if (dtAbertura != null) {
//            return DataHoje.converteData(dtFechamento);
//        } else {
//            return "";
//        }
//    }
//
//    public void setFechamento(String fechamento) {
//        if (!(fechamento.isEmpty())) {
//            this.dtFechamento = DataHoje.converte(fechamento);
//        }
//    }
//
//    public Porte getPorte() {
//        return porte;
//    }
//
//    public void setPorte(Porte porte) {
//        this.porte = porte;
//    }
//
//    public Juridica getContabilidade() {
//        return contabilidade;
//    }
//
//    public void setContabilidade(Juridica contabilidade) {
//        this.contabilidade = contabilidade;
//    }
//
//    public boolean isEmailEscritorio() {
//        return emailEscritorio;
//    }
//
//    public void setEmailEscritorio(boolean emailEscritorio) {
//        this.emailEscritorio = emailEscritorio;
//    }
//}