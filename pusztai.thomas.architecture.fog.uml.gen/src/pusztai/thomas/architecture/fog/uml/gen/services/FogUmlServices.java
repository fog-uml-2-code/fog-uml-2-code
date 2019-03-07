package pusztai.thomas.architecture.fog.uml.gen.services;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.AcceptEventAction;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.EnumerationLiteral;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InitialNode;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Stereotype;

public class FogUmlServices {
	
	public String STEREOTYPE_ACTIVITY_REALIZATION = "Fog::ActivityRealization";
	public String STEREOTYPE_FOG_DEVICE = "Fog::FogDevice";
	public String STEREOTYPE_MODEL_TYPE = "Fog::DataModel";
	
	public String PROPERTY_REALIZED_ACTIVITY = "realizedActivity";
	public String PROPERTY_TARGET_LANGUAGE= "targetLanguage";
	
	/**
	 * Gets a list of all model classes (classes that do not implement 
	 * an interface and have the stereotype ModelType applied
	 * or which reside in a package that has this stereotype applied).
	 * @return a list of all model classes in the specified model.
	 */
	public List<Class> getAllModelClasses(Model model) {
		List<Class> modelClasses = new ArrayList<>();
		UmlUtils.forAllClasses(model, classElement -> {
			if (classElement.getImplementedInterfaces().isEmpty() && (
					classElement.getAppliedStereotype(STEREOTYPE_MODEL_TYPE) != null ||
					classElement.getPackage().getAppliedStereotype(STEREOTYPE_MODEL_TYPE) != null)) {
				modelClasses.add(classElement);
			}
		});
		return modelClasses;
	}
	
	/**
	 * Gets a list of all REST endpoint interfaces classes in the specified model.
	 * All interfaces, which have one or more derived interfaces are excluded, because they are not endpoints.
	 * @return a list of all REST endpoint interfaces in the specified model.
	 */
	public List<Interface> getAllRestInterfaces(Model model) {
		List<Interface> restInterfaces = new ArrayList<>();
		
		UmlUtils.forAllInterfaces(model, interfaceElement -> {
			if (getDerivedInterfaces(interfaceElement).isEmpty()) {
				restInterfaces.add(interfaceElement);
			}
		});
		
		return restInterfaces;
	}
	
	/**
	 * @return a list of all activities in the specified model.
	 */
	public List<Activity> getAllActivities(Model model) {
		List<Activity> activities = new ArrayList<>();
		UmlUtils.forAllActivities(model, activity -> activities.add(activity));
		return activities;
	}
	
	/**
	 * @return a list of classes that directly implement the specified interface.
	 */
	public List<Class> getRealizingClasses(Interface interfaceElement) {
		List<Class> classes = new ArrayList<>();
		interfaceElement.getTargetDirectedRelationships().forEach(rel -> {
			if (rel instanceof Realization) {
				rel.getSources().forEach(source -> classes.add((Class) source)); 
			}
		});
		return classes;
	}
	
	/**
	 * @return the Activity that this package realizes
	 */
	public Activity getRealizedActivity(Package pkg) {
		return this.getRealizedActivityInternal(pkg);
	}
	
	/**
	 * @return the Activity, for which this classifier is part of its realization.
	 */
	public Activity getRealizedActivity(Classifier classifier) {
		return this.getRealizedActivityInternal(classifier);
	}
	
	/**
	 * @return a list of all interfaces that are directly invoked by the specified activity.
	 */
	public List<Interface> getAllInvokedInterfaces(Activity activity) {
		return getAllInvokedTargets(activity, Interface.class);
	}
	
	/**
	 * @return a list of all service classes (classes that do not implement a REST interface)
	 * that are directly invoked by the specified activity or contained in the activity's realization package.
	 */
	public List<Class> getAllServiceClasses(Activity activity) {
		Set<Class> classes = new HashSet<>();
		Consumer<Class> addIfServiceClass = (Class classElement) -> {
			if (classElement.allRealizedInterfaces().isEmpty() && classElement.getAppliedStereotype(STEREOTYPE_MODEL_TYPE) == null) {
				classes.add(classElement);
			}
		};
		
		Package pkg = getActivityRealization(activity);
		UmlUtils.forAllClasses(pkg, addIfServiceClass);
		List<Class> invokedClasses = getAllInvokedTargets(activity, Class.class);
		invokedClasses.forEach(addIfServiceClass);
	
		List<Class> ret = new ArrayList<>(classes.size());
		classes.forEach(serviceClass -> ret.add(serviceClass));
		return ret;
	}
	
