package br.com.rtools.associativo.db;

import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.pessoa.Pessoa;
import java.util.List;
import java.util.Vector;

public interface MovimentosReceberSocialDB {

    /**
     * <ul>
     * <li> <strong> RESULT </strong> </li>
     * <li> 00 - SERVIÇO -> DESCRIÇÃO; </li>
     * <li> 01 - TIPO SERVIÇO -> DESCRIÇÃO;</li>
     * <li> 02 - MOVIMENTO -> REFERÊNCIA;</li>
     * <li> 03 - VALOR;</li>
     * <li> 04 - VALOR MULTA;</li>
     * <li> 05 - VALOR MULTA COM ACRÉSCIMO;</li>
     * <li> 06 - DESCONTO;</li>
     * <li> 07 - VALOR MULTA COM CORREÇÃO;</li>
     * <li> 08 - DATA BAIXA;</li>
     * <li> 09 - VALOR BAIXA;</li>
     * <li> 10 - E/S;</li>
     * <li> 11 - RESPONSÁVEL -> NOME;</li>
     * <li> 12 - BENEFICIÁRIO -> NOME;</li>
     * <li> 13 - RESPONSÁVEL -> ID;</li>
     * <li> 14 - MOVIMENTO -> ID;</li>
     * <li> 15 - LOTE -> ID;</li>
     * <li> 16 - LOTE -> LANÇAMENTO;</li>
     * <li> 17 - MOVIMENTO -> DOCUMENTO;</li>
     * <li> 18 - DIAS EM ATRASO;</li>
     * <li> 19 - MULTA;</li>
     * <li> 20 - JUROS;</li>
     * <li> 21 - CORREÇÃO;</li>
     * <li> 22 - CAIXA -> NOME;</li>
     * <li> 23 - LOTE BAIXA -> ID;</li>
     * <li> 24 - MOVIMENTO -> DOCUMENTO;</li>
     * <li> 25 - TITULAR -> NOME;</li>
     * <li> 26 - BENEFECIÁRIO -> ID;</li>
     * </ul>
     *
     * @param id_pessoa (Beneficiário do movimento)
     * @param id_responsavel (Responsável pelo movimento)
     * @param por_status (Status)
     * @param referencia (Referência)
     * @param tipoPessoa (Se é física ou jurídica)
     * @param lote_baixa (Id da baixa)
     * @return
     */
    public List pesquisaListaMovimentos(String id_pessoa, String id_responsavel, String por_status, String referencia, String tipoPessoa, String lote_baixa);

    public List dadosSocio(int id_lote);

    public Pessoa pesquisaPessoaPorBoleto(String boleto, int id_conta_cobranca);

    public float[] pesquisaValorAcrescimo(int id_movimento);

    public List<Vector> listaBoletosAbertosAgrupado(int id_pessoa, boolean atrasados);

    public List<Movimento> listaMovimentosAbertosAnexarAgrupado(int id_pessoa);

    public List<Movimento> listaMovimentosPorNrCtrBoleto(String nr_ctr_boleto);

    public Pessoa responsavelBoleto(String nr_ctr_boleto);

    public List<TransferenciaCaixa> transferenciaCaixa(Integer id_fechamento_caixa_saida);
}
