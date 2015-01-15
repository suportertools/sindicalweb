package br.com.rtools.financeiro.db;

import br.com.rtools.associativo.HistoricoEmissaoGuias;
import br.com.rtools.financeiro.*;
import br.com.rtools.pessoa.Pessoa;
import java.util.ArrayList;
//import br.com.rtools.listax.GGeracao;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;

public interface MovimentoDB {

    public EntityManager getEntityManager();

    public Movimento pesquisaCodigo(int id);

    public String pesquisaDescMensagem(int id_tipo_servico, int id_servicos, int id_convencao, int id_grupo_cidade);

    public List movimentosAberto(int idPessoa, boolean sindical);

    public List pesquisaTodos();

    public List pesquisaPartidas(int idLote);

    public Movimento pesquisaContraPartida(int idLote);

    public List pesquisaLote();

    public Historico pesquisaHistorico(int id);

    public Cheques pesquisaCheques(int id);

    public String deleteChque(Cheques cheque);

    public String deleteHistorico(Historico historico);

    public List datasMovimento(int idServico, int idTipoServico, int idContaCobranca);

    public List datasMovimento();

    public Boleto pesquisaBoletos(String nrCtrBoleto);

    public List<Movimento> listaMovimentoPorNrCtrBoleto(String nrCtrBoleto);
    
    public MensagemCobranca pesquisaMensagemCobranca(int idMovimento);

    public List pesquisaPorVencimento(Date vencimento);

    public boolean verificaMovimentoArrecadacao(int idPessoa, String referencia, int idServico, int idTipoServico);

    public List listaMovimentosExtrato(String tipo, String faixa_data, String data_inicial, String data_final, String referencia_inicial, String referencia_final, String boleto_inicial, String boleto_final, int id_servico, int id_tipo_servico, int id_pessoa, String ordenacao, boolean movimentoDaEmpresa);
    
    public List<Vector> listaTodosMovimentos(boolean data, boolean contrib, boolean nrBoletos, boolean empresa, boolean tipo, String faixaData,
            Date descDataIni, Date descDataFin, String dtRefInicial, String dtRefFinal, int idContribuicao, int idTipoServico, String descNrBoletoIni,
            String descNrBoletoFin, int descEmpresa, String ordenacao, boolean movimentoDaEmpresa);

    public List listaRecebidasMovimentos(boolean data, boolean contrib, boolean nrBoletos, boolean empresa, boolean tipo, String faixaData,
            Date descDataIni, Date descDataFin, String dtRefInicial, String dtRefFinal, int idContribuicao, int idTipoServico, String descNrBoletoIni,
            String descNrBoletoFin, int descEmpresa, String ordenacao, boolean movimentoDaEmpresa);

    public List listaNaoRecebidasMovimentos(boolean data, boolean contrib, boolean nrBoletos, boolean empresa, boolean tipo, String faixaData,
            Date descDataIni, Date descDataFin, String dtRefInicial, String dtRefFinal, int idContribuicao, int idTipoServico, String descNrBoletoIni,
            String descNrBoletoFin, int descEmpresa, String ordenacao, boolean movimentoDaEmpresa);

    public List listaAtrazadasMovimentos(boolean data, boolean contrib, boolean nrBoletos, boolean empresa, boolean tipo, String faixaData,
            Date descDataIni, Date descDataFin, String dtRefInicial, String dtRefFinal, int idContribuicao, int idTipoServico, String descNrBoletoIni,
            String descNrBoletoFin, int descEmpresa, String ordenacao, boolean movimentoDaEmpresa);

    public List pesquisaMovimentoPorJuridica(int id);

    public List pesquisaServicoCobranca(String id[]);

    public List pesquisaMovPorJuriData(int idJuri, Date vencimento);

    public List pesquisaOrdenarServicoMovimento(List<String> id);

    public Object[] pesquisaValorFolha(Movimento movimento);

    public Object[] pesquisaValorFolha(int idServico, int idTipo, String ref, int idPessoa);

    public Object[] listaImpressaoGeral(int idServico, int idTipoServico, int idContaCobranca, String isEscritorio, List<String> id, List<Integer> listaConvencao, List<Integer> listaGrupoCidade, String todasContas, String email, int id_esc);

    public Movimento pesquisaMovPorNumDocumento(String numero);

    public List<Movimento> pesquisaMovPorTipoDocumentoList(String tipoDocumento, String ref, int idContaCobranca, TipoServico tipoServico);

    public List<Movimento> pesquisaMovPorNumDocumentoListSindical(String numero, int idContaCobranca);

