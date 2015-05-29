package br.com.rtools.utilitarios.db;

import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.financeiro.ConfiguracaoFinanceiro;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class FunctionsDao extends DB implements FunctionsDB {

    /**
     * Trazer o responsável
     *
     * @param idPessoa
     * @param decontoFolha
     * @return
     */
    @Override
    public int responsavel(int idPessoa, boolean decontoFolha) {
        Integer idResponsavel = -1;
        try {
            String queryString = " SELECT func_responsavel(" + idPessoa + ", " + decontoFolha + ") ";
            Query query = getEntityManager().createNativeQuery(queryString);
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
     * @param id_categoria
     * @return float valor
     */
    @Override
    public float valorServico(int idPessoa, int idServico, Date date, int tipo, Integer id_categoria) {
        String dataString = DataHoje.converteData(date);
        String queryString = "SELECT func_valor_servico(" + idPessoa + ", " + idServico + ", '" + dataString + "', " + tipo + ", " + id_categoria + ") ";
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
    
    public float valorServicoCheio(int idPessoa, int idServico, Date date) {
        String dataString = DataHoje.converteData(date);
        String queryString = "SELECT func_valor_servico_cheio(" + idPessoa + ", " + idServico + ", '" + dataString + "') ";
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

    @Override
    public boolean demissionaSocios(int id_grupo_cidade, int nr_quantidade_dias) {
        try {
            Query query = getEntityManager().createNativeQuery(
                    "SELECT func_demissiona_socios(" + id_grupo_cidade + ", " + nr_quantidade_dias + ");");
            List list = query.getResultList();
            boolean xbo;
            if (!list.isEmpty()) {
                xbo = (Boolean) ((List) query.getSingleResult()).get(0);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean incluiPessoaComplemento() {
        try {
            Query query = getEntityManager().createNativeQuery(
                    "SELECT func_inclui_pessoa_complemento();"
            );
            List list = query.getResultList();
            boolean xbo;
            if (!list.isEmpty()) {
                xbo = (Boolean) ((List) query.getSingleResult()).get(0);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Pessoa titularDaPessoa(int id_pessoa) {
        try {
            Query query = getEntityManager().createNativeQuery("SELECT func_titular_da_pessoa(" + id_pessoa + ");");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Pessoa) new Dao().find(new Pessoa(), Integer.parseInt(((Vector) list.get(0)).get(0).toString()));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * Verificar se a pessoa esta inapinplente
     *
     * @param id_pessoa
     * @return
     */
    public Boolean inadimplente(Integer id_pessoa) {
        if (id_pessoa == -1) {
            return false;
        }
        Integer nr_carencia_dias;
        ConfiguracaoFinanceiro cf = (ConfiguracaoFinanceiro) new Dao().find(new ConfiguracaoFinanceiro(), 1);
        if (cf == null) {
            return true;
        }
        SociosDBToplink sociosDBToplink = new SociosDBToplink();
        Socios socios = sociosDBToplink.pesquisaSocioPorPessoaAtivo(id_pessoa);
        if (socios.getId() == -1) {
            nr_carencia_dias = cf.getCarenciaDias();
        } else {
            nr_carencia_dias = socios.getMatriculaSocios().getCategoria().getNrCarenciaBalcao();
        }
        try {
            Query query = getEntityManager().createNativeQuery("SELECT func_inadimplente(" + id_pessoa + ", " + nr_carencia_dias + ")");
            query.setMaxResults(1);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                Boolean bool = Boolean.parseBoolean(((List) list.get(0)).get(0).toString());
                return bool;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    /**
     * Gerar mensalidades
     *
     * @param id_pessoa
     * @param referencia
     */
    public void gerarMensalidades(Integer id_pessoa, String referencia) {
        gerarMensalidadesBoolean(id_pessoa, referencia);
    }

    /**
     * Gerar mensalidades boolean
     *
     * @param id_pessoa
     * @param referencia
     * @return
     */
    public Boolean gerarMensalidadesBoolean(Integer id_pessoa, String referencia) {
        try {
            Query query = getEntityManager().createNativeQuery("SELECT func_geramensalidades(" + id_pessoa + ", '" + referencia + "')");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public Boolean gerarBoletoSocial(List<Movimento> lista_movimento, String vencimento) {
        String ids = "";
        for (Movimento movimento : lista_movimento){
            if (ids.isEmpty())
                ids = ""+movimento.getId();
            else
                ids += ", "+movimento.getId();
        }
         
        try {
            Query query = getEntityManager().createNativeQuery("select func_gerar_boleto_ass('{"+ids+"}','"+vencimento +"');");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
