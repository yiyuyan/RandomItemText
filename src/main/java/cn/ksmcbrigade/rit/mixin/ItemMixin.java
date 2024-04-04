package cn.ksmcbrigade.rit.mixin;

import cn.ksmcbrigade.rit.RandomItemText;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;

@Mixin(Item.class)
public class ItemMixin {

    @Unique
    private Component randomItemText$lastName = null;
    @Unique
    private int randomItemText$nowInterval = 0;
    @Unique
    private boolean randomItemText$restName = false;

    @Inject(method = "getName",at = @At("RETURN"), cancellable = true)
    public void getName(ItemStack p_41458_, CallbackInfoReturnable<Component> cir) throws IOException {
        if(!RandomItemText.init){
            RandomItemText.init();
        }
        boolean change = false;
        if(!RandomItemText.always && !randomItemText$restName){
            change = true;
            randomItemText$restName = true;
        }
        else if(randomItemText$nowInterval==0){
            change = true;
            randomItemText$nowInterval = RandomItemText.interval;
        }
        else{
            randomItemText$nowInterval--;
        }
        if(change){
            randomItemText$lastName = Component.nullToEmpty(RandomItemText.randomString(cir.getReturnValue().getString().length(),RandomItemText.chinese));
            cir.setReturnValue(randomItemText$lastName);
        }
        if(!change){
            if(randomItemText$lastName==null){
                randomItemText$lastName = Component.nullToEmpty(RandomItemText.randomString(cir.getReturnValue().getString().length(),RandomItemText.chinese));
            }
            else{
                cir.setReturnValue(randomItemText$lastName);
            }
        }
    }
}
