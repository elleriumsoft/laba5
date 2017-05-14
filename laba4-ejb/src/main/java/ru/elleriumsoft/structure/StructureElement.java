package ru.elleriumsoft.structure;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by Dmitriy on 19.03.2017.
 */
@XmlType(propOrder = { "id", "nameDepartment", "parent_id", "level", "stateOfElement" }, name = "structureElement")
@XmlRootElement
public class StructureElement implements Serializable
{
    private int id;
    private String nameDepartment;
    private int parent_id;
    private int level;
    private int stateOfElement;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getNameDepartment()
    {
        return nameDepartment;
    }

    public void setNameDepartment(String nameDepartment)
    {
        this.nameDepartment = nameDepartment;
    }

    public int getParent_id()
    {
        return parent_id;
    }

    public void setParent_id(int parent_id)
    {
        this.parent_id = parent_id;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public int getStateOfElement()
    {
        return stateOfElement;
    }

    public void setStateOfElement(int stateOfElement)
    {
        this.stateOfElement = stateOfElement;
    }
}
