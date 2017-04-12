<%@ page import="org.apache.log4j.Logger" %>
<%@ page import="ru.elleriumsoft.department.entity.EntityDeptHome" %>
<%@ page import="static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.rmi.PortableRemoteObject" %>
<%@ page import="ru.elleriumsoft.department.entity.EntityDept" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="javax.ejb.FinderException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Сотрудники элемента структуры</title>
</head>
<body>
<%!
    private static final Logger logger = Logger.getLogger("jsp");
    EntityDept entityDept = null;

    public void jspInit()
    {
        logger.info("Start Department.jsp");

        InitialContext ic = null;
        try
        {
            ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "EntityDeptEJB");
            EntityDeptHome entityDeptHome = (EntityDeptHome) PortableRemoteObject.narrow(remoteObject, EntityDeptHome.class);
            entityDept = entityDeptHome.findByPrimaryKey(4);
        } catch (NamingException e)
        {
            logger.info(e.getMessage());
            e.printStackTrace();
        } catch (RemoteException e)
        {
            logger.info(e.getMessage());
            e.printStackTrace();
        } catch (FinderException e)
        {
            logger.info(e.getMessage());
            e.printStackTrace();
        }

    }
%>

<%= entityDept.getNameOccupation() %>

</body>
</html>
