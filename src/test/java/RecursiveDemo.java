/**
 * Programmer   : pancara
 * Date         : Feb 15, 2011
 * Time         : 5:37:02 PM
 */
public class RecursiveDemo {

    public static int permutate(int x) {
        if (x < 2) return 1;
        else {
            int y = x - 1;
            return x * permutate(y);
        }
    }

    public static void main(String[] args) {
        int result = permutate(10);
        System.out.println("result = " + result);
    }
}
