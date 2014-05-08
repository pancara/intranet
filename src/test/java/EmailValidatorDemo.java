import org.apache.commons.validator.EmailValidator;

/**
 * Programmer   : pancara
 * Date         : Feb 10, 2011
 * Time         : 6:00:50 PM
 */
public class EmailValidatorDemo {
    public static void main(String[] args) {
        String[] emails = {"badu.gmail.com", "badu@mail.@gmail.com"};
        for (String email : emails)
            validateEmail(email);
    }

    public static void validateEmail(String email) {
        if (EmailValidator.getInstance().isValid(email))
            System.out.println(email + " is valid");
        else
            System.out.println(email + " is not valid");

    }
}
