package tld.sima.armorstand.utils;

import org.bukkit.inventory.ItemStack;

public class CustomArmorstandTool {
    private ItemStack tool;
    private ToolType toolType;
    private double fuzzyRadius;

    public CustomArmorstandTool(){
        this.toolType = ToolType.NONE;
        this.tool = null;
        this.fuzzyRadius = 2;
    }

    public CustomArmorstandTool(ItemStack tool, ToolType toolType){
        this.tool = tool;
        this.toolType = toolType;
    }

    public void setToolType(ToolType toolType){
        this.toolType = toolType;
    }

    public void setTool(ItemStack tool){
        this.tool = tool;
    }

    public void setFuzzyRadius(double radius){
        this.fuzzyRadius = radius;
    }

    public ToolType getToolType(){
        return this.toolType;
    }

    public boolean isTool(ItemStack compare){
        if (this.tool != null) {
            return this.tool.isSimilar(compare);
        }else{
            return false;
        }
    }

    public double getFuzzyRadius(){
        return this.fuzzyRadius;
    }
}
