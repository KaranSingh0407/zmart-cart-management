package com.cg.zmart.cms.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CartItemEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long cartItemId;
	
	private long productId;
	
	private String productName;
	
	private long price;
	
	private String imgUrl;
	
	private int quantity;

	public long getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(long cartItemId) {
		this.cartItemId = cartItemId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	
	
	
}
