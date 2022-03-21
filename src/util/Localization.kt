package eu.bbsapps.forgottenfilmsapi.util

import java.io.File

class Localization {

    val localizedResponses = loadLocalizedResponses()

    private fun loadLocalizedResponses(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val valuePairs = File("resources/localizationBg.txt").readText()
        val splitPairs = valuePairs.split(";")
        splitPairs.forEach {
            val keyValue = it.split(":")
            map[keyValue[0].trim()] = keyValue[1].trim()
        }
        return map
    }

    fun getLocalisedValue(id:String) :String {
        return localizedResponses[id] ?: ""
    }

}
