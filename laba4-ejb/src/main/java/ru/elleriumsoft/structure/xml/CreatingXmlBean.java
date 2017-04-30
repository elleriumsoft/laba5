package ru.elleriumsoft.structure.xml;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.objectstructure.ObjectOfStructure;
import ru.elleriumsoft.structure.objectstructure.Structure;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.rmi.RemoteException;

/**
 * Created by Dmitriy on 30.04.2017.
 */
public class CreatingXmlBean implements SessionBean
{
    private static final Logger logger = Logger.getLogger(CreatingXmlBean.class.getName());

    public void generateXml(Object data)
    {
        logger.info("generate xml");
        try {
            JAXBContext context = JAXBContext.newInstance(data.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File file = new File("xml\\structure.xml");
            marshaller.marshal(data, file);
        } catch (JAXBException exception) {
            logger.info("JAXBException: " + exception);
        }
    }

    public String transformXmlToHtml(String xmlData)
    {
        String out = transformer("xml\\" + xmlData + ".xml",  "xslt\\" + xmlData + ".xsl");
        logger.info("out: " + out);
        return out;
    }

    public String transformer(String inFilename, String xslFilename)
    {
        StringWriter writer = new StringWriter();
        try
        {
            TransformerFactory factory = TransformerFactory.newInstance();
            Templates template = factory.newTemplates(new StreamSource(new FileInputStream(xslFilename)));

            Transformer xformer = template.newTransformer();
            Source source = new StreamSource(new FileInputStream(inFilename));
            writer = new StringWriter();
            Result result = new StreamResult(writer);
                        xformer.transform(source, result);
        } catch (TransformerConfigurationException e)
        {
           logger.info("error in xslt " + e.getMessage()) ;
        } catch (TransformerException e)
        {
            logger.info("error transform " + e.getMessage());
        } catch (FileNotFoundException e)
        {
            logger.info("file not found " + e.getMessage());
        }
        return writer.toString();
    }


    public CreatingXmlBean()
    {
    }

    public void ejbCreate() throws CreateException
    {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException
    {
    }

    public void ejbRemove() throws EJBException
    {
    }

    public void ejbActivate() throws EJBException
    {
    }

    public void ejbPassivate() throws EJBException
    {
    }
}
