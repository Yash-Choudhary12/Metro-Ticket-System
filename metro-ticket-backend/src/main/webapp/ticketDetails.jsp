<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.metro.models.Ticket" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.metro.services.TicketService" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ticket Information</title>
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet">
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap');
        
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }

        body {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background-image: url('images/bg.jpg');
            background-size: cover;
            background-position: center;
        }

        .ticket-details {
            background: rgba(64, 64, 64, 0.15);
            border: 3px solid rgba(255, 255, 255, 0.3);
            padding: 30px;
            border-radius: 16px;
            backdrop-filter: blur(25px);
            text-align: center;
            color: white;
            width: 400px;
            box-shadow: 0px 0px 20px 10px rgba(0, 0, 0, 0.15);
        }

        .ticket-title {
            font-size: 30px;
            margin-bottom: 20px;
        }

        .ticket-info {
            margin: 20px 0;
        }

        .ticket-info label {
            font-size: 18px;
            font-weight: 600;
        }

        .ticket-info span {
            font-size: 16px;
            font-weight: 400;
            color: #F37254;
        }

        .back-btn {
            width: 100%;
            padding: 10px 0;
            background: #2F9CF4;
            border: none;
            border-radius: 99px;
            color: white;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: 0.3s;
        }

        .back-btn:hover {
            background: #0B87EC;
        }
    </style>
</head>
<body>
    <div class="ticket-details">
        <h1 class="ticket-title">Ticket Information</h1>
        <% 
            Ticket ticket = (Ticket) request.getAttribute("ticket");
        	String qrCodePath = "/qr_codes/" + String.valueOf(request.getAttribute("qrCodeData")) + ".png";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");

            Calendar calendar = Calendar.getInstance();
            Date currentTime = calendar.getTime();
            calendar.add(Calendar.HOUR_OF_DAY, 24);
            Date expiryTime = calendar.getTime();

            String formattedCurrentTime = dateFormat.format(currentTime);
            String formattedExpiryTime = dateFormat.format(expiryTime);

            String passengerName = (ticket != null) ? ticket.getPassengerName() : "Not Available";
            int ticketId = (ticket != null) ? ticket.getTicketId() : 404;
        %>

        
        <div class="ticket-info">
            <label for="passengerName">Passenger Name:</label>
            <span><%= passengerName %></span>
        </div>
        <div class="ticket-info">
            <label for="ticketStatus">Ticket Status:</label>
            <span>Booked</span>
        </div>
        <div class="ticket-info">
            <label for="ticketIssueTime">Ticket Issue Time:</label>
            <span><%= formattedCurrentTime %></span>
        </div>
        <div class="ticket-info">
            <label for="ticketExpiryTime">Ticket Expiry Time (Next 24 Hours):</label>
            <span><%= formattedExpiryTime %></span>
        </div>
        <div class="ticket-info">
            <label for="qrCode">QR Code:</label>
            <span><%=qrCodePath%></span>
        </div>

        <form action="index.jsp" method="post">
            <button type="submit" class="back-btn">Back to Login</button>
        </form>
    </div>
</body>
</html>
