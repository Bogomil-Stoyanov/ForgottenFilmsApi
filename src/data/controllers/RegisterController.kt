package eu.bbsapps.forgottenfilmsapi.data.controllers

import eu.bbsapps.forgottenfilmsapi.data.collections.User
import eu.bbsapps.forgottenfilmsapi.database
import eu.bbsapps.forgottenfilmsapi.security.getHashWithSalt

object RegisterController {

    /**
     * Checks if the email is valid
     * @param email The email that it will check whether it is valid.
     * It allows numeric values from 0 to 9.
     * Both uppercase and lowercase letters from a to z are allowed.
     *  Allowed are underscore “_”, hyphen “-” and dot “.”.
     * Dot isn't allowed at the start and end of the local-part.
     * Consecutive dots aren't allowed.
     * For the local part, a maximum of 64 characters are allowed.
     * @return Returns true is the email is valid else false
     */
    fun isEmailValid(email: String): Boolean {
        val emailPattern =
            ("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$").toRegex()
        return emailPattern.matches(email)
    }

    /**
     * Checks if email exists in database
     * @param email The email that will be checked if is in database
     * @return Returns true if the email is already in the database else false
     */
    suspend fun userExists(email: String): Boolean {
        return database.checkIfUserExists(email)
    }

    /**
     * Checks if password is valid
     * @param password The password that it will check whether it is valid.
     * Password has to contain and least one uppercase letter (A-Z), at least one lowercase letter (a-z),
     * at least one special character (@#$%^&+=_) and has to be longer than 8 characters
     * @return Returns true is the password is valid else false
     */
    fun isPasswordValid(password: String): Boolean {
        val passwordContainsUpperCaseLetter = password.contains("([A-Z])".toRegex())
        val passwordContainsLowerCaseLetter = password.contains("([a-z])".toRegex())
        val passwordContainsSpecialChar = password.contains("([@#\$%^&+=_])".toRegex())
        val passwordIsLongerThan8Digits = password.length >= 8
        return passwordContainsUpperCaseLetter
                && passwordContainsLowerCaseLetter
                && passwordContainsSpecialChar
                && passwordIsLongerThan8Digits
    }

    /**
     * Checks if nickname is valid
     * @param nickname The nickname that it will check.
     * A given nickname is valid if it is not blank
     * @return Returns true is the nickname is valid
     */
    fun isNicknameValid(nickname: String): Boolean {
        return nickname.isNotBlank()
    }

    /**
     * Hashes the password
     * @param password The password that it will hash
     * @return The hashed password as String
     */
    fun hashPassword(password: String): String {
        return getHashWithSalt(password)
    }


    /**
     * Registers the user
     * @param user The user that will be inserted into the databse
     * @return True if the insertion was successful else false
     */
    suspend fun registerUser(user: User): Boolean {
        return database.registerUser(user)
    }

}