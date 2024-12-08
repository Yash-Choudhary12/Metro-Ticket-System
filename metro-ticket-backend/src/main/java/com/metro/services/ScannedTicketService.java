package com.metro.services;

import com.metro.dao.ScannedTicketDAO;
import com.metro.models.ScannedTicket;
import java.util.List;

public class ScannedTicketService {

	private final ScannedTicketDAO scannedTicketDAO;

	public ScannedTicketService() {
		this.scannedTicketDAO = new ScannedTicketDAO();
	}

	/**
	 * Save a new scanned ticket.
	 *
	 * @param scannedTicket The scanned ticket object.
	 * @return The saved scanned ticket.
	 */
	public ScannedTicket save(ScannedTicket scannedTicket) {
		return scannedTicketDAO.save(scannedTicket);
	}

	/**
	 * Save or update a scanned ticket.
	 *
	 * @param scannedTicket The scanned ticket object.
	 * @return The updated or saved scanned ticket.
	 */
	public ScannedTicket saveOrUpdate(ScannedTicket scannedTicket) {
		return scannedTicketDAO.saveOrUpdate(scannedTicket);
	}

	/**
	 * Find all scanned tickets.
	 *
	 * @return A list of all scanned tickets.
	 */
	public List<ScannedTicket> findAll() {
		return scannedTicketDAO.findAll();
	}

	/**
	 * Find a scanned ticket by its ID.
	 *
	 * @param scanId The ID of the scanned ticket.
	 * @return The scanned ticket, or null if not found.
	 */
	public ScannedTicket findById(int scanId) {
		return scannedTicketDAO.findById(scanId);
	}

	/**
	 * Delete a scanned ticket by its ID.
	 *
	 * @param scanId The ID of the scanned ticket to be deleted.
	 */
	public void delete(int scanId) {
		scannedTicketDAO.delete(scanId);
	}

	/**
	 * Find scanned tickets by ticket ID.
	 *
	 * @param ticketId The ID of the ticket.
	 * @return A list of scanned tickets associated with the provided ticket ID.
	 */
	public List<ScannedTicket> findByTicketId(int ticketId) {
		return scannedTicketDAO.findByTicketId(ticketId);
	}
}
