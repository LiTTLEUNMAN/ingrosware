package us.devs.ingrosware.traits;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public interface Stateable {
    boolean getState();
    void setState(boolean state);
    void onState();
    void onDisable();

    default void toggle() {
        setState(!getState());
    }
}
