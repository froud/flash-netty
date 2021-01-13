package zzy.froud.protocol.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import zzy.froud.protocol.Packet;

import static zzy.froud.protocol.command.Command.LOGIN_REQUEST;

// 登录请求数据包
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;

    }
}