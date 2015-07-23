package br.com.rtools.associativo.db;

import br.com.rtools.associativo.CategoriaDesconto;
import br.com.rtools.associativo.DescontoSocial;
import br.com.rtools.associativo.EventoServicoValor;
import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.associativo.Socios;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class SociosDBToplink extends DB implements SociosDB {

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select s from Socios s");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public Socios pesquisaSocioPorId(int idServicoPessoa) {
        try {
            Query qry = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.id = :pid");
            qry.setParameter("pid", idServicoPessoa);
            return (Socios) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return new Socios();
        }
    }

    @Override
    public List pesquisaSocios(String desc, String por, String como, String status) {
        List lista = new Vector<Object>();
        String textQuery = null;
        if (status.equals("inativo")) {
            status = "not";
        } else {
            status = "";
        }
        if (por.equals("nome")) {
            por = "nome";
            if (como.equals("P")) {
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select soc from Socios soc"
                        + " where UPPER(soc.servicoPessoa.pessoa.nome) like :desc "
                        + "   and soc.matriculaSocios.motivoInativacao is " + status + " null"
                        + " order by soc.servicoPessoa.pessoa.nome";
            } else if (como.equals("I")) {
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select soc from Socios soc"
                        + " where UPPER(soc.servicoPessoa.pessoa.nome) like :desc "
                        + "   and soc.matriculaSocios.motivoInativacao is " + status + " null"
                        + " order by soc.servicoPessoa.pessoa.nome";
            }
        }
        if (por.equals("documento")) {
            por = "documento";
            desc = desc.toLowerCase().toUpperCase() + "%";
            textQuery = "select soc from Socios soc"
                    + " where UPPER(soc.servicoPessoa.pessoa.documento) like :desc "
                    + "   and soc.matriculaSocios.motivoInativacao is " + status + " null"
                    + " order by soc.servicoPessoa.pessoa.nome";
        }
        try {
            Query qry = getEntityManager().createQuery(textQuery);
            if (!desc.equals("%%") && !desc.equals("%")) {
                qry.setParameter("desc", desc);
            }
            lista = qry.getResultList();
        } catch (Exception e) {
            lista = new Vector<Object>();
        }
        return lista;
    }

    @Override
    public List<Socios> listaDependentes(int id_matricula) {
        try {
            Query qry = getEntityManager().createQuery("select s "
                    + "  from Socios s "
                    + " where s.parentesco.id <> 1 "
                    + "   and s.matriculaSocios.id = " + id_matricula
                    + "   and s.servicoPessoa.ativo = true"
                    + " order by s.servicoPessoa.pessoa.nome");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList<Socios>();
        }
    }

    @Override
    public List<Socios> listaDependentesInativos(int id_matricula) {
        try {
            Query qry = getEntityManager().createQuery("select s "
                    + "  from Socios s "
                    + " where s.parentesco.id <> 1 "
                    + "   and s.matriculaSocios.id = " + id_matricula
                    + "   and s.servicoPessoa.ativo = false"
                    + " order by s.servicoPessoa.pessoa.nome");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList<Socios>();
        }
    }
