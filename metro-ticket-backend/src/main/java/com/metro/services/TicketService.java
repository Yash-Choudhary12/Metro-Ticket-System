package com.metro.services;

import com.metro.dao.TicketDAO;
import com.metro.models.Ticket;

import java.util.List;

public class TicketService {

	private final TicketDAO ticketDAO;

	public TicketService() {
		this.ticketDAO = new TicketDAO();
	}

	
	public Ticket saveTicket(Ticket ticket) {
		return ticketDAO.save(ticket);
	}

	
	public Ticket saveOrUpdateTicket(Ticket ticket) {
		return ticketDAO.saveOrUpdate(ticket);
	}


	public List<Ticket> getAllTickets() {
		return ticketDAO.findAll();
	}

	
	public Ticket getTicketById(int ticketId) {
		return ticketDAO.findById(ticketId);
	}


	public Ticket getTicketByQrCode(String qrCode) {
		return ticketDAO.findByQrCode(qrCode);
	}


	public boolean deleteTicket(int ticketId) {
	    Ticket ticket = ticketDAO.findById(ticketId);
	    if (ticket != null) {
	        ticketDAO.delete(ticketId);
	        return true;
	    }
	    return false;
	}


	public boolean updatePaymentStatus(int ticketId, boolean paymentStatus, String razorpayOrderId) {
		Ticket ticket = ticketDAO.findById(ticketId);

		if (ticket != null) {
			ticket.setPaymentStatus(true);
			ticket.setRazorpayOrderId(razorpayOrderId);
			ticketDAO.saveOrUpdate(ticket);
			return true;
		}
		return false;
	}
}
