package br.com.rtools.pessoa.db;

import java.util.List;

public interface CentroComercialDB {
    public List pesquisaTodosOrdernado();
    public List listaCentros(int idTipoCentroComercial,int idJuridica);
}
