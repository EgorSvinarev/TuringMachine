package turing.memento;

import turing.tape.Tape;

public class MachineMemento
{
    public int      steps;
    public String   currentState;
    private boolean halted;
    private Tape    tape;
    private String  status;
    
    public MachineMemento(int steps,String currentState, boolean halted, Tape tape, String status)
    {
        this.steps = steps;
        this.currentState = currentState;
        this.halted = halted;
        this.tape = (Tape) tape.clone();
        this.status = status;
    }
    
    public int getSteps()
    {
        return steps;
    }
    
    public void setSteps(int steps)
    {
        this.steps = steps;
    }
    
    public String getCurrentState()
    {
        return currentState;
    }
    
    public void setCurrentState(String currentState)
    {
        this.currentState = currentState;
    }
    
    public boolean isHalted()
    {
        return halted;
    }
    
    public void setHalted(boolean halted)
    {
        this.halted = halted;
    }
    
    public Tape getTape()
    {
        return tape;
    }
    
    public void setTape(Tape tape)
    {
        this.tape = tape;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
}
