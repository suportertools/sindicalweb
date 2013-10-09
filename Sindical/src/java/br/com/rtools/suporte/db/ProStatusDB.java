package br.com.rtools.suporte.db;

import br.com.rtools.suporte.ProStatus;
import java.util.List;

public interface ProStatusDB {

    public boolean insert(ProStatus proStatus);

    public boolean update(ProStatus proStatus);

    public boolean delete(ProStatus proStatus);

    public ProStatus pesquisaCodigo(int id);

    public List pesquisaTodos();
}