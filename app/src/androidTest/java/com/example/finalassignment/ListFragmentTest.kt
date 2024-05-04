package com.example.finalassignment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.fragment.findNavController
import com.example.finalassignment.databinding.FragmentListBinding
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ListFragmentTest {
    private lateinit var fragment: ListFragment

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        val scenario = launchFragmentInContainer<ListFragment>()
        scenario.onFragment { fragment ->
            this.fragment = fragment
        }
        every { fragment.findNavController() } returns mockk(relaxed = true)
    }

    @Test
    fun onCreateView() {
        val inflater = mockk<LayoutInflater>()
        val container = mockk<ViewGroup>()
        val savedInstanceState = mockk<Bundle>()
        val binding = mockk<FragmentListBinding>()
        every { FragmentListBinding.inflate(inflater, container, false) } returns binding

        val view = fragment.onCreateView(inflater, container, savedInstanceState)

        assertEquals(binding.root, view)
    }
}