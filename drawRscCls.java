/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsc;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author master
 */
public class drawRscCls extends dxf12objects {

// start attributes
public double length = 0;
public double width = 0;
public double depth = 0;
public String topStyle = "0201 (OFOTB)";
public String btmStyle = "0201 (OFOTB)";
public String flute = "B";

public double topAddSz;
public double btmAddSz;    
public String joint;
public boolean make2piece = false;

public double slot = 6;
public String cutlay = "CUT";
public String crelay = "CREASE";
public int col = 1; // Line Colour
public String ltype = "CONTINUOUS"; // Line Type

public double blks1 = 0;
public double blkn2 = 0;
    
private double p1;
private double p2;
private double p3;
private double p4;
private double casedepth;
// private double flap = 0;  // Size of outer flap
// private double flapi = 0; // size of inner flap
private double flange = 30;
private double flngExt = 0;
private double flngAng = 7;

private double tFlpInr; // Top flap Inner
private double tFlpOtr; // Top flap Outer
private double bFlpInr; // Btm flap Inner
private double bFlpOtr; // Btm flap Outer

private double topfa; // top flap allowance
private double topfr;
private double btmfa; // btm flap allowance
private double btmfr;
public double dptAllow = 0;
private double daStd = 0;
private double daHsc = 0;
private double daFfsc = 7;
private double faFfsc = 0;
private double depthAllowance = 0;


private int hndl = 1000; 
  // end attributes
// END Atributes

// Methods
  // start methods
public String drwRsc ()
{
    this.allowanceSetup();  
    this.slot = this.slot / 2; // we only want half the slot size for drawing

    double rtnAry[] = {0,0,0};
    
    this.casedepth = depth + dptAllow;
    System.out.println(depth + "*" + dptAllow);
//  // Set EXTENTS
//  this.dxfxmax = this.flange + this.p1 + this.p2 + this.p3 + this.p4;
//  this.dxfymax = this.tFlpOtr + this.casedepth + this.bFlpOtr;
//  this.DimensionSizes(0,0, this.dxfxmax, this.dxfymax);

  this.dxf += this.dxf_header12();  
  this.dxf += this.caseBody();
// TOP FLAPS
  // Reset X,Y
  this.xabs = this.flange;
  this.yabs = (this.casedepth);
  this.Flap_020x(this.p1, this.tFlpOtr, 1, false);
  if(this.make2piece == false) {
    this.Flap_020x(this.p2, this.tFlpInr, 1, false);
    this.Flap_020x(this.p3, this.tFlpOtr, 1, false);
  }
  this.Flap_020x(this.p4, this.tFlpInr,1 ,true);
// BTM FLAPS
// Reset Abs X & Y
  this.xabs = this.flange;
  this.yabs = 0;

  this.Flap_020x(this.p1, this.bFlpOtr,-1, false);
  if(this.make2piece == false) {
    this.Flap_020x(this.p2, this.bFlpInr,-1, false);
    this.Flap_020x(this.p3, this.bFlpOtr,-1, false);
  }  
  this.Flap_020x(this.p4, this.bFlpInr,-1 ,true);
      
  this.dxf_footer12();  
    
  return this.dxf; // this.dxf; 
} // drw
// *********** END OF MAIN DRAWING ROUTINE *******************************************************************


public String caseBody()
{
  // Flange
  this.relMove((this.flange), (this.casedepth));
  this.Line((this.slot * -1), 0, this.cutlay);
  this.Line(((this.flange - this.slot) * -1), (this.flngAng * -1), this.cutlay);
  this.Line(0, ((this.casedepth - (this.flngAng * 2)) * -1), this.cutlay);
  this.Line((this.flange - this.slot), (this.flngAng * -1), this.cutlay);
  this.Line((this.slot), 0, this.cutlay);
// Flange Crease
  this.Line(0, this.casedepth, this.crelay);
  
  // Panels 
  this.relMove(this.p1, (this.casedepth * -1));
  this.Line(0, this.casedepth, this.crelay);
  
  if(this.make2piece == false) {
    this.relMove(this.p2, (this.casedepth * -1));
    this.Line(0, this.casedepth, this.crelay);

    this.relMove(this.p3, (this.casedepth * -1));
    this.Line(0, this.casedepth, this.crelay);
  }
  this.relMove(this.p4, (this.casedepth * -1));
  this.Line(0, this.casedepth, this.cutlay);

  return dxf;
}


public String Flap_020x(double flpwdth, double flphgt, double vrtOr, boolean bP4Slt)
{
  // Draws Left to Right from the slot as the 1st line
  // $flpwdth = Panel size (total)
  // $flphgt = Size of vertical flap height
  // $vrtOr = Vertical Orientation: 1 or -1
  // $bP4Slt = Is this panel 4? T/F
  // $rghtSlot = Right side Slot - different on P4
  // Global Values...
  // this.slot = 1/2 total slot size
  // this.cutlay = cut layer to use
  // this.crelay = Crease layer to use
  double rghtSlot = 0;
        
  if (bP4Slt == true) { // Yes this is a P4, no slot required right side
    rghtSlot = 0;
  } else {
    rghtSlot = this.slot;
  }
    
  this.Line(this.slot, 0, this.cutlay);
  this.Line(0, flphgt * vrtOr, this.cutlay);
  this.Line((flpwdth - (this.slot + rghtSlot )), 0, this.cutlay);
  this.Line(0, ((flphgt * -1) * vrtOr), this.cutlay);
  this.Line(rghtSlot, 0, this.cutlay);
  //  Do the crease
  this.relMove((flpwdth - this.slot) * -1, 0);
  this.Line((flpwdth - (this.slot + rghtSlot)), 0, this.crelay); // Line Colour & Line Type not yet implemented
  // Place Abs Coords at the Right-side end of the slot / flap
  this.relMove(rghtSlot, 0);
  
  return dxf;
} // Flap_020x


