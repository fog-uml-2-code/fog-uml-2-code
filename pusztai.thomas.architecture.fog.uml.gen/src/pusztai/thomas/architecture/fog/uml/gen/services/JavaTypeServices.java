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
		String umlName = type.getName();
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
			throw new RuntimeException("Unknown PrimitiveType: " + umlName);
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
