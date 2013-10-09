package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.MotivoInativacao;
import java.util.List;
import javax.persistence.EntityManager;

public interface MotivoInativacaoDB {

    public boolean insert(MotivoInativacao motivoInativacao);

    public boolean update(MotivoInativacao motivoInativacao);

    public boolean delete(MotivoInativacao motivoInativacao);

    public EntityManager getEntityManager();

    public MotivoInativacao pesquisaCodigo(int id);

    public List pesquisaTodos();

    public MotivoInativacao idMotivoInativacao(MotivoInativacao des_motivoInativacao);
}
