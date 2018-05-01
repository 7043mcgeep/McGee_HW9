import java.io.IOException;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class alien {
	
	private double angle;
	private double x, y, z;
	private boolean hit;
	public boolean exclude;
	private String name;
	private Group  alien;
	private ObjView view;
	private Point3D car_r = new Point3D(0, 1, 0);
	
	public alien(String name, double x, double y, double z, Group root) {
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		view = new ObjView();
		try {
			view.load(ClassLoader.getSystemResource("alien.obj").toString());
		} catch (IOException e) {
			System.out.println("Trouble loading model");
			e.printStackTrace();
		}
		
		alien = view.getRoot();
		
		alien.setScaleX(25);
		alien.setScaleY(-25);
		alien.setScaleZ(-25);
		alien.setTranslateX(this.x);
		alien.setTranslateY(-60);
		alien.setTranslateZ(this.z);
		alien.setRotationAxis(car_r);
		
		root.getChildren().add(alien);
		
		for (Node n:alien.getChildren())
		{
			MeshView mv = (MeshView) n;
			Mesh m = ((MeshView) n).getMesh();
			TriangleMesh tm = (TriangleMesh) m;
		}
		
	}
	
	public Group getDroid() { return alien; }
	
	public double getAngle(){ return angle; }
	public double getX(){ return x; }
	public double getZ(){ return z; }
	
	public boolean isHit() {
		if(hit) return true;
		else return false;
	}
	
	public BoundingBox collisionBox(){
	    return new BoundingBox(x, 0, z, 60, 50, 60);
	}
	
	public void update() {
		
		alien.setTranslateX(x);
		alien.setTranslateY(-50);
		alien.setTranslateZ(z);
		alien.setRotate(angle);
	}
	
	public void kill() {
		alien.setTranslateY(y+150); 
	}
	
	
}
