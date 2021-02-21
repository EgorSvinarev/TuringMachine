package turing.tape;

public class TapeCaretaker
{
    private TapeMemento memento;

    public TapeCaretaker()
    {
        this.memento = null;
    }
    
    public TapeMemento retrieveMemento()
    {
        return this.memento;
    }
    
    public void saveMemento(TapeMemento memento)
    {
        this.memento = memento;
    }
}