package br.com.rtools.escola.db;

import br.com.rtools.escola.MatriculaContrato;
import br.com.rtools.escola.MatriculaContratoCampos;
import br.com.rtools.escola.MatriculaContratoServico;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class MatriculaContratoDBToplink extends DB implements MatriculaContratoDB {

    @Override
    public MatriculaContrato pesquisaCodigoServico(int servico) {
        MatriculaContrato result = null;
        try {
            Query qry = getEntityManager().createQuery(" SELECT mcs.contrato FROM MatriculaContratoServico mcs WHERE mcs.servicos.id = :servico ");
            qry.setParameter("servico", servico);
            result = (MatriculaContrato) qry.getSingleResult();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public boolean existeMatriculaContrato(MatriculaContrato matriculaContrato) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT MC FROM MatriculaContrato AS MC WHERE MC.titulo = :titulo AND MC.modulo.id = :idModulo ");
            qry.setParameter("idModulo", matriculaContrato.getModulo().getId());
            qry.setParameter("titulo", matriculaContrato.getTitulo());
            if (qry.getResultList().size() > 0) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery(" SELECT MC FROM MatriculaContrato AS MC ORDER BY MC.dataCadastro DESC, MC.dataAtualizado DESC, MC.titulo ASC  ");
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public List pesquisaTodosPorModulo(int idModulo) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT mc FROM MatriculaContrato mc WHERE mc.modulo.id = :idModulo ");
            qry.setParameter("idModulo", idModulo);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    @Override
    public List<MatriculaContratoServico> pesquisaMatriculaContratoServico(int idMatriculaContrato) {
        List<MatriculaContratoServico> list;
        try {
            Query qry = getEntityManager().createQuery(" SELECT mcs FROM MatriculaContratoServico mcs WHERE mcs.contrato.id = :idMatriculaContrato ");
            qry.setParameter("idMatriculaContrato", idMatriculaContrato);
            list = qry.getResultList();
            return list;
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public boolean validaMatriculaContratoServico(int idMatriculaContrato, int idServico) {
        try {
            Query qry = getEntityManager().createQuery(" SELECT mcs FROM MatriculaContratoServico mcs WHERE mcs.contrato.id = :idMatriculaContrato AND mcs.servicos.id = :idServico ");
            qry.setParameter("idMatriculaContrato", idMatriculaContrato);
            qry.setParameter("idServico", idServico);
            if (qry.getResultList().size() > 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public boolean existeMatriculaContratoCampo(MatriculaContratoCampos mcc, String tipoVerificacao) {
        String queryCampo;
        if (tipoVerificacao.equals("campo")) {
            queryCampo = "UPPER(MCC.campo) = '" + mcc.getCampo().toUpperCase() + "'";
        } else if (tipoVerificacao.equals("variavel")) {
            queryCampo = "UPPER(MCC.variavel) = '" + mcc.getCampo().toUpperCase() + "'";
        } else if (tipoVerificacao.equals("todos")) {
            queryCampo = "UPPER(MCC.variavel) = '" + mcc.getCampo().toUpperCase() + "' AND UPPER(MCC.campo) = '" + mcc.getCampo().toUpperCase() + "'";
        } else {
            return false;
        }
        try {
            Query qry = getEntityManager().createQuery(" SELECT MCC FROM MatriculaContratoCampos AS MCC WHERE " + queryCampo + " AND MCC.modulo.id = " + mcc.getModulo().getId());
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            return false;
        }
    }

    @Override
    public List listaMatriculaContratoCampo(int idModulo) {
        List list = new ArrayList();
        String tipoPesquisaModulo = "";
        if (idModulo > 0) {
            tipoPesquisaModulo = " WHERE MCC.modulo.id = :idModulo ";
        }
        try {
            Query query = getEntityManager().createQuery(" SELECT MCC FROM MatriculaContratoCampos AS MCC "+tipoPesquisaModulo+" ORDER BY MCC.modulo.descricao ASC, MCC.campo ASC, MCC.variavel ");
            if (idModulo > 0) {
                query.setParameter("idModulo", idModulo);
            }
            list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return list;
        } finally {
            return list;
        }
    }
    
    @Override
    public List listaModulosMatriculaContratoCampos() {
        List list = new ArrayList();
        try {
            Query query = getEntityManager().createQuery(" SELECT MCC.modulo FROM MatriculaContratoCampos AS MCC GROUP BY MCC.modulo ORDER BY MCC.modulo.descricao ASC ");
            list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return list;
        } finally {
            return list;
        }
    }
}