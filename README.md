![](https://rapidus-info.webnode.com/_files/200000003-4d08d4d08f/AutoPlug%20GitHub%20Header%20800x80.png)
## Links
- [AutoPlug-Releases](https://github.com/Osiris-Team/AutoPlug-Releases)
- [AutoPlug-Client](https://github.com/Osiris-Team/AutoPlug-Client)
- [AutoPlug-Client-Development](https://bit.ly/acprogress)
- [AutoPlug-Plugin-Development](https://bit.ly/approgress)
- [Spigot](https://www.spigotmc.org/members/osiristeam.935748/) TODO CHANGE THIS
- [Discord](https://discord.com/invite/GGNmtCC)

## AutoPlug-Plugin | Installation
(pre-requisite) [AutoPlug-Client](https://www.spigotmc.org/resources/autoplug-automatic-plugin-server-java-self-updater.78414/) installed.
 1. Download latest version of AutoPlug-Plugin from [here]() TODO INSERT LINK HERE.
 2. Stop your server and move the downloaded jar into the /plugins directory.
 3. Start your server.

## AutoPlug-Plugin | What can it do?
Its actually pretty straightforward. It enables you to execute AutoPlug-Client console commands from in-game (even over an in-game GUI).

## AutoPlug-Plugin | Commands
```yaml
commands:

  .:
    description: Opens the AutoPlug-Plugin-GUI.
    usage: /.
    permission: autoplug.plugin.gui

  .restart:
    description: Restarts the server.
    usage: /.restart
    permission: autoplug.plugin.restart

  .r:
    description: Restarts the server. (Shortcut)
    usage: /.r
    permission: autoplug.plugin.restart

  .stop:
    description: Stops the server.
    usage: /.stop
    permission: autoplug.plugin.stop

  .st:
    description: Stops the server. (Shortcut)
    usage: /.st
    permission: autoplug.plugin.stop

  .stop both:
    description: Stops the server and the AutoPlug-Client.
    usage: /.stb
    permission: autoplug.plugin.stopboth

  .stb:
    description: Stops the server and the AutoPlug-Client. (Shortcut)
    usage: /.stb
    permission: autoplug.plugin.stopboth
```

## AutoPlug-Plugin | Contribute

If you have never contributed before, we recommend this [Beginners Article](https://www.jetbrains.com/help/idea/contribute-to-projects.html).
If you are planning to make big changes, create an issue first, where you explain what you want to do. Thank you in advance for every
contribution!

- Written in [Java](https://java.com/), with [JDK 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html), inside of [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- Built with [Maven](https://maven.apache.org/), profiles: clean package
- `AutoPlug-Plugin.jar` gets exported to the `/target` folder

If you don't know how to import a GitHub project, check out this guide:

- For IntelliJ IDEA checkout the [Cloning Guide](https://blog.jetbrains.com/idea/2020/10/clone-a-project-from-github/)

## AutoPlug-Plugin | Libraries
- [Dream-Yaml](https://github.com/Osiris-Team/Dream-Yaml) is used for handling YAML files.
- In-Game GUIs created with [SmartInvs](https://github.com/MinusKube/SmartInvs).
