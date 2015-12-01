package com.shohrab.dependency_injection;

import com.shohrab.utility.SharedPref;
import com.shohrab.model.CarmudiModel;
import com.shohrab.utility.Utility;
import dagger.Component;

/**
 * The connection between the provider of dependencies (@Module) and the classes requesting them through
 * @Inject is made using @Component. This is an interface.
 * Created by shohrab.uddin on 22.11.2015.
 */
@Component (modules = {CarmudiModule.class}) //you can add more modules if needed by using comma
public interface CarmudiComponent {
    CarmudiModel provideCarmudiCars();
    Utility provideUtility();
    SharedPref provideSharedPref();
}
