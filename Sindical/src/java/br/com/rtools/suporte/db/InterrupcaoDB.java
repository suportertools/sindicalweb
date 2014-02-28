package br.com.rtools.suporte.db;

import br.com.rtools.suporte.Interrupcao;
import java.util.List;

public interface InterrupcaoDB {

    public List<String> pesquisaInterrupcao(String des_tipo);

    public List pesquisaInterrupcaoParametros(String por, String combo, String desc);

    public Interrupcao idInterrupcao(Interrupcao des_Interrupcao);

    public List<Interrupcao> listaInterrupcao(int idOrdemServico);
}