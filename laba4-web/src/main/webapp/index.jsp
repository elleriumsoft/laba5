<%@ page import="javax.naming.InitialContext" %>
<%@ page import="java.io.File" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.PATH_STRUCTURE" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Лабораторная работа №4</title>
    </head>
    <body>
    <H1>Проект с J2EE для работы с иерархической базой данных</H1>
    <H2><a href="<%=PATH_STRUCTURE%>structure/Structure.jsp">Структура мэрии</a></H2>
    <H2><a href="department/Department.jsp">Тест депт</a></H2>
    </body>
</html>
