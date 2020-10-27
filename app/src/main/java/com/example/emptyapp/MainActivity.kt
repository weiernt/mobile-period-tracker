package com.example.emptyapp

//import android.R
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.example.emptyapp.R
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var counter : Int = 0

        val viewCounter: TextView = findViewById(R.id.editTextNumber)
        viewCounter.text = 0.toString()

        val button: Button = findViewById(R.id.increment_button)
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                println(counter++)
                viewCounter.text = counter.toString()
            }
        })

        findViewById<Toolbar>(R.id.toolbar).title = "WHEN DO YOU BLEED?"




        /********Below has calendar logic*********/


        val calendarView : CalendarView = findViewById(R.id.calendarView)
        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text = day.date.dayOfMonth.toString()
            }


        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                @SuppressLint("SetTextI18n") // Concatenation warning for `setText` call.
                container.textView.text = "${month.yearMonth.month.name.toLowerCase().capitalize()} ${month.year}"
            }
        }
        calendarView.scrollToMonth(currentMonth)

    }
}


class DayViewContainer(view: View) : ViewContainer(view) {
    val textView : TextView = view.findViewById(R.id.calendarDayText)

    // With ViewBinding
    // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText

    // constructor
    init {
        textView.setOnClickListener{
            println("I've been clicked! At date: ${textView.text}")
        }
    }
}

class MonthViewContainer(view: View) : ViewContainer(view) {
    val textView : TextView = view.findViewById(R.id.calendarHeaderText)
}

