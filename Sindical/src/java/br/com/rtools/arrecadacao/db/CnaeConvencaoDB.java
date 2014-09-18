package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.CnaeConvencao;
import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.PatronalCnae;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import java.util.List;

public interface CnaeConvencaoDB {

    public Convencao pesquisarCnaeConvencao(int idJuridica);

    public Convencao pesquisarCnaeConvencaoPorPessoa(int idPessoa);

    public List pesquisarCnaeConvencaoPorConvencao(int id);

    public CnaeConvencao pesquisaCnaeComConvencao(int idCnae);

    public List<Juridica> pesquisarJuridicaPorCnae(int idCnae);

    public List<Cnae> listaCnaePorConvencao(int id_convencao);

    public List<PatronalCnae> listaCnaePorPatronal(int id_patronal);
}
