import org.apache.commons.lang.BooleanUtils;

/**
 * Programmer   : pancara
 * Date         : Jan 21, 2011
 * Time         : 7:53:39 AM
 */
public class BooleanUtilsDemo {
    public static void main(String[] args){
        Boolean oValue = new Boolean(true);
        boolean value = BooleanUtils.toBoolean(oValue);
        System.out.println("value = " + value);
    }
}
