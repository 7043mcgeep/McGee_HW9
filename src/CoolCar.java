import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial; 
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 * @author Patrick J. McGee
 * 		   Alex Gattone
 * 
 * Car Coin Collector!
 * Drive the fancy car and collect all the coins to win!
 * If you fall off the platform, you lose!
 * Collect all the coins as fast as you can. The less time you take,
 * the higher score you get!
 * 
 * Assignment 9
 * 5/4/2018
 */
public class CoolCar extends Application {

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private static final int FPS = 30;
	private PerspectiveCamera camera;
	private Group cameraDolly;
	
	private final double sceneWidth = screenSize.getWidth();
	private final double sceneHeight = screenSize.getHeight();

	private double mousePosX;
	private double mousePosY;
	private double mouseOldX;
	private double mouseOldY;
	private double mouseDeltaX;
	private double mouseDeltaY;
	 
	public static boolean speedboost = false;
	
	Sphere sphere;
	Car car;
	alien a[] = new alien[30];
	static MediaPlayer mP;
	
	// Import all images
	Image sunset = new Image("sunset.jpg");
	Image mtn1 = new Image("red.jpg");
	Image mtn2 = new Image("mtn_sand_light.jpg");
	Image mtn3 = new Image("mtn_sand_dark.jpg");
	Image mtn4 = new Image("rock.jpg");
	Image ground = new Image("dirt.jpg");
	Image sun = new Image("sun.png");
	
	// Set initial values for sun position (which are changed in update()).
	int sphere_x = 0, sphere_y = -1010, sphere_z = 2000;
	boolean right_cam, left_cam, falling;
	
	AmbientLight light;
	
