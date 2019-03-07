package pusztai.thomas.architecture.fog.uml.gen.services;

import java.util.function.Consumer;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Stereotype;

/**
 * Provides helper methods for UML models.
 */
public class UmlUtils {

	/**
	 * Executes the specified action for all directly and indirectly
	 * nested packages in the root package. There is no defined iteration order.
	 */
	public static void forAllNestedPackages(Package root, Consumer<Package> action) {
		root.getNestedPackages().forEach(nestedPackage -> {
			action.accept(nestedPackage);
			forAllNestedPackages(nestedPackage, action);
		});
	}
	
	/**
	 * Executes the specified action for all classes in the container package and
	 * all direct and indirect subpackages. Classes nested within another class are ignored.
	 */
	public static void forAllClasses(Package container, Consumer<Class> action) {
		forAllElements(Class.class, container, classElement -> {
			if (!(classElement instanceof Behavior) && !(classElement instanceof Component) &&
					!(classElement instanceof Node) && !(classElement instanceof Stereotype)) {
				action.accept(classElement);
			}
		});
	}
	
	/**
	 * Executes the specified action for all interfaces in the container package and
	 * all direct and indirect subpackages. Interfaces nested within another interface or class are ignored.
	 */
	public static void forAllInterfaces(Package container, Consumer<Interface> action) {
		forAllElements(Interface.class, container, action);
	}
	
	/**
	 * Executes the specified action for all activities in the container package and
	 * all direct and indirect subpackages.
	 */
	public static void forAllActivities(Package container, Consumer<Activity> action) {
		forAllElements(Activity.class, container, action);
	}
	
	/**
	 * @return the root Model that owns the element
	 */
	public static Model getRootModel(PackageableElement element) {
		Package pkg = element.getNearestPackage();
		Package parent;
		while ((parent = pkg.getNestingPackage()) != null) {
			pkg = parent;
		}
		if (pkg instanceof Model) {
			return (Model) pkg;
		} else {
			throw new RuntimeException("No root model found.");
		}
	}
	
	/**
	 * Executes the specified action for all elements of the specified type in
	 * the container package and all direct and indirect subpackages. Elements, which
	 * are not directly part of a package (e.g., a nested class) are ignored.
	 */
	private static <T extends Element> void forAllElements(java.lang.Class<T> type, Package container, Consumer<T> action) {
		Consumer<Element> elementAction = element -> {
			if (type.isInstance(element)) {
				action.accept(type.cast(element));
			}
		};
		
		container.getOwnedElements().forEach(elementAction);
		forAllNestedPackages(container, nestedPackage -> {
			nestedPackage.getOwnedElements().forEach(elementAction);
		});
	}
	
}
