package pusztai.thomas.architecture.fog.uml.gen.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.TypedElement;

/**
 * Used to manage the imports for a class or interface.
 */
public class ImportsSet {

	private Set<Type> imports = new HashSet<>();
	private Package classifierPkg;
	
	/**
	 * Creates a new ImportsSet without a classifierPackage, such that all non primitive types will be imported.
	 */
	public ImportsSet() {
		this(null);
	}
	
	/**
	 * 
	 * @param classifierPackage the Package of the class or interface, for which this ImportsSet is created
	 */
	public ImportsSet(Package classifierPackage) {
		this.classifierPkg = classifierPackage;
	}
	
	/**
	 * Adds type of the specified TypedElement to the set of imports if it is not a primitive type or
	 * part of the same package as the host classifier.
	 */
	public boolean addImport(TypedElement typedElement) {
		Type type = typedElement.getType();
		if (!(type instanceof PrimitiveType) && (classifierPkg == null || !classifierPkg.equals(type.getPackage()))) {
			return imports.add(type);
		} else {
			return false;
		}
	}
	
	/**
	 * @return a sorted list of types that need to be imported.
	 */
	public List<Type> getImports() {
		List<Type> importsList = new ArrayList<>(imports);
		importsList.sort((typeA, typeB) -> typeA.getQualifiedName().compareToIgnoreCase(typeB.getQualifiedName()));
		return importsList;
	}
	
}
