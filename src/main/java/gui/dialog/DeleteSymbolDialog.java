package gui.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DeleteSymbolDialog extends TableEditingDialog{

	public DeleteSymbolDialog(Frame owner, String title, String labelMessage) {
		super(owner, title, labelMessage);
		
		renderElements();
		
		saveButton.addActionListener(new DeleteSymbolListener());
	}
	
	class DeleteSymbolListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateValue();
			String value = getValue();
			
			setValidStatus(true);
			setValue(value);
			
			dialog.dispose();
		}
	}
	
}
