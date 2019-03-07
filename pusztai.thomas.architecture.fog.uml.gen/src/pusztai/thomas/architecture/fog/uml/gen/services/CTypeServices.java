package pusztai.thomas.architecture.fog.uml.gen.services;

import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;

public class CTypeServices extends TypeServicesBase {
	
	/* (non-Javadoc)
	 * @see pusztai.thomas.architecture.fog.uml.gen.services.TypeServices#getQualifiedName(org.eclipse.uml2.uml.Type)
	 */
	@Override
	public String getQualifiedName(Type type) {
		if (type instanceof PrimitiveType) {
			return getPrimitiveTypeName((PrimitiveType) type);
		}
		return getNonPrimitiveTypeName(type);
	}
	
	/**
	 * Gets the C type name for the specified UML PrimitiveType.
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
			return "int_t";
		case "Real":
			return "real_t";
		case "String":
			return "string_t";
		case "Boolean":
			return "bool";
		default:
			throw new RuntimeException("Unknown PrimitiveType: " + strRep);
		}
	}
	
	/**
	 * Gets a collection type name for the specified typeName.
	 */
	@Override
	protected String getCollectionTypeName(TypedElement typedElement, String typeName) {
		return typeName.concat("[]");
	}
	
}
