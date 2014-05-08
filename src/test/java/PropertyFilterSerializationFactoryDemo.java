import com.integrasolusi.pusda.intranet.json.PropertyFilterSerializerFactory;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Programmer   : pancara
 * Date         : Feb 10, 2011
 * Time         : 12:10:59 PM
 */
public class PropertyFilterSerializationFactoryDemo {
    public static void main(String[] args) throws IOException {
        Bean bean = new Bean("Badu", "Yogyakarta");
        ObjectMapper mapper = new ObjectMapper();
        PropertyFilterSerializerFactory factory = new PropertyFilterSerializerFactory();
        factory.setClassProperties(Bean.class, new String[]{"address"});
        mapper.setSerializerFactory(factory);

        System.out.println("With default serializer: " + mapper.writeValueAsString(bean));

        String json = mapper.viewWriter(String.class).writeValueAsString(bean);
        System.out.println("With custom serializer: " + json);
    }

    public static class Bean {
        private String name;
        private String address;

        public Bean() {
        }

        public Bean(String name, String address) {
            this.setName(name);
            this.setAddress(address);
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
