package ru.elleriumsoft.servlets;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.object.ObjectOfStructure;
import ru.elleriumsoft.structure.object.ObjectOfStructureHome;
import ru.elleriumsoft.xml.creatingxml.CreatingXml;
import ru.elleriumsoft.xml.creatingxml.CreatingXmlHome;
import ru.elleriumsoft.xml.exchange.export.Export;
import ru.elleriumsoft.xml.exchange.export.ExportHome;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 03.05.2017.
 */
@WebServlet("/StructureServlet")
public class StructureServlet extends HttpServlet
{
    private ObjectOfStructure objectOfStructure;
    private CreatingXml creatingXml;

    private static final Logger logger = Logger.getLogger(StructureServlet.class.getName());

    @Override
    public void init() throws ServletException
    {
        logger.info("Init Beans in StructureServlet");
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
        if (req.getParameter("export") != null)
        {
            exportToXml(req);
            req.getRequestDispatcher("DownloadXmlServlet").forward(req, resp);
        }

        //Инициализируем структуру из бд
        objectOfStructure = getObjectOfStructure(req.getSession());
        objectOfStructure.initStructureFromDb();

        //Модифицируем структуру при необходимости
        if (req.getParameter("newname") != null)
        {
            objectOfStructure.modificationStructure(req.getParameter("newname"));
            resp.sendRedirect("StructureServlet");
        }

        //Открываем/закрываем элементы
        objectOfStructure.changeStateOfElementStructure(req.getParameter("open"));
        if (objectOfStructure.checkNeedUpdatePage()) { resp.sendRedirect("StructureServlet"); }

        //Сохраняем команду модифицирующую струкутуру и id над которым она выполняется
        if (req.getParameter("command") != null && req.getParameter("element") != null)
        {
            objectOfStructure.setCommandForChangeStructure(req.getParameter("command"));
            objectOfStructure.setIdForChangeByCommand(Integer.valueOf(req.getParameter("element")));
        }
        else
        {
            objectOfStructure.setCommandForChangeStructure("no");
        }

        //Создаем из структуры xml и выводим его в виде html применив xslt
        creatingXml.generateXml(objectOfStructure.getObjectStructure(), "structure");

        String htmlPage;
        if (creatingXml.validateXml("structure"))
        {
             htmlPage = creatingXml.transformXmlToHtml("structure");
        }
        else
        {
            htmlPage = "Ошибка 638. Неверные данные. Обратитесь к администрации проекта.";
        }
            resp.setContentType("text/html;charset=utf-8");
            PrintWriter pw = resp.getWriter();
            pw.print(htmlPage);
            pw.close();

        //сохраняем модифицированную структуру
        req.getSession().setAttribute("structure", objectOfStructure);
    }

    private void exportToXml(HttpServletRequest req) throws RemoteException
    {

            Export export = getExportBean();
            if (req.getParameter("withchildren") != null)
            {
                export.setWithChildrenDept(true);
            }
            else
            {
                export.setWithChildrenDept(false);
            }
            if (req.getParameter("withemployees") != null)
            {
                export.setWithEmployees(true);
            }
            else
            {
                export.setWithEmployees(false);
            }
            if (req.getParameter("withocc") != null)
            {
                export.setWithOccupations(true);
            }
            else
            {
                export.setWithOccupations(false);
            }

            export.exportToXml(objectOfStructure.getIdForChangeByCommand());
            creatingXml.generateXml(export.getExchange(), "export");

//            logger.info("size deps=" + export.getExchange().getDepartments().size());
//            for (DeptInfo dept : export.getExchange().getDepartments())
//            {
//                logger.info("dept №" + dept.getIdDept() + ": " + dept.getNameDept());
//            }

    }

    private Export getExportBean()
    {
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "ExportEJB");
            ExportHome exportHome = (ExportHome) PortableRemoteObject.narrow(remoteObject, ExportHome.class);
            return exportHome.create();
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
        return null;
    }

    private ObjectOfStructure getObjectOfStructure(HttpSession session)
    {
        ObjectOfStructure objectOfStructure = (ObjectOfStructure) session.getAttribute("structure");
        if (objectOfStructure == null)
        {
            InitialContext ic = null;
            try
            {
                ic = new InitialContext();
                Object remoteObject = ic.lookup(JNDI_ROOT + "ObjectOfStructureEJB");
                ObjectOfStructureHome objectOfStructureHome = (ObjectOfStructureHome) PortableRemoteObject.narrow(remoteObject, ObjectOfStructureHome.class);
                objectOfStructure = objectOfStructureHome.create();
                session.setAttribute("structure", objectOfStructure);
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
        return objectOfStructure;
    }
}
