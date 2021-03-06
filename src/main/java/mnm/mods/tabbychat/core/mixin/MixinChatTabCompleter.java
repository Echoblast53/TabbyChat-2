package mnm.mods.tabbychat.core.mixin;

import mnm.mods.tabbychat.util.Translation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat.ChatTabCompleter;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatTabCompleter.class)
public abstract class MixinChatTabCompleter extends TabCompleter {

    @Shadow
    @Final
    private Minecraft client;

    public MixinChatTabCompleter(GuiTextField textFieldIn) {
        super(textFieldIn, false);
    }

    @Inject(
            method = "complete()V",
            at = @At("HEAD"),
            cancellable = true)
    private void onComplete(CallbackInfo ci) {
        if (this.didComplete) {

            if (this.completions.size() > 20 && this.requestedCompletions) {
                ci.cancel();

                ITextComponent chat = new TextComponentTranslation(Translation.WARN_COMPLETIONS, this.completions.size());
                client.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(chat, 1);
            }
        }
    }

}
