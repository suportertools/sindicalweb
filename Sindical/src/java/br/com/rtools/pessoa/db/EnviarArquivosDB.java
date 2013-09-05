package br.com.rtools.pessoa.db;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import java.util.List;

public interface EnviarArquivosDB {

    public Juridica pesquisaCodigo(int id);

    public List pesquisaContabilidades();

    public List pesquisaContribuintes(List<Convencao> listaConvencao, List<GrupoCidade> listaGrupoCidade, List<Cnae> listaCnae);

    public List<Convencao> listaConvencao();

    public List<GrupoCidade> listaConvencaoGrupoCidade(List<Convencao> listaConvencao);

    public List<Cnae> listaCnaeConvencao(List<Convencao> listaConvencao);
}