  protected boolean allowanceSetup() {
    // allowances 
    flange = 30; // std Flange size
    if (length < 250 && width < 250) {
        flange = 25;
    }
    if ((flute == "C") || (flute == "EB")) {
      p1 = length + 5; // length main
      p2 = width + 5; //
      p3 = length + 5;
      p4 = width + 2;
      //casedepth = depth + 9; //        
      topfa = 3;
      topfr = -1;
      btmfa = 3;        
      btmfr = -1;
      daStd = 4.5;
      daHsc = 0;
      daFfsc = 7;
      faFfsc = 0;
 
    } else if (flute == "B") {
      p1 = length + 3; // length main
      p2 = width + 3; //
      p3 = length + 3;
      p4 = width + 0;
      //casedepth = depth + 6; // 
      topfa = 1;
      topfr = 1;
      btmfa = 1;        
      btmfr = 1;
      daStd = 3;
      daHsc = 0;
      daFfsc = 4.5;
      faFfsc = 0;

    } else if (flute == "BC") {
      p1 = length + 7; // length main
      p2 = width + 7; //
      p3 = length + 7;
      p4 = width + 4;
      //casedepth = depth + 16; // 
      topfa = 5;
      topfr = -1;
      btmfa = 5;        
      btmfr = -1;
      daStd = 8;
      daHsc = 0;
      daFfsc = 12.5;
      faFfsc = 2;
      flange = 40; // Standard for heavy duty work
      
    } else {  // E Flute
      p1 = length + 2; // length main
      p2 = width + 2; //
      p3 = length + 2;
      p4 = width + 0;
      //casedepth = depth + 4; // 
      topfa = 1;
      topfr = -1;
      btmfa = 1;        
      btmfr = -1;
      daStd = 2;
      daHsc = 0;
      daFfsc = 3;
      faFfsc = 0;
    } // end of if-else
    
    dptAllow = 0;
    double rtnAry[] = {0,0,0};
    rtnAry = flapSizes(this.topStyle, this.topAddSz, this.topfa, this.topfr);
    tFlpOtr = rtnAry[0];
    tFlpInr = rtnAry[1];
    dptAllow = rtnAry[2];
    rtnAry = flapSizes(this.btmStyle, this.btmAddSz, this.btmfa, this.btmfr);
    bFlpOtr = rtnAry[0];
    bFlpInr = rtnAry[1];
    dptAllow += rtnAry[2];
    this.casedepth = depth + dptAllow;
    
    this.blks1 = this.tFlpInr + this.casedepth + this.bFlpInr;
    if(this.make2piece == false) {
        this.blkn2 = this.flange + this.p1 + this.p2 + this.p3 + this.p4;
    } else {
        this.blkn2 = this.flange + this.p1 + this.p4;        
    }
   // System.out.println(depth + "*" + dptAllow);
//Flute   FlangeSize  P1Allow   P2Allow   P3Allow   P4Allow   daStd   faStd   frStd   daHSC   daFFSC  faFFSC
// A  30  6   6   6   3   6   4   1   0   9   2   
// AAA 40   18  18  18  14  18  10  1   0   29  8   
// AAC 40   16  16  16  13  16  9   -1  0   24  6   
// AB   30  9   9   9   6   9   6   -1  0   13.5  3   
// CA   30  11  11  11  8   11  8   -1  0   14  4   
    return true; 
  }   
  

private double[] flapSizes(String flapstyle, double temp, double fa, double fr)
{
//   ******************************
//  Calc Flap Sizes 22/8/1997 - TG
//  This version 01/12/2012
//  ******************************
//  flapstyle = Text description used for selection. Comes from MenuIndexString and is translated to this text in the engine.
//  temp = Additional Size for flap used in 0202, 0209, etc
//  fa = flap allowance
//  fr = flap rounding figure
//  flap = variable used to pass back size of outer flap to draw
//  flapi = variable used to pass back size of inner flap to draw
    double rtnAry[] = {0, 0, 0}; // 0 = flap (Outer Flap ), 1 = flapi (Inner Size)
    double flap = 0;
    double flapi = 0;
    double rtnDptAllow = 0;
    
  //   *** Style Allowances ***}
  switch (flapstyle) {
    case "0200 (Raw Edge)": // No Top / Btm Flaps (FEFCO 0200)
    //  this.da = 0;
      flap = 0;
      flapi = flap;
      rtnDptAllow += this.daHsc;
      break;

    case "0202 (OFO)": // Outer Flap Overlaps (FEFCO 0202)
      temp = Double.parseDouble(JOptionPane.showInputDialog("Flap Overlap Size:"));
      flap = ((this.width + temp) / 2) + fa;
      if (((this.width + temp) / 2) != ((this.width + temp) / 2)) {
        flap = ((this.width + temp + fr) / 2) + fa;
      }
      flapi = flap;
      rtnDptAllow += this.daStd;
      break;

    case "0203 (OFFO)": // Outer Flap Fully Overlaps (FEFCO 0203)
      flap = (this.width + fa);
      flapi = flap;
      rtnDptAllow += this.daFfsc;
      break;

    case "0204 (AFM)": // All Flaps Meet (FEFCO 0204)
      flap = (this.width / 2) + fa;
      if ((this.width / 2) != ((int) (this.width / 2)) ) {
        flap = ((this.width + fr) / 2) + fa;
      }
      flapi = (this.length / 2) + fa;
      if ((this.length / 2) != ((int) (this.length / 2)) ) {
        flapi = ((this.length + fr) / 2) + fa;
      }
      rtnDptAllow += this.daStd;
      break;

    case "0204+)": // 0204+(IFM OFO) / PkgStyle 2404xx - CSOLSC
      //      (setq msg1 "Please Input Overlap size")
      //      (setq msg2 "CSOLSC / 0204+ Inr Meet, Otr OL")
      //      (INOUT msg1 msg2)
      flap = ((this.width + temp) / 2) + fa;
      if (((this.width + temp) / 2) != ((int) ((this.width + temp) / 2)) ) {
        flap = (((this.width + temp) + fr) / 2) + fa;
      }
      flapi = (this.length / 2) + fa;
      if ((this.length / 2) != ((int) (this.length / 2)) ) {
        flapi = ((this.length + fr) / 2) + fa;
      }
      rtnDptAllow += this.daStd;
      break;


    case  "CSFFSC (FEFCO 0203+)": // 0203+(OFFO L&W) / PkgStyle 2406xx - CSFFSC
      flap = this.width  + fa;
      flapi = this.length + fa;
      rtnDptAllow += this.faFfsc;
      break;

    case "0205 (IFM)":  // Inner Flaps Meet (FEFCO 0205)
      flapi = (this.length / 2) + fa;
      if ((this.length / 2) != ((int) (this.length / 2)) ) {
        flapi = ((this.length + fr) / 2) + fa;
      }
      flap = flapi;
      if (flap > (this.width + fa)) { // Stop Otr flap overlapping the width
        flap = (this.width + fa);
      } //if
      rtnDptAllow += this.daStd;
      break;

    case "0206 (IFM/OFFO)": // Inr Meet, Otr Fully Overlap (FEFCO 0206)
      flapi = (this.length / 2) + fa;
      if ((this.length / 2) != ((int) (this.length / 2)) ) {
        flapi = ((this.length + fr) / 2) + fa;
      }
      flap = this.width + fa;
      rtnDptAllow += this.daFfsc;
      break;


    case "0209 (Short Flap)": // 0209-01 Short Flap / PkgStyle 2407xx - Flange Top
      temp = Double.parseDouble(JOptionPane.showInputDialog("Flap Size:"));   
      //      (setq msg1 "Please Input flap size")
      //      (setq msg2 "HSCFL / 0209 Flange")
      //      (INOUT msg1 msg2)
      flap = temp + fa;
      flapi = flap;
      rtnDptAllow += this.daStd;
      break;
    // end cond = HSCFL

    case "0209g (OFG)": // 0209-02 Outer Flaps Gap / PkgStyle 2408xx - Shy Flaps Top
      //      (setq msg1 "Please Input gap size")
      //      (setq msg2 "RSC_SHY / 0209 Outer Flaps Gap")
      //      (INOUT msg1 msg2)
      temp = Double.parseDouble(JOptionPane.showInputDialog("Flap Gap Size:"));  
      flap = ((this.width - temp) / 2) + fa;
      if (((this.width - temp) / 2) != ((this.width - temp) / 2)) {
        flap = ((this.width - temp + fr) / 2) + fa;
      }
      flapi = flap;
      rtnDptAllow += this.daStd;
      break;

    default: // ELSE = RSC / 0201 (OFOTB) / PkgStyle 2401xx
      flap = (this.width / 2) + fa;
      if ((this.width / 2) != ((int) (this.width / 2)) ) {
        flap = ((this.width + fr) / 2) + fa;
      }
      flapi = flap;
      rtnDptAllow += this.daStd;
      break;

  } // switch
    
        System.out.println("flapSizes: " + rtnDptAllow);
        rtnAry[0] = flap;
        rtnAry[1] = flapi;
        rtnAry[2] = rtnDptAllow;
        return rtnAry; // 
} // flapSizes
  // end methods


} // drawRscCls
