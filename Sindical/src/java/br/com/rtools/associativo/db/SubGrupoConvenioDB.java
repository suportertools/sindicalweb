package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ConvenioServico;
import br.com.rtools.associativo.SubGrupoConvenio;
import java.util.List;

public interface SubGrupoConvenioDB {

    public List listaSubGrupoConvenioPorGrupo(int idGrupoConvenio);

    public boolean existeSubGrupoConvenio(SubGrupoConvenio subGrupoConvenio);

    public List listaServicosDisponiveis(int idSubGrupoConvenio);

    public List listaServicosDisponiveisPorGrupoFinanceiro(Integer idSubGrupoConvenio, Integer idGrupoFinanceiro);

    public List listaServicosDisponiveisPorSubGrupoFinanceiro(Integer idSubGrupoConvenio, Integer idSubGrupoFinanceiro);

    public List<ConvenioServico> listaServicosAdicionados(int idSubGrupoConvenio);
//    public List pesquisaSubGrupoConvênioComServico(int idSubGrupo);
//
//    public List pesquisaSubGrupoConvênioSemServico(int idSubGrupo);
}
