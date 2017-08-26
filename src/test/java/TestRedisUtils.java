import com.puyixiaowo.fblog.utils.RedisUtils;
import org.junit.Test;

import java.util.Set;

/**
 * @author Moses
 * @date 2017-08-03 13:26
 */
public class TestRedisUtils {

    @Test
    public void testGet(){
        Set<String> keysSet = RedisUtils.keys("REDIS_KEY_MENU_LIST*");
        String [] keys = keysSet.toArray(new String[keysSet.size()]);
        RedisUtils.delete(keys);
    }
}
