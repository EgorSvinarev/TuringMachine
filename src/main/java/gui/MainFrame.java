package gui;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.awt.event.*;

import gui.*;
import gui.dialog.*;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import turing.Program;
import turing.TuringMachine;
import turing.tape.Tape;

import util.MachineLoader;
import util.TraceLoader;

public class MainFrame extends JFrame {

	private static final long    serialVersionUID  = 1L;
    private static final boolean RUNNING_STATE     = false;
    private static final boolean PAUSED_STATE      = true;
    static final String          MACHINE_DIRECTORY = "files/";
    static final int             RIGHT_PADDING     = 6;
    static final int             DOWN_PADDING      = 10;
    static boolean               state             = PAUSED_STATE;
    
    
	/* Верхнее меню */
	private JMenuBar 		menuBar						= new JMenuBar();
	
	/* Раздел меню по работе с алгоритмом */
	private JMenu			algorithmMenu				= new JMenu("Алгоритм");
	private JMenuItem		newAlgoItem					= new JMenuItem("Создать");
	private JMenuItem 		openAlgoFileItem			= new JMenuItem("Открыть");
	private JMenuItem		loadPreInstalledAlgoItem	= new JMenuItem("Предустановленный");
	private JMenuItem 		saveAlgoItem				= new JMenuItem("Сохранить");
	private JMenuItem		editAlgoItem				= new JMenuItem("Редактировать");
	
	/* Раздел меню по работе с трассой */
	private JMenu 			traceMenu					= new JMenu("Трасса");
	private JMenuItem 		saveTraceItem				= new JMenuItem("Сохранить трассу");
	private JMenuItem		watchTraceItem				= new JMenuItem("Посмотреть трассу");
	
	/* Раздел меню по работе с параметрами запуска машины */
	private JMenu			settingsMenu				= new JMenu("Настройки");
	private JMenuItem		algoSettingsItem			= new JMenuItem("Параметры запуска");
	private JMenuItem 		alphabetSettings			= new JMenuItem("Алфавит");
	private JMenuItem		tapeInputSettings			= new JMenuItem("Лента");
	
	/* Раздел меню, содержащий справочную информацию о работе с приложением */
	private JMenu 			referenceMenu 				= new JMenu("?");
	private JMenuItem 		backgroundInfoItem			= new JMenuItem("Сведения");
	private JMenuItem 		referenceItem				= new JMenuItem("Справка");
	
	/* Панель с информацией о выполнении программы */
	private InformationArea infoArea					= new InformationArea();
	
	/* Панель управления */
	private ImageIcon       runIcon;
    private ImageIcon       pauseIcon;
    private ImageIcon       backIcon;
    private ImageIcon       stepIcon;
    private ImageIcon       resetIcon;
    private JButton         runPauseButton;
    private JButton         backButton;
    private JButton         stepButton;
    private JButton         resetButton;
    private TitledPanel     controlPanel 			   = new TitledPanel("Управление");
	
	/* Панель с информацией о ленте */
	private TapeArea        tapeArea              	   = new TapeArea();
	
	/* Панель с информацией о программе */
	private ProgramArea 	programArea				   = new ProgramArea();
	
	/* Диалоговые окна */
	private SettingsDialog  	settingsDialog		   = new SettingsDialog(this, "Настройки");
	private TableEditingDialog 	addStateDialog		   = new AddStateDialog(this, "Добавить состояние", "Состояние: ");
	private TableEditingDialog 	addSymbolDialog		   = new AddSymbolDialog(this, "Добавить символ", "Символ: ");
	private TableEditingDialog  deleteStateDialog	   = new DeleteStateDialog(this, "Удалить состояние", "Номер состояния: ");
	private TableEditingDialog  deleteSymbolDialog	   = new DeleteSymbolDialog(this, "Удалить символ", "Символ: ");
	private AlphabetDialog		alphabetDialog		   = new AlphabetDialog(this);
	private TapeInputDialog		tapeInputDialog		   = new TapeInputDialog(this);
	private ProgramDialog		programDialog		   = new ProgramDialog(this);
	private ReferenceDialog		referenceDialog		   = new ReferenceDialog(this);
	private TraceDialog			traceInfoDialog		   = new TraceDialog(this);
	private PreinstalledProgramSelectorDialog preinstalledDialog	= new PreinstalledProgramSelectorDialog(this);
	
