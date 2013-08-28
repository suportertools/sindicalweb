package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.Feriados;
import br.com.rtools.pessoa.Filial;
import java.util.List;
import javax.persistence.EntityManager;

public interface FeriadosDB {
    public EntityManager getEntityManager();
    public boolean insert(Feriados feriados);
    public boolean update(Feriados feriados);
    public boolean delete(Feriados feriados);
    public List pesquisaTodos();
    public Feriados pesquisaCodigo(int id);
    public List pesquisarPorData(String data);
    public boolean exiteFeriadoCidade(Feriados feriados);
    public List pesquisarPorDataFilial(String data, Filial filial);

}
