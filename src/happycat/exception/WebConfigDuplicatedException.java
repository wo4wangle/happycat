package happycat.exception;
//if one path has more than one context configuration will throw this exception
public class WebConfigDuplicatedException extends Exception {
    public WebConfigDuplicatedException(String msg) {
        super(msg);
    }
}