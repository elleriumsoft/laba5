package ru.elleriumsoft.structure;

import java.io.Serializable;

/**
 * Created by Dmitriy on 19.03.2017.
 */
public class StructureElement implements Serializable
{
    private int id;
    private String nameDepartment;
    private int parent_id;
    private int Level;
    private int stateOfElement;

    public StructureElement(int id, String nameDepartment, int parent_id)
    {
        this.id = id;
        this.nameDepartment = nameDepartment;
        this.parent_id = parent_id;
//        Level = level;
    }

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
        return Level;
    }

    public void setLevel(int level)
    {
        Level = level;
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
