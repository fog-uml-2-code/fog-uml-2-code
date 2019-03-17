package pusztai.thomas.architecture.fog.uml.gen.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.OpaqueExpression;

public abstract class ConstraintServices {
	
	private Pattern regexCommentLines = Pattern.compile("()(^--.*$)", Pattern.MULTILINE);
	private Pattern regexAnd = Pattern.compile("(\\s)(and)(\\s)");
	private Pattern regexOr = Pattern.compile("(\\s)(or)(\\s)");
	private Pattern regexSingleEquals = Pattern.compile("(\\w|\\s)(=)(\\w|\\s)");
	private Pattern regexFirstLevelVars = Pattern.compile("(\\s|^)(\\b\\D\\w*)");
	private Pattern regexPropertyNames = Pattern.compile("(\\.)(\\w+)((?=(?:\\W|$))(?!\\())", Pattern.MULTILINE);
	private Pattern regexMethodArgs = Pattern.compile("(\\s|^)((?!self\\.|\\d|\\s|null\\b)[\\w\\(\\)\\\"\\.]+)", Pattern.MULTILINE);
	private Pattern regexSelf = Pattern.compile("([^\\.]|)(self)(\\.)");
	private Pattern regexPropertyAccessOperator = Pattern.compile("()(\\.)");
	private Pattern regexCrLf = Pattern.compile("(.?)(\r\n)(.?)");
	
// Currently only int parameters are supported for method arguments. To change this, modifications would be needed:
//
//	private Pattern regexMultiplications = Pattern.compile(assembleOperatorReplacementRegex("*"));
//	private Pattern regexDivisions = Pattern.compile(assembleOperatorReplacementRegex("/"));
//	private Pattern regexAdditions = Pattern.compile(assembleOperatorReplacementRegex("+"));
//	private Pattern regexSubtractions = Pattern.compile(assembleOperatorReplacementRegex("-"));
//	
//	/** Next steps for alternative with operator replacements:
//	 * - Modify regexes to include ))
//	 * - Replace comparison operators
//	 * - Write Class with helper methods and reference them in JavaConstraintServices (e.g., ConstraintsHelper.add(Number a, Number b) and ConstraintsHelper.greaterEqual(Comparable a, Comparable b)
//	 */
//	
//	private static String assembleOperatorReplacementRegex(String operator) {
//		String start = "(\\w[\\w\\d\\.\\(\\)]*)(\\s*\\";
//		String end = "\\s*)([\\w\\d\\.\\(\\)\\\"]+)";
//		return start.concat(operator).concat(end);
//	}
	
	public boolean checkIfConstraintsPresent(Classifier classifier) {
		if (!classifier.getOwnedRules().isEmpty()) {
			return true;
		}
		return classifier.getAllOperations().stream().anyMatch(op -> !op.getOwnedRules().isEmpty());
	}

	public String convertConstraintToCode(OpaqueExpression constraintSpec) {
		String constraint = constraintSpec.getBodies().get(0);
		constraint = executeStaticReplacement(constraint, regexCommentLines, "");
		constraint = executeStaticReplacement(constraint, regexAnd, "&&");
		constraint = executeStaticReplacement(constraint, regexOr, "||");
		constraint = executeStaticReplacement(constraint, regexSingleEquals, "==");
		constraint = replaceFirstLevelVars(constraint);
		constraint = replacePropertyNames(constraint);
		constraint = replaceMethodArgs(constraint);
		
//		constraint = replaceOperand(constraint, regexMultiplications, getMultiplicationOpReplacement());
//		constraint = replaceOperand(constraint, regexDivisions, getDivisionOpReplacement());
//		constraint = replaceOperand(constraint, regexAdditions, getAdditionOpReplacement());
//		constraint = replaceOperand(constraint, regexSubtractions, getSubtractionOpReplacement());
		
		constraint = executeStaticReplacement(constraint, regexSelf, getSelfReplacement());
		constraint = executeStaticReplacement(constraint, regexPropertyAccessOperator, getPropertyAccessOperator());
		
		// If the UML model was created on Windows, the constraints have Windows line endings, so we convert them to Unix line endings.
		// Otherwise we could have both types of line endings in one file.
		constraint = executeStaticReplacement(constraint, regexCrLf, "\n");
		
		return constraint;
	}
	
	protected String executeStaticReplacement(String input, Pattern pattern, String replacement) {
		Matcher matcher = pattern.matcher(input);
		StringBuffer sb = new StringBuffer(input.length());
		while (matcher.find()) {
			replaceCenterGroup(sb, matcher, replacement);
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	protected String replaceFirstLevelVars(String constraint) {
		Matcher matcher = regexFirstLevelVars.matcher(constraint);
		StringBuffer sb = new StringBuffer(constraint.length() * 2);
		
		while (matcher.find()) {
			String varName = matcher.group(2);
			String replacement;
			if (!varName.equals("self") && !varName.equals("null")) {
				replacement = getReplacementForFirstLevelVar(varName);
			} else {
				replacement = matcher.group();
			}
			replaceCenterGroup(sb, matcher, replacement);
		}
		
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	protected String replacePropertyNames(String constraint) {
		Matcher matcher = regexPropertyNames.matcher(constraint);
		StringBuffer sb = new StringBuffer(constraint.length() + 100);
		
		while (matcher.find()) {
			String propName = matcher.group(2);
			String replacement = getReplacementForPropertyName(propName);
			replaceCenterGroup(sb, matcher, replacement);
		}
		
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	protected String replaceMethodArgs(String constraint) {
		Matcher matcher = regexMethodArgs.matcher(constraint);
		StringBuffer sb = new StringBuffer(constraint.length() * 2);
		
		while (matcher.find()) {
			String methodArg = matcher.group(2);
			String replacement = getReplacementForCompleteMethodArg(methodArg);
			replaceCenterGroup(sb, matcher, replacement);
		}
		
		matcher.appendTail(sb);
		return sb.toString();
	}
	
//	protected String replaceOperand(String constraint, Pattern pattern, String replacementOperationName) {
//		Matcher matcher = pattern.matcher(constraint);
//		StringBuffer sb = new StringBuffer(constraint.length() * 2);
//		String replacementOpStart = replacementOperationName.concat("(");
//		
//		while (matcher.find()) {
//			String operandA = matcher.group(1);
//			String operandB = matcher.group(3);
//			String replacement = replacementOpStart.concat(operandA).concat(", ").concat(operandB).concat(")");
//			matcher.appendReplacement(sb, replacement);
//		}
//		
//		matcher.appendTail(sb);
//		return sb.toString();
//	}
	
	protected void replaceCenterGroup(StringBuffer sb, Matcher matcher, String replacement) {
		String prefix = matcher.group(1);
		String suffix;
		if (matcher.groupCount() == 3) {
			suffix = matcher.group(3);
		} else {
			suffix = "";
		}
		
		String group0Replacement = prefix.concat(replacement).concat(suffix);
		matcher.appendReplacement(sb, group0Replacement);
	}
	
	protected abstract String getReplacementForFirstLevelVar(String varName);
	
	protected abstract String getReplacementForPropertyName(String propName);
	
	protected abstract String getReplacementForCompleteMethodArg(String arg);
	
	protected abstract String getPropertyAccessOperator();
	
	protected abstract String getSelfReplacement();
	
//	protected abstract String getAdditionOpReplacement();
//	
//	protected abstract String getSubtractionOpReplacement();
//	
//	protected abstract String getMultiplicationOpReplacement();
//	
//	protected abstract String getDivisionOpReplacement();
	
}
