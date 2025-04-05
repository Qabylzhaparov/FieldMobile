import com.example.cccc.MyApp
import com.example.cccc.MainActivity
import com.example.cccc.di.AppModule
import com.example.cccc.vm.CourseViewModel
import com.example.cccc.vm.CourseViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(courseViewModel: CourseViewModel)
    fun inject(myApp: MyApp)

    fun courseViewModelFactory(): CourseViewModelFactory
}
