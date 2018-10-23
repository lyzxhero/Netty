package com.lyzx.netty.netty11;

//import com.jufan.config.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

public final class HttpServer {
//    private static Logger logger = LogManager.getLogger(HttpServer.class);
    static final boolean SSL = System.getProperty("ssl") != null;

    static final int READER_IDLE_TIME_SECONDS = 0;
    static final int WRITER_IDLE_TIME_SECONDS = 0;
    static final int ALL_IDLE_TIME_SECONDS = 0;

    public static void main(String[] args) throws Exception {
		Map<String, String> paramsMap = new HashMap<>();
		for (int i = 0; i < args.length; i += 2) {
			paramsMap.put(args[i].substring(2), args[i + 1]);
		}

		System.out.println("paramsMap=" + paramsMap);

		boolean LOG_TO_FILE = Boolean.parseBoolean(paramsMap.getOrDefault("log_to_file", "true"));

        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        EventLoopGroup bossGroup = new NioEventLoopGroup(Integer.parseInt(paramsMap.getOrDefault("bossNum", "1")));
        EventLoopGroup workerGroup = new NioEventLoopGroup(Integer.parseInt(paramsMap.getOrDefault("wokerNum", "0")));
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.DEBUG))
             .option(ChannelOption.SO_BACKLOG, 1024)
             .childHandler(new HttpServerInitializer())
             .childOption(ChannelOption.SO_LINGER, 0)
             .childOption(ChannelOption.TCP_NODELAY, true);
            int port = Integer.parseInt(paramsMap.getOrDefault("port", "18080"));
            Channel ch = b.bind(port).sync().channel();

            System.err.println("Open your web browser and navigate to " + (SSL? "https" : "http") + "://127.0.0.1:" + port + '/');
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}