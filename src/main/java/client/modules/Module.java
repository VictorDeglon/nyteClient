package client.modules;

public interface Module {
    String getName();
    void toggle();
    boolean isEnabled();
}
