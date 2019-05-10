package vazkii.botania.common.integration.curios;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.CuriosAPI;
import top.theillusivec4.curios.api.capability.CuriosCapability;
import top.theillusivec4.curios.api.capability.ICurio;
import top.theillusivec4.curios.api.imc.CurioIMCMessage;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemIcePendant;
import vazkii.botania.common.item.equipment.bauble.ItemKnockbackBelt;
import vazkii.botania.common.item.equipment.bauble.ItemReachRing;
import vazkii.botania.common.item.equipment.bauble.ItemWaterRing;
import vazkii.botania.common.item.relic.ItemOdinRing;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// Classloading-safe way to attach curio behaviour to our items
public class CurioIntegration {
    private static final ResourceLocation KEY = new ResourceLocation(LibMisc.MOD_ID, "curio");

    private static class Provider implements ICapabilityProvider {
        private final ICurio curio;
        private final LazyOptional<ICurio> curioCap;

        public Provider(ICurio curio) {
            this.curio = curio;
            curioCap = LazyOptional.of(() -> this.curio);
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
            return CuriosCapability.ITEM.orEmpty(cap, curioCap);
        }
    }

    public static void sendImc(InterModEnqueueEvent evt) {
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("charm"));
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("ring"));
        InterModComms.sendTo("curios", CuriosAPI.IMC.REGISTER_TYPE, () -> new CurioIMCMessage("belt"));
    }

    public static void attachCaps(AttachCapabilitiesEvent<ItemStack> evt) {
        ItemStack stack = evt.getObject();

        if(stack.getItem() == ModItems.icePendant) {
            evt.addCapability(KEY, new Provider(new ItemIcePendant.Curio(stack)));
        } else if (stack.getItem() == ModItems.waterRing) {
            evt.addCapability(KEY, new Provider(new ItemWaterRing.Curio(stack)));
        } else if (stack.getItem() == ModItems.knockbackBelt) {
            evt.addCapability(KEY, new Provider(new ItemKnockbackBelt.Curio(stack)));
        } else if (stack.getItem() == ModItems.odinRing) {
            evt.addCapability(KEY, new Provider(new ItemOdinRing.Curio(stack, ((ItemOdinRing) ModItems.odinRing).getDummy())));
        } else if (stack.getItem() == ModItems.reachRing) {
            evt.addCapability(KEY, new Provider(new ItemReachRing.Curio(stack)));
        }
    }
}