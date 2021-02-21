package turing;

public class StateSymbolPair
{
    private String state;
    private String symbol;
    
    public StateSymbolPair()
    {
        setState("0");
        setSymbol("0");
    }
    
    public StateSymbolPair(String state, String symbol)
    {
//        if (symbol != 0 && symbol != 1)
//        {
//            System.err.println("StateSymbolPair(): error type of symbol, 0 and 1 supported!");
//            System.exit(-1);
//        }
        
        this.setState(state);
        this.setSymbol(symbol);
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }
    
    public String getStateSymbolStringPair() {
    	return this.getState() + this.getSymbol();
    }
    
    public int hashCode() {  
        return (int)Math.pow(2, (int)state.charAt(0)) + (int)Math.pow(3, (int)symbol.charAt(0));
    }
    
    public boolean equals(Object o)
    {
        if ( o instanceof StateSymbolPair )
        {
            StateSymbolPair obj = (StateSymbolPair) o;
            if ( this.state.equals(obj.state) && this.symbol.equals(symbol) )
                return true;
        }
        return false;
    }
}
