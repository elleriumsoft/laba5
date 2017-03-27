package ru.elleriumsoft.printstructure;

/**
 * Created by Dmitriy on 27.03.2017.
 */
public class StateOfElements
{
    protected final static int CLOSE = 1;
    protected final static int OPEN = 2;
    protected final static int NO_CHILD = 3;

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
