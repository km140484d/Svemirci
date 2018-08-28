package sprites.enemies;

import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.*;
import javafx.util.Duration;

public class Scout extends Enemy{
    
    private Rectangle body;
    
    //0-left, 1-right
    private Group [] gr_eyes = new Group [2];
    private Ellipse [] eyes = new Ellipse [2]; 
    private Circle [] pupils = new Circle [2];
    
    private Path [] ears = new Path [2]; 
    
    private Arc mouth; 
    
    private Path helmet;
    public Scout(){
        //ears
        for(int i = 0; i < ears.length; i++){
            ears[i] = new Path(
//                    new MoveTo(5, EN_HEIGHT/2), 
//                    new LineTo(5-EN_WIDTH*2/3, -5),
//                    new LineTo(5-EN_WIDTH*2/3, EN_HEIGHT + 5),
//                    new ClosePath()
                        new MoveTo(i==0 ? -EN_WIDTH/2+5 : EN_WIDTH/2-5, 0), 
                        new LineTo(i==0 ? -EN_WIDTH*7/6+5 : EN_WIDTH*7/6-5, -EN_HEIGHT/2-5),
                        new VLineTo(EN_HEIGHT/2+5),
                        new ClosePath()
            );
            double angle = 15;
            if (i == 1){
                //ears[i].getTransforms().addAll(new Translate(EN_WIDTH-10, 0), new Rotate(180, 5, EN_HEIGHT/2));
                angle = -angle;
            }
            ears[i].setFill(Color.ALICEBLUE);
            
            //flying animation
            Rotate fly_rot = new Rotate();
//            fly_rot.setPivotY(EN_HEIGHT/2);
//            fly_rot.setPivotX(5);
            fly_rot.setPivotY(0);
            fly_rot.setPivotX(i==0 ? -EN_WIDTH/2+5 : EN_WIDTH/2-5);
            ears[i].getTransforms().add(fly_rot);
            Timeline tl = new Timeline(
                    new KeyFrame( Duration.seconds(0),
                            new KeyValue(fly_rot.angleProperty(), angle, Interpolator.EASE_BOTH)
                    ),
                    new KeyFrame( Duration.seconds(2),
                            new KeyValue(fly_rot.angleProperty(), -angle, Interpolator.EASE_BOTH)
                    )
            );
            tl.setAutoReverse(true);
            tl.setCycleCount(Animation.INDEFINITE);
            tl.play();
            getChildren().add(ears[i]);
        }
        
        //body
        body = new Rectangle(0, 0, EN_WIDTH, EN_HEIGHT);
        body.setArcWidth(EN_WIDTH/2);
        body.setArcHeight(EN_HEIGHT/2);
        body.setFill(Color.YELLOW);
        body.setTranslateX(-EN_WIDTH/2);
        body.setTranslateY(-EN_HEIGHT/2);
        getChildren().add(body);

        //eyes
        for(int i = 0; i < eyes.length; i++){ 
            eyes[i] = new Ellipse(EYE_WIDTH, EYE_HEIGHT);
            eyes[i].setFill(Color.WHITE);
            eyes[i].setStroke(Color.BLACK);
            eyes[i].setStrokeWidth(0.2);
            pupils[i] = new Circle(PUPIL_RADIUS);
            gr_eyes[i] = new Group();
            gr_eyes[i].getChildren().addAll(eyes[i], pupils[i]);
            gr_eyes[i].setTranslateX((i == 0? -1:1) * 1.3 *EYE_WIDTH);
            gr_eyes[i].setTranslateY(-EN_HEIGHT/2 + EYE_HEIGHT*3);
//            gr_eyes[i].setTranslateX(EYE_WIDTH*15/8 + i*EYE_WIDTH*5/2);
//            gr_eyes[i].setTranslateY(EYE_HEIGHT*3);
            getChildren().add(gr_eyes[i]);           
        }
        
        //mouth
//        mouth = new Arc(EN_WIDTH*1/2, EN_HEIGHT*2/3, EN_WIDTH/3, EN_HEIGHT/4, 180, 180); 
        mouth = new Arc(0, EN_HEIGHT/7, EN_WIDTH/3, EN_HEIGHT/4, 180, 180); 
        getChildren().add(mouth);
        
//        helmet = new Path(
//                new MoveTo()
//        );
    
    }
    
    public Rectangle getBody(){
        return body;
    }
    
    public Group getLeftEye(){
        return gr_eyes[0];
    }
    
    public Group getRightEye(){
        return gr_eyes[1];
    }
    
}
