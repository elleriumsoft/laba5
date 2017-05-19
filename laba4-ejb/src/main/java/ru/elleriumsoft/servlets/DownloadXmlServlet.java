package ru.elleriumsoft.servlets;

import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * Created by Dmitriy on 15.05.2017.
 */
@WebServlet("/DownloadXmlServlet")
public class DownloadXmlServlet extends HttpServlet
{
    private static final Logger logger = Logger.getLogger(DownloadXmlServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition","attachment;filename=export.xml");
        try
        {
            File fileForRead = new File("xml\\export.xml");
            InputStream is = new FileInputStream(fileForRead);
            int read = 0;
            byte[] bytes = new byte[(int)fileForRead.length()];
            OutputStream os = response.getOutputStream();

            while ((read = is.read(bytes)) != -1)
            {
                os.write(bytes, 0, read);
            }
            os.flush();
            os.close();
        } catch (FileNotFoundException e)
        {
            logger.info("Файл export не найден");
            File file = new File("");
            logger.info("путь=" + file.getAbsolutePath());
        }
//        finally
//        {
//            response.sendRedirect("/app/StructureServlet");
//        }
    }

}
