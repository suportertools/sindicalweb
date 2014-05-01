package br.com.rtools.relatorios.db;

import br.com.rtools.financeiro.FStatus;
import java.util.List;
import java.util.Vector;

public interface RelatorioFinanceiroDB {
    public List<Vector> listaChequesRecebidos(String ids_filial, String ids_caixa, String tipo_data, String data_inicial, String data_final, int id_status);
    public List<FStatus> listaStatusCheque(String ids);
}
