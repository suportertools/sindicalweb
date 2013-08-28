package br.com.rtools.associativo.db;

import br.com.rtools.associativo.CategoriaDesconto;
import br.com.rtools.associativo.EventoServicoValor;
import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.associativo.Socios;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class SociosDBToplink extends DB implements SociosDB{
    @Override
    public boolean insert(Socios socios) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(socios);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(Socios socios) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(socios);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean delete(Socios socios) {
        try{
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(socios);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        }catch(Exception e){
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Socios pesquisaCodigo(int id) {
        Socios result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("Socios.pesquisaID");
            qry.setParameter("pid", id);
            result = (Socios) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery("select s from Socios s");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public Socios pesquisaSocioPorId(int idServicoPessoa) {
        try{
            Query qry = getEntityManager().createQuery(
                    "select s"
                    + " from Socios s"
                    + " where s.servicoPessoa.id = :pid");
            qry.setParameter("pid", idServicoPessoa);
            return (Socios) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            return new Socios();
        }
    }

    @Override
    public List pesquisaSocios(String desc, String por, String como, String status){
        List lista = new Vector<Object>();
        String textQuery = null;
        if (status.equals("inativo"))
            status = "not";
        else
            status = "";
        if(por.equals("nome")){
           por = "nome";
            if (como.equals("P")){
                desc = "%" + desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select soc from Socios soc" +
                            " where UPPER(soc.servicoPessoa.pessoa.nome) like :desc " +
                            "   and soc.matriculaSocios.motivoInativacao is "+status+" null"+
                            " order by soc.servicoPessoa.pessoa.nome";
            }else if (como.equals("I")){
                por = "nome";
                desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select soc from Socios soc" +
                            " where UPPER(soc.servicoPessoa.pessoa.nome) like :desc " +
                            "   and soc.matriculaSocios.motivoInativacao is "+status+" null"+
                            " order by soc.servicoPessoa.pessoa.nome";
            }
        }
        if(por.equals("documento")){
            por = "documento";
            desc = desc.toLowerCase().toUpperCase() + "%";
                textQuery = "select soc from Socios soc" +
                            " where UPPER(soc.servicoPessoa.pessoa.documento) like :desc " +
                            "   and soc.matriculaSocios.motivoInativacao is "+status+" null"+
                            " order by soc.servicoPessoa.pessoa.nome";
        }
        try{
            Query qry = getEntityManager().createQuery(textQuery);
                    if (!desc.equals("%%")&& !desc.equals("%"))
                        qry.setParameter("desc", desc);
                    lista = qry.getResultList();
        }
        catch(Exception e){
            lista = new Vector<Object>();
        }
        return lista;
    }

    @Override
    public List pesquisaDependentes(int idMatricula){
        try{
            Query qry = getEntityManager().createQuery("select s "+
                                                       "  from Socios s " +
                                                       " where s.parentesco.id <> 1 " +
                                                       "   and s.matriculaSocios.id = "+idMatricula);
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
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
    public List pesquisaDependentesOrdenado(int idMatricula){
        try{
            Query qry = getEntityManager().createQuery("select s from Socios s " +
                                                       " where s.parentesco.id <> 1 " +
                                                       "   and s.matriculaSocios.id = "+idMatricula+
                                                       " order by s.parentesco.id");
            return (qry.getResultList());
        }catch(Exception e){
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
        Socios soc = new Socios();
        try{
            Query qry = getEntityManager().createQuery(
                    "select s"
                    + " from Socios s"
                    + " where s.servicoPessoa.pessoa.id = :pid "
                    + " order by s.servicoPessoa.id desc");
//            Query qry = getEntityManager().createQuery(
//                    "select s"
//                    + " from Socios s"
//                    + " where s.servicoPessoa.pessoa.id = :pid"
//                    + "   and s.servicoPessoa.ativo = true");
            qry.setParameter("pid", idPessoa);
            qry.setMaxResults(1);
            soc = (Socios) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return soc;
    }

    @Override
    public Socios pesquisaSocioPorPessoaAtivo(int idPessoa) {
        Socios soc = new Socios();
        try{
            Query qry = getEntityManager().createQuery("select s from Socios s"+
                                                       " where s.servicoPessoa.pessoa.id = :pid" +
                                                       "   and s.matriculaSocios.motivoInativacao is null");
            qry.setParameter("pid", idPessoa);
            soc = (Socios) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return soc;
    }

    @Override
    public List pesquisaSocioPorPessoaInativo(int idPessoa) {
        try{
            Query qry = getEntityManager().createQuery("select s from Socios s"+
                                                       " where s.servicoPessoa.pessoa.id = :pid" +
                                                       "   and s.matriculaSocios.motivoInativacao is not null");
            qry.setParameter("pid", idPessoa);
            return qry.getResultList();
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaSociosAtivos(){
        try{
            Query qry = getEntityManager().createQuery("select soc from Socios soc" +
                                                       " where soc.matriculaSocios.motivoInativacao is null");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaSociosInativos(){
        try{
            Query qry = getEntityManager().createQuery("select soc from Socios soc" +
                                                       " where soc.matriculaSocios.motivoInativacao is not null");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public Socios pesquisaSocioDoDependente(int idDependente){
        try{
            Query qry = getEntityManager().createQuery(
                    "select s " +
                    "  from Socios s "+
                    " where s.dtValidadeCarteirinha is null " +
                    "   and s.matriculaSocios.id = (select si.matriculaSocios.id " +
                    "                                 from Socios si " +
                    "                                where si.parentesco.id <> 1" +
                    "                                  and si.id = :pid )");
            qry.setParameter("pid", idDependente);
            return (Socios) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public Socios pesquisaSocioDoDependente(Pessoa pessoa){
        try{
            Query qry = getEntityManager().createQuery(
                    "select s " +
                    "  from Socios s "+
                    " where s.dtValidadeCarteirinha is null " +
                    "   and s.matriculaSocios.id = (select si.matriculaSocios.id " +
                    "                                 from Socios si " +
                    "                                where si.parentesco.id <> 1" +
                    "                                  and si.servicoPessoa.pessoa.id = :pid )");
            qry.setParameter("pid", pessoa.getId());
            return (Socios) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public float descontoSocioEve(int idPessoa, int idServico){
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
        try{
            qry = getEntityManager().createQuery(textQry);
            fisica = (Fisica)qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            return 0;
        }
        /// -----------

        // PESQUISA O SOCIO ---
        textQry = "select s "
                 +"  from Socios s "
                 +" where s.servicoPessoa.pessoa.id = "+ idPessoa;
        try{
            qry = getEntityManager().createQuery(textQry);
            socios = (Socios)qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            return 0;
        }
        /// -----------


        // PESQUISA CATEGORIA DESCONTO -----
        textQry = "select cd "
                + "  from CategoriaDesconto cd"
                + " where cd.categoria.id = " + socios.getMatriculaSocios().getCategoria().getId()
                + "   and cd.servicos.id = " + idServico;
        try{
            qry = getEntityManager().createQuery(textQry);
            categoriaDesconto = (CategoriaDesconto)qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            return 0;
        }


        // PESQUISA EVE SERVICO VALOR -------------
        if (fisica.getNascimento().length() != 10){
            return 0;
        }
        int idade = dh.calcularIdade(fisica.getDtNascimento());
        textQry = "select ev "
                + "  from EventoServicoValor ev "
                + " where ev.eventoServico.servicos.id = " + idServico
                + "   and ev.idadeInicial <= " + idade
                + "   and ev.idadeFinal >= " + idade;

        try{
            qry = getEntityManager().createQuery(textQry);
            eveServicoValor = (EventoServicoValor)qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
            return 0;
        }

        // CALCULA VALOR COM DESCONTO ---
        if (categoriaDesconto.getDesconto() == 0 ){
            return 0;
        }

        float soma = Moeda.multiplicarValores(eveServicoValor.getValor(), ( Moeda.divisaoValores(categoriaDesconto.getDesconto(),100)));
        soma = Moeda.subtracaoValores(eveServicoValor.getValor(), soma);
        return soma;
    }

    @Override
    public List<SocioCarteirinha> pesquisaCarteirinhasPorSocio(int idSocios) {
        try{
            Query qry = getEntityManager().createQuery("select sc "
                                                     + "  from SocioCarteirinha sc"
                                                     + " where sc.socios.id = "+ idSocios);
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaMotivoInativacao() {
        try{
            Query qry = getEntityManager().createQuery("select mi from SMotivoInativacao mi order by mi.descricao");
            return (qry.getResultList());
        }catch(Exception e){
            //e.getMessage();
            return new ArrayList();
        }
    }
}
