package gui.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteStateDialog extends TableEditingDialog{

	public DeleteStateDialog(Frame owner, String title, String labelMessage) {
		super(owner, title, labelMessage);
		
		renderElements();
		
		saveButton.addActionListener(new DeleteStateListener());
	}
	
	class DeleteStateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateValue();
			String value = getValue();
			
			setValidStatus(true);
			setValue(value);
			
			dialog.dispose();
		}
	}
	
}
