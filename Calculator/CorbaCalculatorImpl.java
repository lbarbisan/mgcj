
/*
 * Created on 1 févr. 2006
 *
 */

/**
 * @author Mathieu MANCEL
 *
 */
public class CorbaCalculatorImpl {
    
    private short[] values = new short[100]; 
    private int index=0;
    
    public void push(short val ){
        index++;
        values[index] = val;
    }
    
    public short pop(){
        short value = values[index];
        index--;
        return value;
    }
    
    public void add(){
        short a = values[index];
        index--;
        short b = values[index];
        values[index] = (short) (a+b);
    }
    
    public void sub(){
        short a = values[index];
        index--;
        short b = values[index];
        values[index] = (short) (a-b);
    }
    
    public void mult(){
        short a = values[index];
        index--;
        short b = values[index];
        values[index] = (short) (a*b);
    }
    
    public void div(){
        short a = values[index];
        index--;
        short b = values[index];
        values[index] = (short) (a/b);
    }

}

