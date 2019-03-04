package pusztai.thomas.architecture.fog.uml.gen.services;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;

public class JavaTypeServices extends TypeServicesBase {
	
	/* (non-Javadoc)
	 * @see pusztai.thomas.architecture.fog.uml.gen.services.TypeServices#getQualifiedName(org.eclipse.uml2.uml.Type)
	 */
	@Override
	public String getQualifiedName(Type type) {
		if (type instanceof PrimitiveType) {
			return getPrimitiveTypeName((PrimitiveType) type);
		}
		String pkg = type.getPackage().getQualifiedName().toLowerCase().replaceAll("::", ".");
		return pkg.concat(".").concat(type.getName());
	}
	
	/**
	 * Gets the Java type name for the specified UML PrimitiveType.
	 */
	@Override
	protected String getPrimitiveTypeName(PrimitiveType type) {
		// The implementation of this method is very ugly, but
		// I was not able to find another way of getting the UML type name.
		
		String strRep = type.toString();
		int typeNameStart = strRep.lastIndexOf('#') + 1;
		int typeNameEnd = strRep.lastIndexOf(')');
		if (typeNameStart == 0 || typeNameEnd == -1) {
			throw new RuntimeException("Unknown PrimitiveType: " + strRep);
		}
		
		String umlName = strRep.substring(typeNameStart, typeNameEnd);
		switch (umlName) {
		case "Integer":
			return "int";
		case "Real":
			return "double";
		case "String":
			return "String";
		case "Boolean":
			return "boolean";
		default:
			throw new RuntimeException("Unknown PrimitiveType: " + strRep);
		}
	}
	
	/**
	 * Gets a collection type name for the specified typeName.
	 */
	@Override
	protected String getCollectionTypeName(TypedElement typedElement, String typeName) {
		switch (typeName) {
		case "int":
			typeName = "Interger";
			break;
		case "double":
			typeName = "Double";
			break;
		case "boolean":
			typeName = "Boolean";
			break;
		default:
			break;
		}
		
		return "List<".concat(typeName).concat(">");
	}
	
}
