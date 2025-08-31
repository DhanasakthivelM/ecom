package com.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.model.Cart;
import com.example.model.CartItemDTO;
import com.example.model.Product;
import com.example.model.Userr;
import com.example.service.CartService;
import com.example.service.ProductService;

import com.example.service.UserrService;

@Controller
public class CartController {
	@Autowired
	CartService cs;

	@Autowired
	UserrService userrService;

	@Autowired
	ProductService productService;

	/**
	 * Adds a product to the authenticated user's cart. If the product is already in
	 * the cart, increments the quantity.
	 * Returns a success or error message as a JSON response.
	 */
	@PostMapping("/addToCart")
	@ResponseBody
	public ResponseEntity<String> addToCart(@RequestParam("productId") int productId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
		}

		String username = authentication.getName();
		Optional<Userr> optionalUser = userrService.getUserByUsername(username);

		if (optionalUser.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}

		Userr userr = optionalUser.get();
		Cart cart = cs.getCartDetailsByUserIdAndProductId(userr.getId(), productId);

		if (cart != null) {
			cart.setQuantity(cart.getQuantity() + 1);
			cs.addCart(cart);
		} else {
			cs.addCart(new Cart(userr.getId(), productId, 1));
		}

		return ResponseEntity.ok("✅ Item added to cart successfully!");
	}

	/**
	 * Displays the cart page for the authenticated user, showing all cart items
	 * with product details.
	 */
	@GetMapping("/cart")
	public String cart(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/req/login"; // Redirect to login page if not authenticated
		}

		String username = authentication.getName();
		Userr userr = userrService.getUserByUsername(username).get();

		List<Cart> cartItems = cs.getAllCartItemsByUserrId(userr.getId());

		List<CartItemDTO> cartItemDTOs = new ArrayList<>();

		for (Cart cart : cartItems) {
			Product product = productService.getProductById(cart.getProductId());
			if (product != null) {
				cartItemDTOs.add(new CartItemDTO(cart, product));
			}
		}

		System.out.println(cartItemDTOs);
		model.addAttribute("cartItems", cartItemDTOs);
		model.addAttribute("user", userr);

		return "cart";
	}

	/**
	 * Displays the checkout page for the authenticated user.
	 */
	@GetMapping("/checkout")
	public String checkout(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/req/login"; // Redirect to login page if not authenticated
		}

		String username = authentication.getName();
		Userr userr = userrService.getUserByUsername(username).get();

		model.addAttribute("user", userr);

		return "checkout"; // This should match your checkout.html or checkout.jsp
	}

	/**
	 * Deletes a cart item by its cartId. Returns a simple string indicating success
	 * or failure.
	 */
	@PostMapping("/deleteCartItem")
	@ResponseBody
	public String deleteCartItem(@RequestParam("cartId") int cartId) {
		if (cs.deleteCartItem((int) cartId) == true) {
			return "SUCCESSFULLDELTED";
		}
		return "NOT DELTED";
	}

}
