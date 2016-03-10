package com.rookiefly.test.commons.image;

import com.rookiefly.commons.image.VerifyCodeUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by rookiefly on 2015/11/9.
 */
public class ImageTest {

    @Test
    public void testVerifyCode() throws IOException {
        VerifyCodeUtils.outputVerifyImage(250, 100, new File("vcode.jpg"), 4);
    }

    @Test
    public void testIm4Java() throws IOException {
        //ProcessStarter.setGlobalSearchPath("D:\\ImageMagick");
        // create command
        ConvertCmd cmd = new ConvertCmd();

        cmd.setSearchPath("D:\\ImageMagick");
        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.resize(800, 600);
        op.addImage("D:\\desktop.jpg");
        op.addImage("D:\\desktop_thumbnail.jpg");

        // execute the operation
        try {
            cmd.run(op);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }
    }
}
