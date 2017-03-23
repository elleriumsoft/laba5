<%@ page import="ru.elleriumsoft.printstructure.PrintStructureBean" %>
<%@ page import="ru.elleriumsoft.printstructure.PrintStructureBeanHome" %>
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
    <title>Testing</title>
</head>
<body>
<%!
    private PrintStructureBean printStructureBean = null;
    public void jspInit()
    {
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup("java:global/laba4-ear-1.0/laba4-ejb-1.0/PrintSturctureBeanEJB");//"laba4-ejb/ru.elleriumsoft.structure.StructureProcessingFromDbHome");
            PrintStructureBeanHome printStructureBeanHome = (PrintStructureBeanHome) PortableRemoteObject.narrow(remoteObject, PrintStructureBeanHome.class);
            printStructureBean = printStructureBeanHome.create();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
%>
<h1 style="color:#191970"><b>Структура мэрии</b></h1>

<% if (request.getParameter("command") == null) {%>
    <input type= "submit" id= "Button1 " onclick= "window.location.href='/Structure.jsp?command=add';return false; " name= " " value= "Добавить " style= "position:absolute;left:9px;top:51px;width:104px;height:25px;color:#FF0000; ">
    <input type= "submit" id= "Button2 " onclick= "window.location.href='/Structure.jsp?command=edit';return false; " name= " " value= "Редактировать " style= "position:absolute;left:121px;top:51px;width:104px;height:25px;color:#FF0000; ">
    <input type= "submit" id= "Button3 " onclick= "window.location.href='/Structure.jsp?command=delete';return false; " name= " " value= "Удалить " style= "position:absolute;left:235px;top:51px;width:104px;height:25px;color:#FF0000; ">
    <input type= "submit" id= "Button4 " onclick= "window.location.href='/Structure.jsp?open=all';return false; " name= " " value= "Открыть всё " style= "position:absolute;left:360px;top:51px;width:104px;height:25px;color:#16520a; ">
    <input type= "submit" id= "Button5 " onclick= "window.location.href='/Structure.jsp?close=all';return false; " name= " " value= "Закрыть всё " style= "position:absolute;left:474px;top:51px;width:104px;height:25px;color:#16520a; ">
    <input type= "submit" id= "Button5 " onclick= "window.location.href='/Structure.jsp?renew=1';return false; " name= " " value= "Обновить данные " style= "position:absolute;left:588px;top:51px;width:154px;height:25px;color:#16520a; ">
<% }
else {
    } %>

<br>
<input type="submit" id="Button1" onclick="window.location.href='/index.jsp';return false;" name="" value="Вернуться в меню" style="position:absolute;left:310px;top:18px;width:184px;height:25px;">

    <%= printStructureBean.printStructure("")%>

</body>
</html>
