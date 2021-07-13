package aszswaz.elasticsearch;

import aszswaz.elasticsearch.commands.CommandsEnum;
import aszswaz.elasticsearch.container.Container;
import aszswaz.elasticsearch.util.Mybatis;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * es的命令行客户端
 *
 * @author aszswaz
 * @date 2021/7/12 13:13:41
 * @IDE IntelliJ IDEA
 */
@Slf4j
@SuppressWarnings("JavaDoc")
public class Elasticsearch {
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                help();
                System.exit(1);
            } else if ("help".equals(args[0])) {
                help();
                return;
            }

            Container.setOriginArgs(args);
            // 初始化数据库
            Mybatis.init();
            // 解析参数
            parseParameter(args);

            CommandsEnum commandsEnum = CommandsEnum.valueOf(args[0]);
            if (args.length >= 2 && "help".equalsIgnoreCase(args[1])) {
                commandsEnum.getCommand().help();
            } else {
                commandsEnum.getCommand().execute(Arrays.copyOfRange(args, 1, args.length));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.exit(1);
        }

    }

    /**
     * 输出帮助信息
     */
    private static void help() {
        CommandsEnum[] commandsEnums = CommandsEnum.values();
        System.err.println("指令");
        for (CommandsEnum commandsEnum : commandsEnums) {
            System.err.printf("%-7s  %-10s%n", commandsEnum.name(), commandsEnum.getHelp());
        }
        System.err.println("[指令] help 可以查看指令的详细帮助信息，比如 search help 就可以查询search的详细帮助信息");
    }

    /**
     * 解析参数
     */
    private static void parseParameter(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-") && i != args.length - 1) {
                Container.PARAMETERS.put(args[i], args[++i]);
            }
        }
    }
}
