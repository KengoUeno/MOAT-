package com.example.samuraitravel.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.form.ReservationRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReservationRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReservationService;
import com.example.samuraitravel.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReservationController {

	private final ReservationRepository reservationRepository;
	private final HouseRepository houseRepository;
	private final ReservationService reservationService;
	private final StripeService stripeService;

	public ReservationController(ReservationRepository reservationRepository, HouseRepository houseRepository,
			ReservationService reservationService, StripeService stripeService) {
		this.reservationRepository = reservationRepository;
		this.houseRepository = houseRepository;
		this.reservationService = reservationService;
		this.stripeService = stripeService;

	}

	@GetMapping("/reservations")
	//	予約一覧ページ
	public String index(Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			//現在ログイン中のユーザーのuserDetailsIMplオブジェクトとして取得する
			@PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {

		User user = userDetailsImpl.getUser();

		Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);

		model.addAttribute("reservationPage", reservationPage);

		return "/reservations/index";
	}

	@GetMapping("/houses/{id}/reservations/input")
	//	人数が定員以内か調べる処理
	public String input(@PathVariable(name = "id") Integer id,
			@ModelAttribute @Validated ReservationInputForm reservationInputForm,
			BindingResult bindingResult, RedirectAttributes redirectAttribute, Model model) {
		//id=民宿id, reservationinputform=チェックインからチェックアウトまでと宿泊人数
		House house = houseRepository.getReferenceById(id);

		Integer numberOfPeople = reservationInputForm.getNumberOfPeople();

		Integer capacity = house.getCapacity();

		if (numberOfPeople != null) {
			if (!reservationService.isWithinCapacity(numberOfPeople, capacity)) {
				FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numberOfPeople",
						"宿泊人数が定員を超えています");
				bindingResult.addError(fieldError);

			}
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			model.addAttribute("errorMessage", "予約内容に不備があります");
			return "houses/show";

		}
		redirectAttribute.addFlashAttribute("reservationInputForm", reservationInputForm);

		return "redirect:/houses/{id}/reservations/confirm";
		//		次は下のメソッドへ

	}




	@GetMapping("/houses/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
                          @ModelAttribute ReservationInputForm reservationInputForm,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                          HttpServletRequest httpServletRequest,
                          Model model)
    {
        House house = houseRepository.getReferenceById(id);
        User user = userDetailsImpl.getUser();

        //チェックイン日とチェックアウト日を取得する
        LocalDate checkinDate = reservationInputForm.getCheckinDate();
        LocalDate checkoutDate = reservationInputForm.getCheckoutDate();

        // 宿泊料金を計算する
        Integer price = house.getPrice();
        Integer amount = reservationService.calculateAmount(checkinDate, checkoutDate, price);

        ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(house.getId(), user.getId(), checkinDate.toString(), checkoutDate.toString(), reservationInputForm.getNumberOfPeople(), amount);

        String sessionId = stripeService.createStripeSession(house.getName(), reservationRegisterForm, httpServletRequest);

        model.addAttribute("house", house);
        model.addAttribute("reservationRegisterForm", reservationRegisterForm);
        model.addAttribute("sessionId", sessionId);

        return "reservations/confirm";
    }





//	@PostMapping("/houses/{id}/reservations/create")
//	confirmから送られてきた予約情報をdbに格納する処理（サービスクラスのcreateメソッド)後にブラウザにメッセージを表示する処理
//	public String create(
//			@ModelAttribute ReservationRegisterForm reservationRegisterForm, RedirectAttributes redirectAttributes) {
//
//		reservationService.create(reservationRegisterForm);
//
//
//		return "redirect:/reservations?reserved";
//		reservation/index.htmlへリダイレクト時に、param.reservedにメッセージを表示させる
//
//
//	}

}
