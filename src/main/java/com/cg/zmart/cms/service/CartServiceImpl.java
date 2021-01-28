package com.cg.zmart.cms.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.zmart.cms.entity.CartEntity;
import com.cg.zmart.cms.entity.CartItemEntity;
import com.cg.zmart.cms.exception.CartException;
import com.cg.zmart.cms.model.CartItem;
import com.cg.zmart.cms.model.CartModel;
import com.cg.zmart.cms.repository.CartRepository;

@Service
public class CartServiceImpl implements CartServcie{
	
	
	@Autowired
	private CartRepository cartRepo;
	
	
	private CartItem toCartItem(CartItemEntity cartItemEntity) {
		CartItem cartItem = new CartItem();
		if(cartItemEntity!=null) {
			cartItem.setCartItemId(cartItemEntity.getCartItemId());
			cartItem.setPrice(cartItemEntity.getPrice());
			cartItem.setProductId(cartItemEntity.getProductId());
			cartItem.setQuantity(cartItemEntity.getQuantity());
			cartItem.setProductName(cartItemEntity.getProductName());
			cartItem.setImgUrl(cartItemEntity.getImgUrl());
		}
		
		return cartItem;
	}
	
	private CartModel toCartModel(CartEntity cartEntity) {
		
		CartModel cartModel = new CartModel();
		
		if(cartEntity!=null) {
			
			cartModel.setUserId(cartEntity.getUserId());
			cartModel.setCartItem(cartEntity.getCartItem().stream().map((entity)->
									toCartItem(entity)).collect(Collectors.toList()));
		
		}
		return cartModel;
	}
	
	private CartItemEntity toCartItemEntity(CartItem cartItem) {
		CartItemEntity cartItemEntity = new CartItemEntity();
		if(cartItem!=null) {
			cartItemEntity.setPrice(cartItem.getPrice());
			cartItemEntity.setProductId(cartItem.getProductId());
			cartItemEntity.setQuantity(cartItem.getQuantity());
			cartItemEntity.setProductName(cartItem.getProductName());
			cartItemEntity.setImgUrl(cartItem.getImgUrl());
		}
		return cartItemEntity;
	}

	@Override
	public CartModel viewCart(long userId) {
		
		if(!cartRepo.findById(userId).isPresent()) {
			throw new CartException("User Id Does Not Exist");
		}
		
		CartEntity cartEntity = cartRepo.findById(userId).get();
		return toCartModel(cartEntity);
	}

	@Override
	public boolean removeItem(long userId, long productId) {

		if(!cartRepo.findById(userId).isPresent()) {
			throw new CartException("User Id Does Not Exist");
		}
		
		CartEntity cartEntity = cartRepo.findById(userId).get();
		
		cartEntity.setCartItem(cartEntity.getCartItem().stream().
				filter(item-> item.getProductId()!=productId).collect(Collectors.toList()));
		cartRepo.save(cartEntity);
		return true;
	}

	@Override
	public CartModel addItem(long userId, CartItem cartItem) {
		
		if(!cartRepo.findById(userId).isPresent()) {
			throw new CartException("User Id Does Not Exist");
		}
		
		CartEntity cartEntity = cartRepo.findById(userId).get();
		boolean flag = true;
		
		for(CartItemEntity cartItemEntity : cartEntity.getCartItem()) {
			if(cartItemEntity.getProductId() == cartItem.getProductId()) {
				cartItemEntity.setQuantity(cartItem.getQuantity()+1);
				flag = false;
				break;
			}
		}
		if(flag) {
			cartEntity.getCartItem().add(toCartItemEntity(cartItem));
		}
	
		cartRepo.save(cartEntity);
		return toCartModel(cartEntity);
	}

	@Override
	public CartModel changeQuantity(long userId, long productId, int quantity) {
		
		if(!cartRepo.findById(userId).isPresent()) {
			throw new CartException("User Id Does Not Exist");
		}
		
		CartEntity cartEntity = cartRepo.findById(userId).get();
		boolean flag = true;
		
		for(CartItemEntity cartItemEntity : cartEntity.getCartItem()) {
			if(cartItemEntity.getProductId() == productId) {
				cartItemEntity.setQuantity(quantity);
				flag = false;
				break;
			}
		}
		if(flag) {
			throw new CartException("Product Id not Found!");
		}
		return toCartModel(cartEntity);
	}

	@Override
	public boolean registerCart(long userId) {
		
		if(cartRepo.findById(userId).isPresent()) {
			throw new CartException("User Id already registered Exist");
		}
		
		CartEntity cartEntity = new CartEntity();
		cartEntity.setUserId(userId);
		cartRepo.save(cartEntity);
		
		return true;
	}

	@Override
	public CartModel checkOut(long userId) {
		
		if(!cartRepo.findById(userId).isPresent()) {
			throw new CartException("User Id Does Not Exist");
		}
		
		CartEntity cartEntity = cartRepo.findById(userId).get();
		
		cartEntity.setCartItem(null);
		
		return toCartModel(cartRepo.save(cartEntity));
	}
;
	
	
}
