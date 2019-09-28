/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin;

import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarRecipeGrid;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CrystalCalculations;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: CrystalCountRecipe
 * Created by HellFirePvP
 * Date: 28.09.2019 / 08:33
 */
public class CrystalCountRecipe extends ConstellationBaseAverageStatsRecipe {

    public CrystalCountRecipe(ResourceLocation recipeId, AltarType altarType, int duration, int starlightRequirement, ItemStack output, AltarRecipeGrid recipeGrid) {
        super(recipeId, altarType, duration, starlightRequirement, output, recipeGrid);
    }

    public static CrystalCountRecipe convertToThis(SimpleAltarRecipe other) {
        return new CrystalCountRecipe(other.getId(), other.getAltarType(), other.getDuration(), other.getStarlightRequirement(), other.getRecipeOutput(), other.getInputs());
    }

    @Nonnull
    @Override
    public ItemStack getOutputForRender(TileAltar altar) {
        ItemStack out = super.getOutputForRender(altar);
        setAmount(out, altar);
        return out;
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs(TileAltar altar) {
        List<ItemStack> out = super.getOutputs(altar);
        out.forEach(stack -> setAmount(stack, altar));
        return out;
    }

    private void setAmount(ItemStack out, TileAltar altar) {
        if (out.getItem() instanceof CrystalAttributeItem) {
            CrystalAttributes attr = ((CrystalAttributeItem) out.getItem()).getAttributes(out);
            if (attr != null && !attr.isEmpty()) {
                out.setCount(CrystalCalculations.getSizeCraftingAmount(attr));
            }
        }
    }
}
