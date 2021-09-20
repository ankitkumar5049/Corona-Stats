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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.coronastats.R
import com.example.coronastats.adapter.StateBedAdapter
import com.example.coronastats.model.BedRegional
import com.example.coronastats.model.BedSummary

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HosAndBedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HosAndBedFragment : Fragment() {

    lateinit var rHos : TextView
    lateinit var rBed : TextView
    lateinit var uHos : TextView
    lateinit var uBed : TextView
    lateinit var totHos : TextView
    lateinit var totBed : TextView

    lateinit var recyclerDashboard : RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var recyclerAdapter: StateBedAdapter



    val regBedList = arrayListOf<BedRegional>()
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
        val view = inflater.inflate(R.layout.fragment_hos_and_bed, container, false)

        recyclerDashboard = view.findViewById(R.id.RecyclerDashboard)

        layoutManager = LinearLayoutManager(activity)

        /*rHos = view.findViewById(R.id.txtRHos)
        uHos = view.findViewById(R.id.txtUHos)

        rBed = view.findViewById(R.id.txtRBed)
        uBed = view.findViewById(R.id.txtUBed)*/
        totHos = view.findViewById(R.id.txtTotHos)
        totBed = view.findViewById(R.id.txtTotBed)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "https://api.rootnet.in/covid19-in/hospitals/beds"
        val jsonObjectRequest = object : JsonObjectRequest(
            Method.GET,url,null,Response.Listener {
                val success = it.getBoolean("success")

                if(success){
                    val data = it.getJSONObject("data")
                    val summary = data.getJSONObject("summary")
                    val bedSummaryObject = BedSummary(
                        summary.getInt("ruralHospitals"),
                        summary.getInt("ruralBeds"),
                        summary.getInt("urbanHospitals"),
                        summary.getInt("urbanBeds"),
                        summary.getInt("totalHospitals"),
                        summary.getInt("totalBeds")
                    )
                    /*rHos.text = "Rural Hospitals: " +bedSummaryObject.ruralHos.toString()
                    uHos.text = "Urban Hospitals: " +bedSummaryObject.urbanHos.toString()

                    rBed.text = "Rural Beds: "+bedSummaryObject.ruralBed.toString()
                    uBed.text = "Urban Beds: "+bedSummaryObject.urbanBed.toString()
                    totBed.text = "Total Beds: "+bedSummaryObject.totalBed.toString()*/
                    totHos.text = "Total Hospital: "+bedSummaryObject.totalHos.toString()


                    val regionalArray = data.getJSONArray("regional")
                    for(i in 0 until regionalArray.length()){
                        val regObject = regionalArray.getJSONObject(i)
                        val bedRegObject = BedRegional(
                            regObject.getString("state"),
                            regObject.getInt("ruralHospitals"),
                            regObject.getInt("ruralBeds"),
                            regObject.getInt("urbanHospitals"),
                            regObject.getInt("urbanBeds"),
                            regObject.getInt("totalHospitals"),
                            regObject.getInt("totalBeds")

                        )

                        regBedList.add(bedRegObject)

                        recyclerAdapter = StateBedAdapter(activity as Context,regBedList)

                        recyclerDashboard.adapter = recyclerAdapter

                        recyclerDashboard.layoutManager = layoutManager
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
        return view;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HosAndBedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HosAndBedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}