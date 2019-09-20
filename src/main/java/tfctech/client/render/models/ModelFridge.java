package tfctech.client.render.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFridge extends ModelBase
{
    private final ModelRenderer outside_box;
    private final ModelRenderer machine;
    private final ModelRenderer coils1;
    private final ModelRenderer coils1part1;
    private final ModelRenderer coils1part2;
    private final ModelRenderer coils1part3;
    private final ModelRenderer coils1part4;
    private final ModelRenderer coils2;
    private final ModelRenderer coils2part1;
    private final ModelRenderer coils2part2;
    private final ModelRenderer coils2part3;
    private final ModelRenderer coils2part4;
    private final ModelRenderer casing;
    private final ModelRenderer door;
    private final ModelRenderer stand1;
    private final ModelRenderer stand2;
    private final ModelRenderer stand3;
    private final ModelRenderer stand4;

    private float open = 0.0F;

    public ModelFridge() {
        textureWidth = 128;
        textureHeight = 64;

        outside_box = new ModelRenderer(this);
        outside_box.setRotationPoint(-8.0F, 16.0F, 8.0F);
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 6, 14.5F, -19.0F, -13.0F, 1, 24, 12, 0.0F, false));
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 0, 0.5F, -19.0F, -1.0F, 15, 24, 1, 0.0F, false));
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 6, 0.5F, -19.0F, -13.0F, 1, 24, 12, 0.0F, false));
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 7, 0.0F, -20.0F, -14.0F, 16, 1, 14, 0.0F, false));
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 6, 0.0F, 5.0F, -13.0F, 16, 1, 13, 0.0F, false));
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 1, 0.0F, 6.0F, -2.0F, 2, 2, 2, 0.0F, false));
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 1, 0.0F, 6.0F, -13.0F, 2, 2, 2, 0.0F, false));
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 1, 14.0F, 6.0F, -13.0F, 2, 2, 2, 0.0F, false));
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 1, 14.0F, 6.0F, -2.0F, 2, 2, 2, 0.0F, false));
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 0, 15.5F, -15.0F, -13.2F, 0, 2, 0, 0.0F, false));
        outside_box.cubeList.add(new ModelBox(outside_box, 0, 0, 15.5F, -1.0F, -13.2F, 0, 2, 0, 0.0F, false));

        machine = new ModelRenderer(this);
        machine.setRotationPoint(-8.0F, 16.0F, 0.0F);
        machine.cubeList.add(new ModelBox(machine, 0, 0, 9.1F, -23.0F, -3.3F, 0, 3, 0, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 0, 10.6F, -23.0F, -3.3F, 0, 3, 0, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 0, 10.6F, -23.0F, 5.0F, 0, 3, 0, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 0, 9.1F, -23.0F, 5.0F, 0, 3, 0, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 0, 7.6F, -23.0F, -3.3F, 0, 3, 0, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 0, 7.6F, -23.0F, 5.0F, 0, 3, 0, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 0, 4.6F, -23.0F, -3.3F, 0, 3, 0, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 0, 4.6F, -23.0F, 5.0F, 0, 3, 0, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 0, 6.1F, -23.0F, -3.3F, 0, 3, 0, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 0, 6.1F, -23.0F, 5.0F, 0, 3, 0, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 4, 4.0F, -23.0F, -3.0F, 8, 3, 8, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 4, 10.6F, -23.5F, -3.0F, 0, 0, 8, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 4, 9.1F, -23.5F, -3.0F, 0, 0, 8, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 4, 7.6F, -23.5F, -3.0F, 0, 0, 8, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 4, 6.1F, -23.5F, -3.0F, 0, 0, 8, 0.0F, false));
        machine.cubeList.add(new ModelBox(machine, 0, 4, 4.6F, -23.5F, -3.0F, 0, 0, 8, 0.0F, false));

        coils1 = new ModelRenderer(this);
        coils1.setRotationPoint(-8.0F, 16.0F, 8.0F);

        coils1part1 = new ModelRenderer(this);
        coils1part1.setRotationPoint(0.0F, 0.0F, 0.0F);
        coils1.addChild(coils1part1);
        coils1part1.cubeList.add(new ModelBox(coils1part1, 0, 0, 1.7F, -1.5F, -3.0F, 0, 4, 1, 0.0F, false));
        coils1part1.cubeList.add(new ModelBox(coils1part1, 0, 1, 1.7F, 1.5F, -5.0F, 0, 1, 2, 0.0F, false));
        coils1part1.cubeList.add(new ModelBox(coils1part1, 0, 1, 1.7F, -1.5F, -8.0F, 0, 1, 2, 0.0F, false));
        coils1part1.cubeList.add(new ModelBox(coils1part1, 0, 0, 1.7F, -1.5F, -6.0F, 0, 4, 1, 0.0F, false));
        coils1part1.cubeList.add(new ModelBox(coils1part1, 0, 0, 1.7F, -1.5F, -9.0F, 0, 4, 1, 0.0F, false));
        coils1part1.cubeList.add(new ModelBox(coils1part1, 0, 0, 1.7F, -1.5F, -12.0F, 0, 4, 1, 0.0F, false));
        coils1part1.cubeList.add(new ModelBox(coils1part1, 0, 1, 1.7F, 1.5F, -11.0F, 0, 1, 2, 0.0F, false));

        coils1part2 = new ModelRenderer(this);
        coils1part2.setRotationPoint(0.0F, 0.0F, 0.0F);
        coils1.addChild(coils1part2);
        coils1part2.cubeList.add(new ModelBox(coils1part2, 0, 0, 1.7F, -6.5F, -3.0F, 0, 4, 1, 0.0F, false));
        coils1part2.cubeList.add(new ModelBox(coils1part2, 0, 1, 1.7F, -3.5F, -5.0F, 0, 1, 2, 0.0F, false));
        coils1part2.cubeList.add(new ModelBox(coils1part2, 0, 1, 1.7F, -6.5F, -8.0F, 0, 1, 2, 0.0F, false));
        coils1part2.cubeList.add(new ModelBox(coils1part2, 0, 0, 1.7F, -6.5F, -6.0F, 0, 4, 1, 0.0F, false));
        coils1part2.cubeList.add(new ModelBox(coils1part2, 0, 0, 1.7F, -6.5F, -9.0F, 0, 4, 1, 0.0F, false));
        coils1part2.cubeList.add(new ModelBox(coils1part2, 0, 0, 1.7F, -6.5F, -12.0F, 0, 4, 1, 0.0F, false));
        coils1part2.cubeList.add(new ModelBox(coils1part2, 0, 1, 1.7F, -3.5F, -11.0F, 0, 1, 2, 0.0F, false));

        coils1part3 = new ModelRenderer(this);
        coils1part3.setRotationPoint(0.0F, 0.0F, 0.0F);
        coils1.addChild(coils1part3);
        coils1part3.cubeList.add(new ModelBox(coils1part3, 0, 0, 1.7F, -11.5F, -3.0F, 0, 4, 1, 0.0F, false));
        coils1part3.cubeList.add(new ModelBox(coils1part3, 0, 1, 1.7F, -8.5F, -5.0F, 0, 1, 2, 0.0F, false));
        coils1part3.cubeList.add(new ModelBox(coils1part3, 0, 1, 1.7F, -11.5F, -8.0F, 0, 1, 2, 0.0F, false));
        coils1part3.cubeList.add(new ModelBox(coils1part3, 0, 0, 1.7F, -11.5F, -6.0F, 0, 4, 1, 0.0F, false));
        coils1part3.cubeList.add(new ModelBox(coils1part3, 0, 0, 1.7F, -11.5F, -9.0F, 0, 4, 1, 0.0F, false));
        coils1part3.cubeList.add(new ModelBox(coils1part3, 0, 0, 1.7F, -11.5F, -12.0F, 0, 4, 1, 0.0F, false));
        coils1part3.cubeList.add(new ModelBox(coils1part3, 0, 1, 1.7F, -8.5F, -11.0F, 0, 1, 2, 0.0F, false));

        coils1part4 = new ModelRenderer(this);
        coils1part4.setRotationPoint(0.0F, 0.0F, 0.0F);
        coils1.addChild(coils1part4);
        coils1part4.cubeList.add(new ModelBox(coils1part4, 0, 0, 1.7F, -16.5F, -3.0F, 0, 4, 1, 0.0F, false));
        coils1part4.cubeList.add(new ModelBox(coils1part4, 0, 1, 1.7F, -13.5F, -5.0F, 0, 1, 2, 0.0F, false));
        coils1part4.cubeList.add(new ModelBox(coils1part4, 0, 1, 1.7F, -16.5F, -8.0F, 0, 1, 2, 0.0F, false));
        coils1part4.cubeList.add(new ModelBox(coils1part4, 0, 0, 1.7F, -16.5F, -6.0F, 0, 4, 1, 0.0F, false));
        coils1part4.cubeList.add(new ModelBox(coils1part4, 0, 0, 1.7F, -16.5F, -9.0F, 0, 4, 1, 0.0F, false));
        coils1part4.cubeList.add(new ModelBox(coils1part4, 0, 0, 1.7F, -16.5F, -12.0F, 0, 4, 1, 0.0F, false));
        coils1part4.cubeList.add(new ModelBox(coils1part4, 0, 1, 1.7F, -13.5F, -11.0F, 0, 1, 2, 0.0F, false));

        coils2 = new ModelRenderer(this);
        coils2.setRotationPoint(-8.0F, 16.0F, 8.0F);

        coils2part1 = new ModelRenderer(this);
        coils2part1.setRotationPoint(0.0F, 0.0F, 0.0F);
        coils2.addChild(coils2part1);
        coils2part1.cubeList.add(new ModelBox(coils2part1, 0, 0, 14.0F, -1.5F, -3.0F, 0, 4, 1, 0.0F, false));
        coils2part1.cubeList.add(new ModelBox(coils2part1, 0, 1, 14.0F, 1.5F, -5.0F, 0, 1, 2, 0.0F, false));
        coils2part1.cubeList.add(new ModelBox(coils2part1, 0, 1, 14.0F, -1.5F, -8.0F, 0, 1, 2, 0.0F, false));
        coils2part1.cubeList.add(new ModelBox(coils2part1, 0, 0, 14.0F, -1.5F, -6.0F, 0, 4, 1, 0.0F, false));
        coils2part1.cubeList.add(new ModelBox(coils2part1, 0, 0, 14.0F, -1.5F, -9.0F, 0, 4, 1, 0.0F, false));
        coils2part1.cubeList.add(new ModelBox(coils2part1, 0, 0, 14.0F, -1.5F, -12.0F, 0, 4, 1, 0.0F, false));
        coils2part1.cubeList.add(new ModelBox(coils2part1, 0, 1, 14.0F, 1.5F, -11.0F, 0, 1, 2, 0.0F, false));

        coils2part2 = new ModelRenderer(this);
        coils2part2.setRotationPoint(0.0F, 0.0F, 0.0F);
        coils2.addChild(coils2part2);
        coils2part2.cubeList.add(new ModelBox(coils2part2, 0, 0, 14.0F, -6.5F, -3.0F, 0, 4, 1, 0.0F, false));
        coils2part2.cubeList.add(new ModelBox(coils2part2, 0, 1, 14.0F, -3.5F, -5.0F, 0, 1, 2, 0.0F, false));
        coils2part2.cubeList.add(new ModelBox(coils2part2, 0, 1, 14.0F, -6.5F, -8.0F, 0, 1, 2, 0.0F, false));
        coils2part2.cubeList.add(new ModelBox(coils2part2, 0, 0, 14.0F, -6.5F, -6.0F, 0, 4, 1, 0.0F, false));
        coils2part2.cubeList.add(new ModelBox(coils2part2, 0, 0, 14.0F, -6.5F, -9.0F, 0, 4, 1, 0.0F, false));
        coils2part2.cubeList.add(new ModelBox(coils2part2, 0, 0, 14.0F, -6.5F, -12.0F, 0, 4, 1, 0.0F, false));
        coils2part2.cubeList.add(new ModelBox(coils2part2, 0, 1, 14.0F, -3.5F, -11.0F, 0, 1, 2, 0.0F, false));

        coils2part3 = new ModelRenderer(this);
        coils2part3.setRotationPoint(0.0F, 0.0F, 0.0F);
        coils2.addChild(coils2part3);
        coils2part3.cubeList.add(new ModelBox(coils2part3, 0, 0, 14.0F, -11.5F, -3.0F, 0, 4, 1, 0.0F, false));
        coils2part3.cubeList.add(new ModelBox(coils2part3, 0, 1, 14.0F, -8.5F, -5.0F, 0, 1, 2, 0.0F, false));
        coils2part3.cubeList.add(new ModelBox(coils2part3, 0, 1, 14.0F, -11.5F, -8.0F, 0, 1, 2, 0.0F, false));
        coils2part3.cubeList.add(new ModelBox(coils2part3, 0, 0, 14.0F, -11.5F, -6.0F, 0, 4, 1, 0.0F, false));
        coils2part3.cubeList.add(new ModelBox(coils2part3, 0, 0, 14.0F, -11.5F, -9.0F, 0, 4, 1, 0.0F, false));
        coils2part3.cubeList.add(new ModelBox(coils2part3, 0, 0, 14.0F, -11.5F, -12.0F, 0, 4, 1, 0.0F, false));
        coils2part3.cubeList.add(new ModelBox(coils2part3, 0, 1, 14.0F, -8.5F, -11.0F, 0, 1, 2, 0.0F, false));

        coils2part4 = new ModelRenderer(this);
        coils2part4.setRotationPoint(0.0F, 0.0F, 0.0F);
        coils2.addChild(coils2part4);
        coils2part4.cubeList.add(new ModelBox(coils2part4, 0, 0, 14.0F, -16.5F, -3.0F, 0, 4, 1, 0.0F, false));
        coils2part4.cubeList.add(new ModelBox(coils2part4, 0, 1, 14.0F, -13.5F, -5.0F, 0, 1, 2, 0.0F, false));
        coils2part4.cubeList.add(new ModelBox(coils2part4, 0, 1, 14.0F, -16.5F, -8.0F, 0, 1, 2, 0.0F, false));
        coils2part4.cubeList.add(new ModelBox(coils2part4, 0, 0, 14.0F, -16.5F, -6.0F, 0, 4, 1, 0.0F, false));
        coils2part4.cubeList.add(new ModelBox(coils2part4, 0, 0, 14.0F, -16.5F, -9.0F, 0, 4, 1, 0.0F, false));
        coils2part4.cubeList.add(new ModelBox(coils2part4, 0, 0, 14.0F, -16.5F, -12.0F, 0, 4, 1, 0.0F, false));
        coils2part4.cubeList.add(new ModelBox(coils2part4, 0, 1, 14.0F, -13.5F, -11.0F, 0, 1, 2, 0.0F, false));

        casing = new ModelRenderer(this);
        casing.setRotationPoint(-8.0F, 16.0F, 8.0F);
        casing.cubeList.add(new ModelBox(casing, 0, 6, 2.0F, -19.0F, -13.0F, 12, 1, 12, 0.0F, false));
        casing.cubeList.add(new ModelBox(casing, 0, 6, 2.0F, -18.0F, -13.0F, 1, 22, 12, 0.0F, false));
        casing.cubeList.add(new ModelBox(casing, 0, 6, 13.0F, -18.0F, -13.0F, 1, 22, 12, 0.0F, false));
        casing.cubeList.add(new ModelBox(casing, 0, 6, 2.0F, 4.0F, -13.0F, 12, 1, 12, 0.0F, false));
        casing.cubeList.add(new ModelBox(casing, 0, 0, 2.0F, -18.0F, -2.0F, 12, 23, 1, 0.0F, false));

        door = new ModelRenderer(this);
        door.setRotationPoint(7.0F, 16.0F, -5.0F);
        setRotationAngle(door, 0.0F, 1.5708F, 0.0F);
        door.cubeList.add(new ModelBox(door, 0, 0, 0.0F, -19.0F, -14.5F, 1, 24, 15, 0.0F, false));
        door.cubeList.add(new ModelBox(door, 0, 0, 1.0F, -14.0F, -12.2F, 0, 10, 1, 0.0F, false));

        stand1 = new ModelRenderer(this);
        stand1.setRotationPoint(-8.0F, 16.0F, 8.0F);
        stand1.cubeList.add(new ModelBox(stand1, 0, 5, 9.35F, 0.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 5, 10.4F, 0.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 5, 11.45F, 0.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 5, 12.5F, 0.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 5, 8.25F, 0.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 5, 7.15F, 0.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 0, 3.0F, 0.0F, -12.0F, 10, 0, 1, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 0, 3.0F, 0.0F, -3.0F, 10, 0, 1, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 5, 6.05F, 0.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 5, 5.0F, 0.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 5, 4.0F, 0.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand1.cubeList.add(new ModelBox(stand1, 0, 5, 3.0F, 0.0F, -12.0F, 0, 0, 10, 0.0F, false));

        stand2 = new ModelRenderer(this);
        stand2.setRotationPoint(-8.0F, 16.0F, 8.0F);
        stand2.cubeList.add(new ModelBox(stand2, 0, 5, 9.35F, -4.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 5, 10.4F, -4.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 5, 11.45F, -4.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 5, 12.5F, -4.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 5, 8.25F, -4.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 5, 7.15F, -4.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 0, 3.0F, -4.5F, -12.0F, 10, 0, 1, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 0, 3.0F, -4.5F, -3.0F, 10, 0, 1, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 5, 6.05F, -4.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 5, 5.0F, -4.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 5, 4.0F, -4.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand2.cubeList.add(new ModelBox(stand2, 0, 5, 3.0F, -4.5F, -12.0F, 0, 0, 10, 0.0F, false));

        stand3 = new ModelRenderer(this);
        stand3.setRotationPoint(-8.0F, 16.0F, 8.0F);
        stand3.cubeList.add(new ModelBox(stand3, 0, 5, 9.35F, -9.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 5, 10.4F, -9.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 5, 11.45F, -9.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 5, 12.5F, -9.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 5, 8.25F, -9.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 5, 7.15F, -9.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 0, 3.0F, -9.0F, -12.0F, 10, 0, 1, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 0, 3.0F, -9.0F, -3.0F, 10, 0, 1, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 5, 6.05F, -9.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 5, 5.0F, -9.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 5, 4.0F, -9.0F, -12.0F, 0, 0, 10, 0.0F, false));
        stand3.cubeList.add(new ModelBox(stand3, 0, 5, 3.0F, -9.0F, -12.0F, 0, 0, 10, 0.0F, false));

        stand4 = new ModelRenderer(this);
        stand4.setRotationPoint(-8.0F, 16.0F, 8.0F);
        stand4.cubeList.add(new ModelBox(stand4, 0, 5, 9.35F, -13.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 5, 10.4F, -13.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 5, 11.45F, -13.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 5, 12.5F, -13.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 5, 8.25F, -13.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 5, 7.15F, -13.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 0, 3.0F, -13.5F, -12.0F, 10, 0, 1, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 0, 3.0F, -13.5F, -3.0F, 10, 0, 1, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 5, 6.05F, -13.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 5, 5.0F, -13.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 5, 4.0F, -13.5F, -12.0F, 0, 0, 10, 0.0F, false));
        stand4.cubeList.add(new ModelBox(stand4, 0, 5, 3.0F, -13.5F, -12.0F, 0, 0, 10, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        outside_box.render(f5);
        machine.render(f5);
        coils1.render(f5);
        coils2.render(f5);
        casing.render(f5);

        setRotateAngleInDegrees(door, 0, -open + 90F, 0);
        door.render(f5);

        stand1.render(f5);
        stand2.render(f5);
        stand3.render(f5);
        stand4.render(f5);
    }
    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    private void setRotateAngleInDegrees(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = (float) (x * Math.PI / 180);
        modelRenderer.rotateAngleY = (float) (y * Math.PI / 180);
        modelRenderer.rotateAngleZ = (float) (z * Math.PI / 180);
    }

    public void setOpen(float value)
    {
        open = value;
    }
}
