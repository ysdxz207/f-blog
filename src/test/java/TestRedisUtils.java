import com.puyixiaowo.fblog.utils.RedisUtils;
import org.junit.Test;

/**
 * @author Moses
 * @date 2017-08-03 13:26
 */
public class TestRedisUtils {

    @Test
    public void testGet(){
        System.out.println(RedisUtils.get("aa", Integer.class) == 123);
    }
}
