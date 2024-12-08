<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Styled Login Form</title>
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

        .login-form {
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

        .login-title {
            font-size: 40px;
            margin-bottom: 40px;
        }

        .input-box {
            margin: 20px 0;
            position: relative;
        }

        .input-box input {
            width: 100%;
            background: rgba(255, 255, 255, 0.1);
            border: none;
            padding: 12px 12px 12px 45px;
            border-radius: 99px;
            outline: 3px solid transparent;
            transition: 0.3s;
            font-size: 17px;
            color: white;
            font-weight: 600;
        }

        .input-box input::placeholder {
            color: rgba(255, 255, 255, 0.8);
            font-size: 17px;
            font-weight: 500;
        }

        .input-box input:focus {
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

        .login-btn {
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

        .login-btn:hover {
            background: #0B87EC;
        }
        
        .input-box input:active {
		    background: rgba(255, 255, 255, 0.15);
		    color: white;
		}
		
		@media screen and (max-width: 768px) {
	        .login-form {
	            width: 90%;
	            padding: 20px;
	            border-radius: 12px;
	        }
	
	        .login-title {
	            font-size: 30px;
	            margin-bottom: 20px;
	        }
	
	        .input-box input {
	            font-size: 16px;
	            padding: 10px 10px 10px 40px;
	        }
	
	        .input-box i {
	            font-size: 18px;
	        }
	
	        .login-btn {
	            font-size: 14px;
	            padding: 8px 0;
	        }
	    }
	
	    @media screen and (max-width: 480px) {
	        .login-title {
	            font-size: 24px;
	        }
	
	        .input-box input {
	            font-size: 14px;
	            padding: 8px 8px 8px 35px;
	        }
	
	        .input-box i {
	            font-size: 16px;
	        }
	
	        .login-btn {
	            font-size: 12px;
	            padding: 6px 0;
	        }
    }
    </style>
</head>
<body>
    <form class="login-form" method="post" action="login">
        <h1 class="login-title">Login</h1>

        <!-- Name Field -->
        <div class="input-box">
            <i class='bx bxs-user'></i>
            <input type="text" id="name" name="name" placeholder="Enter your name" autocomplete="off">
        </div>

        <!-- Email Field -->
        <div class="input-box">
            <i class='bx bxs-envelope'></i>
            <input type="email" id="email" name="email" placeholder="Enter your email" required autocomplete="off">
        </div>

        <!-- Phone Number Field -->
        <div class="input-box">
            <i class='bx bxs-phone'></i>
            <input type="text" id="phoneNumber" name="phoneNumber" placeholder="Enter your phone number" required autocomplete="off">
        </div>

        <!-- Submit Button -->
        <button class="login-btn" type="submit">Save</button>
    </form>
</body>
</html>
