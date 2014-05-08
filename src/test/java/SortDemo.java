import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Programmer   : pancara
 * Date         : Feb 15, 2011
 * Time         : 5:34:13 PM
 */
public class SortDemo {
    public static void main(String[] args ){
        List<Integer> numbers = new LinkedList<Integer>();
        Random random = new Random();
        for(int i = 0; i < 10; i++)
            numbers.add(random.nextInt());

        for(Integer number  : numbers)
            System.out.println("number = " + number);

        Collections.sort(numbers);

        System.out.println("sorted...");
        for(Integer number : numbers)
            System.out.println("number = " + number);
    }
}
