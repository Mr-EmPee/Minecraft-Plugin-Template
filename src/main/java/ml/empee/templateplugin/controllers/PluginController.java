package ml.empee.templateplugin.controllers;

import ml.empee.commandsManager.CommandManager;
import ml.empee.commandsManager.command.CommandExecutor;
import ml.empee.commandsManager.command.annotations.CommandNode;
import ml.empee.ioc.Bean;
import ml.empee.templateplugin.Permissions;

/** Controller used for managing the plugin. **/

@CommandNode(label = "", permission = Permissions.ADMIN, exitNode = false)
public class PluginController extends CommandExecutor implements Bean {

  /** IoC Constructor **/
  public PluginController(CommandManager commandManager) {
    commandManager.registerCommand(this);
  }

}
