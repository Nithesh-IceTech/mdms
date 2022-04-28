package za.co.spsi.toolkit.ee.util;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class BeanUtil {

    public static List<String> getAvailableDataSources(String rootName) {
        try {
            Context root = (Context) new InitialContext().lookup(rootName);
            NamingEnumeration<NameClassPair> names = root.list("");
            List<String> jndi = new ArrayList<>();
            while (names.hasMore()) {
                jndi.add(rootName+(names.next().getName()));
            }
            return jndi;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getAvailableDataSources() {
        return getAvailableDataSources("java:/jdbc/");
    }

    public static DataSource getDataSource(String jndiName) {
        try {
            InitialContext ic= new InitialContext();
            return (DataSource) ic.lookup(jndiName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getBean(final BeanManager manager, final Class<T> type) {
        T result = null;
        Bean<T> bean = (Bean<T>) manager.resolve(manager.getBeans(type));
        if (bean != null) {
            CreationalContext<T> context = manager.createCreationalContext(bean);
            if (context != null) {
                result = (T) manager.getReference(bean, type, context);
            }
        }
        return result;
    }

    public static <T> T getNewBean(final BeanManager manager, final Class<T> type) {
        T result = null;
        Bean<T> bean = (Bean<T>) manager.resolve(manager.getBeans(type));
        if (bean != null) {
            CreationalContext<T> context = manager.createCreationalContext(bean);
            if (context != null) {
                result = (T) manager.getReference(bean, type, context);
            }
        }
        return result;
    }

    public static <T> List<T> getBeans(final BeanManager manager, final Class<T> type,Annotation annotations) {
        List<T> beans = new ArrayList<>();
        for (Bean bean : manager.getBeans(type, annotations)) {
            CreationalContext<T> context = manager.createCreationalContext(bean);
            if (context != null) {
                T beanInstance = (T) manager.getReference(bean, type, context);
                if (beanInstance != null) {
                    beans.add(beanInstance);
                }
            }
        }
        return beans;
    }

    public static <T> List<T> getBeans(final BeanManager manager, final Class<T> type) {
        return getBeans(manager,type,new AnnotationLiteral<Any>() {});
    }

    public static <T> List<T> getSubClassBeans(final BeanManager manager, final Class<T> type) {
        return getBeans(manager,type,new AnnotationLiteral<Any>() {});
    }

    public static <T> List<T> getInstancesWithSignature(List<T> beans, Class<? extends Annotation> aClass, String method, Object methodResult) {
        try {
            List<T> values = new ArrayList<T>();
            for (T value : beans) {
                Annotation a = value.getClass().getAnnotation(aClass);
                if (za.co.spsi.toolkit.util.Util.isAnnotationSetWithMethodSignature(a, method, methodResult)) {
                    values.add(value);
                }
            }
            return values;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T getInstanceWithSignature(List<T> beans, Class<? extends Annotation> aClass, String method, Object methodResult) {
        List<T> values = getInstancesWithSignature(beans, aClass, method, methodResult);
        return values.isEmpty() ? null : values.get(0);
    }
}
