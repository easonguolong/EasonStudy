package com.lancang.weimall;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lancang.weimall.common.Keys;
import com.lancang.weimall.common.PayResult;
import com.lancang.weimall.common.SignUtils;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    // 基本地址
    public static String BASE_SERVER = "http://jp.songlai.wang/app/app.php?verify="+encode("56564371abc");
    private EditText mount, note;
    private Button btn_pay;
    private RadioGroup radioGroup;
    private RadioButton rb_wx, rb_ali;
    String pay_id = "";
    private String recharge_amount;
    private String vip_note;
    private String sn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        mount = (EditText) findViewById(R.id.mount);
        note = (EditText) findViewById(R.id.note);
        btn_pay = (Button) findViewById(R.id.toPay);
        radioGroup = (RadioGroup) findViewById(R.id.pay_menth);
        rb_wx = (RadioButton) findViewById(R.id.pay_wx);
        rb_ali = (RadioButton) findViewById(R.id.pay_alib);
        radioGroup.setOnCheckedChangeListener(this);
        btn_pay.setOnClickListener(this);
    }

    public static String encode(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            StringBuffer buf = new StringBuffer("");
            for (int i : b) {
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            String res = buf.toString();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        switch (checkedId) {
            case R.id.pay_wx:
                pay_id = "4";
                break;
            case R.id.pay_alib:
                pay_id = "5";
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toPay:
                recharge_amount = mount.getText().toString().trim();
                vip_note = note.getText().toString().trim();
                toPay(pay_id);
                break;
        }
    }

    private void toPay(String payment) {
        String url = recharge("9709", recharge_amount, vip_note, payment);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configResponseTextCharset("UTF-8");
        httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
        httpUtils.configSoTimeout(5 * 1000);
        httpUtils.configTimeout(5 * 1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String json = arg0.result;
                try {
                    JSONObject js = new JSONObject(json);

                    int error_ = js.getInt("error");
                    if (error_ == 0) {
                        String message = js.getString("message");
                        JSONObject jb = js.getJSONObject("order");
                        sn = jb.get("order_sn").toString();
                        String amount = jb.get("order_amount")
                                .toString();
                        if (rb_wx.isChecked()) {
                            // 微信
                            JSONObject jwx = js.getJSONObject("pay_content").getJSONObject("jsApiParameters");
                            PayReq req = new PayReq();

                            req.appId = jwx.get("appid").toString();// 公众账号ID
                            req.partnerId = jwx.get("partnerid").toString();// 商户号
                            req.prepayId = jwx.get("prepayid").toString(); // 预支付交易会话ID
                            req.packageValue = "Sign=WXPay";// 扩展字段
                            req.nonceStr = jwx.get("noncestr").toString();// 随机字符串
                            req.timeStamp = jwx.get("timestamp").toString();// 时间戳
                            req.sign = jwx.get("sign").toString();// 签名
                            MyApp.api.sendReq(req);
//                            MyApplication.WX_pay_mark =3;
//                            MyApplication.Order_sn=sn;
                        } else {
                            alipay("充值", "充值", amount, sn);
                        }
                    } else {

                    }

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(MainActivity.this, "加载数据失败",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String recharge(String user_id, String amount, String user_note, String payment) {
        return BASE_SERVER + "&op=ucenter"
                + "&act=account_deposit_act&user_id=" + user_id + "&amount="
                + amount + "&user_note=" + user_note + "&payment_id=" + payment;
    }


    private void alipay(String goodsname, String miaoshu, String price, String sn) {
        if (TextUtils.isEmpty(Keys.PARTNER)
                || TextUtils.isEmpty(Keys.RSA_PRIVATE)
                || TextUtils.isEmpty(Keys.SELLER)) {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    finish();
                                }
                            }).show();
            return;
        }
        String orderInfo = getOrderInfo(goodsname, miaoshu, price, sn);
        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(MainActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = Keys.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Keys.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        callback();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(MainActivity.this, "支付结果确认中", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                }
                break;
            }
        }
    };


    private void callback() {
        String url = recharge_zfb_Callback("9000", sn, "9709");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configResponseTextCharset("UTF-8");
        httpUtils.configCurrentHttpCacheExpiry(5 * 1000);
        httpUtils.configSoTimeout(5 * 1000);
        httpUtils.configTimeout(5 * 1000);
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                String json = arg0.result;
                try {
                    JSONObject js = new JSONObject(json);
                    if (js.getInt("error") == 0) {
                        Toast.makeText(MainActivity.this, "充值成功！", Toast.LENGTH_SHORT)
                                .show();
                        finish();
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(MainActivity.this, "加载数据失败",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 我的钱包-支付宝充值回调
    public static String recharge_zfb_Callback(String resultstatus, String sn,
                                               String uid) {
        return BASE_SERVER + "&op=ucenter&act=account_deposit_resultalipay&resultstatus="
                + resultstatus + "&order_sn=" + sn + "&user_id=" + uid;

    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, Keys.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public static String getOrderInfo(String subject, String body,String price, String sn) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Keys.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Keys.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + sn + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        return orderInfo;
    }


}
