package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.Spc;
import java.util.List;

public interface SpcDB {

    public List<Spc> lista (Spc spc, boolean filtro, boolean fitroPorPessoa);
    
    public List<Spc> lista (Spc spc, boolean filtro, boolean fitroPorPessoa, String descricaoPesquisa, String porPesquisa, String comoPesquisa);
    
    public boolean existeCadastroSPC(Spc spc);
    
}
