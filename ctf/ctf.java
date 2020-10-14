import edu.hws.jcm.awt.JCMPanel;
import edu.hws.jcm.awt.VariableInput;
import edu.hws.jcm.awt.VariableSlider;
import edu.hws.jcm.functions.ExpressionFunction;
import java.awt.Container;
import java.awt.Label;

public class ctf extends java.applet.Applet
{
  private edu.hws.jcm.draw.DisplayCanvas canvas;
  private JCMPanel main;
  private edu.hws.jcm.awt.ExpressionInput input;
  private edu.hws.jcm.draw.DrawString info;
  private edu.hws.jcm.draw.Crosshair crosshair;
  private edu.hws.jcm.data.Function func;
  private VariableInput vInput;
  private VariableInput csInput;
  private VariableInput ccInput;
  private VariableInput deInput;
  private VariableInput diInput;
  private VariableInput dfInput;
  private VariableInput drInput;
    
  public void setYLimit(double paramDouble1, double paramDouble2)
  {
    edu.hws.jcm.draw.CoordinateRect localCoordinateRect = this.canvas.getCoordinateRect();
    localCoordinateRect.setLimits(0.0D, 0.2D, paramDouble1, paramDouble2);
    localCoordinateRect.setRestoreBuffer();
  }
  
  public void setFunction(String paramString, boolean paramBoolean) {
    this.input.setText(paramString);
    if (paramBoolean) this.input.getOnUserAction().compute();
  }
  
  public String getFunctionString() {
    return this.input.getText();
  }
  
  public void setInfoVisible(boolean paramBoolean) {
    this.info.setVisible(paramBoolean);
    this.crosshair.setVisible(paramBoolean);
    this.canvas.doRedraw();
  }
  
  public double getY(double paramDouble) {
    return this.func.getVal(new double[] { paramDouble });
  }
  
  public String getScopePara() {
    String str = this.vInput.getText() + " " + this.csInput.getText() + " " + this.ccInput.getText() + 
      " " + this.deInput.getText() + " " + this.diInput.getText() + " " + 
      this.dfInput.getText() + " " + this.drInput.getText();
    return str;
  }
  
  public void setScopePara(String paramString) {
    java.util.StringTokenizer localStringTokenizer = new java.util.StringTokenizer(paramString);
    String[] arrayOfString = new String[7];
    int i = 0;
    while (localStringTokenizer.hasMoreTokens()) {
      arrayOfString[i] = localStringTokenizer.nextToken();
      i++;
    }
    this.vInput.setText(arrayOfString[0]);
    this.csInput.setText(arrayOfString[1]);
    this.ccInput.setText(arrayOfString[2]);
    this.deInput.setText(arrayOfString[3]);
    this.diInput.setText(arrayOfString[4]);
    this.dfInput.setText(arrayOfString[5]);
    this.drInput.setText(arrayOfString[6]);
    this.drInput.getOnUserAction().compute();
  }
  
