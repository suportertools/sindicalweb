package br.com.rtools.utilitarios;

import br.com.rtools.principal.DB;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Column;
import javax.persistence.Query;

public class SelectTranslate extends DB {
    private String select_class_name;
    private String select_text;
    private Class classe; 
    
    public select select(Object object){
        return new select(object);
    }

    public class select {
        public select(Object object) {
            
            classe = (Class) object.getClass(); 
            String table_name = "";
            select_class_name = classe.getSimpleName();
            if (!classe.getSimpleName().equals("String")){
                for (Annotation ann : classe.getAnnotations()) {
                    if(!ann.annotationType().equals(javax.persistence.Table.class)) continue;

                    javax.persistence.Table t = (javax.persistence.Table) ann;
                    table_name  = t.name();
                }     
                select_class_name = classe.getSimpleName();
            }else{
                table_name = object.toString();
            }
            
            select_text = "SELECT * FROM " + table_name;
        }
        
        public List find() {
            Query qry = getEntityManager().createNativeQuery(select_text);
            
            List<Vector> result_list = qry.getResultList();
            List<Object> return_list = new ArrayList<Object>();

            for (Vector list : result_list){
                return_list.add(getEntityManager().createQuery("SELECT ob FROM "+select_class_name+" ob WHERE ob.id = "+(Integer)list.get(0)).getSingleResult());
            }
            return return_list;
        }
        
//        public innerjoin innerjoin(List<Object> innerlist){ // INCONPLETO POR CONTA DE TER MAIS DE UM INNER JOIN
//            return new innerjoin(innerlist);
//        }
        
        public where where(String field, String value){
            return new where(field, value);
        }
    }
    
//    public class innerjoin{
//        private String inner_text = "";
//        
//        public innerjoin(List<Object> innerlist){
//            for(Object inner : innerlist){
//                
//                Class inner_class = (Class) inner.getClass(); 
//                String inner_table_name = "";
//                
//                for (Annotation ann : classe.getAnnotations()) {
//                    if(!ann.annotationType().equals(javax.persistence.Table.class)) continue;
//
//                    javax.persistence.Table t = (javax.persistence.Table) ann;
//                    inner_table_name  = t.name();
//                }     
//                select_class_name = classe.getSimpleName();
//                
//                inner_text += " INNER JOIN "+ inner_table_name; // INCONPLETO POR CONTA DE TER MAIS DE UM INNER JOIN
//            }
//        }
//        
//        public where where(String field, String value){
//            return new where(field, value);
//        }        
//    }
    
    public class where{
        private String where_text = "";

        public where(String field, String value){
            for (Field atributo : classe.getDeclaredFields()) {
                for (Annotation ann : atributo.getAnnotations()) {
                    if(ann.annotationType().equals(javax.persistence.Column.class)) {
                        Column c = (Column) ann;     
                        if (field.equals(atributo.getName()))
                            field = c.name();
                        //String sxc = "get" + atributo.getName().substring(0, 1).toUpperCase() + atributo.getName().substring(1);
                    }
                }              
            }            
            
            value = Normalizer.normalize(value, Normalizer.Form.NFD);  
            value = value.toLowerCase().replaceAll("[^\\p{ASCII}]", "");
            
            where_text = " WHERE LOWER(TRANSLATE(" + field + ")) LIKE '" + value + "' ORDER BY " + field;
        }
        
        public List find() {
            Query qry = getEntityManager().createNativeQuery(select_text + where_text);
            
            List<Vector> result_list = qry.getResultList();
            List<Object> return_list = new ArrayList<Object>();

            for (Vector list : result_list){
                return_list.add(getEntityManager().createQuery("SELECT ob FROM "+select_class_name+" ob WHERE ob.id = "+(Integer)list.get(0)).getSingleResult());
            }
            return return_list;
        }
    }
    
    
    
}





















//            String s = "";
//            s += "\n";
//            ArrayList<String> met = new ArrayList();
//            for (Field atributo : classe.getDeclaredFields()) {
//                for (Annotation ann : atributo.getAnnotations()) {
//                    if(ann.annotationType().equals(javax.persistence.Column.class)) {
//                        Column c = (Column) ann;                        
//                        s += c.toString() + "\n";
//                        s += atributo.getName() + "\n";
//                        met.add("get" + atributo.getName().substring(0, 1).toUpperCase() + atributo.getName().substring(1));
//                    }
//                }              
//            }
//            s += "\n";
//            for (Method m : classe.getMethods()) {
//                if(!met.contains(m.getName())) continue;
//                s += m.getName() + "\n";
//                try {
//                    s += m.invoke(object, null) + "\n";
//                } catch (Exception ex) {
//                    s += "faio\n";
//                }
//            }