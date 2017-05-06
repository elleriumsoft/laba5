<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="ru.elleriumsoft.department.object.ObjectDept" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT" %>
<%@ page import="ru.elleriumsoft.department.object.ObjectDeptHome" %>
<%@ page import="ru.elleriumsoft.occupation.object.ObjectOccupation" %>
<%@ page import="ru.elleriumsoft.occupation.object.ObjectOccupationHome" %>
<%@ page import="ru.elleriumsoft.structure.object.ObjectOfStructure" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.rmi.PortableRemoteObject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Сотрудники элемента структуры</title>
</head>
<body>
<%!
    private static final Logger logger = Logger.getLogger("Department.jsp");
    private ObjectDept objectDept = null;

    public void jspInit()
    {
        logger.info("Start Department.jsp");
    }
%>
<%
    ObjectOfStructure objectOfStructure = (ObjectOfStructure) session.getAttribute("structure");
    objectOfStructure.setSelectedId(Integer.valueOf(request.getParameter("id")));

        logger.info("new objectDept create");
        InitialContext ic = new InitialContext();
        Object remoteObject = ic.lookup(JNDI_ROOT + "ObjectDeptEJB");
        ObjectDeptHome objectOfStructureHome = (ObjectDeptHome) PortableRemoteObject.narrow(remoteObject, ObjectDeptHome.class);
        objectDept = objectOfStructureHome.create();
        objectDept.readAllEmployeeFromDept(objectOfStructure.getSelectedId());
        session.setAttribute("dept", objectDept);
%>
    <h1> <%= objectOfStructure.getNameDeptForSelectedId() %> </h1>

    <a href="/app/StructureServlet"><img src="images/exit.png" width="33" height="33" align = "bottom" alt="Вернуться"></a>
    &nbsp&nbsp
    <a href="AddDept.jsp?id=new"><img src="images/create.png" width="33" height="33" align = "bottom" alt="Добавить элемент"></a>
    <br>

    <table border>
    <th>Фамилия Имя Отчество</th> <th>Дата рождения</th> <th>Должность</th>

    <% for (int i = 0; i < objectDept.getSizeObject(); i++) { %>
        <tr>
        <td>&nbsp <%= objectDept.getNameEmployee(i) %>    &nbsp</td>
        <td>&nbsp <%= objectDept.getEmploymentDate(i) %>  &nbsp</td>
        <td>&nbsp <%= objectDept.getProfession(i) %>      &nbsp</td>

        <td>&nbsp <a href="AddDept.jsp?id=<%= i %>"><img src="images/edit.png" width="16" height="16" align = "center" alt="Редактировать элемент"></a> &nbsp</td>
        <td>&nbsp <a href="DeleteDept.jsp?id=<%= i %>"><img src="images/delete.png" width="16" height="16" align = "center" alt="Удалить элемент"></a> &nbsp</td>
        </tr>
    <% } %>
    </table>

</body>
</html>