	/**
	 * @return the realizingClass if it is not null, otherwise the baseInterface.
	 */
	public Classifier getDefiningClassifier(Interface baseInterface, Class realizingClass) {
		return realizingClass != null ? realizingClass : baseInterface;
	}
	
	/**
	 * @return the package that realizes the specified activity.
	 */
	public Package getActivityRealization(Activity activity) {
		Model model = UmlUtils.getRootModel(activity);
		AtomicReference<Package> activityRealizationPkg = new AtomicReference<>();
		UmlUtils.forAllNestedPackages(model, pkg -> {
			Stereotype activityRealization = pkg.getAppliedStereotype(STEREOTYPE_ACTIVITY_REALIZATION);
			if (activityRealization != null && activity.equals(pkg.getValue(activityRealization, PROPERTY_REALIZED_ACTIVITY))) {
				activityRealizationPkg.set(pkg);
			}
		});
		return activityRealizationPkg.get();
	}
	
	/**
	 * @return the target programming language for the specified activity's realization.
	 */
	public String getActivityRealizationTargetLanguage(Package activityRealization) {
		return getTargetLanguage(activityRealization);
	}
	
	/**
	 * @return the target programming language for the specified restController (= defining classifier for the REST endpoint controller).
	 */
	public String getRestControllerTargetLanguage(Classifier restController) {
		return getTargetLanguage(restController);
	}
	
	/**
	 * @return the InitialNode of the specified Activity.
	 */
	public InitialNode getActivityInitialNode(Activity activity) {
		AtomicReference<InitialNode> initialNode = new AtomicReference<>();
		activity.getNodes().forEach(node -> {
			if (node instanceof InitialNode) {
				InitialNode prevInitialNode = initialNode.get();
				if (prevInitialNode != null && prevInitialNode != node) {
					throw new RuntimeException("Too many InitialNodes found in activity: '" + activity.getName() + "'. Each activity must have exactly one InitialNode.");
				}
				initialNode.set((InitialNode) node);
			}
		});
		
		InitialNode ret = initialNode.get();
		if (ret == null) {
			throw new RuntimeException("No InitialNode found in activity: '" + activity.getName() + "'. Each activity must have exactly one InitialNode.");
		}
		return ret;
	}
	
	/**
	 * @return the name of the class that will be the handler for the specified ActivityNode.
	 */
	public String getHandlerClassName(ActivityNode node, Activity activity) {
		String nodeName = node.getName();
		if (nodeName == null || nodeName.length() == 0) {
			ActivityNode predecessor = node;
			do {
				EList<ActivityEdge> incoming = predecessor.getIncomings();
				if (!incoming.isEmpty()) {
					predecessor = incoming.get(0).getSource();
					nodeName = predecessor.getName();
				} else {
					nodeName = "Start";
				}
			} while (nodeName == null || nodeName.length() == 0);
			nodeName = "After".concat(nodeName).concat("_Id").concat(getActivityNodeId(node, activity));
		}
		nodeName = nodeName.replaceAll(" ", "");
		if (!Path.isValidPosixSegment(nodeName) || !Path.isValidWindowsSegment(nodeName)) {
			throw new RuntimeException(nodeName + " would result in an invalid file name.");
		}
		return nodeName;
	}
	
	/**
	 * @return the ID of the specified node in the specified activity.
	 */
	public String getActivityNodeId(ActivityNode node, Activity activity) {
		int index = activity.getNodes().indexOf(node);
		if (index == -1) {
			throw new RuntimeException("ActivityNode " + node.getName() + " could not be found in Activity " + activity.getName());
		}
		return Integer.toString(index);
	}
	
