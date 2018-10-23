package com.lyzx.netty.netty10;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URLDecoder;
import java.util.regex.Pattern;
import  io.netty.handler.codec.http.HttpVersion;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.LOCATION;
import static io.netty.handler.codec.http.HttpMethod.GET;

/**
 *
 *
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    @Override
    public void channelRead0(ChannelHandlerContext ctx,FullHttpRequest request) throws Exception {
        System.out.println("=============="+request.getClass().getName());
        // 首先检查解码是否成功，如果失败则返回错误页面
        if (!request.decoderResult().isSuccess()){
            sendError(ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }

        //由于是通过浏览器直接访问，判断如果不是GET直接返回
        if (request.method() != GET){
            sendError(ctx,HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        //获取uri并检测，如果url检测不通过则返回错误码
        final String uri = request.uri();
        final String path = sanitizeUri(uri);
        if (path == null){
            sendError(ctx,HttpResponseStatus.FORBIDDEN);
            return;
        }

        //非法资源则返回
        File file = new File(path);
        if (!file.exists() || file.isHidden()){
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        //如果是文件夹则按照文件夹的方式处理
        if(file.isDirectory()){
            directProcess(ctx, uri, file);
        }else{
            fileProcess(ctx, request, file);
        }
    }

    /**
     * 用于处理请求是文件夹的请求
     * @param ctx
     * @param uri
     * @param file
     */
    private void directProcess(ChannelHandlerContext ctx, String uri, File file) {
        if(uri.endsWith("/")){
            showList(ctx,file);
        }else{
            redirect(ctx,uri + '/');
        }
        return;
    }

    /**
     * 用于返回请求是文件的请求
     * @param ctx
     * @param request
     * @param file
     * @throws IOException
     */
    private void fileProcess(ChannelHandlerContext ctx, FullHttpRequest request, File file) throws IOException {
        RandomAccessFile randomAccessFile;
        try{
            randomAccessFile = new RandomAccessFile(file,"r");// 以只读的方式打开文件
        }catch(FileNotFoundException fnfe){
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        long fileLength = randomAccessFile.length();
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);
        HttpUtil.setContentLength(response, fileLength);
        setContentTypeHeader(response,file);
        if(HttpUtil.isKeepAlive(request)){
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        ctx.write(response);
        ChannelFuture sendFileFuture = ctx.write(new ChunkedFile(randomAccessFile,0,fileLength,8192), ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
                public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                    if (total < 0) { // total unknown
                        System.err.println("Transfer progress: " + progress);
                    }else{
                        System.err.println("Transfer progress: " + progress + " / "+ total);
                    }
                }
                public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                    System.out.println("Transfer complete.");
                }
        });

        ChannelFuture lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!HttpUtil.isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()){
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * 检查url的合法性
     * @param uri
     * @return
     *  null表示这个uri不合法
     *  String表示这个uri合法
     */
    private String sanitizeUri(String uri) {
        try{
            uri = URLDecoder.decode(uri,"UTF-8");
        }catch(UnsupportedEncodingException e) {
            throw new Error();
        }

        if(!uri.startsWith("/")) {
            return null;
        }

        uri = uri.replace('/',File.separatorChar);
        if(uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.startsWith(".") || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }
        return System.getProperty("user.dir")+uri;
    }

    /**
     * 返回一个文件目录,以及每个文件的路径
     * 调用这个方法说明,dir是一个路径
     * @param ctx
     * @param dir
     */
    private static void showList(ChannelHandlerContext ctx, File dir){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
        StringBuilder buf = new StringBuilder();
        String dirPath = dir.getPath();
        buf.append("<!DOCTYPE html><br/><html><head><title>")
            .append(dirPath)
            .append(" 目录:</title></head><body><br/>")
            .append("<h3>")
            .append(dirPath)
            .append(" 目录：")
            .append("</h3><br/><ul><li>链接如下</li><br/>");

        for(File f : dir.listFiles()){
            if(f.isHidden() || !f.canRead()){
                continue;
            }

            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()){
                continue;
            }

            String type = f.isFile() ? "-":"d";

            buf.append("<li>")
               .append(" ").append(type).append(" ")
               .append("<a href='").append(name).append("'>")
               .append(name)
               .append("</a></li>\r\n");
        }
        buf.append("</ul></body></html><br/>");

        ByteBuf buffer = Unpooled.copiedBuffer(buf,CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    //访问成功设置跳转
    private static void redirect(ChannelHandlerContext ctx, String newUri){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(LOCATION,newUri);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     *  返回各种错误状态码
     * @param ctx
     * @param status
     */
    private static void sendError(ChannelHandlerContext ctx,HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status, Unpooled.copiedBuffer("Failure: " + status.toString()+"\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
          ctx.writeAndFlush(response)
             .addListener(ChannelFutureListener.CLOSE);
    }

    //设置response_content_type_header
    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        response.headers().set(CONTENT_TYPE,mimeTypesMap.getContentType(file.getPath()));
    }
}