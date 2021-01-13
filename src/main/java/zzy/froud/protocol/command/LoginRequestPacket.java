package zzy.froud.protocol.command;

import lombok.Data;

import static zzy.froud.protocol.command.Command.LOGIN_REQUEST;

// 登录请求数据包
@Data
public class LoginRequestPacket extends Packet {

    private Integer userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;

    }
}