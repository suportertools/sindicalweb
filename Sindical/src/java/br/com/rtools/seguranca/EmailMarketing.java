package br.com.rtools.seguranca;

/**
 * <p>
 * Configurado atualmente para o servidor da WebOmega
 * </p>
 * <p>
 * <a href="http://www.webomega.com.br" target="_blank">www.webomega.com.br</a>
 * </p>
 *
 * @author rtools
 */
public class EmailMarketing {

    private static final String HOSTNAME = "31.133.15.222";
    private static final int PORT = 2525;
    private static final String LOGIN = "fale.sender";
    private static final String PASSWORD = "R2014TLS";
    // {'AUTH'=>'true'}
    private static final boolean AUTH = true;
    // SIS_EMAIL_PROTOCOLO: {'id' => '1', 'ds_descricao'=>'Nenhuma' }
    private static final int PROTOCOL = 1;

    public static String getHOSTNAME() {
        return HOSTNAME;
    }

    public static int getPORT() {
        return PORT;
    }

    public static String getLOGIN() {
        return LOGIN;
    }

    public static String getPASSWORD() {
        return PASSWORD;
    }

    public static boolean isAUTH() {
        return AUTH;
    }

    public static int getPROTOCOL() {
        return PROTOCOL;
    }
}
