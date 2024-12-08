package com.metro.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.metro.models.ScannedTicket;
import com.metro.models.Ticket;
import com.metro.services.ScannedTicketService;
import com.metro.services.TicketService;

@SuppressWarnings("serial")
@WebServlet(name = "TicketScanningServlet", urlPatterns = { "/scanTicket" })
public class TicketScanningServlet extends HttpServlet {

    private TicketService ticketService;
    private ScannedTicketService scannedTicketService;

    @Override
    public void init() throws ServletException {
        ticketService = new TicketService();
        scannedTicketService = new ScannedTicketService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            String qrCode = request.getParameter("qrCode");

            if (qrCode == null || qrCode.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"error\": \"QR code is required\"}");
                return;
            }

            Ticket ticket = ticketService.getTicketByQrCode(qrCode);

            if (ticket == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"error\": \"Invalid ticket or ticket not found\"}");
                return;
            }

            // Check expiration (24 hours validity)
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ticket.getBookingTime());
            calendar.add(Calendar.HOUR, 24);

            if (now.after(calendar.getTime())) {
                boolean isDeleted = ticketService.deleteTicket(ticket.getTicketId());
                if (isDeleted) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.write("{\"message\": \"Expired ticket deleted\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    out.write("{\"error\": \"Failed to delete expired ticket\"}");
                }
                return;
            }

            // Prevent duplicate scans
            if (ticket.isScanned()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                out.write("{\"error\": \"Ticket already scanned\"}");
                return;
            }

            // Mark as scanned and log
            ticket.setScanned(true);
            ticketService.saveOrUpdateTicket(ticket);

            ScannedTicket scannedTicket = new ScannedTicket();
            scannedTicket.setTicket(ticket);
            scannedTicketService.save(scannedTicket);

            response.setStatus(HttpServletResponse.SC_OK);
            out.write("{\"message\": \"Ticket scanned successfully\", \"ticketId\": \"" + ticket.getTicketId() + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"error\": \"An unexpected error occurred\"}");
        }
    }
}
