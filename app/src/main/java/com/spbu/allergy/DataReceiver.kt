package com.spbu.allergy

class DataReceiverFactory(private val userProfileSettings : UserProfile, private val forecastApi: SomeApi, private val updatableScreens : Array<UpdatableFragment>) {
    private var instance: DataReceiver? = null

    fun getInstance() : DataReceiver =
        if (instance == null)
            DataReceiver(userProfileSettings, forecastApi, updatableScreens)
        else
            instance!!

    class DataReceiver(private val userProfileSettings : UserProfile, private val forecastApi : SomeApi, private val updatableScreens : Array<UpdatableFragment>){

        private fun dataRequest(user: UserData): Data = forecastApi.request( user)

        private fun refreshScreens(currentData:Data){
            for (screen in updatableScreens){
                screen.Update(currentData)
            }
        }

        fun start(){
            while (true){
                val currentUserData = userProfileSettings.getInfo()
                val currentData = dataRequest(currentUserData)
                refreshScreens(currentData)
                val threeHours = (1000 * 60 * 60  * 3).toLong()
                Thread.sleep(threeHours)}
        }
    }
}

//Далее идут аглушки для класса
class Data{}
class UserData{}
class UserProfile{
    fun getInfo(): UserData {
        return UserData()
    }
}
class SomeApi() {
    fun request(user : UserData):Data {
        return Data()
    }
}