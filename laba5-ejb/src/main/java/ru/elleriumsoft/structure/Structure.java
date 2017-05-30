package ru.elleriumsoft.structure;

import org.apache.log4j.Logger;
import ru.elleriumsoft.structure.entity.StructureProcessingFromDbHome;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;

import static ru.elleriumsoft.jdbc.ConnectToDb.JNDI_ROOT;

@XmlType(propOrder = { "errorOnImport", "resultOfImport", "commandForChangeStructure", "elementIdForChange", "structureForPrint" }, name = "structure")//@XmlType(propOrder = { "statesOfElements", "structureForPrint" }, name = "structure")
@XmlRootElement
public class Structure implements Serializable
{
    private String errorOnImport;
    private String resultOfImport;
    private String commandForChangeStructure;
    private Integer elementIdForChange;
    private ArrayList<StructureElement> structureForPrint;

    private static final Logger logger = Logger.getLogger(Structure.class.getName());

    public ArrayList<StructureElement> getStructureForPrint()
    {
        return structureForPrint;
    }

    public void setStructureForPrint(ArrayList<StructureElement> structureForPrint)
    {
        this.structureForPrint = structureForPrint;
    }

    public String getCommandForChangeStructure()
    {
        return commandForChangeStructure;
    }

    public void setCommandForChangeStructure(String commandForChangeStructure)
    {
        this.commandForChangeStructure = commandForChangeStructure;
    }

    public Integer getElementIdForChange()
    {
        return elementIdForChange;
    }

    public void setElementIdForChange(Integer elementIdForChange)
    {
        this.elementIdForChange = elementIdForChange;
    }

    public String getErrorOnImport()
    {
        return errorOnImport;
    }

    public void setErrorOnImport(String errorOnImport)
    {
        this.errorOnImport = errorOnImport;
    }

    public String getResultOfImport()
    {
        return resultOfImport;
    }

    public void setResultOfImport(String resultOfImport)
    {
        this.resultOfImport = resultOfImport;
    }

    public StructureProcessingFromDbHome getStructureHome()
    {
        StructureProcessingFromDbHome structureProcessingFromDbHome = null;
        try
        {
            InitialContext ic = new InitialContext();
            Object remoteObject = ic.lookup(JNDI_ROOT + "StructureProcessingFromDbEJB");
            structureProcessingFromDbHome = (StructureProcessingFromDbHome) PortableRemoteObject.narrow(remoteObject, StructureProcessingFromDbHome.class);
        } catch (Exception e)
        {
            logger.info(e.getMessage());
        }
        return structureProcessingFromDbHome;
    }
}
