package de.fu_berlin.cdv.chasingpictures.security;

/*
Copyright (C) 2012 Sveinung Kval Bakken, sveinung.bakken@gmail.com

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import de.fu_berlin.cdv.chasingpictures.R;


public class SecurePreferences {

    private static final String TAG = "SecurePreferences";

    public static class SecurePreferencesException extends RuntimeException {

		public SecurePreferencesException(Throwable e) {
			super(e);
		}

	}

	public static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final String KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	private static final String SECRET_KEY_HASH_TRANSFORMATION = "SHA-256";
	private static final String CHARSET = "UTF-8";

	private final boolean encryptKeys;
	private final Cipher writer;
	private final Cipher reader;
	private final Cipher keyWriter;
	private final SharedPreferences preferences;


    /**
     * Returns an instance with the key and IV stored in the resources of this app.
     * @param context Application context
     * @param preferenceNameId Resource ID for preference name.
     */
    public static SecurePreferences getInstanceFromResources(Context context, int preferenceNameId) {
        SecretKey key = base64DecodeKey(context.getString(R.string.security_KEY));
        IvParameterSpec iv = new IvParameterSpec(Base64.decode(context.getString(R.string.security_IV), Base64.DEFAULT));
        return new SecurePreferences(context, context.getString(preferenceNameId), key, iv, true);
    }

    /**
     * Returns an instance with the key and IV stored in the resources of this app.
     * @param context Application context
     * @param preferenceName Name of preferences to use.
     */
    public static SecurePreferences getInstanceFromResources(Context context, String preferenceName) {
        SecretKey key = base64DecodeKey(context.getString(R.string.security_KEY));
        IvParameterSpec iv = new IvParameterSpec(Base64.decode(context.getString(R.string.security_IV), Base64.DEFAULT));
        return new SecurePreferences(context, preferenceName, key, iv, true);
    }

    /**
     * ACCESS TO THE KEYSTORE CURRENTLY DOES NOT WORK!
     *
     * Returns an instance with the stored key and IV for this app.
     * If these do not exist, they are generated the first time the preferences are used.
     * @param context Application context
     * @param preferenceNameId Resource ID for preference name.
     */
    @Deprecated
    public static SecurePreferences getInstance(Context context, int preferenceNameId) {
        return SecurePreferences.getInstance(context, context.getString(preferenceNameId));
    }

    /**
     * ACCESS TO THE KEYSTORE CURRENTLY DOES NOT WORK!
     *
     * Returns an instance with the stored key and IV for this app.
     * If these do not exist, they are generated the first time the preferences are used.
     * @param context Application context
     * @param preferenceName Name of preferences to use.
     */
    @Deprecated
    public static SecurePreferences getInstance(Context context, String preferenceName) {
        KeyStore keyStore = KeyStore.getInstance();
        byte[] keyBytes, ivBytes;
        SecretKey key;
        IvParameterSpec iv;
        String secretKeyKey = context.getString(R.string.security_secretKey_storeKey);
        final String ivKey = context.getString(R.string.security_IV_storeKey);

        keyBytes = keyStore.get(secretKeyKey);
        if (keyBytes != null) {
            key = new SecretKeySpec(keyBytes, "AES");
        } else {
            Log.d(TAG, "No secret key in keystore, generating new key.");
            KeyGenerator aes = null;
            try {
                aes = KeyGenerator.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            key = aes.generateKey();
            keyStore.put(secretKeyKey, key.getEncoded());
        }


        ivBytes = keyStore.get(ivKey);
        if (ivBytes != null) {
            iv = new IvParameterSpec(ivBytes);
        }
        else {
            Log.d(TAG, "No IV in keystore, generating new IV.");
            iv = SecurePreferences.getIv();
            keyStore.put(ivKey, iv.getIV());
        }

        return new SecurePreferences(context, preferenceName, key, iv, true);
    }

	/**
	 * This will initialize an instance of the SecurePreferences class
	 * @param context your current context.
	 * @param preferenceName name of preferences file (preferenceName.xml)
	 * @param secureKey the key used for encryption, finding a good key scheme is hard.
	 * Hardcoding your key in the application is bad, but better than plaintext preferences. Having the user enter the key upon application launch is a safe(r) alternative, but annoying to the user.
	 * @param encryptKeys settings this to false will only encrypt the values,
	 * true will encrypt both values and keys. Keys can contain a lot of information about
	 * the plaintext value of the value which can be used to decipher the value.
	 * @throws SecurePreferencesException
	 */
    public SecurePreferences(Context context, String preferenceName, SecretKey secureKey, IvParameterSpec ivSpec, boolean encryptKeys) throws SecurePreferencesException {
        try {
            this.writer = Cipher.getInstance(TRANSFORMATION);
            this.reader = Cipher.getInstance(TRANSFORMATION);
            this.keyWriter = Cipher.getInstance(KEY_TRANSFORMATION);

            initCiphers(secureKey, ivSpec);

            this.preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);

            this.encryptKeys = encryptKeys;
        }
        catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new SecurePreferencesException(e);
        }
    }

	protected void initCiphers(SecretKey secretKey, IvParameterSpec ivSpec) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		writer.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		reader.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
		keyWriter.init(Cipher.ENCRYPT_MODE, secretKey);
	}

	public static IvParameterSpec getIv() {
        byte[] iv;
        try {
            iv = new byte[Cipher.getInstance(TRANSFORMATION).getBlockSize()];
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new SecurePreferencesException(e);
        }
        new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}

    protected static SecretKeySpec base64DecodeKey(String key) {
        byte[] keyBytes = Base64.decode(key, Base64.DEFAULT);
        return new SecretKeySpec(keyBytes, TRANSFORMATION);
    }
	
	protected static SecretKeySpec getSecretKey(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		byte[] keyBytes = createKeyBytes(key);
		return new SecretKeySpec(keyBytes, TRANSFORMATION);
	}

	protected static byte[] createKeyBytes(String key) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(SECRET_KEY_HASH_TRANSFORMATION);
		md.reset();
        return md.digest(key.getBytes(CHARSET));
	}

	public void put(String key, String value) {
		if (value == null) {
			remove(key);
		}
		else {
			putValue(toKey(key), value);
		}
	}

	public boolean containsKey(String key) {
		return preferences.contains(toKey(key));
	}

	public void remove(String key) {
		preferences.edit().remove(toKey(key)).apply();
	}

	public String get(String key) throws SecurePreferencesException {
		if (preferences.contains(toKey(key))) {
			String securedEncodedValue = preferences.getString(toKey(key), "");
			return decrypt(securedEncodedValue);
		}
		return null;
	}

	public void clear() {
		preferences.edit().clear().apply();
	}

	private String toKey(String key) {
		if (encryptKeys)
			return encrypt(key, keyWriter);
		else return key;
	}

	private void putValue(String key, String value) throws SecurePreferencesException {
		String secureValueEncoded = encrypt(value, writer);

		preferences.edit().putString(key, secureValueEncoded).apply();
	}

	protected String encrypt(String value, Cipher writer) throws SecurePreferencesException {
		byte[] secureValue;
		try {
			secureValue = convert(writer, value.getBytes(CHARSET));
		}
		catch (UnsupportedEncodingException e) {
			throw new SecurePreferencesException(e);
		}
        return Base64.encodeToString(secureValue, Base64.NO_WRAP);
	}

	protected String decrypt(String securedEncodedValue) {
		byte[] securedValue = Base64.decode(securedEncodedValue, Base64.NO_WRAP);
		byte[] value = convert(reader, securedValue);
		try {
			return new String(value, CHARSET);
		}
		catch (UnsupportedEncodingException e) {
			throw new SecurePreferencesException(e);
		}
	}

	private static byte[] convert(Cipher cipher, byte[] bs) throws SecurePreferencesException {
		try {
			return cipher.doFinal(bs);
		}
		catch (Exception e) {
			throw new SecurePreferencesException(e);
		}
	}
}
