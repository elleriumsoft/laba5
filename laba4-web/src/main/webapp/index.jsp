<%@ page import="ru.elleriumsoft.TestBean.Structure" %>
<%@ page import="ru.elleriumsoft.TestBean.StructureHome" %>
<%@ page import="javax.naming.InitialContext" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%!
            Structure bean;
            public void jspInit() {
                try {
                    InitialContext ctx = new InitialContext();
                    bean = ((StructureHome)
                            ctx.lookup(
                                    "java:global/laba4-ear-1.0/laba4-ejb-1.0/StructureEJB!ru.elleriumsoft.TestBean.StructureHome")
                    ).create();
                } catch (Exception e) {
                    System.err.println(e);
                }

            }
        %>
        <%=bean.say("Ура")%>
    </body>
</html>
