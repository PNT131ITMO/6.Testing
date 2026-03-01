package lab2.testutil;

import lab2.mathfunctions.log.LnFunction;
import lab2.mathfunctions.log.LogBaseFunction;
import lab2.mathfunctions.trig.*;
import lab2.stub.CsvTableStubFunction;

import java.math.BigDecimal;

public final class Stubs {
  private Stubs() {}

  public static SinFunction sinStub(CsvTableStubFunction stub) { return new SinStub(stub); }
  public static CosFunction cosStub(CsvTableStubFunction stub) { return new CosStub(stub); }
  public static TanFunction tanStub(CsvTableStubFunction stub) { return new TanStub(stub); }
  public static SecFunction secStub(CsvTableStubFunction stub) { return new SecStub(stub); }
  public static CscFunction cscStub(CsvTableStubFunction stub) { return new CscStub(stub); }
  public static CotFunction cotStub(CsvTableStubFunction stub) { return new CotStub(stub); }

  public static LnFunction lnStub(CsvTableStubFunction stub) { return new LnStub(stub); }
  public static LogBaseFunction log2Stub(CsvTableStubFunction stub) { return new Log2Stub(stub); }
  public static LogBaseFunction log5Stub(CsvTableStubFunction stub) { return new Log5Stub(stub); }
  public static LogBaseFunction log10Stub(CsvTableStubFunction stub) { return new Log10Stub(stub); }

  private static abstract class BaseStub {
    final CsvTableStubFunction stub;
    BaseStub(CsvTableStubFunction stub) { this.stub = stub; }
    BigDecimal calc(BigDecimal x, BigDecimal eps) { return stub.calculate(x, eps); }
  }

  private static final class SinStub extends SinFunction {
    private final BaseStub b;
    SinStub(CsvTableStubFunction stub) { this.b = new BaseStub(stub) {}; }
    @Override public BigDecimal calculate(BigDecimal x, BigDecimal eps) { return b.calc(x, eps); }
  }

  private static final class CosStub extends CosFunction {
    private final BaseStub b;
    CosStub(CsvTableStubFunction stub) { this.b = new BaseStub(stub) {}; }
    @Override public BigDecimal calculate(BigDecimal x, BigDecimal eps) { return b.calc(x, eps); }
  }

  private static final class TanStub extends TanFunction {
    private final BaseStub b;
    TanStub(CsvTableStubFunction stub) { this.b = new BaseStub(stub) {}; }
    @Override public BigDecimal calculate(BigDecimal x, BigDecimal eps) { return b.calc(x, eps); }
  }

  private static final class SecStub extends SecFunction {
    private final BaseStub b;
    SecStub(CsvTableStubFunction stub) { this.b = new BaseStub(stub) {}; }
    @Override public BigDecimal calculate(BigDecimal x, BigDecimal eps) { return b.calc(x, eps); }
  }

  private static final class CscStub extends CscFunction {
    private final BaseStub b;
    CscStub(CsvTableStubFunction stub) { this.b = new BaseStub(stub) {}; }
    @Override public BigDecimal calculate(BigDecimal x, BigDecimal eps) { return b.calc(x, eps); }
  }

  private static final class CotStub extends CotFunction {
    private final BaseStub b;
    CotStub(CsvTableStubFunction stub) { this.b = new BaseStub(stub) {}; }
    @Override public BigDecimal calculate(BigDecimal x, BigDecimal eps) { return b.calc(x, eps); }
  }

  private static final class LnStub extends LnFunction {
    private final BaseStub b;
    LnStub(CsvTableStubFunction stub) { this.b = new BaseStub(stub) {}; }
    @Override public BigDecimal calculate(BigDecimal x, BigDecimal eps) { return b.calc(x, eps); }
  }

  private static final class Log2Stub extends LogBaseFunction {
    private final BaseStub b;
    Log2Stub(CsvTableStubFunction stub) { super(2); this.b = new BaseStub(stub) {}; }
    @Override public BigDecimal calculate(BigDecimal x, BigDecimal eps) { return b.calc(x, eps); }
  }

  private static final class Log5Stub extends LogBaseFunction {
    private final BaseStub b;
    Log5Stub(CsvTableStubFunction stub) { super(5); this.b = new BaseStub(stub) {}; }
    @Override public BigDecimal calculate(BigDecimal x, BigDecimal eps) { return b.calc(x, eps); }
  }

  private static final class Log10Stub extends LogBaseFunction {
    private final BaseStub b;
    Log10Stub(CsvTableStubFunction stub) { super(10); this.b = new BaseStub(stub) {}; }
    @Override public BigDecimal calculate(BigDecimal x, BigDecimal eps) { return b.calc(x, eps); }
  }
}