  public void init() {
    this.main = new JCMPanel();
    this.main.setInsetGap(3);
    
    this.canvas = new edu.hws.jcm.draw.DisplayCanvas();
    this.canvas.setHandleMouseZooms(true);
    edu.hws.jcm.draw.CoordinateRect localCoordinateRect = this.canvas.getCoordinateRect();
    localCoordinateRect.setLimits(0.0D, 0.2D, -1.0D, 1.0D);
    localCoordinateRect.setRestoreBuffer();
    
    edu.hws.jcm.data.Parser localParser = new edu.hws.jcm.data.Parser();
    edu.hws.jcm.data.Variable localVariable = new edu.hws.jcm.data.Variable("s");
    localParser.add(localVariable);
    
    this.vInput = new VariableInput("v", "300", localParser);this.vInput.setMin(0.0D);
    this.csInput = new VariableInput("Cs", "1.6", localParser);this.csInput.setMin(0.0D);
    this.ccInput = new VariableInput("Cc", "2.2", localParser);this.ccInput.setMin(0.0D);
    this.deInput = new VariableInput("dE", "0.9", localParser);this.deInput.setMin(0.0D);
    this.diInput = new VariableInput("dI", "10", localParser);this.diInput.setMin(0.0D);
    this.dfInput = new VariableInput("dF", "10", localParser);this.dfInput.setMin(0.0D);
    this.drInput = new VariableInput("dR", "1", localParser);this.drInput.setMin(0.0D);
    
    VariableSlider localVariableSlider1 = new VariableSlider("dZ", new edu.hws.jcm.data.Constant(0.0D), new edu.hws.jcm.data.Constant(100000.0D), localParser);
    VariableSlider localVariableSlider2 = new VariableSlider("B", new edu.hws.jcm.data.Constant(0.0D), new edu.hws.jcm.data.Constant(1000.0D), localParser);
    VariableSlider localVariableSlider3 = new VariableSlider("Q", new edu.hws.jcm.data.Constant(0.0D), new edu.hws.jcm.data.Constant(1.0D), localParser);
    VariableSlider localVariableSlider4 = new VariableSlider("a", new edu.hws.jcm.data.Constant(0.0D), new edu.hws.jcm.data.Constant(1.0D), localParser);
    VariableSlider localVariableSlider5 = new VariableSlider(localCoordinateRect.getValueObject(0), 
      localCoordinateRect.getValueObject(1));
    
    localVariableSlider2.setBackground(java.awt.Color.lightGray);
    localVariableSlider4.setBackground(java.awt.Color.lightGray);
    localVariableSlider3.setBackground(java.awt.Color.lightGray);
    localVariableSlider1.setBackground(java.awt.Color.lightGray);
    localVariableSlider5.setBackground(java.awt.Color.lightGray);
    
    localVariableSlider2.setVal(100.0D);
    localVariableSlider4.setVal(0.1D);
    localVariableSlider3.setVal(0.0D);
    localVariableSlider1.setVal(20000.0D);
    localVariableSlider5.setVal(0.1D);
    
    ExpressionFunction localExpressionFunction1 = new ExpressionFunction("sinc", new String[] { "s" }, "abs(s)<=1e-5?1:sin(s)/s", localParser);
    ExpressionFunction localExpressionFunction2 = new ExpressionFunction("j0", new String[] { "s" }, 
      "(57568490574.0+s*s*(-13362590354.0+s*s*(651619640.7+s*s*(-11214424.18+s*s*(77392.33017+s*s*(-184.9052456))))))/(57568490411.0+s*s*(1029532985.0+s*s*(9494680.718+s*s*(59272.64853+s*s*(267.8532712+s*s*1.0)))))", localParser);
    
    ExpressionFunction localExpressionFunction3 = new ExpressionFunction("lambda", new String[] { "v" }, "12.2639/sqrt(v*1000.0+.97845*v*v)", localParser);
    ExpressionFunction localExpressionFunction4 = new ExpressionFunction("gamma", new String[] { "s", "v", "Cs", "dZ" }, "2*pi*(2.5e6*Cs*lambda(v)^3*s^4-0.5000*dZ*lambda(v)*s^2)", localParser);
    ExpressionFunction localExpressionFunction5 = new ExpressionFunction("ctf", new String[] { "s", "v", "Cs", "dZ", "Q" }, "sqrt(1-Q*Q)*sin(gamma(s,v,Cs,dZ))-Q*cos(gamma(s,v,Cs,dZ))", localParser);
    ExpressionFunction localExpressionFunction6 = new ExpressionFunction("Gsc", new String[] { "s", "v", "Cs", "dZ", "a" }, "exp(-(pi*a*(1.0e7*Cs*lambda(v)^2*s^3-dZ*s))^2/1e6)", localParser);
    ExpressionFunction localExpressionFunction7 = new ExpressionFunction("Gtc", new String[] { "s", "v", "Cc", "dE" }, "exp(-1.0e8*(pi*Cc*lambda(v)*s^2*dE/v)^2/(16*ln(2)))", localParser);
    ExpressionFunction localExpressionFunction8 = new ExpressionFunction("Gol", new String[] { "s", "v", "Cc", "dI" }, "exp(-1.0e2*(pi*Cc*lambda(v)*s^2*dI)^2/(4*ln(2)))", localParser);
    ExpressionFunction localExpressionFunction9 = new ExpressionFunction("Glm", new String[] { "s", "v", "dF" }, "j0(pi*dF*lambda(v)*s*s)", localParser);
    ExpressionFunction localExpressionFunction10 = new ExpressionFunction("Gtm", new String[] { "s", "dR" }, "sinc(pi*s*dR)", localParser);
    ExpressionFunction localExpressionFunction11 = new ExpressionFunction("Gau", new String[] { "s", "B" }, "exp(-B*s*s)", localParser);
    
    this.input = new edu.hws.jcm.awt.ExpressionInput("ctf(s,v,Cs,dZ,Q)", localParser);
    this.func = this.input.getFunction(localVariable);
    edu.hws.jcm.draw.Graph1D localGraph1D = new edu.hws.jcm.draw.Graph1D(this.func);
    
    edu.hws.jcm.awt.ComputeButton localComputeButton = new edu.hws.jcm.awt.ComputeButton("Plot");
    
    edu.hws.jcm.draw.LimitControlPanel localLimitControlPanel = new edu.hws.jcm.draw.LimitControlPanel(33, false);
    localLimitControlPanel.addCoords(this.canvas);
    
    edu.hws.jcm.data.Constant localConstant = new edu.hws.jcm.data.Constant(1.0D);
    this.info = new edu.hws.jcm.draw.DrawString("1/s = # A\nf(s) = #", 0, 
      new edu.hws.jcm.data.Value[] { new edu.hws.jcm.data.ValueMath(localConstant, localVariableSlider5, '/'), new edu.hws.jcm.data.ValueMath(this.func, localVariableSlider5) });
    this.info.setFont(new java.awt.Font("SansSerif", 1, 12));
    this.info.setColor(new java.awt.Color(0, 100, 0));
    this.info.setOffset(10);
    this.info.setNumSize(6);
    
    this.canvas.add(new edu.hws.jcm.draw.Axes());
    this.canvas.add(localGraph1D);
    this.canvas.add(new edu.hws.jcm.draw.DrawBorder(java.awt.Color.black, 1));
    this.canvas.add(this.crosshair = new edu.hws.jcm.draw.Crosshair(localVariableSlider5, this.func));
    this.canvas.add(this.info);
    
    JCMPanel localJCMPanel1 = new JCMPanel(14, 1, 2);
    localJCMPanel1.add(new Label("Voltage(keV)"));
    localJCMPanel1.add(this.vInput);
    localJCMPanel1.add(new Label("Cs(mm)"));
    localJCMPanel1.add(this.csInput);
    localJCMPanel1.add(new Label("Cc(mm)"));
    localJCMPanel1.add(this.ccInput);
    localJCMPanel1.add(new Label("Energy spread(eV)"));
    localJCMPanel1.add(this.deInput);
    localJCMPanel1.add(new Label("Lens current spread(ppm)"));
    localJCMPanel1.add(this.diInput);
    localJCMPanel1.add(new Label("Vertical motion(Angstrom)"));
    localJCMPanel1.add(this.dfInput);
    localJCMPanel1.add(new Label("Drift(Angstrom)"));
    localJCMPanel1.add(this.drInput);
    
    this.main.add(localJCMPanel1, "West");
    this.main.add(this.canvas, "Center");
    this.main.add(localLimitControlPanel, "East");
    
    JCMPanel localJCMPanel2 = new JCMPanel();
    this.main.add(localJCMPanel2, "South");
    
    JCMPanel localJCMPanel3 = new JCMPanel();
    localJCMPanel2.add(localJCMPanel3, "North");
    
    JCMPanel localJCMPanel4 = new JCMPanel(5, 1);
    JCMPanel localJCMPanel5 = new JCMPanel(5, 1);
    JCMPanel localJCMPanel6 = new JCMPanel(5, 1);
    localJCMPanel3.add(localJCMPanel4, "West");
    localJCMPanel3.add(localJCMPanel5, "Center");
    localJCMPanel3.add(localJCMPanel6, "East");
    
    localJCMPanel4.add(new Label("dZ(angstrom)", 2));
    localJCMPanel4.add(new Label("B(angstrom^2)", 2));
    localJCMPanel4.add(new Label("Amp Contrast", 2));
    localJCMPanel4.add(new Label("Angle(mrad)", 2));
    localJCMPanel4.add(new Label("s(1/angstrom)", 2));
    
    localJCMPanel5.add(localVariableSlider1);
    localJCMPanel5.add(localVariableSlider2);
    localJCMPanel5.add(localVariableSlider3);
    localJCMPanel5.add(localVariableSlider4);
    localJCMPanel5.add(localVariableSlider5);
    
    VariableInput localVariableInput1 = new VariableInput();
    
    VariableInput localVariableInput2 = new VariableInput();
    localVariableInput2.setMin(0.0D);
    
    VariableInput localVariableInput3 = new VariableInput();
    localVariableInput3.setMin(0.0D);localVariableInput3.setMax(1.0D);
    
    VariableInput localVariableInput4 = new VariableInput();
    localVariableInput4.setMin(0.0D);
    
    VariableInput localVariableInput5 = new VariableInput();
    
    localJCMPanel6.add(localVariableInput1);
    localJCMPanel6.add(localVariableInput2);
    localJCMPanel6.add(localVariableInput3);
    localJCMPanel6.add(localVariableInput4);
    localJCMPanel6.add(localVariableInput5);
    
    localJCMPanel2.add(new Label("Enter a function f(s), which can use the varibles(s,v,a,dZ,B,Cs,Cc,Q,dE,dI,dF,dR):"), "Center");
    JCMPanel localJCMPanel7 = new JCMPanel();
    localJCMPanel2.add(localJCMPanel7, "South");
    localJCMPanel7.add(this.input, "Center");
    localJCMPanel7.add(localComputeButton, "East");
    
    edu.hws.jcm.awt.Controller localController = this.main.getController();
    localController.setErrorReporter(this.canvas);
    localLimitControlPanel.setErrorReporter(this.canvas);
    
    this.vInput.setOnUserAction(localController);
    this.csInput.setOnUserAction(localController);
    this.ccInput.setOnUserAction(localController);
    this.deInput.setOnUserAction(localController);
    this.diInput.setOnUserAction(localController);
    this.dfInput.setOnUserAction(localController);
    this.drInput.setOnUserAction(localController);
    
    this.input.setOnUserAction(localController);
    localComputeButton.setOnUserAction(localController);
    
    localVariableSlider3.setOnUserAction(localController);
    localVariableInput3.setOnUserAction(localController);
    localController.add(new edu.hws.jcm.awt.Tie(localVariableSlider3, localVariableInput3));
    
    localVariableSlider4.setOnUserAction(localController);
    localVariableInput4.setOnUserAction(localController);
    localController.add(new edu.hws.jcm.awt.Tie(localVariableSlider4, localVariableInput4));
    
    localVariableSlider2.setOnUserAction(localController);
    localVariableInput2.setOnUserAction(localController);
    localController.add(new edu.hws.jcm.awt.Tie(localVariableSlider2, localVariableInput2));
    
    localVariableSlider1.setOnUserAction(localController);
    localVariableInput1.setOnUserAction(localController);
    localController.add(new edu.hws.jcm.awt.Tie(localVariableSlider1, localVariableInput1));
    
    localVariableSlider5.setOnUserAction(localController);
    localVariableInput5.setOnUserAction(localController);
    localController.add(new edu.hws.jcm.awt.Tie(localVariableSlider5, localVariableInput5));
        
    setBackground(java.awt.Color.white);
    setLayout(new java.awt.BorderLayout());
    add(this.main, "Center");
  }
}