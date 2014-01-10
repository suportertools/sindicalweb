package br.com.rtools.utilitarios.db;

import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

public class FunctionsDBTopLink extends DB implements FunctionsDB {

    /**
     * Trazer o responsável
     *
     * @param idPessoa
     * @param decontoFolha
     * @return
     */
    @Override
    public int responsavel(int idPessoa, boolean decontoFolha) {
        int idResponsavel = -1;
        try {
            Query query = getEntityManager().createNativeQuery(" SELECT func_responsavel(" + idPessoa + ", " + decontoFolha + ") ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                idResponsavel = Integer.parseInt(((List) query.getSingleResult()).get(0).toString());
                if (idResponsavel == 0) {
                    idResponsavel = -1;
                }
            }
        } catch (Exception e) {
            return -1;
        }
        return idResponsavel;
    }

    /**
     *
     * @param idPessoa
     * @param idServico
     * @param date
     * @param tipo (0 -> Valor (já calculado) - ), (1 -> Valor até o vencimento
     * (já calculado)), (2 -> Taxa até o vencimento (já calculado))
     * @return float valor
     */
    @Override
    public float valorServico(int idPessoa, int idServico, Date date, int tipo) {
        String dataString = DataHoje.converteData(date);
        String queryString = "SELECT func_valor_servico(" + idPessoa + ", " + idServico + ", '" + dataString + "', " + tipo + ") ";
        try {
            Query qry = getEntityManager().createNativeQuery(queryString);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                list = (List) qry.getSingleResult();
                float valor = Float.parseFloat(list.get(0).toString());
                return valor;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

    /**
     * Retorna a idade da pessoa
     *
     * @param campoData --> Nome do campo
     * @param dataString --> Default current_date
     * @param idPessoa
     * @return
     */
    @Override
    public int idade(String campoData, String dataString, int idPessoa) {
        int idade = 0;
        try {
            Query query = getEntityManager().createNativeQuery("SELECT func_idade(" + campoData + ", " + dataString + ") FROM pes_fisica WHERE id_pessoa = " + idPessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                idade = Integer.parseInt(((List) query.getSingleResult()).get(0).toString());
            }
        } catch (Exception e) {
            idade = 0;
        }
        return idade;
    }

    /**
     * Retorna operações e linhas de comando passados via SQL
     *
     * @param script --> Nome da linha de comando
     * @return
     */
    @Override
    public String scriptSimples(String script) {
        String retorno = "";
        try {
            Query query = getEntityManager().createNativeQuery("SELECT " + script);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                retorno = ((List) query.getSingleResult()).get(0).toString();
            }
        } catch (Exception e) {
            retorno = "";
        }
        return retorno;
    }

    /**
     * Retorna quantidade de vagas disponíveis para cadastro de turma
     *
     * @param turma ID da turma
     * @return int
     */
    @Override
    public int vagasEscolaTurma(int turma) {
        int vagas = 0;
        try {
            Query query = getEntityManager().createNativeQuery("SELECT func_esc_turmas_vagas_disponiveis(" + turma + ");");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                vagas = Integer.parseInt(((List) query.getSingleResult()).get(0).toString());
            }
        } catch (Exception e) {
            vagas = 0;
        }
        return vagas;
    }
}
