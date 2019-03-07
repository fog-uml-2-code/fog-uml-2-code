/*******************************************************************************
 * Copyright (c) 2008, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package pusztai.thomas.architecture.fog.uml.gen.ui.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;


/**
 * Main entry point of the 'Fog UML 2 Code' generation module.
 */
public class GenerateAll {

	/**
	 * The model URI.
	 */
	private URI modelURI;

	/**
	 * The output folder.
	 */
	private IContainer targetFolder;

	/**
	 * The other arguments.
	 */
	List<? extends Object> arguments;

	/**
	 * Constructor.
	 * 
	 * @param modelURI
	 *            is the URI of the model.
	 * @param targetFolder
	 *            is the output folder
	 * @param arguments
	 *            are the other arguments
	 * @throws IOException
	 *             Thrown when the output cannot be saved.
	 * @generated
	 */
	public GenerateAll(URI modelURI, IContainer targetFolder, List<? extends Object> arguments) {
		this.modelURI = modelURI;
		this.targetFolder = targetFolder;
		this.arguments = arguments;
	}

	/**
	 * Launches the generation.
	 *
	 * @param monitor
	 *            This will be used to display progress information to the user.
	 * @throws IOException
	 *             Thrown when the output cannot be saved.
	 * @generated
	 */
	public void doGenerate(IProgressMonitor monitor) throws IOException {
		if (!targetFolder.getLocation().toFile().exists()) {
			targetFolder.getLocation().toFile().mkdirs();
		}
		
		monitor.subTask("Loading...");
		pusztai.thomas.architecture.fog.uml.gen.main.FogUml2Code gen0 = new pusztai.thomas.architecture.fog.uml.gen.main.FogUml2Code(modelURI, targetFolder.getLocation().toFile(), arguments);
		monitor.worked(1);
		String generationID = org.eclipse.acceleo.engine.utils.AcceleoLaunchingUtil.computeUIProjectID("pusztai.thomas.architecture.fog.uml.gen", "pusztai.thomas.architecture.fog.uml.gen.main.FogUml2Code", modelURI.toString(), targetFolder.getFullPath().toString(), new ArrayList<String>());
		gen0.setGenerationID(generationID);
		gen0.doGenerate(BasicMonitor.toMonitor(monitor));
			
//		EObject model = gen0.getModel();
//		if (model != null) {
//				
//			
//			monitor.subTask("Loading...");
//			pusztai.thomas.architecture.fog.uml.gen.services.CConstraintServices gen1 = new pusztai.thomas.architecture.fog.uml.gen.services.CConstraintServices(model, targetFolder.getLocation().toFile(), arguments);
//			monitor.worked(1);
//			generationID = org.eclipse.acceleo.engine.utils.AcceleoLaunchingUtil.computeUIProjectID("pusztai.thomas.architecture.fog.uml.gen", "pusztai.thomas.architecture.fog.uml.gen.services.CConstraintServices", modelURI.toString(), targetFolder.getFullPath().toString(), new ArrayList<String>());
//			gen1.setGenerationID(generationID);
//			gen1.doGenerate(BasicMonitor.toMonitor(monitor));
//			
//			monitor.subTask("Loading...");
//			pusztai.thomas.architecture.fog.uml.gen.services.CTypeServices gen2 = new pusztai.thomas.architecture.fog.uml.gen.services.CTypeServices(model, targetFolder.getLocation().toFile(), arguments);
//			monitor.worked(1);
//			generationID = org.eclipse.acceleo.engine.utils.AcceleoLaunchingUtil.computeUIProjectID("pusztai.thomas.architecture.fog.uml.gen", "pusztai.thomas.architecture.fog.uml.gen.services.CTypeServices", modelURI.toString(), targetFolder.getFullPath().toString(), new ArrayList<String>());
//			gen2.setGenerationID(generationID);
//			gen2.doGenerate(BasicMonitor.toMonitor(monitor));
//			
//			monitor.subTask("Loading...");
//			pusztai.thomas.architecture.fog.uml.gen.services.FogUmlServices gen3 = new pusztai.thomas.architecture.fog.uml.gen.services.FogUmlServices(model, targetFolder.getLocation().toFile(), arguments);
//			monitor.worked(1);
//			generationID = org.eclipse.acceleo.engine.utils.AcceleoLaunchingUtil.computeUIProjectID("pusztai.thomas.architecture.fog.uml.gen", "pusztai.thomas.architecture.fog.uml.gen.services.FogUmlServices", modelURI.toString(), targetFolder.getFullPath().toString(), new ArrayList<String>());
//			gen3.setGenerationID(generationID);
//			gen3.doGenerate(BasicMonitor.toMonitor(monitor));
//			
//			monitor.subTask("Loading...");
//			pusztai.thomas.architecture.fog.uml.gen.services.JavaConstraintServices gen4 = new pusztai.thomas.architecture.fog.uml.gen.services.JavaConstraintServices(model, targetFolder.getLocation().toFile(), arguments);
//			monitor.worked(1);
//			generationID = org.eclipse.acceleo.engine.utils.AcceleoLaunchingUtil.computeUIProjectID("pusztai.thomas.architecture.fog.uml.gen", "pusztai.thomas.architecture.fog.uml.gen.services.JavaConstraintServices", modelURI.toString(), targetFolder.getFullPath().toString(), new ArrayList<String>());
//			gen4.setGenerationID(generationID);
//			gen4.doGenerate(BasicMonitor.toMonitor(monitor));
//			
//			monitor.subTask("Loading...");
//			pusztai.thomas.architecture.fog.uml.gen.services.JavaTypeServices gen5 = new pusztai.thomas.architecture.fog.uml.gen.services.JavaTypeServices(model, targetFolder.getLocation().toFile(), arguments);
//			monitor.worked(1);
//			generationID = org.eclipse.acceleo.engine.utils.AcceleoLaunchingUtil.computeUIProjectID("pusztai.thomas.architecture.fog.uml.gen", "pusztai.thomas.architecture.fog.uml.gen.services.JavaTypeServices", modelURI.toString(), targetFolder.getFullPath().toString(), new ArrayList<String>());
//			gen5.setGenerationID(generationID);
//			gen5.doGenerate(BasicMonitor.toMonitor(monitor));
//		}
			
		
	}

}
