package com.example.samuraitravel.controller;
//民泊一覧ページ(クライアント用)を担当するコントローラー

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.repository.HouseRepository;

@Controller
@RequestMapping("/houses")
public class HouseController {

	private final HouseRepository houseRepository;

	public HouseController(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;
	}

	@GetMapping
	public String index(
//			検索処理
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "area", required = false) String area,
			@RequestParam(name = "price", required = false) Integer price,
			@RequestParam(name = "order", required = false) String order,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		Page<House> housePage;

		if (keyword != null && !keyword.isEmpty()) {
			//		キーワード検索時の処理
			housePage = houseRepository.findByNameLikeOrAddressLike("%" + keyword + "%", "%" + keyword + "%", pageable);
			//keyword検索で値が格納されているケースに呼び出す
			if (order != null && order.equals("priceAsc")) {
//				もしキーワードに加えて、オーダーの値も引数で受け取った場合、下記のメソッドを呼び出してhousePageの内容を上書きする。
				housePage = houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%",
						"%" + keyword + "%", pageable);
			} else {
				housePage = houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%",

						"%" + keyword + "%", pageable);
			}

		} else if (area != null && !area.isEmpty()) {
			//		エリア検索時の処理
			housePage = houseRepository.findByAddressLike("%" + area + "%", pageable);
			if (order != null && order.equals("priceAsc")) {
				//リクエストパラメーターorderで値priceAscを受け取った場合安い順に並べ替えるメソッドを呼び出す
				housePage = houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
			} else {
				housePage = houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
			}
		} else if (price != null) {
			//		料金検索時の処理
			housePage = houseRepository.findByPriceLessThanEqual(price, pageable);
			if (order != null && order.equals("priceAsc")) {
				housePage = houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
			} else {
				housePage = houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
			}

		} else {
			housePage = houseRepository.findAll(pageable);
			if(order !=null && order.equals("priceAsc")) {
				housePage = houseRepository.findAllByOrderByPriceAsc(pageable);
			}else {
				housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);
			}
		}
			//	得に検索処理がない場合に全体表示

			model.addAttribute("housePage", housePage);
			model.addAttribute("keyword", keyword);
			//	入力されたキーわーどを検索後も表示できる様にモデルに格納
			model.addAttribute("area", area);
			model.addAttribute("price", price);
			model.addAttribute("price",price);
			model.addAttribute("order",order);


		return "houses/index";

	}

	@GetMapping("/{id}")
//	民泊詳細ページへ
	public String show(@PathVariable(name="id")Integer id, Model model) {
		House house = houseRepository.getReferenceById(id);

		model.addAttribute("house",house);
		model.addAttribute("reservationInputForm", new ReservationInputForm());


	return "houses/show";
	}


}
