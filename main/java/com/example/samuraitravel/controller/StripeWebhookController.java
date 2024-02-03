package com.example.samuraitravel.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.samuraitravel.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

@Controller
public class StripeWebhookController {
	private final StripeService stripeService;


	@Value("${stripe.api-key}")
	private String stripeApiKey;


	@Value("${stripe.webhook-secret}")
	private String webhookSecret;


	public StripeWebhookController(StripeService stripeService) {
		this.stripeService=stripeService;

}

	@PostMapping("/stripe/webhook")
	public ResponseEntity<String> webhook(@RequestBody String payload,
										  @RequestHeader("Stripe-Signature") String sigHeader){
		//webhookイベント通知先(ターミナルで打ち込んだstripe listen--forward to localhost:8080/stripe/webhookと、ここの@postmapping
//		の引数("/stripe/webhook")を一致させる必要がある(本番環境の場合はhttp://ドメイン名/stripe/webhook
		Stripe.apiKey = stripeApiKey;

		Event event = null;


		try {
			event = Webhook.constructEvent(payload,sigHeader,webhookSecret);

		} catch (SignatureVerificationException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		if("checkout.session.completed".equals(event.getType())) {
			//checkoutsessioncompleteがeventgettypeと等しいかどうかでで決済が成功したかどうを確認
			stripeService.processSessionCompleted(event);
			//等しい（決済成功)の場合はprocesssessionocmpleteを呼び出す。
		}

			return new ResponseEntity<>("Scucess", HttpStatus.OK);
//			ページのリダイレクトはstrple側が行う
		}

	}
