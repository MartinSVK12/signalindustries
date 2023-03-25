package sunsetsatellite.signalindustries.dim.worldgen;

import net.minecraft.src.*;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Random;

public class WorldGenEternalTreeFancy extends WorldGenerator {
    protected int leavesID;
    protected int logID;
    static final byte[] field_882_a = new byte[]{2, 0, 0, 1, 2, 1};
    Random rand = new Random();
    World worldObj;
    int[] basePos = new int[]{0, 0, 0};
    int treeHeight = 0;
    int height;
    double field_876_g = 0.6;
    double field_874_i = 0.4;
    double field_873_j = 1.0;
    double field_872_k = 1.0;
    int field_871_l = 1;
    int treeMaxHeight = 12;
    int field_869_n = 4;
    int[][] field_868_o;
    int heightMod;

    public WorldGenEternalTreeFancy(int leavesID, int logID) {
        this.leavesID = leavesID;
        this.logID = logID;
        this.heightMod = 0;
    }

    public WorldGenEternalTreeFancy(int leavesID, int logID, int heightMod) {
        this.leavesID = leavesID;
        this.logID = logID;
        this.heightMod = heightMod;
    }

    void func_521_a() {
        this.height = (int)((double)this.treeHeight * this.field_876_g);
        if (this.height >= this.treeHeight) {
            this.height = this.treeHeight - 1;
        }

        int i = (int)(1.381 + Math.pow(this.field_872_k * (double)this.treeHeight / 7.0, 2.0));
        if (i < 1) {
            i = 1;
        }

        int[][] ai = new int[i * this.treeHeight][4];
        int j = this.basePos[1] + this.treeHeight - this.field_869_n;
        int k = 1;
        int l = this.basePos[1] + this.height;
        int i1 = j - this.basePos[1];
        ai[0][0] = this.basePos[0];
        ai[0][1] = j;
        ai[0][2] = this.basePos[2];
        ai[0][3] = l;
        --j;

        while(true) {
            while(i1 >= 0) {
                int j1 = 0;
                float f = this.func_528_a(i1);
                if (f < 0.0F) {
                    --j;
                    --i1;
                } else {
                    for(double d = 0.5; j1 < i; ++j1) {
                        double d1 = this.field_873_j * (double)f * ((double)this.rand.nextFloat() + 0.328);
                        double d2 = (double)this.rand.nextFloat() * 2.0 * 3.14159;
                        int k1 = MathHelper.floor_double(d1 * Math.sin(d2) + (double)this.basePos[0] + d);
                        int l1 = MathHelper.floor_double(d1 * Math.cos(d2) + (double)this.basePos[2] + d);
                        int[] ai1 = new int[]{k1, j, l1};
                        int[] ai2 = new int[]{k1, j + this.field_869_n, l1};
                        if (this.func_524_a(ai1, ai2) == -1) {
                            int[] ai3 = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
                            double d3 = Math.sqrt(Math.pow((double)Math.abs(this.basePos[0] - ai1[0]), 2.0) + Math.pow((double)Math.abs(this.basePos[2] - ai1[2]), 2.0));
                            double d4 = d3 * this.field_874_i;
                            if ((double)ai1[1] - d4 > (double)l) {
                                ai3[1] = l;
                            } else {
                                ai3[1] = (int)((double)ai1[1] - d4);
                            }

                            if (this.func_524_a(ai3, ai1) == -1) {
                                ai[k][0] = k1;
                                ai[k][1] = j;
                                ai[k][2] = l1;
                                ai[k][3] = ai3[1];
                                ++k;
                            }
                        }
                    }

                    --j;
                    --i1;
                }
            }

            this.field_868_o = new int[k][4];
            System.arraycopy(ai, 0, this.field_868_o, 0, k);
            return;
        }
    }

