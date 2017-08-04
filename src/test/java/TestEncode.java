import com.puyixiaowo.fblog.utils.Md5Utils;

import java.io.UnsupportedEncodingException;

/**
 * @author Moses
 * @date 2017-08-04 16:50
 */
public class TestEncode {
    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(Md5Utils.md5("body=百事可乐300毫升&fee_type=1&input_charset=UTF-8&notify_url=http://lamic.cn&out_trade_no=44168776551467264356&partner=12038387&return_url=http://lamic.cn&service_version=2.14&service=pay_service&show_url=http://lamic.cn&spbill_create_ip=127.0.0.1&subject=百事可乐300毫升&total_fee=2.5&trans_channel=alipay_qr_p&trans_pattern=0f7d16bef35c740c4884ffc4b9a89ab91"));
    }
}
