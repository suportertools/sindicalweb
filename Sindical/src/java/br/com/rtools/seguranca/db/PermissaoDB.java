//package br.com.rtools.seguranca.db;
//
//import br.com.rtools.seguranca.Permissao;
//import br.com.rtools.seguranca.UsuarioAcesso;
//import java.util.List;
//import javax.persistence.EntityManager;
//
//public interface PermissaoDB {
//
//    public EntityManager getEntityManager();
//
//    public Permissao pesquisaCodigo(int id);
//
//    public List pesquisaTodos();
//
//    public List pesquisaTodosAgrupados();
//
//    public List pesquisaTodosAgrupadosPorModulo(int idModulo);
//
//    public List pesquisaPermissaoModRot(int idModulo, int idRotina);
//
//    public List pesquisaPermissaoModRotEve(int idModulo, int idRotina, int idEvento);
//
//    public Permissao pesquisaPermissaoModuloRotinaEvento(int idModulo, int idRotina, int idEvento);
//
//    public List listaModuloPermissaoAgrupado();
//
//    public List listaRotinaPermissaoAgrupado(int idModulo);
//
//    public List listaEventoPermissaoAgrupado(int idModulo, int idRotina);
//
//    public UsuarioAcesso pesquisaUsuarioAcessoModuloRotinaEvento(int idUsuario, int idModulo, int idRotina, int idEvento);
//
//    public List<UsuarioAcesso> listaUsuarioAcesso(int idUsuario, int idModulo, int idRotina, int idEvento);
//}
