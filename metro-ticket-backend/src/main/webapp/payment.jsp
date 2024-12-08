<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.metro.models.Station" %>
<%@ page import="com.metro.services.StationService" %>
<%@ page import="com.metro.config.PropertiesManager" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Metro Ticket</title>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
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

        .payment-form {
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

        .form-title {
            font-size: 40px;
            margin-bottom: 40px;
        }

        .input-box {
            margin: 20px 0;
            position: relative;
            z-index: 2;
        }

        .input-box input,
        .input-box select {
            width: 100%;
            background: rgba(255, 255, 255, 0.1);
            border: none;
            padding: 12px 10px 12px 45px;
            border-radius: 99px;
            outline: 3px solid transparent;
            transition: 0.3s;
            font-size: 17px;
            color: white;
            font-weight: 600;
            appearance: none;
        }

        .input-box input::placeholder {
            color: rgba(255, 255, 255, 0.8);
            font-size: 17px;
            font-weight: 600;
        }

        .input-box input:focus,
        .input-box select:focus {
            outline: 3px solid rgba(255, 255, 255, 0.3);
            background: rgba(255, 255, 255, 0.2);
            color: white;
        }

        .input-box i {
            position: absolute;
            left: 15px;
            top: 50%;
            transform: translateY(-50%);
            font-size: 20px;
            color: rgba(255, 255, 255, 0.8);
        }

        .payment-btn {
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

        .payment-btn:hover {
            background: #0B87EC;
        }
        
        .select-dropdown {
            background: rgba(255, 255, 255, 0.1);
            border-radius: 99px;
            padding: 12px;
            color: white;
            height: 60%;
            font-size: 17px;
            font-weight: 600;
            appearance: none;
            position: relative;
            cursor: pointer;
        }

        .select-dropdown option {
            color: black; 
            background: white;
        }
        
        .select-arrow {
		    position: absolute;
		    width: 25px;
		    right: 15px;
		    top: 50%;
		    transform: translateY(-50%);
		    pointer-events: none;
		    font-size: 18px;
		    color: rgba(255, 255, 255, 0.8);
		}
		
		 @media screen and (max-width: 768px) {
	        .payment-form {
	            width: 90%;
	            padding: 20px;
	            border-radius: 12px;
	        }
	
	        .form-title {
	            font-size: 30px;
	            margin-bottom: 20px;
	        }
	
	        .input-box input,
	        .input-box select {
	            font-size: 16px;
	            padding: 10px 10px 10px 40px;
	        }
	
	        .input-box i {
	            font-size: 18px;
	        }
	
	        .payment-btn {
	            font-size: 14px;
	            padding: 8px 0;
	        }
	
	        .select-arrow {
	            font-size: 16px;
	        }
	    }
	
	    @media screen and (max-width: 480px) {
	        .form-title {
	            font-size: 24px;
	        }
	
	        .input-box input,
	        .input-box select {
	            font-size: 14px;
	            padding: 8px 8px 8px 35px;
	        }
	
	        .input-box i {
	            font-size: 16px;
	        }
	
	        .payment-btn {
	            font-size: 12px;
	            padding: 6px 0;
	        }
	
	        .select-arrow {
	            font-size: 14px;
	        }
	    }
    </style>
    
    <script>
		    function updateDestinations() {
		        const source = document.getElementById("source").value;
		        const destinationDropdown = document.getElementById("destination");

		        destinationDropdown.innerHTML = '<option value="" disabled selected>Select Destination</option>';

		        fetch(`/metro-ticket-backend/paymentCallback?type=updateDestinations&source=${source}`)
		        .then(response => {
		            if (!response.ok) {
		                throw new Error('Network response was not ok');
		            }
		            return response.json();
		        })
		        .then(data => {
		            console.log(data);  
		            if (data.error) {
		                alert(data.error);
		                return;
		            }

		            const destinationDropdown = document.getElementById("destination");
		            data.destinations.forEach(dest => {
		                const option = document.createElement("option");
		                option.value = dest;
		                option.textContent = dest;
		                destinationDropdown.appendChild(option);
		            });
		        })
		        .catch(error => {
		            console.error("Error updating destinations:", error);
		            alert(`Error: ${error.message}`);
		        });
		    }

		    function calculatePrice() {
		        const source = document.getElementById("source").value;
		        const destination = document.getElementById("destination").value;
		        const priceField = document.getElementById("price");
		
		        if (source && destination) {
		            fetch(`/metro-ticket-backend/paymentCallback?type=calculatePrice&source=${source}&destination=${destination}`)
		                .then(response => response.json())
		                .then(data => {
		                    if (data.error) {
		                        alert(data.error);
		                        return;
		                    }
		
		                    priceField.value = data.price;
		                })
		                .catch(error => console.error("Error calculating price:", error));
		        }
		    }
	</script>
	
    
</head>
<body>
    <form id="payment-form" class="payment-form" method="post" action="paymentCallback">
        <h1 class="form-title">Metro Ticket</h1>

        <!-- Passenger Name -->
        <div class="input-box">
            <i class='bx bxs-user'></i>
            <input type="text" id="passengerName" name="passengerName" placeholder="Passenger Name" value="<%= request.getAttribute("passengerName") != null ? request.getAttribute("passengerName") : "" %>" required autocomplete="off">
        </div>

        <!-- Source Dropdown -->
		<div class="input-box">
		    <i class='bx bx-map'></i>
		    <select id="source" name="source" class="select-dropdown" onchange="updateDestinations()" required>
		        <option value="" disabled <%= request.getAttribute("source") == null ? "selected" : "" %>>Select Source</option>
		        <% 
		            List<Station> stations = new StationService().getAllStations();
		            for (Station station : stations) {
		                String selected = station.getStation_name().equals(request.getAttribute("source")) ? "selected" : "";
		        %>
		                <option value="<%= station.getStation_name() %>" <%= selected %>><%= station.getStation_name() %></option>
		        <% } %>
		    </select>
		     <span class="select-arrow">▼</span>
		</div>
		
		<!-- Destination Dropdown -->
		<div class="input-box">
		    <i class='bx bx-map-pin'></i>
		    <select id="destination" name="destination" class="select-dropdown" onchange="calculatePrice()" required>
		       <option value="" disabled selected>Select Destination</option>
		    </select>
		     <span class="select-arrow">▼</span>
		</div>
		
		<!-- Price Field -->
		<div class="input-box">
		    <i class='bx bx-rupee'></i>
		    <input type="text" id="price" name="price" 
		           placeholder="Fare" 
		           value="<%= request.getAttribute("calculatedPrice") != null ? request.getAttribute("calculatedPrice") : "" %>" 
		           readonly>
		</div>
		
		<input type="hidden" id="razorpay_payment_id" name="razorpay_payment_id">
		<input type="hidden" id="razorpay_order_id" name="razorpay_order_id">
		<input type="hidden" id="razorpay_signature" name="razorpay_signature">
		

        <!-- Pay Now Button -->
        <button type="button" id="rzp-button" class="payment-btn" >
        Pay Now
    	</button>
    </form>
    
    <script src="https://checkout.razorpay.com/v1/checkout.js"></script>

	<script>
	    function fetchPaymentDetails() {
	        var source = document.getElementById("source").value;
	        var destination = document.getElementById("destination").value;
	        var passengerName = document.getElementById("passengerName").value;
	        var priceData = document.getElementById("price").value;

	
	        const url = `/metro-ticket-backend/paymentCallback?type=fetchPaymentDetails&source=${source}&destination=${destination}&price=${priceData}&passengerName=${passengerName}`;
	
	        fetch(url, {method:'GET'})
	            .then(response => response.json()) 
	            .then(data => {
	                if (data.error) {
	                    alert(data.error);
	                } else {
	  
	                    var orderId = data.orderId;
	                    var amount = data.amount;
	                    <% Properties properties = PropertiesManager.getInstance(); 
	                    	String API_KEY = properties.getProperty("API_KEY");
	                    %>
	
	                    var options = {
	                        "key": "<%=API_KEY%>",                       
	                        "amount": amount, 
	                        "currency": "INR",
	                        "name": "Metro Ticket System",
	                        "description": "Ticket Payment",
	                        "order_id": orderId,
	                        "handler": function (response) {
	                            document.getElementById("razorpay_payment_id").value = response.razorpay_payment_id;
	                            document.getElementById("razorpay_order_id").value = response.razorpay_order_id;
	                            document.getElementById("razorpay_signature").value = response.razorpay_signature;
	                            document.getElementById("payment-form").submit();
	                        },
	                        "prefill": {
	                            "name": passengerName, 
	                            "email": "<%= request.getAttribute("email") %>",       
	                            "contact": "<%= request.getAttribute("contact") %>" 
	                        },
	                        "theme": {
	                            "color": "#F37254"
	                        }
	                    };
	
	                    var rzp = new Razorpay(options);
	                    rzp.on('payment.failed', function(response) {
	                        alert(response.error.code);
	                        alert(response.error.description);
	                        alert(response.error.source);
	                        alert(response.error.step);
	                        alert(response.error.reason);
	                        alert(response.error.metadata.order_id);
	                        alert(response.error.metadata.payment_id);
	                    });
	
	                    rzp.open();
	                }
	            })
	            .catch(error => {
	                alert("Failed to fetch payment details: " + error);
	            });
	    }
	
	    document.getElementById('rzp-button').onclick = function(e) {
	        fetchPaymentDetails();
	        e.preventDefault(); 
	    };
	</script>




    	

</body>
</html>
