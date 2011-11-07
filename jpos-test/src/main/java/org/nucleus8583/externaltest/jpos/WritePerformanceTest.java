package org.nucleus8583.externaltest.jpos;

import java.io.OutputStream;
import java.text.DecimalFormat;

import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.ISO87APackager;
import org.nucleus8583.core.Message;
import org.nucleus8583.core.MessageSerializer;

public class WritePerformanceTest {
    private static final int DEFAULT_LOOPS = 1;

    private static int LOOPS = DEFAULT_LOOPS;

    private MessageSerializer serializer;

    private ISOPackager packager;

    private OutputStream nullOut;

    public void initialize() throws Exception {
        serializer = new MessageSerializer("classpath:META-INF/nucleus8583/packagers/iso87ascii.xml");

        packager = new ISO87APackager();

        nullOut = new NullOutputStream();
    }

    private long doJobForNucleus8583() throws Exception {
        long startDate = System.currentTimeMillis();

        for (int i = LOOPS - 1; i >= 0; --i) {
            Message msg = new Message();

            msg.setMti("0200");
            msg.set(2, "3125 ");
            msg.set(7, "0104132431");
            msg.set(11, "1");
            msg.set(12, "132431");
            msg.set(13, "0104");
            msg.set(29, "C01000000");
            msg.set(37, "1762745214");
            msg.set(39, "00");
            msg.set(48, "01000abcdefghijkl ");

            serializer.write(msg, nullOut);
        }

        long endDate = System.currentTimeMillis();

        return endDate - startDate;
    }

    private long doJobForJPos() throws Exception {
        long startDate = System.currentTimeMillis();

        for (int i = LOOPS - 1; i >= 0; --i) {
            ISOMsg msg = new ISOMsg();

            msg.setMTI("0200");
            msg.set(2, "3125 ");
            msg.set(7, "0104132431");
            msg.set(11, "1");
            msg.set(12, "132431");
            msg.set(13, "0104");
            msg.set(29, "C01000000");
            msg.set(37, "1762745214");
            msg.set(39, "00");
            msg.set(48, "01000abcdefghijkl ");

            msg.setPackager(packager);
            msg.pack(nullOut);
        }

        long endDate = System.currentTimeMillis();

        return endDate - startDate;
    }

    public static void main(String[] args) throws Exception {
        boolean nucleus8583 = false;
        int recount = 10;

        if (args.length > 0) {
            LOOPS = Integer.parseInt(args[0]);

            if (args.length > 1) {
                nucleus8583 = args[1].equals("nuc");

                if (args.length > 2) {
                    recount = Integer.parseInt(args[2]);
                }
            }
        }

        DecimalFormat fmt = new DecimalFormat("#,##0.00");

        long jposElapsed = 0;
        long nucleus8583Elapsed = 0;

        WritePerformanceTest tester = new WritePerformanceTest();
        tester.initialize();

        for (int i = 0; i < recount; ++i) {
            if (nucleus8583) {
                nucleus8583Elapsed = tester.doJobForNucleus8583();
            } else {
                jposElapsed = tester.doJobForJPos();
            }

            if (nucleus8583Elapsed > 0) {
                if (i == 0) {
                    System.out.println("NUCLEUS8583 3.0.0 DETAILS");
                    System.out.println("===========================");
                    System.out.println("[x] Number of Data Samples  = " + fmt.format(LOOPS));
                    System.out.println();

                    System.out.println(String.format("%1$10s %2$20s", "Times (sec)", "Throughput (tps)"));
                }

                System.out.println(String.format("%1$10s %2$20s", fmt.format(nucleus8583Elapsed / 1000.0f), fmt.format(LOOPS * 1000.0f / nucleus8583Elapsed)));
            }

            if (jposElapsed > 0) {
                if (i == 0) {
                    System.out.println("JPOS 1.8.2 DETAILS");
                    System.out.println("===========================");
                    System.out.println("[x] Number of Data Samples  = " + fmt.format(LOOPS));
                    System.out.println();

                    System.out.println(String.format("%1$10s %2$20s", "Times (sec)", "Throughput (tps)"));
                }

                System.out.println(String.format("%1$10s %2$20s", fmt.format(jposElapsed / 1000.0f), fmt.format(LOOPS * 1000.0f / jposElapsed)));
            }

            Thread.yield();
        }

        System.out.println();
    }
}
