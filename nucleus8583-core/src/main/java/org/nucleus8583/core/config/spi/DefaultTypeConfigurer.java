package org.nucleus8583.core.config.spi;

import org.nucleus8583.core.config.Order;
import org.nucleus8583.core.config.Configuration;
import org.nucleus8583.core.config.Configurer;
import org.nucleus8583.core.type.Alignment;
import org.nucleus8583.core.type.spi.*;

@Order(Order.FIRST)
public class DefaultTypeConfigurer implements Configurer {

    @Override
    public void configure(Configuration config) {
        config.define("bitmap", new Base16Bitmap.Builder());
        config.define("lbitmap", new LiteralBitmap.Builder());

        config.define("a", new AsciiText.Builder());
        config.define("n", new AsciiText.Builder().withAlignment(Alignment.TRIMMED_RIGHT).withPadWith("0").withEmptyValue("0"));
        config.define("s", new AsciiText.Builder());
        config.define("an", new AsciiText.Builder());
        config.define("ns", new AsciiText.Builder());
        config.define("ans", new AsciiText.Builder());
        config.define("amount", new AsciiAmount.Builder());

        config.define("b", new Base16Binary.Builder());
        config.define("lb", new LiteralBinary.Builder());

        config.define("bcd", new BcdNumeric.Builder());
        config.define("bcd amount", new BcdAmount.Builder());

        config.define("ebcdic", new EbcdicText.Builder());
        config.define("ebcdic amount", new EbcdicAmount.Builder());

        config.define("a .", new AsciiPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("a ..", new AsciiPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("a ...", new AsciiPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("a ....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("a .....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("n .", new AsciiPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("n ..", new AsciiPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("n ...", new AsciiPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("n ....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("n .....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("s .", new AsciiPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("s ..", new AsciiPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("s ...", new AsciiPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("s ....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("s .....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("an .", new AsciiPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("an ..", new AsciiPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("an ...", new AsciiPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("an ....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("an .....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("ns .", new AsciiPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("ns ..", new AsciiPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("ns ...", new AsciiPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("ns ....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("ns .....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("ans .", new AsciiPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("ans ..", new AsciiPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("ans ...", new AsciiPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("ans ....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("ans .....", new AsciiPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("b .", new AsciiPrefixedBase16Binary.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("b ..", new AsciiPrefixedBase16Binary.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("b ...", new AsciiPrefixedBase16Binary.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("b ....", new AsciiPrefixedBase16Binary.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("b .....", new AsciiPrefixedBase16Binary.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("lb .", new AsciiPrefixedLiteralBinary.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("lb ..", new AsciiPrefixedLiteralBinary.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("lb ...", new AsciiPrefixedLiteralBinary.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("lb ....", new AsciiPrefixedLiteralBinary.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("lb .....", new AsciiPrefixedLiteralBinary.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("a . bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("a .. bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("a ... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("a .... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("a ..... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("n . bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("n .. bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("n ... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("n .... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("n ..... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("s . bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("s .. bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("s ... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("s .... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("s ..... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("an . bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("an .. bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("an ... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("an .... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("an ..... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("as . bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("as .. bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("as ... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("as .... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("as ..... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("ns . bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("ns .. bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("ns ... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("ns .... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("ns ..... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("ans . bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("ans .. bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("ans ... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("ans .... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("ans ..... bcd", new BcdPrefixedAsciiText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("b . bcd", new BcdPrefixedBase16Binary.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("b .. bcd", new BcdPrefixedBase16Binary.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("b ... bcd", new BcdPrefixedBase16Binary.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("b .... bcd", new BcdPrefixedBase16Binary.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("b ..... bcd", new BcdPrefixedBase16Binary.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("lb . bcd", new BcdPrefixedLiteralBinary.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("lb .. bcd", new BcdPrefixedLiteralBinary.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("lb ... bcd", new BcdPrefixedLiteralBinary.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("lb .... bcd", new BcdPrefixedLiteralBinary.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("lb ..... bcd", new BcdPrefixedLiteralBinary.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("bcd . bcd", new BcdPrefixedBcdNumeric.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("bcd .. bcd", new BcdPrefixedBcdNumeric.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("bcd ... bcd", new BcdPrefixedBcdNumeric.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("bcd .... bcd", new BcdPrefixedBcdNumeric.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("bcd ..... bcd", new BcdPrefixedBcdNumeric.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("ebcdic . bcd", new BcdPrefixedEbcdicText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("ebcdic .. bcd", new BcdPrefixedEbcdicText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("ebcdic ... bcd", new BcdPrefixedEbcdicText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("ebcdic .... bcd", new BcdPrefixedEbcdicText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("ebcdic ..... bcd", new BcdPrefixedEbcdicText.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("bcd . ebcdic", new EbcdicPrefixedBcdNumeric.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("bcd .. ebcdic", new EbcdicPrefixedBcdNumeric.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("bcd ... ebcdic", new EbcdicPrefixedBcdNumeric.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("bcd .... ebcdic", new EbcdicPrefixedBcdNumeric.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("bcd ..... ebcdic", new EbcdicPrefixedBcdNumeric.Builder().withPrefixLength(5).withMaxLength(99999));

        config.define("ebcdic . ebcdic", new EbcdicPrefixedEbcdicText.Builder().withPrefixLength(1).withMaxLength(9));
        config.define("ebcdic .. ebcdic", new EbcdicPrefixedEbcdicText.Builder().withPrefixLength(2).withMaxLength(99));
        config.define("ebcdic ... ebcdic", new EbcdicPrefixedEbcdicText.Builder().withPrefixLength(3).withMaxLength(999));
        config.define("ebcdic .... ebcdic", new EbcdicPrefixedEbcdicText.Builder().withPrefixLength(4).withMaxLength(9999));
        config.define("ebcdic ..... ebcdic", new EbcdicPrefixedEbcdicText.Builder().withPrefixLength(5).withMaxLength(99999));
    }
}
