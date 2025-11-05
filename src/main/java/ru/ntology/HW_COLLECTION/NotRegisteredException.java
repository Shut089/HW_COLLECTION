package ru.ntology.HW_COLLECTION;

public class NotRegisteredException extends RuntimeException{
    public NotRegisteredException(String playerName) {
        super("Игрок с именем '" + playerName + "' не зарегистрирован для участия в турнире.");
    }

}
