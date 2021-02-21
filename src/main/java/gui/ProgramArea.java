package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import turing.TuringMachine;
import turing.tape.Tape;
import turing.Program;

import gui.dialog.*;

public class ProgramArea extends TitledPanel implements Observer
{
    private static final long   serialVersionUID    = 1L;
    private static final String TITLE               = "Программа";
    private JScrollPane         scrollPane          = new JScrollPane();
    private JPanel				btnPanel			= new JPanel();
    private JTable              table               = new JTable();
    private JButton				addColumnBtn		= new JButton("Добавить символ");
    private JButton				addRowBtn			= new JButton("Добавить состояние");
    private JButton				deleteColumnBtn		= new JButton("Удалить символ");
    private JButton				deleteRowBtn		= new JButton("Удалить состояние");
    private TapeTableModel      dataModel           = new TapeTableModel();
    private CurrentRuleRenderer renderer            = new CurrentRuleRenderer();
    private Program				program;
    private String				colNames;

    private TableEditingDialog  addStateDialog;
    private TableEditingDialog  addSymbolDialog;
    private TableEditingDialog  deleteStateDialog;
    private TableEditingDialog  deleteSymbolDialog;
    
    public ProgramArea()
    {
    	super(TITLE);
    	
    	renderElements();
    }
    
    public void renderElements() {
    	
        this.setLayout(new GridLayout(1, 1));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        addColumnBtn.setPreferredSize(new Dimension(200, 30));
        addColumnBtn.addActionListener(new AddingColumnListener());
        addRowBtn.setPreferredSize(new Dimension(200, 30));
        addRowBtn.addActionListener(new AddingRowListener());
        deleteColumnBtn.setPreferredSize(new Dimension(200, 30));
        deleteColumnBtn.addActionListener(new DeletingColumnListener());
        deleteRowBtn.setPreferredSize(new Dimension(200, 30));
        deleteRowBtn.addActionListener(new DeletingRowListener());
        
        btnPanel.add(addColumnBtn);
        btnPanel.add(addRowBtn);
        btnPanel.add(deleteColumnBtn);
        btnPanel.add(deleteRowBtn);

//        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        this.add(scrollPane);
//        this.add(btnPanel);
        
        scrollPane.setViewportView(table);
        
        /* Object.class означает, что рендерер активирован для всех видов данных в таблице */
        table.setDefaultRenderer(Object.class, renderer);
        renderer.setCurrentState(CurrentRuleRenderer.INIT_STATE);
        renderer.setCurrentSymbol(CurrentRuleRenderer.INIT_SYMBOL);
        this.setTableProperties();
        
        this.updateTable();
        
        dataModel.addTableModelListener(new TableChangesListener());
        
        
    }
    
    public void setProgram(Program program) {
    	this.program = program;
    	
    	updateTable();
    	table.repaint();
    }
    
    public void setAddStateDialog(TableEditingDialog window) {
    	this.addStateDialog = window;
    }
    
    public void setAddSymbolDialog(TableEditingDialog window) {
    	this.addSymbolDialog = window;
    }
    
    public void setDeleteStateDialog(TableEditingDialog window) {
    	this.deleteStateDialog = window;
    }
    
    public void setDeleteSymbolDialog(TableEditingDialog window) {
    	this.deleteSymbolDialog = window;
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
    	
    	String[] alphabet = this.program.getAlphabet();
    	String[] states = this.program.getStates();
    	
        String[] colNames = new String[states.length + 1];
        colNames[0] = " ";
        for (int i = 1; i < colNames.length; i++) {
        	colNames[i] = "q" + states[i - 1];
        }
        
        String[][] rules = this.program.getRules();
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
    
    public void update(Observable o, Object arg)
    {
        TuringMachine machine = (TuringMachine) o;
        /* Установка выделения области */
        renderer.setCurrentState(machine.getCurrentState());
        renderer.setCurrentSymbol(machine.getTape().getCurrentSymbol());
        this.setTableProperties();
        
        this.updateTable();
        table.repaint();
    }
    
    public Program getProgram() {
    	return this.program;
    }
    
    class TapeTableModel extends DefaultTableModel
    {
        private static final long serialVersionUID = 1L;
        
        public boolean isCellEditable(int row, int column)
        {
            return false;
        }
    }
    
    class CurrentRuleRenderer extends DefaultTableCellRenderer
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
    
    class AddingColumnListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		addSymbolDialog.setValidStatus(false);
    		addSymbolDialog.setVisible(true);
    	}
    }
    
    class AddingRowListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		addStateDialog.setValidStatus(false);
    		addStateDialog.setVisible(true);
    	}
    }
    
    class DeletingColumnListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		deleteSymbolDialog.setValidStatus(false);
    		deleteSymbolDialog.setVisible(true);
    	}
    }
    
    class DeletingRowListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		deleteStateDialog.setValidStatus(false);
    		deleteStateDialog.setVisible(true);
    	}
    }
    
    class TableChangesListener implements TableModelListener {

		public void tableChanged(TableModelEvent e) {
			int row = e.getFirstRow();
	        int column = e.getColumn();
	        
	        if ((row == -1) || (column == -1)) return;
	        
	        TableModel model = (TableModel)e.getSource();
	        String data = (String) model.getValueAt(row, column);
			
	        program.setNewRule(row, column - 1, data);
		}
    	
    }
}
