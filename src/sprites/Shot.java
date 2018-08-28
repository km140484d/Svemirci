package sprites;

import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class Shot extends Sprite {

    private static final double SHOT_VELOCITY = -5;
    
    private Path body;
    
    private static final int a = 8;
    
    public Shot() {
        double h = a * 1.0 * Math.sqrt(3)/2;
        //System.out.println();
        body = new Path(
                new MoveTo(h, -a/2),
                new LineTo(0, -a),
                new LineTo(-h, -a/2),
                new LineTo(-h, a/2),
                new LineTo(0, a),
                new LineTo(h, a/2),
                new ClosePath()
        );
        body.setFill(Color.RED);
        
        RotateTransition rt = new RotateTransition(Duration.seconds(1), body);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setCycleCount(Animation.INDEFINITE);
        rt.play();
        getChildren().addAll(body);
    }
    
    @Override
    public void update() {
        setTranslateY(getTranslateY() + SHOT_VELOCITY);
    }
    
}
