package sprites.awards;

import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import main.Main;
import sprites.*;

public class Bonus extends Sprite{

    private static final double BONUS_R = 25;
    private static final double BONUS_FREQ = 0.1;
    private static final double BLACK_FREQ = 0.9;
    private static final double YELLOW_FREQ = BLACK_FREQ;
    private static final double RED_FREQ = 0.5;
    private static final double GREEN_FREQ = 0.1;
    private double velocityY = 2;
    
    public static final double POINT_S = 50;
    public static final double POINT_M = 100;
    public static final double POINT_L = 150;
    
    public interface BonusType{} 
    /*3 - 0.2*/ public static enum RedBonus implements BonusType{Stream /*S*/, Boomerang /*B*/};
    /*2 - 0.3*/ public static enum YellowBonus implements BonusType{Speed /*V*/, Rotation /*R*/, Shield /*S*/, ShotGrowth /*G*/, KnockOut /*K*/};
    /*4 - 0.1*/ public static enum GreenBonus implements BonusType{Life /*H*/, PointS /*S*/, PointM /*M*/, PointL /*L*/};
    /*1 - 0.4*/ public static enum BlackBonus implements BonusType{Munition /*M*/, Triangle /*T*/, Rhombus /*R*/, Pentagon /*P*/, Hexagon /*H*/};
//    
//    public static enum BonusType{Stream /*1*/, Boomerang /*2*/, 
//                                    Speed /*1*/, Rotation /*2*/, Shield /*3*/, ProjectileGrowth /*4*/, KnockOut /*5*/, 
//                                    Life /*1*/, PointS /*2*/, PointM /*3*/, PointL /*4*/, 
//                                    Munition /*1*/, Triangle /*2*/, Rhombus /*3*/, Petagon /*4*/, Hexagon /*5*/};
    private BonusType type;
    private Circle power;
    private Image icon;
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
                            else return YellowBonus.ShotGrowth;
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
                path = "/resources/bonus/fukiya.png";
                break;
            default:
                path = "/resources/bonus/boomerang.png";
                break;
        }
    }
    
    public void initYellowBonus(YellowBonus bonus){
        switch(bonus){
            case Speed:
                path = "/resources/bonus/speed.png";
                break;
            case Rotation:
                path = "/resources/bonus/rotate.png";
                break;
            case Shield:
                path = "/resources/bonus/shield.png";
                break;
            case ShotGrowth:
                path = "/resources/bonus/enlarge.png";
                break;
            case KnockOut:
                path = "/resources/bonus/destroy.png";
                break;
        }
    }
    
    public void initGreenBonus(GreenBonus bonus){
        switch(bonus){
            case Life:
                path = "/resources/bonus/hollowheart.png";
                break;
            case PointS:
                path = "/resources/bonus/smallpoints.png";
                break;
            case PointM:
                path = "/resources/bonus/mediumpoints.png";
                break;
            case PointL:
                path = "/resources/bonus/bigpoints.png";
                break;       
        }
    }
    
    public void initBlackBonus(BlackBonus bonus){
        switch(bonus){
            case Munition:
                path = "/resources/bonus/powerup.png";
                break;
            case Triangle:
                path = "/resources/bonus/triangle.png";
                break;
            case Rhombus:
                path = "/resources/bonus/rhombus.png";
                break;
            case Pentagon:
                path = "/resources/bonus/pentagon.png";
                break;
            case Hexagon:
                path = "/resources/bonus/hexagon.png";
                break;
        }
    }

    public BonusType getBonusType(){
        return type;
    }
    
    public String getPath(){
        return path;
    }    

    @Override
    public void update() {
        if ((getTranslateY() + velocityY) > Main.WINDOW_HEIGHT)
            Main.removeSprite(this);
        else
            setTranslateY(getTranslateY() + velocityY);
    }
    
    
    
}
