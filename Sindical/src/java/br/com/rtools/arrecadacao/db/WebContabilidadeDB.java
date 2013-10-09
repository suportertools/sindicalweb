package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.MensagemConvencao;
import java.util.List;

public interface WebContabilidadeDB {

    public List listaEmpresasPertContabilidade(int idContabilidade);

    public List pesquisaMovParaWebContabilidade(int idJuridica);

    public List pesquisaMovParaWebContabilidadeComRef(int idJuridica, String referencia);
}
