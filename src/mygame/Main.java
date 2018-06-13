package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
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
import java.util.Random;
import javafx.scene.text.Text;


public class Main extends SimpleApplication implements PhysicsCollisionListener, ActionListener{
    
    private BulletAppState state;
    private RigidBodyControl wallRigidBody;
    private RigidBodyControl dartRigidBody;
    private RigidBodyControl targetRigidBody;
    private BetterCharacterControl physicsDart;
    private boolean tiro = false;
    private boolean Up = false;
    private boolean Down = false;
    private boolean Left = false;
    private boolean Right = false;
    private boolean first = true;
    private int time = 4;
    private int quantidade = 1;
    private int points = 10;
    private Spatial Target1, Target;
    private BitmapText texto ;
    private  BitmapText pontos;
    private boolean b = true;
   

    public static void main(String[] args) {
        Main app = new Main();
        app.showSettings = false;
        app.start();
    }
   
    @Override
    public void simpleInitApp() {
        createLigth();
        criarFisica();
        initKeys();
        center();
        createWall();
        CreateDart(-0.48f, -0.65f, +8);
        CreateAlvo("Target", 19, 19, 13, 13);
        createFundo();
        
        
        
        flyCam.setEnabled(paused);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(b){
        texto = new BitmapText(guiFont,false);
         pontos = new BitmapText(guiFont,false);
         b=false;
        }
         rootNode.attachChild(texto);
      rootNode.attachChild(pontos);
    texto.setName("texto");
    pontos.setName("pontos");
    texto.setSize(5);      // font size
    pontos.setSize(5); 
    texto.setColor(ColorRGBA.Black); 
    pontos.setColor(ColorRGBA.Black);    // font color
    texto.setText("Points : ");     
     pontos.setText(""+points);// the text
     if(rootNode.getChild("Dart").getLocalTranslation().y >-2)
     {
    texto.setLocalTranslation(-10f,20f, -34f); // position
    pontos.setLocalTranslation(10f, 20f,-34f);
     }
     else
     {
         texto.setLocalTranslation(-10f,-15f, -34f); // position
        pontos.setLocalTranslation(10f, -15f,-34f);
     }
      
      
      
            
      
      
        
        dartRigidBody.activate();
        wallRigidBody.activate();
        targetRigidBody.activate();
        System.out.println(points);
        if (tiro)
            rootNode.getChild("Dart").move(0, 0, tpf * -45f);
        
        if(Up && !tiro && rootNode.getChild("Dart").getLocalTranslation().y < 20)
            rootNode.getChild("Dart").move(0,tpf*15f,0);
        
        if(Down && !tiro && rootNode.getChild("Dart").getLocalTranslation().y > -20)
            rootNode.getChild("Dart").move(0, tpf*-15f,0);

        if(Right && !tiro && rootNode.getChild("Dart").getLocalTranslation().x < 25)
            rootNode.getChild("Dart").move(tpf*15f,0,0);

        if(Left && !tiro && rootNode.getChild("Dart").getLocalTranslation().x > -25)
            rootNode.getChild("Dart").move(tpf*-15f,0,0);
        
        dartRigidBody.setPhysicsLocation(rootNode.getChild("Dart").getLocalTranslation());
        
        Vector3f v = new Vector3f(rootNode.getChild("Dart").getLocalTranslation().x+0.48f, rootNode.getChild("Dart").getLocalTranslation().y+0.65f, 10);
        cam.setLocation(v);
        
        if(points <= 50){
            time = 4;
            quantidade = 1;
        }
        if(points > 50 && points <= 100){
            time = 3;
            quantidade = 1;
        }
        if(points > 100){
            time = 5;
            quantidade = 2;
        }
        
        com.jme3.system.Timer tempo = getTimer();
        if (tempo.getTimeInSeconds() > time && quantidade == 1) {
            RigidBodyControl r = rootNode.getChild("Target").getControl(RigidBodyControl.class);
            state.getPhysicsSpace().remove(r);
            rootNode.detachChildNamed("Target");
            
            CreateAlvo("Target", 19, 19, 13, 13);
            if(points > 0)
                 
                    points -= 5;
            tempo.reset();
            first = true;
        }
        if (tempo.getTimeInSeconds() > time && quantidade == 2) {
            RigidBodyControl r = rootNode.getChild("Target").getControl(RigidBodyControl.class);
            state.getPhysicsSpace().remove(r);
            rootNode.detachChildNamed("Target");
             
            if(!first){
                RigidBodyControl r1 = rootNode.getChild("Target1").getControl(RigidBodyControl.class);
                state.getPhysicsSpace().remove(r1);
                rootNode.detachChildNamed("Target1");
            }
            else
                first = false;
            CreateAlvo("Target", 19, 1, 13, 1);
            CreateAlvo("Target1", 1, 19, 1, 13);
            if(points > 0)
                    points -= 10;
             
            tempo.reset();
        }
        
        if(rootNode.hasChild(Target1) && quantidade == 1){
            RigidBodyControl r = rootNode.getChild("Target1").getControl(RigidBodyControl.class);
            state.getPhysicsSpace().remove(r);
            rootNode.detachChildNamed("Target1");
            if(points > 0)
                points -= 5;
             
        }
        
        if(!rootNode.hasChild(Target1) && quantidade == 2)
            CreateAlvo("Target1", 1, 19, 1, 13);
        
        if(rootNode.getChild("Dart").getLocalTranslation().z < -35){
            RigidBodyControl r = rootNode.getChild("Dart").getControl(RigidBodyControl.class);
            state.getPhysicsSpace().remove(r);
            float x = rootNode.getChild("Dart").getLocalTranslation().x;
            float y = rootNode.getChild("Dart").getLocalTranslation().y;
            rootNode.detachChildNamed("Dart");
            if(points > 0)
                points -= 5;
             
            CreateDart(x, y, 8);
        }
        
        if(rootNode.getChild("Dart").getLocalTranslation().z < rootNode.getChild("Target").getLocalTranslation().z && 
                rootNode.getChild("Dart").getLocalTranslation().x+0.48 <= rootNode.getChild("Target").getLocalTranslation().x+1 &&
                rootNode.getChild("Dart").getLocalTranslation().x+0.48 >= rootNode.getChild("Target").getLocalTranslation().x-1 &&
                rootNode.getChild("Dart").getLocalTranslation().y+0.65 <= rootNode.getChild("Target").getLocalTranslation().y+1 &&
                rootNode.getChild("Dart").getLocalTranslation().y+0.65 >= rootNode.getChild("Target").getLocalTranslation().y-1)
        {
            System.out.println("Dentroooooooo");
            points += 10;
             
            RigidBodyControl r = rootNode.getChild("Dart").getControl(RigidBodyControl.class);
            state.getPhysicsSpace().remove(r);
            float x = rootNode.getChild("Dart").getLocalTranslation().x;
            float y = rootNode.getChild("Dart").getLocalTranslation().y;
            rootNode.detachChildNamed("Dart");
            r = rootNode.getChild("Target").getControl(RigidBodyControl.class);
            state.getPhysicsSpace().remove(r);
            rootNode.detachChildNamed("Target");
            if(quantidade == 1)
                CreateAlvo("Target", 19, 19, 13, 13);
            else
                CreateAlvo("Target", 19, 1, 13, 1);
            if(rootNode.hasChild(Target1)){
                r = rootNode.getChild("Target1").getControl(RigidBodyControl.class);
                state.getPhysicsSpace().remove(r);
                rootNode.detachChildNamed("Target1");
                CreateAlvo("Target1", 1, 19, 1, 13);
            }
            CreateDart(x, y, 8);
            tempo.reset();
        }
        else if(quantidade == 2){
            if(rootNode.getChild("Dart").getLocalTranslation().z < rootNode.getChild("Target1").getLocalTranslation().z && 
                    rootNode.getChild("Dart").getLocalTranslation().x+0.48 <= rootNode.getChild("Target1").getLocalTranslation().x+1 &&
                    rootNode.getChild("Dart").getLocalTranslation().x+0.48 >= rootNode.getChild("Target1").getLocalTranslation().x-1 &&
                    rootNode.getChild("Dart").getLocalTranslation().y+0.65 <= rootNode.getChild("Target1").getLocalTranslation().y+1 &&
                    rootNode.getChild("Dart").getLocalTranslation().y+0.65 >= rootNode.getChild("Target1").getLocalTranslation().y-1 && quantidade == 2)
            {
                System.out.println("Dentroooooooo");
                 
                points += 10;
                RigidBodyControl r = rootNode.getChild("Dart").getControl(RigidBodyControl.class);
                state.getPhysicsSpace().remove(r);
                float x = rootNode.getChild("Dart").getLocalTranslation().x;
                float y = rootNode.getChild("Dart").getLocalTranslation().y;
                rootNode.detachChildNamed("Dart");
                r = rootNode.getChild("Target1").getControl(RigidBodyControl.class);
                state.getPhysicsSpace().remove(r);
                rootNode.detachChildNamed("Target1");
                CreateAlvo("Target1", 1, 19, 1, 13);
                if(rootNode.hasChild(Target)){
                    r = rootNode.getChild("Target").getControl(RigidBodyControl.class);
                    state.getPhysicsSpace().remove(r);
                    rootNode.detachChildNamed("Target");
                    CreateAlvo("Target", 19, 1, 13, 1);
                }
                CreateDart(x, y, 8);
                tempo.reset();
            }
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
        System.out.println(event.getNodeA().getName());
        if (event.getNodeA().getName().equals("Dart") || event.getNodeB().getName().equals("Dart")){
            if(event.getNodeA().getName().equals("Target") || event.getNodeB().getName().equals("Target")){
                RigidBodyControl r = rootNode.getChild("Dart").getControl(RigidBodyControl.class);
                state.getPhysicsSpace().remove(r);
                float x = rootNode.getChild("Dart").getLocalTranslation().x;
                float y = rootNode.getChild("Dart").getLocalTranslation().y;
                rootNode.detachChildNamed("Dart");
                r = rootNode.getChild("Target").getControl(RigidBodyControl.class);
                state.getPhysicsSpace().remove(r);
                rootNode.detachChildNamed("Target");
                
                points += 10;
                CreateAlvo("Target", 19, 19, 13, 13);
                CreateDart(x, y, 8);
            }
            if(event.getNodeA().getName().equals("Wall") || event.getNodeB().getName().equals("Wall")){
                RigidBodyControl r = rootNode.getChild("Dart").getControl(RigidBodyControl.class);
                state.getPhysicsSpace().remove(r);
                float x = rootNode.getChild("Dart").getLocalTranslation().x;
                float y = rootNode.getChild("Dart").getLocalTranslation().y;
                rootNode.detachChildNamed("Dart");
                if(points > 0)
                    points -= 5;
                
                CreateDart(x, y, 8);
            }
        }
    }
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed && name.equals("Tiro")) 
            tiro = true;

