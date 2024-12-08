package com.metro.services;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.razorpay.RazorpayClient;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.Utils;

public class PaymentService {

	private static final String RAZORPAY_KEY_ID = "your_razorpay_key_id"; // Replace with your Razorpay key ID
	private static final String RAZORPAY_KEY_SECRET = "your_razorpay_key_secret"; // Replace with your Razorpay key
																					// secret

	private RazorpayClient razorpayClient;

	public PaymentService() {
		try {
			// Initialize Razorpay client
			this.razorpayClient = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to initialize Razorpay client");
		}
	}

	/**
	 * Creates a new Razorpay order.
	 *
	 * @param amount    Amount in INR (e.g., 100 for ₹100).
	 * @param currency  The currency, e.g., "INR".
	 * @param receiptId A unique receipt identifier.
	 * @return The created Razorpay order as a JSON object.
	 */
	public JSONObject createOrder(int amount, String currency, String receiptId) {
		try {
			Map<String, Object> options = new HashMap<>();
			options.put("amount", amount * 100); // Razorpay requires the amount in paise
			options.put("currency", currency);
			options.put("receipt", receiptId);
			options.put("payment_capture", 1); // Auto-capture payment

			// Create Razorpay order
			Order order = razorpayClient.orders.create((JSONObject) options);

			return new JSONObject(order.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to create order: " + e.getMessage());
		}
	}

	/**
	 * Verifies the Razorpay payment signature.
	 *
	 * @param orderId   The Razorpay order ID.
	 * @param paymentId The Razorpay payment ID.
	 * @param signature The Razorpay signature.
	 * @return True if the signature is valid, false otherwise.
	 */
	public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
		try {
			// Verify signature using Razorpay's Utils class
			String payload = orderId + "|" + paymentId;
			boolean isValid = Utils.verifyWebhookSignature(payload, signature, RAZORPAY_KEY_SECRET);
			return isValid;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Fetches payment details for a given payment ID.
	 *
	 * @param paymentId The Razorpay payment ID.
	 * @return The payment details as a JSON object.
	 */
	public JSONObject fetchPaymentDetails(String paymentId) {
		try {
			// Fetch payment details
			Payment payment = razorpayClient.payments.fetch(paymentId);
			return new JSONObject(payment.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to fetch payment details: " + e.getMessage());
		}
	}
}
