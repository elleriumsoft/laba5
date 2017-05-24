package ru.elleriumsoft.structure;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

@XmlType(propOrder = { "id", "nameDepartment", "parentId", "level", "stateOfElement" }, name = "structureElement")
@XmlRootElement
public class StructureElement implements Serializable
{
    private int id;
    private String nameDepartment;
    private int parentId;
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

    public int getParentId()
    {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
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
