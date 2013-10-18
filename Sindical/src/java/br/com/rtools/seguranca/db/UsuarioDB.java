package br.com.rtools.seguranca.db;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Usuario;
import java.util.List;
import javax.persistence.EntityManager;

public interface UsuarioDB {

    public EntityManager getEntityManager();

    public Pessoa ValidaUsuarioWeb(String login, String senha);

    public Usuario ValidaUsuario(String login, String senha);

    public Usuario ValidaUsuarioSuporteWeb(String login, String senha);

    public List pesquisaLogin(String login, int idPessoa);

    public Usuario ValidaUsuarioContribuinte(int idUsuario);

    public Pessoa ValidaUsuarioContribuinteWeb(int idPessoa);

    public Usuario ValidaUsuarioContabilidade(int idUsuario);

    public Pessoa ValidaUsuarioContabilidadeWeb(int idPessoa);

    public Pessoa ValidaUsuarioPatronalWeb(int idPessoa);

    public void updateAcordoMovimento();

    public List<Usuario> pesquisaTodosPorDescricao(String descricaoPesquisa);
    
    public Usuario pesquisaUsuarioPorPessoa(int id_pessoa);
}
