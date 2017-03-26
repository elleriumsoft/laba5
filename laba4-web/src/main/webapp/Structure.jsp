<%@ page import="ru.elleriumsoft.printstructure.PrintStructure" %>
<%@ page import="ru.elleriumsoft.printstructure.PrintStructureHome" %>
<%@ page import="ru.elleriumsoft.structurecommands.CommandsForStructure" %>
<%@ page import="ru.elleriumsoft.structurecommands.CommandsForStructureHome" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.rmi.PortableRemoteObject" %>
<%@ page import="ru.elleriumsoft.actionforstucture.ActionForStructureHome" %>
<%@ page import="ru.elleriumsoft.actionforstucture.ActionForStructure" %>
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
    private PrintStructure printStructure = null;
    private CommandsForStructure commandsForStructure = null;
    private ActionForStructure actionForStructure = null;
    public void jspInit()
    {
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup("java:global/laba4-ear-1.0/laba4-ejb-1.0/PrintSturctureEJB");//"laba4-ejb/ru.elleriumsoft.structure.StructureProcessingFromDbHome");
            PrintStructureHome printStructureHome = (PrintStructureHome) PortableRemoteObject.narrow(remoteObject, PrintStructureHome.class);
            printStructure = printStructureHome.create();
            remoteObject = ic.lookup("java:global/laba4-ear-1.0/laba4-ejb-1.0/CommandsForStructureEJB");//"laba4-ejb/ru.elleriumsoft.structure.StructureProcessingFromDbHome");
            CommandsForStructureHome commandsForStructureHome = (CommandsForStructureHome) PortableRemoteObject.narrow(remoteObject, CommandsForStructureHome.class);
            commandsForStructure = commandsForStructureHome.create();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
%>

<%
    actionForStructure = (ActionForStructure) session.getAttribute("action");
    if (actionForStructure == null)
    {
        InitialContext ic = new InitialContext();
        Object remoteObject = ic.lookup("java:global/laba4-ear-1.0/laba4-ejb-1.0/ActionForStructureEJB");
        ActionForStructureHome actionForStructureHome = (ActionForStructureHome) PortableRemoteObject.narrow(remoteObject, ActionForStructureHome.class);
        actionForStructure = actionForStructureHome.create();
        session.setAttribute("action", actionForStructure);
    }
%>
<h1 style="color:#191970"><b>Структура мэрии</b></h1>

    <% actionForStructure.action(request.getParameter("newname"), printStructure.getMaxId()); %>

    <%= commandsForStructure.build(actionForStructure, request.getParameter("command"), request.getParameter("element"))%>
    <% session.setAttribute("action", actionForStructure); %>

    <br>

    <%= printStructure.printStructure(request.getParameter("printcommand"))%>

</body>
</html>
