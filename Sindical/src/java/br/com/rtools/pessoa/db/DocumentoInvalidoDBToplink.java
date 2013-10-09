package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.DocumentoInvalido;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class DocumentoInvalidoDBToplink extends DB implements DocumentoInvalidoDB {

    @Override
    public boolean insert(DocumentoInvalido documentoInvalido) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(documentoInvalido);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean update(DocumentoInvalido documentoInvalido) {
        try {
            getEntityManager().merge(documentoInvalido);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(DocumentoInvalido documentoInvalido) {
        try {
            getEntityManager().remove(documentoInvalido);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public DocumentoInvalido pesquisaCodigo(int id) {
        DocumentoInvalido result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("DocumentoInvalido.pesquisaID");
            qry.setParameter("pid", id);
            result = (DocumentoInvalido) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select docs from DocumentoInvalido docs");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<DocumentoInvalido> pesquisaNumeroBoleto(String numero) {
        List<DocumentoInvalido> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select docInv "
                    + "  from DocumentoInvalido docInv "
                    + " where docInv.documentoInvalido like '%" + numero + "%'");
            result = qry.getResultList();
            return result;
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List<DocumentoInvalido> pesquisaNumeroBoletoPessoa() {
        List vetor;
        List<DocumentoInvalido> result = new ArrayList();
        String textQuery = "";
        try {
            textQuery = "select doc.id as idi, doc.ds_documento_invalido,pes.id "
                    + "  from pes_documento_invalido doc, pes_pessoa pes "
                    + " where '000'||substring(replace(replace(replace(pes.ds_documento,'/',''),'-',''),'.',''),1,12) = doc.ds_documento_invalido";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            vetor = qry.getResultList();
            if (!vetor.isEmpty()) {
                for (int i = 0; i < vetor.size(); i++) {
                    result.add(pesquisaCodigo((Integer) ((Vector) vetor.get(i)).get(0)));
                }
            }
            return result;
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
}
