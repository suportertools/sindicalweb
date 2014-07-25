package br.com.rtools.relatorios.db;

import br.com.rtools.relatorios.Relatorios;
import java.util.List;

public interface RelatorioHomologacaoDB {

    public List pesquisaHomologacao(Relatorios relatorio, boolean booEmpresa, int id_empresa, boolean booFuncionario, int id_funcionario, boolean booData, String data_ini, String data_fim,
            boolean booHomologador, int id_homologador, int id_filial, String ordem);
}
