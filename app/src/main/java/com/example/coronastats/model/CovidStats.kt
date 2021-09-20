package com.example.coronastats.model

import org.json.JSONArray
import org.json.JSONObject

class CovidStats (
    val summary: ArrayList<Summary>,
    val unofficial_summary: ArrayList<UnSummary>,
    val regional: ArrayList<Regional>
        )