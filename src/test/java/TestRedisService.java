import com.puyixiaowo.fblog.utils.JedisUtils;
import org.junit.Test;

/**
 * @author Moses
 * @date 2017-08-03 13:26
 */
public class TestRedisService {

    @Test
    public void testGet(){
        System.out.println(JedisUtils.get("aa", Integer.class) == 123);
    }
}