	/* Модели */
	private Program			program					   = new Program();
	private Tape			tape					   = new Tape();
	private TuringMachine	machine					   = new TuringMachine(program.getRulesHashMap(), tape);
	
	/* Таймеры для анимаций */
	private Timer			stepTimer;
	private int				delay;
	
	/* Режим работы машины */
	private int 			machineMode				   = 1;
	
	private boolean			isMachineStarted		   = false;		
	private boolean			isEditable				   = true;
	
	public MainFrame() {
		super();
		
		this.setSize(new Dimension(900,680));
		this.setResizable(false);
		setForeground(new Color(200, 200, 200));
        setBackground(new Color(200, 200, 200));
        
        this.addWindowListener(new WindowAdapter()
        {
            /* Вызывается когда окно программы закрывается. */
            public void windowClosed(WindowEvent e)
            {
                System.exit(0);
            }
            
            /* Вызывается когда окно закрывается. */
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        
        renderElements();
        
        programArea.setProgram(program);
        programArea.setAddStateDialog(addStateDialog);
		programArea.setAddSymbolDialog(addSymbolDialog);
		programArea.setDeleteStateDialog(deleteStateDialog);
		programArea.setDeleteSymbolDialog(deleteSymbolDialog);
		
		tapeArea.setMachine(machine);
		
		machine.addObserver(tapeArea);
		machine.addObserver(programArea);
		machine.addObserver(infoArea);
		
		programDialog.setProgram(program);
		
		delay = 500;
		stepTimer = new Timer(delay, new StepTimerListener());
		stepTimer.setInitialDelay(delay);
	}
	
	public void run() {
		this.setVisible(true);
	}
	
	private void renderElements() {
		/* Настройка верхнего меню */
		this.setJMenuBar(menuBar);
		
		menuBar.add(algorithmMenu);
		
		algorithmMenu.add(newAlgoItem);
		algorithmMenu.add(openAlgoFileItem);
		algorithmMenu.add(saveAlgoItem);
		algorithmMenu.add(editAlgoItem);
		algorithmMenu.add(loadPreInstalledAlgoItem);
		
		newAlgoItem.addActionListener(new NewAlgoItemListener());
		openAlgoFileItem.addActionListener(new OpenAlgoItemListener());
		saveAlgoItem.addActionListener(new SaveAlgoItemListener());
		editAlgoItem.addActionListener(new EditAlgoItemListener());
		loadPreInstalledAlgoItem.addActionListener(new LoadPreinstalledAlgoListener());
		
		menuBar.add(traceMenu);
		
		traceMenu.add(saveTraceItem);
		traceMenu.add(watchTraceItem);
		
		menuBar.add(settingsMenu);
		
		settingsMenu.add(algoSettingsItem);
		settingsMenu.add(alphabetSettings);
		settingsMenu.add(tapeInputSettings);
		
		algoSettingsItem.addActionListener(new AlgoSettingsItemLinsetener());
		tapeInputSettings.addActionListener(new TapeInputItemListener());
		
		menuBar.add(referenceMenu);
		
		referenceMenu.add(referenceItem);
		referenceMenu.add(backgroundInfoItem);
		
		referenceItem.addActionListener(new ReferenceItemListener());
		backgroundInfoItem.addActionListener(new BackgroundInfoItemListener());
		
		
		/* Настройка информационной панели */
        infoArea.setBounds(0, 0, this.getWidth() - RIGHT_PADDING, 50);
        
        
        
        /* Настройка панели управления */
        runIcon = new ImageIcon(this.getClass().getResource("/images/Play24.gif"));
        pauseIcon = new ImageIcon(this.getClass().getResource("/images/Pause24.gif"));
        backIcon = new ImageIcon(this.getClass().getResource("/images/StepBack24.gif"));
        stepIcon = new ImageIcon(this.getClass().getResource("/images/StepForward24.gif"));
        resetIcon = new ImageIcon(this.getClass().getResource("/images/Stop24.gif"));
        
        runPauseButton = new JButton(runIcon);
//        runPauseButton.addActionListener(new RunPauseButtonListener());
        runPauseButton.setToolTipText("Запуск");
        runPauseButton.addActionListener(new RunPauseButtonListener());
        
        backButton = new JButton(backIcon);
//        backButton.addActionListener(new BackButtonListener());
        backButton.setToolTipText("Шаг назад");
        backButton.addActionListener(new BackButtonListener());
        
        stepButton = new JButton(stepIcon);
//        stepButton.addActionListener(new StepButtonListener());
        stepButton.setToolTipText("Шаг вперед");
        stepButton.addActionListener(new StepButtonListener());
        
        resetButton = new JButton(resetIcon);
        resetButton.setToolTipText("Заново");
        resetButton.addActionListener(new ResetButtonListener());
        
        alphabetSettings.addActionListener(new AlphabetSettingsItemListener());
        
        saveTraceItem.addActionListener(new SaveTraceItemListener());
        watchTraceItem.addActionListener(new WatchTraceItemListener());
        
        controlPanel.setLayout(new GridLayout(1, 4, 5, 0));
        controlPanel.add(runPauseButton);
        controlPanel.add(backButton);
//        controlPanel.add(stepButton);
        controlPanel.add(resetButton);
        
        controlPanel.setBounds(0, 170, this.getWidth() - RIGHT_PADDING, 80);
        
        
        
		/* Настройка панели с лентой */
		tapeArea.setBounds(0, 50, this.getWidth() - RIGHT_PADDING, 120);
        
        
        
        /* Настройка панели с программой */
        programArea.setBounds(0, 250, this.getWidth() - RIGHT_PADDING, 360);
        
        
        /* Добавление обработчиков для диалоговых окон */
        addStateDialog.addWindowListener(new AddStateDialogWindowListener());
        addSymbolDialog.addWindowListener(new AddSymbolDialogWindowListener());
        deleteStateDialog.addWindowListener(new DeleteStateDialogWindowListener());
        deleteSymbolDialog.addWindowListener(new DeleteSymbolDialogWindowListener());
        alphabetDialog.addWindowListener(new AlphabetDialogWindowListener());
        tapeInputDialog.addWindowListener(new TapeInpuDialogWindowListener());
        settingsDialog.addWindowListener(new SettingsDialogWindowListener());
        programDialog.addWindowListener(new ProgramDialogWindowListener());
        preinstalledDialog.addWindowListener(new LoadPreinstalledWindowListener());
        
		/* Добавление панелей в контейнер */
        this.setLayout(null);
		this.getContentPane().add(infoArea);
		this.getContentPane().add(tapeArea);
		this.getContentPane().add(controlPanel);
		this.getContentPane().add(programArea);        
		

		
	}
	
	private void setHalted() {
		runPauseButton.setIcon(runIcon);
        runPauseButton.setToolTipText("Запуск");
        state = PAUSED_STATE;
        stepTimer.stop();
        infoArea.setStatus("Halted");
        
        JOptionPane.showMessageDialog(MainFrame.this, "Работа программы завершена.",
                "", JOptionPane.INFORMATION_MESSAGE, null);
    }
	
	private void setRunning() {
        runPauseButton.setIcon(pauseIcon);
        runPauseButton.setToolTipText("Пауза");
        state = RUNNING_STATE;
        stepTimer.start();
        infoArea.setStatus("running");
        machine.setStatus("running");
    }
	
    private void setPaused() {
        runPauseButton.setIcon(runIcon);
        runPauseButton.setToolTipText("Запуск");
        state = PAUSED_STATE;
        stepTimer.stop();
        infoArea.setStatus("paused");
    }
	
	private class AlgoSettingsItemLinsetener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			settingsDialog.setVisible(true);
		}
	}
	
