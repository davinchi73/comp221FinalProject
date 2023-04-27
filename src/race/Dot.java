package race;

import edu.macalester.graphics.Ellipse;

import java.awt.Color;

public class Dot extends Ellipse {

    public Dot(double xpos, double ypos, double radius) {
        super(xpos, ypos, radius, radius);
        super.setFillColor(Color.BLACK);
    }
    
        
    

}
