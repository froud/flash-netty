package zzy.froud;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.w3c.dom.Attr;


public class NettyServer {

    public static void main(String[] args) {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup(); //监听端口，accept 新连接的线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();//处理每一条连接的数据读写的线程组

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(bossGroup,workerGroup) //引导类配置两大线程组
                .handler(new ChannelInitializer<NioServerSocketChannel>() { //指定在服务端启动过程中的一些逻辑，通常情况下呢，我们用不着这个方法。
                    @Override
                    protected void initChannel(NioServerSocketChannel nioServerSocketChannel) throws Exception {
                        System.out.println("服务端启动中");
                    }
                })
                .channel(NioServerSocketChannel.class) //指定 IO 模型 --> NIO
                .attr(AttributeKey.newInstance("serverName"), "myServer")//给服务端的channel 一个serverName 属性   channel.attr()取出
                .option(ChannelOption.SO_BACKLOG,1024)//给服务端channel设置一些属性 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .childAttr(AttributeKey.newInstance("clientKey"),"myClient")//给每一个连接指定自定义属性 , channel.attr()取出
                .childOption(ChannelOption.SO_KEEPALIVE,true)//给每条连接设置一些TCP底层相关的属性  表示是否开启TCP底层心跳机制，true为开启
                .childOption(ChannelOption.TCP_NODELAY,true)// 表示是否开启Nagle算法 true表示关闭，false表示开启，通俗地说，如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启。
                .childHandler(new ChannelInitializer<NioSocketChannel>() { // 定义后续每条连接的数据读写，业务处理逻辑
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                    }
                });
        bind(serverBootstrap,20000);
    }


    private static void bind( final ServerBootstrap serverBootstrap , final  int port){
        serverBootstrap.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("端口[" + port + "]绑定成功!");
                }else{
                    System.err.println("端口[" + port + "]绑定失败!");
                    bind(serverBootstrap,port+1);
                }
            }
        });
    }

}
