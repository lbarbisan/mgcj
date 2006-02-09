package fr.umlv.corba.calculator.applet;

public interface Stack {
	
	void push(short obj);

	short pop();

	short first();
	
	boolean isEmpty();
	
	void clear();
	
	short size();
}
