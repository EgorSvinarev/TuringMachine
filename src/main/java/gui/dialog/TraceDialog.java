package gui.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import turing.memento.MachineCaretaker;

public class TraceDialog extends JDialog {

	private JScrollPane 		scrollPanel		= new JScrollPane();
	private JPanel				panel			= new JPanel();
	private JLabel              guideText		= new JLabel();
    private StringBuilder       builder			= new StringBuilder();
	
    private MachineCaretaker	careTaker;
    
    public TraceDialog(Frame owner) {
    	super(owner, "Трасса", true);
    
    	renderElements();
    }
    
    public void setMachineCaretaker(MachineCaretaker mc) {
    	this.careTaker = mc;
    }
    
    private void renderElements() {
    	panel.add(guideText);
    	
    	scrollPanel.setViewportView(panel);
    	
    	this.add(scrollPanel);
    	
    	setSize(new Dimension(170, 250));
        setResizable(false);
    }
    
    public void updateTrace() {
    	String[] tapes = careTaker.exportTape();
    	builder = new StringBuilder();
    	
    	builder.append("<html><body>");
    	
    	for (int i = 0; i < tapes.length; i++) {
    		builder.append("<p>" + tapes[i] + "</p>");
    	}
    	
    	builder.append("</html></body>");
    	
    	guideText.setText(builder.toString());
    }
    
}
