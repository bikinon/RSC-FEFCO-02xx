package rsc;

/**
  *
  * Description
  *
  * @version 1.0 from 7/30/2016
  * @author Tim Gathercole
  */

public class dxf12objects {
  
  // start attributes
  public double xabs = 0;
  public double yabs = 0;
  public int hndl = 1000;
  // 1,1 = Right hand, pointing up Flap
  public int Xaxis = 1; // Direction of X axis +1 or -1
  public int Yaxis = 1; // Direction of Y axis +1 or -1
  public int CW = -1;
  public int CCW = 1; 
   
  public String dxf = "";
  public String dxfxmax = "10";
  public String dxfymax = "10";
  public String dimasz = "5.5";
  public String dimtxt = "7.1";
  public String dimexe = "2.2";
  public String lunits = "2";
  public String luprec = "4";
  public String dimdec = "9";
  public String dimzin = "8";
  public String dimexo = "0.1";
  public String dimgap = "0.1";
  // end attributes
  
  // start methods
  protected String dxf_header12() {
    // DXF Version 12 Header - Simplified version
    String cr = "\r\n";
    
    dxf +="  0"+ cr;
    dxf +="SECTION"+ cr;
    dxf +="  2"+ cr;
    dxf +="HEADER"+ cr;
    dxf +="  9"+ cr;
    dxf +="$ACADVER"+ cr;
    dxf +="  1"+ cr;
    dxf +="AC1009"+ cr; // DXF Version 12
    dxf +="  9"+ cr;
    dxf +="$EXTMIN"+ cr; // X, Y, and Z drawing extents 10, 20, 30 lower-left corner (in WCS)
    dxf +=" 10"+ cr;
    dxf +="0"+ cr;
    dxf +=" 20"+ cr;
    dxf +="0"+ cr;
    dxf +="  9"+ cr;
    dxf +="$EXTMAX"+ cr; // X, Y, and Z drawing extents 10, 20, 30 upper-right corner (in WCS)
    dxf +=" 10"+ cr;
    dxf += dxfxmax + cr;
    dxf +=" 20"+ cr;
    dxf += dxfymax + cr;
    dxf +="  0"+ cr;
    dxf +="ENDSEC"+ cr;
    dxf +="  0"+ cr;
    dxf +="SECTION"+ cr;
    dxf +="  2"+ cr;
    dxf +="TABLES"+ cr;
    dxf +="  0"+ cr;
    dxf +="TABLE"+ cr;
    dxf +="  2"+ cr;
    dxf +="LAYER"+ cr;
    dxf +=" 70"+ cr;
    dxf +=" 1"+ cr;
    dxf +="  0"+ cr;
    dxf +="LAYER"+ cr;
    dxf +="  2"+ cr;
    dxf +="Design"+ cr;
    dxf +=" 70"+ cr;
    dxf +="64"+ cr;
    dxf +=" 62"+ cr;
    dxf +="7"+ cr;
    dxf +="  6"+ cr;
    dxf +="CONTINUOUS"+ cr;
    dxf +="  0"+ cr;
    dxf +="ENDTAB"+ cr;
    dxf +="  0"+ cr;
    dxf +="ENDSEC"+ cr;
    dxf +="  0"+ cr;
    dxf +="SECTION"+ cr;
    dxf +="  2"+ cr;
    dxf +="BLOCKS"+ cr;
    dxf +="  0"+ cr;
    dxf +="ENDSEC"+ cr;
    dxf +="  0"+ cr;
    dxf +="SECTION"+ cr;
    dxf +="  2"+ cr;
    dxf +="ENTITIES"+ cr;
    
    return dxf;
  }
  
  
  protected String dxf_footer12() {
    /* WONT PRINT */
    //  Final part of the DXF File
    //  DXF version 12
    String cr = "\r\n"; // Chr(10).Chr(13);
    
    dxf +="  0" + cr;
    dxf +="ENDSEC" + cr;
    dxf +="  0" + cr;
    dxf +="EOF" + cr;
    
    return dxf;
  }
  
