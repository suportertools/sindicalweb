//package br.com.rtools.utilitarios;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
//public class Conversor {
//
//    private Object object;
//
//    public Conversor(Object object) {
//        this.object = object;
//    }
//
//    public Date getDate() {
//        Date data = null;
//        if (object != null) {
//            if (object.getClass().getName().equals("java.sql.Date")) {
//                java.sql.Date dataSql = (java.sql.Date) object;
//                data = DataHoje.converteDateSql(dataSql);
//            } else {
//                data = (Date) object;
//            }
//        }
//
//        return data;
//    }
//
//    public Object getObject() {
//        return object;
//    }
//
//    public String getString() {
//        String objeto = "";
//        try {
//            if (object != null) {
//                objeto = (String) object;
//            }
//        } catch (Exception e) {
//            objeto = "";
//        }
//        return objeto;
//    }
//
//    public Integer getInteger() {
//        Integer objeto = 0;
//        try {
//            if (object != null) {
//                objeto = (Integer) object;
//            }
//        } catch (Exception e) {
//            objeto = 0;
//        }
//        return objeto;
//    }
//
//    public Float getFloat() {
//        Float objeto = new Float(0);
//        try {
//            if (object != null) {
//                objeto = (Float) object;
//            }
//        } catch (Exception e) {
//            objeto = new Float(0);
//        }
//        return objeto;
//    }
//
//    public Double getDouble() {
//        Double objeto = new Double(0);
//        try {
//            if (object != null) {
//                objeto = (Double) object;
//            }
//        } catch (Exception e) {
//            objeto = new Double(0);
//        }
//        return objeto;
//    }
//
//    public Long getLong() {
//        Long objeto = new Long(0);
//        try {
//            if (object != null) {
//                objeto = (Long) object;
//            }
//        } catch (Exception e) {
//            objeto = new Long(0);
//        }
//        return objeto;
//    }
//
//    public void setObject(Object object) {
//        this.object = object;
//    }
//}
