package com.spbu.allergy.buisnessLogic

class DataReceiverFactory(private val userProfileSettings : UserProfile, private val forecastApi: SomeApi, private val updatableScreens : Array<UpdatableFragment>) {
    private var instance: DataReceiver? = null

    fun getInstance() : DataReceiver =
        if (instance == null)
            DataReceiver(
                userProfileSettings,
                forecastApi,
                updatableScreens
            )
        else
            instance!!

    class DataReceiver(private val userProfileSettings : UserProfile, private val forecastApi : SomeApi, private val updatableScreens : Array<UpdatableFragment>){

        private fun dataRequest(userProfileData: UserData): Data = forecastApi.request( userProfileData)

        private fun refreshScreens(currentData: Data){
            for (screen in updatableScreens){
                screen.update(currentData)
            }
        }

        public  fun refreshData(){
            val currentUserData = userProfileSettings.getInfo()
            val currentData = dataRequest(currentUserData)
            refreshScreens(currentData)
        }

        fun start(){
            while (true){
                refreshData()
                //Возможное время обновления можно изменить
                val threeHours = (1000 * 60 * 60  * 3).toLong()
                Thread.sleep(threeHours)}
        }
    }
}