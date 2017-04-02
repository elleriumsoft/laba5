<%@ page import="ru.elleriumsoft.actionforstucture.ActionForStructure" %>
<%@ page import="ru.elleriumsoft.actionforstucture.ActionForStructureHome" %>
<%@ page import="ru.elleriumsoft.printstructure.handlingofstates.HandlingOfStates" %>
<%@ page import="ru.elleriumsoft.printstructure.handlingofstates.HandlingOfStatesHome" %>
<%@ page import="ru.elleriumsoft.printstructure.printonscreen.PrintStructure" %>
<%@ page import="ru.elleriumsoft.printstructure.printonscreen.PrintStructureHome" %>
<%@ page import="ru.elleriumsoft.structurecommands.CommandsForStructure" %>
<%@ page import="ru.elleriumsoft.structurecommands.CommandsForStructureHome" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.PATH_APP" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.rmi.PortableRemoteObject" %>
<%@ page import="ru.elleriumsoft.printstructure.objectstructure.ObjectOfStructure" %>
<%@ page import="ru.elleriumsoft.printstructure.objectstructure.ObjectOfStructureHome" %>
<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="ru.elleriumsoft.structurecommands.commands.Commands" %>
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
</head>
<body>
<%!
    private static final Logger logger = Logger.getLogger("jsp");
    private PrintStructure printStructure = null;
    private CommandsForStructure commandsForStructure = null;
    private ActionForStructure actionForStructure = null;
    private ObjectOfStructure objectOfStructure = null;
    private HandlingOfStates handlingOfStates = null;
    public void jspInit()
    {
        logger.info("Start Structure.jsp");
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "PrintSturctureEJB");
            PrintStructureHome printStructureHome = (PrintStructureHome) PortableRemoteObject.narrow(remoteObject, PrintStructureHome.class);
            printStructure = printStructureHome.create();
            remoteObject = ic.lookup(JNDI_ROOT + "CommandsForStructureEJB");
            CommandsForStructureHome commandsForStructureHome = (CommandsForStructureHome) PortableRemoteObject.narrow(remoteObject, CommandsForStructureHome.class);
            commandsForStructure = commandsForStructureHome.create();
            remoteObject = ic.lookup(JNDI_ROOT + "HandlingOfStatesEJB");//"laba4-ejb/ru.elleriumsoft.structure.StructureProcessingFromDbHome");
            HandlingOfStatesHome handlingOfStatesHome = (HandlingOfStatesHome) PortableRemoteObject.narrow(remoteObject, HandlingOfStatesHome.class);
            handlingOfStates = handlingOfStatesHome.create();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
%>

<%
    actionForStructure = (ActionForStructure) session.getAttribute("action");
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
%>
<h1 style="color:#191970"><b>Структура мэрии</b></h1>

    <% objectOfStructure.initStructureFromDb(); %>

    <% if (request.getParameter("newname") != null) {actionForStructure.action(request.getParameter("newname"), objectOfStructure.getMaxId(), objectOfStructure);} %>

    <%= commandsForStructure.build(actionForStructure, request.getParameter("command"), request.getParameter("element"))%>
    <% session.setAttribute("action", actionForStructure); %>

    <br>

    <% if (handlingOfStates.checkNeedChangeState(request.getParameter("open"), objectOfStructure)) { response.sendRedirect(PATH_APP + "Structure.jsp"); } %>

    <%= printStructure.printStructure(request.getParameter("printcommand"), objectOfStructure)%>
    <% session.setAttribute("structure", objectOfStructure); %>

</body>
</html>
