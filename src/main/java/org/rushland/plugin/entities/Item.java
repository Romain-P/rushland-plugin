package org.rushland.plugin.entities;

import lombok.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.rushland.api.interfaces.database.model.annotations.PrimaryQueryField;
import org.rushland.api.interfaces.database.model.annotations.QueryField;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Item {
    @PrimaryQueryField
    private int id;

    @QueryField
    private String name;

    @QueryField
    private String description;

    @QueryField
    private int grade;

    @QueryField
    private int quantity;

    @QueryField
    private String enchantments;

    @Getter(AccessLevel.NONE)
    private Map<Enchantment, Integer> enchantmentsCache;

    Map<Enchantment, Integer> parseEnchantments0() {
        if (enchantments.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Enchantment, Integer> result = new HashMap<>();

        for (String line : enchantments.split("\\r?\\n")) {
            String[] args = line.split(" -> ");

            Enchantment enchantment = Enchantment.getByName(args[0]);
            int value = Integer.parseInt(args[1]);

            result.put(enchantment, value);
        }

        return result;
    }

    Map<Enchantment, Integer> parseEnchantments() {
        if (enchantmentsCache == null) {
            enchantmentsCache = parseEnchantments0();
        }
        return enchantmentsCache;
    }

    public ItemStack generate() {
        Material material = Material.getMaterial(this.name);

        ItemStack result = this.quantity > 0
                ? new ItemStack(material, this.quantity)
                : new ItemStack(material);

        ItemMeta meta = result.getItemMeta();

        meta.setDisplayName(this.name);
        meta.setLore(Arrays.asList(
                this.description,
                this.grade > 0
                    ? String.format("VIP%d", this.grade)
                    : "NON VIP"
        ));

        for (Map.Entry<Enchantment, Integer> entry : parseEnchantments().entrySet()) {
            meta.addEnchant(entry.getKey(), entry.getValue(), true);
        }

        result.setItemMeta(meta);

        return result;
    }
}
