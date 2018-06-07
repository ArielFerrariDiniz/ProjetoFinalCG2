package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements PhysicsCollisionListener, ActionListener {

    public static void main(String[] args) {
        Main app = new Main();
        app.showSettings = false;
        app.start();
    }

    private boolean tiro = false;
    private boolean Up = false;
    private boolean Down = false;
    private boolean Left = false;
    private boolean Right = false;
    private Node pai = new Node();
    private BulletAppState state;
    private RigidBodyControl wallRigidBody;
    private RigidBodyControl targetRigidBody;
    private RigidBodyControl dartRigidBody;

    @Override
    public void simpleInitApp() {

        criarFisica();
        createWall();
        center();
        CreateDart();
        createLigth();
        initKeys();
        CreateCam();
    }
private void CreateCam()
{
    CameraNode camNode = new CameraNode("CamNode", cam);
         camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
         camNode.setLocalTranslation(new Vector3f(0,0,0));

        camNode.lookAt(pai.getChild("Dart").getLocalTranslation(), Vector3f.UNIT_Y);
        pai.attachChild(camNode);
        //pai.getChild("CamNode").rotate(FastMath.PI,0.1f,0);
        pai.getChild("CamNode").rotate(0.1f,FastMath.PI,0);
        pai.getChild("CamNode").setLocalTranslation(0, 0, 20);
    
}
    private void criarFisica() {
        state = new BulletAppState();
        state.setDebugEnabled(true);
        stateManager.attach(state);
        state.getPhysicsSpace().addCollisionListener(this);
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

    private void createWall() {
        Box box = new Box(20, 14, 0);
        Geometry geom = new Geometry("Wall", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Texture/Madeira.jpg");
        mat.setTexture("ColorMap", t);
        geom.setMaterial(mat);

        wallRigidBody = new RigidBodyControl(0);
        geom.addControl(wallRigidBody);
        state.getPhysicsSpace().add(wallRigidBody);
        rootNode.attachChild(geom);

        geom.setLocalTranslation(0, 0, -5);
        wallRigidBody.setPhysicsLocation(geom.getLocalTranslation());

    }

    public void center() {

        guiNode.detachAllChildren();

        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText center = new BitmapText(guiFont, false);

        center.setSize(guiFont.getCharSet().getRenderedSize());
        center.setText("+");
        center.setLocalTranslation(
                settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize() / 2,
                settings.getHeight() / 2 + center.getLineHeight() / 2, 0);

        guiNode.attachChild(center);
    }

    public void CreateDart() {

        Spatial dart = assetManager.loadModel("Dart/dart.obj");
        dart.scale(0.02f);
        dart.rotate(0, 0, 0);
        dart.setLocalTranslation(-0.5f, -0.6f, +8);
        dart.setName("Dart");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Dart/Untitled picture.png");
        mat.setTexture("ColorMap", t);
        dart.setMaterial(mat);
        //mat.setColor("Color", ColorRGBA.Blue);
        dart.setMaterial(mat);

        dartRigidBody = new RigidBodyControl(0);
        dart.addControl(dartRigidBody);
        state.getPhysicsSpace().add(dartRigidBody);
        rootNode.attachChild(pai);
        pai.attachChild(dart);

        dartRigidBody.setPhysicsLocation(dart.getLocalTranslation());

        

    }

    public void CreateAlvo() {
        Random r = new Random();
        int posX = r.nextInt(9);
        int altera = r.nextInt(9);
        int posY = r.nextInt(5);
        int altera1 = r.nextInt(5);

        Box box = new Box(1, 1, 0);
        Geometry geom = new Geometry("Target", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Target/Target.jpg");
        mat.setTexture("ColorMap", t);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        rootNode.getChild("Target").setLocalTranslation(posX - altera, posY - altera1, -4.9f);

        /*Spatial dartt = assetManager.loadModel("Target/Target.obj");
        dartt.scale(0.005f);
        dartt.rotate(0,0,0);
        dartt.setLocalTranslation(posX - altera, posY - altera1, -20);
        dartt.setName("Target");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Target/Target.jpg");
        mat.setTexture("ColorMap", t);
        //mat.setColor("Color", ColorRGBA.Blue);
        dartt.setMaterial(mat);
        rootNode.attachChild(dartt);*/
    }

    private void initKeys() {
        inputManager.addMapping("Tiro", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(this, "Tiro");

        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_T));

        inputManager.addListener(this, "Up");

        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_G));

        inputManager.addListener(this, "Down");

        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_F));

        inputManager.addListener(this, "Left");

        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_H));

        inputManager.addListener(this, "Right");
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (tiro) {
            rootNode.getChild("Dart").move(0, 0, tpf * -35f);
            dartRigidBody.setPhysicsLocation(rootNode.getChild("Dart").getLocalTranslation());
            if (rootNode.getChild("Dart").getLocalTranslation().z < -30) {
                rootNode.getChild("Dart").setName(null);
                tiro = false;
                CreateDart();
            }
        }
        
        pai.getChild("CamNode").lookAt(pai.getChild("Dart").getLocalTranslation(), Vector3f.UNIT_Y);
        
        if(Up)
            {
                rootNode.getChild("Dart").move(0,0.005f,0);
            }

         if(Down)
            {
                rootNode.getChild("Dart").move(0, -0.005f,0);
            }
          if(Right)
            {
                rootNode.getChild("Dart").move(0.005f,0,0);
            }
           if(Left)
            {
                rootNode.getChild("Dart").move(-0.005f,0,0);
            }
           
       

        com.jme3.system.Timer tempo = getTimer();
        if (tempo.getTimeInSeconds() > 3) {
            rootNode.detachChildNamed("Target");
            CreateAlvo();
            tempo.reset();
        }

    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed && name.equals("Tiro")) {
            tiro = true;
        }
        if (!isPressed && name.equals("Up")) {
            Up = false;
        }
        if (isPressed && name.equals("Up")) {
            Up = true;
             
        }
        if (isPressed && name.equals("Down")) {
            Down = true;
        }
       if (!isPressed && name.equals("Down")) {
            Down = false;
        }
        if (isPressed && name.equals("Left")) {
            Left = true;
        }
         if (!isPressed && name.equals("Left")) {
            Left = false;
        }
        if (isPressed && name.equals("Right")) {
            Right = true;
        }
         if (!isPressed && name.equals("Right")) {
            Right = false;
        }

    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        if (event.getNodeA().getName().equals("Wall") && event.getNodeB().getName().equals("Dart") || event.getNodeA().getName().equals("Dart") && event.getNodeB().getName().equals("Wall")) {
            rootNode.detachChildNamed("Dart");
            CreateDart();
        }
    }
}




/* while (Up) {

            rootNode.getChild("Dart").setLocalTranslation(rootNode.getChild("Dart").getLocalTranslation().x, rootNode.getChild("Dart").getLocalTranslation().y + 0.01f, rootNode.getChild("Dart").getLocalTranslation().z);
         
        }

        while (Down) {

            rootNode.getChild("Dart").setLocalTranslation(rootNode.getChild("Dart").getLocalTranslation().x, rootNode.getChild("Dart").getLocalTranslation().y - 0.01f, rootNode.getChild("Dart").getLocalTranslation().z);
            
        }

        while (Left) {

            rootNode.getChild("Dart").setLocalTranslation(rootNode.getChild("Dart").getLocalTranslation().x + 0.01f, rootNode.getChild("Dart").getLocalTranslation().y, rootNode.getChild("Dart").getLocalTranslation().z);
            
        }

        while (Right) {

            rootNode.getChild("Dart").setLocalTranslation(rootNode.getChild("Dart").getLocalTranslation().x - 0.01f, rootNode.getChild("Dart").getLocalTranslation().y, rootNode.getChild("Dart").getLocalTranslation().z);
          
        }*/