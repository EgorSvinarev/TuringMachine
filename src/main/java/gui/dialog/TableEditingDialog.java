package gui.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public abstract class TableEditingDialog extends JDialog{


	private JPanel			inputPanel	= new JPanel();
	private JLabel 			label		= new JLabel();
	private JTextField		field 		= new JTextField(10);
	
	private JPanel			btnPanel	= new JPanel();
	protected JButton		saveButton	= new JButton("OK");
	
	protected JDialog		dialog		= this;
	
	private String 			value;
	private boolean			isFieldValid = false;
	
	public TableEditingDialog(Frame owner, String title, String labelMessage) {
		super(owner, title, true);
		label.setText(labelMessage);
	}
	
	protected void renderElements() {
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
		inputPanel.add(label);
		inputPanel.add(field);
		
		btnPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		btnPanel.add(saveButton);
		
		Box mainBox = new Box(BoxLayout.Y_AXIS);
		mainBox.add(Box.createVerticalGlue());
		mainBox.add(inputPanel);
		mainBox.add(btnPanel);
		
		mainBox.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		getContentPane().add(mainBox);
		
		setSize(new Dimension(350, 120));
        setResizable(false);
	}
	
	protected void updateValue() {
		this.value = field.getText();
	}
	
	public String getValue() {
		return this.value;
	}
	
	protected void setValue(String value) {
		this.value = value;
	}
	
	public boolean isFormValid() {
		return this.isFieldValid;
	}
	
	public void setValidStatus(boolean status) {
		this.isFieldValid = status;
	}
}
