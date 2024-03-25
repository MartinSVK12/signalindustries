package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderBlockCache;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextureFX;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import static net.minecraft.core.Global.TEXTURE_ATLAS_WIDTH_TILES;

public class RenderSingleBlock {

    private WorldSource blockAccess;
    private Minecraft mc;
    private World world;
    private final int overrideBlockTexture;
    private final boolean flipTexture;
    private final boolean renderAllFaces;
    public static boolean fancyGrass = true;

    private static final float[] SIDE_LIGHT_MULTIPLIER = {0.5f, 1.f, 0.8f, 0.8f, 0.6f, 0.6f};
    public boolean useInventoryTint;
    private final int uvRotateEast;
    private final int uvRotateWest;
    private final int uvRotateSouth;
    private final int uvRotateNorth;
    private final int uvRotateTop;
    private final int uvRotateBottom;
    private boolean enableAO;
    private final int field_22352_G;
    private float colorRedTopLeft;
    private float colorRedBottomLeft;
    private float colorRedBottomRight;
    private float colorRedTopRight;
    private float colorGreenTopLeft;
    private float colorGreenBottomLeft;
    private float colorGreenBottomRight;
    private float colorGreenTopRight;
    private float colorBlueTopLeft;
    private float colorBlueBottomLeft;
    private float colorBlueBottomRight;
    private float colorBlueTopRight;
    public boolean overbright;

    private RenderBlockCache cache = new RenderBlockCache();

    public RenderSingleBlock(World world, WorldSource iblockaccess)
    {
        overrideBlockTexture = -1;
        flipTexture = false;
        renderAllFaces = false;
        useInventoryTint = true;
        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateSouth = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        field_22352_G = 1;
        blockAccess = iblockaccess;
        this.world = world;
        this.mc = Minecraft.getMinecraft(this);
    }

    public RenderSingleBlock()
    {
        overrideBlockTexture = -1;
        flipTexture = false;
        renderAllFaces = false;
        useInventoryTint = true;
        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateSouth = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        field_22352_G = 1;
        this.mc = Minecraft.getMinecraft(this);
    }

    public boolean renderStandardBlock(Block block, int x, int y, int z, int meta, float r, float g, float b) {
        this.enableAO = true;
        cache.setupCache(block, blockAccess, x, y, z);
        boolean notGrass = block != Block.grass;
        boolean somethingRendered = renderSide(block, x, y, z, r, g, b, notGrass,0, meta,
                0, -1, 0, (float)block.minY,
                0, 0, 1, (float)block.maxZ, (float)block.minZ,
                -1, 0, 0, 1-(float)block.minX, 1-(float)block.maxX
        );
        somethingRendered |= renderSide(block, x, y, z, r, g, b, notGrass,1, meta,
                0,1,0, 1-(float)block.maxY,
                0,0,1, (float)block.maxZ, (float)block.minZ,
                1,0,0, (float)block.maxX, (float)block.minX
        );
        somethingRendered |= renderSide(block, x, y, z, r, g, b, notGrass,2, meta,
                0,0,-1, (float)block.minZ,
                -1,0,0, 1-(float)block.minX, 1-(float)block.maxX,
                0,1,0, (float)block.maxY, (float)block.minY
        );
        somethingRendered |= renderSide(block, x, y, z, r, g, b, notGrass,3, meta,
                0,0,1, 1-(float)block.maxZ,
                0,1,0, (float)block.maxY, (float)block.minY,
                -1,0,0, 1-(float)block.minX, 1-(float)block.maxX
        );
        somethingRendered |= renderSide(block, x, y, z, r, g, b, notGrass,4, meta,
                -1,0,0, (float)block.minX,
                0,0,1, (float)block.maxZ, (float)block.minZ,
                0,1,0, (float)block.maxY, (float)block.minY
        );
        somethingRendered |= renderSide(block, x, y, z, r, g, b, notGrass,5, meta,
                1,0,0, 1-(float)block.maxX,
                0,0,1, (float)block.maxZ, (float)block.minZ,
                0,-1,0, 1-(float)block.minY, 1-(float)block.maxY
        );
        this.enableAO = false;
        return somethingRendered;
    }

