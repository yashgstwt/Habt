package com.theo.habt.dataLayer.repositorys

sealed interface RepositoryError {

    sealed class RoomErrors :  Throwable() ,RepositoryError  {
        data object NameAlreadyExist : RoomErrors()

        data object HabitNotFound : RoomErrors()
        data object InvalidColorCode: RoomErrors()
        data object Unknown : RoomErrors()
        data class FetchingFailed(val msg :String = "An Unexpected error occurred while fetching" ): RoomErrors()

    }

    //other category errors like api error


}