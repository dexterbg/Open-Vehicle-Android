package com.openvehicles.OVMS;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HMAC {
	private static final byte IPAD = 54;
	private static final byte OPAD = 92;
	private static final byte PADLEN = 64;
	MessageDigest digest;
	private byte[] ipad;
	private byte[] opad;

	public HMAC(String paramString, byte[] paramArrayOfByte) {
		try {
			this.digest = MessageDigest.getInstance(paramString);
			init(paramArrayOfByte);
			return;
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
		}
		throw new IllegalArgumentException("unknown digest algorithm "
				+ paramString);
	}

	public HMAC(MessageDigest paramMessageDigest, byte[] paramArrayOfByte) {
		paramMessageDigest.reset();
		this.digest = paramMessageDigest;
		init(paramArrayOfByte);
	}

	private void init(byte[] paramArrayOfByte) {
		if (paramArrayOfByte.length > 64) {
			paramArrayOfByte = this.digest.digest(paramArrayOfByte);
			this.digest.reset();
		}
		this.ipad = new byte[64];
		this.opad = new byte[64];
		int i = 0;
		if (i >= paramArrayOfByte.length)
			;
		while (true) {
			if (i >= 64) {
				this.digest.update(this.ipad);
				return;
				this.ipad[i] = (byte) (0x36 ^ paramArrayOfByte[i]);
				this.opad[i] = (byte) (0x5C ^ paramArrayOfByte[i]);
				i++;
				break;
			}
			this.ipad[i] = 54;
			this.opad[i] = 92;
			i++;
		}
	}

	public void clear() {
		this.digest.reset();
		this.digest.update(this.ipad);
	}

	public byte[] sign() {
		byte[] arrayOfByte = this.digest.digest();
		this.digest.reset();
		this.digest.update(this.opad);
		return this.digest.digest(arrayOfByte);
	}

	public void update(byte[] paramArrayOfByte) {
		this.digest.update(paramArrayOfByte);
	}

	public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
		this.digest.update(paramArrayOfByte, paramInt1, paramInt2);
	}

	public boolean verify(byte[] paramArrayOfByte) {
		return Arrays.equals(paramArrayOfByte, sign());
	}
}