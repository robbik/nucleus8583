package org.nucleus8583.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.nucleus8583.core.util.BinaryUtils;

public class Iso87BinaryTest {

    @Test
    public void write1() throws Exception {
        Message msg = new Message();
        msg.setMti("0200");
        msg.set(2, "5200");
        msg.set(4, "89");

        MessageSerializer serializer = MessageSerializer
                .create("classpath:META-INF/nucleus8583/packagers/iso87binary.xml");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serializer.write(msg, out);

        byte[] packed = out.toByteArray();

        assertEquals("02005000000000000000045200000000000089", BinaryUtils.toHex(packed));
    }

    @Test
    public void read1() throws Exception {
        Message msg = new Message();

        MessageSerializer serializer = MessageSerializer
                .create("classpath:META-INF/nucleus8583/packagers/iso87binary.xml");

        serializer.read(BinaryUtils.toBytes("02005000000000000000045200000000000089"), msg);

        assertEquals("0200", msg.getMti());
        assertEquals("5200", msg.get(2));
        assertEquals("89", msg.get(4));
    }

    @Test
    public void case2() throws Exception {
        Message msg = new Message();
        msg.setMti("0800");
        msg.set(7, "1102165000");
        msg.set(11, "100");
        msg.set(33, "152");
        msg.set(48, "abcdef");
        msg.set(70, "101");

        MessageSerializer serializer = MessageSerializer
                .create("classpath:META-INF/nucleus8583/packagers/iso87binary.xml");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serializer.write(msg, out);

        byte[] packed = out.toByteArray();

        assertEquals("080082200000800100000400000000000000110216500000010003152000066162636465660101",
                BinaryUtils.toHex(packed));
    }
}