	// Make the world.
	private void constructWorld(Group root) {
		
		Media song = new Media(ClassLoader.getSystemResource("David Bowie - Life On Mars.mp3").toString());
		//occasionally the music just stops and i'm not sure why, the only time i tell it to is when you restart at the end of the game
		mP = new MediaPlayer(song);
		mP.setCycleCount(20);
		mP.play();
		mP.setVolume(0.7);
		
		// Soft light
		light = new AmbientLight(Color.rgb(153, 153, 153));
		root.getChildren().add(light);

		PointLight pl = new PointLight();
		pl.setTranslateX(5000);
		pl.setTranslateY(-3000);
		pl.setTranslateZ(-2000);
		root.getChildren().add(pl);
		
		// Box for ground
		Box xAxis = new Box(20000, 400, 20000);
		final PhongMaterial gnd = new PhongMaterial();
		gnd.setDiffuseMap(ground);
		gnd.setSpecularColor(Color.WHITE);
		xAxis.setMaterial(gnd);
		xAxis.setTranslateY(190);

		// The Sun.
		sphere = new Sphere(50);
		final PhongMaterial sun_mat = new PhongMaterial();
		sun_mat.setDiffuseMap(sun);
		sun_mat.setSpecularColor(Color.WHITE);
		sphere.setMaterial(sun_mat);
		sphere.setTranslateX(sphere_x);
		sphere.setTranslateY(sphere_y);
		sphere.setTranslateZ(sphere_z);
		
		// Pyramid implementation.
		TriangleMesh pyramidMesh = new TriangleMesh();
		// define (a trivial) texture map
		pyramidMesh.getTexCoords().addAll(
				0.5f, 0,
				0, 0.5f,
				1, 0.5f,
				0, 1,
				1, 1
				);
		// define vertices
		float h = 200;                    // Height
		float s = 400;                    // Base hypotenuse
		pyramidMesh.getPoints().addAll(
		        0,    0,    0,            // Point 0 - Top
		        0,    h,    -s/2,         // Point 1 - Front
		        -s/2, h,    0,            // Point 2 - Left
		        s/2,  h,    0,            // Point 3 - Right
		        0,    h,    s/2           // Point 4 - Back
		    );
		// define faces
		pyramidMesh.getFaces().addAll(
		        0,0,  2,1,  1,2,          // Front left face
		        0,0,  1,1,  3,1,          // Front right face
		        0,0,  3,1,  4,2,          // Back right face
		        0,0,  4,1,  2,2,          // Back left face
		        4,1,  1,4,  2,2,          // Bottom left face
		        4,1,  3,3,  1,4           // Bottom right face
		    ); 
		pyramidMesh.getFaceSmoothingGroups().addAll(
				1, 2, 3, 4, 5, 5);
		MeshView pyramid = new MeshView(pyramidMesh);
		final PhongMaterial pyrMaterial = new PhongMaterial();
		pyrMaterial.setDiffuseMap(mtn1);
		pyrMaterial.setSpecularColor(Color.WHITE);
		pyramid.setMaterial(pyrMaterial);
		pyramid.setTranslateX(-50);
		pyramid.setTranslateY(-210);
		pyramid.setTranslateZ(4000);
		root.getChildren().add(pyramid);
		
		// Another large mountain of a different texture
		MeshView pyr2 = new MeshView(pyramidMesh);
		final PhongMaterial py2_map = new PhongMaterial();
		py2_map.setDiffuseMap(mtn2);
		py2_map.setSpecularColor(Color.WHITE);
		pyr2.setMaterial(py2_map);
		pyr2.setTranslateX(150);
		pyr2.setTranslateY(-210);
		pyr2.setTranslateZ(4250);
		root.getChildren().add(pyr2);
		
		// Define a second, smaller size of a mountain.
		TriangleMesh smallPyr = new TriangleMesh();

		smallPyr.getTexCoords().addAll(
				0.5f, 0,
				0, 0.5f,
				1, 0.5f,
				0, 1,
				1, 1
				);
		// Vertices
		float h2 = 80;                     // Height
		float s2 = 160;                    // Base hypotenuse
		smallPyr.getPoints().addAll(
		        0,    0,    0,              // Point 0 - Top
		        0,    h2,    -s2/2,         // Point 1 - Front
		        -s2/2, h2,    0,            // Point 2 - Left
		        s2/2,  h2,    0,            // Point 3 - Right
		        0,    h2,    s2/2           // Point 4 - Back
		    );
		// Faces
		smallPyr.getFaces().addAll(
		        0,0,  2,1,  1,2,          // Front left face
		        0,0,  1,1,  3,1,          // Front right face
		        0,0,  3,1,  4,2,          // Back right face
		        0,0,  4,1,  2,2,          // Back left face
		        4,1,  1,4,  2,2,          // Bottom left face
		        4,1,  3,3,  1,4           // Bottom right face
		    ); 
		smallPyr.getFaceSmoothingGroups().addAll(
				1, 2, 3, 4, 5, 5);
		MeshView small_mtn = new MeshView(smallPyr);
		final PhongMaterial lightMtn = new PhongMaterial();
		lightMtn.setDiffuseMap(mtn3);
		lightMtn.setSpecularColor(Color.WHITE);
		small_mtn.setMaterial(lightMtn);
		small_mtn.setTranslateX(200);
		small_mtn.setTranslateY(-90);
		small_mtn.setTranslateZ(4000);
		root.getChildren().add(small_mtn);
		
		// Define another small mountain of a different texture
		MeshView sml_mtn2 = new MeshView(smallPyr);
		//pyramid.setDrawMode(DrawMode.LINE);
		final PhongMaterial rockMat = new PhongMaterial();
		rockMat.setDiffuseMap(mtn4);
		rockMat.setSpecularColor(Color.WHITE);
		sml_mtn2.setMaterial(rockMat);
		sml_mtn2.setTranslateX(-240);
		sml_mtn2.setTranslateY(-90);
		sml_mtn2.setTranslateZ(3800);
		root.getChildren().add(sml_mtn2);
		
		car = new Car("Car 1", 0.0, -50.0, -700.0, root); 
		
		for(int i = 0; i <= 29; i++) {
			Random rnd = new Random();
			int randX = rnd.nextInt(10000);
			int randZ = rnd.nextInt(10000);
			if(randX%2 == 0 && randZ%2 != 0)
				a[i] = new alien("alien "+ i , randX , 0, randZ, root);
			else if (randX%2 != 0 && randZ%2 == 0)
				a[i] = new alien("alien "+ i , -randX , 0, randZ, root);
			else if(randX%2 != 0 && randZ%2 != 0)
				a[i] = new alien("alien "+ i , randX , 0, -randZ, root);
			else
				a[i] = new alien("alien "+ i , -randX , 0, -randZ, root);
		}
		
		// Add the main platform "xAxis"
		root.getChildren().addAll(xAxis);

		root.getChildren().addAll(sphere);

	}
	