        if (!isPressed && name.equals("Up")) 
            Up = false;
        
        if (isPressed && name.equals("Up")) 
            Up = true;
             
        if (isPressed && name.equals("Down")) 
            Down = true;
        
        if (!isPressed && name.equals("Down")) 
            Down = false;
        
        if (isPressed && name.equals("Left")) 
            Left = true;
        
        if (!isPressed && name.equals("Left")) 
            Left = false;
        
        if (isPressed && name.equals("Right")) 
            Right = true;
        
        if (!isPressed && name.equals("Right")) 
            Right = false;    
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
    
    private void criarFisica() {
        state = new BulletAppState();
        stateManager.attach(state);
        //state.setDebugEnabled(true);
        state.getPhysicsSpace().addCollisionListener(this);
    }
    
    private void initKeys() {
        inputManager.addMapping("Tiro", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Tiro");
        
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addListener(this, "Up");

        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(this, "Down");

        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addListener(this, "Left");

        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addListener(this, "Right");
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
    
    private void createWall() {
        Box box = new Box(20, 14, 1);
        Geometry geom = new Geometry("Wall", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Texture/Madeira.jpg");
        mat.setTexture("ColorMap", t);
        geom.setMaterial(mat);
        geom.setLocalTranslation(0, 0, -35);
        
        wallRigidBody = new RigidBodyControl(0);
        geom.addControl(wallRigidBody);
        rootNode.attachChild(geom);
        state.getPhysicsSpace().add(wallRigidBody);
                
        wallRigidBody.setPhysicsLocation(geom.getLocalTranslation());
    
    }
        private void createFundo() {
        Box box = new Box(70, 70, 0);
        Geometry geom = new Geometry("Fundo", box);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Fundo/nuvem.jpg");
        mat.setTexture("ColorMap", t);
        geom.setMaterial(mat);
        geom.setLocalTranslation(0, 0, -40);
        
       
        rootNode.attachChild(geom);
     
    }
    
    public void CreateDart(float x, float y, float z) {
        Spatial dart = assetManager.loadModel("Dart/dart.obj");
        dart.setName("Dart");
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Dart/Untitled picture.png");
        mat.setTexture("ColorMap", t);
        dart.setMaterial(mat);
        dart.scale(0.02f);
        dart.setLocalTranslation(x, y, z);
        rootNode.attachChild(dart);
        
        dartRigidBody = new RigidBodyControl(CollisionShapeFactory.createMeshShape(dart), 0);
        
        dart.addControl(dartRigidBody);
        state.getPhysicsSpace().add(dartRigidBody);
        
        dart.getControl(RigidBodyControl.class).getCollisionShape().setScale(dart.getLocalScale());
        tiro = false;
    }
    
    public void CreateAlvo(String name, int x, int x1, int y, int y1) {
        Random r = new Random();
        int posX = r.nextInt(x);
        int altera = r.nextInt(x1);
        int posY = r.nextInt(y);
        int altera1 = r.nextInt(y1);

        Box box = new Box(1, 1, 1);
        Geometry geom = new Geometry(name, box);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture t = assetManager.loadTexture("Target/Target.jpg");
        mat.setTexture("ColorMap", t);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);

        rootNode.getChild(name).setLocalTranslation(posX - altera, posY - altera1, -34.8f);
        
        targetRigidBody = new RigidBodyControl(0);
        geom.addControl(targetRigidBody);
        rootNode.attachChild(geom);
        state.getPhysicsSpace().add(targetRigidBody);
                
        targetRigidBody.setPhysicsLocation(geom.getLocalTranslation());
        
        if(name.equals("Target1"))
            Target1 = geom;
        
        if(name.equals("Target"))
            Target = geom;
    }
}
