package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;

public class RelatorioFechamentoBaileDao extends DB {

    public List<Vector> listaEventoBaile(Integer id_evento_baile) {
        String text
                = "      SELECT \n "
                + "	v.dt_emissao AS emissao,\n "
                + "	PO.DS_NOME AS operador, \n "
                + "	p.id AS codigo, \n "
                + "	p.ds_nome AS convidado, \n "
                + "	st.ds_descricao AS status, \n "
                + "	nr_mesa AS mesa, \n "
                + "	nr_convite as convite, \n "
                + "	mv.dt_vencimento as vencimento, \n "
                + "	b.dt_baixa as pagamento, \n "
                + "	mv.nr_valor_baixa as valor, \n "
                + "	pu.ds_nome as caixa, \n "
                + "	v.ds_obs as obs, \n "
                + "	ese.id_servicos as servico \n "
                + " FROM eve_evento_baile AS eb \n "
                + "INNER JOIN eve_evento AS e ON e.id = eb.id_evento \n "
                + "INNER JOIN eve_desc_evento AS de ON de.id = e.id_descricao_evento \n "
                + " LEFT JOIN eve_venda AS v ON v.id_evento = e.id \n "
                + " LEFT JOIN seg_usuario AS uo ON uo.id = v.id_usuario \n "
                + " LEFT JOIN pes_pessoa AS po ON po.id = uo.id_pessoa \n "
                + " LEFT JOIN pes_pessoa AS p ON p.id = v.id_pessoa \n "
                + " LEFT JOIN eve_evento_baile_mapa AS m ON m.id_venda = v.id \n "
                + " LEFT JOIN eve_evento_baile_convite AS c ON c.id_venda = v.id \n "
                + " LEFT JOIN eve_status AS st on st.id=m.id_status or st.id = c.id_status \n "
                + " LEFT JOIN fin_movimento AS mv ON mv.id = m.id_movimento OR mv.id = c.id_movimento \n "
                + " LEFT JOIN fin_baixa AS b ON b.id = mv.id_baixa \n "
                + " LEFT JOIN seg_usuario AS u ON u.id = b.id_usuario \n "
                + " LEFT JOIN pes_pessoa AS pu ON pu.id = u.id_pessoa \n "
                + " LEFT JOIN eve_evento_servico AS ese ON ese.id = v.id_evento_servico \n "
                + "WHERE eb.id = " + id_evento_baile + " \n "
                + "ORDER BY nr_mesa, nr_convite ";
        try {
            Query query = getEntityManager().createNativeQuery(text);
            return query.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
