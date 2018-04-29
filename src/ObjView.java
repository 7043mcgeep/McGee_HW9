import java.io.IOException;

import javafx.scene.Group;

public class ObjView {

	//Code to use ObjImporter:
	final Group res = new Group();

    public void load(String fileUrl) throws IOException {
	    ObjImporter reader = new ObjImporter(fileUrl);
	    for (String mesh : reader.getMeshes())
	    	res.getChildren().add(reader.buildMeshView(mesh));
    }

    public Group getRoot() {
    	return res;
    }
	
}
