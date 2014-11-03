package br.com.rtools.relatorios.db;

import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.seguranca.Rotina;
import java.util.List;

public interface RelatorioGenericoDB {

    public List<Relatorios> pesquisaTipoRelatorio(int idRotina);

    public List pesquisaCidadesRelatorio();

    public Relatorios pesquisaRelatorios(int idRelatorio);

    public Relatorios pesquisaRelatoriosPorJasper(String dsJasper);

    public List<Rotina> pesquisaRotina();

    public List<Relatorios> pesquisaTodosRelatorios();
}