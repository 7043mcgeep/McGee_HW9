import java.io.IOException;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;

public class FollowCam {
	
	private static double VELOCITY  = 30;
	private static int    ANGLE_INC = 2;
	
	private double angle, velocity;
	public boolean backwards;
	public double x, y, z;
	
	private String name;
	
	private Group   cam;
	private ObjView view;
	
	private Point3D cam_r = new Point3D(0, 1, 0);
	
	public FollowCam(String name, double x, double y, double z, Group root) {
		
		this.name = name;
		
		angle = 180;
		velocity = 0;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		cam = view.getRoot();
		
		cam.setScaleX(5);
		cam.setScaleY(-5);
		cam.setScaleZ(-5);
		cam.setTranslateX(this.x);
		cam.setTranslateY(-50);
		cam.setTranslateZ(this.z);
		cam.setRotationAxis(cam_r);
		cam.setRotate(angle);
		
		root.getChildren().add(cam);
		
	}
	
	public Group getDroid() { return cam; }
	public double getAngle(){ return angle; }
	public double getX(){ return x; }
	public double getY(){ return y; }
	public double getZ(){ return z; }
	
	public void update() {
		z -= velocity * Math.cos(Math.toRadians(angle));
		x -= velocity * Math.sin(Math.toRadians(angle));
		
		cam.setTranslateX(x);
		cam.setTranslateY(y);
		cam.setTranslateZ(z);
		cam.setRotate(angle);
	}
	
	public void forward() {
		if(!CoolCar.speedboost)
			velocity = VELOCITY;
		else
			velocity = VELOCITY * 2;
	}
	
	public void backward() {
		backwards = true;
		velocity = VELOCITY * -1;
	}
	
	public void stop() {
		velocity = 0;
	}
	
	public void right() {
		if(backwards)
			angle = (angle - ANGLE_INC) % 360;
		else
			angle = (angle + ANGLE_INC) % 360;
	}
	
	public void left() {
		if(backwards)
			angle = (angle + ANGLE_INC) % 360;
		else
			angle = (angle - ANGLE_INC) % 360;
	}
	
}