	/**
	 * @return The name of the decision method for the specified edge.
	 */
	public String getEdgeDecisionMethodName(ActivityEdge edge) {
		String edgeName = edge.getName();
		if (edgeName != null && edgeName.length() > 0) {
			edgeName = edgeName.replaceAll("<", "LessThan");
			edgeName = edgeName.replaceAll(">", "GreaterThan");
			edgeName = edgeName.replaceAll("==", "Equal");
			edgeName = edgeName.replaceAll("\\+", "Plus");
			edgeName = edgeName.replaceAll("-", "Minus");
			edgeName = edgeName.replaceAll("\\*", "Times");
			edgeName = edgeName.replaceAll("\\/", "DividedBy");
			edgeName = edgeName.replaceAll("!", "Not");
			edgeName = edgeName.replaceAll("\\W", ""); // Remove all non word characters.
			if (edgeName.matches("^\\d")) {
				edgeName = "is".concat(edgeName);
			}
		}
		if (edgeName == null || edgeName.length() == 0) {
			int edgeIndex = edge.getSource().getOutgoings().indexOf(edge);
			edgeName = "isEdge".concat(Integer.toString(edgeIndex));
		}
		if (edgeName.compareToIgnoreCase("else") == 0) {
			edgeName = "else";
		}
		return edgeName;
	}
	
	/**
	 * @return the duration of the interval of the timeEvent as milliseconds.
	 */
	public long parseDurationToMsec(AcceptEventAction timeEvent) {
		String durationStr = timeEvent.getName();
		Duration duration = Duration.parse(durationStr);
		return duration.toMillis();
	}
	
	/**
	 * Throws a RuntimeException with the specified error message.
	 */
	public String throwError(String msg) {
		throw new RuntimeException(msg);
	}
	
	/**
	 * @return a list of directly derived interfaces of the specified interface.
	 */
	private List<Interface> getDerivedInterfaces(Interface interfaceElement) {
		List<Interface> derivedInterfaces = new ArrayList<>();
		interfaceElement.getTargetDirectedRelationships().forEach(rel -> {
			if (rel instanceof Generalization) {
				rel.getSources().forEach(target -> {
					if (target instanceof Interface) {
						derivedInterfaces.add((Interface) target);
					}
				});
			}
		});
		return derivedInterfaces;
	}
	
	private Activity getRealizedActivityInternal(PackageableElement element) {
		Stereotype activityRealization = element.getAppliedStereotype(STEREOTYPE_ACTIVITY_REALIZATION);
		if (activityRealization == null && element instanceof Classifier) {
			Package pkg = ((Classifier) element).getPackage();
			activityRealization = pkg.getAppliedStereotype(STEREOTYPE_ACTIVITY_REALIZATION);
			element = pkg;
		}
		if (activityRealization != null) {
			Object activity = element.getValue(activityRealization, PROPERTY_REALIZED_ACTIVITY);
			if (activity != null) {
				return (Activity) activity;
			}
		}
		return null;
	}
	
	private String getTargetLanguage(Element element) {
		Stereotype fogDevice = element.getAppliedStereotype(STEREOTYPE_FOG_DEVICE);
		if (fogDevice != null) {
			Object targetLanguage = element.getValue(fogDevice, PROPERTY_TARGET_LANGUAGE);
			if (targetLanguage != null && targetLanguage instanceof EnumerationLiteral) {
				return ((EnumerationLiteral) targetLanguage).getName();
			}
		}
		return "Java";
	}
	
	/**
	 * @return a list of all targets that are directly invoked by the specified activity.
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getAllInvokedTargets(Activity activity, java.lang.Class<T> targetType) {
		Set<T> targets = new HashSet<>();
		activity.getNodes().forEach(node -> {
			if (node instanceof CallOperationAction) {
				CallOperationAction callOpAction = (CallOperationAction) node;
				Element owner = callOpAction.getOperation().getOwner();
				if (targetType.isInstance(owner)) {
					targets.add((T) owner);
				}
			}
		});
		
		List<T> ret = new ArrayList<>(targets.size());
		targets.forEach(target -> ret.add(target));
		return ret;
	}

}
