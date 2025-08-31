package com.example.controller;

import java.security.Principal;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.Cart;

import com.example.model.OrderStatus;
import com.example.model.Orders;
import com.example.model.Payment;
import com.example.model.PaymentStatus;
import com.example.model.Product;
import com.example.model.Userr;
import com.example.service.CartService;
import com.example.service.OrdersService;
import com.example.service.PaymentService;
import com.example.service.ProductService;
import com.example.service.UserrService;

@Controller
public class OrdersController {

	@Autowired
	OrdersService ordersService;
	@Autowired
	UserrService userrService;

	@Autowired
	CartService cs;

	@Autowired
	PaymentService ps;

	@Autowired
	ProductService productService;

	/**
	 * Places an order for the authenticated user, processes payment, and clears the
	 * cart.
	 */
	@PostMapping("/placeOrder")
	public String placeOrder(@RequestParam("name") String fullName, @RequestParam("address") String adress,
			@RequestParam("phone") String phone, @RequestParam("payment") String payment,
			@RequestParam String cardNumber, @RequestParam String expiry, @RequestParam String cvv,
			@RequestParam String upiId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/req/login"; // Redirect to login page if not authenticated
		}

		String username = authentication.getName();
		Userr userr = userrService.getUserByUsername(username).get();

		List<Cart> cartItems = cs.getAllCartItemsByUserrId(userr.getId());

		double totalAmount = 0;
		for (Cart cart : cartItems) {
			Product product = productService.getProductById(cart.getProductId());
			if (product != null) {
				// Decrease stock after ordering
				int newStock = product.getStockQuantity() - cart.getQuantity();
				product.setStockQuantity(Math.max(newStock, 0)); // Prevent negative stock
				productService.addProduct(product); // Save updated product

				totalAmount += cart.getQuantity() * product.getPrice();
			}
		}

		LocalDateTime now = LocalDateTime.now(); // current date and time
		Orders orders = new Orders();
		orders.setUserId(userr.getId());
		orders.setAmount(totalAmount);
		orders.setOrderDate(now);
		orders.setStatus(OrderStatus.SHIPPED);
		Orders o = ordersService.addOrder(orders);
		if (o != null) {

			Payment codPayment = ps
					.addPayment(new Payment(o.getOrderId(), totalAmount, LocalDateTime.now(), PaymentStatus.COMPLETED));

			if (codPayment != null) {
				String status = cs.deleteAllCartItemsUsingUserrId(userr.getId());
				if (status != null)
					return "success";
			}

		}
		return "cart";

	}

	/**
	 * Displays all orders for admin users.
	 */
	@GetMapping("/adminorders")
	public String getAllOrders(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal().equals("anonymousUser")) {
			return "redirect:/req/login"; // Redirect to login page if not authenticated
		}
		model.addAttribute("orderList", ordersService.getAllOrders());

		return "adminorders";
	}

	/**
	 * Displays all orders for the authenticated user.
	 */
	@GetMapping("/userorders")
	public String getUserOrders(Principal principal, Model model) {
		String username = principal.getName();

		Optional<Userr> userOptional = userrService.getUserByUsername(username);

		if (userOptional.isPresent()) {
			Userr user = userOptional.get();
			int userId = user.getId(); // Now you have the user ID
			model.addAttribute("user", userOptional.get());
			model.addAttribute("orderList", ordersService.getOrdersByUserId(userId));
			return "myorders";
		}

		return "redirect:/req/login"; // If user not found, redirect to login
	}

}
