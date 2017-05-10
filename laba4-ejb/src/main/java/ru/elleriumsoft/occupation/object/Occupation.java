package ru.elleriumsoft.occupation.object;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Created by Dmitriy on 17.04.2017.
 */
@XmlType(propOrder = { "id", "name" }, name = "occupations")
@XmlRootElement
public class Occupation implements Serializable
{
    private Integer id;
    private String name;

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