    void placeLeaves(int i, int j, int k, float f, byte byte0, int l) {
        int i1 = (int)((double)f + 0.618);
        byte byte1 = field_882_a[byte0];
        byte byte2 = field_882_a[byte0 + 3];
        int[] ai = new int[]{i, j, k};
        int[] ai1 = new int[]{0, 0, 0};
        int j1 = -i1;
        int k1 = -i1;

        label33:
        for(ai1[byte0] = ai[byte0]; j1 <= i1; ++j1) {
            ai1[byte1] = ai[byte1] + j1;
            int l1 = -i1;

            while(true) {
                while(true) {
                    if (l1 > i1) {
                        continue label33;
                    }

                    double d = Math.sqrt(Math.pow((double)Math.abs(j1) + 0.5, 4.0) + Math.pow((double)Math.abs(l1) + 0.5, 4.0));
                    if (d > (double)f) {
                        ++l1;
                    } else {
                        ai1[byte2] = ai[byte2] + l1;
                        int i2 = this.worldObj.getBlockId(ai1[0], ai1[1], ai1[2]);
                        if (i2 != 0 && i2 != this.leavesID) {
                            ++l1;
                        } else {
                            this.worldObj.setBlockWithNotify(ai1[0], ai1[1], ai1[2], l);
                            ++l1;
                        }
                    }
                }
            }
        }

    }

    float func_528_a(int i) {
        if ((double)i < (double)((float)this.treeHeight) * 0.3) {
            return -1.618F;
        } else {
            float f = (float)this.treeHeight / 2.0F;
            float f1 = (float)this.treeHeight / 2.0F - (float)i;
            float f2;
            if (f1 == 0.0F) {
                f2 = f;
            } else if (Math.abs(f1) >= f) {
                f2 = 0.0F;
            } else {
                f2 = (float)Math.sqrt(Math.pow((double)Math.abs(f), 2.0) - Math.pow((double)Math.abs(f1), 2.0));
            }

            f2 *= 0.5F;
            return f2;
        }
    }

    float func_526_b(int i) {
        if (i >= 0 && i < this.field_869_n) {
            return i != 0 && i != this.field_869_n - 1 ? 3.0F : 2.0F;
        } else {
            return -1.0F;
        }
    }

    void func_520_a(int i, int j, int k) {
        int l = j;

        for(int i1 = j + this.field_869_n; l < i1; ++l) {
            float f = this.func_526_b(l - j);
            this.placeLeaves(i, l, k, f, (byte)1, this.leavesID);
        }

    }

    void func_518_b() {
        int i = 0;

        for(int j = this.field_868_o.length; i < j; ++i) {
            int k = this.field_868_o[i][0];
            int l = this.field_868_o[i][1];
            int i1 = this.field_868_o[i][2];
            this.func_520_a(k, l, i1);
        }

    }

