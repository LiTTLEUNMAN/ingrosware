package us.devs.ingrosware.mixin.accessors;

public interface IEntityPlayerSP {

    boolean isInLiquid();

    boolean isOnLiquid();

    boolean isMoving();

    void setInPortal(boolean inPortal);
}
