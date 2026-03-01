package lab2.testutil;

import lab2.mathfunctions.log.LnFunction;
import lab2.mathfunctions.log.LogBaseFunction;
import lab2.mathfunctions.trig.*;
import lab2.system.SystemFunction;

public final class SystemTestKit {
  private SystemTestKit() {}

  public static class Deps {
    public SinFunction sin;
    public CosFunction cos;
    public TanFunction tan;
    public SecFunction sec;
    public CscFunction csc;
    public CotFunction cot;

    public LnFunction ln;
    public LogBaseFunction log2;
    public LogBaseFunction log5;
    public LogBaseFunction log10;
  }

  public static Deps allStubDeps(int xScale) {
    Deps d = new Deps();

    d.sin = Stubs.sinStub(StubLoader.load("sin.csv", xScale));
    d.cos = Stubs.cosStub(StubLoader.load("cos.csv", xScale));
    d.tan = Stubs.tanStub(StubLoader.load("tan.csv", xScale));
    d.sec = Stubs.secStub(StubLoader.load("sec.csv", xScale));
    d.csc = Stubs.cscStub(StubLoader.load("csc.csv", xScale));
    d.cot = Stubs.cotStub(StubLoader.load("cot.csv", xScale));

    d.ln = Stubs.lnStub(StubLoader.load("ln.csv", xScale));
    d.log2 = Stubs.log2Stub(StubLoader.load("log2.csv", xScale));
    d.log5 = Stubs.log5Stub(StubLoader.load("log5.csv", xScale));
    d.log10 = Stubs.log10Stub(StubLoader.load("log10.csv", xScale));

    return d;
  }

  public static SystemFunction buildSystem(Deps d) {
    return new SystemFunction(
        d.sin, d.cos, d.tan, d.sec, d.csc, d.cot,
        d.ln, d.log2, d.log5, d.log10
    );
  }
}