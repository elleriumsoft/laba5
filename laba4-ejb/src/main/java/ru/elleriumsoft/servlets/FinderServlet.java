package ru.elleriumsoft.servlets;

import org.apache.log4j.Logger;
import ru.elleriumsoft.finder.object.ObjectFinder;
import ru.elleriumsoft.finder.object.ObjectFinderHome;
import ru.elleriumsoft.xml.creatingxml.CreatingXml;
import ru.elleriumsoft.xml.creatingxml.CreatingXmlHome;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 10.05.2017.
 */
@WebServlet("/FinderServlet")
public class FinderServlet extends HttpServlet
{
    private CreatingXml creatingXml;
    private ObjectFinder objectFinder;

    private static final Logger logger = Logger.getLogger(StructureServlet.class.getName());

    @Override
    public void init() throws ServletException
    {
        logger.info("Init Beans in FinderServlet");
        try {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "CreatingXmlEJB");
            CreatingXmlHome creatingXmlHome = (CreatingXmlHome) PortableRemoteObject.narrow(remoteObject, CreatingXmlHome.class);
            creatingXml = creatingXmlHome.create();

            remoteObject = ic.lookup(JNDI_ROOT + "ObjectFinderEJB");
            ObjectFinderHome objectFinderHome = (ObjectFinderHome) PortableRemoteObject.narrow(remoteObject, ObjectFinderHome.class);
            objectFinder = objectFinderHome.create();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        objectFinder.findByParameters(req.getParameter("nameForFind"), req.getParameter("occForFind"), req.getParameter("startDateForFind"), req.getParameter("endDateForFind"));
        logger.info("size=" + objectFinder.getStorageFinderData().getSizeFinderData());
        logger.info("real size=" + objectFinder.getStorageFinderData().getFinderDatas().size());

        creatingXml.generateXml(objectFinder.getStorageFinderData(), "finder");

        String htmlPage = creatingXml.transformXmlToHtml("finder");
        resp.setContentType("text/html;charset=utf-8");
        PrintWriter pw = resp.getWriter();
        pw.print(htmlPage);
        pw.close();
    }
}
