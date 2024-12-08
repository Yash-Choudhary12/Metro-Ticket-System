package com.metro.models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id")
	private int ticketId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "passenger_name", nullable = false)
	private String passengerName;

	@Column(name = "qr_code", nullable = false)
	private String qrCode;

	@Column(name = "booking_time", nullable = false, updatable = false)
	private Timestamp bookingTime;

	@Column(name = "is_scanned", nullable = false)
	private boolean isScanned;

	@Column(name = "payment_status", nullable = false)
	private boolean paymentStatus;

	@Column(name = "razorpay_order_id")
	private String razorpayOrderId;

	public Ticket() {
	}

	public Ticket(User user, String qrCode, String passengerName) {
		this.user = user;
		this.passengerName = passengerName;
		this.qrCode = qrCode;
		this.bookingTime = new Timestamp(System.currentTimeMillis());
		this.isScanned = false;
	}

	public int getTicketId() {
		return ticketId;
	}

	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public Timestamp getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(Timestamp bookingTime) {
		this.bookingTime = bookingTime;
	}

	public boolean isScanned() {
		return isScanned;
	}

	public void setScanned(boolean scanned) {
		isScanned = scanned;
	}

	public boolean isPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getRazorpayOrderId() {
		return razorpayOrderId;
	}

	public void setRazorpayOrderId(String razorpayOrderId) {
		this.razorpayOrderId = razorpayOrderId;
	}

	// Override toString method for debugging
	@Override
	public String toString() {
		return "Ticket{" + "ticketId=" + ticketId + ", user=" + user.getUserId() + ", qrCode='" + qrCode + '\''
				+ ", bookingTime=" + bookingTime + ", isScanned=" + isScanned + '}';
	}
}