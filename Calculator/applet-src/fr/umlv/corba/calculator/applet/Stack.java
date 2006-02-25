package fr.umlv.corba.calculator.applet;

public interface Stack {
	
	void push(short obj);

	short pop();

	short top();
	
	boolean isEmpty();
	
	void clear();
	
	short size();
}
