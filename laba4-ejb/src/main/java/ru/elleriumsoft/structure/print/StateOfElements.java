package ru.elleriumsoft.structure.print;

import java.io.Serializable;

/**
 * Created by Dmitriy on 27.03.2017.
 */
public class StateOfElements implements Serializable
{
    private int id;
    private int state;

    public StateOfElements(int id, int state)
    {
        this.id = id;
        this.state = state;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }
}
