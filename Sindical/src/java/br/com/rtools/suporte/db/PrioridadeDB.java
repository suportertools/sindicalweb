package br.com.rtools.suporte.db;

import br.com.rtools.suporte.Prioridade;
import java.util.List;

public interface PrioridadeDB {

    public boolean insert(Prioridade prioridade);

    public boolean update(Prioridade prioridade);

    public boolean delete(Prioridade prioridade);

    public Prioridade pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List<String> pesquisaPrioridade(String des_tipo);

    public List pesquisaPrioridadeParametros(String por, String combo, String desc);
}