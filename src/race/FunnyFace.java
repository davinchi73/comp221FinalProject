package race;

import edu.macalester.graphics.*;

import java.awt.Color;

@SuppressWarnings("WeakerAccess")  // This stops Java from giving you warnings about your code that are not helpful here
public class FunnyFace {
    private static final Color
        HEAD_COLOR = new Color(0xFFDE30),
        HEAD_OUTLINE_COLOR = new Color(0xAC9620),
        MOUTH_COLOR = new Color(0xE45B5B);


    /**
     * Creates a smiley face emoji.
     *
     * @param size The overall width and height of the emoji.
     * @return A graphic that you can add to a window, or place inside some other graphics group.
     */
    public static GraphicsGroup createSmileyFace(double size) {
        GraphicsGroup group = new GraphicsGroup();

        group.add(createHead(size, size));

        Ellipse eyeOne = createEye1(size * .1, size * .1);
        eyeOne.setCenter(size * .35, size *.4);
        group.add(eyeOne);

        Ellipse eyeTwo = createEye2(size * .1, size * .1);
        eyeTwo.setCenter(size * .65, size *.4);
        group.add(eyeTwo);

        Arc mouth = createSmile(size * 0.6, size * 0.5);
        mouth.setCenter(size * 0.5, size * 0.75);
        group.add(mouth);

        return group;
    }

    /**
     * Creates an empty emoji head. The head fits inside the box from (0,0)
     * to (width,height).
     */
    private static Ellipse createHead(double height, double width) {
        Ellipse head = new Ellipse(0, 0, width, height);
        head.setFillColor(HEAD_COLOR);
        head.setStrokeColor(HEAD_OUTLINE_COLOR);
        head.setStrokeWidth(2);
        return head;
    }
    
    /**
     * Creates an eye to be added onto the empty emoji head.
     */
    private static Ellipse createEye1(double height, double width) {

        Ellipse eye1 = new Ellipse(0, 0, width, height); 
        eye1.setFillColor(Color.BLACK);
        eye1.setStrokeColor(Color.BLACK);
        eye1.setStrokeWidth(1);
        return eye1;

    }

    /**
     * Creates a second eye to be added onto the empty emoji head.
     */
    private static Ellipse createEye2(double height, double width) {

        Ellipse eye2 = new Ellipse(0, 0, width, height); 
        eye2.setFillColor(Color.BLACK);
        eye2.setStrokeColor(Color.BLACK);
        eye2.setStrokeWidth(2);
        return eye2;

    }
    

    /**
     * Creates a smile-shaped arc. The arc is measured relative to its “implied ellipse,” which is
     * the shape that would be formed if the arc were extend all the way around. The size of the
     * resulting arc will be smaller than the implied ellipse’s size.
     *
     * @param ellipseWidth  The width of the implied ellipse from which the smile’s arc is cut.
     * @param ellipseHeight The width of the implied ellipse from which the smile’s arc is cut.
     */
    private static Arc createSmile(double ellipseWidth, double ellipseHeight) {
        Arc mouth = new Arc(0, 0, ellipseWidth, ellipseHeight, 200, 140   );
        mouth.setStrokeColor(MOUTH_COLOR);
        mouth.setStrokeWidth(4);
        return mouth;
    }
}
