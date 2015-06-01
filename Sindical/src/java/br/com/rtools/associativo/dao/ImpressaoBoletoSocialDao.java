package br.com.rtools.associativo.dao;

import br.com.rtools.associativo.MensalidadesAtrasadas;
import br.com.rtools.principal.DB;
import javax.persistence.Query;

public class ImpressaoBoletoSocialDao extends DB{
    public MensalidadesAtrasadas pesquisaMensalidadesAtrasadasPessoa(Integer id_pessoa){
        try {
            Query qry = getEntityManager().createQuery("SELECT ma FROM MensalidadesAtrasadas ma WHERE ma.pessoa.id = "+id_pessoa);
            return (MensalidadesAtrasadas) qry.getSingleResult();
        }catch(Exception e){
            e.getMessage();
        }
        return null;
    }
}
