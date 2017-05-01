<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="ru.elleriumsoft.CreateButtons" %>
<%@ page import="ru.elleriumsoft.occupation.object.ObjectOccupation" %>
<%@ page import="ru.elleriumsoft.occupation.object.ObjectOccupationHome" %>
<%@ page import="ru.elleriumsoft.structure.modifications.ActionForStructure" %>
<%@ page import="ru.elleriumsoft.structure.modifications.ActionForStructureHome" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.PATH_STRUCTURE" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT" %>
<%@ page import="ru.elleriumsoft.structure.objectstructure.ObjectOfStructure" %>
<%@ page import="ru.elleriumsoft.structure.objectstructure.ObjectOfStructureHome" %>
<%@ page import="ru.elleriumsoft.structure.print.handlingofstates.HandlingOfStates" %>
<%@ page import="ru.elleriumsoft.structure.print.handlingofstates.HandlingOfStatesHome" %>
<%@ page import="ru.elleriumsoft.structure.print.printonscreen.PrintStructure" %>
<%@ page import="ru.elleriumsoft.structure.print.printonscreen.PrintStructureHome" %>
<%@ page import="ru.elleriumsoft.structure.xml.CreatingXml" %>
<%@ page import="ru.elleriumsoft.structure.xml.CreatingXmlHome" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.rmi.PortableRemoteObject" %>
<%--
  Created by IntelliJ IDEA.
  User: Dmitriy
  Date: 19.03.2017
  Time: 0:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Структура мэрии</title>
    <link rel="import" href="/app/structure/buttons.html">
</head>
<body>
<%!
    private static final Logger logger = Logger.getLogger("jsp");
    private PrintStructure printStructure = null;
    private ActionForStructure actionForStructure = null;
    private ObjectOfStructure objectOfStructure = null;
    private HandlingOfStates handlingOfStates = null;
    private ObjectOccupation objectOccupation = null;
    private CreatingXml creatingXml = null;

    public void jspInit()
    {
        logger.info("Start Structure.jsp");
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "PrintSturctureEJB");
            PrintStructureHome printStructureHome = (PrintStructureHome) PortableRemoteObject.narrow(remoteObject, PrintStructureHome.class);
            printStructure = printStructureHome.create();

            remoteObject = ic.lookup(JNDI_ROOT + "HandlingOfStatesEJB");
            HandlingOfStatesHome handlingOfStatesHome = (HandlingOfStatesHome) PortableRemoteObject.narrow(remoteObject, HandlingOfStatesHome.class);
            handlingOfStates = handlingOfStatesHome.create();

            remoteObject = ic.lookup(JNDI_ROOT + "CreatingXmlEJB");
            CreatingXmlHome creatingXmlHome = (CreatingXmlHome) PortableRemoteObject.narrow(remoteObject, CreatingXmlHome.class);
            creatingXml = creatingXmlHome.create();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
%>

<%
    actionForStructure = (ActionForStructure) session.getAttribute("actionForStucture");
    if (actionForStructure == null)
    {
        InitialContext ic = new InitialContext();
        Object remoteObject = ic.lookup(JNDI_ROOT + "ActionForStructureEJB");
        ActionForStructureHome actionForStructureHome = (ActionForStructureHome) PortableRemoteObject.narrow(remoteObject, ActionForStructureHome.class);
        actionForStructure = actionForStructureHome.create();
        session.setAttribute("action", actionForStructure);
    }
    objectOfStructure = (ObjectOfStructure) session.getAttribute("structure");
    if (objectOfStructure == null)
    {
        InitialContext ic = new InitialContext();
        Object remoteObject = ic.lookup(JNDI_ROOT + "ObjectOfStructureEJB");
        ObjectOfStructureHome objectOfStructureHome = (ObjectOfStructureHome) PortableRemoteObject.narrow(remoteObject, ObjectOfStructureHome.class);
        objectOfStructure = objectOfStructureHome.create();
        session.setAttribute("structure", objectOfStructure);
    }
    objectOccupation = (ObjectOccupation) session.getAttribute("occupations");
    if (objectOccupation == null)
    {
        InitialContext ic = new InitialContext();
        Object remoteObject = ic.lookup(JNDI_ROOT + "ObjectOccupationEJB");
        ObjectOccupationHome objectOccupationHome = (ObjectOccupationHome) PortableRemoteObject.narrow(remoteObject, ObjectOccupationHome.class);
        objectOccupation = objectOccupationHome.create();
        session.setAttribute("occupations", objectOccupation);
    }
%>
    <h1 style="color:#191970">
        <b>Структура мэрии</b>
        &nbsp&nbsp<a href="/app/finder/Finder.jsp"><img src="images/find.png" width="33" height="33" align = "center" alt="Поиск"></a>
    </h1>

    <%
        objectOfStructure.initStructureFromDb();
        creatingXml.generateXml(objectOfStructure.getObjectStructure(), "structure");
    %>

    <% if (request.getParameter("newname") != null) {actionForStructure.action(request.getParameter("newname"), objectOfStructure.getMaxId(), objectOfStructure);} %>

    <script>
        var link = document.querySelector('link[rel=import]');
        var content = link.import.querySelector(<%= new CreateButtons().selectButtons(actionForStructure, request.getParameter("command"), request.getParameter("element")) %>);
        document.body.appendChild(content.cloneNode(true));
    </script>
    <% session.setAttribute("actionForStucture", actionForStructure); %>

    <br>

    <% if (handlingOfStates.checkNeedChangeState(request.getParameter("open"), objectOfStructure)) { response.sendRedirect(PATH_STRUCTURE + "Structure.jsp"); } %>

    <%= printStructure.printStructure(objectOfStructure)%>
    <% session.setAttribute("structure", objectOfStructure); %>

    <br><br>

    <%= creatingXml.transformXmlToHtml("structure") %>
</body>
</html>
