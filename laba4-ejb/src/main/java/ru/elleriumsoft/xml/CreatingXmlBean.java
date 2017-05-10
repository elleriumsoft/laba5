package ru.elleriumsoft.xml;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;


/**
 * Created by Dmitriy on 30.04.2017.
 */
public class CreatingXmlBean implements SessionBean
{
    private static final Logger logger = Logger.getLogger(CreatingXmlBean.class.getName());

    public void generateXml(Object data, String nameXml)
    {
        logger.info("generate xml");
        try {
            JAXBContext context = JAXBContext.newInstance(data.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            File file = new File("xml\\" + nameXml + ".xml");
            marshaller.marshal(data, file);
        } catch (JAXBException exception) {
            logger.info("JAXBException: " + exception);
        }
    }

    public boolean validateXml(String nameXml)
    {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("xsd\\" + nameXml + ".xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File("xml\\" + nameXml + ".xml")));
        } catch (IOException e){
            logger.info("Exception: "+e.getMessage());
            return false;
        }catch(SAXException e1){
            logger.info("SAX Exception: "+e1.getMessage());
            return false;
        }
        return true;
    }

    public String transformXmlToHtml(String xmlData)
    {
        String out = transformerToString("xml\\" + xmlData + ".xml",  "xslt\\" + xmlData + ".xsl");
        logger.info("out: " + out);
        return out;
    }

    public String transformerToString(String inFilename, String xslFilename)
    {
        StringWriter writer = new StringWriter();
        try
        {
            TransformerFactory factory = TransformerFactory.newInstance();
            Templates template = factory.newTemplates(new StreamSource(new FileInputStream(xslFilename)));

            Transformer former = template.newTransformer();
            Source source = new StreamSource(new FileInputStream(inFilename));
            writer = new StringWriter();
            Result result = new StreamResult(writer);
            former.transform(source, result);
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

    public void transformerToFile(String inFilename,  String outFilename, String xslFilename)
    {
        try
        {
            TransformerFactory factory = TransformerFactory.newInstance();
            Templates template = factory.newTemplates(new StreamSource(new FileInputStream(xslFilename)));

            Transformer former = template.newTransformer();
            Source source = new StreamSource(new FileInputStream(inFilename));
            Result result = new StreamResult(new FileOutputStream(outFilename));
            former.transform(source, result);
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
