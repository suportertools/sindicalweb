
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestMemoria {

    public static void main(String[] args) {
        int numeroDeAlocacoes = 0;
        try {
            System.out.println("Alocando...");
            List list = new ArrayList();
            String grandeString = new BigDecimal(1000).pow(1000).toString();
            for (int i = 0; i < 1000000; i++) {
                list.add((grandeString + numeroDeAlocacoes++).intern());
            }
            System.out.println("Fim.");
        } catch (OutOfMemoryError e) {
            System.out.println(e);
            System.out.println("Alocacoes antes do erro: " + numeroDeAlocacoes);
        }
    }
}
