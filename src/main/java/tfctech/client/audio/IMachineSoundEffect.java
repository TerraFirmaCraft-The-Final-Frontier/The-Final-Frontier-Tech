package tfctech.client.audio;

import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;

public interface IMachineSoundEffect
{
    SoundEvent getSoundEvent();

    boolean isPlaying();

    BlockPos getPos();
}
