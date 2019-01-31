/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2018
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.client.util;

import hellfirepvp.astralsorcery.client.render.tile.TESRTranslucentBlock;
import hellfirepvp.astralsorcery.common.tile.IMultiblockDependantTile;
import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.util.Optional;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: StructureMatchPreview
 * Created by HellFirePvP
 * Date: 03.11.2017 / 09:42
 */
public class StructureMatchPreview {

    public final IMultiblockDependantTile tile;
    private int timeout;

    public StructureMatchPreview(IMultiblockDependantTile tile) {
        this.tile = tile;
        this.timeout = 100;
    }

    public void tick() {
        if (tile.getRequiredStructure() != null && Minecraft.getMinecraft().player != null) {
            BlockPos at = tile.getLocationPos();
            Vec3i v = tile.getRequiredStructure().getSize();
            int maxDim = Math.max(Math.max(v.getX(), v.getY()), v.getZ());
            maxDim = Math.max(9, maxDim);
            if (Minecraft.getMinecraft().player.getDistance(at.getX(), at.getY(), at.getZ()) <= maxDim) {
                resetTimeout();
                return;
            }
        }
        timeout--;
    }

    public void resetTimeout() {
        this.timeout = 300;
    }

    public boolean shouldBeRemoved() {
        return timeout <= 0 ||
                tile.getRequiredStructure() == null ||
                Minecraft.getMinecraft().world == null ||
                Minecraft.getMinecraft().world.provider.getDimension() != ((TileEntity) tile).getWorld().provider.getDimension() ||
                tile.getRequiredStructure().matches(Minecraft.getMinecraft().world, ((TileEntity) tile).getPos()) ||
                ((TileEntity) tile).isInvalid();
    }

    public boolean isOriginatingFrom(IMultiblockDependantTile tile) {
        if (!(tile instanceof TileEntity)) return false;
        if (shouldBeRemoved()) return false;
        return ((TileEntity) this.tile).getPos().equals(((TileEntity) tile).getPos());
    }

    public void appendPreviewBlocks() {
        PatternBlockArray pba = tile.getRequiredStructure();
        if(shouldBeRemoved()) return;
        BlockPos center = ((TileEntity) tile).getPos();
        for (BlockPos key : pba.getPattern().keySet()) {
            if(key.equals(BlockPos.ORIGIN)) continue;
            boolean match = pba.matchSingleBlock(Minecraft.getMinecraft().world, center, key);
            if(!match) {
                TESRTranslucentBlock.addForRender(null, pba.getPattern().get(key).state, center.add(key));
            }
        }
    }

}
