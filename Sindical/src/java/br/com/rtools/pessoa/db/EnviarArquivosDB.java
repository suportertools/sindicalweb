package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Juridica;
import java.util.List;

public interface EnviarArquivosDB {
    public Juridica pesquisaCodigo(int id);
    public List pesquisaContabilidades();
}