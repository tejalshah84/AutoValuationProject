<!-- This form displays the automobile models list so that user can choose one.
It is constructed by data received from CarModelList servlets doGet method -->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Automobile List</title>
	<link rel="stylesheet" type="text/css" href="style.css">
</head>

<body>
	<%@ page import="java.util.Properties" %>
	<%! Properties p;%>

	<h1>Select Automobile Model:</h1>
	<form action = "AutomobileFetch" method="get">
		<% out.println("<div  class='field-block'><label class='label-style'>Model Name:</label>");%>
		<select name="AutoList" id="AutoList">
		  <% p = (Properties) request.getAttribute("AutoList");
		  
		 	for(String k: p.stringPropertyNames()){
			 out.println("<option value="+ p.getProperty(k).replaceAll(" ","_") +">"+ p.getProperty(k) +"</option>");
		 	}
		  %>
		</select></div>
		<input class="button-style" type="submit" value = "Configure Automobile" >
	</form>
</body>
</html>