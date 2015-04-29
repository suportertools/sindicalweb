package br.com.rtools.associativo.db;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Spc;
import java.util.List;
import java.util.Vector;

public interface LancamentoIndividualDB {

    public List pesquisaResponsavel(int id_pessoa, boolean desconto_folha);

    public List pesquisaContribuinteLancamento(int id_pessoa);

    public List pesquisaMovimentoFisica(int id_pessoa);

    public List<Juridica> listaEmpresaConveniada(int id_servico);

    public List<Juridica> listaEmpresaConveniadaPorSubGrupo(int id_sub_grupo);

    public List<Spc> listaSerasa(int id_pessoa);

    public List<Vector> pesquisaServicoValor(int id_pessoa, int id_servico);
}
