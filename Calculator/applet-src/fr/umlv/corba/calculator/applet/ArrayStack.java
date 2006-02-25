package fr.umlv.corba.calculator.applet;


public class ArrayStack implements Stack {

	private short[] array;
	private static final short INC = (short)10;
	private short index = (short)0;
	private short maxSize ;
	
	public ArrayStack() {
		this.array = new short[INC];
		this.index = (short)0;
		this.maxSize = INC;
	}

	public void push(short obj) {
		array[(short)(index++)] = obj;
		
		if(index == maxSize) {
			short[] tmp = new short[(short)(maxSize+INC)];
			maxSize += INC;
			for (short i = 0; i < (short)array.length; i++) {
				tmp[i] = array[i];
			}
			array = tmp;
		}
	}

	public short pop() {
		return array[(short)(--index)];
	}

	public short top() {
		return array[(short)(index-(short)1)];
	}

	public boolean isEmpty() {
		return index == (short)0;
	}

	public void clear() {
		index = (short)0;
	}

	public short size() {
		return index;
	}
}
