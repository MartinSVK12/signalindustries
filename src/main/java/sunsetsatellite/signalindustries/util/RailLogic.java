package sunsetsatellite.signalindustries.util;

import net.minecraft.src.Block;
import net.minecraft.src.BlockRail;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.World;

import java.util.ArrayList;
import java.util.List;

public class RailLogic {
    private final World worldObj;

    private final int trackX;

    private final int trackY;

    private final int trackZ;

    private final boolean isPoweredRail;

    private final List connectedTracks;

    final BlockRail rail;

    public RailLogic(BlockRail blockrail, World world, int i, int j, int k) {
        this.rail = blockrail;
        this.connectedTracks = new ArrayList();
        this.worldObj = world;
        this.trackX = i;
        this.trackY = j;
        this.trackZ = k;
        int l = world.getBlockId(i, j, k);
        int i1 = world.getBlockMetadata(i, j, k);

        this.isPoweredRail = true;
        i1 &= 0xFFFFFFF7;
        setConnections(i1);
    }

    private void setConnections(int i) {
        this.connectedTracks.clear();
        if (i == 0) {
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
        } else if (i == 1) {
            this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
        } else if (i == 2) {
            this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY + 1, this.trackZ));
        } else if (i == 3) {
            this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY + 1, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
        } else if (i == 4) {
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY + 1, this.trackZ - 1));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
        } else if (i == 5) {
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY + 1, this.trackZ + 1));
        } else if (i == 6) {
            this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
        } else if (i == 7) {
            this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ + 1));
        } else if (i == 8) {
            this.connectedTracks.add(new ChunkPosition(this.trackX - 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
        } else if (i == 9) {
            this.connectedTracks.add(new ChunkPosition(this.trackX + 1, this.trackY, this.trackZ));
            this.connectedTracks.add(new ChunkPosition(this.trackX, this.trackY, this.trackZ - 1));
        }
    }

    private void func_785_b() {
        for (int i = 0; i < this.connectedTracks.size(); i++) {
            RailLogic raillogic = getMinecartTrackLogic((ChunkPosition) this.connectedTracks.get(i));
            if (raillogic == null || !raillogic.isConnectedTo(this)) {
                this.connectedTracks.remove(i--);
            } else {
                this.connectedTracks.set(i, new ChunkPosition(raillogic.trackX, raillogic.trackY, raillogic.trackZ));
            }
        }
    }

    private boolean isMinecartTrack(int i, int j, int k) {
        if (BlockRail.isRailBlockAt(this.worldObj, i, j, k))
            return true;
        if (BlockRail.isRailBlockAt(this.worldObj, i, j + 1, k))
            return true;
        return BlockRail.isRailBlockAt(this.worldObj, i, j - 1, k);
    }

    private RailLogic getMinecartTrackLogic(ChunkPosition chunkposition) {
        if (BlockRail.isRailBlockAt(this.worldObj, chunkposition.x, chunkposition.y, chunkposition.z))
            return new RailLogic(this.rail, this.worldObj, chunkposition.x, chunkposition.y, chunkposition.z);
        if (BlockRail.isRailBlockAt(this.worldObj, chunkposition.x, chunkposition.y + 1, chunkposition.z))
            return new RailLogic(this.rail, this.worldObj, chunkposition.x, chunkposition.y + 1, chunkposition.z);
        if (BlockRail.isRailBlockAt(this.worldObj, chunkposition.x, chunkposition.y - 1, chunkposition.z))
            return new RailLogic(this.rail, this.worldObj, chunkposition.x, chunkposition.y - 1, chunkposition.z);
        return null;
    }

    private boolean isConnectedTo(RailLogic raillogic) {
        for (int i = 0; i < this.connectedTracks.size(); i++) {
            ChunkPosition chunkposition = (ChunkPosition) this.connectedTracks.get(i);
            if (chunkposition.x == raillogic.trackX && chunkposition.z == raillogic.trackZ)
                return true;
        }
        return false;
    }

    private boolean isInTrack(int i, int j, int k) {
        for (int l = 0; l < this.connectedTracks.size(); l++) {
            ChunkPosition chunkposition = (ChunkPosition) this.connectedTracks.get(l);
            if (chunkposition.x == i && chunkposition.z == k)
                return true;
        }
        return false;
    }

    private int getAdjacentTracks() {
        int i = 0;
        if (isMinecartTrack(this.trackX, this.trackY, this.trackZ - 1))
            i++;
        if (isMinecartTrack(this.trackX, this.trackY, this.trackZ + 1))
            i++;
        if (isMinecartTrack(this.trackX - 1, this.trackY, this.trackZ))
            i++;
        if (isMinecartTrack(this.trackX + 1, this.trackY, this.trackZ))
            i++;
        return i;
    }

    private boolean handleKeyPress(RailLogic raillogic) {
        if (isConnectedTo(raillogic))
            return true;
        if (this.connectedTracks.size() == 2)
            return false;
        if (this.connectedTracks.size() == 0)
            return true;
        ChunkPosition chunkposition = (ChunkPosition) this.connectedTracks.get(0);
        return true;
    }

    private void func_788_d(RailLogic raillogic) {
        this.connectedTracks.add(new ChunkPosition(raillogic.trackX, raillogic.trackY, raillogic.trackZ));
        boolean flag = isInTrack(this.trackX, this.trackY, this.trackZ - 1);
        boolean flag1 = isInTrack(this.trackX, this.trackY, this.trackZ + 1);
        boolean flag2 = isInTrack(this.trackX - 1, this.trackY, this.trackZ);
        boolean flag3 = isInTrack(this.trackX + 1, this.trackY, this.trackZ);
        byte byte0 = -1;
        if (flag || flag1)
            byte0 = 0;
        if (flag2 || flag3)
            byte0 = 1;
        if (!this.isPoweredRail) {
            if (flag1 && flag3 && !flag && !flag2)
                byte0 = 6;
            if (flag1 && flag2 && !flag && !flag3)
                byte0 = 7;
            if (flag && flag2 && !flag1 && !flag3)
                byte0 = 8;
            if (flag && flag3 && !flag1 && !flag2)
                byte0 = 9;
        }
        if (byte0 == 0) {
            if (BlockRail.isRailBlockAt(this.worldObj, this.trackX, this.trackY + 1, this.trackZ - 1))
                byte0 = 4;
            if (BlockRail.isRailBlockAt(this.worldObj, this.trackX, this.trackY + 1, this.trackZ + 1))
                byte0 = 5;
        }
        if (byte0 == 1) {
            if (BlockRail.isRailBlockAt(this.worldObj, this.trackX + 1, this.trackY + 1, this.trackZ))
                byte0 = 2;
            if (BlockRail.isRailBlockAt(this.worldObj, this.trackX - 1, this.trackY + 1, this.trackZ))
                byte0 = 3;
        }
        if (byte0 < 0)
            byte0 = 0;
        int i = byte0;
        if (this.isPoweredRail)
            i = this.worldObj.getBlockMetadata(this.trackX, this.trackY, this.trackZ) & 0x8 | byte0;
        this.worldObj.setBlockMetadataWithNotify(this.trackX, this.trackY, this.trackZ, i);
    }

    private boolean func_786_c(int i, int j, int k) {
        RailLogic raillogic = getMinecartTrackLogic(new ChunkPosition(i, j, k));
        if (raillogic == null)
            return false;
        raillogic.func_785_b();
        return raillogic.handleKeyPress(this);
    }

    public void func_792_a(boolean flag, boolean flag1) {
        boolean flag2 = func_786_c(this.trackX, this.trackY, this.trackZ - 1);
        boolean flag3 = func_786_c(this.trackX, this.trackY, this.trackZ + 1);
        boolean flag4 = func_786_c(this.trackX - 1, this.trackY, this.trackZ);
        boolean flag5 = func_786_c(this.trackX + 1, this.trackY, this.trackZ);
        byte byte0 = -1;
        if ((flag2 || flag3) && !flag4 && !flag5)
            byte0 = 0;
        if ((flag4 || flag5) && !flag2 && !flag3)
            byte0 = 1;
        if (!this.isPoweredRail) {
            if (flag3 && flag5 && !flag2 && !flag4)
                byte0 = 6;
            if (flag3 && flag4 && !flag2 && !flag5)
                byte0 = 7;
            if (flag2 && flag4 && !flag3 && !flag5)
                byte0 = 8;
            if (flag2 && flag5 && !flag3 && !flag4)
                byte0 = 9;
        }
        if (byte0 == -1) {
            if (flag2 || flag3)
                byte0 = 0;
            if (flag4 || flag5)
                byte0 = 1;
            if (!this.isPoweredRail)
                if (flag) {
                    if (flag3 && flag5)
                        byte0 = 6;
                    if (flag4 && flag3)
                        byte0 = 7;
                    if (flag5 && flag2)
                        byte0 = 9;
                    if (flag2 && flag4)
                        byte0 = 8;
                } else {
                    if (flag2 && flag4)
                        byte0 = 8;
                    if (flag5 && flag2)
                        byte0 = 9;
                    if (flag4 && flag3)
                        byte0 = 7;
                    if (flag3 && flag5)
                        byte0 = 6;
                }
        }
        if (byte0 == 0) {
            if (BlockRail.isRailBlockAt(this.worldObj, this.trackX, this.trackY + 1, this.trackZ - 1))
                byte0 = 4;
            if (BlockRail.isRailBlockAt(this.worldObj, this.trackX, this.trackY + 1, this.trackZ + 1))
                byte0 = 5;
        }
        if (byte0 == 1) {
            if (BlockRail.isRailBlockAt(this.worldObj, this.trackX + 1, this.trackY + 1, this.trackZ))
                byte0 = 2;
            if (BlockRail.isRailBlockAt(this.worldObj, this.trackX - 1, this.trackY + 1, this.trackZ))
                byte0 = 3;
        }
        if (byte0 < 0)
            byte0 = 0;
        setConnections(byte0);
        int i = byte0;
        if (this.isPoweredRail)
            i = this.worldObj.getBlockMetadata(this.trackX, this.trackY, this.trackZ) & 0x8 | byte0;
        if (flag1 || this.worldObj.getBlockMetadata(this.trackX, this.trackY, this.trackZ) != i) {
            this.worldObj.setBlockMetadataWithNotify(this.trackX, this.trackY, this.trackZ, i);
            for (int j = 0; j < this.connectedTracks.size(); j++) {
                RailLogic raillogic = getMinecartTrackLogic((ChunkPosition) this.connectedTracks.get(j));
                if (raillogic != null) {
                    raillogic.func_785_b();
                    if (raillogic.handleKeyPress(this))
                        raillogic.func_788_d(this);
                }
            }
        }
    }

    public static int getNAdjacentTracks(RailLogic raillogic) {
        return raillogic.getAdjacentTracks();
    }
}
