package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.util.MeteorLocation;

import java.awt.image.BufferedImage;

import static java.lang.Math.PI;

public class DynamicTextureMeteorTracker extends DynamicTexture {

    private final Minecraft mc;

    private byte[] compassImageData;

    private double angleFinal;
    private double delta;

    private double scaleFactor;

    public DynamicTextureMeteorTracker(Minecraft minecraft, IconCoordinate iconCoordinate) {
        super(iconCoordinate);
        this.mc = minecraft;
    }

    @Override
    public void postInit() {
        initTexture();

        BufferedImage atlas = targetTexture.parentAtlas.atlas;
        compassImageData = new byte[targetTexture.getArea() * 4];

        for (int x = 0; x < targetTexture.width; x++) {
            for (int y = 0; y < targetTexture.height; y++) {
                putPixel(compassImageData, y * targetTexture.width + x, atlas.getRGB(targetTexture.iconX + x, targetTexture.iconY + y));
            }
        }

        scaleFactor = targetTexture.width / 16.0;
    }

    @Override
    public boolean runUpdates(boolean isPaused) {
        return !isPaused;
    }

    @Override
    public void update() {

        for (int _x = 0; _x < targetTexture.width; _x++) {
            for (int _y = 0; _y < targetTexture.height; _y++) {
                int i = _y * targetTexture.width + _x;

                int a = this.compassImageData[i * 4 + 3] & 0xFF;
                int r = this.compassImageData[i * 4] & 0xFF;
                int g = this.compassImageData[i * 4 + 1] & 0xFF;
                int b = this.compassImageData[i * 4 + 2] & 0xFF;

                this.imageData[i * 4] = (byte) r;
                this.imageData[i * 4 + 1] = (byte) g;
                this.imageData[i * 4 + 2] = (byte) b;
                this.imageData[i * 4 + 3] = (byte) a;
            }
        }

        double angle = 0.0D;
        if (this.mc.currentWorld != null && this.mc.thePlayer != null) {
            Vec3i chunk = null;
            double distance = Double.MAX_VALUE;
            for (MeteorLocation meteorLocation : SignalIndustries.meteorLocations) {
                Vec3i location = meteorLocation.location;
                if (location.getSqDistanceTo((int) this.mc.thePlayer.x, (int) this.mc.thePlayer.y, (int) this.mc.thePlayer.z) < distance) {
                    distance = location.getSqDistanceTo((int) this.mc.thePlayer.x, (int) this.mc.thePlayer.y, (int) this.mc.thePlayer.z);
                    chunk = location;
                }
            }
            if (chunk != null) {
                double var23 = (double) chunk.x - this.mc.thePlayer.x;
                double var25 = (double) chunk.z - this.mc.thePlayer.z;
                angle = (double) (this.mc.thePlayer.yRot - 90.0F) * PI / 180.0D - Math.atan2(var25, var23);
            } else {
                return;
            }
        }

        double angleSmooth;
        angleSmooth = angle - this.angleFinal;
        while (angleSmooth < -PI) {
            angleSmooth += 2 * PI;
        }

        while (angleSmooth >= PI) {
            angleSmooth -= 2 * PI;
        }

        if (angleSmooth < -1.0D) {
            angleSmooth = -1.0D;
        }

        if (angleSmooth > 1.0D) {
            angleSmooth = 1.0D;
        }

        this.delta += angleSmooth * 0.1D;
        this.delta *= 0.8D;
        this.angleFinal += this.delta;

        double x = Math.sin(this.angleFinal);
        double y = Math.cos(this.angleFinal);

        int x2;
        int y2;
        int j;
        int r;
        int g;
        int b;
        int a;

        double xs = (targetTexture.width / 2.0) + 0.5;
        double ys = (targetTexture.height / 2.0) - 0.5;

        for (int i = (int) (-4 * scaleFactor); i <= (int) (4 * scaleFactor); ++i) {
            x2 = (int) (xs + y * (double) i * 0.3D);
            y2 = (int) (ys - x * (double) i * 0.3D * 0.5D);
            j = y2 * targetTexture.width + x2;
            r = 100;
            g = 100;
            b = 100;
            a = 255;

            this.imageData[j * 4] = (byte) r;
            this.imageData[j * 4 + 1] = (byte) g;
            this.imageData[j * 4 + 2] = (byte) b;
            this.imageData[j * 4 + 3] = (byte) a;
        }

        for (int i = (int) (-8 * scaleFactor); i <= (int) (16 * scaleFactor); ++i) {
            x2 = (int) (xs + x * (double) i * 0.3D);
            y2 = (int) (ys + y * (double) i * 0.3D * 0.5D);
            j = y2 * targetTexture.width + x2;
            r = i >= 0 ? 255 : 100;
            g = i >= 0 ? 20 : 100;
            b = i >= 0 ? 20 : 100;
            a = 255;

            this.imageData[j * 4] = (byte) r;
            this.imageData[j * 4 + 1] = (byte) g;
            this.imageData[j * 4 + 2] = (byte) b;
            this.imageData[j * 4 + 3] = (byte) a;
        }
    }
}
