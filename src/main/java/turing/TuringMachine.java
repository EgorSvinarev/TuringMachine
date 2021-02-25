package turing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Observable;

import turing.memento.MachineCaretaker;
import turing.memento.MachineMemento;
import turing.tape.Tape;
import turing.tape.TapeCaretaker;

public class TuringMachine extends Observable
{
    public static final String                      INIT_STATE          = "1";
    public static final boolean                     NOT_HALTED          = Boolean.FALSE;
    public static final boolean                     HALTED              = Boolean.TRUE;
    
//    Правила перехода
    public HashMap<String, TransitionRule> rules;
//    Совершенные шаги
    public int                                      steps;
    public int                                      totalSteps;
    public String                                   currentState;
    private boolean                                 halted;
    private Tape                                    tape;
    private TapeCaretaker                           tapeCaretaker;
    private String                                  status;
    private MachineCaretaker                        machineCaretaker;
    
    public TuringMachine(HashMap<String, TransitionRule> rules, String[] input)
    {
        this.rules = rules;
        this.steps = 0;
        this.totalSteps = 0;
        this.currentState = INIT_STATE;
        this.halted = TuringMachine.NOT_HALTED;
        this.status = "machine ready";
        this.tape = new Tape(input);
        this.tapeCaretaker = new TapeCaretaker();
        tapeCaretaker.saveMemento(tape.createTapeMemento());
        this.machineCaretaker = new MachineCaretaker();
        this.setTotalSteps();
    }
    
    public TuringMachine(HashMap<String, TransitionRule> rules, Tape tape)
    {
        this.rules = rules;
        this.steps = 0;
        this.totalSteps = 0;
        this.currentState = INIT_STATE;
        this.halted = TuringMachine.HALTED;
        this.status = "machine ready";
        this.tape = new Tape(tape);
        this.tapeCaretaker = new TapeCaretaker();
        tapeCaretaker.saveMemento(tape.createTapeMemento());
        this.machineCaretaker = new MachineCaretaker();
        this.setTotalSteps();
        
    }
    
    public String getCurrentState()
    {
        return currentState;
    }
    
    public void setCurrentState(String currentState)
    {
        this.currentState = currentState;
    }
    
    public Tape getTape()
    {
        return this.tape;
    }
    
    public void setTape(Tape t)
    {
        tape = t;
    }
    
    public boolean getHaltState()
    {
        return halted;
    }
    
    public void setHaltState(boolean halted)
    {
        this.halted = halted;
    }
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
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
    
    public int getTotalSteps()
    {
        return totalSteps;
    }
    
    public void setTotalSteps(int totalSteps)
    {
        this.totalSteps = totalSteps;
    }
    
    public void setRules(HashMap<String, TransitionRule> rules) {
    	this.rules = rules;
    	this.resetMachineByInput(null);
    }
    
    public MachineCaretaker getMachineCaretaker() {
    	return this.machineCaretaker;
    }
    
    /** Перезагрузка машины. */
    public void resetMachineByInput(String[] input)
    {
        this.steps = 0;
        this.currentState = INIT_STATE;
        this.halted = TuringMachine.NOT_HALTED;
        this.status = "machine ready";
        if (null == input)
            this.tape.restoreTapeMemento(tapeCaretaker.retrieveMemento());
        else
        {
            this.tape = new Tape(input);
            tapeCaretaker.saveMemento(tape.createTapeMemento());
            this.setTotalSteps();
        }
        setNewHeadPosition(Tape.INIT_POSITION);
        machineCaretaker.clearMementos();
        /* Следующие элементы GUI должны быть перерисованы:
         * + TapeArea (Установка входных значений в ленту)
         * + InformationArea
         * + ProgramArea (Выделение текущей команды в панели с программой) */
        this.setChanged();
        this.notifyObservers();
    }
    
    public void setNewHeadPosition(int cellNumber) {
    	getTape().updateCurrentPositionByNumberOfCell(cellNumber);
    	
//    	this.tapeCaretaker = new TapeCaretaker();
//    	tapeCaretaker.saveMemento(tape.createTapeMemento());
//        this.machineCaretaker = new MachineCaretaker();
    	
    	this.setChanged();
        this.notifyObservers();
    }
    
    public void notifyObs() {
    	this.setChanged();
        this.notifyObservers();
    }
    
    public boolean executeProgram() {
    	int stepCounter = 0;
    	boolean flag = true;
    	while (stepForward() && stepCounter < 100000 && tape.getSize() < 300) {
    		stepCounter++;
    		
    		if (stepCounter == 100000 || tape.getSize() > 300) {
    			flag = false;
    		}
    	}
    	
    	this.setChanged();
        this.notifyObservers();
    	
    	return flag;
    }
    
