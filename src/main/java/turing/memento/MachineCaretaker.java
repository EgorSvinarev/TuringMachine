package turing.memento;

import java.util.Stack;
import java.util.ArrayList;

public class MachineCaretaker
{
    private Stack<MachineMemento> mementos = null;
    
    public MachineCaretaker()
    {
        mementos = new Stack<MachineMemento>();
    }
    
    public void pushMemento(MachineMemento memento)
    {
        mementos.add(memento);
    }
    
    public MachineMemento popMemento()
    {
        return mementos.pop();
    }
    
    public int getMementoSize()
    {
        if (mementos == null)
            return 0;
        return mementos.size();
    }
    
    public void clearMementos()
    {
        if (mementos != null)
            mementos.clear();
    }
    
    public String[] exportTape() {
    	String[] tapes = new String[mementos.size()];
    	
    	for (int i = 0; i < tapes.length; i++) {
    		
    		tapes[i] = mementos.get(i).getTape().toString();
    	}
    	
    	return tapes;
    }
}