    /**
     * @author CyborgCabbage
     * Thanks! :D
     */
    final boolean renderSide(Block block, int x, int y, int z, float r, float g, float b, boolean notGrass, int side, int meta,
                             int dirX, int dirY, int dirZ, float depth,
                             int topX, int topY, int topZ, float topP, float botP,
                             int lefX, int lefY, int lefZ, float lefP, float rigP
    ){
        boolean rendered = false;
        boolean flag = (side == 1) || notGrass;
        block.setBlockBoundsBasedOnSide(world, x, y, z, Side.getSideById(side));
        if (this.renderAllFaces || block.shouldSideBeRendered(this.blockAccess, x+dirX, y+dirY, z+dirZ, side, meta)) {
            float lightTR;
            float lightBR;
            float lightBL;
            float lightTL;

            if (this.overbright) {
                lightTR = 1.0F;
                lightBR = 1.0F;
                lightBL = 1.0F;
                lightTL = 1.0F;
            } else if (this.field_22352_G <= 0) {
                lightTL = lightBL = lightBR = lightTR = cache.getBrightness(dirX, dirY, dirZ);
            } else {
                if (mc.isAmbientOcclusionEnabled()) {
                    {
                        float dirB = cache.getBrightness(dirX, dirY, dirZ);
                        boolean lefT = cache.getOpacity(dirX + lefX, dirY + lefY, dirZ + lefZ);
                        boolean botT = cache.getOpacity(dirX - topX, dirY - topY, dirZ - topZ);
                        boolean topT = cache.getOpacity(dirX + topX, dirY + topY, dirZ + topZ);
                        boolean rigT = cache.getOpacity(dirX - lefX, dirY - lefY, dirZ - lefZ);
                        float lB = cache.getBrightness(dirX + lefX, dirY + lefY, dirZ + lefZ);
                        float bB = cache.getBrightness(dirX - topX, dirY - topY, dirZ - topZ);
                        float tB = cache.getBrightness(dirX + topX, dirY + topY, dirZ + topZ);
                        float rB = cache.getBrightness(dirX - lefX, dirY - lefY, dirZ - lefZ);
                        float blB = botT && lefT ? lB : cache.getBrightness(dirX + lefX - topX, dirY + lefY - topY, dirZ + lefZ - topZ);
                        float tlB = topT && lefT ? lB : cache.getBrightness(dirX + lefX + topX, dirY + lefY + topY, dirZ + lefZ + topZ);
                        float brB = botT && rigT ? rB : cache.getBrightness(dirX - lefX - topX, dirY - lefY - topY, dirZ - lefZ - topZ);
                        float trB = topT && rigT ? rB : cache.getBrightness(dirX - lefX + topX, dirY - lefY + topY, dirZ - lefZ + topZ);
                        lightTL = (tlB + lB + tB + dirB) / 4.0F;
                        lightTR = (tB + dirB + trB + rB) / 4.0F;
                        lightBR = (dirB + bB + rB + brB) / 4.0F;
                        lightBL = (lB + blB + dirB + bB) / 4.0F;
                    }
                    if(depth > 0.01){
                        float dirB = cache.getBrightness(0, 0, 0);
                        boolean lefT = cache.getOpacity(lefX, lefY, lefZ);
                        boolean botT = cache.getOpacity(-topX, -topY, -topZ);
                        boolean topT = cache.getOpacity(topX, topY, topZ);
                        boolean rigT = cache.getOpacity(-lefX, -lefY, -lefZ);
                        float lB = cache.getBrightness(lefX, lefY, lefZ);
                        float bB = cache.getBrightness(-topX, -topY, -topZ);
                        float tB = cache.getBrightness(topX, topY, topZ);
                        float rB = cache.getBrightness(-lefX, -lefY, -lefZ);
                        float blB = botT && lefT ? lB : cache.getBrightness(lefX - topX, lefY - topY, lefZ - topZ);
                        float tlB = topT && lefT ? lB : cache.getBrightness(lefX + topX, lefY + topY, lefZ + topZ);
                        float brB = botT && rigT ? rB : cache.getBrightness(-lefX - topX, -lefY - topY, -lefZ - topZ);
                        float trB = topT && rigT ? rB : cache.getBrightness(-lefX + topX, -lefY + topY, -lefZ + topZ);
                        lightTL = (tlB + lB + tB + dirB) / 4.0F * depth + lightTL*(1-depth);
                        lightTR = (tB + dirB + trB + rB) / 4.0F * depth + lightTR*(1-depth);
                        lightBR = (dirB + bB + rB + brB) / 4.0F * depth + lightBR*(1-depth);
                        lightBL = (lB + blB + dirB + bB) / 4.0F * depth + lightBL*(1-depth);
                    }
                } else {
                    float brightness = cache.getBrightness(dirX, dirY, dirZ);
                    lightTL = lightBL = lightBR = lightTR = brightness;
                }
            }

            if (overbright)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag ? r : 1.0F);
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag ? g : 1.0F);
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag ? b : 1.0F);
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag ? r : 1.0F) * SIDE_LIGHT_MULTIPLIER[side];
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag ? g : 1.0F) * SIDE_LIGHT_MULTIPLIER[side];
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag ? b : 1.0F) * SIDE_LIGHT_MULTIPLIER[side];
            }
            float tl = topP * lightTL + (1 - topP) * lightBL;
            float tr = topP * lightTR + (1 - topP) * lightBR;
            float bl = botP * lightTL + (1 - botP) * lightBL;
            float br = botP * lightTR + (1 - botP) * lightBR;
            float ltl = lefP * tl + (1 - lefP) * tr;
            float lbl = lefP * bl + (1 - lefP) * br;
            float lbr = rigP * bl + (1 - rigP) * br;
            float ltr = rigP * tl + (1 - rigP) * tr;
            this.colorRedTopLeft *= ltl;
            this.colorGreenTopLeft *= ltl;
            this.colorBlueTopLeft *= ltl;

            this.colorRedBottomLeft *= lbl;
            this.colorGreenBottomLeft *= lbl;
            this.colorBlueBottomLeft *= lbl;

            this.colorRedBottomRight *= lbr;
            this.colorGreenBottomRight *= lbr;
            this.colorBlueBottomRight *= lbr;

            this.colorRedTopRight *= ltr;
            this.colorGreenTopRight *= ltr;
            this.colorBlueTopRight *= ltr;

            int tex;
            if (this.overbright) {
                tex = block.getBlockOverbrightTexture(this.blockAccess, x, y, z, side);
            } else {
                tex = block.getBlockTexture(this.blockAccess, x, y, z, Side.getSideById(side));
            }

            if (tex >= 0) {
                if(side == 0) {
                    this.renderBottomFace(block, x, y, z, tex);
                }else if(side == 1){
                    this.renderTopFace(block, x, y, z, tex);
                }else if(side == 2){
                    this.renderNorthFace(block, x, y, z, tex);
                }else if(side == 3){
                    this.renderSouthFace(block, x, y, z, tex);
                }else if(side == 4){
                    this.renderWestFace(block, x, y, z, tex);
                }else if(side == 5){
                    this.renderEastFace(block, x, y, z, tex);
                }
                rendered = true;
            }
            if (fancyGrass && tex == 3 && this.overrideBlockTexture < 0) {
                this.colorRedTopLeft *= r;
                this.colorRedBottomLeft *= r;
                this.colorRedBottomRight *= r;
                this.colorRedTopRight *= r;
                this.colorGreenTopLeft *= g;
                this.colorGreenBottomLeft *= g;
                this.colorGreenBottomRight *= g;
                this.colorGreenTopRight *= g;
                this.colorBlueTopLeft *= b;
                this.colorBlueBottomLeft *= b;
                this.colorBlueBottomRight *= b;
                this.colorBlueTopRight *= b;
                if(side == 2){
                    this.renderNorthFace(block, x, y, z, Block.texCoordToIndex(6, 2));
                }else if(side == 3){
                    this.renderSouthFace(block, x, y, z, Block.texCoordToIndex(6, 2));
                }else if(side == 4){
                    this.renderWestFace(block, x, y, z, Block.texCoordToIndex(6, 2));
                }else if(side == 5){
                    this.renderEastFace(block, x, y, z, Block.texCoordToIndex(6, 2));
                }
            }
        }
        return rendered;
    }

    public void renderBottomFace(Block block, double d, double d1, double d2,
                                 int i)
    {
        Tessellator tessellator = Tessellator.instance;
        if(overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }
        int j = (i % TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;
        int k = (i / TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;;
        double d3 = ((double)j + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = (((double)j + block.maxX * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)k + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = (((double)k + block.maxZ * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        if(block.minX < 0.0D || block.maxX > 1.0D)
        {
            d3 = ((float)j + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        if(block.minZ < 0.0D || block.maxZ > 1.0D)
        {
            d5 = ((float)k + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;
        if(uvRotateBottom == 2)
        {
            d3 = ((double)j + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else
        if(uvRotateBottom == 1)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
            d3 = d7;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else
        if(uvRotateBottom == 3)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }
        double d11 = d + block.minX;
        double d12 = d + block.maxX;
        double d13 = d1 + block.minY;
        double d14 = d2 + block.minZ;
        double d15 = d2 + block.maxZ;
        if(enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
        } else
        {
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
        }
    }

    public void renderTopFace(Block block, double d, double d1, double d2,
                              int i)
    {
        Tessellator tessellator = Tessellator.instance;
        if(overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }
        int j = (i % TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;
        int k = (i / TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;;
        double d3 = ((double)j + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = (((double)j + block.maxX * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)k + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = (((double)k + block.maxZ * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        if(block.minX < 0.0D || block.maxX > 1.0D)
        {
            d3 = ((float)j + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        if(block.minZ < 0.0D || block.maxZ > 1.0D)
        {
            d5 = ((float)k + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;
        if(uvRotateTop == 1)
        {
            d3 = ((double)j + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else
        if(uvRotateTop == 2)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
            d3 = d7;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else
        if(uvRotateTop == 3)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }
        double d11 = d + block.minX;
        double d12 = d + block.maxX;
        double d13 = d1 + block.maxY;
        double d14 = d2 + block.minZ;
        double d15 = d2 + block.maxZ;
        if(enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
        } else
        {
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
        }
    }

    public void renderNorthFace(Block block, double d, double d1, double d2,
                                int i)
    {
        Tessellator tessellator = Tessellator.instance;
        if(overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }
        int j = (i % TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;
        int k = (i / TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;;
        double d3 = ((double)j + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = (((double)j + block.maxX * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        if(flipTexture)
        {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if(block.minX < 0.0D || block.maxX > 1.0D)
        {
            d3 = ((float)j + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        if(block.minY < 0.0D || block.maxY > 1.0D)
        {
            d5 = ((float)k + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if(uvRotateEast == 2)
        {
            d3 = ((double)j + block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else
        if(uvRotateEast == 1)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else
        if(uvRotateEast == 3)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = (((double)k + block.minY * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double d12 = d + block.minX;
        double d13 = d + block.maxX;
        double d14 = d1 + block.minY;
        double d15 = d1 + block.maxY;
        double d16 = d2 + block.minZ;
        if(enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d15, d16, d8, d10);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(d13, d15, d16, d3, d5);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(d13, d14, d16, d9, d11);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d14, d16, d4, d6);
        } else
        {
            tessellator.addVertexWithUV(d12, d15, d16, d8, d10);
            tessellator.addVertexWithUV(d13, d15, d16, d3, d5);
            tessellator.addVertexWithUV(d13, d14, d16, d9, d11);
            tessellator.addVertexWithUV(d12, d14, d16, d4, d6);
        }
    }

    public void renderSouthFace(Block block, double d, double d1, double d2,
                                int i)
    {
        Tessellator tessellator = Tessellator.instance;
        if(overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }
        int j = (i % TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;
        int k = (i / TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;;
        double d3 = ((double)j + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = (((double)j + block.maxX * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        if(flipTexture)
        {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if(block.minX < 0.0D || block.maxX > 1.0D)
        {
            d3 = ((float)j + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        if(block.minY < 0.0D || block.maxY > 1.0D)
        {
            d5 = ((float)k + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if(uvRotateWest == 1)
        {
            d3 = ((double)j + block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else
        if(uvRotateWest == 2)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else
        if(uvRotateWest == 3)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = (((double)k + block.minY * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double d12 = d + block.minX;
        double d13 = d + block.maxX;
        double d14 = d1 + block.minY;
        double d15 = d1 + block.maxY;
        double d16 = d2 + block.maxZ;
        if(enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d15, d16, d3, d5);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(d13, d14, d16, d4, d6);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(d13, d15, d16, d8, d10);
        } else
        {
            tessellator.addVertexWithUV(d12, d15, d16, d3, d5);
            tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
            tessellator.addVertexWithUV(d13, d14, d16, d4, d6);
            tessellator.addVertexWithUV(d13, d15, d16, d8, d10);
        }
    }

    public void renderWestFace(Block block, double d, double d1, double d2,
                               int i)
    {
        Tessellator tessellator = Tessellator.instance;
        if(overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }
        int j = (i % TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;
        int k = (i / TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;;
        double d3 = ((double)j + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = (((double)j + block.maxZ * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        if(flipTexture)
        {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if(block.minZ < 0.0D || block.maxZ > 1.0D)
        {
            d3 = ((float)j + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        if(block.minY < 0.0D || block.maxY > 1.0D)
        {
            d5 = ((float)k + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if(uvRotateNorth == 1)
        {
            d3 = ((double)j + block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else
        if(uvRotateNorth == 2)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else
        if(uvRotateNorth == 3)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = (((double)k + block.minY * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double d12 = d + block.minX;
        double d13 = d1 + block.minY;
        double d14 = d1 + block.maxY;
        double d15 = d2 + block.minZ;
        double d16 = d2 + block.maxZ;
        if(enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d14, d16, d8, d10);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(d12, d13, d15, d9, d11);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d13, d16, d4, d6);
        } else
        {
            tessellator.addVertexWithUV(d12, d14, d16, d8, d10);
            tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
            tessellator.addVertexWithUV(d12, d13, d15, d9, d11);
            tessellator.addVertexWithUV(d12, d13, d16, d4, d6);
        }
    }

    public void renderEastFace(Block block, double d, double d1, double d2,
                               int i)
    {
        Tessellator tessellator = Tessellator.instance;
        if(overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }
        int j = (i % TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;
        int k = (i / TEXTURE_ATLAS_WIDTH_TILES) * TextureFX.tileWidthTerrain;;
        double d3 = ((double)j + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = (((double)j + block.maxZ * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        if(flipTexture)
        {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if(block.minZ < 0.0D || block.maxZ > 1.0D)
        {
            d3 = ((float)j + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        if(block.minY < 0.0D || block.maxY > 1.0D)
        {
            d5 = ((float)k + 0.0F) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + (TextureFX.tileWidthTerrain - 0.01F)) / (float)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if(uvRotateSouth == 2)
        {
            d3 = ((double)j + block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else
        if(uvRotateSouth == 1)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else
        if(uvRotateSouth == 3)
        {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d6 = (((double)k + block.minY * (double)TextureFX.tileWidthTerrain) - 0.01D) / (double)(TextureFX.tileWidthTerrain * TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double d12 = d + block.maxX;
        double d13 = d1 + block.minY;
        double d14 = d1 + block.maxY;
        double d15 = d2 + block.minZ;
        double d16 = d2 + block.maxZ;
        if(enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d13, d16, d9, d11);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.addVertexWithUV(d12, d14, d15, d8, d10);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d14, d16, d3, d5);
        } else
        {
            tessellator.addVertexWithUV(d12, d13, d16, d9, d11);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.addVertexWithUV(d12, d14, d15, d8, d10);
            tessellator.addVertexWithUV(d12, d14, d16, d3, d5);
        }
    }
}
