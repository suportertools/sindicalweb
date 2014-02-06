package br.com.rtools.sistema.db;

import br.com.rtools.sistema.SisPessoa;
import java.util.List;

public interface SisPessoaDB {

    public List pesquisarSisPessoa(String desc, String por, String como);
    
    public SisPessoa sisPessoaExiste(SisPessoa sp);
    
    public SisPessoa sisPessoaExiste(SisPessoa sp, boolean porDocumento);

}
