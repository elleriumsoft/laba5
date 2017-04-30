package ru.elleriumsoft.structure.objectstructure;

import ru.elleriumsoft.structure.print.StateOfElements;
import ru.elleriumsoft.structure.print.StructureElement;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dmitriy on 30.04.2017.
 */
@XmlType(propOrder = { "statesOfElements", "structureForPrint" }, name = "structure")
@XmlRootElement
public class Structure implements Serializable
{
    private ArrayList<StateOfElements> statesOfElements;
    private ArrayList<StructureElement> structureForPrint;

    public ArrayList<StateOfElements> getStatesOfElements()
    {
        return statesOfElements;
    }

    public void setStatesOfElements(ArrayList<StateOfElements> statesOfElements)
    {
        this.statesOfElements = statesOfElements;
    }

    public ArrayList<StructureElement> getStructureForPrint()
    {
        return structureForPrint;
    }

    public void setStructureForPrint(ArrayList<StructureElement> structureForPrint)
    {
        this.structureForPrint = structureForPrint;
    }
}
