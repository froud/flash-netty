package zzy.froud.protocol.response;

import lombok.Data;
import zzy.froud.protocol.Packet;

import static zzy.froud.protocol.command.Command.LOGIN_RESPONSE;
@Data
public class LoginResponsePacket extends Packet {
    private boolean success;
    private String reason;
    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
