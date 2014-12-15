/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.sistema;

import br.com.rtools.seguranca.Usuario;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "sis_resolucao")
@NamedQuery(name = "Resolucao.pesquisaID", query = "select r from Resolucao r where r.id = :pid")
public class Resolucao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "id_tipo_resolucao", referencedColumnName = "id")
    @ManyToOne
    private TipoResolucao tipoResolucao;

    public Resolucao() {
        this.id = -1;
        this.usuario = new Usuario();
        this.tipoResolucao = new TipoResolucao();
    }

    public Resolucao(int id, Usuario usuario, TipoResolucao tipoResolucao, String tamanho) {
        this.id = id;
        this.usuario = usuario;
        this.tipoResolucao = tipoResolucao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoResolucao getTipoResolucao() {
        return tipoResolucao;
    }

    public void setTipoResolucao(TipoResolucao tipoResolucao) {
        this.tipoResolucao = tipoResolucao;
    }
}
