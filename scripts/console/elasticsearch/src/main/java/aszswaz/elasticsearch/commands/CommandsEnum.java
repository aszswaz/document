package aszswaz.elasticsearch.commands;

/**
 * 基本指令集
 *
 * @author aszswaz
 * @date 2021/7/12 13:25:24
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public enum CommandsEnum {
    version(new Version(), "查看服务器的版本信息"),
    server(new Server(), "管理服务器的连接信息");

    private final Command command;
    private final String help;

    CommandsEnum(Command command, String help) {
        this.command = command;
        this.help = help;
    }

    public Command getCommand() {
        return command;
    }

    public String getHelp() {
        return help;
    }
}
