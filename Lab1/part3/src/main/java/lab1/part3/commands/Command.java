package lab1.part3.commands;

public interface Command<R> {
    R execute();
}