package ${package};

<#list imports as import>
import ${import};
</#list> 

<#assign shortname = class.simpleName?replace('Operations','') />
public class ${shortname}Impl extends ${shortname}POA {

	private static final byte[] BYTE = new byte[0];
	
<#list class.declaredMethods as method>
	${method.returnType} ${method.name} (<#assign i = 0 /><#list method.parameterTypes as param>${param.name} arg${i}<#if i < (method.parameterTypes?size - 1)>, </#if><#assign i = i+1 /></#list>) {
		try {
			sendAPDU(${shortname}Constant.${method.name?upper_case}, BYTE, 0);
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
	}
		
</#list>

	public ResponseAPDU sendAPDU(byte instruction, byte[] buffer, int length) throws CardTerminalException {
		try {
			result = javacard.sendAPDU(new ISOCommandAPDU(Calculator.CALCULATOR_CLA, instruction, ZERO, ZERO, buffer, length));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}