package turing;

import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Program {
	private String[][] 					rules;
	private ArrayList<String> 			alphabet			= new ArrayList<String>();
	private ArrayList<String> 			states				= new ArrayList<String>();	
	
	private int lastState; 
	
	public Program() {
		this.initializeEmptyProgram();
		
		lastState = Integer.parseInt(this.states.get(this.states.size() - 1));
	}

	public String[][] getRules() {
		return rules;
	}

	public void setRules(String[][] rules) {
		String[][] newRules = new String[rules.length][];
		
		for (int i = 0; i < newRules.length; i++) {
			newRules[i] = new String[rules[i].length];
			for (int j = 0; j < newRules[i].length; j++) {
				newRules[i][j] = new String(rules[i][j]);
			}
		}
		
		this.rules = newRules;
	}

	public void setAlphabet(String[] alphabet) {
		this.initializeEmptyProgram(alphabet);
	}
	
	public String[] getAlphabet() {
		String[] alphabet = new String[this.alphabet.size()];
		
		for (int i = 0; i < this.alphabet.size(); i++) {
			alphabet[i] = this.alphabet.get(i);
		}
		
		return alphabet;
	}
	
	public String getAlphabetInString() {
		String alphabet = "";
		
		for (int i = 0; i < this.alphabet.size(); i++) {
			alphabet += this.alphabet.get(i);
		}
		
		return alphabet;
	}
	
	public String[] getStates() {
		String[] states = new String[this.states.size()];
		
		for (int i = 0; i < this.states.size(); i++) {
			states[i] = this.states.get(i);
		}
		
		return states;
	}

	public void setStates(String[] newStates) {
		states = new ArrayList<String>();
		
		for (int i = 0; i < newStates.length; i++) {
			states.add(newStates[i]);
		}
	}
	
	public HashMap<String, TransitionRule> getRulesHashMap() {
		HashMap<String, TransitionRule> rules = new HashMap<String, TransitionRule>();
		for (int i = 0; i < this.rules.length; i++) {
			for (int j = 0; j < this.rules[i].length; j++) {
				StateSymbolPair stateSymbolpair = new StateSymbolPair(this.states.get(j), this.alphabet.get(i));
				TransitionRule rule = new TransitionRule(this.rules[i][j]);
				rules.put(stateSymbolpair.getStateSymbolStringPair(), rule);
			}
		}
		return rules;
	}
	
	public int getIndexOfState(String state) {
		int index = -1;
		for (int i = 0; i < this.states.size(); i++) {
			if (this.states.get(i).equals(state)) {
				index = i;
			}
		}
		
		return index;
	}
	
	public int getIndexOfSymbol(String symbol) {
		int index = -1;
		for (int i = 0; i < this.alphabet.size(); i++) {
			if (this.alphabet.get(i).equals(symbol)) {
				index = i;
			}
		}
		
		return index;
	}
	
	public String[] exportProgram() {
		String[] lines = new String[this.alphabet.size() + 1];
		
		String header = "\t";
		
		for (int i = 0; i < this.states.size() - 1; i++) {
			header += this.states.get(i) + "\t";
		}
		header += this.states.get(this.states.size() - 1);
		
		lines[0] = header;
		
		String line;
		int k = 0;
		int n = 1;
		for (int i = 0; i < this.rules.length; i++) {
			line = this.alphabet.get(k) + "\t";
			for (int j = 0; j < this.rules[i].length - 1; j++) {
				line += this.rules[i][j] + "\t";
			}
			line += this.rules[i][this.rules[i].length - 1];
			
			lines[n] = line;
			n++;
			k++;
		}
		
		return lines;
	}
	
	public void importProgram(String[] lines) {
		this.alphabet.clear();
		this.states.clear();
		
		String[] header = lines[0].split("\t");

		for (int i = 0; i < header.length; i++) {
			if (header[i] != "") {
				this.states.add(header[i]);
			}
		}
		
		
		String[][] newRules = new String[lines.length - 1][];
		String[] line;
		
		for (int i = 1; i < lines.length; i++) {
			line = lines[i].split("\t");
			this.alphabet.add(line[0]);
			
			newRules[i - 1] = new String[line.length - 1];
			
			for (int j = 1; j < line.length; j++) {
				newRules[i - 1][j - 1] = line[j];
			}
		}
		
		this.rules = newRules;
	}
	
	public void initializeEmptyProgram() {
		for (int i = 1; i < 4; i++) {
			this.states.add(String.valueOf(i));
		}
		
		for (int i = 0; i < 5; i++) {
			this.alphabet.add(String.valueOf(i));
		}
		
		this.rules = new String[this.alphabet.size()][];
		
		for (int i = 0; i < this.rules.length; i++) {
			this.rules[i] = new String[this.states.size()];
			
			for (int j = 0; j < this.rules[i].length; j++) {
				this.rules[i][j] = "XXX";
			}
		}
		
	}
	
	public void initializeEmptyProgram(String[] alphabet) {
		this.alphabet.clear();
		
		for (int i = 0; i < alphabet.length; i++) {
			this.alphabet.add(alphabet[i]);
		}
		
		this.rules = new String[this.alphabet.size()][];
		
		for (int i = 0; i < this.rules.length; i++) {
			this.rules[i] = new String[this.states.size()];
			
			for (int j = 0; j < this.rules[i].length; j++) {
				this.rules[i][j] = "XXX";
			}
		}
		
	}
	
	public boolean isStateExist(String state) {
    	for (int i = 0; i < this.states.size(); i++) {
    		if (states.get(i).equals(state)) return true;
    	}
    	return false;
    }
    
    public boolean isSymbolExist(String symbol) {
    	for (int i = 0; i < this.alphabet.size(); i++) {
    		if (this.alphabet.get(i).equals(symbol)) return true;
    	}
    	return false;
    }
    
    public void addSymbol(String symbol) {
    	if (isSymbolExist(symbol)) return;
    	
    	this.alphabet.add(symbol);
    	
    	String[][] newRules = new String[this.alphabet.size()][];
    	
    	for (int i = 0; i < newRules.length; i++) {
    		newRules[i] = new String[this.states.size()];
    		for (int j = 0; j < newRules[i].length; j++) {
    			if (i == newRules.length - 1) {
    				newRules[i][j] = "XXX";
    			}
    			else {
    				newRules[i][j] = rules[i][j];
    			}
    		}
    	}
    	
    	this.rules = newRules;
    }
    
    public void addState(String state) {
    	if (isStateExist(state)) return;
    	
    	if (this.states.size() >= 15) return;
    	
    	this.states.add(state);
    	
    	String[][] newRules = new String[this.alphabet.size()][];
    	
    	for (int i = 0; i < newRules.length; i++) {
    		newRules[i] = new String[this.states.size()];
    		for (int j = 0; j < newRules[i].length; j++) {
    			if (j == newRules[i].length - 1) {
    				newRules[i][j] = "XXX";
    			}
    			else {
    				newRules[i][j] = rules[i][j];
    			}
    		}
    	}
    	
    	this.rules = newRules;
    	
    }
    
    public void deleteSymbol(String symbol) {
    	if (!isSymbolExist(symbol)) return;
    	
    	int symbolIndex = this.alphabet.indexOf(symbol);
    	int k = 0;
    	
    	String[][] newRules = new String[this.alphabet.size() - 1][];
    	for (int i = 0; i < rules.length; i++) {
    		if (i == symbolIndex) continue;
    		newRules[k] = new String[this.states.size()];
    		
    		for (int j = 0; j < newRules[k].length; j++) {
    			newRules[k][j] = rules[i][j];
    		}
    		k++;
    	}
    	
    	this.alphabet.remove(symbolIndex);

    	this.rules = newRules;
    }
    
    public void deleteState(String state) {
    	if (!isStateExist(state)) return;
    	
    	if (this.states.size() <= 3) return;
    	
    	int stateIndex = this.states.indexOf(state);
    	int k;
    	
    	String[][] newRules = new String[this.alphabet.size()][];
    	
    	for (int i = 0; i < newRules.length; i++) {
    		newRules[i] = new String[this.states.size() - 1];
    		k = 0;
    		for (int j = 0; j < rules[i].length; j++) {
    			if (j == stateIndex) continue;
    			
    			newRules[i][k] = rules[i][j];
    			k++;
    		}
    	}
    	
    	this.states.remove(stateIndex);
    	
    	this.rules = newRules;
    }
    
    private void updateStates(boolean isIncrimented) {
    	int currentSize = this.states.size();
    	
    	this.states.clear();
    	
    	int newSize;
    	
    	if (isIncrimented) newSize = currentSize + 1;
    	else newSize = currentSize - 1;
    	
    	for (int i = 0; i < newSize; i++) {
    		this.states.add(String.valueOf(i + 1));
    	}
    }
    
    public void addColumn(int columnIndex) {
    	
    	String[][] newRules = new String[this.alphabet.size()][];
    	int k;
    	for (int i = 0; i < newRules.length; i++) {
    		newRules[i] = new String[rules[i].length + 1];
    		k = 0;
    		
    		for (int j = 0; j < newRules[i].length; j++) {
    			if (j == columnIndex) {
    				newRules[i][j] = "XXX";
    			}
    			else {
    				newRules[i][j] = new String(rules[i][k]);
    				k++;
    			}
    		}
    	}
    	
    	updateStates(true);
    	
    	this.rules = newRules;
    }
    
    public void deleteColumn(int columnIndex) {
    	if (columnIndex < 0 || columnIndex > this.states.size() - 1) return;
    	
    	String[][] newRules = new String[this.alphabet.size()][];
    	int k;
    	for (int i = 0; i < newRules.length; i++) {
    		newRules[i] = new String[this.states.size() - 1];
    		k = 0;
    		for (int j = 0; j < rules[i].length; j++) {
    			if (j == columnIndex) {
    				continue;
    			}
    			newRules[i][k] = new String(rules[i][j]);
    			k++;
    		}
    	}
    	
    	updateStates(false);
    	
    	this.rules = newRules;
    	
    }
    
    public void setNewRule(int row, int column, String rule) {
    	this.rules[row][column] = rule;
    }
    
    public void printList(ArrayList<String> l) {
    	for (int i = 0; i < l.size(); i++) {
    		System.out.print(l.get(i) + " ");
    	}
    	System.out.println();
    }
    
    public void printArr(String[] arr) {
    	for (int i = 0; i < arr.length; i++) {
    		System.out.print(arr[i] + " ");
    	}
    	System.out.println();
    }
    
}
