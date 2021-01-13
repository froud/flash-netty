package zzy.froud.protocol.command;

public interface Command {
    //定义登录请求为1
    Byte LOGIN_REQUEST = 1;
    //定义登录响应为2
    Byte LOGIN_RESPONSE = 2;
    //定义发送消息为3
    Byte MESSAGE_REQUEST = 3;
    //定义回复消息为4
    Byte MESSAGE_RESPONSE = 4;
}
