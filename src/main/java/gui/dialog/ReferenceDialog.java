package gui.dialog;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class ReferenceDialog extends JDialog {
	
	private JScrollPane 		scrollPanel		= new JScrollPane();
	private JPanel				panel			= new JPanel();
	private JLabel              guideText		= new JLabel();
    private StringBuilder       builder			= new StringBuilder();
	
    public ReferenceDialog(Frame owner) {
    	super(owner, "Справка", true);
    	
    	renderElements();
    }
    
    private void renderElements() {
    	panel.setBorder(new EmptyBorder(10, 10, 10, 10));
    	
    	guideText.setHorizontalAlignment(SwingConstants.CENTER);
    	guideText.setText(prepareReferenceString());
    	
    	panel.setLayout(new BorderLayout());
    	panel.add(guideText, BorderLayout.NORTH);
    
    	getContentPane().add(panel);
    	
    	setSize(new Dimension(650, 210));
        setResizable(false);
    }
    
    private String prepareReferenceString() {
    	builder.append("<html><body>");
    	builder.append("<p>Самарский университет</p>");
    	builder.append("<p>Кафедра программных систем</p><br>");
    	builder.append("<p>Курсовой проект по дисциплине \"Программная инженерия\"</p><br>");
    	builder.append("<p>Тема проекта: \"Система моделирования вычислительной машины Тьюринга\"<p><br>");
    	builder.append("<p>Разработчики: студенты группы № 6413-020302D</p>");
    	builder.append("<p>Я.А. Лапко, А.А.Саенковой</p>");
    	builder.append("<body></html>");
    	
    	return builder.toString();
    }
    
}