  protected boolean Line(double xval, double yval, String layer) {
    // DXF version 12
    // xval    = Incremental X movement
    // yval    = Incremental Y movement
    // layer   = Layer to use
    // eColor    = Colour to use
    // eLinetype = Line type to use
    //echo xval.'*'.yval.'*'.layer.'*'.eColor.'*'.eLinetype.'<br>';
    String cr = "\r\n";
    
    dxf +="  0" + cr;
    dxf +="LINE" + cr;
    dxf +="  5" + cr; //  Drawing Datadbase Handle
    dxf +=hndl + cr;
    dxf +="  8" + cr; //  Layer name
    dxf += "Design" + cr; // layer + cr;
    
    dxf +=" 6" + cr; // Line Type
    dxf +="CONTINUOUS" + cr;
    dxf +=" 62" + cr; // Line Colour
    dxf += getLineCol(layer) + cr;
    
    dxf +=" 10" + cr; //  Start X
    dxf += xabs + cr;
    dxf +=" 20" + cr; //  Start Y
    dxf += yabs + cr;
    dxf +=" 30" + cr; //  Start Z
    dxf +="0.0" + cr;
    dxf +=" 11" + cr; //  End X
    dxf += String.valueOf(xabs + xval * Xaxis) + cr;
    dxf +=" 21" + cr; //  End Y
    dxf += String.valueOf(yabs + yval * Yaxis) + cr;
    dxf +=" 31" + cr; //  End Z
    dxf +="0.0" + cr;
    //  Finish
    xabs = xabs + xval * Xaxis;
    yabs = yabs + yval * Yaxis;
    hndl = hndl + 1;
    return false;
  }
  
  protected String getLineCol(String layer) {
    // Return a colour for a layer from its Layer Name
    /* 
    See ArtiosCAD: Defaults > Import Tuning Table > Artios DXF - Metric
    0 = Glue assist  (DARK GREEN)
    1 = Cut (BLACK)
    2 = Crease (RED)
    3 = CUT CREASE 10x10 //Annotation   (LIGHT GREEN)
    4 = Partial Cut (BLACK)
    5 = PERF 6x6 //Annotation   (LIGHT GREEN)
    6 = SAFETY EDGE //Annotation   (LIGHT GREEN)
    7 = Cut (BLACK)
    8 = Reverse Crease (RED WITH MARK)
    9 = Reverse Partial Cut (DARk RED? WITH MARK)
    10 = Print Registration (DARK GREEN?)
    11 = Outside Bleed (LIGHT GREEN)
    12 = Annotation   (LIGHT GREEN)
    13 = Annotation   (LIGHT GREEN)
    14 = Annotation   (LIGHT GREEN)
    20 = Annotation   (LIGHT GREEN)
    */
    String col = "1";
    switch (layer) {
      case "CUT" : 
      col = "1";  // Artios understands Colour 7 as CUT   
      break;
      case "CREASE" : 
      col = "2";  // green = 7
      break;
      case "CUTCRE" : 
      col = "20";   // magenta = 6
      break;  
      case "PERF" : 
      col = "20";
      break; 
      case "SAFETY" : 
      col = "20";
      break;           
      default: 
      col = "1";
    } // end of switch
    return col;  
  }
  
  protected boolean circle(double radius, String layer, int eColor) {
    /*
    * Make a relative move to the center of the circle and call this function
    */
    String cr = "\r\n";
    
    dxf +="  0"+ cr;
    dxf +="CIRCLE"+ cr;
    
    dxf +="  5"+ cr; //  Drawing Datadbase Handle
    dxf +=hndl+ cr;
    dxf +="  8"+ cr; //  Layer name
    dxf +="Design" + cr; //layer+ cr;
    
    dxf +=" 6" + cr; // Line Type
    dxf +="CONTINUOUS" + cr;
    dxf +=" 62" + cr; // Line Colour
    dxf += getLineCol(layer) + cr;
    
    dxf +=" 10"+ cr; //  center X
    dxf +=xabs+ cr;
    dxf +=" 20"+ cr; //  center Y
    dxf +=yabs+ cr;
    dxf +=" 30"+ cr; //  center Z
    dxf +="0.0"+ cr;
    dxf +=" 40"+ cr; //  Radius
    dxf +=radius+ cr;
    
    //  Finish
    hndl = hndl + 1;
    
    return true;
  }
  
