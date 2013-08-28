
package br.com.rtools.atendimento.db;

import br.com.rtools.atendimento.AteMovimento;
import br.com.rtools.atendimento.AtePessoa;
import java.util.List;
import javax.persistence.EntityManager;


public interface AtendimentoDB {
    public EntityManager getEntityManager();
    public boolean pessoaOposicao(String cpf);
    public AtePessoa pessoaDocumento(String valor);
    public List<AteMovimento> listaAteMovimento(String cpf, String por);
    public List listaAteMovimentos(String cpf, String por);
    public boolean validaAtendimento(AteMovimento ateMovimento);
}

