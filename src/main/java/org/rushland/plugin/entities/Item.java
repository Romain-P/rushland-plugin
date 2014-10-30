package org.rushland.plugin.entities;

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

    private Map<Enchantment, Integer> enchantmentsCache;

    public Item() {
    }

    public Item(int id, String name, String description, int grade, int quantity, String enchantments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.grade = grade;
        this.quantity = quantity;
        this.enchantments = enchantments;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getEnchantments() {
        return enchantments;
    }

    public void setEnchantments(String enchantments) {
        this.enchantments = enchantments;
    }
}
