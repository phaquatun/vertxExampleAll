/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.User.Handle;

import FormatPojo.FormatUploadFile;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class UploadImgCodeC implements MessageCodec<FormatUploadFile, FormatUploadFile> {

    @Override
    public void encodeToWire(Buffer buffer, FormatUploadFile s) {

    }

    @Override
    public FormatUploadFile decodeFromWire(int i, Buffer buffer) {
        
        return null;
    }

    @Override
    public FormatUploadFile transform(FormatUploadFile s) {

        return s;
    }

    @Override
    public String name() {

        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }

}
