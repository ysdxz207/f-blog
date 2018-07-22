package win.hupubao.test;

import org.jsoup.Connection;
import win.hupubao.common.http.Page;

public class Test {

    @org.junit.Test
    public void test() {
        String str = "<xml><appid><![CDATA[wx2a8f9411d194d079]]></appid><attach><![CDATA[GKNMEBCWMHNKTXX]]></attach><bank_type><![CDATA[SCNX_DEBIT]]></bank_type><cash_fee><![CDATA[6950]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1245231202]]></mch_id><nonce_str><![CDATA[1532137179054]]></nonce_str><openid><![CDATA[oBaAGs1EkhUqHhl2zamLVRsLJAwo]]></openid><out_trade_no><![CDATA[18072109393901268000297]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[FAE540F34EB33CC43AF1E6A00C38DA98]]></sign><sub_appid><![CDATA[wx8131c80d20713d8b]]></sub_appid><sub_is_subscribe><![CDATA[N]]></sub_is_subscribe><sub_mch_id><![CDATA[1420605002]]></sub_mch_id><sub_openid><![CDATA[o9_yiwSUeA5nhFULFyah74aPeLrU]]></sub_openid><time_end><![CDATA[20180721093955]]></time_end><total_fee>6950</total_fee><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[4200000133201807215987733240]]></transaction_id></xml>";
        Page.Response res =  Page.create().request("http://localhost:2337/port/pay/wxnotify",
                str, Connection.Method.POST);

        System.out.println(res);
    }
}