    void placeWood(int[] ai, int[] ai1, int i) {
        int[] ai2 = new int[]{0, 0, 0};
        byte byte0 = 0;

        byte j;
        for(j = 0; byte0 < 3; ++byte0) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[j])) {
                j = byte0;
            }
        }

        if (ai2[j] != 0) {
            byte byte1 = field_882_a[j];
            byte byte2 = field_882_a[j + 3];
            byte byte3;
            if (ai2[j] > 0) {
                byte3 = 1;
            } else {
                byte3 = -1;
            }

            double d = (double)ai2[byte1] / (double)ai2[j];
            double d1 = (double)ai2[byte2] / (double)ai2[j];
            int[] ai3 = new int[]{0, 0, 0};
            int k = 0;

            for(int l = ai2[j] + byte3; k != l; k += byte3) {
                ai3[j] = MathHelper.floor_double((double)(ai[j] + k) + 0.5);
                ai3[byte1] = MathHelper.floor_double((double)ai[byte1] + (double)k * d + 0.5);
                ai3[byte2] = MathHelper.floor_double((double)ai[byte2] + (double)k * d1 + 0.5);
                this.worldObj.setBlockWithNotify(ai3[0], ai3[1], ai3[2], i);
            }

        }
    }

    boolean func_527_c(int i) {
        return (double)i >= (double)this.treeHeight * 0.2;
    }

    void func_529_c() {
        int i = this.basePos[0];
        int j = this.basePos[1];
        int k = this.basePos[1] + this.height;
        int l = this.basePos[2];
        int[] ai = new int[]{i, j, l};
        int[] ai1 = new int[]{i, k, l};
        this.placeWood(ai, ai1, this.logID);
        if (this.field_871_l == 2) {
            int var10002 = ai[0]++;
            var10002 = ai1[0]++;
            this.placeWood(ai, ai1, this.logID);
            var10002 = ai[2]++;
            var10002 = ai1[2]++;
            this.placeWood(ai, ai1, this.logID);
            var10002 = ai[0]--;
            var10002 = ai1[0]--;
            this.placeWood(ai, ai1, this.logID);
        }

    }

    void func_525_d() {
        int i = 0;
        int j = this.field_868_o.length;

        for(int[] ai = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]}; i < j; ++i) {
            int[] ai1 = this.field_868_o[i];
            int[] ai2 = new int[]{ai1[0], ai1[1], ai1[2]};
            ai[1] = ai1[3];
            int k = ai[1] - this.basePos[1];
            if (this.func_527_c(k)) {
                this.placeWood(ai, ai2, this.logID);
            }
        }

    }

    int func_524_a(int[] ai, int[] ai1) {
        int[] ai2 = new int[]{0, 0, 0};
        byte byte0 = 0;

        byte i;
        for(i = 0; byte0 < 3; ++byte0) {
            ai2[byte0] = ai1[byte0] - ai[byte0];
            if (Math.abs(ai2[byte0]) > Math.abs(ai2[i])) {
                i = byte0;
            }
        }

        if (ai2[i] == 0) {
            return -1;
        } else {
            byte byte1 = field_882_a[i];
            byte byte2 = field_882_a[i + 3];
            byte byte3;
            if (ai2[i] > 0) {
                byte3 = 1;
            } else {
                byte3 = -1;
            }

            double d = (double)ai2[byte1] / (double)ai2[i];
            double d1 = (double)ai2[byte2] / (double)ai2[i];
            int[] ai3 = new int[]{0, 0, 0};
            int j = 0;

            int k;
            for(k = ai2[i] + byte3; j != k; j += byte3) {
                ai3[i] = ai[i] + j;
                ai3[byte1] = MathHelper.floor_double((double)ai[byte1] + (double)j * d);
                ai3[byte2] = MathHelper.floor_double((double)ai[byte2] + (double)j * d1);
                int l = this.worldObj.getBlockId(ai3[0], ai3[1], ai3[2]);
                if (l != 0 && l != this.leavesID) {
                    break;
                }
            }

            return j == k ? -1 : Math.abs(j);
        }
    }

    boolean func_519_e() {
        int[] ai = new int[]{this.basePos[0], this.basePos[1], this.basePos[2]};
        int[] ai1 = new int[]{this.basePos[0], this.basePos[1] + this.treeHeight - 1, this.basePos[2]};
        int i = this.worldObj.getBlockId(this.basePos[0], this.basePos[1] - 1, this.basePos[2]);
        int j = this.func_524_a(ai, ai1);
        if (j == -1) {
            return true;
        } else if (j < 6) {
            return false;
        } else {
            this.treeHeight = j;
            return true;
        }
    }

    public void func_517_a(double d, double d1, double d2) {
        this.treeMaxHeight = (int)(d * 20.0);
        if (d > 0.5) {
            this.field_869_n = 5;
        }

        this.field_873_j = d1;
        this.field_872_k = d2;
    }

    public boolean generate(World world, Random random, int i, int j, int k) {
        this.worldObj = world;
        long l = random.nextLong();
        this.rand.setSeed(l);
        this.basePos[0] = i;
        this.basePos[1] = j;
        this.basePos[2] = k;
        if (this.treeHeight == 0) {
            this.treeHeight = 5 + this.rand.nextInt(this.treeMaxHeight);
        }



        if (!this.func_519_e()) {
            return false;
        } else {
            for (int x = -3; x < 2; x++) {
                for (int z = -3; z < 2; z++) {
                    if(random.nextInt(2) == 0){
                        world.setBlockWithNotify(this.basePos[0]+x,this.basePos[1]-1,this.basePos[2]+z, SignalIndustries.rootedFabric.blockID);
                    }
                }
            }
            this.func_521_a();
            this.func_518_b();
            this.func_529_c();
            this.func_525_d();
            return true;
        }
    }
}
