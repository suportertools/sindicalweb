package br.com.rtools.relatorios.db;

import java.util.List;

public interface RelatorioContribuintesDB {

    public List pesquisaContabilidades();

    public List pesquisarCnaeConvencaoPorConvencao(String ids);

    public List pesquisarGrupoPorConvencao(String ids);

    public List listaRelatorioContribuintes(String emails, String condicao, String escritorio, String tipoPCidade, String cidade,
            String ordem, String cnaes, int idTipoEndereco, String centroComercial, String cTipo, String dsNumero, String idGrupos, String bairros);

    public List listaCentros(String ids);

    public List listaRelatorioContribuintesPorJuridica(String condicao, String escritorio, String tipoPCidade, String cidade, String ordem, String cnaes, int idTipoEndereco, String idsJuridica);
}
