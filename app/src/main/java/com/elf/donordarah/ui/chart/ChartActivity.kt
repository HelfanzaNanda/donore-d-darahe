package com.elf.donordarah.ui.chart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.elf.donordarah.R
import com.elf.donordarah.models.Chart
import com.elf.donordarah.utils.Constants
import com.elf.donordarah.utils.ext.gone
import com.elf.donordarah.utils.ext.toast
import com.elf.donordarah.utils.ext.visible
import kotlinx.android.synthetic.main.activity_chart.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChartActivity : AppCompatActivity() {

    private val chartViewModel : ChartViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)
        observe()
    }

    private fun observe() {
        observeState()
        observeChart()
    }

    private fun observeState() = chartViewModel.listenToState().observe(this, Observer { handleUiState(it) })
    private fun observeChart() = chartViewModel.listenToCharts().observe(this, Observer { handleChart(it) })

    private fun handleChart(list: List<Chart>?) {
        list?.let {
            val cartesian = AnyChart.column()
            val data : MutableList<DataEntry> = mutableListOf()
            it.forEach { chart ->
                data.add(ValueDataEntry(chart.month, chart.count))
            }
            val column = cartesian.column(data)
            column.tooltip().apply {
                titleFormat("{}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0.0)
                    .offsetY(5.0)
                    .format("\${%Value}{groupsSeparator: }")
            }
            cartesian.animation(true)
            cartesian.title("Anda sering donor pada bulan")

            cartesian.yScale().minimum(0.0)

            cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)

            cartesian.xAxis(0).title("Bulan")
            cartesian.yAxis(0).title("Total")
            any_chart_view.setChart(cartesian)
        }
    }

    private fun handleUiState(chartState: ChartState?) {
        chartState?.let {
            when(it){
                is ChartState.Loading -> handleLoading(it.state)
                is ChartState.ShowToast -> toast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if(state) progress_bar.visible() else progress_bar.gone()
    }

    private fun fetchChart() = chartViewModel.chart(Constants.getToken(this@ChartActivity))

    override fun onResume() {
        super.onResume()
        fetchChart()
    }
}