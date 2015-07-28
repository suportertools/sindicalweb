package br.com.rtools.utilitarios;

public class QueryString {

    /**
     *
     * @param field
     * @param type - 0 = Total; 1 = Inicial; 2 = Parcial; 3 Filnal;
     * @return
     */
    public static String typeSearch(String field, int type) {
        switch (type) {
            case 0:
                field = " = TRIM(UPPER(FUNC_TRANSLATE('" + field + "')))";
                break;
            case 1:
                field = " LIKE TRIM(UPPER(FUNC_TRANSLATE('" + field + "%')))";
                break;
            case 2:
                field = " LIKE TRIM(UPPER(FUNC_TRANSLATE('%" + field + "%')))";
                break;
            case 3:
                field = " LIKE TRIM(UPPER(FUNC_TRANSLATE('%" + field + "')))";
                break;
        }
        return field;
    }

}
