<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="ru.elleriumsoft.finder.object.ObjectFinder" %>
<%@ page import="ru.elleriumsoft.finder.object.ObjectFinderHome" %>
<%@ page import="ru.elleriumsoft.occupation.object.ObjectOccupation" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT" %>
<%@ page import="ru.elleriumsoft.occupation.object.ObjectOccupationHome" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.rmi.PortableRemoteObject" %><%--
  Created by IntelliJ IDEA.
  User: Dmitriy
  Date: 23.04.2017
  Time: 16:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Поисковик</title>
</head>
<body>
<%!
    private static final Logger logger = Logger.getLogger("Finder.jsp");
    private ObjectFinder objectFinder = null;

    public void jspInit()
    {
        logger.info("Start Finder.jsp");
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "ObjectFinderEJB");
            ObjectFinderHome objectFinderHome = (ObjectFinderHome) PortableRemoteObject.narrow(remoteObject, ObjectFinderHome.class);
            objectFinder = objectFinderHome.create();
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
%>
<%
    ObjectOccupation objectOccupation = (ObjectOccupation) session.getAttribute("occupations");
    if (objectOccupation == null)
    {
        InitialContext ic = new InitialContext();
        Object remoteObject = ic.lookup(JNDI_ROOT + "ObjectOccupationEJB");
        ObjectOccupationHome objectOccupationHome = (ObjectOccupationHome) PortableRemoteObject.narrow(remoteObject, ObjectOccupationHome.class);
        objectOccupation = objectOccupationHome.create();
        session.setAttribute("occupations", objectOccupation);
    }
    //ObjectOccupation objectOccupation = (ObjectOccupation) session.getAttribute("occupations");
%>

<h1 style="color:#191970">
    <b>Поиск сотрудников в структуре мэрии</b>
</h1>
<a href="/app/StructureServlet"><img src="images/exit.png" width="33" height="33" align = "bottom" alt="Вернуться"></a>
<br>

<table border>
    <form name="find" method="get" action="Finder.jsp">

        <b>Введите имя полностью или его часть</b>
        <br>
        <input type="text" id="Editbox1" name="nameForFind" value=""  maxlength="125">

        <br><br>

        <b>Выберите должность для поиска</b><br>
        <select size="6" name="occForFind">
           <option disabled>Выберите должность</option>
           <%= objectOccupation.getHtmlCodeForSelectOption() %>
        </select>

        <br><br>

        <b>Выберите интервал дат рождения для поиска</b>
        <br>
        <input type="date" id="Editbox2" name="startDateForFind" value=""  maxlength="10">
        &nbsp-&nbsp
        <input type="date" id="Editbox3" name="endDateForFind" value=""  maxlength="10">

        <br><br>

        <input type="image" src="images/find.png" width="33" height="33">

    </form>
</table>
    <br>

<% if (request.getParameter("x") != null) {
    objectFinder.findByParameters(request.getParameter("nameForFind"), request.getParameter("occForFind"), request.getParameter("startDateForFind"), request.getParameter("endDateForFind"));%>

    Найдено записей = <%= objectFinder.size()%>
    <table border>
        <th>Отдел</th><th>Фамилия Имя Отчество</th> <th>Дата рождения</th> <th>Должность</th>
<%   for (int i = 0; i < objectFinder.size(); i++) { %>
        <tr>
            <td>&nbsp <a href="/app/department/Department.jsp?id=<%=objectFinder.getDatas(i).getIdDepartment()%>"> <%= objectFinder.getDatas(i).getNameDepartment() %> </a> &nbsp</td>
            <td>&nbsp <%= objectFinder.getDatas(i).getNameEmployee() %>   &nbsp</td>
            <td>&nbsp <%= objectFinder.getDatas(i).getEmploymentDate() %> &nbsp</td>
            <td>&nbsp <%= objectFinder.getDatas(i).getNameProfession() %> &nbsp</td>
        </tr>
<%    }
} %>

</body>
</html>
