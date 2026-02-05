package com.example.demo.enums;

public enum Rarity {
    C(true),
    U(true),
    R(true),
    SR(false),
    UR(false),
    SEC(false);

    private final boolean autoConvert;

    Rarity(boolean autoConvert) {
        this.autoConvert = autoConvert;
    }

    public boolean isAutoConvert() {
        return autoConvert;
    }
}
