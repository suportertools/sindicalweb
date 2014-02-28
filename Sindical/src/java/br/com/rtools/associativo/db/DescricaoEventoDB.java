package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEvento;
import br.com.rtools.associativo.DescricaoEvento;
import java.util.List;

public interface DescricaoEventoDB {

    public List<DescricaoEvento> pesquisaDescricaoPorGrupo(int idGrupoEvento);

    public List<AEvento> listaEventoPorDescricao(int idDescEvento);

    public boolean existeDescricaoEvento(DescricaoEvento de);
}
