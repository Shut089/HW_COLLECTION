package ru.ntology.HW_COLLECTION;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {
    private Game game;
    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    void setUp() {
        game = new Game();
        player1 = new Player(1, "Игрок 1", "Мастер");
        player2 = new Player(2, "Игрок 2", "Средний");
        player3 = new Player(3, "Игрок 3", "Мастер");
    }

    @Test
    void testRegister() {
        // Проверяем, что игрок успешно регистрируется
        game.register(player1);
        
        // Проверяем, что игрок добавлен в список (через поиск по имени)
        Player found = game.findByName("Игрок 1");
        Assertions.assertNotNull(found);
        Assertions.assertEquals(player1, found);
    }

    @Test
    void testRoundPlayer1Wins() {
        // Регистрируем обоих игроков
        game.register(player1); // level = "Мастер"
        game.register(player2); // level = "Средний"

        // Первый игрок должен победить (Мастер > Средний)
        int result = game.round("Игрок 1", "Игрок 2");
        Assertions.assertEquals(1, result);
    }

    @Test
    void testRoundPlayer2Wins() {
        // Регистрируем обоих игроков
        game.register(player1); // level = "Мастер"
        game.register(player2); // level = "Средний"

        // Второй игрок должен победить (меняем порядок: Средний < Мастер)
        int result = game.round("Игрок 2", "Игрок 1");
        Assertions.assertEquals(2, result);
    }

    @Test
    void testRoundDraw() {
        // Регистрируем игроков с одинаковым уровнем
        game.register(player1); // level = "Мастер"
        game.register(player3); // level = "Мастер"

        // Должна быть ничья
        int result = game.round("Игрок 1", "Игрок 3");
        Assertions.assertEquals(0, result);
    }

    @Test
    void testRoundPlayer1NotRegistered() {
        // Регистрируем только второго игрока
        game.register(player2);

        // Попытка соревнования с незарегистрированным первым игроком
        Assertions.assertThrows(NotRegisteredException.class, () -> {
            game.round("Игрок 1", "Игрок 2");
        });
    }

    @Test
    void testRoundPlayer2NotRegistered() {
        // Регистрируем только первого игрока
        game.register(player1);

        // Попытка соревнования с незарегистрированным вторым игроком
        Assertions.assertThrows(NotRegisteredException.class, () -> {
            game.round("Игрок 1", "Игрок 2");
        });
    }

    @Test
    void testRoundBothPlayersNotRegistered() {
        // Не регистрируем никого

        // Попытка соревнования с незарегистрированными игроками
        Assertions.assertThrows(NotRegisteredException.class, () -> {
            game.round("Игрок 1", "Игрок 2");
        });
    }

    @Test
    void testFindByNameExistingPlayer() {
        // Регистрируем игрока
        game.register(player1);

        // Поиск существующего игрока
        Player found = game.findByName("Игрок 1");
        Assertions.assertNotNull(found);
        Assertions.assertEquals(player1, found);
        Assertions.assertEquals(1, found.id());
        Assertions.assertEquals("Мастер", found.level());
    }

    @Test
    void testFindByNameNonExistingPlayer() {
        // Не регистрируем игроков

        // Поиск несуществующего игрока
        Player found = game.findByName("Несуществующий игрок");
        Assertions.assertNull(found);
    }

    @Test
    void testRoundWithDifferentStrengthOrder() {
        // Проверяем, что порядок регистрации не влияет на результат
        game.register(player2); // level = "Средний"
        game.register(player1); // level = "Мастер"

        // Первый игрок (player1) должен победить, даже если зарегистрирован вторым
        int result = game.round("Игрок 1", "Игрок 2");
        Assertions.assertEquals(1, result);
    }

    @Test
    void testRegisterMultiplePlayers() {
        // Регистрируем нескольких игроков
        game.register(player1);
        game.register(player2);
        game.register(player3);

        // Проверяем, что все игроки найдены
        Assertions.assertNotNull(game.findByName("Игрок 1"));
        Assertions.assertNotNull(game.findByName("Игрок 2"));
        Assertions.assertNotNull(game.findByName("Игрок 3"));
    }

    @Test
    void testNotRegisteredExceptionMessage() {
        // Проверяем сообщение исключения
        game.register(player1);

        try {
            game.round("Игрок 1", "Несуществующий");
            Assertions.fail("Должно было быть выброшено исключение");
        } catch (NotRegisteredException e) {
            Assertions.assertTrue(e.getMessage().contains("Несуществующий"));
            Assertions.assertTrue(e.getMessage().contains("не зарегистрирован"));
        }
    }

    @Test
    void testRoundNoviceVsMedium() {
        // Проверяем, что Средний побеждает Новичка
        Player novice = new Player(4, "Новичек", "Новичек");
        Player medium = new Player(5, "Средний", "Средний");
        
        game.register(novice);
        game.register(medium);
        
        int result = game.round("Средний", "Новичек");
        Assertions.assertEquals(1, result); // Средний побеждает
        
        result = game.round("Новичек", "Средний");
        Assertions.assertEquals(2, result); // Средний побеждает (второй)
    }

    @Test
    void testRoundNoviceVsMaster() {
        // Проверяем, что Мастер побеждает Новичка
        Player novice = new Player(6, "Новичек", "Новичек");
        Player master = new Player(7, "Мастер", "Мастер");
        
        game.register(novice);
        game.register(master);
        
        int result = game.round("Мастер", "Новичек");
        Assertions.assertEquals(1, result); // Мастер побеждает
        
        result = game.round("Новичек", "Мастер");
        Assertions.assertEquals(2, result); // Мастер побеждает (второй)
    }

    @Test
    void testRoundMediumVsMaster() {
        // Проверяем, что Мастер побеждает Средний
        Player medium = new Player(8, "Средний", "Средний");
        Player master = new Player(9, "Мастер", "Мастер");
        
        game.register(medium);
        game.register(master);
        
        int result = game.round("Мастер", "Средний");
        Assertions.assertEquals(1, result); // Мастер побеждает
        
        result = game.round("Средний", "Мастер");
        Assertions.assertEquals(2, result); // Мастер побеждает (второй)
    }

    @Test
    void testRoundNoviceDraw() {
        // Проверяем ничью для Новичков
        Player novice1 = new Player(10, "Новичек 1", "Новичек");
        Player novice2 = new Player(11, "Новичек 2", "Новичек");
        
        game.register(novice1);
        game.register(novice2);
        
        int result = game.round("Новичек 1", "Новичек 2");
        Assertions.assertEquals(0, result); // Ничья
    }

    @Test
    void testRoundMediumDraw() {
        // Проверяем ничью для Средних
        Player medium1 = new Player(12, "Средний 1", "Средний");
        Player medium2 = new Player(13, "Средний 2", "Средний");
        
        game.register(medium1);
        game.register(medium2);
        
        int result = game.round("Средний 1", "Средний 2");
        Assertions.assertEquals(0, result); // Ничья
    }

    @Test
    void testFindByNameWithMultiplePlayers() {
        // Регистрируем несколько игроков
        game.register(player1);
        game.register(player2);
        game.register(player3);
        
        // Поиск существующего игрока в списке из нескольких игроков
        Player found = game.findByName("Игрок 2");
        Assertions.assertNotNull(found);
        Assertions.assertEquals(player2, found);
        
        // Поиск несуществующего игрока в списке из нескольких игроков
        Player notFound = game.findByName("Несуществующий");
        Assertions.assertNull(notFound);
    }

    @Test
    void testRoundWithUnknownLevel() {
        // Проверяем исключение для неизвестного уровня
        Player playerWithUnknownLevel = new Player(14, "Игрок с неизвестным уровнем", "Неизвестный");
        Player normalPlayer = new Player(15, "Обычный игрок", "Новичек");
        
        game.register(playerWithUnknownLevel);
        game.register(normalPlayer);
        
        // Попытка соревнования с игроком, имеющим неизвестный уровень
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.round("Игрок с неизвестным уровнем", "Обычный игрок");
        });
        
        // Проверяем, что исключение выбрасывается и для второго игрока
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.round("Обычный игрок", "Игрок с неизвестным уровнем");
        });
    }

    @Test
    void testPlayerEquals() {
        // Проверяем равенство объекта с самим собой
        Assertions.assertTrue(player1.equals(player1));
        
        // Проверяем равенство одинаковых объектов
        Player player1Copy = new Player(1, "Игрок 1", "Мастер");
        Assertions.assertTrue(player1.equals(player1Copy));
        Assertions.assertTrue(player1Copy.equals(player1));
        
        // Проверяем неравенство разных объектов
        Assertions.assertFalse(player1.equals(player2));
        Assertions.assertFalse(player2.equals(player1));
        
        // Проверяем сравнение с null
        Assertions.assertFalse(player1.equals(null));
        
        // Проверяем сравнение с объектом другого класса
        Assertions.assertFalse(player1.equals("Игрок 1"));
        
        // Проверяем неравенство при разных id
        Player playerDifferentId = new Player(99, "Игрок 1", "Мастер");
        Assertions.assertFalse(player1.equals(playerDifferentId));
        
        // Проверяем неравенство при разных именах
        Player playerDifferentName = new Player(1, "Другой игрок", "Мастер");
        Assertions.assertFalse(player1.equals(playerDifferentName));
        
        // Проверяем неравенство при разных уровнях
        Player playerDifferentLevel = new Player(1, "Игрок 1", "Новичек");
        Assertions.assertFalse(player1.equals(playerDifferentLevel));
        
        // Проверяем равенство с null значениями полей
        Player playerWithNullName = new Player(100, null, "Мастер");
        Player playerWithNullName2 = new Player(100, null, "Мастер");
        Assertions.assertTrue(playerWithNullName.equals(playerWithNullName2));
        
        Player playerWithNullLevel = new Player(101, "Игрок", null);
        Player playerWithNullLevel2 = new Player(101, "Игрок", null);
        Assertions.assertTrue(playerWithNullLevel.equals(playerWithNullLevel2));
        
        // Проверяем неравенство, когда одно поле null, а другое нет
        Player playerWithName = new Player(102, "Игрок", "Мастер");
        Assertions.assertFalse(playerWithNullName.equals(playerWithName));
        Assertions.assertFalse(playerWithName.equals(playerWithNullName));
    }

    @Test
    void testPlayerHashCode() {
        // Проверяем, что равные объекты имеют одинаковый hashCode
        Player player1Copy = new Player(1, "Игрок 1", "Мастер");
        Assertions.assertEquals(player1.hashCode(), player1Copy.hashCode());
        
        // Проверяем hashCode для объектов с null значениями
        Player playerWithNullName = new Player(100, null, "Мастер");
        Player playerWithNullName2 = new Player(100, null, "Мастер");
        Assertions.assertEquals(playerWithNullName.hashCode(), playerWithNullName2.hashCode());
        
        Player playerWithNullLevel = new Player(101, "Игрок", null);
        Player playerWithNullLevel2 = new Player(101, "Игрок", null);
        Assertions.assertEquals(playerWithNullLevel.hashCode(), playerWithNullLevel2.hashCode());
        

    }
}

