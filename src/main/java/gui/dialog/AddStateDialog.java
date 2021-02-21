package gui.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class AddStateDialog extends TableEditingDialog{

	public AddStateDialog(Frame owner, String title, String labelMessage) {
		super(owner, title, labelMessage);
	
		renderElements();
		
		saveButton.addActionListener(new AddStateListener());
	}
	
	class AddStateListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateValue();
			setValidStatus(false);
			String value = getValue();
			int intVal;
			
			try {
				intVal = Integer.valueOf(value).intValue();
			}
			catch (NumberFormatException exc)
            {
                JOptionPane.showMessageDialog(dialog, "Введите положительное число.",
                        "Bad Format", JOptionPane.ERROR_MESSAGE, null);
                return;
            }
			
			if (intVal < 1)
            {
                JOptionPane.showMessageDialog(dialog, "Please enter a positive integer",
                        "Bad Format", JOptionPane.ERROR_MESSAGE, null);
                return;
            }
			
			setValidStatus(true);
			setValue(value);
			
			dialog.dispose();
		}
	}
}
