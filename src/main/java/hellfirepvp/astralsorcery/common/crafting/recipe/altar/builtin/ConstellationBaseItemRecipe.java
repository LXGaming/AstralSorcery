/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.constellation.ConstellationBaseItem;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: ConstellationBaseItemRecipe
 * Created by HellFirePvP
 * Date: 28.09.2019 / 07:12
 */
public class ConstellationBaseItemRecipe extends SimpleAltarRecipe {

    private static final String KEY_CONSTELLATION = "constellation";

    private IConstellation constellation = null;

    public ConstellationBaseItemRecipe(ResourceLocation recipeId, AltarType altarType, int duration, int starlightRequirement, ItemStack output, AltarRecipeGrid recipeGrid) {
        super(recipeId, altarType, duration, starlightRequirement, output, recipeGrid);
    }

    public static ConstellationBaseItemRecipe convertToThis(SimpleAltarRecipe other) {
        return new ConstellationBaseItemRecipe(other.getId(), other.getAltarType(), other.getDuration(), other.getStarlightRequirement(), other.getRecipeOutput(), other.getInputs());
    }

    @Override
    public void deserializeAdditionalJson(JsonObject recipeObject) throws JsonSyntaxException {
        super.deserializeAdditionalJson(recipeObject);

        if (JSONUtils.hasField(recipeObject, KEY_CONSTELLATION)) {
            ResourceLocation cstName = new ResourceLocation(JSONUtils.getString(recipeObject, KEY_CONSTELLATION));
            IConstellation cst = RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(cstName);
            if (cst != null) {
                this.constellation = cst;
            }
        }
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ItemStack getOutputForRender(TileAltar altar) {
        ItemStack out = super.getOutputForRender(altar);
        setConstellations(out);
        return out;
    }

    @Override
    @Nonnull
    public List<ItemStack> getOutputs(TileAltar altar) {
        List<ItemStack> out = super.getOutputs(altar);
        out.forEach(this::setConstellations);
        return out;
    }

    private void setConstellations(ItemStack out) {
        if (out.getItem() instanceof ConstellationBaseItem) {
            if (this.constellation != null) {
                ((ConstellationBaseItem) out.getItem()).setConstellation(out, this.constellation);
            }
        }
    }

    @Override
    public void writeRecipeSync(PacketBuffer buf) {
        super.writeRecipeSync(buf);

        ByteBufUtils.writeOptional(buf, this.constellation, ByteBufUtils::writeRegistryEntry);
    }

    @Override
    public void readRecipeSync(PacketBuffer buf) {
        super.readRecipeSync(buf);

        this.constellation = ByteBufUtils.readOptional(buf, ByteBufUtils::readRegistryEntry);
    }
}