  /**
  * ARC2
  * @param XcenterAbs
  * @param YcenterAbs
  * @param radius
  * @param startAngle (0-90-180-270) Mid(3) not Top Str
  * @param endAngle
  * @param endPosX - Incremental movement to update AbsX
  * @param endPosY - to update AbsY
  * @param layer
  * @return DXF Arc
  */ 
  protected boolean arc2(double XcenterAbs, double YcenterAbs, double radius, double startAngle, double endAngle, double endPosX, double endPosY, String layer) {
    String cr = "\r\n";
    
    dxf +="  0" + cr;
    dxf +="ARC" + cr;
    
    dxf +="  5" + cr; //  Drawing Datadbase Handle
    dxf += Integer.toString(hndl) + cr;
    dxf +="  8" + cr; //  Layer name
    dxf +="Design" + cr; //layer + cr;
    
    dxf +=" 6" + cr; // Line Type
    dxf +="CONTINUOUS" + cr;
    dxf +=" 62" + cr; // Line Colour
    dxf += getLineCol(layer) + cr;
    
    dxf +=" 10" + cr; //  center X
    dxf += XcenterAbs + cr;
    dxf +=" 20" + cr; //  center Y
    dxf += YcenterAbs + cr;
    
    dxf +=" 30" + cr; //  center Z
    dxf +="0.0" + cr;
    
    dxf +=" 40" + cr; //  Radius
    dxf +=radius + cr;
    
    dxf +=" 50" + cr; //  Start Angle of Arc
    dxf +=startAngle + cr;
    dxf +=" 51" + cr; //  End Angle of Arc
    dxf +=endAngle + cr;
    
    //  Finish
    xabs = xabs + endPosX;
    yabs = yabs + endPosY;
    hndl += 1;
    
    return false;
  }
  
  /** 
  * @param xval
  * @param yval
  * @param radius
  * @param layer
  * @param eColor
  * @param cw
  * @return
  */
  protected boolean arc(double xval, double yval, double radius, String layer, int eColor, int cw) {
    // DXF version 12
    // xval   = Incremental X movement to End point
    // yval   = Incremental Y movement to End point
    // radius   = Radius of Arc
    // layer    = Layer to use    (CUT)
    // eColor   = Colour to use  (1)
    // cw     = Clockwise movement of arc: -1 = CW, 1 = CCW so we can just multiply values  CW = std
    
    // Find Centre point of arc and
    double arcArray[] = {0, 0, 0, 0}; // {0 => 0, 1 => 0, 'startAngle' => 0, 'endAngle' => 0}
    arcArray = getArcAngles(xval, yval, radius, cw);
    arcArray[0] = xabs + arcArray[0];
    arcArray[1] = yabs + arcArray[1];
    
    String cr = "\r\n";
    
    dxf +="  0"+ cr;
    dxf +="ARC"+ cr;
    
    dxf +="  5"+ cr; //  Drawing Datadbase Handle
    dxf +=hndl+ cr;
    dxf +="  8"+ cr; //  Layer name
    dxf +="Design" + cr; //layer+ cr;
    
    dxf +=" 6" + cr; // Line Type
    dxf +="CONTINUOUS" + cr;
    dxf +=" 62" + cr; // Line Colour
    dxf += getLineCol(layer) + cr;
    
    dxf +=" 10"+ cr; //  center X
    dxf +=arcArray[0]+ cr;
    dxf +=" 20"+ cr; //  center Y
    dxf +=arcArray[1]+ cr;
    
    dxf +=" 30"+ cr; //  center Z
    dxf +="0.0"+ cr;
    
    dxf +=" 40"+ cr; //  Radius
    dxf +=radius+ cr;
    
    dxf +=" 50"+ cr; //  Start Angle of Arc
    dxf +=arcArray[2]+ cr;
    dxf +=" 51"+ cr; //  End Angle of Arc
    dxf +=arcArray[3]+ cr;
    
    //  Finish
    xabs = xabs + xval * Xaxis;
    yabs = yabs + yval * Yaxis;
    hndl = hndl + 1;
    
    return true;
    
  } // arc - DXF v12
  
