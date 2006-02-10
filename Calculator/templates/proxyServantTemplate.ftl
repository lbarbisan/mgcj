package ${package};

<#list imports as import>
import ${import};
</#list> 

public class CalculatorImpl extends CorbaCalculatorPOA {

	private static final byte[] BYTE = new byte[0];

	public static final byte ZERO = (byte) 0;

	private CFlex32CardService javacard;

	private ResponseAPDU result;

	public CalculatorImpl(CFlex32CardService javacard) {
		super();
		this.javacard = javacard;
	}
	
<#list methods as method>
	${method} {
		try {
			sendAPDU(Calculator.DIV, BYTE,0);
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
	}
</#list> 

${class.simpleName}

	public ResponseAPDU sendAPDU(byte instruction, byte[] buffer, int length) throws CardTerminalException {
		try {
			result = javacard.sendAPDU(new ISOCommandAPDU(Calculator.CALCULATOR_CLA, instruction, ZERO, ZERO, buffer, length));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}