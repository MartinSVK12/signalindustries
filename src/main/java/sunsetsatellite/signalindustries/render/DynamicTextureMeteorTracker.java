package sunsetsatellite.signalindustries.render;

import static java.lang.Math.*;

import java.awt.image.BufferedImage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.core.Global;
import net.minecraft.core.util.helper.Color;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.item.Item;
import net.minecraft.client.util.helper.Textures;
import sunsetsatellite.signalindustries.SignalIndustries;

public class DynamicTextureMeteorTracker extends DynamicTexture {
	
	private Minecraft mc;
	
	private byte[] compassImageData;
	
	private double angleFinal;
	private double delta;
	
	private double scaleFactor;
	
	public DynamicTextureMeteorTracker(Minecraft minecraft, int resolution) {
		super(SignalIndustries.meteorTracker.getIconFromDamage(0), resolution, 1);
		
		this.mc = minecraft;

		compassImageData = new byte[resolution * resolution * 4];
		BufferedImage compass = mc.renderEngine.getImage("/assets/signalindustries/item/meteor_tracker.png");

		for(int x=0; x < resolution; x++) {
			for(int y=0; y < resolution; y++) {
				putPixel(compassImageData, y * resolution + x, compass.getRGB(x, y));
			}
		}
		
		scaleFactor = resolution / 16.0;
	}

	@Override
	public void update() {
		for(int i = 0; i < resolution * resolution; i++) {
			int a = this.compassImageData[i * 4 + 3] & 0xFF;
			int r = this.compassImageData[i * 4 + 0] & 0xFF;
			int g = this.compassImageData[i * 4 + 1] & 0xFF;
			int b = this.compassImageData[i * 4 + 2] & 0xFF;
			
			this.imageData[i * 4 + 0] = (byte)r;
			this.imageData[i * 4 + 1] = (byte)g;
			this.imageData[i * 4 + 2] = (byte)b;
			this.imageData[i * 4 + 3] = (byte)a;
		}
		
		double angle = 0.0D;
		if(this.mc.theWorld != null && this.mc.thePlayer != null) {
			ChunkCoordinates chunk = null;
			double distance = Double.MAX_VALUE;
			for (ChunkCoordinates meteorLocation : SignalIndustries.meteorLocations) {
				if(meteorLocation.getSqDistanceTo((int) this.mc.thePlayer.x, (int) this.mc.thePlayer.y, (int) this.mc.thePlayer.z) < distance){
					distance = meteorLocation.getSqDistanceTo((int) this.mc.thePlayer.x, (int) this.mc.thePlayer.y, (int) this.mc.thePlayer.z);
					chunk = meteorLocation;
				}
			}
			if(chunk != null){
				double var23 = (double)chunk.x - this.mc.thePlayer.x;
				double var25 = (double)chunk.z - this.mc.thePlayer.z;
				angle = (double)(this.mc.thePlayer.yRot - 90.0F) * PI / 180.0D - Math.atan2(var25, var23);
				if(this.mc.theWorld.dimension == Dimension.nether) {
					angle = Math.random() * PI * 2;
				}
			} else {
				return;
			}

		}

		double angleSmooth;
		for(angleSmooth = angle - this.angleFinal; angleSmooth < -PI; angleSmooth += 2 * PI) {
			;
		}

		while(angleSmooth >= PI) {
			angleSmooth -= 2 * PI;
		}

		if(angleSmooth < -1.0D) {
			angleSmooth = -1.0D;
		}

		if(angleSmooth > 1.0D) {
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
		
		double xs = (resolution / 2.0) + 0.5;
		double ys = (resolution / 2.0) - 0.5;


		
		for(int i = (int)(-4 * scaleFactor); i <= (int)(4 * scaleFactor); ++i) {
			x2 = (int)(xs + y * (double)i * 0.3D);
			y2 = (int)(ys - x * (double)i * 0.3D * 0.5D);
			j = y2 * resolution + x2;
			r = 100;
			g = 100;
			b = 100;
			a = 255;

			this.imageData[j * 4 + 0] = (byte)r;
			this.imageData[j * 4 + 1] = (byte)g;
			this.imageData[j * 4 + 2] = (byte)b;
			this.imageData[j * 4 + 3] = (byte)a;
		}

		for(int i = (int)(-8 * scaleFactor); i <= (int)(16 * scaleFactor); ++i) {
			x2 = (int)(xs + x * (double)i * 0.3D);
			y2 = (int)(ys + y * (double)i * 0.3D * 0.5D);
			j = y2 * resolution + x2;
			r = i >= 0 ? 255 : 200; //
			g = i >= 0 ? i*8 : 10; // determines the color of the pointer
			b = i >= 0 ? 20 : 128; //
			a = 255;

			this.imageData[j * 4 + 0] = (byte)r;
			this.imageData[j * 4 + 1] = (byte)g;
			this.imageData[j * 4 + 2] = (byte)b;
			this.imageData[j * 4 + 3] = (byte)a;
		}
	}

	@Override
	public String getTextureName() {
		return "/gui/items.png";
	}

}
