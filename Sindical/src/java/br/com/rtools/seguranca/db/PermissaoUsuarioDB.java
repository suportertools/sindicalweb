package br.com.rtools.seguranca.db;

import br.com.rtools.seguranca.*;
import java.util.List;
import javax.persistence.EntityManager;

public interface PermissaoUsuarioDB {

    public EntityManager getEntityManager();

    public boolean insert(PermissaoUsuario permissaoUsuario);

    public boolean update(PermissaoUsuario permissaoUsuario);

    public boolean delete(PermissaoUsuario permissaoUsuario);

    public List pesquisaListaPermissaoPorUsuario(int idUsuario);

    public PermissaoUsuario pesquisaPermissaoUsuario(int idUsuario, int idDepartamento, int idNivel);

    public List<PermissaoUsuario> listaPermissaoUsuario(int idUsuario);

    public Rotina pesquisaRotinaPermissao(String dsPermissao);

    public Rotina pesquisaRotinaPermissaoPorClasse(String dsClasse);

    public PermissaoUsuario pesquisaAcessoPermissao(int idUsuario, int idModulo, int idRotina, int idEvento);

    public PermissaoUsuario pesquisaAcessoPermissaoSM(int idUsuario, int idRotina, int idEvento);

    public Evento pesquisaCodigoEvento(int id);

    public Modulo pesquisaCodigoModulo(int id);

    public Departamento pesquisaCodigoDepartamento(int id);

    public List pesquisaTodosDepOrdenado();

    public Nivel pesquisaCodigoNivel(int id);

    public List pesquisaTodosNiveis();

    public List pesquisaTodosModuloOrdenado();

    public List pesquisaTodosEventoOrdenado();

    public UsuarioAcesso pesquisaUsuarioAcesso(int id_usuario, int id_permissao);

    public Permissao pesquisaPermissao(int id_modulo, int id_rotina, int id_evento);

    public List<PermissaoDepartamento> pesquisaPDepartamento(int id_departamento, int id_nivel);

    public List<UsuarioAcesso> pesquisaAcesso(int id_permissao);

    public List<PermissaoUsuario> pesquisaPermissaoUser(int id_depto, int id_nivel);

    public PermissaoDepartamento pesquisaPermissaoDepartamento(int id_departamento, int id_nivel, int id_permissao);

    public boolean existePermissaoUsuario(PermissaoUsuario permissaoUsuario);
}
