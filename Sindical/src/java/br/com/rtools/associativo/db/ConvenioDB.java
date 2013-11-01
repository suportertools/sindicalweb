package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Convenio;
import java.util.List;

public interface ConvenioDB {

    public List listaTodos(boolean orderPessoa, boolean orderGrupoConvenio, boolean orderSubGrupoConvenio);
            
    public List listaTodosPorPessoa(boolean orderPessoa, boolean orderGrupoConvenio, boolean orderSubGrupoConvenio, Convenio convenio);
    
    public boolean existeSubGrupoEmpresa(Convenio convenio);

    public List pesquisaConvenioPorGrupoPessoa(int idPessoaJuridica, int idGrupoConvenio);

    public List pesquisaJuridicaPorGrupoESubGrupo(int idSubGrupoConvenio, int idGrupo);
}
