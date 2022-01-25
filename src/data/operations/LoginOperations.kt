package eu.bbsapps.forgottenfilmsapi.data.operations

import eu.bbsapps.forgottenfilmsapi.database


object LoginOperations {

    /**
     * Hashes the provided password and then checks if it matches the hashed password that corresponds to the email in database
     * @param email The email that it will check password
     * @param password The password that it will check if matches the hashed password that corresponds to the email in database
     * @return True if the password is correct else false
     */
    suspend fun isPasswordCorrect(email: String, password: String): Boolean {
        return database.checkPasswordForEmail(email, password)
    }
}