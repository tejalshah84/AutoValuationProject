<!-- This form displays the automobile options list so that user can configure it.
It is constructed by data received from AutomobileFetch servlets doGet method -->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" type="text/css" href="style.css">
	<title>Configure Auto</title>
</head>
<body>
	<%@ page import="java.util.Properties" %>


	<h1><% out.println(" Configure Automobile Price: " + request.getAttribute("AutoName")); %></h1>
	<form class="form-style" action = "AutomobileFetch" method="post">
	 <% if(request.getAttribute("Error").equals("")){
	 	String[] osName = (String[]) request.getAttribute("OptionSet");
	 	out.println("<div class='field-block'><label class='label-style'>Model Name:</label>");
	 	out.println("<input class='input-syle' type=\"text\" value=\"" + request.getAttribute("AutoName") + "\"name=\"AutoName\" disabled /></div>");
		 	for(int i=0; i<osName.length; i++){
		 		out.println("<div class='field-block'><label class='label-style'>" + osName[i] + ":</label>");
		 		out.println("<select name=" + (osName[i]).replaceAll(" ","_") + "id=" + osName[i] + ">");
		 		Properties p = (Properties) request.getAttribute(osName[i]);
		 		for(String k: p.stringPropertyNames()){
					 out.println("<option value="+ k.replaceAll(" ", "_") +">"+ k +"</option>");
				}
		 		out.println("</select></div>");
		 	}
		}
	 else {
		 out.println("<p>"+request.getAttribute("Error")+"</P>");
	 }
	 %>
	<input class="button-style" type="submit" value = "Calculate Price" />
	</form>
</body>
</html>