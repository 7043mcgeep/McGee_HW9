import java.io.IOException;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class alien {
	
	private double angle;
	private double x, z;
	
	private String name;
	
	private Group  alien;
	private ObjView view;
	
	private Point3D car_r = new Point3D(0, 1, 0);
	
	public alien(String name, double x, double z, Group root) {
		
		this.name = name;
		
		this.x = x;
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
	
	public Group getDroid() { return alien; }
	
	public double getAngle(){ return angle; }
	public double getX(){ return x; }
	public double getZ(){ return z; }
	
	public void update() {
		
		alien.setTranslateX(x);
		alien.setTranslateY(-50);
		alien.setTranslateZ(z);
		alien.setRotate(angle);
	}
	
	
}
