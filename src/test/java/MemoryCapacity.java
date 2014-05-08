import java.util.LinkedList;

/**
 * Programmer   : pancara
 * Date         : 3/22/11
 * Time         : 12:44 PM
 */
public class MemoryCapacity {
    public static void main(String[] argS) {
        java.util.List<Long> ids = new LinkedList<Long>();
        System.out.println("Runtime.getRuntime().freeMemory() = " + Runtime.getRuntime().freeMemory());
        System.out.println("Runtime.getRuntime().totalMemory() = " + Runtime.getRuntime().totalMemory());
        System.out.println("Runtime.getRuntime().maxMemory() = " + Runtime.getRuntime().maxMemory());


        for(Long i = 0L; i < 5000000L; i++) {
            ids.add(i);
        }
        System.out.println("Runtime.getRuntime().freeMemory() = " + Runtime.getRuntime().freeMemory());
        System.out.println("Runtime.getRuntime().totalMemory() = " + Runtime.getRuntime().totalMemory());
        System.out.println("Runtime.getRuntime().maxMemory() = " + Runtime.getRuntime().maxMemory());
    }
}
