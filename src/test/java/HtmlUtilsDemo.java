import org.springframework.web.util.HtmlUtils;
import org.w3c.dom.Entity;

/**
 * Programmer   : pancara
 * Date         : 3/23/11
 * Time         : 9:52 AM
 */
public class HtmlUtilsDemo {
    public static void main(String[] args) {
        String filename = "abcd efh.jpg";
        String escaped = HtmlUtils.htmlEscape(filename);

        System.out.println(escaped);
    }
}
