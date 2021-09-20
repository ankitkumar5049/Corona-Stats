package com.example.coronastats.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.coronastats.R
import com.example.coronastats.model.CovidStats
import com.example.coronastats.model.Regional
import com.example.coronastats.model.Summary
import com.example.coronastats.model.UnSummary
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {

    lateinit var txtTotalCases: TextView
    lateinit var txtRecoveredCases : TextView
    lateinit var txtDeath : TextView
    lateinit var txtInd : TextView
    lateinit var txtFor : TextView

    val summaryList = arrayListOf<Summary>()
    val unSummaryList = arrayListOf<UnSummary>()
    val regionalList = arrayListOf<Regional>()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        txtTotalCases = view.findViewById(R.id.txtTotalCases)
        txtRecoveredCases = view.findViewById(R.id.txtRecoveredCases)
        txtDeath = view.findViewById(R.id.txtDeaths)
        txtInd = view.findViewById(R.id.txtTotalIndCases)
        txtFor = view.findViewById(R.id.txtTotalForCases)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://api.rootnet.in/covid19-in/stats/latest"
        val jsonObjectRequest = object : JsonObjectRequest(Method.GET,url,null,Response.Listener {
           //Log.i("Data",response.toString())
            val success = it.getBoolean("success")

            if(success){
                val data = it.getJSONObject("data")
                val summary = data.getJSONObject("summary")
                val summaryObject = Summary(
                    summary.getInt("total"),
                    summary.getInt("confirmedCasesIndian"),
                    summary.getInt("confirmedCasesForeign"),
                    summary.getInt("discharged"),
                    summary.getInt("deaths"),
                    summary.getInt("confirmedButLocationUnidentified")
                )
                summaryList.add(summaryObject)
                txtTotalCases.text = "Total Cases: " +summaryObject.total.toString()
                txtInd.text = "Indians: " + summaryObject.confirmedCasesIndian.toString()
                txtFor.text = "Foreigners: " + summaryObject.confirmedCasesForeign.toString()
                txtRecoveredCases.text = "Recovered: " +summaryObject.discharged.toString()
                txtDeath.text = "Deaths: " +summaryObject.deaths.toString()


                val unofficialSummaryArray = data.getJSONArray("unofficial-summary")
                for(i in 0 until unofficialSummaryArray.length()){
                    val usObject = unofficialSummaryArray.getJSONObject(i)
                    val unSummaryObject = UnSummary(
                        usObject.getString("source"),
                        usObject.getInt("total"),
                        usObject.getInt("recovered"),
                        usObject.getInt("deaths"),
                        usObject.getInt("active")
                    )
                    unSummaryList.add(unSummaryObject)
                }

                val regArray = data.getJSONArray("regional")
                for(i in 0 until regArray.length()){
                    val regObject = regArray.getJSONObject(i)
                    val regionalObject = Regional(
                        regObject.getString("loc"),
                        regObject.getInt("confirmedCasesIndian"),
                        regObject.getInt("confirmedCasesForeign"),
                        regObject.getInt("discharged"),
                        regObject.getInt("deaths"),
                        regObject.getInt("totalConfirmed")
                    )
                    regionalList.add(regionalObject)
                }




            }
        },Response.ErrorListener {

            if(activity!=null) {
                Toast.makeText(
                    activity as Context,
                    "Volley error occurred !",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }){

        }
        queue.add(jsonObjectRequest)


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}