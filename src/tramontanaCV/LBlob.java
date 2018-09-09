
package tramontanaCV;
import tramontanaCV.BVector;
import processing.core.PApplet;
import processing.core.PShape;


public class LBlob {
	public BVector[] pts;
	public int nPts;
	PApplet sketch;
	private processing.core.PShape currentShape;
	
	public LBlob(PApplet parent)
	{
	  sketch = parent;
	  
	}
	
	public void draw() {
		
	  currentShape  =  sketch.createShape();
	  currentShape.setVisible(true);
	  currentShape.beginShape();
	  currentShape.fill(0, 0, 255);
	  currentShape.noStroke();
	  for(int i=0;i<nPts;i++) {
		  currentShape.vertex(pts[i].x, pts[i].y); 
	  }
	  currentShape.endShape(processing.core.PApplet.CLOSE);
	  sketch.shape(currentShape);
	}
}
