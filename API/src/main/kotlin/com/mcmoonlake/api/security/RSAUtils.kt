/*
 * Copyright (C) 2016-Present The MoonLake (mcmoonlake@hotmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mcmoonlake.api.security

import com.mcmoonlake.api.throwGiven
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * * 2048-bit RSA and SHA512 mode signature tools.
 *
 * @author lgou2w
 * @since 2.0
 */
object RSAUtils {

    private const val BIT = 2048
    private const val RSA = "RSA"
    private const val EMPTY = ""
    private const val NEWLINE = "\n"
    private const val SIGNATURE = "SHA512withRSA"
    private const val DECRYPT_BLOCK_SIZE = BIT / 8
    private const val ENCRYPT_BLOCK_SIZE = BIT / 8 - 11
    private const val PRIVATE_KEY_HEADER = "-----BEGIN PRIVATE KEY-----"
    private const val PRIVATE_KEY_ENDER = "-----END PRIVATE KEY-----"
    private const val PUBLIC_KEY_HEADER = "-----BEGIN PUBLIC KEY-----"
    private const val PUBLIC_KEY_ENDER = "-----END PUBLIC KEY-----"

    @JvmStatic
    @JvmName("generateKeyPair")
    @Throws(IOException::class)
    fun generateKeyPair(): KeyPair = try {
        val keyPairGenerator = KeyPairGenerator.getInstance(RSA)
        keyPairGenerator.initialize(BIT, SecureRandom())
        keyPairGenerator.genKeyPair()
    } catch(e: Exception) {
        throw IOException("Unable to generate key pair.", e)
    }

    @JvmStatic
    @JvmName("decodePublicKey")
    @Throws(IOException::class)
    fun decodePublicKey(encodedKey: ByteArray): PublicKey = try {
        KeyFactory.getInstance(RSA).generatePublic(X509EncodedKeySpec(encodedKey))
    } catch(e: GeneralSecurityException) {
        throw IOException("Unable to decrypt the public key.", e)
    }

    @JvmStatic
    @JvmName("decodePublicKey")
    @Throws(IOException::class)
    fun decodePublicKey(encodedKey: String): PublicKey = try {
        val value = encodedKey.replace(NEWLINE, EMPTY)
                .replace(PUBLIC_KEY_HEADER, EMPTY)
                .replace(PUBLIC_KEY_ENDER, EMPTY)
        decodePublicKey(Base64.getDecoder().decode(value))
    } catch(e: Exception) {
        e.throwGiven { IOException("Unable to decrypt the public key.", e.cause ?: e) }
    }

    @JvmStatic
    @JvmName("decodePrivateKey")
    @Throws(IOException::class)
    fun decodePrivateKey(encodedKey: ByteArray): PrivateKey = try {
        KeyFactory.getInstance(RSA).generatePrivate(PKCS8EncodedKeySpec(encodedKey))
    } catch(e: GeneralSecurityException) {
        throw IOException("Unable to decrypt the private key.", e)
    }

    @JvmStatic
    @JvmName("decodePrivateKey")
    @Throws(IOException::class)
    fun decodePrivateKey(encodedKey: String): PrivateKey = try {
        val value = encodedKey.replace(NEWLINE, EMPTY)
                .replace(PRIVATE_KEY_HEADER, EMPTY)
                .replace(PRIVATE_KEY_ENDER, EMPTY)
        decodePrivateKey(Base64.getDecoder().decode(value))
    } catch(e: Exception) {
        e.throwGiven { IOException("Unable to decrypt the private key.", e.cause ?: e) }
    }

    @JvmStatic
    @JvmName("signature")
    @Throws(IOException::class)
    fun signature(privateKey: PrivateKey, data: ByteArray): ByteArray = try {
        val signature = Signature.getInstance(SIGNATURE)
        signature.initSign(privateKey)
        signature.update(data)
        signature.sign()
    } catch(e: Exception) {
        throw IOException("Unable to signature the data.", e)
    }

    @JvmStatic
    @JvmName("signature")
    @Throws(IOException::class)
    fun signature(privateKey: PrivateKey, file: File): ByteArray = try {
        signature(privateKey, file.readBytes())
    } catch(e: Exception) {
        e.throwGiven { IOException("Unable to signature the data.", e.cause ?: e) }
    }

    @JvmStatic
    @JvmName("verify")
    fun verify(publicKey: PublicKey, data: ByteArray, sign: ByteArray): Boolean = try {
        val signature = Signature.getInstance(SIGNATURE)
        signature.initVerify(publicKey)
        signature.update(data)
        signature.verify(sign)
    } catch(e: Exception) {
        false
    }

    @JvmStatic
    @JvmName("verify")
    fun verify(publicKey: PublicKey, file: File, signFile: File): Boolean = try {
        verify(publicKey, file.readBytes(), signFile.readBytes())
    } catch(e: Exception) {
        false
    }

    @JvmStatic
    @JvmName("encrypt")
    @Throws(IOException::class)
    fun encrypt(key: Key, data: ByteArray): ByteArray
            = runEncryption(Cipher.ENCRYPT_MODE, key, data)

    @JvmStatic
    @JvmName("decrypt")
    @Throws(IOException::class)
    fun decrypt(key: Key, data: ByteArray): ByteArray
            = runEncryption(Cipher.DECRYPT_MODE, key, data)

    @JvmStatic
    @JvmName("runEncryption")
    @Throws(IOException::class, UnsupportedOperationException::class)
    private fun runEncryption(mode: Int, key: Key, data: ByteArray): ByteArray = try {
        val cipher = Cipher.getInstance(RSA)
        cipher.init(mode, key)
        val maxBlockSize = when(mode) {
            Cipher.ENCRYPT_MODE -> ENCRYPT_BLOCK_SIZE
            Cipher.DECRYPT_MODE -> DECRYPT_BLOCK_SIZE
            else -> throw UnsupportedOperationException("Unsupported encryption mode: $mode.")
        }
        val dataLength = data.size
        val output = ByteArrayOutputStream()
        var cache: ByteArray
        var offset = 0
        var index = 0
        while(dataLength - offset > 0) {
            cache = if(dataLength - offset > maxBlockSize) cipher.doFinal(data, offset, maxBlockSize)
            else cipher.doFinal(data, offset, dataLength - offset)
            output.write(cache)
            offset = ++index * maxBlockSize
        }
        val result = output.toByteArray()
        output.close()
        result
    } catch(e: GeneralSecurityException) {
        throw IOException("Unable to run encryption.", e)
    }
}