    public List<Movimento> pesquisaMovPorNumDocumentoList(String numero, Date vencimento, int idContaCobranca);

    public List<Movimento> pesquisaMovPorNumDocumentoListBaixadoArr(String numero, int idContaCobranca);

    public List<Movimento> pesquisaMovPorNumDocumentoListArr(String numero, int idContaCobranca);

    public List<Movimento> pesquisaMovPorNumDocumentoListBaixadoAss(String numero, int idContaCobranca);

    public List<Movimento> pesquisaMovPorNumDocumentoListAss(String numero, int idContaCobranca);
    
    public List<Movimento> pesquisaMovPorNumPessoaListBaixado(String numero, int idContaCobranca);

    //public List<Movimento> movimentosDoLote(int idLote);

    public int movimentosDoLote(int idLote);

    public List<Movimento> pesquisaMovPorTipoDocumentoBaixado(String tipoDocumento, String ref, int idContaCobranca, TipoServico tipoServico);

    public boolean delete(MensagemCobranca mensagemCobranca);

    public boolean delete(Movimento movimento);

    public Movimento pesquisaMovimentos(int idPessoa, String idRef, int idTipoServ, int idServicos);

    public List movimentosBaixados(int idLoteBaixa);

    public boolean extornarMovimento(List<Integer> listaLoteBaixa);

    public List pesquisaLogWeb(int idMovimento);

    public boolean delete(ImpressaoWeb impressaoWeb);

    public List pesquisaGuia(String campo, String valor);

    public List<Movimento> pesquisaGuia(Guia guia);

    public List<Movimento> pesquisaAcordoAberto(int idAcordo);

    public List<Movimento> pesquisaAcordoTodos(int idAcordo);

    public List<Movimento> pesquisaAcordoParaExclusao(int idAcordo);

    public boolean excluirAcordoIn(String ids, int idAcordo);
//    public String gerarContribuicao(List<GGeracao> listaGeracao, List<Servicos> listaServicos, int idRotina);

    public List movimentosAbertoComVencimentoOriginal(int idPessoa);

    public boolean pesquisaMatriculaBaixa(int idMatricula);

    public int inserirBoletoNativo(int id_conta_cobranca);

    public List<FormaPagamento> pesquisaFormaPagamento(int id_baixa);

    public List<Movimento> movimentoIdbaixa(int id_baixa);

    public List<Movimento> listaMovimentoBaixaOrder(int id_baixa);
    
    public List<Vector> pesquisaAcrescimo(int id_movimento);

    public List<Movimento> listaMovimentosDoLote(int idLote);

    public List<Movimento> pesquisaMovimentoChaveValor(int id_pessoa, String ref, int id_conta_cobranca, int id_tipo_servico);

    public Double funcaoJuros(int id_pessoa, int id_servico, int id_tipo_servico, String referencia);

    public Double funcaoMulta(int id_pessoa, int id_servico, int id_tipo_servico, String referencia);

    public Double funcaoCorrecao(int id_pessoa, int id_servico, int id_tipo_servico, String referencia);

    public List movimentosBaixadosPorEvt(int idEvt);

    public Movimento pesquisaMovimentosAcordado(int idPessoa, String idRef, int idTipoServ, int idServicos);
    
    /** 
     * @param pessoa
     * @param vencimento ( Caso a data vencimento seja definida como null, será passado o parâmetro )
     * @return List<Movimento> (Lista movimentos em débito da pessoa)
     */      
    public List<Movimento> listaDebitoPessoa(Pessoa pessoa, Date vencimento);
    
    /**
     * @param pessoa
     * @param vencimento ( Caso a data vencimento seja definida como null, será passado o parâmetro )
     * @return boolean (true -> se existe débito / false -> caso não exista)
     */    
    public boolean existeDebitoPessoa(Pessoa pessoa, Date vencimento);
    
    public List<HistoricoEmissaoGuias> pesquisaHistoricoEmissaoGuias(int id_usuario);
    public HistoricoEmissaoGuias pesquisaHistoricoEmissaoGuiasPorMovimento(int id_usuario, int id_movimento);
    public Guia pesquisaGuias(int id_lote) ;
    public List<Movimento> pesquisaMovimentoCadastrado(String documento);
    
    public List<Impressao> listaImpressao(int id_movimento);
    
    public List<Movimento> listaMovimentoBeneficiarioServicoPeriodoAtivo(int id_beneficiario, int id_servico, int periodo_dias, boolean socio);
    public List<Movimento> listaMovimentoBeneficiarioServicoMesVigente(int id_beneficiario, int id_servico, boolean socio);
}
