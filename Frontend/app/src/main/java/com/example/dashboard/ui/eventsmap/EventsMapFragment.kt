package com.example.dashboard.ui.eventsmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

class EventsMapFragment : Fragment() {

    private var _binding: Any? = null
    private val binding get() = _binding!!

    private lateinit var eventsMapViewModel: EventsMapViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        eventsMapViewModel = ViewModelProvider(this).get(EventsMapViewModel::class.java)

        // Use reflection to get the binding class and inflate method
        val bindingClass = Class.forName("com.example.dashboard.databinding.FragmentEventsMapBinding")
        val inflateMethod = bindingClass.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        _binding = inflateMethod.invoke(null, inflater, container, false)

        // Get the root view using reflection
        val rootView = bindingClass.getMethod("getRoot").invoke(binding) as View

        // Set up click listener for map marker (if mapView exists)
        try {
//            val mapViewField = bindingClass.getDeclaredField("mapView")
//            val mapView = mapViewField.get(binding)
//            val setOnMarkerClickListenerMethod = mapView.javaClass.getMethod("setOnMarkerClickListener", Function1::class.java)
//            setOnMarkerClickListenerMethod.invoke(mapView) { _: Any? ->
//                val action = EventsMapFragmentDirections.actionEventsMapFragmentToEventDetailsFragment()
//                findNavController().navigate(action)
//                true
//            }
        } catch (e: NoSuchFieldException) {
            // mapView doesn't exist in the layout, skip setting the listener
        }

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class EventsMapViewModel : ViewModel() {
        private val _text = MutableLiveData<String>().apply {
            value = "This is the Events Map Fragment"
        }
        val text: LiveData<String> = _text

        // Add more properties and methods as needed for your EventsMap functionality
    }
}