/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.rtools.utilitarios;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author rtools2
 */
public class T {

    public static <T> T map(Class<T> type, Object[] tuple) {
        List<Class<?>> tupleTypes = new ArrayList<>();
        for (Object field : tuple) {
            tupleTypes.add(field.getClass());
        }
        try {
            Constructor<T> ctor = type.getConstructor(tupleTypes.toArray(new Class<?>[tuple.length]));
            return ctor.newInstance(tuple);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> map(Class<T> type, List<Object[]> records) {
        List<T> result = new LinkedList<>();
        for (Object[] record : records) {
            result.add(map(type, record));
        }
        return result;
    }

    public static <T> List<T> getResultList(Query query, Class<T> type) {
        @SuppressWarnings("unchecked")
        List<Object[]> records = query.getResultList();
        return map(type, records);
    }
}
