package br.com.rtools.relatorios.db;

import br.com.rtools.relatorios.Relatorios;
import java.util.List;

public interface RelatorioContribuintesDB {

    public List pesquisaContabilidades();

    public List pesquisarCnaeConvencaoPorConvencao(String ids);

    public List pesquisarGrupoPorConvencao(String ids);

    public List listaRelatorioContribuintes(Relatorios relatorios, String emails, String condicao, String escritorio, String tipoPCidade, String cidade,
            String ordem, String cnaes, int idTipoEndereco, String centroComercial, String cTipo, String dsNumero, String idGrupos, String bairros, String convencoes);

    public List listaCentros(String ids);

    public List listaRelatorioContribuintesPorJuridica(String condicao, String escritorio, String tipoPCidade, String cidade, String ordem, String cnaes, int idTipoEndereco, String idsJuridica);
}
