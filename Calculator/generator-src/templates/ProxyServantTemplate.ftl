package ${package};

import opencard.cflex.service.CFlex32CardService;
import opencard.core.terminal.CardTerminalException;
import opencard.core.terminal.ResponseAPDU;
import opencard.opt.terminal.ISOCommandAPDU;

import ${packagePrefix}constants.${className}Constants;
import ${packagePrefix}${className}POA;

public class ${className}Impl extends ${className}POA {

	private static final byte[] BYTE = new byte[0];
	public static final byte ZERO = (byte) 0;
	private CFlex32CardService javacard;
	private ResponseAPDU result;

	public ${className}Impl(CFlex32CardService javacard) {
		super();
		this.javacard = javacard;
	}
	
<#list class.declaredMethods as method>
	/** 
	* ${method.name}
	**/
	public ${method.returnType} ${method.name}(<#assign i = 0 /><#list method.parameterTypes as param>${param.name} arg${i}<#if i < (method.parameterTypes?size - 1)>, </#if><#assign i = i+1 /></#list>) <#assign i = 0 /><#if method.exceptionTypes?size != 0 > throws </#if><#list method.exceptionTypes as param> ${param.name}<#if i < (method.exceptionTypes?size - 1)>, </#if><#assign i = i+1 /></#list>{
		// TODO Generated by MGCJ generator
		try {
			sendAPDU(${className}Constants.${method.name?upper_case}, BYTE, 0);
		} catch (CardTerminalException e) {
			e.printStackTrace();
		}
		<#switch method.returnType>
  			<#case "short">
		return 0;
    			<#break>
  			<#case "int">
		return 0;
    			<#break>
    		<#case "void">
    			<#break>
  			<#default>
  		return null;
		</#switch>
	}
</#list>

	/**
	 * send the APDU
	 */
	public ResponseAPDU sendAPDU(byte instruction, byte[] buffer, int length) throws CardTerminalException {
		try {
			result = javacard.sendAPDU(new ISOCommandAPDU(${className}Constants.${className?upper_case}_CLA, instruction, ZERO, ZERO, buffer, length));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}