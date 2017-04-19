<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="ru.elleriumsoft.department.action.ChangeItemDepartment" %>
<%@ page import="ru.elleriumsoft.department.action.ChangeItemDepartmentHome" %>
<%@ page import="ru.elleriumsoft.department.object.ObjectDept" %>
<%@ page import="javax.ejb.CreateException" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="javax.rmi.PortableRemoteObject" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT" %>
<%@ page import="java.rmi.RemoteException" %><%--
  Created by IntelliJ IDEA.
  User: Dmitriy
  Date: 19.04.2017
  Time: 23:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Удаление</title>
</head>
<body>
<%!
    ChangeItemDepartment changeItemDepartment = null;
    private static final Logger logger = Logger.getLogger("DeleteDept.jsp");

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
<%  ObjectDept objectDept = (ObjectDept) session.getAttribute("dept"); %>

<% if (request.getParameter("confirm") == null) {
    session.setAttribute("idForAction", objectDept.getId(Integer.valueOf(request.getParameter("id"))));%>
    <h2>Подтвердите удаление сотрудника <%= objectDept.getNameEmployee(Integer.valueOf(request.getParameter("id"))) %></h2>
    <input type="submit" id="Button1" onclick="window.location.href='DeleteDept.jsp?confirm=yes';return false;" name="" value="Да, удалить" style="position:absolute;left:10px;top:50px;width:184px;height:25px;">
    <input type="submit" id="Button2" onclick="window.location.href='DeleteDept.jsp?confirm=no';return false;" name="" value="Нет, не нужно" style="position:absolute;left:200px;top:50px;width:184px;height:25px;">
<% } else {
        if (request.getParameter("confirm").equals("yes"))
        {
            changeItemDepartment.changeItem("delete", (Integer) session.getAttribute("idForAction"), 0, "", "", 0);
        }

        response.sendRedirect("/app/department/Department.jsp?id=" + objectDept.getIdDepartment());
   } %>
</body>
</html>
