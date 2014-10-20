package br.com.rtools.relatorios.db;

import br.com.rtools.relatorios.Relatorios;
import java.util.Date;
import java.util.List;

public interface RelatorioMovimentosDB {

    public List listaMovimentos(Relatorios relatorios, String condicao, int idServico, int idTipoServico, int idJuridica, boolean data,
            String tipoData, Date dtInicial, Date dtFinal, String dtRefInicial,
            String dtRefFinal, String ordem, boolean chkPesEmpresa, String porPesquisa, String filtroEmpresa,
            int idConvencao, int idGrupoCidade, String idsCidades, String idsEsc, String inCnaes);
}
