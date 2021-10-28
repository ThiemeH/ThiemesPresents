package nl.thieme.tp.models;

import com.cryptomorin.xseries.XSound;

public class FullSound {

    private XSound sound;
    private Float volume;
    private Float pitch;

    public FullSound(XSound sound, Float volume, Float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Float getPitch() {
        return pitch;
    }

    public Float getVolume() {
        return volume;
    }

    public XSound getXSound() {
        return sound;
    }
}
