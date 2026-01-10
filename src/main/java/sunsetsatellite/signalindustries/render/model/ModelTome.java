package sunsetsatellite.signalindustries.render.model;

import net.minecraft.client.render.model.Cube;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.core.util.helper.MathHelper;

public class ModelTome extends ModelBase {

    public Cube coverRight = new Cube(0,0);
    public Cube coverLeft = new Cube(16,0); ;
    public Cube pagesRight = new Cube(0,10); ;
    public Cube pagesLeft = new Cube(12,10); ;
    public Cube spine = new Cube(12,0); ;
    public Cube flippingRight = new Cube(24,10); ;
    public Cube flippingLeft = new Cube(24,10); ;


    public ModelTome() {
        coverRight.addBox(-6,-5,0,6,10,0);
        coverLeft.addBox(0,-5,0,6,10,0);
        pagesRight.addBox(0,-4,-0.99f,5,8,1);
        pagesLeft.addBox(0,-4,-0.01f,5,8,1);
        flippingRight.addBox(0,-4,0,5,8,0);
        flippingLeft.addBox(0,-4,0,5,8,0);
        spine.addBox(-1,-5,0,2,10,0);

        coverRight.setRotationPoint(0,0,-1);
        coverLeft.setRotationPoint(0,0,1);
        spine.yRot = (float) (Math.PI/2f);
    }

    @Override
    public void render(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale) {
        setRotation(limbSwing, limbYaw, limbPitch, headYaw, headPitch, scale);
        coverRight.render(scale);
        coverLeft.render(scale);
        spine.render(scale);
        pagesRight.render(scale);
        pagesLeft.render(scale);
        flippingRight.render(scale);
        flippingLeft.render(scale);
    }

    public void setRotation(float limbSwing, float limbYaw, float limbPitch, float headYaw, float headPitch, float scale){
        float rot = (MathHelper.sin(limbSwing * 0.02f) * 0.1f + 1.25f) * headYaw;
        coverRight.yRot = (float) (Math.PI + rot);
        coverLeft.yRot = -rot;
        pagesRight.yRot = rot;
        pagesLeft.yRot = -rot;
        flippingRight.yRot = rot - rot*2f * limbYaw;
        flippingLeft.yRot = rot + rot*2f * limbPitch;
        pagesRight.x = MathHelper.sin(rot);
        pagesLeft.x = MathHelper.sin(rot);
        flippingRight.x = MathHelper.sin(rot);
        flippingLeft.x = MathHelper.sin(rot);
    }
}
