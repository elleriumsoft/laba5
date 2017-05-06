package ru.elleriumsoft.occupation.object;

import java.io.Serializable;

/**
 * Created by Dmitriy on 17.04.2017.
 */
public class Occupation implements Serializable
{
    private Integer id;
    private String name;

    public Occupation(Integer id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
