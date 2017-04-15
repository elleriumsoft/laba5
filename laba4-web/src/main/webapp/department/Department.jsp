<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="ru.elleriumsoft.department.entity.EntityDeptHome" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.rmi.PortableRemoteObject" %>
<%@ page import="ru.elleriumsoft.department.entity.EntityDept" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="javax.ejb.FinderException" %>
<%@ page import="java.util.Collection" %>
<%@ page import="ru.elleriumsoft.department.object.ObjectDept" %>
<%@ page import="ru.elleriumsoft.department.object.ObjectDeptHome" %>
<%@ page import="ru.elleriumsoft.department.object.Department" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Сотрудники элемента структуры</title>
</head>
<body>
<%!
    private static final Logger logger = Logger.getLogger("jsp");
    private ObjectDept objectDept = null;

    public void jspInit()
    {
        logger.info("Start Department.jsp");
    }
%>
<%
    objectDept = (ObjectDept) session.getAttribute("dept");
    if (objectDept == null || objectDept.getIdDepartment() != Integer.valueOf(request.getParameter("id")))
    {
        InitialContext ic = new InitialContext();
        Object remoteObject = ic.lookup(JNDI_ROOT + "ObjectDeptEJB");
        ObjectDeptHome objectOfStructureHome = (ObjectDeptHome) PortableRemoteObject.narrow(remoteObject, ObjectDeptHome.class);
        objectDept = objectOfStructureHome.create();
        objectDept.readAllEmployeeFromDept(Integer.valueOf(request.getParameter("id")));
        session.setAttribute("dept", objectDept);
    }
%>

    <table border>
    <th>Фамилия Имя Отчество</th> <th>Дата рождения</th> <th>Должность</th>
    <% for (int i = 0; i < objectDept.getSizeObject(); i++) { %>
        <tr>
        <td>&nbsp <%= objectDept.getNameEmployee(i) %>    &nbsp</td>
        <td>&nbsp <%= objectDept.getEmploymentDate(i) %>  &nbsp</td>
        <td>&nbsp <%= objectDept.getProfession(i) %>      &nbsp</td>
        </tr>
    <% } %>

</body>
</html>
