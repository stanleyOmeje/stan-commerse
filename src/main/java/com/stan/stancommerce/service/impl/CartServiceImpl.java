package com.stan.stancommerce.service.impl;

import com.stan.stancommerce.dto.AddItemtoCartRequest;
import com.stan.stancommerce.dto.CartDto;
import com.stan.stancommerce.dto.CartItemDto;
import com.stan.stancommerce.dto.UpdateCartRequest;
import com.stan.stancommerce.dto.response.DefaultResponse;
import com.stan.stancommerce.entities.Cart;
import com.stan.stancommerce.entities.CartItems;
import com.stan.stancommerce.entities.Product;
import com.stan.stancommerce.enums.ResponseStatus;
import com.stan.stancommerce.exception.NotFoundException;
import com.stan.stancommerce.mapper.CartMapper;
import com.stan.stancommerce.repositories.CartRepository;
import com.stan.stancommerce.repositories.ProductRepository;
import com.stan.stancommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    public DefaultResponse<?> createCart() {
        DefaultResponse<CartDto> response = new DefaultResponse<>();
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setMessage(ResponseStatus.FAILED.getMessage());
        CartDto cartDto = null;
        try {
            Cart cart = new Cart();
            cart = cartRepository.save(cart);
            response.setStatus(ResponseStatus.CREATED.getCode());
            response.setMessage(ResponseStatus.CREATED.getMessage());
            cartDto = cartMapper.mapCartToCartDto(cart);
            response.setData(cartDto);

            return response;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    @Override
    public DefaultResponse<?> addToCart(Long cartId, AddItemtoCartRequest request) {
        //check if cart exit
        DefaultResponse<CartItemDto> response = new DefaultResponse();
        CartItemDto cartItemDto = null;
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isEmpty()) {
            throw new NotFoundException("Cart not found");
        }
        //check if product exit
        Optional<Product> optionalProduct = productRepository.findById(request.getProductId());
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException("product not found");
        }
        Cart cart = optionalCart.get();

        Product product = optionalProduct.get();
        log.info("Product....{}", product);
        CartItems cartItems = cart.addCartItem(product);
        try {
            cart = cartRepository.save(cart);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        cartItemDto = cartMapper.mapCartToCartItemDto(cartItems);
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());
        response.setData(cartItemDto);
        return response;
    }


    @Override
    public DefaultResponse<?> getCart(Long cartId) {
        DefaultResponse<CartDto> response = new DefaultResponse<>();
        response.setStatus(ResponseStatus.SUCCESS.getCode());
        response.setMessage(ResponseStatus.SUCCESS.getMessage());

        CartDto cartDto = null;
        Cart cart = null;
        try {
            cart = cartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not found"));
            if (cart == null) {
                response.setStatus(ResponseStatus.NOT_FOUND.getCode());
                response.setMessage(ResponseStatus.NOT_FOUND.getMessage());
                return response;
            }
        } catch (NotFoundException e) {
            response.setStatus(ResponseStatus.NOT_FOUND.getCode());
            response.setMessage(ResponseStatus.NOT_FOUND.getMessage());
            return response;
        } catch (Exception e) {
            response.setStatus(e.getMessage());
            log.error(e.getMessage());
        }
        if (cart == null) {
            throw new NotFoundException();
        }
        Set<CartItems> cartItems = cart.getCartItems();
        if (cartItems != null && !cartItems.isEmpty()) {
            cartDto = cartMapper.mapCartItemtoCartDto(cart.getCartItems(), cart);
            response.setStatus(ResponseStatus.SUCCESS.getCode());
            response.setMessage(ResponseStatus.SUCCESS.getMessage());
            response.setData(cartDto);
            return response;
        }
        cartDto = new CartDto();
        cartDto.setId(cartId);
        response.setData(cartDto);
        return response;
    }

    @Override
    public DefaultResponse<CartItemDto> updateCartItems(Long cartId, Long productId, UpdateCartRequest updateCartRequest) {
        DefaultResponse<CartItemDto> response = new DefaultResponse<>();
        response.setStatus(ResponseStatus.FAILED.getCode());
        response.setMessage(ResponseStatus.FAILED.getMessage());
        CartItemDto cartItemDto = null;
        Cart cart = null;
        try {
            cart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
            CartItems cartItems = cart.getCartItem(productId);
            if (cartItems != null) {
                cartItems.setQuantity(updateCartRequest.getQuantity());
                cart = cartRepository.save(cart);
                cartItemDto = cartMapper.mapCartToCartItemDto(cartItems);
                response.setStatus(ResponseStatus.SUCCESS.getCode());
                response.setMessage(ResponseStatus.SUCCESS.getMessage());
                response.setData(cartItemDto);
                return response;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return response;
    }

    @Override
    public void removeProductFromCart(Long cartId, Long productId) {
        log.info("Removing product from cart " + productId);
        try {
            Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not found"));
            if (cart == null) {
                throw new IllegalArgumentException("Cart is empty");
            }
            cart.removeCartItem(productId);
            cartRepository.save(cart);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        if (cart == null) {
            throw new IllegalArgumentException("Cart is empty");
        }
        cart.clearCart();
        cartRepository.save(cart);
    }

}
