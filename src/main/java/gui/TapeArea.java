package gui;

/* TuringTapeArea: Отрисовка и анимирование ленты */

import java.awt.*;
import java.awt.event.*;

import java.util.Observable;
import java.util.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import turing.TuringMachine;
import turing.tape.Tape;

public class TapeArea extends TitledPanel implements Observer
{
    private static final long     serialVersionUID    = 1L;
    private static final String   TITLE               = "Лента";
    private static final int      COLUMN_SIZE         = 1;
    private static final int      DEFAULT_COLUMN_SIZE = 11;
    
    
    private JScrollPane           scrollPane          = new JScrollPane();
    private JTable                table               = new JTable();
    
    private JPanel				  buttonPanel		  = new JPanel();
    private JButton		   		  clearButton		  = new JButton("Очистить");
    
    private TapeTableModel        dataModel           = new TapeTableModel();
    private CurrentSymbolRenderer renderer            = new CurrentSymbolRenderer();
    private String[]              colNames;
    private String[][]            cellValues;
    private int                   colSize;
    private TuringMachine		  machine;
    
    public TapeArea()
    {
        super(TITLE);
        
        renderElements();
        
        clearButton.addActionListener(new ClearButtonListener());
        dataModel.addTableModelListener(new TableChangesListener());
    }
    
    private void renderElements() {
    	this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
    	buttonPanel.add(clearButton);
    	
    	this.add(scrollPane);
        this.add(buttonPanel);
    	
    	
    	scrollPane.setViewportView(table);
        
        table.setDefaultRenderer(Object.class, renderer);
        this.setTableProperties();
        
        this.setData(null);
        dataModel.setDataVector(cellValues, colNames);
        table.setModel(dataModel);
    }
    
    private void setData(Tape tape)
    {
        if (null == tape)
        {
            colSize = DEFAULT_COLUMN_SIZE;
            
            colNames = new String[colSize];
            colNames[0] = " ";
            for (int i = 1; i < colSize; i++)
                colNames[i] = String.valueOf(i);
            
            cellValues = new String[COLUMN_SIZE][];
            cellValues[0] = new String[colSize + 1];
            cellValues[0][0] = String.valueOf('#');
            cellValues[0][1] = String.valueOf('0');
            for (int i = 2; i < colSize; i++)
                cellValues[0][i] = String.valueOf(" ");
        }
        else
        {
            this.colSize = tape.getSize();

            colNames = new String[colSize + 1];
            colNames[0] = " ";
            
            for (int i = 1; i < colNames.length; i++) {
            	colNames[i] = String.valueOf(i);
            }
            
            cellValues = new String[COLUMN_SIZE][];
            cellValues[0] = new String[colSize + 1];
            cellValues[0][0] = "#";
            
            for (int i = 1; i < cellValues[0].length; i++) {
            	cellValues[0][i] = tape.getSymbolAt(i - 1);
            }
            
            
        }
    }
    
    public void setMachine(TuringMachine machine) {
    	this.machine = machine;
    }
    
    private void setTableProperties()
    {
        /* Установка высоты строки таблицы */
        table.setRowHeight(40);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        /* Масштабирование таблицы в зависимости от её контента */
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setColumnSelectionAllowed(false);
        /* Установка шрифта для текста */
        table.getTableHeader().setFont(new Font("Dialog", Font.PLAIN, 12));
        table.setFont(new Font("Dialog", Font.BOLD, 16));
        
        for (int i = 0; i < table.getColumnCount(); i++)
        {
            table.getColumnModel().getColumn(i).setPreferredWidth(20);
            table.getColumnModel().getColumn(i).setMinWidth(40);
        }
    }
    
    public void update(Observable o, Object arg)
    {
    	Tape tape = ((TuringMachine) o).getTape();
    	
        renderer.setPosition(tape.getCurrentPosition());
        this.setTableProperties();
        
        setData(tape);
        dataModel.setDataVector(cellValues, colNames);
        table.repaint();
    }
    
    public CurrentSymbolRenderer getRenderer() {
    	return this.renderer;
    }
    
    private void printArr(String[] arr) {
    	for (int i = 0; i < arr.length; i++) {
    		System.out.print(arr[i] + " ");
    	}
    	System.out.println();
    }
    
    class TapeTableModel extends DefaultTableModel
    {
        private static final long serialVersionUID = 1L;
        
        public boolean isCellEditable(int row, int column)
        {
            return (column > 1);	 
        }
        
    }
    
    class CurrentSymbolRenderer extends DefaultTableCellRenderer
    {
        private static final long serialVersionUID = 1L;
        private static final int  INIT_POSITION    = 2;
        private static final int  INIT_ROW_NUMBER  = 0;
        DefaultTableCellRenderer  renderer         = new DefaultTableCellRenderer();
        private int               position         = INIT_POSITION;
        
        public CurrentSymbolRenderer()
        {
            super.setHorizontalAlignment(SwingConstants.CENTER);
            renderer.setHorizontalAlignment(SwingConstants.CENTER);
            position = INIT_POSITION;
        }
        
        public int getPosition()
        {
            return position;
        }
        
        public void setPosition(int position)
        {
            this.position = position;
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column)
        {
//        	if (position == INIT_POSITION) {
//        		return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus,
//                        row, column);
//        	}
    		if (row == INIT_ROW_NUMBER && column == position + 1)
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
	        
	        if (data.length() > 1 && (data.charAt(0) + "").equals(" ")) {
	        	data = data.substring(1);
	        }
	        else if (data.equals("")) {
	        	data = Tape.EMPTY_SYMBOL;
	        }
	        
	        machine.getTape().setSymbolAt(column - 1, data);
	        machine.notifyObs();
		}
    }
		
    private class ClearButtonListener implements ActionListener {
    	public void actionPerformed(ActionEvent e) {
    		machine.getTape().clearTape();
    		
    		machine.notifyObs();
    	}
    }
    
}
