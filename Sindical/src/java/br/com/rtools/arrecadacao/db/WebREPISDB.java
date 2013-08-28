package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.Patronal;
import br.com.rtools.arrecadacao.PisoSalarial;
import br.com.rtools.arrecadacao.PisoSalarialLote;
import br.com.rtools.arrecadacao.RepisMovimento;
import java.util.List;

public interface WebREPISDB {
    public RepisMovimento pesquisaCodigoRepisMovimento(int id);
    public List listaContribuinteWeb();
    public List listaContabilidadeWeb();
    public List validaPessoaRepisAno(int idPessoa, int ano);
    public List listaProtocolosPorContabilidade(int idPessoa, int ano);
    public List listaProtocolosPorPatronal(int idConvencao, int idGrupoCidade);
    public Patronal pesquisaPatronalPorPessoa(int idPessoa);
    public Patronal pesquisaPatronalPorConvGrupo(int id_convencao, int id_grupo_cidade);
    public PisoSalarialLote pesquisaPisoSalarial(int ano, int id_patronal, int id_porte);
    public List<PisoSalarial> listaPisoSalarialLote(int id_piso_lote);
    public List listaProtocolosPorPatronalCnae(int id_patronal);
    public boolean pesquisaCnaePermitido(int id_cnae, int id_grupo_cidade);

}
