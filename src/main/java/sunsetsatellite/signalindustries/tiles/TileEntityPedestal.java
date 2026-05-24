package sunsetsatellite.signalindustries.tiles;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.pos.TilePos;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class TileEntityPedestal extends TileEntity {

    public int ticks;
    public float flip;

    public float flipPrev;
    public float flipRot;
    public float flipRot2;

    public float bookSpread;

    public float bookSpreadPrev;
    public float bookRot2;
    public float bookRot;
    public float bookRotPrev;

    public static Random rand = new Random();

    @Override
    public void tick() {
        super.tick();
        if (worldObj == null) return;
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotPrev = this.bookRot2;
        Player closestPlayer = this.worldObj.getClosestPlayer(tilePos.x + 0.5f, tilePos.y + 0.5f, tilePos.z + 0.5f, 3);
        if (closestPlayer != null) {
            double xPos = closestPlayer.x - tilePos.x + 0.5f;
            double zPos = closestPlayer.z - tilePos.z + 0.5f;
            this.bookRot = 0;
            //this.bookRot = (float) Math.atan2(zPos, xPos);
            this.bookSpread += 0.1f;

            if (bookSpreadPrev < 0.5f || rand.nextInt(40) == 0) {
                float startingFlipRot = this.flipRot;
                do {
                    this.flipRot += (float) (rand.nextInt(4) - rand.nextInt(4));
                } while (startingFlipRot == this.flipRot);
            }
        } else {
            this.bookRot += 0.02f;
            this.bookSpread -= 0.1f;
        }

        while (this.bookRot2 >= (float) Math.PI * 2) {
            this.bookRot2 -= ((float) Math.PI * 4F);
        }

        while (this.bookRot2 < -(float) Math.PI * 2) {
            this.bookRot2 += ((float) Math.PI * 4F);
        }

        while (this.bookRot >= (float) Math.PI * 2) {
            this.bookRot -= ((float) Math.PI * 4F);
        }

        while (this.bookRot < -(float) Math.PI * 2) {
            this.bookRot += ((float) Math.PI * 4F);
        }

        float startingBookRot = this.bookRot - this.bookRot2;
        while (startingBookRot >= (float) Math.PI * 2) {
            startingBookRot -= ((float) Math.PI * 4F);
        }

        while (startingBookRot < -(float) Math.PI * 2) {
            startingBookRot += ((float) Math.PI * 4F);
        }

        this.bookRot2 = startingBookRot * 0.4f;

        if (this.bookSpread < 0f) {
            this.bookSpread = 0f;
        }

        if (bookSpread > 1f) {
            bookSpread = 1f;
        }

        ++this.ticks;
        flipPrev = flip;

        float currentFlip = (flipRot - flip) * 0.4f;
        if (currentFlip < -0.2f) {
            currentFlip = -0.2f;
        }
        if (currentFlip > 0.2f) {
            currentFlip = 0.2f;
        }
        flipRot2 += (currentFlip - flipRot2) * 0.9f;
        flip += flipRot2;
    }

	@Override
	public void readAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	@Override
	public void writeAdditionalData(@NotNull CompoundTag compoundTag) {

	}
}
