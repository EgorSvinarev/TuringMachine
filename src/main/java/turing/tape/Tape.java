package turing.tape;

import java.util.ArrayList;
import java.util.Iterator;

import util.MethodFactory;

public class Tape implements Cloneable
{
    public static final int    LEFT_DIRECTION       = -1;
    public static final int    STAY_DIRECTION       = 0;
    public static final int    RIGHT_DIRECTION      = 1;
    public static final String LEFT_DIRECTION_FLAG  = "L";
    public static final String STAY_DIRECTION_FLAG  = "C";
    public static final String RIGHT_DIRECTION_FLAG = "R";
    public static final int    INIT_POSITION        = 2;
    public static final int    LEFT_MOST_POSITION   = 1;
    public static final char   FILL_SYMBOL_FLAG     = '0';
    public static final String EMPTY_SYMBOL          = String.valueOf(' ');
    public static final String FILL_SYMBOL          = String.valueOf('*');
    public static final char   COUNTING_SYMBOL_FLAG = '1';
    public static final int    COUNTING_SYMBOL      = 1;
    
    private int                position             = INIT_POSITION;
    private ArrayList<String>  cells;
    
    /* Конструктор создает ленту из строки. Символы должны быть разделены
     * как мининум одним пробелом. Квадратные скобки вокруг символа указывают
     * на стартовую позицию читающей головки. Если никакой символ не обрамлен скобками, 
     * позиция читающей головки стандартно будет установлена на 2-ую позицию. Если обрамлен
     * более, чем один символ, читающая головка будет помещена на последний из них. */
    public Tape(String[] s)
    {
        this.position = INIT_POSITION;
        cells = new ArrayList<String>();
        
        if (s.length == 0) {
        	cells.add("0");
        	for (int i = 0; i < 5; i++)
            {
                cells.add(EMPTY_SYMBOL);
            }
        	return;
        }
        
        if (!s[0].equals("0")) cells.add("0"); 
        
        for (int i = 0; i < s.length; i++)
        {
            cells.add(s[i]);
        }
    }

    public Tape() {
    	this.position = INIT_POSITION;
    	cells = new ArrayList<String>();
        cells.add("0"); 
        for (int i = 0; i < 10; i++)
        {
            cells.add(EMPTY_SYMBOL);
        }
    }
    
    public Tape(int size) {
    	this.position = INIT_POSITION;
    	cells = new ArrayList<String>();
        cells.add("0"); 
        for (int i = 0; i < size; i++)
        {
            cells.add(EMPTY_SYMBOL);
        }
    }
    
    @SuppressWarnings("unchecked")
    public Tape(Tape t)
    {
        position = t.position;
        cells = (ArrayList<String>) t.cells.clone();
    }
    
    public int getCurrentPosition()
    {
        return position;
    }
    
    public String getCurrentSymbol()
    {
        return cells.get(position);
    }
    
    public void updateCurrentSymbol(String symbol)
    {
        cells.set(position, symbol);
    }
    
    public String getTapeInString() {
    	String tape = "";
    	
    	for (int i = 0; i < cells.size(); i++) {
    		tape += cells.get(i);
    	}
    	
    	return tape;
    }
    
    public void updateCurrentPositionByNumberOfCell(int cellNumber) {
    	if (cellNumber > position) {
    		while (updateCurrentPosition(Tape.RIGHT_DIRECTION) && cellNumber > position);
    	}
    	else if (cellNumber < position) {
    		while (updateCurrentPosition(Tape.LEFT_DIRECTION) && cellNumber < position);
    	}
    	else return;
    }
    
    public boolean updateCurrentPosition(int direction)
    {
        boolean flag = true;
        if (position == LEFT_MOST_POSITION && direction == Tape.LEFT_DIRECTION)
            flag = false;
        else
        {
            switch (direction)
            {
            case Tape.LEFT_DIRECTION:
                position--;
                break;
            case Tape.STAY_DIRECTION:
                break;
            case Tape.RIGHT_DIRECTION:
                position++;
                break;
            default:
                System.err.println("updateCurrentPosition(): error direction value!");
                break;
            }
            if (position >= cells.size())
            {
            	addCells(5);
            }
            flag = true;
        }
        return flag;
    }
    
    public void addCells(int cellsNumber) {
    	for (int i = 0; i < cellsNumber; i++) {
    		cells.add(EMPTY_SYMBOL);
    	}
    }
    
    public String getSymbolAt(int i)
    {
        return cells.get(i).toString();
    }
    
    public void setSymbolAt(int i, String symbol) {
    	cells.set(i, symbol);
    }
    
    public int getSize()
    {
        return cells.size();
    }
    
    public void clearTape() {
    	for (int i = 0; i < cells.size(); i++) {
    		cells.set(i, EMPTY_SYMBOL);
    	}
    }
    
    public void printTape()
    {
        StringBuilder info = new StringBuilder();
        Iterator<String> it = cells.iterator();
        while (it.hasNext())
            info.append(it.next().toString() + " ");
        info.append(", position is " + position);
        System.out.println(info);
    }
    
    public TapeMemento createTapeMemento()
    {
        return new TapeMemento(cells, position);
    }
    
    @SuppressWarnings("unchecked")
    public void restoreTapeMemento(TapeMemento memento)
    {
        this.position = memento.getPosition();
        this.cells = (ArrayList<String>) MethodFactory.deepCopy(memento.getCells());
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Object clone()
    {
        Tape tapeClone = null;
        try
        {
            tapeClone = (Tape) super.clone();
            tapeClone.position = this.position;
            tapeClone.cells = (ArrayList<String>) MethodFactory.deepCopy(this.cells);
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }

        return tapeClone;
    }
    
    @Override
    public String toString() {
    	String tape = "";
    	
    	for (int i = 0; i < cells.size(); i++) {
    		tape += cells.get(i);
    	}
    	
    	return tape;
    }
}
