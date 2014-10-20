package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.DocumentoInvalido;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class DocumentoInvalidoDBToplink extends DB implements DocumentoInvalidoDB {
    @Override
    public List<DocumentoInvalido> pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select docs from DocumentoInvalido docs where docs.checado = false");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
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
        String textQuery = 
                "SELECT di.* " +
                "  FROM pes_documento_invalido di " +
                " WHERE ('000'||di.ds_documento_invalido " +
                "	IN ( " +
                "            SELECT '000'||SUBSTRING(REPLACE(REPLACE(REPLACE(p.ds_documento,'/',''),'-',''),'.',''),1,12) " +
                "              FROM pes_pessoa p ORDER BY p.ds_documento " +
                "        ) " +
                "        OR di.ds_documento_invalido IN ( " +
                "			SELECT '000'||SUBSTRING(REPLACE(REPLACE(REPLACE(p.ds_documento,'/',''),'-',''),'.',''),1,12) " +
                "                               FROM pes_pessoa p ORDER BY p.ds_documento " +
                "        ) " +
                " ) " +
                " AND di.checado = false ";
//            textQuery = "select doc.id as idi, doc.ds_documento_invalido,pes.id "
//                    + "  from pes_documento_invalido doc, pes_pessoa pes "
//                    + " where '000'||substring(replace(replace(replace(pes.ds_documento,'/',''),'-',''),'.',''),1,12) = doc.ds_documento_invalido";
        try {
            Query qry = getEntityManager().createNativeQuery(textQuery, DocumentoInvalido.class);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
}
