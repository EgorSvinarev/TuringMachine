
package turing.tape;

import java.util.ArrayList;

import util.MethodFactory;

public class TapeMemento
{
    private int                position;
    private ArrayList<String>  cells;
    
    @SuppressWarnings("unchecked")
    public TapeMemento(ArrayList<String> cells, int position)
    {
        this.setPosition(position);
        this.setCells((ArrayList<String>) MethodFactory.deepCopy(cells));
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public ArrayList<String> getCells()
    {
        return cells;
    }

    public void setCells(ArrayList<String> cells)
    {
        this.cells = cells;
    }
}
