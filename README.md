# FogUML2Code

*FogUML2Code* is a code generator to support model-driven engineering of Fog/IoT applications, which are powered by REST services.


## Overview

Our approach is to model device REST interfaces and data in a fog application with UML class diagrams, and business processes within the application with UML activity diagrams. Those record the architecture of the application as well as constraints on its behavior at runtime.

FogUML2Code is able to generate correct-by-construction boilerplate code from the models defined. The generated artifact captures the fog computing architecture, is able to target heterogeneous implementation languages (currently C and Java) used throughout the application, and is completed by application developers.

This project includes a [Fog UML2 profile](./FogUMLProfile), which needs to be applied to a UML model in order to leverage FogUML2Code. It consists of three stereotypes and some helper elements. The stereotypes are used during the code generation process to gain information on how elements should be treated - they are:
* `DataModel`: designates a class or a package as being part of the data model of the fog application.
* `FogDevice`: informs the code generator that the interface, class, or package, to which it has been applied, will be deployed on a fog device. This stereotype contains some properties that can be used to customize the code generation. `targetLanguage` is the most important property (and the only one considered at the moment). It defines the programming language that will be used to generate code for this device ("C" or "Java").

FogUML2Code is an Eclipse plugin, using the Eclipse MDT UML2 metamodel, and is realized with [Acceleo](https://www.eclipse.org/acceleo/). The UML models for FogUML2Code can be designed with any editor targeting the Eclipse UML2 metamodel, e.g., [UML Designer](http://www.umldesigner.org/).


## Installation Instructions

To install and use FogUML2Code you need to follow these steps:
1. Download and install the [Eclipse Modeling Tools](https://www.eclipse.org/downloads/packages/) package.
2. Install *Acceleo* from the Eclipse Marketplace.
3. (optional) Install the [UML Designer](http://www.umldesigner.org/) Eclipse plugin or another UML editor for Eclipse.
4. Clone the FogUML2Code repository.
5. Import all projects from this repository into your Eclipse workspace (File -> Import -> Existing Projects into Workspace).
6. Open `FogUML2Code.ui/plugin.xml` and on the *Overview* tab choose *Launch an Eclipse application*.


## Usage

To use FogUML2Code:
1. Create an empty Eclipse project (File -> New -> Other -> General -> Project).
2. Add a UML model to your project (Right click project -> New -> Other -> Example EMF Model Creation Wizards -> UML Model).
3. Open the newly created `.uml` file and load the `Fog` UML profile (UML Editor -> Load Resource -> Browse Workspace -> Select the Fog profile).
4. Apply the `Fog` profile by selecting your root model or package element and then clicking *UML Editor -> Package -> Apply Profile*.

You can now design your UML model.

Once you have finished designing your UML model, you can generate code by right clicking the `.uml` file and selecting *Acceleo Model to Text -> Generate FogUML2Code*. The code will be generated in the `src-gen` subfolder of the project that contains the UML model.

The generation process can be customized by creating a `codeGeneration.properties` file in your project. It contains a list of settings, which can be configured globally and individually for each module (REST controller or activity realization). The settings use the following scheme:

```
codegen.<module>.<setting_name> = <value>
```

By replacing `<module>` with `default` the global settings for all modules are set.
By replacing it with the name of a module (the name of the REST controller or activity), the global setting can be overridden for that module. For a full list of available settings and example for overriding, see [codeGeneration.properties](./FogUML2Code/src/pusztai/thomas/architecture/fog/uml/gen/properties/codeGeneration.properties).
