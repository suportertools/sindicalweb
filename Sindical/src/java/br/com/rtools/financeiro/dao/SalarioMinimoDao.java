/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.financeiro.dao;

import br.com.rtools.financeiro.SalarioMinimo;
import br.com.rtools.principal.DB;
import javax.persistence.Query;

public class SalarioMinimoDao extends DB {

    public SalarioMinimo salarioMinimoVigente() {
        try {
            Query query = getEntityManager().createQuery("SELECT SM FROM SalarioMinimo AS SM ORDER BY SM.id DESC ");
            query.setMaxResults(1);
            return (SalarioMinimo) query.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

}
