package ${package};

public interface ${className}Constants {
	// The applet ${className} is designed to respond to the following
	// class of instructions.
	final static byte ${className?upper_case}_CLA = (byte) 0x${CLA};
	
	<#assign i = 10 />
	<#list class.declaredMethods as method>
	final static byte ${method.name?upper_case} = (byte)0x${i};
	<#assign i = i + 10 />
	</#list>
	final static byte SELECT = (byte)0xA4;
}
