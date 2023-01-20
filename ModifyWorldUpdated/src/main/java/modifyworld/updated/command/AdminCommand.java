package modifyworld.updated.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.Subcommand;
import modifyworld.updated.Modifyworld;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandAlias("modifyworld|mw")
public class AdminCommand extends BaseCommand {

    @Subcommand("reload")
    @Description("Перезагрузка конфига плагина")
    @CommandPermission("modifyworld.reload")
    public static void onReload(CommandSender player) {
        Modifyworld.getInstance().reloadPlugin();
        player.sendMessage("Конфиг был перезагружен.");
    }
}
