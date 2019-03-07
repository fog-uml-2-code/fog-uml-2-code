package pusztai.thomas.architecture.fog.uml.gen.services;

import java.util.List;

import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.MultiplicityElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;

import pusztai.thomas.architecture.fog.uml.gen.utils.ImportsSet;

public abstract class TypeServicesBase implements TypeServices {

	/* (non-Javadoc)
	 * @see pusztai.thomas.architecture.fog.uml.gen.services.TypeServices#getAllRequiredImports(org.eclipse.uml2.uml.Classifier, java.util.List)
	 */
	@Override
	public List<Type> getAllRequiredImports(Classifier classifier, List<Property> attributes) {
		ImportsSet imports = new ImportsSet(classifier.getPackage());
		
		attributes.forEach(attr -> imports.addImport(attr));
		classifier.getAllOperations().forEach(op -> {
			op.getOwnedParameters().forEach(param -> imports.addImport(param));
		});
		
		return imports.getImports();
	}
	
	@Override
	public List<Type> getAllRequiredImports(CallOperationAction action) {
		ImportsSet imports = new ImportsSet();
		action.getOperation().getOwnedParameters().forEach(param -> imports.addImport(param));
		return imports.getImports();
	}
	
	/* (non-Javadoc)
	 * @see pusztai.thomas.architecture.fog.uml.gen.services.TypeServices#getReturnTypeName(org.eclipse.uml2.uml.Operation)
	 */
	@Override
	public String getReturnTypeName(Operation op) {
		Parameter returnResult = op.getReturnResult();
		if (returnResult == null) {
			return getVoidTypeName();
		} else {
			return getTypeName(returnResult);
		}
	}

	/* (non-Javadoc)
	 * @see pusztai.thomas.architecture.fog.uml.gen.services.TypeServices#getTypeName(org.eclipse.uml2.uml.TypedElement)
	 */
	@Override
	public String getTypeName(TypedElement typedElement) {
		Type type = typedElement.getType();
		String typeName;
		if (type instanceof PrimitiveType) {
			PrimitiveType primitiveType = (PrimitiveType) type;
			typeName = getPrimitiveTypeName(primitiveType);
		} else {
			typeName = getNonPrimitiveTypeName(type);
		}
		
		if (typedElement instanceof MultiplicityElement) {
			MultiplicityElement multElement = (MultiplicityElement) typedElement;
			if (multElement.getUpper() > 1 || multElement.getUpper() == -1) {
				typeName = getCollectionTypeName(typedElement, typeName);
			}
		}
		return typeName;
	}
	
	/**
	 * Gets the language specific type name for the specified non primitive Type.
	 */
	protected String getNonPrimitiveTypeName(Type type) {
		return type.getName();
	}
	
	/**
	 * Gets the language specific type name for void
	 */
	protected String getVoidTypeName() {
		return "void";
	}
	
	/**
	 * Gets the language specific type name for the specified UML PrimitiveType.
	 */
	protected abstract String getPrimitiveTypeName(PrimitiveType type);
	
	/**
	 * Gets a collection type name for the specified typeName.
	 */
	protected abstract String getCollectionTypeName(TypedElement typedElement, String typeName);
	
}
