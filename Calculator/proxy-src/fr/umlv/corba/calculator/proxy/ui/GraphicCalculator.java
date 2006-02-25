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
    private final CorbaCalculatorOperations calculator;
    private static JTextField label;
    private JTextArea history;
    private static boolean result = false;
    
    public GraphicCalculator(final CorbaCalculatorOperations calculator){
      
        this.calculator = calculator;
        this.buttons = new ArrayList<JButton>();
        String[] buttonsNames = {"CE","7","8","9","/","C","4","5","6","*","=","1","2","3","-","OK","","0","-(x)","+"};
        
	    	for (int i = 0; i < buttonsNames.length; i++) {
            final JButton button = new JButton(buttonsNames[i]);
            if(button.getText()==""){
        		button.setEnabled(false);
        	}
            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0) {
                	String text = label.getText();
					String historyText = history.getText();
                	try {
						if(button.getText().equals("OK")&& text.length()>0){
                           try{
                               short shortValue = new Integer(text).shortValue();
                               label.setText("");
                               history.setText(historyText+"\n"+shortValue);
                               calculator.push(shortValue);
                            }catch(NumberFormatException e){};
                		}
                		else if(button.getText().equals("+")){
                			result = true;
        					calculator.add();
        					label.setText(new Integer(calculator.top()).toString());
        					history.setText(historyText+"\n+");
                		}
                		else if(button.getText().equals("-")){
                			result = true;
                			calculator.sub();
        					label.setText(new Integer(calculator.top()).toString());
        					history.setText(historyText+"\n-");
                		}
                		else if(button.getText().equals("*")){
                			result = true;
                			calculator.mult();
        					label.setText(new Integer(calculator.top()).toString());
        					history.setText(historyText+"\n*");
                		}
                		else if(button.getText().equals("/")){
                			result = true;
                			calculator.div();
        					label.setText(new Integer(calculator.top()).toString());
        					history.setText(historyText+"\n/");
                		}
                		else if(button.getText().equals("=")){
                			result = true;
                			String result =  new Integer(calculator.pop()).toString();
        					label.setText(result);
        					history.setText(historyText+"\n=" + result);
                		}
                		else if(button.getText().equals("CE")){
                			label.setText("");
                		}
                		else if(button.getText().equals("C")){
                			calculator.reset();
                			label.setText("");
                			history.setText("Opérations:");
                		}
                		else if(button.getText().equals("-(x)")){
                			String pos = "";
                			if(text.length()>0 && text.contains("-")){
                				pos = text.substring(1);
                			} else if(text.length()>0 && !text.contains("-")){
                				pos = "-" + text;
                			}
                			label.setText(pos);
                			if(result) {
                				calculator.pop();
                				calculator.push(new Integer(pos).shortValue());
                				
                			}
                			
                		}
                		else{
                			if(result)
                				label.setText(button.getText());
                			else
                				label.setText(text+button.getText());
                			result = false;
                		}
                		} catch (InvalidNumberOfOperators e) {
                			history.setText(historyText+"\n" + e.message);
                			//e.printStackTrace();
                		} catch (UnKnowErrorException e) {
                			history.setText(historyText+"\n" + e.message);
                			//e.printStackTrace();
                		} catch (ArithmeticException e) {
                			history.setText(historyText+"\n" + e.message);
                			//e.printStackTrace();
                		}
                	}});
            buttons.add(button);
        }

        this.frame = new JFrame("CorbaCalculator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        
        label = new JTextField(10);
        label.setFocusable(false);
        
		// Zone de Résultat
        label.setBackground(Color.black);// Fond Noir
        label.setForeground(Color.green);// Chiffres en Vert 
        label.setHorizontalAlignment(JTextField.RIGHT);// à partir de la droite
        label.setFont(new Font("Times",Font.BOLD,16));// Police
        
        this.history = new JTextArea(5,10);
        this.history.setFocusable(false);
       
        this.history.setText("Opérations:");
        JScrollPane scrollPane = new JScrollPane(history,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4,5));
        for(int i=0;i<buttons.size();i++){
            buttonsPanel.add(buttons.get(i));
        }
        
        container.add(buttonsPanel,BorderLayout.CENTER);
        container.add(label,BorderLayout.NORTH);
        container.add(scrollPane,BorderLayout.SOUTH);
        
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
        this.frame.setResizable(false);
    }
    
    /**
     * @return Returns the frame.
     */
    public JFrame getFrame()
    {
        return frame;
    }
 
}

