package me.ste.stevesdbclient.network.packet;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PacketId {
    int value();
}