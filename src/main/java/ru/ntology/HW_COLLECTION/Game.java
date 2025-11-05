package ru.ntology.HW_COLLECTION;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> registeredPlayers = new ArrayList<>();

    // Метод регистрации игрока
    public void register(Player player) {
        registeredPlayers.add(player);
    }

    // Вспомогательный метод для поиска игрока по имени
    // Возвращает Player, если найден, или null, если не найден.
    public Player findByName(String name) {
        for (Player player : registeredPlayers) {
            if (player.name().equals(name)) {
                return player;
            }
        }
        return null; // Игрок не найден
    }

    /**
     * Метод соревнования двух игроков.
     * @param playerName1 Имя первого игрока.
     * @param playerName2 Имя второго игрока.
     * @return 0 - ничья, 1 - победа первого игрока, 2 - победа второго игрока.
     * @throws NotRegisteredException Если хотя бы один игрок не зарегистрирован.
     */
    public int round(String playerName1, String playerName2) {
        // 1. Поиск игроков и проверка регистрации
        Player player1 = findByName(playerName1);
        Player player2 = findByName(playerName2);

        if (player1 == null) {
            throw new NotRegisteredException(playerName1);
        }
        if (player2 == null) {
            throw new NotRegisteredException(playerName2);
        }

        // 2. Сравнение уровней (основная бизнес-логика)
        // Порядок: "Новичек" < "Средний" < "Мастер"
        int level1Strength = getLevelStrength(player1.level());
        int level2Strength = getLevelStrength(player2.level());
        
        if (level1Strength > level2Strength) {
            return 1; // Победа первого
        } else if (level1Strength < level2Strength) {
            return 2; // Победа второго
        } else {
            return 0; // Ничья
        }
    }

    /**
     * Вспомогательный метод для преобразования уровня в числовое значение для сравнения.
     * @param level Уровень игрока
     * @return Числовое значение уровня: Новичек = 1, Средний = 2, Мастер = 3
     */
    private int getLevelStrength(String level) {
        switch (level) {
            case "Новичек":
                return 1;
            case "Средний":
                return 2;
            case "Мастер":
                return 3;
            default:
                throw new IllegalArgumentException("Неизвестный уровень: " + level);
        }
    }
}