//    public List pesquisaDependentes(int idPessoaSocio){
//        try{
//            Query qry = getEntityManager().createQuery("select s from Socios s " +
//                                                       " where s.parentesco.id <> 1 " +
//                                                       "   and s.matriculaSocios.id = ( " +
//                                                       "       select si.matriculaSocios.id from Socios si, ServicoPessoa sp " +
//                                                       "        where si.servicoPessoa.id = sp.id" +
//                                                       "          and si.parentesco.id = 1"+
//                                                       //"          and si.matriculaSocios.motivoInativacao is null " +
//                                                       "          and sp.pessoa.id = "+idPessoaSocio+" )");
//            return (qry.getResultList());
//        }catch(Exception e){
//            e.getMessage();
//            return null;
//        }
//    }

    @Override
    public List pesquisaDependentesOrdenado(int idMatricula) {
        try {
            Query qry = getEntityManager().createQuery("select s from Socios s "
                    + " where s.parentesco.id <> 1 "
                    + "   and s.matriculaSocios.id = " + idMatricula
                    + "   and s.servicoPessoa.ativo = true "
                    + " order by s.parentesco.id");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

//    public List pesquisaDependentesOrdenado(int idPessoaSocio){
//        try{
//            Query qry = getEntityManager().createQuery("select s from Socios s " +
//                                                       " where s.parentesco.id <> 1 " +
//                                                       "   and s.matriculaSocios.id = ( " +
//                                                       "       select si.matriculaSocios.id from Socios si, ServicoPessoa sp " +
//                                                       "        where si.servicoPessoa.id = sp.id" +
//                                                       "          and si.parentesco.id = 1" +
//                                                       //"          and si.matriculaSocios.motivoInativacao is null " +
//                                                       "          and sp.pessoa.id = "+idPessoaSocio+" )" +
//                                                       " order by s.parentesco.id");
//            return (qry.getResultList());
//        }catch(Exception e){
//            e.getMessage();
//            return null;
//        }
//    }
    @Override
    public Socios pesquisaSocioPorPessoa(int idPessoa) {
        Socios socios = new Socios();
        try {
            Query query = getEntityManager().createNativeQuery(
                    "  SELECT S.*                                                          \n"
                    + "  FROM soc_socios              AS S                                 \n"
                    + " INNER JOIN fin_servico_pessoa AS SP ON SP.id = S.id_servico_pessoa \n"
                    + " INNER JOIN pes_pessoa         AS P  ON P.id  = SP.id_pessoa        \n"
                    + "      WHERE SP.id_pessoa = " + idPessoa + "\n"
                    + "   ORDER BY SP.id", Socios.class);

            List<Socios> list = query.getResultList();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getServicoPessoa().isAtivo()) {
                    return list.get(i);
                }
                socios = list.get(i);
            }

//            Query qry = getEntityManager().createQuery(""
//                    + " SELECT s "
//                    + "   FROM Socios s "
//                    + "  WHERE s.servicoPessoa.pessoa.id = :pid "
//                    + "  ORDER BY s.servicoPessoa.id DESC");
//            qry.setParameter("pid", idPessoa);
            //soc = (Socios) qry.setMaxResults(1).getSingleResult();
//            soc = (Socios) qry.getSingleResult();
        } catch (EJBQLException e) {
            e.getMessage();
        }
        return socios;
    }

    @Override
    public Socios pesquisaSocioPorPessoaEMatriculaSocio(int idPessoa, int idMatriculaSocios) {
        Socios socio = new Socios();

        try {

            Query qry = getEntityManager().createNativeQuery(
                    "SELECT s.id "
                    + "  FROM soc_socios s "
                    + " INNER JOIN fin_servico_pessoa sp ON sp.id = s.id_servico_pessoa"
                    + " INNER JOIN pes_pessoa p ON p.id = sp.id_pessoa"
                    + " WHERE sp.id_pessoa = " + idPessoa
                    + "   AND s.id_matricula_socios = " + idMatriculaSocios
                    + " ORDER BY sp.id");

            List<Vector> lista = qry.getResultList();

            for (int i = 0; i < lista.size(); i++) {
                socio = (Socios) (new SalvarAcumuladoDBToplink()).pesquisaCodigo((Integer) lista.get(i).get(0), "Socios");
            }

            //            Query qry = getEntityManager().createQuery(""
            //                    + " SELECT s "
            //                    + "   FROM Socios s "
            //                    + "  WHERE s.servicoPessoa.pessoa.id = :pid "
            //                    + "  ORDER BY s.servicoPessoa.id DESC");
            //            qry.setParameter("pid", idPessoa);
            //soc = (Socios) qry.setMaxResults(1).getSingleResult();
            //            soc = (Socios) qry.getSingleResult();
        } catch (EJBQLException e) {
            e.getMessage();
        }
        return socio;
    }

    @Override
    public Socios pesquisaSocioPorPessoaAtivo(int idPessoa) {
        Socios soc = new Socios();
        try {
            Query qry = getEntityManager().createQuery("select s from Socios s"
                    + " where s.servicoPessoa.pessoa.id = :pid"
                    + "   and s.servicoPessoa.ativo = true");
            qry.setParameter("pid", idPessoa);
            List list = qry.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                soc = (Socios) qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return soc;
    }

    @Override
    public List<Socios> pesquisaSocioPorPessoaInativo(int idPessoa) {
        try {
            Query query = getEntityManager().createNativeQuery(
                    "       SELECT S.*                                                       \n "
                    + "       FROM soc_socios AS S                                           \n "
                    + " INNER JOIN fin_servico_pessoa sp ON SP.id = S.id_servico_pessoa      \n "
                    + "      WHERE SP.id_pessoa = " + idPessoa + "                           \n "
                    + "        AND SP.is_ativo = false                                       \n "
                    + "   ORDER BY S.id_matricula_socios DESC ", Socios.class
            );
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    @Override
    public List<Socios> pesquisaSocioPorPessoaTitularInativo(int idPessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.pessoa.id = :pessoa AND S.servicoPessoa.ativo = false AND S.parentesco.id = 1 ORDER BY S.id DESC");
            query.setParameter("pessoa", idPessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    @Override
    public Socios pesquisaSocioTitularInativoPorPessoa(int idPessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.matriculaSocios.titular.id = :pessoa AND S.servicoPessoa.pessoa.id = :pessoaTitular AND S.servicoPessoa.ativo = false ORDER BY S.matriculaSocios.id DESC");
            query.setParameter("pessoa", idPessoa);
            query.setParameter("pessoaTitular", idPessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Socios) list.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    @Override
    public List<Socios> listaSocioTitularInativoPorPessoa(int idPessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT S FROM Socios AS S WHERE S.servicoPessoa.pessoa.id = :pessoa AND S.servicoPessoa.ativo = false AND S.matriculaSocios.dtInativo IS NOT NULL ORDER BY S.matriculaSocios.dtInativo DESC");
            query.setParameter("pessoa", idPessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    @Override
    public List pesquisaSociosAtivos() {
        try {
            Query qry = getEntityManager().createQuery("select soc from Socios soc"
                    + " where soc.matriculaSocios.motivoInativacao is null");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaSociosInativos() {
        try {
            Query qry = getEntityManager().createQuery("select soc from Socios soc"
                    + " where soc.matriculaSocios.motivoInativacao is not null");
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public Socios pesquisaSocioDoDependente(int idDependente) {
        try {
            Query qry = getEntityManager().createQuery(
                    "select s "
                    + "  from Socios s "
                    + " where s.matriculaSocios.id = (select si.matriculaSocios.id "
                    + "                                 from Socios si "
                    + "                                where si.parentesco.id <> 1"
                    + "                                  and si.id = :pid )");
            qry.setParameter("pid", idDependente);
            return (Socios) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public Socios pesquisaSocioDoDependente(Pessoa pessoa) {
        try {
            Query qry = getEntityManager().createQuery("SELECT s FROM Socios s WHERE s.matriculaSocios.titular.id = :pid AND s.matriculaSocios.dtInativo IS NOT NULL");
//            Query qry = getEntityManager().createQuery(
//                    "select s "
//                    + "  from Socios s "
//                    + " where s.matriculaSocios.id = (select si.matriculaSocios.id "
//                    + "                                 from Socios si "
//                    + "                                where si.parentesco.id <> 1"
//                    + "                                  and si.servicoPessoa.pessoa.id = :pid )");
//            qry.setParameter("pid", pessoa.getId());
            return (Socios) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public float descontoSocioEve(int idPessoa, int idServico) {
        Query qry = null;
        DataHoje dh = new DataHoje();
        Socios socios = new Socios();
        CategoriaDesconto categoriaDesconto = new CategoriaDesconto();
        EventoServicoValor eveServicoValor = new EventoServicoValor();
        Fisica fisica = new Fisica();
        // PESQUISA PESSOA FISICA ------------------
        String textQry = "select f "
                + "  from Fisica f "
                + " where f.pessoa.id = " + idPessoa;
        try {
            qry = getEntityManager().createQuery(textQry);
            fisica = (Fisica) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return 0;
        }
        /// -----------

        // PESQUISA O SOCIO ---
        textQry = "select s "
                + "  from Socios s "
                + " where s.servicoPessoa.pessoa.id = " + idPessoa;
        try {
            qry = getEntityManager().createQuery(textQry);
            socios = (Socios) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return 0;
        }
        /// -----------

        // PESQUISA CATEGORIA DESCONTO -----
        textQry = "select cd "
                + "  from CategoriaDesconto cd"
                + " where cd.categoria.id = " + socios.getMatriculaSocios().getCategoria().getId()
                + "   and cd.servicoValor.servicos.id = " + idServico;
        try {
            qry = getEntityManager().createQuery(textQry);
            categoriaDesconto = (CategoriaDesconto) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return 0;
        }

        // PESQUISA EVE SERVICO VALOR -------------
        if (fisica.getNascimento().length() != 10) {
            return 0;
        }
        int idade = dh.calcularIdade(fisica.getDtNascimento());
        textQry = "select ev "
                + "  from EventoServicoValor ev "
                + " where ev.eventoServico.servicos.id = " + idServico
                + "   and ev.idadeInicial <= " + idade
                + "   and ev.idadeFinal >= " + idade;

        try {
            qry = getEntityManager().createQuery(textQry);
            eveServicoValor = (EventoServicoValor) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
            return 0;
        }

        // CALCULA VALOR COM DESCONTO ---
        if (categoriaDesconto.getDesconto() == 0) {
            return 0;
        }

        float soma = Moeda.multiplicarValores(eveServicoValor.getValor(), (Moeda.divisaoValores(categoriaDesconto.getDesconto(), 100)));
        soma = Moeda.subtracaoValores(eveServicoValor.getValor(), soma);
        return soma;
    }

    @Override
    public List<SocioCarteirinha> pesquisaCarteirinhasPorPessoa(int id_pessoa, int id_modelo) {
        try {
            Query qry = getEntityManager().createQuery(
                    " SELECT sc "
                    + " FROM SocioCarteirinha sc "
                    + "WHERE sc.pessoa.id = " + id_pessoa
                    + "  AND sc.modeloCarteirinha.id = " + id_modelo);
            return (qry.getResultList());
        } catch (Exception e) {
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaMotivoInativacao() {
        try {
            Query qry = getEntityManager().createQuery("select mi from SMotivoInativacao mi order by mi.descricao");
            return (qry.getResultList());
        } catch (Exception e) {
            //e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public boolean socioDebito(int idPessoa) {
        try {
            Query query = getEntityManager().createNativeQuery(""
                    + "     SELECT *                                            "
                    + "       FROM fin_movimento AS m                           "
                    + " INNER JOIN fin_lote AS l ON l.id = m.id_lote            "
                    + "      WHERE m.id_pessoa = " + idPessoa + "               "
                    + "        AND dt_vencimento < NOW()                        "
                    + "        AND id_baixa IS NULL                             "
                    + "        AND l.is_desconto_folha IS NULL                  "
                    + "   GROUP BY m.id_pessoa                                  "
            );
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public List<DescontoSocial> listaDescontoSocial(int id_categoria) {
        try {
            Query query = getEntityManager().createQuery(
                    "SELECT ds FROM DescontoSocial ds WHERE ds.categoria.id = :id_categoria"
            );

            query.setParameter("id_categoria", id_categoria);
            return query.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }

    @Override
    public List<ServicoPessoa> listaServicoPessoaPorDescontoSocial(Integer id_desconto_social, Integer id_pessoa) {
        try {
            String text_qry = "SELECT sp FROM ServicoPessoa sp WHERE sp.descontoSocial.id = " + id_desconto_social;
            if (id_pessoa != null) {
                text_qry += " AND sp.pessoa.id <> " + id_pessoa;
            }

            Query query = getEntityManager().createQuery(text_qry);

            return query.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
