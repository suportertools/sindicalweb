package br.com.rtools.suporte.db;

import br.com.rtools.suporte.Protocolo;
import java.util.List;

public interface ProtocoloDB {

    public boolean insert(Protocolo protocolo);

    public boolean update(Protocolo protocolo);

    public boolean delete(Protocolo protocolo);

    public Protocolo pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List<String> pesquisaProtocolo(String des_tipo);

    public List pesquisaProtocoloParametros(String por, String combo, String desc);

    public Protocolo idProtocolo(Protocolo des_Protocolo);
}
