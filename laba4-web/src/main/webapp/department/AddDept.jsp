<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="ru.elleriumsoft.department.action.ChangeItemDepartment" %>
<%@ page import="ru.elleriumsoft.department.action.ChangeItemDepartmentHome" %>
<%@ page import="ru.elleriumsoft.department.object.ObjectDept" %>
<%@ page import="ru.elleriumsoft.occupation.object.ObjectOccupation" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="javax.rmi.PortableRemoteObject" %>
<%@ page import="java.rmi.RemoteException" %><%--
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
<%!
    ChangeItemDepartment changeItemDepartment = null;
    private static final Logger logger = Logger.getLogger("AddDept.jsp");

    public void jspInit()
    {
        InitialContext ic = null;
        try
        {
            ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "ChangeItemDepartmentEJB");
            ChangeItemDepartmentHome changeItemDepartmentHome = (ChangeItemDepartmentHome) PortableRemoteObject.narrow(remoteObject, ChangeItemDepartmentHome.class);
            changeItemDepartment = changeItemDepartmentHome.create();
        } catch (NamingException e)
        {
            e.printStackTrace();
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (CreateException e)
        {
            e.printStackTrace();
        }
    }
%>
<%

    ObjectOccupation objectOccupation = (ObjectOccupation) session.getAttribute("occupations");
    ObjectDept objectDept = (ObjectDept) session.getAttribute("dept");
    logger.info("name dept=" + objectDept.getNameDepartment());
%>

<% if (request.getParameter("id") != null) { %>
    <% if (request.getParameter("id").equals("new")) { %>

        <h1>Добавить сотрудника</h1>
        <table border="2">
        <form name="add" method="get" action="AddDept.jsp">
            <th>Имя сотрудника  <input type="text" id="Editbox1" name="name" value=""  maxlength="125"></th>
            <th>Дата  рождения  <input type="date" id="Editbox2" name="date" value=""  maxlength="10"></th>
            <th>Должность       <select size="2" required size = "1" name="occ"><option disabled>Выберите должность</option><%= objectOccupation.getHtmlCodeForSelectOption() %></select></th>
            <input type="submit" id="Button1" name="" value="Готово!">
        </form>
        </table>
        <%
            session.setAttribute("action", "add");
            session.setAttribute("idForAction", objectDept.getMaxId()+1);
        %>

    <% } else { %>

        <h1>Редактировать сотрудника</h1>
        <table border="2">
            <form name="edit" method="get" action="AddDept.jsp">
                <th>Имя сотрудника  <input type="text" id="Editbox3" name="name" value="<%= objectDept.getNameEmployee(Integer.valueOf(request.getParameter("id")))%>" maxlength="125"></th>
                <th>Дата  рождения  <input type="date" id="Editbox4" name="date" value="<%= objectDept.getDateForEdit(Integer.valueOf(request.getParameter("id")))%>"  maxlength="10"></th>
                <th>Должность       <select size="2" required size = "1" name="occ"><option disabled>Выберите должность</option><%= objectOccupation.getHtmlCodeForSelectOptionWithSelection(Integer.valueOf(request.getParameter("id"))) %></select></th>
                <input type="submit" id="Button2" name="" value="Готово!">
            </form>
        </table>
        <%
            session.setAttribute("action", "edit");
            session.setAttribute("idForAction", objectDept.getId(Integer.valueOf(request.getParameter("id"))));
        %>

    <% } %>
<% } else
{
    changeItemDepartment.changeItem((String) session.getAttribute("action"), (Integer) session.getAttribute("idForAction"), objectDept.getIdDepartment(), request.getParameter("name"), request.getParameter("date"), Integer.valueOf(request.getParameter("occ")));
    session.setAttribute("dept", objectDept);
    response.sendRedirect("/app/department/Department.jsp?id=" + objectDept.getIdDepartment());
} %>
</body>
</html>
