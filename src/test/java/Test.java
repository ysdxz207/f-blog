import static spark.Spark.*;

/**
 * @author Moses
 * @date 2017-08-01 18:18
 */
public class Test {

    public static void main(String[] args) {
        port(2333);
        get("/hello", (request, response) -> "Hello World!");
    }
}
