package br.com.rtools.pessoa.dao;

import br.com.rtools.pessoa.BiometriaErro;
import br.com.rtools.principal.DB;
import javax.persistence.Query;

public class BiometriaErroDao extends DB {

    public BiometriaErro findByDecice(Integer device) {
        return find(device, null);
    }

    public BiometriaErro findByMac(Integer mac) {
        return find(null, mac);
    }

    public BiometriaErro find(Integer device, Integer mac) {
        try {
            Query query;
            String queryString = "";
            if (device != null) {
                queryString = "SELECT BE.* FROM pes_biometria_erro AS BE WHERE BE.nr_device = " + device;
                query = getEntityManager().createNativeQuery(queryString, BiometriaErro.class);
                return (BiometriaErro) query.getSingleResult();
            } else if (mac != null) {
                query = getEntityManager().createQuery("SELECT BE FROM BiometriaErro AS BE WHERE BE.macFilial.id = :mac");
                query.setParameter("mac", mac);
                return (BiometriaErro) query.getSingleResult();
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

}
