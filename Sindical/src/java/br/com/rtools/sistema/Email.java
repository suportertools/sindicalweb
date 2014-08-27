package br.com.rtools.sistema;

import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "sis_email")
public class Email implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data", nullable = true)
    private Date data;
    @Column(name = "ds_hora", length = 5)
    private String hora;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name = "id_sis_prioridade", referencedColumnName = "id")
    @ManyToOne
    private EmailPrioridade emailPrioridade;
    @Column(name = "ds_assunto", length = 255, nullable = true)
    private String assunto;
    @Column(name = "ds_mensagem", length = 255, nullable = true)
    private String mensagem;
    @Column(name = "is_confirma_recebimento", columnDefinition = "boolean default false")
    private boolean confirmaRecebimento;
    @Column(name = "is_rascunho", columnDefinition = "boolean default false")
    private boolean rascunho;

    public Email() {
        this.id = -1;
        this.data = new Date();
        this.hora = "";
        this.usuario = new Usuario();
        this.rotina = new Rotina();
        this.emailPrioridade = new EmailPrioridade();
        this.assunto = "";
        this.mensagem = "";
        this.confirmaRecebimento = false;
        this.rascunho = false;
    }

    public Email(int id, Date data, String hora, Usuario usuario, Rotina rotina, EmailPrioridade emailPrioridade, String assunto, String mensagem, boolean confirmaRecebimento, boolean rascunho) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.usuario = usuario;
        this.rotina = rotina;
        this.emailPrioridade = emailPrioridade;
        this.assunto = assunto;
        this.mensagem = mensagem;
        this.confirmaRecebimento = confirmaRecebimento;
        this.rascunho = rascunho;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDataString() {
        return DataHoje.converteData(data);
    }

    public void setDataString(String data) {
        this.data = DataHoje.converte(data);
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isConfirmaRecebimento() {
        return confirmaRecebimento;
    }

    public void setConfirmaRecebimento(boolean confirmaRecebimento) {
        this.confirmaRecebimento = confirmaRecebimento;
    }

    public boolean isRascunho() {
        return rascunho;
    }

    public void setRascunho(boolean rascunho) {
        this.rascunho = rascunho;
    }

    public EmailPrioridade getEmailPrioridade() {
        return emailPrioridade;
    }

    public void setEmailPrioridade(EmailPrioridade emailPrioridade) {
        this.emailPrioridade = emailPrioridade;
    }

}
