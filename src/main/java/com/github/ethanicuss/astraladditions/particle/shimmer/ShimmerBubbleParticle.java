package com.github.ethanicuss.astraladditions.particle.shimmer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

@Environment(EnvType.CLIENT)
public class ShimmerBubbleParticle extends SpriteBillboardParticle {

	public ShimmerBubbleParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
		super(clientWorld, d, e, f);
		this.setBoundingBoxSpacing(0.02F, 0.02F);
		this.scale *= this.random.nextFloat() * 0.6F + 0.2F;
		this.velocityX = g *0.2F + (Math.random() *2.0F -1.0F) *0.02F;
		this.velocityY = h *0.2F + (Math.random() *2.0F -1.0F) *0.02F;
		this.velocityZ = i *0.2F + (Math.random() *2.0F -1.0F) *0.02F;
		this.maxAge = (int) (8.0F / (Math.random() * 0.8 + 0.2));


	}

	public void tick() {
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		if (this.maxAge-- <= 0) {
			this.markDead();
		} else {
			this.velocityY += 0.002;
			this.move(this.velocityX, this.velocityY, this.velocityZ);
			this.velocityX *= 0.85F;
			this.velocityY *= 0.85F;
			this.velocityZ *= 0.85F;

		}
	}

	@Override
	public int getBrightness(float tint) {
		return 240;
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			ShimmerBubbleParticle shimmerBubbleParticle = new ShimmerBubbleParticle(clientWorld, d, e, f, g, h, i);
			shimmerBubbleParticle.setSprite(this.spriteProvider);
			return shimmerBubbleParticle;
		}
	}


}