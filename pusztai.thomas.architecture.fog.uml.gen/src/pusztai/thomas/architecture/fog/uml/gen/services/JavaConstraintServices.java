package pusztai.thomas.architecture.fog.uml.gen.services;

public class JavaConstraintServices extends ConstraintServices {

	@Override
	protected String getReplacementForFirstLevelVar(String varName) {
		return varName;
	}

	@Override
	protected String getReplacementForPropertyName(String propName) {
		char[] propNameChars = propName.toCharArray();
		propNameChars[0] = Character.toUpperCase(propNameChars[0]);
		return "get".concat(new String(propNameChars)).concat("()");
	}

	@Override
	protected String getPropertyAccessOperator() {
		return ".";
	}

	@Override
	protected String getReplacementForCompleteMethodArg(String arg) {
		return "((Number) ".concat(arg).concat(").intValue()");
	}

	@Override
	protected String getSelfReplacement() {
		return "self";
	}

//	@Override
//	protected String getAdditionOpReplacement() {
//		return "Math.addExact";
//	}
//
//	@Override
//	protected String getSubtractionOpReplacement() {
//		return "Math.subtractExact";
//	}
//
//	@Override
//	protected String getMultiplicationOpReplacement() {
//		return "Math.multiplyExact";
//	}
//
//	@Override
//	protected String getDivisionOpReplacement() {
//		return "Math.divideExact";
//	}

}
