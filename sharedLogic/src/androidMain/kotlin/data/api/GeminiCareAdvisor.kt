package com.example.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiCareAdvisor {

    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun getCareAdvice(userQuery: String, patientAge: String, condition: String): String = withContext(Dispatchers.IO) {
        val apiKey = System.getenv("GEMINI_API_KEY").orEmpty()

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext generateFallbackAdvice(userQuery, patientAge, condition)
        }

        try {
            val systemInstruction = """
                You are a highly empathetic and knowledgeable AI Healthcare & Home Nursing Care Advisor for CareHome services.
                Your job is to analyze the user's patient condition, symptoms, or age group and recommend the appropriate home healthcare service:
                1. General Home Nursing Visit (wound dressing, injections, vital checks)
                2. 24/7 ICU Trained Home Nurse (critical care, tracheostomy, ventilator support)
                3. Home Vaccination Drive (sterile vaccinations at home)
                4. Compassionate Elder Care (senior assistance, mobility, daily routine, companionship)
                5. Post-Surgical Recovery Care (wound management, drain care, post-op mobility)
                6. Home Diagnostic Vitals & ECG (portable ECG, blood sugar, sample collection)
                7. Palliative & Hospice Care (bedridden patient care, pain relief)

                Always provide a structured, polite, medical-grade recommendation with clear steps, safety disclaimer, and suggested nursing package.
            """.trimIndent()

            val promptText = "Patient Age: $patientAge years. Medical Condition: $condition. User Query: $userQuery"

            val jsonBody = JSONObject().apply {
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", systemInstruction)))
                })
                put("contents", JSONArray().put(
                    JSONObject().apply {
                        put("parts", JSONArray().put(JSONObject().put("text", promptText)))
                    }
                ))
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseStr = response.body?.string() ?: ""

            if (response.isSuccessful && responseStr.isNotBlank()) {
                val jsonResponse = JSONObject(responseStr)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "Unable to generate advice.")
                    }
                }
            }

            return@withContext generateFallbackAdvice(userQuery, patientAge, condition)
        } catch (e: Exception) {
            return@withContext generateFallbackAdvice(userQuery, patientAge, condition)
        }
    }

    private fun generateFallbackAdvice(userQuery: String, patientAge: String, condition: String): String {
        val queryLower = userQuery.lowercase() + " " + condition.lowercase()
        val recommendation = when {
            queryLower.contains("elder") || queryLower.contains("senior") || queryLower.contains("old") || (patientAge.toIntOrNull() ?: 0) >= 65 ->
                "**Recommended Service: Compassionate Elder Care (12/24 Hrs Shift)**\n\nFor senior citizens needing daily routine assistance, medication compliance, and mobility support. Our certified caregivers ensure comfort and continuous companionship."
            queryLower.contains("icu") || queryLower.contains("critical") || queryLower.contains("ventilator") || queryLower.contains("pipe") ->
                "**Recommended Service: 24/7 ICU Trained Home Nurse**\n\nFor patients requiring round-the-clock intensive monitoring, tracheostomy, catheter care, or post-ICU step-down support at home."
            queryLower.contains("surgery") || queryLower.contains("operation") || queryLower.contains("wound") || queryLower.contains("dressing") ->
                "**Recommended Service: Post-Surgical Recovery Care & Dressing**\n\nIdeal for sterile wound cleaning, surgical drain management, pain relief, and infection prevention after hospital discharge."
            queryLower.contains("vaccine") || queryLower.contains("fever") || queryLower.contains("flu") ->
                "**Recommended Service: Home Vaccination & Basic Vitals Visit**\n\nSterile, cold-chain doorstep vaccination with 15-min post-shot monitoring."
            else ->
                "**Recommended Service: General Home Nursing Visit**\n\nA qualified nurse will visit your home within 60-90 minutes to assess patient vitals, administer prescribed medications/injections, and provide expert care guidance."
        }

        return """
            🩺 **CareHome AI Health Advisory**
            
            $recommendation
            
            📋 **Key Care Recommendations:**
            • Ensure patient vital parameters (BP, Pulse, Temperature) are logged daily.
            • Keep doctor's prescriptions and medical discharge summary accessible.
            • Schedule a qualified home nurse visit for professional hands-on care.
            
            ⚠️ *Disclaimer: This AI advisory is for informational guidance only and does not substitute for emergency medical care. In case of acute emergency, please contact 112 or visit the nearest hospital.*
        """.trimIndent()
    }
}
