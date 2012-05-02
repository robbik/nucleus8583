package org.nucleus8583.externaltest.jpos;

import static org.junit.Assert.assertEquals;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.ISO87BPackager;
import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.Message;
import org.nucleus8583.core.MessageSerializer;

public class Iso87BinaryTest {

    private static int[] MAX_LENGTHS = { 4, 16, 19, 6, 12, 12, 12, 10, 8, 8, 8, 6, 6, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3,
            3, 2, 2, 1, 9, 9, 9, 9, 11, 11, 28, 37, 104, 12, 6, 2, 3, 8, 15, 40, 25, 76, 999, 999, 999, 3, 3, 3, 8, 16,
            120, 999, 999, 999, 999, 999, 999, 999, 999, 999, 8, 1, 1, 2, 3, 3, 3, 4, 4, 6, 10, 10, 10, 10, 10, 10, 10,
            10, 12, 12, 12, 12, 16, 16, 16, 16, 42, 1, 2, 6, 7, 42, 16, 17, 25, 11, 11, 17, 28, 28, 100, 999, 999, 999,
            999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 999, 8 };

    private static Set<Integer> BINARIES = new HashSet<Integer>(Arrays.asList(1, 52, 64, 65, 96, 128));

    private SecureRandom srnd;

    @Before
    public void before() {
        srnd = new SecureRandom();
    }

    private String generateFieldValue(int minLength, int maxLength) {
        StringBuilder sb = new StringBuilder();

        int len = minLength + srnd.nextInt(maxLength - minLength + 1);
        for (int i = 0; i < len; ++i) {
            sb.append(1 + srnd.nextInt(9));
        }

        return sb.toString();
    }

    private Map<Integer, String> generateCase(int minFieldNo, int maxFieldNo) {
        Map<Integer, String> case1 = new HashMap<Integer, String>();

        for (int i = minFieldNo; i <= maxFieldNo; ++i) {
            if (i != 65) {
                case1.put(Integer.valueOf(i),
                        generateFieldValue(BINARIES.contains(i) ? MAX_LENGTHS[i] : 1, MAX_LENGTHS[i]));
            }
        }

        return case1;
    }

    private void fill(Message nucMsg, Map<Integer, String> case1) {
        for (Map.Entry<Integer, String> e : case1.entrySet()) {
            int fldno = e.getKey().intValue();

            if (BINARIES.contains(fldno)) {
                nucMsg.set(fldno, e.getValue().getBytes());
            } else {
                nucMsg.set(fldno, e.getValue());
            }
        }
    }

    private void fill(ISOMsg jposMsg, Map<Integer, String> case1) throws Exception {
        for (Map.Entry<Integer, String> e : case1.entrySet()) {
            int fldno = e.getKey().intValue();

            if (BINARIES.contains(fldno)) {
                jposMsg.set(fldno, e.getValue().getBytes());
            } else {
                jposMsg.set(fldno, e.getValue());
            }
        }
    }

    @Test
    public void writeShouldResultTheSameThing() throws Exception {
        ISOPackager jposPackager = new ISO87BPackager();

        MessageSerializer nucSerializer = MessageSerializer.create(
        		"classpath:META-INF/nucleus8583/packagers/iso87binary.xml");

        for (int retry = 0; retry < 20; ++retry) {
            for (int fldno = 2; fldno <= 128; ++fldno) {
                if (fldno == 65) {
                    continue;
                }

                Map<Integer, String> case1 = generateCase(fldno, fldno);
                case1.put(0, "0780");

                Message nucMsg = new Message();
                ISOMsg jposMsg = new ISOMsg();

                fill(nucMsg, case1);
                fill(jposMsg, case1);

                jposMsg.setPackager(jposPackager);

                String jposr = BinaryUtils.toHex(jposMsg.pack());
                String nucr = BinaryUtils.toHex(nucSerializer.write(nucMsg));

                assertEquals("Field #" + fldno + ", value = " + case1.get(fldno) + ".", jposr, nucr);
            }
        }
    }

    @Test
    public void jposResultCanBeReadByNucleusAndShouldResultTheSame() throws Exception {
        ISOPackager jposPackager = new ISO87BPackager();

        MessageSerializer nucSerializer = MessageSerializer.create(
        		"classpath:META-INF/nucleus8583/packagers/iso87binary.xml");

        for (int retry = 0; retry < 20; ++retry) {
            for (int fldno = 2; fldno <= 128; ++fldno) {
                if (fldno == 65) {
                    continue;
                }

                Map<Integer, String> case1 = generateCase(fldno, fldno);
                case1.put(0, "0780");

                ISOMsg jposMsg = new ISOMsg();
                fill(jposMsg, case1);

                jposMsg.setPackager(new ISO87BPackager());

                byte[] packed = jposMsg.pack();

                Message nucMsg = new Message();
                nucSerializer.read(packed, nucMsg);

                jposMsg = new ISOMsg();
                jposMsg.setPackager(jposPackager);
                jposMsg.unpack(packed);

                if (BINARIES.contains(fldno)) {
                    assertEquals("Field #" + fldno + ", value = " + case1.get(fldno) + ".",
                            BinaryUtils.toHex(jposMsg.getBytes(fldno)), BinaryUtils.toHex((byte[]) nucMsg.get(fldno)));
                } else {
                    assertEquals("Field #" + fldno + ", value = " + case1.get(fldno) + ".", jposMsg.getValue(fldno),
                            nucMsg.get(fldno));
                }
            }
        }
    }

    @Test
    public void nucleusResultCanBeReadByJposAndShouldResultTheSame() throws Exception {
        ISOPackager jposPackager = new ISO87BPackager();

        MessageSerializer nucSerializer = MessageSerializer.create(
        		"classpath:META-INF/nucleus8583/packagers/iso87binary.xml");

        for (int retry = 0; retry < 20; ++retry) {
            for (int fldno = 2; fldno <= 128; ++fldno) {
                if (fldno == 65) {
                    continue;
                }

                Map<Integer, String> case1 = generateCase(fldno, fldno);
                case1.put(0, "0780");

                Message nucMsg = new Message();
                fill(nucMsg, case1);

                byte[] packed = nucSerializer.write(nucMsg);

                nucMsg = new Message();
                nucSerializer.read(packed, nucMsg);

                ISOMsg jposMsg = new ISOMsg();
                jposMsg.setPackager(jposPackager);
                jposMsg.unpack(packed);

                if (BINARIES.contains(fldno)) {
                    assertEquals("Field #" + fldno + ", value = " + case1.get(fldno) + ".",
                            BinaryUtils.toHex(jposMsg.getBytes(fldno)), BinaryUtils.toHex((byte[]) nucMsg.get(fldno)));
                } else {
                    assertEquals("Field #" + fldno + ", value = " + case1.get(fldno) + ".", jposMsg.getValue(fldno),
                            nucMsg.get(fldno));
                }
            }
        }
    }
}
