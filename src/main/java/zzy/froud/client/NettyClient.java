package zzy.froud.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyClient {

    private static final int MAX_RETRY = 20;
    private static final int PORT = 20000;
    private static final String HOST= "127.0.0.1";

    public static void main(String[] args) {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();


        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                //1.指定线程组
                .group(workGroup)
                //2.指定IO类型 为 NIO
                .channel(NioSocketChannel.class)
                //3.IO 处理逻辑
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //指定老连接数据读写逻辑
                        ch.pipeline().addLast(new FirstClientHandler());
                    }
                })
                .attr(AttributeKey.newInstance("clientName"), "myClient")//给客户端的 NioSocketChannel 绑定属性, 通过 channel.attr()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000) //方法可以给连接设置一些 TCP 底层相关的属性
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true);

        //4. 建立连接
        connect(bootstrap,HOST,PORT,MAX_RETRY);

    }


    private static void connect( Bootstrap bootstrap, String host, int port,int retry) {
        bootstrap.connect(host,port).addListener(future->{
            if (future.isSuccess()){
                System.out.println("连接成功!");
            }else if(retry == 0){
                System.err.println("重试次数已用完，放弃连接！");
            }else{
                //第几次重连
                int order = (MAX_RETRY - retry) + 1;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(()->connect(bootstrap,host,port,retry-1),5, TimeUnit.SECONDS);
            }

        });
    }

}
