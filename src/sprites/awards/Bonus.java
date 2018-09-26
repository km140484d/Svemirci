package sprites.awards;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import sprites.*;

public class Bonus extends Sprite{

    private static final double BONUS_R = 25;
    private static final double BONUS_FREQ = 0.1;
    private static final double BLACK_FREQ = 0.09;
    private static final double YELLOW_FREQ = 0.07;
    private static final double RED_FREQ = 0.05;
    private static final double GREEN_FREQ = 0.01;
    private double velocityY = 2;
    
    public static final double POINT_S = 50;
    public static final double POINT_M = 100;
    public static final double POINT_L = 150;
    
    public interface BonusType{} 
    /*3 - 0.2*/ public static enum RedBonus implements BonusType{Stream /*S*/, Boomerang /*B*/};
    /*2 - 0.3*/ public static enum YellowBonus implements BonusType{Speed /*V*/, Rotation /*R*/, Shield /*S*/, ShotGrowth /*G*/, KnockOut /*K*/};
    /*4 - 0.1*/ public static enum GreenBonus implements BonusType{Life /*H*/, PointS /*S*/, PointM /*M*/, PointL /*L*/};
    /*1 - 0.4*/ public static enum BlackBonus implements BonusType{Munition /*M*/, Triangle /*T*/, Rhombus /*R*/, Pentagon /*P*/, Hexagon /*H*/};

    private BonusType type;
    private Circle power;
    
    private static final ImagePattern im_stream, im_boomerang;
    private static final ImagePattern im_speed, im_rotation, im_shield, im_shot_growth, im_knock_out;
    private static final ImagePattern im_life, im_point_s, im_point_m, im_point_l;
    private static final ImagePattern im_munition, im_triangle, im_rhombus, im_pentagon, im_hexagon;
    
    static{
        //red
        im_stream = new ImagePattern(new Image("/resources/awards/fukiya.png"));
        im_boomerang = new ImagePattern(new Image("/resources/awards/boomerang.png"));
        //yellow
        im_speed = new ImagePattern(new Image("/resources/awards/speed.png"));
        im_rotation = new ImagePattern(new Image("/resources/awards/rotate.png"));
        im_shield = new ImagePattern(new Image("/resources/awards/shield.png"));
        im_shot_growth = new ImagePattern(new Image("/resources/awards/enlarge.png"));
        im_knock_out = new ImagePattern(new Image("/resources/awards/pause.png"));
        //green
        im_life = new ImagePattern(new Image("/resources/awards/hollowheart.png"));
        im_point_s = new ImagePattern(new Image("/resources/awards/smallpoints.png"));
        im_point_m = new ImagePattern(new Image("/resources/awards/mediumpoints.png"));
        im_point_l = new ImagePattern(new Image("/resources/awards/bigpoints.png"));
        //black
        im_munition = new ImagePattern(new Image("/resources/awards/powerup.png"));
        im_triangle = new ImagePattern(new Image("/resources/awards/triangle.png"));
        im_rhombus = new ImagePattern(new Image("/resources/awards/rhombus.png"));
        im_pentagon = new ImagePattern(new Image("/resources/awards/pentagon.png"));
        im_hexagon = new ImagePattern(new Image("/resources/awards/hexagon.png"));       
    }
    
    public Bonus(BonusType type, double x, double y){
        this.type = type;
        power = new Circle(BONUS_R);
        if (type instanceof RedBonus)
            power.setFill(initRedBonus((RedBonus)type));
        else
            if (type instanceof YellowBonus)
                power.setFill(initYellowBonus((YellowBonus)type));
            else
                if (type instanceof GreenBonus)
                    power.setFill(initGreenBonus((GreenBonus) type));
                else
                    power.setFill(initBlackBonus((BlackBonus)type));
        //power.setFill(new ImagePattern(new Image(path)));
        getChildren().addAll(power);
        setTranslateX(x);
        setTranslateY(y);
    }
    
    public static BonusType pickBonus(){
        double rand = Math.random();
        if (rand < BONUS_FREQ*4){
            if (rand < BLACK_FREQ*1) return BlackBonus.Triangle;
            else
                if (rand < BLACK_FREQ*2) return BlackBonus.Rhombus;
                else
                    if (rand < BLACK_FREQ*3) return BlackBonus.Pentagon;
                    else
                        if (rand < BLACK_FREQ*4) return BlackBonus.Hexagon;
                        else return BlackBonus.Munition;
        }else{
            if (rand < 0.4 + BONUS_FREQ*3){
                rand -= 0.4;
                if (rand < YELLOW_FREQ*1) return YellowBonus.Speed;
                else
                    if (rand < YELLOW_FREQ*2) return YellowBonus.Shield;
                    else
                        if (rand < YELLOW_FREQ*3) return YellowBonus.Rotation;
                        else
                            if (rand < YELLOW_FREQ*4) return YellowBonus.ShotGrowth;
                            else return YellowBonus.KnockOut;
            }else{
                if (rand < 0.7 + BONUS_FREQ*2){
                    rand -= 0.7;
                    if (rand < RED_FREQ*1) return RedBonus.Boomerang;
                    else return RedBonus.Stream;
                }else{
                    rand -= 0.9;
                    if (rand < GREEN_FREQ*4) return GreenBonus.PointS;
                    else
                        if (rand < GREEN_FREQ*7) return GreenBonus.PointM;
                        else 
                            if (rand < GREEN_FREQ*9) return GreenBonus.PointL;
                            else return GreenBonus.Life;
                }
            }
        }
    }
    
    public ImagePattern initRedBonus(RedBonus bonus){
        switch(bonus){
            case Stream:
                return im_stream;
            default:
                return im_boomerang;
        }
    }
    
    public ImagePattern initYellowBonus(YellowBonus bonus){
        switch(bonus){
            case Speed:
                return im_speed;
            case Rotation:
                return im_rotation;
            case Shield:
                return im_shield;
            case ShotGrowth:
                return im_shot_growth;
            default:
                return im_knock_out;
        }
    }
    
    public ImagePattern initGreenBonus(GreenBonus bonus){
        switch(bonus){
            case Life:
                return im_life;
            case PointS:
                return im_point_s;
            case PointM:
                return im_point_m;
            default:
                return im_point_l;      
        }
    }
    
    public ImagePattern initBlackBonus(BlackBonus bonus){
        switch(bonus){
            case Munition:
                return im_munition;
            case Triangle:
                return im_triangle;
            case Rhombus:
                return im_rhombus;
            case Pentagon:
                return im_pentagon;
            default:
                return im_hexagon;
        }
    }

    public BonusType getBonusType(){
        return type;
    }
    
    public ImagePattern getImage(){
        return (ImagePattern)power.getFill();
    }    
    
    public double getVelocityY(){
        return velocityY;
    }

    @Override
    public void update() {
        setTranslateY(getTranslateY() + velocityY);
    }

}
