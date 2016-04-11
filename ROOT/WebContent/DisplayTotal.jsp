<!-- This form displays the estimated price of selected automobile by the user.
It is constructed by data received from AutomobileFetch servlets doPost methods response -->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Automobile Total Price</title>
	<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
<%@ page import="java.util.Properties" %>


	<h1>Estimated Automobile Price:</h1>
	 <% 
	 if(request.getAttribute("Error").equals("")){
	 out.println("<table><tr><td>" + request.getAttribute("AutoName")  +"</td><td>Base Price</td><td>" + request.getAttribute("BasePrice") + "</td>");
	 	Properties p = (Properties) request.getAttribute("Choices");
	 	Properties p1 = (Properties) request.getAttribute("Prices");
	 	if(p!=null){
	 	for(String k: p.stringPropertyNames()){
	 		out.println("<tr><td>" + k + "</td><td> "+ p.getProperty(k)+ "</td><td>" + p1.getProperty(p.getProperty(k)) + "</td></tr>");
	 	}
	 	}
	 	out.println("<tr class='result'><td>Total Cost</td><td></td><td>"  + request.getAttribute("Total Cost") + "</td></tr></table>");
	 }
	 else {
		 out.println("<p>"+request.getAttribute("Error")+"</P>");
	 }
	 %>
	<a class="link-style" href='CarModelList'>View Automodel List</a>

</body>
</html>