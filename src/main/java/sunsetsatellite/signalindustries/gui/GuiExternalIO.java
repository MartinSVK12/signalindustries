package sunsetsatellite.signalindustries.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Vec3f;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerExternalIO;
import sunsetsatellite.signalindustries.inventories.TileEntityExternalIO;
import sunsetsatellite.signalindustries.util.Tier;

public class GuiExternalIO extends GuiContainer {

    public String name = "External I/O";
    public EntityPlayer entityplayer;
    public TileEntityExternalIO tile;


    public GuiExternalIO(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerExternalIO(inventoryPlayer, (TileEntityExternalIO) tile));
        this.tile = (TileEntityExternalIO) tile;
        this.entityplayer = inventoryPlayer.player;
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {
        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/basic_gui_blank.png");
        if(tile.tier == Tier.REINFORCED){
            i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/reinforced_gui_base.png");
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
                break;
            case BASIC:
                color = 0xFFFF8080;
                break;
            case REINFORCED:
                color = 0xFFFF0000;
                break;
            case AWAKENED:
                color = 0xFFFFA500;
                break;
        }
        fontRenderer.drawCenteredString(name, 90, 6, color);
        int x = (this.xSize) / 2;
        int y = (this.ySize) / 4;
        if(tile.tier == Tier.REINFORCED){
            if(tile.externalTilePos == null){
                fontRenderer.drawCenteredString("Disconnected.",x,y,0xFFFF0000);
                fontRenderer.drawCenteredString("No position.",x,y+12,0xFFFF0000);
            } else if(tile.externalTilePos.containsKey("x") && tile.externalTilePos.containsKey("y") && tile.externalTilePos.containsKey("z") && tile.externalTilePos.containsKey("dim") && tile.externalTilePos.containsKey("side")){
                if(tile.externalTile != null){
                    fontRenderer.drawCenteredString("Connected.",x,y,0xFF00FF00);
                    fontRenderer.drawCenteredString(tile.externalTile.getClass().getSimpleName()+ " @ " + tile.externalTile.x +" "+tile.externalTile.y+" "+tile.externalTile.z,x,y+12,0xFF00FF00);
                    fontRenderer.drawCenteredString(String.valueOf(Side.getSideById(tile.externalTilePos.getInteger("side"))),x,y+24,0xFF00FF00);
                } else {
                    int eX = tile.externalTilePos.getInteger("x");
                    int eY = tile.externalTilePos.getInteger("y");
                    int eZ = tile.externalTilePos.getInteger("z");
                    int dim = tile.externalTilePos.getInteger("dim");
                    Vec3i pos = new Vec3i(eX,eY,eZ);
                    Vec3f selfPos = new Vec3f(tile.x,tile.y,tile.z);
                    if(dim != tile.worldObj.dimension.id){
                        fontRenderer.drawCenteredString("Can't connect.",x,y,0xFFFFA500);
                        fontRenderer.drawCenteredString("Outside this world.",x,y+12,0xFFFFA500);
                    } else if(pos.distanceTo(selfPos) > TileEntityExternalIO.range){
                        fontRenderer.drawCenteredString("Can't connect.",x,y,0xFFFFA500);
                        fontRenderer.drawCenteredString("Out of reach.",x,y+12,0xFFFFA500);
                    }
                }
            }
        } else {
            if(tile.externalTile != null){
                fontRenderer.drawCenteredString("Connected.",x,y,0xFF00FF00);
                fontRenderer.drawCenteredString(tile.externalTile.getClass().getSimpleName()+ " @ " + tile.externalTileSide.getName(),x,y+12,0xFF00FF00);
            } else {
                fontRenderer.drawCenteredString("Disconnected.",x,y,0xFFFF0000);
            }
        }
    }
    protected void buttonPressed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        switch (guibutton.id){
            case 0:
                SignalIndustries.displayGui(entityplayer, () -> new GuiFluidIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            case 1:
                SignalIndustries.displayGui(entityplayer, () -> new GuiItemIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            case 2:
                Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Link removed!");
                tile.externalTile = null;
                tile.externalTileSide = null;
                tile.externalTilePos = null;
            default:
                break;
        }
    }

    public void init()
    {
        GuiButton fluidIo = new GuiButton(0, Math.round(width / 2) + 60, Math.round(height / 2) - 80, 20, 20, "F");
        GuiButton itemIo = new GuiButton(1, Math.round(width / 2) + 60, Math.round(height / 2) - 60, 20, 20, "I");
        GuiButton removeLink = new GuiButton(2, Math.round(width / 2) - 80, Math.round(height / 2) - 80, 20, 20, "X");
        if(!(tile.externalTile instanceof IInventory)){
            itemIo.enabled = false;
        }
        if(!(tile.externalTile instanceof IFluidInventory)){
            fluidIo.enabled = false;
        }
        controlList.add(fluidIo);
        controlList.add(itemIo);
        controlList.add(removeLink);
        super.init();
    }
}
