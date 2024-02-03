package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
//エンティティのhouse.javaからdbのカラムを指定しているフィールドが提供される。
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.service.HouseService;
//housrepositoryからCRUDメソッドが提供される

@Controller
@RequestMapping("/admin/houses")
public class AdminHouseController {
	//管理者用の各種民泊ページを制御するためのコントローラー

	private final HouseRepository houseRepository;
	//	importしたHOuserepositoryから定数housserepositoryを定義する
	private final HouseService houseService;

	public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
		this.houseRepository = houseRepository;
		//		戻り値の記載がないからコンストラクタだけどセッターみたいな機能したるな
		this.houseService = houseService;
	}

	@GetMapping

	public String index(Model model,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			@RequestParam(name = "keyword", required = false) String keyword) {
		//		keywordは取得するリクエストパラメーター名で、htmlのフォームに入力された値を受け取る。
		//		required true=値必須、ないとエラー、false=値なくてもOK

		Page<House> housePage;
		//		pageインターフェースはページネーションされたListに加えて、現在のページデータ、ページ番号、要素の総数、総ページ数、最初/最後のページかどうかの情報を提供する。
		//Houseクラスを実装したHouseインスタンス
		if (keyword != null && !keyword.isEmpty()) {
			housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable);
			//keyworがある場合はそれと一致するものをfindbynamelikeで探して値をhomepageに格納
		} else {
			housePage = houseRepository.findAll(pageable);
			//			findAllで全権取得するが、pageabeインターフェースはページネーションに必要なページ番号、１ページの表示数、並べ替えの条件の情報を提供する、雛形のイメージ。
			//			keywordが入力されていない場合は通常通りの全権取得/
		}

		model.addAttribute("housePage", housePage);
		model.addAttribute("keyword", keyword);

		return "admin/houses/index";

	}

	//	民泊詳細を表示するメソッド

	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id") Integer id, Model model) {
		//		@pathvariable= URLの一部を引数をして割り当てる(baind）できる。URLの一部を変数扱いにできる。

		House house = houseRepository.getReferenceById(id);
		//		Dbからidを元にデータを取得、houseに格納
		model.addAttribute("house", house);

		return "admin/houses/show";
	}

	@GetMapping("/register")

	//	民宿登録ページ
	public String register(Model model) {

		model.addAttribute("houseRegisterForm", new HouseRegisterForm());
		//		HouseRegisterFormクラスから、インスタンスhouseRegisterFormを作ってモデルく格納。
		//		イメージ、解答が未記入状態の問題容姿がhouseregisterformで、解答はクライアントが記入する。
		//		つまりHouseRegisterFormクラスは問題容姿の原本でインスタンスはそのコピー。コピーを作ったら、modelという名前の「机」に置いて、それをクライアントが見て解く。

		return "/admin/houses/register";
	}

	@PostMapping("/create")
	//	上のregisterメソッドでモデルに格納されたhuseretisterformに、クライアントが値を入れて送ってきました
	//	ここではその中身に間違いがないか答え合わせ（validateion)をしていきます。
	public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		//		ModelAttribute= フォームから送られてきたデータ（インスタンスhouseServiceForm)をその引数にバインドする）
		//		validated=引数houseRegisterFormに対してバリデーション（HouseRegisterFormで付与した＠NotBlankなど)を実装する
		//		bindingresult=validatedで引っかかったエラー内容が格納される、hasErrorsメソッドでエラーの有無を確認し、thymeleafのth:errorsでエラーメッセージを表示できる

		if (bindingResult.hasErrors()) {
			//	        	 エラーがあればこっち
			return "admin/houses/register";
			//	             register.htmlに戻った時にthymelaafのth:errorでエラーを表示できる
		}
		//	         エラーがなければこっち
		houseService.create(houseRegisterForm);
		//	         HouseServiceクラスで実装しているcreateメソッドにhouseRegisterForm(答案用紙)を渡して呼び出す。
		//	         HouserService.java のCreateメソッドへー＞
		//	         createメソッド内でsava()を実装し、フォームに記入された値をdbへ保存する

		redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。");
		//	         redirectAttribute= 登録成功時にreturnのreduirect/admin/houses(つまり民泊一覧ページ)にリダイレクトする
		//	         addFlashAttrubute=リダイレクトした時にメッセージを一度だけ(Flash)表示するメソッド。
		return "redirect:/admin/houses";

	}

	@GetMapping("{id}/edit")
	public String edit(@PathVariable(name = "id") Integer id, Model model) {

		House house = houseRepository.getReferenceById(id);
		//		IDから民泊データを取得してhouseに格納
		String imageName = house.getImageName();
		//		houseEditFormにはiamgeFileをmultiplepartFile型で定義しているが、編集時には、multipartFile型のファイルを直接渡す必要はなく、
		//		そのファイルのファイル名だけでいいので、下記のインスタンスの生成時には、multipartfileの部分はnullにしておく。
		//		代わりに、上記でdbに格納されているcreate時にファイル名(HouseServcice/public void create/house.setImageName(hasedImageName)を用いる

		HouseEditForm houseEditForm = new HouseEditForm(house.getId(), house.getName(), null, house.getDescription(),
				house.getPrice(), house.getCapacity(), house.getPostalCode(), house.getAddress(),
				house.getPhoneNumber());
		//		houseに格納された、編集したい民泊データの値を一つづつとりだして、新しく作るhouseeditformフィールドに格納する

		model.addAttribute("imageName", imageName);
		model.addAttribute("houseEditForm", houseEditForm);

		return "admin/houses/edit";

	}

	@PostMapping("{id}/update")
	public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, BindingResult bindingResult, 
			RedirectAttributes redirectAttributes) {
		
		if(bindingResult.hasErrors()) {
			return "admin/houses/edit";
		}
		
		houseService.update(houseEditForm);
		
		redirectAttributes.addFlashAttribute("successMessage", "民宿情報を更新しました。");
		
		return"redirect:/admin/houses";
		
	}
	
	@PostMapping("{id}/delete")
	public String delete(@PathVariable(name="id")Integer id, RedirectAttributes redirectAttributes) {
		
		houseRepository.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage","民泊を削除しました");
		return "redirect:/admin/houses";
//		
	}
	

}
