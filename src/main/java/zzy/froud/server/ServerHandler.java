package zzy.froud.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import zzy.froud.protocol.Packet;
import zzy.froud.protocol.PacketCodeC;
import zzy.froud.protocol.request.LoginRequestPacket;
import zzy.froud.protocol.response.LoginResponsePacket;

import java.nio.charset.Charset;
import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
        if (packet instanceof LoginRequestPacket){
            //执行登录逻辑
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            if (valid(loginRequestPacket)) {
                loginResponsePacket.setSuccess(true);
                System.out.println(new Date() + ": 登录成功!");
            }else{
                loginResponsePacket.setSuccess(false);
                loginResponsePacket.setReason("账号密码校验失败");
                System.out.println(new Date() + ": 登录失败!");
            }
            // 登录响应
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }

    }

    /**
     * 验证登录请求 校验用户名 密码
     *
     * @param loginRequestPacket
     * @return
     */
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        boolean res = false;
        String userId = loginRequestPacket.getUserId();
        String password = loginRequestPacket.getPassword();
        String username = loginRequestPacket.getUsername();
        if (username.equals("zzy") || password.equals("123456")){
            res = true;
        }

        return res;
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        //1. 获取二进制抽象 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();

        //2.准备数据,指定字符串的字符集为 utf-8
        byte[] bytes = "你好,客户端".getBytes(Charset.forName("utf-8"));

        //3.填充数据到 ByteBuf
        buffer.writeBytes(bytes);

        return buffer;
    }
}
