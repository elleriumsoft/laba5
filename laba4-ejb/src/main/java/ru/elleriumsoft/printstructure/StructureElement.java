package ru.elleriumsoft.printstructure;

/**
 * Created by Dmitriy on 19.03.2017.
 */
public class StructureElement
{
    private int id;
    private String nameDepartment;
    private int parent_id;
    private int nestingLevel;

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
        return nestingLevel;
    }

    public void setLevel(int nestingLevel)
    {
        this.nestingLevel = nestingLevel;
    }
}
