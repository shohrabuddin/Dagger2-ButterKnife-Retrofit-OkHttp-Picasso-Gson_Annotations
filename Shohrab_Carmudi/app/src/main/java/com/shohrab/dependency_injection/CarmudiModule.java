package com.shohrab.dependency_injection;

import com.shohrab.model.CarmudiModel;
import com.shohrab.utility.SharedPref;
import com.shohrab.utility.Utility;
import com.shohrab.model.CarmudiModelOffline;
import dagger.Module;
import dagger.Provides;

/**
 * This class acts as an object provider for the whole project we will need with its dependencies satisfied
 * Created by shohrab.uddin on 22.11.2015.
 */
@Module //@Module is a dagger annotation. Dagger will look for @Module annotated class to supply necessary objects and theirs dependencies
public class CarmudiModule {

    @Provides //@Provides is a dagger annotation. A @Module annotated class normally have @Provides annotated methods which means these methods will provide object
    CarmudiModel provideCarmudiCars()
    {
        return new CarmudiModel();
    }

    @Provides
    Utility provideUtility(){
        return new Utility();
    }

    @Provides
    CarmudiModelOffline provideCarmudiModelOffline(){
        return new CarmudiModelOffline();
    }

    @Provides
    SharedPref provideSharedPref(){
        return new SharedPref();
    }


}
