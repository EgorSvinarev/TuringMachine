package gui.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import gui.dialog.AlphabetDialog.SaveButtonListener;

import java.awt.*;
import java.awt.event.*;

public class TapeInputDialog extends JDialog {

	private JPanel			inputPanel	= new JPanel();
	private JLabel 			label			= new JLabel("Лента: ");
	private JTextField		field			= new JTextField(10);
	
	private JPanel			btnPanel		= new JPanel();
	protected JButton		saveButton		= new JButton("OK");
	
	protected JDialog		dialog			= this;
	
	private String 			value;
	private boolean			isFieldValid 	= false;
	
	public TapeInputDialog(Frame owner) {
		super(owner, "Значения ленты", true);
	
		renderElements();
		
		saveButton.addActionListener(new SaveButtonListener());
	}
	
	private void renderElements() {
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
	
	public void updateValue() {
		this.value = field.getText();
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String[] getValueInArr() {
		updateValue();
		String[] arr = new String[this.value.length()];
		
		for (int i = 0; i < this.value.length(); i++) {
			arr[i] = this.value.charAt(i) + "";
		}
		
		return arr;
	}
	
	public void setValue(String value) {
		this.field.setText(value);
	}
	
	public boolean isFormValid() {
		return this.isFieldValid;
	}
	
	public void setValidStatus(boolean status) {
		this.isFieldValid = status;
	}
	
	class SaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setValidStatus(true);
			dialog.dispose();
		}
	}
	
	
}
