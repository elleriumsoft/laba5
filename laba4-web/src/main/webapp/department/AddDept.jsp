<%@ page import="ru.elleriumsoft.occupation.object.ObjectOccupation" %>
<%@ page import="ru.elleriumsoft.department.object.ObjectDept" %>
<%@ page import="javax.persistence.criteria.CriteriaBuilder" %><%--
  Created by IntelliJ IDEA.
  User: Dmitriy
  Date: 17.04.2017
  Time: 22:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавить/Редактировать элемент</title>
</head>
<body>
<% ObjectOccupation objectOccupation = (ObjectOccupation) session.getAttribute("occupations");
   ObjectDept objectDept = (ObjectDept) session.getAttribute("dept");
%>

<% if (request.getParameter("id") != null) { %>
    <% if (request.getParameter("id").equals("new")) { %>

        <h1>Добавить сотрудника</h1>
        <table border="2">
        <form name="add" method="get" action="AddDept.jsp">
            <th>Имя сотрудника  <input type="text" id="Editbox1" name="AddNameLine" value=""  maxlength="125"></th>
            <th>Дата  рождения  <input type="date" id="Editbox2" name="AddDateLine" value=""  maxlength="10"></th>
            <th>Должность       <select size="2" required size = "1" name="occ"><option disabled>Выберите должность</option><%= objectOccupation.getHtmlCodeForSelectOption() %></select></th>
            <input type="submit" id="Button1" name="" value="Готово!">
        </form>
        </table>

    <% } else { %>

        <h1>Редактировать сотрудника</h1>
        <table border="2">
            <form name="edit" method="get" action="AddDept.jsp">
                <th>Имя сотрудника  <input type="text" id="Editbox3" name="AddNameLine" value="<%= objectDept.getNameEmployee(Integer.valueOf(request.getParameter("id")))%>" maxlength="125"></th>
                <th>Дата  рождения  <input type="date" id="Editbox4" name="AddDateLine" value="<%= objectDept.getDateForEdit(Integer.valueOf(request.getParameter("id")))%>"  maxlength="10"></th>
                <th>Должность       <select size="2" required size = "1" name="occ"><option disabled>Выберите должность</option><%= objectOccupation.getHtmlCodeForSelectOption() %></select></th>
                <input type="submit" id="Button2" name="" value="Готово!">
            </form>
        </table>

    <% } %>
<% } else { %>

<% } %>
</body>
</html>