	private class TapeInputItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String currentTape = machine.getTape().getTapeInString();
			
			tapeInputDialog.setValue(currentTape);
			
			tapeInputDialog.setValidStatus(false);
			tapeInputDialog.setVisible(true);
		}
	}
	
	private class NewAlgoItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!doesUserWantSaveAlgo()) {
				program = new Program();
				programArea.setProgram(program);
				programDialog.setProgram(program);
				
				isEditable = true;
        		editAlgoItem.setEnabled(true);
			}
		}
		
		private boolean doesUserWantSaveAlgo() {
			String[] options = {"Да", "Нет"}; 
            int result = JOptionPane.showOptionDialog(
               MainFrame.this,
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
	
	private class OpenAlgoItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "mt");
	        JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
	        fileChooser.setFileFilter(filter);
	        if ( fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
	        	String fileName = fileChooser.getSelectedFile().getAbsolutePath();
	        	try {
	        		String[] lines = MachineLoader.loadAlgo(fileName);
	        		program.importProgram(lines);
	        		
	        		machine.setRules(program.getRulesHashMap());
	        		
	        		programArea.updateTable();	        		
	        		programArea.repaint();
	        		
	        		programDialog.updateTable();
	        		programDialog.repaint();
	        		
	        		isEditable = true;
	        		editAlgoItem.setEnabled(true);
	        	}
	        	catch (IOException exc) {
	        		JOptionPane.showMessageDialog(MainFrame.this, "Не удалось загрузить алгоритм.",
	                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
	        	}
	        }	
		}
	}
	
	private class  LoadPreinstalledAlgoListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			preinstalledDialog.setVisible(true);
		}
	}
	
	private class LoadPreinstalledWindowListener extends WindowAdapter {
		public void windowClosed(WindowEvent e) {
			String fileName = preinstalledDialog.getChoosenFile();
			
			if (fileName == null) return;
			
			try {
        		String[] lines = MachineLoader.loadAlgo(fileName);
        		program.importProgram(lines);
        		
        		machine.setRules(program.getRulesHashMap());
        		
        		programArea.updateTable();
        		programArea.repaint();
        		
        		programDialog.updateTable();
        		programDialog.repaint();
        		
        		isEditable = false;
        		editAlgoItem.setEnabled(false);
			}
        	catch (IOException exc) {
        		JOptionPane.showMessageDialog(MainFrame.this, "Не удалось загрузить программу.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
        	}
		}
	}
	
	private class RunPauseButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (machine.getHaltState() == TuringMachine.HALTED)
                resetButton.doClick();
			
			if (PAUSED_STATE == state && machineMode != SettingsDialog.STEP_MODE) {
            	int headPosition = machine.getTape().getCurrentPosition();
            	
            	if (!isMachineStarted) {
            		machine.setRules(program.getRulesHashMap());
            		machine.getTape().updateCurrentPositionByNumberOfCell(headPosition);
            	}
            	
            	if (machineMode == SettingsDialog.DELAY_MODE) {
            		isMachineStarted = true;
            		setRunning();
            	}
            	else {
            		boolean flag = machine.executeProgram();
            		if (!flag) {
            			JOptionPane.showMessageDialog(MainFrame.this, "Программа превысила допустимые ограничения по выполнению.",
    	                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
            		}
            		setHalted();
            	}
            }
            else if (machineMode == SettingsDialog.STEP_MODE) {
        		machine.stepForwardN(1);
                infoArea.setSteps(machine.getSteps());
                if (machine.getHaltState() == TuringMachine.HALTED)
                    setHalted();
        	}
            else {
            	setPaused();
            }
                
		}
	}
	
	private class BackButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setPaused();
            machine.stepBackwardN(1);
		}
	}
	
	private class StepButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (machine.getHaltState() == TuringMachine.HALTED)
                return;
            else if (RUNNING_STATE == state)
                setPaused();
			machine.setRules(program.getRulesHashMap());
            machine.stepForwardN(1);
            if (machine.getHaltState() == TuringMachine.HALTED)
                setHalted();
		}
	}
	
	private class ResetButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setPaused();
			isMachineStarted = false;
            machine.resetMachine(null);
		}
	}
	
	private class SettingsDialogWindowListener extends WindowAdapter {
		public void windowClosed(WindowEvent e) {
			setPaused();
			settingsDialog.updateValues();
			
			int tapeLength = settingsDialog.getTapeLength() - 1;
			int delay = settingsDialog.getDelay();
			int headPosition = settingsDialog.getHeadPosition();
			String firstOperand = settingsDialog.getFirstOperand();
			String secondOperand = settingsDialog.getSecondOperand();
			
			
			String[] tape = generateTape(firstOperand, secondOperand);;
			
			
			stepTimer = new Timer(delay * 1000, new StepTimerListener());
			stepTimer.setInitialDelay(delay * 1000);
			
			if (tapeLength >= tape.length) {
				machine.resetMachineByInput(tape);
				machine.getTape().addCells(tapeLength - tape.length);
			}
			else if (tapeLength <= tape.length ) {
				machine.resetMachineByInput(tape);
			}
			
			
			machine.setNewHeadPosition(headPosition - 1);
			
			machineMode = settingsDialog.getMachineMode();
		}
		
		public String[] generateTape(String firstOperand, String secondOperand) {
			String currentTape = machine.getTape().getTapeInString();
			
			String[] newTape = new String[currentTape.length()];
			
			for (int i = 0; i < currentTape.length(); i++) {
				if (i == 1) {
					if (!firstOperand.equals("-1")) newTape[i] = "0";
					else newTape[i] = currentTape.charAt(i) + "";
				}
				else if (i == 2) {
					if (!firstOperand.equals("-1")) newTape[i] = firstOperand;
					else newTape[i] = currentTape.charAt(i) + "";;
				}
				else if (i == 4) {
					if (!secondOperand.equals("-1")) newTape[i] = secondOperand;
					else newTape[i] = currentTape.charAt(i) + "";;
				}
				else {
					newTape[i] = currentTape.charAt(i) + "";
				}
			}
			
			return newTape;
		}
	}
	
	private class ProgramDialogWindowListener extends WindowAdapter {
		public void windowClosed(WindowEvent e) {
			programArea.updateTable();
		}
	}
	
	private class AddStateDialogWindowListener extends WindowAdapter {

		public void windowClosed(WindowEvent e) {
			setPaused();
			if (addStateDialog.isFormValid()) {
				String value = addStateDialog.getValue();
				Program program = programArea.getProgram();
				
				if (program.isStateExist(value)) {
					JOptionPane.showMessageDialog(MainFrame.this, "Данное состояние уже существует в машине.",
	                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
					return;
				}
				
				program.addState(value);
				
				programArea.updateTable();
			}
		}
	}
	
	private class AddSymbolDialogWindowListener extends WindowAdapter {

		public void windowClosed(WindowEvent e) {
			setPaused();
			if (addSymbolDialog.isFormValid()) {
				String value = addSymbolDialog.getValue();
				Program program = programArea.getProgram();
				
				if (program.isSymbolExist(value)) {
					JOptionPane.showMessageDialog(MainFrame.this, "Данный символ уже существует в алфавите.",
	                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
					return;
				}
				
				program.addSymbol(value);
				
				programArea.updateTable();
			}
		}
	}
	
	private class DeleteStateDialogWindowListener extends WindowAdapter {
		public void windowClosed(WindowEvent e) {
			setPaused();
			if (deleteStateDialog.isFormValid()) {
				String value = deleteStateDialog.getValue();
				Program program = programArea.getProgram();
				
				if (!program.isStateExist(value)) {
					JOptionPane.showMessageDialog(MainFrame.this, "Данное состояние отсутствует в машине.",
	                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
					return;
				}
				
				program.deleteState(value);
				
				programArea.updateTable();
			}
		}
	}
	
	private class DeleteSymbolDialogWindowListener extends WindowAdapter {
		public void windowClosed(WindowEvent e) {
			setPaused();
			if (deleteSymbolDialog.isFormValid()) {
				String value = deleteSymbolDialog.getValue();
				Program program = programArea.getProgram();
				
				if (!program.isSymbolExist(value)) {
					JOptionPane.showMessageDialog(MainFrame.this, "Данный символ отсутствует в алфавите.",
	                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
					return;
				}
				
				program.deleteSymbol(value);
				
				programArea.updateTable();
			}
		}
	}
	
	private class SaveAlgoItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setPaused();
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "mt");
	        JFileChooser fileChooser = new JFileChooser(new File("algo"));
	        fileChooser.setFileFilter(filter);
	        if ( fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
	        	String fileName = fileChooser.getSelectedFile().getAbsolutePath();
	        	try {
	        		MachineLoader.saveAlgo(fileName, program.exportProgram());
	        	}
	        	catch (IOException exc) {
	        		JOptionPane.showMessageDialog(MainFrame.this, "Не удалось сохранить алгоритм.",
	                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
	        	}
	        }
		}
	}
	
	private class EditAlgoItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setPaused();
			programDialog.initializeBuffer();
			programDialog.updateTable();
			programDialog.setVisible(true);
		}
	}
	
	private class AlphabetSettingsItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setPaused();
			
			alphabetDialog.setValue(program.getAlphabetInString());
			alphabetDialog.setVisible(true);
			
		}
	}

	private class SaveTraceItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			setPaused();
			
			String[] tapes = machine.getMachineCaretaker().exportTape();
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "track");
	        JFileChooser fileChooser = new JFileChooser(new File("track"));
	        fileChooser.setFileFilter(filter);
	        if ( fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
	        	String fileName = fileChooser.getSelectedFile().getAbsolutePath();
	        	try {
	        		TraceLoader.saveTrace(fileName, tapes);
	        	}
	        	catch (IOException exc) {
	        		JOptionPane.showMessageDialog(MainFrame.this, "Не удалось сохранить алгоритм.",
	                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
	        	}
	        }
		}
	}
	
	private class WatchTraceItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			traceInfoDialog.setMachineCaretaker(machine.getMachineCaretaker());
			
			traceInfoDialog.updateTrace();
			traceInfoDialog.setVisible(true);
		}
	}
	
	private class AlphabetDialogWindowListener extends WindowAdapter {
		public void windowClosed(WindowEvent e) {
			setPaused();
			
			if (alphabetDialog.isFormValid()) {
				String[] value = alphabetDialog.getValueInArr();
				program.setAlphabet(value);
				
				programArea.updateTable();
				programDialog.updateTable();
			}
		}
	}
	
	private class TapeInpuDialogWindowListener extends WindowAdapter {
		public void windowClosed(WindowEvent e) {
			setPaused();
			
			if (tapeInputDialog.isFormValid()) {
				String[] arr = tapeInputDialog.getValueInArr();

				machine.resetMachineByInput(arr);
			
			}
		}
	}
	
	private class StepTimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			machine.stepForwardN(1);
            infoArea.setSteps(machine.getSteps());
            if (machine.getHaltState() == TuringMachine.HALTED)
                setHalted();
		}
	}
	
	private class ReferenceItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			referenceDialog.setVisible(true);
		}
	}
	
	private class BackgroundInfoItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			File htmlFile = new File("index.html");
			try {
				Desktop.getDesktop().browse(htmlFile.toURI());
			}
			catch (IOException exc) {
				JOptionPane.showMessageDialog(MainFrame.this, "Не удалось найти файл со сведениями.",
                        "Ошибка", JOptionPane.ERROR_MESSAGE, null);
			}
		}
	}
	
	
	
}
