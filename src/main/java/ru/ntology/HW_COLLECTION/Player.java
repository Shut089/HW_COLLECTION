package ru.ntology.HW_COLLECTION;

import java.util.Objects;

public class Player {
    private final int id;
    private final String name;
    private final String level;

    public Player(int id, String name, String level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String level() {
        return level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id && Objects.equals(name, player.name) && Objects.equals(level, player.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, level);
    }
}
