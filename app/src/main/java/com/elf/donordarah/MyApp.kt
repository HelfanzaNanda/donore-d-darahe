package com.elf.donordarah

import android.app.Application
import com.elf.donordarah.repositories.*
import com.elf.donordarah.ui.add_edit_donor.AddEditDonorViewModel
import com.elf.donordarah.ui.add_edit_submission.AddEditSubmissionViewModel
import com.elf.donordarah.ui.chart.ChartViewModel
import com.elf.donordarah.ui.information.InformationViewModel
import com.elf.donordarah.ui.login.LoginViewModel
import com.elf.donordarah.ui.main.donor.DonorViewModel
import com.elf.donordarah.ui.main.profile.ProfileViewModel
import com.elf.donordarah.ui.main.schedulle.SchedulleViewModel
import com.elf.donordarah.ui.main.stock.StockViewModel
import com.elf.donordarah.ui.news.NewsViewModel
import com.elf.donordarah.ui.register.RegisterViewModel
import com.elf.donordarah.ui.submission.SubmissionViewModel
import com.elf.donordarah.webservices.ApiClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            modules(listOf(repositoryModules, viewModelModules, retrofitModule))
        }
    }
}

val retrofitModule = module {
    single { ApiClient.instance() }
    single { ApiClient.instanceCapil() }
}

val repositoryModules = module {
    factory { InformationRepository(get()) }
    factory { NewsRepository(get()) }
    factory { SchedulleRepository(get()) }
    factory { StockRepository(get()) }
    factory { UserRepository(get()) }
    factory { SubmissionRepository(get()) }
    factory { DonorRepository(get(), get()) }
}

val viewModelModules = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { InformationViewModel(get()) }
    viewModel { NewsViewModel(get()) }
    viewModel { DonorViewModel(get()) }
    viewModel { SchedulleViewModel(get()) }
    viewModel { StockViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { SubmissionViewModel(get()) }
    viewModel { AddEditSubmissionViewModel(get()) }
    viewModel { AddEditDonorViewModel(get()) }
    viewModel { ChartViewModel(get()) }

}