    public void resetMachine(Tape tape)
    {
        this.steps = 0;
        this.currentState = INIT_STATE;
        this.halted = TuringMachine.NOT_HALTED;
        this.status = "machine ready";
        
        if (null == tape)
            this.tape.restoreTapeMemento(tapeCaretaker.retrieveMemento());
        else
        {
        	this.tape = new Tape(tape);
            tapeCaretaker.saveMemento(tape.createTapeMemento());
            this.setTotalSteps();
        }
        
        machineCaretaker.clearMementos();
        /* Следующие элементы GUI должны быть перерисованы:
         * + TapeArea (Установка входных значений в ленту)
         * + InformationArea
         * + ProgramArea (Выделение текущей команды в панели с программой) */
        this.setChanged();
        this.notifyObservers();
    }

    private boolean stepForward()
    {
    	if (machineCaretaker != null)
            machineCaretaker.pushMemento(this.createMachineMemento());
        String currentSymbol = tape.getCurrentSymbol();
        StateSymbolPair key = new StateSymbolPair(currentState, currentSymbol);
        TransitionRule rule = rules.get(key.getStateSymbolStringPair());
        if (rule == null)
        {
            halted = HALTED;
            this.status = "machine halted";
            return false;
        }
        else
        {
        	String newSymbol = rule.getNewSymbol();
            int direction = rule.getDirection();
            String newState = rule.getNewState();
            currentState = newState;
            tape.updateCurrentSymbol(newSymbol);
            if (!tape.updateCurrentPosition(direction))
            {
                halted = HALTED;
                this.status = "machine halted";
                return false;
            }
            this.status = "machine ready";
            steps++;
        }
        
        
        
        return true;
    }
    
    /* Метод выполняет n шагов машины.
     * @return
     * * true если после выполнения n шагов, машина не остановилась
     * * false в других случаях */
    public void stepForwardN(int n)
    {
        for (int i = 0; i < n; i++)
        {
            if (!this.stepForward())
            {
                this.status = new String("halted before " + n + " steps");
                break;
            }
        }
        /* После перемещения, некоторые компоненты должны быть перерисованы:
         * + TapeArea
         * + InformationArea (шаг)
         * + ProgramArea (выделение текущей команды)
         * Имеются две ситуации, при которых машина может остановиться:
         * №1. Не объявлена команда для пары (команда/состояние)
         * №2. Головка машина перемещена в крайнее левое положение */
        this.setChanged();
        this.notifyObservers();
    }
    
    /* stepBack: восстанавливает машину и ленту в том состоянии, в котором
     *  они были на последнем шаге. Возвращает:
     * false если операция stepBack была неуспешна (потому что память шагов пуста). */
    public boolean stepBack()
    {
        if (machineCaretaker != null && machineCaretaker.getMementoSize() > 0)
        {
            this.restoreMachineMemento(machineCaretaker.popMemento());
            return true;
        }
        else
        {
            this.status = "cannot step back";
            return false;
        }
    }
    
    public void stepBackwardN(int n)
    {
        for (int i = 0; i < n; i++)
        {
            if (!this.stepBack())
            {
                this.status = new String("cannot step back");
                break;
            }
        }
        /* После смещения некоторые компоненты GUI должны быть перерисованы:
         * + TapeArea
         * + InformationArea (шаг)
         * + ProgramArea (выделение текущей команды)
         * Имеются две ситуации, при которых машина может остановиться:
         * №1. Не объявлена команда для пары (команда/состояние)
         * №2. Головка машина перемещена в крайнее левое положение */
        this.setChanged();
        this.notifyObservers();
    }
    
    private void setTotalSteps()
    {
//        while (this.stepForward());
//        System.out.println(1);
        this.totalSteps = 0;
        /** We should reset machine */
        this.steps = 0;
        this.currentState = INIT_STATE;
        this.halted = TuringMachine.NOT_HALTED;
        this.status = "machine ready";
        tape.restoreTapeMemento(tapeCaretaker.retrieveMemento());
        machineCaretaker.clearMementos();
    }
    
    public void printRules()
    {
        System.out.println("printRules(): ******************************************************");
        Iterator<Entry<String, TransitionRule>> it = rules.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, TransitionRule> entry = it.next();
            String key = entry.getKey();
            TransitionRule value = entry.getValue();
            System.out.println(key.charAt(0) + "\t" + key.charAt(1) + ": "
                    + value.getRuleString());
        }
        System.out.println("********************************************************************");
    }
     
    public String getRuleString(String state, String symbol)
    {
        String ruleString = null;
        StateSymbolPair key = new StateSymbolPair(state, symbol);
        TransitionRule rule = rules.get(key);
        if (null == rule)
            ruleString = TransitionRule.getUndifinedRule();
        else
            ruleString = rule.getRuleString();
        return ruleString;
    }
    
    public MachineMemento createMachineMemento()
    {
        return new MachineMemento(steps, currentState, halted, tape, status);
    }
    
    public void restoreMachineMemento(MachineMemento memento)
    {
        this.steps = memento.getSteps();
        this.currentState = memento.getCurrentState();
        this.halted = memento.isHalted();
        this.tape = (Tape) memento.getTape().clone();
        this.status = memento.getStatus();
    }
}
