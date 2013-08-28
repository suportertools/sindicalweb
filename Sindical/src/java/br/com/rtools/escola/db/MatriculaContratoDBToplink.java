package br.com.rtools.escola.db;

import br.com.rtools.escola.MatriculaContrato;
import br.com.rtools.escola.MatriculaContratoServico;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MatriculaContratoDBToplink extends DB implements MatriculaContratoDB {

    @Override
    public MatriculaContrato pesquisaCodigo(int id) {
        MatriculaContrato result = null;
        try{
            Query qry = getEntityManager().createNamedQuery("MatriculaContrato.pesquisaID");
            qry.setParameter("pid", id);
            result = (MatriculaContrato) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }
    
    @Override
    public MatriculaContrato pesquisaCodigoServico(int servico) {
        MatriculaContrato result = null;
        try{
            Query qry = getEntityManager().createQuery(" SELECT mcs.contrato FROM MatriculaContratoServico mcs WHERE mcs.servicos.id = :servico ");
            qry.setParameter("servico", servico);
            result = (MatriculaContrato) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return result;
    }
    
    @Override
    public boolean pesquisaTitulo(String titulo) {
        List result = null;
        try{
            Query qry = getEntityManager().createQuery(" SELECT mc FROM MatriculaContrato mc WHERE mc.titulo = :titulo ");
            qry.setParameter("titulo", titulo);
            if(qry.getResultList().size() > 0){
                return true;
            }
        }catch(Exception e){
            e.getMessage();
            return false;
        }
        return false;
    }

    @Override
    public List pesquisaTodos() {
        try{
            Query qry = getEntityManager().createQuery(" SELECT mc FROM MatriculaContrato mc ");
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }
    
    @Override
    public List pesquisaTodosPorModulo(int idModulo) {
        try{
            Query qry = getEntityManager().createQuery(" SELECT mc FROM MatriculaContrato mc WHERE mc.modulo.id = :idModulo ");
            qry.setParameter("idModulo", idModulo);
            return (qry.getResultList());
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }
    
    @Override
    public List<MatriculaContratoServico> pesquisaMatriculaContratoServico(int idMatriculaContrato) {
        List<MatriculaContratoServico> list;
        try{
            Query qry = getEntityManager().createQuery(" SELECT mcs FROM MatriculaContratoServico mcs WHERE mcs.contrato.id = :idMatriculaContrato ");
            qry.setParameter("idMatriculaContrato", idMatriculaContrato);
            list = qry.getResultList();
            return list;
        }catch(Exception e){
            e.getMessage();
            return new ArrayList();
        }
    }
    
    @Override
    public boolean validaMatriculaContratoServico(int idMatriculaContrato, int idServico) {
        try{
            Query qry = getEntityManager().createQuery(" SELECT mcs FROM MatriculaContratoServico mcs WHERE mcs.contrato.id = :idMatriculaContrato AND mcs.servicos.id = :idServico ");
            qry.setParameter("idMatriculaContrato", idMatriculaContrato);
            qry.setParameter("idServico", idServico);
            if(qry.getResultList().size() > 0){
                return true;
            }
        }catch(Exception e){
            e.getMessage();
            return false;
        }
        return false;
    }
}