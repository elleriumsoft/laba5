<%@ page import="ru.elleriumsoft.structure.StructureProcessingFromDb" %>
<%@ page import="ru.elleriumsoft.structure.StructureProcessingFromDbHome" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="javax.rmi.PortableRemoteObject" %><%--
  Created by IntelliJ IDEA.
  User: Dmitriy
  Date: 19.03.2017
  Time: 0:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Testing</title>
</head>
<body>
<%!
    private StructureProcessingFromDbHome structure = null;
    public void jspInit()
    {
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup("java:global/laba4-ear-1.0/laba4-ejb-1.0/StructureProcessingFromDbEJB");//"laba4-ejb/ru.elleriumsoft.structure.StructureProcessingFromDbHome");
            structure = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
%>
<%
    StructureProcessingFromDb element = null;
    try
    {
         element = structure.findByPrimaryKey("2");
    }
    catch (Exception e)
    {
        System.out.println(e.getMessage());
    }
    if (element != null)
    {
%>
    <%=element.getNameDepartment()%>
<%}%>
</body>
</html>
