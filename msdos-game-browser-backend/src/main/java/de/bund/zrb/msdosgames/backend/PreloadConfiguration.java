package de.bund.zrb.msdosgames.backend;

public final class PreloadConfiguration {

    private final boolean enabled;
    private final int lookBehind;
    private final int lookAhead;
    private final int detailThreads;
    private final boolean preloadImagesWhenIdle;

    private PreloadConfiguration(boolean enabled, int lookBehind, int lookAhead, int detailThreads, boolean preloadImagesWhenIdle) {
        this.enabled = enabled;
        this.lookBehind = Math.max(0, lookBehind);
        this.lookAhead = Math.max(0, lookAhead);
        this.detailThreads = Math.max(1, detailThreads);
        this.preloadImagesWhenIdle = preloadImagesWhenIdle;
    }

    public static PreloadConfiguration disabled() {
        return new PreloadConfiguration(false, 0, 0, 1, false);
    }

    public static PreloadConfiguration enabled(int lookBehind, int lookAhead, int detailThreads, boolean preloadImagesWhenIdle) {
        return new PreloadConfiguration(true, lookBehind, lookAhead, detailThreads, preloadImagesWhenIdle);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getLookBehind() {
        return lookBehind;
    }

    public int getLookAhead() {
        return lookAhead;
    }

    public int getDetailThreads() {
        return detailThreads;
    }

    public boolean isPreloadImagesWhenIdle() {
        return preloadImagesWhenIdle;
    }
}