	/**
	 *  Update variables for one time step
	 */
	double i = 1;
	int kills = 0;
	public boolean increase = true;
	boolean set = true, east, rise, west;
	int sun_z = -1000;
	public void update() {
		car.update();
		
		for(int i = 0; i <= 29; i++) {

			if(a[i].collisionBox().intersects(car.collisionBox()) && !a[i].isHit() && !a[i].exclude && kills < 30) {
				a[i].exclude = true;
				kills++;
				System.out.println("ALIEN " + i + " HIT!");
				System.out.println("Kills: " + kills + " / 30");
				a[i].kill();
			}
			else if(kills == 30) {
				System.out.println("-----------------\nThey're all dead!\nYou Win!");
				System.exit(0);
			}
		}
		
		// Move sun down and back until it hits lower limit
		if(set && sphere_y < 100) {
			sphere.setTranslateY(sphere_y);
			sphere_y++;
		}
		
		// Bring sun to east if it is down
		else if(!rise && !east && !west && sphere_y >= 100) {
			set = false;
			east = true;
		}
		// One-time transport sun to the east for rising.
		if(east && !set && !rise && !west) {
	    	sphere.setTranslateZ(sun_z);
	    	east = false;
	    	rise = true; 
		}
	    
	    // Sun must return at 0, -610, 1000 (starting point) for one cycle.
	    if(rise) {
	    	if(sphere_y > -610) {
				sphere_y--;
				sphere.setTranslateY(sphere_y);
	    	}
			else if(sphere_y <= 610) {
		    	rise = false;
		    	west = true;
			}
	    }
	    
	    // Sun will move west... long day!
	    if(west) {
	    	if(sun_z < 1000) {
		    	sphere.setTranslateZ(sun_z);
		    	sun_z++;
	    	}
	    	else if(sun_z >= 1000) {
		    	west = false;
		    	set = true;			// Set sun in the west again
		    	sun_z = -1000;		// Reset temporary variable
	    	}
	    }
		
	    // For breathing animation of the sun
		if(i < 1.5 && increase) {
			sphere.setScaleX(i);
			sphere.setScaleY(i);
			sphere.setScaleZ(i);
			i += 0.01;
			if(i >= 1.5)
				increase = false;
		}
		
		// Decrease sun scale
		else if(!increase){
			sphere.setScaleX(i);
			sphere.setScaleY(i);
			sphere.setScaleZ(i);
			i -= 0.01;
			if(i <= 1)
				increase = true;
		}
		
		cameraDolly.setTranslateX(car.getX());
		cameraDolly.setTranslateY(car.getY() - 50);
		cameraDolly.setTranslateZ(car.getZ() - 450);
		
		if(car.getZ() <= -10000 || car.getZ() >= 10000 || car.getX() <= -10000 || car.getX() >= 10000 || car.getY() > -50) {
			car.y+=15;
		}
		if(car.getY() > 1500) {
			System.out.println("You fell off the edge! You lose!");
			System.exit(0);
		}
		
		
	}

	@Override
	public void start(Stage primaryStage) {

		// Build your Scene and Camera
		Group sceneRoot = new Group();
		constructWorld(sceneRoot);

		// Fourth parameter to indicate 3D world:
		Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true);
		ImagePattern pattern = new ImagePattern(sunset);
		scene.setFill(pattern);
		
		camera = new PerspectiveCamera(true);
		camera.setNearClip(0.1);
		camera.setFarClip(10000.0);
		scene.setCamera(camera);
		// translations through dolly
		cameraDolly = new Group();
		cameraDolly.setTranslateX(car.getX());
		cameraDolly.getChildren().add(camera);
		Point3D cam_r = new Point3D(0, car.getX(), 0);
		cameraDolly.setRotationAxis(cam_r);
		cameraDolly.setRotate(car.getAngle());
		
		sceneRoot.getChildren().add(cameraDolly);
		// rotation transforms

		Rotate xRotate = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
		Rotate yRotate = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
		camera.getTransforms().addAll(xRotate, yRotate);
		
		// Use keyboard to control camera position
		scene.setOnKeyPressed(event -> {
			// What key did the user press?
			KeyCode keycode = event.getCode();

			if (keycode == KeyCode.A) {
				car.left();
				left_cam = true;
			}
			if (keycode == KeyCode.D) {
				car.right();
				right_cam = true;
			}
			if (keycode == KeyCode.SHIFT) {
				speedboost = true;
			}
			if (keycode == KeyCode.W) {
					car.forward();
			}
			if (keycode == KeyCode.S) {
				car.backward();
			}
			
		});

		// Use mouse to control camera rotation
		scene.setOnMousePressed(me -> {
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
		});

		scene.setOnMouseDragged(me -> {
			mouseOldX = mousePosX;
			mouseOldY = mousePosY;
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
			mouseDeltaX = (mousePosX - mouseOldX);
			mouseDeltaY = (mousePosY - mouseOldY);

			yRotate.setAngle(((yRotate.getAngle() - mouseDeltaX * 0.2) % 360 + 540) % 360 - 180); // +
			xRotate.setAngle(((xRotate.getAngle() + mouseDeltaY * 0.2) % 360 + 540) % 360 - 180); // -
		});
		
		scene.setOnKeyReleased(me -> {
			KeyCode c = me.getCode();
			if (c == KeyCode.W || c == KeyCode.S) {
				car.stop();
			}
			if(c == KeyCode.A || c == KeyCode.D) {
				left_cam = false;
				right_cam = false;
			}
			if(c == KeyCode.SHIFT) {
				speedboost = false;
			}
		});

		// Setup and start animation loop (Timeline)
		KeyFrame kf = new KeyFrame(Duration.millis(1000 / FPS),
				e -> {
					// update position
					update();
				}
			);
		Timeline mainLoop = new Timeline(kf);
		mainLoop.setCycleCount(Animation.INDEFINITE);
		mainLoop.play();
		
		primaryStage.setTitle("Cool Car!");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}