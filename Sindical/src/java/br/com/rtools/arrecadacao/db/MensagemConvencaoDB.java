package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.MensagemConvencao;
import br.com.rtools.pessoa.Pessoa;
import java.util.List;
import javax.persistence.EntityManager;

public interface MensagemConvencaoDB {

    public boolean insert(MensagemConvencao mensagemConvencao);

    public boolean update(MensagemConvencao mensagemConvencao);

    public boolean delete(MensagemConvencao mensagemConvencao);

    public EntityManager getEntityManager();

    public MensagemConvencao pesquisaCodigo(int id);

    public List pesquisaTodos();

    public MensagemConvencao verificaMensagem(int idConvencao, int idServicos, int idTipoServicos, int idGrupo, String referencia);

    public List mesmoTipoServico(int idServicos, int idTipoServico, String ano);

    public List pesquisaSemServ4();

    public List pesquisaSemRef();

    public MensagemConvencao retornaDiaString(int idJuridica, String ref, int idTipoServico, int idServicos);

    public MensagemConvencao retornaDiaString(Pessoa pessoa, String ref, int idTipoServico, int idServicos);

    public MensagemConvencao pesquisarUltimaMensagem(int idConvencao, int idServicos, int idTipoServico, int idGrupoCidade);

    public List pesquisaTodosOrdenados(String referencia, int idServicos, int idTipoServico);

    public List<MensagemConvencao> pesquisaTodosAno(String ano);
}
