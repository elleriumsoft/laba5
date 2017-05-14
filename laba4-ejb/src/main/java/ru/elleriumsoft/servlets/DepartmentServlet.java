package ru.elleriumsoft.servlets;

import org.apache.log4j.Logger;
import ru.elleriumsoft.department.modification.ChangeItemDepartment;
import ru.elleriumsoft.department.modification.ChangeItemDepartmentHome;
import ru.elleriumsoft.department.object.ObjectDept;
import ru.elleriumsoft.department.object.ObjectDeptHome;
import ru.elleriumsoft.structure.object.ObjectOfStructure;
import ru.elleriumsoft.xml.creatingxml.CreatingXml;
import ru.elleriumsoft.xml.creatingxml.CreatingXmlHome;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 05.05.2017.
 */
@WebServlet("/DepartmentServlet")
public class DepartmentServlet extends HttpServlet
{
    private CreatingXml creatingXml;
    private ObjectDept objectDept;
    private static final Logger logger = Logger.getLogger(DepartmentServlet.class.getName());

    @Override
    public void init() throws ServletException
    {
        logger.info("Init Beans in DepartmentServlet");
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "CreatingXmlEJB");
            CreatingXmlHome creatingXmlHome = (CreatingXmlHome) PortableRemoteObject.narrow(remoteObject, CreatingXmlHome.class);
            creatingXml = creatingXmlHome.create();

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {

        if (req.getParameter("id") == null)
        //Модификации
        {
            objectDept = (ObjectDept) req.getSession().getAttribute("dept");
            if (objectDept != null)
            {
                modificationElement(req, resp);
                resp.sendRedirect("/app/DepartmentServlet?id=" + objectDept.getIdDepartment());
            }
        } else
            //Отображение
        {
            objectDept = initObjectDept(req, Integer.valueOf(req.getParameter("id")));
            if (req.getParameter("command") != null)
            {
                objectDept.setCommandForModification(req.getParameter("command"));
                if (!objectDept.getCommandForModification().equals("add"))
                {
                    objectDept.setIdForModification(Integer.valueOf(req.getParameter("idElement")));
                    objectDept.setPositionForModification(Integer.valueOf(req.getParameter("position")));
                }
            } else
            {
                objectDept.setCommandForModification("no");
                objectDept.setIdForModification(0);
                objectDept.setPositionForModification(0);
            }

            req.getSession().setAttribute("dept", objectDept);

            //Создаем из структуры xml
            creatingXml.generateXml(objectDept.getAllDept(), "dept");

            //Проверяем его на правильность и выводим его в виде html применив xslt
            String htmlPage;
            if (creatingXml.validateXml("dept"))
            {
                htmlPage = creatingXml.transformXmlToHtml("dept");
            }
            else
            {
                htmlPage = "Ошибка 638. Неверные данные. Обратитесь к администрации проекта.";
            }
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter pw = resp.getWriter();
            pw.print(htmlPage);
            pw.close();
        }
    }

    private void modificationElement(HttpServletRequest req, HttpServletResponse resp)
    {
            try
            {
                InitialContext ic = new InitialContext();
                Object remoteObject = ic.lookup(JNDI_ROOT + "ChangeItemDepartmentEJB");
                ChangeItemDepartmentHome changeItemDepartmentHome = (ChangeItemDepartmentHome) PortableRemoteObject.narrow(remoteObject, ChangeItemDepartmentHome.class);
                ChangeItemDepartment changeItemDepartment = changeItemDepartmentHome.create();
                switch (objectDept.getCommandForModification())
                {
                    case "add":
                        changeItemDepartment.changeItem(objectDept.getCommandForModification(), objectDept.getMaxId() + 1, objectDept.getIdDepartment(), req.getParameter("NewName"),
                            req.getParameter("NewDate"), req.getParameter("NewOcc"));
                        break;
                    case "edit":
                        changeItemDepartment.changeItem(objectDept.getCommandForModification(), objectDept.getIdForModification(), objectDept.getIdDepartment(), req.getParameter("NewName"),
                                req.getParameter("NewDate"), req.getParameter("NewOcc"));
                        break;
                    case "delete":
                        changeItemDepartment.changeItem(objectDept.getCommandForModification(), objectDept.getIdForModification(), objectDept.getIdDepartment(), "",
                                "", "");
                        break;
                }
            } catch (NamingException e)
            {
                e.printStackTrace();
            } catch (RemoteException e)
            {
                e.printStackTrace();
            } catch (CreateException e)
            {
                e.printStackTrace();
            }
    }

    private ObjectDept initObjectDept(HttpServletRequest req, Integer idDept)
    {
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "ObjectDeptEJB");
            ObjectDeptHome objectOfStructureHome = (ObjectDeptHome) PortableRemoteObject.narrow(remoteObject, ObjectDeptHome.class);
            ObjectDept objectDept = objectOfStructureHome.create();

            ObjectOfStructure objectOfStructure = (ObjectOfStructure) req.getSession().getAttribute("structure");
            objectDept.setNameDepartment(objectOfStructure.getNameDeptForSelectedId(idDept));

            objectDept.readAllEmployeeFromDept(idDept);
            return objectDept;
        } catch (RemoteException e)
        {
            e.printStackTrace();
        } catch (CreateException e)
        {
            e.printStackTrace();
        } catch (NamingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
