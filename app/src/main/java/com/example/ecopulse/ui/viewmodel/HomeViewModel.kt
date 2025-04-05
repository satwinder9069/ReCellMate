package com.example.ecopulse.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ecopulse.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {
    val cardItems = listOf(
        EcoCard(
            R.drawable.electronic_recycling_art,
            "E-Waste",
            "The Silent Crisis of the Digital Age"
        ),
        EcoCard(
            R.drawable.everything_is_recycleble,
            "The Urgency Now",
            "E-Waste Today, No Tomorrow"
        ),
        EcoCard(
            R.drawable.recycle_today,
            "Future- Proofing Our Planet",
            "Recycle Electronics Secure the Future"
        ),
        EcoCard(
            R.drawable.recycling_concept,
            "Smart Disposal For Smart Devices",
            "Don't Trash Tech, Recycle It Right!"
        ),
        EcoCard(
            R.drawable.earch,
            "The Hidden Toxin",
            "E-Waste: Out of Sight Shouldn't Mean Out of Mind."
        )
    )
    val healthTip = listOf(
        HealthTip("How E-Waste Affects Your Health", "Learn More", R.drawable.poison),
        HealthTip(
            "The Hidden Environmental Cost of E-Waste",
            "Discover",
            R.drawable.tree_planting
        ),
        HealthTip("Why Recycling E-Waste Matters", "See Why", R.drawable.recycling_earth),
        HealthTip("Your Guide to Responsible E-Waste Recycling", "Learn How", R.drawable.reading),
    )

    val topicDetails = listOf(
        TopicDetails(
            title = "How E-Waste Affects Your Health",
            subtitle = "The hidden dangers in your old devices!",
            description = "E-waste contains toxic chemicals that can severely harm human health, especially when improperly disposed of" +
                    " or recycled.",
            effects = listOf(
                "Burning e-waste releases toxic fumes(dioxins) causing asthma, lung damage and chronic bronchitis",
                "Chemical leaks contaminate water and soil",
                "Children near e-waste sites face developmental issues",
                "Direct contact with e-waste chemicals can cause rashes, burns and dermatitis."
            ),
            preventionTips = listOf(
                "Always dispose of electronics at certified e-waste recycling centers.",
                "Avoid Burning E-Waste - Prevents toxic smoke inhalation",
                "If handling e-waste, wear gloves, masks and goggles.",
                "Extend device life by repairing or donating functional electronics.",
                "Choose brands with take-back programs and non toxic materials ",
            ),
            imageRes = listOf(
                R.drawable.imgres_1,
                R.drawable.burning_3,
                R.drawable.burning_1,
                R.drawable.imgres2,

                )
        ),
        TopicDetails(
            title = "The Hidden Environmental Cost of E-Waste",
            subtitle = "Silent destruction of ecosystems",
            description = "Improper e-waste disposal causes long-term environmental damage.",
            effects = listOf(
                "Heavy metals (lead, cadmium) from buried electronics seep into farmland, contaminating crops for decades." +
                        " Just 1 CRT monitor can pollute 30,000 liters of water with lead.",
                "1 ton of e-waste contaminates 50,000 liters of groundwater",
                "Burning e-waste releases:  \n" +
                        "     ✖ Dioxins (cancer-causing)  \n" +
                        "     ✖ Mercury vapor (brain damage)  \n" +
                        "     ✖ Black carbon (accelerates climate change)  ",
                "Soil remains toxic for decades after contamination",
                "Microplastics enter marine food chains"
            ),
            preventionTips = listOf(
                "Support extended producer responsibility programs",
                " Boycott planned obsolescence - Keep phones 3+ years  \n" +
                        "✓ Buy refurbished tech - Saves 80% CO2 vs new  ",
                "Advocate for stricter dumping regulations"
            ),
            imageRes = listOf(
                R.drawable.imgres4,
                R.drawable.imageres3,
            )
        ),
        TopicDetails(
            title = "Why Recycling E-Waste Matters",
            subtitle = "Turning trash into treasure",
            description = "Proper recycling recovers valuable materials while reducing harm..",
            effects = listOf(
                "1 million phones can yield 35,000 lbs of copper",
                "Recycling saves 80% energy vs. mining new materials",
                "Prevents 70% of toxic landfill leakage"
            ),
            preventionTips = listOf(
                "Locate certified recycling centers near you",
                "Remove batteries before disposal",
                "Wipe data but don't destroy components"
            ),
            imageRes = listOf(
                R.drawable.imgres5,
                R.drawable.imgres10

            )
        ),
        TopicDetails(
            title = "Your Guide to Responsible E-Waste Recycling",
            subtitle = "Step-by-step sustainability",
            description = "Follow these steps to ensure your e-waste gets recycled properly...",
            effects = listOf(
                "95% of device materials can be reused",
                "Proper recycling creates green jobs",
                "Reduces demand for conflict minerals"
            ),
            preventionTips = listOf(
                "1. Backup and factory reset devices",
                "2. Remove personal accessories",
                "3. Find certified drop-off locations",
                "4. Get receipt for proper documentation"
            ),
            imageRes = listOf(
                R.drawable.imgres9,
                R.drawable.preview,

                )
        ),
    )

    fun getTopicDetail(title: String): TopicDetails? {
        return topicDetails.find { it.title == title }
    }

    /*
        private val _selectedTip = MutableStateFlow<HealthTip?>(null)
        val selectedTip: StateFlow<HealthTip?> = _selectedTip
        fun selectTip(tip: HealthTip) {
            _selectedTip.value = tip
        }
    */

}
