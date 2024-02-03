package com.example.samuraitravel.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
//spring initailizer でっ設定したspring Data JPAが提供するインターフェース。CRUDが使えるようになる

import com.example.samuraitravel.entity.House;
//houseエンティティをインポートする

public interface HouseRepository extends JpaRepository<House,Integer> {
//	JpaRepository<エンティティのクラス型,主キーのデータ型>

	public Page<House> findByNameLike(String keyword, Pageable pageable);
//	キーワードを含む民泊名を検索するメソッド
	public Page<House> findByNameLikeOrAddressLike(String nameKeyword, String addressKeyword, Pageable pageable);
	public Page<House> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);
	public Page<House> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);
	//	キーワードを含む民泊名もしくは住所から検索するメソッド

	public Page<House> findByAddressLike(String area, Pageable pageable);
	public Page<House> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);
	public Page<House> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);
//	エリア検索するメソッド
	public Page<House> findByPriceLessThanEqual(Integer price, Pageable pageable);
	public Page<House> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
	public Page<House> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);
//	料金から検索するメソッド

	public Page<House>findAllByOrderByCreatedAtDesc(Pageable pageable);
	public Page<House>findAllByOrderByPriceAsc(Pageable pageable);

	public List<House>findTop10ByOrderByCreatedAtDesc();
//	TOP = sqlのlimitの役割、




}
