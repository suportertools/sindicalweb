package br.com.rtools.utilitarios.dao;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class FindDao extends DB {

    /**
     * Nome da tabela onde esta a lista de filiais Ex:
     * findNotInByTabela('seg_filial_rotina', 'id_filial', 1);
     *
     * @param classid (Objeto.class => default null)
     * @param table (Tabela principal)
     * @param order (Ordenação => default null)
     * @param table_not_in (Use alias T+colum)
     * @param column
     * @param colum_filter_key Nome da coluna do filtro
     * @return Todas as rotinas não usadas em uma chave conforme o valor
     * @param colum_filter_value Valor do filtro
     * @param where default null (Use T1 para a tabela principal)
     */
    public List findNotInByTabela(Class classid, String table, String[] order, String table_not_in, String column, String colum_filter_key, String colum_filter_value, String where) {
        if (column == null || column.isEmpty()) {
            return new ArrayList();
        }
        if (where == null || where.isEmpty()) {
            where = "";
        }
        if (colum_filter_key == null || colum_filter_key.isEmpty() || colum_filter_value == null || colum_filter_value.isEmpty()) {
            return new ArrayList();
        }
        String order_by = "";
        try {
            for (int i = 0; i < order.length; i++) {
                if (i == 0) {
                    order_by += " ORDER BY T1." + order[i];
                } else {
                    order_by += "\n, T1." + order[i];
                }
            }
        } catch (Exception e) {

        }
        try {
            String queryString
                    = "     SELECT T1.* FROM " + table + " AS T1                                    \n"
                    + "      WHERE T1.id NOT IN (                                                   \n"
                    + "	           SELECT T2." + column + "                                         \n"
                    + "              FROM " + table_not_in + " AS T2                                       \n"
                    + "             WHERE T2." + colum_filter_key + " = " + colum_filter_value + "  \n"
                    + "          GROUP BY T2." + column + "                                         \n"
                    + ")                                                                            \n"
                    + where + "                                                                     \n"
                    + order_by;
            Query query;
            if (classid == null) {
                query = getEntityManager().createNativeQuery(queryString);
            } else {
                query = getEntityManager().createNativeQuery(queryString, classid);
            }
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
}
