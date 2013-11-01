package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ConvenioServico;
import br.com.rtools.associativo.SubGrupoConvenio;
import br.com.rtools.financeiro.Servicos;
import java.util.List;

public interface SubGrupoConvenioDB {

    public List listaSubGrupoConvenioPorGrupo(int idGrupoConvenio);

    public boolean existeSubGrupoConvenio(SubGrupoConvenio subGrupoConvenio);

    public List<Servicos> listaServicosDisponiveis(int idSubGrupoConvenio);

    public List<ConvenioServico> listaServicosAdicionados(int idSubGrupoConvenio);
//    public List pesquisaSubGrupoConvênioComServico(int idSubGrupo);
//
//    public List pesquisaSubGrupoConvênioSemServico(int idSubGrupo);
}
