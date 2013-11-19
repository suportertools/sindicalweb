package br.com.rtools.relatorios.db;

import java.util.List;

public interface RelatorioContabilidadesDB {

    public List pesquisaContabilidades();

    public List pesquisaQntEmpresas(int id_contabilidade);
    
    public int quantidadeEmpresas();

    public List pesquisarCnaeContabilidade();

    public List listaRelatorioContabilidades(String pEmpresas, int indexEmp1, int indexEmp2, String tipoPCidade, String cidade, String ordem, int idTipoEndereco);
}
