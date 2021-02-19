package dev.gerg.minecraft.moonphases;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.client.renderer.Tessellator;

@Mod("fixedmoonphases")
public class Phases {

    private static final Logger LOGGER = LogManager.getLogger();

    public Phases() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Init Fixed Moon Phases mod");
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    private void drawMoon() {
        WorldProvider world = DimensionManager.getProvider(0); // surface world
        long worldtime = world.getWorldTime();

	    //double offangle = (double)(Math.PI); // opposite sun (full moon)
	    double offangle = (((double)(worldtime % 192000.0))*2.0D*(double)(Math.PI)/192000.0D) + (double)(Math.PI)*7.0D/8.0D; // 7pi/8 so full moon lasts from pi-pi/8 to pi+pi/8 (centered on pi)
	    double moony = 100.0D * Math.cos(offangle);
	    double moonz = -100.0D * Math.sin(offangle);
	    double moonx = 30.0D * Math.cos(offangle/12.0D); // 6 week periodicity of incline off the ecliptic

        double moonRadius = 20.0D;
        double yOffset = moonRadius * Math.sin(offangle);
        double zOffset = moonRadius * Math.cos(offangle);

        int moonPhase = world.getMoonPhase(worldtime);
        int var30 = moonPhase % 4;
        int var29 = moonPhase / 4 % 2;
        float var16 = (float)(var30 + 0) / 4.0F;
        float var17 = (float)(var29 + 0) / 2.0F;
        float var18 = (float)(var30 + 1) / 4.0F;
        float var19 = (float)(var29 + 1) / 2.0F;

	    // UV coordinates had to be changed to rotate texture by 90 degrees so that proper side was lit
	    // made y and z coordinates a function of the offangle, which will be a function of time
	    // add +/- 55 units (sun radius = 30, moon radius = 20) in x direction to avoid eclipses
        Tessellator var23 = Tessellator.instance;
        var23.addVertexWithUV(moonx - moonRadius, moony - yOffset, moonz - zOffset, (double)var16, (double)var19);
        var23.addVertexWithUV(moonx + moonRadius, moony - yOffset, moonz - zOffset, (double)var16, (double)var17);
        var23.addVertexWithUV(moonx + moonRadius, moony + yOffset, moonz + zOffset, (double)var18, (double)var17);
        var23.addVertexWithUV(moonx - moonRadius, moony + yOffset, moonz + zOffset, (double)var18, (double)var19);
	    //ORIGINAL LINES (puts moon opposite sun always)
        //var23.addVertexWithUV((double)(-var12), -100.0D, (double)var12, (double)var18, (double)var19);
        //var23.addVertexWithUV((double)var12, -100.0D, (double)var12, (double)var16, (double)var19);
        //var23.addVertexWithUV((double)var12, -100.0D, (double)(-var12), (double)var16, (double)var17);
        //var23.addVertexWithUV((double)(-var12), -100.0D, (double)(-var12), (double)var18, (double)var17);
        var23.draw();

    }

}
