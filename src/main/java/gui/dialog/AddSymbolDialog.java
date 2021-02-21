package gui.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class AddSymbolDialog extends TableEditingDialog{

	public AddSymbolDialog(Frame owner, String title, String labelMessage) {
		super(owner, title, labelMessage);
		
		renderElements();
		
		saveButton.addActionListener(new AddSymbolListener());
	}
	
	class AddSymbolListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateValue();
			String value = getValue();
			
			setValidStatus(true);
			setValue(value);
			
			dialog.dispose();
		}
	}
}
