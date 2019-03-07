package pusztai.thomas.architecture.fog.uml.gen.services;

public class CConstraintServices extends ConstraintServices {

	@Override
	protected String getReplacementForFirstLevelVar(String varName) {
		return toLowerFirst(varName);
	}

	@Override
	protected String getReplacementForPropertyName(String propName) {
		return this.toLowerFirst(propName);
	}

	@Override
	protected String getReplacementForCompleteMethodArg(String arg) {
		return arg;
	}

	@Override
	protected String getPropertyAccessOperator() {
		return ".";
	}

	@Override
	protected String getSelfReplacement() {
		return "*target";
	}
	
	private String toLowerFirst(String str) {
		char[] chars = str.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);
		return new String(chars);
	}

}
