import com.integrasolusi.pusda.intranet.persistence.Person;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * Programmer   : pancara
 * Date         : Feb 10, 2011
 * Time         : 10:12:01 AM
 */
public class JsonJacksonDemo {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Writer writer = new OutputStreamWriter(System.out);

        List<Person> persons = new LinkedList<Person>();
        Person person = new Person();
        person.setName("Wicak");
        person.setAddress("Yogyakarta");
        person.setEmail("wicaksana@gmail.com");
        persons.add(person);

        persons.add(new Person());
        writer = new OutputStreamWriter(System.out);
        mapper.writeValue(writer, persons);

    }
}
