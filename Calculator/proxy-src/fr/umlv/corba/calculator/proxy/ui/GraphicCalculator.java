package fr.umlv.corba.calculator.proxy.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import fr.umlv.corba.calculator.proxy.CorbaCalculatorOperations;

/**
 * @author Mathieu MANCEL
 *
 */
public class GraphicCalculator {

    private JFrame frame;
    private ArrayList<JButton> buttons;
    private static CorbaCalculatorOperations calculator;
    private JTextField label;
    
    public GraphicCalculator(CorbaCalculatorOperations calculator){
      
        GraphicCalculator.calculator = calculator;
        this.buttons = new ArrayList<JButton>();
        // Ajout des chiffres
        for(int i=0;i<9;i++){
            final JButton button = new JButton(Integer.toString(i+1));
            button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0) {
                    //GraphicCalculator.calculator.push(new Integer(button.getText()).shortValue());
                    label.setText(label.getText()+button.getText());
                }});
            buttons.add(button);
        }
        final JButton button = new JButton("0");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                label.setText(label.getText()+button.getText());
            }});
        buttons.add(button);
        
        // Ajout des opérations
        this.frame = new JFrame("CorbaCalculator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        JPanel numbers = new JPanel();
        
        BoxLayout numLayout = new BoxLayout(numbers,BoxLayout.Y_AXIS);
        Box line1 = new Box(BoxLayout.X_AXIS);
        for(int i=0;i<3;i++){
            line1.add(buttons.get(i));
        }
        Box line2 = new Box(BoxLayout.X_AXIS);
        for(int i=3;i<6;i++){
            line2.add(buttons.get(i));
        }
        Box line3 = new Box(BoxLayout.X_AXIS);
        for(int i=6;i<9;i++){
            line3.add(buttons.get(i));
        }
        Box line4 = new Box(BoxLayout.X_AXIS);
        line4.add(Box.createHorizontalGlue());
        line4.add(buttons.get(9));
        line4.add(Box.createHorizontalGlue());
        
        numbers.add(line1);
        numbers.add(line2);
        numbers.add(line3);
        numbers.add(line4);
        numbers.setLayout(numLayout);
        
        JPanel operations = new JPanel();
        BoxLayout operationsLayout = new BoxLayout(operations,BoxLayout.X_AXIS);
        Box col = new Box(BoxLayout.Y_AXIS);
        
        JButton add = new JButton("+");
        add.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                GraphicCalculator.calculator.add();
                label.setText(new Integer(GraphicCalculator.calculator.pop()).toString());
            }});
        
        JButton sub = new JButton("-");
        sub.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                GraphicCalculator.calculator.sub();
                label.setText(new Integer(GraphicCalculator.calculator.pop()).toString());
            }});
        
        JButton mult = new JButton("X");
        mult.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                GraphicCalculator.calculator.mult();
                label.setText(new Integer(GraphicCalculator.calculator.pop()).toString());
            }});
        
        JButton div = new JButton("/");
        div.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                GraphicCalculator.calculator.div();
                label.setText(new Integer(GraphicCalculator.calculator.pop()).toString());
            }});
        
        col.add(add);
        col.add(sub);
        col.add(mult);
        col.add(div);
        col.add(Box.createVerticalGlue());
        
        operations.add(Box.createHorizontalGlue());
        operations.add(col);
        operations.setLayout(operationsLayout);
        
        JPanel top = new JPanel();
        this.label = new JTextField(10);
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                if(label.getText().length()>0){
                    try{
                        short shortValue = new Integer(label.getText()).shortValue();
                        System.out.println("La valeur envoyer est " + shortValue);
						GraphicCalculator.calculator.push(shortValue);
                        label.setText("");
                    }catch(NumberFormatException e){
                        label.setText("");
                    };
                }
            }});
        JButton cancel = new JButton("C");
        cancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent arg0) {
                label.setText("");
            }});
        top.add(label);
        top.add(ok);
        top.add(cancel);
        top.setBorder(BorderFactory.createEtchedBorder());
        numbers.setBorder(BorderFactory.createEtchedBorder());
        operations.setBorder(BorderFactory.createEtchedBorder());
        container.add(top,BorderLayout.NORTH);
        container.add(numbers,BorderLayout.CENTER);
        container.add(operations,BorderLayout.EAST);
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }
    
    /**
     * @return Returns the frame.
     */
    public JFrame getFrame() {
        return frame;
    }
    
}

