package org.nucleus8583.core.util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BcdPrefixerTest {

    @Test
    void smokeTestWriteReadUint() throws IOException {
        Random rnd = new Random();

        for (int i = 1; i < 1000; ++i) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                int z = rnd.nextInt(Integer.MAX_VALUE - 100);

                BcdPrefixer p = new BcdPrefixer();
                p.setPrefixLength(10);

                p.writeUint(out, z);
                out.flush();

                int z2 = p.readUint(new ByteArrayInputStream(out.toByteArray()));

                assertThat(z2, is(z));
            }
        }
    }

    @Test
    void smokeTestWriteUint() throws IOException {
        Random rnd = new Random();

        for (int i = 1; i < 1000; ++i) {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                int z = rnd.nextInt(Integer.MAX_VALUE - 100);

                BcdPrefixer p = new BcdPrefixer();
                p.setPrefixLength(10);

                p.writeUint(out, z);
                out.flush();

                assertThat(BcdUtils.bcdToLong(out.toByteArray()), is((long) z));
            }
        }
    }

    @Test
    void smokeTestReadUint() throws IOException {
        Random rnd = new Random();

        for (int i = 1; i < 1000; ++i) {
            int z = rnd.nextInt(Integer.MAX_VALUE - 100);
            byte[] bcd = new byte[5];

            BcdUtils.intToBcd(z, bcd);

            BcdPrefixer p = new BcdPrefixer();
            p.setPrefixLength(10);

            assertThat(p.readUint(new ByteArrayInputStream(bcd)), is(z));
        }
    }

    @Test
    void readUintShouldThrowEOFException() {
        assertThrows(EOFException.class, () -> {
            BcdPrefixer p = new BcdPrefixer();
            p.setPrefixLength(3);

            p.readUint(new ByteArrayInputStream(new byte[] { 1 }));
        });
    }
}
