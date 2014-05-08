import org.springframework.util.AntPathMatcher;

/**
 * Programmer   : pancara
 * Date         : Feb 18, 2011
 * Time         : 9:59:14 AM
 */
public class AntPathMatcherDemo {
    public static void main(String[] args) {
        AntPathMatcher matcher = new AntPathMatcher();
        boolean match = matcher.match("/intranet/**", "/intranet/config/set.html");
        System.out.println("match = " + match);

    }
}
