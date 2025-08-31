package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Payment;
import com.example.repository.PaymentRepository;

@Service
public class PaymentService {
	@Autowired
	PaymentRepository pr;

	/**
	 * Adds a new payment to the database.
	 */
	public Payment addPayment(Payment payment) {
		return pr.save(payment);
	}
}
