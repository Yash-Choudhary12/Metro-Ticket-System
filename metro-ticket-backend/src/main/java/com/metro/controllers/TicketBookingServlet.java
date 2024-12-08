package com.metro.controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.metro.models.Ticket;
import com.metro.models.User;
import com.metro.services.TicketService;
import com.metro.utils.QRCodeGenerator;

@SuppressWarnings("serial")
@WebServlet(name = "TicketBookingServlet", urlPatterns = { "/bookTicket" })
public class TicketBookingServlet extends HttpServlet {

    private TicketService ticketService;

    @Override
    public void init() throws ServletException {
        super.init();
        ticketService = new TicketService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        try {
        	HttpSession session = request.getSession();
            String passengerName = (String) session.getAttribute("passengerName");
            String razorpayOrderId = (String) session.getAttribute("razorpayOrderId");
            User user = (User) session.getAttribute("user");

            if (passengerName == null || passengerName.isEmpty() || razorpayOrderId == null || razorpayOrderId.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\": \"Missing ticket booking details.\"}");
                return;
            }


            // Generate QR code for the ticket
            String qrCodeData = razorpayOrderId + "-" + System.currentTimeMillis();
            String qrCodeFilePath = "C:\\Users\\YASH\\eclipse-workspace\\metro-ticket-backend\\src\\main\\webapp\\qr_codes\\" + qrCodeData + ".png";
            QRCodeGenerator.generateQRCode(qrCodeData, qrCodeFilePath, 200, 200);
            
            
            Ticket ticket = new Ticket(user, qrCodeFilePath, passengerName);

            
            Ticket savedTicket = ticketService.saveTicket(ticket);
            boolean isDataUpdated = ticketService.updatePaymentStatus(savedTicket.getTicketId(), true, razorpayOrderId);
            

            if (isDataUpdated) {
            	request.setAttribute("ticket", savedTicket);
            	request.setAttribute("qrCodeData", qrCodeData);
            	
            	request.getRequestDispatcher("/ticketDetails.jsp").forward(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Failed to book the ticket.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"An unexpected error occurred.\"}");
        }
    }
}
