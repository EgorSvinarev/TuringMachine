package turing;

import turing.tape.Tape;


public class TransitionRule
{
    public static final String UNDEFINED_RULE  = "XXX";
    public static final int    NEW_SYMBOL_INDEX = 0;
    public static final int    DIRECTION_INDEX  = 1;
    public static final int    NEW_STATE_INDEX  = 2;
    
    private String             newSymbol        = "0";
    private int                direction        = 0;
    private String             newState         = "0";
    
    public TransitionRule()
    {
    }
    
    public TransitionRule(String rule)
    {
    	if (rule.equals(UNDEFINED_RULE)) return;   	
        this.newSymbol = findNewSymbol(rule);
        
        String dirString = rule.substring(this.newSymbol.length(), this.newSymbol.length() + 1);
        
        if (dirString.equals(Tape.LEFT_DIRECTION_FLAG))
            this.direction = Tape.LEFT_DIRECTION;
        else if (dirString.equals(Tape.RIGHT_DIRECTION_FLAG))
            this.direction = Tape.RIGHT_DIRECTION;
        else if (dirString.equals(Tape.STAY_DIRECTION_FLAG))
            this.direction = Tape.STAY_DIRECTION;
        else 
        {
            System.err.println("TransitionRule(): error direction flag!");
            System.exit(-1);
        }
        
        // Оставшаяся строка - это новый номер состояния машины
        this.newState = rule.substring(this.newSymbol.length() + 1, rule.length());
    }
    
    public static String findNewSymbol(String rule) {
    	int lastIndex = -1;
    	
    	for (int i = 0; i < rule.length(); i++) {
    		if ((rule.charAt(i) + "").equals("R") || (rule.charAt(i) + "").equals("L") || (rule.charAt(i) + "").equals("C")) {
    			lastIndex = i;
    			break;
    		}
    	}
    	if (lastIndex == -1) {
    		return "";
    	}
    	
    	return rule.substring(0,  lastIndex);
    }
    
    public static boolean isValidRule(String rule)
    {
        if (rule == null)
            return false;
        else {
        	String newSymbol = findNewSymbol(rule);
        	
        	if (newSymbol.equals("")) return false;
        	
        	String dirString = rule.substring(newSymbol.length(), newSymbol.length() + 1);
        	String newState = rule.substring(newSymbol.length() + 1, rule.length());
        	
        	if (!(dirString.equals(Tape.STAY_DIRECTION_FLAG) || dirString.equals(Tape.RIGHT_DIRECTION_FLAG) || dirString.equals(Tape.LEFT_DIRECTION_FLAG)))  {
        		return false;
        	}
        	return true;
        	
        }
    }
    
    public static String[] parseRule(String rule) {
    	String[] res = new String[3];
    	
    	String newSymbol = findNewSymbol(rule);
    	String dirString = rule.substring(newSymbol.length(), newSymbol.length() + 1);
    	String newState = rule.substring(newSymbol.length() + 1, rule.length());
    	
    	res[0] = newSymbol;
    	res[1] = dirString;
    	res[2] = newState;
    	
    	return res;
    }
    
    public String getNewSymbol()
    {
        return this.newSymbol;
    }
    
    public int getDirection()
    {
        return this.direction;
    }
    
    public String getNewState()
    {
        return this.newState;
    }
    
    public String getRuleString()
    {
        StringBuilder rule = new StringBuilder();
        rule.append(newSymbol);
        switch ( direction )
        {
            case Tape.LEFT_DIRECTION:
                rule.append(Tape.LEFT_DIRECTION_FLAG);
                break;
            case Tape.RIGHT_DIRECTION:
                rule.append(Tape.RIGHT_DIRECTION_FLAG);
                break;
            case Tape.STAY_DIRECTION:
                rule.append(Tape.STAY_DIRECTION_FLAG);
                break;
        }
        rule.append(newState);
        return rule.toString();
    }
    
    public static String getUndifinedRule()
    {
        return UNDEFINED_RULE;
    }
}
