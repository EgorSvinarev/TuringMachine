package gui.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import turing.Program;
import turing.TransitionRule;
import turing.TuringMachine;

import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import gui.MainFrame;
import gui.dialog.*;

public class ProgramDialog extends JDialog  implements Observer{

	private JPanel				editingButtonPanel				= new JPanel();
	private JButton				insertRightButton		= new JButton("Вставить\nсправа");
	private JButton				insertLeftButton		= new JButton("Вставить\nслева");
	private JButton				deleteRightButton		= new JButton("Удалить\nсправа");
	private JButton				deleteLeftButton		= new JButton("Удалить\nслева");
	
	private JScrollPane         scrollPane          	= new JScrollPane();
	private JTable              table               	= new JTable();
	private TapeTableModel      dataModel           	= new TapeTableModel();
	
	private JPanel				saveButtonPanel			= new JPanel();
	private JButton				saveButton				= new JButton("OK");
	
    private CurrentRuleRenderer renderer            	= new CurrentRuleRenderer();
    private Frame				owner;
    
    private Program				program;
    private Program				buffer;
    
	public ProgramDialog(Frame owner) {
		super(owner, "Программа", true);
		
		renderElements();
	}
	
	private void renderElements() {

        this.setLayout(new GridLayout(2, 1));
        this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        insertRightButton.setPreferredSize(new Dimension(150, 30));
        insertRightButton.addActionListener(new InsertRightListener());
        insertLeftButton.setPreferredSize(new Dimension(150, 30));
        insertLeftButton.addActionListener(new InsertLeftListener());
        deleteRightButton.setPreferredSize(new Dimension(150, 30));
        deleteRightButton.addActionListener(new DeleteRightListener());
        deleteLeftButton.setPreferredSize(new Dimension(150, 30));
        deleteLeftButton.addActionListener(new DeleteLeftListener());
        
        
        saveButton.addActionListener(new SaveButtonListener());
        saveButton.setPreferredSize(new Dimension(150, 30));
        
        editingButtonPanel.add(insertRightButton);
        editingButtonPanel.add(insertLeftButton);
        editingButtonPanel.add(deleteRightButton);
        editingButtonPanel.add(deleteLeftButton);
  
        saveButtonPanel.add(saveButton);
        saveButtonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        this.add(editingButtonPanel);
        this.add(scrollPane);
        this.add(saveButtonPanel);
        
        scrollPane.setViewportView(table);

        
        /* Object.class означает, что рендерер активирован для всех видов данных в таблице */
        table.setDefaultRenderer(Object.class, renderer);
        renderer.setCurrentState(CurrentRuleRenderer.INIT_STATE);
        renderer.setCurrentSymbol(CurrentRuleRenderer.INIT_SYMBOL);
        this.setTableProperties();
        setSize(new Dimension(700, 480));
        setResizable(false);
        
        this.updateTable();
        
        dataModel.addTableModelListener(new TableChangesListener());
	}
	
	public Program getProgram() {
		return this.program;
	}
	
	public void setProgram(Program program) {
		this.program = program;
		
		initializeBuffer();
	}
	
	public void initializeBuffer() {
		
		buffer.setAlphabet(program.getAlphabet());
		buffer.setRules(program.getRules());
		buffer.setStates(program.getStates());
	}
	
	public void update(Observable o, Object arg) {
        TuringMachine machine = (TuringMachine) o;
        /* Установка выделения области */
        renderer.setCurrentState(machine.getCurrentState());
        renderer.setCurrentSymbol(machine.getTape().getCurrentSymbol());
        this.setTableProperties();
        
        this.updateTable();
        table.repaint();
    }
	
	private void setTableProperties()
    {
        /* Установка высоты строки таблицы */
        table.setRowHeight(25);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        /* Установка запрета на масшатабирование таблицы */
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setColumnSelectionAllowed(false);
        /* Установка шрифта для текста */
        table.getTableHeader().setFont(new Font("Dialog", Font.PLAIN, 12));
        table.setFont(new Font("Dialog", Font.BOLD, 16));  
    }
	
	public void updateTable() {
    	if (this.program == null) {
    		this.program = new Program();
    	}
    	if (this.buffer == null) {
    		this.buffer = new Program();
    		initializeBuffer();
    	}
    	
    	
    	String[] alphabet = this.buffer.getAlphabet();
    	String[] states = this.buffer.getStates();
    	
        String[] colNames = new String[states.length + 1];
        colNames[0] = " ";
        for (int i = 1; i < colNames.length; i++) {
        	colNames[i] = "q" + states[i - 1];
        }
        
        String[][] rules = this.buffer.getRules();
        String[][] cellValues = new String[rules.length][];
        int k = 0;
        for (int i = 0; i < rules.length; i++) {
        	cellValues[i] = new String[rules[i].length + 1];
        	cellValues[i][0] = alphabet[k];
        	k++;
        	for (int j = 0; j < cellValues[i].length - 1; j++) {
    			cellValues[i][j + 1] = rules[i][j];
        	}
        }
        
        dataModel.setDataVector(cellValues, colNames);
        table.setModel(dataModel);
        
    }
	
