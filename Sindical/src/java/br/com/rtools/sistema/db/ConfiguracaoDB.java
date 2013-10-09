package br.com.rtools.sistema.db;

import br.com.rtools.sistema.Configuracao;
import java.util.List;

public interface ConfiguracaoDB {

    public boolean existeIdentificador(Configuracao configuracao);

    public boolean existeIdentificadorPessoa(Configuracao configuracao);

    public List listaConfiguracao(String descricaoPesquisa);
}
