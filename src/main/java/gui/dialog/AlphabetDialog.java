package gui.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.AlphabetLoader;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class AlphabetDialog extends JDialog{

	private JPanel			inputPanel	= new JPanel();
	private JLabel 			label			= new JLabel("Алфавит: ");
	private JTextField		field			= new JTextField(10);
	
	private JPanel			btnPanel		= new JPanel();
	private JButton 		loadButton		= new JButton("Загрузить");
	private JButton			saveButton		= new JButton("OK");
	
	protected JDialog		dialog			= this;
	
	private String 			value;
	private boolean			isFieldValid 	= false;
	
	private Frame			owner;
	
	public AlphabetDialog(Frame owner) {
		super(owner, "Настройки алфавита", true);
		
		this.owner = owner;
		
		renderElements();
		
		saveButton.addActionListener(new SaveButtonListener());
		loadButton.addActionListener(new LoadButtonListener());
	}
	
	private void renderElements() {
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
		inputPanel.add(label);
		inputPanel.add(field);
		
		btnPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		btnPanel.add(saveButton);
		btnPanel.add(loadButton);
		
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
			if (!doesUserWantSaveAlgo()) {
				setValidStatus(true);
				dialog.dispose();
				
			}
			else {
				setValidStatus(false);
				dialog.dispose();
			}
		}
		
		private boolean doesUserWantSaveAlgo() {
			String[] options = {"Да", "Нет"}; 
            int result = JOptionPane.showOptionDialog(
               AlphabetDialog.this,
               "Несохраненная программа будет удалена.\n Продолжить?", 
               "Внимание!",            
               JOptionPane.YES_NO_OPTION,
               JOptionPane.QUESTION_MESSAGE,
               null,     //no custom icon
               options,  //button titles
               options[0] //default button
            );
            if(result == JOptionPane.YES_OPTION){
               return false; 
            }else if (result == JOptionPane.NO_OPTION){
               return true;
            }else {
               return true;
            }
		}
	}
	
	class LoadButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "track");
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setFileFilter(filter);
	        if ( fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
	        	String fileName = fileChooser.getSelectedFile().getAbsolutePath();
	        	try {
	        		String alphabet = AlphabetLoader.loadAphabet(fileName)[0];
	        		field.setText(alphabet);
	        	}
	        	catch (IOException exc) {
	        		JOptionPane.showMessageDialog(owner, "Не удалось сохранить алгоритм.",
	                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
	        	}
	        }
		}
	}
	
}
