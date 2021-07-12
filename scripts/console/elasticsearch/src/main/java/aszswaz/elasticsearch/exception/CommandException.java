package aszswaz.elasticsearch.exception;

/**
 * 指令错误
 *
 * @author aszswaz
 * @date 2021/7/12 15:56:55
 * @IDE IntelliJ IDEA
 */
@SuppressWarnings("JavaDoc")
public class CommandException extends Exception {
    public CommandException(String message) {
        super(message);
    }
}
