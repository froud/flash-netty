package zzy.froud.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import zzy.froud.protocol.Packet;
import zzy.froud.protocol.PacketCodeC;
import zzy.froud.protocol.request.LoginRequestPacket;
import zzy.froud.protocol.response.LoginResponsePacket;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

public class ClientHandler extends ChannelInboundHandlerAdapter  {

    /**
     * 这个方法会在客户端连接建立成功之后被调用
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = LoginRequestPacket
                .builder()
                .userId(UUID.randomUUID().toString())
                .username("zzy")
                .password("1234567")
                .build();


        // 编码
        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequestPacket);

        // 写数据
        ctx.channel().writeAndFlush(buffer);
    }

    /**
     * 写数据的逻辑分为两步：
     * 首先我们需要获取一个 netty 对二进制数据的抽象 ByteBuf， ctx.alloc() 获取到一个 ByteBuf 的内存管理器，这个 内存管理器的作用就是分配一个 ByteBuf;
     * 然后我们把字符串的二进制数据填充到 ByteBuf，这样我们就获取到了Netty 需要的一个数据格式，最后我们调用 ctx.channel().writeAndFlush() 把数据写到服务端
     *
     * @param ctx
     * @return
     */
    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        //1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        //2.准备数据,指定字符串的字符集为 utf-8
        byte[] bytes = "你好,服务器".getBytes(Charset.forName("utf-8"));

        //3.填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
        if (packet instanceof LoginResponsePacket) {
            //执行登录逻辑
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            if (loginResponsePacket.isSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功");
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
            }
        }
    }
}
