package fr.umlv.corba.calculator.proxy.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import fr.umlv.corba.calculator.proxy.ArithmeticException;
import fr.umlv.corba.calculator.proxy.CorbaCalculatorOperations;
import fr.umlv.corba.calculator.proxy.InvalidNumberOfOperators;
import fr.umlv.corba.calculator.proxy.UnKnowErrorException;

/**
 * @author Mathieu MANCEL
 *
 */
public class GraphicCalculator {

    private JFrame frame;
    private ArrayList<JButton> buttons;
    private static CorbaCalculatorOperations calculator;
    private JTextField label;
    private JTextArea history;
    
    public GraphicCalculator(CorbaCalculatorOperations calculator){
      
        GraphicCalculator.calculator = calculator;
        
        this.buttons = new ArrayList<JButton>();
        String[] buttonsNames = {"CE","7","8","9","/","C","4","5","6","*","=","1","2","3","-","OK","","0","","+"};
        
	    	for (int i = 0; i < buttonsNames.length; i++) {
            final JButton button = new JButton(buttonsNames[i]);
            if(button.getText()==""){
        		button.setEnabled(false);
        	}
            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0) {
                	try {
                	
                		if(button.getText().equals("OK")&& label.getText().length()>0){
                           try{
                               short shortValue = new Integer(label.getText()).shortValue();
                               label.setText("");
                               history.setText(history.getText()+"\n"+shortValue);
                               GraphicCalculator.calculator.push(shortValue);
                            }catch(NumberFormatException e){};
                		}
                		else if(button.getText().equals("+")){
        					GraphicCalculator.calculator.add();
        					label.setText(new Integer(GraphicCalculator.calculator.top()).toString());
        					history.setText(history.getText()+"\n+");
                		}
                		else if(button.getText().equals("-")){
        					GraphicCalculator.calculator.sub();
        					label.setText(new Integer(GraphicCalculator.calculator.top()).toString());
        					history.setText(history.getText()+"\n-");
                		}
                		else if(button.getText().equals("*")){
        					GraphicCalculator.calculator.mult();
        					label.setText(new Integer(GraphicCalculator.calculator.top()).toString());
        					history.setText(history.getText()+"\n*");
                		}
                		else if(button.getText().equals("/")){
        					GraphicCalculator.calculator.div();
        					label.setText(new Integer(GraphicCalculator.calculator.top()).toString());
        					history.setText(history.getText()+"\n/");
                		}
                		else if(button.getText().equals("=")){
                			String result =  new Integer(GraphicCalculator.calculator.pop()).toString();
        					label.setText(result);
        					history.setText(history.getText()+"\n=" + result);
                		}
                		else if(button.getText().equals("CE")){
                			label.setText("");
                		}
                		else if(button.getText().equals("C")){
                			//TODO VIDER LA PILE
                			label.setText("");
                			history.setText("Opérations:");
                		}
                		else{
                			label.setText(label.getText()+button.getText());
                		}
                		} catch (InvalidNumberOfOperators e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		} catch (UnKnowErrorException e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		} catch (ArithmeticException e) {
                			// TODO Auto-generated catch block
                			e.printStackTrace();
                		}
                	}});
            buttons.add(button);
        }

        this.frame = new JFrame("CorbaCalculator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        
        this.label = new JTextField(10);
        this.label.setEnabled(false);
        
		// Zone de Résultat
        this.label.setBackground(Color.black);// Fond Noir
        this.label.setForeground(Color.green);// Chiffres en Vert 
        this.label.setHorizontalAlignment(JTextField.RIGHT);// à partir de la droite
        this.label.setFont(new Font("Times",Font.BOLD,16));// Police
        
        this.history = new JTextArea(5,10);
        this.history.setEnabled(false);
        this.history.setText("Opérations:");
        JScrollPane scrollPane = new JScrollPane(history,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4,5));
        for(int i=0;i<buttons.size();i++){
            buttonsPanel.add(buttons.get(i));
        }
        
        container.add(buttonsPanel,BorderLayout.CENTER);
        container.add(this.label,BorderLayout.NORTH);
        container.add(scrollPane,BorderLayout.SOUTH);
        
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
    }
    
    /**
     * @return Returns the frame.
     */
    public JFrame getFrame() {
        return frame;
    }
}

