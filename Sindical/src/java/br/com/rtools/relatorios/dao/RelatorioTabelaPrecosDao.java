package br.com.rtools.relatorios.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.RelatorioOrdem;
import br.com.rtools.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class RelatorioTabelaPrecosDao extends DB {

    private Relatorios relatorios;
    private RelatorioOrdem relatorioOrdem;

    /**
     * <p>
     * <ul style="list-style-type: none;">
     * <li><strong>RESULT</strong></li>
     * <li><br /></li>
     * <li>0 - GRUPO -> DESCRIÇÃO;</li>
     * <li>1 - SUBGRUPO -> DESCRIÇÃO;</li>
     * <li>2 - SERVIÇO -> ID;</li>
     * <li>3 - SERVIÇO -> DESCRIÇÃO;</li>
     * <li>4 - CATEGORIA -> ID;</li>
     * <li>5 - CATEGORIA -> DESCRIÇÃO;</li>
     * <li>6 - IDADE;</li>
     * <li>7 - VALOR CHEIO;</li>
     * <li>8 - VALOR TOTAL;</li>
     * <li>9 - PERCENTUAL DESCONTO.</li>
     * </ul>
     * </p>
     *
     * @param inIdServicos Um ou mais serviços, SELECT IN (....)
     * @param inIdSubGrupoCategoria Um ou mais subgrupos, SELECT IN (....)
     * @return
     */
    public List find(String inIdServicos, String inIdSubGrupoCategoria) {
        List listWhere = new ArrayList();
        String queryString = ""
                + "     SELECT G.ds_descricao  AS grupo_descricao,     \n" // 0 - GRUPO -> DESCRIÇÃO
                + "            SG.ds_descricao AS subgrupo_descricao,  \n" // 1 - SUBGRUPO -> DESCRIÇÃO
                + "            S.id            AS servico_id,          \n" // 2 - SERVIÇO -> ID
                + "            S.ds_descricao  AS servico_descricao,   \n" // 3 - SERVIÇO -> DESCRIÇÃO
                + "            CT.id           AS categoria_id,        \n" // 4 - CATEGORIA -> ID
                + "            CT.ds_categoria AS categoria_descricao, \n" // 5 - CATEGORIA -> DESCRIÇÃO
                + "            SV.nr_idade_ini || ' / ' || SV.nr_idade_fim AS idade, \n" // 6 - IDADE
                + "            SV.nr_valor 	AS valor_cheio,        \n" // 7 - VALOR CHEIO
                + "            cast(                                   \n"    
                + "            round(                                  \n"
                + "                  CAST(                             \n"
                + "                          SV.nr_valor - ((SV.nr_valor * DC.nr_desconto) / 100 ) AS numeric \n"
                + "                  ), 2                                                   \n"
                + "             ) AS double precision) AS valor_final,                      \n" // 8 - VALOR TOTAL
                + "             -- DC.nr_desconto  AS  percentual_desconto                  \n"
                + "             0.00 AS percentual_desconto                                 \n" // 9 - PERCENTUAL DESCONTO
                + "       FROM fin_servicos           AS S                                  \n"
                + " INNER JOIN fin_servico_valor      AS SV ON SV.id_servico = S.id         \n"
                + " INNER JOIN fin_subgrupo           AS SG ON SG.id = S.id_subgrupo        \n"
                + " INNER JOIN fin_grupo              AS G  ON G.id = SG.id_grupo           \n"
                + "  LEFT JOIN soc_categoria_desconto AS DC ON DC.id_servico_valor = SV.id  \n"
                + "  LEFT JOIN soc_categoria          AS CT ON CT.id = DC.id_categoria      \n"
                + "      WHERE S.is_tabela          \n"
                + "        AND S.ds_situacao = 'A'  \n"
                + "   ORDER BY G.ds_descricao,      \n"
                + "            SG.ds_descricao,     \n"
                + "            S.ds_descricao,      \n"
                + "            CT.ds_categoria,     \n"
                + "            SV.nr_idade_ini      \n";
        if (inIdServicos != null && !inIdServicos.isEmpty()) {
            listWhere.add(" S.id IN (" + inIdServicos + ") ");
        }
        if (inIdSubGrupoCategoria != null && !inIdSubGrupoCategoria.isEmpty()) {
            listWhere.add(" S.id_subgrupo IN (" + inIdSubGrupoCategoria + ") ");
        }
        if (!listWhere.isEmpty()) {
            queryString += " WHERE ";
            for (int i = 0; i < listWhere.size(); i++) {
                if (i > 0) {
                    queryString += " AND ";
                }
                queryString += listWhere.get(i).toString() + " \n";

            }
        }
        try {
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }

        return new ArrayList<>();
    }

    public Relatorios getRelatorios() {
        return relatorios;
    }

    public void setRelatorios(Relatorios relatorios) {
        this.relatorios = relatorios;
    }

    public RelatorioOrdem getRelatorioOrdem() {
        return relatorioOrdem;
    }

    public void setRelatorioOrdem(RelatorioOrdem relatorioOrdem) {
        this.relatorioOrdem = relatorioOrdem;
    }
}
