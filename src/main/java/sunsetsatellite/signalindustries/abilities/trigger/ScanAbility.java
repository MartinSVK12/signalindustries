package sunsetsatellite.signalindustries.abilities.trigger;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIBlocks;

import java.util.ArrayList;
import java.util.HashMap;

public class ScanAbility extends TriggerBaseEffectAbility {

    public static HashMap<Block,OreInfo> oreMap = new HashMap<>();

    public ScanAbility(String name, int cost, int cooldown, int effectTime, int costPerTick) {
        super(name, cost, cooldown, effectTime, costPerTick);
    }

    @Override
    public void deactivate(int x, int y, int z, Player player, World world, ItemStack trigger, ItemStack harness) {
        deactivate(player, world, trigger, harness);
    }

    @Override
    public void deactivate(Player player, World world, ItemStack trigger, ItemStack harness) {
        oreMap.clear();
    }

    @Override
    public void tick(Player player, World world, ItemStack trigger, ItemStack harness) {

    }

    @Override
    public void activate(int x, int y, int z, Player player, World world, ItemStack trigger, ItemStack harness) {
        activate(player, world, trigger, harness);
    }

    @Override
    public void activate(Player player, World world, ItemStack trigger, ItemStack harness) {
        int range = 16;

        oreMap.clear();

        oreMap.put(Blocks.ORE_COAL_STONE,new OreInfo());
        oreMap.put(Blocks.ORE_IRON_STONE,new OreInfo());
        oreMap.put(Blocks.ORE_GOLD_STONE,new OreInfo());
        oreMap.put(Blocks.ORE_LAPIS_STONE,new OreInfo());
        oreMap.put(Blocks.ORE_REDSTONE_STONE,new OreInfo());

        oreMap.put(Blocks.ORE_COAL_BASALT,new OreInfo());
        oreMap.put(Blocks.ORE_IRON_BASALT,new OreInfo());
        oreMap.put(Blocks.ORE_GOLD_BASALT,new OreInfo());
        oreMap.put(Blocks.ORE_LAPIS_BASALT,new OreInfo());
        oreMap.put(Blocks.ORE_REDSTONE_BASALT,new OreInfo());

        oreMap.put(Blocks.ORE_COAL_LIMESTONE,new OreInfo());
        oreMap.put(Blocks.ORE_IRON_LIMESTONE,new OreInfo());
        oreMap.put(Blocks.ORE_GOLD_LIMESTONE,new OreInfo());
        oreMap.put(Blocks.ORE_LAPIS_LIMESTONE,new OreInfo());
        oreMap.put(Blocks.ORE_REDSTONE_LIMESTONE,new OreInfo());

        oreMap.put(Blocks.ORE_COAL_GRANITE,new OreInfo());
        oreMap.put(Blocks.ORE_IRON_GRANITE,new OreInfo());
        oreMap.put(Blocks.ORE_GOLD_GRANITE,new OreInfo());
        oreMap.put(Blocks.ORE_LAPIS_GRANITE,new OreInfo());
        oreMap.put(Blocks.ORE_REDSTONE_GRANITE,new OreInfo());

        oreMap.put(SIBlocks.signalumOre,new OreInfo());
        oreMap.put(SIBlocks.dilithiumOre,new OreInfo());

        for (int i = -range; i < range; i++) {
            for (int j = -range; j < range; j++) {
                for (int k = 0; k < world.getHeightValue((int) (player.x + i), (int) (player.z + j)); k++) {
                    int blockId = world.getBlockId((int) (player.x + i), k, (int) (player.z + j));
                    int finalK = k;
                    int finalI = (int) (player.x + i);
                    int finalJ = (int) (player.z + j);
                    oreMap.forEach((block, oreInfo) -> {
                        if(blockId == block.id()){
                            oreInfo.count++;
                            oreInfo.positions.add(new Vec3i(finalI,finalK, finalJ));
                        }
                    });
                }
            }
        }

        player.sendMessage("--- SCAN RESULTS ---");
        oreMap.forEach((block, oreInfo) -> {
            if(oreInfo.count > 0){
                player.sendMessage(String.format("%s%s | Count: %s%d",block.asItem().getTranslatedName(block.getDefaultStack()), TextFormatting.LIGHT_GRAY, TextFormatting.WHITE, oreInfo.count));
            }
        });
        player.sendMessage("--------------------");
    }

    public static class OreInfo {
        public int count = 0;
        public ArrayList<Vec3i> positions = new ArrayList<>();

        public OreInfo() {}
    }
}
