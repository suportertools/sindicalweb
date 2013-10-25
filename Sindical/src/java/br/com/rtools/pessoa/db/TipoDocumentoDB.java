package br.com.rtools.pessoa.db;

import java.util.List;

public interface TipoDocumentoDB {

    public List<String> pesquisaTipoDocumento(String des_tipoDocumento);
}
