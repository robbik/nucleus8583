package org.nucleus8583.core.charset;

import java.io.IOException;
import java.io.InputStream;

public class Utf8Decoder implements CharsetDecoder {

    public int read(InputStream in) throws IOException {
        int ubyte;
        int ichar;

        boolean missing = false;

        ubyte = in.read();
        if (ubyte < 0) {
            return -1;
        }

        if ((ubyte & 0x80) == 0) {
            ichar = ubyte;
        } else if ((ubyte & 0xE0) == 0xC0) {
            ichar = ubyte & 0x1F; // 110xxxxx

            ubyte = in.read();
            if (ubyte < 0) {
                missing = true;
            } else {
                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx
            }
        } else if ((ubyte & 0xF0) == 0xE0) {
            ichar = ubyte & 0x0F; // 1110xxxx

            ubyte = in.read();
            if (ubyte < 0) {
                missing = true;
            } else {
                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                } else {
                    ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx
                }
            }
        } else if ((ubyte & 0xF8) == 0xF0) {
            ichar = ubyte & 0x07; // 11110xxx

            ubyte = in.read();
            if (ubyte < 0) {
                missing = true;
            } else {
                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                } else {
                    ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                    ubyte = in.read();
                    if (ubyte < 0) {
                        missing = true;
                    } else {
                        ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx
                    }
                }
            }
        } else if ((ubyte & 0xFC) == 0xF8) {
            ichar = ubyte & 0x03; // 111110xx

            ubyte = in.read();
            if (ubyte < 0) {
                missing = true;
            } else {
                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                } else {
                    ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                    ubyte = in.read();
                    if (ubyte < 0) {
                        missing = true;
                    } else {
                        ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                        ubyte = in.read();
                        if (ubyte < 0) {
                            missing = true;
                        } else {
                            ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx
                        }
                    }
                }
            }
        } else if ((ubyte & 0xFE) == 0xFC) {
            ichar = ubyte & 0x01; // 1111110x

            ubyte = in.read();
            if (ubyte < 0) {
                missing = true;
            } else {
                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                } else {
                    ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                    ubyte = in.read();
                    if (ubyte < 0) {
                        missing = true;
                    } else {
                        ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                        ubyte = in.read();
                        if (ubyte < 0) {
                            missing = true;
                        } else {
                            ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx
                        }
                    }
                }
            }
        } else {
            throw new IOException("data stream contains value (code=" + ubyte
                    + ") which is not compatible with UTF-8 encoding");
        }

        if (missing) {
            throw new IOException("missing data while reading in UTF-8 encoding");
        }

        return ichar;
    }

    public int read(InputStream in, char[] cbuf, int off, int len) throws IOException {
        int rlen = 0;
        boolean missing = false;

        int ubyte;
        int ichar;

        for (int j = off; rlen < len;) {
            ubyte = in.read();
            if (ubyte < 0) {
                break;
            }

            if ((ubyte & 0x80) == 0) {
                ichar = ubyte;
            } else if ((ubyte & 0xE0) == 0xC0) {
                ichar = ubyte & 0x1F; // 110xxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx
            } else if ((ubyte & 0xF0) == 0xE0) {
                ichar = ubyte & 0x0F; // 1110xxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx
            } else if ((ubyte & 0xF8) == 0xF0) {
                ichar = ubyte & 0x07; // 11110xxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx
            } else if ((ubyte & 0xFC) == 0xF8) {
                ichar = ubyte & 0x03; // 111110xx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx
            } else if ((ubyte & 0xFE) == 0xFC) {
                ichar = ubyte & 0x01; // 1111110x

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx

                ubyte = in.read();
                if (ubyte < 0) {
                    missing = true;
                    break;
                }

                ichar = (ichar << 6) | (ubyte & 0x3F); // 10xxxxxx
            } else {
                throw new IOException("data stream contains value (code=" + ubyte
                        + ") which is not compatible with UTF-8 encoding");
            }

            cbuf[j++] = (char) ichar;
            ++rlen;
        }

        if (missing) {
            throw new IOException("missing data while reading in UTF-8 encoding");
        }

        return rlen;
    }
}
