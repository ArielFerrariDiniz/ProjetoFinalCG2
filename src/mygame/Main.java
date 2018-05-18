package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Spatial;

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

    @Override
    public void simpleInitApp() {
        
        
        center();
        CreateDart();
        createLigth();
        initKeys();
        
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
        Spatial dart = assetManager.loadModel("folder/dart.obj");
        dart.scale(0.02f);
        dart.rotate(0,0,0);
        dart.setLocalTranslation(-0.5f,-0.6f,+8);
        dart.setName("Dart");
        rootNode.attachChild(dart);
    }
    
    private void initKeys() {
        inputManager.addMapping("Tiro", new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(this, "Tiro");
    }
    @Override
    public void simpleUpdate(float tpf) {
        if(tiro){
            rootNode.getChild("Dart").move(0,0,-0.05f);
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
