package sunsetsatellite.signalindustries.api.impl.itempipes.misc;

import net.minecraft.src.*;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.itempipes.tiles.TileEntityItemPipe;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3f;

public class EntityPipeItem extends Entity {
    public ItemStack item;
    private int field_803_e;
    public int age = 0;
    public int delayBeforeCanPickup;
    private int health = 5;
    public float field_804_d = (float)(Math.random() * Math.PI * 2.0);
    public TileEntityItemPipe pipe;
    private NBTTagCompound tempNBT = null;
    private boolean render = true;

    public EntityPipeItem(World world, double d, double d1, double d2, ItemStack itemstack, TileEntity pipe) {
        super(world);
        this.setSize(0.25F, 0.25F);
        this.setPosition(d, d1, d2);
        this.item = itemstack;
        this.motionX = 0.0f;
        this.motionY = 0.0f;
        this.motionZ = 0.0f;
        this.pipe = (TileEntityItemPipe) pipe;
    }

    public PipeItem getPipeItem(){
        if(pipe != null){
            for (PipeItem pipeItem : pipe.items) {
                if(pipeItem.entity == this){
                    return pipeItem;
                }
            }
        }
        return null;
    }

    protected boolean canTriggerWalking() {
        return false;
    }

    public EntityPipeItem(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit() {

    }

    public void onUpdate() {
        if(pipe == null && tempNBT != null){
            linkEntityToPipe(tempNBT);
            tempNBT = null;
        }
        super.onUpdate();
        if (this.delayBeforeCanPickup > 0) {
            --this.delayBeforeCanPickup;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        //this.moveEntity(this.motionX, this.motionY, this.motionZ);

        /*for (EntityPlayer player : worldObj.players) {
            if(player instanceof EntityPlayerMP && !worldObj.isMultiplayerAndNotHost){
                ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new PacketPipeItemPos(this));
                /*if((this.prevPosX != this.posX ) || (this.prevPosY != this.posY) || (this.prevPosZ != this.posZ)){

                }
            }
        }*/

    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        if(this.pipe == null){
            setEntityDead();
            return;
        }
        nbttagcompound.setShort("Health", (short)((byte)this.health));
        nbttagcompound.setShort("Age", (short)this.age);
        nbttagcompound.setCompoundTag("Item", this.item.writeToNBT(new NBTTagCompound()));
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("x",pipe.xCoord);
        tag.setInteger("y",pipe.yCoord);
        tag.setInteger("z",pipe.zCoord);
        nbttagcompound.setCompoundTag("pipe",tag);
        tag = new NBTTagCompound();
        for (PipeItem pipeItem : pipe.items) {
            if(pipeItem.entity == this){
                pipeItem.offset.writeToNBT(tag);
                nbttagcompound.setInteger("inDir",pipeItem.inDir.ordinal());
                nbttagcompound.setInteger("outDir",pipeItem.outDir.ordinal());
                nbttagcompound.setBoolean("center",pipeItem.goingToCenter);
                nbttagcompound.setBoolean("end",pipeItem.atEnd);
            }
        }
        nbttagcompound.setCompoundTag("offset",tag);



    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.health = nbttagcompound.getShort("Health") & 255;
        this.age = nbttagcompound.getShort("Age");
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("Item");
        this.item = new ItemStack(nbttagcompound1);
        tempNBT = nbttagcompound;
    }

    public void linkEntityToPipe(NBTTagCompound nbttagcompound){
        NBTTagCompound tag = nbttagcompound.getCompoundTag("pipe");
        TileEntity pipe = worldObj.getBlockTileEntity(tag.getInteger("x"),tag.getInteger("y"),tag.getInteger("z"));
        if(!(pipe instanceof TileEntityItemPipe)){
            this.setEntityDead();
        } else {
            this.pipe = (TileEntityItemPipe) pipe;
            Vec3f offset = new Vec3f(nbttagcompound.getCompoundTag("offset"));
            Direction inDir = Direction.values()[nbttagcompound.getInteger("inDir")];
            Direction outDir = Direction.values()[nbttagcompound.getInteger("outDir")];
            boolean center = nbttagcompound.getBoolean("center");
            boolean end = nbttagcompound.getBoolean("end");
            PipeItem pipeItem = new PipeItem(this,inDir,outDir,offset);
            pipeItem.goingToCenter = center;
            pipeItem.atEnd = end;
            this.pipe.items.add(pipeItem);
        }
    }

    public void linkEntityToPipe(TileEntityItemPipe pipe, Vec3f offset, Direction inDir, Direction outDir, boolean center, boolean end){
        this.pipe = pipe;
        PipeItem pipeItem = new PipeItem(this,inDir,outDir,offset);
        pipeItem.goingToCenter = center;
        pipeItem.atEnd = end;
        this.pipe.items.add(pipeItem);
    }

    public boolean canRender() {
        return render;
    }

    public EntityPipeItem setRender(boolean render) {
        this.render = render;
        return this;
    }

}
