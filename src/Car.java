import java.io.IOException;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Car {
	
	private static double VELOCITY  = 30;
	private static int    ANGLE_INC = 2;
	
	private double angle, velocity;
	private double x, z;
	
	private String name;
	
	private Group   droid;
	private ObjView view;
	
	private Point3D car_r = new Point3D(0, 1, 0);
	
	public Car(String name, double x, double z, Group root) {
		
		this.name = name;
		
		angle = 180;
		velocity = 0;
		
		this.x = x;
		this.z = z;
		
		view = new ObjView();
		try {
			view.load(ClassLoader.getSystemResource("LEGO_CAR_B2.obj").toString());
		} catch (IOException e) {
			System.out.println("Trouble loading model");
			e.printStackTrace();
		}
		
		droid = view.getRoot();
		
		droid.setScaleX(5);
		droid.setScaleY(-5);
		droid.setScaleZ(-5);
		droid.setTranslateX(this.x);
		droid.setTranslateY(-50);
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
//			System.out.println("Faces: "+tm.getFaceElementSize());
//			System.out.println(tm.getFaces() );
//			System.out.println(tm.getFaceSmoothingGroups());
//			System.out.println("Normals: "+tm.getNormalElementSize());
//			System.out.println(tm.getNormals());
//			System.out.println("Points: "+tm.getPointElementSize());
//			System.out.println(tm.getPoints());
//			System.out.println("TexCoords: "+tm.getTexCoordElementSize());
//			System.out.println(tm.getTexCoords());
		}
		
	}
	
	public Group getDroid() { return droid; }
	
	public double getAngle(){ return angle; }
	public double getX(){ return x; }
	public double getZ(){ return z; }
	
	public void update() {
//		System.out.println("car.update()");
		z -= velocity * Math.cos(Math.toRadians(angle));
		x -= velocity * Math.sin(Math.toRadians(angle));
		
		droid.setTranslateX(x);
		droid.setTranslateY(-50);
		droid.setTranslateZ(z);
		droid.setRotate(angle);
	}
	
	public void forward() {
		if(!CoolCar.speedboost)
			velocity = VELOCITY;
		else
			velocity = VELOCITY * 2;
	}
	
	public void backward() {
		velocity = VELOCITY * -1;
	}
	
	public void stop() {
		velocity = 0;
	}
	
	public void right() {
		angle = (angle + ANGLE_INC) % 360;
		
	}
	
	public void left() {
		angle = (angle - ANGLE_INC) % 360;
	}
	
}
