/*
 * Decompiled with CFR 0.152.
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.util.Random;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.VolumeControl;
import javax.microedition.rms.RecordStore;

public final class a
extends Canvas
implements Runnable {
    public String[] a = null;
    public String b = null;
    private Player g = null;
    public byte c;
    private Bobby h;
    private int i;
    private int j;
    private int k;
    private int l;
    private String m = "EN";
    private Random n;
    public boolean d = false;
    public boolean e = false;
    public boolean f;
    private byte[] o = new byte[4];
    private boolean[] p = new boolean[4];
    private boolean q;
    private byte[] r = new byte[7];
    private boolean s;
    private boolean t;
    private boolean u;
    private byte v;
    private short w;
    private short x;
    private long y;
    private int z;
    private String[] A = new String[5];
    private boolean B;
    private boolean C;
    private boolean D;
    private boolean E;
    private boolean F;
    private boolean G;
    private boolean H;
    private boolean I;
    private int J;
    private int K;
    private int[] L = new int[]{49, 49, 51, 51, 55, 55, 57, 57};
    private int M = 0;
    private boolean N = false;
    private boolean O = false;
    private int[] P = new int[]{57, 49, 51, 51, 55};
    private int Q = 0;
    private boolean R = false;
    private short[] S = new short[5];
    private short[] T = new short[5];
    private byte[] U = new byte[5];
    private int V;
    private byte W;
    private String X;
    private char[] Y;
    private int Z;
    private int aa;
    private String ab;
    private String ac;
    private int ad;
    private int ae;
    private String af;
    private int ag;
    private int ah;
    private int ai;
    private int aj;
    private int ak;
    private int al;
    private int am;
    private int an;
    private int ao;
    private int ap;
    private int aq;
    private int ar;
    private int as;
    private int at;
    private int au;
    private int av;
    private int aw;
    private int ax;
    private int ay;
    private int az;
    private int aA;
    private int aB;
    private int aC;
    private int aD;
    private int aE;
    private int aF;
    private int aG;
    private int aH;
    private int aI;
    private int aJ;
    private int aK;
    private int aL;
    private int aM;
    private int aN;
    private byte aO;
    private byte aP;
    private byte aQ;
    private byte aR;
    private byte aS;
    private byte aT;
    private boolean aU;
    private boolean aV;
    private boolean aW;
    private boolean aX;
    private boolean aY;
    private boolean aZ;
    private boolean ba;
    private boolean bb;
    private boolean bc;
    private boolean bd;
    private boolean be;
    private boolean bf;
    private int bg;
    private int bh;
    private int bi;
    private static final byte[] bj = new byte[]{94, 95, -112, -111};
    private byte bk = (byte)3;
    private final byte[] bl = new byte[]{5, 10, 30, 20, 10, 25, 10};
    private byte bm;
    private int bn;
    private int bo;
    private int bp;
    private int bq;
    private byte br;
    private boolean bs;
    private int bt;
    private int bu;
    private int bv;
    private int bw;
    private int bx;
    private boolean by;
    private int bz;
    private int bA;
    private int bB;
    private int bC;
    private int bD;
    private int bE;
    private int bF;
    private int bG;
    private int bH;
    private int bI;
    private int bJ;
    private int bK;
    private int bL;
    private int bM;
    private int bN;
    private int bO;
    private int bP;
    private long bQ;
    private long bR;
    private boolean bS;
    private int bT;
    private int bU;
    private byte bV;
    private int bW;
    private int[] bX = new int[5];
    private int[] bY = new int[5];
    private int[] bZ = new int[]{9, 10, 11, 12};
    private Image ca;
    private Image cb;
    private Image cc;
    private Image cd;
    private Image ce;
    private Image cf;
    private Image cg;
    private Image ch;
    private Image ci;
    private Image[] cj = new Image[10];
    private Image ck;
    private byte[][] cl;
    private byte[][] cm;
    private String cn;
    private String co;
    private byte cp;
    private String[] cq = new String[4];
    private byte[] cr = new byte[4];
    private int cs;
    private int ct;
    private int cu;
    private byte[] cv;
    private byte[] cw;
    private byte[] cx;
    private boolean[] cy;
    private short[] cz;
    private short[] cA;
    private short cB;
    private short cC;
    private short cD;
    private short cE;
    private short cF;
    private short cG;
    private short cH;
    private short cI;
    private short cJ;
    private short cK;
    private boolean cL;
    private boolean cM;
    private boolean cN;
    private boolean cO;
    private boolean cP;
    private boolean cQ;
    private boolean cR;
    private boolean cS;
    private boolean cT;
    private boolean cU;
    private boolean cV;
    private boolean cW;
    private boolean cX;
    private byte[] cY;
    private byte[] cZ;
    private byte[] da;
    private byte[] db;
    private byte[] dc = new byte[5];
    private byte[] dd = new byte[5];
    private byte[] de = new byte[5];
    private int df;
    private int dg;
    private int dh;
    private int di;
    private byte dj;
    private byte dk;
    private byte dl;
    private byte dm;
    private int dn;
    private int cfr_renamed_0;
    private int dp;
    private int dq;
    private int dr;
    private int ds;
    private int dt;
    private int du;
    private int dv;
    private int dw;
    private boolean dx;
    private byte[][] dy;
    private byte[][] dz;
    private Image dA;
    private Graphics dB;
    private Image dC;
    private Image dD = null;
    private int dE;
    private boolean dF;
    private int dG;
    private byte dH;
    private byte dI;
    private final short[] dJ = new short[]{0, 0, 1, 3, 5, 8, 12, 17, 23, 31, 41, 53, 70, 91, 118, 153, 198, 256, 256};
    private boolean dK = false;
    private boolean dL = false;
    private boolean dM = false;
    private boolean dN = false;
    private boolean dO = false;
    private String[] dP;
    private short[] dQ;
    private byte dR;
    private byte dS;
    private byte dT;
    private byte dU;
    private byte dV;
    private byte dW;
    private byte dX;
    private byte dY;
    private String dZ;
    private int ea;
    private int eb;
    private int ec;
    private int ed;
    private String ee;
    private int ef;
    private int eg;
    private int eh;
    private int ei;
    private int ej;
    private int ek;
    private int el;
    private int em;
    private int en;
    private int eo;
    private boolean ep;
    private byte eq;
    private String er;
    private byte es = 0;
    private short[] et = new short[]{59, 58, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80};
    private byte[][] eu = new byte[][]{new byte[0], new byte[0], {-54, -52}, {-57, -56}, {-106}, {-36, -96, -35}, {-72, -74, -94}, {-19}, {-61, -58, -65, -62}, {-80, -81}, {-71, -69, -66, -67, -92}, {89, 87, -90}, {-20}, {-13}, {-12, -11}, {-29, -24, -40, -39}, {-79, -78, -76, -77}, {-44, -43, -42}, {-32, -30, -14, -15}, {-48, -45, -87, -86}, {-49, -33}, {-97, 124}, {-10}};
    private byte ev;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public final void a(String string) {
        int n = 0;
        FilterInputStream filterInputStream = null;
        this.a = null;
        try {
            filterInputStream = new DataInputStream(this.getClass().getResourceAsStream(string));
            n = ((DataInputStream)filterInputStream).readShort();
            this.a = new String[n];
            for (int i = 0; i < n; ++i) {
                this.a[i] = ((DataInputStream)filterInputStream).readUTF();
            }
        }
        catch (Exception exception) {
            this.c();
        }
        finally {
            if (filterInputStream != null) {
                try {
                    filterInputStream.close();
                }
                catch (Exception exception) {}
            }
        }
    }

    public final void a(String string, int n, boolean bl) {
        if (bl) {
            if (this.b != null && this.b.compareTo(string) == 0) {
                return;
            }
        } else {
            this.b = null;
        }
        this.a();
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(string);
            this.g = Manager.createPlayer(inputStream, "audio/midi");
            this.g.setLoopCount(bl ? -1 : 1);
            this.g.realize();
            try {
                VolumeControl volumeControl = (VolumeControl)this.g.getControl("VolumeControl");
                if (volumeControl != null) {
                    volumeControl.setLevel(n * 25);
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
            this.g.prefetch();
            this.g.start();
            this.b = string;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public final void a() {
        if (this.g != null) {
            try {
                this.g.stop();
                this.g.deallocate();
                this.g.close();
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            this.g = null;
        }
        this.b = null;
    }

    public a(Bobby bobby) {
        this.h = bobby;
        this.a(this.m + ".dat");
        this.n = new Random(System.currentTimeMillis());
        try {
            this.J = Integer.parseInt(bobby.getAppProperty("X-SoftKey1"));
            this.K = Integer.parseInt(bobby.getAppProperty("X-SoftKey2"));
        }
        catch (Exception exception) {
            this.J = -6;
            this.K = -7;
        }
        this.c = 1;
        this.setFullScreenMode(true);
        this.i = this.getWidth();
        this.j = this.getHeight();
        this.k = this.j - 2 - 12 - 6;
        this.b(this.i, this.j, true);
        this.ca = this.a(this.ca, "/font.png");
        this.dC = this.a(this.dC, "/logo.png");
        this.bT = 0;
        this.bV = (byte)-1;
        this.a(true, 0);
        this.l = 10;
    }

    private final void c() {
        Alert alert = new Alert("Error", this.a[51], null, AlertType.ERROR);
        alert.setTimeout(-2);
        this.h.a.setCurrent(alert, this);
        while (!this.F) {
            try {
                Thread.sleep(50L);
            }
            catch (Exception exception) {}
        }
        this.h.destroyApp(true);
    }

    public void hideNotify() {
        if (this.d) {
            this.d = false;
            if (this.c == 1 && this.l != 16) {
                this.a();
                this.c = 0;
            }
        }
        if (!this.dL) {
            this.I = false;
            this.dL = true;
            this.l();
        }
    }

    public void showNotify() {
        if (!this.d && this.dP != null) {
            for (int i = 0; i < this.dT; ++i) {
                if (this.dQ[i] != 11) continue;
                this.dP[i] = this.a[4] + this.a[this.c == 1 ? 2 : 3];
            }
        }
        this.d = true;
    }

    public void run() {
        boolean bl = true;
        while (!this.e) {
            long l = System.currentTimeMillis();
            if (this.d && (bl |= this.b())) {
                bl = false;
                this.repaint();
                this.serviceRepaints();
            }
            bl |= this.b();
            long l2 = System.currentTimeMillis() - l + 10L;
            if (l2 >= 62L) continue;
            try {
                Thread.sleep(62L - l2);
            }
            catch (Exception exception) {}
        }
        if (this.c == 1) {
            this.a();
        }
        this.h.notifyDestroyed();
    }

    private final void d() {
        this.dU = (byte)((this.k - 22) / 25);
        this.q = false;
        this.e();
        String string = this.m;
        this.g();
        if (string.compareTo(this.m) != 0) {
            this.a(this.m + ".dat");
        }
        this.ad();
        this.j();
        this.cb = this.a(this.cb, "/numbers.png");
        this.cc = this.a(this.cc, "/arrows.png");
        this.ce = this.a(this.ce, "/misc.png");
        this.cf = this.a(this.cf, "/ts.png");
        this.cg = this.a(this.cg, "/mow.png");
        this.af();
        if (!this.f) {
            this.d((byte)2);
        } else {
            this.F = false;
            this.H = false;
            this.G = false;
            this.l = 11;
            this.c((byte)2, (byte)-1);
        }
    }

    private final void e() {
        this.f = false;
        try {
            RecordStore recordStore = null;
            int n = -1;
            try {
                recordStore = RecordStore.openRecordStore("BC5Data", true);
                n = recordStore.getNumRecords();
            }
            catch (Exception exception) {
                // empty catch block
            }
            if (n != 1) {
                int n2;
                this.f = true;
                if (n != 0) {
                    if (recordStore != null) {
                        recordStore.closeRecordStore();
                    }
                    RecordStore.deleteRecordStore("BC5Data");
                    recordStore = RecordStore.openRecordStore("BC5Data", true);
                }
                for (n2 = 0; n2 < 4; ++n2) {
                    this.o[n2] = 0;
                    this.p[n2] = false;
                }
                for (n2 = 0; n2 < this.r.length; ++n2) {
                    this.r[n2] = 0;
                }
                this.s = false;
                this.t = false;
                this.v = 0;
                this.w = 0;
                this.x = 0;
                this.y = 0L;
                this.z = this.n.nextInt();
                for (n2 = 0; n2 < this.A.length; ++n2) {
                    this.A[n2] = "";
                }
                this.u = false;
                byte[] byArray = this.i();
                recordStore.addRecord(byArray, 0, byArray.length);
            }
            recordStore.closeRecordStore();
        }
        catch (Exception exception) {
            this.c();
        }
    }

    private final void f() {
        try {
            RecordStore.deleteRecordStore("BC5Data");
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void g() {
        ByteArrayInputStream byteArrayInputStream = null;
        FilterInputStream filterInputStream = null;
        try {
            int n;
            byteArrayInputStream = new ByteArrayInputStream(this.a("BC5Data", 1));
            filterInputStream = new DataInputStream(byteArrayInputStream);
            this.m = ((DataInputStream)filterInputStream).readUTF();
            this.bk = ((DataInputStream)filterInputStream).readByte();
            for (n = 0; n < 4; ++n) {
                this.o[n] = ((DataInputStream)filterInputStream).readByte();
                this.p[n] = ((DataInputStream)filterInputStream).readBoolean();
            }
            for (n = 0; n < this.r.length; ++n) {
                this.r[n] = ((DataInputStream)filterInputStream).readByte();
            }
            this.s = ((DataInputStream)filterInputStream).readBoolean();
            this.t = ((DataInputStream)filterInputStream).readBoolean();
            this.v = ((DataInputStream)filterInputStream).readByte();
            this.w = ((DataInputStream)filterInputStream).readShort();
            this.x = ((DataInputStream)filterInputStream).readShort();
            this.y = ((DataInputStream)filterInputStream).readLong();
            this.z = ((DataInputStream)filterInputStream).readInt();
            for (n = 0; n < this.A.length; ++n) {
                this.A[n] = ((DataInputStream)filterInputStream).readUTF();
            }
            this.u = ((DataInputStream)filterInputStream).readBoolean();
        }
        catch (Exception exception) {
            this.c();
        }
        finally {
            if (filterInputStream != null) {
                try {
                    filterInputStream.close();
                }
                catch (Exception exception) {}
                filterInputStream = null;
            }
            if (byteArrayInputStream != null) {
                try {
                    byteArrayInputStream.close();
                }
                catch (Exception exception) {}
                byteArrayInputStream = null;
            }
        }
    }

    private final void h() {
        this.a("BC5Data", 1, this.i());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final byte[] i() {
        byte[] byArray = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        FilterOutputStream filterOutputStream = null;
        try {
            int n;
            byteArrayOutputStream = new ByteArrayOutputStream();
            filterOutputStream = new DataOutputStream(byteArrayOutputStream);
            ((DataOutputStream)filterOutputStream).writeUTF(this.m);
            ((DataOutputStream)filterOutputStream).writeByte(this.bk);
            for (n = 0; n < 4; ++n) {
                ((DataOutputStream)filterOutputStream).writeByte(this.o[n]);
                ((DataOutputStream)filterOutputStream).writeBoolean(this.p[n]);
            }
            for (n = 0; n < this.r.length; ++n) {
                ((DataOutputStream)filterOutputStream).writeByte(this.r[n]);
            }
            ((DataOutputStream)filterOutputStream).writeBoolean(this.s);
            ((DataOutputStream)filterOutputStream).writeBoolean(this.t);
            ((DataOutputStream)filterOutputStream).writeByte(this.v);
            ((DataOutputStream)filterOutputStream).writeShort(this.w);
            ((DataOutputStream)filterOutputStream).writeShort(this.x);
            ((DataOutputStream)filterOutputStream).writeLong(this.y);
            ((DataOutputStream)filterOutputStream).writeInt(this.z);
            for (n = 0; n < this.A.length; ++n) {
                ((DataOutputStream)filterOutputStream).writeUTF(this.A[n]);
            }
            ((DataOutputStream)filterOutputStream).writeBoolean(this.u);
            byArray = byteArrayOutputStream.toByteArray();
        }
        catch (Exception exception) {
            this.c();
        }
        finally {
            if (filterOutputStream != null) {
                try {
                    filterOutputStream.close();
                }
                catch (Exception exception) {}
                filterOutputStream = null;
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                }
                catch (Exception exception) {}
                byteArrayOutputStream = null;
            }
        }
        return byArray;
    }

    private final byte[] a(String string, int n) {
        byte[] byArray = null;
        try {
            RecordStore recordStore = RecordStore.openRecordStore(string, false);
            byArray = recordStore.getRecord(n);
            recordStore.closeRecordStore();
        }
        catch (Exception exception) {
            try {
                RecordStore.deleteRecordStore(string);
            }
            catch (Exception exception2) {
                // empty catch block
            }
            this.c();
        }
        return byArray;
    }

    private final void a(String string, int n, byte[] byArray) {
        try {
            RecordStore recordStore = RecordStore.openRecordStore(string, false);
            recordStore.setRecord(n, byArray, 0, byArray.length);
            recordStore.closeRecordStore();
        }
        catch (Exception exception) {
            try {
                RecordStore.deleteRecordStore(string);
            }
            catch (Exception exception2) {
                // empty catch block
            }
            this.c();
        }
    }

    private final void j() {
        this.l = 6;
        this.repaint();
        this.serviceRepaints();
    }

    public final void keyPressed(int n) {
        if (n == 0) {
            return;
        }
        if (!this.N) {
            if (n == this.L[this.M]) {
                ++this.M;
                if (this.M >= this.L.length) {
                    this.M = 0;
                    this.N = true;
                }
            } else {
                this.M = 0;
            }
        }
        if (!this.R) {
            if (n == this.P[this.Q]) {
                ++this.Q;
                if (this.Q >= this.P.length) {
                    this.Q = 0;
                    this.R = true;
                }
            } else {
                this.Q = 0;
            }
        }
        if (this.l == 1 && this.q) {
            if (n == 42) {
                this.O = true;
            } else if (n == 35) {
                if (this.O) {
                    this.O = false;
                    this.w = (short)(this.w + 5);
                }
            } else {
                this.O = false;
            }
        }
        this.I = true;
        if (n == this.J) {
            this.G = true;
            return;
        }
        if (n == this.K) {
            this.H = true;
            return;
        }
        if (n == 50) {
            this.B = true;
        } else if (n == 56) {
            this.C = true;
        } else if (n == 52) {
            this.D = true;
        } else if (n == 54) {
            this.E = true;
        } else if (n == 53) {
            this.F = true;
        } else {
            int n2 = this.getGameAction(n);
            if (n2 == 1) {
                this.B = true;
            } else if (n2 == 6) {
                this.C = true;
            } else if (n2 == 2) {
                this.D = true;
            } else if (n2 == 5) {
                this.E = true;
            } else if (n2 == 8) {
                this.F = true;
            }
        }
    }

    public final void keyReleased(int n) {
        if (n == 0) {
            return;
        }
        if (n == this.J) {
            this.G = false;
            return;
        }
        if (n == this.K) {
            this.H = false;
            return;
        }
        if (n == 50) {
            this.B = false;
        } else if (n == 56) {
            this.C = false;
        } else if (n == 52) {
            this.D = false;
        } else if (n == 54) {
            this.E = false;
        } else if (n == 53) {
            this.F = false;
        } else {
            int n2 = this.getGameAction(n);
            if (n2 == 1) {
                this.B = false;
            } else if (n2 == 6) {
                this.C = false;
            } else if (n2 == 2) {
                this.D = false;
            } else if (n2 == 5) {
                this.E = false;
            } else if (n2 == 8) {
                this.F = false;
            }
        }
    }

    private final void a(Graphics graphics) {
        int n;
        this.d(graphics, this.bz, this.bA);
        this.c(graphics, this.bz, this.bA);
        if (this.bh != -1 && this.bg % 8 >= 4) {
            this.a(graphics, true, (byte)-8, this.bh - this.bz, this.bi - this.bA);
        }
        this.b(graphics, this.bz, this.bA);
        if (this.cR) {
            for (n = 0; n < 3; ++n) {
                this.a(graphics, false, (byte)(52 + this.bw), this.cB * 32 - this.bz, this.cC * 32 - 16 - 32 * n - this.bA);
            }
        }
        if (this.cS) {
            for (n = 0; n < 3; ++n) {
                this.a(graphics, false, (byte)(52 + this.bw), this.cD * 32 - this.bz, this.cE * 32 + 16 + 32 * n - this.bA);
            }
        }
        if (this.cT) {
            for (n = 0; n < 3; ++n) {
                this.a(graphics, false, (byte)(55 + this.bw), this.cF * 32 - 16 - 32 * n - this.bz, this.cG * 32 - this.bA);
            }
        }
        if (this.cU) {
            for (n = 0; n < 3; ++n) {
                this.a(graphics, false, (byte)(55 + this.bw), this.cH * 32 + 16 + 32 * n - this.bz, this.cI * 32 - this.bA);
            }
        }
        this.a(graphics, this.bz, this.bA);
        if (this.dj > 0) {
            this.b(graphics, this.cd, this.bm < 4 ? 196 : 217, 0, 21, 21, this.dg - 10 - this.bz, this.dh - 10 - this.bA);
        }
        if (this.ar > 0 && this.as >= 4) {
            int n2;
            int n3;
            int n4;
            switch (this.aP) {
                case 0: {
                    n4 = 57;
                    n3 = 28;
                    n2 = 26;
                    break;
                }
                case 1: {
                    n4 = 85;
                    n3 = 16;
                    n2 = 26;
                    break;
                }
                case 2: {
                    n4 = 101;
                    n3 = 26;
                    n2 = 27;
                    break;
                }
                case 3: {
                    n4 = 127;
                    n3 = 26;
                    n2 = 26;
                    break;
                }
                default: {
                    n4 = 170;
                    n3 = 26;
                    n2 = 26;
                }
            }
            n = this.ag + (32 - n3 >> 1);
            int n5 = this.ah - 1 + -24 - n2;
            if (n5 - this.bA < 0) {
                n5 += n2 + 48 + 2;
            }
            this.b(graphics, this.cd, n4, 0, n3, n2, n - this.bz, n5 - this.bA);
        }
        if (this.cN) {
            for (n = 0; n < 5; ++n) {
                this.b(graphics, this.cd, 238, 0, 12, 8, this.bX[n] >> 4, this.bY[n] >> 4);
            }
        } else {
            this.b(graphics, this.ch, 0, this.br / 3 * 16, 16, 16, (this.bn >> 4) - this.bz, (this.bo >> 4) - this.bA);
        }
        this.b(graphics);
        if (this.aR != -1) {
            this.b(graphics, this.ci, this.aR * 48, 0, 48, 48, this.i - 48 >> 1, this.j - 48 >> 1);
        }
        if (this.bV > 0) {
            this.c(graphics);
        }
        if (!this.dN) {
            if (this.dL) {
                this.a(graphics, this.a[37], (byte)1, 0);
            } else if (this.dX > 0) {
                this.a(graphics, this.dZ, this.dY, 0);
            } else if (this.an == 5) {
                this.a(graphics, this.a[this.aR == -1 ? 53 : 54], (byte)(this.aR != -1 ? 1 : 0), 0);
            }
        }
    }

    private final void b(Graphics graphics) {
        int n = 2;
        int n2 = 2;
        if (this.bM != 0) {
            long l = !this.bS ? System.currentTimeMillis() - this.bR + this.bQ : this.bQ;
            if (this.cM) {
                if (this.cW) {
                    if ((l = 60000L - l) < 0L) {
                        l = 0L;
                    }
                } else {
                    l = 60000L;
                }
            }
            int n3 = (int)l / 60000;
            long l2 = l % 60000L;
            this.a(graphics, n, n2, n3, 2);
            int n4 = (int)l2 / 1000;
            this.a(graphics, n += 32, n2, n4, 2);
            if (this.bS || n4 % 2 == 0) {
                graphics.setClip(n -= 6, n2, 5, 13);
                graphics.drawImage(this.cb, n - 120, n2, 20);
            }
            n = this.i - (this.cL ? 28 : 17) - 2;
            graphics.setClip(n, n2, this.cL ? 28 : 17, this.cL ? 27 : 21);
            graphics.drawImage(this.cd, n - (this.cL ? 29 : 153), n2, 20);
            this.a(graphics, n -= 28, n2 + ((this.cL ? 28 : 17) - 13 >> 1), this.ct, 2);
        }
        if (this.dM) {
            n = this.i - 29 >> 1;
            graphics.setClip(n, n2, 29, 27);
            graphics.drawImage(this.cd, n - 0, n2, 20);
        }
        if (this.bM != 0) {
            n = this.i;
            n2 += (this.cL ? 27 : 21) + 2;
            if (this.cO) {
                graphics.setClip(n -= 30, n2, 28, 26);
                graphics.drawImage(this.cd, n - 57, n2, 20);
            }
            if (this.cP) {
                graphics.setClip(n -= 28, n2, 26, 27);
                graphics.drawImage(this.cd, n - 101, n2, 20);
            }
            if (this.cQ) {
                graphics.setClip(n -= 28, n2, 26, 26);
                graphics.drawImage(this.cd, n - 127, n2, 20);
            }
            if (this.cu > 0) {
                graphics.setClip(n -= 28, n2, 26, 26);
                graphics.drawImage(this.cd, n - 170, n2, 20);
            }
            if (this.cV || this.r[2] > 0) {
                graphics.setClip(n -= 18, n2, 16, 26);
                graphics.drawImage(this.cd, n - 85, n2, 20);
            }
        }
    }

    private final void a(Graphics graphics, Image image, int n, int n2, int n3, int n4, int n5, int n6) {
        this.b(graphics, image, n, n2, n3, n4, n5, n6);
        if (n3 + n5 < this.i) {
            this.b(graphics, image, n, n2, n3, n4, 256 + n5, n6);
        }
    }

    public final void paint(Graphics graphics) {
        try {
            block1 : switch (this.l) {
                case 1: {
                    this.a(graphics);
                    break;
                }
                case 2: {
                    this.a(graphics);
                    this.a(graphics, 22935, 10370, true, false, true, true);
                    this.a(graphics, this.ab, this.ac, true);
                    return;
                }
                case 4: {
                    this.d(graphics, this.bz, this.bA);
                    this.c(graphics, 0, 0);
                    this.a(graphics, 0, 0);
                    graphics.setClip(0, 0, this.i, this.j);
                    int n = this.k - this.dC.getHeight() - 32 - 48 >> 1;
                    graphics.drawImage(this.dC, this.i >> 1, n, 17);
                    this.a("EXTRA-LEVELPACK 2", graphics, this.i >> 1, n + this.dC.getHeight(), true);
                    if (!this.dN && this.dF) {
                        this.a(graphics, this.a[81], (byte)3, this.j - 26 - 5);
                    }
                    if (this.bV <= 0) break;
                    this.c(graphics);
                    break;
                }
                case 5: {
                    this.a(graphics, 22935, 10370, true, true, true, true);
                    this.a(graphics, this.eq == 0 ? this.a[31] : null, this.eq != 0 ? this.a[29] : null, true);
                    break;
                }
                case 6: {
                    graphics.setClip(0, 0, this.i, this.j);
                    graphics.setColor(0);
                    graphics.fillRect(0, 0, this.i, this.j);
                    this.a(graphics, this.a[38], (byte)0, 0);
                    return;
                }
                case 7: {
                    if (this.bV > 0) {
                        this.a(graphics);
                    } else {
                        graphics.setColor(0);
                        graphics.fillRect(0, 0, this.i, this.j);
                    }
                    if (this.ee == null) break;
                    this.d(graphics);
                    this.a(graphics, this.a[31], null, true);
                    break;
                }
                case 8: {
                    this.e(graphics);
                    break;
                }
                case 9: {
                    this.a(graphics, 22935, 10370, true, true, true, true);
                    switch (this.ev) {
                        case 0: 
                        case 1: 
                        case 2: {
                            this.a(graphics, this.a[32], this.a[33], true);
                            break block1;
                        }
                    }
                    this.a(graphics, this.a[30], null, true);
                    break;
                }
                case 10: {
                    graphics.setColor(0xFFFFFF);
                    graphics.fillRect(0, 0, this.i, this.j);
                    this.b(graphics, this.dC, 0, this.bT * 100, 176, 100, (this.i - 176 >> 1) + (this.dH == 1 ? -this.e(this.i) : this.e(this.i)), this.j - 100 >> 1);
                    break;
                }
                case 11: {
                    graphics.setColor(0);
                    graphics.fillRect(0, 0, this.i, this.j);
                    break;
                }
                case 12: {
                    graphics.setClip(0, 0, this.i, this.j);
                    graphics.setColor(1259130);
                    graphics.fillRect(0, 0, this.i, this.j);
                    this.b(graphics, this.dD, 0, 0, 256, 64, 0, 0);
                    this.a(graphics, this.dD, 0, 64, 256, 25, -(this.Z >> 4), 39);
                    this.b(graphics, this.dD, 0, 102, 256, 36, 0, 23);
                    this.a(graphics, this.dD, 0, 89, 256, 13, -(this.aa >> 4), 51);
                    if (this.bV > 0) {
                        this.c(graphics);
                    }
                    if (this.dT > 0) {
                        this.a(graphics, true);
                    } else {
                        this.a(graphics, 22935, 10370, false, false, true, true);
                        this.a(graphics, null, this.a[29], false);
                    }
                    return;
                }
                case 13: {
                    this.d(graphics, this.bz, this.bA);
                    this.c(graphics, 0, 0);
                    graphics.setClip(0, 0, this.i, this.j);
                    graphics.drawImage(this.dC, this.i >> 1, this.j, 33);
                    int n = this.i - 32 - ((this.X.length() - 1) * 9 + 8) >> 1;
                    int n2 = 16;
                    this.a(graphics, true, (byte)-10, n, n2);
                    this.a(this.X, graphics, n + 32, n2 + 10, false);
                    this.a(graphics, 22935, 10370, false, false, false, false);
                    if (this.bV > 0) {
                        this.c(graphics);
                    }
                    return;
                }
                case 14: {
                    this.d(graphics, this.bz, this.bA);
                    this.c(graphics, 0, 0);
                    if (this.ai == -1) {
                        this.a(graphics, 0, 0);
                        this.a(graphics, 22935, 10370, false, false, false, true);
                    } else {
                        this.a(graphics, true, (byte)-10, this.ai, this.aj);
                        this.a(graphics, 0, 0);
                    }
                    if (this.bV > 0) {
                        this.c(graphics);
                    }
                    return;
                }
                case 15: {
                    graphics.setClip(0, 0, this.i, this.j);
                    graphics.setColor(1259130);
                    graphics.fillRect(0, 0, this.i, this.j);
                    this.a(graphics, 22935, 10370, false, false, true, true);
                    this.a(graphics, true, (byte)-10, this.i - 32 >> 1, 16);
                    if (this.bV > 0) {
                        this.c(graphics);
                    }
                    this.a(graphics, this.a[31], null, false);
                    return;
                }
                case 16: {
                    this.a(graphics, 22935, 10370, true, true, true, true);
                    this.a(graphics, this.ab, this.ac, true);
                    return;
                }
            }
            if (this.dN) {
                this.a(graphics, true);
            }
            if (this.dO && this.bV == 0) {
                this.b(graphics, this.cc, 0, 7, 13, 7, 2, this.j - 7 - 2);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private final void a(Graphics graphics, boolean bl, byte by, int n, int n2) {
        int n3 = bl ? 4 : 2;
        int n4 = (by & 0xFF) >> n3;
        int n5 = n4 << 5;
        int n6 = (by & 0xFF) - (n4 << n3) << 5;
        this.b(graphics, bl ? this.cf : this.ck, n6, n5, 32, 32, n, n2);
    }

    private final void c(Graphics graphics) {
        int n = this.bT;
        int n2 = this.bT;
        int n3 = this.j + 32 - 1 >> 5;
        int n4 = this.i + 32 - 1 >> 5;
        graphics.setClip(0, 0, this.i, this.j);
        graphics.setColor(0);
        switch (this.bV) {
            case 1: {
                int n5;
                int n6 = n3 - 1 << 5;
                int n7 = 0;
                for (n5 = 0; n5 < n4; ++n5) {
                    graphics.fillRect(n7, 0, 32 - n2, this.j);
                    if ((n2 -= 2) < 0) {
                        n2 = 0;
                    }
                    n7 += 32;
                }
                for (n5 = 0; n5 < n3; ++n5) {
                    graphics.fillRect(0, n6, this.i, 32 - n);
                    if ((n -= 2) < 0) {
                        n = 0;
                    }
                    n6 -= 32;
                }
                if (n < 32 || n2 < 32) break;
                this.bV = 0;
                break;
            }
            case 2: {
                int n8;
                int n9 = 0;
                int n10 = n4 - 1 << 5;
                for (n8 = 0; n8 < n4; ++n8) {
                    graphics.fillRect(n10, 0, n2, this.j);
                    if ((n2 -= 2) < 0) {
                        n2 = 0;
                    }
                    n10 -= 32;
                }
                for (n8 = 0; n8 < n3; ++n8) {
                    graphics.fillRect(0, n9, this.i, n);
                    if ((n -= 2) < 0) {
                        n = 0;
                    }
                    n9 += 32;
                }
                if (n < 32 || n2 < 32) break;
                this.bV = 0;
            }
        }
    }

    private final void k() {
        this.bT += 2;
    }

    private final void a(Graphics graphics, int n, int n2) {
        if (this.aZ) {
            int n3;
            int n4;
            int n5;
            boolean bl = true;
            int n6 = 0;
            switch (this.an) {
                case 0: {
                    n5 = 40;
                    n4 = -4;
                    n3 = 0;
                    n6 = 32;
                    break;
                }
                case 1: {
                    n5 = 40;
                    n4 = -4;
                    n3 = n5;
                    n6 = -32;
                    break;
                }
                case 2: {
                    n5 = 32;
                    n4 = 0;
                    n3 = 80;
                    bl = false;
                    break;
                }
                default: {
                    n5 = 32;
                    n4 = 0;
                    n3 = 80 + n5;
                }
            }
            int n7 = 56;
            int n8 = -32;
            if ((this.aE > 0 || this.bb) && bl) {
                this.a(graphics, n6, n, n2);
            }
            this.b(graphics, this.cj[7], n3, this.am * n7, n5, n7, this.ag + n4 - n, this.ah + n8 - n2);
            if ((this.aE > 0 || this.bb) && !bl) {
                this.a(graphics, n6, n, n2);
            }
        } else if (this.bd) {
            int n9;
            switch (this.an) {
                case 0: {
                    n9 = 0;
                    break;
                }
                case 1: {
                    n9 = 80;
                    break;
                }
                case 2: {
                    n9 = 160;
                    break;
                }
                default: {
                    n9 = 240;
                }
            }
            this.b(graphics, this.cj[9], n9, 0, 80, 48, this.ag + -24 - n, this.ah + -24 - this.aN - n2);
        } else if (this.aT > 0) {
            int n10;
            switch (this.an) {
                case 0: {
                    n10 = 0;
                    break;
                }
                case 1: {
                    n10 = 48;
                    break;
                }
                case 2: {
                    n10 = 96;
                    break;
                }
                default: {
                    n10 = 144;
                }
            }
            this.b(graphics, this.cj[8], n10, this.am / 3 * 48, 48, 48, this.ag + -8 - n, this.ah + -24 - this.aN - n2);
        } else {
            int n11;
            int n12;
            int n13;
            int n14;
            boolean bl = false;
            int n15 = 0;
            if (this.an == 5) {
                n14 = 32;
                n13 = 48;
                n12 = 0;
                n11 = -24;
            } else {
                n14 = 32;
                n13 = 48;
                n12 = 0;
                n11 = -24;
                if (this.aE > 0) {
                    switch (!this.ba ? this.an : 2) {
                        case 0: {
                            n15 = 24;
                            break;
                        }
                        case 1: {
                            n15 = -24;
                            break;
                        }
                        case 3: {
                            bl = true;
                        }
                    }
                }
            }
            int n16 = this.ag + n12;
            int n17 = this.ah + n11 - this.aN;
            if (this.aE > 0 && bl) {
                this.a(graphics, n15, n, n2);
            }
            this.b(graphics, this.cj[!this.ba ? this.an : 2], this.am * n14, 0, n14, n13, n16 - n, n17 - n2);
            if (this.aE > 0 && !bl) {
                this.a(graphics, n15, n, n2);
            }
        }
    }

    private final void a(Graphics graphics, int n, int n2, int n3) {
        this.b(graphics, this.cg, this.au / 3 << 5, this.bb ? 0 : 32, 32, 32, this.ag + n - n2, this.ah - 8 - n3);
    }

    private final void b(Graphics graphics, int n, int n2) {
        for (int i = 0; i < this.cs; ++i) {
            int n3 = this.cv[i] & 0xFF;
            int n4 = n3 >> 4;
            int n5 = n4 << 5;
            int n6 = n3 - (n4 << 4) << 5;
            int n7 = this.cz[i] - n;
            int n8 = this.cA[i] - n2;
            this.b(graphics, this.cf, n6, n5, 32, 32, n7, n8);
        }
    }

    private final void b(Graphics graphics, Image image, int n, int n2, int n3, int n4, int n5, int n6) {
        graphics.setClip(n5, n6, n3, n4);
        graphics.drawImage(image, n5 - n, n6 - n2, 20);
    }

    private final void a(byte by, byte by2, int n, int n2) {
        this.dB.setClip(n, n2, 32, 32);
        this.a(by, n, n2);
        if (by == -1 || by == -57) {
            return;
        }
        this.a(by2, n, n2);
    }

    private final void a(byte by, int n, int n2) {
        int n3;
        boolean bl = false;
        if (this.bv != 0 && by == -106 && this.ct == 0) {
            n3 = 0 + this.bv - 1;
        } else if (this.bv != 0 && by == -8 && this.by) {
            n3 = 15 + this.bv - 1;
        } else if (this.bu != 0 && by == -12) {
            n3 = 26 + this.bu - 1;
        } else if (this.bt != 0 && by == 86) {
            n3 = 39 + this.bt - 1;
        } else if (this.bw != 0 && (by == 88 || by == 87 || by == 90 || by == 89 || by == 91 || by == 92 || by == 93)) {
            switch (by) {
                case 88: {
                    n3 = 31;
                    break;
                }
                case 87: {
                    n3 = 33;
                    break;
                }
                case 90: {
                    n3 = 35;
                    break;
                }
                case 89: {
                    n3 = 37;
                    break;
                }
                case 91: {
                    n3 = 46;
                    break;
                }
                case 92: {
                    n3 = 48;
                    break;
                }
                default: {
                    n3 = 50;
                }
            }
            n3 += this.bw - 1;
        } else if (this.bv != 0 && (by == -75 || by == -74 || by == -73 || by == -72)) {
            switch (by) {
                case -75: {
                    n3 = 3;
                    break;
                }
                case -74: {
                    n3 = 6;
                    break;
                }
                case -73: {
                    n3 = 9;
                    break;
                }
                default: {
                    n3 = 12;
                }
            }
            n3 += this.bv - 1;
        } else if (this.cR && this.bw != 0 && by == -48) {
            n3 = 18 + this.bw - 1;
        } else if (this.cS && this.bw != 0 && by == -47) {
            n3 = 20 + this.bw - 1;
        } else if (this.cT && this.bw != 0 && by == -46) {
            n3 = 22 + this.bw - 1;
        } else if (this.cU && this.bw != 0 && by == -45) {
            n3 = 24 + this.bw - 1;
        } else {
            bl = true;
            n3 = by & 0xFF;
        }
        int n4 = bl ? 4 : 2;
        int n5 = n3 >> n4;
        int n6 = n5 << 5;
        int n7 = n3 - (n5 << n4) << 5;
        this.dB.drawImage(bl ? this.cf : this.ck, n - n7, n2 - n6, 20);
    }

    private final void l() {
        this.bQ += System.currentTimeMillis() - this.bR;
        this.bS = true;
    }

    private final void m() {
        this.bR = System.currentTimeMillis();
        this.bS = false;
    }

    public final boolean b() {
        try {
            if (this.bV > 0) {
                this.k();
            }
            if (this.dN) {
                return this.aj() || this.bV > 0;
            }
            switch (this.l) {
                case 0: {
                    this.d();
                    return true;
                }
                case 1: {
                    if (this.dO && this.G && this.bV == 0) {
                        this.G = false;
                        this.c((byte)1, (byte)-1);
                        return true;
                    }
                    if (this.dL) {
                        if (this.I) {
                            this.G = false;
                            this.H = false;
                            this.F = false;
                            this.D = false;
                            this.E = false;
                            this.B = false;
                            this.C = false;
                            this.dL = false;
                            this.m();
                            return true;
                        }
                        return this.bV > 0;
                    }
                    if (this.dX > 0) {
                        this.dX = (byte)(this.dX - 1);
                    }
                    if (!this.aU && this.aK == 0 && this.an != 5 && this.F) {
                        this.F = false;
                        boolean bl = this.dM = !this.dM;
                        if (!this.dM) {
                            this.W();
                        }
                    }
                    if (this.ar > 0) {
                        this.as = (this.as + 1) % 8;
                        if (this.as == 0) {
                            --this.ar;
                        }
                    }
                    if (this.H()) {
                        if (this.l != 1) {
                            return true;
                        }
                        if (this.dj > 0) {
                            this.Q();
                        }
                        this.P();
                        this.S();
                        this.V();
                        if (this.cN) {
                            this.T();
                        } else {
                            this.U();
                        }
                        this.G();
                        if (this.s) {
                            this.F();
                        } else {
                            this.bh = -1;
                        }
                        if (this.dM) {
                            if (this.D) {
                                this.bF -= 16;
                            } else if (this.E) {
                                this.bF += 16;
                            } else if (this.B) {
                                this.bG -= 16;
                            } else if (this.C) {
                                this.bG += 16;
                            }
                            this.X();
                        }
                        if (this.bF != this.bz || this.bG != this.bA && this.aF == 0) {
                            this.Y();
                        }
                    }
                    return true;
                }
                case 2: {
                    return this.C();
                }
                case 3: {
                    this.h.destroyApp(true);
                    return false;
                }
                case 4: {
                    return this.ai() || this.bV > 0;
                }
                case 5: {
                    return this.an();
                }
                case 7: {
                    return this.aq() || this.bV > 0;
                }
                case 8: {
                    return this.as();
                }
                case 9: {
                    return this.at();
                }
                case 10: {
                    if (this.bV == -1) {
                        --this.dG;
                        if (this.dG < 0) {
                            this.dH = 0;
                            this.dG = 0;
                            this.bV = (byte)-2;
                        }
                    } else if (this.bV == -2) {
                        ++this.at;
                        if (this.at >= 3) {
                            this.at = 0;
                            ++this.bT;
                            if (this.bT >= 3) {
                                this.bV = (byte)-3;
                            }
                        }
                    } else if (this.bV == -3) {
                        ++this.at;
                        if (this.at >= 40) {
                            this.a(false, 0);
                            this.bV = (byte)-4;
                        }
                    } else {
                        --this.dG;
                        if (this.dG < 0) {
                            this.dH = 0;
                            this.dG = 0;
                            this.bV = 0;
                            this.bT = 0;
                            this.dC = null;
                            this.l = 0;
                        }
                    }
                    return true;
                }
                case 12: {
                    return this.B();
                }
                case 13: {
                    return this.w();
                }
                case 14: {
                    return this.r();
                }
                case 15: {
                    return this.t();
                }
                case 16: {
                    return this.E();
                }
            }
        }
        catch (Throwable throwable) {
            this.c();
        }
        return false;
    }

    private final void c(Graphics graphics, int n, int n2) {
        for (int i = 0; i < 5; ++i) {
            byte by = this.U[i];
            if (by < 0) continue;
            int n3 = by / 5;
            int n4 = n3 * 12;
            int n5 = (by - n3 * 5) * 12;
            this.b(graphics, this.ck, 64 + n5, 448 + n4, 12, 12, this.S[i] - n - 6, this.T[i] - n2 - 6);
        }
    }

    private final void n() {
        int n;
        int n2;
        this.cl = null;
        this.cm = null;
        int n3 = this.i / 32 + 1;
        this.dn = n3 * 3;
        this.cfr_renamed_0 = this.j / 32 + 1;
        this.cl = new byte[this.cfr_renamed_0][this.dn];
        this.cm = new byte[this.cfr_renamed_0][this.dn];
        this.bA = 0;
        this.bz = 0;
        for (n2 = 0; n2 < this.cfr_renamed_0; ++n2) {
            for (n = 0; n < n3 * 2; ++n) {
                int n4 = this.b(10);
                int n5 = n4 == 9 ? 71 : (n4 >= 7 ? 72 : 73);
                this.cl[n2][n] = (byte)n5;
                this.cm[n2][n] = -1;
                this.cl[n2][n + n3] = (byte)n5;
                this.cm[n2][n + n3] = -1;
            }
        }
        for (n2 = 0; n2 < this.cfr_renamed_0; ++n2) {
            for (n = 0; n < n3; ++n) {
                this.cl[n2][n + n3 * 2] = this.cl[n2][n];
                this.cm[n2][n + n3 * 2] = this.cm[n2][n];
            }
        }
        this.ag();
        this.f(this.bz, this.bA);
        this.ak = this.dn * 32 - 1;
        this.al = this.cfr_renamed_0 * 32 - 1;
        this.bB = (n3 << 1) * 32 - 1;
        this.bC = this.al - this.dq;
        if (this.bB < 0) {
            this.bB = 0;
        }
        if (this.bC < 0) {
            this.bC = 0;
        }
        for (int i = 0; i < 5; ++i) {
            this.U[i] = (byte)this.b(8);
            this.S[i] = (short)(32 + this.b(this.i));
            this.T[i] = -12;
        }
    }

    private final void o() {
        int n = 2;
        this.bz += n;
        if (this.bz > this.bB) {
            this.bz -= this.bz / 32 * 32;
            this.f(this.bz, this.bA);
        }
        this.g(this.bz, this.bA);
        for (int i = 0; i < 5; ++i) {
            byte by = this.U[i];
            if (this.bx == 0) {
                by = (byte)(by + 1);
            }
            short s = (short)(this.S[i] - n);
            if (by >= 8 || s <= -12) {
                by = 0;
                s = (short)(32 + this.b(this.i));
                this.T[i] = (short)this.b(this.j);
            }
            this.U[i] = by;
            this.S[i] = s;
        }
        ++this.bx;
        if (this.bx >= 4) {
            this.bx = 0;
        }
    }

    private final void p() {
        this.d(false);
        this.aZ = false;
        this.ba = false;
        this.aN = 0;
        this.aE = 0;
        this.bd = true;
        this.am = 0;
        this.ap = 1;
        this.an = 1;
        this.ag = (this.i >> 1) - 16;
        this.ai = -1;
        this.aj = this.ah = this.j / 3;
        this.d(this.bN);
        this.V = 0;
        this.q();
        this.n();
        this.b(true, -1);
        this.l = 14;
        this.b("/fly.mid");
    }

    private final void q() {
        int n = this.co.indexOf(35, this.V);
        if (n == -1) {
            n = this.co.length();
        }
        String string = this.co.substring(this.V, n);
        this.V = n + 1;
        this.er = string;
        this.ek = 0;
        this.ei = this.ah + 32;
        this.ej = 0;
        this.em = this.i;
        this.a(null, 0, 0, false, false, false, true);
        this.aH = 192;
    }

    private final boolean r() {
        if (this.bV == 0) {
            if (this.bU == 0) {
                this.s();
                return true;
            }
            if (this.ai == -1) {
                --this.aH;
                if (this.aH < 0) {
                    if (this.V < this.co.length()) {
                        this.q();
                    } else {
                        this.ai = this.i;
                    }
                }
            } else if (this.ai > this.ag) {
                this.ai -= 2;
            } else {
                this.x = (short)(this.x + 1);
                this.a(true, this.bN, 10);
                this.h();
                this.b(false, 0);
            }
        }
        if (this.bV != 2) {
            this.O();
            this.o();
        }
        return true;
    }

    private final void s() {
        this.er = this.a[120] + this.x + this.a[121];
        this.ek = 0;
        this.ei = 64;
        this.ej = 0;
        this.em = this.i;
        this.a(null, 0, 0, false, false, true, true);
        if (this.c == 1) {
            this.a("/cleared.mid", (int)this.bk, false);
        }
        this.b(true, -1);
        this.l = 15;
    }

    private final boolean t() {
        if (this.bV == 0) {
            if (this.bU == 0) {
                if (this.bM == 0) {
                    this.ah();
                } else {
                    this.f(true);
                }
            } else {
                if (this.G || this.F) {
                    this.F = false;
                    this.G = false;
                    this.b(false, 0);
                    return true;
                }
                if (this.B) {
                    this.B = false;
                    if (this.ek > 0) {
                        this.ek -= 2;
                        if (this.ek < 0) {
                            this.ek = 0;
                        }
                        return true;
                    }
                } else if (this.C) {
                    this.C = false;
                    if (this.ek < this.el) {
                        this.ek += 2;
                        if (this.ek > this.el) {
                            this.ek = this.el;
                        }
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private final void u() {
        this.d(false);
        this.W = (byte)(this.x > 100 ? 100 : (int)this.x);
        this.X = " X " + this.W;
        this.aH = 256;
        this.er = "0000 0000 0000 0000##" + this.a[105];
        this.Y = this.er.toCharArray();
        this.ek = 0;
        this.ei = 64;
        this.ej = 0;
        this.em = this.i;
        this.v();
        this.a(null, 0, 0, false, false, false, false);
        this.n();
        this.b(true, -1);
        this.dC = this.a(null, "/sleep.png");
        this.l = 13;
        this.b("/universe.mid");
    }

    private final void v() {
        int n = 0;
        for (int i = 1; i <= 16; ++i) {
            int n2 = this.b(32);
            this.Y[n] = (char)(n2 < 10 ? n2 + 48 : n2 - 10 + 65);
            ++n;
            if (i % 4 != 0) continue;
            ++n;
        }
        this.er = new String(this.Y);
    }

    private final boolean w() {
        if (this.bV == 0) {
            if (this.bU == 0) {
                this.y();
                if (this.A[1].length() > 0) {
                    this.ah();
                } else {
                    this.a(this.a[107] + this.W + this.a[108], 0, (byte)0);
                }
            } else if (this.aH > 0) {
                --this.aH;
                if (this.aH == 0) {
                    String string = this.x();
                    for (int i = this.A.length - 1; i > 0; --i) {
                        this.A[i] = this.A[i - 1];
                    }
                    this.A[0] = string;
                    this.er = string + "##" + this.a[106];
                    this.Y = null;
                    this.ek = 0;
                    this.ei = 64;
                    this.ej = 0;
                    this.em = this.i;
                    this.a(null, 0, 0, false, false, false, false);
                    this.x = (short)(this.x - this.W);
                    this.h();
                } else {
                    this.v();
                }
            } else if (this.F || this.G) {
                this.G = false;
                this.F = false;
                this.b(false, 0);
            }
        }
        this.o();
        return true;
    }

    private final String x() {
        byte[] byArray = new byte[10];
        this.a(byArray, 0, this.z);
        byArray[5] = this.W;
        this.a(byArray, 6, (int)System.currentTimeMillis());
        int n = 76;
        for (int i = 0; i < 10; ++i) {
            if (i == 4) continue;
            n = (byte)(n + ~((byte)(byArray[i] + i)));
        }
        byArray[4] = (byte)n;
        String string = this.a(byArray, byArray.length << 3);
        string = string.substring(0, 4) + ' ' + string.substring(4, 8) + ' ' + string.substring(8, 12) + ' ' + string.substring(12);
        return string;
    }

    private final void a(byte[] byArray, int n, int n2) {
        int n3 = 24;
        for (int i = 0; i < 4; ++i) {
            byArray[n + i] = (byte)(0xFF & n2 >>> n3);
            n3 -= 8;
        }
    }

    private final String a(byte[] byArray, int n) {
        StringBuffer stringBuffer = new StringBuffer(19);
        String string = "";
        int n2 = 0;
        boolean bl = false;
        int n3 = 0;
        for (int i = 0; i < n; ++i) {
            int n4 = byArray[i >> 3] >> 7 - i % 8 & 1;
            n2 |= n4 << 4 - n3;
            if (++n3 <= 4) continue;
            stringBuffer.append(this.a(n2));
            n3 = 0;
            n2 = 0;
        }
        if (n3 > 0) {
            stringBuffer.append(this.a(n2));
        }
        return stringBuffer.toString();
    }

    private final char a(int n) {
        if (n < 10) {
            return (char)(n + 48);
        }
        return (char)(n - 10 + 65);
    }

    private final void y() {
        this.dC = null;
        System.gc();
    }

    private final void z() {
        this.dD = null;
        System.gc();
    }

    private final void A() {
        this.j();
        this.y();
        this.c((byte)4, (byte)-1);
        this.dN = false;
        if (this.dT == 0) {
            this.er = this.a[119];
            this.ek = 0;
            this.ei = 64;
            this.ej = 0;
            this.em = this.i;
            this.a(null, 0, 0, false, false, true, true);
        }
        this.aa = 0;
        this.Z = 0;
        this.dD = this.a(this.dD, "/train.png");
        this.b(true, -1);
        this.a(true, 0);
        this.l = 12;
        this.b("/train.mid");
    }

    private final boolean B() {
        if (this.dH == 0) {
            if (this.dT > 0) {
                if (this.B) {
                    this.B = false;
                    this.dS = this.dS > 0 ? (byte)(this.dS - 1) : (byte)(this.dT - 1);
                    if (this.dS < this.dV) {
                        this.dV = this.dS;
                    } else if (this.dS >= this.dV + this.dU) {
                        this.dV = (byte)(this.dS - this.dU + 1);
                    }
                } else if (this.C) {
                    this.C = false;
                    this.E = false;
                    this.dS = this.dS < this.dT - 1 ? (byte)(this.dS + 1) : (byte)0;
                    if (this.dS < this.dV) {
                        this.dV = this.dS;
                    } else if (this.dS >= this.dV + this.dU) {
                        this.dV = (byte)(this.dS - this.dU + 1);
                    }
                } else if (this.F || this.G) {
                    this.G = false;
                    this.F = false;
                    this.b(false, -1);
                    this.a(false, 2);
                }
            } else if (this.B) {
                this.B = false;
                if (this.ek > 0) {
                    this.ek -= 2;
                    if (this.ek < 0) {
                        this.ek = 0;
                    }
                }
            } else if (this.C) {
                this.C = false;
                if (this.ek < this.el) {
                    this.ek += 2;
                    if (this.ek > this.el) {
                        this.ek = this.el;
                    }
                }
            }
            if (this.H) {
                this.H = false;
                this.b(false, -1);
                this.a(false, 1);
            }
        } else {
            --this.dG;
            if (this.dG < 0) {
                this.dG = 0;
                if (this.dH == 2) {
                    this.dH = 0;
                    switch (this.dI) {
                        case 0: {
                            break;
                        }
                        case 1: {
                            this.j();
                            this.z();
                            this.ah();
                            return true;
                        }
                        case 2: {
                            this.j();
                            if (this.c == 1) {
                                this.a();
                            }
                            this.bM = 0;
                            this.bL = this.dQ[this.dS] == 1 ? 3 : 2;
                            this.j();
                            this.z();
                            this.aa();
                            this.l = 1;
                            this.d(true);
                        }
                    }
                } else {
                    this.dH = 0;
                }
            }
        }
        this.Z += 16;
        if (this.Z >> 4 >= 256) {
            this.Z -= 4096;
        }
        this.aa += 42;
        if (this.aa >> 4 >= 256) {
            this.aa -= 4096;
        }
        return true;
    }

    private final void a(int n, String string, String string2, String string3) {
        this.ad = n;
        this.ab = string2;
        this.ac = string3;
        this.er = string;
        this.ek = 0;
        this.ei = 16;
        this.ej = 16;
        this.em = this.i - 32;
        this.a(null, 0, 0, true, false, true, true);
        if (this.eo > this.en) {
            int n2 = (this.eo - this.en) * 16;
            this.ei += n2 >> 1;
            this.ej += n2 >> 1;
            this.el = 0;
        }
        this.l = 2;
        this.a(true, -1);
    }

    private final boolean C() {
        boolean bl = false;
        if (this.dH == 0) {
            if (this.B) {
                this.B = false;
                if (this.ek > 0) {
                    this.ek -= 2;
                    if (this.ek < 0) {
                        this.ek = 0;
                    }
                    bl = true;
                }
            } else if (this.C) {
                this.C = false;
                if (this.ek < this.el) {
                    this.ek += 2;
                    if (this.ek > this.el) {
                        this.ek = this.el;
                    }
                    bl = true;
                }
            } else if (this.ab != null && this.G) {
                this.G = false;
                this.a(false, 0);
            } else if (this.ac != null && this.H) {
                this.H = false;
                this.a(false, 1);
            }
        } else {
            --this.dG;
            if (this.dG < 0) {
                this.dG = 0;
                if (this.dH == 2) {
                    this.dH = 0;
                    switch (this.dI) {
                        case 0: {
                            bl = this.a(true);
                            break;
                        }
                        case 1: {
                            bl = this.a(false);
                        }
                    }
                } else {
                    this.dH = 0;
                }
            }
            bl = true;
        }
        return bl;
    }

    private final boolean a(boolean bl) {
        this.l = 1;
        if (this.ad == -1) {
            return true;
        }
        if (!bl) {
            if (this.ad == 10) {
                this.ah();
            }
            return true;
        }
        if (this.ad == 8) {
            this.q = true;
        } else if (this.ad == 7) {
            if (this.w < 3) {
                this.cX = true;
            } else {
                this.w = (short)(this.w - 3);
            }
            this.cV = true;
        } else if (this.ad == 9) {
            this.u();
        } else if (this.ad == 10) {
            this.p();
        } else if (this.ad == 11) {
            this.f();
            this.l = 3;
        } else {
            this.w = (short)(this.w - this.bl[this.ad]);
            this.cl[this.aj][this.ai] = -98;
            this.f(this.bz, this.bA);
            int n = this.ad;
            this.r[n] = (byte)(this.r[n] + 1);
            if (this.ad == 4) {
                this.v = this.r[4];
            }
            this.h();
        }
        return true;
    }

    private final void D() {
        this.d(false);
        this.y();
        this.c(false);
        this.a(0, this.a[111], null, this.a[30], null);
        this.l = 16;
    }

    private final void a(int n, String string, String string2, String string3, String string4) {
        this.b("/universe.mid");
        this.ab = string3;
        this.ac = string4;
        this.ae = n;
        this.af = string2;
        this.er = string;
        this.ek = 0;
        this.ej = 0;
        this.em = this.i;
        this.ei = 0;
        this.a(null, 0, 0, true, true, true, true);
        this.a(true, -1);
    }

    private final boolean E() {
        boolean bl = false;
        if (this.dH == 0) {
            if (this.B) {
                this.B = false;
                if (this.ae != 5 && this.ek > 0) {
                    this.ek -= 2;
                    if (this.ek < 0) {
                        this.ek = 0;
                    }
                    bl = true;
                }
            } else if (this.C) {
                this.C = false;
                if (this.ae != 5 && this.ek < this.el) {
                    this.ek += 2;
                    if (this.ek > this.el) {
                        this.ek = this.el;
                    }
                    bl = true;
                }
            } else if (this.ab != null && this.G) {
                this.G = false;
                this.a(false, 0);
            } else if (this.ac != null && this.H) {
                this.H = false;
                this.a(false, 1);
            }
        } else {
            --this.dG;
            if (this.dG < 0) {
                this.dG = 0;
                this.dK = false;
                if (this.dH == 2) {
                    this.dH = 0;
                    switch (this.dI) {
                        case 0: {
                            bl = this.b(true);
                            break;
                        }
                        case 1: {
                            bl = this.b(false);
                        }
                    }
                } else {
                    this.dH = 0;
                }
            }
            bl = true;
        }
        return bl;
    }

    private final boolean b(boolean bl) {
        this.ab = null;
        this.ac = null;
        boolean bl2 = false;
        switch (this.ae) {
            case 0: {
                bl2 = true;
            }
        }
        if (bl2) {
            this.y();
            this.ah();
        }
        return true;
    }

    private final int b(int n) {
        int n2 = this.n.nextInt();
        if (n2 < 0) {
            n2 = -n2;
        }
        return n2 % n;
    }

    private final void F() {
        if (this.bh != -1) {
            --this.bg;
            if (this.bg <= 0) {
                this.bh = -1;
            }
        } else {
            int n = this.bz + this.b(this.dp) >> 5;
            int n2 = this.bA + this.b(this.dq) >> 5;
            if (n < this.dn && n2 < this.cfr_renamed_0 && this.cm[n2][n] == -8 && (this.cl[n2][n] == -57 || this.cl[n2][n] == -56)) {
                this.bh = n << 5;
                this.bi = n2 << 5;
                this.bg = 32;
            }
        }
    }

    private final void G() {
        if (this.aF > 0) {
            --this.aF;
            int n = 32 * this.aF / 8;
            int n2 = this.b(n + 1) - (n >> 1);
            int n3 = this.b(n + 1) - (n >> 1);
            this.bz += n2;
            this.bA += n3;
            if (this.bz < 0) {
                this.bz = 0;
            } else if (this.bz > this.bB) {
                this.bz = this.bB;
            }
            if (this.bA < 0) {
                this.bA = 0;
            } else if (this.bA > this.bC) {
                this.bA = this.bC;
            }
            this.g(this.bz, this.bA);
        }
    }

    private final boolean H() {
        int n;
        long l;
        if (this.cM && this.cW && this.an != 5 && (l = !this.bS ? 60000L - (System.currentTimeMillis() - this.bR + this.bQ) : 60000L - this.bQ) <= 0L) {
            this.aR = 0;
            this.K();
        }
        if (this.aK > 0 && this.bF == this.bz && this.bG == this.bA) {
            --this.aK;
            if (this.aK == 0) {
                this.aQ = (byte)-1;
                this.av = -1;
                this.W();
            }
        }
        if (this.bM == 0 && this.bL == 1 && !this.q && this.N) {
            this.N = false;
            this.a(8, "DO YOU WANT TO ENABLE THE CHEAT?", this.a[32], this.a[33]);
            return false;
        }
        if (this.bM == 0 && this.bL == 1 && this.R) {
            this.R = false;
            this.a(11, "DO YOU WANT TO FORMAT THE RMS AND COMPLETELY RESET THE GAME?", this.a[32], this.a[33]);
            return false;
        }
        int n2 = this.bz;
        int n3 = this.bA;
        if (this.an <= 4 && this.ap == 0 && this.aT == 0) {
            this.be = false;
            if (this.aY) {
                this.aY = false;
                this.aE = 0;
                n = 0;
                boolean bl = true;
                byte by = this.cv[this.aq];
                short s = this.cz[this.aq];
                short s2 = this.cA[this.aq];
                if (by == -20) {
                    int n4 = s2 >> 5;
                    int n5 = s >> 5;
                    byte by2 = this.cl[n4][n5];
                    if ((by2 == 87 || by2 == 91 || by2 == 92 || by2 == 93) && this.an == 2) {
                        bl = false;
                    } else if (by2 == 88 && this.an == 3) {
                        bl = false;
                    } else if (by2 == 89 && this.an == 0) {
                        bl = false;
                    } else if (by2 == 90 && this.an == 1) {
                        bl = false;
                    }
                    if (bl) {
                        switch (this.an) {
                            case 0: {
                                if (!this.a(n5 - 1, n4, this.an, by) || this.a(s - 32, (int)s2, this.aq, 0)) break;
                                n = 1;
                                break;
                            }
                            case 1: {
                                if (!this.a(n5 + 1, n4, this.an, by) || this.a(s + 32, (int)s2, this.aq, 1)) break;
                                n = 1;
                                break;
                            }
                            case 2: {
                                if (!this.a(n5, n4 - 1, this.an, by) || this.a((int)s, s2 - 32, this.aq, 2)) break;
                                n = 1;
                                break;
                            }
                            case 3: {
                                if (!this.a(n5, n4 + 1, this.an, by) || this.a((int)s, s2 + 32, this.aq, 3)) break;
                                n = 1;
                            }
                        }
                    }
                    if (n != 0) {
                        this.cx[this.aq] = 32;
                        this.cw[this.aq] = (byte)this.an;
                        this.cy[this.aq] = false;
                    }
                }
            }
            if (this.aq == -1 || this.cw[this.aq] == 4) {
                if (this.M()) {
                    if (!this.aY) {
                        this.aq = -1;
                    }
                    this.aX = true;
                    if (!this.aZ && !this.bd) {
                        this.am = 3;
                    }
                } else if (this.at == 0 && !this.aZ && !this.bd && this.aT == 0) {
                    this.am = 3;
                }
            }
        } else if (this.aT > 0) {
            this.aT = (byte)(this.aT - 1);
            if (this.aT <= 0) {
                this.aT = 0;
                n = this.ai;
                int n6 = this.aj;
                switch (this.an) {
                    case 0: {
                        --n;
                        break;
                    }
                    case 1: {
                        ++n;
                        break;
                    }
                    case 2: {
                        --n6;
                        break;
                    }
                    case 3: {
                        ++n6;
                    }
                }
                this.cl[n6][n] = 124;
                this.f(this.bz, this.bA);
                this.am = 3;
                this.at = 0;
                this.bf = true;
            }
        }
        if (this.O()) {
            return false;
        }
        if (this.an == 5 && (this.F || this.G || this.H)) {
            this.H = false;
            this.G = false;
            this.F = false;
            if (!this.cX) {
                this.ab();
                this.d(true);
            } else {
                this.f(false);
            }
            return true;
        }
        if (this.ap != 0) {
            if (this.aX && this.ap <= 16) {
                this.aX = false;
                this.J();
                if (this.aS == 0 && !this.bd) {
                    this.aN = this.aq != -1 ? 8 : 0;
                }
            }
            switch (this.aS) {
                case 1: {
                    this.aN += 4;
                    break;
                }
                case 2: {
                    this.aN -= 4;
                }
            }
            this.at = 0;
            this.N();
            if (this.ap == 0) {
                switch (this.aS) {
                    case 1: {
                        this.aS = 0;
                        this.bd = true;
                        this.aN = 16;
                        this.am = 0;
                        this.aE = 1;
                        break;
                    }
                    case 2: {
                        this.aS = 0;
                        this.bd = false;
                        this.aN = 0;
                        this.am = 0;
                    }
                }
                this.at = 0;
                this.bc = false;
                if (this.bb) {
                    this.bb = false;
                    if (this.cl[this.aj][this.ai] == -56) {
                        this.cm[this.aj][this.ai] = (byte)(this.cL ? 202 : 203);
                    }
                    this.cl[this.aj][this.ai] = bj[this.b(4)];
                    this.f(this.bz, this.bA);
                }
                switch (this.aO) {
                    case 1: {
                        this.aO = 0;
                        this.cm[this.aj][this.ai] = -1;
                        this.f(this.bz, this.bA);
                        this.aE = 0;
                        this.aZ = true;
                        this.am = 0;
                        this.b(this.I());
                        break;
                    }
                    case 2: {
                        this.aO = 0;
                        this.aZ = false;
                        this.aE = 0;
                        this.cm[this.aj][this.ai] = -36;
                        this.f(this.bz, this.bA);
                        ++this.ai;
                        this.ag += 32;
                        this.b(this.I());
                        this.C = false;
                        this.B = false;
                        this.E = false;
                        this.D = false;
                    }
                }
            }
        } else if (!(this.aZ || this.ba || this.bd || this.aT != 0 || this.an >= 4 || ++this.at < 160)) {
            this.an = 4;
            this.am = 0;
            this.aV = true;
        }
        if (this.aC != -1) {
            --this.aG;
            if (this.aG <= 0) {
                if (this.cm[this.aD][this.aC] == -43) {
                    this.cm[this.aD][this.aC] = -42;
                    this.aG = 6;
                } else {
                    this.cm[this.aD][this.aC] = -1;
                    this.aC = -1;
                }
                this.f(this.bz, this.bA);
            }
        }
        if (this.di > 0) {
            this.R();
        }
        if (this.dj == 0) {
            --this.aH;
            if (this.aH <= 0) {
                n = this.cm[this.cK][this.cJ];
                switch (n) {
                    case -41: {
                        n = -24;
                        break;
                    }
                    case -24: {
                        n = -23;
                        break;
                    }
                    default: {
                        n = -41;
                        this.dg = this.cJ * 32;
                        this.dh = this.cK * 32 + 16;
                        this.dk = 0;
                        this.dl = (byte)-1;
                        this.dm = (byte)4;
                        this.dj = 1;
                    }
                }
                this.cm[this.cK][this.cJ] = (byte)n;
                this.aH = 6;
                this.f(this.bz, this.bA);
            }
        }
        if (!this.dM && this.aK == 0) {
            this.d(this.ag, this.ah);
        }
        if (this.bz != n2 || this.bA != n3) {
            this.g(this.bz, this.bA);
        }
        return true;
    }

    private final String I() {
        if (this.aZ) {
            return "/mow.mid";
        }
        if (this.bM != 0) {
            if (!this.cM) {
                if (this.v != -1) {
                    return "/ingame" + this.v + ".mid";
                }
                return "/ingame" + this.b(this.r[4] + 1) + ".mid";
            }
            if (!this.cW) {
                return "/shop.mid";
            }
            return "/bonus.mid";
        }
        if (this.bL == 1) {
            return "/shop.mid";
        }
        if (this.bL == 2 || this.bL == 4 || this.bL == 5) {
            return "/sandman.mid";
        }
        return "/shop.mid";
    }

    private final void b(String string) {
        if (this.c == 1) {
            this.a(string, (int)this.bk, true);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private final void J() {
        byte by = this.cl[this.aj][this.ai];
        byte by2 = this.cm[this.aj][this.ai];
        if (this.aw != -1) {
            this.cl[this.ax][this.aw] = -81;
            this.f(this.bz, this.bA);
            this.aw = -1;
        } else if (this.ay != -1) {
            this.cl[this.az][this.ay] = this.b(this.cl[this.az][this.ay]);
            this.f(this.bz, this.bA);
            this.ay = -1;
        } else if (this.aL != -1) {
            this.cl[this.aM][this.aL] = this.c(this.cl[this.aM][this.aL]);
            this.f(this.bz, this.bA);
            this.aL = -1;
        } else if (this.aA != -1) {
            if (this.aC != -1) {
                this.cm[this.aD][this.aC] = -1;
                this.f(this.bz, this.bA);
            }
            this.cm[this.aB][this.aA] = -43;
            this.f(this.bz, this.bA);
            this.aC = this.aA;
            this.aD = this.aB;
            this.aG = 6;
            this.aA = -1;
        } else if (this.aI != -1) {
            --this.ct;
            this.cm[this.aJ][this.aI] = -52;
            this.f(this.bz, this.bA);
            this.aI = -1;
        }
        if (this.bd) {
            if (by2 != -11) return;
            this.aS = (byte)2;
        } else if (by2 == -12) {
            this.aS = 1;
            this.aN = 0;
            return;
        }
        if (by2 == -44) {
            this.aA = this.ai;
            this.aB = this.aj;
            return;
        }
        if (by2 == -39 && this.cJ != -1 && this.dj == -1) {
            this.dj = 0;
            this.aH = 6;
        }
        this.ba = false;
        if (!this.aZ) {
            if (by2 == -51) {
                this.cV = false;
                if (this.cM && !this.cW) {
                    this.cW = true;
                    this.b(this.I());
                    this.m();
                    this.bQ = 0L;
                }
                this.cm[this.aj][this.ai] = -1;
                this.f(this.bz, this.bA);
            } else if (by2 == -8) {
                ++this.bO;
                this.cm[this.aj][this.ai] = -1;
                this.f(this.bz, this.bA);
            } else if (by2 == -35) {
                this.ar = 0;
                this.cO = true;
                this.cm[this.aj][this.ai] = -1;
                this.f(this.bz, this.bA);
            } else if (by2 == -13) {
                this.ar = 0;
                this.cP = true;
                this.cm[this.aj][this.ai] = -1;
                this.f(this.bz, this.bA);
            } else if (by2 == -49) {
                ++this.cu;
                this.cm[this.aj][this.ai] = -1;
                this.f(this.bz, this.bA);
            } else if (by2 == -54) {
                --this.ct;
                this.cm[this.aj][this.ai] = -55;
                this.f(this.bz, this.bA);
            } else if (by2 == -53) {
                this.aI = this.ai;
                this.aJ = this.aj;
            } else if (by2 == -33) {
                if (this.cu > 0) {
                    --this.cu;
                    this.cY[this.df] = (byte)this.ai;
                    this.cZ[this.df] = (byte)this.aj;
                    this.da[this.df] = 1;
                    this.db[this.df] = 16;
                    ++this.df;
                    this.cm[this.aj][this.ai] = -17;
                    this.f(this.bz, this.bA);
                } else {
                    this.aP = (byte)4;
                    this.ar = 4;
                }
            } else if (by2 == -18 || by2 == -34 || by2 == -50) {
                this.ba = true;
            } else if (by2 == -36) {
                this.aO = 1;
            } else if (by2 == -10) {
                this.x = (short)(this.x + 1);
                this.cm[this.aj][this.ai] = -1;
                this.f(this.bz, this.bA);
                this.a(true, this.bM, this.bL);
                this.w = (short)(this.w + this.bO);
                this.dX = 0;
                this.h();
                this.d(false);
                this.l();
                this.s();
                return;
            }
        }
        if (by == -92) {
            this.L();
            return;
        } else if (by == -94) {
            this.c(0);
            return;
        } else if (by == -108) {
            this.be = true;
            this.am = 1;
            return;
        } else if ((by & 0xFF) >= 191 && (by & 0xFF) <= 194) {
            this.a(by);
            return;
        } else if (by == -90) {
            this.c(1);
            return;
        } else if (by == -80) {
            this.aw = this.ai;
            this.ax = this.aj;
            return;
        } else if (!this.aZ && (by & 0xFF) >= 185 && (by & 0xFF) <= 190) {
            this.ay = this.ai;
            this.az = this.aj;
            return;
        } else if (!this.aZ && (by & 0xFF) >= 177 && (by & 0xFF) <= 180) {
            this.aL = this.ai;
            this.aM = this.aj;
            return;
        } else if (!this.aZ && by == -97) {
            this.cQ = true;
            this.cl[this.aj][this.ai] = 124;
            this.f(this.bz, this.bA);
            return;
        } else if (by == -89) {
            this.cR = false;
            this.a((byte)-89, (byte)-88);
            return;
        } else if (by == -87) {
            this.cS = false;
            this.a((byte)-87, (byte)-86);
            return;
        } else if (by == -85) {
            this.cT = false;
            this.a((byte)-85, (byte)-84);
            return;
        } else if (by == -83) {
            this.cU = false;
            this.a((byte)-83, (byte)-82);
            return;
        } else if (by == -88) {
            this.cR = true;
            this.aQ = (byte)2;
            this.a((byte)-88, (byte)-89);
            this.aK = 64;
            this.d(this.cB * 32, this.cC * 32);
            return;
        } else if (by == -86) {
            this.cS = true;
            this.aQ = (byte)3;
            this.a((byte)-86, (byte)-87);
            this.aK = 64;
            this.d(this.cD * 32, this.cE * 32);
            return;
        } else if (by == -84) {
            this.cT = true;
            this.aQ = 0;
            this.a((byte)-84, (byte)-85);
            this.aK = 64;
            this.d(this.cF * 32, this.cG * 32);
            return;
        } else if (by == -82) {
            this.cU = true;
            this.aQ = 1;
            this.a((byte)-82, (byte)-83);
            this.aK = 64;
            this.d(this.cH * 32, this.cI * 32);
            return;
        } else if (!this.aZ && by == -81) {
            this.K();
            return;
        } else if (!this.aZ && this.ct == 0 && by == -106) {
            this.d(false);
            this.l();
            this.ao = this.an;
            this.an = 6;
            this.am = 0;
            this.aV = true;
            return;
        } else if (this.aZ && by == -96) {
            this.aO = (byte)2;
            return;
        } else {
            if ((by & 0xFF) < 151 || (by & 0xFF) > 157) return;
            int n = (by & 0xFF) - 151;
            boolean bl = this.w >= this.bl[n];
            this.a(bl ? n : -1, this.a[82 + n] + this.a[91 + n] + (bl ? this.a[99] : this.a[98]), bl ? this.a[32] : this.a[30], bl ? this.a[33] : null);
        }
    }

    private final void K() {
        this.d(false);
        this.l();
        this.ap = 0;
        this.an = 5;
        this.bd = false;
        this.ba = false;
        this.am = 0;
        if (this.dM) {
            this.dM = false;
            this.W();
        }
        if (this.c == 1) {
            this.a(this.aR == -1 ? "/death.mid" : "/alarm.mid", (int)this.bk, false);
        }
        this.I = false;
    }

    private final void L() {
        for (int i = 0; i < this.cfr_renamed_0; ++i) {
            for (int j = 0; j < this.dn; ++j) {
                this.cl[i][j] = this.b(this.cl[i][j]);
            }
        }
        this.f(this.bz, this.bA);
    }

    private final void c(int n) {
        byte by;
        byte by2;
        byte by3;
        byte by4;
        byte by5;
        byte by6;
        switch (n) {
            case 0: {
                by6 = -73;
                by5 = -72;
                by4 = -75;
                by3 = -74;
                by2 = -95;
                by = -94;
                break;
            }
            default: {
                by6 = 90;
                by5 = 89;
                by4 = 88;
                by3 = 87;
                by2 = -91;
                by = -90;
            }
        }
        for (int i = 0; i < this.cfr_renamed_0; ++i) {
            for (int j = 0; j < this.dn; ++j) {
                this.cl[i][j] = this.a(this.cl[i][j], by6, by4, by5, by3, by2, by);
            }
        }
        this.f(this.bz, this.bA);
    }

    private final void a(byte by) {
        byte by2;
        byte by3;
        byte by4;
        byte by5;
        switch (by) {
            case -65: 
            case -64: {
                by5 = -65;
                by4 = -64;
                by3 = -61;
                by2 = -60;
                break;
            }
            case -63: 
            case -62: {
                by5 = -63;
                by4 = -62;
                by3 = -59;
                by2 = -58;
                break;
            }
            default: {
                return;
            }
        }
        for (int i = 0; i < this.cfr_renamed_0; ++i) {
            for (int j = 0; j < this.dn; ++j) {
                by = this.cl[i][j];
                if (by == by5) {
                    this.cl[i][j] = by4;
                    continue;
                }
                if (by == by4) {
                    this.cl[i][j] = by5;
                    continue;
                }
                if (by == by3) {
                    this.cl[i][j] = by2;
                    continue;
                }
                if (by != by2) continue;
                this.cl[i][j] = by3;
            }
        }
        this.f(this.bz, this.bA);
    }

    private final void a(byte by, byte by2) {
        for (int i = 0; i < this.cfr_renamed_0; ++i) {
            for (int j = 0; j < this.dn; ++j) {
                if (this.cl[i][j] != by) continue;
                this.cl[i][j] = by2;
            }
        }
        this.f(this.bz, this.bA);
    }

    private final byte a(byte by, byte by2, byte by3, byte by4, byte by5, byte by6, byte by7) {
        if (by == by6) {
            return by7;
        }
        if (by == by7) {
            return by6;
        }
        if (by == by5) {
            return by3;
        }
        if (by == by3) {
            return by5;
        }
        if (by == by4) {
            return by2;
        }
        if (by == by2) {
            return by4;
        }
        return by;
    }

    private final byte b(byte by) {
        switch (by) {
            case -93: {
                return -92;
            }
            case -92: {
                return -93;
            }
            case -71: {
                return -68;
            }
            case -70: {
                return -71;
            }
            case -69: {
                return -70;
            }
            case -68: {
                return -69;
            }
            case -67: {
                return -66;
            }
            case -66: {
                return -67;
            }
        }
        return by;
    }

    private final byte c(byte by) {
        switch (by) {
            case -79: {
                return -78;
            }
            case -78: {
                return -76;
            }
            case -77: {
                return -79;
            }
            case -76: {
                return -77;
            }
        }
        return by;
    }

    private final boolean a(int n, int n2) {
        n <<= 5;
        n2 <<= 5;
        for (int i = 0; i < this.cs; ++i) {
            if (this.cw[i] != 4 || this.cz[i] != n || this.cA[i] != n2) continue;
            this.aY = true;
            this.aq = (byte)i;
            return true;
        }
        return false;
    }

    private final boolean M() {
        boolean bl;
        byte by = this.cl[this.aj][this.ai];
        byte by2 = this.cm[this.aj][this.ai];
        if (this.bf) {
            this.bf = false;
            switch (this.an) {
                case 0: {
                    if (!this.a(-1, 0, false)) break;
                    --this.ai;
                    this.ap = 32;
                    if (this.t) {
                        this.bc = true;
                    }
                    return true;
                }
                case 1: {
                    if (!this.a(1, 0, false)) break;
                    ++this.ai;
                    this.ap = 32;
                    if (this.t) {
                        this.bc = true;
                    }
                    return true;
                }
                case 2: {
                    if (!this.a(0, -1, false)) break;
                    --this.aj;
                    this.ap = 32;
                    if (this.t) {
                        this.bc = true;
                    }
                    return true;
                }
                case 3: {
                    if (!this.a(0, 1, false)) break;
                    ++this.aj;
                    this.ap = 32;
                    if (this.t) {
                        this.bc = true;
                    }
                    return true;
                }
            }
        }
        if (this.bd) {
            switch (this.an) {
                case 0: {
                    --this.ai;
                    this.ap = 32;
                    break;
                }
                case 1: {
                    ++this.ai;
                    this.ap = 32;
                    break;
                }
                case 2: {
                    --this.aj;
                    this.ap = 32;
                    break;
                }
                case 3: {
                    ++this.aj;
                    this.ap = 32;
                }
            }
            return true;
        }
        if (by2 != -44) {
            if (by == -73 && this.a(-1, 0, true)) {
                this.an = 0;
                this.aE = 3;
            } else if (by == -72 && this.a(1, 0, true)) {
                this.an = 1;
                this.aE = 3;
            } else if (by == -75 && this.a(0, -1, true)) {
                this.an = 2;
                this.aE = 3;
            } else if (by == -74 && this.a(0, 1, true)) {
                this.an = 3;
                this.aE = 3;
            }
        }
        if (this.aE > 0) {
            bl = true;
            boolean bl2 = false;
            switch (this.an) {
                case 0: {
                    if (this.a(-1, 0, false)) {
                        --this.ai;
                        if (this.dM || !this.D) break;
                        bl = false;
                        break;
                    }
                    bl2 = true;
                    break;
                }
                case 1: {
                    if (this.a(1, 0, false)) {
                        ++this.ai;
                        if (this.dM || !this.E) break;
                        bl = false;
                        break;
                    }
                    bl2 = true;
                    break;
                }
                case 2: {
                    if (this.a(0, -1, false)) {
                        --this.aj;
                        if (this.dM || !this.B) break;
                        bl = false;
                        break;
                    }
                    bl2 = true;
                    break;
                }
                case 3: {
                    if (this.a(0, 1, false)) {
                        ++this.aj;
                        if (this.dM || !this.C) break;
                        bl = false;
                        break;
                    }
                    bl2 = true;
                }
            }
            if (!bl2) {
                if (this.aZ && this.c(this.ai, this.aj) == -19) {
                    this.cm[this.aj][this.ai] = -1;
                    this.f(this.bz, this.bA);
                    this.aF = 8;
                }
                this.aE = !bl || by == -108 ? 3 : --this.aE;
                if (by == -108) {
                    this.be = true;
                    this.aE = 3;
                }
                this.ap = 32;
                return true;
            }
            this.aE = 0;
            this.aF = 8;
            this.aW = false;
        }
        if (this.ap == 0 && by == -108) {
            bl = false;
            switch (this.an) {
                case 0: {
                    if (this.a(-1, 0, false)) {
                        --this.ai;
                        break;
                    }
                    bl = true;
                    break;
                }
                case 1: {
                    if (this.a(1, 0, false)) {
                        ++this.ai;
                        break;
                    }
                    bl = true;
                    break;
                }
                case 2: {
                    if (this.a(0, -1, false)) {
                        --this.aj;
                        break;
                    }
                    bl = true;
                    break;
                }
                case 3: {
                    if (this.a(0, 1, false)) {
                        ++this.aj;
                        break;
                    }
                    bl = true;
                    break;
                }
                default: {
                    bl = true;
                }
            }
            if (!bl) {
                this.ap = 32;
                this.be = true;
                this.am = 1;
                if (!this.aZ && this.t) {
                    this.bc = true;
                }
                return true;
            }
        }
        if (!this.dM && this.aK == 0) {
            if (this.D) {
                if (this.a(-1, 0, false)) {
                    --this.ai;
                    this.an = 0;
                    this.ap = 32;
                    if (!this.aZ && this.t) {
                        this.bc = true;
                    }
                    return true;
                }
            } else if (this.E) {
                if (this.a(1, 0, false)) {
                    ++this.ai;
                    this.an = 1;
                    this.ap = 32;
                    if (!this.aZ && this.t) {
                        this.bc = true;
                    }
                    return true;
                }
            } else if (this.B) {
                if (this.a(0, -1, false)) {
                    --this.aj;
                    this.an = 2;
                    this.ap = 32;
                    if (!this.aZ && this.t) {
                        this.bc = true;
                    }
                    return true;
                }
            } else if (this.C && this.a(0, 1, false)) {
                ++this.aj;
                this.an = 3;
                this.ap = 32;
                if (!this.aZ && this.t) {
                    this.bc = true;
                }
                return true;
            }
        }
        return false;
    }

    private final byte b(int n, int n2) {
        if (n < 0 || n2 < 0 || n >= this.dn || n2 >= this.cfr_renamed_0) {
            return -1;
        }
        return this.cl[n2][n];
    }

    private final byte c(int n, int n2) {
        if (n < 0 || n2 < 0 || n >= this.dn || n2 >= this.cfr_renamed_0) {
            return -1;
        }
        return this.cm[n2][n];
    }

    private final boolean a(int n, int n2, boolean bl) {
        int n3 = this.ai + n;
        int n4 = this.aj + n2;
        this.aT = 0;
        if (n3 < 0 || n4 < 0 || n3 >= this.dn || n4 >= this.cfr_renamed_0) {
            return false;
        }
        if (this.bd) {
            return true;
        }
        byte by = this.cl[this.aj][this.ai];
        byte by2 = this.cl[n4][n3];
        byte by3 = this.cm[n4][n3];
        boolean bl2 = true;
        if (by == -66) {
            bl2 = n != 0;
        } else if (by == -67) {
            bl2 = n2 != 0;
        } else if (by == -68) {
            bl2 = n == 1 || n2 == 1;
        } else if (by == -69) {
            bl2 = n == -1 || n2 == 1;
        } else if (by == -70) {
            bl2 = n == -1 || n2 == -1;
        } else if (by == -71) {
            boolean bl3 = bl2 = n == 1 || n2 == -1;
        }
        if (!bl2) {
            return false;
        }
        if (!this.aZ && this.a(this.ai + n, this.aj + n2)) {
            return true;
        }
        boolean bl4 = bl2 = (by2 & 0xFF) >= 94 && (by2 & 0xFF) <= 200;
        if (bl2) {
            if ((by2 & 0xFF) >= 185 && (by2 & 0xFF) <= 190) {
                if (!this.aZ) {
                    switch (by2) {
                        case -71: {
                            bl2 = n == -1 || n2 == 1;
                            break;
                        }
                        case -70: {
                            bl2 = n == 1 || n2 == 1;
                            break;
                        }
                        case -69: {
                            bl2 = n == 1 || n2 == -1;
                            break;
                        }
                        case -68: {
                            bl2 = n == -1 || n2 == -1;
                            break;
                        }
                        case -67: {
                            bl2 = n2 != 0;
                            break;
                        }
                        case -66: {
                            bl2 = n != 0;
                        }
                    }
                } else {
                    bl2 = false;
                }
            } else if ((by2 & 0xFF) >= 177 && (by2 & 0xFF) <= 180) {
                bl2 = !this.aZ;
            } else if (by2 == -57 || by2 == -56) {
                if (this.aZ) {
                    this.bb = true;
                } else {
                    bl2 = false;
                }
            } else if (by2 == -61 || by2 == -59) {
                bl2 = false;
            }
        } else if (by2 == 77 && !this.aZ) {
            if (this.cQ) {
                this.aT = (byte)32;
                this.am = 0;
                this.an = n != 0 ? (n < 0 ? 0 : 1) : (n2 < 0 ? 2 : 3);
                this.bc = false;
                this.aE = 0;
                this.at = 0;
            } else {
                this.aP = (byte)3;
                this.ar = 4;
            }
        }
        if (!bl2) {
            switch (by3) {
                case -50: 
                case -44: 
                case -34: {
                    bl2 = !this.aZ;
                    break;
                }
                default: {
                    bl2 = false;
                }
            }
            if (!bl2) {
                return false;
            }
        } else {
            switch (by3) {
                case -54: {
                    bl2 = !this.aZ;
                    break;
                }
                case -52: 
                case -48: 
                case -47: 
                case -46: 
                case -45: 
                case -41: 
                case -40: 
                case -38: 
                case -37: 
                case -29: 
                case -25: 
                case -7: 
                case -6: 
                case -5: 
                case -4: 
                case -3: 
                case -2: {
                    bl2 = false;
                    break;
                }
                case -51: {
                    if (!this.aZ) {
                        if (!this.cV && this.r[2] == 0) {
                            this.aP = 1;
                            this.ar = 4;
                            bl2 = false;
                            break;
                        }
                        bl2 = true;
                        break;
                    }
                    bl2 = false;
                    break;
                }
                case -36: {
                    if (!this.cO) {
                        this.aP = 0;
                        this.ar = 4;
                        bl2 = false;
                        break;
                    }
                    bl2 = true;
                    break;
                }
                case -22: 
                case -9: {
                    if (this.cM) {
                        String string;
                        int n5;
                        boolean bl5 = false;
                        if (!this.cW) {
                            if (this.r[2] > 0) {
                                n5 = -1;
                                string = this.a[113] + this.a[114];
                            } else if (!this.cV) {
                                n5 = 7;
                                if (this.w >= 3) {
                                    string = this.a[113] + this.a[115] + this.w + this.a[116];
                                } else {
                                    string = this.a[113] + this.a[117];
                                    bl5 = true;
                                }
                            } else {
                                n5 = -1;
                                string = this.a[118];
                            }
                        } else {
                            n5 = -1;
                            string = this.a[118];
                        }
                        this.a(n5, string, this.a[n5 != -1 ? (bl5 ? 34 : 32) : 30], n5 != -1 ? this.a[33] : null);
                    } else if (this.bM == 0) {
                        if (this.bL == 1) {
                            this.a(-1, this.a[100] + this.w + this.a[101], this.a[30], null);
                        } else if (this.bL == 2) {
                            if (this.x > 0) {
                                this.a(9, this.a[102] + this.x + this.a[103], this.a[32], this.a[33]);
                            } else {
                                this.a(-1, this.a[102] + this.a[104], this.a[30], null);
                            }
                        } else if (this.bL == 3) {
                            this.a(-1, this.a[109], this.a[30], null);
                        } else if (this.bL == 4) {
                            this.a(10, this.a[112], this.a[32], this.a[33]);
                        } else if (this.bL == 5) {
                            this.a(-1, this.a[110], this.a[30], null);
                        }
                    }
                    bl2 = false;
                    break;
                }
                case -21: {
                    this.D();
                    bl2 = false;
                    break;
                }
                case -19: {
                    bl2 = this.aZ && (this.aE > 0 || bl);
                    break;
                }
                case -12: {
                    if (!this.aZ) {
                        if (this.cP) {
                            bl2 = true;
                            break;
                        }
                        this.aP = (byte)2;
                        this.ar = 4;
                        bl2 = false;
                        break;
                    }
                    bl2 = false;
                    break;
                }
                default: {
                    bl2 = true;
                }
            }
            if (!bl2) {
                return false;
            }
        }
        return true;
    }

    private final void N() {
        int n = this.an;
        int n2 = this.aE > 0 || this.bc ? 4 : 2;
        this.ap -= n2;
        if (this.an == 6) {
            n = this.ao;
        }
        switch (n) {
            case 0: {
                this.ag -= n2;
                break;
            }
            case 1: {
                this.ag += n2;
                break;
            }
            case 2: {
                this.ah -= n2;
                break;
            }
            case 3: {
                this.ah += n2;
            }
        }
    }

    private final boolean O() {
        if (!this.aW || this.an <= 3 && !this.bd && (this.aE > 0 || this.t && !this.aZ && this.aT <= 0)) {
            this.au = (this.au + 1) % 12;
            if (this.aZ) {
                this.am = (this.am + 1) % 2;
            } else if (this.bd) {
                this.am = 0;
            } else if (this.aT > 0) {
                this.am = (this.am + 1) % 9;
            } else {
                switch (this.an) {
                    case 0: 
                    case 1: 
                    case 2: 
                    case 3: {
                        if (this.ap == 0) break;
                        if (!this.be) {
                            this.am = (this.am + 1) % 8;
                            break;
                        }
                        this.am = 1;
                        break;
                    }
                    case 4: {
                        if (this.aV) {
                            ++this.am;
                            if (this.am < 3) break;
                            this.am = 1;
                            this.aV = false;
                            break;
                        }
                        --this.am;
                        if (this.am >= 0) break;
                        this.am = 1;
                        this.aV = true;
                        break;
                    }
                    case 5: {
                        if (this.am >= 7) break;
                        ++this.am;
                        break;
                    }
                    case 6: {
                        if (this.aV) {
                            ++this.am;
                            if (this.am < 10) break;
                            if (this.bM != 0) {
                                this.ap();
                            } else if (this.bL == 1) {
                                this.ah();
                                this.aF = 0;
                                this.bF = this.bz;
                                this.bG = this.bA;
                            } else if (this.bL == 5) {
                                this.u = true;
                                this.h();
                                this.bM = this.bN;
                                this.bL = 1;
                                this.aa();
                                this.d(true);
                            } else {
                                this.A();
                            }
                            return true;
                        }
                        --this.am;
                        if (this.am >= 0) break;
                        this.an = 3;
                        this.am = 3;
                        this.m();
                    }
                }
            }
            if (this.aR != -1) {
                this.aR = (byte)((this.aR + 1) % 2);
            }
            this.aW = true;
        } else {
            this.aW = false;
        }
        if (this.an <= 3 && this.be) {
            this.am = 1;
        }
        return false;
    }

    private final void P() {
        for (int i = 0; i < this.cs; ++i) {
            short s;
            short s2;
            byte by = this.cv[i];
            int n = this.cw[i];
            int n2 = this.cz[i];
            int n3 = this.cA[i];
            int n4 = this.cx[i];
            boolean bl = this.cy[i];
            short s3 = 2;
            boolean bl2 = false;
            if (n4 > 0) {
                s2 = 0;
                s = 0;
                switch (n) {
                    case 0: {
                        s = bl ? (short)-4 : (short)-s3;
                        break;
                    }
                    case 1: {
                        s = bl ? (short)4 : s3;
                        break;
                    }
                    case 2: {
                        s2 = bl ? (short)-4 : (short)-s3;
                        break;
                    }
                    case 3: {
                        short s4 = s2 = bl ? (short)4 : s3;
                    }
                }
                if (!bl || !this.a(n2 + s, n3 + s2, i, 4)) {
                    this.cz[i] = (short)(n2 += s);
                    this.cA[i] = (short)(n3 += s2);
                    if (this.aq == i) {
                        this.ag += s;
                        this.ah += s2;
                        this.ai = this.ag >> 5;
                        this.aj = this.ah >> 5;
                    }
                    n4 -= bl ? (short)4 : s3;
                }
            }
            if (i == this.av && this.aK > 1) {
                this.d(n2, n3);
                --this.aK;
            }
            if (n4 <= 0) {
                s2 = (short)(n3 >> 5);
                s = (short)(n2 >> 5);
                byte by2 = this.cl[s2][s];
                byte by3 = this.cm[s2][s];
                n4 = 0;
                bl = false;
                if (by == -20) {
                    if (by2 == 90) {
                        if (this.a(s - 1, (int)s2, 0, by) && !this.a(n2 - 32, n3, i, 0)) {
                            n = 0;
                            n4 = 32;
                        }
                    } else if (by2 == 89) {
                        if (this.a(s + 1, (int)s2, 1, by) && !this.a(n2 + 32, n3, i, 1)) {
                            n = 1;
                            n4 = 32;
                        }
                    } else if (by2 == 88) {
                        if (this.a((int)s, s2 - 1, 2, by) && !this.a(n2, n3 - 32, i, 2)) {
                            n = 2;
                            n4 = 32;
                        }
                    } else if (by2 == 87) {
                        if (this.a((int)s, s2 + 1, 3, by) && !this.a(n2, n3 + 32, i, 3)) {
                            n = 3;
                            n4 = 32;
                        }
                    } else if ((by2 == 91 || by2 == 92 || by2 == 93) && this.a((int)s, s2 + 1, 3, by) && !this.a(n2, n3 + 32, i, 3)) {
                        n = 3;
                        n4 = 32;
                        bl = true;
                    }
                } else if (by == -32 && by3 == -16 || by == -31 && by3 == -15 || by == -30 && by3 == -14) {
                    bl2 = true;
                } else if (this.aK == 0 || this.bF == this.bz && this.bG == this.bA || this.av != -1) {
                    if (this.cR && n != 2 && s == this.cB && s2 >= this.cC - 3 && s2 <= this.cC - 1 && this.a((int)s, s2 - 1, 2, by) && !this.a(n2, n3 - 32, i, 2)) {
                        n = 2;
                        n4 = 32;
                    } else if (this.cS && n != 3 && s == this.cD && s2 >= this.cE + 1 && s2 <= this.cE + 3 && this.a((int)s, s2 + 1, 3, by) && !this.a(n2, n3 + 32, i, 3)) {
                        n = 3;
                        n4 = 32;
                    } else if (this.cT && n != 0 && s2 == this.cG && s >= this.cF - 3 && s <= this.cF - 1 && this.a(s - 1, (int)s2, 0, by) && !this.a(n2 - 32, n3, i, 0)) {
                        n = 0;
                        n4 = 32;
                    } else if (this.cU && n != 1 && s2 == this.cI && s >= this.cH + 1 && s <= this.cH + 3 && this.a(s + 1, (int)s2, 1, by) && !this.a(n2 + 32, n3, i, 1)) {
                        n = 1;
                        n4 = 32;
                    }
                    if (n4 != 0 && this.aQ == n) {
                        this.aQ = (byte)-1;
                        this.av = i;
                        this.aK = 64;
                        this.d(s * 32, s2 * 32);
                    }
                }
                if (!bl2 && n4 == 0) {
                    switch (n) {
                        case 0: {
                            if (!this.a(s - 1, (int)s2, 0, by) || this.a(n2 - 32, n3, i, 0)) break;
                            n4 = 32;
                            break;
                        }
                        case 1: {
                            if (!this.a(s + 1, (int)s2, 1, by) || this.a(n2 + 32, n3, i, 1)) break;
                            n4 = 32;
                            break;
                        }
                        case 2: {
                            if (!this.a((int)s, s2 - 1, 2, by) || this.a(n2, n3 - 32, i, 2)) break;
                            n4 = 32;
                            break;
                        }
                        case 3: {
                            if (!this.a((int)s, s2 + 1, 3, by) || this.a(n2, n3 + 32, i, 3)) break;
                            n4 = 32;
                        }
                    }
                }
                if (n4 == 0) {
                    n = 4;
                }
            }
            this.cx[i] = (byte)n4;
            this.cw[i] = (byte)n;
            this.cy[i] = bl;
        }
    }

    private final boolean a(int n, int n2, int n3, int n4) {
        for (int i = 0; i < this.cs; ++i) {
            if (i == n3) continue;
            if (n3 != -1) {
                if (this.cw[i] == n4 || !this.b(n, n2, this.cz[i], this.cA[i])) continue;
                return true;
            }
            byte by = this.cx[i];
            short s = this.cz[i];
            short s2 = this.cA[i];
            switch (this.cw[i]) {
                case 0: {
                    s = (short)(s - by);
                    break;
                }
                case 1: {
                    s = (short)(s + by);
                    break;
                }
                case 2: {
                    s2 = (short)(s2 - by);
                    break;
                }
                case 3: {
                    s2 = (short)(s2 + by);
                }
            }
            if (!this.b(n, n2, s, s2)) continue;
            return true;
        }
        return false;
    }

    private final boolean b(int n, int n2, int n3, int n4) {
        int n5 = n + 32 - 1;
        int n6 = n3 + 32 - 1;
        int n7 = n2 + 32 - 1;
        int n8 = n4 + 32 - 1;
        if (n7 < n4) {
            return false;
        }
        if (n2 > n8) {
            return false;
        }
        if (n5 < n3) {
            return false;
        }
        return n <= n6;
    }

    private final boolean a(int n, int n2, int n3, byte by) {
        if (n < 0 || n2 < 0 || n >= this.dn || n2 >= this.cfr_renamed_0) {
            return false;
        }
        byte by2 = this.cm[n2][n];
        switch (by2) {
            case -44: 
            case -43: 
            case -42: 
            case -29: 
            case -28: 
            case -27: 
            case -26: 
            case -19: 
            case -7: 
            case -6: 
            case -5: 
            case -4: 
            case -3: 
            case -2: {
                return false;
            }
        }
        by2 = this.cl[n2][n];
        if (by == -20) {
            switch (by2) {
                case 88: {
                    return n3 != 3;
                }
                case 87: {
                    return n3 != 2;
                }
                case 90: {
                    return n3 != 1;
                }
                case 89: {
                    return n3 != 0;
                }
                case 85: 
                case 86: {
                    return true;
                }
                case 91: 
                case 92: 
                case 93: {
                    return n3 != 2;
                }
            }
        } else {
            switch (n3) {
                case 0: {
                    if (!this.cU || n2 != this.cI || n < this.cH + 1 || n > this.cH + 3) break;
                    return false;
                }
                case 1: {
                    if (!this.cT || n2 != this.cG || n < this.cF - 3 || n > this.cF - 1) break;
                    return false;
                }
                case 2: {
                    if (!this.cS || n != this.cD || n2 < this.cE + 1 || n2 > this.cE + 3) break;
                    return false;
                }
                case 3: {
                    if (!this.cR || n != this.cB || n2 < this.cC - 3 || n2 > this.cC - 1) break;
                    return false;
                }
            }
            if ((by2 & 0xFF) >= 71 && (by2 & 0xFF) <= 76) {
                return true;
            }
        }
        return false;
    }

    private final void Q() {
        int n;
        this.bm = (byte)((this.bm + 1) % 8);
        this.aK = 16;
        this.d(this.dg, this.dh);
        if (this.dm == 3) {
            boolean bl;
            n = this.dg / 32;
            int n2 = this.dh / 32;
            if (n < 0 || n2 < 0 || n >= this.dn || n2 >= this.cfr_renamed_0) {
                this.dj = (byte)-1;
                return;
            }
            byte by = this.cl[n2][n];
            byte by2 = this.cm[n2][n];
            boolean bl2 = bl = (by & 0xFF) >= 94 && (by & 0xFF) <= 200 || (by & 0xFF) >= 85 && (by & 0xFF) <= 93 || (by & 0xFF) >= 71 && (by & 0xFF) <= 76;
            if (bl) {
                switch (by) {
                    case -61: 
                    case -59: {
                        bl = false;
                    }
                }
            }
            if (bl) {
                switch (by2) {
                    case -41: 
                    case -40: {
                        bl = false;
                        break;
                    }
                    case -29: {
                        this.b((byte)n, (byte)n2);
                        break;
                    }
                    case -19: {
                        bl = false;
                    }
                }
            }
            if (bl) {
                switch (by) {
                    case -79: {
                        if (this.dk == 0) {
                            this.dl = (byte)3;
                            break;
                        }
                        if (this.dk == 2) {
                            this.dl = 1;
                            break;
                        }
                        bl = false;
                        break;
                    }
                    case -78: {
                        if (this.dk == 1) {
                            this.dl = (byte)3;
                            break;
                        }
                        if (this.dk == 2) {
                            this.dl = 0;
                            break;
                        }
                        bl = false;
                        break;
                    }
                    case -77: {
                        if (this.dk == 0) {
                            this.dl = (byte)2;
                            break;
                        }
                        if (this.dk == 3) {
                            this.dl = 1;
                            break;
                        }
                        bl = false;
                        break;
                    }
                    case -76: {
                        if (this.dk == 1) {
                            this.dl = (byte)2;
                            break;
                        }
                        if (this.dk == 3) {
                            this.dl = 0;
                            break;
                        }
                        bl = false;
                    }
                }
            }
            if (!bl) {
                this.dj = (byte)-1;
                return;
            }
        } else if (this.dm == 0) {
            this.dm = (byte)8;
            if (this.dl != -1) {
                this.dk = this.dl;
                this.dl = (byte)-1;
            }
        }
        this.dm = (byte)(this.dm - 1);
        n = 4;
        switch (this.dk) {
            case 0: {
                this.dg -= n;
                break;
            }
            case 1: {
                this.dg += n;
                break;
            }
            case 2: {
                this.dh -= n;
                break;
            }
            default: {
                this.dh += n;
            }
        }
    }

    private final void b(byte by, byte by2) {
        if (this.di >= 5) {
            this.cm[this.dd[0]][this.dc[0]] = -1;
            for (int i = 0; i < this.di - 1; ++i) {
                this.dc[i] = this.dc[i + 1];
                this.dd[i] = this.dd[i + 1];
                this.de[i] = this.de[i + 1];
            }
            --this.di;
        }
        this.dc[this.di] = by;
        this.dd[this.di] = by2;
        this.de[this.di] = 6;
        ++this.di;
        this.cm[by2][by] = -28;
        this.f(this.bz, this.bA);
    }

    private final void R() {
        boolean bl = false;
        for (int i = 0; i < this.di; ++i) {
            byte by = this.de[i];
            if (by <= 0) {
                byte by2 = this.dc[i];
                byte by3 = this.dd[i];
                int n = this.cm[by3][by2];
                boolean bl2 = false;
                bl = true;
                switch (n) {
                    case -28: {
                        n = -27;
                        break;
                    }
                    case -27: {
                        n = -26;
                        break;
                    }
                    case -26: {
                        n = -1;
                        bl2 = true;
                    }
                }
                this.de[i] = 6;
                this.cm[by3][by2] = (byte)n;
                if (!bl2) continue;
                for (int j = i; j < this.di - 1; ++j) {
                    this.dc[j] = this.dc[j + 1];
                    this.dd[j] = this.dd[j + 1];
                    this.de[j] = this.de[j + 1];
                }
                --this.di;
                --i;
                continue;
            }
            this.de[i] = (byte)(by - 1);
        }
        if (bl) {
            this.f(this.bz, this.bA);
        }
    }

    private final void S() {
        boolean bl = false;
        for (int i = 0; i < this.df; ++i) {
            byte by = this.db[i];
            if (by <= 0) {
                byte by2;
                byte by3 = this.cY[i];
                byte by4 = this.cZ[i];
                byte by5 = this.da[i];
                boolean bl2 = true;
                if (by4 - by5 >= 0 && this.cm[by4 - by5][by3] == -1 && ((by2 = this.cl[by4 - by5][by3]) & 0xFF) >= 0 && (by2 & 0xFF) <= 93) {
                    this.cm[by4 - by5 + 1][by3] = -34;
                    if (by5 <= 1) {
                        this.cm[by4][by3] = -18;
                    }
                    this.cm[by4 - by5][by3] = -50;
                    this.da[i] = (byte)(by5 + 1);
                    this.db[i] = 16;
                    bl2 = false;
                    bl = true;
                }
                if (!bl2) continue;
                for (int j = i; j < this.df - 1; ++j) {
                    this.cY[j] = this.cY[j + 1];
                    this.cZ[j] = this.cZ[j + 1];
                    this.db[j] = this.db[j + 1];
                }
                --this.df;
                --i;
                continue;
            }
            this.db[i] = (byte)(by - 1);
        }
        if (bl) {
            this.f(this.bz, this.bA);
        }
    }

    private final void T() {
        int n = 32;
        if (this.bW < this.bA) {
            n -= 16;
        } else if (this.bW > this.bA) {
            n += 16;
        }
        int n2 = 0;
        while (n2 < 5) {
            if (this.bY[n2] >> 4 > this.j) {
                this.bX[n2] = this.b(this.i) << 4;
                this.bY[n2] = -(this.b(10) << 4);
            }
            int n3 = n2;
            this.bX[n3] = this.bX[n3] + (this.b(3) - 1 << 4);
            int n4 = n2++;
            this.bY[n4] = this.bY[n4] + n;
        }
        this.bW = this.bA;
    }

    private final void U() {
        if (this.bs) {
            this.br = (byte)(this.br + 1);
            if (this.br >= 8) {
                this.bs = false;
                this.br = (byte)8;
            }
        } else {
            this.br = (byte)(this.br - 1);
            if (this.br <= 0) {
                this.bs = true;
                this.br = 0;
            }
        }
        int n = this.bn >> 4;
        int n2 = this.bo >> 4;
        if (n == this.bp) {
            this.bp = this.b(this.ak + 1 - 16);
        }
        if (n2 == this.bq) {
            this.bq = this.b(this.al + 1 - 16);
        }
        int n3 = this.b(32);
        this.bn += n < this.bp ? n3 : -n3;
        n3 = this.b(16);
        this.bo += n2 < this.bq ? n3 : -n3;
    }

    private final void V() {
        if (this.bx == 0) {
            int n;
            int n2;
            this.bt = (this.bt + 1) % 8;
            this.bu = (this.bu + 1) % 6;
            this.bv = (this.bv + 1) % 4;
            this.bw = (this.bw + 1) % 3;
            for (int i = 0; i < 3; ++i) {
                int n3 = this.U[i] + 1;
                if (n3 == 0 || n3 >= 8) {
                    n2 = this.b(this.i) + this.bz;
                    if (this.c(n2 / 32, (n = this.b(this.j) + this.bA) / 32) == -1) {
                        int n4 = this.b(n2 / 32, n / 32) & 0xFF;
                        if (n4 < 71 || n4 > 76) {
                            n3 = -1;
                        }
                    } else {
                        n3 = -1;
                    }
                    this.S[i] = (short)n2;
                    this.T[i] = (short)n;
                }
                this.U[i] = (byte)n3;
            }
            for (n = 0; n < this.dw; ++n) {
                for (n2 = 0; n2 < this.dv; ++n2) {
                    boolean bl = false;
                    byte by = this.dy[n][n2];
                    byte by2 = this.dz[n][n2];
                    switch (by) {
                        case -75: 
                        case -74: 
                        case -73: 
                        case -72: 
                        case 86: 
                        case 87: 
                        case 88: 
                        case 89: 
                        case 90: 
                        case 91: 
                        case 92: 
                        case 93: {
                            bl = true;
                            break;
                        }
                        case -106: {
                            if (this.ct != 0) break;
                            bl = true;
                        }
                    }
                    if (!bl) {
                        switch (by2) {
                            case -48: {
                                bl = this.cR;
                                break;
                            }
                            case -47: {
                                bl = this.cS;
                                break;
                            }
                            case -46: {
                                bl = this.cT;
                                break;
                            }
                            case -45: {
                                bl = this.cU;
                                break;
                            }
                            case -12: {
                                bl = true;
                                break;
                            }
                            case -8: {
                                bl = this.by;
                            }
                        }
                    }
                    if (!bl) continue;
                    this.a(by, by2, n2 << 5, n << 5);
                }
            }
        }
        if (this.bv == 0) {
            if (this.by) {
                this.by = false;
            } else if (this.b(7) == 0) {
                this.by = true;
            }
        }
        ++this.bx;
        if (this.bx >= 4) {
            this.bx = 0;
        }
    }

    private final void W() {
        this.bD = 0;
        this.bE = 0;
        this.d(this.ag, this.ah);
        this.bI = 0;
        this.bH = 0;
        this.bK = 0;
        this.bJ = 0;
        this.aU = true;
        this.Y();
    }

    private final void d(int n, int n2) {
        this.bF = n + 16 - (this.dp >> 1) + this.bD;
        this.bG = n2 + 16 - (this.dq >> 1) + this.bE;
        this.X();
    }

    private final void X() {
        if (this.bF < 0) {
            this.bF = 0;
        } else if (this.bF > this.bB) {
            this.bF = this.bB;
        }
        if (this.bG < 0) {
            this.bG = 0;
        } else if (this.bG > this.bC) {
            this.bG = this.bC;
        }
    }

    private final void Y() {
        int n = this.bF - this.bz;
        int n2 = this.bG - this.bA;
        boolean bl = true;
        boolean bl2 = true;
        if (n < 0) {
            n = -n;
            bl = false;
        }
        if (n2 < 0) {
            n2 = -n2;
            bl2 = false;
        }
        if (n != 0) {
            if (n > this.bJ + this.bH) {
                if (this.bH < (this.aU || this.dM || this.aK > 0 ? 16 : 4)) {
                    ++this.bH;
                    this.bJ += this.bH;
                }
            } else if (n < this.bJ + this.bH && this.bH > 1) {
                this.bJ -= this.bH;
                --this.bH;
            }
        } else {
            this.bJ = 0;
            this.bH = 0;
        }
        if (n2 != 0) {
            if (n2 > this.bK + this.bI) {
                if (this.bI < (this.aU || this.dM || this.aK > 0 ? 16 : 4)) {
                    ++this.bI;
                    this.bK += this.bI;
                }
            } else if (n2 < this.bK + this.bI && this.bI > 1) {
                this.bK -= this.bI;
                --this.bI;
            }
        } else {
            this.bK = 0;
            this.bI = 0;
        }
        if (n - this.bH < 0) {
            this.bH = n;
        }
        if (n2 - this.bI < 0) {
            this.bI = n2;
        }
        this.bz += bl ? this.bH : -this.bH;
        this.bA += bl2 ? this.bI : -this.bI;
        this.g(this.bz, this.bA);
        if (this.aU && this.bF == this.bz && this.bG == this.bA) {
            this.aU = false;
        }
    }

    private final void Z() {
        for (int i = 0; i < this.cj.length; ++i) {
            this.cj[i] = this.a(this.cj[i], "/b" + i + ".png");
        }
        this.ck = this.a(this.ck, "/ta.png");
        this.cd = this.a(this.cd, "/hud.png");
        this.ch = this.a(this.ch, "/bf.png");
        this.ci = this.a(this.ci, "/alarm.png");
        System.gc();
    }

    private final void c(boolean bl) {
        for (int i = 0; i < (bl ? 9 : 10); ++i) {
            this.cj[i] = null;
        }
        if (!bl) {
            this.ck = null;
        }
        this.ch = null;
        this.ci = null;
    }

    private final void aa() {
        System.gc();
        this.Z();
        this.ab();
    }

    private final void ab() {
        String string;
        int n;
        this.N = false;
        this.R = false;
        this.b(true, -1);
        this.cH = (short)-1;
        this.cF = (short)-1;
        this.cD = (short)-1;
        this.cB = (short)-1;
        this.cU = false;
        this.cT = false;
        this.cS = false;
        this.cR = false;
        this.cJ = (short)-1;
        this.dg = -1;
        this.dj = (byte)-1;
        this.cN = false;
        if (this.bM == 0) {
            this.s = false;
            this.t = false;
        }
        this.e(this.bM, this.bL);
        this.ae();
        if (this.cN) {
            for (n = 0; n < 5; ++n) {
                this.bX[n] = this.b(this.i) << 4;
                this.bY[n] = this.b(this.j) << 4;
            }
        } else {
            this.bp = this.b(2) == 0 ? -16 : this.ak + 1;
            this.bq = this.b(this.al + 1 - 16);
            this.bn = this.bp << 4;
            this.bo = this.bq << 4;
        }
        this.bh = -1;
        this.bO = 0;
        this.cX = false;
        this.cV = false;
        this.cW = false;
        this.cu = 0;
        this.bS = true;
        this.bQ = 0L;
        this.bt = 0;
        this.bu = 0;
        this.bv = 0;
        this.bw = 0;
        this.dL = false;
        this.ap = 0;
        this.an = 6;
        this.aV = false;
        this.aW = false;
        this.am = 9;
        this.aX = false;
        this.aY = false;
        this.cQ = false;
        this.cP = false;
        this.cO = false;
        this.aE = 0;
        this.aF = 0;
        this.aq = -1;
        this.aZ = false;
        this.ba = false;
        this.bd = false;
        this.aS = 0;
        this.bf = false;
        this.be = false;
        this.aO = 0;
        this.bb = false;
        this.aT = 0;
        this.bc = false;
        this.ar = 0;
        this.at = 0;
        this.aN = 0;
        this.aR = (byte)-1;
        this.aK = 0;
        this.aR = (byte)-1;
        this.aQ = (byte)-1;
        this.av = -1;
        this.aw = -1;
        this.aI = -1;
        this.ay = -1;
        this.aL = -1;
        this.aC = -1;
        this.aA = -1;
        for (n = 0; n < 5; ++n) {
            this.U[n] = (byte)(-this.b(8) - 1);
        }
        this.ak = (this.dn << 5) - 1;
        this.al = (this.cfr_renamed_0 << 5) - 1;
        this.bB = this.ak - this.dp;
        this.bC = this.al - this.dq;
        if (this.bB < 0) {
            this.bB = 0;
        }
        if (this.bC < 0) {
            this.bC = 0;
        }
        this.dM = false;
        this.aU = false;
        this.bD = 0;
        this.bE = 0;
        this.d(this.ag, this.ah);
        this.bz = this.bF;
        this.bA = this.bG;
        this.ag();
        this.f(this.bz, this.bA);
        if (this.bM != 0) {
            if (this.bL == 11 || this.bL == 12) {
                string = this.a[42];
            } else {
                int n2 = this.bM;
                if (this.bM <= this.bZ.length) {
                    n2 = this.bZ[this.bM - 1];
                }
                string = this.a[39] + n2 + '-' + this.bL;
            }
        } else {
            switch (this.bL) {
                case 1: {
                    string = this.a[22];
                    break;
                }
                case 2: {
                    string = this.a[40];
                    break;
                }
                case 3: {
                    string = this.a[41];
                    break;
                }
                case 4: {
                    string = this.a[42];
                    break;
                }
                default: {
                    string = this.a[43];
                }
            }
        }
        this.a((byte)75, string, (byte)1);
        this.b(this.I());
    }

    private final void a(byte by, String string, byte by2) {
        this.dX = by;
        this.dZ = string;
        this.dY = by2;
    }

    private final Image a(Image image, String string) {
        if (image != null) {
            return image;
        }
        try {
            return Image.createImage(string);
        }
        catch (Throwable throwable) {
            this.c();
            return null;
        }
    }

    private final void ac() {
        this.cl = null;
        this.cm = null;
        this.cv = null;
        this.cw = null;
        this.cx = null;
        this.cz = null;
        this.cA = null;
        this.cy = null;
        this.cY = null;
        this.cZ = null;
        this.da = null;
        this.db = null;
        System.gc();
    }

    private final void ad() {
        int n = 0;
        for (int i = 1; i <= 4; ++i) {
            this.d(i);
            this.cq[n] = this.cn;
            this.cr[n] = this.cp;
            ++n;
        }
    }

    private final void d(int n) {
        int n2 = 1;
        if (this.m.compareTo("DE") == 0) {
            n2 = 0;
        } else if (this.m.compareTo("EN") == 0) {
            n2 = 1;
        } else if (this.m.compareTo("FR") == 0) {
            n2 = 2;
        } else if (this.m.compareTo("IT") == 0) {
            n2 = 3;
        } else if (this.m.compareTo("SP") == 0) {
            n2 = 4;
        } else if (this.m.compareTo("PG") == 0) {
            n2 = 5;
        }
        try {
            InputStream inputStream = this.getClass().getResourceAsStream((n < 10 ? "0" : "") + n + ".dat");
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            dataInputStream.readShort();
            this.cp = dataInputStream.readByte();
            for (int i = 0; i <= n2; ++i) {
                this.cn = dataInputStream.readUTF();
                this.co = dataInputStream.readUTF();
            }
            dataInputStream.close();
            dataInputStream = null;
        }
        catch (Exception exception) {
            this.c();
        }
    }

    private final void e(int n, int n2) {
        this.ac();
        this.bP = 0;
        try {
            int n3;
            int n4;
            int n5;
            int n6;
            InputStream inputStream = this.getClass().getResourceAsStream((n < 10 ? "0" : "") + n + ".dat");
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            for (n6 = 0; n6 < n2; ++n6) {
                int n7;
                for (n5 = dataInputStream.readShort(); (n7 = dataInputStream.skipBytes(n5)) < n5; n5 -= n7) {
                }
            }
            dataInputStream.readShort();
            this.dn = dataInputStream.readByte();
            this.cfr_renamed_0 = dataInputStream.readByte();
            this.cl = new byte[this.cfr_renamed_0][this.dn];
            for (n6 = 0; n6 < this.cfr_renamed_0; ++n6) {
                dataInputStream.readFully(this.cl[n6]);
            }
            this.cm = new byte[this.cfr_renamed_0][this.dn];
            for (n4 = 0; n4 < this.cfr_renamed_0; ++n4) {
                for (n3 = 0; n3 < this.dn; ++n3) {
                    this.cm[n4][n3] = -1;
                }
            }
            this.cs = dataInputStream.readByte();
            this.cv = new byte[this.cs];
            this.cw = new byte[this.cs];
            this.cx = new byte[this.cs];
            this.cz = new short[this.cs];
            this.cA = new short[this.cs];
            this.cy = new boolean[this.cs];
            n5 = dataInputStream.readShort();
            int n8 = 0;
            for (n6 = 0; n6 < n5; ++n6) {
                byte by = dataInputStream.readByte();
                n3 = dataInputStream.readByte();
                n4 = dataInputStream.readByte();
                boolean bl = false;
                switch (by) {
                    case -32: 
                    case -31: 
                    case -30: 
                    case -20: {
                        this.cz[n8] = (short)(n3 << 5);
                        this.cA[n8] = (short)(n4 << 5);
                        this.cw[n8] = 4;
                        this.cx[n8] = 0;
                        this.cv[n8] = by;
                        ++n8;
                        bl = true;
                        break;
                    }
                    case -41: {
                        this.cm[n4][n3 + 1] = -40;
                        this.cm[n4][n3 + 2] = -39;
                        break;
                    }
                    case -38: {
                        this.cm[n4 + 1][n3] = -22;
                        break;
                    }
                    case -37: {
                        this.cm[n4 + 1][n3] = -21;
                        break;
                    }
                    case -25: {
                        this.cm[n4 + 1][n3] = -9;
                        break;
                    }
                    case -8: {
                        ++this.bP;
                    }
                }
                if (bl) continue;
                this.cm[n4][n3] = by;
            }
            dataInputStream.close();
            dataInputStream = null;
        }
        catch (Exception exception) {
            this.c();
        }
    }

    private final void ae() {
        byte[] byArray = new byte[]{0, 0, 0, 0, 0, 0, 0};
        if (this.bM == 0 && this.bL == 1) {
            byArray[6] = (byte)(1 - this.r[6]);
            byArray[4] = (byte)(2 - this.r[4]);
            byArray[3] = (byte)(1 - this.r[3]);
            byArray[5] = (byte)(1 - this.r[5]);
            byArray[2] = (byte)(1 - this.r[2]);
            byArray[0] = (byte)(1 - this.r[0]);
            byArray[1] = (byte)(1 - this.r[1]);
        }
        this.ct = 0;
        int n = 0;
        this.cM = this.bM != 0 && (this.bL == 11 || this.bL == 12);
        for (int i = 0; i < this.cfr_renamed_0; ++i) {
            for (int j = 0; j < this.dn; ++j) {
                byte by = this.cl[i][j];
                byte by2 = this.cm[i][j];
                if (by == -107) {
                    this.ai = j;
                    this.aj = i;
                    this.ag = j << 5;
                    this.ah = i << 5;
                } else if (by == -56) {
                    ++this.ct;
                } else if (by == 77) {
                    this.cN = true;
                } else if (this.bM == 0 && this.bL == 1 && (by & 0xFF) >= 151 && (by & 0xFF) <= 157) {
                    int n2 = (by & 0xFF) - 151;
                    if (byArray[n2] > 0) {
                        int n3 = n2;
                        byArray[n3] = (byte)(byArray[n3] - 1);
                    } else {
                        this.cl[i][j] = -98;
                    }
                }
                if (by2 == -54) {
                    this.cL = true;
                    ++this.ct;
                    continue;
                }
                if (by2 == -53) {
                    this.cL = false;
                    ++this.ct;
                    continue;
                }
                if (by2 == -33) {
                    ++n;
                    continue;
                }
                if (by2 == -48) {
                    this.cB = (short)j;
                    this.cC = (short)i;
                    continue;
                }
                if (by2 == -47) {
                    this.cD = (short)j;
                    this.cE = (short)i;
                    continue;
                }
                if (by2 == -46) {
                    this.cF = (short)j;
                    this.cG = (short)i;
                    continue;
                }
                if (by2 == -45) {
                    this.cH = (short)j;
                    this.cI = (short)i;
                    continue;
                }
                if (by2 != -41) continue;
                this.cJ = (short)j;
                this.cK = (short)i;
            }
        }
        this.df = 0;
        this.cY = new byte[n];
        this.cZ = new byte[n];
        this.da = new byte[n];
        this.db = new byte[n];
        this.di = 0;
    }

    private final void b(int n, int n2, boolean bl) {
        this.dp = n;
        this.dq = n2;
        this.dx = bl;
        this.dr = n + 32 - 1 >> 5;
        this.ds = n2 + 32 - 1 >> 5;
        int n3 = !this.dx ? 3 : 1;
        this.dv = this.dr + n3;
        this.dw = this.ds + n3;
        this.dt = this.dv << 5;
        this.du = this.dw << 5;
    }

    private final void af() {
        this.dB = null;
        this.dA = null;
        this.dA = Image.createImage(this.dt, this.du);
        this.dB = this.dA.getGraphics();
        this.dz = null;
        this.dy = this.dz;
        this.dy = new byte[this.dw][this.dv];
        this.dz = new byte[this.dw][this.dv];
        for (int i = 0; i < this.dw; ++i) {
            for (int j = 0; j < this.dv; ++j) {
                this.dy[i][j] = -1;
                this.dz[i][j] = 0;
            }
        }
    }

    private final void ag() {
        this.dB.setColor(0);
        this.dB.setClip(0, 0, this.dt, this.du);
        this.dB.fillRect(0, 0, this.dt, this.du);
        for (int i = 0; i < this.dw; ++i) {
            for (int j = 0; j < this.dv; ++j) {
                this.dy[i][j] = -1;
                this.dz[i][j] = 0;
            }
        }
    }

    private final void f(int n, int n2) {
        int n3 = !this.dx ? 1 : 0;
        int n4 = (n >> 5) - n3;
        int n5 = (n2 >> 5) - n3;
        int n6 = n4 + this.dr + n3;
        int n7 = n5 + this.ds + n3;
        if (n4 < 0) {
            n4 = 0;
        }
        if (n6 >= this.dn) {
            n6 = this.dn - 1;
        }
        if (n5 < 0) {
            n5 = 0;
        }
        if (n7 >= this.cfr_renamed_0) {
            n7 = this.cfr_renamed_0 - 1;
        }
        int n8 = n4 % this.dv;
        int n9 = n5 % this.dw;
        for (int i = n5; i <= n7; ++i) {
            int n10 = n8;
            for (int j = n4; j <= n6; ++j) {
                this.c(j, i, n10, n9);
                if (++n10 < this.dv) continue;
                n10 = 0;
            }
            if (++n9 < this.dw) continue;
            n9 = 0;
        }
    }

    private final void g(int n, int n2) {
        int n3;
        int n4 = !this.dx ? 1 : 0;
        int n5 = (n >> 5) - n4;
        int n6 = (n2 >> 5) - n4;
        int n7 = n5 + this.dr + n4;
        int n8 = n6 + this.ds + n4;
        int n9 = n5 >= 0 ? n5 : 0;
        int n10 = n7 < this.dn ? n7 : this.dn - 1;
        int n11 = n6 % this.dw;
        int n12 = n8 % this.dw;
        int n13 = n9 % this.dv;
        for (n3 = n9; n3 <= n10; ++n3) {
            if (n6 >= 0) {
                this.c(n3, n6, n13, n11);
            }
            if (n8 < this.cfr_renamed_0) {
                this.c(n3, n8, n13, n12);
            }
            if (++n13 < this.dv) continue;
            n13 = 0;
        }
        n9 = n6 >= 0 ? n6 : 0;
        n10 = n8 < this.cfr_renamed_0 ? n8 : this.cfr_renamed_0 - 1;
        n11 = n5 % this.dv;
        n12 = n7 % this.dv;
        int n14 = n9 % this.dw;
        for (n3 = n9; n3 <= n10; ++n3) {
            if (n5 >= 0) {
                this.c(n5, n3, n11, n14);
            }
            if (n7 < this.dn) {
                this.c(n7, n3, n12, n14);
            }
            if (++n14 < this.dw) continue;
            n14 = 0;
        }
    }

    private final void c(int n, int n2, int n3, int n4) {
        byte by = this.cl[n2][n];
        byte by2 = this.cm[n2][n];
        if (this.dy[n4][n3] == by && this.dz[n4][n3] == by2) {
            return;
        }
        this.dy[n4][n3] = by;
        this.dz[n4][n3] = by2;
        this.a(by, by2, n3 << 5, n4 << 5);
    }

    private final void d(Graphics graphics, int n, int n2) {
        if (this.dA != null) {
            boolean bl = false;
            boolean bl2 = false;
            int n3 = n % this.dt;
            int n4 = n2 % this.du;
            if (n3 + this.dp > this.dt) {
                bl = true;
            }
            if (n4 + this.dq > this.du) {
                bl2 = true;
            }
            graphics.drawImage(this.dA, -n3, -n4, 20);
            if (bl) {
                graphics.drawImage(this.dA, this.dt - n3, -n4, 20);
            }
            if (bl2) {
                graphics.drawImage(this.dA, -n3, this.du - n4, 20);
            }
            if (bl && bl2) {
                graphics.drawImage(this.dA, this.dt - n3, this.du - n4, 20);
            }
        }
    }

    private final void d(boolean bl) {
        this.dO = bl;
    }

    private final void ah() {
        this.j();
        this.c(true);
        this.cj[9] = this.a(this.cj[9], "/b9.png");
        this.ck = this.a(this.ck, "/ta.png");
        this.dC = this.a(this.dC, "/title.png");
        this.aZ = false;
        this.ba = false;
        this.aN = 0;
        this.aE = 0;
        this.bd = true;
        this.am = 0;
        this.ap = 1;
        this.at = 0;
        this.an = 1;
        this.ao = 0;
        this.aw = 0;
        this.ax = 0;
        this.az = (this.k - this.dC.getHeight() - 32 - 48 >> 1) + this.dC.getHeight() + 32 + 16;
        this.ag = -80;
        this.ah = this.k;
        this.ai = this.ag << 4;
        this.aj = this.ah << 4;
        this.n();
        this.b(true, -1);
        this.l = 4;
        this.b("/title.mid");
        this.dE = 0;
        this.dF = true;
        this.d(false);
        this.I = false;
    }

    private final boolean ai() {
        if (this.bV == 0 && this.I) {
            this.H = false;
            this.G = false;
            this.F = false;
            this.c((byte)0, (byte)-1);
            return true;
        }
        ++this.dE;
        if (this.dE >= 20) {
            this.dE = 0;
            this.dF = !this.dF;
        }
        this.ai += this.aw;
        this.ag = this.ai >> 4;
        this.aj += this.ax;
        this.ah = this.aj >> 4;
        if (this.ah <= this.az) {
            this.ax = 0;
        }
        switch (this.ao) {
            case 0: {
                ++this.at;
                if (this.at < 64) break;
                this.aw = 64;
                this.ax = -16;
                ++this.ao;
                break;
            }
            case 1: {
                if (this.ag <= this.i - 32 >> 1) break;
                ++this.ao;
                break;
            }
            case 2: {
                if (this.aw > 0) {
                    this.aw -= 4;
                    break;
                }
                ++this.ao;
                break;
            }
            case 3: {
                if (this.aw > -32) {
                    this.aw -= 4;
                }
                if (this.ag > (this.i - 32 >> 1) + 16) break;
                ++this.ao;
                break;
            }
            case 4: {
                if (this.aw < 0) {
                    this.aw += 4;
                    break;
                }
                this.aw = 0;
                ++this.ao;
            }
        }
        this.O();
        this.o();
        return true;
    }

    private final int e(int n) {
        switch (this.dH) {
            case 0: {
                return 0;
            }
            case 1: {
                return this.dJ[this.dG] * n >> 8;
            }
        }
        return this.dJ[18 - this.dG] * n >> 8;
    }

    private final void a(boolean bl, int n) {
        this.dH = (byte)(bl ? 1 : 2);
        this.dI = (byte)n;
        this.dG = 18;
    }

    private final void c(byte by, byte by2) {
        int n;
        int n2;
        int n3 = 0;
        int n4 = -1;
        this.dR = by;
        this.dW = by2;
        if (!this.dN) {
            if (!this.dL) {
                this.l();
            }
            this.d(false);
            this.dN = true;
        }
        this.dP = new String[20];
        this.dQ = new short[20];
        this.dV = 0;
        this.dS = 0;
        switch (by) {
            case 0: {
                for (n2 = 0; n2 < 4; ++n2) {
                    if (this.o[n2] <= 0) continue;
                    this.dP[n3] = this.a[1];
                    this.dQ[n3++] = 1;
                    break;
                }
                this.dP[n3] = this.a[0];
                this.dQ[n3++] = 0;
                this.dP[n3] = this.a[22];
                this.dQ[n3++] = 20;
                this.dP[n3] = this.a[23];
                this.dQ[n3++] = 14;
                if (this.A[0].length() > 0) {
                    this.dP[n3] = this.a[24];
                    this.dQ[n3++] = 15;
                }
                this.dP[n3] = this.a[18];
                this.dQ[n3++] = 10;
                this.dP[n3] = this.a[4] + this.a[this.c == 1 ? 2 : 3];
                this.dQ[n3++] = 11;
                this.dP[n3] = this.a[5] + this.bk;
                this.dQ[n3++] = 13;
                if (this.r[3] > 0) {
                    this.dP[n3] = this.a[6];
                    this.dQ[n3++] = 16;
                }
                this.dP[n3] = this.a[17];
                this.dQ[n3++] = 12;
                this.dP[n3] = this.a[19];
                this.dQ[n3++] = 3;
                this.dP[n3] = this.a[21];
                this.dQ[n3++] = 4;
                break;
            }
            case 1: {
                if (this.bM != 0) {
                    if (this.q) {
                        this.dP[n3] = "CHEAT!";
                        this.dQ[n3++] = 99;
                    }
                    if (this.r[5] > 0) {
                        this.dP[n3] = this.a[87] + this.a[this.t ? 2 : 3];
                        this.dQ[n3++] = 30;
                    }
                    if (this.r[6] > 0) {
                        this.dP[n3] = this.a[88] + this.a[this.s ? 2 : 3];
                        this.dQ[n3++] = 31;
                    }
                    if (!this.cX) {
                        this.dP[n3] = this.a[27];
                        this.dQ[n3++] = 6;
                    }
                    this.dP[n3] = this.a[18];
                    this.dQ[n3++] = 10;
                }
                this.dP[n3] = this.a[4] + this.a[this.c == 1 ? 2 : 3];
                this.dQ[n3++] = 11;
                if (this.bM != 0 && this.r[4] > 0) {
                    this.dP[n3] = this.a[89] + (this.v != -1 ? Integer.toString(this.v + 1) : this.a[90]);
                    this.dQ[n3++] = 32;
                    n4 = this.a[89].length() + this.a[90].length();
                }
                this.dP[n3] = this.a[20];
                this.dQ[n3++] = 8;
                break;
            }
            case 2: {
                int n5;
                n = 0;
                int n6 = 0;
                while ((n5 = "DEUTSCH=DE;ENGLISH=EN;FRAN\u00c7AIS=FR;ITALIANO=IT;ESPA\u00d1OL=SP;PORTUGU\u00caS=PG;".indexOf(59, n6)) != -1) {
                    ++n;
                    n6 = n5 + 1;
                }
                n6 = 0;
                for (n2 = 0; n2 < n; ++n2) {
                    n5 = "DEUTSCH=DE;ENGLISH=EN;FRAN\u00c7AIS=FR;ITALIANO=IT;ESPA\u00d1OL=SP;PORTUGU\u00caS=PG;".indexOf(61, n6);
                    this.dP[n3] = "DEUTSCH=DE;ENGLISH=EN;FRAN\u00c7AIS=FR;ITALIANO=IT;ESPA\u00d1OL=SP;PORTUGU\u00caS=PG;".substring(n6, n5);
                    n6 = n5 + 1;
                    this.dQ[n3++] = (short)n6;
                    n5 = "DEUTSCH=DE;ENGLISH=EN;FRAN\u00c7AIS=FR;ITALIANO=IT;ESPA\u00d1OL=SP;PORTUGU\u00caS=PG;".indexOf(59, n6);
                    if (this.m.compareTo("DEUTSCH=DE;ENGLISH=EN;FRAN\u00c7AIS=FR;ITALIANO=IT;ESPA\u00d1OL=SP;PORTUGU\u00caS=PG;".substring(n6, n5)) == 0) {
                        this.dS = (byte)n2;
                    }
                    n6 = n5 + 1;
                }
                break;
            }
            case 3: {
                int n5 = 0;
                for (n2 = 1; n2 <= 4; ++n2) {
                    if (by2 == 0 || by2 == 1 && this.o[n5] > 0) {
                        this.dP[n3] = this.cq[n5];
                        this.dQ[n3++] = (short)n2;
                    }
                    ++n5;
                }
                break;
            }
            case 4: {
                if (this.r[0] > 0) {
                    this.dP[n3] = this.a[25];
                    this.dQ[n3++] = 1;
                }
                if (this.r[1] <= 0) break;
                this.dP[n3] = this.a[26];
                this.dQ[n3++] = 2;
                break;
            }
            case 5: {
                this.dP[n3] = this.a[7];
                this.dQ[n3++] = 0;
                this.dP[n3] = this.a[8];
                this.dQ[n3++] = 1;
                this.dP[n3] = this.a[9];
                this.dQ[n3++] = 2;
                this.dP[n3] = this.a[12];
                this.dQ[n3++] = 3;
                this.dP[n3] = this.a[13];
                this.dQ[n3++] = 4;
                this.dP[n3] = this.a[14];
                this.dQ[n3++] = 5;
                this.dP[n3] = this.a[15];
                this.dQ[n3++] = 6;
                this.dP[n3] = this.a[16];
                this.dQ[n3++] = 7;
                this.dP[n3] = this.a[10];
                this.dQ[n3++] = 8;
                this.dP[n3] = this.a[11];
                this.dQ[n3++] = 9;
            }
        }
        this.dT = (byte)n3;
        this.ea = this.k - Math.min(this.dU, n3) * 25 >> 1;
        if (this.dS >= n3) {
            this.dS = (byte)(n3 - 1);
        }
        if (this.dV > this.dS) {
            this.dV = this.dS;
        }
        if (this.dS >= this.dV + this.dU) {
            this.dV = (byte)(this.dS - this.dU + 1);
        }
        this.eb = n4 == -1 ? 0 : n4;
        for (n2 = 0; n2 < n3; ++n2) {
            n = this.dP[n2].length();
            if (n <= this.eb) continue;
            this.eb = n;
        }
        this.eb = (this.eb + 1) * 9 + 8;
        if (this.dR == 2) {
            this.ec = (this.i - this.eb >> 1) - 20;
            this.eb += 58;
        } else if (this.dR == 3) {
            this.ec = (this.i - this.eb >> 1) - 20;
            this.ed = this.i - this.ec - 20;
            this.eb += 58;
        }
        this.eb = Math.max((this.i << 1) / 3, this.eb);
        this.a(true, -1);
    }

    private final boolean aj() {
        boolean bl = false;
        if (this.dH == 0) {
            if (this.B) {
                this.B = false;
                this.dS = this.dS > 0 ? (byte)(this.dS - 1) : (byte)(this.dT - 1);
                if (this.dS < this.dV) {
                    this.dV = this.dS;
                } else if (this.dS >= this.dV + this.dU) {
                    this.dV = (byte)(this.dS - this.dU + 1);
                }
                bl = true;
            } else if (this.C) {
                this.C = false;
                this.E = false;
                this.dS = this.dS < this.dT - 1 ? (byte)(this.dS + 1) : (byte)0;
                if (this.dS < this.dV) {
                    this.dV = this.dS;
                } else if (this.dS >= this.dV + this.dU) {
                    this.dV = (byte)(this.dS - this.dU + 1);
                }
                bl = true;
            } else if (this.E || this.D) {
                bl = this.e(this.D);
                this.D = false;
                this.E = false;
            } else if (this.G || this.F) {
                this.I = false;
                this.F = false;
                this.G = false;
                short s = this.dQ[this.dS];
                if (this.dR == 5 || this.dR != 2 && (s == 11 || s == 13 || s == 30 || s == 31 || s == 32 || s == 101)) {
                    bl = this.ak();
                } else {
                    if (this.dR == 0 && s != 0 && s != 1 && s != 16 && s != 12 || this.dR == 1 && s != 99 && s != 100 || this.dR == 3) {
                        this.b(false, -1);
                    }
                    this.a(false, 0);
                }
            } else if (this.H) {
                this.I = false;
                this.H = false;
                if (this.l != 11) {
                    this.a(false, 1);
                }
            }
        } else {
            --this.dG;
            if (this.dG < 0) {
                this.dG = 0;
                if (this.bV <= 0) {
                    if (this.dH == 2) {
                        this.dH = 0;
                        switch (this.dI) {
                            case 0: {
                                this.ak();
                                break;
                            }
                            case 1: {
                                this.al();
                            }
                        }
                    } else {
                        this.dH = 0;
                    }
                }
            }
            bl = true;
        }
        return bl;
    }

    private final boolean e(boolean bl) {
        boolean bl2 = false;
        if (this.dR == 0 && this.dQ[this.dS] == 13) {
            if (bl && this.bk > 1) {
                bl2 = true;
                this.bk = (byte)(this.bk - 1);
            } else if (!bl && this.bk < 5) {
                bl2 = true;
                this.bk = (byte)(this.bk + 1);
            }
            if (bl2) {
                if (this.c == 1) {
                    this.a();
                    this.b("/title.mid");
                }
                this.h();
                this.dP[this.dS] = this.a[5] + this.bk;
            }
        }
        return bl2;
    }

    private final boolean ak() {
        boolean bl = false;
        boolean bl2 = false;
        int n = this.dQ[this.dS];
        switch (this.dR) {
            case 0: 
            case 1: {
                switch (n) {
                    case 0: 
                    case 1: {
                        this.c((byte)3, (byte)n);
                        bl = true;
                        break;
                    }
                    case 3: {
                        this.a(this.a[52], 0, (byte)-1);
                        this.dN = false;
                        bl = true;
                        break;
                    }
                    case 4: {
                        this.d((byte)1);
                        return true;
                    }
                    case 6: {
                        this.j();
                        this.ab();
                        this.l = 1;
                        this.d(true);
                        bl2 = true;
                        break;
                    }
                    case 8: {
                        if (this.bM != 0 || this.bL == 4) {
                            this.d((byte)0);
                        } else {
                            this.am();
                            if (this.bL == 1 || this.bL == 5) {
                                this.ah();
                            } else if (this.bL == 2 || this.bL == 3) {
                                this.A();
                            }
                        }
                        return true;
                    }
                    case 10: {
                        this.ar();
                        this.dN = false;
                        bl = true;
                        break;
                    }
                    case 11: {
                        this.c = this.c == 1 ? (byte)0 : 1;
                        if (this.l == 4) {
                            if (this.c == 0) {
                                this.a();
                            } else {
                                this.b("/title.mid");
                            }
                        } else if (this.c == 0) {
                            this.a();
                        } else {
                            this.b(this.I());
                        }
                        this.h();
                        this.dP[this.dS] = this.a[4] + this.a[this.c == 1 ? 2 : 3];
                        bl = true;
                        break;
                    }
                    case 12: {
                        this.c((byte)2, (byte)-1);
                        bl = true;
                        break;
                    }
                    case 13: {
                        this.bk = (byte)(this.bk + 1);
                        if (this.bk > 5) {
                            this.bk = 1;
                        }
                        if (this.c == 1) {
                            this.a();
                            this.b("/title.mid");
                        }
                        this.h();
                        this.dP[this.dS] = this.a[5] + this.bk;
                        bl = true;
                        break;
                    }
                    case 14: {
                        this.A();
                        break;
                    }
                    case 15: {
                        String string = this.a[122];
                        for (int i = 0; i < this.A.length && this.A[i].length() > 0; ++i) {
                            string = string + this.A[i] + '#';
                            if (i != 0) continue;
                            string = string + '#';
                        }
                        this.a(string, 0, (byte)-1);
                        this.dN = false;
                        bl = true;
                        break;
                    }
                    case 16: {
                        this.c((byte)5, (byte)0);
                        bl = true;
                        break;
                    }
                    case 20: {
                        this.j();
                        this.y();
                        if (this.c == 1) {
                            this.a();
                        }
                        this.bM = 0;
                        this.bL = 1;
                        this.aa();
                        this.l = 1;
                        bl2 = true;
                        break;
                    }
                    case 30: {
                        this.t = !this.t;
                        this.h();
                        this.dP[this.dS] = this.a[87] + this.a[this.t ? 2 : 3];
                        bl = true;
                        break;
                    }
                    case 31: {
                        this.s = !this.s;
                        this.h();
                        this.dP[this.dS] = this.a[88] + this.a[this.s ? 2 : 3];
                        bl = true;
                        break;
                    }
                    case 32: {
                        this.v = (byte)(this.v + 1);
                        if (this.v > this.r[4]) {
                            this.v = (byte)-1;
                        }
                        this.b(this.I());
                        this.h();
                        this.dP[this.dS] = this.a[89] + (this.v != -1 ? Integer.toString(this.v + 1) : this.a[90]);
                        bl = true;
                        break;
                    }
                    case 99: {
                        this.am();
                        this.ct = 0;
                        this.d(false);
                        this.l();
                        if (this.dM) {
                            this.dM = false;
                            this.W();
                        }
                        this.ao = this.an;
                        this.an = 6;
                        this.am = 0;
                        this.aV = true;
                        this.ap();
                        bl = true;
                    }
                }
                break;
            }
            case 2: {
                String string = "DEUTSCH=DE;ENGLISH=EN;FRAN\u00c7AIS=FR;ITALIANO=IT;ESPA\u00d1OL=SP;PORTUGU\u00caS=PG;".substring(n, "DEUTSCH=DE;ENGLISH=EN;FRAN\u00c7AIS=FR;ITALIANO=IT;ESPA\u00d1OL=SP;PORTUGU\u00caS=PG;".indexOf(59, n));
                if (string.compareTo(this.m) != 0) {
                    this.m = string;
                    this.a(this.m + ".dat");
                    this.h();
                    this.ad();
                }
                if (this.l != 11) {
                    this.c((byte)0, (byte)-1);
                } else {
                    this.am();
                    this.d((byte)2);
                }
                bl = true;
                break;
            }
            case 3: {
                this.j();
                this.y();
                if (this.c == 1) {
                    this.a();
                }
                if (this.u) {
                    this.bM = n;
                    if (this.dW == 0) {
                        this.bL = 1;
                    } else {
                        this.bL = this.o[n - 1];
                        if (this.bL == 11 && this.i(this.bM, 11)) {
                            this.bL = 3;
                        } else if (this.bL == 12 && this.i(this.bM, 12)) {
                            this.bL = 6;
                        }
                    }
                } else {
                    this.bN = n;
                    this.bM = 0;
                    this.bL = 5;
                }
                this.aa();
                this.l = 1;
                bl2 = true;
                break;
            }
            case 5: {
                String string;
                switch (n) {
                    case 0: 
                    case 1: 
                    case 2: {
                        string = "/ingame" + n + ".mid";
                        break;
                    }
                    case 3: {
                        string = "/mow.mid";
                        break;
                    }
                    case 4: {
                        string = "/sandman.mid";
                        break;
                    }
                    case 5: {
                        string = "/shop.mid";
                        break;
                    }
                    case 6: {
                        string = "/universe.mid";
                        break;
                    }
                    case 7: {
                        string = "/fly.mid";
                        break;
                    }
                    case 8: {
                        string = "/bonus.mid";
                        break;
                    }
                    default: {
                        string = "/cleared.mid";
                    }
                }
                this.a(string, (int)this.bk, false);
            }
        }
        if (bl2) {
            this.am();
            bl = true;
        }
        return bl;
    }

    private final boolean al() {
        boolean bl = false;
        boolean bl2 = false;
        switch (this.dR) {
            case 0: 
            case 1: {
                bl2 = true;
                break;
            }
            case 2: 
            case 5: {
                this.a();
                this.b("/title.mid");
            }
            case 3: {
                this.c((byte)0, (byte)-1);
                bl = true;
            }
        }
        if (bl2) {
            this.am();
            bl = true;
        }
        return bl;
    }

    private final void am() {
        this.dN = false;
        if (this.l != 4 && this.l != 11) {
            this.d(true);
        }
        this.dP = null;
        this.dQ = null;
        if (!this.dL) {
            this.m();
        }
    }

    private final void a(Graphics graphics, boolean bl) {
        int n = 0;
        int n2 = this.dV;
        int n3 = this.ea;
        n3 -= this.e(this.j);
        int n4 = 25 - this.e(25);
        if (this.dV > 0) {
            this.b(graphics, this.cc, 0, 0, 13, 7, this.i - 13 >> 1, n3 - 7 - 2);
        }
        while (n < this.dU && n2 < this.dT) {
            int n5;
            if (bl) {
                this.a(graphics, this.i - this.eb >> 1, n3, this.eb, 22, n2 == this.dS ? 41658 : 22935, 10370);
            }
            this.a(this.dP[n2], graphics, this.i - (this.dP[n2].length() - 1) * 9 - 8 >> 1, n3 + 3 + 2, false);
            if (this.dR == 2) {
                n5 = -1;
                String string = "DEUTSCH=DE;ENGLISH=EN;FRAN\u00c7AIS=FR;ITALIANO=IT;ESPA\u00d1OL=SP;PORTUGU\u00caS=PG;".substring(this.dQ[n2], "DEUTSCH=DE;ENGLISH=EN;FRAN\u00c7AIS=FR;ITALIANO=IT;ESPA\u00d1OL=SP;PORTUGU\u00caS=PG;".indexOf(59, (int)this.dQ[n2]));
                if (string.compareTo("DE") == 0) {
                    n5 = 0;
                } else if (string.compareTo("EN") == 0) {
                    n5 = 1;
                } else if (string.compareTo("FR") == 0) {
                    n5 = 2;
                } else if (string.compareTo("IT") == 0) {
                    n5 = 3;
                } else if (string.compareTo("SP") == 0) {
                    n5 = 4;
                } else if (string.compareTo("PG") == 0) {
                    n5 = 5;
                }
                if (n5 != -1) {
                    this.b(graphics, this.ce, 50 + n5 * 20, 0, 20, 14, this.ec, n3 + 3 + 2 + -1);
                }
            } else if (this.dR == 3) {
                n5 = this.dQ[n2];
                boolean bl2 = this.p[n5 - 1];
                if (bl2) {
                    this.b(graphics, this.ce, 15, 0, 20, 14, this.ec, n3 + 3 + 2 + -1);
                } else {
                    this.b(graphics, this.ce, 0, 0, 15, 14, this.ec + 2, n3 + 3 + 2 + -1);
                }
                int n6 = -1;
                int n7 = -1;
                int n8 = -1;
                byte by = this.cr[n5 - 1];
                switch (by) {
                    case 1: {
                        n6 = 7;
                        n7 = 7;
                        n8 = 6;
                        break;
                    }
                    case 2: {
                        n6 = 7;
                        n7 = 15;
                        n8 = 6;
                        break;
                    }
                    case 3: {
                        n6 = 0;
                        n7 = 15;
                        n8 = 13;
                    }
                }
                if (n6 != -1) {
                    this.b(graphics, this.ce, 35, n6, n7, n7, this.ed + (20 - n7 >> 1), n3 + 3 + 2 + (12 - n8 >> 1));
                }
            }
            n3 += n4;
            ++n2;
            ++n;
        }
        if (this.dV + this.dU < this.dT) {
            this.b(graphics, this.cc, 0, 7, 13, 7, this.i - 13 >> 1, n3 + 2);
        }
        this.a(graphics, this.a[30], this.l != 11 ? this.a[29] : null, bl);
    }

    private final void d(Graphics graphics) {
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5 = this.ee.length();
        int n6 = 0;
        int n7 = 0;
        if (graphics != null) {
            n = this.i - this.ef >> 1;
            n2 = this.k - this.eg >> 1;
            this.a(graphics, n, n2, this.ef, this.eg, 22935, 10370);
            n2 += 6;
        }
        while (n3 < n5) {
            char c = this.ee.charAt(n3);
            if (c == '#' || n3 == n5 - 1) {
                int n8;
                if (c != '#') {
                    ++n3;
                }
                if ((n8 = n3 - n4) > n6) {
                    n6 = n8;
                }
                if (graphics != null) {
                    n = this.i - n8 * 9 >> 1;
                    for (int i = n4; i < n3; ++i) {
                        this.a(graphics, n, n2, this.ee.charAt(i));
                        n += 9;
                    }
                    n2 += 16;
                }
                n4 = n3 + 1;
                ++n7;
            }
            ++n3;
        }
        if (graphics == null) {
            this.ef = n6 * 9 + 16;
            this.eg = n7 * 16 + 10;
        }
    }

    private final void a(Graphics graphics, int n, int n2, int n3, int n4, int n5, int n6) {
        graphics.setClip(n, n2, n3, n4);
        graphics.setColor(n6);
        graphics.fillRect(n, n2 + 1, n3, n4 - 2);
        graphics.fillRect(n + 1, n2, n3 - 2, n4);
        graphics.setColor(n5);
        graphics.fillRect(n + 3, n2 + 2, n3 - 6, n4 - 4);
        graphics.drawLine(n + 2, n2 + 3, n + 2, n2 + n4 - 4);
        graphics.drawLine(n + n3 - 3, n2 + 3, n + n3 - 3, n2 + n4 - 4);
    }

    private final void a(Graphics graphics, String string, byte by, int n) {
        int n2 = string.length() * 9 + 16;
        int n3 = 26;
        int n4 = this.i - n2 >> 1;
        if (by != 3) {
            n = by == 0 ? this.k - n3 >> 1 : (by == 1 ? 34 : this.k - n3 - 5);
        }
        this.a(graphics, n4, n, n2, n3, 22935, 10370);
        this.a(string, graphics, this.i >> 1, n + 6, true);
    }

    private final int a(String string, Graphics graphics, int n, int n2, boolean bl) {
        int n3 = string.length();
        int n4 = (n3 - 1) * 9 + 8;
        if (bl) {
            n -= n4 >> 1;
        }
        int n5 = n;
        for (int i = 0; i < n3; ++i) {
            this.a(graphics, n, n2, string.charAt(i));
            n += 9;
        }
        return n5;
    }

    private final void a(Graphics graphics, int n, int n2, int n3, int n4) {
        int n5 = 10;
        int n6 = 1;
        n += 13 * (n4 - 1);
        for (int i = 0; i < n4; ++i) {
            graphics.setClip(n, n2, 12, 13);
            graphics.drawImage(this.cb, n - n3 % n5 / n6 * 12, n2, 20);
            n -= 13;
            n6 = n5;
            n5 *= 10;
        }
    }

    private final void a(Graphics graphics, int n, int n2, char c) {
        int n3;
        int n4 = -1;
        if (graphics != null) {
            graphics.setClip(n, n2, 8, 12);
        }
        if (c >= '0' && c <= '9') {
            n3 = c - 48;
        } else if (c >= 'A' && c <= 'Z') {
            n3 = 10 + c - 65;
        } else {
            switch (c) {
                case '.': {
                    n3 = 36;
                    break;
                }
                case ',': {
                    n3 = 37;
                    break;
                }
                case '-': {
                    n3 = 38;
                    break;
                }
                case ':': {
                    n3 = 39;
                    break;
                }
                case '!': {
                    n3 = 40;
                    break;
                }
                case '?': {
                    n3 = 41;
                    break;
                }
                case '*': {
                    n3 = 42;
                    break;
                }
                case '\'': {
                    n3 = 43;
                    break;
                }
                case '\u00a9': {
                    n3 = 44;
                    break;
                }
                case '@': {
                    n3 = 45;
                    break;
                }
                case '\u00c0': {
                    n3 = 46;
                    n4 = 0;
                    break;
                }
                case '\u00c8': {
                    n3 = 47;
                    n4 = 0;
                    break;
                }
                case '\u00cc': {
                    n3 = 48;
                    n4 = 0;
                    break;
                }
                case '\u00d2': {
                    n3 = 49;
                    n4 = 0;
                    break;
                }
                case '\u00d9': {
                    n3 = 50;
                    n4 = 0;
                    break;
                }
                case '\u00c2': {
                    n3 = 46;
                    n4 = 2;
                    break;
                }
                case '\u00ca': {
                    n3 = 47;
                    n4 = 2;
                    break;
                }
                case '\u00ce': {
                    n3 = 48;
                    n4 = 2;
                    break;
                }
                case '\u00d4': {
                    n3 = 49;
                    n4 = 2;
                    break;
                }
                case '\u00db': {
                    n3 = 50;
                    n4 = 2;
                    break;
                }
                case '\u00c1': {
                    n3 = 46;
                    n4 = 4;
                    break;
                }
                case '\u00c9': {
                    n3 = 47;
                    n4 = 4;
                    break;
                }
                case '\u00cd': {
                    n3 = 48;
                    n4 = 4;
                    break;
                }
                case '\u00d3': {
                    n3 = 49;
                    n4 = 4;
                    break;
                }
                case '\u00da': {
                    n3 = 50;
                    n4 = 4;
                    break;
                }
                case '\u00c4': {
                    n3 = 46;
                    n4 = 1;
                    break;
                }
                case '\u00cb': {
                    n3 = 47;
                    n4 = 1;
                    break;
                }
                case '\u00cf': {
                    n3 = 48;
                    n4 = 1;
                    break;
                }
                case '\u00d6': {
                    n3 = 49;
                    n4 = 1;
                    break;
                }
                case '\u00dc': {
                    n3 = 50;
                    n4 = 1;
                    break;
                }
                case '\u00c3': {
                    n3 = 46;
                    n4 = 3;
                    break;
                }
                case '\u00d1': {
                    n3 = 51;
                    n4 = 3;
                    break;
                }
                case '\u00d5': {
                    n3 = 49;
                    n4 = 3;
                    break;
                }
                case '\u00c7': {
                    n3 = 12;
                    n4 = 5;
                    break;
                }
                default: {
                    return;
                }
            }
        }
        if (graphics != null) {
            int n5 = n3 / 18;
            int n6 = n3 % 18;
            graphics.drawImage(this.ca, n - n6 * 8, n2 - n5 * 12, 20);
            if (n4 != -1) {
                graphics.setClip(n, n2 + (n4 != 5 ? -3 : 12), 8, 3);
                n5 = n4 / 2;
                n6 = n4 % 2;
                graphics.drawImage(this.ca, n - (128 + n6 * 8), n2 + (n4 != 5 ? -3 : 12) - (24 + n5 * 3), 20);
            }
        }
    }

    private final void a(String string, int n, byte by) {
        this.ep = this.dN;
        this.er = string;
        this.ek = 0;
        this.ei = n;
        this.ej = 0;
        this.em = this.i;
        this.eq = by;
        this.a(null, 0, 0, true, true, true, true);
        this.eh = this.l;
        this.l = 5;
        this.a(true, -1);
    }

    private final boolean an() {
        boolean bl = false;
        if (this.dH == 0) {
            if (this.B) {
                this.B = false;
                if (this.ek > 0) {
                    this.ek -= 2;
                    if (this.ek < 0) {
                        this.ek = 0;
                    }
                    bl = true;
                }
            } else if (this.C) {
                this.C = false;
                if (this.ek < this.el) {
                    this.ek += 2;
                    if (this.ek > this.el) {
                        this.ek = this.el;
                    }
                    bl = true;
                }
            } else if (this.G || this.H || this.F) {
                this.F = false;
                this.H = false;
                this.G = false;
                this.a(false, 0);
            }
        } else {
            --this.dG;
            if (this.dG < 0) {
                this.dG = 0;
                if (this.dH == 2) {
                    this.dH = 0;
                    switch (this.dI) {
                        case 0: {
                            this.ao();
                        }
                    }
                } else {
                    this.dH = 0;
                }
            }
            bl = true;
        }
        return bl;
    }

    private final void ao() {
        if (this.eq == -1) {
            this.l = this.eh;
            this.dN = this.ep;
            this.b(true, -1);
            this.a(true, -1);
        } else if (this.eq == 0) {
            this.ah();
        }
    }

    private final void a(Graphics graphics, int n, int n2, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        int n3 = 0;
        int n4 = 0;
        int n5 = this.k - this.ei - this.ej;
        if (graphics != null && bl) {
            if (bl2) {
                graphics.setClip(0, 0, this.i, this.j);
                graphics.setColor(0);
                graphics.fillRect(0, 0, this.i, this.j);
            }
            int n6 = this.em - this.e(this.em);
            int n7 = n5 - this.e(n5);
            this.a(graphics, this.i - n6 >> 1, this.ei + (n5 - n7 >> 1), n6, n7, n, n2);
        }
        int n8 = (this.em - 7) / 9;
        this.eo = (n5 - 17) / 16;
        int n9 = this.ek + this.eo;
        int n10 = this.er.length();
        int n11 = 0;
        this.en = 0;
        int n12 = 0;
        int n13 = -1;
        n4 = this.e(this.i);
        if (graphics != null) {
            n3 = 5 + this.ei + (n5 - (bl ? 17 : 7) - this.eo * 16 >> 1);
            if (this.el < 0 && bl4) {
                n3 -= this.el * 16 >> 1;
            }
        }
        while (n12 < n10) {
            char c = this.er.charAt(n12);
            ++n11;
            if (c == ' ' || c == '.' || c == ',' || c == '-' || c == ':' || c == ':' || c == '#') {
                n13 = n12;
            }
            if (n11 >= n8 || c == '#' || n12 >= n10 - 1) {
                if (n13 == -1 || n12 >= n10 - 1) {
                    n13 = n12;
                }
                if (graphics != null && this.en >= this.ek) {
                    if (this.dH == 0 || !bl) {
                        c = this.er.charAt(n13);
                        int n14 = c != ' ' && c != '#' ? this.i - (n13 - (n12 - n11 + 1) + 1) * 9 >> 1 : this.i - (n13 - (n12 - n11 + 1)) * 9 >> 1;
                        for (int i = n12 - n11 + 1; i <= n13; ++i) {
                            this.a(graphics, n14 - n4, n3, this.er.charAt(i));
                            n14 += 9;
                        }
                    }
                    n3 += 16;
                }
                n11 = n12 - n13;
                if (n13 + 1 < n10 && this.er.charAt(n13 + 1) == ' ') {
                    ++n12;
                }
                n13 = -1;
                ++this.en;
                if (graphics != null && this.en >= n9) break;
            }
            ++n12;
        }
        if (graphics != null) {
            if (bl3 && this.dH == 0) {
                if (this.ek > 0) {
                    this.b(graphics, this.cc, 0, 0, 13, 7, (this.i >> 1) - 13 + 1, this.ei + n5 - 7 - 4);
                }
                if (this.ek < this.el) {
                    this.b(graphics, this.cc, 0, 7, 13, 7, (this.i >> 1) - 1, this.ei + n5 - 7 - 4);
                }
            }
        } else {
            if (n11 > 0) {
                ++this.en;
            }
            this.el = this.en - this.eo;
        }
    }

    private final void b(boolean bl, int n) {
        this.bT = 0;
        this.bV = (byte)(bl ? 1 : 2);
        this.bU = n;
    }

    private final void ap() {
        this.w = (short)(this.w + this.bO);
        this.dX = 0;
        this.d(false);
        if (this.c == 1) {
            this.a("/cleared.mid", (int)this.bk, false);
        }
        this.b(false, -1);
        if (this.bL != 11 && this.bL != 12) {
            int n = 2 + this.a[48].length();
            int n2 = (int)this.bQ / 1000;
            int n3 = n2 / 60;
            int n4 = n2 - n3 * 60;
            StringBuffer stringBuffer = new StringBuffer(100);
            stringBuffer.append(this.a[44]);
            stringBuffer.append("##");
            stringBuffer.append(this.a[45]);
            int n5 = stringBuffer.length();
            if (n3 <= 9) {
                stringBuffer.append('0');
            }
            stringBuffer.append(n3);
            stringBuffer.append(':');
            if (n4 <= 9) {
                stringBuffer.append('0');
            }
            stringBuffer.append(n4);
            this.a(stringBuffer, "", n - (stringBuffer.length() - n5), ' ', false);
            stringBuffer.append("#");
            stringBuffer.append(this.a[46]);
            n5 = stringBuffer.length();
            stringBuffer.append(this.bO);
            stringBuffer.append(this.a[48]);
            stringBuffer.append(this.bP);
            this.a(stringBuffer, "", n - (stringBuffer.length() - n5), ' ', false);
            stringBuffer.append("#");
            stringBuffer.append(this.a[47]);
            this.a(stringBuffer, String.valueOf(this.w), n, ' ', false);
            this.ee = stringBuffer.toString();
            this.d(null);
        } else {
            this.ee = null;
        }
        this.l = 7;
    }

    private final void a(StringBuffer stringBuffer, String string, int n, char c, boolean bl) {
        int n2 = string.length();
        if (!bl) {
            stringBuffer.append(string);
        }
        for (int i = n2; i < n; ++i) {
            stringBuffer.append(c);
        }
        if (bl) {
            stringBuffer.append(string);
        }
    }

    private final boolean aq() {
        if (this.bV <= 0) {
            if (this.ee != null && (this.G || this.F)) {
                this.F = false;
                this.H = false;
                this.G = false;
                this.f(true);
                return true;
            }
            if (this.ee == null) {
                this.f(true);
                return true;
            }
            return false;
        }
        return true;
    }

    private final void f(boolean bl) {
        this.bN = this.bM;
        int n = this.bL + 1;
        int n2 = this.bM;
        boolean bl2 = true;
        boolean bl3 = false;
        switch (this.bL) {
            case 3: {
                if (this.i(this.bM, 11)) break;
                n = 11;
                break;
            }
            case 6: {
                if (this.i(this.bM, 12)) break;
                n = 12;
                break;
            }
            case 11: {
                n = 4;
                break;
            }
            case 12: {
                n = 7;
                break;
            }
            case 10: {
                this.p[this.bM - 1] = true;
                bl3 = true;
                if (!this.i(this.bM, 10)) {
                    n2 = 0;
                    n = 4;
                    break;
                }
                bl2 = false;
                break;
            }
        }
        this.o[this.bM - 1] = (byte)(!bl3 ? n : 0);
        this.h();
        if (bl2) {
            this.bM = n2;
            this.bL = n;
            if (bl) {
                this.j();
            }
            this.ab();
            this.l = 1;
            this.d(true);
        } else {
            this.ah();
        }
    }

    private final int h(int n, int n2) {
        int n3 = (n - 1) * 3;
        if (n2 == 12) {
            ++n3;
        } else if (n2 == 10) {
            n3 += 2;
        } else if (n2 != 11) {
            return -1;
        }
        return n3;
    }

    private final boolean i(int n, int n2) {
        int n3 = this.h(n, n2);
        if (n3 == -1) {
            return true;
        }
        return (this.y & 1L << n3) != 0L;
    }

    private final void a(boolean bl, int n, int n2) {
        int n3 = this.h(n, n2);
        if (n3 == -1) {
            return;
        }
        this.y = bl ? (this.y |= 1L << n3) : (this.y &= 1L << n3 ^ 0xFFFFFFFFFFFFFFFFL);
    }

    private final void ar() {
        this.a(this.a[this.et[this.es]], 44, (byte)-1);
        this.l = 8;
    }

    private final boolean as() {
        boolean bl = false;
        if (this.E) {
            this.E = false;
            this.es = (byte)(this.es + 1);
            if (this.es >= this.et.length) {
                this.es = 0;
            }
            this.ek = 0;
            this.er = this.a[this.et[this.es]];
            this.em = this.i;
            this.a(null, 0, 0, true, true, true, true);
            bl = true;
        } else if (this.D) {
            this.D = false;
            this.es = (byte)(this.es - 1);
            if (this.es < 0) {
                this.es = (byte)(this.et.length - 1);
            }
            this.ek = 0;
            this.er = this.a[this.et[this.es]];
            this.em = this.i;
            this.a(null, 0, 0, true, true, true, true);
            bl = true;
        }
        return bl |= this.an();
    }

    private final void e(Graphics graphics) {
        this.a(graphics, 22935, 10370, true, true, true, true);
        int n = 42;
        int n2 = this.i - this.e(this.i);
        int n3 = n - this.e(n);
        this.a(graphics, this.i - n2 >> 1, n - n3 >> 1, n2, n3, 22935, 10370);
        if (this.dH == 0) {
            if (this.eu[this.es].length == 0) {
                String string = this.es == 0 ? this.a[49] : this.a[50];
                this.a(string, graphics, this.i >> 1, 14, true);
            } else {
                int n4 = this.eu[this.es].length;
                int n5 = this.i - n4 * 40 + 8 >> 1;
                int n6 = n - 32 >> 1;
                for (int i = 0; i < n4; ++i) {
                    byte by = this.eu[this.es][i];
                    this.a(graphics, true, by, n5, n6);
                    n5 += 32 + (by != -24 && by != -40 && by != -39 ? 8 : 0);
                }
            }
            this.b(graphics, this.cc, 13, 0, 7, 13, 5, n - 13 >> 1);
            this.b(graphics, this.cc, 20, 0, 7, 13, this.i - 7 - 5, n - 13 >> 1);
        }
        this.a(graphics, null, this.a[29], true);
    }

    private final void a(Graphics graphics, String string, String string2, boolean bl) {
        int n = !this.dK ? this.e(18) : 0;
        if (bl) {
            graphics.setClip(0, 0, this.i, this.j);
            graphics.setColor(22935);
            graphics.fillRect(0, this.k + 2 + n, this.i, 18);
        }
        if (string != null) {
            this.a(string, graphics, 3, this.k + 2 + 3 + n, false);
        }
        if (string2 != null) {
            this.a(string2, graphics, this.i - ((string2.length() - 1) * 9 + 8) - 3, this.k + 2 + 3 + n, false);
        }
    }

    private final void d(byte by) {
        this.ev = by;
        this.ek = 0;
        this.ei = 0;
        this.ej = 0;
        this.em = this.i;
        this.dN = false;
        switch (this.ev) {
            case 0: {
                this.er = this.a[55];
                break;
            }
            case 1: {
                this.er = this.a[56];
                break;
            }
            case 2: {
                this.er = this.a[57];
            }
        }
        this.l = 9;
        this.a(null, 0, 0, true, true, true, true);
        this.a(true, -1);
    }

    private final boolean at() {
        if (this.dH == 0) {
            if (this.G) {
                this.G = false;
                this.a(false, 0);
                return true;
            }
            if (this.H) {
                this.H = false;
                this.a(false, 1);
                return true;
            }
            if (this.B) {
                this.B = false;
                if (this.ek > 0) {
                    this.ek -= 2;
                    if (this.ek < 0) {
                        this.ek = 0;
                    }
                    return true;
                }
            } else if (this.C) {
                this.C = false;
                if (this.ek < this.el) {
                    this.ek += 2;
                    if (this.ek > this.el) {
                        this.ek = this.el;
                    }
                    return true;
                }
            }
            return false;
        }
        --this.dG;
        if (this.dG < 0) {
            this.dG = 0;
            if (this.dH == 2) {
                this.dH = 0;
                switch (this.dI) {
                    case 0: {
                        this.g(true);
                        break;
                    }
                    case 1: {
                        this.g(false);
                    }
                }
            } else {
                this.dH = 0;
            }
        }
        return true;
    }

    private final void g(boolean bl) {
        switch (this.ev) {
            case 0: {
                if (bl) {
                    this.ah();
                    break;
                }
                this.l = 1;
                this.dN = true;
                this.b(true, -1);
                this.a(true, -1);
                break;
            }
            case 1: {
                if (bl) {
                    this.l = 3;
                    this.dN = false;
                    break;
                }
                this.l = 4;
                this.dN = true;
                this.b(true, -1);
                this.a(true, -1);
                break;
            }
            case 2: {
                this.c = (byte)(bl ? 1 : 0);
                this.ah();
            }
        }
    }
}