  protected double[] getArcAngles(double IncMoveX, double IncMoveY, double radius, int cw) {
    /* Partial conversion from HPGL2DXF - Largely a Special case protected boolean for 0422 slots
    * Find
    * Xcenter, Ycenter
    * startAngle, endAngle
    */
    // array('Xcenter' => 0, 'Ycenter' => 0, 'startAngle' => 0, 'endAngle' => 0) ** Java doesn't support associative arrays **
    double rtnArray[] = {0, 0, 0, 0};
    
    double rx =  (IncMoveX / 2) * Xaxis ; // absX - Xc
    double ry =  (IncMoveY / 2) * Yaxis; // absY - Yc
    double ang = -1; // set-up
    double aplus = 0; // set-up
    double angle = 0; // not required?
    double wrx = 0;
    double wry = 0;
    double tmp = 0;
    double strang = 0;
    double endang = 0;
    
    if (rx == 0) {
      angle = 180; // simple 180 arc
      //  rad = Abs(ry);
      if (ry > 0) {
        ang = 270; //90;
      } else {
        ang = 90; //270;
      }
      //GoTo arnxt
    } else if (ry == 0) {
      angle = 180; // simple 180 arc
      // rad = Abs(rx)
      if (rx > 0) {
        ang = 180; //0;
      } else {
        ang = 0; //180;
      }
      // GoTo arnxt
      //  rad = (Abs(rx) * Abs(rx)) + (Abs(ry) * Abs(ry)) // *** Radius is Known in this instance ***
      //  rad = Sqr(rad)
      
    } else if (rx > 0 && ry > 0) {
      aplus = 0;
      wrx = rx;
      wry = ry;
    } else if (rx < 0 && ry > 0) {
      aplus = 90;
      wrx = ry;
      wry = rx;
    } else if (rx < 0 && ry < 0) {
      aplus = 180;
      wrx = rx;
      wry = ry;
    } else if (rx > 0 && ry < 0) {
      aplus = 270;
      wrx = ry;
      wry = rx;
    }
    
    if (ang == -1) { // not touched by the first 2 conditions
      ang = Math.abs(wry) / Math.abs(wrx);  /* angle if not 0/90/180/270 */
      ang = Math.atan(ang);
      ang = (ang / 3.141592654) * 180;  /* rad2deg */
    }
    //arnxt:
    //  radius = rad
    
    strang = ang + aplus;
    endang = strang + angle;
    if (endang > 360) {
      endang = endang - 360;
    }
    
    /* start_angle = (strang * 3.141592654) /180  deg2rad */
    /* end_angle = (endang * 3.141592654) /180  deg2rad */
    if (Yaxis == -1) { // flipped slot
      tmp = strang;
      strang = endang;
      endang = tmp;
    }
    
    rtnArray[0] = rx; // Xcenter
    rtnArray[1] = ry; // Ycenter
    rtnArray[2] = strang; // startAngle
    rtnArray[3] = endang; // endAngle
    
    return rtnArray;
  } // getArcAngles
  
  protected void relMove(double relX, double relY) {
    // Relative movement - takes global absolute co-ordinatates and adds a relative movement to them
    xabs = xabs + relX * Xaxis;
    yabs = yabs + relY * Yaxis;  
  } // relMove
  
  protected void absMove(double posX, double posY) {
    // Absolute movement - takes global absolute co-ordinatates and adds a relative movement to them
    xabs = posX * Xaxis;
    yabs = posY * Yaxis;  
  } // absMove

  
 /**
   * 
   * @param angLineX 
   * @param angLineY
   * @param rad Radius
   * @return
   */
  public static double[] findFilletPoints(double angLineX, double angLineY, double rad) {
    /* Finds Fillet points of an Arc on angled line, when filleting 2 lines - One Straight
     * TG - 26/08/2016
     */
    double rtnAry[] = {0, 0, 0};
    
    double lineAngle = Math.toDegrees(Math.atan2(angLineX, angLineY)); 
    rtnAry[0] = rad * Math.sin(Math.toRadians(lineAngle)); // Opposite (X)
    rtnAry[1] = rad * Math.cos(Math.toRadians(lineAngle)); // Adjacent (Y)
    rtnAry[2] = lineAngle; // Angle of Line / Angle of Arc hit point () 
    if (lineAngle > 45) {
      rtnAry[0] = rad - rtnAry[0];
      // Adjacent = IS OK
    }   
    
    return rtnAry; // 0=YendPt = this.topOtrRad - arcDat[0] , 1=XendPt , 2=endAngle = 360 - arcDat[2]
  } // findFilletPoints  
  
  // end methods
} // end of dxf12objects
