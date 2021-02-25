package gui.dialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class SettingsDialog extends JDialog {
	
	/* Модели спиннеров */
	SpinnerNumberModel 			tapeLengthSpinnerModel	= new SpinnerNumberModel(10, 10, 300, 1);
	SpinnerNumberModel			firstOperandSpinnerModel= new SpinnerNumberModel(0, 0, 10, 1);
	SpinnerNumberModel			secondOperandSpinnerModel= new SpinnerNumberModel(0, 0, 10, 1);
	SpinnerNumberModel			delaySpinnerModel		= new SpinnerNumberModel(1, 1, 10, 1);
	SpinnerNumberModel			headPositionSpinnerModel = new SpinnerNumberModel(3, 2, 300, 1);
	
	/* Панель настройки ленты */
	private JPanel 				tapeSettingsPanel		= new JPanel();
	private JLabel 				tapeLengthLabel			= new JLabel("Длина ленты:        ");
	private JSpinner			tapeLengthField			= new JSpinner(tapeLengthSpinnerModel);
	
	/* Панель указания операндов для работы алгоритма */
	private JPanel				operandsSettingsPanel	= new JPanel();
	private JLabel				operandTitleLabel		= new JLabel("Значение операндов");
	private JLabel				firstOperandLabel		= new JLabel("Операнд №1: "); 
	private JLabel				secondOperandLabel		= new JLabel("      Операнд №2: ");
	private JSpinner			firstOperandField		= new JSpinner(firstOperandSpinnerModel);
	private JSpinner			secondOperandField		= new JSpinner(secondOperandSpinnerModel);
	
	/* Панель настройки задержки между выполнением команд*/
	private	JPanel				delaySettingsPanel		= new JPanel();
	private JLabel				delayLabel				= new JLabel("Время задержки: ");
	private JSpinner 			delayField				= new JSpinner(delaySpinnerModel);
	
	private JPanel				headPositionPanel		= new JPanel();
	private JLabel				headPositionLabel		= new JLabel("Положение головки: ");
	private JSpinner			headPositionField		= new JSpinner(headPositionSpinnerModel);
	
	/* Панель настройки режима работы машины */
	private JPanel				machineModePanel		= new JPanel();
	private JLabel				machineModeTitle		= new JLabel("Режимы работы");
	private ButtonGroup			machineBtnGroup				= new ButtonGroup();
	private JRadioButton		delayModeButton			= new JRadioButton("C задержкой", true);
	private JRadioButton		quickModeButton			= new JRadioButton("Моментально", false);
	private JRadioButton		stepModeButton			= new JRadioButton("Пошагово   ", false);
	
	/* Панель с кнопкой сохранения настроек*/
	private	JPanel				btnPanel				= new JPanel();
	private JButton 			saveButton				= new JButton("ОК");
	
	private boolean 			isFieldValid			= false;
	private int					tapeLength				= (Integer) tapeLengthField.getValue();
	private String				firstOperand			= firstOperandField.getValue().toString();
	private String				secondOperand			= secondOperandField.getValue().toString();
	private int					delay					= (Integer) delayField.getValue();
	private int					headPosition			= (Integer) headPositionField.getValue();
	public static final int    STEP_MODE				= 1;
	public static final int    QUICK_MODE				= 2;
	public static final int    DELAY_MODE				= 3;

	public SettingsDialog(Frame owner, String message) {
		super(owner, message, true);
		
		renderElements();
	
		saveButton.addActionListener(new SaveButtonListener());
	}
	
	private void renderElements() {
		tapeSettingsPanel.setLayout(new BoxLayout(tapeSettingsPanel, BoxLayout.X_AXIS));
		tapeSettingsPanel.add(tapeLengthLabel);
		tapeSettingsPanel.add(tapeLengthField);
		
		Box operandPanelBox = new Box(BoxLayout.Y_AXIS);
		
		operandPanelBox.add(Box.createVerticalGlue());
		Box operandTitleBox = new Box(BoxLayout.X_AXIS);
		operandTitleBox.add(operandTitleLabel);
		operandPanelBox.add(operandTitleBox);
		
		operandPanelBox.add(Box.createVerticalGlue());
		Box operandsBox = new Box(BoxLayout.X_AXIS);
		operandsBox.add(firstOperandLabel);
		operandsBox.add(firstOperandField);
		operandsBox.add(secondOperandLabel);
		operandsBox.add(secondOperandField);
		operandPanelBox.add(operandsBox);
		
		operandsSettingsPanel.add(operandPanelBox);
		
		delaySettingsPanel.setLayout(new BoxLayout(delaySettingsPanel, BoxLayout.X_AXIS));
		delaySettingsPanel.add(delayLabel);
		delaySettingsPanel.add(delayField);
		
		headPositionPanel.setLayout(new BoxLayout(headPositionPanel	, BoxLayout.X_AXIS));
		headPositionPanel.add(headPositionLabel);
		headPositionPanel.add(headPositionField);
		
		Box modePanelBox = new Box(BoxLayout.Y_AXIS);
		
		modePanelBox.add(Box.createVerticalGlue());
		Box modeTitleBox = new Box(BoxLayout.X_AXIS);
		modeTitleBox.add(machineModeTitle);
		modePanelBox.add(modeTitleBox);
		
		modePanelBox.add(Box.createVerticalGlue());
		Box modesBox = new Box(BoxLayout.X_AXIS);
		modesBox.add(delayModeButton);
		modesBox.add(quickModeButton);
		modesBox.add(stepModeButton);
		modePanelBox.add(modesBox);
		
		machineBtnGroup.add(quickModeButton);
		machineBtnGroup.add(delayModeButton);
		machineBtnGroup.add(stepModeButton);
		
		machineModePanel.add(modePanelBox);
		
		btnPanel.setBorder(new EmptyBorder(0, 10, 0, 10));
		btnPanel.add(saveButton);
		
		Box mainBox = new Box(BoxLayout.Y_AXIS);
		mainBox.add(Box.createVerticalGlue());
		mainBox.add(tapeSettingsPanel);
		mainBox.add(operandsSettingsPanel);
		mainBox.add(delaySettingsPanel);
		mainBox.add(headPositionPanel);
		mainBox.add(machineModePanel);
		mainBox.add(btnPanel);
		
		mainBox.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		getContentPane().add(mainBox);
		
        setSize(new Dimension(370, 250));
        setResizable(false);

	}
	
	public boolean isFormValid() {
		return this.isFieldValid;
	}
	
	public void setValidStatus(boolean status) {
		this.isFieldValid = status;
	}
	
	public void updateValues() {
		tapeLength = (Integer) this.tapeLengthField.getValue();
		firstOperand = firstOperandField.getValue().toString();
		secondOperand = secondOperandField.getValue().toString();
		delay = (Integer) delayField.getValue();
		headPosition = (Integer) headPositionField.getValue();
	}
	
	public int getTapeLength() {
		return tapeLength;
	}
	
	public String getFirstOperand() {
		return firstOperand;
	}
	
	public String getSecondOperand() {
		return secondOperand;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public int getHeadPosition() {
		return this.headPosition;
	}
	
	public int getMachineMode() {
		if (delayModeButton.isSelected()) {
			return DELAY_MODE;
		}
		else if (stepModeButton.isSelected()) return STEP_MODE;
		else return QUICK_MODE;
	}
	
	class SaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setValidStatus(true);
			dispose();
		}
	}
	
}
