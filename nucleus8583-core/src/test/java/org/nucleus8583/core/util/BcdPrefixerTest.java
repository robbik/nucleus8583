package org.nucleus8583.core.util;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Random;

import org.junit.Test;

public class BcdPrefixerTest {

    @Test
    public void smokeTestWriteReadUint() throws IOException {
        Random rnd = new Random();

        for (int i = 1; i < 1000; ++i) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int z = rnd.nextInt(Integer.MAX_VALUE - 100);
            
            BcdPrefixer p = new BcdPrefixer();
            p.setPrefixLength(10);

            p.writeUint(out, z);
            out.flush();

            int z2 = p.readUint(new ByteArrayInputStream(out.toByteArray()));

            assertEquals(z, z2);
        }
    }

    @Test
    public void smokeTestWriteUint() throws IOException {
        Random rnd = new Random();

        for (int i = 1; i < 1000; ++i) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int z = rnd.nextInt(Integer.MAX_VALUE - 100);

            BcdPrefixer p = new BcdPrefixer();
            p.setPrefixLength(10);

            p.writeUint(out, z);
            out.flush();

            assertEquals(z, BcdUtils.bcdToLong(out.toByteArray()));
        }
    }

    @Test
    public void smokeTestReadUint() throws IOException {
        Random rnd = new Random();

        for (int i = 1; i < 1000; ++i) {
            int z = rnd.nextInt(Integer.MAX_VALUE - 100);
            byte[] bcd = new byte[5];

            BcdUtils.intToBcd(z, bcd);

            BcdPrefixer p = new BcdPrefixer();
            p.setPrefixLength(10);

            assertEquals(z, p.readUint(new ByteArrayInputStream(bcd)));
        }
    }

    @Test(expected = EOFException.class)
    public void readUintShouldThrowEOFException() throws IOException {
        BcdPrefixer p = new BcdPrefixer();
        p.setPrefixLength(3);

        p.readUint(new ByteArrayInputStream(new byte[] { 1 }));
    }
}
