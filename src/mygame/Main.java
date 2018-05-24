package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.Random;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ActionListener{

    public static void main(String[] args) {
        Main app = new Main();
        app.showSettings = false;
        app.start();
    }
    
    private boolean tiro=false;
    private ArrayList<Spatial> alvos = new ArrayList<>();

    @Override
    public void simpleInitApp() {
        
        Box box = new Box(25, 15, 0);
        Geometry geom = new Geometry("Box", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Texture/Madeira.jpg");
        mat.setTexture("ColorMap", t);
        geom.setMaterial(mat);
        rootNode.attachChild(geom); 
        
        rootNode.getChild("Box").setLocalTranslation(0, 0, -40);
        
        
        center();
        CreateDart();
        createLigth();
        initKeys();
        CreateAlvo();
        
    }
    
    private void createLigth() {

        DirectionalLight l1 = new DirectionalLight();
        l1.setDirection(new Vector3f(1, -0.7f, 0));
        rootNode.addLight(l1);

        DirectionalLight l2 = new DirectionalLight();
        l2.setDirection(new Vector3f(-1, 0, 0));
        rootNode.addLight(l2);

        DirectionalLight l3 = new DirectionalLight();
        l3.setDirection(new Vector3f(0, 0, -1.0f));
        rootNode.addLight(l3);

        DirectionalLight l4 = new DirectionalLight();
        l4.setDirection(new Vector3f(0, 0, 1.0f));
        rootNode.addLight(l4);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);

    }
    public void center(){
        
        guiNode.detachAllChildren();
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText center = new BitmapText(guiFont, false);
        
        center.setSize(guiFont.getCharSet().getRenderedSize());
        center.setText("+");
        center.setLocalTranslation(
                    settings.getWidth()/2 - guiFont.getCharSet().getRenderedSize()/2,
                    settings.getHeight()/2 + center.getLineHeight()/2, 0);
        
        guiNode.attachChild(center);
    }
    public void CreateDart()
    {
        Spatial dart = assetManager.loadModel("Dart/dart.obj");
        dart.scale(0.02f);
        dart.rotate(0,0,0);
        dart.setLocalTranslation(-0.5f,-0.6f,+8);
        dart.setName("Dart");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Dart/Untitled picture.png");
        mat.setTexture("ColorMap", t);
        dart.setMaterial(mat);
        //mat.setColor("Color", ColorRGBA.Blue);
        dart.setMaterial(mat);
        rootNode.attachChild(dart);
    }
    
    public void CreateAlvo()
    {
        Random r = new Random();
        int posX = r.nextInt(12);
        int altera = r.nextInt(12);
        int posY = r.nextInt(7);
        int altera1 = r.nextInt(7);
        
        
        Spatial dartt = assetManager.loadModel("Target/Target.obj");
        dartt.scale(0.005f);
        dartt.rotate(0,0,0);
        dartt.setLocalTranslation(posX - altera, posY - altera1, -20);
        dartt.setName("Target");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Target/Target.jpg");
        mat.setTexture("ColorMap", t);
        //mat.setColor("Color", ColorRGBA.Blue);
        dartt.setMaterial(mat);
        rootNode.attachChild(dartt);
        
        alvos.add(dartt);
        
        
    }
    
    private void initKeys() {
        inputManager.addMapping("Tiro", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(this, "Tiro");
    }
    @Override
    public void simpleUpdate(float tpf) {
        if(tiro){
            rootNode.getChild("Dart").move(0,0,tpf*-5f);
        }
            
    }
    

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if(isPressed && name.equals("Tiro"))
            tiro = true;
        
    }
}