	private class TapeTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;
        
        public boolean isCellEditable(int row, int column)
        {
            if (column > 0) return true;
            return false;
        }
	}
	
	private class CurrentRuleRenderer extends DefaultTableCellRenderer
    {
        private static final long 		serialVersionUID = 1L;
        private static final String 	INIT_SYMBOL      = "0";
        private static final String  	INIT_STATE       = "1";
        DefaultTableCellRenderer  		renderer         = new DefaultTableCellRenderer();
        private String            		currentSymbol    = INIT_SYMBOL;
        private String            		currentState     = INIT_STATE;
        
        public CurrentRuleRenderer()
        {
            super.setHorizontalAlignment(SwingConstants.CENTER);
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            currentState = INIT_STATE;
            currentSymbol = INIT_SYMBOL;
        }
        
        public String getCurrentSymbol()
        {
            return currentSymbol;
        }
        
        public void setCurrentSymbol(String currentSymbol)
        {
            this.currentSymbol = currentSymbol;
        }
        
        public String getCurrentState()
        {
            return currentState;
        }
        
        public void setCurrentState(String currentState)
        {
            this.currentState = currentState;
        }
     
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column)
        {
        	
        	int currentStateIndex = getProgram().getIndexOfState(currentState) + 1;
        	int currentSymbolIndex = getProgram().getIndexOfSymbol(currentSymbol);

        	
            /* Установка смещения */
            if (currentState == INIT_STATE && currentSymbol == INIT_SYMBOL)
                return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
            else if (row == currentSymbolIndex && column == currentStateIndex)
            {
                this.setBackground(new Color(128, 128, 255));
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
            }
            else
                return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
        }
    }
	
	private class TableChangesListener implements TableModelListener {

		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
	        int column = e.getColumn();
	        
	        if ((row == -1) || (column == -1)) return;
	        
	        TableModel model = (TableModel)e.getSource();
	        String data = (String) model.getValueAt(row, column);
			
	        if (data.equals(TransitionRule.UNDEFINED_RULE)) {
	        	buffer.setNewRule(row, column - 1, data);
	        	return;
	        }
	        
	        String[] parsedRule = TransitionRule.parseRule(data);
	        
	        boolean flag = true;
	        
	        if (!TransitionRule.isValidRule(data)) {
	        	JOptionPane.showMessageDialog(owner, "Некорректное правило.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
	        	flag = false;
	        }
	        else if (!program.isStateExist(parsedRule[2])) {
	        	JOptionPane.showMessageDialog(owner, "Данного состояния не существует.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
	        	flag = false;
	        }
	        else if (!program.isSymbolExist(parsedRule[0])) {
	        	JOptionPane.showMessageDialog(owner, "Данный символ отсутствует в алфавите.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
	        	flag = false;
	        }
	        else {
	        	buffer.setNewRule(row, column - 1, data);
	        }
	        
	        if (!flag) {
	        	model.setValueAt("XXX", row, column);
	        }
		}
    	
    }
	
	private class InsertRightListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
	
			if (program.getStates().length >= 15) {
				JOptionPane.showMessageDialog(owner, "Недопустимое количество состояний.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			
			int selectedColumnIndex = table.getSelectedColumn();
			
			if (selectedColumnIndex == -1) return;
		
			buffer.addColumn(selectedColumnIndex);
			
			updateTable();
		}
	}
	
	private class InsertLeftListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (program.getStates().length >= 15) {
				JOptionPane.showMessageDialog(owner, "Недопустимое количество состояний.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			
			int selectedColumnIndex = table.getSelectedColumn();
			
			if (selectedColumnIndex == -1) return;
			
			buffer.addColumn(selectedColumnIndex - 1);
			updateTable();
		}
	}
	
	private class DeleteRightListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if (program.getStates().length <= 3) {
				JOptionPane.showMessageDialog(owner, "Недопустимое количество состояний.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			
			int selectedColumnIndex = table.getSelectedColumn();
			
			if (selectedColumnIndex == -1) return;
			
			buffer.deleteColumn(selectedColumnIndex);
			
			updateTable();
		}
	}
	
	private class DeleteLeftListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int selectedColumnIndex = table.getSelectedColumn();
			
			if (program.getStates().length <= 3) {
				JOptionPane.showMessageDialog(owner, "Недопустимое количество состояний.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
				return;
			}
			
			if (selectedColumnIndex == -1) return;
			
			buffer.deleteColumn(selectedColumnIndex - 2);
			
			updateTable();
		}
	}
	
	private class SaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			program.setStates(buffer.getStates());
			program.setRules(buffer.getRules());
			initializeBuffer();
			
			dispose();
		}
	}
}
