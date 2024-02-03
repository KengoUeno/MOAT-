package com.example.samuraitravel.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.form.ReservationRegisterForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

import jakarta.servlet.http.HttpServletRequest;



@Service
public class StripeService {
    @Value("${stripe.api-key}")
    private String stripeApiKey;

    private final ReservationService reservationService;

    public StripeService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // セッションを作成し、Stripeに必要な情報を返す
    public String createStripeSession(String houseName, ReservationRegisterForm reservationRegisterForm, HttpServletRequest httpServletRequest) {
        Stripe.apiKey = stripeApiKey;
        String requestUrl = new String(httpServletRequest.getRequestURL());


        SessionCreateParams params =
            SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(houseName)
                                        .build())
                                .setUnitAmount((long)reservationRegisterForm.getAmount())
                                .setCurrency("jpy")
                                .build())
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(requestUrl.replaceAll("/houses/[0-9]+/reservations/confirm", "") + "/reservations?reserved")

                .setCancelUrl(requestUrl.replace("/reservations/confirm", ""))
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("houseId", reservationRegisterForm.getHouseId().toString())
                        .putMetadata("userId", reservationRegisterForm.getUserId().toString())
                        .putMetadata("checkinDate", reservationRegisterForm.getCheckinDate())
                        .putMetadata("checkoutDate", reservationRegisterForm.getCheckoutDate())
                        .putMetadata("numberOfPeople", reservationRegisterForm.getNumberOfPeople().toString())
                        .putMetadata("amount", reservationRegisterForm.getAmount().toString())
                        .build())
                .build();
        try {
            Session session = Session.create(params);
            return session.getId();
        } catch (StripeException e) {
            e.printStackTrace();

            return "";
        }
    }


    //セッションから予約情報を取得し、reservationserviceクラスを介してデータベースに登録する処理
    public void processSessionCompleted(Event event) {
    	Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
    	//引数で受け取ったEventからStripeObjectを取得。
//    	event = stripeから通知されるイベント内容を表現
//    	stripe object = stripe のapiから返されるデータを表現する基本的なオブジェクト

//    	optional- nullが入る可能性のあるオブジェクト
//    	-nullチェックが簡単でコードがシンプル（optionalを使わない場合、if分を用いたnullチェックが必要になるので、コードが長くなる
//    	-nullで発生するエラーを軽減

    	optionalStripeObject.ifPresent(stripeObject -> {
    		Session session = (Session)stripeObject;
    		SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("payment_intent").build();

    		try {
    			session = Session.retrieve(session.getId(),params,null);

    			Map<String,String>paymentIntentObject = session.getPaymentIntentObject().getMetadata();

    			reservationService.create(paymentIntentObject);
    		} catch (StripeException e) {
    			e.printStackTrace();
    		}

    	});
    }

}
