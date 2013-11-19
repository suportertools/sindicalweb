package br.com.rtools.pessoa.utilitarios;

import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.TipoEndereco;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEmpresaDB;
import br.com.rtools.pessoa.db.PessoaEmpresaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;

public class PessoaUtilitarios {

    private Pessoa pessoa;
    private MacFilial macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
    private Usuario usuarioSessao = (Usuario) GenericaSessao.getObject("sessaoUsuario");

    public PessoaUtilitarios(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public PessoaUtilitarios() {
        this.pessoa = new Pessoa();
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    // Fácil
    /**
     * Retorna lista de endereços da pessoa
     *
     * @return
     */
    public List<PessoaEndereco> listaPessoaEndereco() {
        List<PessoaEndereco> pessoaEnderecos = new ArrayList<PessoaEndereco>();
        if (this.pessoa.getId() != -1) {
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            pessoaEnderecos = (List<PessoaEndereco>) pessoaEnderecoDB.pesquisaEndPorPessoa(this.pessoa.getId());
        }
        return pessoaEnderecos;
    }

    /**
     * Retorna lista de endereços da pessoa
     *
     * @param tipoEndereco
     * @return
     */
    public PessoaEndereco pessoaEndereco(TipoEndereco tipoEndereco) {
        PessoaEndereco pessoaEnderecos = new PessoaEndereco();
        if (this.pessoa.getId() != -1) {
            PessoaEnderecoDB pessoaEnderecoDB = new PessoaEnderecoDBToplink();
            if (tipoEndereco.getId() != -1) {
                pessoaEnderecos = (PessoaEndereco) pessoaEnderecoDB.pesquisaEndPorPessoaTipo(this.pessoa.getId(), tipoEndereco.getId());
            }
        }
        return pessoaEnderecos;
    }

    /**
     * Retorna lista de empresas da pessoa
     *
     * @return
     */
    public List<PessoaEmpresa> listaPessoaEmpresa() {
        List<PessoaEmpresa> pessoaEmpresa = new ArrayList<PessoaEmpresa>();
        if (this.pessoa.getId() != -1) {
            PessoaEmpresaDB pessoaEmpresaDB = new PessoaEmpresaDBToplink();
            Fisica fisica = fisica();
            pessoaEmpresa = (List<PessoaEmpresa>) pessoaEmpresaDB.listaPessoaEmpresaPorFisica(fisica.getId());
        }
        return pessoaEmpresa;
    }

    /**
     * Retorna lista de empresas da pessoa
     *
     * @return
     */
    public PessoaEmpresa pessoaEmpresa() {
        PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
        if (this.pessoa.getId() != -1) {
            PessoaEmpresaDB pessoaEmpresaDB = new PessoaEmpresaDBToplink();
            pessoaEmpresa = (PessoaEmpresa) pessoaEmpresaDB.pesquisaPessoaEmpresaPorPessoa(this.pessoa.getId());
        }
        return pessoaEmpresa;
    }

    /**
     * Retorna pessoa jurídica da pessoa
     *
     * @return
     */
    public Juridica juridica() {
        Juridica juridica = new Juridica();
        if (this.pessoa.getId() != -1) {
            JuridicaDB juridicaDB = new JuridicaDBToplink();
            juridicaDB.pesquisaJuridicaPorPessoa(this.pessoa.getId());
        }
        return juridica;
    }

    /**
     * Retorna pessoa física da pessoa
     *
     * @return
     */
    public Fisica fisica() {
        Fisica fisica = new Fisica();
        if (this.pessoa.getId() != -1) {
            FisicaDB fisicaDB = new FisicaDBToplink();
            fisicaDB.pesquisaFisicaPorPessoa(this.pessoa.getId());
        }
        return fisica;
    }

    /**
     * Retorna usuário da pessoa
     *
     * @return
     */
    public Usuario usuario() {
        Usuario usuario1 = new Usuario();
        if (this.pessoa.getId() != -1) {
            UsuarioDB usuarioDB = new UsuarioDBToplink();
            usuarioDB.pesquisaUsuarioPorPessoa(this.pessoa.getId());
        }
        return usuario1;
    }

    /**
     * Retorna usuário da pessoa
     *
     * @return
     */
    public Registro registro() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1);
        return registro(pessoa1);
    }

    /**
     * Retorna usuário da pessoa
     *
     * @param pessoa
     * @return
     */
    public Registro registro(Pessoa pessoa) {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        Registro registro = new Registro();
        if (pessoa.getId() != -1) {
            registro = (Registro) salvarAcumuladoDB.pesquisaObjeto(pessoa.getId(), "Registro");
        }
        return registro;
    }

    /**
     * Retorna macfilial da sessão
     *
     * @return
     */
    public MacFilial getMacFilial() {
        return macFilial;
    }

    /**
     * Retorna usuário da sessão
     * 
     * @return 
     */
    public Usuario getUsuarioSessao() {
        return usuarioSessao;
    }
}
