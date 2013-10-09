package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.MensagemConvencao;
import java.util.List;

public interface WebContribuintesDB {

    public List pesquisaMovParaWebContribuinte(int id_pessoa);

    public List pesquisaMovParaWebContribuinteComRef(int id_pessoa, String referencia);
}
