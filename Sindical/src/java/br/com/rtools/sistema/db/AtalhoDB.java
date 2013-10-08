package br.com.rtools.sistema.db;

import br.com.rtools.sistema.Atalhos;
import br.com.rtools.sistema.ContadorAcessos;
import java.util.List;

public interface AtalhoDB {

    public List<Atalhos> listaTodos(int id_pessoa);

    public Atalhos pesquisaPorSigla(String sigla);

    public Atalhos pesquisaPorRotina(int id_rotina);

    public List<ContadorAcessos> listaAcessosUsuario(int id_usuario);

    public ContadorAcessos pesquisaContadorAcessos(int id_usuario, int id_rotina);
}
