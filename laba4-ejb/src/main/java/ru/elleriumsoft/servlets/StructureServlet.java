package ru.elleriumsoft.servlets;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.StateOfElements;
import ru.elleriumsoft.structure.VariantsOfState;
import ru.elleriumsoft.structure.object.ObjectOfStructure;
import ru.elleriumsoft.structure.object.ObjectOfStructureHome;
import ru.elleriumsoft.xml.creatingxml.CreatingXml;
import ru.elleriumsoft.xml.creatingxml.CreatingXmlHome;
import ru.elleriumsoft.xml.exchange.exportxml.Export;
import ru.elleriumsoft.xml.exchange.exportxml.ExportHome;
import ru.elleriumsoft.xml.exchange.importxml.ImportXml;
import ru.elleriumsoft.xml.exchange.importxml.ImportXmlHome;

import javax.ejb.CreateException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

/**
 * Created by Dmitriy on 03.05.2017.
 */
@WebServlet("/StructureServlet")
@MultipartConfig
public class StructureServlet extends HttpServlet
{
    private final static String NAME_FOR_PROCESSING = "structure";
    private final static String FILE_NAME_FOR_IMPORT = "import";

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

        //Сохраняем команду и id над которым она выполняется
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
        creatingXml.generateXml(objectOfStructure.getObjectStructure(), NAME_FOR_PROCESSING);

        String htmlPage;
        if (creatingXml.validateXml(NAME_FOR_PROCESSING))
        {
             htmlPage = creatingXml.transformXmlToHtml(NAME_FOR_PROCESSING);
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
        objectOfStructure.setErrorOnImport("no");
        req.getSession().setAttribute(NAME_FOR_PROCESSING, objectOfStructure);
    }

    //Импорт файла приходит сюда
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException
    {
        logger.info("загрузка файла");

        Part uploadFile = request.getPart("xmlfile");
        logger.info("size for upload=" + uploadFile.getSize());

        if (uploadFile != null && uploadFile.getSize() > 0 && uploadFile.getInputStream() != null)
        {
            Path outputFile = Paths.get("xml\\" + FILE_NAME_FOR_IMPORT + ".xml");

            ReadableByteChannel input = Channels.newChannel(uploadFile.getInputStream());
            WritableByteChannel output = Channels.newChannel(new FileOutputStream(outputFile.toFile()));
            try
            {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (input.read(buffer) >= 0 || buffer.position() > 0) {
                    buffer.flip();
                    output.write(buffer);
                    buffer.compact();
                }
            }
            catch (Exception e)
            {
                objectOfStructure.setErrorOnImport("Ошибка загрузки файла!");
                resp.sendRedirect("/app/StructureServlet");
            }
            finally
            {
                input.close();
                output.close();
            }
            String resultImportXml = importXmlToDatabase(Boolean.valueOf(request.getParameter("withoverwrite")));
            objectOfStructure.setErrorOnImport(resultImportXml);
        }
        else
        {
            objectOfStructure.setErrorOnImport("Ошибка загрузки файла!");
        }
        resp.sendRedirect("/app/StructureServlet");
    }

    private String importXmlToDatabase(boolean withOverwrite) throws RemoteException
    {
        if (creatingXml.validateXml(FILE_NAME_FOR_IMPORT))
        {
            logger.info("xml validate OK");
            ImportXml importXml = getImportBean();
            importXml.importFromXmlToDatabase("xml\\" + FILE_NAME_FOR_IMPORT + ".xml", withOverwrite);

            if (importXml.isErrorOnImport())
            {
                return importXml.getTypeErrorImport();
            } else
            {
                objectOfStructure.setResultOfImport(importXml.getResultOfImport());
                openImportedElement(importXml);
                return "ok";
            }

        } else
        {
            logger.info("error xml validate");
            return "Файл не прошел проверку!";
        }
    }

    private void openImportedElement(ImportXml importXml) throws RemoteException
    {
        if (importXml.getFirstDept() != null)
        {
            StateOfElements stateForParentFirstDept = objectOfStructure.getStateOfElement(importXml.getFirstDept().getParentIdDept());
            if (stateForParentFirstDept != null && stateForParentFirstDept.getState() == VariantsOfState.OPEN)
            {
                objectOfStructure.addStateElement(importXml.getFirstDept().getIdDept(), VariantsOfState.CLOSE);
            }
        }
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

            creatingXml.generateXml(export.createExchangeForExportToXml(objectOfStructure.getIdForChangeByCommand()), "export");
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
            logger.info("naming error: " + e.getMessage());
        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        } catch (CreateException e)
        {
            logger.info("create error: " + e.getMessage());
        }
        return null;
    }

    private ImportXml getImportBean()
    {
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "ImportXmlEJB");
            ImportXmlHome importXmlHome = (ImportXmlHome) PortableRemoteObject.narrow(remoteObject, ImportXmlHome.class);
            return importXmlHome.create();
        } catch (NamingException e)
        {
            logger.info("naming error: " + e.getMessage());
        } catch (RemoteException e)
        {
            logger.info("remote error: " + e.getMessage());
        } catch (CreateException e)
        {
            logger.info("create error: " + e.getMessage());
        }
        return null;
    }

    private ObjectOfStructure getObjectOfStructure(HttpSession session)
    {
        ObjectOfStructure objectOfStructure = (ObjectOfStructure) session.getAttribute(NAME_FOR_PROCESSING);
        if (objectOfStructure == null)
        {
            InitialContext ic = null;
            try
            {
                ic = new InitialContext();
                Object remoteObject = ic.lookup(JNDI_ROOT + "ObjectOfStructureEJB");
                ObjectOfStructureHome objectOfStructureHome = (ObjectOfStructureHome) PortableRemoteObject.narrow(remoteObject, ObjectOfStructureHome.class);
                objectOfStructure = objectOfStructureHome.create();
                session.setAttribute(NAME_FOR_PROCESSING, objectOfStructure);
            } catch (NamingException e)
            {
                logger.info("naming error: " + e.getMessage());
            } catch (RemoteException e)
            {
                logger.info("remote error: " + e.getMessage());
            } catch (CreateException e)
            {
                logger.info("create error: " + e.getMessage());
            }
        }
        return objectOfStructure;
    }
}
