package com.chess.engine.board;

import java.util.Objects;

public class esempio {


    private int x;
    private int y;


    public esempio(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        esempio esempio = (esempio) o;
        return x == esempio.x &&
                y == esempio.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
