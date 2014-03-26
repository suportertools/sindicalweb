package br.com.rtools.financeiro.db;

import br.com.rtools.pessoa.TipoDocumento;
import java.util.List;

public interface LancamentoFinanceiroDB {
    public List<TipoDocumento> listaTipoDocumento();
}
