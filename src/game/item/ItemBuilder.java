/*
 *
 */

package game.item;

/**
 *
 * @author  Martin Pröhl alias MythGraphics
 * @version 1.0.0
 *
 */

import game.ID;
import game.Resource.ResourceType;
import static game.item.ItemEffect.ValueType.*;
import game.item.ItemEffect.ItemEffectType;
import static game.item.ItemEffect.ItemEffectType.PRÄFIX;
import static game.item.ItemEffect.ItemEffectType.SUFFIX;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemBuilder {

    private final List<String> itemNames;
    private final List<BufferedImage> itemBGImages;
    private final List<Image> itemGUIImages;
    private final List<ItemEffect> itemEffectList_prä   = new ArrayList<>();
    private final List<ItemEffect> itemEffectList_suff  = new ArrayList<>();

    public ItemBuilder() {
        this.itemNames = new ArrayList<>();
        this.itemBGImages = new ArrayList<>();
        this.itemGUIImages = new ArrayList<>();
    }

    public ItemBuilder(List<String> names, List<BufferedImage> bgImgs, List<Image> guiImgs) {
        this.itemNames      = names;
        this.itemBGImages   = bgImgs;
        this.itemGUIImages  = guiImgs;
    }

    public void addItem(String name, BufferedImage bgImg, Image guiImg) {
        itemNames.add(name);
        itemBGImages.add(bgImg);
        itemGUIImages.add(guiImg);
    }

    public Item createRandomItem() {
        Random r = new Random();
        int rintItem = r.nextInt( itemNames.size() );
        int rintPrä  = r.nextInt( itemEffectList_prä.size() );
        int rintSuff = r.nextInt( itemEffectList_suff.size() );
        return createItem(
            itemNames.get(rintItem),
            itemBGImages.get(rintItem),
            itemGUIImages.get(rintItem),
            itemEffectList_prä.get(rintPrä),
            itemEffectList_suff.get(rintSuff)
        );
    }

    public static UsableItem createCoinPouch(String suffix, int value) {
        UsableItem item = new UsableItem( 0, "Münzbeutel" );
        item.setPrice(value);
        item.addItemEffect( new ItemEffect( suffix, SUFFIX, ResourceType.CREDIT, value, ABSOLUTE ));
        return item;
    }

    public static ReUsableItem createItem(
        String name, BufferedImage bgImg, Image uiImg, ItemEffect effect_prä, ItemEffect effect_suff
    ) {
        ReUsableItem item = new ReUsableItem( ID.getNextItemId(), name );
        item.addItemEffect(effect_prä, effect_suff);
        item.setImg(bgImg);
        item.setIcon(uiImg);
        return item;
    }

}
