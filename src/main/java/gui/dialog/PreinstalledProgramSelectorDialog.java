package gui.dialog;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

import java.util.*;

public class PreinstalledProgramSelectorDialog extends JDialog {
	
	private JPanel					programsPanel			= new JPanel();
	private ButtonGroup				btnGroup				= new ButtonGroup();
	
	private JPanel					btnPanel				= new JPanel();
	private JButton					saveButton				= new JButton("OK");
	
	private ArrayList<JRadioButton>  buttons				= new ArrayList<JRadioButton>();
	private HashMap<Integer, String> btnToFileNameMap		= new HashMap<Integer, String>();
	
	public PreinstalledProgramSelectorDialog(Frame owner) {
		super(owner, "Программы", true);
		
		renderElements();
		
		saveButton.addActionListener(new SaveButtonListener());
	}
	
	private void addButton(String title, String fileName) {
		btnToFileNameMap.put(buttons.size(), fileName);
		
		JRadioButton btn = new JRadioButton(title);
		
		btnGroup.add(btn);
		buttons.add(btn);
	}
	
	private void formButtonGroup() {
		addButton("Сложение", "algo/plus.mt");
		addButton("Вычитание", "algo/minus.mt");
	}
	
	private void renderElements() {
		formButtonGroup();
		
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		programsPanel.setLayout(new BoxLayout(programsPanel, BoxLayout.Y_AXIS));
		
		for (JRadioButton button: buttons) {
			programsPanel.add(button);
		}
		
		programsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		btnPanel.add(saveButton);
		
		getContentPane().add(programsPanel);
		getContentPane().add(btnPanel);
		
		this.setSize(new Dimension(200, 130));
		this.setResizable(false);
		
	}
	
	public String getChoosenFile() {
		int index = -1;
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).isSelected()) {
				index = i;
				break;
			}
		}
		
		if (index == -1) return null;
		
		return btnToFileNameMap.get(index);
	}
	
	private class SaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	
}
