

package Model.User.Handle;

import FormatPojo.FormatUserRegister;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;



public class UserRegisterCodeC implements MessageCodec<FormatUserRegister, FormatUserRegister>{

    @Override
    public void encodeToWire(Buffer buffer, FormatUserRegister s) {
        
    }

    @Override
    public FormatUserRegister decodeFromWire(int i, Buffer buffer) {
        return null;
    }

    @Override
    public FormatUserRegister transform(FormatUserRegister s) {
        
        return s;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return  -1;
    }
    
}
