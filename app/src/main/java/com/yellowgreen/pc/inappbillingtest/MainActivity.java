package com.yellowgreen.pc.inappbillingtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.TransactionDetails;

public class MainActivity extends Activity implements BillingProcessor.IBillingHandler {
    BillingProcessor bp;
    Button buy;
    String GP_LICENSE_KEY;
    String ProductID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GP_LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtMX0KeCZR2uuHD5W5USBzC9/z153dQ0RX7BQbdFaqsNccoR9C8l4PyyrF61MxhcYO7ElLAZPvrB0DPE5BwEVDBh6dXp62GoDG3wiQ3qX1jwK1rwRa+Oa6ZyH4YcwWU5L/cZbhAF2axuCHTrkrAKct/LSIn6295IIqnGyeFweY90ieAtMuW9wVVlYXmI1jrbqCJtLhuXTky1iNb6cydb5UMznhRKWOFfab6XWqyLl5X+p0saUTDOtZrymLfLMy0QRzlo4wDrsv2QwJ5rcZTUzU0aw2zmCKpY6WVBjEjAP64t4aCR1o2ZJVpQJuSoKvT9vC+GmfGzytqsI8aE7EcT0ewIDAQAB";
        ProductID = "in_app_billing_test";
        bp = new BillingProcessor(this, GP_LICENSE_KEY, this);
        buy = (Button)findViewById(R.id.buy);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bp.isPurchased(ProductID)) {
//                    bp.consumePurchase(ProductID);
                    Toast.makeText(MainActivity.this, "이미 구입하신 상품 입니다.", Toast.LENGTH_SHORT).show();
                }else {
                    bp.purchase(MainActivity.this, ProductID);
                }
            }
        });
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
//        메소드이름이 다 말해주듯...특정 제품 ID를 가진 아이템의 구매 성공시 호출된다.
//        여기에서는 보상을 지급하거나, comsume을 할 필요성이있는 아이템이라면 comsume하는 작업을 하면된다.
        Toast.makeText(this, "결재 성공!!!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPurchaseHistoryRestored() {
//        깃헙 설명에보면 Called when purchase history was restored and the list of all owned PRODUCT ID's was loaded from Google Play
//        라고 되어있는데 언제 쓸런지는 잘 모르겠다.

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
//        구매시 어떤 오류가 발생했을 때 호출된다.
//        구매자가 구매과정에서 그냥 취소해도 발생되는데, 이때의 errorCode는  Constants.BILLING_RESPONSE_RESULT_USER_CANCELED 라고한다.
        if(errorCode == Constants.BILLING_RESPONSE_RESULT_USER_CANCELED)
            Toast.makeText(this, "결재 취소!!!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "결재 실패!!!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBillingInitialized() {
//        BillingProcessor가 초기화되고, 구매 준비가 되면 호출된다.
//        이 부분에서 구매할 아이템들을 리스트로 구성해서 보여주는 코드를 구현하면된다.


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(bp.handleActivityResult(requestCode,resultCode,data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if(bp != null)
            bp.release();

        super.onDestroy();
    }
}
