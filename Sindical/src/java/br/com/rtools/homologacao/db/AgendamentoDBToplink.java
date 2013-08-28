package br.com.rtools.homologacao.db;

import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.homologacao.Agendamento;
import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.Senha;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class AgendamentoDBToplink extends DB implements AgendamentoDB {

    
    @Override
    public boolean insert(Agendamento agendamento) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(agendamento);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    
    @Override
    public boolean update(Agendamento agendamento) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(agendamento);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    
    @Override
    public boolean delete(Agendamento agendamento) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(agendamento);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    
    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select c from Agendamento c");
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    
    @Override
    public Agendamento pesquisaCodigo(int id) {
        Agendamento result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Agendamento.pesquisaID");
            qry.setParameter("pid", id);
            if (!qry.getResultList().isEmpty()) {
                result = (Agendamento) qry.getSingleResult();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    
    @Override
    public Agendamento pesquisaProtocolo(int id) {
        Agendamento result = null;
        try {
            Query qry = getEntityManager().createQuery("select a from Agendamento a where a.id = :pid "
                    + "   and a.agendador is null "
                    + "   and a.horarios is null ");
            qry.setParameter("pid", id);
            if (!qry.getResultList().isEmpty()) {
                result = (Agendamento) qry.getSingleResult();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    public List pesquisaAgendado(int idFilial, Date data) {
        String dataCampo = "";
        if (data != null) {
            dataCampo = "   and a.dtData = :data ";
        }
        try {
            Query qry = getEntityManager().createQuery("select a "
                    + "  from Agendamento a where a.horarios is not null"
                    + dataCampo
                    + "   and a.status.id = 2"
                    + "   and a.horarios.ativo = true"
                    + "   and a.filial.id = :idFilial"
                    + "   order by a.horarios.hora");
            if (data != null) {
                qry.setParameter("data", data);
            }
            qry.setParameter("idFilial", idFilial);
            if (!qry.getResultList().isEmpty()) {
                List xxx = (qry.getResultList());
                return xxx;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }
    
    public List<Agendamento> pesquisaNaoAtendido(int idFilial, Date dataInicial, Date dataFinal) {
        List<Agendamento> agendamentos = new ArrayList<Agendamento>();
        String dataCampo = "";
        if (dataInicial != null) {
            dataCampo = " AND age.dt_data = '"+DataHoje.converteData(dataInicial)+"'  ";
        }
        if (dataFinal != null && dataInicial != null) {
            dataCampo = " AND age.dt_data BETWEEN '"+DataHoje.converteData(dataInicial)+"' AND '"+DataHoje.converteData(dataFinal)+"'  ";
        }
        try {
            String textoQry =   "     SELECT age.id                                      "
                              + "       FROM hom_agendamento age                         "
                              + " INNER JOIN hom_horarios hor ON hor.id = age.id_horario "
                              + "      WHERE age.id_horario IS NOT NULL                  " 
                              +              dataCampo
                              + "                             "
                              + "        AND age.id_status = 7                           "
                              + "        AND hor.ativo = true                            "
                              + "        AND age.id_filial = "+idFilial 
                              + "   ORDER BY hor.ds_hora                                 ";
            Query qry = getEntityManager().createNativeQuery(textoQry);
            if (!qry.getResultList().isEmpty()) {
                SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
                List list = qry.getResultList();
                for(int i = 0; i < list.size(); i++){
                    agendamentos.add((Agendamento) dB.pesquisaCodigo((Integer) ((List) list.get(i)).get(0), "Agendamento"));
                }
                return agendamentos;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }
    
    
    @Override
    public List pesquisaAgendadoDataMaior(Date data) {
        try {
            Query qry = getEntityManager().createQuery("select a "
                    + "  from Agendamento a where a.horarios is not null"
                    + "   and a.dtData >= :data"
                    + "   and a.status.id = 2"
                    + "   and a.horarios.ativo = true"
                    + "   order by a.dtData, a.horarios.hora");
            qry.setParameter("data", data);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    
    @Override
    public List pesquisaAgendadoPorEmpresa(Date data, int idEmpresa) {
        try {
            Query qry = getEntityManager().createQuery("select a "
                    + "  from Agendamento a where a.horarios is not null"
                    + "   and a.dtData = :data"
                    + "   and a.status.id = 2"
                    + "   and a.horarios.ativo = true"
                    + "   and a.pessoaEmpresa.juridica.pessoa.id = :idEmpresa"
                    + "   order by a.horarios.hora");
            qry.setParameter("data", data);
            qry.setParameter("idEmpresa", idEmpresa);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    
    @Override
    public List pesquisaAgendadoPorEmpresaSemHorario(int id_filial, Date data, int idEmpresa) {
        try {
            Query qry = getEntityManager().createQuery("select a "
                    + "  from Agendamento a where (a.dtData = :data or a.dtData is null)"
                    + "   and a.status.id = 2"
                    + "   and a.filial.id = :idFilial"
                    + "   and a.pessoaEmpresa.juridica.pessoa.id = :idEmpresa"
                    + "   order by a.id");


            qry.setParameter("data", data);
            qry.setParameter("idEmpresa", idEmpresa);
            qry.setParameter("idFilial", id_filial);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    
    @Override
    public List pesquisaAgendadoPorEmpresaDataMaior(int idEmpresa) {
        try {
            Query qry = getEntityManager().createQuery("select a "
                    + "  from Agendamento a where a.status.id = 2"
                    + "   and a.pessoaEmpresa.juridica.pessoa.id = :idEmpresa");
            qry.setParameter("idEmpresa", idEmpresa);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    public List pesquisaCancelado(int idFilial, Date data, int idUsuario) {
        String dataCampo = "";
        String homologadorCampo = "";
        if (data != null) {
            dataCampo = "   and a.dtData = :data ";
        }
        if (idUsuario != 0) {
            homologadorCampo = "   and a.homologador.id = :usuario ";
        }
        try {
            Query qry = getEntityManager().createQuery("select a "
                    + "  from Agendamento a where a.horarios is not null"
                    + dataCampo
                    + "   and a.status.id = 3"
                    + "   and a.horarios.ativo = true"
                    + "   and a.filial.id = :idFilial"
                    + homologadorCampo
                    + "   order by a.horarios.hora");
            if (data != null) {
                qry.setParameter("data", data);
            }
            if (idUsuario != 0) {
                qry.setParameter("usuario", idUsuario);
            }
            qry.setParameter("idFilial", idFilial);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    public List pesquisaHomologado(int idFilial, Date data, int idUsuario) {
        String dataCampo = "";
        String homologadorCampo = "";
        if (data != null) {
            dataCampo = "   and a.dtData = :data ";
        }
        if (idUsuario != 0) {
            homologadorCampo = "   and a.homologador.id = :usuario ";
        }
        try {
            Query qry = getEntityManager().createQuery("select a "
                    + "  from Agendamento a where a.horarios is not null"
                    + dataCampo
                    + "   and a.status.id = 4"
                    + "   and a.horarios.ativo = true"
                    + "   and a.filial.id = :idFilial"
                    + homologadorCampo
                    + "   order by a.horarios.hora");
            if (data != null) {
                qry.setParameter("data", data);
            }
            if (idUsuario != 0) {
                qry.setParameter("usuario", idUsuario);
            }
            qry.setParameter("idFilial", idFilial);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    
    @Override
    public List<Agendamento> pesquisaAgendamento(int idStatus, int idFilial, Date dataInicial, Date dataFinal, int idUsuario, int idPessoaFisica, int idPessoaJuridica) {
        
        List<Agendamento> agendamentos = new ArrayList<Agendamento>();
        String dataCampo = "";
        String homologadorCampo = "";
        String statusCampo = "";
        String innerPessoaEmpresa = "";
        String pessoaEmpresaCampo = "";
        if(idPessoaFisica > 0  || idPessoaJuridica > 0){
            innerPessoaEmpresa = " INNER JOIN pes_pessoa_empresa pesemp ON pesemp.id = age.id_pessoa_empresa ";
            if(idPessoaFisica > 0){
                pessoaEmpresaCampo = " AND pesemp.id_fisica = "+idPessoaFisica;
            }else{
                pessoaEmpresaCampo = " AND pesemp.id_juridica = "+idPessoaJuridica;                
            }
        }
        if (dataInicial != null) {
            dataCampo = " AND age.dt_data = '"+DataHoje.converteData(dataInicial)+"'  ";
        }
        if (dataFinal != null && dataInicial != null) {
            dataCampo = " AND age.dt_data BETWEEN '"+DataHoje.converteData(dataInicial)+"' AND '"+DataHoje.converteData(dataFinal)+"'  ";
        }
        if (idUsuario != 0) {
            homologadorCampo = " and age.id_homologador = "+idUsuario+" ";
        }
        if(idStatus > 0){
            statusCampo = " AND age.id_status = "+idStatus;
        }
        try {
            String textoQry =   "     SELECT age.id                                      "
                              + "       FROM hom_agendamento age                         "
                              + " INNER JOIN hom_horarios hor ON hor.id = age.id_horario "
                              +              innerPessoaEmpresa
                              + "      WHERE age.id_horario IS NOT NULL                  " 
                              +              dataCampo
                              +              homologadorCampo                            
                              +              statusCampo
                              +              pessoaEmpresaCampo
                              + "        AND hor.ativo = true                            "
                              + "        AND age.id_filial = "+idFilial 
                              + "      LIMIT 1000                                        ";
            Query qry = getEntityManager().createNativeQuery(textoQry);
            if (!qry.getResultList().isEmpty()) {
                SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
                List list = qry.getResultList();
                String stringIn = "";
                for(int i = 0; i < list.size(); i++){
                    if(i == 0){
                        stringIn = ((Integer) ((List) list.get(i)).get(0)).toString();
                    }else{
                        stringIn += " , " +((Integer) ((List) list.get(i)).get(0)).toString();
                    }
                }
                Query qryListaAgendamento = getEntityManager().createQuery( " SELECT A FROM Agendamento AS A WHERE A.id IN("+stringIn+") ORDER BY A.dtData DESC, A.horarios.hora ASC " );
                if(!qryListaAgendamento.getResultList().isEmpty()){
                    agendamentos = qryListaAgendamento.getResultList();                    
                }
                return agendamentos;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }
    
    public List<Agendamento> pesquisaAgendamentoPorProtocolo(int numeroProtocolo) {
        List<Agendamento> agendamentos = new ArrayList<Agendamento>();
        try {
            Query qry = getEntityManager().createQuery( " SELECT A FROM Agendamento AS A WHERE A.id = :id " );
            qry.setParameter("id", numeroProtocolo);
            if(!qry.getResultList().isEmpty()){                
                agendamentos = qry.getResultList();
            }
            return agendamentos;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }
    
    public List<Agendamento> pesquisaAtendimento(int idFilial, Date dataInicial, Date dataFinal, int idUsuario) {
        List<Agendamento> agendamentos = new ArrayList<Agendamento>();
        String dataCampo = "";
        String homologadorCampo = "";
        if (dataInicial != null) {
            dataCampo = " AND age.dt_data = '"+DataHoje.converteData(dataInicial)+"'  ";
        }
        if (dataFinal != null && dataInicial != null) {
            dataCampo = " AND age.dt_data BETWEEN '"+DataHoje.converteData(dataInicial)+"' AND '"+DataHoje.converteData(dataFinal)+"'  ";
        }
        if (idUsuario != 0) {
            homologadorCampo = " and a.id_homologador = "+idUsuario+" ";
        }        
        try {
            String textoQry =   "     SELECT age.id                                      "
                              + "       FROM hom_agendamento age                         "
                              + " INNER JOIN hom_horarios hor ON hor.id = age.id_horario "
                              + "      WHERE age.id_horario IS NOT NULL                  " 
                              +              dataCampo
                              +              homologadorCampo                            
                              + "        AND age.id_status = 5                           "
                              + "        AND hor.ativo = true                            "
                              + "        AND age.id_filial = "+idFilial 
                              + "   ORDER BY hor.ds_hora                                 ";
            Query qry = getEntityManager().createNativeQuery(textoQry);
            if (!qry.getResultList().isEmpty()) {
                SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
                List list = qry.getResultList();
                for(int i = 0; i < list.size(); i++){
                    agendamentos.add((Agendamento) dB.pesquisaCodigo((Integer) ((List) list.get(i)).get(0), "Agendamento"));
                }
                return agendamentos;
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    
    @Override
    public List pesquisaTodos(int idFilial) {
        try {
            Query qry = getEntityManager().createQuery("select a "
                    + "  from Agendamento a where a.horarios is not null"
                    + "   and a.horarios.ativo = true"
                    + "   and a.filial.id = :idFilial"
                    + "   order by a.horarios.hora, a.dtData DESC"
                    + "    ");
            qry.setParameter("idFilial", idFilial);
            qry.setMaxResults(300);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    
//    @Override
//    public int pesquisaQntdDisponivel(int idFilial, Horarios horarios, Date data) {
//        try {
//            String text = " select ( "
//                    + "          (select count(*) from hom_agendamento where id_horario = " + horarios.getId() + " and id_filial = " + idFilial + " and dt_data = '" + data + "' and id_status = 2) + "
//                    + "          (select case when (select count(*) "
//                    + "                               from hom_cancelar_horario "
//                    + "                              where id_horarios = " + horarios.getId() 
//                    + "                                and id_filial = " + idFilial
//                    + "                                and dt_data = '" + data + "'"
//                    + "                             ) = 0 then 0 else (select nr_quantidade "
//                    + "                                                 from hom_cancelar_horario "
//                    + "                                                where id_horarios = " + horarios.getId() 
//                    + "                                                  and id_filial = " + idFilial
//                    + "                                                  and dt_data = '" + data + "'"
//                    + "                                              )  end as quantidade) "
//                    + "       ) qnt";
//            Query qry = getEntityManager().createNativeQuery(text);
//            List list = qry.getResultList();
//            if (!list.isEmpty()) {
//                return (Integer.valueOf(String.valueOf((Long) ((List) list.get(0)).get(0))));
//            }
//        } catch (EJBQLException e) {
//            // e.printStackTrace();
//        }
//        return -1;
//    }
    
    @Override
    public int pesquisaQntdDisponivel(int idFilial, Horarios horarios, Date data) {
        try {
            String text = " " 
            +"      SELECT CASE WHEN  "  
            +"      ( SELECT nr_quantidade  "
            +"          FROM hom_horarios   "
            +"         WHERE id = " + horarios.getId() + " ) -                                                                                                                                         "    
            +"      ( SELECT func_nullInteger ( "
            +"          ( SELECT nr_quantidade "
            +"              FROM hom_cancelar_horario "
            +"             WHERE id_horarios = " + horarios.getId() 
            +"               AND dt_data = '" + data + "' "
            +"          )"
            +"        ) "
            +"      ) - "
            +"    ( SELECT func_nullInteger ("
            +"          ( SELECT cast(count(*) AS int) "
            +"              FROM hom_agendamento "
            +"             WHERE id_horario = " + horarios.getId() + " "
            +"               AND id_filial = " + idFilial + " "
            +"               AND dt_data = '" + data + "' "
            +"               AND id_status = 2 "
            +"          ) "
            + "     ) "
            + "  ) IS NULL THEN 0 ELSE "
            +"      ( SELECT "  
            +"      ( SELECT nr_quantidade  "
            +"          FROM hom_horarios   "
            +"         WHERE id = " + horarios.getId() + " ) -                                                                                                                                         "    
            +"      ( SELECT func_nullInteger ( "
            +"          ( SELECT nr_quantidade "
            +"              FROM hom_cancelar_horario "
            +"             WHERE id_horarios = " + horarios.getId() 
            +"               AND dt_data = '" + data + "' "
            +"          )"
            +"        ) "
            +"      ) - "
            +"    ( SELECT func_nullInteger ("
            +"          ( SELECT cast(count(*) AS int) "
            +"              FROM hom_agendamento "
            +"             WHERE id_horario = " + horarios.getId() + " "
            +"               AND id_filial = " + idFilial + " "
            +"               AND dt_data = '" + data + "' "
            +"               AND id_status = 2 "
            +"          ) "
            + "     ) ) ) END; " 
                    
                    
                    ;                
            Query qry = getEntityManager().createNativeQuery(text);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                try {
                    Integer quantidade = Integer.valueOf(String.valueOf(((List) list.get(0)).get(0)));
                    if(quantidade < 0){
                        return 0;
                    }
                    return quantidade;
                } catch (Exception e) { 
                    return 0;
                }
            }
        } catch (EJBQLException e) {
            // e.printStackTrace();
        }
        return -1;
    }
    
    @Override
    public int pesquisaQuantidadeAgendado(int idFilial, Horarios horarios, Date data) {
        try {
            String text = " SELECT count(*) FROM hom_agendamento WHERE id_horario = " + horarios.getId() + " AND id_filial = " + idFilial + " AND dt_data = '" + data + "' AND id_status = 2 ";
            Query qry = getEntityManager().createNativeQuery(text);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return (Integer.valueOf(String.valueOf((Long) ((List) list.get(0)).get(0))));
            }
        } catch (EJBQLException e) {
            // e.printStackTrace();
        }
        return -1;
    }

    
    @Override
    public List pesquisaTodosHorariosDisponiveis(int idFilial, int idDiaSemana) {
        try {
            Query qry = getEntityManager().createQuery(
                      "   SELECT h "
                    + "     FROM Horarios h "
                    + "    WHERE h.ativo = true "
                    + "      AND h.filial.id = :idFilial "
                    + "      AND h.semana.id = :idDiaSemana "
                    + " ORDER BY h.hora");
            qry.setParameter("idFilial", idFilial);
            qry.setParameter("idDiaSemana", idDiaSemana);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    
    @Override
    public PessoaEmpresa pesquisaPessoaEmpresaOutra(String doc) {
        PessoaEmpresa result = new PessoaEmpresa();
        try {
            Query qry = getEntityManager().createQuery("select pesEmp"
                    + "  from PessoaEmpresa pesEmp,"
                    + "       Pessoa pes"
                    + " where pesEmp.fisica.pessoa.id = pes.id"
                    + "   and pesEmp.dtDemissao is null"
                    + "   and pes.documento like :Sdoc");
            qry.setParameter("Sdoc", doc);
            if (!qry.getResultList().isEmpty()) {
                result = (PessoaEmpresa) qry.getSingleResult();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    
    @Override
    public List pesquisaPessoaEmpresaPertencente(String doc) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select pesEmp"
                    + "  from PessoaEmpresa pesEmp,"
                    + "       Pessoa pes"
                    + " where pesEmp.fisica.pessoa.id = pes.id"
                    + //"   and pesEmp.dtDemissao is null" +
                    "   and pes.documento like :Sdoc order by pesEmp.id desc");
            qry.setParameter("Sdoc", doc);
            if (!qry.getResultList().isEmpty()) {
                result = qry.getResultList();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    
    @Override
    public List pesquisaEmpresaEmDebito(int id_pessoa, String vencimento) {
        try {
            String queryString = "select id from fin_movimento where id_pessoa = " + id_pessoa + " and dt_vencimento < '" + vencimento + "' and is_ativo = true and id_baixa is null";
            Query qry = getEntityManager().createNativeQuery(queryString);
            if (!qry.getResultList().isEmpty()) {
                return qry.getResultList();
            }
        } catch (EJBQLException e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    
    @Override
    public List pesquisaAgendamentoPorPessoaEmpresa(int idPessoaEmpresa) {
        try {
            Query qry = getEntityManager().createQuery("select a "
                    + "  from Agendamento a where a.pessoaEmpresa.id = " + idPessoaEmpresa);
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new ArrayList();
    }

    
    @Override
    public Oposicao pesquisaFisicaOposicao(String cpf, int id_juridica) {
        Oposicao result = null;
        try {
            Query qry = getEntityManager().createQuery("select o "
                    + "  from Oposicao o where o.oposicaoPessoa.cpf = '" + cpf + "' and o.juridica.id = " + id_juridica);
            if (!qry.getResultList().isEmpty()) {
                result = (Oposicao) qry.getSingleResult();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    
    @Override
    public List<Oposicao> pesquisaFisicaOposicaoSemEmpresa(String cpf) {
        List<Oposicao> result = new ArrayList();
        try {
            String referencia = DataHoje.livre(new Date(), "yyyyMM");
            Query qry = getEntityManager().createQuery("select o "
                    + "  from Oposicao o where o.oposicaoPessoa.cpf = '" + cpf + "' "
                    + "   and '" + referencia + "' BETWEEN CONCAT( SUBSTRING(o.convencaoPeriodo.referenciaInicial, 4, 8), SUBSTRING(o.convencaoPeriodo.referenciaInicial, 0, 3) ) "
                    + "   and                   CONCAT( SUBSTRING(o.convencaoPeriodo.referenciaFinal, 4, 8), SUBSTRING(o.convencaoPeriodo.referenciaFinal, 0, 3) ) order by o.id desc");
            if (!qry.getResultList().isEmpty()) {
                result = qry.getResultList();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    
    @Override
    public Oposicao pesquisaFisicaOposicaoAgendamento(String cpf, int id_juridica, String referencia) {
        Oposicao result = null;
        try {
            Query qry = getEntityManager().createQuery("select o "
                    + "  from Oposicao o where o.oposicaoPessoa.cpf = '" + cpf + "' "
                    + "   and o.juridica.id = " + id_juridica
                    + "   and '" + referencia + "' BETWEEN CONCAT( SUBSTRING(o.convencaoPeriodo.referenciaInicial, 4, 8), SUBSTRING(o.convencaoPeriodo.referenciaInicial, 0, 3) ) "
                    + "   and                   CONCAT( SUBSTRING(o.convencaoPeriodo.referenciaFinal, 4, 8), SUBSTRING(o.convencaoPeriodo.referenciaFinal, 0, 3) )");
            if (!qry.getResultList().isEmpty()) {
                result = (Oposicao) qry.getSingleResult();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return result;
    }

    
    @Override
    public Agendamento pesquisaFisicaAgendada(int id_fisica) {
        Agendamento result = null;
        try {
            Query qry = getEntityManager().createQuery("select a "
                    + "  from Agendamento a where a.pessoaEmpresa.fisica.id = " + id_fisica + " and a.dtData >= :data and (a.status.id = 2 or a.status.id = 5)");
            qry.setParameter("data", DataHoje.dataHoje());
            if (!qry.getResultList().isEmpty()) {
                result = (Agendamento) qry.getSingleResult();
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return result;
    }

//    public Agendamento pesquisaFisicaAgendada(int id_fisica, Date data){
//        Agendamento result = new Agendamento();
//        try{
//            Query qry = getEntityManager().createQuery("select a " +
//                                                       "  from Agendamento a where a.pessoaEmpresa.fisica.id = " +id_fisica+" and a.dtData >= :data and (a.status.id = 2 or a.status.id = 5)");
//            qry.setParameter("data", data);
//            result = (Agendamento)qry.getSingleResult();
//        }catch(Exception e){
//            result = null;
//       ///     e.printStackTrace();
//        }
//        return result;
//    }
    
    @Override
    public int pesquisaUltimaSenha(int id_filial) {
        int result = 0;
        try {
            Query qry = getEntityManager().createQuery("SELECT max(s.senha) FROM Senha s WHERE s.dtData = :data AND s.filial.id = "+id_filial);
            qry.setParameter("data", DataHoje.dataHoje());
            if (!qry.getResultList().isEmpty()) {
                result = (Integer) qry.getSingleResult();
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return result;
    }

    
    @Override
    public Senha pesquisaSenhaAgendamento(int id_agendamento) {
        Senha result = new Senha();
        try {
            Query qry = getEntityManager().createQuery("SELECT S FROM Senha AS S WHERE S.agendamento.id = " + id_agendamento);
            if (!qry.getResultList().isEmpty()) {
                result = (Senha) qry.getSingleResult();
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return result;
    }

    
    @Override
    public Senha pesquisaSenhaAtendimento(int id_filial) {
        Senha result = new Senha();
        try {
            Query qry = getEntityManager().createQuery("select s "
                                                     + " from Senha s "
                                                     + " where s.senha = (select min(s2.senha) from Senha s2 where s2.dtData = :data and s2.mesa = 0 and s2.filial.id = "+id_filial+") "
                                                     + " and s.dtData = :data and s.filial.id = "+id_filial);
            qry.setParameter("data", DataHoje.dataHoje());
            if (!qry.getResultList().isEmpty()) {
                result = (Senha) qry.getSingleResult();
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return result;
    }

    
    @Override
    public Senha pesquisaAtendimentoIniciado(int id_usuario, int nr_mesa, int id_filial) {
        Senha result = new Senha();
        try {
            Query qry = getEntityManager().createQuery(
                    "  SELECT S "
                    + "  FROM Senha AS S "
                    + " WHERE S.mesa = :nr_mesa "
                    + "   AND S.agendamento.homologador.id = :id_usuario "
                    + "   AND S.dtData = :data"
                    + "   AND S.agendamento.status.id = 5 and S.filial.id = :id_filial");
            qry.setParameter("data", DataHoje.dataHoje());
            qry.setParameter("nr_mesa", nr_mesa);
            qry.setParameter("id_usuario", id_usuario);
            qry.setParameter("id_filial", id_filial);
            if (!qry.getResultList().isEmpty()) {
                result = (Senha) qry.getSingleResult();
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return result;
    }

    
    @Override
    public boolean verificaNaoAtendidosSegRegistroAgendamento() {
        try {
            Query qry = getEntityManager().createNativeQuery(
                      " SELECT *                                            "
                    + "   FROM seg_registro                                 "
                    + "  WHERE (CURRENT_DATE - 1) = dt_atualiza_homologacao ");
            if (qry.getResultList().isEmpty()) {
                getEntityManager().getTransaction().begin();
                Query qryUpdateAgendamento = getEntityManager().createNativeQuery(
                          "UPDATE hom_agendamento                                                               "
                        + "   SET id_status = 7                                                                 "
                        + " WHERE dt_data > (                                                                   "
                        + "       SELECT dt_atualiza_homologacao                                                "
                        + "         FROM seg_registro                                                           "
                        + "        WHERE id = 1                                                                 "
                        + " )                                                                                   "
                        + "   AND dt_data < CURRENT_DATE                                                        "
                        + "   AND id NOT IN ( SELECT id_agendamento FROM hom_senha WHERE nr_senha IS NOT NULL ) "
                        + "   AND id_status = 2");
                if (qryUpdateAgendamento.executeUpdate() == 0) {
                    getEntityManager().getTransaction().rollback();
                    return false;
                }
                Query qryUpdateRegistro = getEntityManager().createNativeQuery(
                          " UPDATE seg_registro                              "
                        + "    SET dt_atualiza_homologacao = CURRENT_DATE - 1");
                if (qryUpdateRegistro.executeUpdate() == 0) {
                    getEntityManager().getTransaction().rollback();
                    return false;
                }
                getEntityManager().getTransaction().commit();
            }
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
        return true;
    }
}