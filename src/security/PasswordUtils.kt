package eu.bbsapps.forgottenfilmsapi.security

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

/**
 * Implementation of password hashing with SHA256 with salt of length 2
 * @return hashed password
 */
fun getHashWithSalt(stringToHash: String, saltLength: Int = 2): String {
    val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
    val saltAsHex = Hex.encodeHexString(salt)
    val hash = DigestUtils.sha256Hex("$saltAsHex$stringToHash")
    return "$saltAsHex:$hash"
}

/**
 * Checks if a password has the same hash as another
 * @return true if hashes match
 */
fun checkHashForPassword(password: String, hashWithSalt: String): Boolean {
    val hashAndSalt = hashWithSalt.split(":")
    if (hashAndSalt.size != 2) {
        return false
    }
    val salt = hashAndSalt[0]
    val hash = hashAndSalt[1]
    val passwordHash = DigestUtils.sha256Hex("$salt$password")
    return hash == passwordHash
}