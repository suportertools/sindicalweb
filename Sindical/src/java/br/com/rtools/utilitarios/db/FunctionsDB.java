package br.com.rtools.utilitarios.db;

import br.com.rtools.pessoa.Pessoa;
import java.util.Date;

public interface FunctionsDB {

    /**
     * Trazer o responsável
     *
     * @param idPessoa
     * @param decontoFolha
     * @return
     */
    public int responsavel(int idPessoa, boolean decontoFolha);

    /**
     *
     * @param idPessoa
     * @param idServico
     * @param date
     * @param tipo (0 -> Valor (já calculado) - ), (1 -> Valor até o vencimento
     * (já calculado)), (2 -> Taxa até o vencimento (já calculado))
     * @return float valor
     */
    public float valorServico(int idPessoa, int idServico, Date date, int tipo, Integer id_categoria);
    
    public float valorServicoCheio(int idPessoa, int idServico, Date date);

    /**
     * Retorna a idade da pessoa
     *
     * @param campoData --> Nome do campo
     * @param dataString --> Default current_date
     * @param idPessoa
     * @return
     */
    public int idade(String campoData, String dataString, int idPessoa);

    /**
     * Retorna operações e linhas de comando passados via SQL
     *
     * @param script --> Nome da linha de comando
     * @return
     */
    public String scriptSimples(String script);

    /**
     * Retorna quantidade de vagas disponíveis para cadastro de turma
     *
     * @param turma ID da turma
     * @return int
     */
    public int vagasEscolaTurma(int turma);
    
    public boolean demissionaSocios(int id_grupo_cidade, int nr_quantidade_dias);
    
    public boolean incluiPessoaComplemento();
    
    public Pessoa titularDaPessoa(int id_pessoa);
}
