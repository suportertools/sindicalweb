//package br.com.rtools.sistema;
//
//import br.com.rtools.pessoa.Juridica;
//
//import java.io.Serializable;
//import java.util.Date;
//import javax.persistence.*;
//
//@Entity
//@Table(name = "SIS_NOTIFICACAO")
//public class SisNotificacao implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//    @Column(name = "DS_TITULO", length = 300)
//    private String titulo;
//    @Column(name = "DS_PERSISTENCE", length = 200)
//    private String persistence;
//    @Column(name = "DS_CAMINHO_SISTEMA", length = 200)
//    private String caminhoSistema;
//    @Column(name = "DS_IDENTIFICA", length = 100, unique = true)
//    private String identifica;
//    @JoinColumn(name = "ID_JURIDICA", referencedColumnName = "ID", nullable = false)
//    @ManyToOne
//    private Juridica juridica;
//    @Column(name = "NR_ACESSO")
//    private int acessos;
//    @Column(name = "DT_CADASTRO")
//    @Temporal(TemporalType.DATE)
//    private Date dtCadastro;
//    @Column(name = "IS_ATIVO")
//    private boolean ativo;
//    @Column(name = "DS_HOST", length = 300)
//    private String host;
//    @Column(name = "DS_SENHA", length = 300)
//    private String senha;
//
//
//}
