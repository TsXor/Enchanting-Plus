package net.darkhax.eplus.api;

import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;


/**
 * Entry of <code>ModBookTextures</code>. Wrapper of a <code>RenderMaterial</code>.
 */
@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class BookTexture extends ForgeRegistryEntry<BookTexture> {
    public final RenderMaterial material;

    public BookTexture(RenderMaterial material) {
        this.material = material;
    }

    /**
     * Make an instance from a resource path.
     * <br/>Path is like <code>"${ModID}:${TextureFilePath}"</code>, for example, <code>"eplus:textures/entity/enchantingplus_book.png"</code>.
     * @param resourceName texture path
     */
    public static BookTexture at(String resourceName) {
        return new BookTexture(
                new RenderMaterial(
                        AtlasTexture.LOCATION_BLOCKS,
                        ResourceLocation.of(resourceName, ':')
                )
        );
    }

    /**
     * Make a supplier from a resource path.
     * <br/> For path format, see {@link BookTexture#at(String)}.
     * @param resourceName texture path
     * @return a builder that can be used in {@link DeferredRegister#register(String, Supplier)}
     */
    public static Supplier<BookTexture> builderOfPath(String resourceName) {
        return () -> BookTexture.at(resourceName);
    }
}
