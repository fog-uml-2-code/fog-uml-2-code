package pusztai.thomas.architecture.fog.uml.gen.services;

import java.util.List;

import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;

/**
 * Base services that need to be implemented for every target language.
 */
public interface TypeServices {

	/**
	 * Gets a list of all imports required by the specified class or interface.
	 * @param classifier The classifier for which to generate the imports list.
	 * @param attributes All attributes of the classifier, including those from association ends.
	 * 	For some reason, classifier.getAllAttributes() does not include attributes from association ends,
	 * 	so we extract them using OCL in fogUmlServices.mtl and pass them as a parameter.
	 */
	List<Type> getAllRequiredImports(Classifier classifier, List<Property> attributes);
	
	/**
	 * Gets a list of all imports required by the specified CallOperationAction.
	 */
	List<Type> getAllRequiredImports(CallOperationAction action);

	/**
	 * Gets the qualified name for use in Java.
	 */
	String getQualifiedName(Type type);

	/**
	 * Gets the name of the return type of the operation.
	 */
	String getReturnTypeName(Operation op);

	/**
	 * Gets the name of the specified type.
	 */
	String getTypeName(TypedElement typedElement);

}