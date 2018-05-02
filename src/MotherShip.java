import java.io.IOException;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class MotherShip {

	private static double VELOCITY  = 30;
	private static int    ANGLE_INC = 1;
	
	private double angle, velocity;
	public double x, y, z;
	
	private String name;
	
	private Group   droid;
	private ObjView view;
	
	private Point3D car_r = new Point3D(0, 1, 0);
	
	public MotherShip(String name, double x, double y, double z, Group root) {
		
		this.name = name;
		
		angle = 180;
		velocity = 0;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		view = new ObjView();
		try {
			view.load(ClassLoader.getSystemResource("SpaceShipDetailed.obj").toString());
		} catch (IOException e) {
			System.out.println("Trouble loading model");
			e.printStackTrace();
		}
		
		droid = view.getRoot();
		
		droid.setScaleX(7);
		droid.setScaleY(-7);
		droid.setScaleZ(-7);
		droid.setTranslateX(this.x);
		droid.setTranslateZ(this.z);
		droid.setRotationAxis(car_r);
		droid.setRotate(angle);
		
		root.getChildren().add(droid);
		
		for (Node n:droid.getChildren())
		{
			MeshView mv = (MeshView) n;
			Mesh m = ((MeshView) n).getMesh();
//			System.out.println(n);
//			System.out.println(m);
			TriangleMesh tm = (TriangleMesh) m;
		}
		
	}
	
	public Group getDroid() { return droid; }
	
	public double getAngle(){ return angle; }
	public double getX(){ return x; }
	public double getY(){ return y; }
	public double getZ(){ return z; }
	
	public void update() {
//		System.out.println("car.update()");
		z -= velocity * Math.cos(Math.toRadians(angle));
		x -= velocity * Math.sin(Math.toRadians(angle));
		
		droid.setTranslateX(x);
		droid.setTranslateY(y);
		droid.setTranslateZ(z);
		droid.setRotate(angle);
	}
	
	public void up() {
		droid.setTranslateY(y++);
	}
	
	public void down() {
		droid.setTranslateY(y--);
	}
	
	public void right() {
		angle = (angle + ANGLE_INC) % 360;
		
	}
	
	public void left() {
		angle = (angle - ANGLE_INC) % 360;
	}
	
	public BoundingBox collisionBox(){
	    return new BoundingBox(x-30, y, z-30, 90, 60, 150);
	}
}
