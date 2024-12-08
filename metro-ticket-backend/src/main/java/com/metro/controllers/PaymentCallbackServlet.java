package com.metro.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.metro.config.PropertiesManager;
import com.metro.models.Station;
import com.metro.models.User;
import com.metro.services.StationService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

@SuppressWarnings("serial")
@WebServlet(name = "PaymentCallbackServlet", urlPatterns = { "/paymentCallback" })
public class PaymentCallbackServlet extends HttpServlet {
    private RazorpayClient razorpayClient;
    private Properties propertiesManager = PropertiesManager.getInstance();;

    @Override
    public void init() throws ServletException {
        try {
            razorpayClient = new RazorpayClient(propertiesManager.getProperty("API_KEY"), propertiesManager.getProperty("API_KEY_SECRET"));
        } catch (RazorpayException e) {
            throw new ServletException("Failed to initialize Razorpay client", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	
        	response.setHeader("Access-Control-Allow-Origin", "*");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                request.setAttribute("error", "User not logged in. Please log in to continue.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
           
            
            String passengerName = request.getParameter("passengerName");
            String type = request.getParameter("type");
            System.out.println("Request type: " + type); 
            StationService stationService = new StationService();
            String source = request.getParameter("source");
            String destination;

            switch (type) {
            case "updateDestinations":
            	String finalSource = source;
                if (finalSource == null || finalSource.isEmpty()) {
                    sendJsonError(response, "Source is missing.");
                    return;
                }

                List<Station> destinations = stationService.getAllStations().stream()
                        .filter(station -> !station.getStation_name().equals(finalSource))
                        .collect(Collectors.toList());

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("destinations", destinations.stream().map(Station::getStation_name).toArray());
                sendJsonResponse(response, jsonResponse);
                break;

            case "calculatePrice":
            	
                destination = request.getParameter("destination");
                if (source == null || source.isEmpty() || destination == null || destination.isEmpty()) {
                    sendJsonError(response, "Source or destination is missing.");
                    return;
                }

                int price = stationService.calculatePrice(source, destination);
                jsonResponse = new JSONObject();
                jsonResponse.put("price", price);
                sendJsonResponse(response, jsonResponse);
                break;

            default:
            	source = request.getParameter("source");
                destination = request.getParameter("destination");
                int priceItem = Integer.parseInt(request.getParameter("price"));
                passengerName = request.getParameter("passengerName");
                if (source == null || source.isEmpty() || destination == null || destination.isEmpty()) {
                    sendJsonError(response, "Source or destination is missing.");
                    return;
                }
                
                

                jsonResponse = new JSONObject();
                jsonResponse.put("amount", priceItem * 100); // Amount in paise
                jsonResponse.put("currency", "INR");
                jsonResponse.put("receipt", "txn_" + System.currentTimeMillis());
                Order order = razorpayClient.orders.create(jsonResponse);

                String orderId = order.get("id");
                jsonResponse.put("orderId", orderId);

                request.setAttribute("passengerName", passengerName);
                request.setAttribute("source", source);
                request.setAttribute("destination", destination);
                request.setAttribute("calculatedPrice", priceItem);
                request.setAttribute("email", user.getEmail());
                request.setAttribute("contact", user.getPhoneNumber());

                sendJsonResponse(response, jsonResponse);
                break;
        }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.setAttribute("error", "An unexpected error occurred.");
        }
    }

    private void sendJsonResponse(HttpServletResponse response, JSONObject json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }

    private void sendJsonError(HttpServletResponse response, String errorMessage) throws IOException {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", errorMessage);
        sendJsonResponse(response, errorResponse);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (user == null) {
                request.setAttribute("error", "User not logged in. Please log in to continue.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Step 1: Fetch Passenger Details and Route Info
            String passengerName = request.getParameter("passengerName");
            String source = request.getParameter("source");
            String destination = request.getParameter("destination");

            if (passengerName == null || source == null || destination == null) {
                request.setAttribute("error", "Missing passenger or route details.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Step 2: Razorpay Callback Handling
            String razorpayPaymentId = request.getParameter("razorpay_payment_id");
            String razorpayOrderId = request.getParameter("razorpay_order_id");
            String razorpaySignature = request.getParameter("razorpay_signature");

            if (razorpayPaymentId == null || razorpayOrderId == null || razorpaySignature == null) {
                request.setAttribute("error", "Invalid payment callback parameters.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // Step 3: Verify Razorpay Payment Signature
            boolean isSignatureValid = Utils.verifySignature(
                    razorpayOrderId + "|" + razorpayPaymentId,
                    razorpaySignature, propertiesManager.getProperty("API_KEY_SECRET"));

            if (!isSignatureValid) {
                request.setAttribute("error", "Invalid payment signature.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            

            if (isSignatureValid) {
                request.getSession().setAttribute("razorpayOrderId", razorpayOrderId);
                request.getSession().setAttribute("passengerName", passengerName);
                request.setAttribute("user", user);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/bookTicket");
                dispatcher.forward(request, response);
            } 
            
            else {
                request.setAttribute("error", "Failed to update ticket payment status.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            request.setAttribute("error", "An unexpected error occurred.");
        }
    }
}
