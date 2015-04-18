package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.DescontoServicoEmpresa;
import br.com.rtools.financeiro.Servicos;
import java.util.List;

public interface DescontoServicoEmpresaDB {

    public boolean existeDescontoServicoEmpresa(DescontoServicoEmpresa descontoServicoEmpresa);

    public List<DescontoServicoEmpresa> listaTodos();

    public List<DescontoServicoEmpresa> listaTodosPorEmpresa(int idJuridica);

    public List<DescontoServicoEmpresa> pesquisaDescontoServicoEmpresas(String pesquisaPor, String descricao, String comoPesquisa);

    public DescontoServicoEmpresa pesquisaDescontoServicoEmpresa(DescontoServicoEmpresa descontoServicoEmpresa);

    public List<Servicos> listaTodosServicosDisponiveis(Integer id_empresa, Integer id_subgrupo_financeiro);

    public List<Servicos> listaTodosServicosDisponiveis(Integer id_empresa, Integer id_grupo_financeiro, Integer id_subgrupo_financeiro);
}
