package br.com.rtools.utilitarios;

import java.lang.annotation.Annotation;
import javax.persistence.Table;

public class Tables {

    public static String name(Object o) {
        String tableName = "";
        Class c = (Class) o.getClass();
        String className = c.getSimpleName();
        if (!c.getSimpleName().equals("String")) {
            for (Annotation ann : c.getAnnotations()) {
                if (!ann.annotationType().equals(Table.class)) {
                    continue;
                }

                Table t = (Table) ann;
                tableName = t.name();
            }
            className = c.getSimpleName();
        } else {
            tableName = o.toString();
        }
        return tableName;
    }
}
