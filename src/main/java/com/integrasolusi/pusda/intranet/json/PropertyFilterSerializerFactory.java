package com.integrasolusi.pusda.intranet.json;

import org.apache.commons.lang.ArrayUtils;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.BeanSerializerBuilder;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;

import java.util.HashMap;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 10, 2011
 * Time         : 12:02:29 PM
 */
public class PropertyFilterSerializerFactory extends CustomSerializerFactory {
    private java.util.Map<Class, String[]> serializedProperties = new HashMap<Class, String[]>();

    public PropertyFilterSerializerFactory() {
    }

    public PropertyFilterSerializerFactory(Class clazz, String[] properties) {
        this();
        setClassProperties(clazz, properties);
    }

    @Override
    protected void processViews(SerializationConfig config, BeanSerializerBuilder builder) {
        super.processViews(config, builder);
        BasicBeanDescription beanDesc = builder.getBeanDescription();
        String[] properties = serializedProperties.get(beanDesc.getBeanClass());

        if (properties != null) {
            List<BeanPropertyWriter> props = builder.getProperties();
            BeanPropertyWriter[] writers = props.toArray(new BeanPropertyWriter[props.size()]);
            for (int i = 0; i < writers.length; ++i) {
                String propertyName = writers[i].getName();
                if (!ArrayUtils.contains(properties, propertyName)) {
                    writers[i] = null;
                }
            }
            builder.setFilteredProperties(writers);
        }
    }

    public void setClassProperties(Class clazz, String[] properties) {
        serializedProperties.put(clazz, properties);
    }

    public void removeClassProperties(Class clazz) {
        serializedProperties.remove(clazz);
    }

}
