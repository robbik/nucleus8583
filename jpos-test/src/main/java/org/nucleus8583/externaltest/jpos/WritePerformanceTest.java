package org.nucleus8583.externaltest.jpos;

import java.io.OutputStream;

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
        if (args.length > 0) {
            LOOPS = Integer.parseInt(args[0]);
        }

        WritePerformanceTest tester = new WritePerformanceTest();

        tester.initialize();

        long nucleus8583Elapsed = tester.doJobForNucleus8583();
        long jposElapsed = tester.doJobForJPos();

        System.out.println();

        System.out.println("NUCLEUS8583 3.0.0 SUMMARY");
        System.out.println("===========================");
        System.out.println("[x] Number of Data Samples  = " + LOOPS);
        System.out.println("[x] Times Elapsed           = " + (nucleus8583Elapsed / 1000.0f) + " sec");
        System.out.println("[x] Throughput              = " + (LOOPS * 1000.0f / nucleus8583Elapsed) + " tps");
        System.out.println();

        System.out.println("JPOS 1.8.2 SUMMARY");
        System.out.println("===========================");
        System.out.println("[x] Number of Data Samples  = " + LOOPS);
        System.out.println("[x] Times Elapsed           = " + (jposElapsed / 1000.0f) + " sec");
        System.out.println("[x] Throughput              = " + (LOOPS * 1000.0f / jposElapsed) + " tps");
        System.out.println();
    }
}
