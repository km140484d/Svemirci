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
    private String path = "";
    
    public Bonus(BonusType type, double x, double y){
        this.type = type;
        power = new Circle(BONUS_R);
        if (type instanceof RedBonus)
            initRedBonus((RedBonus)type);
        else
            if (type instanceof YellowBonus)
                initYellowBonus((YellowBonus)type);
            else
                if (type instanceof GreenBonus)
                    initGreenBonus((GreenBonus) type);
                else
                    initBlackBonus((BlackBonus)type);
        power.setFill(new ImagePattern(new Image(path)));
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
    
    public void initRedBonus(RedBonus bonus){
        switch(bonus){
            case Stream:
                path = "/resources/awards/fukiya.png";
                break;
            default:
                path = "/resources/awards/boomerang.png";
                break;
        }
    }
    
    public void initYellowBonus(YellowBonus bonus){
        switch(bonus){
            case Speed:
                path = "/resources/awards/speed.png";
                break;
            case Rotation:
                path = "/resources/awards/rotate.png";
                break;
            case Shield:
                path = "/resources/awards/shield.png";
                break;
            case ShotGrowth:
                path = "/resources/awards/enlarge.png";
                break;
            case KnockOut:
                path = "/resources/awards/pause.png";
                break;
        }
    }
    
    public void initGreenBonus(GreenBonus bonus){
        switch(bonus){
            case Life:
                path = "/resources/awards/hollowheart.png";
                break;
            case PointS:
                path = "/resources/awards/smallpoints.png";
                break;
            case PointM:
                path = "/resources/awards/mediumpoints.png";
                break;
            case PointL:
                path = "/resources/awards/bigpoints.png";
                break;       
        }
    }
    
    public void initBlackBonus(BlackBonus bonus){
        switch(bonus){
            case Munition:
                path = "/resources/awards/powerup.png";
                break;
            case Triangle:
                path = "/resources/awards/triangle.png";
                break;
            case Rhombus:
                path = "/resources/awards/rhombus.png";
                break;
            case Pentagon:
                path = "/resources/awards/pentagon.png";
                break;
            case Hexagon:
                path = "/resources/awards/hexagon.png";
                break;
        }
    }

    public BonusType getBonusType(){
        return type;
    }
    
    public String getPath(){
        return path;
    }    
    
    public double getVelocityY(){
        return velocityY;
    }

    @Override
    public void update() {
        setTranslateY(getTranslateY() + velocityY);
    }

}
