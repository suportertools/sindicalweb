package br.com.rtools.atendimento.db;

import br.com.rtools.atendimento.AteMovimento;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.sistema.SisPessoa;
import java.util.List;
import javax.persistence.EntityManager;

public interface AtendimentoDB {

    public EntityManager getEntityManager();

    public boolean pessoaOposicao(String cpf);

    public SisPessoa pessoaDocumento(String valor);

    public List<AteMovimento> listaAteMovimento(String cpf, String por);

    public List listaAteMovimentos(String cpf, String por, int id_filial);

    public boolean existeAtendimento(AteMovimento ateMovimento);
    
    public Senha pesquisaSenha(int id_atendimento);
}
