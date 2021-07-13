package aszswaz.elasticsearch.commands;

/**
 * 指令对象
 *
 * @author aszswaz
 * @date 2021/7/12 13:26:07
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public interface Command {
    /**
     * 执行指令
     */
    void execute(String[] args) throws Exception;

    /**
     * 输出帮助信息
     */
    void help();
}
