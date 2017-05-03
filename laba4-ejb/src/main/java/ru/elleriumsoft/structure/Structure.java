package ru.elleriumsoft.structure;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dmitriy on 30.04.2017.
 */
@XmlType(propOrder = { "commandForChangeStructure", "elementIdForChange", "structureForPrint" }, name = "structure")//@XmlType(propOrder = { "statesOfElements", "structureForPrint" }, name = "structure")
@XmlRootElement
public class Structure implements Serializable
{
    private String commandForChangeStructure;
    private Integer elementIdForChange;
    private ArrayList<StructureElement> structureForPrint;

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
}
