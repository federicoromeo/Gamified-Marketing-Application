<%--
  Created by IntelliJ IDEA.
  User: Francesco
  Date: 30/11/2020
  Time: 13:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns:th="http://www.thymeleaf.org">

<head>

    <meta charset="ISO-8859-1">
    <h1> Gamified Marketing Application </h1><br>
    <title> Gamified Marketing Application </title>
    <link rel="shortcut icon" type="image/x-icon" href=".\icon.ico" />

    <link rel="stylesheet" href="css/style.css" th:href="@{/css/style.css}" >
    <link href="https://fonts.googleapis.com/css2?family=Montserrat&display=swap" rel="stylesheet">

</head>

<body>

<form action="CheckLogin" method="POST" id="login-form">
    <h3>Login Form</h3><br>
    Username: <input type="text"     name="username" placeholder="Username:" required> <br>
    Password: <input type="password" name="password" placeholder="Password:" required>   <br>
    <input id="login-button"      type="submit" value="Login">
    <input id="show-registration" type="submit" value="Register">
    <p name="errormessage"  id="errormessage-login" class="errormessage"></p>
</form>

<form th:action="@{CheckRegistration}" method="POST" id="registration-form" class="masked">
    <h3>Registration Form</h3><br>
    Email: 	  <input type="email"    name="email"    placeholder="Email:"   id="email" required> <br>
    Username: <input type="text"     name="username" placeholder="Username:" required>           <br>
    Password: <input type="password" name="password" placeholder="Password:" required>           <br>
    <input id="register-button" type="button" value="Register">
    <input id="show-login" type="button" value="Back to login">
    <p name="errormessage" id="errormessage-registration" class="errormessage"></p>
</form>

</body>


</html>