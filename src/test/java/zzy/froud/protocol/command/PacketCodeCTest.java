package zzy.froud.protocol.command;

import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;
import zzy.froud.serialize.Serializer;
import zzy.froud.serialize.impl.JSONSerializer;

public class PacketCodeCTest {

    @Test
    public void encode() {
        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserId(123);
        loginRequestPacket.setUsername("zhangsan");
        loginRequestPacket.setPassword("password");

        PacketCodeC packetCodeC = new PacketCodeC();
        ByteBuf byteBuf = packetCodeC.encode(loginRequestPacket); //编码成 bytebuf
        Packet decodedPacket = packetCodeC.decode(byteBuf); //解码成对象

        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }
}
