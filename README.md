# ET project template

This is a project template for a greenfield Java project. Its chatbot is named _ET_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/et/ET.java` file, right-click it, and choose `Run ET.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see the following output:
   ```
   ____________________________________________________________
    _____ _____
   | ____|_   _|
   |  _|   | |
   | |___  | |
   |_____| |_|
   Hello, friend! I'm ET, a gentle visitor from far away.
   I may be a little lost, but I would be happy to help with your tasks.
   What can I do for you?
   ____________________________________________________________
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Creating and running the application JAR

The project uses the Shadow Gradle plugin to create an executable **fat JAR**: a single JAR that contains ET and all of its runtime dependencies.

1. From the project root, build the JAR:
   ```bash
   ./gradlew shadowJar
   ```
   On Windows, use `gradlew.bat shadowJar`.
1. Find the generated file at `build/libs/et.jar`.
1. Run ET with Java 25:
   ```bash
   java -jar build/libs/et.jar
   ```

You can also use `./gradlew clean shadowJar` when you want Gradle to remove previous build output before creating a fresh JAR.
