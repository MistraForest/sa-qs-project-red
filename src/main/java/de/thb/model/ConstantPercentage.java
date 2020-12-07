package de.thb.model;

public enum ConstantPercentage {
    PERCENTAGE_10(0.1f),
    PERCENTAGE_5(0.05f),
    PERCENTAGE_0(0.0f);

    private float percentage;

    ConstantPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getPercentage() {
        return percentage ;
    }
}
