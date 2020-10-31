package com.example.emptyapp

//import android.R
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.example.emptyapp.R
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*

class MainActivity : AppCompatActivity() {
    private var selectedDate: LocalDate? = null
    private var oldSelectedDate : LocalDate? = null
    private val today = LocalDate.now()

    private var endDate: LocalDate? = null

//    private lateinit var menuItem: MenuItem


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var counter: Int = 0

        val viewCounter: TextView = findViewById(R.id.editTextNumber)
        viewCounter.text = 0.toString()

        val button: Button = findViewById(R.id.increment_button)
        button.setOnClickListener {
            println(counter++)
            viewCounter.text = counter.toString()
        }

        // WHOLE calendar
        val calendarView: CalendarView = findViewById(R.id.calendarView)

        findViewById<Toolbar>(R.id.toolbar).title = "Android Mobile Period Tracker"

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay

            val textView: TextView = view.findViewById(R.id.calendarDayText)

            // constructor
            init {
                textView.setOnClickListener {
                    println("I've been clicked! At date: ${day.date.month}/${day.date.dayOfMonth}")
                    println("selectedDate = ${selectedDate?.dayOfMonth}")
                    if (day.owner == DayOwner.THIS_MONTH) {
                        // if the Clicked on date, is same as this DayViewContainer's date:
                        oldSelectedDate = selectedDate
                        selectedDate = day.date
//                        calendarView.notifyDayChanged(day)
//                        oldSelectedDate?.let { calendarView.notifyDateChanged(oldSelectedDate!!)}
                        calendarView.notifyCalendarChanged()


//                        if (selectedDate == day.date) {
//                            selectedDate = null
//                            calendarView.notifyDayChanged(day)
//                        } else {
//                            println("Does this ever run, is selecteddate!= day.date?")
//                            val oldDate = selectedDate
//                            selectedDate = day.date
//                            calendarView.notifyDateChanged(day.date)
//
//                            oldDate?.let { calendarView.notifyDateChanged(oldDate) }
//                        }

//                        menuItem.isVisible = selectedDate != null
                    }
                }
            }
        }

        /********Below has calendar logic*********/

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




        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View): DayViewContainer = DayViewContainer(view)
            @RequiresApi(Build.VERSION_CODES.M)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                // Rough solution/hack for now
                endDate = selectedDate?.plusDays(3) ?: selectedDate

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.visibility = VISIBLE
                    when {
                        selectedDate == oldSelectedDate && day.date!=today -> {
                            oldSelectedDate =  null
                            selectedDate = null
                            textView.setTextColor(getColor(R.color.black))
                            textView.background = null
                        }
                        day.date == selectedDate -> {
                            textView.setTextColor(getColor(R.color.white))
                            textView.setBackgroundResource(R.drawable.ic_launcher_background)
                        }
                        day.date == today -> {
                            textView.setTextColor(getColor(R.color.red))
                            textView.setBackgroundColor(getColor(R.color.LightGrey))
                        }
                         selectedDate!=null && day.date >= selectedDate && day.date <= endDate -> {
                            textView.setTextColor(getColor(R.color.white))
                            textView.setBackgroundResource(R.drawable.ic_launcher_background)
                        }

                        else -> {
                            textView.setTextColor(getColor(R.color.black))
                            textView.background = null
                        }
                    }


                } else {
                    textView.visibility = View.INVISIBLE
                }
            }
        }
        calendarView.scrollToMonth(currentMonth)
    }



    class MonthViewContainer(view: View) : ViewContainer(view) {
        val textView: TextView = view.findViewById(R.id.calendarHeaderText)
    }



}

