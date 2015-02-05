package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.CertidaoDisponivel;
import br.com.rtools.arrecadacao.CertidaoMensagem;
import br.com.rtools.arrecadacao.CertidaoTipo;
import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.Patronal;
import br.com.rtools.arrecadacao.PisoSalarial;
import br.com.rtools.arrecadacao.PisoSalarialLote;
import br.com.rtools.arrecadacao.RepisMovimento;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.pessoa.Juridica;
import java.util.List;

public interface WebREPISDB {

    public RepisMovimento pesquisaCodigoRepisMovimento(int id);

    public List listaContribuinteWeb();

    public List listaContabilidadeWeb();

    public List validaPessoaRepisAno(int idPessoa, int ano);
    
    public List validaPessoaRepisAnoTipoPatronal(int idPessoa, int ano, int id_tipo_certidao, int id_patronal);

    public List listaProtocolosPorContabilidade(int idPessoa, int ano);

    public List listaProtocolosPorPatronal(int idConvencao, int idGrupoCidade);

    public Patronal pesquisaPatronalPorPessoa(int idPessoa);

    public Patronal pesquisaPatronalPorConvGrupo(int id_convencao, int id_grupo_cidade);

    public PisoSalarialLote pesquisaPisoSalarial(int ano, int id_patronal, int id_porte);

    public List<PisoSalarial> listaPisoSalarialLote(int id_piso_lote);

    /**
     * <p>
     * Mudança de estrutura.</p>
     *
     * @deprecated
     * @param id_patronal
     * @return
     */
    public List listaProtocolosPorPatronalCnae(int id_patronal);

    public boolean pesquisaCnaePermitido(int id_cnae, int id_grupo_cidade);

    public List<RepisMovimento> listaRepisMovimento(String por, String descricao);

    public Juridica pesquisaEscritorioDaEmpresa(int id_pessoa);

    public List<Movimento> listaAcordoAberto(int id_pessoa);

    public Patronal pesquisaPatronalPorSolicitante(int id_solicitante);

    public List<RepisMovimento> listaProtocolosPorPatronal(int idPatronal);
    
    public List<CertidaoTipo> listaCertidaoTipo();
    
    public List<CertidaoDisponivel> listaCertidaoDisponivel(int id_cidade, int id_convencao);
    public List<ConvencaoPeriodo> listaConvencaoPeriodo(int id_cidade, int id_convencao);
    public List<ConvencaoPeriodo> listaConvencaoPeriodoData(int id_cidade, int id_convencao, String referencia);
    public List<RepisMovimento> pesquisarListaLiberacao(String por, String descricao, int id_patronal, String quantidade);
    //public List<RepisMovimento> pesquisarListaSolicitacao(String por, String descricao, int id_pessoa, int id_contabilidade, int ano);
    public List<RepisMovimento> pesquisarListaSolicitacao(String por, String descricao, int id_pessoa, int id_contabilidade);
    public CertidaoMensagem pesquisaCertidaoMensagem(int id_cidade, int id_certidao_tipo);
    
    public List listRepisPorPessoa(int idPessoa);
    
}
