package br.com.rtools.pessoa.dao;

import br.com.rtools.pessoa.Biometria;
import br.com.rtools.pessoa.BiometriaDepartamento;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class BiometriaDao extends DB {

    public List pesquisaStatusPorComputador(Integer macFilial) {
        try {
            getEntityManager().clear();
            Query query = getEntityManager().createQuery("SELECT BS FROM BiometriaServidor AS BS WHERE BS.macFilial.id = :macFilial  ");
            query.setParameter("macFilial", macFilial);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List pesquisaBiometriaCapturaPorPessoa(Integer pessoa) {
        try {
            getEntityManager().clear();
            Query query = getEntityManager().createQuery("SELECT BC FROM BiometriaCaptura AS BC WHERE BC.pessoa.id = :pessoa ");
            query.setParameter("pessoa", pessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public List pesquisaBiometriaCapturaPorMacFilial(Integer macFilial) {
        try {
            getEntityManager().clear();
            Query query = getEntityManager().createQuery("SELECT BC FROM BiometriaCaptura AS BC WHERE BC.macFilial.id = :macFilial ");
            query.setParameter("macFilial", macFilial);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

    public Biometria pesquisaBiometriaPorPessoa(Integer pessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT B FROM Biometria AS B WHERE B.pessoa.id = :pessoa ");
            query.setParameter("pessoa", pessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Biometria) list.get(0);
            }
        } catch (Exception e) {

        }
        return null;
    }

    public List listaBiometriaDepartamentoPorPessoa(Integer pessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT BD FROM BiometriaDepartamento AS BD WHERE BD.biometria.pessoa.id = :pessoa ");
            query.setParameter("pessoa", pessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {

        }
        return new ArrayList();
    }

}
