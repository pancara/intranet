package com.integrasolusi.pusda.intranet.utils;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.WrapDynaClass;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

/**
 * Programmer   : pancara
 * Date         : Jan 7, 2011
 * Time         : 7:44:54 PM
 */
public class PropertyHelper {
    private static Logger logger = Logger.getLogger(PropertyHelper.class);
    private static BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();

    /**
     * Get property type from a class. Nested property is supported.
     *
     * @param clazz
     * @param property
     * @return
     */
    public static Class getPropertyClass(Class clazz, String property) {
        String[] arrProperty = StringUtils.split(property, ".");

        for (int i = 0; i < arrProperty.length; i++)
            clazz = WrapDynaClass.createDynaClass(clazz).getDynaProperty(arrProperty[i]).getType();
        return clazz;
    }

    /**
     * Get a property value from an object. Nested property is supported.
     *
     * @param bean         source object.
     * @param propertyName property name.
     * @return Object property value.
     */
    public static Object getPropertyValue(Object bean, String propertyName) {
        try {
            return beanUtils.getPropertyUtils().getProperty(bean, propertyName);
        } catch (Exception e) {
            logger.debug(e);
        }
        return null;
    }

    /**
     * Set property value of object.
     *
     * @param object       target object.
     * @param propertyName property name.
     * @param value        property value.
     */
    public static void setPropertyValue(Object object, String propertyName, Object value) {
        try {
            beanUtils.setProperty(object, propertyName, value);
        } catch (Exception e) {
            logger.debug(e);
        }
    }

    /**
     * Copy property value from an object.
     *
     * @param src
     * @param srcProperty
     * @param target
     * @param targetProperty
     */
    public static void copyPropertyValue(Object src, String srcProperty, Object target, String targetProperty) {
        Object value = getPropertyValue(src, srcProperty);
        try {
            org.apache.commons.beanutils.BeanUtils.setProperty(target, targetProperty, value);
        } catch (Exception e) {
            logger.debug(e);
        }
    }


    public static void copyProperties(Object source, Object target) {
        try {
            beanUtils.getPropertyUtils().copyProperties(target, source);
        } catch (IllegalAccessException e) {
            logger.debug(e);
        } catch (InvocationTargetException e) {
            logger.debug(e);
        } catch (NoSuchMethodException e) {
            logger.debug(e);
        }
    }

    public static Object cloneBean(Object object) {
        try {
            return beanUtils.cloneBean(object);
        } catch (Exception ex) {
            logger.debug(ex);
            throw new RuntimeException(ex);
        }
    }
}

