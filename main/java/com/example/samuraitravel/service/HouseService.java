package com.example.samuraitravel.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.HouseEditForm;
import com.example.samuraitravel.form.HouseRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;

@Service
//サービスクラスとして指定

public class HouseService {

	private final HouseRepository houseRepository;
	//    HouseReopsitory型の定数、houserepositoryフィールドを定義crudメソッドがつかえるようになる

	public HouseService(HouseRepository houseRepository) {
		//定義した定数を引数にとるコンストラクタで、受け取った値(houseRepository)をこのクラスの定数houserepositoryに格納する
		this.houseRepository = houseRepository;

	}

	@Transactional
	//    トランザクション化する、（データベース処理が０か１００か、処理が途中までという事態にはならない、データの整合性、一貫性を保つ）

	public void create(HouseRegisterForm houseRegisterForm) {
		//    	AdminHouseController.java内のcreateメソッド内のhouseService.create(houseRegisterFomr)の引数に格納された答案用紙をうけとる。
		House house = new House();
		//    	DBに新しい行レコードをつくるので、データを入れる箱も新しく作る。
		//    	House型のフィールドhouseを作って、インスタンスを格納。
		MultipartFile imageFile = houseRegisterForm.getImageFile();
		//    	引数で受け取ったhouseRegisterFormに格納されたイメージファイル（画像データ）をMultipartFile型のフィールドimageFileに格納。

		if (!imageFile.isEmpty()) {
			//        	ファイルがからでなければ..つまりこのページに遷移した時はファイルはまだ入ってないからこの処理はなし。

			String imageName = imageFile.getOriginalFilename();
			//            ファイルのオリジナルの名前データのみを取得

			String hashedImageName = generateNewFileName(imageName);
			//            ファイルのオリジナルの名前をgenereteNewFileNameUUIDを使って、重複しない一意の名前にする

			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
			//            Pathクラス＝特定のファイルやディレクトリのパスを表現するためのクラス
			//            Pathsクラス=pathクラスのインスタンス生成を補助するクラス。
			//            画像ファイルを保存してるディレクトリ"src/main/resources/static/storage/"にhashedImageNameで新しく作った名前のファイル(空)を 作るための筋道（パス）を指定する

			copyImageFile(imageFile, filePath);
			//            failePathの筋道の先にあるhashedImageNamem（新しい名前の空ファイル）に画像データ(imageFile)を格納

			house.setImageName(hashedImageName);
			//            エンティティから作ったhouseインスタンスに画像名をセット

		}

		house.setName(houseRegisterForm.getName());
		//       エンティティから作ったhouseインスタンスに民泊名をセット
		house.setDescription(houseRegisterForm.getDescription());
		//       エンティティから作ったhouseインスタンスに説明文をセット
		house.setPrice(houseRegisterForm.getPrice());
		//       エンティティから作ったhouseインスタンスに料金をセット
		house.setCapacity(houseRegisterForm.getCapacity());
		//       エンティティから作ったhouseインスタンスに定員数をセット
		house.setPostalCode(houseRegisterForm.getPostalCode());
		//       エンティティから作ったhouseインスタンスに郵便番号をセット
		house.setAddress(houseRegisterForm.getAddress());
		//       エンティティから作ったhouseインスタンスに住所をセット
		house.setPhoneNumber(houseRegisterForm.getPhoneNumber());
		//       エンティティから作ったhouseインスタンスに電話番号をセット
		houseRepository.save(house);
		//         データベースに保存する

	}

	@Transactional
	public void update(HouseEditForm houseEditForm) {
		// TODO 自動生成されたメソッド・スタブ

		House house = houseRepository.getReferenceById(houseEditForm.getId());
//		更新したい民泊の情報をidから取得する。

		MultipartFile imageFile = houseEditForm.getImageFile();

		if (!imageFile.isEmpty()) {

			String imageName = imageFile.getOriginalFilename();
			//			 画像データオリジナルの名前をimageNameに格納
			String hashedImageName = generateNewFileName(imageName);
			//			 generateNewFileNameメソッドを使って画像名をハッシュ化してhashFileNameに格納

			Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);

			copyImageFile(imageFile, filePath);

			house.setImageName(hashedImageName);

		}

		house.setName(houseEditForm.getName());
		//       エンティティから作ったhouseインスタンスに民泊名をセット
		house.setDescription(houseEditForm.getDescription());
		//       エンティティから作ったhouseインスタンスに説明文をセット
		house.setPrice(houseEditForm.getPrice());
		//       エンティティから作ったhouseインスタンスに料金をセット
		house.setCapacity(houseEditForm.getCapacity());
		//       エンティティから作ったhouseインスタンスに定員数をセット
		house.setPostalCode(houseEditForm.getPostalCode());
		//       エンティティから作ったhouseインスタンスに郵便番号をセット
		house.setAddress(houseEditForm.getAddress());
		//       エンティティから作ったhouseインスタンスに住所をセット
		house.setPhoneNumber(houseEditForm.getPhoneNumber());
		//       エンティティから作ったhouseインスタンスに電話番号をセット
		houseRepository.save(house);
		//         データベースに保存する

	}
	// UUIDを使って生成したファイル名を返す

	public String generateNewFileName(String fileName) {

		String[] fileNames = fileName.split("\\.");

		for (int i = 0; i < fileNames.length - 1; i++) {

			fileNames[i] = UUID.randomUUID().toString();

		}

		String hashedFileName = String.join(".", fileNames);

		return hashedFileName;

	}

	// 画像ファイルを指定したファイルにコピーする

	public void copyImageFile(MultipartFile imageFile, Path filePath) {
		try {

			Files.copy(imageFile.getInputStream(), filePath);

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}
