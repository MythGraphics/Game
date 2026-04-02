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
import game.item.ItemEffect.TYPE;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ItemBuilder {

    private final List<String> itemNames;
    private final List<Image>  itemBGImages;
    private final List<Image>  itemGUIImages;
    private final List<ItemEffect> itemEffectList_prä   = new ArrayList<>();
    private final List<ItemEffect> itemEffectList_suff  = new ArrayList<>();

    public ItemBuilder() {
        this.itemNames = new ArrayList<>();
        this.itemBGImages = new ArrayList<>();
        this.itemGUIImages = new ArrayList<>();
    }

    public ItemBuilder(List<String> names, List<Image> bgImgs, List<Image> guiImgs) {
        this.itemNames = names;
        this.itemBGImages = bgImgs;
        this.itemGUIImages = guiImgs;
    }

    public void addItem(String name, Image bgImg, Image guiImg) {
        itemNames.add(name);
        itemBGImages.add(bgImg);
        itemGUIImages.add(guiImg);
    }

    public void addEffect(String effectName, int value, int buff, TYPE effectType, ResourceType r) {
        ItemEffect ie = new ItemEffect(effectName, effectType, r, value, buff);
        if ( effectType == TYPE.PRÄFIX ) {
            itemEffectList_prä.add(ie);
        } else {
            itemEffectList_suff.add(ie);
        }
    }

    public Item createRandomItem() {
        Random r = new Random();
        int rintItem = r.nextInt( itemNames.size() );
        int rintPrä = r.nextInt( itemEffectList_prä.size() );
        int rintSuff = r.nextInt( itemEffectList_suff.size() );
        return createItem(
            itemNames.get(rintItem),
            itemBGImages.get(rintItem),
            itemGUIImages.get(rintItem),
            itemEffectList_prä.get(rintPrä),
            itemEffectList_suff.get(rintSuff)
        );
    }

    public static Item createCoinPouch(String präfix, int value) {
        Item item = new Item( 0, "Münzbeutel" );
        item.setPrice(value);
        item.addItemEffect( new ItemEffect( präfix, TYPE.PRÄFIX, ResourceType.CREDIT, value ));
        return item;
    }

    public static Item createItem(String name, Image bgImg, Image uiImg, ItemEffect effect_prä, ItemEffect effect_suff) {
        Item item = new Item( ID.getNextItemID(), name );
        item.addItemEffect(effect_prä, effect_suff);
        item.setImg(bgImg);
        item.setIcon(uiImg);
        return item;
    }

}
