package hellfirepvp.astralsorcery.client.effect.fx;

import hellfirepvp.astralsorcery.client.effect.IComplexEffect;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EntityComplexFX
 * Created by HellFirePvP
 * Date: 17.09.2016 / 22:55
 */
public abstract class EntityComplexFX implements IComplexEffect {

    protected int age = 0;
    protected int maxAge = 40;

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public boolean canRemove() {
        return age >= maxAge;
    }

    @Override
    public RenderTarget getRenderTarget() {
        return RenderTarget.RENDERLOOP;
    }

    @Override
    public void tick() {
        age++;
    }

}
